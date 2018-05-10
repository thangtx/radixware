
/* Radix::ServiceBus::SbImpExpUtil - Server Executable*/

/*Radix::ServiceBus::SbImpExpUtil-Server Dynamic Class*/

package org.radixware.ads.ServiceBus.server;

import org.radixware.kernel.server.utils.SrvValAsStr;
import java.util.*;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbImpExpUtil")
public published class SbImpExpUtil  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return SbImpExpUtil_mi.rdxMeta;}

	/*Radix::ServiceBus::SbImpExpUtil:Nested classes-Nested Classes*/

	/*Radix::ServiceBus::SbImpExpUtil:Properties-Properties*/





























	/*Radix::ServiceBus::SbImpExpUtil:Methods-Methods*/

	/*Radix::ServiceBus::SbImpExpUtil:exportPipeline-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbImpExpUtil:exportPipeline")
	 static  org.radixware.ads.ServiceBus.common.ImpExpXsd.PipelineDocument exportPipeline (org.radixware.ads.ServiceBus.server.Pipeline pipeline) {
		ImpExpXsd:PipelineDocument xDoc = ImpExpXsd:PipelineDocument.Factory.newInstance();
		ImpExpXsd:Pipeline xPipeline = xDoc.addNewPipeline();

		exportEntity(xPipeline, pipeline, null);

		xPipeline.ExtGuid = pipeline.extGuid;
		xPipeline.Title = pipeline.title;
		xPipeline.Description = pipeline.description;
		DiagramXsd:DiagramDocument xDiagram = pipeline.getDiagram();
		if (xDiagram != null)
		    xPipeline.Diagram = xDiagram.Diagram;
		xPipeline.TraceProfile = pipeline.traceProfile;
		xPipeline.Checked = pipeline.checked;

		if (pipeline instanceof Pipeline.Transformer) {
		    Pipeline.Transformer t = (Pipeline.Transformer) pipeline;
		    xPipeline.TransformerStartNode = t.startPid;
		    xPipeline.TransformerEndNode = t.drainPid;
		}

		final Set<IPipelineNode> nodes = pipeline.getNodes();
		for (IPipelineNode node : nodes) {
		    if (node instanceof PipelineNode) {
		        if (!xPipeline.isSetPipelineNodes())
		            xPipeline.addNewPipelineNodes();
		        xPipeline.PipelineNodes.addNewNode().set(node.export());
		    } else {
		        if (!xPipeline.isSetOtherNodes())
		            xPipeline.addNewOtherNodes();
		        xPipeline.OtherNodes.addNewNode().set(node.export());
		    }
		}

		final Map<Str, Str> params = SbServerUtil.getPipelineParams(pipeline);
		if (params.size() > 0) {
		    xPipeline.addNewParams();
		    for (Map.Entry<Str, Str> entry : params.entrySet()) {
		        ImpExpXsd:Pipeline.Params.Param xParam = xPipeline.Params.addNewParam();
		        xParam.Name = entry.getKey();
		        xParam.Value = entry.getValue();
		    }
		}

		exportPipelineUserFuncLibs(xPipeline, pipeline);

		return xDoc;
	}

	/*Radix::ServiceBus::SbImpExpUtil:exportEntity-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbImpExpUtil:exportEntity")
	public static published  void exportEntity (org.radixware.ads.ServiceBus.common.ImpExpXsd.Entity xEntity, org.radixware.ads.Types.server.Entity entity, java.util.List<org.radixware.kernel.common.types.Id> ignoreProps) {
		CfgManagement::ImpExpUtils.exportEntity(xEntity, entity, true, ignoreProps);
	}

	/*Radix::ServiceBus::SbImpExpUtil:importNode-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbImpExpUtil:importNode")
	public static published  void importNode (org.radixware.ads.ServiceBus.common.ImpExpXsd.Node xNode, org.radixware.ads.Types.server.Entity node, boolean overwriteLocalSettings, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		SbImpExpUtil.importEntity(xNode, node, helper);

		if (overwriteLocalSettings && xNode.isSetReferenceProps()) {
		    for (Common::CommonXsd:EditableProperty xProp : xNode.ReferenceProps.ItemList) {
		        final Meta::PropDef prop = node.getRadMeta().getPropById(xProp.Id);
		        if (prop != null) {
		            Object val = SrvValAsStr.fromStr(Arte::Arte.getInstance(), xProp.Value, prop.getValType());
		            if (prop.getValType() == Meta::ValType:ParentRef || prop.getValType() == Meta::ValType:Object && val != null)
		                try {
		                    val = Types::Entity.load((Types::Pid) val);
		                } catch (Exceptions::Exception e) {
		                    throw new AppError("Node '" + xNode.Title + "' has invalid reference '" + prop.Title + "'");
		                }
		            node.setProp(prop.Id, val);
		        }
		    }
		}
	}

	/*Radix::ServiceBus::SbImpExpUtil:importEntity-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbImpExpUtil:importEntity")
	public static published  void importEntity (org.radixware.ads.ServiceBus.common.ImpExpXsd.Entity xEntity, org.radixware.ads.Types.server.Entity entity, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		CfgManagement::ImpExpUtils.importProps(xEntity, entity, helper);

		/* 
		повтор importNode ?
		if (xEntity instanceof ) {
		     xNode = () xEntity;
		    if (xNode.isSetReferenceProps()) {
		        for ( xProp : xNode.ReferenceProps.PropList) {
		            final  prop = entity.getRadMeta().getPropById(xProp.Id);
		            if (prop == null)
		                continue;
		            Object val = SrvValAsStr.fromStr(.(), xProp.Value, prop.getValType());
		            if (prop.getValType() ==  || prop.getValType() ==  && val != null) {
		                try {
		                    val = .(() val);
		                } catch ( e) {
		                    throw new ("Node '" + xNode.Title + "' has nvalid reference '" + prop.Title + "'");
		                    val = null;
		                }
		            }
		            entity.(prop.Id, val);
		        }
		    }
		}
		*/

	}

	/*Radix::ServiceBus::SbImpExpUtil:importPipeline-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbImpExpUtil:importPipeline")
	 static  void importPipeline (org.radixware.ads.ServiceBus.common.ImpExpXsd.Pipeline xPipeline, org.radixware.ads.ServiceBus.server.Pipeline pipeline, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		SbImpExpUtil.importEntity(xPipeline, pipeline, helper);

		String val = helper.getImportSettings().getStringParam(CfgPipelineOptions:DELETE_EXISTENT_NODES.Name);
		final boolean deleteExistentNodes = val != null ? Bool.parseBoolean(val) : false;

		val = helper.getImportSettings().getStringParam(CfgPipelineOptions:OVERWRITE_LOCAL_SETTINGS.Name);
		final boolean overwriteLocalSettings = val != null ? Bool.parseBoolean(val) : false;

		val = helper.getImportSettings().getStringParam(CfgPipelineOptions:MATCH_NODE_BY_PID.Name);
		final boolean matchNodeByPid = val != null ? Bool.parseBoolean(val) : false;

		val = helper.getImportSettings().getStringParam(CfgPipelineOptions:DELETE_PARAMETERS.Name);
		final boolean deleteObsoleteParams = val != null ? Bool.parseBoolean(val) : false;

		val = helper.getImportSettings().getStringParam(CfgPipelineOptions:OVERWRITE_PARAMETERS.Name);
		final boolean overwriteParams = val != null ? Bool.parseBoolean(val) : false;

		pipeline.title = xPipeline.Title;
		pipeline.description = xPipeline.Description;
		pipeline.traceProfile = xPipeline.TraceProfile;
		pipeline.lastUpdateTime = Utils::Timing.getCurrentTime();
		pipeline.lastUpdateUserName = Arte::Arte.getInstance().UserName;
		pipeline.myEntityGuid = Str.valueOf(pipeline.EntityId);
		pipeline.myEntityPid = Str.valueOf(pipeline.id);
		pipeline.checked = xPipeline.Checked;

		if (pipeline instanceof Pipeline.Transformer) {
		    Pipeline.Transformer t = (Pipeline.Transformer) pipeline;
		    t.startPid = xPipeline.TransformerStartNode;
		    t.drainPid = xPipeline.TransformerEndNode;
		}

		pipeline.refreshCache(true);
		final List<ImpExpXsd:Node> xNodes = new ArrayList<ImpExpXsd:Node>();
		if (xPipeline.isSetPipelineNodes())
		    xNodes.addAll(xPipeline.PipelineNodes.NodeList);
		if (xPipeline.isSetOtherNodes())
		    xNodes.addAll(xPipeline.OtherNodes.NodeList);

		final Map<Types::Pid, ImpExpXsd:Node> pid2xNode = new HashMap<Types::Pid, ImpExpXsd:Node>();
		final Map<Str, ImpExpXsd:Node> guid2xNode = new HashMap<Str, ImpExpXsd:Node>();
		for (ImpExpXsd:Node xNode : xNodes) {
		    final Types::Pid xPid = new Pid(Arte::Arte.getInstance(), Types::Id.Factory.loadFrom(xNode.EntityId), xNode.EntityPid);
		    final Str xGuid = xNode.ExtGuid;
		    pid2xNode.put(xPid, xNode);
		    guid2xNode.put(xGuid, xNode);
		}

		final Str nodeTitlePrefix = pipeline.calcTitle() + " / ";
		final Map<Types::Pid, IPipelineNode> pid2Node = new HashMap<Types::Pid, IPipelineNode>();
		final Map<Str, IPipelineNode> guid2Node = new HashMap<Str, IPipelineNode>();
		for (IPipelineNode node : pipeline.getNodes()) {
		    boolean del = deleteExistentNodes
		            || (matchNodeByPid && !pid2xNode.containsKey(node.getHolderEntityPid()))
		            || (!matchNodeByPid && !guid2xNode.containsKey(node.getExtGuid()));
		    if (del) {
		        if (!deleteExistentNodes)
		            helper.reportObjectDeleted(nodeTitlePrefix + node.getTitle());
		        final Types::Entity entity = Types::Entity.load(node.getHolderEntityPid());
		        entity.delete();
		    } else {
		        pid2Node.put(node.getHolderEntityPid(), node);
		        guid2Node.put(node.getExtGuid(), node);
		    }
		}
		//import liobrary before importing nodes in case node's user functions may refer to library functions
		importPipelineUserFuncLibs(xPipeline, pipeline, helper);

		final Set<IPipelineNode> nodes = new HashSet<IPipelineNode>();
		final Map<Types::Pid, Types::Pid> pids = new HashMap<Types::Pid, Types::Pid>();
		for (ImpExpXsd:Node xNode : xNodes) {
		    final Types::Pid xPid = new Pid(Arte::Arte.getInstance(), Types::Id.Factory.loadFrom(xNode.EntityId), xNode.EntityPid);
		    final Str xGuid = xNode.ExtGuid;

		    IPipelineNode node = null;
		    if (matchNodeByPid) {
		        node = pid2Node.get(xPid);
		    } else {
		        node = guid2Node.get(xGuid);
		    }
		    /*    
		     if (!deleteExistentNodes) {
		     if (matchNodeByPid) {
		     if (pid2Node.containsKey(xPid))
		     node = pid2Node.remove(xPid);
		     } else {
		     if (guid2Node.containsKey(xGuid))
		     node = guid2Node.remove(xGuid);
		     }
		     }
		     */
		    if (node != null) {
		        node.import(xNode, overwriteLocalSettings, helper);
		        node.refreshCache();
		        helper.reportObjectUpdated(nodeTitlePrefix + node.getTitle());
		    } else {
		        final Types::Entity entity = (Types::Entity) Arte::Arte.newObject(xNode.ClassId);
		        node = (IPipelineNode) entity;
		        entity.init(null, null, Types::EntityInitializationPhase:PROGRAMMED_CREATION);
		        node.setExtGuid(getUniqueGuidForNode(xNode.ExtGuid, entity));
		        node.setPipeline(pipeline);
		        node.import(xNode, true, helper);
		        if (!deleteExistentNodes)
		            helper.reportObjectCreated(nodeTitlePrefix + node.getTitle());
		    }

		    final Types::Entity entity = (Types::Entity) node;
		    if (entity.isInDatabase(false)) {
		        entity.update();
		    } else {
		        entity.create();
		    }

		    pids.put(xPid, node.getHolderEntityPid());
		    nodes.add(node);
		}

		// delete old nodes
		//for ( node : pid2Node.values()) {
		//    if (!deleteExistentNodes)
		//        helper.(nodeTitlePrefix + node.());
		//    final  entity = .(node.());
		//    entity.();
		//}
		if (xPipeline.Diagram != null) {
		    DiagramXsd:DiagramDocument xDiagram = DiagramXsd:DiagramDocument.Factory.newInstance();

		    xDiagram.Diagram = xPipeline.Diagram;
		    for (DiagramXsd:Node xNode : xDiagram.Diagram.Nodes.NodeList) {
		        final Types::Pid oldPid = new Pid(Arte::Arte.getInstance(), Types::Id.Factory.loadFrom(xNode.EntityId), xNode.EntityPid);
		        final Types::Pid newPid = pids.get(oldPid);
		        if (newPid != null) {
		            xNode.EntityId = Str.valueOf(newPid.EntityId);
		            xNode.EntityPid = Str.valueOf(newPid);
		        }
		    }
		    for (DiagramXsd:Edge xEdge : xDiagram.Diagram.Edges.EdgeList) {
		        final Str inRole = SbServerUtil.getConnectionRole(xEdge.InConnectorId);
		        final Types::Pid oldInPid = SbServerUtil.getConnectionPid(xEdge.InConnectorId);
		        final Types::Pid newInPid = pids.get(oldInPid);
		        if (newInPid != null) {
		            xEdge.InConnectorId = SbServerUtil.getConnectionId(newInPid, inRole);
		        }
		        final Str outRole = SbServerUtil.getConnectionRole(xEdge.OutConnectorId);
		        final Types::Pid oldOutPid = SbServerUtil.getConnectionPid(xEdge.OutConnectorId);
		        final Types::Pid newOutPid = pids.get(oldOutPid);
		        if (newOutPid != null) {
		            xEdge.OutConnectorId = SbServerUtil.getConnectionId(newOutPid, outRole);
		        }
		    }
		    pipeline.setDiagram(xDiagram);
		}

		for (IPipelineNode node : nodes) {
		    final Str roles[] = node.getOutConnectorRoles();
		    if (roles != null) {
		        for (Str role : roles) {
		            final Str connectorId = node.getConnectorId(role);
		            if (connectorId == null)
		                continue;
		            final Types::Pid oldPid = SbServerUtil.getConnectionPid(connectorId);
		            final Types::Pid newPid = pids.get(oldPid);
		            if (newPid != null) {
		                node.connect(role, SbServerUtil.getConnectionId(newPid, SbServerUtil.getConnectionRole(connectorId)));
		            }
		        }
		    }
		    node.afterImport(pids);
		    if (node instanceof Types::Entity)
		        ((Types::Entity) node).update();
		}

		if (xPipeline.isSetParams()) {
		    ServiceBus.Db::GetPipelineParamsCursor paramCursor = ServiceBus.Db::GetPipelineParamsCursor.open(pipeline.id);
		    List<ImpExpXsd:Pipeline.Params.Param> xParams = xPipeline.Params.ParamList;
		    try {
		        while (paramCursor.next()) {
		            boolean wasFound = false;
		            for (int index = 0; index < xParams.size(); index++) {
		                ImpExpXsd:Pipeline.Params.Param xParam = xParams.get(index);
		                if (paramCursor.name.equals(xParam.Name)) {
		                    wasFound = true;
		                    if (overwriteParams) {
		                        paramCursor.param.val = xParam.Value;
		                    }
		                    xParams.remove(index);
		                    break;
		                }
		            }
		            if (!wasFound && deleteObsoleteParams) {
		                paramCursor.param.delete();
		            }
		        }
		    } finally {
		        paramCursor.close();
		    }
		    for (ImpExpXsd:Pipeline.Params.Param xParam : xParams) {
		        final PipelineParam param = new PipelineParam();
		        param.init();
		        param.pipelineId = pipeline.id;
		        param.name = xParam.Name;
		        param.val = xParam.Value;
		        param.create();
		    }
		}

		pipeline.update();//pipeline.();
		pipeline.refreshCache(true);
	}

	/*Radix::ServiceBus::SbImpExpUtil:importUnit-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbImpExpUtil:importUnit")
	public static published  void importUnit (org.radixware.ads.ServiceBus.common.ImpExpXsd.Unit xUnit, org.radixware.ads.System.server.Unit unit, boolean overwriteLocalSettings, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		//unit. = xUnit.Type; Unit type defined when unit is created, and can't be changed
		unit.rid = helper.importRid(unit, System::Unit.loadByRid(xUnit.Rid), xUnit.Rid);
		if (overwriteLocalSettings) {
		    unit.use = xUnit.Use;
		    unit.guiTraceProfile = xUnit.GuiTraceProfile;
		    unit.dbTraceProfile = xUnit.DbTraceProfile;
		    unit.fileTraceProfile = xUnit.FileTraceProfile;
		}
	}

	/*Radix::ServiceBus::SbImpExpUtil:exportUnit-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbImpExpUtil:exportUnit")
	public static published  void exportUnit (org.radixware.ads.ServiceBus.common.ImpExpXsd.Unit xUnit, org.radixware.ads.System.server.Unit unit) {
		xUnit.Rid = unit.rid;
		xUnit.Type = unit.type;
		xUnit.Use = unit.use;
		xUnit.GuiTraceProfile = unit.guiTraceProfile;
		xUnit.DbTraceProfile = unit.dbTraceProfile;
		xUnit.FileTraceProfile = unit.fileTraceProfile;
	}

	/*Radix::ServiceBus::SbImpExpUtil:importServiceUnit-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbImpExpUtil:importServiceUnit")
	public static published  void importServiceUnit (org.radixware.ads.ServiceBus.common.ImpExpXsd.ServiceUnit xUnit, org.radixware.ads.System.server.Unit.AbstractServiceDriver unit, boolean overwriteLocalSettings, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		SbImpExpUtil.importUnit(xUnit, unit, overwriteLocalSettings, helper);
		if (overwriteLocalSettings) {
		    ImpExpXsd:Sap xSap = xUnit.Sap;
		    if (xUnit.isSetSapPropsXml()) {
		        unit.sapPropsXml = xUnit.SapPropsXml;
		    } else {
		        unit.sapPropsXml = importSapProps(xSap, overwriteLocalSettings);
		    }
		}
	}

	/*Radix::ServiceBus::SbImpExpUtil:exportServiceUnit-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbImpExpUtil:exportServiceUnit")
	public static published  void exportServiceUnit (org.radixware.ads.ServiceBus.common.ImpExpXsd.ServiceUnit xUnit, org.radixware.ads.System.server.Unit.AbstractServiceDriver unit) {
		SbImpExpUtil.exportUnit(xUnit, unit);
		xUnit.SapPropsXml = System::ServerSapUtils.writeProps(System::Sap.loadByPK(unit.currSapId, true), null);
	}

	/*Radix::ServiceBus::SbImpExpUtil:exportNode-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbImpExpUtil:exportNode")
	public static published  void exportNode (org.radixware.ads.ServiceBus.common.ImpExpXsd.Node xNode, org.radixware.ads.Types.server.Entity node, Str guid, Str title, java.util.List<org.radixware.kernel.common.types.Id> ignoreProps, org.radixware.ads.Common.common.CommonXsd.EditableProperties refProps) {
		SbImpExpUtil.exportEntity(xNode, node, ignoreProps);
		xNode.ExtGuid = guid;
		xNode.Title = title;
		xNode.EntityId = Str.valueOf(node.getPid().EntityId);
		xNode.EntityPid = Str.valueOf(node.getPid());
		xNode.ReferenceProps = refProps;
		/*
		if (refProps != null) {
		    xNode.addNewReferenceProps();
		    for (Map.Entry<, > e: refProps.entrySet()) {
		         xProp = xNode.ReferenceProps.addNewProp();
		        xProp.Id = e.getKey();
		        if (e.getValue() != null)
		            xProp.Condition = e.getValue().();        
		    }
		}
		*/
	}

	/*Radix::ServiceBus::SbImpExpUtil:importPipelineNode-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbImpExpUtil:importPipelineNode")
	public static published  void importPipelineNode (org.radixware.ads.ServiceBus.common.ImpExpXsd.PipelineNode xNode, org.radixware.ads.ServiceBus.server.PipelineNode node, boolean overwriteLocalSettings, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		if (!overwriteLocalSettings) {
		    SbImpExpUtil.excludeLocalUserPropsFromImportNode(xNode, node.getExcludedUserPropsIds());
		}
		SbImpExpUtil.importNode(xNode, node, overwriteLocalSettings, helper);
		if (overwriteLocalSettings) {
		    node.title = xNode.Title;
		    node.description = xNode.Description;
		}
		node.lastUpdateTime = Utils::Timing.getCurrentTime();
		node.lastUpdateUserName = Arte::Arte.getInstance().UserName;

		if (node.isInDatabase(false)) {
		    node.update();
		    ServiceBus.Db::DeleteNodeConnectors.execute(node.id);
		    ServiceBus.Db::DeleteNodeStages.execute(node.id);
		} else {
		    node.create();
		}

		if (xNode.isSetParams()) {
		    final boolean deleteObsoleteParams;
		    final boolean overwriteParams;
		    if (helper.getImportSettings() != null) {
		        String val = helper.getImportSettings().getStringParam(CfgPipelineOptions:DELETE_PARAMETERS.Name);
		        deleteObsoleteParams = val != null ? Bool.parseBoolean(val) : false;
		        
		        val = helper.getImportSettings().getStringParam(CfgPipelineOptions:OVERWRITE_PARAMETERS.Name);
		        overwriteParams = val != null ? Bool.parseBoolean(val) : false;
		    } else {
		        deleteObsoleteParams = false;
		        overwriteParams = false;
		    }

		    ServiceBus.Db::GetNodeParamsCursor paramCursor = ServiceBus.Db::GetNodeParamsCursor.open(node.id);
		    List<ImpExpXsd:PipelineNode.Params.Param> xParams = xNode.Params.ParamList;
		    try {
		        while (paramCursor.next()) {
		            boolean wasFound = false;
		            for (int index = 0; index < xParams.size(); index++) {
		                ImpExpXsd:PipelineNode.Params.Param xParam = xParams.get(index);
		                if (paramCursor.name.equals(xParam.Name)) {
		                    wasFound = true;
		                    if (overwriteParams) {
		                        paramCursor.param.val = xParam.Value;
		                    }
		                    xParams.remove(index);
		                    break;
		                }
		            }
		            if (!wasFound && deleteObsoleteParams) {
		                paramCursor.param.delete();
		            }
		        }
		    } finally {
		        paramCursor.close();
		    }

		    for (ImpExpXsd:PipelineNode.Params.Param xParam : xParams) {
		        final PipelineNodeParam param = new PipelineNodeParam();
		        param.init();
		        param.nodeId = node.id;
		        param.name = xParam.Name;
		        param.val = xParam.Value;
		        param.create();
		    }
		}

		if (xNode.isSetConnectors()) {
		    for (ImpExpXsd:PipelineNode.Connectors.Connector xConnector : xNode.Connectors.ConnectorList) {
		        final PipelineConnector connector = new PipelineConnector();
		        connector.init();
		        connector.nodeId = node.id;
		        connector.role = xConnector.Role;
		        connector.rqType = xConnector.RqType;
		        connector.rsType = xConnector.RsType;
		        connector.title = xConnector.Title;
		        connector.create();
		    }
		}

		if (xNode.isSetTransformStages()) {
		    for (ImpExpXsd:PipelineNode.TransformStages.TransformStage xStage : xNode.TransformStages.TransformStageList) {
		        final TransformStage stage = (TransformStage) Arte::Arte.newObject(xStage.ClassId);
		        stage.init(null, null, Types::EntityInitializationPhase:PROGRAMMED_CREATION);
		        importEntity(xStage, stage, helper);
		        stage.nodeId = node.id;
		        stage.title = xStage.Title;
		        stage.seq = xStage.Seq;
		        stage.create();
		        if (!xStage.isSetXPathTable())
		            continue;
		        for (ImpExpXsd:PipelineNode.TransformStages.TransformStage.XPathTable.Item xItem : xStage.XPathTable.ItemList) {
		            final XPathTable table = new XPathTable();
		            table.init();
		            table.stageId = stage.id;
		            table.title = xItem.Title;
		            table.seq = xItem.Seq;
		            table.isRequest = xItem.IsRequest;
		            table.srcXPath = xItem.SrcXPath;
		            table.dstXPath = xItem.DstXPath;
		            table.create();
		        }
		    }
		}
	}

	/*Radix::ServiceBus::SbImpExpUtil:exportPipelineNode-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbImpExpUtil:exportPipelineNode")
	public static published  org.radixware.ads.ServiceBus.common.ImpExpXsd.PipelineNode exportPipelineNode (org.radixware.ads.ServiceBus.server.PipelineNode node, java.util.List<org.radixware.kernel.common.types.Id> ignoreProps, org.radixware.ads.Common.common.CommonXsd.EditableProperties refProps) {
		ImpExpXsd:PipelineNode xNode = ImpExpXsd:PipelineNode.Factory.newInstance();
		SbImpExpUtil.exportNode(xNode, node, node.extGuid, node.title, ignoreProps, refProps);
		xNode.Description = node.description;

		final Map<Str, Str> params = SbServerUtil.getNodeParams(node);
		if (params.size() > 0) {
		    xNode.addNewParams();
		    for (Map.Entry<Str, Str> entry: params.entrySet()) {
		        ImpExpXsd:PipelineNode.Params.Param xParam = xNode.Params.addNewParam();
		        xParam.Name = entry.getKey();
		        xParam.Value = entry.getValue();
		    }
		}

		final List<Types::Pid> cs = SbServerUtil.getNodeConnectors(node);
		if (cs.size() > 0) {
		    xNode.addNewConnectors();
		    for (Types::Pid pid: cs) {
		        final PipelineConnector connector = (PipelineConnector)PipelineConnector.load(pid);
		        final ImpExpXsd:PipelineNode.Connectors.Connector xConnector = xNode.Connectors.addNewConnector();
		        xConnector.Role = connector.role;        
		        xConnector.RqType = connector.rqType;        
		        xConnector.RsType = connector.rsType;
		        xConnector.Title = connector.title;
		    }
		}

		final List<Types::Pid> ts = SbServerUtil.getNodeStages(node);
		if (ts.size() > 0) {
		    xNode.addNewTransformStages();
		    for (Types::Pid pid: ts) {
		        final TransformStage stage = (TransformStage)TransformStage.load(pid);
		        final ImpExpXsd:PipelineNode.TransformStages.TransformStage xStage = xNode.TransformStages.addNewTransformStage();
		        exportEntity(xStage, stage, null);
		        xStage.Title = stage.title;
		        xStage.Seq = stage.seq;
		        for (Types::Pid xpid: SbServerUtil.getXPaths(stage.id)) {
		            final XPathTable table = (XPathTable)XPathTable.load(xpid);
		            if (!xStage.isSetXPathTable())
		                xStage.addNewXPathTable();
		            final ImpExpXsd:PipelineNode.TransformStages.TransformStage.XPathTable.Item xItem = xStage.XPathTable.addNewItem();
		            xItem.Seq = table.seq;
		            xItem.Title = table.title;
		            xItem.IsRequest = table.isRequest;
		            xItem.SrcXPath = table.srcXPath;
		            xItem.DstXPath = table.dstXPath;
		        }
		    }
		}

		return xNode;
	}

	/*Radix::ServiceBus::SbImpExpUtil:importSapProps-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbImpExpUtil:importSapProps")
	public static  Str importSapProps (org.radixware.ads.ServiceBus.common.ImpExpXsd.Sap xSap, boolean overwriteLocalSettings) {
		final System::Sap tempSap = new System::Sap();
		tempSap.init();

		tempSap.accessibility = xSap.Accessibility;
		try {
		    if (overwriteLocalSettings) {
		        tempSap.title = xSap.Title;
		        tempSap.address = xSap.Address;
		        tempSap.checkClientCert = xSap.CheckClientCert;
		        tempSap.notes = xSap.Notes;
		        tempSap.securityProtocol = xSap.SecurityProtocol;
		        tempSap.serverKeyAliases = ArrStr.fromValAsStr(xSap.ServerKeyAliases);
		        tempSap.clientCertAliases = ArrStr.fromValAsStr(xSap.ClientCertAliases);
		        tempSap.clientKeyAliases = ArrStr.fromValAsStr(xSap.ClientKeyAliases);
		        tempSap.serverCertAliases = ArrStr.fromValAsStr(xSap.ServerCertAliases);
		    }

		    return System::ServerSapUtils.writeProps(tempSap, null);
		} finally {
		    tempSap.discard();
		}
	}

	/*Radix::ServiceBus::SbImpExpUtil:exportSapSettings-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbImpExpUtil:exportSapSettings")
	@Deprecated
	private static  void exportSapSettings (org.radixware.ads.System.server.Sap sap, org.radixware.ads.ServiceBus.common.ImpExpXsd.Sap xSap) {
		xSap.Title = sap.title;
		xSap.Address = sap.address;
		xSap.CheckClientCert = sap.checkClientCert;
		xSap.Notes = sap.notes;
		xSap.SecurityProtocol = sap.securityProtocol;
		if (sap.serverKeyAliases != null)
		    xSap.ServerKeyAliases = Types::ValAsStr.toStr(sap.serverKeyAliases, Meta::ValType:ArrStr);
		if (sap.clientCertAliases != null)
		    xSap.ClientCertAliases = Types::ValAsStr.toStr(sap.clientCertAliases, Meta::ValType:ArrStr);
		if (sap.clientKeyAliases != null)
		    xSap.ClientKeyAliases = Types::ValAsStr.toStr(sap.clientKeyAliases, Meta::ValType:ArrStr);
		if (sap.serverCertAliases != null)
		    xSap.ServerCertAliases = Types::ValAsStr.toStr(sap.serverCertAliases, Meta::ValType:ArrStr);
		xSap.Accessibility = sap.accessibility;
	}

	/*Radix::ServiceBus::SbImpExpUtil:createVersion-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbImpExpUtil:createVersion")
	 static  org.radixware.ads.ServiceBus.server.PipelineVer createVersion (org.radixware.ads.ServiceBus.server.Pipeline pipeline, Str notes) {
		final ImpExpXsd:PipelineDocument xDoc = SbImpExpUtil.exportPipeline(pipeline);
		final Clob settings;
		try {
		    settings = Arte::Arte.getDbConnection().createClob();
		    final java.io.Writer writer = settings.setCharacterStream(1L);
		    xDoc.save(writer);
		    writer.close();
		} catch (Exception e) {
		    throw new DatabaseError("Can't write settings to clob: " + e.getMessage(), e);
		}

		final PipelineVer ver = new PipelineVer();
		ver.pipelineId = pipeline.id;
		ver.createTime = Utils::Timing.getCurrentTime();
		ver.creatorUserName = Arte::Arte.getUserName();
		ver.notes = notes;
		ver.settings = settings;
		ver.init();
		ver.create();

		return ver;
	}

	/*Radix::ServiceBus::SbImpExpUtil:exportPipelineUserFuncLibs-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbImpExpUtil:exportPipelineUserFuncLibs")
	 static  void exportPipelineUserFuncLibs (org.radixware.ads.ServiceBus.common.ImpExpXsd.Pipeline xPipeline, org.radixware.ads.ServiceBus.server.Pipeline pipeline) {
		org.radixware.kernel.server.types.EntityGroup userFuncLibGroup = Arte::Arte.getInstance().getGroupHander(idof[System::UserFuncLib]);
		userFuncLibGroup.set(
		        null,
		        Arte::Arte.getDefManager().getClassDef(idof[UserFunc::UserFuncLib]).getPresentation().getSelectorPresentationById(idof[UserFunc::UserFuncLib:ForPipeline]),
		        null,
		        null,
		        null,
		        null);
		Iterator<UserFunc::UserFuncLib> iter = userFuncLibGroup.iterator();
		while (iter.hasNext()) {
		    UserFunc::UserFuncLib userFuncLib = iter.next();
		    if (userFuncLib != null && userFuncLib.pipelineId == pipeline.id) {
		        if (xPipeline.UserFuncLibGroup != null) {
		            xPipeline.UserFuncLibGroup.addNewLibrary().set(userFuncLib.exportToXml().UserFuncLib);
		        } else {
		            xPipeline.addNewUserFuncLibGroup().addNewLibrary().set(userFuncLib.exportToXml().UserFuncLib);
		        }
		    }
		}
	}

	/*Radix::ServiceBus::SbImpExpUtil:importPipelineUserFuncLibs-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbImpExpUtil:importPipelineUserFuncLibs")
	public static  void importPipelineUserFuncLibs (org.radixware.ads.ServiceBus.common.ImpExpXsd.Pipeline xPipeline, org.radixware.ads.ServiceBus.server.Pipeline pipeline, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		if (xPipeline.isSetUserFuncLibGroup()) {
		    for (UserFunc::ImpExpXsd:UserFuncLib xLib : xPipeline.UserFuncLibGroup.LibraryList) {
		        xLib.PipelineId = pipeline.id;
		        xLib.PipelineExtGuid = pipeline.extGuid;
		    }
		    UserFunc::UserFuncLib.importAll(xPipeline.UserFuncLibGroup, helper);
		}
	}

	/*Radix::ServiceBus::SbImpExpUtil:excludeLocalUserPropsFromImportNode-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbImpExpUtil:excludeLocalUserPropsFromImportNode")
	public static  void excludeLocalUserPropsFromImportNode (org.radixware.ads.ServiceBus.common.ImpExpXsd.Node xNode, java.util.List<org.radixware.kernel.common.types.Id> propsToExclude) {
		if (xNode == null || xNode.getUserProps() == null) {
		    return;
		}

		for (Types::Id propId : propsToExclude) {
		    for (int i = 0; i < xNode.getUserProps().getUserPropList().size(); i++) {
		        if (xNode.getUserProps().getUserPropList().get(i).Id == propId) {
		            xNode.getUserProps().removeUserProp(i);
		            break;
		        }
		    }
		}
	}

	/*Radix::ServiceBus::SbImpExpUtil:getUniqueGuidForNode-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbImpExpUtil:getUniqueGuidForNode")
	private static  Str getUniqueGuidForNode (Str guid, org.radixware.ads.Types.server.Entity node) {
		if (guid != null) {
		    if (node instanceof PipelineNode) {
		        try (ServiceBus.Db::GetPipelineNodeIdByExtGuidCursor c = ServiceBus.Db::GetPipelineNodeIdByExtGuidCursor.open(guid)) {
		            if (c.next()) {
		                return Arte::Arte.generateGuid();
		            }
		            return guid;
		        }
		    } else if (node instanceof CfgManagement::ICfgReferencedObject) {
		        try {
		            CfgManagement::ICfgObjectLookupAdvizor advisor = CfgManagement::ImpExpUtils.createCfgLookupAdvizor(node.getClassDefinitionId());
		            if (advisor != null) {
		                java.util.List<Types::Entity> entites = advisor.getCfgObjectsByExtGuid(guid, false, null);
		                if (entites != null && !entites.isEmpty()) {
		                    return Arte::Arte.generateGuid();
		                } else {
		                    return guid;
		                }
		            }
		        } catch (Exceptions::Exception ex) {
		            Arte::Trace.error(Arte::Trace.exceptionStackToString(ex), Arte::EventSource:AppCfgPackage);
		        }
		    }
		}
		return Arte::Arte.generateGuid();
	}


}

/* Radix::ServiceBus::SbImpExpUtil - Server Meta*/

/*Radix::ServiceBus::SbImpExpUtil-Server Dynamic Class*/

package org.radixware.ads.ServiceBus.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class SbImpExpUtil_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adc5R3PUJEBDFHIPIU4X4JWQYZZLY"),"SbImpExpUtil",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::ServiceBus::SbImpExpUtil:Properties-Properties*/
						null,

						/*Radix::ServiceBus::SbImpExpUtil:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthE4E3XYNGSZCYNEIJTE4OMNRHKM"),"exportPipeline",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pipeline",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSGNWRF6MOZHUJK5TAISOQF3XIE"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7MNGBFAKKNGQTKDYWSWGAYHDTM"),"exportEntity",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xEntity",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprC25YH2KXVZFN3FKNQINR2PGOWI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entity",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFZ7RGJ2VWNGOBKRNCCMQ4QISJQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("ignoreProps",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr37CXFJ7PBRFEBGKB6KTL64TXXM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWIONYDQEUBAFTF67XGAH7IXYYU"),"importNode",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xNode",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr66SXHGMVRVEMBJ62GNKI4TR2FA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("node",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEOGBRRAP2BBGVJ3B5JTSIHDUQ4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("overwriteLocalSettings",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprP6TIY3JWNZABNNLGKQXG72QOZM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFNNNQUDEIVAMJAFRD4XFHAZ3EY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSSX6K6XO5RHA7L3HJ7NBAV6SUU"),"importEntity",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xEntity",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr54XOZZMLFRHZJN5YZUGR5S3ECQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entity",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYQM7EC6XSRCI3HWTNKBXK5EOSM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSWG2EQQ47FHRHOA5L5AHC56F2M"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth63ANREX7BZBU7G7IG62XD5DYOY"),"importPipeline",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xPipeline",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2SCREDHKIBAFVJVJMLZCV3WFKY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pipeline",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprI6AL5SQO7ZG2XMDNP253Y626R4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBDTD7FOSYFCVZOPVPMII7BH6PU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7TE2D73EONE6ZBSMDY22OMHKXM"),"importUnit",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xUnit",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIRT2WCCJL5BRRGH3VKTFJOVWV4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("unit",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprT3GVZZOSAREFVHQYEVJACHE5HM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("overwriteLocalSettings",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprND2JA7FKXNGBPHAUZWGPJKYYMA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCN2AWEJ2FZHRPO6AKJRLMCKAOM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTHD4A5ZQGVCKJA2VSO3WBMZZXU"),"exportUnit",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xUnit",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHSNLBAZDNRD7VEWZTPEMQFRR24")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("unit",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWBSOJQHBKRB7PAHZEAN52AQKYY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6OV2ZMMXTZDC3C5KDPQY4HIPLY"),"importServiceUnit",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xUnit",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFP7ONRRUUVH43KFMVQ6C33SD5Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("unit",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIRE7LC576BH6BFOWLNWWSY5KXI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("overwriteLocalSettings",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAGC5NFWXTFESFKPSHQXXEQLNWI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6NMH7PMD25FYBNNLNHJQTHG5GM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD2DQZAXPRVCOVOXI3M6IWYY6YI"),"exportServiceUnit",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xUnit",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr66JRQ5OZTND23MNJEA47VL3QQI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("unit",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBNYTTXKXFBE6LI2TGM2UEQ7DIY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthE3ZNLBUJCNB7JFD252LTMMY4K4"),"exportNode",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xNode",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprV4U2SJQZMRHC5GP6KWA6DGKUBM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("node",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAUHRVNXJTRAGVNSKJHCUWGPMEY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("guid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprD67MDSI7NFGQTEXTF3SX5XLODU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLIUPPPMRNZDRNDCVWYB42OKHPE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("ignoreProps",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDBDCUU6PXJBBRDUGJDPOTCM4BY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("refProps",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUKIUZAS7KZGO3PVKLWVO32IMAQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDQ4AHBYNAFGILMVYR6XQVM4TVI"),"importPipelineNode",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xNode",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTP7MLCV5OBBJTKYPV6PHX2NEQY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("node",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPEVSY4FRURAORFX42UX5RUN5LA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("overwriteLocalSettings",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7C2TY7YVRZAVFBIZFVLZKO2J4M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZQMX5V73H5B4XPEOTMTIPGRNZY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQXSVH7BQMZC73NOCKTJCQOAAAM"),"exportPipelineNode",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("node",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLGQQ6HH45RCHTJOGDZUNX3WMSE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("ignoreProps",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYXL53VCCQVCATPIFR4MXLWVNBQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("refProps",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPBBSXU2Y4ZHP5D54EGNXIAJMXA"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJB3LGKGLAZGFRCL5QQ5TT265B4"),"importSapProps",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xSap",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFBYZGL4FOBGPBNDU4TWB64ATVU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("overwriteLocalSettings",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRZ45LOY7D5FKJPLAN5XLGTO4LE"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHXJAECAZWZHWNOFOST3E4ZZOXM"),"exportSapSettings",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sap",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYRRPKDOLBRG5DF3LFVH5QF5C44")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xSap",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNHV7C6SNVRDHLB5P44TGSLN2AM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJCRLCYQUNJHBJGVZXT4FMI2HXM"),"createVersion",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pipeline",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVPYWAWMWOVCANBFYTMIZSSJ2KY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("notes",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3WZ742BTR5AABP43JTWWU34DAM"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZ5JHDUSIIBHEHHA4NYFSANGYT4"),"exportPipelineUserFuncLibs",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xPipeline",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSTDRI3UMRNFU3NOW4CYHMZ6XJI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pipeline",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprG3ACFUCBB5ADTASXY5CMMX7CYU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthO44UBVZYXJAM3ETOB77ILWDLXA"),"importPipelineUserFuncLibs",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xPipeline",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEG2AX5QMHFFW3H56VNYSX33APM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pipeline",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2H5UFQONRBG4XGTLT42YNAMH2Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprL7PAK3MVTZHYRBNFSQEFVFW3KU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVZ24AP4QYRBFJAVYFEWAEGHLQY"),"excludeLocalUserPropsFromImportNode",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xNode",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCBQUX2RANFC4BHL43QDNBV5CYA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propsToExclude",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprS435PKUNLNAFRD2F7CVQMH6AZE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthO3RGO3M5N5D35FOSLOVETYQTM4"),"getUniqueGuidForNode",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("guid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7TEC2L3E65FRFBUERPLD7RKMPY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("node",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprY33X75S76ND5ZFOVD5HPQYXLQA"))
								},org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
}

/* Radix::ServiceBus::SbImpExpUtil - Localizing Bundle */
package org.radixware.ads.ServiceBus.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class SbImpExpUtil - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(SbImpExpUtil - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadc5R3PUJEBDFHIPIU4X4JWQYZZLY"),"SbImpExpUtil - Localizing Bundle",$$$items$$$);
}
