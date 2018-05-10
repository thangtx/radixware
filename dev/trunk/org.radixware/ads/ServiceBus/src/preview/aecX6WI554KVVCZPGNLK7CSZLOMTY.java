
/* Radix::ServiceBus::Pipeline - Server Executable*/

/*Radix::ServiceBus::Pipeline-Entity Class*/

package org.radixware.ads.ServiceBus.server;

import java.io.Writer;
import java.util.*;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline")
public published class Pipeline  extends org.radixware.ads.Types.server.Entity  implements org.radixware.ads.ServiceBus.server.IPipeline,org.radixware.ads.CfgManagement.server.ICfgReferencedObject,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	public static final Types::Id CFG_ITEM_PIPELINE_SINGLE_ID = idof[CfgItem.PipelineSingle];
	private Map<Str, Types::Pid> dataSchemes = null;
	private Set<Types::Pid> nodes = null;

	private Map<Str, Str> params = null;
	private Map<Str, java.lang.Object> vars = 
	    new HashMap<Str, java.lang.Object>();
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Pipeline_mi.rdxMeta;}

	/*Radix::ServiceBus::Pipeline:Nested classes-Nested Classes*/

	/*Radix::ServiceBus::Pipeline:CfgObjectLookupAdvizor-Server Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:CfgObjectLookupAdvizor")
	private static class CfgObjectLookupAdvizor  implements org.radixware.ads.CfgManagement.server.ICfgObjectLookupAdvizor,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


		/**Metainformation accessor method*/
		@SuppressWarnings("unused")
		public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Pipeline_mi.rdxMeta_adcQ6UH2IGO3JHBBLSTBPTDJHQ2UU;}

		/*Radix::ServiceBus::Pipeline:CfgObjectLookupAdvizor:Nested classes-Nested Classes*/

		/*Radix::ServiceBus::Pipeline:CfgObjectLookupAdvizor:Properties-Properties*/





























		/*Radix::ServiceBus::Pipeline:CfgObjectLookupAdvizor:Methods-Methods*/

		/*Radix::ServiceBus::Pipeline:CfgObjectLookupAdvizor:getCfgObjectsByExtGuid-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:CfgObjectLookupAdvizor:getCfgObjectsByExtGuid")
		public published  java.util.List<org.radixware.ads.Types.server.Entity> getCfgObjectsByExtGuid (Str extGuid, boolean considerContext, org.radixware.ads.Types.server.Entity context) {
			if (extGuid == null) {
			    return java.util.Collections.emptyList();
			}
			java.util.ArrayList<Types::Entity> pipelines = new java.util.ArrayList<Types::Entity>(1);
			final ServiceBus.Db::GetPipelineByExtGuidCursor cursor = ServiceBus.Db::GetPipelineByExtGuidCursor.open(extGuid);
			try {
			    while (cursor.next()) {
			        pipelines.add(cursor.pipeline);
			    }
			} finally {
			    cursor.close();
			}
			return pipelines;
		}


	}

	/*Radix::ServiceBus::Pipeline:Properties-Properties*/

	/*Radix::ServiceBus::Pipeline:classGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:classGuid")
	public published  Str getClassGuid() {
		return classGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:classGuid")
	public published   void setClassGuid(Str val) {
		classGuid = val;
	}

	/*Radix::ServiceBus::Pipeline:description-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:description")
	public published  Str getDescription() {
		return description;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:description")
	public published   void setDescription(Str val) {
		description = val;
	}

	/*Radix::ServiceBus::Pipeline:diagramm-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:diagramm")
	public published  java.sql.Clob getDiagramm() {
		return diagramm;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:diagramm")
	public published   void setDiagramm(java.sql.Clob val) {
		diagramm = val;
	}

	/*Radix::ServiceBus::Pipeline:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:id")
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:id")
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::ServiceBus::Pipeline:lastUpdateTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:lastUpdateTime")
	public published  java.sql.Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:lastUpdateTime")
	public published   void setLastUpdateTime(java.sql.Timestamp val) {
		lastUpdateTime = val;
	}

	/*Radix::ServiceBus::Pipeline:lastUpdateUserName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:lastUpdateUserName")
	public published  Str getLastUpdateUserName() {
		return lastUpdateUserName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:lastUpdateUserName")
	public published   void setLastUpdateUserName(Str val) {
		lastUpdateUserName = val;
	}

	/*Radix::ServiceBus::Pipeline:myEntityGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:myEntityGuid")
	public published  Str getMyEntityGuid() {
		return myEntityGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:myEntityGuid")
	public published   void setMyEntityGuid(Str val) {
		myEntityGuid = val;
	}

	/*Radix::ServiceBus::Pipeline:myEntityPid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:myEntityPid")
	public published  Str getMyEntityPid() {
		return myEntityPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:myEntityPid")
	public published   void setMyEntityPid(Str val) {
		myEntityPid = val;
	}

	/*Radix::ServiceBus::Pipeline:title-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:title")
	public published  Str getTitle() {
		return title;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:title")
	public published   void setTitle(Str val) {
		title = val;
	}

	/*Radix::ServiceBus::Pipeline:traceProfile-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:traceProfile")
	public published  Str getTraceProfile() {
		return traceProfile;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:traceProfile")
	public published   void setTraceProfile(Str val) {
		traceProfile = val;
	}

	/*Radix::ServiceBus::Pipeline:checked-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:checked")
	public published  Bool getChecked() {
		return checked;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:checked")
	public published   void setChecked(Bool val) {
		checked = val;
	}

	/*Radix::ServiceBus::Pipeline:caching-Dynamic Property*/



	protected org.radixware.ads.Utils.server.Caching caching=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:caching")
	private final  org.radixware.ads.Utils.server.Caching getCaching() {

		if(internal[caching] == null) {
		    internal[caching] = new Caching(this, Common::CachingPeriod:RecheckPeriod.getValue().intValue(), -1);
		} 
		return internal[caching];
	}

	/*Radix::ServiceBus::Pipeline:lastUpdateTimeGetter-Dynamic Property*/



	protected org.radixware.ads.Utils.server.ILastUpdateTimeGetter lastUpdateTimeGetter=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:lastUpdateTimeGetter")
	private final  org.radixware.ads.Utils.server.ILastUpdateTimeGetter getLastUpdateTimeGetter() {

		if (internal[lastUpdateTimeGetter] == null) {
		    internal[lastUpdateTimeGetter] = new LastUpdateTimeGetterById(dbName[Radix::ServiceBus::Pipeline], dbName[lastUpdateTime], dbName[id], id);
		}
		return internal[lastUpdateTimeGetter];
	}

	/*Radix::ServiceBus::Pipeline:extGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:extGuid")
	public published  Str getExtGuid() {
		return extGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:extGuid")
	public published   void setExtGuid(Str val) {
		extGuid = val;
	}

	/*Radix::ServiceBus::Pipeline:isUniDirect-Dynamic Property*/



	protected Bool isUniDirect=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:isUniDirect")
	public  Bool getIsUniDirect() {

		return isUniDirect();
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:isUniDirect")
	public   void setIsUniDirect(Bool val) {
		isUniDirect = val;
	}

	/*Radix::ServiceBus::Pipeline:classTitle-Dynamic Property*/



	protected Str classTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:classTitle")
	public  Str getClassTitle() {

		return getRadMeta().getTitle();
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:classTitle")
	public   void setClassTitle(Str val) {
		classTitle = val;
	}

	/*Radix::ServiceBus::Pipeline:changeLog-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:changeLog")
	public published  org.radixware.ads.CfgManagement.server.ChangeLog getChangeLog() {
		return changeLog;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:changeLog")
	public published   void setChangeLog(org.radixware.ads.CfgManagement.server.ChangeLog val) {
		changeLog = val;
	}



























































































































	/*Radix::ServiceBus::Pipeline:Methods-Methods*/

	/*Radix::ServiceBus::Pipeline:checkConfig-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:checkConfig")
	public published  void checkConfig () throws org.radixware.ads.ServiceBus.server.PipelineConfigException {
		for (IPipelineNode node: getNodes()) {
		    node.checkConfig();
		}
	}

	/*Radix::ServiceBus::Pipeline:getHolderEntityPid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:getHolderEntityPid")
	public published  org.radixware.kernel.server.types.Pid getHolderEntityPid () {
		return new Pid(Arte::Arte.getInstance(), Types::Id.Factory.loadFrom(myEntityGuid), myEntityPid);
	}

	/*Radix::ServiceBus::Pipeline:getNodes-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:getNodes")
	public published  java.util.Set<org.radixware.ads.ServiceBus.server.IPipelineNode> getNodes () {
		if (nodes == null) {
		    nodes = SbServerUtil.getPipelineNodes(this);
		}

		final Set<IPipelineNode> list = new HashSet<IPipelineNode>();
		for (Types::Pid pid : nodes) {
		    list.add(SbServerUtil.getPipelineNode(pid));
		}

		return list;
	}

	/*Radix::ServiceBus::Pipeline:getDiagram-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:getDiagram")
	public published  org.radixware.ads.ServiceBus.common.DiagramXsd.DiagramDocument getDiagram () {
		if (diagramm == null)
		    return null;

		try {
		    return DiagramXsd:DiagramDocument.Factory.parse(diagramm.getCharacterStream());
		} catch (Exception e) {
		    throw new DatabaseError("Can't read context: " + e.getClass().getName() + (e.getMessage() != null ? ":" + e.getMessage() : ""), e);
		}

	}

	/*Radix::ServiceBus::Pipeline:setDiagram-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:setDiagram")
	public published  void setDiagram (org.radixware.ads.ServiceBus.common.DiagramXsd.DiagramDocument diagram) {
		if (diagram != null)
		    try {
		        diagramm = Arte::Arte.getDbConnection().createClob();
		        final Writer writer = diagramm.setCharacterStream(1L);
		        diagram.save(writer); 
		        writer.close();
		    } catch (Exception e) {
		        throw new DatabaseError("Can't write context: " + e.getClass().getName() + (e.getMessage() != null ? ":" + e.getMessage() : ""), e);
		    }
		else
		    diagramm = null;
	}

	/*Radix::ServiceBus::Pipeline:beforeDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:beforeDelete")
	protected published  boolean beforeDelete () {
		final Set<IPipelineNode> nodes = getNodes();
		for (IPipelineNode node: nodes) {
		    final Types::Entity entity = Types::Entity.load(node.getHolderEntityPid());
		    if (entity instanceof System::Unit) {
		        final System::Unit unit = (System::Unit)entity;
		        if (unit.started.booleanValue()) {
		            throw new ServiceProcessClientFault(Utils::IEasFaultReasonEnum.ACCESS_VIOLATION.toString(), "Deletion of started units is forbidden. Stop units first", null, null);
		        }
		    }
		    if (entity instanceof Types::Entity) {
		        final Types::PresentationEntityAdapter<?> adapter = (Types::PresentationEntityAdapter<?>)new PresentationEntityAdapter<Types::Entity>((Types::Entity)entity);
		        try {     
		            adapter.doCheckCascadeBeforeDelete(false);
		        } catch (Exceptions::DeleteCascadeConfirmationRequiredException e) {
		            final Str exMess = Str.format("Cannot delete object %s because it is used by other objects", node.getTitle());
		            throw new ServiceProcessClientFault(Utils::IEasFaultReasonEnum.ACCESS_VIOLATION.toString(), exMess, null, null);
		        } catch (Exceptions::DeleteCascadeRestrictedException e) {
		            final Str exMess = Str.format("Cannot delete object %s because it is used by object %s", node.getTitle(), adapter.calcChildTitleForConfirmation(e.getSubobjectEntityId()));
		            throw new ServiceProcessClientFault(Utils::IEasFaultReasonEnum.ACCESS_VIOLATION.toString(), exMess, null, null);
		        }
		    }
		}

		Str sp = Arte::Arte.setSavepoint();
		try {
		    for (IPipelineNode node: nodes) {
		        final Types::Entity entity = Types::Entity.load(node.getHolderEntityPid());
		        entity.delete();
		    }
		} catch (Exceptions::RuntimeException e) {
		    try {
		        Arte::Arte.rollbackToSavepoint(sp);
		    } catch (Exceptions::Exception ex) {
		        // do nothing
		    }
		    throw e;
		}
		return super.beforeDelete();
	}

	/*Radix::ServiceBus::Pipeline:getNode-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:getNode")
	public published  org.radixware.ads.ServiceBus.server.IPipelineNode getNode (org.radixware.kernel.server.types.Pid pid) {
		for (IPipelineNode node: getNodes()) {
		    if (node.getHolderEntityPid().equals(pid))
		        return node;
		}
		return null;
	}

	/*Radix::ServiceBus::Pipeline:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:beforeCreate")
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		onUpdate();
		return super.beforeCreate(src);
	}

	/*Radix::ServiceBus::Pipeline:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:beforeUpdate")
	protected published  boolean beforeUpdate () {
		onUpdate();
		return super.beforeUpdate();
	}

	/*Radix::ServiceBus::Pipeline:getTraceProfile-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:getTraceProfile")
	public published  Str getTraceProfile () {
		return traceProfile;
	}

	/*Radix::ServiceBus::Pipeline:getParams-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:getParams")
	public published  java.util.Map<Str,Str> getParams () {
		if (params == null) {
		    params = SbServerUtil.getPipelineParams(this);
		}
		return params;
	}

	/*Radix::ServiceBus::Pipeline:getChecked-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:getChecked")
	public published  boolean getChecked () {
		return checked.booleanValue();
	}

	/*Radix::ServiceBus::Pipeline:afterCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:afterCreate")
	protected published  void afterCreate (org.radixware.kernel.server.types.Entity src) {
		super.afterCreate(src);
		if (src == null)
		    return;

		try {
		    final Set<IPipelineNode> nodes = new HashSet<IPipelineNode>();
		    final Map<Types::Pid, Types::Pid> pid2Pid = new HashMap<Types::Pid, Types::Pid>();    
		    final Pipeline src_p = (Pipeline)src;
		    for (IPipelineNode src_node: src_p.getNodes()) {
		        final Types::Entity src_entity = Types::Entity.load(src_node.getHolderEntityPid());
		        final Types::Entity entity = (Types::Entity)Arte::Arte.getInstance().newObject(src_entity.getClassDefinitionId());
		        final IPipelineNode node = (IPipelineNode)entity;
		        entity.init(null, src_entity);
		        myEntityPid = Str.valueOf(id); // bug in getHolderEntityId...
		        node.setPipeline(this);
		        entity.create();
		        pid2Pid.put(src_node.getHolderEntityPid(), node.getHolderEntityPid());
		        nodes.add(node);
		    }
		    
		    DiagramXsd:DiagramDocument xDiagram = getDiagram();
		    if (xDiagram != null) {        
		        for (DiagramXsd:Node xNode: xDiagram.Diagram.Nodes.NodeList) {
		            final Types::Pid oldPid = new Pid(Arte::Arte.getInstance(), Types::Id.Factory.loadFrom(xNode.EntityId), xNode.EntityPid);
		            final Types::Pid newPid = pid2Pid.get(oldPid);
		            if (newPid != null) {
		                xNode.EntityId = Str.valueOf(newPid.EntityId);
		                xNode.EntityPid = Str.valueOf(newPid);
		            }        
		        }        
		        for (DiagramXsd:Edge xEdge: xDiagram.Diagram.Edges.EdgeList) {
		            final Str inRole = SbServerUtil.getConnectionRole(xEdge.InConnectorId);
		            final Types::Pid oldInPid = SbServerUtil.getConnectionPid(xEdge.InConnectorId);
		            final Types::Pid newInPid = pid2Pid.get(oldInPid);
		            if (newInPid != null) {
		                xEdge.InConnectorId = SbServerUtil.getConnectionId(newInPid, inRole);
		            }        
		            final Str outRole = SbServerUtil.getConnectionRole(xEdge.OutConnectorId);
		            final Types::Pid oldOutPid = SbServerUtil.getConnectionPid(xEdge.OutConnectorId);
		            final Types::Pid newOutPid = pid2Pid.get(oldOutPid);
		            if (newOutPid != null) {
		                xEdge.OutConnectorId = SbServerUtil.getConnectionId(newOutPid, outRole);
		            }        
		        }
		        setDiagram(xDiagram);
		    }

		    for (IPipelineNode node: nodes) {
		        final Str roles[] = node.getOutConnectorRoles();
		        if (roles != null) {
		            for (Str role: roles) {
		                final Str connectorId = node.getConnectorId(role);
		                if (connectorId == null)
		                    continue;
		                final Types::Pid oldPid = SbServerUtil.getConnectionPid(connectorId);
		                final Types::Pid newPid = pid2Pid.get(oldPid);
		                if (newPid != null) {
		                    node.connect(role, SbServerUtil.getConnectionId(newPid, SbServerUtil.getConnectionRole(connectorId)));
		                }
		            }
		            final Types::Entity  entity = Types::Entity.load(node.getHolderEntityPid());
		            entity.update();
		        }
		    }     

		    final Map<Str, Str> params = SbServerUtil.getPipelineParams(src_p);
		    for (Map.Entry<Str, Str> entry: params.entrySet()) {
		        final PipelineParam param = new PipelineParam();
		        param.init();
		        param.pipelineId = id;
		        param.name = entry.getKey();
		        param.val = entry.getValue();
		        param.create();
		    }
		    update();
		} catch (Exceptions::Exception e) {
		    Arte::Arte.getInstance().Trace.put(Arte::EventSeverity:Error.Value, Utils::ExceptionTextFormatter.getExceptionMess(e) + "\nStack:\n" + Utils::ExceptionTextFormatter.exceptionStackToString(e), Arte::EventSource:ServiceBus.Value);
		}
	}

	/*Radix::ServiceBus::Pipeline:refreshCache-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:refreshCache")
	public published  boolean refreshCache () {
		return refreshCache(false);
	}

	/*Radix::ServiceBus::Pipeline:onCommand_CreateVer-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:onCommand_CreateVer")
	  void onCommand_CreateVer (org.radixware.ads.ServiceBus.Cmd.common.CommandsXsd.VersionRqDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		SbImpExpUtil.createVersion(this, input.VersionRq.Notes);

	}

	/*Radix::ServiceBus::Pipeline:getDataSchemes-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:getDataSchemes")
	public published  java.util.Map<Str,org.radixware.kernel.server.types.Pid> getDataSchemes () {
		if (dataSchemes == null) {
		    dataSchemes = SbServerUtil.getDataSchemes();
		}
		return dataSchemes;
	}

	/*Radix::ServiceBus::Pipeline:process-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:process")
	public published  org.apache.xmlbeans.XmlObject process (org.apache.xmlbeans.XmlObject xml) throws org.radixware.ads.ServiceBus.server.PipelineException {
		for (IPipelineNode node: getNodes()) {
		    if (node instanceof ServiceBus.Nodes::Endpoint.PipelineApi) {
		        return ((ServiceBus.Nodes::Endpoint.PipelineApi)node).process(xml);
		    }
		}
		throw new PipelineException("API call node not found");
	}

	/*Radix::ServiceBus::Pipeline:findNodeByTitle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:findNodeByTitle")
	public published  org.radixware.ads.ServiceBus.server.IPipelineNode findNodeByTitle (Str title) {
		for (IPipelineNode node: getNodes()) {
		    if (node.getTitle() == title)
		        return node;
		}
		return null;
	}

	/*Radix::ServiceBus::Pipeline:onUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:onUpdate")
	public published  void onUpdate () {
		lastUpdateTime = Utils::Timing.getCurrentTime();
		lastUpdateUserName = Arte::Arte.getUserName();
	}

	/*Radix::ServiceBus::Pipeline:getVars-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:getVars")
	public published  java.util.Map<Str,java.lang.Object> getVars () {
		return vars;
	}

	/*Radix::ServiceBus::Pipeline:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:onCommand_Import")
	  org.radixware.schemas.types.StrDocument onCommand_Import (org.radixware.ads.ServiceBus.common.ImpExpXsd.PipelineDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		CfgManagement::CfgImportHelper.Silent helper = new CfgImportHelper.Silent(CfgManagement::ICfgImportHelper.Action.UPDATE);
		helper.setImportSettings(Pipeline.createSettingsIteractive());
		SbImpExpUtil.importPipeline(input.Pipeline, this, helper);
		helper.reportObjectUpdated(this);
		return helper.getResultsAsHtmlStr();
	}

	/*Radix::ServiceBus::Pipeline:afterInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:afterInit")
	protected published  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		extGuid = Arte::Arte.generateGuid();
		super.afterInit(src, phase);
	}

	/*Radix::ServiceBus::Pipeline:loadByPidStr-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:loadByPidStr")
	 static  org.radixware.ads.ServiceBus.server.Pipeline loadByPidStr (Str pidAsStr, boolean checkExistance) {
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblX6WI554KVVCZPGNLK7CSZLOMTY"),pidAsStr);
		try{
		return (
		org.radixware.ads.ServiceBus.server.Pipeline) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::ServiceBus::Pipeline:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:loadByPK")
	public static published  org.radixware.ads.ServiceBus.server.Pipeline loadByPK (Int id, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(id==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNQ7ACV4LNVHNBGGPWFPOCPH2P4"),id);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblX6WI554KVVCZPGNLK7CSZLOMTY"),pkValsMap);
		try{
		return (
		org.radixware.ads.ServiceBus.server.Pipeline) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::ServiceBus::Pipeline:create-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:create")
	 static  org.radixware.ads.ServiceBus.server.Pipeline create (org.radixware.kernel.common.types.Id classGuid, Str extGuid) {
		Pipeline obj = (Pipeline) Arte::Arte.getInstance().newObject(classGuid);
		obj.init();
		obj.extGuid = extGuid;
		obj.classGuid = classGuid.toString();
		return obj;
	}

	/*Radix::ServiceBus::Pipeline:exportAll-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:exportAll")
	 static  void exportAll (org.radixware.ads.CfgManagement.server.CfgExportData data, java.util.Iterator<org.radixware.ads.ServiceBus.server.Pipeline> iter) {
		ImpExpXsd:PipelineGroupDocument groupDoc = ImpExpXsd:PipelineGroupDocument.Factory.newInstance();
		groupDoc.addNewPipelineGroup();

		if (iter == null) {
		    ServiceBus.Db::PipelineCursor c = ServiceBus.Db::PipelineCursor.open();
		    iter = new EntityCursorIterator(c, idof[ServiceBus.Db::PipelineCursor:pipeline]);
		}

		try {
		    while (iter.hasNext()) {
		        Pipeline p = iter.next();
		        try {
		            ImpExpXsd:PipelineDocument singleDoc = p.exportToXml();
		            data.children.add(new CfgExportData(p, null, idof[CfgItem.PipelineSingle], singleDoc));
		            groupDoc.PipelineGroup.addNewItem().set(singleDoc.Pipeline);
		        } catch (Exceptions::Exception ex) {
		            Arte::Trace.error("Error on export Pipeline '" + p.title + "'\n"
		                    + Arte::Trace.exceptionStackToString(ex), Arte::EventSource:AppCfgPackage);
		        }
		    }
		} finally {
		    EntityCursorIterator.closeIterator(iter);
		}

		data.itemClassId = idof[CfgItem.PipelineGroup];
		data.object = null;
		data.data = null;
		data.fileContent = groupDoc;
	}

	/*Radix::ServiceBus::Pipeline:exportThis-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:exportThis")
	  void exportThis (org.radixware.ads.CfgManagement.server.CfgExportData data) {
		data.itemClassId = idof[CfgItem.PipelineSingle];
		data.object = this;
		ImpExpXsd:PipelineDocument s = exportToXml();
		data.data = s;
		data.fileContent = s;

	}

	/*Radix::ServiceBus::Pipeline:exportToXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:exportToXml")
	private final  org.radixware.ads.ServiceBus.common.ImpExpXsd.PipelineDocument exportToXml () {
		return SbImpExpUtil.exportPipeline(this);
	}

	/*Radix::ServiceBus::Pipeline:importAll-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:importAll")
	 static  void importAll (org.radixware.ads.ServiceBus.common.ImpExpXsd.PipelineGroup xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		for (ImpExpXsd:Pipeline s : xml.ItemList) {
		    importOne(s, helper);
		    if (helper.wasCancelled())
		        break;
		}

	}

	/*Radix::ServiceBus::Pipeline:importOne-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:importOne")
	 static  org.radixware.ads.ServiceBus.server.Pipeline importOne (org.radixware.ads.ServiceBus.common.ImpExpXsd.Pipeline xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		if (xml == null)
		    return null;

		Pipeline obj = Pipeline.loadByExtGuid(xml.ExtGuid);
		final Types::Id classGuid = xml.ClassId;
		CfgManagement::ICfgImportSettings settings = Pipeline.createSettings(java.util.EnumSet.noneOf(CfgPipelineOptions.class));
		helper.setImportSettings(settings);
		if (obj == null) {
		    obj = create(classGuid, xml.ExtGuid);
		    obj.importThis(xml, helper);
		} else
		    switch (helper.getActionIfObjExists(obj)) {
		        case UPDATE:
		            try {
		                settings = Pipeline.createSettingsIteractive();
		                helper.setImportSettings(settings);
		            } catch (Exception ex) {
		                Arte::Trace.debug(Arte::Trace.exceptionStackToString(ex), Arte::EventSource:ServiceBus);
		            }
		            obj.importThis(xml, helper);
		            break;
		        case NEW:
		            obj = create(classGuid, Arte::Arte.generateGuid());
		            obj.importThis(xml, helper);
		            break;
		        case CANCELL:
		            helper.reportObjectCancelled(obj);
		            break;
		    }
		return obj;
	}

	/*Radix::ServiceBus::Pipeline:importThis-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:importThis")
	  void importThis (org.radixware.ads.ServiceBus.common.ImpExpXsd.Pipeline xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		title = xml.Title;
		helper.setDefaultActionIfObjExists(isInDatabase(false)
		        ? CfgManagement::ICfgImportHelper.Action.UPDATE
		        : CfgManagement::ICfgImportHelper.Action.NEW);
		try {
		    helper.createOrUpdateAndReport(this);
		    SbImpExpUtil.importPipeline(xml, this, helper);
		} finally {
		    helper.setDefaultActionIfObjExists(null);
		}
	}

	/*Radix::ServiceBus::Pipeline:updateFromCfgItem-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:updateFromCfgItem")
	  void updateFromCfgItem (org.radixware.ads.ServiceBus.server.CfgItem.PipelineSingle cfg, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		CfgManagement::ICfgImportSettings settings = new CfgImportSettings(helper.getImportSettings());
		settings.setStringParam(CfgPipelineOptions:DELETE_EXISTENT_NODES.Name, cfg.deleteExistentNodes.toString());
		settings.setStringParam(CfgPipelineOptions:OVERWRITE_LOCAL_SETTINGS.Name, cfg.overwriteLocalSettings.toString());
		helper.setImportSettings(settings);
		importThis(cfg.myData.Pipeline, helper);
	}

	/*Radix::ServiceBus::Pipeline:loadByExtGuid-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:loadByExtGuid")
	public static published  org.radixware.ads.ServiceBus.server.Pipeline loadByExtGuid (Str guid) {
		if (guid == null)
		    return null;

		ServiceBus.Db::GetPipelineByExtGuidCursor cur = ServiceBus.Db::GetPipelineByExtGuidCursor.open(guid);
		try {
		    if (cur.next())
		        return cur.pipeline;
		    return null;
		} finally {
		    cur.close();
		}

	}

	/*Radix::ServiceBus::Pipeline:refreshCache-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:refreshCache")
	public published  boolean refreshCache (boolean force) {
		final boolean needRefresh = caching.refresh(lastUpdateTimeGetter);
		if (needRefresh || force) {
		    nodes = null;
		    params = null;
		    dataSchemes = null;
		    for (IPipelineNode node : getNodes()) {
		        final Types::Entity entity = Types::Entity.load(node.getHolderEntityPid());
		        entity.reread();
		        entity.keepInCache(null);
		        node.refreshCache();
		    }    
		}
		return needRefresh;
	}

	/*Radix::ServiceBus::Pipeline:isUniDirect-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:isUniDirect")
	public published  boolean isUniDirect () {
		return false;
	}

	/*Radix::ServiceBus::Pipeline:isTransformation-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:isTransformation")
	public published  boolean isTransformation () {
		return false;
	}

	/*Radix::ServiceBus::Pipeline:getCfgReferenceExtGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:getCfgReferenceExtGuid")
	public published  Str getCfgReferenceExtGuid () {
		return extGuid;
	}

	/*Radix::ServiceBus::Pipeline:getCfgReferencePropId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:getCfgReferencePropId")
	public published  org.radixware.kernel.common.types.Id getCfgReferencePropId () {
		return idof[Pipeline:extGuid];
	}

	/*Radix::ServiceBus::Pipeline:createSettings-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:createSettings")
	 static  org.radixware.ads.CfgManagement.common.ICfgImportSettings createSettings (java.util.EnumSet<org.radixware.ads.ServiceBus.common.CfgPipelineOptions> options) {
		CfgManagement::CfgImportSettings opts = new CfgImportSettings();
		opts.setStringParam(CfgPipelineOptions:DELETE_EXISTENT_NODES.Name, Boolean.toString(options.contains(CfgPipelineOptions:DELETE_EXISTENT_NODES)));
		opts.setStringParam(CfgPipelineOptions:MATCH_NODE_BY_PID.Name, Boolean.toString(options.contains(CfgPipelineOptions:MATCH_NODE_BY_PID)));
		opts.setStringParam(CfgPipelineOptions:OVERWRITE_LOCAL_SETTINGS.Name, Boolean.toString(options.contains(CfgPipelineOptions:OVERWRITE_LOCAL_SETTINGS)));
		opts.setStringParam(CfgPipelineOptions:DELETE_PARAMETERS.Name, Boolean.toString(options.contains(CfgPipelineOptions:DELETE_PARAMETERS)));
		opts.setStringParam(CfgPipelineOptions:OVERWRITE_PARAMETERS.Name, Boolean.toString(options.contains(CfgPipelineOptions:OVERWRITE_PARAMETERS)));
		return opts;
	}

	/*Radix::ServiceBus::Pipeline:createSettingsIteractive-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:createSettingsIteractive")
	 static  org.radixware.ads.CfgManagement.common.ICfgImportSettings createSettingsIteractive () {
		java.util.EnumSet<CfgPipelineOptions> opts = java.util.EnumSet.noneOf(CfgPipelineOptions.class);
		try {
		    if (Client.Resources::MessageDialogResource.confirmation(
		            Arte::Arte.getInstance(), "Import Pipeline", "Delete existing nodes?")) {
		        opts.add(CfgPipelineOptions:DELETE_EXISTENT_NODES);
		    }
		    if (!opts.contains(CfgPipelineOptions:DELETE_EXISTENT_NODES)) {
		        if (Client.Resources::MessageDialogResource.confirmation(
		                Arte::Arte.getInstance(), "Import Pipeline", "Overwrite local settings?")) {
		            opts.add(CfgPipelineOptions:OVERWRITE_LOCAL_SETTINGS);
		        }
		    }
		    if (Client.Resources::MessageDialogResource.confirmation(
		            Arte::Arte.getInstance(), "Import Pipeline", "Overwrite pipeline parameters?")) {
		        opts.add(CfgPipelineOptions:OVERWRITE_PARAMETERS);
		    }
		    if (Client.Resources::MessageDialogResource.confirmation(
		            Arte::Arte.getInstance(), "Import Pipeline", "Delete obsolete pipeline parameters?")) {
		        opts.add(CfgPipelineOptions:DELETE_PARAMETERS);
		    }
		} catch (Exceptions::InterruptedException | Exceptions::ResourceUsageException | Exceptions::ResourceUsageTimeout e) {
		    throw new AppError("Error on import pipeline", e);
		}
		return createSettings(opts);
	}




	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmdI7XAPURSLNBYVN3AGZKCO6GHQY){
			org.radixware.schemas.types.StrDocument result = onCommand_Import((org.radixware.ads.ServiceBus.common.ImpExpXsd.PipelineDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.ads.ServiceBus.common.ImpExpXsd.PipelineDocument.class),newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else if(cmdId == cmdQPFU5JRHQVE2TA6VLCAQDHO7Q4){
			onCommand_CreateVer((org.radixware.ads.ServiceBus.Cmd.common.CommandsXsd.VersionRqDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.ads.ServiceBus.Cmd.common.CommandsXsd.VersionRqDocument.class),newPropValsById);
			return null;
		} else 
			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::ServiceBus::Pipeline - Server Meta*/

/*Radix::ServiceBus::Pipeline-Entity Class*/

package org.radixware.ads.ServiceBus.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Pipeline_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),"Pipeline",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBZDPWSON6VENNC7DWBNV3YM6JA"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::ServiceBus::Pipeline:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
							/*Owner Class Name*/
							"Pipeline",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBZDPWSON6VENNC7DWBNV3YM6JA"),
							/*Property presentations*/

							/*Radix::ServiceBus::Pipeline:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::ServiceBus::Pipeline:classGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOE3X3Z3RC5ANFOCA72MDUTOIVQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus::Pipeline:description:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCGJULBTXSRAFDE2IKCVHFAXWZM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus::Pipeline:diagramm:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOBH63TJRIBGKFL77Q4SKEU2SEE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus::Pipeline:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNQ7ACV4LNVHNBGGPWFPOCPH2P4"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus::Pipeline:lastUpdateTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col74BRHCTUWJBWBNSLTZU6WRQLXM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus::Pipeline:lastUpdateUserName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col75T5SOB6DRHXXJOJJO3MTTY3JE"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus::Pipeline:myEntityGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIT3FZJ6PFJC6FAK22RGG7PNYWQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus::Pipeline:myEntityPid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNFW3BHI6OJFUHJRXQHDJ77575M"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus::Pipeline:title:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA4MWSYIO2JBZPGKY445BVZJZYM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus::Pipeline:traceProfile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJN4CXGS7FVHNDIOXSQGB4FWKDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus::Pipeline:checked:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6R52LGN5LVDE7BXSJMDJIFJLRQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus::Pipeline:extGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZIZFJNA77FEKRI4DGSX7CHPXWI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus::Pipeline:isUniDirect:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNJPB4O555NESJNHFAOI2SW4KIE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus::Pipeline:classTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdM3X27ZAOHFAO5N542IEBHGZ7T4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus::Pipeline:changeLog:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruTFA54WKAU5HSPAEFWOTUOTOVRA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6HHEKF43V5BUPOUOE7WW7JP2PU")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::ServiceBus::Pipeline:Export-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdKPSQP4JZLRDLBDNFD33Y4YLPQE"),"Export",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),true,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::ServiceBus::Pipeline:Import-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdI7XAPURSLNBYVN3AGZKCO6GHQY"),"Import",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::ServiceBus::Pipeline:CreateVer-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQPFU5JRHQVE2TA6VLCAQDHO7Q4"),"CreateVer",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),false,true)
							},
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::ServiceBus::Pipeline:Id-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXI3YZF76EBEOTARF74WYDUNWAY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),"Id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCKFKG6AATRCCJOCGTL63S4NFC4"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNQ7ACV4LNVHNBGGPWFPOCPH2P4"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::ServiceBus::Pipeline:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZBJJ2XHI2RAFPFBPDE7TSLZYZY"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									null,
									144,

									/*Radix::ServiceBus::Pipeline:Edit:TitleFormat-Object Title Format*/

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

											new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colNQ7ACV4LNVHNBGGPWFPOCPH2P4"),"{0}) ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

											new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colA4MWSYIO2JBZPGKY445BVZJZYM"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
									},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.EDITOR_PRESENTATION),

									/*Radix::ServiceBus::Pipeline:Edit:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::ServiceBus::Pipeline:Edit:PipelineParam-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiUCNX44IYKBAETPVXV2K3CD7AJA"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec6LBVOFKHB5DPNDOESAUHN6SLNY"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprT546HGKIZFCXBMMYV2OCWRF4DU"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refLRQ6Q2GMEBHEJD3MPQWKFNFYX4"),
													null,
													null),

												/*Radix::ServiceBus::Pipeline:Edit:PipelineVer-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiMRMWDJOZPJEUBC3ZGWM6TBINRM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLHHBNT3LOBHEBF7ZW5C43D5COM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprMRND7UW5RZCSPH2EPT7DPZXWQY"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refLYP33RARYJCPDAJQAIFROUIFM4"),
													null,
													null),

												/*Radix::ServiceBus::Pipeline:Edit:UserFuncLib-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi4LTAWOM5INAW3L2AYKQ7LRSREU"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecAM6HEX43SFCQTMG23G7JHOBCMA"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprBZLBEB65Q5HR5DXKP7UTQ5FJ6Q"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refM2PQZRTB5BAKPGZIZRK4L3AHAM"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::ServiceBus::Pipeline:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIR4HKKXXRRHHRO5MC6S56AVBVI"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									null,
									144,

									/*Radix::ServiceBus::Pipeline:Create:TitleFormat-Object Title Format*/

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

											new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colNQ7ACV4LNVHNBGGPWFPOCPH2P4"),"{0}) ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

											new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colA4MWSYIO2JBZPGKY445BVZJZYM"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
									},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.EDITOR_PRESENTATION),

									/*Radix::ServiceBus::Pipeline:Create:Children-Explorer Items*/
										null
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::ServiceBus::Pipeline:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprEJDXY52ON5CRNK3IHMEI33UF6I"),"General",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNQ7ACV4LNVHNBGGPWFPOCPH2P4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA4MWSYIO2JBZPGKY445BVZJZYM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCGJULBTXSRAFDE2IKCVHFAXWZM"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXI3YZF76EBEOTARF74WYDUNWAY"),true,null,true,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZBJJ2XHI2RAFPFBPDE7TSLZYZY")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIR4HKKXXRRHHRO5MC6S56AVBVI")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(16416,null,null,null),org.radixware.kernel.common.types.Id.Factory.loadFrom("ecc4KTEEWZMLFDGPEWZ64FQOFGHHE"))
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("sprEJDXY52ON5CRNK3IHMEI33UF6I"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::ServiceBus::Pipeline:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colNQ7ACV4LNVHNBGGPWFPOCPH2P4"),"{0}) ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colA4MWSYIO2JBZPGKY445BVZJZYM"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::ServiceBus::Pipeline:All-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("ecc4KTEEWZMLFDGPEWZ64FQOFGHHE"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),null,30.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),null)}
									)
							},
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblX6WI554KVVCZPGNLK7CSZLOMTY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::ServiceBus::Pipeline:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::ServiceBus::Pipeline:classGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOE3X3Z3RC5ANFOCA72MDUTOIVQ"),"classGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::Pipeline:description-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCGJULBTXSRAFDE2IKCVHFAXWZM"),"description",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHESSGCJ52ZHMRLKC5K4EECUDZE"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::Pipeline:diagramm-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOBH63TJRIBGKFL77Q4SKEU2SEE"),"diagramm",null,org.radixware.kernel.common.enums.EValType.CLOB,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::Pipeline:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNQ7ACV4LNVHNBGGPWFPOCPH2P4"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMNWV3KJQKJAZDFEHDNHCSRRRRU"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::Pipeline:lastUpdateTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col74BRHCTUWJBWBNSLTZU6WRQLXM"),"lastUpdateTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4IB5JWISVRES7N37MGA5VLV7WQ"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::Pipeline:lastUpdateUserName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col75T5SOB6DRHXXJOJJO3MTTY3JE"),"lastUpdateUserName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4TBA3NKOFZCSNJ6QZNO4S5O7DY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::Pipeline:myEntityGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIT3FZJ6PFJC6FAK22RGG7PNYWQ"),"myEntityGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRDINWW7L7ZCGFL5JUR4HXK7RDY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::Pipeline:myEntityPid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNFW3BHI6OJFUHJRXQHDJ77575M"),"myEntityPid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC34NPFW42RD2XOLITIMKAIUD7A"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::Pipeline:title-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA4MWSYIO2JBZPGKY445BVZJZYM"),"title",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLRPR6VK4ERHNLBN6M4NVMV4W6A"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::Pipeline:traceProfile-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJN4CXGS7FVHNDIOXSQGB4FWKDM"),"traceProfile",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJIR6IWCKDNHDFOGURAUGNF2ADI"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Event")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::Pipeline:checked-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6R52LGN5LVDE7BXSJMDJIFJLRQ"),"checked",null,org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::Pipeline:caching-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTV2BKSOCEZFVHKYL6P2MR2I32A"),"caching",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::Pipeline:lastUpdateTimeGetter-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCFIXZVNJ5NDCJN757OABHZ22RU"),"lastUpdateTimeGetter",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::Pipeline:extGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZIZFJNA77FEKRI4DGSX7CHPXWI"),"extGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::Pipeline:isUniDirect-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNJPB4O555NESJNHFAOI2SW4KIE"),"isUniDirect",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::Pipeline:classTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdM3X27ZAOHFAO5N542IEBHGZ7T4"),"classTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7YUYHORLWBHV7PM2JRVSEA6W7Y"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus::Pipeline:changeLog-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruTFA54WKAU5HSPAEFWOTUOTOVRA"),"changeLog",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEYCDXBU5ZNEFNA7B6DEFBU42JI"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblX6WI554KVVCZPGNLK7CSZLOMTY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::ServiceBus::Pipeline:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLLZWWOAPHBF5LBB42K7W646TN4"),"checkConfig",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3O7LYVNX2RB47MNNKANF25LJTQ"),"getHolderEntityPid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQT6QWPG525CLRBTJ7Z44F3X2AA"),"getNodes",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHJSQTQ2KVBHVFJSQM74VNQWF3A"),"getDiagram",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJ7RPZ5NWKJB4VL3BERSOEP2LE4"),"setDiagram",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("diagram",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEZGRTUOBQFC4VANN7VB5THNRWA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNZ5Z7AS6RFRPIX6ZD5JDKEC34"),"beforeDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2JZ5SGWOBVC77B2AJ7XZWB3TXM"),"getNode",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pid",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3CHOMWMGPVCQTOQ4GPZW3WMPQI"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLTNCRJDYPNCOPKO2GARKLZWZBI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTJD5LKLR35A6POYGFDYIZVK4NY"),"getTraceProfile",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7ARK2SQU5RAPRFATSH7MOBF43U"),"getParams",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPBXID5RSRBGP7CHXS25MESNBGE"),"getChecked",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6KS7LCP4W5AW5OCCJHH5KGO4YQ"),"afterCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNMKONCXZ2ZBK7AIE6KRIBUGYFQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJ3FGNIYQDNFABHD4O5ON6RPK6Y"),"refreshCache",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdQPFU5JRHQVE2TA6VLCAQDHO7Q4"),"onCommand_CreateVer",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5UJDOMURZZHXVO6LQIHUYX3KM4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2SOXEXNZA5BEFMNXGFZU4U36BI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIR5AQSWXA5FDJKMH4KLR4Q7X4I"),"getDataSchemes",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRCVO24YQDVAMZAF3AXWX6MS3V4"),"process",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIL5YTMHCPNE4LFFY56D4R24M2Q"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthARVXG6A7M5C25LNEZH7J6SOTNI"),"findNodeByTitle",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMUBT2NY22JDU3EAYXMD73E4CMY"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOOVAINCA4BGC5ALUUJWY22I4DI"),"onUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth74UENACRGJE6HFPRHQFLWKGWXM"),"getVars",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdI7XAPURSLNBYVN3AGZKCO6GHQY"),"onCommand_Import",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr55GB6YI3KBHQRJFK7VTASKKRY4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVHA7VBYCYFFUJKZDXTDR2ZOUUA"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXITY34TSSNHM3KVNI6UP3QCXRA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQAHGMS3QCNGLXE7SGEET6MBLVE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPidAsStr__________________")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNQ7ACV4LNVHNBGGPWFPOCPH2P4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth36S2GWQL6BDGNDPTVXM4WJTVYU"),"create",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("classGuid",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMCC7SUK6SNCSLC42SCG6XPVEUM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRKD4DQJZ45BCBCWQDGOHYJNCVY"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDCYIW34TUBAVBHEZNEE42U2RH4"),"exportAll",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSY7BLG7O3BHKBIUZS7UE5KVS3Y")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("iter",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWECZS7BX25AR7HQL3765DBBEGA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7ZIUVSX62BG5RLRK4R3LX2J5EY"),"exportThis",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSY7BLG7O3BHKBIUZS7UE5KVS3Y"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQB6I4LWBKJDSJEEH2JRFUBEZWI"),"exportToXml",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTBSIKMREM5GHHLWFVHCQIL6DLU"),"importAll",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2RLOK42HYZCS3LYDTUKUPR2RD4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5AEE44BN5VCFBEDIUMN2FNENKA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNZLN4GS2GVAAJANRPMDGBHJCZI"),"importOne",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2RLOK42HYZCS3LYDTUKUPR2RD4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUAHGASFHCNFIBNEQK3K2GZ7XLQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4HRO3N7TKBFRTIVGY6BKVMMCSY"),"importThis",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2RLOK42HYZCS3LYDTUKUPR2RD4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPK55TF3CIRCULIX6OX7FD4N2LQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFJGLHJNOSBE3FPQ74KSXYKQUME"),"updateFromCfgItem",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("cfg",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVAMRIOYK4ZGAJIMTDCHQBGSUZ4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5IUSIB24WNFVZOBXWL2TL57JUQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFIYHCZOO2JAPZN26FMN5RHQFKI"),"loadByExtGuid",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("guid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6LBQ4WYVWBHCHCBFMYAU6M4XQQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAQ3LGL5S6ZGUFNDDGUFYUFC6WU"),"refreshCache",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("force",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDTVADL2QPJD2NJGULVQPK5KSIU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVVLOKJK5QNFKBMQPK5SNY4FANY"),"isUniDirect",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7R4KOQOXMZCHXKNGB7EQRQXYDM"),"isTransformation",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQXRW7PZ4QBBEJAJL57JFPES7EQ"),"getCfgReferenceExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTIFZEEQC2NEUNPFIRAGRH2B7EY"),"getCfgReferencePropId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth325A73ZKKJGPXC3OIDSIU52WCE"),"createSettings",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("options",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOX2X7NX6PVGCXETLEISTTTB5AQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthX5TIIN3JG5HDJBX7KONQQIVXAQ"),"createSettingsIteractive",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,null,org.radixware.kernel.common.enums.EValType.USER_CLASS)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta_adcQ6UH2IGO3JHBBLSTBPTDJHQ2UU = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcQ6UH2IGO3JHBBLSTBPTDJHQ2UU"),"CfgObjectLookupAdvizor",null,

						org.radixware.kernel.common.enums.EClassType.ENTITY,
						null,
						null,
						null,

						/*Radix::ServiceBus::Pipeline:CfgObjectLookupAdvizor:Properties-Properties*/
						null,

						/*Radix::ServiceBus::Pipeline:CfgObjectLookupAdvizor:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJPAOFWUPW5GIJKM3O7SE3KNB7Q"),"getCfgObjectsByExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUHB7QZBKHFD7LBNC422X3EGQ4M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("considerContext",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2F6PLICI4ZCSRGMFEYGOH3ZVTE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("context",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprB4UJM7ASHRBXJGTBF5AZUBXFX4"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						null,
						null,null,null,false);
}

/* Radix::ServiceBus::Pipeline - Desktop Executable*/

/*Radix::ServiceBus::Pipeline-Entity Class*/

package org.radixware.ads.ServiceBus.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline")
public interface Pipeline {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}





		public org.radixware.ads.ServiceBus.explorer.Pipeline.Pipeline_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.ServiceBus.explorer.Pipeline.Pipeline_DefaultModel )  super.getEntity(i);}
	}












































































































	/*Radix::ServiceBus::Pipeline:checked:checked-Presentation Property*/


	public class Checked extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public Checked(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:checked:checked")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:checked:checked")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Checked getChecked();
	/*Radix::ServiceBus::Pipeline:lastUpdateTime:lastUpdateTime-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:lastUpdateTime:lastUpdateTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:lastUpdateTime:lastUpdateTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastUpdateTime getLastUpdateTime();
	/*Radix::ServiceBus::Pipeline:lastUpdateUserName:lastUpdateUserName-Presentation Property*/


	public class LastUpdateUserName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LastUpdateUserName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:lastUpdateUserName:lastUpdateUserName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:lastUpdateUserName:lastUpdateUserName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LastUpdateUserName getLastUpdateUserName();
	/*Radix::ServiceBus::Pipeline:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::ServiceBus::Pipeline:description:description-Presentation Property*/


	public class Description extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Description(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:description:description")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:description:description")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Description getDescription();
	/*Radix::ServiceBus::Pipeline:myEntityGuid:myEntityGuid-Presentation Property*/


	public class MyEntityGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public MyEntityGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:myEntityGuid:myEntityGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:myEntityGuid:myEntityGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MyEntityGuid getMyEntityGuid();
	/*Radix::ServiceBus::Pipeline:traceProfile:traceProfile-Presentation Property*/


	public class TraceProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public TraceProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:traceProfile:traceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:traceProfile:traceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public TraceProfile getTraceProfile();
	/*Radix::ServiceBus::Pipeline:myEntityPid:myEntityPid-Presentation Property*/


	public class MyEntityPid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public MyEntityPid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:myEntityPid:myEntityPid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:myEntityPid:myEntityPid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MyEntityPid getMyEntityPid();
	/*Radix::ServiceBus::Pipeline:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::ServiceBus::Pipeline:diagramm:diagramm-Presentation Property*/


	public class Diagramm extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public Diagramm(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:diagramm:diagramm")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:diagramm:diagramm")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Diagramm getDiagramm();
	/*Radix::ServiceBus::Pipeline:classGuid:classGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:classGuid:classGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:classGuid:classGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassGuid getClassGuid();
	/*Radix::ServiceBus::Pipeline:extGuid:extGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:extGuid:extGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:extGuid:extGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtGuid getExtGuid();
	/*Radix::ServiceBus::Pipeline:classTitle:classTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:classTitle:classTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:classTitle:classTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassTitle getClassTitle();
	/*Radix::ServiceBus::Pipeline:isUniDirect:isUniDirect-Presentation Property*/


	public class IsUniDirect extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsUniDirect(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:isUniDirect:isUniDirect")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:isUniDirect:isUniDirect")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsUniDirect getIsUniDirect();
	/*Radix::ServiceBus::Pipeline:changeLog:changeLog-Presentation Property*/


	public class ChangeLog extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public ChangeLog(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.explorer.ChangeLog.ChangeLog_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.CfgManagement.explorer.ChangeLog.ChangeLog_DefaultModel)super.openEntityModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:changeLog:changeLog")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:changeLog:changeLog")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ChangeLog getChangeLog();
	public static class Import extends org.radixware.kernel.common.client.models.items.Command{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.types.StrDocument send(org.radixware.ads.ServiceBus.common.ImpExpXsd.PipelineDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.types.StrDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.types.StrDocument.class);
		}

	}

	public static class Export extends org.radixware.kernel.common.client.models.items.Command{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class CreateVer extends org.radixware.kernel.common.client.models.items.Command{
		protected CreateVer(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.ads.ServiceBus.Cmd.common.CommandsXsd.VersionRqDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			processResponse(response, null);
		}

	}



}

/* Radix::ServiceBus::Pipeline - Desktop Meta*/

/*Radix::ServiceBus::Pipeline-Entity Class*/

package org.radixware.ads.ServiceBus.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Pipeline_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::ServiceBus::Pipeline:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
			"Radix::ServiceBus::Pipeline",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("img4CFUGEUNT5AI3FWWPNGVA355IA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprEJDXY52ON5CRNK3IHMEI33UF6I"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBZDPWSON6VENNC7DWBNV3YM6JA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4PGYZGVUSVABNHTJNRSXOIX5WI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBZDPWSON6VENNC7DWBNV3YM6JA"),0,

			/*Radix::ServiceBus::Pipeline:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::ServiceBus::Pipeline:classGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOE3X3Z3RC5ANFOCA72MDUTOIVQ"),
						"classGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
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

						/*Radix::ServiceBus::Pipeline:classGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus::Pipeline:description:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCGJULBTXSRAFDE2IKCVHFAXWZM"),
						"description",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHESSGCJ52ZHMRLKC5K4EECUDZE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
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

						/*Radix::ServiceBus::Pipeline:description:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus::Pipeline:diagramm:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOBH63TJRIBGKFL77Q4SKEU2SEE"),
						"diagramm",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
						org.radixware.kernel.common.enums.EValType.CLOB,
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

						/*Radix::ServiceBus::Pipeline:diagramm:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus::Pipeline:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNQ7ACV4LNVHNBGGPWFPOCPH2P4"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMNWV3KJQKJAZDFEHDNHCSRRRRU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::ServiceBus::Pipeline:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus::Pipeline:lastUpdateTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col74BRHCTUWJBWBNSLTZU6WRQLXM"),
						"lastUpdateTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4IB5JWISVRES7N37MGA5VLV7WQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::ServiceBus::Pipeline:lastUpdateTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus::Pipeline:lastUpdateUserName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col75T5SOB6DRHXXJOJJO3MTTY3JE"),
						"lastUpdateUserName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4TBA3NKOFZCSNJ6QZNO4S5O7DY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
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
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::ServiceBus::Pipeline:lastUpdateUserName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus::Pipeline:myEntityGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIT3FZJ6PFJC6FAK22RGG7PNYWQ"),
						"myEntityGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRDINWW7L7ZCGFL5JUR4HXK7RDY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
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

						/*Radix::ServiceBus::Pipeline:myEntityGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus::Pipeline:myEntityPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNFW3BHI6OJFUHJRXQHDJ77575M"),
						"myEntityPid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC34NPFW42RD2XOLITIMKAIUD7A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
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

						/*Radix::ServiceBus::Pipeline:myEntityPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus::Pipeline:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA4MWSYIO2JBZPGKY445BVZJZYM"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLRPR6VK4ERHNLBN6M4NVMV4W6A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
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

						/*Radix::ServiceBus::Pipeline:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus::Pipeline:traceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJN4CXGS7FVHNDIOXSQGB4FWKDM"),
						"traceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJIR6IWCKDNHDFOGURAUGNF2ADI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeF3QUTT7XY3NRDB6ZAALOMT5GDM"),
						true,

						/*Radix::ServiceBus::Pipeline:traceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus::Pipeline:checked:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6R52LGN5LVDE7BXSJMDJIFJLRQ"),
						"checked",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::ServiceBus::Pipeline:checked:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus::Pipeline:extGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZIZFJNA77FEKRI4DGSX7CHPXWI"),
						"extGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
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

						/*Radix::ServiceBus::Pipeline:extGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus::Pipeline:isUniDirect:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNJPB4O555NESJNHFAOI2SW4KIE"),
						"isUniDirect",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
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

						/*Radix::ServiceBus::Pipeline:isUniDirect:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus::Pipeline:classTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdM3X27ZAOHFAO5N542IEBHGZ7T4"),
						"classTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7YUYHORLWBHV7PM2JRVSEA6W7Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
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

						/*Radix::ServiceBus::Pipeline:classTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus::Pipeline:changeLog:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruTFA54WKAU5HSPAEFWOTUOTOVRA"),
						"changeLog",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						5,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6HHEKF43V5BUPOUOE7WW7JP2PU")},
						null,
						0L,
						0L,false,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::ServiceBus::Pipeline:Export-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdKPSQP4JZLRDLBDNFD33Y4YLPQE"),
						"Export",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3ATIATSQ6VANJG2AZFUEWBSXUU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgVORNT5ZFORBEHLSYMJNUWJK6II"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("afh23ABGRFJERE6VD6NXQQJKOVFRA"),
						org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
						true,
						false,
						true,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::ServiceBus::Pipeline:Import-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdI7XAPURSLNBYVN3AGZKCO6GHQY"),
						"Import",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNFP6CA2IZFCPBIUQDN5XSSKJS4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgMUCCZVMA4JC4NMTVVEGYXEXU5Q"),
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
					/*Radix::ServiceBus::Pipeline:CreateVer-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQPFU5JRHQVE2TA6VLCAQDHO7Q4"),
						"CreateVer",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsID2B5UVPSNANVIBMOGIA4IGL6M"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgRVFCLIYKQJHC7OZEPG3RNPZUBA"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
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
					/*Radix::ServiceBus::Pipeline:Id-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXI3YZF76EBEOTARF74WYDUNWAY"),
						"Id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCKFKG6AATRCCJOCGTL63S4NFC4"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNQ7ACV4LNVHNBGGPWFPOCPH2P4"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3QADUGWZLNGYHNQ7SBIEACTOG4"),"Pipeline=>User (lastUpdateUserName=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblX6WI554KVVCZPGNLK7CSZLOMTY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col75T5SOB6DRHXXJOJJO3MTTY3JE")},new String[]{"lastUpdateUserName"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4")},new String[]{"name"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZBJJ2XHI2RAFPFBPDE7TSLZYZY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIR4HKKXXRRHHRO5MC6S56AVBVI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprEJDXY52ON5CRNK3IHMEI33UF6I")},
			true,false,false);
}

/* Radix::ServiceBus::Pipeline - Web Meta*/

/*Radix::ServiceBus::Pipeline-Entity Class*/

package org.radixware.ads.ServiceBus.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Pipeline_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::ServiceBus::Pipeline:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
			"Radix::ServiceBus::Pipeline",
			null,
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBZDPWSON6VENNC7DWBNV3YM6JA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4PGYZGVUSVABNHTJNRSXOIX5WI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBZDPWSON6VENNC7DWBNV3YM6JA"),0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::ServiceBus::Pipeline:Edit - Desktop Meta*/

/*Radix::ServiceBus::Pipeline:Edit-Editor Presentation*/

package org.radixware.ads.ServiceBus.explorer;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZBJJ2XHI2RAFPFBPDE7TSLZYZY"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblX6WI554KVVCZPGNLK7CSZLOMTY"),
	null,
	null,

	/*Radix::ServiceBus::Pipeline:Edit:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::ServiceBus::Pipeline:Edit:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgLDYPL2BKCVHFHCICPQZWYNVMTA"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIT7YGWHLLVCQBEGQT6SDYPYBEQ"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNQ7ACV4LNVHNBGGPWFPOCPH2P4"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA4MWSYIO2JBZPGKY445BVZJZYM"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col74BRHCTUWJBWBNSLTZU6WRQLXM"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col75T5SOB6DRHXXJOJJO3MTTY3JE"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCGJULBTXSRAFDE2IKCVHFAXWZM"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJN4CXGS7FVHNDIOXSQGB4FWKDM"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdM3X27ZAOHFAO5N542IEBHGZ7T4"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruTFA54WKAU5HSPAEFWOTUOTOVRA"),0,7,1,false,false)
			},null),

			/*Radix::ServiceBus::Pipeline:Edit:Diagram-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadCustomEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgI6R3GW2U4RBQ3MTRDQ5NIFILAE"),"Diagram",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU3ANYYPHZBHA5LGFM4R7FPTXKU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("img4CFUGEUNT5AI3FWWPNGVA355IA"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("cepJ2YQAK7IFNCBFMHSVK5W3F5OA4")),

			/*Radix::ServiceBus::Pipeline:Edit:Params-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgZX2VDXB7OVCGRLPRRBDJ35AQHU"),"Params",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiUCNX44IYKBAETPVXV2K3CD7AJA")),

			/*Radix::ServiceBus::Pipeline:Edit:Versions-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgWAMDOGG3PJCHJE3N7J6F552XHM"),"Versions",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiMRMWDJOZPJEUBC3ZGWM6TBINRM")),

			/*Radix::ServiceBus::Pipeline:Edit:Function Libraries-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg7F6AQNDWLBGBXINKPOTU75ZZ7Y"),"Function Libraries",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi4LTAWOM5INAW3L2AYKQ7LRSREU"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgLDYPL2BKCVHFHCICPQZWYNVMTA")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgI6R3GW2U4RBQ3MTRDQ5NIFILAE")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgZX2VDXB7OVCGRLPRRBDJ35AQHU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgWAMDOGG3PJCHJE3N7J6F552XHM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg7F6AQNDWLBGBXINKPOTU75ZZ7Y"))}
	,

	/*Radix::ServiceBus::Pipeline:Edit:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::ServiceBus::Pipeline:Edit:PipelineParam-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiUCNX44IYKBAETPVXV2K3CD7AJA"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec6LBVOFKHB5DPNDOESAUHN6SLNY"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprT546HGKIZFCXBMMYV2OCWRF4DU"),
					0,
					null,
					16560,true),

				/*Radix::ServiceBus::Pipeline:Edit:PipelineVer-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiMRMWDJOZPJEUBC3ZGWM6TBINRM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLHHBNT3LOBHEBF7ZW5C43D5COM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprMRND7UW5RZCSPH2EPT7DPZXWQY"),
					0,
					null,
					16560,true),

				/*Radix::ServiceBus::Pipeline:Edit:UserFuncLib-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi4LTAWOM5INAW3L2AYKQ7LRSREU"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecAM6HEX43SFCQTMG23G7JHOBCMA"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprBZLBEB65Q5HR5DXKP7UTQ5FJ6Q"),
					0,
					null,
					16560,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	144,0,0,null);
}
/* Radix::ServiceBus::Pipeline:Edit:Model - Desktop Executable*/

/*Radix::ServiceBus::Pipeline:Edit:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.explorer;

import java.util.*;


@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Model")
public class Edit:Model  extends org.radixware.ads.ServiceBus.explorer.Pipeline.Pipeline_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }

	private ServiceBus.Graph::GraphView graph;

	private com.trolltech.qt.gui.QToolButton deleteBtn;
	private com.trolltech.qt.gui.QToolButton directionBtn;
	private com.trolltech.qt.gui.QToolButton duplicateBtn;
	private com.trolltech.qt.gui.QToolButton pointerBtn;
	private com.trolltech.qt.gui.QToolButton lineBtn;
	private com.trolltech.qt.gui.QToolButton checkBtn;
	private com.trolltech.qt.gui.QToolButton commentBtn;

	private final List<com.trolltech.qt.gui.QToolButton> btns = new ArrayList<com.trolltech.qt.gui.QToolButton>();
	private static Map<String,ServiceBus.Cmd::CommandsXsd:NodeInfoListRsDocument> xInfoMap = new java.util.HashMap<String,ServiceBus.Cmd::CommandsXsd:NodeInfoListRsDocument>();

	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus::Pipeline:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus::Pipeline:Edit:Model:Properties-Properties*/

	/*Radix::ServiceBus::Pipeline:Edit:Model:Methods-Methods*/

	/*Radix::ServiceBus::Pipeline:Edit:Model:EditorPageView_opened-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Model:EditorPageView_opened")
	public  void EditorPageView_opened (com.trolltech.qt.gui.QWidget widget) {
		graph = new GraphView(Environment, this, getPid());

		DiagramXsd:DiagramDocument xDoc = null;
		if (diagramm.Value != null) {
		    try {
		        xDoc = DiagramXsd:DiagramDocument.Factory.parse(diagramm.Value);
		    } catch (Exceptions::XmlException e) {
		        Explorer.Env::Application.processException(e);        
		        xDoc = null;
		    }
		}
		graph.import(xDoc);

		graph.inserted.connect(this, idof[Pipeline:Edit:Edit:Model:itemInserted] + "()");
		graph.modified.connect(this, idof[Pipeline:Edit:Edit:Model:modified] + "()");
		graph.scene().selectionChanged.connect(this, idof[Pipeline:Edit:Edit:Model:selectionChanged] + "()");

		com.trolltech.qt.gui.QToolBar toolBar = new com.trolltech.qt.gui.QToolBar();
		Pipeline:Edit:Diagram:View:EditorPageView.layout().addWidget(toolBar);
		Pipeline:Edit:Diagram:View:EditorPageView.layout().addWidget(graph);

		deleteBtn = new com.trolltech.qt.gui.QToolButton();
		deleteBtn.setToolTip("Delete");
		deleteBtn.setIcon(Explorer.Icons::ExplorerIcon.getQIcon(Environment.DefManager.getImage(idof[ServiceBus.Dlg::delete])));
		deleteBtn.clicked.connect(this, idof[Pipeline:Edit:Edit:Model:deleteBtn_clicked] + "()");
		toolBar.addWidget(deleteBtn);

		directionBtn = new com.trolltech.qt.gui.QToolButton();
		directionBtn.setToolTip("Change Direction");
		directionBtn.setIcon(Explorer.Icons::ExplorerIcon.getQIcon(Environment.DefManager.getImage(idof[System::Restart])));
		directionBtn.clicked.connect(this, idof[Pipeline:Edit:Edit:Model:inverseBtn_clicked] + "()");
		toolBar.addWidget(directionBtn);

		duplicateBtn = new com.trolltech.qt.gui.QToolButton();
		duplicateBtn.setToolTip("Create Node Copy");
		duplicateBtn.setIcon(Explorer.Icons::ExplorerIcon.getQIcon(Environment.DefManager.getImage(idof[Explorer.Icons::copy])));
		duplicateBtn.clicked.connect(this, idof[Pipeline:Edit:Edit:Model:duplicateBtn_clicked] + "()");
		toolBar.addWidget(duplicateBtn);

		toolBar.addSeparator();

		pointerBtn = new com.trolltech.qt.gui.QToolButton();
		pointerBtn.setCheckable(true);
		pointerBtn.setToolTip("Select Node");
		pointerBtn.setIcon(Explorer.Icons::ExplorerIcon.getQIcon(Environment.DefManager.getImage(idof[ServiceBus.Dlg::pointer])));
		pointerBtn.clicked.connect(this, idof[Pipeline:Edit:Edit:Model:pointerBtn_clicked] + "()");
		toolBar.addWidget(pointerBtn);

		lineBtn = new com.trolltech.qt.gui.QToolButton();
		lineBtn.setCheckable(true);
		lineBtn.setToolTip("Create Link");
		lineBtn.setIcon(Explorer.Icons::ExplorerIcon.getQIcon(Environment.DefManager.getImage(idof[ServiceBus.Dlg::linepointer])));
		lineBtn.clicked.connect(this, idof[Pipeline:Edit:Edit:Model:lineBtn_clicked] + "()");
		toolBar.addWidget(lineBtn);

		toolBar.addSeparator();

		checkBtn = new com.trolltech.qt.gui.QToolButton();
		checkBtn.setToolTip("Check");
		checkBtn.setIcon(Explorer.Icons::ExplorerIcon.getQIcon(Environment.DefManager.getImage(idof[Explorer.Icons::type_bool])));
		checkBtn.clicked.connect(this, idof[Pipeline:Edit:Edit:Model:checkBtn_clicked] + "()");
		toolBar.addWidget(checkBtn);

		toolBar.addSeparator();

		commentBtn = new com.trolltech.qt.gui.QToolButton();
		commentBtn.setCheckable(true);
		commentBtn.setToolTip("Comment");
		commentBtn.setIcon(Explorer.Icons::ExplorerIcon.getQIcon(Environment.DefManager.getImage(idof[Explorer.Icons::edit])));
		commentBtn.clicked.connect(this, idof[Pipeline:Edit:Edit:Model:commentBtn_clicked] + "()");
		toolBar.addWidget(commentBtn);

		ServiceBus.Cmd::CommandsXsd:NodeInfoListRsDocument xInfoList = xInfoMap.get(classGuid.Value);

		if (xInfoList == null){
		    xInfoList = ServiceBus.Cmd::CommandUtil.getNodeInfoList(Environment,id.Value);
		    xInfoMap.put(classGuid.Value,xInfoList);
		}

		if (xInfoList != null) {
		    final Map<SbPos, ServiceBus.Cmd::CommandsXsd:NodeInfo> items = new HashMap<SbPos, ServiceBus.Cmd::CommandsXsd:NodeInfo>();
		    final Map<Num, List<Num>> xys = new HashMap<Num, List<Num>>();
		    for (ServiceBus.Cmd::CommandsXsd:NodeInfo xInfo: xInfoList.NodeInfoListRs.NodeInfoList) {
		        items.put(new SbPos(xInfo.OrderPosX, xInfo.OrderPosY), xInfo);
		        List<Num> ys = xys.get(xInfo.OrderPosX);
		        if (ys == null) {
		            ys = new ArrayList<Num>();
		            xys.put(xInfo.OrderPosX, ys);
		        }
		        ys.add(xInfo.OrderPosY);
		    }
		    
		    final List<Num> xs = new ArrayList<Num>(xys.keySet());
		    java.util.Collections.sort(xs);

		    btns.clear();
		    for (Num x: xs) {
		        final List<Num> ys = xys.get(x);
		        java.util.Collections.sort(ys);
		        
		        SbPos p = new SbPos(x, ys.get(0));
		        ServiceBus.Cmd::CommandsXsd:NodeInfo xInfo = items.get(p);
		        final com.trolltech.qt.gui.QToolButton btn = new com.trolltech.qt.gui.QToolButton();
		        btn.setCheckable(true);
		        if (xInfo.Icon != null)
		            btn.setIcon(Explorer.Icons::ExplorerIcon.getQIcon(Environment.DefManager.getImage(Types::Id.Factory.loadFrom(xInfo.Icon))));
		        btn.setToolTip(xInfo.Title);
		        btn.setProperty("info", xInfo);
		        btn.clicked.connect(this, idof[Pipeline:Edit:Edit:Model:nodeBtn_clicked] + "()");
		        btns.add(btn);
		        
		        if (ys.size() <= 1)
		            continue;
		                
		        final com.trolltech.qt.gui.QMenu menu = new com.trolltech.qt.gui.QMenu(btn);
		        btn.setPopupMode(com.trolltech.qt.gui.QToolButton.ToolButtonPopupMode.InstantPopup);
		        btn.setMenu(menu);
		        for(Num y : ys) {
		            p = new SbPos(x, y);
		            xInfo = items.get(p);
		            final Explorer.Qt.Types::QAction action = menu.addAction(xInfo.Title);
		            action.setProperty("info", xInfo);
		            action.setProperty("btn", btn);
		            if (xInfo.Icon != null)
		                action.setIcon(Explorer.Icons::ExplorerIcon.getQIcon(Environment.DefManager.getImage(Types::Id.Factory.loadFrom(xInfo.Icon))));
		            action.triggered.connect(this, idof[Pipeline:Edit:Edit:Model:nodeMenu_clicked] + "()");
		        }
		    }
		    
		    for (com.trolltech.qt.gui.QToolButton btn: btns)
		        toolBar.addWidget(btn);
		}

		updateApiBtn();
		updateButtons(pointerBtn);
		selectionChanged();

		diagramm.subscribe(graph);
	}

	/*Radix::ServiceBus::Pipeline:Edit:Model:checkBtn_clicked-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Model:checkBtn_clicked")
	public  void checkBtn_clicked () {
		graph.setMode(ServiceBus.Graph::GraphView.MOVE_MODE);
		updateButtons(pointerBtn);

		Str mess = graph.checkConfig();
		checked.setValue(mess == null);
		if (mess != null)
		    Explorer.Dialogs::SimpleDlg.messageError(Environment,"Diagram Check", mess);
		else
		    Explorer.Dialogs::SimpleDlg.messageInformation(Environment,"Diagram Check", "Diagram is valid");

	}

	/*Radix::ServiceBus::Pipeline:Edit:Model:deleteBtn_clicked-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Model:deleteBtn_clicked")
	public  void deleteBtn_clicked () {
		graph.deleteSelected();
		selectionChanged();
	}

	/*Radix::ServiceBus::Pipeline:Edit:Model:inverseBtn_clicked-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Model:inverseBtn_clicked")
	public  void inverseBtn_clicked () {
		graph.inverseSelected();
	}

	/*Radix::ServiceBus::Pipeline:Edit:Model:itemInserted-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Model:itemInserted")
	public  void itemInserted () {
		graph.setMode(ServiceBus.Graph::GraphView.MOVE_MODE);
		updateButtons(pointerBtn);
	}

	/*Radix::ServiceBus::Pipeline:Edit:Model:lineBtn_clicked-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Model:lineBtn_clicked")
	public  void lineBtn_clicked () {
		graph.setMode(ServiceBus.Graph::GraphView.INSERT_LINE_MODE);
		graph.scene().clearSelection();
		updateButtons(lineBtn);
	}

	/*Radix::ServiceBus::Pipeline:Edit:Model:nodeBtn_clicked-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Model:nodeBtn_clicked")
	public  void nodeBtn_clicked () {
		final com.trolltech.qt.gui.QToolButton nodeBtn = (com.trolltech.qt.gui.QToolButton)Explorer.Qt.Types::QObject.signalSender();
		graph.setMode(ServiceBus.Graph::GraphView.INSERT_NODE_MODE, (ServiceBus.Cmd::CommandsXsd:NodeInfo)nodeBtn.property("info"));
		graph.scene().clearSelection();
		updateButtons(nodeBtn);
	}

	/*Radix::ServiceBus::Pipeline:Edit:Model:pointerBtn_clicked-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Model:pointerBtn_clicked")
	public  void pointerBtn_clicked () {
		graph.setMode(ServiceBus.Graph::GraphView.MOVE_MODE);
		updateButtons(pointerBtn);
	}

	/*Radix::ServiceBus::Pipeline:Edit:Model:selectionChanged-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Model:selectionChanged")
	public  void selectionChanged () {
		deleteBtn.setEnabled(graph.scene().selectedItems().size() > 0);

		boolean isDirectNodeSelected = false;
		boolean isDuplicateNodeSelected = false;
		for (Explorer.Qt.Types::QGraphicsItemInterface item: graph.scene().selectedItems()) {
		    if (!(item instanceof ServiceBus.Graph::BaseNode))
		        continue;
		    final ServiceBus.Graph::BaseNode node = (ServiceBus.Graph::BaseNode)item;
		    isDirectNodeSelected = isDirectNodeSelected || (node instanceof ServiceBus.Graph::Node);    
		    isDuplicateNodeSelected = isDuplicateNodeSelected || !node.isApi();
		}

		directionBtn.setEnabled(isDirectNodeSelected);
		duplicateBtn.setEnabled(isDuplicateNodeSelected);
	}

	/*Radix::ServiceBus::Pipeline:Edit:Model:updateButtons-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Model:updateButtons")
	public  void updateButtons (com.trolltech.qt.gui.QToolButton checkedBtn) {
		pointerBtn.setChecked(false);
		lineBtn.setChecked(false);

		for (com.trolltech.qt.gui.QToolButton btn: btns) 
		    btn.setChecked(false);
		    
		commentBtn.setChecked(false);
		if (checkedBtn != null)
		    checkedBtn.setChecked(true);
	}

	/*Radix::ServiceBus::Pipeline:Edit:Model:modified-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Model:modified")
	public  void modified () {
		diagramm.unsubscribe(graph);
		final DiagramXsd:DiagramDocument xDoc = graph.export();
		if (xDoc != null)
		    diagramm.setValue(xDoc.xmlText());
		else
		    diagramm.setValue(null);

		updateApiBtn();
		diagramm.subscribe(graph);
	}

	/*Radix::ServiceBus::Pipeline:Edit:Model:EditorPageView_closed-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Model:EditorPageView_closed")
	public  void EditorPageView_closed () {
		diagramm.unsubscribe(graph);
		graph.inserted.disconnect();
		graph.scene().selectionChanged.disconnect();
	}

	/*Radix::ServiceBus::Pipeline:Edit:Model:nodeMenu_clicked-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Model:nodeMenu_clicked")
	public  void nodeMenu_clicked () {
		Explorer.Qt.Types::QAction action = (Explorer.Qt.Types::QAction)Explorer.Qt.Types::QObject.signalSender();
		graph.setMode(ServiceBus.Graph::GraphView.INSERT_NODE_MODE, (ServiceBus.Cmd::CommandsXsd:NodeInfo)action.property("info"));
		graph.scene().clearSelection();
		updateButtons((com.trolltech.qt.gui.QToolButton)action.property("btn"));
	}

	/*Radix::ServiceBus::Pipeline:Edit:Model:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Model:beforeUpdate")
	protected published  boolean beforeUpdate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if (graph != null) {
		    checked.setValue(graph.checkConfig() == null);
		}
		return super.beforeUpdate();
	}

	/*Radix::ServiceBus::Pipeline:Edit:Model:duplicateBtn_clicked-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Model:duplicateBtn_clicked")
	public  void duplicateBtn_clicked () {
		graph.duplicateSelected();
	}

	/*Radix::ServiceBus::Pipeline:Edit:Model:commentBtn_clicked-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Model:commentBtn_clicked")
	public  void commentBtn_clicked () {
		final com.trolltech.qt.gui.QToolButton commentBtn = (com.trolltech.qt.gui.QToolButton)Explorer.Qt.Types::QObject.signalSender();
		graph.setMode(ServiceBus.Graph::GraphView.INSERT_NODE_MODE, null);
		graph.scene().clearSelection();
		updateButtons(commentBtn);
	}

	/*Radix::ServiceBus::Pipeline:Edit:Model:onCommand_CreateVer-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Model:onCommand_CreateVer")
	protected  void onCommand_CreateVer (org.radixware.ads.ServiceBus.explorer.Pipeline.CreateVer command) {
		final Common.Dlg::CommentDlg dlg = SbDesktopUtil.showCommentDlg(Environment);
		final Common.Dlg::CommentDlg:Model model = dlg.getModel();
		if (dlg.exec() != Explorer.Qt.Types::QDialog.DialogCode.Accepted.value())
		    return;

		final ServiceBus.Cmd::CommandsXsd:VersionRqDocument xDoc = ServiceBus.Cmd::CommandsXsd:VersionRqDocument.Factory.newInstance();
		xDoc.addNewVersionRq().Notes = model.comment;

		try {
		    command.send(xDoc);
		} catch(Exceptions::Exception e) {
		    showException(e);
		    return;
		}

		Explorer.Env::Application.messageInformation("Version created");

		try {
		    getView().reread();
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}

	/*Radix::ServiceBus::Pipeline:Edit:Model:delete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Model:delete")
	public published  boolean delete (boolean forced) throws org.radixware.kernel.common.exceptions.ServiceClientException,java.lang.InterruptedException {
		try {
		    return super.delete(forced);
		} catch (Exceptions::ServiceClientException e) {
		    Explorer.Dialogs::SimpleDlg.messageError(getEnvironment(), e.getMessage());
		}
		return false;
	}

	/*Radix::ServiceBus::Pipeline:Edit:Model:updateApiBtn-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Model:updateApiBtn")
	public  void updateApiBtn () {
		final Set<ServiceBus.Graph::Node> nodes = graph.getNodesByClassId(idof[ServiceBus.Nodes::Endpoint.PipelineApi]);
		for (com.trolltech.qt.gui.QToolButton btn : btns) {
		    ServiceBus.Cmd::CommandsXsd:NodeInfo xInfo = (ServiceBus.Cmd::CommandsXsd:NodeInfo) btn.property("info");
		    if (xInfo.ClassId == idof[ServiceBus.Nodes::Endpoint.PipelineApi].toString()) {
		        if (isUniDirectPipeline()) {
		            btn.setVisible(false);
		        } else {
		            btn.setEnabled(nodes.isEmpty());
		        }
		        break;
		    }
		}
	}

	/*Radix::ServiceBus::Pipeline:Edit:Model:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Model:onCommand_Import")
	protected  void onCommand_Import (org.radixware.ads.ServiceBus.explorer.Pipeline.Import command) {
		Bool toSave = Explorer.Env::Application.messageQuestion(command.getTitle(), "Save current version before replacing?");
		if (toSave == null)
		    return;
		if (toSave == true) {
		    final Common.Dlg::CommentDlg dlg = SbDesktopUtil.showCommentDlg(Environment);
		    final Common.Dlg::CommentDlg:Model model = dlg.getModel();
		    if (dlg.execDialog() != Client.Views::DialogResult.ACCEPTED)
		        return;

		    final ServiceBus.Cmd::CommandsXsd:VersionRqDocument xDoc = ServiceBus.Cmd::CommandsXsd:VersionRqDocument.Factory.newInstance();
		    xDoc.addNewVersionRq().Notes = model.comment;
		    try {
		        getEnvironment().getEasSession().executeCommand(this, null, idof[Pipeline:CreateVer], null, xDoc);
		    } catch (Exceptions::Exception e) {
		        showException(e);
		        return;
		    }
		}

		java.io.File file = CfgManagement::DesktopUtils.openFileForImport(findNearestView(), command.getTitle());
		if (file == null)
		    return;

		try {
		    final ImpExpXsd:PipelineDocument xDoc = ImpExpXsd:PipelineDocument.Factory.parse(file);
		    if (!SbDesktopUtil.inputReferences(getEnvironment(), xDoc))
		        return;

		    Common.Dlg::ClientUtils.viewImportResult(command.send(xDoc));
		    if (getView() != null)
		        getView().reread();
		} catch (Exceptions::XmlException | Exceptions::ServiceClientException | Exceptions::IOException e) {
		    showException(e);
		} catch (Exceptions::InterruptedException e) {
		}
	}

	/*Radix::ServiceBus::Pipeline:Edit:Model:isUniDirectPipeline-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Model:isUniDirectPipeline")
	public  boolean isUniDirectPipeline () {
		return this.isUniDirect.Value == Bool.TRUE;
	}
	public final class Import extends org.radixware.ads.ServiceBus.explorer.Pipeline.Import{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Import( this );
		}

	}

	public final class CreateVer extends org.radixware.ads.ServiceBus.explorer.Pipeline.CreateVer{
		protected CreateVer(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_CreateVer( this );
		}

	}




















}

/* Radix::ServiceBus::Pipeline:Edit:Model - Desktop Meta*/

/*Radix::ServiceBus::Pipeline:Edit:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemZBJJ2XHI2RAFPFBPDE7TSLZYZY"),
						"Edit:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus::Pipeline:Edit:Model:Properties-Properties*/
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

/* Radix::ServiceBus::Pipeline:Edit:Diagram:View - Desktop Executable*/

/*Radix::ServiceBus::Pipeline:Edit:Diagram:View-Custom Page Editor for Desktop*/

package org.radixware.ads.ServiceBus.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Diagram:View")
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
		EditorPageView.setGeometry(new com.trolltech.qt.core.QRect(0, 0, 566, 280));
		EditorPageView.setFont(DEFAULT_FONT);
		final com.trolltech.qt.gui.QVBoxLayout $layout1 = new com.trolltech.qt.gui.QVBoxLayout(EditorPageView);
		$layout1.setObjectName("verticalLayout");
		$layout1.setContentsMargins(1, 1, 1, 1);
		$layout1.setSizeConstraint(com.trolltech.qt.gui.QLayout.SizeConstraint.SetDefaultConstraint);
		$layout1.setSpacing(1);
		EditorPageView.opened.connect(model, "mthDBJHRWW7WJHMDC4ZFMTMPNA6XY(com.trolltech.qt.gui.QWidget)");
		EditorPageView.closed.connect(model, "mth3PKCH2KGWBFVFA4BWX7CNVGNWU()");
		opened.emit(this);
	}
	public final org.radixware.ads.ServiceBus.explorer.Edit:Model getModel() {
		return (org.radixware.ads.ServiceBus.explorer.Edit:Model) super.getModel();
	}

}

/* Radix::ServiceBus::Pipeline:Edit:Diagram:WebView - Web Executable*/

/*Radix::ServiceBus::Pipeline:Edit:Diagram:WebView-Rwt Custom Page Editor*/

package org.radixware.ads.ServiceBus.web;
@SuppressWarnings({"unchecked","rawtypes"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:Edit:Diagram:WebView")
public class WebView extends org.radixware.wps.views.editor.EditorPageView{
	public WebView widget;
	public WebView getWidget(){ return  widget;}
	public WebView(org.radixware.kernel.common.client.IClientEnvironment env,org.radixware.kernel.common.client.views.IView view
	,org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page){
		super(env,view,page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model){
		super.open(model);
		//============ Radix::ServiceBus::Pipeline:Edit:Diagram:WebView:widget ==============
		this.widget = this;
		fireOpened();
	}
}

/* Radix::ServiceBus::Pipeline:Create - Desktop Meta*/

/*Radix::ServiceBus::Pipeline:Create-Editor Presentation*/

package org.radixware.ads.ServiceBus.explorer;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIR4HKKXXRRHHRO5MC6S56AVBVI"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblX6WI554KVVCZPGNLK7CSZLOMTY"),
			null,
			null,

			/*Radix::ServiceBus::Pipeline:Create:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::ServiceBus::Pipeline:Create:General-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMJBLJZXGKFFK5IULS6N7XJJJJE"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNQ7ACV4LNVHNBGGPWFPOCPH2P4"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA4MWSYIO2JBZPGKY445BVZJZYM"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCGJULBTXSRAFDE2IKCVHFAXWZM"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdM3X27ZAOHFAO5N542IEBHGZ7T4"),0,1,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMJBLJZXGKFFK5IULS6N7XJJJJE"))}
			,

			/*Radix::ServiceBus::Pipeline:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			144,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.ServiceBus.explorer.Pipeline.Pipeline_DefaultModel(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::ServiceBus::Pipeline:General - Desktop Meta*/

/*Radix::ServiceBus::Pipeline:General-Selector Presentation*/

package org.radixware.ads.ServiceBus.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprEJDXY52ON5CRNK3IHMEI33UF6I"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX6WI554KVVCZPGNLK7CSZLOMTY"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblX6WI554KVVCZPGNLK7CSZLOMTY"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXI3YZF76EBEOTARF74WYDUNWAY"),
		null,
		false,
		true,
		null,
		16416,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIR4HKKXXRRHHRO5MC6S56AVBVI")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZBJJ2XHI2RAFPFBPDE7TSLZYZY")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNQ7ACV4LNVHNBGGPWFPOCPH2P4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA4MWSYIO2JBZPGKY445BVZJZYM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCGJULBTXSRAFDE2IKCVHFAXWZM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
}
/* Radix::ServiceBus::Pipeline:General:Model - Desktop Executable*/

/*Radix::ServiceBus::Pipeline:General:Model-Group Model Class*/

package org.radixware.ads.ServiceBus.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:General:Model")
public class General:Model  extends org.radixware.ads.ServiceBus.explorer.Pipeline.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus::Pipeline:General:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus::Pipeline:General:Model:Properties-Properties*/

	/*Radix::ServiceBus::Pipeline:General:Model:Methods-Methods*/

	/*Radix::ServiceBus::Pipeline:General:Model:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::Pipeline:General:Model:onCommand_Import")
	protected  void onCommand_Import (org.radixware.ads.ServiceBus.explorer.PipelineGroup.Import command) {
		java.io.File file = CfgManagement::DesktopUtils.openFileForImport(findNearestView(), command.getTitle());
		if (file == null)
		    return;

		try {
		    ImpExpXsd:PipelineGroupDocument xDoc;
		    try {
		        xDoc = ImpExpXsd:PipelineGroupDocument.Factory.parse(file);
		        if (!SbDesktopUtil.inputReferences(getEnvironment(), xDoc)) 
		            return;
		    } catch (Exceptions::XmlException ex) {
		        ImpExpXsd:PipelineDocument xDoc2 = ImpExpXsd:PipelineDocument.Factory.parse(file);
		        if (!SbDesktopUtil.inputReferences(getEnvironment(), xDoc2)) 
		            return;
		        xDoc = ImpExpXsd:PipelineGroupDocument.Factory.newInstance();
		        xDoc.addNewPipelineGroup().ItemList.add(xDoc2.Pipeline);
		    }
		    Common.Dlg::ClientUtils.viewImportResult(command.send(xDoc));
		    reread();
		} catch (Exceptions::Exception e) {
		    showException(e);
		}
	}
	public final class Import extends org.radixware.ads.ServiceBus.explorer.PipelineGroup.Import{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Import( this );
		}

	}

















}

/* Radix::ServiceBus::Pipeline:General:Model - Desktop Meta*/

/*Radix::ServiceBus::Pipeline:General:Model-Group Model Class*/

package org.radixware.ads.ServiceBus.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmEJDXY52ON5CRNK3IHMEI33UF6I"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus::Pipeline:General:Model:Properties-Properties*/
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

/* Radix::ServiceBus::Pipeline - Localizing Bundle */
package org.radixware.ads.ServiceBus.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Pipeline - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import Pipeline");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импорт конвейера");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2I2RDX7MTBHTFAXYLR67JYUD4M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспортировать");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3ATIATSQ6VANJG2AZFUEWBSXUU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Save current version before replacing?");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сохранить текущую версию перед заменой?");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3JQBRJEW6BDUVITH5SP3VIHMRM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last updated");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время последней модификации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4IB5JWISVRES7N37MGA5VLV7WQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pipelines");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конвейеры");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4PGYZGVUSVABNHTJNRSXOIX5WI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last updated by");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Автор последней модификации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4TBA3NKOFZCSNJ6QZNO4S5O7DY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Cannot delete object %s because it is used by object %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Удаление объекта %s запрещено, так как этот объект используется объектом %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5WTQW3BE6BGHBN4XNRGIPO5QUA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import Pipeline");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импорт конвейера");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls722YWL3QV5HGZCHOOWWKSGJYYA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Version created");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Версия создана");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7KN25P4WXNGDLEVFKAIGUQKOGY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Delete existing nodes?");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Удалить существующие узлы?");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7OMMEMHUUZDMNH5PC5BPUSWKOQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pipeline class");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс конвейера");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7YUYHORLWBHV7PM2JRVSEA6W7Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pipeline");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конвейер");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA6SVLTREC5DRRCR7NJBIUA2LUQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Comment");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Комментарий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsABY25EBQ2VHMXAR37HWBLNV4WQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Overwrite pipeline parameters?");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Переписать параметры конвейера?");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB56HH262FBAEBBTY5H2YJEIZPQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import Pipeline");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импорт конвейера");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBUB2FBHZ7BDKXCF7BXUNAN3TBY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Online Pipeline ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конвейер Online");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBZDPWSON6VENNC7DWBNV3YM6JA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Entity PID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ключ сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC34NPFW42RD2XOLITIMKAIUD7A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCKFKG6AATRCCJOCGTL63S4NFC4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проверить");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDVAVVKNFN5HSVPB6NJO5OSHBGI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Deletion of started units is forbidden. Stop units first");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Удаление запущенных модулей запрещено.  Модули необходимо предварительно остановить");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsECU6UVWK4BCHBH7ULJRMIKNHHY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pipeline");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конвейер");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGDULQ27LFJDMDMOYARWW66CTU4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Notes");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Примечания");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHESSGCJ52ZHMRLKC5K4EECUDZE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pipelines");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конвейеры");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHVFP7NJ4MRBF5JX6U7AQIULD3U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Create Version");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Создать версию");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsID2B5UVPSNANVIBMOGIA4IGL6M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Diagram is valid");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибок не обнаружено");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIEJ72ZRSMFDI7L6MXUGSZOFBXY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Select Node");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выбрать узел");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIOCR4J5JRZGO3AYCVXBKPIQTTI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIT7YGWHLLVCQBEGQT6SDYPYBEQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль трассы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJIR6IWCKDNHDFOGURAUGNF2ADI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import Pipeline");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импорт конвейера");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJSXBTR635BH33O3K5YN4HLSYOY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Overwrite local settings?");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Переписать локальные настройки?");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJTMGXSZKCRGWVHCTS3CSTI3MJM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Create Link");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Создать связь");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK36EHFW5QNBAXECW5T7434NQSE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLRPR6VK4ERHNLBN6M4NVMV4W6A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Diagram Check");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проверка схемы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLYD2UDOLZFBXJFB2ZKJ623I23I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Change Direction");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сменить направление");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM2VSNLMSURBBNFHI74BXXXMY24"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMNWV3KJQKJAZDFEHDNHCSRRRRU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импортировать");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNFP6CA2IZFCPBIUQDN5XSSKJS4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Delete");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Удалить");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOMHHMLKTDZAG3KOXAHUDOEMMJQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Entity ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRDINWW7L7ZCGFL5JUR4HXK7RDY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Diagram Check");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проверка схемы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS2HGEJTOYNBVNBAPFXDSWERCTI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Cannot delete object %s because it is used by other objects");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Удаление объекта %s запрещено, так как этот объект используется другими объектами");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSSAOCEZAGJGPVBLWQJWZEJJSRQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Create Node Copy");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Создать копию узла");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTDA3PLYPJZG2PHANDZRWEVFIA4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"API call node not found");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Узел вызова через API не найден");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTHI2CF2CHZDRBB3E45AINURZ3M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Diagram");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Диаграмма");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU3ANYYPHZBHA5LGFM4R7FPTXKU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Delete obsolete pipeline parameters?");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Удалить устаревшие параметры конвейера?");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZUEYOK6OVNAO3KGXOAID5LVW3I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Pipeline - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecX6WI554KVVCZPGNLK7CSZLOMTY"),"Pipeline - Localizing Bundle",$$$items$$$);
}
