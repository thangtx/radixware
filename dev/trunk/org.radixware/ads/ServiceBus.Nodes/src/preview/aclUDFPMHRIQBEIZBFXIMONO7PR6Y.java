
/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client - Server Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.server;

import java.util.*;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client")
@Deprecated
public published class Endpoint.JmsHub.Client  extends org.radixware.ads.Jms.server.Unit.JmsHandler  implements org.radixware.ads.ServiceBus.server.IPipelineNode,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private static final int JMS_SERVICE_TIMEOUT = 120;
	private static final int JMS_SERVICE_KEEP_CONNECTIONTIME = 20;
	private final static Str IN = "In";
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Endpoint.JmsHub.Client_mi.rdxMeta;}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityGuid-User Property*/


















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityGuid")
	@Deprecated
	public published  Str getPipelineEntityGuid() {
		return pipelineEntityGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityGuid")
	@Deprecated
	public published   void setPipelineEntityGuid(Str val) {
		pipelineEntityGuid = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityPid-User Property*/


















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityPid")
	@Deprecated
	public published  Str getPipelineEntityPid() {
		return pipelineEntityPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityPid")
	@Deprecated
	public published   void setPipelineEntityPid(Str val) {
		pipelineEntityPid = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityTitle-Dynamic Property*/



	@Deprecated
	protected Str pipelineEntityTitle=null;













	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityTitle")
	@Deprecated
	public published  Str getPipelineEntityTitle() {

		if (pipelineEntityGuid == null || pipelineEntityPid == null)
		    return null;
		return ServiceBus::SbServerUtil.getEntityTitle(
		    new Pid(Arte, Types::Id.Factory.loadFrom(pipelineEntityGuid), pipelineEntityPid)
		    );
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityTitle")
	@Deprecated
	public published   void setPipelineEntityTitle(Str val) {
		pipelineEntityTitle = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceLevel-Dynamic Property*/



	@Deprecated
	protected Int traceLevel=null;













	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceLevel")
	@Deprecated
	private final  Int getTraceLevel() {
		return traceLevel;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceLevel")
	@Deprecated
	private final   void setTraceLevel(Int val) {
		traceLevel = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:isClient-Detail Column Property*/




	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRqData-Dynamic Property*/



	@Deprecated
	protected org.apache.xmlbeans.XmlObject inRqData=null;













	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRqData")
	@Deprecated
	protected published  org.apache.xmlbeans.XmlObject getInRqData() {
		return inRqData;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRqData")
	@Deprecated
	protected published   void setInRqData(org.apache.xmlbeans.XmlObject val) {
		inRqData = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRsData-Dynamic Property*/



	@Deprecated
	protected org.apache.xmlbeans.XmlObject inRsData=null;













	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRsData")
	@Deprecated
	protected published  org.apache.xmlbeans.XmlObject getInRsData() {
		return inRsData;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRsData")
	@Deprecated
	protected published   void setInRsData(org.apache.xmlbeans.XmlObject val) {
		inRsData = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:extGuid-User Property*/


















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:extGuid")
	@Deprecated
	public published  Str getExtGuid() {
		return extGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:extGuid")
	@Deprecated
	public published   void setExtGuid(Str val) {
		extGuid = val;
	}













































































	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:checkTraceLevel-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:checkTraceLevel")
	@Deprecated
	public published  boolean checkTraceLevel (org.radixware.kernel.common.enums.EEventSeverity severity) {
		if (traceLevel == null)
		    traceLevel = Arte::Trace.getMinSeverity(Arte::EventSource:ServiceBus);

		if (severity == Arte::EventSeverity:Debug)
		    return traceLevel.intValue() == Arte::EventSeverity:Debug.Value.intValue();

		return traceLevel.intValue() <= severity.Value.intValue();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:trace-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:trace")
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

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceDebug-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceDebug")
	@Deprecated
	public published  void traceDebug (Str mess, boolean sensitive) {
		trace(Arte::EventSeverity:Debug, mess, sensitive);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceError-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceError")
	@Deprecated
	public published  void traceError (Str mess) {
		trace(Arte::EventSeverity:Error, mess, false);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceEvent-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceEvent")
	@Deprecated
	public published  void traceEvent (Str mess) {
		trace(Arte::EventSeverity:Event, mess, false);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceWarning-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceWarning")
	@Deprecated
	public published  void traceWarning (Str mess) {
		trace(Arte::EventSeverity:Warning, mess, false);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:afterResponseDelivered-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:afterResponseDelivered")
	@Deprecated
	public published  void afterResponseDelivered (org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRs rs) throws org.radixware.ads.ServiceBus.server.PipelineException {

	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:checkConfig-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:checkConfig")
	@Deprecated
	public published  void checkConfig () throws org.radixware.ads.ServiceBus.server.PipelineConfigException {

	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:connect-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:connect")
	@Deprecated
	public published  void connect (Str outConnectorRole, Str inConnectorId) {
		throw new AppError("Unknown role " + outConnectorRole);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getConnectorIconId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getConnectorIconId")
	@Deprecated
	public published  org.radixware.kernel.common.types.Id getConnectorIconId (Str role) {
		if (role == IN)
		    return idof[ServiceBus::next];
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getConnectorId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getConnectorId")
	@Deprecated
	public published  Str getConnectorId (Str role) {
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getConnectorRole-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getConnectorRole")
	@Deprecated
	public published  Str getConnectorRole (Str id) {
		throw new AppError("Unknown id " + id);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getConnectorRqDataType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getConnectorRqDataType")
	@Deprecated
	public published  Str getConnectorRqDataType (Str role) {
		if (role == IN)
		    return BINARY_TYPE;
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getConnectorRsDataType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getConnectorRsDataType")
	@Deprecated
	public published  Str getConnectorRsDataType (Str role) {
		if (role == IN)
		    return BINARY_TYPE;
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getConnectorSide-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getConnectorSide")
	@Deprecated
	public published  org.radixware.ads.ServiceBus.common.Side getConnectorSide (Str role) {
		if (role == IN)
		    return ServiceBus::Side:Left;
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getConnectorTitle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getConnectorTitle")
	@Deprecated
	public published  Str getConnectorTitle (Str role) {
		if (role == IN)
		    return "Outbound requests";
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getCreatingPresentationId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getCreatingPresentationId")
	@Deprecated
	public published  org.radixware.kernel.common.types.Id getCreatingPresentationId () {
		return idof[Endpoint.JmsHub.Client:Create];
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getDescription-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getDescription")
	@Deprecated
	public published  Str getDescription () {
		return title;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getHolderEntityPid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getHolderEntityPid")
	@Deprecated
	public published  org.radixware.kernel.server.types.Pid getHolderEntityPid () {
		return getPid();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getEditingPresentationId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getEditingPresentationId")
	@Deprecated
	public published  org.radixware.kernel.common.types.Id getEditingPresentationId () {
		return idof[Endpoint.JmsHub.Client:Edit];
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getIconId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getIconId")
	@Deprecated
	public published  org.radixware.kernel.common.types.Id getIconId () {
		return idof[JMLClient];
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getInConnectorRoles-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getInConnectorRoles")
	@Deprecated
	public published  Str[] getInConnectorRoles () {
		return new Str[] { IN };
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getOutConnectorRoles-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getOutConnectorRoles")
	@Deprecated
	public published  Str[] getOutConnectorRoles () {
		return new Str[] {};
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getPipeline-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getPipeline")
	@Deprecated
	public published  org.radixware.ads.ServiceBus.server.IPipeline getPipeline () {
		return ServiceBus::SbServerUtil.getPipeline(new Pid(Arte, Types::Id.Factory.loadFrom(pipelineEntityGuid), pipelineEntityPid));
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getTitle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getTitle")
	@Deprecated
	public published  Str getTitle () {
		return title;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getPipelineNodeParam-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getPipelineNodeParam")
	@Deprecated
	public published  Str getPipelineNodeParam (Str name) {
		final java.util.Map<Str,Str> params = getPipeline().getParams();
		if (params != null)
		    params.get(name);
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getOrderPos-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getOrderPos")
	@Deprecated
	public static published  org.radixware.ads.ServiceBus.common.SbPos getOrderPos () {
		return new SbPos(1.61);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:processRequest-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:processRequest")
	@Deprecated
	public published  org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRs processRequest (org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRq rq) throws org.radixware.ads.ServiceBus.server.PipelineException {
		if (jmsMessFormat == Jms::JmsMessageFormat:BIN) { // bin
		    final Jms::JmsHandlerWsdl:ProcessBinDocument xDoc = Jms::JmsHandlerWsdl:ProcessBinDocument.Factory.newInstance();
		    final Jms::JmsHandlerXsd:ProcessBinRq xRq = xDoc.addNewProcessBin().addNewProcessBinRq();
		    xRq.setMessage(ServiceBus::SbServerUtil.castToBytes(rq.CurStage.Data));
		    ServiceBus::SbServerUtil.prepareJmsMessage(xRq, rq.OutExtHeader);
		    traceDebug("Send data: " + (xRq.Message == null ? "" : new Str(xRq.Message)), true);
		    inRqData = ServiceBus::SbCommonUtil.copyXml(rq.CurStage.Data);

		    try {
		        xDoc.set(Arte::Arte.invokeInternalService(xDoc, Jms::JmsHandlerWsdl:ProcessBinDocument.class, "http://schemas.radixware.org/jmshandler.wsdl#" + id, JMS_SERVICE_KEEP_CONNECTIONTIME, JMS_SERVICE_TIMEOUT, null));
		    } catch(Exceptions::ServiceCallFault e) {
		        if (e.getFaultString() == Jms::JmsHandlerXsd:ExceptionEnum.EXT_SYS_CALL_TIMEOUT.toString()) {
		            traceError("Service call receive timeout");
		            throw new PipelineException.Timeout.Receive(e.getMessage(), this);
		        } else if (e.getFaultString() == Jms::JmsHandlerXsd:ExceptionEnum.UNCORRELATED_RECV.toString()) {
		            traceError(e.getMessage());
		            throw new PipelineException.UncorrelatedRs(e.getMessage(), this);
		        } else {
		            traceError("Service call fault: " + e.getMessage());
		            throw new PipelineException(e.getMessage(), e);
		        }
		    } catch(Exceptions::ServiceCallException e) {
		        traceError("Service call exception: " + e.getMessage());
		        throw new PipelineException.Timeout.Receive(e.getMessage(), this);
		    } catch(Exceptions::ServiceCallTimeout e) {    
		        traceError("Service call timeout: " + e.getMessage());
		        throw new PipelineException.Timeout.Receive(e.getMessage(), this);
		    } catch(Exceptions::InterruptedException e) {
		        traceError("Interrupted exception: " + e.getMessage());
		        throw new PipelineException.Interrupted(e.getMessage(), this);
		    }

		    final Jms::JmsHandlerXsd:ProcessBinRs xRs = xDoc.getProcessBin().getProcessBinRs();
		    traceDebug("Received data: " + (xRs.Message == null ? "" : new Str(xRs.Message)), true);
		    
		    Xml xData = ServiceBus::SbServerUtil.castToBinBase64(xRs.Message);
		    inRsData = ServiceBus::SbCommonUtil.copyXml(xData);

		    ServiceBus::SbXsd:PipelineMessageRs rs = ServiceBus::SbServerUtil.prepareNextResponse(null, rq.CurStage, xData);
		    ServiceBus::SbServerUtil.prepareJmsHeader(rs.InExtHeader, xRs);
		    rs.InExtHeader.NodeEntityId = Str.valueOf(Pid.EntityId);
		    rs.InExtHeader.NodeEntityPid = Str.valueOf(Pid);
		    return rs;
		} else { // text
		    final Jms::JmsHandlerWsdl:ProcessTextDocument xDoc = Jms::JmsHandlerWsdl:ProcessTextDocument.Factory.newInstance();
		    final Jms::JmsHandlerXsd:ProcessTextRq xRq = xDoc.addNewProcessText().addNewProcessTextRq();
		    final byte[] bytes = ServiceBus::SbServerUtil.castToBytes(rq.CurStage.Data);
		    if (bytes != null) {
		        final Str text = new Str(bytes);
		        xRq.setMessage(text);
		        traceDebug("Send data: " + text, true);
		    } else
		        traceDebug("Send data: null", true);
		    ServiceBus::SbServerUtil.prepareJmsMessage(xRq, rq.OutExtHeader);
		    inRqData = ServiceBus::SbCommonUtil.copyXml(rq.CurStage.Data);

		    try {
		        xDoc.set(Arte::Arte.invokeInternalService(xDoc, Jms::JmsHandlerWsdl:ProcessTextDocument.class, "http://schemas.radixware.org/jmshandler.wsdl#" + id, JMS_SERVICE_KEEP_CONNECTIONTIME, JMS_SERVICE_TIMEOUT, null));
		    } catch(Exceptions::ServiceCallFault e) {
		        if (e.getFaultString() == Jms::JmsHandlerXsd:ExceptionEnum.EXT_SYS_CALL_TIMEOUT.toString()) {
		            traceError("Service call receive timeout");
		            throw new PipelineException.Timeout.Receive(e.getMessage(), this);
		        } else if (e.getFaultString() == Jms::JmsHandlerXsd:ExceptionEnum.UNCORRELATED_RECV.toString()) {
		            traceError(e.getMessage());
		            throw new PipelineException.UncorrelatedRs(e.getMessage(), this);
		        } else {
		            traceError("Service call fault: " + e.getMessage());
		            throw new PipelineException(e.getMessage(), e);
		        }
		    } catch(Exceptions::ServiceCallException e) {
		        traceError("Service call exception: " + e.getMessage());
		        throw new PipelineException.Timeout.Receive(e.getMessage(), this);
		    } catch(Exceptions::ServiceCallTimeout e) {    
		        traceError("Service call timeout: " + e.getMessage());
		        throw new PipelineException.Timeout.Receive(e.getMessage(), this);
		    } catch(Exceptions::InterruptedException e) {
		        traceError("Interrupted exception: " + e.getMessage());
		        throw new PipelineException.Interrupted(e.getMessage(), this);
		    }

		    final Jms::JmsHandlerXsd:ProcessTextRs xRs = xDoc.getProcessText().getProcessTextRs();
		    traceDebug("Received data: " + (xRs.Message == null ? "null" : xRs.Message), true);

		    Xml xData = ServiceBus::SbServerUtil.castToBinBase64(xRs.Message == null ? null : xRs.Message.Bytes);
		    inRsData = ServiceBus::SbCommonUtil.copyXml(xData);

		    ServiceBus::SbXsd:PipelineMessageRs rs = ServiceBus::SbServerUtil.prepareNextResponse(null, rq.CurStage, xData);
		    ServiceBus::SbServerUtil.prepareJmsHeader(rs.InExtHeader, xRs);
		    rs.InExtHeader.NodeEntityId = Str.valueOf(Pid.EntityId);
		    rs.InExtHeader.NodeEntityPid = Str.valueOf(Pid);
		    return rs;
		}
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:export-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:export")
	@Deprecated
	public published  org.radixware.ads.ServiceBus.common.ImpExpXsd.Node export () {
		ServiceBus::ImpExpXsd:Node xNode = ServiceBus::ImpExpXsd:Node.Factory.newInstance();
		Common::CommonXsd:EditableProperties xProps = Common::CommonXsd:EditableProperties.Factory.newInstance();
		Common::CommonXsd:EditableProperty xProp = xProps.addNewItem();
		xProp.Id = idof[System::Unit:instance];
		//xProp.Value = .toStr(.(), , );
		ServiceBus::SbImpExpUtil.exportNode(xNode, this, extGuid, title,
		    Arrays.asList(idof[Endpoint.JmsHub.Client:pipelineEntityGuid], idof[Endpoint.JmsHub.Client:pipelineEntityPid], idof[Endpoint.JmsHub.Client:extGuid]),
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

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:setPipeline-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:setPipeline")
	@Deprecated
	public published  void setPipeline (org.radixware.ads.ServiceBus.server.IPipeline pipeline) {
		pipelineEntityGuid = Str.valueOf(pipeline.getHolderEntityPid().EntityId);
		pipelineEntityPid = Str.valueOf(pipeline.getHolderEntityPid());
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:refreshCache-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:refreshCache")
	@Deprecated
	public published  void refreshCache () {
		traceLevel = null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceDebugXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceDebugXml")
	@Deprecated
	public published  void traceDebugXml (Str mess, org.apache.xmlbeans.XmlObject data, boolean sensitive) {
		trace(Arte::EventSeverity:Debug, (mess != null ? mess + ": " : "") + (data == null ? "null" : data.toString()), sensitive);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:onRequest-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:onRequest")
	@Deprecated
	public published  org.radixware.schemas.jmshandler.MessageDocument onRequest (org.radixware.schemas.jmshandler.MessageDocument.Message mess) {
		traceError("Uncorrelated request: " + (mess == null ? "null" : new Str(mess.Data)));
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:import-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:import")
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
		    isClient = true;
		    jmsMessFormat = xJmsHub.JmsMessFormat;
		    jmsConnectProps = (Clob)org.radixware.kernel.server.utils.SrvValAsStr.fromStr(Arte::Arte.getInstance(), xJmsHub.JmsConnectProps, Meta::ValType:CLOB);
		    jmsMessProps = (Clob)org.radixware.kernel.server.utils.SrvValAsStr.fromStr(Arte::Arte.getInstance(), xJmsHub.JmsMessProps, Meta::ValType:CLOB);
		    jmsLogin = xJmsHub.JmsLogin;
		    jmsPassword = xJmsHub.JmsPassword;
		    msRqQueueName = xJmsHub.MsRqQueueName;
		    msRsQueueName = xJmsHub.MsRsQueueName;
		    inSeanceCnt = xJmsHub.InSeanceCnt;
		    outSeanceCnt = xJmsHub.OutSeanceCnt;
		    rsTimeout = xJmsHub.RsTimeout;
		}
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getLastInRqData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getLastInRqData")
	@Deprecated
	public published  org.apache.xmlbeans.XmlObject getLastInRqData () {
		return inRqData;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getLastInRsData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getLastInRsData")
	@Deprecated
	public published  org.apache.xmlbeans.XmlObject getLastInRsData () {
		return inRsData;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getLastOutRqData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getLastOutRqData")
	@Deprecated
	public published  org.apache.xmlbeans.XmlObject getLastOutRqData () {
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getLastOutRsData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:getLastOutRsData")
	@Deprecated
	public published  org.apache.xmlbeans.XmlObject getLastOutRsData () {
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:afterImport-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:afterImport")
	@Deprecated
	public published  void afterImport (java.util.Map<org.radixware.kernel.server.types.Pid,org.radixware.kernel.server.types.Pid> pids) {
		// do nothing
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:beforeUpdate")
	@Deprecated
	protected published  boolean beforeUpdate () {
		onUpdate();
		return super.beforeUpdate();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:beforeDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:beforeDelete")
	@Deprecated
	protected published  boolean beforeDelete () {
		onUpdate();
		return super.beforeDelete();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:beforeCreate")
	@Deprecated
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		onUpdate();
		return super.beforeCreate(src);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:afterUpdatePropObject-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:afterUpdatePropObject")
	@Deprecated
	protected published  void afterUpdatePropObject (org.radixware.kernel.common.types.Id propId, org.radixware.kernel.server.types.Entity propVal) {
		onUpdate();
		super.afterUpdatePropObject(propId, propVal);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:onUpdate-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:onUpdate")
	@Deprecated
	public published  void onUpdate () {
		getPipeline().onUpdate();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:setExtGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:setExtGuid")
	@Deprecated
	public published  void setExtGuid (Str extGuid) {
		extGuid = extGuid;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:geExtGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:geExtGuid")
	@Deprecated
	public published  Str geExtGuid () {
		if (extGuid == null)
		    extGuid = Arte::Arte.generateGuid();
		return extGuid;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:afterInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:afterInit")
	@Deprecated
	protected published  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		super.afterInit(src, phase);
		if (extGuid == null || (src != null && ((Endpoint.JmsHub.Client)src).extGuid == extGuid))
		    extGuid = Arte::Arte.generateGuid();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:isSuitableForPipeline-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:isSuitableForPipeline")
	@Deprecated
	public published  boolean isSuitableForPipeline (org.radixware.ads.ServiceBus.server.IPipeline pipeline) {
		return false;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client - Server Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.JmsHub.Client_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUDFPMHRIQBEIZBFXIMONO7PR6Y"),"Endpoint.JmsHub.Client",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKRKWPQFUW5DU5G2Z27DBLNVTSQ"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUDFPMHRIQBEIZBFXIMONO7PR6Y"),
							/*Owner Class Name*/
							"Endpoint.JmsHub.Client",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKRKWPQFUW5DU5G2Z27DBLNVTSQ"),
							/*Property presentations*/

							/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruCOWZSAI5ZFB5LP5AFTLEBFH5OM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityPid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruAIE4OFJHRZH33MVTGNRNJ7FEAA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXVQOH2LBP5CEFAFZ2O5Z5UNS7A"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceLevel:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4NAYNRQLF5AYFCTJBFF3OX5GKE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRqData:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdP5MMYF6VZNEZBLTX3VQFS742CI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRsData:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXIEGKU6AYZGSBAGK52A2ID22BM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:extGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruI6BAGJSKV5EEZFJXRREWAF4WXE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
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
									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5DF5K5OUWVFUZIM7W4DSYYVCQM"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJV6IPHCDKBFMJHMMQLL2HSSSLE"),
									35888,
									null,

									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprL42WDH4R5FFV3IUQQIAUF5DT2Y"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5DF5K5OUWVFUZIM7W4DSYYVCQM"),
									39984,
									null,

									/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJV6IPHCDKBFMJHMMQLL2HSSSLE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRADZIYXO75DFDKFEM2XQPRSQOE")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5DF5K5OUWVFUZIM7W4DSYYVCQM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprL42WDH4R5FFV3IUQQIAUF5DT2Y")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							null,
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityGuid-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruCOWZSAI5ZFB5LP5AFTLEBFH5OM"),"pipelineEntityGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT3FVM7LXLJGQLCADOZPDRC37FM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityPid-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruAIE4OFJHRZH33MVTGNRNJ7FEAA"),"pipelineEntityPid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS4B4VWNCEJCTVOHYF4TKYGUQSM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXVQOH2LBP5CEFAFZ2O5Z5UNS7A"),"pipelineEntityTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6RLBF5BEYBBGJIYI3N4LH2ZG5M"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceLevel-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4NAYNRQLF5AYFCTJBFF3OX5GKE"),"traceLevel",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:isClient-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWAQMCEU35BG3TP4YDKKTVOE2LU"),"isClient",null,org.radixware.kernel.common.enums.EValType.BOOL,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refKCN6JHUWCRA5DIGFJONLLABUJA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colA2PFHZGUNRBXPPOKMGSZI2W4CI"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRqData-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdP5MMYF6VZNEZBLTX3VQFS742CI"),"inRqData",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRsData-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXIEGKU6AYZGSBAGK52A2ID22BM"),"inRsData",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:extGuid-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruI6BAGJSKV5EEZFJXRREWAF4WXE"),"extGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7JXSRN7HJNANHAN7CZOA2VSTJU"),"checkTraceLevel",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("severity",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3WO7RGIMA5AGPM3FLOPTE2MGIY"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNLROOCGTF5CUVM66WIOJDRSHCE"),"trace",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("severity",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7X7MNAYXOJE3HM6VXWGT6KI2U4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprE6UHW6ZNNBFIZAJ3WG4K6PQSKA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sensitive",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4DFJOUIZM5FR7ORNXI2CDHARYE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWQC7HI6DMJB33MVZT5VNNK6LR4"),"traceDebug",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMFZV7WBHBFGNXJFKR5CFKNTC3E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sensitive",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCEGO4CVNRRGRRIH4L5EEKZJEWQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQ7MQNDFPEVCSZM46CIFMM4ISAI"),"traceError",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIJ457VIIIFHCNKFVJABWHQHAQ4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNY56F4MVVVAABKIBM7GQ52OD5U"),"traceEvent",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWMURKRIPCFDJHMRF2YMBQYWWUY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOYF3L7CFTFB27GLSUU7QWQ44PM"),"traceWarning",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIUZXLGPJDVFT3J3MKJEPZZLEMY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTO3N6ZUC25BNLNVHR6WUL3OSCU"),"afterResponseDelivered",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rs",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprV6K3OL52Q5GI3MZKEXVO7BZZOA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOC7IFQSQQNCXLAY4YULNTEVVL4"),"checkConfig",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRJJVN7SZFRGFPKSWMK5OODDPHM"),"connect",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("outConnectorRole",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIK7DUKQICBG5FCUNRFCBCFCOSE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("inConnectorId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKT3XA7BPIFE6BF5EJGOPKQ446Q"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZEZONIMUSNHEJHOFQ7OPMP3CLU"),"getConnectorIconId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprX6JHNWSTSFB2BE74OBAT3IXD2Y"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFST4X6BGFNA5PH6LQNOG73MSTQ"),"getConnectorId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRGP2QSRBDFHVVMTBSDA6T5ZP5U"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWE5I24A36RE3BGGGIIG4FYAV7M"),"getConnectorRole",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprALIPD7JIPNAC7BVSLM2J74YPRQ"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEIEXINMQGJB35G65UDWFOOSDZU"),"getConnectorRqDataType",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprD47EBT5IQJBAVAMNN4XVTUQ2XI"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHZIY6G7TCVCX3DR6QWA77MJJHI"),"getConnectorRsDataType",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWSWX4BZ25NA3PKPONM72VAD6P4"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthB25WTYWZUJAJXMCSH7UVSSJMP4"),"getConnectorSide",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprB6TANLUBNRDYNJLLTJQ237FRTU"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLB23XTDDKVGSRN5R4BPLLXUMFY"),"getConnectorTitle",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNNL2LISXSZD2DJF7AM5Y6S64WA"))
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

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("name",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZVOPLD3FSVFPXAIZVL47WBPBG4"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAXJJZNLFOJGVBNHZN5AEP5GJGM"),"getOrderPos",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7MEKN6RYYBASBPAYYLM556VGPM"),"processRequest",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCZY65DIDVNHQFGIXASZS5YGLWY"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSJVD663SIFFXVLMYDV2Y2VU544"),"export",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthG2W65RASFFH7RIEA37LOBPKUFI"),"setPipeline",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pipeline",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIBFS2UUSBNA67FLBGBCHQ3KX5Y"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthM7KLX4PIANEH3NYQZZSZ55GMIE"),"refreshCache",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7WQS73OLXNHIFAN2RWVICOSG6I"),"traceDebugXml",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIYSQGYI4ZNFPRAWR3ZQJAVPJAI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3QOZ4CQ6UFABTEEDTQQUNMPKFI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sensitive",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXKAWFGVWOBC6VHMO4M5FDBGBDY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5QSCSEQ3ZJFXVKMUX7VRMSKGLQ"),"onRequest",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNB72QLSEMBDFVL5FV5LLWZZX3A"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKMN6A6YHPVBNFON237GIJY4ZPU"),"import",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xNode",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7HZFU4JPMBHZPCQCW7E3VZS5N4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("overwriteLocalSettings",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLZ4CAMFC4ZEV3CGVTXBWVM73TI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2YXNVDGZ3ZGV7EEAGPN3X7NTMM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNYO5JRURNRFWLFFLCVODWPOTLI"),"getLastInRqData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6XF3CCTG7NGA7O7SEKOQTWLIHE"),"getLastInRsData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLC5CACZEKVCFHEZPWGJ7AN732I"),"getLastOutRqData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6D7TO5XHNRCCZMYQJQZMIWCSUQ"),"getLastOutRsData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBXD5LT3S2VA2ZGTSBZIOEVFN2I"),"afterImport",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pids",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLLK2QM2HLRABNDOVW5WYR472II"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNZ5Z7AS6RFRPIX6ZD5JDKEC34"),"beforeDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4WAS6UYMC5FP5AIDBVGCEZ46IQ"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAZ2HAURWXVB53HEDENC67YLQAU"),"afterUpdatePropObject",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPBCJWIV6YFHTDOWCBXD7AIAM6I")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propVal",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOE7ISWMNL5ATJFTSTRYBM4APCA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7BTYKNU7LRHXFGL2SWO3JQ3NJY"),"onUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZVCMFP66RZEELGIQECRMCICA3E"),"setExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr43V7HOW66BE4BBCISH3ASTIUW4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthW4LHL5WOQJF7NK3VL3LMQETFTI"),"geExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZVGQU7YFGRCF5OMPOBGH3TXQO4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVRUFWOZOPBDOHJ72FX6MZYL7VA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNSQ7CEKCGFFWRLVESA72LZ7DEM"),"isSuitableForPipeline",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pipeline",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGEMHA2GSWRCZ5BHYKYEXULBL5U"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("refKCN6JHUWCRA5DIGFJONLLABUJA")},
						null,null,null,true);
}

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client - Desktop Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client")
public interface Endpoint.JmsHub.Client   extends org.radixware.ads.Jms.explorer.Unit.JmsHandler  {































































































































	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceLevel:traceLevel-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceLevel:traceLevel")
		@Deprecated
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceLevel:traceLevel")
		@Deprecated
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public TraceLevel getTraceLevel();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRqData:inRqData-Presentation Property*/


	public class InRqData extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public InRqData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRqData:inRqData")
		@Deprecated
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRqData:inRqData")
		@Deprecated
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public InRqData getInRqData();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRsData:inRsData-Presentation Property*/


	public class InRsData extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public InRsData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRsData:inRsData")
		@Deprecated
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRsData:inRsData")
		@Deprecated
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public InRsData getInRsData();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityTitle:pipelineEntityTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityTitle:pipelineEntityTitle")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityTitle:pipelineEntityTitle")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityTitle getPipelineEntityTitle();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityPid:pipelineEntityPid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityPid:pipelineEntityPid")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityPid:pipelineEntityPid")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityPid getPipelineEntityPid();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityGuid:pipelineEntityGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityGuid:pipelineEntityGuid")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityGuid:pipelineEntityGuid")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityGuid getPipelineEntityGuid();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:extGuid:extGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:extGuid:extGuid")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:extGuid:extGuid")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtGuid getExtGuid();


}

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.JmsHub.Client_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUDFPMHRIQBEIZBFXIMONO7PR6Y"),
			"Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKRKWPQFUW5DU5G2Z27DBLNVTSQ"),null,null,0,

			/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruCOWZSAI5ZFB5LP5AFTLEBFH5OM"),
						"pipelineEntityGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT3FVM7LXLJGQLCADOZPDRC37FM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUDFPMHRIQBEIZBFXIMONO7PR6Y"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruAIE4OFJHRZH33MVTGNRNJ7FEAA"),
						"pipelineEntityPid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS4B4VWNCEJCTVOHYF4TKYGUQSM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUDFPMHRIQBEIZBFXIMONO7PR6Y"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXVQOH2LBP5CEFAFZ2O5Z5UNS7A"),
						"pipelineEntityTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6RLBF5BEYBBGJIYI3N4LH2ZG5M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUDFPMHRIQBEIZBFXIMONO7PR6Y"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceLevel:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4NAYNRQLF5AYFCTJBFF3OX5GKE"),
						"traceLevel",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUDFPMHRIQBEIZBFXIMONO7PR6Y"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceLevel:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRqData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdP5MMYF6VZNEZBLTX3VQFS742CI"),
						"inRqData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUDFPMHRIQBEIZBFXIMONO7PR6Y"),
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

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRsData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXIEGKU6AYZGSBAGK52A2ID22BM"),
						"inRsData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUDFPMHRIQBEIZBFXIMONO7PR6Y"),
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

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:extGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruI6BAGJSKV5EEZFJXRREWAF4WXE"),
						"extGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUDFPMHRIQBEIZBFXIMONO7PR6Y"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:extGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
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
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5DF5K5OUWVFUZIM7W4DSYYVCQM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprL42WDH4R5FFV3IUQQIAUF5DT2Y")},
			true,true,false);
}

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client - Web Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client")
public interface Endpoint.JmsHub.Client   extends org.radixware.ads.Jms.web.Unit.JmsHandler  {































































































































	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceLevel:traceLevel-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceLevel:traceLevel")
		@Deprecated
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceLevel:traceLevel")
		@Deprecated
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public TraceLevel getTraceLevel();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRqData:inRqData-Presentation Property*/


	public class InRqData extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public InRqData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRqData:inRqData")
		@Deprecated
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRqData:inRqData")
		@Deprecated
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public InRqData getInRqData();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRsData:inRsData-Presentation Property*/


	public class InRsData extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public InRsData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRsData:inRsData")
		@Deprecated
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRsData:inRsData")
		@Deprecated
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public InRsData getInRsData();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityTitle:pipelineEntityTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityTitle:pipelineEntityTitle")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityTitle:pipelineEntityTitle")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityTitle getPipelineEntityTitle();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityPid:pipelineEntityPid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityPid:pipelineEntityPid")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityPid:pipelineEntityPid")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityPid getPipelineEntityPid();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityGuid:pipelineEntityGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityGuid:pipelineEntityGuid")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityGuid:pipelineEntityGuid")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityGuid getPipelineEntityGuid();
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:extGuid:extGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:extGuid:extGuid")
		@Deprecated
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:extGuid:extGuid")
		@Deprecated
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtGuid getExtGuid();


}

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.JmsHub.Client_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUDFPMHRIQBEIZBFXIMONO7PR6Y"),
			"Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMU4BLUT6FJCZLDQ2WL6SCH43NE"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKRKWPQFUW5DU5G2Z27DBLNVTSQ"),null,null,0,

			/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruCOWZSAI5ZFB5LP5AFTLEBFH5OM"),
						"pipelineEntityGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT3FVM7LXLJGQLCADOZPDRC37FM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUDFPMHRIQBEIZBFXIMONO7PR6Y"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruAIE4OFJHRZH33MVTGNRNJ7FEAA"),
						"pipelineEntityPid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS4B4VWNCEJCTVOHYF4TKYGUQSM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUDFPMHRIQBEIZBFXIMONO7PR6Y"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXVQOH2LBP5CEFAFZ2O5Z5UNS7A"),
						"pipelineEntityTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6RLBF5BEYBBGJIYI3N4LH2ZG5M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUDFPMHRIQBEIZBFXIMONO7PR6Y"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:pipelineEntityTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceLevel:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4NAYNRQLF5AYFCTJBFF3OX5GKE"),
						"traceLevel",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUDFPMHRIQBEIZBFXIMONO7PR6Y"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:traceLevel:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRqData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdP5MMYF6VZNEZBLTX3VQFS742CI"),
						"inRqData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUDFPMHRIQBEIZBFXIMONO7PR6Y"),
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

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:inRsData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXIEGKU6AYZGSBAGK52A2ID22BM"),
						"inRsData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUDFPMHRIQBEIZBFXIMONO7PR6Y"),
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

					/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:extGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruI6BAGJSKV5EEZFJXRREWAF4WXE"),
						"extGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUDFPMHRIQBEIZBFXIMONO7PR6Y"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:extGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
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

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5DF5K5OUWVFUZIM7W4DSYYVCQM"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJV6IPHCDKBFMJHMMQLL2HSSSLE"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUDFPMHRIQBEIZBFXIMONO7PR6Y"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD4HTBE62IJBGZKJHOGIURVYQ5E"),
	null,

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Settings-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgCSEXGV454FGTVDPBTUAL7ZM2HY"),"Settings",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUDFPMHRIQBEIZBFXIMONO7PR6Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVMASPCA4Z5A7TJ7VBQMWBHETRA"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBC6VGIU6S5CFNAN6V4PG3DKCUQ"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO77HLWLCFFFWVIKV7MKDDEZY5A"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA2LTDQXERRBJ5HAYLJT3KMSBEY"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2YNMUHO7AJCVTMJW2K5OPQ3GQQ"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC5VS2BZB3RAHFNHR5SWKU5LBA4"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRH4YQQJF4NCVNIMAN3HXIQSWLY"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col37TNUN2QIVAWJE2HLAOG6OY4YA"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKFSA4DVNEZEWFLARG7YYJFLVSE"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCJFJTBWSSJH55NNIHQSUHEWUDY"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXVQOH2LBP5CEFAFZ2O5Z5UNS7A"),0,0,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOCDVZAOTBXOBDNTPAAMPGXSZKU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgCSEXGV454FGTVDPBTUAL7ZM2HY")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgKYIKI4FJUFFNVLRYCIE7H5KCVM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUJB2RCH6KZHD5IMTQTE76DQSZA")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgJ4BHY7HBLBBBXIIQZEPCKTW2MU"))}
	,

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	35888,0,0);
}
/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.web;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5DF5K5OUWVFUZIM7W4DSYYVCQM"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJV6IPHCDKBFMJHMMQLL2HSSSLE"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUDFPMHRIQBEIZBFXIMONO7PR6Y"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD4HTBE62IJBGZKJHOGIURVYQ5E"),
	null,

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Editor Pages-Editor Presentation Pages*/
	null,
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
	}
	,

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	35888,0,0);
}
/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model - Desktop Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model")
public class Edit:Model  extends org.radixware.ads.ServiceBus.Nodes.explorer.Endpoint.JmsHub.Client.Endpoint.JmsHub.Client_DefaultModel.eprJV6IPHCDKBFMJHMMQLL2HSSSLE_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model:pipelineEntityTitle-Presentation Property*/




	public class PipelineEntityTitle extends org.radixware.ads.ServiceBus.Nodes.explorer.Endpoint.JmsHub.Client.Endpoint.JmsHub.Client_DefaultModel.eprJV6IPHCDKBFMJHMMQLL2HSSSLE_ModelAdapter.prdXVQOH2LBP5CEFAFZ2O5Z5UNS7A{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model:pipelineEntityTitle")
		public  Str getValue() {
			return Value;
		}
		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model:pipelineEntityTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityTitle getPipelineEntityTitle(){return (PipelineEntityTitle)getProperty(prdXVQOH2LBP5CEFAFZ2O5Z5UNS7A);}








	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		pipelineEntityTitle.setVisible(!(getContext() instanceof ServiceBus::ISbContext));
		super.beforeOpenView();
	}


}

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aem5DF5K5OUWVFUZIM7W4DSYYVCQM"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemJV6IPHCDKBFMJHMMQLL2HSSSLE"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model - Web Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model")
public class Edit:Model  extends org.radixware.ads.ServiceBus.Nodes.web.Endpoint.JmsHub.Client.Endpoint.JmsHub.Client_DefaultModel.eprJV6IPHCDKBFMJHMMQLL2HSSSLE_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model:pipelineEntityTitle-Presentation Property*/




	public class PipelineEntityTitle extends org.radixware.ads.ServiceBus.Nodes.web.Endpoint.JmsHub.Client.Endpoint.JmsHub.Client_DefaultModel.eprJV6IPHCDKBFMJHMMQLL2HSSSLE_ModelAdapter.prdXVQOH2LBP5CEFAFZ2O5Z5UNS7A{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model:pipelineEntityTitle")
		public  Str getValue() {
			return Value;
		}
		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model:pipelineEntityTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityTitle getPipelineEntityTitle(){return (PipelineEntityTitle)getProperty(prdXVQOH2LBP5CEFAFZ2O5Z5UNS7A);}








	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		pipelineEntityTitle.setVisible(!(getContext() instanceof ServiceBus::ISbContext));
		super.beforeOpenView();
	}


}

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aem5DF5K5OUWVFUZIM7W4DSYYVCQM"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemJV6IPHCDKBFMJHMMQLL2HSSSLE"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Edit:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprL42WDH4R5FFV3IUQQIAUF5DT2Y"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5DF5K5OUWVFUZIM7W4DSYYVCQM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUDFPMHRIQBEIZBFXIMONO7PR6Y"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW6XZODKIOFF6HCXEYP7IGC42OE"),
	null,
	null,
	null,

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	39984,0,0);
}
/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.web;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprL42WDH4R5FFV3IUQQIAUF5DT2Y"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5DF5K5OUWVFUZIM7W4DSYYVCQM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUDFPMHRIQBEIZBFXIMONO7PR6Y"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW6XZODKIOFF6HCXEYP7IGC42OE"),
	null,
	null,
	null,

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	39984,0,0);
}
/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Model - Desktop Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Model")
public class Create:Model  extends org.radixware.ads.ServiceBus.Nodes.explorer.Edit:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Model:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Model:beforeOpenView")
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

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Model - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemL42WDH4R5FFV3IUQQIAUF5DT2Y"),
						"Create:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aem5DF5K5OUWVFUZIM7W4DSYYVCQM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Model - Web Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Model")
public class Create:Model  extends org.radixware.ads.ServiceBus.Nodes.web.Edit:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Model:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Model:beforeOpenView")
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

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Model - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemL42WDH4R5FFV3IUQQIAUF5DT2Y"),
						"Create:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aem5DF5K5OUWVFUZIM7W4DSYYVCQM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client:Create:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Endpoint.JmsHub.Client - Localizing Bundle */
package org.radixware.ads.ServiceBus.Nodes.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.JmsHub.Client - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pipeline");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конвейер");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6RLBF5BEYBBGJIYI3N4LH2ZG5M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"JMS - Client");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"JMS - клиент");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD4HTBE62IJBGZKJHOGIURVYQ5E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"JMS - Client");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"JMS - клиент");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKRKWPQFUW5DU5G2Z27DBLNVTSQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Outbound requests");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Исходящие запросы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN72ZUIDFKVAMTD32S4F64RF4VU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Entity PID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ключ сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS4B4VWNCEJCTVOHYF4TKYGUQSM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Entity ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT3FVM7LXLJGQLCADOZPDRC37FM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Settings");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Настройки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVMASPCA4Z5A7TJ7VBQMWBHETRA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"JMS - Client");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"JMS - клиент");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW6XZODKIOFF6HCXEYP7IGC42OE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Endpoint.JmsHub.Client - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclUDFPMHRIQBEIZBFXIMONO7PR6Y"),"Endpoint.JmsHub.Client - Localizing Bundle",$$$items$$$);
}
