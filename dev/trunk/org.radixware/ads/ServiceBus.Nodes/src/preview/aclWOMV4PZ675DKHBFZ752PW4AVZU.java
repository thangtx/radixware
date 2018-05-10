
/* Radix::ServiceBus.Nodes::Endpoint.Net.Client - Server Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Client-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.server;

import org.radixware.schemas.netporthandler.*;
import org.radixware.schemas.netporthandlerWsdl.*;
import java.util.*;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client")
public published class Endpoint.Net.Client  extends org.radixware.ads.Net.server.NetClient  implements org.radixware.ads.ServiceBus.server.IPipelineNode,org.radixware.kernel.server.net.NetProtocolHandler,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private static final int NETPORT_SERVICE_TIMEOUT = 120;
	private static final int NETPORT_SERVICE_KEEP_CONNECTIONTIME = 20;
	private final static Str IN = "In";
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Endpoint.Net.Client_mi.rdxMeta;}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityGuid-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityGuid")
	public published  Str getPipelineEntityGuid() {
		return pipelineEntityGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityGuid")
	public published   void setPipelineEntityGuid(Str val) {
		pipelineEntityGuid = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityPid-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityPid")
	public published  Str getPipelineEntityPid() {
		return pipelineEntityPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityPid")
	public published   void setPipelineEntityPid(Str val) {
		pipelineEntityPid = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityTitle-Dynamic Property*/



	protected Str pipelineEntityTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityTitle")
	public published  Str getPipelineEntityTitle() {

		if (pipelineEntityGuid == null || pipelineEntityPid == null)
		    return null;
		return ServiceBus::SbServerUtil.getEntityTitle(
		    new Pid(Arte, Types::Id.Factory.loadFrom(pipelineEntityGuid), pipelineEntityPid)
		    );
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityTitle")
	public published   void setPipelineEntityTitle(Str val) {
		pipelineEntityTitle = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:rqType-Dynamic Property*/



	protected Str rqType=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:rqType")
	public published  Str getRqType() {

		return getConnectorRqDataType(IN);
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:rqType")
	public published   void setRqType(Str val) {
		rqType = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:rsType-Dynamic Property*/



	protected Str rsType=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:rsType")
	public published  Str getRsType() {

		return getConnectorRsDataType(IN);
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:rsType")
	public published   void setRsType(Str val) {
		rsType = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:netPortHandler-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:netPortHandler")
	public published  org.radixware.ads.System.server.Unit getNetPortHandler() {
		return netPortHandler;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:netPortHandler")
	public published   void setNetPortHandler(org.radixware.ads.System.server.Unit val) {
		netPortHandler = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:toKeepConnectFunc-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:toKeepConnectFunc")
	public published  org.radixware.ads.ServiceBus.Nodes.server.UserFunc.KeepConnect getToKeepConnectFunc() {
		return toKeepConnectFunc;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:toKeepConnectFunc")
	public published   void setToKeepConnectFunc(org.radixware.ads.ServiceBus.Nodes.server.UserFunc.KeepConnect val) {
		toKeepConnectFunc = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:connectTimeout-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:connectTimeout")
	public published  Int getConnectTimeout() {
		return connectTimeout;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:connectTimeout")
	public published   void setConnectTimeout(Int val) {
		connectTimeout = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceLevel-Dynamic Property*/



	protected Int traceLevel=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceLevel")
	private final  Int getTraceLevel() {
		return traceLevel;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceLevel")
	private final   void setTraceLevel(Int val) {
		traceLevel = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRqData-Dynamic Property*/



	protected org.apache.xmlbeans.XmlObject inRqData=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRqData")
	protected published  org.apache.xmlbeans.XmlObject getInRqData() {
		return inRqData;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRqData")
	protected published   void setInRqData(org.apache.xmlbeans.XmlObject val) {
		inRqData = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRsData-Dynamic Property*/



	protected org.apache.xmlbeans.XmlObject inRsData=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRsData")
	protected published  org.apache.xmlbeans.XmlObject getInRsData() {
		return inRsData;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRsData")
	protected published   void setInRsData(org.apache.xmlbeans.XmlObject val) {
		inRsData = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:extGuid-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:extGuid")
	public published  Str getExtGuid() {
		return extGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:extGuid")
	public published   void setExtGuid(Str val) {
		extGuid = val;
	}





































































































	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:afterResponseDelivered-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:afterResponseDelivered")
	public published  void afterResponseDelivered (org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRs rq) throws org.radixware.ads.ServiceBus.server.PipelineException {

	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:checkConfig-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:checkConfig")
	public published  void checkConfig () throws org.radixware.ads.ServiceBus.server.PipelineConfigException {
		if (toKeepConnectFunc != null && toKeepConnectFunc.isValid != Bool.TRUE)
		    throw new PipelineConfigException("Invalid \"Keep connection timeout\" function", this);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:connect-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:connect")
	public published  void connect (Str outConnectorRole, Str inConnectorId) {
		throw new AppError("Unknown role " + outConnectorRole);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getConnectorIconId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getConnectorIconId")
	public published  org.radixware.kernel.common.types.Id getConnectorIconId (Str role) {
		if (role == IN)
		    return idof[ServiceBus::next];
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getConnectorId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getConnectorId")
	public published  Str getConnectorId (Str role) {
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getConnectorRole-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getConnectorRole")
	public published  Str getConnectorRole (Str id) {
		throw new AppError("Unknown id " + id);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getConnectorRqDataType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getConnectorRqDataType")
	public published  Str getConnectorRqDataType (Str role) {
		if (role == IN)
		    return BINARY_TYPE;
		throw new AppError("Unknown role " + role);

	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getConnectorRsDataType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getConnectorRsDataType")
	public published  Str getConnectorRsDataType (Str role) {
		if (role == IN)
		    return BINARY_TYPE;
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getConnectorTitle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getConnectorTitle")
	public published  Str getConnectorTitle (Str role) {
		if (role == IN)
		    return "Outbound requests";
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getDescription-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getDescription")
	public published  Str getDescription () {
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getHolderEntityPid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getHolderEntityPid")
	public published  org.radixware.kernel.server.types.Pid getHolderEntityPid () {
		return getPid();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getIconId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getIconId")
	public published  org.radixware.kernel.common.types.Id getIconId () {
		return idof[NetClient];
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getInConnectorRoles-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getInConnectorRoles")
	public published  Str[] getInConnectorRoles () {
		return new Str[] { IN };
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getOutConnectorRoles-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getOutConnectorRoles")
	public published  Str[] getOutConnectorRoles () {
		return new Str[] {};
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getPipeline-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getPipeline")
	public published  org.radixware.ads.ServiceBus.server.IPipeline getPipeline () {
		return ServiceBus::SbServerUtil.getPipeline(new Pid(Arte, Types::Id.Factory.loadFrom(pipelineEntityGuid), pipelineEntityPid));
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getTitle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getTitle")
	public published  Str getTitle () {
		return title;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getNetProtocolHandler-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getNetProtocolHandler")
	protected published  org.radixware.kernel.server.net.NetProtocolHandler getNetProtocolHandler () {
		return this;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:processRequest-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:processRequest")
	public published  org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRs processRequest (org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRq rq) throws org.radixware.ads.ServiceBus.server.PipelineException {
		final ConnectDocument doc = ConnectDocument.Factory.newInstance();
		final ConnectRq cRq = doc.addNewConnect().addNewConnectRq();
		cRq.setClientId(id);
		cRq.setCallbackPid(String.valueOf(id));
		cRq.setCallbackWid(String.valueOf(0));
		if (rq.OutExtHeader.isSetNet()) {
		    cRq.setLocalAddress(rq.OutExtHeader.Net.ClientAddress);
		    cRq.setRemoteAddress(rq.OutExtHeader.Net.ServerAddress);
		}
		cRq.setIsRecvSync(true);
		cRq.setRecvTimeout(recvTimeout);
		cRq.setConnectTimeout(connectTimeout);
		if (rq.OutExtHeader.HttpHeader != null) 
		    cRq.SendPackedHeaders = (Arte::TypesXsd:MapStrStr)rq.OutExtHeader.HttpHeader.copy();
		cRq.setSendPacket(ServiceBus::SbServerUtil.castToBytes(rq.CurStage.Data));
		traceDebug("Send data: " + (cRq.SendPacket == null ? "" : new Str(cRq.SendPacket)), true);
		inRqData = ServiceBus::SbCommonUtil.copyXml(rq.CurStage.Data);

		/*
		if (rq.CurStage.Data instanceof ) 
		    cRq.setSendPacket((()rq.CurStage.Data).ByteArrayValue);
		else if (rq.CurStage.Data instanceof ) 
		    cRq.setSendPacket((()rq.CurStage.Data).ByteArrayValue);
		*/
		try {
		    doc.set(Arte::Arte.invokeInternalService(doc, ConnectDocument.class, ((Net::Unit.NetPortHandler)netPortHandler).getUri(), NETPORT_SERVICE_KEEP_CONNECTIONTIME, NETPORT_SERVICE_TIMEOUT, null));
		    Int closeDelay = keepConnectTimeout;
		    if (toKeepConnectFunc != null)
		        closeDelay = toKeepConnectFunc.calcKeepConnect(this, cRq.getSendPacket(), doc.getConnect().getConnectRs().ReceivedPacket);
		    if (closeDelay != null) {
		        final CloseDocument closeDoc = CloseDocument.Factory.newInstance();
		        final CloseRq closeRq = closeDoc.addNewClose().addNewCloseRq();
		        closeRq.setSID(doc.getConnect().getConnectRs().SID);
		        closeRq.setCloseDelay(closeDelay);
		        Arte::Arte.invokeInternalService(closeDoc, CloseDocument.class, ((Net::Unit.NetPortHandler)netPortHandler).getUri(), NETPORT_SERVICE_KEEP_CONNECTIONTIME, NETPORT_SERVICE_TIMEOUT, null);
		    }
		} catch(Exceptions::ServiceCallFault e) {
		    if (e.getFaultString() == Net::NetPortHandlerXsd:ExceptionEnum.CONNECT_TIMEOUT.toString()) {
		        traceError("Service call connect timeout");
		        throw new PipelineException.Timeout.Connect(e.getMessage(), this);
		    } else if (e.getFaultString() == Net::NetPortHandlerXsd:ExceptionEnum.RECEIVE_TIMEOUT.toString()) {
		        traceError("Service call receive timeout");
		        throw new PipelineException.Timeout.Receive(e.getMessage(), this);
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

		final ConnectRs cRs = doc.getConnect().getConnectRs();
		traceDebug("Received data: " + (cRs.ReceivedPacket == null ? "" : new Str(cRs.ReceivedPacket)), true);

		Xml xData = ServiceBus::SbServerUtil.castToBinBase64(cRs.ReceivedPacket);
		inRsData = ServiceBus::SbCommonUtil.copyXml(xData);

		final ServiceBus::SbXsd:PipelineMessageRs rs = ServiceBus::SbServerUtil.prepareNextResponse(null, rq.CurStage, xData);
		if (cRs.ReceivedPackedHeaders != null)
		    rs.InExtHeader.HttpHeader = (Arte::TypesXsd:MapStrStr)cRs.ReceivedPackedHeaders.copy();
		rs.InExtHeader.NodeEntityId = Str.valueOf(Pid.EntityId);
		rs.InExtHeader.NodeEntityPid = Str.valueOf(Pid);
		return rs;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getCreatingPresentationId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getCreatingPresentationId")
	public published  org.radixware.kernel.common.types.Id getCreatingPresentationId () {
		return idof[Endpoint.Net.Client:General];
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getEditingPresentationId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getEditingPresentationId")
	public published  org.radixware.kernel.common.types.Id getEditingPresentationId () {
		return idof[Endpoint.Net.Client:General];
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:onConnect-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:onConnect")
	public published  org.radixware.schemas.netporthandler.OnConnectRsDocument onConnect (Int channelId, Str serverAddr, Str clientAddr, Str clientCertCn, Str sid) throws org.radixware.kernel.common.exceptions.AppException {
		Net::NetPortHandlerXsd:OnConnectRsDocument doc = Net::NetPortHandlerXsd:OnConnectRsDocument.Factory.newInstance();
		Net::NetPortHandlerXsd:OnConnectRsDocument.OnConnectRs rs = doc.addNewOnConnectRs();
		rs.setCallbackPid(String.valueOf(id));
		rs.setCallbackWid(String.valueOf(0));
		rs.setSendPacket(sid.getBytes());
		rs.setRecvTimeout(recvTimeout);
		traceDebug("OnConnect: sendPacket = " + new String(rs.SendPacket) + ", callbackPid = " + rs.CallbackPid + ", callbackWid = " + rs.CallbackWid, true);
		return doc;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:onDisconnect-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:onDisconnect")
	public published  void onDisconnect (Int channelId, Str serverAddr, Str clientAddr, Str clientCertCn, Str sid, Str callbackPid, Str callbackWid) {
		traceDebug("OnDisconnect: sid = " + sid + ", callbackPid = " + callbackPid + ", callbackWid = " + callbackWid, false);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:onRecv-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:onRecv")
	public published  org.radixware.schemas.netporthandler.OnRecvRsDocument onRecv (Int channelId, Str serverAddr, Str clientAddr, Str clientCertCn, Str sid, Str callbackPid, Str callbackWid, java.util.Map<Str,Str> recvPacketHeaders, org.radixware.kernel.common.types.Bin recvPacket, Bool connected) throws org.radixware.kernel.common.exceptions.AppException {
		Net::NetPortHandlerXsd:OnRecvRsDocument recvRsDoc = Net::NetPortHandlerXsd:OnRecvRsDocument.Factory.newInstance();
		Net::NetPortHandlerXsd:OnRecvRsDocument.OnRecvRs recvRs = recvRsDoc.addNewOnRecvRs();
		recvRs.setCallbackPid(callbackPid);
		recvRs.setCallbackWid(callbackWid);
		recvRs.setRecvTimeout(recvTimeout);
		return recvRsDoc;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceDebug-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceDebug")
	public published  void traceDebug (Str mess, boolean sensitive) {
		trace(Arte::EventSeverity:Debug, mess, sensitive);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceError-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceError")
	public published  void traceError (Str mess) {
		trace(Arte::EventSeverity:Error, mess, false);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceEvent-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceEvent")
	public published  void traceEvent (Str mess) {
		trace(Arte::EventSeverity:Event, mess, false);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:trace-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:trace")
	public published  void trace (org.radixware.kernel.common.enums.EEventSeverity severity, Str mess, boolean sensitive) {
		if (!checkTraceLevel(severity))
		    return;

		Arte::Trace.enterContext(Arte::EventContextType:NetChannel, id);
		Object traceTargetHandler = Arte::Trace.addContextProfile(getPipeline().getTraceProfile(), (getPipeline() instanceof Types::Entity) ? ((Types::Entity) getPipeline()) : this);
		try {
		    if (sensitive/* && .intValue() != .Value.intValue()*/)
		        Arte::Trace.putSensitive(severity, mess, Arte::EventSource:ServiceBus);
		    else    
		        Arte::Trace.put(severity, mess, Arte::EventSource:ServiceBus);
		} finally {
		    Arte::Trace.leaveContext(Arte::EventContextType:NetChannel, id);
		    Arte::Trace.delContextProfile(traceTargetHandler);
		}
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceWarning-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceWarning")
	public published  void traceWarning (Str mess) {
		trace(Arte::EventSeverity:Warning, mess, false);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:onRecvTimeout-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:onRecvTimeout")
	public published  org.radixware.schemas.netporthandler.OnRecvTimeoutRsDocument onRecvTimeout (Int channelId, Str serverAddr, Str clientAddr, Str clientCertCn, Str sid, Str callbackPid, Str callbackWid) throws org.radixware.kernel.common.exceptions.AppException {
		Net::NetPortHandlerXsd:OnRecvTimeoutRsDocument doc = Net::NetPortHandlerXsd:OnRecvTimeoutRsDocument.Factory.newInstance();
		Net::NetPortHandlerXsd:OnRecvTimeoutRsDocument.OnRecvTimeoutRs rs = doc.addNewOnRecvTimeoutRs();
		rs.setCallbackPid(callbackWid);
		rs.setCallbackWid(callbackPid);
		rs.setRecvTimeout(recvTimeout);
		return doc;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getConnectorSide-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getConnectorSide")
	public published  org.radixware.ads.ServiceBus.common.Side getConnectorSide (Str role) {
		if (role == IN)
		    return ServiceBus::Side:Left;
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:checkTraceLevel-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:checkTraceLevel")
	public published  boolean checkTraceLevel (org.radixware.kernel.common.enums.EEventSeverity severity) {
		if (traceLevel == null)
		    traceLevel = Arte::Trace.getMinSeverity(Arte::EventSource:ServiceBus);

		if (severity == Arte::EventSeverity:Debug)
		    return traceLevel.intValue() == Arte::EventSeverity:Debug.Value.intValue();

		return traceLevel.intValue() <= severity.Value.intValue();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:onConnectTimeout-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:onConnectTimeout")
	public published  org.radixware.schemas.netporthandler.OnConnectTimeoutRsDocument onConnectTimeout (Int channelId, Str serverAddr, Str clientAddr, Str clientCertCn, Str sid, Str callbackPid, Str callbackWid) throws org.radixware.kernel.common.exceptions.AppException {
		Net::NetPortHandlerXsd:OnConnectTimeoutRsDocument doc = Net::NetPortHandlerXsd:OnConnectTimeoutRsDocument.Factory.newInstance();
		Net::NetPortHandlerXsd:OnConnectTimeoutRsDocument.OnConnectTimeoutRs rs = doc.addNewOnConnectTimeoutRs();
		rs.setConnectTimeout(null);
		return doc;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getOrderPos-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getOrderPos")
	public static published  org.radixware.ads.ServiceBus.common.SbPos getOrderPos () {
		return new SbPos(1.1);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getPipelineNodeParam-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getPipelineNodeParam")
	public published  Str getPipelineNodeParam (Str name) {
		final java.util.Map<Str,Str> params = getPipeline().getParams();
		if (params != null)
		    params.get(name);
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:export-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:export")
	public published  org.radixware.ads.ServiceBus.common.ImpExpXsd.Node export () {
		ServiceBus::ImpExpXsd:Node xNode = ServiceBus::ImpExpXsd:Node.Factory.newInstance();
		Common::CommonXsd:EditableProperties xProps = Common::CommonXsd:EditableProperties.Factory.newInstance();
		Common::CommonXsd:EditableProperty xProp = xProps.addNewItem();
		xProp.Id = idof[Endpoint.Net.Client:netPortHandler];
		//xProp.Value = .toStr(.(), , );
		final Explorer.Utils::SqmlExpression expr = Explorer.Utils::SqmlExpression.equal(
		    Explorer.Utils::SqmlExpression.this_property(idof[System::Unit], idof[System::Unit:type]),
		    Explorer.Utils::SqmlExpression.valueInt(System::UnitType:NetPortHandler.intValue())
		    );
		xProp.Condition = expr.asXsqml();
		if (netPortHandler != null && netPortHandler.rid != null) {
		    xProp.Rid = netPortHandler.rid;
		    xProp.EntityId = idof[System::Unit];
		} 

		ServiceBus::SbImpExpUtil.exportNode(xNode, this, extGuid, title,
		    Arrays.asList(idof[Endpoint.Net.Client:pipelineEntityGuid], idof[Endpoint.Net.Client:pipelineEntityPid], idof[Endpoint.Net.Client:extGuid]),
		    xProps
		    );

		ServiceBus::ImpExpXsd:NetChannel xChannel = ServiceBus::ImpExpXsd:NetChannel.Factory.newInstance();
		xChannel.Rid = rid;
		xChannel.Address = address;
		xChannel.LinkLevelProtocolKind = linkLevelProtocolKind;
		xChannel.RequestFrame = requestFrame;
		xChannel.ResponseFrame = responseFrame;
		xChannel.RecvTimeout = recvTimeout;
		xChannel.SendTimeout = sendTimeout;
		xChannel.KeepConnectTimeout = keepConnectTimeout;
		xChannel.MaxSessionCount = maxSessionCount;
		//xChannel.CurSessionCount = ;
		xChannel.GuiTraceProfile = guiTraceProfile;
		xChannel.FileTraceProfile = fileTraceProfile;
		xChannel.DbTraceProfile = dbTraceProfile;
		xChannel.SecurityProtocol = securityProtocol;
		xChannel.CheckClientCert = checkClientCert;
		if (serverKeyAliases != null)
		    xChannel.ServerKeyAliases = Str.valueOf(serverKeyAliases);
		if (clientCertAliases != null)
		    xChannel.ClientCertAliases = Str.valueOf(clientCertAliases);

		xNode.addNewExt().set(xChannel);
		return xNode;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:setPipeline-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:setPipeline")
	public published  void setPipeline (org.radixware.ads.ServiceBus.server.IPipeline pipeline) {
		pipelineEntityGuid = Str.valueOf(pipeline.getHolderEntityPid().EntityId);
		pipelineEntityPid = Str.valueOf(pipeline.getHolderEntityPid());
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:refreshCache-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:refreshCache")
	public published  void refreshCache () {
		traceLevel = null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceDebugXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceDebugXml")
	public published  void traceDebugXml (Str mess, org.apache.xmlbeans.XmlObject data, boolean sensitive) {
		trace(Arte::EventSeverity:Debug, (mess != null ? mess + ": " : "") + (data == null ? "null" : data.toString()), sensitive);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:import-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:import")
	public published  void import (org.radixware.ads.ServiceBus.common.ImpExpXsd.Node xNode, boolean overwriteLocalSettings, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		ServiceBus::SbImpExpUtil.importNode(xNode, this, overwriteLocalSettings, helper);

		ServiceBus::ImpExpXsd:NetChannel xChannel;
		try {
		    xChannel = ServiceBus::ImpExpXsd:NetChannel.Factory.parse(xNode.Ext.newInputStream());
		} catch (Exceptions::Exception e) {
		    throw new AppError(e.getMessage(), e);
		}

		linkLevelProtocolKind = xChannel.LinkLevelProtocolKind;
		requestFrame = xChannel.RequestFrame;
		responseFrame = xChannel.ResponseFrame;
		recvTimeout = xChannel.RecvTimeout;
		sendTimeout = xChannel.SendTimeout;
		keepConnectTimeout = xChannel.KeepConnectTimeout;
		rid = helper.importRid(this, Net::NetChannel.loadByRid(unitId, xChannel.Rid), xChannel.Rid);

		if (overwriteLocalSettings) {
		    title = xNode.Title;
		    address = xChannel.Address;
		    maxSessionCount = xChannel.MaxSessionCount;
		    // = xChannel.CurSessionCount;
		    guiTraceProfile = xChannel.GuiTraceProfile;
		    fileTraceProfile = xChannel.FileTraceProfile;
		    dbTraceProfile = xChannel.DbTraceProfile;
		    securityProtocol = xChannel.SecurityProtocol;
		    checkClientCert = xChannel.CheckClientCert;
		    serverKeyAliases = ArrStr.fromValAsStr(xChannel.ServerKeyAliases);
		    clientCertAliases = ArrStr.fromValAsStr(xChannel.ClientCertAliases);
		}
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:needCaching-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:needCaching")
	public published  boolean needCaching () {
		return false;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getLastInRqData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getLastInRqData")
	public published  org.apache.xmlbeans.XmlObject getLastInRqData () {
		return inRqData;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getLastInRsData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getLastInRsData")
	public published  org.apache.xmlbeans.XmlObject getLastInRsData () {
		return inRsData;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getLastOutRqData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getLastOutRqData")
	public published  org.apache.xmlbeans.XmlObject getLastOutRqData () {
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getLastOutRsData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getLastOutRsData")
	public published  org.apache.xmlbeans.XmlObject getLastOutRsData () {
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:afterImport-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:afterImport")
	public published  void afterImport (java.util.Map<org.radixware.kernel.server.types.Pid,org.radixware.kernel.server.types.Pid> pids) {
		// do nothing
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:beforeCreate")
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		onUpdate();
		return super.beforeCreate(src);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:beforeDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:beforeDelete")
	protected published  boolean beforeDelete () {
		onUpdate();
		return super.beforeDelete();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:beforeUpdate")
	protected published  boolean beforeUpdate () {
		onUpdate();
		return super.beforeUpdate();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:afterUpdatePropObject-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:afterUpdatePropObject")
	protected published  void afterUpdatePropObject (org.radixware.kernel.common.types.Id propId, org.radixware.kernel.server.types.Entity propVal) {
		onUpdate();
		super.afterUpdatePropObject(propId, propVal);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:onUpdate-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:onUpdate")
	public published  void onUpdate () {
		getPipeline().onUpdate();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:getExtGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:getExtGuid")
	public published  Str getExtGuid () {
		if (extGuid == null)
		    extGuid = Arte::Arte.generateGuid();
		return extGuid;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:setExtGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:setExtGuid")
	public published  void setExtGuid (Str extGuid) {
		extGuid = extGuid;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:afterInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:afterInit")
	protected published  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		super.afterInit(src, phase);
		if (extGuid == null || (src != null && ((Endpoint.Net.Client)src).extGuid == extGuid))
		    extGuid = Arte::Arte.generateGuid();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:isSuitableForPipeline-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:isSuitableForPipeline")
	public published  boolean isSuitableForPipeline (org.radixware.ads.ServiceBus.server.IPipeline pipeline) {
		return !pipeline.isTransformation();
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::ServiceBus.Nodes::Endpoint.Net.Client - Server Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Client-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.Net.Client_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),"Endpoint.Net.Client",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7QCADCK43BDA3E6P4XHK3PG6GY"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
							/*Owner Class Name*/
							"Endpoint.Net.Client",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7QCADCK43BDA3E6P4XHK3PG6GY"),
							/*Property presentations*/

							/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruKFSTLXGCARDIXIKQSMEYPHLTDA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityPid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruO2IBA6LGUZGDZC2KCAS46URYKI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4AZIUDJ5ZNFP7A6C3SOMWS7CXY"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:rqType:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIRI2YV2QHVCS5OSFB4NDL4I4HY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:rsType:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLQSJ2Q3JUNHKPLFJPOIVV4PNFM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:netPortHandler:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7RSALOODWNBCXDDTRPJFWBGFMU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colEWJT6TIHR5VDBNSIAAUMFADAIA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:ConstValue EnumId=\"acsLSG35D4GIHNRDJIEACQMTAIZT4\" ItemId=\"aciK2BEJTYZVPORDJHCAANE2UAFXA\"/></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:toKeepConnectFunc:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruRBMSTK7JW5CJRIWXPQ5X3WXXCI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},org.radixware.kernel.common.types.Id.Factory.loadFrom("eccZMCDFRDL4DOBDIEAAALOMT5GDM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:connectTimeout:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6QHRGESMHBDJVEGFVH6SEMGYRM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceLevel:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGVVSKX3L3VC4VF4ZWTIXI6L53Q"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRqData:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdMXIVIH44AJC4TCNFWCFFLZA7XA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRsData:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZCM7GYUPENEWFEWOUZ5C24NM7E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:extGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruPO7BQN5HUVGJLP7XO2OJ53GC54"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
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
									/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAKSYBYFTWRFMBFL26NNNROUY4Q"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIAXY6SPXVZGZ3AKL75PZGPUPZI"),
									35890,
									null,

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCNRWSZS2HNRDB7BAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAKSYBYFTWRFMBFL26NNNROUY4Q")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							null,
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclD5WJCV7H7JGR5P4BVMYG6FKV7Q"),

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityGuid-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruKFSTLXGCARDIXIKQSMEYPHLTDA"),"pipelineEntityGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHJUCR2NYCNHCLDPRXLXHV6RGTM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityPid-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruO2IBA6LGUZGDZC2KCAS46URYKI"),"pipelineEntityPid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZIZR733LS5BLRNCTBO3MYCDUEU"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4AZIUDJ5ZNFP7A6C3SOMWS7CXY"),"pipelineEntityTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7O6X33R2BBE6HH2557ZLPJALQA"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:rqType-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIRI2YV2QHVCS5OSFB4NDL4I4HY"),"rqType",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:rsType-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLQSJ2Q3JUNHKPLFJPOIVV4PNFM"),"rsType",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:netPortHandler-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7RSALOODWNBCXDDTRPJFWBGFMU"),"netPortHandler",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFSYWTKFMCZETHBG7AMFF33BDWY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refLP2PLKPZ3DNRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:toKeepConnectFunc-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruRBMSTK7JW5CJRIWXPQ5X3WXXCI"),"toKeepConnectFunc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOITILFYUHVBGLBGVRN2AYOVSMY"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUEGT6YCHSNGDRPGY4TKY7XNB7I"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:connectTimeout-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6QHRGESMHBDJVEGFVH6SEMGYRM"),"connectTimeout",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEU7MVV2M7JAWRBYMCME26TWK4U"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30")),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceLevel-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGVVSKX3L3VC4VF4ZWTIXI6L53Q"),"traceLevel",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRqData-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdMXIVIH44AJC4TCNFWCFFLZA7XA"),"inRqData",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRsData-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZCM7GYUPENEWFEWOUZ5C24NM7E"),"inRsData",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:extGuid-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruPO7BQN5HUVGJLP7XO2OJ53GC54"),"extGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTO3N6ZUC25BNLNVHR6WUL3OSCU"),"afterResponseDelivered",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprB75AC7LQHRBYFIAJ6M5QYGSXU4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOC7IFQSQQNCXLAY4YULNTEVVL4"),"checkConfig",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRJJVN7SZFRGFPKSWMK5OODDPHM"),"connect",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("outConnectorRole",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKOIDF6ZMLJD25ELPSF4YHIFM7Y")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("inConnectorId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprT6WT5WB2UZGVLIMH5BKLBOWDQM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZEZONIMUSNHEJHOFQ7OPMP3CLU"),"getConnectorIconId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprE77CXZCXWFBUJEQ7AGMARUJ5UA"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFST4X6BGFNA5PH6LQNOG73MSTQ"),"getConnectorId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQJKWAPGHMVHZFJKF33B5R26WCQ"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWE5I24A36RE3BGGGIIG4FYAV7M"),"getConnectorRole",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZ6HMEJSTXJHERHEMN76HHO7D2Q"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEIEXINMQGJB35G65UDWFOOSDZU"),"getConnectorRqDataType",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKZ7VU57GJBDSBID7JYBKWNCFBM"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHZIY6G7TCVCX3DR6QWA77MJJHI"),"getConnectorRsDataType",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW6ZXLYRSCRC4VBIHE2XC6OE42Q"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLB23XTDDKVGSRN5R4BPLLXUMFY"),"getConnectorTitle",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEZSTAU4H4NGQPCGIJVOMB4PWNU"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVA6DRQSCRRCMRNAAYI556P43VE"),"getDescription",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4Y335FOB6VF3JHZ5WI7UI5TCHM"),"getHolderEntityPid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOK5OVIZTJ5CUTKASBMH6PUUWGQ"),"getIconId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIQ7FIDN6DFEYXJSX6G5WONK32M"),"getInConnectorRoles",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZHFRUV5SQ5CYNG4JLLR26M4UWU"),"getOutConnectorRoles",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYFZPW475EFFCLLXOYQHNVMU3UM"),"getPipeline",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRGPLXVTN4BB5LNLYHDLSL3K444"),"getTitle",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthK2JMTSV73FBGBGSJJOH2ZLBYCI"),"getNetProtocolHandler",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7MEKN6RYYBASBPAYYLM556VGPM"),"processRequest",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprF2JW2URB6ZBU5NESNR3SSGABNQ"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLILYOANG5RBVTHLQIJLYCSDJM4"),"getCreatingPresentationId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIOMPW3QBZ5ASTPONSTPGMH5ZHQ"),"getEditingPresentationId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGUAPALQ5DJBAFAWDIAXWPKXUNY"),"onConnect",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("channelId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprG4ICA43ATVADFAH3OJZMGJNWEY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("serverAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNX3ALXEND5CIVEMVIVO7VLAIJI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVCXEA3WALRHS3OO7FZGSZZOJJM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientCertCn",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2L7IH6GHN5AKBJSBERAKTV6WCY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKVJXBJ6SGBGXLABVFE7CZRWS7Q"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthO6EBWEP46VFXTAYRWGTHPX35WI"),"onDisconnect",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("channelId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprS25J5TRLOBH6NJVYVEHZBDUMPU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("serverAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprX2DOOZSBOFEYPAYYLIDMVGDHRU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAR5RNPUS55E73D5DHCWVHXB22Y")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientCertCn",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYBDTQLZS4ZEKRFL47CVPASOUXA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPXFOEFDTZ5FALF3DB3ZBHAESTQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackPid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIOK2JF74KFDGLKPTRFUTMPQ6YM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackWid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprF7IDKMTNXFB2HNEBPJHMD74ZQQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBZAATQUT4BAS7MPBI3VBZ6QNEI"),"onRecv",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("channelId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLPDWOXHXGZCW3PKP22KD6GLVXY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("serverAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMZ4KSKFZFZD7ZHWQNC36GXM634")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprARH46O7EA5DPVGMFP6FK5FW6NY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientCertCn",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7MZ3FZWW5RBSZHYK4WCHZ2TCIQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprL6FHVNOSZ5D5LFMLZR5BSWL6SU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackPid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprP3R73VMJ5RAHTOOPCDCXTGFGGY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackWid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAH5DFY4ZXBDM5FJGMI3YLEBYIA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("recvPacketHeaders",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr57OZZ62QJZDMVOITJ6JL4FQWTI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("recvPacket",org.radixware.kernel.common.enums.EValType.BIN,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr62NO6CJS3JALVAH5ROTKSWAC6Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("connected",org.radixware.kernel.common.enums.EValType.BOOL,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWFU46B5L2NBOXCT2BAOB3EBWWY"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWQC7HI6DMJB33MVZT5VNNK6LR4"),"traceDebug",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGRHKY4E5C5HOXMYJZCFXUM6D3Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sensitive",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMHUQLO2UKJAV5GVRHGMHJ34GB4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQ7MQNDFPEVCSZM46CIFMM4ISAI"),"traceError",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW2WDVHQEAVFR5JIY7VOHHR75XU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNY56F4MVVVAABKIBM7GQ52OD5U"),"traceEvent",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprH7O4RAFFNZF7LCFLWD53N7M7W4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNLROOCGTF5CUVM66WIOJDRSHCE"),"trace",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("severity",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr63JZZOZJBZHA3AEUILCMUYMDQQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJ3JTGGT2NNDUBJ7HKOVZCB3ELQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sensitive",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6DVYFHCEUVCGRIE7S6N7R3U6HI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOYF3L7CFTFB27GLSUU7QWQ44PM"),"traceWarning",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRCPMUAG2BNDKLEBVBLFXTUV7MM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSC2RWSPCHVHJZL6TN5L5ZPUXBY"),"onRecvTimeout",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("channelId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6J4C2LAC3FGIRHRQVVBIHEIMDQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("serverAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFWA2ZDID4BDWRDYIBKAQLEQJHI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHCIRQGXBJZGEBPIWZWYCGVJKOY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientCertCn",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprD7DR5EKODFEMRDCVNRHMKJJUTI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW4P4TCZEEBCLRPBLUFHSWWLVLU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackPid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr32WGX5HC7ZFILLAZ5UGP3KE7OU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackWid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprC25DK5AJWFHOHN33PACZ7TRPYM"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthB25WTYWZUJAJXMCSH7UVSSJMP4"),"getConnectorSide",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprS4VYEBJ3NBC3LPA3T34NLFUEVM"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7JXSRN7HJNANHAN7CZOA2VSTJU"),"checkTraceLevel",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("severity",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6ONHNQWDRFASBPJOIWLIIRSITY"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPGZZ43N6ZJBBFBXOPUQKOYSBLQ"),"onConnectTimeout",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("channelId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRWKMBNI62FAVJHGRTTCQXSNNGU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("serverAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7VX6AB5LFZFNHOT2H5BIXYGQ5I")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprT4GBB6SPB5FLRKHOMGMVT7PJEI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientCertCn",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKUVURQ5QONF7RAAOMBWHLJRXQM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprI4J4WRSIKJBLNLPJIEIOQPNZPQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackPid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPXQOX4ZPANAJVETPP6GUMBYTJM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackWid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYAA7JC7EMVAZ3NOV5XI3AZAAPI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAXJJZNLFOJGVBNHZN5AEP5GJGM"),"getOrderPos",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPI4ONCPYCFAIJNIXF3ZKSVPAYM"),"getPipelineNodeParam",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("name",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFTW3XBRJKFB2ZHQ3XPMPTUDNZM"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSJVD663SIFFXVLMYDV2Y2VU544"),"export",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthG2W65RASFFH7RIEA37LOBPKUFI"),"setPipeline",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pipeline",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAVZSFFKQABGCZGVUAUN6VQPSME"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthM7KLX4PIANEH3NYQZZSZ55GMIE"),"refreshCache",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLOOYWJC2DNGELHZHFDPO2CVO3A"),"traceDebugXml",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGQ6USHRUQNBZZAHRWSIC3M67SM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTT4KTPCLQZFQBD4ZS2UU2A52BQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sensitive",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprATW4BADRUZGG5CTTFHW3XCP4OE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKMN6A6YHPVBNFON237GIJY4ZPU"),"import",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xNode",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBLAHXJJYFVBVXLJRTUBCPT4RK4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("overwriteLocalSettings",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2TSBFAKU35GD5F5DU7RA7HTBCA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4WIQKXHTJRBGLOHXHDGA7QNAL4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3VOSK24FZVFTLKSZISQ3XGMJDE"),"needCaching",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNYO5JRURNRFWLFFLCVODWPOTLI"),"getLastInRqData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6XF3CCTG7NGA7O7SEKOQTWLIHE"),"getLastInRsData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLC5CACZEKVCFHEZPWGJ7AN732I"),"getLastOutRqData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6D7TO5XHNRCCZMYQJQZMIWCSUQ"),"getLastOutRsData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBXD5LT3S2VA2ZGTSBZIOEVFN2I"),"afterImport",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pids",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprO2EWFKI72ZHJ7HEVJFHA4AYBY4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4TZSWKM5JBDPDCPTG5TBLHXLBU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNZ5Z7AS6RFRPIX6ZD5JDKEC34"),"beforeDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAZ2HAURWXVB53HEDENC67YLQAU"),"afterUpdatePropObject",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprY2HICYRVFZB23DU6EIPWIXZ6YQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propVal",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprF7Y6DTAMCBBPDGZJOT6O5RAF6M"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthND2HW4NWXRA3HMKQ2AU5EMWWGY"),"onUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthW4LHL5WOQJF7NK3VL3LMQETFTI"),"getExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZVCMFP66RZEELGIQECRMCICA3E"),"setExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLTNMS5T7WJFJ3EVE3A5PCMJLQ4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAK3UOYVSWFFKFAZNRDJNC7ADIM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW64IC3GQXVAOZFPQTFLF75KMAI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNSQ7CEKCGFFWRLVESA72LZ7DEM"),"isSuitableForPipeline",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pipeline",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5VDXQGLJXVENXLN6WQO4D5QPXU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

/* Radix::ServiceBus.Nodes::Endpoint.Net.Client - Desktop Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Client-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client")
public interface Endpoint.Net.Client   extends org.radixware.ads.Net.explorer.NetClient  {



























































































































































































	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:netPortHandler:netPortHandler-Presentation Property*/


	public class NetPortHandler extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public NetPortHandler(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:netPortHandler:netPortHandler")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:netPortHandler:netPortHandler")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public NetPortHandler getNetPortHandler();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityTitle:pipelineEntityTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityTitle:pipelineEntityTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityTitle:pipelineEntityTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityTitle getPipelineEntityTitle();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceLevel:traceLevel-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceLevel:traceLevel")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceLevel:traceLevel")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public TraceLevel getTraceLevel();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:rqType:rqType-Presentation Property*/


	public class RqType extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RqType(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:rqType:rqType")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:rqType:rqType")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RqType getRqType();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:rsType:rsType-Presentation Property*/


	public class RsType extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RsType(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:rsType:rsType")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:rsType:rsType")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RsType getRsType();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRqData:inRqData-Presentation Property*/


	public class InRqData extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public InRqData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRqData:inRqData")
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRqData:inRqData")
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public InRqData getInRqData();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRsData:inRsData-Presentation Property*/


	public class InRsData extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public InRsData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRsData:inRsData")
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRsData:inRsData")
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public InRsData getInRsData();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:connectTimeout:connectTimeout-Presentation Property*/


	public class ConnectTimeout extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ConnectTimeout(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:connectTimeout:connectTimeout")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:connectTimeout:connectTimeout")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ConnectTimeout getConnectTimeout();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityGuid:pipelineEntityGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityGuid:pipelineEntityGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityGuid:pipelineEntityGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityGuid getPipelineEntityGuid();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityPid:pipelineEntityPid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityPid:pipelineEntityPid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityPid:pipelineEntityPid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityPid getPipelineEntityPid();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:extGuid:extGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:extGuid:extGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:extGuid:extGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtGuid getExtGuid();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:toKeepConnectFunc:toKeepConnectFunc-Presentation Property*/


	public class ToKeepConnectFunc extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public ToKeepConnectFunc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:toKeepConnectFunc:toKeepConnectFunc")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:toKeepConnectFunc:toKeepConnectFunc")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ToKeepConnectFunc getToKeepConnectFunc();


}

/* Radix::ServiceBus.Nodes::Endpoint.Net.Client - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Client-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.Net.Client_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
			"Radix::ServiceBus.Nodes::Endpoint.Net.Client",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclD5WJCV7H7JGR5P4BVMYG6FKV7Q"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7QCADCK43BDA3E6P4XHK3PG6GY"),null,null,0,

			/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruKFSTLXGCARDIXIKQSMEYPHLTDA"),
						"pipelineEntityGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHJUCR2NYCNHCLDPRXLXHV6RGTM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruO2IBA6LGUZGDZC2KCAS46URYKI"),
						"pipelineEntityPid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZIZR733LS5BLRNCTBO3MYCDUEU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4AZIUDJ5ZNFP7A6C3SOMWS7CXY"),
						"pipelineEntityTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7O6X33R2BBE6HH2557ZLPJALQA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:rqType:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIRI2YV2QHVCS5OSFB4NDL4I4HY"),
						"rqType",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:rqType:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:rsType:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLQSJ2Q3JUNHKPLFJPOIVV4PNFM"),
						"rsType",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:rsType:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:netPortHandler:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7RSALOODWNBCXDDTRPJFWBGFMU"),
						"netPortHandler",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFSYWTKFMCZETHBG7AMFF33BDWY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:toKeepConnectFunc:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruRBMSTK7JW5CJRIWXPQ5X3WXXCI"),
						"toKeepConnectFunc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOITILFYUHVBGLBGVRN2AYOVSMY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUEGT6YCHSNGDRPGY4TKY7XNB7I"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						null,
						0L,
						0L,false,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:connectTimeout:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6QHRGESMHBDJVEGFVH6SEMGYRM"),
						"connectTimeout",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEU7MVV2M7JAWRBYMCME26TWK4U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.USER,
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
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:connectTimeout:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceLevel:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGVVSKX3L3VC4VF4ZWTIXI6L53Q"),
						"traceLevel",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceLevel:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRqData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdMXIVIH44AJC4TCNFWCFFLZA7XA"),
						"inRqData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
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

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRsData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZCM7GYUPENEWFEWOUZ5C24NM7E"),
						"inRsData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
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

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:extGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruPO7BQN5HUVGJLP7XO2OJ53GC54"),
						"extGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:extGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
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
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAKSYBYFTWRFMBFL26NNNROUY4Q")},
			true,true,false);
}

/* Radix::ServiceBus.Nodes::Endpoint.Net.Client - Web Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Client-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client")
public interface Endpoint.Net.Client   extends org.radixware.ads.Net.web.NetClient  {















































































































































































	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:netPortHandler:netPortHandler-Presentation Property*/


	public class NetPortHandler extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public NetPortHandler(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:netPortHandler:netPortHandler")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:netPortHandler:netPortHandler")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public NetPortHandler getNetPortHandler();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityTitle:pipelineEntityTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityTitle:pipelineEntityTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityTitle:pipelineEntityTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityTitle getPipelineEntityTitle();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceLevel:traceLevel-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceLevel:traceLevel")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceLevel:traceLevel")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public TraceLevel getTraceLevel();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:rqType:rqType-Presentation Property*/


	public class RqType extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RqType(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:rqType:rqType")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:rqType:rqType")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RqType getRqType();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:rsType:rsType-Presentation Property*/


	public class RsType extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RsType(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:rsType:rsType")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:rsType:rsType")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RsType getRsType();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRqData:inRqData-Presentation Property*/


	public class InRqData extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public InRqData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRqData:inRqData")
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRqData:inRqData")
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public InRqData getInRqData();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRsData:inRsData-Presentation Property*/


	public class InRsData extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public InRsData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRsData:inRsData")
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRsData:inRsData")
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public InRsData getInRsData();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:connectTimeout:connectTimeout-Presentation Property*/


	public class ConnectTimeout extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ConnectTimeout(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:connectTimeout:connectTimeout")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:connectTimeout:connectTimeout")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ConnectTimeout getConnectTimeout();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityGuid:pipelineEntityGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityGuid:pipelineEntityGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityGuid:pipelineEntityGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityGuid getPipelineEntityGuid();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityPid:pipelineEntityPid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityPid:pipelineEntityPid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityPid:pipelineEntityPid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityPid getPipelineEntityPid();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:extGuid:extGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:extGuid:extGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:extGuid:extGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtGuid getExtGuid();


}

/* Radix::ServiceBus.Nodes::Endpoint.Net.Client - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Client-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.Net.Client_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
			"Radix::ServiceBus.Nodes::Endpoint.Net.Client",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclD5WJCV7H7JGR5P4BVMYG6FKV7Q"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7QCADCK43BDA3E6P4XHK3PG6GY"),null,null,0,

			/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruKFSTLXGCARDIXIKQSMEYPHLTDA"),
						"pipelineEntityGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHJUCR2NYCNHCLDPRXLXHV6RGTM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruO2IBA6LGUZGDZC2KCAS46URYKI"),
						"pipelineEntityPid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZIZR733LS5BLRNCTBO3MYCDUEU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4AZIUDJ5ZNFP7A6C3SOMWS7CXY"),
						"pipelineEntityTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7O6X33R2BBE6HH2557ZLPJALQA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:pipelineEntityTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:rqType:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIRI2YV2QHVCS5OSFB4NDL4I4HY"),
						"rqType",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:rqType:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:rsType:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLQSJ2Q3JUNHKPLFJPOIVV4PNFM"),
						"rsType",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:rsType:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:netPortHandler:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7RSALOODWNBCXDDTRPJFWBGFMU"),
						"netPortHandler",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFSYWTKFMCZETHBG7AMFF33BDWY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:toKeepConnectFunc:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruRBMSTK7JW5CJRIWXPQ5X3WXXCI"),
						"toKeepConnectFunc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOITILFYUHVBGLBGVRN2AYOVSMY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUEGT6YCHSNGDRPGY4TKY7XNB7I"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						null,
						null,
						null,
						0L,
						0L,false,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:connectTimeout:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6QHRGESMHBDJVEGFVH6SEMGYRM"),
						"connectTimeout",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEU7MVV2M7JAWRBYMCME26TWK4U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.USER,
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
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:connectTimeout:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceLevel:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGVVSKX3L3VC4VF4ZWTIXI6L53Q"),
						"traceLevel",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:traceLevel:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRqData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdMXIVIH44AJC4TCNFWCFFLZA7XA"),
						"inRqData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
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

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:inRsData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZCM7GYUPENEWFEWOUZ5C24NM7E"),
						"inRsData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
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

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:extGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruPO7BQN5HUVGJLP7XO2OJ53GC54"),
						"extGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:extGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
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
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAKSYBYFTWRFMBFL26NNNROUY4Q")},
			true,true,false);
}

/* Radix::ServiceBus.Nodes::Endpoint.Net.Client:General - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAKSYBYFTWRFMBFL26NNNROUY4Q"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIAXY6SPXVZGZ3AKL75PZGPUPZI"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWZIGKXVBUJCAXFUD3O7JLYV5VE"),
	null,

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGNP4GNUTDFD3NNNXEJDMQADON4"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4PCKEINNPJFEPJCCRPHSSZEUKU"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKA3TGQME2DNRDB7AAALOMT5GDM"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKE3TGQME2DNRDB7AAALOMT5GDM"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ2GUXJUE2DNRDB7AAALOMT5GDM"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGRXTHMME2DNRDB7AAALOMT5GDM"),0,13,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVGDODIF6BLOBDCGJAALOMT5GDM"),0,15,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKSQETMV6BLOBDCGJAALOMT5GDM"),0,16,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colH2W5NJG535G7TCZERIU3I2KW6I"),0,19,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6OTLV7WBZVGDFM7SYNM5KXYSGQ"),0,20,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSOCMOJR6SRCGBL63P2ALGCKNUQ"),0,21,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5R5UG4B5JEUPMEJLRID7SISTY"),0,22,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4AZIUDJ5ZNFP7A6C3SOMWS7CXY"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruRBMSTK7JW5CJRIWXPQ5X3WXXCI"),0,18,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7QM5XU6LIJHIPK2G47EWNFLRF4"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7RSALOODWNBCXDDTRPJFWBGFMU"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB3UVBAV46ZHJ5POIV5R3JDFQ2E"),0,17,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6QHRGESMHBDJVEGFVH6SEMGYRM"),0,14,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTFACUXG6NJAMNJZ33CKFZTYORY"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJWZOTJWGMFBWDMHWTBSXO6MZNY"),0,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4QBLQUIZAZFQPILQILYXGOSMGM"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col22YVF3LEOFE5BCJXA5KDPU73NA"),0,23,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colF3F5Y5EF2DNRDB7AAALOMT5GDM"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRKQ2IQSPJDUJFZWAVDVZYN6ZI"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVZZGWK24VGBBAQGW4QAVPJ4GY"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN7MFLE4DNJB3RIQUOC2I37BXPI"),0,25,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB3IQTAEF2DNRDB7AAALOMT5GDM"),0,24,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGEUOICAGRBAU3NBME7T63HEL7I"),0,1,1,false,false)
			},null),

			/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:TraceOptions-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgCWCNH76WQNGY5IUPVIUKKE4CCQ"),"TraceOptions",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPTLOGCXBOVEHZJGKMK534YS2MM"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXOEZ5O4YNXOBDCJJAALOMT5GDM"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVKQVABX6FH7PAEUHL7URGEEAY"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNXKEWDHZK5CILMRUHOP7KW46Q4"),0,2,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGNP4GNUTDFD3NNNXEJDMQADON4")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgCWCNH76WQNGY5IUPVIUKKE4CCQ")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg2OZOQGDAB5G2XP3FXEHO6W53BQ"))}
	,

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	35890,0,0);
}
/* Radix::ServiceBus.Nodes::Endpoint.Net.Client:General - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.web;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAKSYBYFTWRFMBFL26NNNROUY4Q"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIAXY6SPXVZGZ3AKL75PZGPUPZI"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWZIGKXVBUJCAXFUD3O7JLYV5VE"),
	null,

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGNP4GNUTDFD3NNNXEJDMQADON4"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWOMV4PZ675DKHBFZ752PW4AVZU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4PCKEINNPJFEPJCCRPHSSZEUKU"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKA3TGQME2DNRDB7AAALOMT5GDM"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKE3TGQME2DNRDB7AAALOMT5GDM"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ2GUXJUE2DNRDB7AAALOMT5GDM"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGRXTHMME2DNRDB7AAALOMT5GDM"),0,13,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVGDODIF6BLOBDCGJAALOMT5GDM"),0,15,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKSQETMV6BLOBDCGJAALOMT5GDM"),0,16,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colH2W5NJG535G7TCZERIU3I2KW6I"),0,19,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6OTLV7WBZVGDFM7SYNM5KXYSGQ"),0,20,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSOCMOJR6SRCGBL63P2ALGCKNUQ"),0,21,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5R5UG4B5JEUPMEJLRID7SISTY"),0,22,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4AZIUDJ5ZNFP7A6C3SOMWS7CXY"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruRBMSTK7JW5CJRIWXPQ5X3WXXCI"),0,18,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7QM5XU6LIJHIPK2G47EWNFLRF4"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7RSALOODWNBCXDDTRPJFWBGFMU"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB3UVBAV46ZHJ5POIV5R3JDFQ2E"),0,17,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6QHRGESMHBDJVEGFVH6SEMGYRM"),0,14,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTFACUXG6NJAMNJZ33CKFZTYORY"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJWZOTJWGMFBWDMHWTBSXO6MZNY"),0,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4QBLQUIZAZFQPILQILYXGOSMGM"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col22YVF3LEOFE5BCJXA5KDPU73NA"),0,23,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colF3F5Y5EF2DNRDB7AAALOMT5GDM"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRKQ2IQSPJDUJFZWAVDVZYN6ZI"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVZZGWK24VGBBAQGW4QAVPJ4GY"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN7MFLE4DNJB3RIQUOC2I37BXPI"),0,25,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB3IQTAEF2DNRDB7AAALOMT5GDM"),0,24,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGEUOICAGRBAU3NBME7T63HEL7I"),0,1,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGNP4GNUTDFD3NNNXEJDMQADON4")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg2OZOQGDAB5G2XP3FXEHO6W53BQ"))}
	,

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	35890,0,0);
}
/* Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model - Desktop Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model")
public class General:Model  extends org.radixware.ads.ServiceBus.Nodes.explorer.Endpoint.Net.Client.Endpoint.Net.Client_DefaultModel.eprIAXY6SPXVZGZ3AKL75PZGPUPZI_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:pipelineEntityTitle-Presentation Property*/




	public class PipelineEntityTitle extends org.radixware.ads.ServiceBus.Nodes.explorer.Endpoint.Net.Client.Endpoint.Net.Client_DefaultModel.eprIAXY6SPXVZGZ3AKL75PZGPUPZI_ModelAdapter.prd4AZIUDJ5ZNFP7A6C3SOMWS7CXY{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:pipelineEntityTitle")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:pipelineEntityTitle")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityTitle getPipelineEntityTitle(){return (PipelineEntityTitle)getProperty(prd4AZIUDJ5ZNFP7A6C3SOMWS7CXY);}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:syncMode-Presentation Property*/




	public class SyncMode extends org.radixware.ads.ServiceBus.Nodes.explorer.Endpoint.Net.Client.Endpoint.Net.Client_DefaultModel.eprIAXY6SPXVZGZ3AKL75PZGPUPZI_ModelAdapter.colPRKQ2IQSPJDUJFZWAVDVZYN6ZI{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:syncMode")
		public published  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:syncMode")
		public published   void setValue(Bool val) {

			internal[syncMode] = val;
			setVisibilityLocal();
		}
	}
	public SyncMode getSyncMode(){return (SyncMode)getProperty(colPRKQ2IQSPJDUJFZWAVDVZYN6ZI);}










	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		if (getContext() instanceof ServiceBus::ISbContext) {
		    final ServiceBus::ISbContext context = (ServiceBus::ISbContext)getContext();
		    if (isNew()) {
		        pipelineEntityGuid.Value = String.valueOf(context.getPipelinePid().getTableId());
		        pipelineEntityPid.Value = String.valueOf(context.getPipelinePid());
		    }
		    pipelineEntityTitle.setVisible(false);
		    netPortHandler.setVisible(true);
		} else {
		    pipelineEntityTitle.setVisible(true);
		    netPortHandler.setVisible(false);
		}
		super.beforeOpenView();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:setVisibilityLocal-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:setVisibilityLocal")
	public published  void setVisibilityLocal () {
		final boolean isSync = syncMode.Value != null && syncMode.Value.booleanValue();
		curBusySessionCount.setVisible(isSync);
		isCurBusySessionCountOn.setVisible(isSync);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		if (!(getContext() instanceof Explorer.Context::SelectorRowContext)) {
		    setVisibilityLocal();
		}
	}


}

/* Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemAKSYBYFTWRFMBFL26NNNROUY4Q"),
						"General:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemFCNRWSZS2HNRDB7BAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model - Web Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model")
public class General:Model  extends org.radixware.ads.ServiceBus.Nodes.web.Endpoint.Net.Client.Endpoint.Net.Client_DefaultModel.eprIAXY6SPXVZGZ3AKL75PZGPUPZI_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:pipelineEntityTitle-Presentation Property*/




	public class PipelineEntityTitle extends org.radixware.ads.ServiceBus.Nodes.web.Endpoint.Net.Client.Endpoint.Net.Client_DefaultModel.eprIAXY6SPXVZGZ3AKL75PZGPUPZI_ModelAdapter.prd4AZIUDJ5ZNFP7A6C3SOMWS7CXY{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:pipelineEntityTitle")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:pipelineEntityTitle")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityTitle getPipelineEntityTitle(){return (PipelineEntityTitle)getProperty(prd4AZIUDJ5ZNFP7A6C3SOMWS7CXY);}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:syncMode-Presentation Property*/




	public class SyncMode extends org.radixware.ads.ServiceBus.Nodes.web.Endpoint.Net.Client.Endpoint.Net.Client_DefaultModel.eprIAXY6SPXVZGZ3AKL75PZGPUPZI_ModelAdapter.colPRKQ2IQSPJDUJFZWAVDVZYN6ZI{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:syncMode")
		public published  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:syncMode")
		public published   void setValue(Bool val) {

			internal[syncMode] = val;
			setVisibilityLocal();
		}
	}
	public SyncMode getSyncMode(){return (SyncMode)getProperty(colPRKQ2IQSPJDUJFZWAVDVZYN6ZI);}










	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		if (getContext() instanceof ServiceBus::ISbContext) {
		    final ServiceBus::ISbContext context = (ServiceBus::ISbContext)getContext();
		    if (isNew()) {
		        pipelineEntityGuid.Value = String.valueOf(context.getPipelinePid().getTableId());
		        pipelineEntityPid.Value = String.valueOf(context.getPipelinePid());
		    }
		    pipelineEntityTitle.setVisible(false);
		    netPortHandler.setVisible(true);
		} else {
		    pipelineEntityTitle.setVisible(true);
		    netPortHandler.setVisible(false);
		}
		super.beforeOpenView();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:setVisibilityLocal-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:setVisibilityLocal")
	public published  void setVisibilityLocal () {
		final boolean isSync = syncMode.Value != null && syncMode.Value.booleanValue();
		curBusySessionCount.setVisible(isSync);
		isCurBusySessionCountOn.setVisible(isSync);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		if (!(getContext() instanceof Explorer.Context::SelectorRowContext)) {
		    setVisibilityLocal();
		}
	}


}

/* Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemAKSYBYFTWRFMBFL26NNNROUY4Q"),
						"General:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemFCNRWSZS2HNRDB7BAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Endpoint.Net.Client:General:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Endpoint.Net.Client - Localizing Bundle */
package org.radixware.ads.ServiceBus.Nodes.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.Net.Client - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4PCKEINNPJFEPJCCRPHSSZEUKU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Invalid \"Keep connection timeout\" function");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Функция  \"Длительность удержания соединения\" некорректна");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls525KUNPB3ZDUVCEYRMA2MVY4WI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pipeline");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конвейер");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7O6X33R2BBE6HH2557ZLPJALQA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Network Channel - Client");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сетевой канал - клиент");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7QCADCK43BDA3E6P4XHK3PG6GY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Connection timeout (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тайм-аут соединения (с)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEU7MVV2M7JAWRBYMCME26TWK4U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"System unit");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Модуль системы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFSYWTKFMCZETHBG7AMFF33BDWY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Entity ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHJUCR2NYCNHCLDPRXLXHV6RGTM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Outbound requests");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Исходящие запросы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIOX3YQWIWFDFLCIKHJP5W37BPU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Keep connection timeout function");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Длительность удержания соединения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOITILFYUHVBGLBGVRN2AYOVSMY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOXJ7BFL67ZCZ7NY2XXLZCYKXXE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace Settings");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Настройки трассы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPTLOGCXBOVEHZJGKMK534YS2MM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Network Channel - Client");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сетевой канал - клиент");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWZIGKXVBUJCAXFUD3O7JLYV5VE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Entity PID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ключ сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZIZR733LS5BLRNCTBO3MYCDUEU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Endpoint.Net.Client - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclWOMV4PZ675DKHBFZ752PW4AVZU"),"Endpoint.Net.Client - Localizing Bundle",$$$items$$$);
}
