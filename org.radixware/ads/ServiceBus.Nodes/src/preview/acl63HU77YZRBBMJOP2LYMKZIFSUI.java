
/* Radix::ServiceBus.Nodes::Endpoint.Net.Server - Server Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Server-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.server;

import java.util.*;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server")
public published class Endpoint.Net.Server  extends org.radixware.ads.Net.server.NetListener  implements org.radixware.ads.ServiceBus.server.IPipelineNode,org.radixware.kernel.server.net.NetProtocolHandler,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private final static Str OUT = "Out";
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Endpoint.Net.Server_mi.rdxMeta;}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityGuid-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityGuid")
	public published  Str getPipelineEntityGuid() {
		return pipelineEntityGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityGuid")
	public published   void setPipelineEntityGuid(Str val) {
		pipelineEntityGuid = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityPid-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityPid")
	public published  Str getPipelineEntityPid() {
		return pipelineEntityPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityPid")
	public published   void setPipelineEntityPid(Str val) {
		pipelineEntityPid = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectId-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectId")
	public published  Str getOutbConnectId() {
		return outbConnectId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectId")
	public published   void setOutbConnectId(Str val) {
		outbConnectId = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityTitle-Dynamic Property*/



	protected Str pipelineEntityTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityTitle")
	public published  Str getPipelineEntityTitle() {

		if (pipelineEntityGuid == null || pipelineEntityPid == null)
		    return null;
		return ServiceBus::SbServerUtil.getEntityTitle(
		    new Pid(Arte, Types::Id.Factory.loadFrom(pipelineEntityGuid), pipelineEntityPid)
		    );
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityTitle")
	public published   void setPipelineEntityTitle(Str val) {
		pipelineEntityTitle = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectTitle-Dynamic Property*/



	protected Str outbConnectTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectTitle")
	public published  Str getOutbConnectTitle() {

		return ServiceBus::SbServerUtil.getConnectionTitle(outbConnectId);
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectTitle")
	public published   void setOutbConnectTitle(Str val) {
		outbConnectTitle = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:rqType-Dynamic Property*/



	protected Str rqType=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:rqType")
	public published  Str getRqType() {

		return getConnectorRqDataType(OUT);
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:rqType")
	public published   void setRqType(Str val) {
		rqType = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:rsType-Dynamic Property*/



	protected Str rsType=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:rsType")
	public published  Str getRsType() {

		return getConnectorRsDataType(OUT);
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:rsType")
	public published   void setRsType(Str val) {
		rsType = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityId-Dynamic Property*/



	protected Str holderEntityId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityId")
	public published  Str getHolderEntityId() {

		return String.valueOf(getHolderEntityPid().EntityId);
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityId")
	public published   void setHolderEntityId(Str val) {
		holderEntityId = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityPid-Dynamic Property*/



	protected Str holderEntityPid=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityPid")
	public published  Str getHolderEntityPid() {

		return String.valueOf(getHolderEntityPid());
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityPid")
	public published   void setHolderEntityPid(Str val) {
		holderEntityPid = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:netPortHandler-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:netPortHandler")
	public published  org.radixware.ads.System.server.Unit getNetPortHandler() {
		return netPortHandler;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:netPortHandler")
	public published   void setNetPortHandler(org.radixware.ads.System.server.Unit val) {
		netPortHandler = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:toKeepConnectFunc-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:toKeepConnectFunc")
	public published  org.radixware.ads.ServiceBus.Nodes.server.UserFunc.KeepConnect getToKeepConnectFunc() {
		return toKeepConnectFunc;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:toKeepConnectFunc")
	public published   void setToKeepConnectFunc(org.radixware.ads.ServiceBus.Nodes.server.UserFunc.KeepConnect val) {
		toKeepConnectFunc = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceLevel-Dynamic Property*/



	protected Int traceLevel=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceLevel")
	private final  Int getTraceLevel() {
		return traceLevel;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceLevel")
	private final   void setTraceLevel(Int val) {
		traceLevel = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:isConnectReadyNtfOn-Column-Based Property*/




	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRqData-Dynamic Property*/



	protected org.apache.xmlbeans.XmlObject outRqData=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRqData")
	protected published  org.apache.xmlbeans.XmlObject getOutRqData() {
		return outRqData;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRqData")
	protected published   void setOutRqData(org.apache.xmlbeans.XmlObject val) {
		outRqData = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRsData-Dynamic Property*/



	protected org.apache.xmlbeans.XmlObject outRsData=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRsData")
	protected published  org.apache.xmlbeans.XmlObject getOutRsData() {
		return outRsData;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRsData")
	protected published   void setOutRsData(org.apache.xmlbeans.XmlObject val) {
		outRsData = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:extGuid-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:extGuid")
	public published  Str getExtGuid() {
		return extGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:extGuid")
	public published   void setExtGuid(Str val) {
		extGuid = val;
	}





























































































































	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:afterResponseDelivered-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:afterResponseDelivered")
	public published  void afterResponseDelivered (org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRs rq) throws org.radixware.ads.ServiceBus.server.PipelineException {

	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:checkConfig-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:checkConfig")
	public published  void checkConfig () throws org.radixware.ads.ServiceBus.server.PipelineConfigException {
		if (toKeepConnectFunc != null && toKeepConnectFunc.isValid != Bool.TRUE)
		    throw new PipelineConfigException("Invalid \"Keep connection timeout\" function", this);
		ServiceBus::SbServerUtil.checkConnection(this, OUT, outbConnectId);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:connect-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:connect")
	public published  void connect (Str outConnectorRole, Str inConnectorId) {
		if (outConnectorRole == OUT) {
		    outbConnectId = inConnectorId;
		} else    
		    throw new AppError("Unknown role " + outConnectorRole);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getConnectorIconId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getConnectorIconId")
	public published  org.radixware.kernel.common.types.Id getConnectorIconId (Str role) {
		if (role == OUT)
		    return idof[ServiceBus::next];
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getConnectorId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getConnectorId")
	public published  Str getConnectorId (Str role) {
		if (role == OUT)
		    return outbConnectId;
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getConnectorRole-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getConnectorRole")
	public published  Str getConnectorRole (Str id) {
		if (id == outbConnectId)
		    return OUT;
		throw new AppError("Unknown id " + id);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getConnectorRqDataType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getConnectorRqDataType")
	public published  Str getConnectorRqDataType (Str role) {
		if (role == OUT)
		    return BINARY_TYPE;
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getConnectorRsDataType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getConnectorRsDataType")
	public published  Str getConnectorRsDataType (Str role) {
		if (role == OUT)
		    return BINARY_TYPE;
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getConnectorTitle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getConnectorTitle")
	public published  Str getConnectorTitle (Str role) {
		if (role == OUT)
		    return "Inbound requests";
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getDescription-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getDescription")
	public published  Str getDescription () {
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getHolderEntityPid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getHolderEntityPid")
	public published  org.radixware.kernel.server.types.Pid getHolderEntityPid () {
		return getPid();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getIconId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getIconId")
	public published  org.radixware.kernel.common.types.Id getIconId () {
		return idof[NetServer];
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getInConnectorRoles-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getInConnectorRoles")
	public published  Str[] getInConnectorRoles () {
		return new Str[] {};
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getOutConnectorRoles-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getOutConnectorRoles")
	public published  Str[] getOutConnectorRoles () {
		return new Str[] { OUT };
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getPipeline-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getPipeline")
	public published  org.radixware.ads.ServiceBus.server.IPipeline getPipeline () {
		return ServiceBus::SbServerUtil.getPipeline(new Pid(Arte, Types::Id.Factory.loadFrom(pipelineEntityGuid), pipelineEntityPid));
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getTitle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getTitle")
	public published  Str getTitle () {
		return title;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getNetProtocolHandler-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getNetProtocolHandler")
	protected published  org.radixware.kernel.server.net.NetProtocolHandler getNetProtocolHandler () {
		return this;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:processRequest-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:processRequest")
	public published  org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRs processRequest (org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRq rq) throws org.radixware.ads.ServiceBus.server.PipelineException {
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getCreatingPresentationId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getCreatingPresentationId")
	public published  org.radixware.kernel.common.types.Id getCreatingPresentationId () {
		return idof[Endpoint.Net.Server:General];
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getEditingPresentationId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getEditingPresentationId")
	public published  org.radixware.kernel.common.types.Id getEditingPresentationId () {
		return idof[Endpoint.Net.Server:General];
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:onConnect-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:onConnect")
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

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:onDisconnect-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:onDisconnect")
	public published  void onDisconnect (Int channelId, Str serverAddr, Str clientAddr, Str clientCertCn, Str sid, Str callbackPid, Str callbackWid) {
		traceDebug("OnDisconnect: sid = " + sid + ", callbackPid = " + callbackPid + ", callbackWid = " + callbackWid, false);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:onRecv-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:onRecv")
	public published  org.radixware.schemas.netporthandler.OnRecvRsDocument onRecv (Int channelId, Str serverAddr, Str clientAddr, Str clientCertCn, Str sid, Str callbackPid, Str callbackWid, java.util.Map<Str,Str> recvPacketHeaders, org.radixware.kernel.common.types.Bin recvPacket, Bool connected) throws org.radixware.kernel.common.exceptions.AppException {
		getPipeline().refreshCache();
		if (!getPipeline().getChecked()) {
		    if (checkTraceLevel(Arte::EventSeverity:Debug))
		        traceWarning("Pipeline not checked");
		    else
		        throw new AppError("Pipeline not checked");
		}

		Xml xData = ServiceBus::SbServerUtil.castToBinBase64(recvPacket.get());
		outRqData = ServiceBus::SbCommonUtil.copyXml(xData);

		ServiceBus::SbXsd:PipelineMessageRq rq =
		        ServiceBus::SbServerUtil.prepareNextRequest(null, OUT, outbConnectId, xData);

		ServiceBus::SbXsd:ExtHeader.Net xNet = rq.InExtHeader.addNewNet();
		xNet.ServerAddress = serverAddr;
		xNet.ClientAddress = clientAddr;
		xNet.ClientCertCn = clientCertCn;

		rq.InExtHeader.HttpHeader = ServiceBus::SbServerUtil.mapToXml(recvPacketHeaders);
		rq.InExtHeader.NodeEntityId = Str.valueOf(Pid.EntityId);
		rq.InExtHeader.NodeEntityPid = Str.valueOf(Pid);
		rq.Event = ServiceBus::PipelineEvent:Rq;

		Str sRecvPacket = recvPacket.get() == null ? "" : new Str(recvPacket.get());
		traceDebug("OnRecv: recvPacket = " + sRecvPacket + ", sid = " + sid + ", callbackPid = " + callbackPid + ", callbackWid = " + callbackWid, true);
		ServiceBus::SbXsd:PipelineMessageRs rs;
		try {
		    rs = ServiceBus::SbServerUtil.processRequest(outbConnectId, rq);
		    ServiceBus::SbServerUtil.processAfterResponseDelivered(rs);
		} catch (ServiceBus::PipelineException e) {
		    throw new AppException("Pipeline processing exception: " + e.getMessage(), e);
		} catch (Exceptions::Exception e) {
		    if (e instanceof Exceptions::RuntimeException)
		        throw (Exceptions::RuntimeException)e;
		    throw new AppError(e.getMessage(), e);    
		}

		outRsData = ServiceBus::SbCommonUtil.copyXml(rs.CurStage.Data);
		Net::NetPortHandlerXsd:OnRecvRsDocument recvRsDoc = Net::NetPortHandlerXsd:OnRecvRsDocument.Factory.newInstance();
		Net::NetPortHandlerXsd:OnRecvRsDocument.OnRecvRs recvRs = recvRsDoc.addNewOnRecvRs();
		recvRs.setCallbackPid(callbackPid);
		recvRs.setCallbackWid(callbackWid);

		byte[] sendPacket = ServiceBus::SbServerUtil.castToBytes(rs.CurStage.Data);
		if (sendPacket != null)
		    recvRs.setSendPacket(sendPacket);

		recvRs.setRecvTimeout(recvTimeout);
		recvRs.setCloseDelay(keepConnectTimeout);

		if (rs.OutExtHeader.HttpHeader != null)
		    recvRs.SendPackedHeaders = (Arte::TypesXsd:MapStrStr)rs.OutExtHeader.HttpHeader.copy();

		try {
		    if (toKeepConnectFunc != null)
		        recvRs.setCloseDelay(toKeepConnectFunc.calcKeepConnect(this, recvPacket.get(), sendPacket));
		} catch (ServiceBus::PipelineException e) {
		    throw new AppException("Keep connection calculation exception: " + e.getMessage(), e);
		} catch (Exceptions::Exception e) {
		    if (e instanceof Exceptions::RuntimeException)
		        throw (Exceptions::RuntimeException)e;
		    throw new AppError(e.getMessage(), e);    
		}

		traceDebug("OnRecv: sendPacket " + (sendPacket == null ? "" : new Str(sendPacket)), true);
		return recvRsDoc;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceDebug-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceDebug")
	public published  void traceDebug (Str mess, boolean sensitive) {
		trace(Arte::EventSeverity:Debug, mess, sensitive);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceError-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceError")
	public published  void traceError (Str mess) {
		trace(Arte::EventSeverity:Error, mess, false);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceEvent-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceEvent")
	public published  void traceEvent (Str mess) {
		trace(Arte::EventSeverity:Event, mess, false);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:trace-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:trace")
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

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceWarning-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceWarning")
	public published  void traceWarning (Str mess) {
		trace(Arte::EventSeverity:Warning, mess, false);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:onRecvTimeout-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:onRecvTimeout")
	public published  org.radixware.schemas.netporthandler.OnRecvTimeoutRsDocument onRecvTimeout (Int channelId, Str serverAddr, Str clientAddr, Str clientCertCn, Str sid, Str callbackPid, Str callbackWid) throws org.radixware.kernel.common.exceptions.AppException {
		traceDebug("OnRecvTimeout: sid = " + sid + ", callbackPid = " + callbackPid + ", callbackWid = " + callbackWid, false);
		Net::NetPortHandlerXsd:OnRecvTimeoutRsDocument doc = Net::NetPortHandlerXsd:OnRecvTimeoutRsDocument.Factory.newInstance();
		Net::NetPortHandlerXsd:OnRecvTimeoutRsDocument.OnRecvTimeoutRs rs = doc.addNewOnRecvTimeoutRs();
		rs.setCallbackPid(callbackWid);
		rs.setCallbackWid(callbackPid);
		rs.setRecvTimeout(recvTimeout);
		return doc;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getConnectorSide-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getConnectorSide")
	public published  org.radixware.ads.ServiceBus.common.Side getConnectorSide (Str role) {
		if (role == OUT)
		    return ServiceBus::Side:Right;
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:checkTraceLevel-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:checkTraceLevel")
	public published  boolean checkTraceLevel (org.radixware.kernel.common.enums.EEventSeverity severity) {
		if (traceLevel == null)
		    traceLevel = Arte::Trace.getMinSeverity(Arte::EventSource:ServiceBus);

		if (severity == Arte::EventSeverity:Debug)
		    return traceLevel.intValue() == Arte::EventSeverity:Debug.Value.intValue();

		return traceLevel.intValue() <= severity.Value.intValue();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:onConnectTimeout-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:onConnectTimeout")
	public published  org.radixware.schemas.netporthandler.OnConnectTimeoutRsDocument onConnectTimeout (Int channelId, Str serverAddr, Str clientAddr, Str clientCertCn, Str sid, Str callbackPid, Str callbackWid) throws org.radixware.kernel.common.exceptions.AppException {
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getOrderPos-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getOrderPos")
	public static published  org.radixware.ads.ServiceBus.common.SbPos getOrderPos () {
		return new SbPos(1.2);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getPipelineNodeParam-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getPipelineNodeParam")
	public published  Str getPipelineNodeParam (Str name) {
		final java.util.Map<Str,Str> params = getPipeline().getParams();
		if (params != null)
		    params.get(name);
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:export-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:export")
	public published  org.radixware.ads.ServiceBus.common.ImpExpXsd.Node export () {
		ServiceBus::ImpExpXsd:Node xNode = ServiceBus::ImpExpXsd:Node.Factory.newInstance();
		Common::CommonXsd:EditableProperties xProps = Common::CommonXsd:EditableProperties.Factory.newInstance();
		Common::CommonXsd:EditableProperty xProp = xProps.addNewItem();
		xProp.Id = idof[Endpoint.Net.Server:netPortHandler];
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
		    Arrays.asList(idof[Endpoint.Net.Server:pipelineEntityGuid], idof[Endpoint.Net.Server:pipelineEntityPid], idof[Endpoint.Net.Server:extGuid]),
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

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:setPipeline-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:setPipeline")
	public published  void setPipeline (org.radixware.ads.ServiceBus.server.IPipeline pipeline) {
		pipelineEntityGuid = Str.valueOf(pipeline.getHolderEntityPid().EntityId);
		pipelineEntityPid = Str.valueOf(pipeline.getHolderEntityPid());
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:refreshCache-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:refreshCache")
	public published  void refreshCache () {
		traceLevel = null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceDebugXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceDebugXml")
	public published  void traceDebugXml (Str mess, org.apache.xmlbeans.XmlObject data, boolean sensitive) {
		trace(Arte::EventSeverity:Debug, (mess != null ? mess + ": " : "") + (data == null ? "null" : data.toString()), sensitive);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:import-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:import")
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

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:needCaching-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:needCaching")
	public published  boolean needCaching () {
		return false;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getLastInRqData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getLastInRqData")
	public published  org.apache.xmlbeans.XmlObject getLastInRqData () {
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getLastInRsData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getLastInRsData")
	public published  org.apache.xmlbeans.XmlObject getLastInRsData () {
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getLastOutRqData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getLastOutRqData")
	public published  org.apache.xmlbeans.XmlObject getLastOutRqData () {
		return outRqData;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getLastOutRsData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getLastOutRsData")
	public published  org.apache.xmlbeans.XmlObject getLastOutRsData () {
		return outRsData;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:afterImport-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:afterImport")
	public published  void afterImport (java.util.Map<org.radixware.kernel.server.types.Pid,org.radixware.kernel.server.types.Pid> pids) {
		// do nothing
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:beforeDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:beforeDelete")
	protected published  boolean beforeDelete () {
		onUpdate();
		return super.beforeDelete();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:beforeUpdate")
	protected published  boolean beforeUpdate () {
		onUpdate();
		return super.beforeUpdate();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:beforeCreate")
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		onUpdate();
		return super.beforeCreate(src);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:afterUpdatePropObject-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:afterUpdatePropObject")
	protected published  void afterUpdatePropObject (org.radixware.kernel.common.types.Id propId, org.radixware.kernel.server.types.Entity propVal) {
		onUpdate();
		super.afterUpdatePropObject(propId, propVal);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:onUpdate-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:onUpdate")
	public published  void onUpdate () {
		getPipeline().onUpdate();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:getExtGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:getExtGuid")
	public published  Str getExtGuid () {
		if (extGuid == null)
		    extGuid = Arte::Arte.generateGuid();
		return extGuid;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:setExtGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:setExtGuid")
	public published  void setExtGuid (Str extGuid) {
		extGuid = extGuid;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:afterInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:afterInit")
	protected published  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		super.afterInit(src, phase);
		if (extGuid == null || (src != null && ((Endpoint.Net.Server)src).extGuid == extGuid))
		    extGuid = Arte::Arte.generateGuid();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:isSuitableForPipeline-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:isSuitableForPipeline")
	public published  boolean isSuitableForPipeline (org.radixware.ads.ServiceBus.server.IPipeline pipeline) {
		return !pipeline.isTransformation();
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::ServiceBus.Nodes::Endpoint.Net.Server - Server Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Server-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.Net.Server_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),"Endpoint.Net.Server",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL5EHENUNJBAZVERRZAYIDBNIOY"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
							/*Owner Class Name*/
							"Endpoint.Net.Server",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL5EHENUNJBAZVERRZAYIDBNIOY"),
							/*Property presentations*/

							/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruF3BUCTCZANAODNE2VMK5FDHKCQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityPid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZ4JNO7DUGZALRHRYBXBROBPT4Y"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruSNMZTZA6DRBA5OGQRGQCEXYCYU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdWYS3OIUU3FGTXO6CYJEVNAEJUE"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYFQ26TIS7VFRFPZXXHZRW6VGN4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:rqType:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdV3UU47WMM5EB7L4MLJA52FVNZ4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:rsType:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7U4JQT5Y3JAHZBF6HMWXIMQDPU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdESTRGITXENGBBFVJPOSYCLGSOE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityPid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVQBLI4FWIZBVJDYHQYW4UW3XGI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:netPortHandler:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRDRSM3ANLZFOTAZYPRNAMWIXUI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colEWJT6TIHR5VDBNSIAAUMFADAIA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:ConstValue EnumId=\"acsLSG35D4GIHNRDJIEACQMTAIZT4\" ItemId=\"aciK2BEJTYZVPORDJHCAANE2UAFXA\"/></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(523263,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:toKeepConnectFunc:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2C5HDILW3NCRHMUSH2VYY5OVAQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},org.radixware.kernel.common.types.Id.Factory.loadFrom("eccZMCDFRDL4DOBDIEAAALOMT5GDM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceLevel:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdG3B4CWTVUZFW7D6YZI4LBL6E5Y"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:isConnectReadyNtfOn:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUUCCDQOTEJG77LPBXMHD6QDK5E"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRqData:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXPVRJGUDSBD3DHEA2JU4HFGL4M"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRsData:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3D27U657D5DU3OSSTE4RDWCLZU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:extGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruTXGNALPOOFFMNBW4EY5XRBTB7I"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
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
									/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEJ42SXIISVAFTPDVZS3D65M24U"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCNRWSZS2HNRDB7BAALOMT5GDM"),
									35890,
									null,

									/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCNRWSZS2HNRDB7BAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEJ42SXIISVAFTPDVZS3D65M24U")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							null,
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWCPOX5N4HJCPVN4F7WUG2XEUF4"),

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityGuid-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruF3BUCTCZANAODNE2VMK5FDHKCQ"),"pipelineEntityGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls55PWMJIHRRGJPNZRMHXUHKL6GE"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityPid-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZ4JNO7DUGZALRHRYBXBROBPT4Y"),"pipelineEntityPid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZKPYZPN2AVB6JHBH3UGPSDGHDE"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectId-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruSNMZTZA6DRBA5OGQRGQCEXYCYU"),"outbConnectId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3S3DEVDTJNC77LRT6FCOM4U3HU"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdWYS3OIUU3FGTXO6CYJEVNAEJUE"),"pipelineEntityTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4OMOWV2WK5FJFPGEFISEFGN454"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYFQ26TIS7VFRFPZXXHZRW6VGN4"),"outbConnectTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE2I5TL53MVDJDOO6CZCD6X2TBA"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:rqType-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdV3UU47WMM5EB7L4MLJA52FVNZ4"),"rqType",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:rsType-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7U4JQT5Y3JAHZBF6HMWXIMQDPU"),"rsType",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdESTRGITXENGBBFVJPOSYCLGSOE"),"holderEntityId",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityPid-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVQBLI4FWIZBVJDYHQYW4UW3XGI"),"holderEntityPid",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:netPortHandler-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRDRSM3ANLZFOTAZYPRNAMWIXUI"),"netPortHandler",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTL2MVL75QRE5HCDD7RBLV4DPX4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refLP2PLKPZ3DNRDISQAAAAAAAAAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:toKeepConnectFunc-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2C5HDILW3NCRHMUSH2VYY5OVAQ"),"toKeepConnectFunc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGVRAZWTLSZCWJBSLOZP75K2IIY"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUEGT6YCHSNGDRPGY4TKY7XNB7I"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceLevel-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdG3B4CWTVUZFW7D6YZI4LBL6E5Y"),"traceLevel",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:isConnectReadyNtfOn-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUUCCDQOTEJG77LPBXMHD6QDK5E"),"isConnectReadyNtfOn",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP4QZHHMPS5CIVHO6ESKJYF2WSA"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRqData-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXPVRJGUDSBD3DHEA2JU4HFGL4M"),"outRqData",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRsData-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3D27U657D5DU3OSSTE4RDWCLZU"),"outRsData",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:extGuid-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruTXGNALPOOFFMNBW4EY5XRBTB7I"),"extGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTO3N6ZUC25BNLNVHR6WUL3OSCU"),"afterResponseDelivered",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSUSL3UZKDVAYLBJ4CF7X4BEGBI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOC7IFQSQQNCXLAY4YULNTEVVL4"),"checkConfig",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRJJVN7SZFRGFPKSWMK5OODDPHM"),"connect",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("outConnectorRole",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDMJHOPQNSRBXRI6IPFZA52XXZY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("inConnectorId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDTJKGD3N6FDIXHJHS72DTHZZII"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZEZONIMUSNHEJHOFQ7OPMP3CLU"),"getConnectorIconId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAWIWEBIETVCPNDX2WWHS2ADTXQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFST4X6BGFNA5PH6LQNOG73MSTQ"),"getConnectorId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprREYQGCQZA5BN5JOGT3XBFWQIXE"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWE5I24A36RE3BGGGIIG4FYAV7M"),"getConnectorRole",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEVHJFLXFT5AO5I7IRJXOCO7XOI"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEIEXINMQGJB35G65UDWFOOSDZU"),"getConnectorRqDataType",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprU7UW5CCAYNH4XJUFFI6S4OKBGA"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHZIY6G7TCVCX3DR6QWA77MJJHI"),"getConnectorRsDataType",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIZ6RQUK4VZAVNO6D76LVS2RDPU"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLB23XTDDKVGSRN5R4BPLLXUMFY"),"getConnectorTitle",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJFUKGZ5P5ZFINNPBXLQGR5EDKI"))
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

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLWALDKKGBNEODDX6XYY7JKKALE"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLILYOANG5RBVTHLQIJLYCSDJM4"),"getCreatingPresentationId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIOMPW3QBZ5ASTPONSTPGMH5ZHQ"),"getEditingPresentationId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGUAPALQ5DJBAFAWDIAXWPKXUNY"),"onConnect",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("channelId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBVH32FPLNZDFVMKB7H35MWEUL4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("serverAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNQB5XEP7GFALVBG6Q26FONENXE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr25UZR67FSNF2VCAIMEMXXOACSQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientCertCn",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJTHYVUOLJNDLNM6HOOZMHG7LWI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr627AZ6XMNBDUZFJWJSHUA3FGEQ"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthO6EBWEP46VFXTAYRWGTHPX35WI"),"onDisconnect",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("channelId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDC7I7AJTSVFNXNYWS55DFJUNCA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("serverAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2F5KUMH6XFH6JPFLTRCR6H2BK4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr266YQQOPJBFDHFOUHOCUYHSPZA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientCertCn",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQU2ZYBMVORD6JAJEFLJSEGDV5M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3RYQ2QHL7RAKVISROVPP5CGPOY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackPid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIDJWQW6REZG5PPYTUO436Q5RKE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackWid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprO2Z62QFMCFAVJDRXFV3DJXRY4U"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBZAATQUT4BAS7MPBI3VBZ6QNEI"),"onRecv",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("channelId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWFZORVZQENF25MSMSGPTAQV4GM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("serverAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2IQ747TAGRGFZHWFX6W2QWQFQM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5QEEIL4I6ZHZVMC7DRF2LYFLU4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientCertCn",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprITGP3YGMTJBGBCRJ42BFBTCPBM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYA2VQEOLOBEGXBTCKWWFV5N4HI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackPid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6KCKWUJOGFCMPNNBT4WTIBARHQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackWid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSRW3NF5ZAZASLHT5OA3KG6C2IQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("recvPacketHeaders",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFLZPEXDUNRAJPH7AOVJVJEFA3E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("recvPacket",org.radixware.kernel.common.enums.EValType.BIN,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprS7NOO7HIFBCATCLWHKXLBWMTJQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("connected",org.radixware.kernel.common.enums.EValType.BOOL,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZWHTY7AOIZEGRB2I36QQWAIWYU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWQC7HI6DMJB33MVZT5VNNK6LR4"),"traceDebug",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYFJQTTBLXFHJ7I4VZP2MKGVPXU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sensitive",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW336XA2N3VAVDMHW7IXMLRWH2Q"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQ7MQNDFPEVCSZM46CIFMM4ISAI"),"traceError",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFK64O4Y35ZCQBG4T3KXE4EOGTY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNY56F4MVVVAABKIBM7GQ52OD5U"),"traceEvent",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNUVTTTSHHBGBDCABPECWGUFEBI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNLROOCGTF5CUVM66WIOJDRSHCE"),"trace",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("severity",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVD62K47B7BAELNY32P4PHPLY7Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr36GURMONIZECLGCVJ66AHMB5KQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sensitive",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLZ7MJVXZ7RGDJPJRXCSHEMUWPM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOYF3L7CFTFB27GLSUU7QWQ44PM"),"traceWarning",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSI7OID2TTZALPBRO7QDSNOO7I4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSC2RWSPCHVHJZL6TN5L5ZPUXBY"),"onRecvTimeout",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("channelId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLR7G5QQ5ARD5HJAVUK7DGPN2CQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("serverAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUOAB55YP35CP5JOYLLQ6UGHZKY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprY7FWEQ7ASBGMHOSHEB7IZV6EDI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientCertCn",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5ILBTFWVXFDM3LRXQRR3N2P2LY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNT5CLRIN4FGCTNSRZQ7UORII2M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackPid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIU6SMT7LA5CZZM7U4SW5FFQAT4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackWid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMNKDBZKMYVH2PAZXTJDW4KZOLM"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthB25WTYWZUJAJXMCSH7UVSSJMP4"),"getConnectorSide",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprT547YSNUGJHE5A6DV4Q5WSBZ3I"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7JXSRN7HJNANHAN7CZOA2VSTJU"),"checkTraceLevel",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("severity",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZGN3HCVYUNBU5MTJJWXPIPS5NM"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPGZZ43N6ZJBBFBXOPUQKOYSBLQ"),"onConnectTimeout",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("channelId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYMHBOQ43AVAJ5M53LD4TONSW5I")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("serverAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2IG7VFVJ2BE2FOIQPPLCDSDEBY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZSUBXF7U6BFWHMR2FPPNNET5QU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientCertCn",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEUCS54MWD5FIVGINTFY4MKQJOM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVTZRRI4DKVCTHAI6EARFH5572U")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackPid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6QWWFSCI75DHTFNNPKTABNXD4M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackWid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZHFBE6IFE5DGXLUPPXVGMM4V3Q"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAXJJZNLFOJGVBNHZN5AEP5GJGM"),"getOrderPos",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPI4ONCPYCFAIJNIXF3ZKSVPAYM"),"getPipelineNodeParam",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("name",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCN3YRUPAS5CP3C3HQJIYSH7F3A"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSJVD663SIFFXVLMYDV2Y2VU544"),"export",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthG2W65RASFFH7RIEA37LOBPKUFI"),"setPipeline",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pipeline",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4KEUCDKDL5GWBL2GZCV6NJBKRE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthM7KLX4PIANEH3NYQZZSZ55GMIE"),"refreshCache",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth32XUVPFG2VH6ZJZNOLQBQC7NUM"),"traceDebugXml",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprF7JEO2QAXVDSRHHFPZK65CCB6E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprT537F6LIAZEI5EVV65Q3KIX7AE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sensitive",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPDU3Y66W4FHJ3K5EKESFPWEI7U"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKMN6A6YHPVBNFON237GIJY4ZPU"),"import",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xNode",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEMYR4I3QK5CMLHWFDJB74VDEPA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("overwriteLocalSettings",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW32LJOB3NNFEFIPDSOK7QQAVSI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4MI7KCNM4VDRBK6SQGFBO3JLSA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3VOSK24FZVFTLKSZISQ3XGMJDE"),"needCaching",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNYO5JRURNRFWLFFLCVODWPOTLI"),"getLastInRqData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6XF3CCTG7NGA7O7SEKOQTWLIHE"),"getLastInRsData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLC5CACZEKVCFHEZPWGJ7AN732I"),"getLastOutRqData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6D7TO5XHNRCCZMYQJQZMIWCSUQ"),"getLastOutRsData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBXD5LT3S2VA2ZGTSBZIOEVFN2I"),"afterImport",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pids",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJFV2RY7SNRHNVFPJWKZXJ7SYCY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNZ5Z7AS6RFRPIX6ZD5JDKEC34"),"beforeDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQUC72A32ZFDL3JU3CQJOLDZ3VQ"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAZ2HAURWXVB53HEDENC67YLQAU"),"afterUpdatePropObject",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXDGW3K54JBDRXADI2FETQWDLDQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propVal",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKTYOMEZVSZFHTJA2TF7GFA2OAY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthS4RO7GZ2YFDY3ASVJZRUE6EQJU"),"onUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthW4LHL5WOQJF7NK3VL3LMQETFTI"),"getExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZVCMFP66RZEELGIQECRMCICA3E"),"setExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEH3SFXOVPNHOFOE6URICU33PQ4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW3DR47FBFFBYRAJ5KEN5IRVL6I")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEGPWQLV4UJH6XJYQSEWTRZSUIM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNSQ7CEKCGFFWRLVESA72LZ7DEM"),"isSuitableForPipeline",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pipeline",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNMEQL2QUAJCW5BFEYBGZVBCXQU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

/* Radix::ServiceBus.Nodes::Endpoint.Net.Server - Desktop Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Server-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server")
public interface Endpoint.Net.Server   extends org.radixware.ads.Net.explorer.NetListener  {































































































































































































































	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:netPortHandler:netPortHandler-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:netPortHandler:netPortHandler")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:netPortHandler:netPortHandler")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public NetPortHandler getNetPortHandler();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRsData:outRsData-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRsData:outRsData")
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRsData:outRsData")
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public OutRsData getOutRsData();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:rsType:rsType-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:rsType:rsType")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:rsType:rsType")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RsType getRsType();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityId:holderEntityId-Presentation Property*/


	public class HolderEntityId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public HolderEntityId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityId:holderEntityId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityId:holderEntityId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public HolderEntityId getHolderEntityId();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceLevel:traceLevel-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceLevel:traceLevel")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceLevel:traceLevel")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public TraceLevel getTraceLevel();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:rqType:rqType-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:rqType:rqType")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:rqType:rqType")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RqType getRqType();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityPid:holderEntityPid-Presentation Property*/


	public class HolderEntityPid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public HolderEntityPid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityPid:holderEntityPid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityPid:holderEntityPid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public HolderEntityPid getHolderEntityPid();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityTitle:pipelineEntityTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityTitle:pipelineEntityTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityTitle:pipelineEntityTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityTitle getPipelineEntityTitle();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRqData:outRqData-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRqData:outRqData")
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRqData:outRqData")
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public OutRqData getOutRqData();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectTitle:outbConnectTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectTitle:outbConnectTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectTitle:outbConnectTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OutbConnectTitle getOutbConnectTitle();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:toKeepConnectFunc:toKeepConnectFunc-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:toKeepConnectFunc:toKeepConnectFunc")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:toKeepConnectFunc:toKeepConnectFunc")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ToKeepConnectFunc getToKeepConnectFunc();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityGuid:pipelineEntityGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityGuid:pipelineEntityGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityGuid:pipelineEntityGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityGuid getPipelineEntityGuid();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectId:outbConnectId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectId:outbConnectId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectId:outbConnectId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OutbConnectId getOutbConnectId();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:extGuid:extGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:extGuid:extGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:extGuid:extGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtGuid getExtGuid();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityPid:pipelineEntityPid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityPid:pipelineEntityPid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityPid:pipelineEntityPid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityPid getPipelineEntityPid();


}

/* Radix::ServiceBus.Nodes::Endpoint.Net.Server - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Server-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.Net.Server_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
			"Radix::ServiceBus.Nodes::Endpoint.Net.Server",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWCPOX5N4HJCPVN4F7WUG2XEUF4"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL5EHENUNJBAZVERRZAYIDBNIOY"),null,null,0,

			/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruF3BUCTCZANAODNE2VMK5FDHKCQ"),
						"pipelineEntityGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls55PWMJIHRRGJPNZRMHXUHKL6GE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZ4JNO7DUGZALRHRYBXBROBPT4Y"),
						"pipelineEntityPid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZKPYZPN2AVB6JHBH3UGPSDGHDE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruSNMZTZA6DRBA5OGQRGQCEXYCYU"),
						"outbConnectId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3S3DEVDTJNC77LRT6FCOM4U3HU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdWYS3OIUU3FGTXO6CYJEVNAEJUE"),
						"pipelineEntityTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4OMOWV2WK5FJFPGEFISEFGN454"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYFQ26TIS7VFRFPZXXHZRW6VGN4"),
						"outbConnectTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE2I5TL53MVDJDOO6CZCD6X2TBA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:rqType:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdV3UU47WMM5EB7L4MLJA52FVNZ4"),
						"rqType",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:rqType:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:rsType:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7U4JQT5Y3JAHZBF6HMWXIMQDPU"),
						"rsType",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:rsType:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdESTRGITXENGBBFVJPOSYCLGSOE"),
						"holderEntityId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVQBLI4FWIZBVJDYHQYW4UW3XGI"),
						"holderEntityPid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:netPortHandler:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRDRSM3ANLZFOTAZYPRNAMWIXUI"),
						"netPortHandler",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTL2MVL75QRE5HCDD7RBLV4DPX4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},
						null,
						null,
						523263,
						133693439,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:toKeepConnectFunc:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2C5HDILW3NCRHMUSH2VYY5OVAQ"),
						"toKeepConnectFunc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGVRAZWTLSZCWJBSLOZP75K2IIY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceLevel:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdG3B4CWTVUZFW7D6YZI4LBL6E5Y"),
						"traceLevel",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceLevel:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:isConnectReadyNtfOn:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUUCCDQOTEJG77LPBXMHD6QDK5E"),
						"isConnectReadyNtfOn",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						7,
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:isConnectReadyNtfOn:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRqData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXPVRJGUDSBD3DHEA2JU4HFGL4M"),
						"outRqData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRsData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3D27U657D5DU3OSSTE4RDWCLZU"),
						"outRsData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:extGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruTXGNALPOOFFMNBW4EY5XRBTB7I"),
						"extGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:extGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
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
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEJ42SXIISVAFTPDVZS3D65M24U")},
			true,true,false);
}

/* Radix::ServiceBus.Nodes::Endpoint.Net.Server - Web Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Server-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server")
public interface Endpoint.Net.Server   extends org.radixware.ads.Net.web.NetListener  {



















































































































































































































	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:netPortHandler:netPortHandler-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:netPortHandler:netPortHandler")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:netPortHandler:netPortHandler")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public NetPortHandler getNetPortHandler();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRsData:outRsData-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRsData:outRsData")
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRsData:outRsData")
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public OutRsData getOutRsData();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:rsType:rsType-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:rsType:rsType")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:rsType:rsType")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RsType getRsType();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityId:holderEntityId-Presentation Property*/


	public class HolderEntityId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public HolderEntityId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityId:holderEntityId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityId:holderEntityId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public HolderEntityId getHolderEntityId();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceLevel:traceLevel-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceLevel:traceLevel")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceLevel:traceLevel")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public TraceLevel getTraceLevel();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:rqType:rqType-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:rqType:rqType")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:rqType:rqType")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RqType getRqType();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityPid:holderEntityPid-Presentation Property*/


	public class HolderEntityPid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public HolderEntityPid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityPid:holderEntityPid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityPid:holderEntityPid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public HolderEntityPid getHolderEntityPid();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityTitle:pipelineEntityTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityTitle:pipelineEntityTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityTitle:pipelineEntityTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityTitle getPipelineEntityTitle();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRqData:outRqData-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRqData:outRqData")
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRqData:outRqData")
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public OutRqData getOutRqData();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectTitle:outbConnectTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectTitle:outbConnectTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectTitle:outbConnectTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OutbConnectTitle getOutbConnectTitle();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityGuid:pipelineEntityGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityGuid:pipelineEntityGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityGuid:pipelineEntityGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityGuid getPipelineEntityGuid();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectId:outbConnectId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectId:outbConnectId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectId:outbConnectId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OutbConnectId getOutbConnectId();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:extGuid:extGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:extGuid:extGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:extGuid:extGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtGuid getExtGuid();
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityPid:pipelineEntityPid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityPid:pipelineEntityPid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityPid:pipelineEntityPid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityPid getPipelineEntityPid();


}

/* Radix::ServiceBus.Nodes::Endpoint.Net.Server - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Server-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.Net.Server_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
			"Radix::ServiceBus.Nodes::Endpoint.Net.Server",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclWCPOX5N4HJCPVN4F7WUG2XEUF4"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL5EHENUNJBAZVERRZAYIDBNIOY"),null,null,0,

			/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruF3BUCTCZANAODNE2VMK5FDHKCQ"),
						"pipelineEntityGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls55PWMJIHRRGJPNZRMHXUHKL6GE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZ4JNO7DUGZALRHRYBXBROBPT4Y"),
						"pipelineEntityPid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZKPYZPN2AVB6JHBH3UGPSDGHDE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruSNMZTZA6DRBA5OGQRGQCEXYCYU"),
						"outbConnectId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3S3DEVDTJNC77LRT6FCOM4U3HU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdWYS3OIUU3FGTXO6CYJEVNAEJUE"),
						"pipelineEntityTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4OMOWV2WK5FJFPGEFISEFGN454"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:pipelineEntityTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYFQ26TIS7VFRFPZXXHZRW6VGN4"),
						"outbConnectTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE2I5TL53MVDJDOO6CZCD6X2TBA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outbConnectTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:rqType:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdV3UU47WMM5EB7L4MLJA52FVNZ4"),
						"rqType",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:rqType:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:rsType:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7U4JQT5Y3JAHZBF6HMWXIMQDPU"),
						"rsType",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:rsType:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdESTRGITXENGBBFVJPOSYCLGSOE"),
						"holderEntityId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVQBLI4FWIZBVJDYHQYW4UW3XGI"),
						"holderEntityPid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:holderEntityPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:netPortHandler:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRDRSM3ANLZFOTAZYPRNAMWIXUI"),
						"netPortHandler",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTL2MVL75QRE5HCDD7RBLV4DPX4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},
						null,
						null,
						523263,
						133693439,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:toKeepConnectFunc:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2C5HDILW3NCRHMUSH2VYY5OVAQ"),
						"toKeepConnectFunc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGVRAZWTLSZCWJBSLOZP75K2IIY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceLevel:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdG3B4CWTVUZFW7D6YZI4LBL6E5Y"),
						"traceLevel",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:traceLevel:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:isConnectReadyNtfOn:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUUCCDQOTEJG77LPBXMHD6QDK5E"),
						"isConnectReadyNtfOn",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						7,
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:isConnectReadyNtfOn:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRqData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXPVRJGUDSBD3DHEA2JU4HFGL4M"),
						"outRqData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:outRsData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3D27U657D5DU3OSSTE4RDWCLZU"),
						"outRsData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

					/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:extGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruTXGNALPOOFFMNBW4EY5XRBTB7I"),
						"extGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:extGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
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
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEJ42SXIISVAFTPDVZS3D65M24U")},
			true,true,false);
}

/* Radix::ServiceBus.Nodes::Endpoint.Net.Server:General - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEJ42SXIISVAFTPDVZS3D65M24U"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCNRWSZS2HNRDB7BAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5D224VGZSBAAJEZTZ44Y3MXIYE"),
	null,

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5562VEGTBXOBDNTPAAMPGXSZKU"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLGQND6UJLRCLXDMDHCEHJAQVG4"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKA3TGQME2DNRDB7AAALOMT5GDM"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKE3TGQME2DNRDB7AAALOMT5GDM"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ2GUXJUE2DNRDB7AAALOMT5GDM"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGRXTHMME2DNRDB7AAALOMT5GDM"),0,13,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colF3F5Y5EF2DNRDB7AAALOMT5GDM"),0,14,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB3IQTAEF2DNRDB7AAALOMT5GDM"),0,24,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVGDODIF6BLOBDCGJAALOMT5GDM"),0,15,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKSQETMV6BLOBDCGJAALOMT5GDM"),0,16,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colH2W5NJG535G7TCZERIU3I2KW6I"),0,19,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6OTLV7WBZVGDFM7SYNM5KXYSGQ"),0,20,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSOCMOJR6SRCGBL63P2ALGCKNUQ"),0,21,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5R5UG4B5JEUPMEJLRID7SISTY"),0,22,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdWYS3OIUU3FGTXO6CYJEVNAEJUE"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYFQ26TIS7VFRFPZXXHZRW6VGN4"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2C5HDILW3NCRHMUSH2VYY5OVAQ"),0,18,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7QM5XU6LIJHIPK2G47EWNFLRF4"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRDRSM3ANLZFOTAZYPRNAMWIXUI"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB3UVBAV46ZHJ5POIV5R3JDFQ2E"),0,17,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTFACUXG6NJAMNJZ33CKFZTYORY"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJWZOTJWGMFBWDMHWTBSXO6MZNY"),0,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col22YVF3LEOFE5BCJXA5KDPU73NA"),0,23,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4QBLQUIZAZFQPILQILYXGOSMGM"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRKQ2IQSPJDUJFZWAVDVZYN6ZI"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVZZGWK24VGBBAQGW4QAVPJ4GY"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN7MFLE4DNJB3RIQUOC2I37BXPI"),0,25,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGEUOICAGRBAU3NBME7T63HEL7I"),0,1,1,false,false)
			},null),

			/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:TraceOptions-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgCWCNH76WQNGY5IUPVIUKKE4CCQ"),"TraceOptions",org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS4BUAZUBG5EI7GO6SISBA3X5QY"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXOEZ5O4YNXOBDCJJAALOMT5GDM"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVKQVABX6FH7PAEUHL7URGEEAY"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNXKEWDHZK5CILMRUHOP7KW46Q4"),0,2,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5562VEGTBXOBDNTPAAMPGXSZKU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgCWCNH76WQNGY5IUPVIUKKE4CCQ")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg2OZOQGDAB5G2XP3FXEHO6W53BQ"))}
	,

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	35890,0,0);
}
/* Radix::ServiceBus.Nodes::Endpoint.Net.Server:General - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.web;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEJ42SXIISVAFTPDVZS3D65M24U"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCNRWSZS2HNRDB7BAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5D224VGZSBAAJEZTZ44Y3MXIYE"),
	null,

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5562VEGTBXOBDNTPAAMPGXSZKU"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("acl63HU77YZRBBMJOP2LYMKZIFSUI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLGQND6UJLRCLXDMDHCEHJAQVG4"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKA3TGQME2DNRDB7AAALOMT5GDM"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKE3TGQME2DNRDB7AAALOMT5GDM"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ2GUXJUE2DNRDB7AAALOMT5GDM"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGRXTHMME2DNRDB7AAALOMT5GDM"),0,13,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colF3F5Y5EF2DNRDB7AAALOMT5GDM"),0,14,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB3IQTAEF2DNRDB7AAALOMT5GDM"),0,24,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVGDODIF6BLOBDCGJAALOMT5GDM"),0,15,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKSQETMV6BLOBDCGJAALOMT5GDM"),0,16,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colH2W5NJG535G7TCZERIU3I2KW6I"),0,19,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6OTLV7WBZVGDFM7SYNM5KXYSGQ"),0,20,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSOCMOJR6SRCGBL63P2ALGCKNUQ"),0,21,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5R5UG4B5JEUPMEJLRID7SISTY"),0,22,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdWYS3OIUU3FGTXO6CYJEVNAEJUE"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYFQ26TIS7VFRFPZXXHZRW6VGN4"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru2C5HDILW3NCRHMUSH2VYY5OVAQ"),0,18,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7QM5XU6LIJHIPK2G47EWNFLRF4"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRDRSM3ANLZFOTAZYPRNAMWIXUI"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB3UVBAV46ZHJ5POIV5R3JDFQ2E"),0,17,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTFACUXG6NJAMNJZ33CKFZTYORY"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJWZOTJWGMFBWDMHWTBSXO6MZNY"),0,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col22YVF3LEOFE5BCJXA5KDPU73NA"),0,23,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4QBLQUIZAZFQPILQILYXGOSMGM"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRKQ2IQSPJDUJFZWAVDVZYN6ZI"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVZZGWK24VGBBAQGW4QAVPJ4GY"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN7MFLE4DNJB3RIQUOC2I37BXPI"),0,25,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGEUOICAGRBAU3NBME7T63HEL7I"),0,1,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5562VEGTBXOBDNTPAAMPGXSZKU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg2OZOQGDAB5G2XP3FXEHO6W53BQ"))}
	,

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	35890,0,0);
}
/* Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model - Desktop Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model")
public class General:Model  extends org.radixware.ads.ServiceBus.Nodes.explorer.Endpoint.Net.Server.Endpoint.Net.Server_DefaultModel.eprFCNRWSZS2HNRDB7BAALOMT5GDM_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:pipelineEntityTitle-Presentation Property*/




	public class PipelineEntityTitle extends org.radixware.ads.ServiceBus.Nodes.explorer.Endpoint.Net.Server.Endpoint.Net.Server_DefaultModel.eprFCNRWSZS2HNRDB7BAALOMT5GDM_ModelAdapter.prdWYS3OIUU3FGTXO6CYJEVNAEJUE{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:pipelineEntityTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:pipelineEntityTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityTitle getPipelineEntityTitle(){return (PipelineEntityTitle)getProperty(prdWYS3OIUU3FGTXO6CYJEVNAEJUE);}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:outbConnectTitle-Presentation Property*/




	public class OutbConnectTitle extends org.radixware.ads.ServiceBus.Nodes.explorer.Endpoint.Net.Server.Endpoint.Net.Server_DefaultModel.eprFCNRWSZS2HNRDB7BAALOMT5GDM_ModelAdapter.prdYFQ26TIS7VFRFPZXXHZRW6VGN4{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:outbConnectTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:outbConnectTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OutbConnectTitle getOutbConnectTitle(){return (OutbConnectTitle)getProperty(prdYFQ26TIS7VFRFPZXXHZRW6VGN4);}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:syncMode-Presentation Property*/




	public class SyncMode extends org.radixware.ads.ServiceBus.Nodes.explorer.Endpoint.Net.Server.Endpoint.Net.Server_DefaultModel.eprFCNRWSZS2HNRDB7BAALOMT5GDM_ModelAdapter.colPRKQ2IQSPJDUJFZWAVDVZYN6ZI{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:syncMode")
		public published  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:syncMode")
		public published   void setValue(Bool val) {

			internal[syncMode] = val;
			setVisibility();
		}
	}
	public SyncMode getSyncMode(){return (SyncMode)getProperty(colPRKQ2IQSPJDUJFZWAVDVZYN6ZI);}












	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:beforeOpenView")
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

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:setVisibility-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:setVisibility")
	public published  void setVisibility () {
		final boolean isSync = syncMode.Value != null && syncMode.Value.booleanValue();
		curBusySessionCount.setVisible(isSync);
		isCurBusySessionCountOn.setVisible(isSync);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		if (!(getContext() instanceof Explorer.Context::SelectorRowContext)) {
		    setVisibility();
		}
	}


}

/* Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemEJ42SXIISVAFTPDVZS3D65M24U"),
						"General:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemFCNRWSZS2HNRDB7BAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model - Web Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model")
public class General:Model  extends org.radixware.ads.ServiceBus.Nodes.web.Endpoint.Net.Server.Endpoint.Net.Server_DefaultModel.eprFCNRWSZS2HNRDB7BAALOMT5GDM_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:pipelineEntityTitle-Presentation Property*/




	public class PipelineEntityTitle extends org.radixware.ads.ServiceBus.Nodes.web.Endpoint.Net.Server.Endpoint.Net.Server_DefaultModel.eprFCNRWSZS2HNRDB7BAALOMT5GDM_ModelAdapter.prdWYS3OIUU3FGTXO6CYJEVNAEJUE{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:pipelineEntityTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:pipelineEntityTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityTitle getPipelineEntityTitle(){return (PipelineEntityTitle)getProperty(prdWYS3OIUU3FGTXO6CYJEVNAEJUE);}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:syncMode-Presentation Property*/




	public class SyncMode extends org.radixware.ads.ServiceBus.Nodes.web.Endpoint.Net.Server.Endpoint.Net.Server_DefaultModel.eprFCNRWSZS2HNRDB7BAALOMT5GDM_ModelAdapter.colPRKQ2IQSPJDUJFZWAVDVZYN6ZI{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:syncMode")
		public published  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:syncMode")
		public published   void setValue(Bool val) {

			internal[syncMode] = val;
			setVisibility();
		}
	}
	public SyncMode getSyncMode(){return (SyncMode)getProperty(colPRKQ2IQSPJDUJFZWAVDVZYN6ZI);}










	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:beforeOpenView")
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

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:setVisibility-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:setVisibility")
	public published  void setVisibility () {
		final boolean isSync = syncMode.Value != null && syncMode.Value.booleanValue();
		curBusySessionCount.setVisible(isSync);
		isCurBusySessionCountOn.setVisible(isSync);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		if (!(getContext() instanceof Explorer.Context::SelectorRowContext)) {
		    setVisibility();
		}
	}


}

/* Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemEJ42SXIISVAFTPDVZS3D65M24U"),
						"General:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemFCNRWSZS2HNRDB7BAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Endpoint.Net.Server:General:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Endpoint.Net.Server - Localizing Bundle */
package org.radixware.ads.ServiceBus.Nodes.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.Net.Server - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Inbound requests");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Входящие запросы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2WSHNIHSKBDIZJXOO36GFBIZMA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Outbound connection");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Соединение выходного коннектора");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3S3DEVDTJNC77LRT6FCOM4U3HU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pipeline");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конвейер");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4OMOWV2WK5FJFPGEFISEFGN454"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Entity ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls55PWMJIHRRGJPNZRMHXUHKL6GE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Network Channel - Server");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сетевой канал - сервер");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5D224VGZSBAAJEZTZ44Y3MXIYE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsANVOZKCN4ZG2TKP6XEZ42CGTJ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Connector");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Коннектор");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE2I5TL53MVDJDOO6CZCD6X2TBA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Keep connection calculation exception: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Исключение при расчете времени удержания соединения: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGANDZ3EARJCXFOXKFDA4OEKV3Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Keep connection timeout function");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Длительность удержания соединения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGVRAZWTLSZCWJBSLOZP75K2IIY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Invalid \"Keep connection timeout\" function");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Функция  \"Длительность удержания соединения\" некорректна");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHGCHTTBG4VGWDE7Z6MFCV6R2IQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pipeline processing exception: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Исключение при обработке узлов конвейера: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK2KU2SLXKVEU3KTBDBSLZYTM3M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Network Channel - Server");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сетевой канал - сервер");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL5EHENUNJBAZVERRZAYIDBNIOY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLGQND6UJLRCLXDMDHCEHJAQVG4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace Settings");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Настройки трассы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS4BUAZUBG5EI7GO6SISBA3X5QY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pipeline not checked");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конвейер не проверен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSUZNUR6475BT7GRL375VM5CBUQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"System unit");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Модуль системы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTL2MVL75QRE5HCDD7RBLV4DPX4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pipeline not checked");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конвейер не проверен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYARP763KMRESPKD5QQU5Z6ZX34"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Entity PID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ключ сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZKPYZPN2AVB6JHBH3UGPSDGHDE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Endpoint.Net.Server - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacl63HU77YZRBBMJOP2LYMKZIFSUI"),"Endpoint.Net.Server - Localizing Bundle",$$$items$$$);
}
