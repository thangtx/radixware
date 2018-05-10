
/* Radix::ServiceBus.Nodes::Processor.Jml - Server Executable*/

/*Radix::ServiceBus.Nodes::Processor.Jml-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.server;

import java.util.List;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml")
public published class Processor.Jml  extends org.radixware.ads.ServiceBus.server.PipelineNode  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	protected final static Str IN = "In";
	private List<Types::Pid> connectors = null;
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Processor.Jml_mi.rdxMeta;}

	/*Radix::ServiceBus.Nodes::Processor.Jml:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Processor.Jml:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Processor.Jml:process-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:process")
	public published  org.radixware.ads.ServiceBus.Nodes.server.UserFunc.Process getProcess() {
		return process;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:process")
	public published   void setProcess(org.radixware.ads.ServiceBus.Nodes.server.UserFunc.Process val) {
		process = val;
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:outConnectsTitle-Dynamic Property*/



	protected Str outConnectsTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:outConnectsTitle")
	public published  Str getOutConnectsTitle() {

		final java.util.Map<Str,Str> conns = parseConnections();
		final Str[] roles = getOutConnectorRoles();
		if (roles.length == 0)
		    return null;
		    
		int idx = 0;
		Str title = "";
		for (Str role : roles) {
		    Str connectId = conns.get(role);
		    title += (connectId == null) ? "-" : ServiceBus::SbServerUtil.getConnectionTitle(connectId);
		    if (++idx < roles.length)
		        title += "\n";
		}

		return title;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:outConnectsTitle")
	public published   void setOutConnectsTitle(Str val) {
		outConnectsTitle = val;
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:rqType-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:rqType")
	public published  Str getRqType() {
		return rqType;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:rqType")
	public published   void setRqType(Str val) {
		rqType = val;
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:rsType-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:rsType")
	public published  Str getRsType() {
		return rsType;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:rsType")
	public published   void setRsType(Str val) {
		rsType = val;
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:outConnectionIds-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:outConnectionIds")
	public published  Str getOutConnectionIds() {
		return outConnectionIds;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:outConnectionIds")
	public published   void setOutConnectionIds(Str val) {
		outConnectionIds = val;
	}



























































	/*Radix::ServiceBus.Nodes::Processor.Jml:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Processor.Jml:getOutConnectorRoles-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:getOutConnectorRoles")
	public published  Str[] getOutConnectorRoles () {
		final List<Types::Pid> cs = getConnectors();

		int idx = 0;
		Str[] roles = new Str[cs.size()];
		for (Types::Pid pid: cs) {
		    final ServiceBus::PipelineConnector c = (ServiceBus::PipelineConnector)ServiceBus::PipelineConnector.load(pid);    
		    roles[idx++] = c.role;
		}
		return roles;
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:getInConnectorRoles-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:getInConnectorRoles")
	public published  Str[] getInConnectorRoles () {
		return new Str[] { IN };
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:processRequest-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:processRequest")
	public published  org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRs processRequest (org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRq rq) throws org.radixware.ads.ServiceBus.server.PipelineException {
		rq.CurStage.NodeParams = getParams();
		traceDebugXml("Request data", rq.CurStage.Data, true);
		inRqData = ServiceBus::SbCommonUtil.copyXml(rq.CurStage.Data);

		ServiceBus::SbXsd:StageRq curStage = (ServiceBus::SbXsd:StageRq)rq.CurStage.copy();
		ServiceBus::SbXsd:PipelineMessageRs rs = (ServiceBus::SbXsd:PipelineMessageRs) process.process(this, rq);
		rs.CurStage.CorrespondentRq = curStage;

		inRsData = ServiceBus::SbCommonUtil.copyXml(rs.CurStage.Data);
		traceDebugXml("Response data", rs.CurStage.Data, true);
		return rs;
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:getIconId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:getIconId")
	public published  org.radixware.kernel.common.types.Id getIconId () {
		return idof[JMLProcessor];
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:getEditingPresentationId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:getEditingPresentationId")
	public published  org.radixware.kernel.common.types.Id getEditingPresentationId () {
		return idof[Processor.Jml:Edit];
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:getCreatingPresentationId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:getCreatingPresentationId")
	public published  org.radixware.kernel.common.types.Id getCreatingPresentationId () {
		return idof[Processor.Jml:Create];
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:getConnectorTitle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:getConnectorTitle")
	public published  Str getConnectorTitle (Str role) {
		if (role == IN)
		    return "Input";

		final ServiceBus::PipelineConnector c = getConnector(role);
		if (c == null)
		    throw new AppError("Unknown role " + role);

		return c.title;
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:getConnectorRsDataType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:getConnectorRsDataType")
	public published  Str getConnectorRsDataType (Str role) {
		if (role == IN)
		    return rsType;
		    
		final ServiceBus::PipelineConnector c = getConnector(role);
		if (c == null)
		    throw new AppError("Unknown role " + role);

		return c.rsType;
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:getConnectorRqDataType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:getConnectorRqDataType")
	public published  Str getConnectorRqDataType (Str role) {
		if (role == IN)
		    return rqType;
		    
		final ServiceBus::PipelineConnector c = getConnector(role);
		if (c == null)
		    throw new AppError("Unknown role " + role);

		return c.rqType;
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:getConnectorIconId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:getConnectorIconId")
	public published  org.radixware.kernel.common.types.Id getConnectorIconId (Str role) {
		return null;
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:connect-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:connect")
	public published  void connect (Str outConnectorRole, Str inConnectorId) {
		final java.util.Map<Str,Str> conns = parseConnections();

		if (inConnectorId == null) {
		    conns.remove(outConnectorRole);
		} else {
		    conns.put(outConnectorRole, inConnectorId);
		}

		mergeConnections(conns);
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:checkConfig-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:checkConfig")
	public published  void checkConfig () throws org.radixware.ads.ServiceBus.server.PipelineConfigException {
		if (process == null || process.isValid != Bool.TRUE)
		    throw new PipelineConfigException("Processing function not defined or invalid", this);

		checkConnectors();
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:mergeConnections-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:mergeConnections")
	protected published  void mergeConnections (java.util.Map<Str,Str> list) {
		int idx = 0;
		Str ids = null;

		if (list != null) {
		    ids = "";
		    for (java.util.Map.Entry<Str,Str> e: list.entrySet()) {
		        ids += e.getKey() + "=" + e.getValue();
		        if (++idx < list.size())
		            ids += ";";
		    }
		}

		outConnectionIds = ids;
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:parseConnections-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:parseConnections")
	protected published  java.util.Map<Str,Str> parseConnections () {
		final java.util.Map<Str,Str> list = new java.util.HashMap<Str,Str>();
		final Str ids = outConnectionIds;

		if (ids != null) {
		    final Str[] entries = ids.split(";");
		    for (Str e: entries) {
		        final Str[] parts = e.split("=");
		        if (parts.length == 2)
		            list.put(parts[0], parts[1]);        
		    }
		}

		return list;
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:prepareRs-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:prepareRs")
	public published  org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRs prepareRs () {
		return ServiceBus::SbServerUtil.prepareNextResponse(null, null, null);
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:prepareNextRq-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:prepareNextRq")
	public published  org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRq prepareNextRq (org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRq rq) {
		return ServiceBus::SbServerUtil.prepareNextRequest(rq, null, null, null);
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:prepareNextRs-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:prepareNextRs")
	public published  org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRs prepareNextRs (org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRs rs) {
		return ServiceBus::SbServerUtil.prepareNextResponse(rs, null, null);
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:processRequest-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:processRequest")
	public published  org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRs processRequest (Str outRole, org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRq rq) throws org.radixware.ads.ServiceBus.server.PipelineException {
		final java.util.Map<Str,Str> conns = parseConnections();
		final Str connectionId = conns.get(outRole);
		if (connectionId == null)
		    throw new PipelineException(Str.format("No connector found for the role %s", outRole));

		if (rq == null)
		    rq = ServiceBus::SbServerUtil.prepareNextRequest(null, outRole, connectionId, null);
		    
		if (rq.CurStage == null)
		    rq.CurStage = ServiceBus::SbXsd:StageRq.Factory.newInstance();

		final Types::Pid pid = ServiceBus::SbServerUtil.getConnectionPid(connectionId);
		rq.CurStage.NodeEntityId = Str.valueOf(pid.EntityId);
		rq.CurStage.NodeEntityPid = Str.valueOf(pid);
		rq.CurStage.InConnector = ServiceBus::SbServerUtil.getConnectionRole(connectionId);

		outRqData = ServiceBus::SbCommonUtil.copyXml(rq.CurStage.Data);
		ServiceBus::SbXsd:PipelineMessageRs rs = 
		    ServiceBus::SbServerUtil.processRequest(connectionId, rq);
		outRsData = ServiceBus::SbCommonUtil.copyXml(rs.CurStage.Data);
		    
		return rs;
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:getConnectorId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:getConnectorId")
	public published  Str getConnectorId (Str role) {
		final java.util.Map<Str,Str> conns = parseConnections();
		return conns.get(role);

	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:getConnectorRole-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:getConnectorRole")
	public published  Str getConnectorRole (Str id) {
		final java.util.Map<Str,Str> conns = parseConnections();
		for (java.util.Map.Entry<Str,Str> e: conns.entrySet())
		    if (e.getValue() == id)
		        return e.getKey();
		return null;
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:afterResponseDelivered-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:afterResponseDelivered")
	public published  void afterResponseDelivered (org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRs rs) throws org.radixware.ads.ServiceBus.server.PipelineException {

	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:getConnectorSide-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:getConnectorSide")
	public published  org.radixware.ads.ServiceBus.common.Side getConnectorSide (Str role) {
		if (role == IN)
		    return ServiceBus::Side:Left;
		    
		final ServiceBus::PipelineConnector c = getConnector(role);
		if (c == null)
		    throw new AppError("Unknown role " + role);
		    
		return ServiceBus::Side:Right;
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:getConnectors-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:getConnectors")
	protected published  java.util.List<org.radixware.kernel.server.types.Pid> getConnectors () {
		if (connectors == null) {
		    connectors = ServiceBus::SbServerUtil.getNodeConnectors(this);
		    // кэшируем
		    for (Types::Pid pid : connectors) {
		        if (pid.isExistsInCache()) {
		            // очищаем кэш
		            final ServiceBus::PipelineConnector connector = (ServiceBus::PipelineConnector)ServiceBus::PipelineConnector.load(pid);
		            connector.discard();
		        }
		        final ServiceBus::PipelineConnector connector = (ServiceBus::PipelineConnector)ServiceBus::PipelineConnector.load(pid);
		        connector.keepInCache(null);
		    }    
		}
		return connectors;
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:getConnector-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:getConnector")
	protected published  org.radixware.ads.ServiceBus.server.PipelineConnector getConnector (Str role) {
		for (Types::Pid pid: getConnectors()) {
		    final ServiceBus::PipelineConnector c = (ServiceBus::PipelineConnector)ServiceBus::PipelineConnector.load(pid);
		    if (c.role == role)
		        return c;
		}
		return null;
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:getOrderPos-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:getOrderPos")
	public static published  org.radixware.ads.ServiceBus.common.SbPos getOrderPos () {
		return new SbPos(20.1, 1.1);
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:checkConnectors-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:checkConnectors")
	protected published  void checkConnectors () throws org.radixware.ads.ServiceBus.server.PipelineConfigException {
		final java.util.Map<Str,Str> conns = parseConnections();
		for (Str role : getOutConnectorRoles()) {
		    Str connectId = conns.get(role);
		    ServiceBus::SbServerUtil.checkConnection(this, role, connectId);
		}
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:getParams-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:getParams")
	protected published  org.radixware.schemas.types.MapStrStr getParams () {
		if (params == null) {
		    params = ServiceBus::SbServerUtil.getNodeParams(this);
		}
		return ServiceBus::SbServerUtil.mapToXml(params);
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:refreshCache-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:refreshCache")
	public published  void refreshCache () {
		super.refreshCache();

		if (connectors != null) {
		    for (Types::Pid pid : connectors) {
		        if (pid.isExistsInCache()) {
		            // очищаем кэш
		            final ServiceBus::PipelineConnector connector = (ServiceBus::PipelineConnector)ServiceBus::PipelineConnector.load(pid);
		            connector.discard();
		        }
		    }
		    connectors = null;
		}
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:prepareNextRq-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:prepareNextRq")
	public published  org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRq prepareNextRq (org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRq rq, org.apache.xmlbeans.XmlObject data) {
		return ServiceBus::SbServerUtil.prepareNextRequest(rq, null, null, data);
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:prepareNextRs-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:prepareNextRs")
	public published  org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRs prepareNextRs (org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRs rs, org.apache.xmlbeans.XmlObject data) {
		return ServiceBus::SbServerUtil.prepareNextResponse(rs, null, data);
	}

	/*Radix::ServiceBus.Nodes::Processor.Jml:prepareRs-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:prepareRs")
	public published  org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRs prepareRs (org.apache.xmlbeans.XmlObject data) {
		return ServiceBus::SbServerUtil.prepareNextResponse(null, null, data);
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::ServiceBus.Nodes::Processor.Jml - Server Meta*/

/*Radix::ServiceBus.Nodes::Processor.Jml-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Processor.Jml_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXM5EJPEZFFBGBGEXO4W3T7ZJFM"),"Processor.Jml",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3ED2MVATYZA3PNQJEBZFLT6OXI"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::ServiceBus.Nodes::Processor.Jml:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXM5EJPEZFFBGBGEXO4W3T7ZJFM"),
							/*Owner Class Name*/
							"Processor.Jml",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3ED2MVATYZA3PNQJEBZFLT6OXI"),
							/*Property presentations*/

							/*Radix::ServiceBus.Nodes::Processor.Jml:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::ServiceBus.Nodes::Processor.Jml:process:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruTDGMAUN2LNECHHZUYWBLOQ5EFM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},org.radixware.kernel.common.types.Id.Factory.loadFrom("eccZMCDFRDL4DOBDIEAAALOMT5GDM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Processor.Jml:outConnectsTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdG3G2HTCTF5BIRB2LG5N3HAGPQA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Processor.Jml:rqType:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruYSQALBKNT5BUJLLRYD35E47JLI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Processor.Jml:rsType:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruAL3RD22D2BHG7P2DNU62T5LLHQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
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
									/*Radix::ServiceBus.Nodes::Processor.Jml:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprITJ4KKM2BZBVPJA3EE44TY2LTE"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6CT7IGLHQBFNRBZL5B3OUJGKE4"),
									35888,
									null,

									/*Radix::ServiceBus.Nodes::Processor.Jml:Edit:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::ServiceBus.Nodes::Processor.Jml:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDLFS35EC7RC3VDG6K7OFLP6A2U"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCPQXRJGZ7JDCBMQ7DM4TBA22GM"),
									35888,
									null,

									/*Radix::ServiceBus.Nodes::Processor.Jml:Create:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6CT7IGLHQBFNRBZL5B3OUJGKE4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCPQXRJGZ7JDCBMQ7DM4TBA22GM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprITJ4KKM2BZBVPJA3EE44TY2LTE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDLFS35EC7RC3VDG6K7OFLP6A2U")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::ServiceBus.Nodes::Processor.Jml:All-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccFKBZ5CCJ6BBSDEVHA5VOXSSGXY"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXM5EJPEZFFBGBGEXO4W3T7ZJFM"),null,60.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXM5EJPEZFFBGBGEXO4W3T7ZJFM"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblPNJV5QTXIBGJPBHK64RO3LH73A"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecPNJV5QTXIBGJPBHK64RO3LH73A"),

						/*Radix::ServiceBus.Nodes::Processor.Jml:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::ServiceBus.Nodes::Processor.Jml:process-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruTDGMAUN2LNECHHZUYWBLOQ5EFM"),"process",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFXNBCTJ6BVEXPLNBDYRNJ7UWQI"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblPNJV5QTXIBGJPBHK64RO3LH73A"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclSQEW5FAQJ5EBFPU57S47V5XJYU"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Processor.Jml:outConnectsTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdG3G2HTCTF5BIRB2LG5N3HAGPQA"),"outConnectsTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIORVC2XZUBFKDCAQE3JLQ7P3DU"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Processor.Jml:rqType-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruYSQALBKNT5BUJLLRYD35E47JLI"),"rqType",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZPBMZU26T5HGHNRZABJRS424GI"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("http://schemas.radixware.org/types.xsd#BinBase64")),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblPNJV5QTXIBGJPBHK64RO3LH73A"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Processor.Jml:rsType-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruAL3RD22D2BHG7P2DNU62T5LLHQ"),"rsType",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDPZ74MEU5RANNEYXT4OS5PTWGM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("http://schemas.radixware.org/types.xsd#BinBase64")),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblPNJV5QTXIBGJPBHK64RO3LH73A"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Processor.Jml:outConnectionIds-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruVLBPZSKBKRHPXNJXGHN7ODND64"),"outConnectionIds",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblPNJV5QTXIBGJPBHK64RO3LH73A"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::ServiceBus.Nodes::Processor.Jml:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZHFRUV5SQ5CYNG4JLLR26M4UWU"),"getOutConnectorRoles",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIQ7FIDN6DFEYXJSX6G5WONK32M"),"getInConnectorRoles",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7MEKN6RYYBASBPAYYLM556VGPM"),"processRequest",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKWMHPRYO45GNBEN5EL7XW5LZNI"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOK5OVIZTJ5CUTKASBMH6PUUWGQ"),"getIconId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIOMPW3QBZ5ASTPONSTPGMH5ZHQ"),"getEditingPresentationId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLILYOANG5RBVTHLQIJLYCSDJM4"),"getCreatingPresentationId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLB23XTDDKVGSRN5R4BPLLXUMFY"),"getConnectorTitle",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAJKV5VDL6JCLDJL3WZJ7FZYBCM"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHZIY6G7TCVCX3DR6QWA77MJJHI"),"getConnectorRsDataType",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6LP26IWYJRDINGDQXOFG52QWFI"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEIEXINMQGJB35G65UDWFOOSDZU"),"getConnectorRqDataType",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEQRTXT2JUFHT3JBVDK7CUVS4AE"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZEZONIMUSNHEJHOFQ7OPMP3CLU"),"getConnectorIconId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr46GQLZEQTJEN3IIUHZ3B5C5HUQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRJJVN7SZFRGFPKSWMK5OODDPHM"),"connect",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("outConnectorRole",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAAMPQK76IVFQNN2TRIEGMFGGDA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("inConnectorId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTHNXTFJB3JGGNN6QF5Q6IGZCKM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOC7IFQSQQNCXLAY4YULNTEVVL4"),"checkConfig",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKIKCF2LMNBFG5EJXJRALQBB3MQ"),"mergeConnections",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("list",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprORF7FDMJ2ZG6FABCKSSUYVURB4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthL37QLUHYK5FFVAMPM6YYWBW2IU"),"parseConnections",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6JYJKH3M65GW3AG7KBMOKTZQ6M"),"prepareRs",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5ZWTQD262BBXBNJZW5HZJGWWKA"),"prepareNextRq",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDNLOZ4OWTJCQBCLN2VSDOO7ZQU"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXNYOJASQ7BEVJOEF4U7FET4NKI"),"prepareNextRs",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rs",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAYK3CQWN2FH33AK33XCCZ7OPYQ"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3N4A5JFUMZFIXIXTYRXT7FICGE"),"processRequest",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("outRole",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7PLS4YMTV5B63MO2RLMAZRW5OI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprP3ZN3SGMFBAHTERHLH5KIJLLDQ"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFST4X6BGFNA5PH6LQNOG73MSTQ"),"getConnectorId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOPKW7L4RGNAUTCUXOK6KZFVCGE"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWE5I24A36RE3BGGGIIG4FYAV7M"),"getConnectorRole",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZM663633MVF5HCGZGGYQ25FHDI"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTO3N6ZUC25BNLNVHR6WUL3OSCU"),"afterResponseDelivered",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rs",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr74EARDVR4VE5VDVKD3BK57WE2Y"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthB25WTYWZUJAJXMCSH7UVSSJMP4"),"getConnectorSide",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprY3ZSNLQ2KZAPPADVGEFYJNEZAQ"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIXINRU5D7FDWVMUHAABRNWLYPI"),"getConnectors",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPKHQQNYVLNDLDDCTPZWAWWUPLM"),"getConnector",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUK57DW5W7VBXVOTUZIVYRSWF5Y"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAXJJZNLFOJGVBNHZN5AEP5GJGM"),"getOrderPos",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPDA6TXZWDNHHXI7YHI5265KY3A"),"checkConnectors",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIQLCU3ASXVDIDI255QJ5U2EZ3I"),"getParams",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthM7KLX4PIANEH3NYQZZSZ55GMIE"),"refreshCache",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHG5CR7IQYJARHAKMHHGJVL7UJU"),"prepareNextRq",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVRQAYZLYH5HLLPCWHO54PFXN2M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprE3BDTDZ6XBCZJC3W2DBCTGDYXQ"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBHKMMTZC2JH3HFWDTUEBT76ITU"),"prepareNextRs",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rs",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprK3ZWTTG2SVD7XNFEITWPAZBHMU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAMZDN4RT6JCG5KNCEXINFELTGQ"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQQKZWWRRU5AGLGFG56JYKV62O4"),"prepareRs",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUTMLQDSSHNC2HGEZEWIBDZ4Y4E"))
								},org.radixware.kernel.common.enums.EValType.XML)
						},
						null,
						null,null,null,false);
}

/* Radix::ServiceBus.Nodes::Processor.Jml - Desktop Executable*/

/*Radix::ServiceBus.Nodes::Processor.Jml-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml")
public interface Processor.Jml   extends org.radixware.ads.ServiceBus.explorer.PipelineNode  {












































































































































	/*Radix::ServiceBus.Nodes::Processor.Jml:outConnectsTitle:outConnectsTitle-Presentation Property*/


	public class OutConnectsTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public OutConnectsTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:outConnectsTitle:outConnectsTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:outConnectsTitle:outConnectsTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OutConnectsTitle getOutConnectsTitle();
	/*Radix::ServiceBus.Nodes::Processor.Jml:rsType:rsType-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:rsType:rsType")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:rsType:rsType")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RsType getRsType();
	/*Radix::ServiceBus.Nodes::Processor.Jml:process:process-Presentation Property*/


	public class Process extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public Process(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:process:process")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:process:process")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Process getProcess();
	/*Radix::ServiceBus.Nodes::Processor.Jml:rqType:rqType-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:rqType:rqType")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:rqType:rqType")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RqType getRqType();


}

/* Radix::ServiceBus.Nodes::Processor.Jml - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Processor.Jml-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Processor.Jml_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::ServiceBus.Nodes::Processor.Jml:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXM5EJPEZFFBGBGEXO4W3T7ZJFM"),
			"Radix::ServiceBus.Nodes::Processor.Jml",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecPNJV5QTXIBGJPBHK64RO3LH73A"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3ED2MVATYZA3PNQJEBZFLT6OXI"),null,null,0,

			/*Radix::ServiceBus.Nodes::Processor.Jml:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::ServiceBus.Nodes::Processor.Jml:process:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruTDGMAUN2LNECHHZUYWBLOQ5EFM"),
						"process",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFXNBCTJ6BVEXPLNBDYRNJ7UWQI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXM5EJPEZFFBGBGEXO4W3T7ZJFM"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclSQEW5FAQJ5EBFPU57S47V5XJYU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						null,
						0L,
						0L,false,false),

					/*Radix::ServiceBus.Nodes::Processor.Jml:outConnectsTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdG3G2HTCTF5BIRB2LG5N3HAGPQA"),
						"outConnectsTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIORVC2XZUBFKDCAQE3JLQ7P3DU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXM5EJPEZFFBGBGEXO4W3T7ZJFM"),
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

						/*Radix::ServiceBus.Nodes::Processor.Jml:outConnectsTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Processor.Jml:rqType:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruYSQALBKNT5BUJLLRYD35E47JLI"),
						"rqType",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZPBMZU26T5HGHNRZABJRS424GI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXM5EJPEZFFBGBGEXO4W3T7ZJFM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("http://schemas.radixware.org/types.xsd#BinBase64"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeGT7ATZZLDRHJ7NL5BYLWCPGVBQ"),
						true,

						/*Radix::ServiceBus.Nodes::Processor.Jml:rqType:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Processor.Jml:rsType:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruAL3RD22D2BHG7P2DNU62T5LLHQ"),
						"rsType",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDPZ74MEU5RANNEYXT4OS5PTWGM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXM5EJPEZFFBGBGEXO4W3T7ZJFM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("http://schemas.radixware.org/types.xsd#BinBase64"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeGT7ATZZLDRHJ7NL5BYLWCPGVBQ"),
						true,

						/*Radix::ServiceBus.Nodes::Processor.Jml:rsType:PropertyPresentation:Edit Options:-Edit Mask Str*/
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
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprITJ4KKM2BZBVPJA3EE44TY2LTE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDLFS35EC7RC3VDG6K7OFLP6A2U")},
			true,false,false);
}

/* Radix::ServiceBus.Nodes::Processor.Jml - Web Meta*/

/*Radix::ServiceBus.Nodes::Processor.Jml-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Processor.Jml_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::ServiceBus.Nodes::Processor.Jml:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXM5EJPEZFFBGBGEXO4W3T7ZJFM"),
			"Radix::ServiceBus.Nodes::Processor.Jml",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecPNJV5QTXIBGJPBHK64RO3LH73A"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3ED2MVATYZA3PNQJEBZFLT6OXI"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::ServiceBus.Nodes::Processor.Jml:Edit - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Processor.Jml:Edit-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprITJ4KKM2BZBVPJA3EE44TY2LTE"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6CT7IGLHQBFNRBZL5B3OUJGKE4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXM5EJPEZFFBGBGEXO4W3T7ZJFM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblPNJV5QTXIBGJPBHK64RO3LH73A"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4RJJHMLQTFDTTF4VJADSE7H2V4"),
	null,

	/*Radix::ServiceBus.Nodes::Processor.Jml:Edit:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::ServiceBus.Nodes::Processor.Jml:Edit:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgJPMENUUSJBEKBHSF7JGBCW7QKM"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXM5EJPEZFFBGBGEXO4W3T7ZJFM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7FQM2HBPUVFJBIJKYRSTMHBQS4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("imgUQ3RHVJDQRCKJJ6NCBSP5VJL6M"),new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colINHZQ7NTTVDYDATLRAMEPNM55U"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXNDK3TLXSZC2TOKLUPN7Y2MU6Y"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col33JH36QBOZFNLFI5IIRQI4LQ2U"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdG3G2HTCTF5BIRB2LG5N3HAGPQA"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYMNM6EH6LVB4BOBROI66ZOVBCE"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ4QH3CFLQFH4ZENFIHVYOWNBVA"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruTDGMAUN2LNECHHZUYWBLOQ5EFM"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruYSQALBKNT5BUJLLRYD35E47JLI"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruAL3RD22D2BHG7P2DNU62T5LLHQ"),0,3,1,false,false)
			},null),

			/*Radix::ServiceBus.Nodes::Processor.Jml:Edit:Params-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgM6QWC5ELGRCH7PVIAIYZC243SU"),"Params",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiDMVPOQIUSZA4XMGPEC7QNQ5IGA")),

			/*Radix::ServiceBus.Nodes::Processor.Jml:Edit:Connectors-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgAFCCT7JQPJDHPOMOHRISLU72SE"),"Connectors",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXM5EJPEZFFBGBGEXO4W3T7ZJFM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLPCD4CCVWRF2VNTEYELPWD35XE"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiGSTJVLAC6JDIPGW7V7Z5FSBSNE"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgJPMENUUSJBEKBHSF7JGBCW7QKM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgAFCCT7JQPJDHPOMOHRISLU72SE")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgM6QWC5ELGRCH7PVIAIYZC243SU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgIKFTVBTED5DMPMVVT4KRXWWP7Y"))}
	,

	/*Radix::ServiceBus.Nodes::Processor.Jml:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	35888,0,0);
}
/* Radix::ServiceBus.Nodes::Processor.Jml:Edit:Model - Desktop Executable*/

/*Radix::ServiceBus.Nodes::Processor.Jml:Edit:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:Edit:Model")
public class Edit:Model  extends org.radixware.ads.ServiceBus.Nodes.explorer.Processor.Jml.Processor.Jml_DefaultModel.epr6CT7IGLHQBFNRBZL5B3OUJGKE4_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Processor.Jml:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Processor.Jml:Edit:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Processor.Jml:Edit:Model:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Processor.Jml:Edit:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:Edit:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		if (ServiceBus::SbDesktopUtil.isUniDirectPipeline(this)) {
		    rsType.setVisible(false);
		}
	}


}

/* Radix::ServiceBus.Nodes::Processor.Jml:Edit:Model - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Processor.Jml:Edit:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemITJ4KKM2BZBVPJA3EE44TY2LTE"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aem6CT7IGLHQBFNRBZL5B3OUJGKE4"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Processor.Jml:Edit:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Processor.Jml:Create - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Processor.Jml:Create-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDLFS35EC7RC3VDG6K7OFLP6A2U"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCPQXRJGZ7JDCBMQ7DM4TBA22GM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXM5EJPEZFFBGBGEXO4W3T7ZJFM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblPNJV5QTXIBGJPBHK64RO3LH73A"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFBQPAPDAQNFKNCGZXG2HER6YUU"),
	null,

	/*Radix::ServiceBus.Nodes::Processor.Jml:Create:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::ServiceBus.Nodes::Processor.Jml:Create:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgG5LB7V4KLZC45O746SZ3CT3BBI"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXM5EJPEZFFBGBGEXO4W3T7ZJFM"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("imgUQ3RHVJDQRCKJJ6NCBSP5VJL6M"),new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ4QH3CFLQFH4ZENFIHVYOWNBVA"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colINHZQ7NTTVDYDATLRAMEPNM55U"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYMNM6EH6LVB4BOBROI66ZOVBCE"),0,2,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgG5LB7V4KLZC45O746SZ3CT3BBI"))}
	,

	/*Radix::ServiceBus.Nodes::Processor.Jml:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	35888,0,0);
}
/* Radix::ServiceBus.Nodes::Processor.Jml:Create:Model - Desktop Executable*/

/*Radix::ServiceBus.Nodes::Processor.Jml:Create:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:Create:Model")
public class Create:Model  extends org.radixware.ads.ServiceBus.Nodes.explorer.Processor.Jml.Processor.Jml_DefaultModel.eprCPQXRJGZ7JDCBMQ7DM4TBA22GM_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Processor.Jml:Create:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Processor.Jml:Create:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Processor.Jml:Create:Model:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Processor.Jml:Create:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Processor.Jml:Create:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		if (ServiceBus::SbDesktopUtil.isUniDirectPipeline(this)) {
		    rsType.setVisible(false);
		}
	}


}

/* Radix::ServiceBus.Nodes::Processor.Jml:Create:Model - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Processor.Jml:Create:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemDLFS35EC7RC3VDG6K7OFLP6A2U"),
						"Create:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemCPQXRJGZ7JDCBMQ7DM4TBA22GM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Processor.Jml:Create:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Processor.Jml - Localizing Bundle */
package org.radixware.ads.ServiceBus.Nodes.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Processor.Jml - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"JML Processor");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"JML процессор");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3ED2MVATYZA3PNQJEBZFLT6OXI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Processing function not defined or invalid");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользовательская функция обработки не задана или некорректна");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4KXN2X2UKBGHPJP6GF3UJIHZQM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"JML Processor");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"JML процессор");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4RJJHMLQTFDTTF4VJADSE7H2V4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"No connector found for the role %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не найден коннектор для роли %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6DDAIK7RDFB3BATKMNLU2Q4ETQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7FQM2HBPUVFJBIJKYRSTMHBQS4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Response data type");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип данных ответа");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDPZ74MEU5RANNEYXT4OS5PTWGM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"JML Processor");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"JML процессор");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFBQPAPDAQNFKNCGZXG2HER6YUU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Processing function");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Функция обработки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFXNBCTJ6BVEXPLNBDYRNJ7UWQI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Connectors");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Коннекторы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIORVC2XZUBFKDCAQE3JLQ7P3DU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Output Connectors");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выходные коннекторы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLPCD4CCVWRF2VNTEYELPWD35XE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Input");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вход");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQYDCUOPUJNHS3PTOE67AO2LLJ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Request data type");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип данных запроса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZPBMZU26T5HGHNRZABJRS424GI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Processor.Jml - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclXM5EJPEZFFBGBGEXO4W3T7ZJFM"),"Processor.Jml - Localizing Bundle",$$$items$$$);
}
