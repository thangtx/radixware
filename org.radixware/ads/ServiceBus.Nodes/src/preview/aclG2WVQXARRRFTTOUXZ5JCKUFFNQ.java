
/* Radix::ServiceBus.Nodes::Endpoint.Service.Server - Server Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.Service.Server-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.server;

import java.util.*;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server")
public published class Endpoint.Service.Server  extends org.radixware.ads.System.server.Unit.Arte  implements org.radixware.ads.ServiceBus.server.IPipelineNode,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private final static Str OUT = "Out";
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Endpoint.Service.Server_mi.rdxMeta;}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityGuid-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityGuid")
	public published  Str getPipelineEntityGuid() {
		return pipelineEntityGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityGuid")
	public published   void setPipelineEntityGuid(Str val) {
		pipelineEntityGuid = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityPid-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityPid")
	public published  Str getPipelineEntityPid() {
		return pipelineEntityPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityPid")
	public published   void setPipelineEntityPid(Str val) {
		pipelineEntityPid = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectId-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectId")
	public published  Str getOutbConnectId() {
		return outbConnectId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectId")
	public published   void setOutbConnectId(Str val) {
		outbConnectId = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityTitle-Dynamic Property*/



	protected Str pipelineEntityTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityTitle")
	public published  Str getPipelineEntityTitle() {

		if (pipelineEntityGuid == null || pipelineEntityPid == null)
		    return null;
		return ServiceBus::SbServerUtil.getEntityTitle(
		    new Pid(Arte, Types::Id.Factory.loadFrom(pipelineEntityGuid), pipelineEntityPid)
		    );
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityTitle")
	public published   void setPipelineEntityTitle(Str val) {
		pipelineEntityTitle = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectTitle-Dynamic Property*/



	protected Str outbConnectTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectTitle")
	public published  Str getOutbConnectTitle() {

		return ServiceBus::SbServerUtil.getConnectionTitle(outbConnectId);
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectTitle")
	public published   void setOutbConnectTitle(Str val) {
		outbConnectTitle = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceLevel-Dynamic Property*/



	protected Int traceLevel=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceLevel")
	private final  Int getTraceLevel() {
		return traceLevel;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceLevel")
	private final   void setTraceLevel(Int val) {
		traceLevel = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:service-Dynamic Property*/




	@Override
	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:service")
	public published   void setService(org.radixware.ads.System.server.Service val) {

		internal[service] = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRqData-Dynamic Property*/



	protected org.apache.xmlbeans.XmlObject outRqData=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRqData")
	protected published  org.apache.xmlbeans.XmlObject getOutRqData() {
		return outRqData;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRqData")
	protected published   void setOutRqData(org.apache.xmlbeans.XmlObject val) {
		outRqData = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRsData-Dynamic Property*/



	protected org.apache.xmlbeans.XmlObject outRsData=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRsData")
	protected published  org.apache.xmlbeans.XmlObject getOutRsData() {
		return outRsData;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRsData")
	protected published   void setOutRsData(org.apache.xmlbeans.XmlObject val) {
		outRsData = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:extGuid-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:extGuid")
	public published  Str getExtGuid() {
		return extGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:extGuid")
	public published   void setExtGuid(Str val) {
		extGuid = val;
	}

























































































	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:afterResponseDelivered-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:afterResponseDelivered")
	public published  void afterResponseDelivered (org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRs rq) throws org.radixware.ads.ServiceBus.server.PipelineException {

	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:checkConfig-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:checkConfig")
	public published  void checkConfig () throws org.radixware.ads.ServiceBus.server.PipelineConfigException {
		ServiceBus::SbServerUtil.checkConnection(this, OUT, outbConnectId);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:connect-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:connect")
	public published  void connect (Str outConnectorRole, Str inConnectorId) {
		if (outConnectorRole == OUT) {
		    outbConnectId = inConnectorId;
		} else    
		    throw new AppError("Unknown role " + outConnectorRole);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:getConnectorIconId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:getConnectorIconId")
	public published  org.radixware.kernel.common.types.Id getConnectorIconId (Str role) {
		if (role == OUT)
		    return idof[ServiceBus::next];
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:getConnectorId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:getConnectorId")
	public published  Str getConnectorId (Str role) {
		if (role == OUT)
		    return outbConnectId;
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:getConnectorRole-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:getConnectorRole")
	public published  Str getConnectorRole (Str id) {
		if (id == outbConnectId)
		    return OUT;
		throw new AppError("Unknown id " + id);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:getConnectorRqDataType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:getConnectorRqDataType")
	public published  Str getConnectorRqDataType (Str role) {
		if (role == OUT)
		    return ServiceBus::SbCommonUtil.ENVELOPE_TYPE;
		throw new AppError("Unknown role " + role);

	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:getConnectorRsDataType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:getConnectorRsDataType")
	public published  Str getConnectorRsDataType (Str role) {
		if (role == OUT)
		    return ServiceBus::SbCommonUtil.ENVELOPE_TYPE;
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:getConnectorTitle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:getConnectorTitle")
	public published  Str getConnectorTitle (Str role) {
		if (role == OUT)
		    return "Inbound requests";
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:getDescription-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:getDescription")
	public published  Str getDescription () {
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:getHolderEntityPid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:getHolderEntityPid")
	public published  org.radixware.kernel.server.types.Pid getHolderEntityPid () {
		return getPid();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:getIconId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:getIconId")
	public published  org.radixware.kernel.common.types.Id getIconId () {
		return idof[ServiceServer];
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:getInConnectorRoles-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:getInConnectorRoles")
	public published  Str[] getInConnectorRoles () {
		return new Str[] {};
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:getOutConnectorRoles-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:getOutConnectorRoles")
	public published  Str[] getOutConnectorRoles () {
		return new Str[] { OUT };
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:getPipeline-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:getPipeline")
	public published  org.radixware.ads.ServiceBus.server.IPipeline getPipeline () {
		return ServiceBus::SbServerUtil.getPipeline(new Pid(Arte, Types::Id.Factory.loadFrom(pipelineEntityGuid), pipelineEntityPid));
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:getTitle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:getTitle")
	public published  Str getTitle () {
		return title;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:processRequest-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:processRequest")
	public published  org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRs processRequest (org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRq rq) throws org.radixware.ads.ServiceBus.server.PipelineException {
		// do nothing
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:getCreatingPresentationId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:getCreatingPresentationId")
	public published  org.radixware.kernel.common.types.Id getCreatingPresentationId () {
		return idof[Endpoint.Service.Server:Create];
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:getEditingPresentationId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:getEditingPresentationId")
	public published  org.radixware.kernel.common.types.Id getEditingPresentationId () {
		return idof[Endpoint.Service.Server:General];
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceDebug-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceDebug")
	public published  void traceDebug (Str mess, boolean sensitive) {
		trace(Arte::EventSeverity:Debug, mess, sensitive);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceError-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceError")
	public published  void traceError (Str mess) {
		trace(Arte::EventSeverity:Error, mess, false);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceEvent-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceEvent")
	public published  void traceEvent (Str mess) {
		trace(Arte::EventSeverity:Event, mess, false);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:trace-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:trace")
	public published  void trace (org.radixware.kernel.common.enums.EEventSeverity severity, Str mess, boolean sensitive) {
		if (!checkTraceLevel(severity))
		    return;

		Arte::Trace.enterContext(Arte::EventContextType:SystemUnit, id);
		Object traceTargetHandler = Arte::Trace.addContextProfile(getPipeline().getTraceProfile(), (getPipeline() instanceof Types::Entity) ? ((Types::Entity) getPipeline()) : this);
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

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceWarning-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceWarning")
	public published  void traceWarning (Str mess) {
		trace(Arte::EventSeverity:Warning, mess, false);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:getConnectorSide-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:getConnectorSide")
	public published  org.radixware.ads.ServiceBus.common.Side getConnectorSide (Str role) {
		if (role == OUT)
		    return ServiceBus::Side:Right;
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:checkTraceLevel-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:checkTraceLevel")
	public published  boolean checkTraceLevel (org.radixware.kernel.common.enums.EEventSeverity severity) {
		if (traceLevel == null)
		    traceLevel = Arte::Trace.getMinSeverity(Arte::EventSource:ServiceBus);

		if (severity == Arte::EventSeverity:Debug)
		    return traceLevel.intValue() == Arte::EventSeverity:Debug.Value.intValue();

		return traceLevel.intValue() <= severity.Value.intValue();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:getOrderPos-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:getOrderPos")
	public static published  org.radixware.ads.ServiceBus.common.SbPos getOrderPos () {
		return new SbPos(1.5);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:getPipelineNodeParam-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:getPipelineNodeParam")
	public published  Str getPipelineNodeParam (Str name) {
		final java.util.Map<Str,Str> params = getPipeline().getParams();
		if (params != null)
		    params.get(name);
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceDebugXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceDebugXml")
	public published  void traceDebugXml (Str mess, org.apache.xmlbeans.XmlObject data, boolean sensitive) {
		trace(Arte::EventSeverity:Debug, (mess != null ? mess + ": " : "") + (data == null ? "null" : data.toString()), sensitive);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:loadServiceProps-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:loadServiceProps")
	protected  void loadServiceProps () {

	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:updateService-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:updateService")
	protected published  void updateService () {
		try {
		    System::Service service = System::Service.loadByPK(1, serviceUri, true);
		    if (service == null) {
		        service = new System::Service();
		        service.init();
		        service.title = "Service of unit #" + id;
		        service.systemId = 1;
		        service.wsdlUri = serviceUri;
		        service.uri = serviceUri;
		        service.accessibility = null;
		        service.implementedInArte = false;
		        service.create();
		    }
		} catch (Exceptions::Exception e) {
		    throw new AppError("Service: " + e.LocalizedMessage, e);
		}

		super.updateService();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:export-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:export")
	public published  org.radixware.ads.ServiceBus.common.ImpExpXsd.Node export () {
		ServiceBus::ImpExpXsd:Node xNode = ServiceBus::ImpExpXsd:Node.Factory.newInstance();
		Common::CommonXsd:EditableProperties xProps = Common::CommonXsd:EditableProperties.Factory.newInstance();
		Common::CommonXsd:EditableProperty xProp = xProps.addNewItem();
		xProp.Id = idof[System::Unit:instance];
		//xProp.Value = .toStr(.(), , );

		ServiceBus::SbImpExpUtil.exportNode(xNode, this, extGuid, title,
		    Arrays.asList(idof[Endpoint.Service.Server:pipelineEntityGuid], idof[Endpoint.Service.Server:pipelineEntityPid], idof[Endpoint.Service.Server:extGuid]),
		    xProps
		    );

		ServiceBus::ImpExpXsd:ServiceServer xUnit = ServiceBus::ImpExpXsd:ServiceServer.Factory.newInstance();
		ServiceBus::SbImpExpUtil.exportUnit(xUnit, this);
		xUnit.SapPropsXml = System::ServerSapUtils.writeProps(sap, null);
		xUnit.ServiceUri = serviceUri;
		xUnit.HighArteInstCount = highArteInstCount;
		xNode.addNewExt().set(xUnit);
		return xNode;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:setPipeline-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:setPipeline")
	public published  void setPipeline (org.radixware.ads.ServiceBus.server.IPipeline pipeline) {
		pipelineEntityGuid = Str.valueOf(pipeline.getHolderEntityPid().EntityId);
		pipelineEntityPid = Str.valueOf(pipeline.getHolderEntityPid());
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:beforeCreate")
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		onUpdate();
		return super.beforeCreate(src);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:refreshCache-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:refreshCache")
	public published  void refreshCache () {
		traceLevel = null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:process-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:process")
	public published  org.apache.xmlbeans.XmlObject process (Str serverAddr, Str clientAddr, Str clientCertCn, java.util.Map<Str,Str> headerAttrs, org.apache.xmlbeans.XmlObject request) {
		getPipeline().refreshCache();
		if (!getPipeline().getChecked()) {
		    if (checkTraceLevel(Arte::EventSeverity:Debug))
		        traceWarning("Pipeline not checked");
		    else
		        throw new AppError("Pipeline not checked");
		}

		traceDebugXml("Received data", request, true);
		System::SoapXsd:EnvelopeDocument xData = ServiceBus::SbServerUtil.castToEnvelopeDocument(request);
		outRqData = ServiceBus::SbCommonUtil.copyXml(xData);

		ServiceBus::SbXsd:PipelineMessageRq rq =
		        ServiceBus::SbServerUtil.prepareNextRequest(null, OUT, outbConnectId, xData);

		ServiceBus::SbXsd:ExtHeader.Net xNet = rq.InExtHeader.addNewNet();
		xNet.ServerAddress = serverAddr;
		xNet.ClientAddress = clientAddr;
		xNet.ClientCertCn = clientCertCn;

		rq.InExtHeader.HttpHeader = ServiceBus::SbServerUtil.mapToXml(headerAttrs);
		rq.InExtHeader.NodeEntityId = Str.valueOf(Pid.EntityId);
		rq.InExtHeader.NodeEntityPid = Str.valueOf(Pid);
		rq.Event = ServiceBus::PipelineEvent:Rq;

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
		Xml xBody = ServiceBus::SbServerUtil.castToEnvelopeBody(rs.CurStage.Data);
		traceDebugXml("Send data", xBody, true);
		return xBody;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:import-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:import")
	public published  void import (org.radixware.ads.ServiceBus.common.ImpExpXsd.Node xNode, boolean overwriteLocalSettings, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		ServiceBus::SbImpExpUtil.importNode(xNode, this, overwriteLocalSettings, helper);
		ServiceBus::ImpExpXsd:ServiceServer xUnit;
		try {
		    xUnit = ServiceBus::ImpExpXsd:ServiceServer.Factory.parse(xNode.Ext.newInputStream());
		} catch (Exceptions::Exception e) {
		    throw new AppError(e.getMessage(), e);
		}
		serviceUri = xUnit.ServiceUri;

		ServiceBus::SbImpExpUtil.importUnit(xUnit, this, overwriteLocalSettings, helper);
		if (overwriteLocalSettings) {
		    title = xNode.Title;
		    ServiceBus::ImpExpXsd:Sap xSap = xUnit.Sap;
		    if (xUnit.isSetSapPropsXml()) {
		        sapPropsXml = xUnit.SapPropsXml;
		    } else {
		        sapPropsXml = ServiceBus::SbImpExpUtil.importSapProps(xSap, overwriteLocalSettings);
		    }

		    highArteInstCount = xUnit.HighArteInstCount;
		}
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		loadServiceProps();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:appendIdToServiceUri-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:appendIdToServiceUri")
	protected published  boolean appendIdToServiceUri () {
		return false;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:getLastInRqData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:getLastInRqData")
	public published  org.apache.xmlbeans.XmlObject getLastInRqData () {
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:getLastInRsData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:getLastInRsData")
	public published  org.apache.xmlbeans.XmlObject getLastInRsData () {
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:getLastOutRqData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:getLastOutRqData")
	public published  org.apache.xmlbeans.XmlObject getLastOutRqData () {
		return outRqData;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:getLastOutRsData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:getLastOutRsData")
	public published  org.apache.xmlbeans.XmlObject getLastOutRsData () {
		return outRsData;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:afterImport-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:afterImport")
	public published  void afterImport (java.util.Map<org.radixware.kernel.server.types.Pid,org.radixware.kernel.server.types.Pid> pids) {
		// do nothing
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:beforeDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:beforeDelete")
	protected published  boolean beforeDelete () {
		onUpdate();
		return super.beforeDelete();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:beforeUpdate")
	protected published  boolean beforeUpdate () {
		onUpdate();
		return super.beforeUpdate();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:afterUpdatePropObject-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:afterUpdatePropObject")
	protected published  void afterUpdatePropObject (org.radixware.kernel.common.types.Id propId, org.radixware.kernel.server.types.Entity propVal) {
		onUpdate();
		super.afterUpdatePropObject(propId, propVal);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:onUpdate-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:onUpdate")
	public published  void onUpdate () {
		getPipeline().onUpdate();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:getExtGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:getExtGuid")
	public published  Str getExtGuid () {
		if (extGuid == null)
		    extGuid = Arte::Arte.generateGuid();
		return extGuid;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:setExtGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:setExtGuid")
	public published  void setExtGuid (Str extGuid) {
		extGuid = extGuid;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:afterInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:afterInit")
	protected published  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		super.afterInit(src, phase);
		if (extGuid == null || (src != null && ((Endpoint.Service.Server)src).extGuid == extGuid))
		    extGuid = Arte::Arte.generateGuid();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:isSuitableForPipeline-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:isSuitableForPipeline")
	public published  boolean isSuitableForPipeline (org.radixware.ads.ServiceBus.server.IPipeline pipeline) {
		return !pipeline.isTransformation();
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::ServiceBus.Nodes::Endpoint.Service.Server - Server Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Service.Server-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.Service.Server_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),"Endpoint.Service.Server",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVGP5RYYBG5CRPE2SKGNYKBOMSQ"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
							/*Owner Class Name*/
							"Endpoint.Service.Server",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVGP5RYYBG5CRPE2SKGNYKBOMSQ"),
							/*Property presentations*/

							/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruEBMZUZUWLBDGXER52ZKEOSETO4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityPid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2EB24GYFA5H73ONHSX5RGZQ43A"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruBY7DHNK35NDXZHLJFGQKSUH33I"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQ77HU2CMKVBBLPFQRKBEIMHQCE"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDSIJ5S6ORZB65DTUHNEJ2XVZPQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceLevel:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdI5EA6CHJRRBPHM6DUQCGZWDCVE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:service:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4BMIX7VU2FEYVHRPZNE2U4HW4A"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,null,null,null,org.radixware.kernel.server.types.Restrictions.Factory.newInstance(16439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRqData:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd33RFJXK3M5FA3JSIZI7C7HH2IU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRsData:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDGG6EFLVORFNTMN5JZ5XTEJPNY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:extGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruEGIUOLX4IVCYNFTBK4AAFY4TYY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
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
									/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOW5CC3NXXNBJLMFI3DV7XCMWNY"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZZ6KB4TXLPOBDCJEAALOMT5GDM"),
									35888,
									null,

									/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEDKM4DSLOFCLRBNJQASPBOI25A"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOW5CC3NXXNBJLMFI3DV7XCMWNY"),
									35888,
									null,

									/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null)
							},
							/*Selector presentations*/
							null,
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZZ6KB4TXLPOBDCJEAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2NLJ6YFMK7OBDCJAAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOW5CC3NXXNBJLMFI3DV7XCMWNY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEDKM4DSLOFCLRBNJQASPBOI25A")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							null,
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),

						/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityGuid-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruEBMZUZUWLBDGXER52ZKEOSETO4"),"pipelineEntityGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLFM4UDZ4CFF2FETY2D7A376UUU"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityPid-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2EB24GYFA5H73ONHSX5RGZQ43A"),"pipelineEntityPid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQDAXVSBZDFFRJCODWX6AVTTFHQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectId-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruBY7DHNK35NDXZHLJFGQKSUH33I"),"outbConnectId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRPMPGRAD2JAW3HYNPHUYC5E5BQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQ77HU2CMKVBBLPFQRKBEIMHQCE"),"pipelineEntityTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7EWVGDUV7FEY7CQB2KPXCUOU4Q"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDSIJ5S6ORZB65DTUHNEJ2XVZPQ"),"outbConnectTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOCDQ3EQZP5ENTPLP7X3KZMYG74"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceLevel-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdI5EA6CHJRRBPHM6DUQCGZWDCVE"),"traceLevel",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:service-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4BMIX7VU2FEYVHRPZNE2U4HW4A"),"service",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6N6MJFIM4JA7RAXGRCX2S7E5DY"),org.radixware.kernel.common.enums.EValType.PARENT_REF,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecC2OWQGDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRqData-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd33RFJXK3M5FA3JSIZI7C7HH2IU"),"outRqData",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRsData-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDGG6EFLVORFNTMN5JZ5XTEJPNY"),"outRsData",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:extGuid-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruEGIUOLX4IVCYNFTBK4AAFY4TYY"),"extGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTO3N6ZUC25BNLNVHR6WUL3OSCU"),"afterResponseDelivered",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRTV42YXFDNDSVBVSRJ34D4EUEQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOC7IFQSQQNCXLAY4YULNTEVVL4"),"checkConfig",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRJJVN7SZFRGFPKSWMK5OODDPHM"),"connect",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("outConnectorRole",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQRORR7LFRJA25K77VLB7L6EZTE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("inConnectorId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTMKUYBOAYRCVHNW5AB3CWYM7IU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZEZONIMUSNHEJHOFQ7OPMP3CLU"),"getConnectorIconId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRRIHK4YMTNEYFJ2ZF2S3E36HBE"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFST4X6BGFNA5PH6LQNOG73MSTQ"),"getConnectorId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMTYGH24L4JHXVP6HH3IDL2H5GA"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWE5I24A36RE3BGGGIIG4FYAV7M"),"getConnectorRole",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGVDTE54TZBGTNKLKOJSP2ZMAVY"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEIEXINMQGJB35G65UDWFOOSDZU"),"getConnectorRqDataType",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6AHCPLROZVGVPEC7VZCPTZO24A"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHZIY6G7TCVCX3DR6QWA77MJJHI"),"getConnectorRsDataType",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDIYNUPWW3VF6RBDIO27AFAIJIE"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLB23XTDDKVGSRN5R4BPLLXUMFY"),"getConnectorTitle",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFM33V7JCCFHVFKTNTD7FUJSGM4"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVA6DRQSCRRCMRNAAYI556P43VE"),"getDescription",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4Y335FOB6VF3JHZ5WI7UI5TCHM"),"getHolderEntityPid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOK5OVIZTJ5CUTKASBMH6PUUWGQ"),"getIconId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIQ7FIDN6DFEYXJSX6G5WONK32M"),"getInConnectorRoles",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZHFRUV5SQ5CYNG4JLLR26M4UWU"),"getOutConnectorRoles",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYFZPW475EFFCLLXOYQHNVMU3UM"),"getPipeline",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRGPLXVTN4BB5LNLYHDLSL3K444"),"getTitle",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7MEKN6RYYBASBPAYYLM556VGPM"),"processRequest",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRRFBHZCF4ZHQFK4V5ENGEPJH4A"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLILYOANG5RBVTHLQIJLYCSDJM4"),"getCreatingPresentationId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIOMPW3QBZ5ASTPONSTPGMH5ZHQ"),"getEditingPresentationId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWQC7HI6DMJB33MVZT5VNNK6LR4"),"traceDebug",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCLQWQIXLORFJFCCK4O7WU2U4U4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sensitive",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFMPIVDXB5ZDFZACPTGKFQNOROQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQ7MQNDFPEVCSZM46CIFMM4ISAI"),"traceError",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNXYAY5X5RNHGBGKRF472FMLWLU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNY56F4MVVVAABKIBM7GQ52OD5U"),"traceEvent",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMFVH65XSLRAE3HI62QT4NON42Q"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNLROOCGTF5CUVM66WIOJDRSHCE"),"trace",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("severity",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWXWA2SVVEVGL7BE4OJJNRIRCWM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIQNFI2AD3RHZFGY5OGR5YSDSRA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sensitive",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPPMFLSCBJZEIDE4UBOCOMSWNTM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOYF3L7CFTFB27GLSUU7QWQ44PM"),"traceWarning",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3JBUPSX3DNEOLAZEJ2RFL362AQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthB25WTYWZUJAJXMCSH7UVSSJMP4"),"getConnectorSide",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHVAHS3F7TZHJHNZLGE23CFEY2I"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7JXSRN7HJNANHAN7CZOA2VSTJU"),"checkTraceLevel",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("severity",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMV4GPGUVHFBUXGZ6DVMUSTCYP4"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAXJJZNLFOJGVBNHZN5AEP5GJGM"),"getOrderPos",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPI4ONCPYCFAIJNIXF3ZKSVPAYM"),"getPipelineNodeParam",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("name",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEVLNWHMSW5D77E53O3HLRHIAMU"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFZFSSWAX7ZD6BAPVNVU4VXBGVM"),"traceDebugXml",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKL5VETV3OVABHCAHXTXITUC2FE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXJ4LEY4JSBC2DLVTNQENTHS75A")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sensitive",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprR52RHVRQE5FX7KCCTUCJNVDVME"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHIFWOHVALPOBDCJEAALOMT5GDM"),"loadServiceProps",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHMFWOHVALPOBDCJEAALOMT5GDM"),"updateService",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSJVD663SIFFXVLMYDV2Y2VU544"),"export",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthG2W65RASFFH7RIEA37LOBPKUFI"),"setPipeline",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pipeline",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3JMHEMVONZDL5KI2RVTQARMHPQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMJ2K22P2URF3RN3MBUZYJWZ46E"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthM7KLX4PIANEH3NYQZZSZ55GMIE"),"refreshCache",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4LMGQAO7HNAAHEQGEKUGRB4CWA"),"process",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("serverAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIS3OE4HKU5F6BDDGQ2L66D5SJI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprV5FQFH3MU5FLNLHZZWNMJGCC5M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientCertCn",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYGIT3N46YFA27OUHEJPR3R7N34")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("headerAttrs",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBTFXQ43WN5FAXLS7AQWA7VDBXI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("request",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2X4XUELTCZCPBAB5U5MWFDGZEA"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKMN6A6YHPVBNFON237GIJY4ZPU"),"import",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xNode",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBDVNDOZIOJHLRJJC23NXVKFZSM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("overwriteLocalSettings",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5V2A2F3AJFEMDBDDKT62WKVNCU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQ5IUZV6GOVHXRJUXMDOXBWUTVA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCVICCNQ5IJE25PKGOLZVB6Y54E"),"afterRead",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJ2H3SDBOTNCRHEZAMLXGNWMIGA"),"appendIdToServiceUri",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNYO5JRURNRFWLFFLCVODWPOTLI"),"getLastInRqData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6XF3CCTG7NGA7O7SEKOQTWLIHE"),"getLastInRsData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLC5CACZEKVCFHEZPWGJ7AN732I"),"getLastOutRqData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6D7TO5XHNRCCZMYQJQZMIWCSUQ"),"getLastOutRsData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBXD5LT3S2VA2ZGTSBZIOEVFN2I"),"afterImport",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pids",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAPTMSKYKEVAYRM6P5EK7E7L42Y"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNZ5Z7AS6RFRPIX6ZD5JDKEC34"),"beforeDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAZ2HAURWXVB53HEDENC67YLQAU"),"afterUpdatePropObject",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprP22JS6NQ6JALPGWABTBYV2KRIY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propVal",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2G5FGXEVVNAY5GNYIHS67HAQVU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOCTD62IE3JCPNN3V26ABSE4UD4"),"onUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthW4LHL5WOQJF7NK3VL3LMQETFTI"),"getExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZVCMFP66RZEELGIQECRMCICA3E"),"setExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBZGGV5QEJ5AYPPKLPFZIXGCQ5Q"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVNA2RNGK4VFS3FJPMK34EZGOZM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCWAGRU7QDNCEFCMLGPIUUQHXGU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNSQ7CEKCGFFWRLVESA72LZ7DEM"),"isSuitableForPipeline",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pipeline",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr25VY72FAEFACHKBSYMRRNS4B3I"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("refAIKHITFNK7OBDCJAAALOMT5GDM")},
						null,null,null,false);
}

/* Radix::ServiceBus.Nodes::Endpoint.Service.Server - Desktop Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.Service.Server-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server")
public interface Endpoint.Service.Server   extends org.radixware.ads.System.explorer.Unit.Arte  {























































































































































	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRqData:outRqData-Presentation Property*/


	public class OutRqData extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public OutRqData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRqData:outRqData")
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRqData:outRqData")
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public OutRqData getOutRqData();
	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRsData:outRsData-Presentation Property*/


	public class OutRsData extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public OutRsData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRsData:outRsData")
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRsData:outRsData")
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public OutRsData getOutRsData();
	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectTitle:outbConnectTitle-Presentation Property*/


	public class OutbConnectTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public OutbConnectTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectTitle:outbConnectTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectTitle:outbConnectTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OutbConnectTitle getOutbConnectTitle();
	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceLevel:traceLevel-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceLevel:traceLevel")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceLevel:traceLevel")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public TraceLevel getTraceLevel();
	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityTitle:pipelineEntityTitle-Presentation Property*/


	public class PipelineEntityTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PipelineEntityTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityTitle:pipelineEntityTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityTitle:pipelineEntityTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityTitle getPipelineEntityTitle();
	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityPid:pipelineEntityPid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityPid:pipelineEntityPid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityPid:pipelineEntityPid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityPid getPipelineEntityPid();
	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectId:outbConnectId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectId:outbConnectId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectId:outbConnectId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OutbConnectId getOutbConnectId();
	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityGuid:pipelineEntityGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityGuid:pipelineEntityGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityGuid:pipelineEntityGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityGuid getPipelineEntityGuid();
	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:extGuid:extGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:extGuid:extGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:extGuid:extGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtGuid getExtGuid();


}

/* Radix::ServiceBus.Nodes::Endpoint.Service.Server - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Service.Server-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.Service.Server_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
			"Radix::ServiceBus.Nodes::Endpoint.Service.Server",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVGP5RYYBG5CRPE2SKGNYKBOMSQ"),null,null,0,

			/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruEBMZUZUWLBDGXER52ZKEOSETO4"),
						"pipelineEntityGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLFM4UDZ4CFF2FETY2D7A376UUU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2EB24GYFA5H73ONHSX5RGZQ43A"),
						"pipelineEntityPid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQDAXVSBZDFFRJCODWX6AVTTFHQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruBY7DHNK35NDXZHLJFGQKSUH33I"),
						"outbConnectId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRPMPGRAD2JAW3HYNPHUYC5E5BQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQ77HU2CMKVBBLPFQRKBEIMHQCE"),
						"pipelineEntityTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7EWVGDUV7FEY7CQB2KPXCUOU4Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDSIJ5S6ORZB65DTUHNEJ2XVZPQ"),
						"outbConnectTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOCDQ3EQZP5ENTPLP7X3KZMYG74"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceLevel:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdI5EA6CHJRRBPHM6DUQCGZWDCVE"),
						"traceLevel",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceLevel:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:service:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4BMIX7VU2FEYVHRPZNE2U4HW4A"),
						"service",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						63,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecC2OWQGDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblC2OWQGDVVHWDBROXAAIT4AGD7E"),
						null,
						null,
						null,
						16439,
						20,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRqData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd33RFJXK3M5FA3JSIZI7C7HH2IU"),
						"outRqData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
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

					/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRsData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDGG6EFLVORFNTMN5JZ5XTEJPNY"),
						"outRsData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
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

					/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:extGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruEGIUOLX4IVCYNFTBK4AAFY4TYY"),
						"extGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:extGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
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
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOW5CC3NXXNBJLMFI3DV7XCMWNY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEDKM4DSLOFCLRBNJQASPBOI25A")},
			true,true,false);
}

/* Radix::ServiceBus.Nodes::Endpoint.Service.Server - Web Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.Service.Server-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server")
public interface Endpoint.Service.Server   extends org.radixware.ads.System.web.Unit.Arte  {























































































































































	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRqData:outRqData-Presentation Property*/


	public class OutRqData extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public OutRqData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRqData:outRqData")
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRqData:outRqData")
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public OutRqData getOutRqData();
	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRsData:outRsData-Presentation Property*/


	public class OutRsData extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public OutRsData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRsData:outRsData")
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRsData:outRsData")
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public OutRsData getOutRsData();
	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectTitle:outbConnectTitle-Presentation Property*/


	public class OutbConnectTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public OutbConnectTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectTitle:outbConnectTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectTitle:outbConnectTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OutbConnectTitle getOutbConnectTitle();
	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceLevel:traceLevel-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceLevel:traceLevel")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceLevel:traceLevel")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public TraceLevel getTraceLevel();
	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityTitle:pipelineEntityTitle-Presentation Property*/


	public class PipelineEntityTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PipelineEntityTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityTitle:pipelineEntityTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityTitle:pipelineEntityTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityTitle getPipelineEntityTitle();
	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityPid:pipelineEntityPid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityPid:pipelineEntityPid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityPid:pipelineEntityPid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityPid getPipelineEntityPid();
	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectId:outbConnectId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectId:outbConnectId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectId:outbConnectId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OutbConnectId getOutbConnectId();
	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityGuid:pipelineEntityGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityGuid:pipelineEntityGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityGuid:pipelineEntityGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityGuid getPipelineEntityGuid();
	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:extGuid:extGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:extGuid:extGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:extGuid:extGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtGuid getExtGuid();


}

/* Radix::ServiceBus.Nodes::Endpoint.Service.Server - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Service.Server-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.Service.Server_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
			"Radix::ServiceBus.Nodes::Endpoint.Service.Server",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFKCYABVIK7OBDCJAAALOMT5GDM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVGP5RYYBG5CRPE2SKGNYKBOMSQ"),null,null,0,

			/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruEBMZUZUWLBDGXER52ZKEOSETO4"),
						"pipelineEntityGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLFM4UDZ4CFF2FETY2D7A376UUU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2EB24GYFA5H73ONHSX5RGZQ43A"),
						"pipelineEntityPid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQDAXVSBZDFFRJCODWX6AVTTFHQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruBY7DHNK35NDXZHLJFGQKSUH33I"),
						"outbConnectId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRPMPGRAD2JAW3HYNPHUYC5E5BQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQ77HU2CMKVBBLPFQRKBEIMHQCE"),
						"pipelineEntityTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7EWVGDUV7FEY7CQB2KPXCUOU4Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:pipelineEntityTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDSIJ5S6ORZB65DTUHNEJ2XVZPQ"),
						"outbConnectTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOCDQ3EQZP5ENTPLP7X3KZMYG74"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outbConnectTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceLevel:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdI5EA6CHJRRBPHM6DUQCGZWDCVE"),
						"traceLevel",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:traceLevel:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:service:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4BMIX7VU2FEYVHRPZNE2U4HW4A"),
						"service",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						63,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecC2OWQGDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblC2OWQGDVVHWDBROXAAIT4AGD7E"),
						null,
						null,
						null,
						16439,
						20,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRqData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd33RFJXK3M5FA3JSIZI7C7HH2IU"),
						"outRqData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
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

					/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:outRsData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDGG6EFLVORFNTMN5JZ5XTEJPNY"),
						"outRsData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
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

					/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:extGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruEGIUOLX4IVCYNFTBK4AAFY4TYY"),
						"extGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:extGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
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
			null,
			true,true,false);
}

/* Radix::ServiceBus.Nodes::Endpoint.Service.Server:General - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOW5CC3NXXNBJLMFI3DV7XCMWNY"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZZ6KB4TXLPOBDCJEAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLC2VBZQ4JVAGRA254YUISSTJW4"),
	null,

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOCDVZAOTBXOBDNTPAAMPGXSZKU"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYQ4JGWXHUBFKNIECGKPHHZ3UJI"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA"),0,0,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU7QY4J3ZXDORDF72ABIFNQAABA"),0,2,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI6NERFZYS5VDBFCXAAUMFADAIA"),0,3,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY5NZZOJBXTNRDB6QAALOMT5GDM"),0,6,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVX5YH2SS5VDBN2IAAUMFADAIA"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDSIJ5S6ORZB65DTUHNEJ2XVZPQ"),0,5,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQ77HU2CMKVBBLPFQRKBEIMHQCE"),0,4,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4Z6HA6NC5ZADBCPDEBBNET2IRA"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTYUCIQQYTZCDVDV7MJQVDW2M5A"),1,7,1,true,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBWQ33IP5GJER3MDGKP5K7RNDHQ"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd65KAVNFIANDIFIOL2PUOBZWAZI"),1,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVA2Y4NZKIZAS7DIOUYDAT6VCQI"),0,1,2,false,false)
			},null),

			/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Service-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgDDS3G5LML5A2PHX25ZXZASPK2U"),"Service",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVNHBSWFJUNFOBBH35P5MCKNMGY"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU77W6DFOK7OBDCJAAALOMT5GDM"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUX7W6DFOK7OBDCJAAALOMT5GDM"),0,0,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOCDVZAOTBXOBDNTPAAMPGXSZKU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgDDS3G5LML5A2PHX25ZXZASPK2U")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg4EOJB4NOK7OBDCJAAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUJB2RCH6KZHD5IMTQTE76DQSZA")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgJ4BHY7HBLBBBXIIQZEPCKTW2MU"))}
	,

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	35888,0,0);
}
/* Radix::ServiceBus.Nodes::Endpoint.Service.Server:General - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.web;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOW5CC3NXXNBJLMFI3DV7XCMWNY"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZZ6KB4TXLPOBDCJEAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLC2VBZQ4JVAGRA254YUISSTJW4"),
	null,

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Editor Pages-Editor Presentation Pages*/
	null,
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
	}
	,

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	35888,0,0);
}
/* Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model - Desktop Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model")
public class General:Model  extends org.radixware.ads.ServiceBus.Nodes.explorer.Endpoint.Service.Server.Endpoint.Service.Server_DefaultModel.eprZZ6KB4TXLPOBDCJEAALOMT5GDM_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		pipelineEntityTitle.setVisible(!(getContext() instanceof ServiceBus::ISbContext));
		super.beforeOpenView();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model:sapEditorPageOpened-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model:sapEditorPageOpened")
	public published  void sapEditorPageOpened (com.trolltech.qt.gui.QWidget widget) {
		super.sapEditorPageOpened(widget);
		sapEditor.setPredefinedAccessibility(System::ServiceAccessibility:BOTH);
	}


}

/* Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemOW5CC3NXXNBJLMFI3DV7XCMWNY"),
						"General:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemZZ6KB4TXLPOBDCJEAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model - Web Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model")
public class General:Model  extends org.radixware.ads.ServiceBus.Nodes.web.Endpoint.Service.Server.Endpoint.Service.Server_DefaultModel.eprZZ6KB4TXLPOBDCJEAALOMT5GDM_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		pipelineEntityTitle.setVisible(!(getContext() instanceof ServiceBus::ISbContext));
		super.beforeOpenView();
	}


}

/* Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemOW5CC3NXXNBJLMFI3DV7XCMWNY"),
						"General:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemZZ6KB4TXLPOBDCJEAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:General:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEDKM4DSLOFCLRBNJQASPBOI25A"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOW5CC3NXXNBJLMFI3DV7XCMWNY"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4AIH7HR5JNA6DDKK5FSKNLEGZA"),
	null,

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Editor Pages-Editor Presentation Pages*/
	null,
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOCDVZAOTBXOBDNTPAAMPGXSZKU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgDDS3G5LML5A2PHX25ZXZASPK2U")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg4EOJB4NOK7OBDCJAAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUJB2RCH6KZHD5IMTQTE76DQSZA"))}
	,

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	35888,0,0);
}
/* Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.web;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEDKM4DSLOFCLRBNJQASPBOI25A"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOW5CC3NXXNBJLMFI3DV7XCMWNY"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4AIH7HR5JNA6DDKK5FSKNLEGZA"),
	null,

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Editor Pages-Editor Presentation Pages*/
	null,
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
	}
	,

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	35888,0,0);
}
/* Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Model - Desktop Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Model")
public class Create:Model  extends org.radixware.ads.ServiceBus.Nodes.explorer.General:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Model:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		if (getContext() instanceof ServiceBus::ISbContext) {
		    final ServiceBus::ISbContext context = (ServiceBus::ISbContext)getContext();
		    pipelineEntityGuid.Value = Str.valueOf(context.getPipelinePid().getTableId());
		    pipelineEntityPid.Value = Str.valueOf(context.getPipelinePid());        
		}
		super.beforeOpenView();
	}


}

/* Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Model - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemEDKM4DSLOFCLRBNJQASPBOI25A"),
						"Create:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemOW5CC3NXXNBJLMFI3DV7XCMWNY"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Model - Web Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Model")
public class Create:Model  extends org.radixware.ads.ServiceBus.Nodes.web.General:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Model:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		if (getContext() instanceof ServiceBus::ISbContext) {
		    final ServiceBus::ISbContext context = (ServiceBus::ISbContext)getContext();
		    pipelineEntityGuid.Value = Str.valueOf(context.getPipelinePid().getTableId());
		    pipelineEntityPid.Value = Str.valueOf(context.getPipelinePid());        
		}
		super.beforeOpenView();
	}


}

/* Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Model - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemEDKM4DSLOFCLRBNJQASPBOI25A"),
						"Create:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemOW5CC3NXXNBJLMFI3DV7XCMWNY"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Endpoint.Service.Server:Create:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Endpoint.Service.Server - Localizing Bundle */
package org.radixware.ads.ServiceBus.Nodes.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.Service.Server - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Service Channel - Server");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервисный канал - сервер");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4AIH7HR5JNA6DDKK5FSKNLEGZA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pipeline");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конвейер");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7EWVGDUV7FEY7CQB2KPXCUOU4Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Inbound requests");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Входящие запросы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7JNUOCGIN5G4HF52IRELHOK3IU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Service: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервис: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCEEUTAEEVFCG3DEI5CEQQF3SBE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pipeline not checked");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конвейер не проверен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGSG4CGZXIBCETNZO3QH4SQ3AUA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Service Channel - Server");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервисный канал - сервер");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLC2VBZQ4JVAGRA254YUISSTJW4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Entity ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLFM4UDZ4CFF2FETY2D7A376UUU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pipeline processing exception: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Исключение при обработке узлов конвейера: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNDTHFAZRQJC35E6IPHBYR2KQFE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Connector");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Коннектор");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOCDQ3EQZP5ENTPLP7X3KZMYG74"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pipeline not checked");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конвейер не проверен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPIC5TVO4MVB6PI5YE6LJENFZNY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Entity ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQDAXVSBZDFFRJCODWX6AVTTFHQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Outbound connection");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Соединение выходного коннектора");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRPMPGRAD2JAW3HYNPHUYC5E5BQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSY3EGTJPGJD4FOBM2WMPK2AABU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Service Channel - Server");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервисный канал - сервер");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVGP5RYYBG5CRPE2SKGNYKBOMSQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Service Parameters");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры сервиса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVNHBSWFJUNFOBBH35P5MCKNMGY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYQ4JGWXHUBFKNIECGKPHHZ3UJI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Endpoint.Service.Server - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclG2WVQXARRRFTTOUXZ5JCKUFFNQ"),"Endpoint.Service.Server - Localizing Bundle",$$$items$$$);
}
