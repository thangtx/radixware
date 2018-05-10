
/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server - Server Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.server;

import java.util.*;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server")
@Deprecated
public published class Endpoint.JmsHub.Server  extends org.radixware.ads.Jms.server.Unit.JmsHandler  implements org.radixware.ads.ServiceBus.server.IPipelineNode,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private final static Str OUT = "Out";
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Endpoint.JmsHub.Server_mi.rdxMeta;}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectId-User Property*/


















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectId")
	@Deprecated
	public published  Str getOutbConnectId() {
		return outbConnectId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectId")
	@Deprecated
	public published   void setOutbConnectId(Str val) {
		outbConnectId = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityGuid-User Property*/


















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityGuid")
	@Deprecated
	public published  Str getPipelineEntityGuid() {
		return pipelineEntityGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityGuid")
	@Deprecated
	public published   void setPipelineEntityGuid(Str val) {
		pipelineEntityGuid = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityPid-User Property*/


















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityPid")
	@Deprecated
	public published  Str getPipelineEntityPid() {
		return pipelineEntityPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityPid")
	@Deprecated
	public published   void setPipelineEntityPid(Str val) {
		pipelineEntityPid = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectTitle-Dynamic Property*/



	@Deprecated
	protected Str outbConnectTitle=null;













	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectTitle")
	@Deprecated
	public published  Str getOutbConnectTitle() {

		return ServiceBus::SbServerUtil.getConnectionTitle(outbConnectId);
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectTitle")
	@Deprecated
	public published   void setOutbConnectTitle(Str val) {
		outbConnectTitle = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityTitle-Dynamic Property*/



	@Deprecated
	protected Str pipelineEntityTitle=null;













	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityTitle")
	@Deprecated
	public published  Str getPipelineEntityTitle() {

		if (pipelineEntityGuid == null || pipelineEntityPid == null)
		    return null;
		return ServiceBus::SbServerUtil.getEntityTitle(
		    new Pid(Arte, Types::Id.Factory.loadFrom(pipelineEntityGuid), pipelineEntityPid)
		    );
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityTitle")
	@Deprecated
	public published   void setPipelineEntityTitle(Str val) {
		pipelineEntityTitle = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceLevel-Dynamic Property*/



	@Deprecated
	protected Int traceLevel=null;













	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceLevel")
	@Deprecated
	private final  Int getTraceLevel() {
		return traceLevel;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceLevel")
	@Deprecated
	private final   void setTraceLevel(Int val) {
		traceLevel = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:isClient-Detail Column Property*/




	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRqData-Dynamic Property*/



	@Deprecated
	protected org.apache.xmlbeans.XmlObject outRqData=null;













	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRqData")
	@Deprecated
	protected published  org.apache.xmlbeans.XmlObject getOutRqData() {
		return outRqData;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRqData")
	@Deprecated
	protected published   void setOutRqData(org.apache.xmlbeans.XmlObject val) {
		outRqData = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRsData-Dynamic Property*/



	@Deprecated
	protected org.apache.xmlbeans.XmlObject outRsData=null;













	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRsData")
	@Deprecated
	protected published  org.apache.xmlbeans.XmlObject getOutRsData() {
		return outRsData;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRsData")
	@Deprecated
	protected published   void setOutRsData(org.apache.xmlbeans.XmlObject val) {
		outRsData = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:extGuid-User Property*/


















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:extGuid")
	@Deprecated
	public published  Str getExtGuid() {
		return extGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:extGuid")
	@Deprecated
	public published   void setExtGuid(Str val) {
		extGuid = val;
	}

























































































	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:checkTraceLevel-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:checkTraceLevel")
	@Deprecated
	public published  boolean checkTraceLevel (org.radixware.kernel.common.enums.EEventSeverity severity) {
		if (traceLevel == null)
		    traceLevel = Arte::Trace.getMinSeverity(Arte::EventSource:ServiceBus);

		if (severity == Arte::EventSeverity:Debug)
		    return traceLevel.intValue() == Arte::EventSeverity:Debug.Value.intValue();

		return traceLevel.intValue() <= severity.Value.intValue();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:trace-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:trace")
	@Deprecated
	public published  void trace (org.radixware.kernel.common.enums.EEventSeverity severity, Str mess, boolean sensitive) {
		if (!checkTraceLevel(severity))
		    return;

		Arte::Trace.enterContext(Arte::EventContextType:SystemUnit, id);
		Object traceTargetHandler = Arte::Trace.addContextProfile(getPipeline().getTraceProfile());
		try {
		    if (sensitive/* && .intValue() != .Value.intValue()*/)
		        Arte::Trace.putSensitive(severity, mess, Arte::EventSource:ServiceBus);
		    else    
		        Arte::Trace.put(severity, mess, Arte::EventSource:ServiceBus);
		} finally {
		    Arte::Trace.leaveContext(Arte::EventContextType:SystemUnit, id);
		    Arte::Trace.delContextProfile(traceTargetHandler);
		}
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceDebug-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceDebug")
	@Deprecated
	public published  void traceDebug (Str mess, boolean sensitive) {
		trace(Arte::EventSeverity:Debug, mess, sensitive);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceError-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceError")
	@Deprecated
	public published  void traceError (Str mess) {
		trace(Arte::EventSeverity:Error, mess, false);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceEvent-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceEvent")
	@Deprecated
	public published  void traceEvent (Str mess) {
		trace(Arte::EventSeverity:Event, mess, false);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceWarning-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceWarning")
	@Deprecated
	public published  void traceWarning (Str mess) {
		trace(Arte::EventSeverity:Warning, mess, false);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:afterResponseDelivered-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:afterResponseDelivered")
	@Deprecated
	public published  void afterResponseDelivered (org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRs rs) throws org.radixware.ads.ServiceBus.server.PipelineException {

	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:checkConfig-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:checkConfig")
	@Deprecated
	public published  void checkConfig () throws org.radixware.ads.ServiceBus.server.PipelineConfigException {
		ServiceBus::SbServerUtil.checkConnection(this, OUT, outbConnectId);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:connect-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:connect")
	@Deprecated
	public published  void connect (Str outConnectorRole, Str inConnectorId) {
		if (outConnectorRole == OUT) {
		    outbConnectId = inConnectorId;
		} else    
		    throw new AppError("Unknown role " + outConnectorRole);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getConnectorIconId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getConnectorIconId")
	@Deprecated
	public published  org.radixware.kernel.common.types.Id getConnectorIconId (Str role) {
		if (role == OUT)
		    return idof[ServiceBus::next];
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getConnectorId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getConnectorId")
	@Deprecated
	public published  Str getConnectorId (Str role) {
		if (role == OUT)
		    return outbConnectId;
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getConnectorRole-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getConnectorRole")
	@Deprecated
	public published  Str getConnectorRole (Str id) {
		if (id == outbConnectId)
		    return OUT;
		throw new AppError("Unknown id " + id);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getConnectorRqDataType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getConnectorRqDataType")
	@Deprecated
	public published  Str getConnectorRqDataType (Str role) {
		if (role == OUT)
		    return BINARY_TYPE;
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getConnectorRsDataType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getConnectorRsDataType")
	@Deprecated
	public published  Str getConnectorRsDataType (Str role) {
		if (role == OUT)
		    return BINARY_TYPE;
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getConnectorSide-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getConnectorSide")
	@Deprecated
	public published  org.radixware.ads.ServiceBus.common.Side getConnectorSide (Str role) {
		if (role == OUT)
		    return ServiceBus::Side:Right;
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getConnectorTitle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getConnectorTitle")
	@Deprecated
	public published  Str getConnectorTitle (Str role) {
		if (role == OUT)
		    return "Inbound requests";
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getCreatingPresentationId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getCreatingPresentationId")
	@Deprecated
	public published  org.radixware.kernel.common.types.Id getCreatingPresentationId () {
		return idof[Endpoint.JmsHub.Server:Create];
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getDescription-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getDescription")
	@Deprecated
	public published  Str getDescription () {
		return title;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getHolderEntityPid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getHolderEntityPid")
	@Deprecated
	public published  org.radixware.kernel.server.types.Pid getHolderEntityPid () {
		return getPid();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getEditingPresentationId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getEditingPresentationId")
	@Deprecated
	public published  org.radixware.kernel.common.types.Id getEditingPresentationId () {
		return idof[Endpoint.JmsHub.Server:Edit];
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getIconId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getIconId")
	@Deprecated
	public published  org.radixware.kernel.common.types.Id getIconId () {
		return idof[JMLServer];
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getInConnectorRoles-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getInConnectorRoles")
	@Deprecated
	public published  Str[] getInConnectorRoles () {
		return new Str[] {};
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getOutConnectorRoles-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getOutConnectorRoles")
	@Deprecated
	public published  Str[] getOutConnectorRoles () {
		return new Str[] { OUT };
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getPipeline-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getPipeline")
	@Deprecated
	public published  org.radixware.ads.ServiceBus.server.IPipeline getPipeline () {
		return ServiceBus::SbServerUtil.getPipeline(new Pid(Arte, Types::Id.Factory.loadFrom(pipelineEntityGuid), pipelineEntityPid));
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getTitle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getTitle")
	@Deprecated
	public published  Str getTitle () {
		return title;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getPipelineNodeParam-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getPipelineNodeParam")
	@Deprecated
	public published  Str getPipelineNodeParam (Str name) {
		final java.util.Map<Str,Str> params = getPipeline().getParams();
		if (params != null)
		    params.get(name);
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getOrderPos-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getOrderPos")
	@Deprecated
	public static published  org.radixware.ads.ServiceBus.common.SbPos getOrderPos () {
		return new SbPos(1.62);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:processRequest-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:processRequest")
	@Deprecated
	public published  org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRs processRequest (org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRq rq) throws org.radixware.ads.ServiceBus.server.PipelineException {
		// do nothing
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:export-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:export")
	@Deprecated
	public published  org.radixware.ads.ServiceBus.common.ImpExpXsd.Node export () {
		ServiceBus::ImpExpXsd:Node xNode = ServiceBus::ImpExpXsd:Node.Factory.newInstance();
		Common::CommonXsd:EditableProperties xProps = Common::CommonXsd:EditableProperties.Factory.newInstance();
		Common::CommonXsd:EditableProperty xProp = xProps.addNewItem();
		xProp.Id = idof[System::Unit:instance];
		//xProp.Value = .toStr(.(), , );

		ServiceBus::SbImpExpUtil.exportNode(xNode, this, extGuid, title,
		    Arrays.asList(idof[Endpoint.JmsHub.Server:pipelineEntityGuid], idof[Endpoint.JmsHub.Server:pipelineEntityPid], idof[Endpoint.JmsHub.Server:extGuid]),
		    xProps
		    );

		ServiceBus::ImpExpXsd:JmsHubUnit xJmsHub = ServiceBus::ImpExpXsd:JmsHubUnit.Factory.newInstance();
		ServiceBus::SbImpExpUtil.exportServiceUnit(xJmsHub, this);

		xJmsHub.JmsMessFormat = jmsMessFormat;
		xJmsHub.JmsConnectProps = org.radixware.kernel.server.utils.SrvValAsStr.toStr(Arte::Arte.getInstance(), jmsConnectProps, Meta::ValType:CLOB);
		xJmsHub.JmsMessProps = org.radixware.kernel.server.utils.SrvValAsStr.toStr(Arte::Arte.getInstance(), jmsMessProps, Meta::ValType:CLOB);
		xJmsHub.JmsLogin = jmsLogin;
		xJmsHub.JmsPassword = jmsPassword;
		xJmsHub.MsRqQueueName = msRqQueueName;
		xJmsHub.MsRsQueueName = msRsQueueName;
		xJmsHub.InSeanceCnt = inSeanceCnt;
		xJmsHub.OutSeanceCnt = outSeanceCnt;
		xJmsHub.RsTimeout = rsTimeout;

		xNode.addNewExt().set(xJmsHub);
		return xNode;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:setPipeline-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:setPipeline")
	@Deprecated
	public published  void setPipeline (org.radixware.ads.ServiceBus.server.IPipeline pipeline) {
		pipelineEntityGuid = Str.valueOf(pipeline.getHolderEntityPid().EntityId);
		pipelineEntityPid = Str.valueOf(pipeline.getHolderEntityPid());
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:refreshCache-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:refreshCache")
	@Deprecated
	public published  void refreshCache () {
		traceLevel = null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceDebugXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceDebugXml")
	@Deprecated
	public published  void traceDebugXml (Str mess, org.apache.xmlbeans.XmlObject data, boolean sensitive) {
		trace(Arte::EventSeverity:Debug, (mess != null ? mess + ": " : "") + (data == null ? "null" : data.toString()), sensitive);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:onRequest-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:onRequest")
	@Deprecated
	public published  org.radixware.schemas.jmshandler.MessageDocument onRequest (org.radixware.schemas.jmshandler.MessageDocument.Message mess) {
		getPipeline().refreshCache();
		if (!getPipeline().getChecked()) {
		    if (checkTraceLevel(Arte::EventSeverity:Debug))
		        traceWarning("Pipeline not checked");
		    else
		        throw new AppError("Pipeline not checked");
		}

		Xml xData = ServiceBus::SbServerUtil.castToBinBase64(mess.Data);
		outRqData = ServiceBus::SbCommonUtil.copyXml(xData);

		ServiceBus::SbXsd:PipelineMessageRq rq = ServiceBus::SbServerUtil.prepareNextRequest(null, OUT, outbConnectId, xData);
		ServiceBus::SbServerUtil.prepareJmsHeader(rq.InExtHeader, mess);

		rq.InExtHeader.NodeEntityId = Str.valueOf(Pid.EntityId);
		rq.InExtHeader.NodeEntityPid = Str.valueOf(Pid);
		rq.Event = ServiceBus::PipelineEvent:Rq;
		traceDebug("Request: " + (mess == null ? "null" : new Str(mess.Data)), true);

		ServiceBus::SbXsd:PipelineMessageRs rs;
		try {
		    rs = ServiceBus::SbServerUtil.processRequest(outbConnectId, rq);
		    ServiceBus::SbServerUtil.processAfterResponseDelivered(rs);
		} catch (ServiceBus::PipelineException e) {
		    throw new AppError("Pipeline processing exception: " + e.getMessage(), e);
		} catch (Exceptions::Exception e) {
		    if (e instanceof Exceptions::RuntimeException)
		        throw (Exceptions::RuntimeException)e;
		    throw new AppError(e.getMessage(), e);    
		}

		outRsData = ServiceBus::SbCommonUtil.copyXml(rs.CurStage.Data);
		byte[] sendPacket = ServiceBus::SbServerUtil.castToBytes(rs.CurStage.Data);
		traceDebug("Response: " + (sendPacket == null ? "null" : new Str(sendPacket)), true);

		Jms::JmsHandlerXsd:MessageDocument xRs = Jms::JmsHandlerXsd:MessageDocument.Factory.newInstance();
		Jms::JmsHandlerXsd:Message xMess = xRs.addNewMessage();
		ServiceBus::SbServerUtil.prepareJmsMessage(xMess, rs.OutExtHeader);
		xMess.Data = sendPacket;
		return xRs;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:import-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:import")
	@Deprecated
	public published  void import (org.radixware.ads.ServiceBus.common.ImpExpXsd.Node xNode, boolean overwriteLocalSettings, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		ServiceBus::SbImpExpUtil.importNode(xNode, this, overwriteLocalSettings, helper);

		ServiceBus::ImpExpXsd:JmsHubUnit xJmsHub;
		try {
		    xJmsHub = ServiceBus::ImpExpXsd:JmsHubUnit.Factory.parse(xNode.Ext.newInputStream());
		} catch (Exceptions::Exception e) {
		    throw new AppError(e.getMessage(), e);
		}
		ServiceBus::SbImpExpUtil.importServiceUnit(xJmsHub, this, overwriteLocalSettings, helper);
		if (overwriteLocalSettings) {
		    title = xNode.Title;
		    isClient = false;
		    jmsMessFormat = xJmsHub.JmsMessFormat;
		    jmsConnectProps = (Clob) org.radixware.kernel.server.utils.SrvValAsStr.fromStr(Arte::Arte.getInstance(), xJmsHub.JmsConnectProps, Meta::ValType:CLOB);
		    jmsMessProps = (Clob) org.radixware.kernel.server.utils.SrvValAsStr.fromStr(Arte::Arte.getInstance(), xJmsHub.JmsMessProps, Meta::ValType:CLOB);
		    jmsLogin = xJmsHub.JmsLogin;
		    jmsPassword = xJmsHub.JmsPassword;
		    msRqQueueName = xJmsHub.MsRqQueueName;
		    msRsQueueName = xJmsHub.MsRsQueueName;
		    inSeanceCnt = xJmsHub.InSeanceCnt;
		    outSeanceCnt = xJmsHub.OutSeanceCnt;
		    rsTimeout = xJmsHub.RsTimeout;
		}
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:updateSap-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:updateSap")
	@Deprecated
	protected published  void updateSap (org.radixware.ads.System.server.Sap sap) {
		if(sap.isNewObject()) {
		    sap.address = "localhost";
		}
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getLastInRqData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getLastInRqData")
	@Deprecated
	public published  org.apache.xmlbeans.XmlObject getLastInRqData () {
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getLastInRsData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getLastInRsData")
	@Deprecated
	public published  org.apache.xmlbeans.XmlObject getLastInRsData () {
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getLastOutRqData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getLastOutRqData")
	@Deprecated
	public published  org.apache.xmlbeans.XmlObject getLastOutRqData () {
		return outRqData;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getLastOutRsData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getLastOutRsData")
	@Deprecated
	public published  org.apache.xmlbeans.XmlObject getLastOutRsData () {
		return outRsData;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:afterImport-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:afterImport")
	@Deprecated
	public published  void afterImport (java.util.Map<org.radixware.kernel.server.types.Pid,org.radixware.kernel.server.types.Pid> pids) {
		// do nothing
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:beforeUpdate")
	@Deprecated
	protected published  boolean beforeUpdate () {
		onUpdate();
		return super.beforeUpdate();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:beforeDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:beforeDelete")
	@Deprecated
	protected published  boolean beforeDelete () {
		onUpdate();
		return super.beforeDelete();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:beforeCreate")
	@Deprecated
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		onUpdate();
		return super.beforeCreate(src);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:afterUpdatePropObject-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:afterUpdatePropObject")
	@Deprecated
	protected published  void afterUpdatePropObject (org.radixware.kernel.common.types.Id propId, org.radixware.kernel.server.types.Entity propVal) {
		onUpdate();
		super.afterUpdatePropObject(propId, propVal);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:onUpdate-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:onUpdate")
	@Deprecated
	public published  void onUpdate () {
		getPipeline().onUpdate();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getUsedAddresses-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getUsedAddresses")
	@Deprecated
	protected published  java.util.Collection<org.radixware.ads.System.server.AddressInfo> getUsedAddresses () {
		return new ArrayList<System::AddressInfo>();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getExtGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:getExtGuid")
	@Deprecated
	public published  Str getExtGuid () {
		if (extGuid == null)
		    extGuid = Arte::Arte.generateGuid();
		return extGuid;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:setExtGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:setExtGuid")
	@Deprecated
	public published  void setExtGuid (Str extGuid) {
		extGuid = extGuid;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:afterInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:afterInit")
	@Deprecated
	protected  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		super.afterInit(src, phase);
		if (extGuid == null || (src != null && ((Endpoint.JmsHub.Server)src).extGuid == extGuid))
		    extGuid = Arte::Arte.generateGuid();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:isSuitableForPipeline-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:isSuitableForPipeline")
	@Deprecated
	public published  boolean isSuitableForPipeline (org.radixware.ads.ServiceBus.server.IPipeline pipeline) {
		return false;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server - Server Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.JmsHub.Server_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),"Endpoint.JmsHub.Server",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU77ZDSX5TNAH3GPUD4QTDJ5MBE"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
							/*Owner Class Name*/
							"Endpoint.JmsHub.Server",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU77ZDSX5TNAH3GPUD4QTDJ5MBE"),
							/*Property presentations*/

							/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruGZRKWADUIBH65FNNCTS65PSI4I"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruCCLDFMOYWBGMRCSZZXOGYQQN4Y"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityPid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruWZV4XJJVGZHNHINWJ2TBYKPF7E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7V52DXWKEJASFIWTIRFBBJJNQQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6SGVZWGD3NC4ZHEJIKZVCZPBXU"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceLevel:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNSKHCNLFI5F2RGXRXG4327BD2M"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRqData:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdHOMIGCIC4RHMRIGDJCMY4B2P4E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRsData:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBBUKCPU4PRCDVK55JK2B5KJIQM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:extGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2R5CLL5MAFCCPE4RQKTHLXEZKA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
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
									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHIQA2DTL6NE3BB6532VCHWW5YM"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJV6IPHCDKBFMJHMMQLL2HSSSLE"),
									35888,
									null,

									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHOBP2DIPWZGN7IS2HNUESWFUJQ"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHIQA2DTL6NE3BB6532VCHWW5YM"),
									39984,
									null,

									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJV6IPHCDKBFMJHMMQLL2HSSSLE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRADZIYXO75DFDKFEM2XQPRSQOE")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHIQA2DTL6NE3BB6532VCHWW5YM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHOBP2DIPWZGN7IS2HNUESWFUJQ")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							null,
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectId-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruGZRKWADUIBH65FNNCTS65PSI4I"),"outbConnectId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYIIRDWMGRZE7XMUOBU4FNACWXQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityGuid-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruCCLDFMOYWBGMRCSZZXOGYQQN4Y"),"pipelineEntityGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2JZL2LPVU5A7VOBTX24OJTXPR4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityPid-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruWZV4XJJVGZHNHINWJ2TBYKPF7E"),"pipelineEntityPid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNG3ZCABP4FF7RLIX6RUEGY6RSA"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7V52DXWKEJASFIWTIRFBBJJNQQ"),"outbConnectTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWHAYBH74QBGSVAOUNAGYOOQAMA"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6SGVZWGD3NC4ZHEJIKZVCZPBXU"),"pipelineEntityTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNWYATYLLTJCZXFO7Y7JDM4I52I"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceLevel-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNSKHCNLFI5F2RGXRXG4327BD2M"),"traceLevel",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:isClient-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWAQMCEU35BG3TP4YDKKTVOE2LU"),"isClient",null,org.radixware.kernel.common.enums.EValType.BOOL,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refKCN6JHUWCRA5DIGFJONLLABUJA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colA2PFHZGUNRBXPPOKMGSZI2W4CI"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRqData-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdHOMIGCIC4RHMRIGDJCMY4B2P4E"),"outRqData",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRsData-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBBUKCPU4PRCDVK55JK2B5KJIQM"),"outRsData",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:extGuid-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2R5CLL5MAFCCPE4RQKTHLXEZKA"),"extGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7JXSRN7HJNANHAN7CZOA2VSTJU"),"checkTraceLevel",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("severity",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWQQUPEXM6VFUDJ4MNFYJC4LOGE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNLROOCGTF5CUVM66WIOJDRSHCE"),"trace",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("severity",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEIHMEZQT3JALDPU3EYYHO2IZEM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQ4FFHU5SQNDNDINTWUE7I6HCZY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sensitive",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXUOEYQYBIFB5PATE36WEWO3MHA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWQC7HI6DMJB33MVZT5VNNK6LR4"),"traceDebug",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprG54Z6EAHZVBP3P6DLWRHBWIPO4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sensitive",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprU7RZ7Z4LZZDZHCPVXEGK3GOI44"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQ7MQNDFPEVCSZM46CIFMM4ISAI"),"traceError",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQYVIVRWPH5GMPPRA74VUSAH3HQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNY56F4MVVVAABKIBM7GQ52OD5U"),"traceEvent",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAIY3PFQSPNDLFGLOACESPRL62I"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOYF3L7CFTFB27GLSUU7QWQ44PM"),"traceWarning",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQVAFMCCP7BHAPFOPBHECU37YAM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTO3N6ZUC25BNLNVHR6WUL3OSCU"),"afterResponseDelivered",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rs",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRTPX4JC3URCUTFSS34Z3J5YOFQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOC7IFQSQQNCXLAY4YULNTEVVL4"),"checkConfig",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRJJVN7SZFRGFPKSWMK5OODDPHM"),"connect",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("outConnectorRole",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprU55HYXBMXRDFFB7XBMYJBIIJ3M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("inConnectorId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDJQ5CLDYWZCF5HXU4JSECMS4GQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZEZONIMUSNHEJHOFQ7OPMP3CLU"),"getConnectorIconId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLBFVBODCXREB7KDQGA6ZQRJZWA"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFST4X6BGFNA5PH6LQNOG73MSTQ"),"getConnectorId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprF6H73X4YRJCNHBIPV2GJCIFGCE"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWE5I24A36RE3BGGGIIG4FYAV7M"),"getConnectorRole",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprF23IADTQ7VGRBGEP2JUHYTYAC4"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEIEXINMQGJB35G65UDWFOOSDZU"),"getConnectorRqDataType",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXDDDKHCNZJFMBGZYR5ZNM2L4B4"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHZIY6G7TCVCX3DR6QWA77MJJHI"),"getConnectorRsDataType",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPG4ML6TM6BF3LFOCRJYWIBFWVA"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthB25WTYWZUJAJXMCSH7UVSSJMP4"),"getConnectorSide",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5KHWNUEJ4ND6LHPT3VSOULZXOU"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLB23XTDDKVGSRN5R4BPLLXUMFY"),"getConnectorTitle",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6HA2E2FSTBGG3DZVD777CNQSG4"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLILYOANG5RBVTHLQIJLYCSDJM4"),"getCreatingPresentationId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVA6DRQSCRRCMRNAAYI556P43VE"),"getDescription",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4Y335FOB6VF3JHZ5WI7UI5TCHM"),"getHolderEntityPid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIOMPW3QBZ5ASTPONSTPGMH5ZHQ"),"getEditingPresentationId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOK5OVIZTJ5CUTKASBMH6PUUWGQ"),"getIconId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIQ7FIDN6DFEYXJSX6G5WONK32M"),"getInConnectorRoles",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZHFRUV5SQ5CYNG4JLLR26M4UWU"),"getOutConnectorRoles",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYFZPW475EFFCLLXOYQHNVMU3UM"),"getPipeline",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRGPLXVTN4BB5LNLYHDLSL3K444"),"getTitle",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPI4ONCPYCFAIJNIXF3ZKSVPAYM"),"getPipelineNodeParam",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("name",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprISWAOVSCONH2BA5NPOV2K6E32U"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAXJJZNLFOJGVBNHZN5AEP5GJGM"),"getOrderPos",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7MEKN6RYYBASBPAYYLM556VGPM"),"processRequest",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSE3YSLCKVNFS3AHCTCWCDXJW7E"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSJVD663SIFFXVLMYDV2Y2VU544"),"export",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthG2W65RASFFH7RIEA37LOBPKUFI"),"setPipeline",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pipeline",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHBJU7NWPBVBRZFX2MLFK4O4XVU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthM7KLX4PIANEH3NYQZZSZ55GMIE"),"refreshCache",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7YX5JA3HABE75ADA3FRMDSUGR4"),"traceDebugXml",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEQKAMOP3AVA2DIZWIEXNZ5CPZM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJV4TOA37JRDWZE32I25ZXD2HVM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sensitive",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRCZFT3RMTZEXZGTB54JGUXO7JI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5QSCSEQ3ZJFXVKMUX7VRMSKGLQ"),"onRequest",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprODQ3FFCKQNBPDAZR3T54T2R4UA"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKMN6A6YHPVBNFON237GIJY4ZPU"),"import",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xNode",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXJVEBSYZZJEAXGBWYE67GHX75Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("overwriteLocalSettings",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprML47BKO6G5BLFD77SZM7IABGMI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprV4YHJWQDNZCLNPWLWDFKGZI2RQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUZTMDZO2VVFH3D3QAYKO3ZOWLU"),"updateSap",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sap",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprL53Q6BZXFFEGZIKCFYH7XKTUZ4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNYO5JRURNRFWLFFLCVODWPOTLI"),"getLastInRqData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6XF3CCTG7NGA7O7SEKOQTWLIHE"),"getLastInRsData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLC5CACZEKVCFHEZPWGJ7AN732I"),"getLastOutRqData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6D7TO5XHNRCCZMYQJQZMIWCSUQ"),"getLastOutRsData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBXD5LT3S2VA2ZGTSBZIOEVFN2I"),"afterImport",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pids",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVAUM5ISYFZDEHMATKJYOESJXEQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNZ5Z7AS6RFRPIX6ZD5JDKEC34"),"beforeDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFKFJ4A2E7VD7JHQ3UW7AMW5MOY"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAZ2HAURWXVB53HEDENC67YLQAU"),"afterUpdatePropObject",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNT4WQ4MIXRGWBDSM3UFWHYMJGI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propVal",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHRXQ64Y7DFDBHKIYN4BTKGRY2E"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOUL4OYCWUBAAZKQJ2U4JRELLAI"),"onUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQIF7CPRFE5GUTIY26PT5SYUD6E"),"getUsedAddresses",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthW4LHL5WOQJF7NK3VL3LMQETFTI"),"getExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZVCMFP66RZEELGIQECRMCICA3E"),"setExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCSKSFVINQFDP5JGAF2O7RLDBVQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2MPXEPJCNBFLLLXORNNMFEYHAA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW5X2HZGZEJA4THK72HZPJFMELI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNSQ7CEKCGFFWRLVESA72LZ7DEM"),"isSuitableForPipeline",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pipeline",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSJCCHEC4IJA53PEKDL42YWZNXE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("refKCN6JHUWCRA5DIGFJONLLABUJA")},
						null,null,null,true);
}

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server - Desktop Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server")
public interface Endpoint.JmsHub.Server   extends org.radixware.ads.Jms.explorer.Unit.JmsHandler  {























































































































































	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityTitle:pipelineEntityTitle-Presentation Property*/


	public class PipelineEntityTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PipelineEntityTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityTitle:pipelineEntityTitle")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityTitle:pipelineEntityTitle")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityTitle getPipelineEntityTitle();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectTitle:outbConnectTitle-Presentation Property*/


	public class OutbConnectTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public OutbConnectTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectTitle:outbConnectTitle")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectTitle:outbConnectTitle")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OutbConnectTitle getOutbConnectTitle();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRsData:outRsData-Presentation Property*/


	public class OutRsData extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public OutRsData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Deprecated
		@Override
		public Class<org.apache.xmlbeans.XmlObject> getValClass(){
			return org.apache.xmlbeans.XmlObject.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			org.apache.xmlbeans.XmlObject dummy = x == null ? null : (org.apache.xmlbeans.XmlObject)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.apache.xmlbeans.XmlObject.class);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRsData:outRsData")
		@Deprecated
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRsData:outRsData")
		@Deprecated
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public OutRsData getOutRsData();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRqData:outRqData-Presentation Property*/


	public class OutRqData extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public OutRqData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Deprecated
		@Override
		public Class<org.apache.xmlbeans.XmlObject> getValClass(){
			return org.apache.xmlbeans.XmlObject.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			org.apache.xmlbeans.XmlObject dummy = x == null ? null : (org.apache.xmlbeans.XmlObject)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.apache.xmlbeans.XmlObject.class);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRqData:outRqData")
		@Deprecated
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRqData:outRqData")
		@Deprecated
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public OutRqData getOutRqData();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceLevel:traceLevel-Presentation Property*/


	public class TraceLevel extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public TraceLevel(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceLevel:traceLevel")
		@Deprecated
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceLevel:traceLevel")
		@Deprecated
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public TraceLevel getTraceLevel();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:extGuid:extGuid-Presentation Property*/


	public class ExtGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ExtGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:extGuid:extGuid")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:extGuid:extGuid")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtGuid getExtGuid();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityGuid:pipelineEntityGuid-Presentation Property*/


	public class PipelineEntityGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PipelineEntityGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityGuid:pipelineEntityGuid")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityGuid:pipelineEntityGuid")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityGuid getPipelineEntityGuid();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectId:outbConnectId-Presentation Property*/


	public class OutbConnectId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public OutbConnectId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectId:outbConnectId")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectId:outbConnectId")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OutbConnectId getOutbConnectId();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityPid:pipelineEntityPid-Presentation Property*/


	public class PipelineEntityPid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PipelineEntityPid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityPid:pipelineEntityPid")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityPid:pipelineEntityPid")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityPid getPipelineEntityPid();


}

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.JmsHub.Server_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
			"Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU77ZDSX5TNAH3GPUD4QTDJ5MBE"),null,null,0,

			/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruGZRKWADUIBH65FNNCTS65PSI4I"),
						"outbConnectId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYIIRDWMGRZE7XMUOBU4FNACWXQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruCCLDFMOYWBGMRCSZZXOGYQQN4Y"),
						"pipelineEntityGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2JZL2LPVU5A7VOBTX24OJTXPR4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruWZV4XJJVGZHNHINWJ2TBYKPF7E"),
						"pipelineEntityPid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNG3ZCABP4FF7RLIX6RUEGY6RSA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7V52DXWKEJASFIWTIRFBBJJNQQ"),
						"outbConnectTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWHAYBH74QBGSVAOUNAGYOOQAMA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6SGVZWGD3NC4ZHEJIKZVCZPBXU"),
						"pipelineEntityTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNWYATYLLTJCZXFO7Y7JDM4I52I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceLevel:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNSKHCNLFI5F2RGXRXG4327BD2M"),
						"traceLevel",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceLevel:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRqData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdHOMIGCIC4RHMRIGDJCMY4B2P4E"),
						"outRqData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
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
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRsData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBBUKCPU4PRCDVK55JK2B5KJIQM"),
						"outRsData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
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
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:extGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2R5CLL5MAFCCPE4RQKTHLXEZKA"),
						"extGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:extGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true)
			},
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHIQA2DTL6NE3BB6532VCHWW5YM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHOBP2DIPWZGN7IS2HNUESWFUJQ")},
			true,true,false);
}

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server - Web Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server")
public interface Endpoint.JmsHub.Server   extends org.radixware.ads.Jms.web.Unit.JmsHandler  {























































































































































	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityTitle:pipelineEntityTitle-Presentation Property*/


	public class PipelineEntityTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PipelineEntityTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityTitle:pipelineEntityTitle")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityTitle:pipelineEntityTitle")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityTitle getPipelineEntityTitle();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectTitle:outbConnectTitle-Presentation Property*/


	public class OutbConnectTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public OutbConnectTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectTitle:outbConnectTitle")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectTitle:outbConnectTitle")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OutbConnectTitle getOutbConnectTitle();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRsData:outRsData-Presentation Property*/


	public class OutRsData extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public OutRsData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Deprecated
		@Override
		public Class<org.apache.xmlbeans.XmlObject> getValClass(){
			return org.apache.xmlbeans.XmlObject.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			org.apache.xmlbeans.XmlObject dummy = x == null ? null : (org.apache.xmlbeans.XmlObject)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.apache.xmlbeans.XmlObject.class);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRsData:outRsData")
		@Deprecated
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRsData:outRsData")
		@Deprecated
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public OutRsData getOutRsData();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRqData:outRqData-Presentation Property*/


	public class OutRqData extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public OutRqData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Deprecated
		@Override
		public Class<org.apache.xmlbeans.XmlObject> getValClass(){
			return org.apache.xmlbeans.XmlObject.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			org.apache.xmlbeans.XmlObject dummy = x == null ? null : (org.apache.xmlbeans.XmlObject)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.apache.xmlbeans.XmlObject.class);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRqData:outRqData")
		@Deprecated
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRqData:outRqData")
		@Deprecated
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public OutRqData getOutRqData();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceLevel:traceLevel-Presentation Property*/


	public class TraceLevel extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public TraceLevel(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceLevel:traceLevel")
		@Deprecated
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceLevel:traceLevel")
		@Deprecated
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public TraceLevel getTraceLevel();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:extGuid:extGuid-Presentation Property*/


	public class ExtGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ExtGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:extGuid:extGuid")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:extGuid:extGuid")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtGuid getExtGuid();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityGuid:pipelineEntityGuid-Presentation Property*/


	public class PipelineEntityGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PipelineEntityGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityGuid:pipelineEntityGuid")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityGuid:pipelineEntityGuid")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityGuid getPipelineEntityGuid();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectId:outbConnectId-Presentation Property*/


	public class OutbConnectId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public OutbConnectId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectId:outbConnectId")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectId:outbConnectId")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OutbConnectId getOutbConnectId();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityPid:pipelineEntityPid-Presentation Property*/


	public class PipelineEntityPid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PipelineEntityPid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityPid:pipelineEntityPid")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityPid:pipelineEntityPid")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityPid getPipelineEntityPid();


}

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.JmsHub.Server_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
			"Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU77ZDSX5TNAH3GPUD4QTDJ5MBE"),null,null,0,

			/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruGZRKWADUIBH65FNNCTS65PSI4I"),
						"outbConnectId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYIIRDWMGRZE7XMUOBU4FNACWXQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruCCLDFMOYWBGMRCSZZXOGYQQN4Y"),
						"pipelineEntityGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2JZL2LPVU5A7VOBTX24OJTXPR4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruWZV4XJJVGZHNHINWJ2TBYKPF7E"),
						"pipelineEntityPid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNG3ZCABP4FF7RLIX6RUEGY6RSA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7V52DXWKEJASFIWTIRFBBJJNQQ"),
						"outbConnectTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWHAYBH74QBGSVAOUNAGYOOQAMA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outbConnectTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6SGVZWGD3NC4ZHEJIKZVCZPBXU"),
						"pipelineEntityTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNWYATYLLTJCZXFO7Y7JDM4I52I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:pipelineEntityTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceLevel:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNSKHCNLFI5F2RGXRXG4327BD2M"),
						"traceLevel",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:traceLevel:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRqData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdHOMIGCIC4RHMRIGDJCMY4B2P4E"),
						"outRqData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
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
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:outRsData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBBUKCPU4PRCDVK55JK2B5KJIQM"),
						"outRsData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
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
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:extGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2R5CLL5MAFCCPE4RQKTHLXEZKA"),
						"extGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:extGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true)
			},
			null,
			null,
			null,
			null,
			null,
			true,true,false);
}

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHIQA2DTL6NE3BB6532VCHWW5YM"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJV6IPHCDKBFMJHMMQLL2HSSSLE"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIDQJI44F3VFPBODZWRAEYBFF7E"),
	null,

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Settings-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgCSEXGV454FGTVDPBTUAL7ZM2HY"),"Settings",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5IWMH4Y5DRATVDTBHUWP2PHL5Y"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKHT276DSLVD7FKJLBW3IWOQMFM"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBC6VGIU6S5CFNAN6V4PG3DKCUQ"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO77HLWLCFFFWVIKV7MKDDEZY5A"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2YNMUHO7AJCVTMJW2K5OPQ3GQQ"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC5VS2BZB3RAHFNHR5SWKU5LBA4"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRH4YQQJF4NCVNIMAN3HXIQSWLY"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col37TNUN2QIVAWJE2HLAOG6OY4YA"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6SGVZWGD3NC4ZHEJIKZVCZPBXU"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7V52DXWKEJASFIWTIRFBBJJNQQ"),0,1,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOCDVZAOTBXOBDNTPAAMPGXSZKU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgCSEXGV454FGTVDPBTUAL7ZM2HY")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUJB2RCH6KZHD5IMTQTE76DQSZA")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgJ4BHY7HBLBBBXIIQZEPCKTW2MU"))}
	,

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	35888,0,0);
}
/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.web;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHIQA2DTL6NE3BB6532VCHWW5YM"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJV6IPHCDKBFMJHMMQLL2HSSSLE"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIDQJI44F3VFPBODZWRAEYBFF7E"),
	null,

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Editor Pages-Editor Presentation Pages*/
	null,
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
	}
	,

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	35888,0,0);
}
/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model - Desktop Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model")
public class Edit:Model  extends org.radixware.ads.ServiceBus.Nodes.explorer.Endpoint.JmsHub.Server.Endpoint.JmsHub.Server_DefaultModel.eprJV6IPHCDKBFMJHMMQLL2HSSSLE_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:pipelineEntityTitle-Presentation Property*/




	public class PipelineEntityTitle extends org.radixware.ads.ServiceBus.Nodes.explorer.Endpoint.JmsHub.Server.Endpoint.JmsHub.Server_DefaultModel.eprJV6IPHCDKBFMJHMMQLL2HSSSLE_ModelAdapter.prd6SGVZWGD3NC4ZHEJIKZVCZPBXU{
		public PipelineEntityTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}













		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:pipelineEntityTitle")
		public  Str getValue() {
			return Value;
		}
		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:pipelineEntityTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityTitle getPipelineEntityTitle(){return (PipelineEntityTitle)getProperty(prd6SGVZWGD3NC4ZHEJIKZVCZPBXU);}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:outbConnectTitle-Presentation Property*/




	public class OutbConnectTitle extends org.radixware.ads.ServiceBus.Nodes.explorer.Endpoint.JmsHub.Server.Endpoint.JmsHub.Server_DefaultModel.eprJV6IPHCDKBFMJHMMQLL2HSSSLE_ModelAdapter.prd7V52DXWKEJASFIWTIRFBBJJNQQ{
		public OutbConnectTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}













		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:outbConnectTitle")
		public  Str getValue() {
			return Value;
		}
		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:outbConnectTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OutbConnectTitle getOutbConnectTitle(){return (OutbConnectTitle)getProperty(prd7V52DXWKEJASFIWTIRFBBJJNQQ);}










	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		pipelineEntityTitle.setVisible(!(getContext() instanceof ServiceBus::ISbContext));
		super.beforeOpenView();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:sapEditorPageOpened-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:sapEditorPageOpened")
	public published  void sapEditorPageOpened (com.trolltech.qt.gui.QWidget widget) {
		super.sapEditorPageOpened(widget);
		if (sapEditor != null) {
		    sapEditor.setSettingsPageVisible(false);
		}
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:checkSapEditor-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:checkSapEditor")
	protected published  boolean checkSapEditor () {
		return true;
	}


}

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemHIQA2DTL6NE3BB6532VCHWW5YM"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemJV6IPHCDKBFMJHMMQLL2HSSSLE"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model - Web Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model")
public class Edit:Model  extends org.radixware.ads.ServiceBus.Nodes.web.Endpoint.JmsHub.Server.Endpoint.JmsHub.Server_DefaultModel.eprJV6IPHCDKBFMJHMMQLL2HSSSLE_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:pipelineEntityTitle-Presentation Property*/




	public class PipelineEntityTitle extends org.radixware.ads.ServiceBus.Nodes.web.Endpoint.JmsHub.Server.Endpoint.JmsHub.Server_DefaultModel.eprJV6IPHCDKBFMJHMMQLL2HSSSLE_ModelAdapter.prd6SGVZWGD3NC4ZHEJIKZVCZPBXU{
		public PipelineEntityTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}













		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:pipelineEntityTitle")
		public  Str getValue() {
			return Value;
		}
		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:pipelineEntityTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityTitle getPipelineEntityTitle(){return (PipelineEntityTitle)getProperty(prd6SGVZWGD3NC4ZHEJIKZVCZPBXU);}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:outbConnectTitle-Presentation Property*/




	public class OutbConnectTitle extends org.radixware.ads.ServiceBus.Nodes.web.Endpoint.JmsHub.Server.Endpoint.JmsHub.Server_DefaultModel.eprJV6IPHCDKBFMJHMMQLL2HSSSLE_ModelAdapter.prd7V52DXWKEJASFIWTIRFBBJJNQQ{
		public OutbConnectTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}













		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:outbConnectTitle")
		public  Str getValue() {
			return Value;
		}
		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:outbConnectTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OutbConnectTitle getOutbConnectTitle(){return (OutbConnectTitle)getProperty(prd7V52DXWKEJASFIWTIRFBBJJNQQ);}










	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		pipelineEntityTitle.setVisible(!(getContext() instanceof ServiceBus::ISbContext));
		super.beforeOpenView();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:checkSapEditor-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:checkSapEditor")
	protected published  boolean checkSapEditor () {
		return true;
	}


}

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemHIQA2DTL6NE3BB6532VCHWW5YM"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemJV6IPHCDKBFMJHMMQLL2HSSSLE"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Edit:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHOBP2DIPWZGN7IS2HNUESWFUJQ"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHIQA2DTL6NE3BB6532VCHWW5YM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXZH3Q37JAZHA7D3ZDP6DROUWHQ"),
	null,
	null,
	null,

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	39984,0,0);
}
/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.web;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHOBP2DIPWZGN7IS2HNUESWFUJQ"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHIQA2DTL6NE3BB6532VCHWW5YM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBRC2F5RBORDB7PCXGBAJAIY2IE"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXZH3Q37JAZHA7D3ZDP6DROUWHQ"),
	null,
	null,
	null,

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	39984,0,0);
}
/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Model - Desktop Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Model")
public class Create:Model  extends org.radixware.ads.ServiceBus.Nodes.explorer.Edit:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Model:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		if (getContext() instanceof ServiceBus::ISbContext) {
		    final ServiceBus::ISbContext context = (ServiceBus::ISbContext)getContext();
		    if (isNew()) {
		        pipelineEntityGuid.Value = String.valueOf(context.getPipelinePid().getTableId());
		        pipelineEntityPid.Value = String.valueOf(context.getPipelinePid());
		    }
		}
		super.beforeOpenView();
	}


}

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Model - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemHOBP2DIPWZGN7IS2HNUESWFUJQ"),
						"Create:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemHIQA2DTL6NE3BB6532VCHWW5YM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Model - Web Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Model")
public class Create:Model  extends org.radixware.ads.ServiceBus.Nodes.web.Edit:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Model:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		if (getContext() instanceof ServiceBus::ISbContext) {
		    final ServiceBus::ISbContext context = (ServiceBus::ISbContext)getContext();
		    if (isNew()) {
		        pipelineEntityGuid.Value = String.valueOf(context.getPipelinePid().getTableId());
		        pipelineEntityPid.Value = String.valueOf(context.getPipelinePid());
		    }
		}
		super.beforeOpenView();
	}


}

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Model - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemHOBP2DIPWZGN7IS2HNUESWFUJQ"),
						"Create:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemHIQA2DTL6NE3BB6532VCHWW5YM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server:Create:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Server - Localizing Bundle */
package org.radixware.ads.ServiceBus.Nodes.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.JmsHub.Server - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Entity ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2JZL2LPVU5A7VOBTX24OJTXPR4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Settings");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Настройки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5IWMH4Y5DRATVDTBHUWP2PHL5Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pipeline not checked");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конвейер не проверен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7I7ETGSRHRCMXHMJ73F62ZHZ7E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pipeline processing exception: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Исключение при обработке узлов конвейера: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC6SVCAN7F5HT7FNPDEM5IXBMOM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"JMS - Server");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"JMS - сервер");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIDQJI44F3VFPBODZWRAEYBFF7E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Entity PID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ключ сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNG3ZCABP4FF7RLIX6RUEGY6RSA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pipeline");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конвейер");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNWYATYLLTJCZXFO7Y7JDM4I52I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pipeline not checked");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конвейер не проверен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPB5ERRCOL5E2DJX75VC4RFFYXY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Inbound requests");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Входящие запросы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQOTB7MC67ZB5NLXZLKKH47HEE4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"JMS- Server");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"JMS - сервер");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU77ZDSX5TNAH3GPUD4QTDJ5MBE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Connector");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Коннектор");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWHAYBH74QBGSVAOUNAGYOOQAMA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"JMS - Server");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"JMS - сервер");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXZH3Q37JAZHA7D3ZDP6DROUWHQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Outbound connection");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Соединение выходного коннектора");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYIIRDWMGRZE7XMUOBU4FNACWXQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Endpoint.JmsHub.Server - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclBRC2F5RBORDB7PCXGBAJAIY2IE"),"Endpoint.JmsHub.Server - Localizing Bundle",$$$items$$$);
}
