
/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer - Server Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer")
public published class Endpoint.MessageQueue.Producer  extends org.radixware.ads.ServiceBus.server.PipelineNode  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private final static Str IN = "In";
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Endpoint.MessageQueue.Producer_mi.rdxMeta;}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:queue-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:queue")
	public published  org.radixware.ads.MessageQueue.server.MessageQueue.Producer getQueue() {
		return queue;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:queue")
	public published   void setQueue(org.radixware.ads.MessageQueue.server.MessageQueue.Producer val) {
		queue = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:inbConnectId-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:inbConnectId")
	public published  Str getInbConnectId() {
		return inbConnectId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:inbConnectId")
	public published   void setInbConnectId(Str val) {
		inbConnectId = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:classTitle-Dynamic Property*/



	protected Str classTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:classTitle")
	public published  Str getClassTitle() {

		return getClassDefinitionTitle();
	}











































	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:afterImport-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:afterImport")
	public published  void afterImport (java.util.Map<org.radixware.kernel.server.types.Pid,org.radixware.kernel.server.types.Pid> pids) {
		super.afterImport(pids);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:export-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:export")
	public published  org.radixware.ads.ServiceBus.common.ImpExpXsd.Node export () {
		ServiceBus::ImpExpXsd:Node  node = ServiceBus::SbImpExpUtil.exportPipelineNode(this, java.util.Arrays.asList(idof[Endpoint.MessageQueue.Producer:queue]), null);
		Common::CommonXsd:EditableProperties props = node.addNewReferenceProps();
		Common::CommonXsd:EditableProperty prop = props.addNewItem();
		prop.Id = idof[Endpoint.MessageQueue.Producer:queue];
		return node;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getHolderEntityPid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getHolderEntityPid")
	public published  org.radixware.kernel.server.types.Pid getHolderEntityPid () {
		return getPid();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:import-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:import")
	public published  void import (org.radixware.ads.ServiceBus.common.ImpExpXsd.Node xNode, boolean overwriteLocalSettings, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		ServiceBus::SbImpExpUtil.excludeLocalUserPropsFromImportNode(xNode, java.util.Arrays.asList(idof[Endpoint.MessageQueue.Producer:queue]));
		super.import(xNode, overwriteLocalSettings, helper);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getOrderPos-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getOrderPos")
	public static published  org.radixware.ads.ServiceBus.common.SbPos getOrderPos () {
		return new SbPos(1.625);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getEditingPresentationId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getEditingPresentationId")
	public published  org.radixware.kernel.common.types.Id getEditingPresentationId () {
		return idof[Endpoint.MessageQueue.Producer:Edit];
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getCreatingPresentationId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getCreatingPresentationId")
	public published  org.radixware.kernel.common.types.Id getCreatingPresentationId () {
		return idof[Endpoint.MessageQueue.Producer:Create];
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getIconId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getIconId")
	public published  org.radixware.kernel.common.types.Id getIconId () {
		return idof[MessageQueue::MessageQueueOut];
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:afterResponseDelivered-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:afterResponseDelivered")
	public published  void afterResponseDelivered (org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRs rs) throws org.radixware.ads.ServiceBus.server.PipelineException {

	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:checkConfig-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:checkConfig")
	public published  void checkConfig () throws org.radixware.ads.ServiceBus.server.PipelineConfigException {
		//.("In="+IN+", conn="+,);
		//.(this, IN, );
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:connect-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:connect")
	public published  void connect (Str outConnectorRole, Str inConnectorId) {
		Arte::Trace.warning("Connect: "+outConnectorRole+" and "+inConnectorId,Arte::EventSource:ServiceBus);
		if (outConnectorRole == IN) {
		    inbConnectId = inConnectorId;
		    Arte::Trace.warning("Connect successful",Arte::EventSource:ServiceBus);
		} else {
		     throw new AppError("Unknown role " + outConnectorRole);
		}
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getConnectorIconId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getConnectorIconId")
	public published  org.radixware.kernel.common.types.Id getConnectorIconId (Str role) {
		if (role == IN) {
		    return idof[ServiceBus::next];     
		}
		else {
		    throw new AppError("Unknown role "+role);
		}


	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getConnectorId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getConnectorId")
	public published  Str getConnectorId (Str role) {
		if (role == IN) {
		    return inbConnectId;
		}
		else {
		    throw new AppError("Unknown role " + role);    
		}

	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getConnectorRole-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getConnectorRole")
	public published  Str getConnectorRole (Str id) {
		if (id == inbConnectId) {
		    return IN;
		} else {
		    throw new AppError("Unknown id " + id);    
		}
		    

	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getConnectorRqDataType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getConnectorRqDataType")
	public published  Str getConnectorRqDataType (Str role) {
		if (role == IN) {
		    return BINARY_TYPE;
		} else {
		    throw new AppError("Unknown role " + role);    
		}

	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getConnectorRsDataType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getConnectorRsDataType")
	public published  Str getConnectorRsDataType (Str role) {
		if (role == IN) {
		    return BINARY_TYPE;
		} else {
		    throw new AppError("Unknown role " + role);    
		}
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getConnectorSide-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getConnectorSide")
	public published  org.radixware.ads.ServiceBus.common.Side getConnectorSide (Str role) {
		if (role == IN) {
		    return ServiceBus::Side:Left;
		} else {
		    throw new AppError("Unknown role " + role);    
		}

	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getConnectorTitle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getConnectorTitle")
	public published  Str getConnectorTitle (Str role) {
		if (role == IN) {
		    return IN;
		}
		else {
		    throw new AppError("Unknown role " + role);    
		}

	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getInConnectorRoles-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getInConnectorRoles")
	public published  Str[] getInConnectorRoles () {
		return new Str[] {IN};
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getOutConnectorRoles-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:getOutConnectorRoles")
	public published  Str[] getOutConnectorRoles () {
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:processRequest-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:processRequest")
	public published  org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRs processRequest (org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRq rq) throws org.radixware.ads.ServiceBus.server.PipelineException {
		Arte::Trace.enterContext(Arte::EventContextType:ServiceBus, id);
		try {
		    final MessageQueue::MessageQueueXsd:MqMessage msg = MessageQueue::MessageQueueXsd:MqMessage.Factory.newInstance();
		    msg.ensureBody().setByteBody(ServiceBus::SbServerUtil.castToBytes(rq.CurStage.Data));
		    
		    if (rq.OutExtHeader != null && rq.OutExtHeader.MqMeta != null) {
		        msg.setMeta(rq.OutExtHeader.MqMeta);
		    }

		    if (this.queue != null) {
		        try {
		            this.queue.post(msg);
		            return ServiceBus::SbServerUtil.prepareNextResponse(null, rq.CurStage, ServiceBus::SbServerUtil.castToBinBase64(new byte[]{}));
		        } catch (MessageQueue::MessageQueueException | Exceptions::IOException exc) {
		            throw new PipelineException(exc.getMessage(), exc);
		        }
		    } else {
		        throw new PipelineException("Pipeline execution error: outbound queue is not defined  for '" + this.calcTitle() + "'");
		    }
		} finally {
		    Arte::Trace.leaveContext(Arte::EventContextType:ServiceBus, id);
		}
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:refreshCache-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:refreshCache")
	public published  void refreshCache () {
		super.refreshCache();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:setExtGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:setExtGuid")
	public published  void setExtGuid (Str extGuid) {
		//.("Set ext guid: "+extGuid,);
		this.extGuid = extGuid;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:setPipeline-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:setPipeline")
	public published  void setPipeline (org.radixware.ads.ServiceBus.server.IPipeline pipeline) {
		pipelineEntityGuid = Str.valueOf(pipeline.getHolderEntityPid().EntityId);
		pipelineEntityPid = Str.valueOf(pipeline.getHolderEntityPid());
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:isSuitableForPipeline-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:isSuitableForPipeline")
	public published  boolean isSuitableForPipeline (org.radixware.ads.ServiceBus.server.IPipeline pipeline) {
		return !pipeline.isTransformation();
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer - Server Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.MessageQueue.Producer_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclACEEAMNMKZFYDCHMKUIU64MP2U"),"Endpoint.MessageQueue.Producer",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHIGSIY4PAZBJNCKOJJJ5F4HOZE"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclACEEAMNMKZFYDCHMKUIU64MP2U"),
							/*Owner Class Name*/
							"Endpoint.MessageQueue.Producer",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHIGSIY4PAZBJNCKOJJJ5F4HOZE"),
							/*Property presentations*/

							/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:queue:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruGPZR37Q5HZAXVBVUPW3P2GBJAA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("sprMW66X772GNBUNIZYRLKPK6XGWU"),new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(33029099,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7JNXRJQ3R5BDBMFTUG72M2JTXE")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:inbConnectId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruGRZOSVT7ZRC2TGDJ4ZVB5FJ2W4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:classTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdMTGAQLCN2JFQJLF2MO5VFZP2LY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
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
									/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprTBXKZTCIOBA5HIAUQVMFOTAYQA"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6CT7IGLHQBFNRBZL5B3OUJGKE4"),
									36016,
									null,

									/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Edit:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprE4TXUBQYK5HT5MFSPLNSVJVCWE"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCPQXRJGZ7JDCBMQ7DM4TBA22GM"),
									36016,
									null,

									/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Create:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6CT7IGLHQBFNRBZL5B3OUJGKE4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCPQXRJGZ7JDCBMQ7DM4TBA22GM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprTBXKZTCIOBA5HIAUQVMFOTAYQA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprE4TXUBQYK5HT5MFSPLNSVJVCWE")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:newClassCatalog-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccCCWP7EI5SVE3FJHE3YAK2NVFMY"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclACEEAMNMKZFYDCHMKUIU64MP2U"),null,10.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclACEEAMNMKZFYDCHMKUIU64MP2U"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblPNJV5QTXIBGJPBHK64RO3LH73A"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecPNJV5QTXIBGJPBHK64RO3LH73A"),

						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:queue-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruGPZR37Q5HZAXVBVUPW3P2GBJAA"),"queue",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXXNP4MIOCVFZDO6BUS2QA3X42E"),org.radixware.kernel.common.enums.EValType.PARENT_REF,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblPNJV5QTXIBGJPBHK64RO3LH73A"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclIJMFWGNFU5AV3PP4PS3SDW3NKY"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:inbConnectId-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruGRZOSVT7ZRC2TGDJ4ZVB5FJ2W4"),"inbConnectId",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("out")),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblPNJV5QTXIBGJPBHK64RO3LH73A"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:classTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdMTGAQLCN2JFQJLF2MO5VFZP2LY"),"classTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZCTJWASZRRBZZHKALV5F5OZAXA"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE)
						},

						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBXD5LT3S2VA2ZGTSBZIOEVFN2I"),"afterImport",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pids",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUQANQEJWDVAXTE6AKALVZRR64M"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSJVD663SIFFXVLMYDV2Y2VU544"),"export",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4Y335FOB6VF3JHZ5WI7UI5TCHM"),"getHolderEntityPid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKMN6A6YHPVBNFON237GIJY4ZPU"),"import",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xNode",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFTM2TNWIPNEWFD6DNMV2WRP6D4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("overwriteLocalSettings",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVTKOMKXWCRHIDIOPTSVNTUAEYM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprON7FWZNGG5BSDBL45NAMYII3PM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAXJJZNLFOJGVBNHZN5AEP5GJGM"),"getOrderPos",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIOMPW3QBZ5ASTPONSTPGMH5ZHQ"),"getEditingPresentationId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLILYOANG5RBVTHLQIJLYCSDJM4"),"getCreatingPresentationId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOK5OVIZTJ5CUTKASBMH6PUUWGQ"),"getIconId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTO3N6ZUC25BNLNVHR6WUL3OSCU"),"afterResponseDelivered",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rs",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFROVWFXXCVAXNFE6E6AAAF43OI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOC7IFQSQQNCXLAY4YULNTEVVL4"),"checkConfig",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRJJVN7SZFRGFPKSWMK5OODDPHM"),"connect",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("outConnectorRole",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7JUB6CW4HZGYZLRUYATNCWJW6Y")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("inConnectorId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQ4AP2FNVWNFFJPW3PMD4OLFLPI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZEZONIMUSNHEJHOFQ7OPMP3CLU"),"getConnectorIconId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIU3BFENPGRC6FENYHXMONLN7WE"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFST4X6BGFNA5PH6LQNOG73MSTQ"),"getConnectorId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprR7QLTPTHEBGXNOG3CGYYNBV6C4"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWE5I24A36RE3BGGGIIG4FYAV7M"),"getConnectorRole",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXL532CZUEVCR5AX545PG2LUW3Y"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEIEXINMQGJB35G65UDWFOOSDZU"),"getConnectorRqDataType",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXBWVMFXEYVB5LEVW6L3MVHLJ5U"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHZIY6G7TCVCX3DR6QWA77MJJHI"),"getConnectorRsDataType",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprM42WDAOX5VCUPBXT2QXDV46HKQ"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthB25WTYWZUJAJXMCSH7UVSSJMP4"),"getConnectorSide",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTSESXL5JDJENNGTWQGNE3W4PTE"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLB23XTDDKVGSRN5R4BPLLXUMFY"),"getConnectorTitle",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKXJXJIG5UJHSJMSLEX2F6YQT64"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIQ7FIDN6DFEYXJSX6G5WONK32M"),"getInConnectorRoles",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZHFRUV5SQ5CYNG4JLLR26M4UWU"),"getOutConnectorRoles",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7MEKN6RYYBASBPAYYLM556VGPM"),"processRequest",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZ5ITDVM5VNHYLE4LTZAM5NYVMQ"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthM7KLX4PIANEH3NYQZZSZ55GMIE"),"refreshCache",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZVCMFP66RZEELGIQECRMCICA3E"),"setExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZJ54J2ER5FGAHLSGD2RFZ3ZSNQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthG2W65RASFFH7RIEA37LOBPKUFI"),"setPipeline",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pipeline",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYIG2ZCQELNADZPMQE4S67PX6HU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNSQ7CEKCGFFWRLVESA72LZ7DEM"),"isSuitableForPipeline",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pipeline",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLSPLXNUP4VADHDLGCYODAK5TKY"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer - Desktop Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer")
public interface Endpoint.MessageQueue.Producer   extends org.radixware.ads.ServiceBus.explorer.PipelineNode  {

























































































































	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:classTitle:classTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:classTitle:classTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:classTitle:classTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassTitle getClassTitle();
	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:queue:queue-Presentation Property*/


	public class Queue extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Queue(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.MessageQueue.explorer.MessageQueue.MessageQueue_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.MessageQueue.explorer.MessageQueue.MessageQueue_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.MessageQueue.explorer.MessageQueue.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.MessageQueue.explorer.MessageQueue.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:queue:queue")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:queue:queue")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Queue getQueue();
	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:inbConnectId:inbConnectId-Presentation Property*/


	public class InbConnectId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public InbConnectId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:inbConnectId:inbConnectId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:inbConnectId:inbConnectId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public InbConnectId getInbConnectId();


}

/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.MessageQueue.Producer_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclACEEAMNMKZFYDCHMKUIU64MP2U"),
			"Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecPNJV5QTXIBGJPBHK64RO3LH73A"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHIGSIY4PAZBJNCKOJJJ5F4HOZE"),null,null,0,

			/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:queue:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruGPZR37Q5HZAXVBVUPW3P2GBJAA"),
						"queue",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXXNP4MIOCVFZDO6BUS2QA3X42E"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclACEEAMNMKZFYDCHMKUIU64MP2U"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclIJMFWGNFU5AV3PP4PS3SDW3NKY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQZ2AJHN3PFDWXC5BT6I66OS5PQ"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7JNXRJQ3R5BDBMFTUG72M2JTXE")},
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("sprMW66X772GNBUNIZYRLKPK6XGWU"),
						33029099,
						133693419,false),

					/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:inbConnectId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruGRZOSVT7ZRC2TGDJ4ZVB5FJ2W4"),
						"inbConnectId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclACEEAMNMKZFYDCHMKUIU64MP2U"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("out"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:inbConnectId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:classTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdMTGAQLCN2JFQJLF2MO5VFZP2LY"),
						"classTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZCTJWASZRRBZZHKALV5F5OZAXA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclACEEAMNMKZFYDCHMKUIU64MP2U"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:classTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprTBXKZTCIOBA5HIAUQVMFOTAYQA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprE4TXUBQYK5HT5MFSPLNSVJVCWE")},
			true,false,false);
}

/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.MessageQueue.Producer_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclACEEAMNMKZFYDCHMKUIU64MP2U"),
			"Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecPNJV5QTXIBGJPBHK64RO3LH73A"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHIGSIY4PAZBJNCKOJJJ5F4HOZE"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Edit - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Edit-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprTBXKZTCIOBA5HIAUQVMFOTAYQA"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6CT7IGLHQBFNRBZL5B3OUJGKE4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclACEEAMNMKZFYDCHMKUIU64MP2U"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblPNJV5QTXIBGJPBHK64RO3LH73A"),
	null,
	null,

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Edit:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Edit:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5D5AVCYTCFFKRFF7Q5UUF7MCEU"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclACEEAMNMKZFYDCHMKUIU64MP2U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO4QA4EAWCZBTDFN6C4FTEZAFEM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("imgUQ3RHVJDQRCKJJ6NCBSP5VJL6M"),new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colINHZQ7NTTVDYDATLRAMEPNM55U"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXNDK3TLXSZC2TOKLUPN7Y2MU6Y"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col33JH36QBOZFNLFI5IIRQI4LQ2U"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYMNM6EH6LVB4BOBROI66ZOVBCE"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ4QH3CFLQFH4ZENFIHVYOWNBVA"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruGPZR37Q5HZAXVBVUPW3P2GBJAA"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdMTGAQLCN2JFQJLF2MO5VFZP2LY"),0,2,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5D5AVCYTCFFKRFF7Q5UUF7MCEU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgIKFTVBTED5DMPMVVT4KRXWWP7Y"))}
	,

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	36016,0,0);
}
/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Edit:Model - Desktop Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Edit:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Edit:Model")
public class Edit:Model  extends org.radixware.ads.ServiceBus.Nodes.explorer.Endpoint.MessageQueue.Producer.Endpoint.MessageQueue.Producer_DefaultModel.epr6CT7IGLHQBFNRBZL5B3OUJGKE4_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Edit:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Edit:Model:Methods-Methods*/


}

/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Edit:Model - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Edit:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemTBXKZTCIOBA5HIAUQVMFOTAYQA"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aem6CT7IGLHQBFNRBZL5B3OUJGKE4"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Edit:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Create - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Create-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprE4TXUBQYK5HT5MFSPLNSVJVCWE"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprCPQXRJGZ7JDCBMQ7DM4TBA22GM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclACEEAMNMKZFYDCHMKUIU64MP2U"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblPNJV5QTXIBGJPBHK64RO3LH73A"),
	null,
	null,

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Create:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Create:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgRFAZSJQTOJCBDNXPQOEPQE3QVI"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclACEEAMNMKZFYDCHMKUIU64MP2U"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("imgUQ3RHVJDQRCKJJ6NCBSP5VJL6M"),new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ4QH3CFLQFH4ZENFIHVYOWNBVA"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colINHZQ7NTTVDYDATLRAMEPNM55U"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYMNM6EH6LVB4BOBROI66ZOVBCE"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruGPZR37Q5HZAXVBVUPW3P2GBJAA"),0,3,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgRFAZSJQTOJCBDNXPQOEPQE3QVI"))}
	,

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	36016,0,0);
}
/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Create:Model - Desktop Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Create:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Create:Model")
public class Create:Model  extends org.radixware.ads.ServiceBus.Nodes.explorer.Endpoint.MessageQueue.Producer.Endpoint.MessageQueue.Producer_DefaultModel.eprCPQXRJGZ7JDCBMQ7DM4TBA22GM_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Create:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Create:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Create:Model:Methods-Methods*/


}

/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Create:Model - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Create:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemE4TXUBQYK5HT5MFSPLNSVJVCWE"),
						"Create:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemCPQXRJGZ7JDCBMQ7DM4TBA22GM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer:Create:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Producer - Localizing Bundle */
package org.radixware.ads.ServiceBus.Nodes.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.MessageQueue.Producer - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отправитель очереди сообщений");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message Queue Producer");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHIGSIY4PAZBJNCKOJJJ5F4HOZE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\ServiceBus.Nodes"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO4QA4EAWCZBTDFN6C4FTEZAFEM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\ServiceBus.Nodes"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Очередь");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Queue");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXXNP4MIOCVFZDO6BUS2QA3X42E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\ServiceBus.Nodes"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZCTJWASZRRBZZHKALV5F5OZAXA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\ServiceBus.Nodes"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Endpoint.MessageQueue.Producer - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclACEEAMNMKZFYDCHMKUIU64MP2U"),"Endpoint.MessageQueue.Producer - Localizing Bundle",$$$items$$$);
}
