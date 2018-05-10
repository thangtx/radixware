
/* Radix::PersoComm::RecvMessage - Server Executable*/

/*Radix::PersoComm::RecvMessage-Entity Class*/

package org.radixware.ads.PersoComm.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage")
public final published class RecvMessage  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return RecvMessage_mi.rdxMeta;}

	/*Radix::PersoComm::RecvMessage:Nested classes-Nested Classes*/

	/*Radix::PersoComm::RecvMessage:Properties-Properties*/

	/*Radix::PersoComm::RecvMessage:channelId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:channelId",line=41)
	public published  Int getChannelId() {
		return channelId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:channelId",line=47)
	public published   void setChannelId(Int val) {
		channelId = val;
	}

	/*Radix::PersoComm::RecvMessage:address-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:address",line=70)
	public published  Str getAddress() {
		return address;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:address",line=76)
	public published   void setAddress(Str val) {
		address = val;
	}

	/*Radix::PersoComm::RecvMessage:sendTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:sendTime",line=99)
	public published  java.sql.Timestamp getSendTime() {
		return sendTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:sendTime",line=105)
	public published   void setSendTime(java.sql.Timestamp val) {
		sendTime = val;
	}

	/*Radix::PersoComm::RecvMessage:recvTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:recvTime",line=128)
	public published  java.sql.Timestamp getRecvTime() {
		return recvTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:recvTime",line=134)
	public published   void setRecvTime(java.sql.Timestamp val) {
		recvTime = val;
	}

	/*Radix::PersoComm::RecvMessage:importance-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:importance",line=165)
	public published  org.radixware.kernel.common.enums.EPersoCommImportance getImportance() {
		return importance;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:importance",line=171)
	public published   void setImportance(org.radixware.kernel.common.enums.EPersoCommImportance val) {
		importance = val;
	}

	/*Radix::PersoComm::RecvMessage:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:id",line=194)
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:id",line=200)
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::PersoComm::RecvMessage:body-Column-Based Property*/




















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:body",line=227)
	public published  Str getBody() {
		return body;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:body",line=233)
	public published   void setBody(Str val) {
		body = val;
	}

	/*Radix::PersoComm::RecvMessage:subject-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:subject",line=256)
	public published  Str getSubject() {
		return subject;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:subject",line=262)
	public published   void setSubject(Str val) {
		subject = val;
	}

	/*Radix::PersoComm::RecvMessage:destEntityGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:destEntityGuid",line=285)
	public published  Str getDestEntityGuid() {
		return destEntityGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:destEntityGuid",line=291)
	public published   void setDestEntityGuid(Str val) {
		destEntityGuid = val;
	}

	/*Radix::PersoComm::RecvMessage:destMsgId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:destMsgId",line=314)
	public published  Str getDestMsgId() {
		return destMsgId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:destMsgId",line=320)
	public published   void setDestMsgId(Str val) {
		destMsgId = val;
	}

	/*Radix::PersoComm::RecvMessage:destPid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:destPid",line=343)
	public published  Str getDestPid() {
		return destPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:destPid",line=349)
	public published   void setDestPid(Str val) {
		destPid = val;
	}

	/*Radix::PersoComm::RecvMessage:destTitle-Dynamic Property*/



	protected Str destTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:destTitle",line=371)
	public published  Str getDestTitle() {

		if(internal[destTitle] == null){
		    try{
		        internal[destTitle] = Utils.getSourceTitle(destEntityGuid, destPid);
		    }catch(Exceptions::Error e){
		        internal[destTitle] = e.getMessage();
		    }
		}

		return internal[destTitle];
	}

	/*Radix::PersoComm::RecvMessage:unit-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:unit",line=403)
	public published  org.radixware.ads.System.server.Unit getUnit() {
		return unit;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:unit",line=409)
	public published   void setUnit(org.radixware.ads.System.server.Unit val) {
		unit = val;
	}

	/*Radix::PersoComm::RecvMessage:channelUnit-Dynamic Property*/



	protected org.radixware.ads.PersoComm.server.Unit.Channel.AbstractBase channelUnit=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:channelUnit",line=431)
	public published  org.radixware.ads.PersoComm.server.Unit.Channel.AbstractBase getChannelUnit() {

		return (Unit.Channel.AbstractBase)unit;
	}

	/*Radix::PersoComm::RecvMessage:isUssd-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:isUssd",line=455)
	public published  Bool getIsUssd() {
		return isUssd;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:isUssd",line=461)
	public published   void setIsUssd(Bool val) {
		isUssd = val;
	}

	/*Radix::PersoComm::RecvMessage:ussdServiceOp-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:ussdServiceOp",line=484)
	public published  Int getUssdServiceOp() {
		return ussdServiceOp;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:ussdServiceOp",line=490)
	public published   void setUssdServiceOp(Int val) {
		ussdServiceOp = val;
	}

	/*Radix::PersoComm::RecvMessage:channelKind-Dynamic Property*/



	protected org.radixware.ads.PersoComm.common.ChannelKind channelKind=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:channelKind",line=512)
	public published  org.radixware.ads.PersoComm.common.ChannelKind getChannelKind() {

		return channelUnit == null ? null : channelUnit.kind;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:channelKind",line=519)
	public published   void setChannelKind(org.radixware.ads.PersoComm.common.ChannelKind val) {
		channelKind = val;
	}



























































































































	/*Radix::PersoComm::RecvMessage:Methods-Methods*/

	/*Radix::PersoComm::RecvMessage:loadByPidStr-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:loadByPidStr",line=651)
	public static published  org.radixware.ads.PersoComm.server.RecvMessage loadByPidStr (Str pidAsStr, boolean checkExistance) {
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLXLOGLESUTOBDANVABIFNQAABA"),pidAsStr);
		try{
		return (
		org.radixware.ads.PersoComm.server.RecvMessage) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::PersoComm::RecvMessage:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:loadByPK",line=666)
	public static published  org.radixware.ads.PersoComm.server.RecvMessage loadByPK (Int id, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(id==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colX3PXDZRCVLOBDCLSAALOMT5GDM"),id);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLXLOGLESUTOBDANVABIFNQAABA"),pkValsMap);
		try{
		return (
		org.radixware.ads.PersoComm.server.RecvMessage) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::PersoComm::RecvMessage:createFromXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:createFromXml",line=683)
	public static published  org.radixware.ads.PersoComm.server.RecvMessage createFromXml (org.radixware.schemas.personalcommunications.MessageType xMess) {
		if (xMess != null) {
		    RecvMessage recvMessage = new RecvMessage();

		    recvMessage.init();

		    if (xMess.isSetImportance()) {
		        recvMessage.importance = Importance.getForValue(xMess.getImportance());
		    } else {
		        recvMessage.importance = Importance:Normal;
		    }

		    recvMessage.address = xMess.getAddressFrom();
		    recvMessage.sendTime = xMess.getSentTime();
		    recvMessage.recvTime = Arte::Arte.getDatabaseSysTimestamp();
		    recvMessage.subject = xMess.getSubject();
		    recvMessage.body = xMess.getBody();
		    recvMessage.channelId = xMess.getChannelId();
		    recvMessage.isUssd = xMess.IsUssd;
		    recvMessage.ussdServiceOp = xMess.UssdServiceOp;

		    recvMessage.create();

		    if (xMess.isSetAttachments()) {
		        for (PersoCommXsd:Attachment xAttach : xMess.getAttachments().AttachmentList) {
		            Attachment attachment = new Attachment();
		            attachment.init();

		            attachment.messId = recvMessage.id;
		            attachment.seq = xAttach.Seq;
		            attachment.mimeType = xAttach.MimeType == null ? Common::MimeType:Bin : Common::MimeType.getForValue(xAttach.MimeType);
		            attachment.title = xAttach.Title;

		            attachment.create();
		        }
		    }
		    return recvMessage;
		} else {
		    throw new java.lang.NullPointerException("Message is not defined");
		}

	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::PersoComm::RecvMessage - Server Meta*/

/*Radix::PersoComm::RecvMessage-Entity Class*/

package org.radixware.ads.PersoComm.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class RecvMessage_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),"RecvMessage",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA53KJRJ5VXOBDCLUAALOMT5GDM"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::PersoComm::RecvMessage:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
							/*Owner Class Name*/
							"RecvMessage",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA53KJRJ5VXOBDCLUAALOMT5GDM"),
							/*Property presentations*/

							/*Radix::PersoComm::RecvMessage:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::PersoComm::RecvMessage:address:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col726AU7BDVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::PersoComm::RecvMessage:sendTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNQ6OMXZDVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::PersoComm::RecvMessage:recvTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNU6OMXZDVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::PersoComm::RecvMessage:importance:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW53AP6RCVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::PersoComm::RecvMessage:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colX3PXDZRCVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::PersoComm::RecvMessage:body:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXB3AP6RCVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::PersoComm::RecvMessage:subject:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXF3AP6RCVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::PersoComm::RecvMessage:destEntityGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUESOPNO7S5DEXMRED7NWYI7IFU"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::RecvMessage:destMsgId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW2PIZESMNZHVXOWVZDGB24HSBQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::RecvMessage:destPid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNJJ6EUBFNNC4RGFO3RDQW4M6UQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::RecvMessage:destTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKJZSQCXP2RE3DKTTC5Y6YIVZWY"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::PersoComm::RecvMessage:unit:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFQAETETA6ZGZTNYAADE5MFDKQY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(262143,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::PersoComm::RecvMessage:isUssd:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMHW5BQRKWNEQHIGODNT4FJOEV4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::RecvMessage:ussdServiceOp:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDNDZIZBEJACZGTFJ6UZYLXJV4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::RecvMessage:channelKind:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5GBK5RKSFND3RI7MWIHKWOEE7A"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::PersoComm::RecvMessage:ViewDest-Property Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdPLVNXYJEGFEJNKRRBA2PNCJPZI"),"ViewDest",org.radixware.kernel.common.enums.ECommandScope.PROPERTY,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTBHNMZQEJFHUJEKE7DIBKEY6BU")},org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.LOCAL,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),false,true)
							},
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::PersoComm::RecvMessage:Time-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtH764BABKAFB7BBJUGRNUN3B6SA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),"Time",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYSB4ZI6EMBAKZCBWU5JOAODWDI"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNU6OMXZDVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.EOrder.DESC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::PersoComm::RecvMessage:Common-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSQWLHWR5VXOBDCLUAALOMT5GDM"),
									"Common",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::PersoComm::RecvMessage:Common:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::PersoComm::RecvMessage:Common:Attachement-Entity Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadTableExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiQVWLTJHMVXOBDCLVAALOMT5GDM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5O3H4DJEVLOBDCLSAALOMT5GDM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6BHCGLBRVXOBDCLUAALOMT5GDM"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tbl5O3H4DJEVLOBDCLSAALOMT5GDM\" PropId=\"col5W3H4DJEVLOBDCLSAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblLXLOGLESUTOBDANVABIFNQAABA\" PropId=\"colX3PXDZRCVLOBDCLSAALOMT5GDM\" Owner=\"PARENT\"/></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::PersoComm::RecvMessage:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7TGF7PWBJ5D3XLVFA2EBYHVO5Q"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::PersoComm::RecvMessage:Create:Children-Explorer Items*/
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
									/*Radix::PersoComm::RecvMessage:Common-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprFWHOGC2LVXOBDCLUAALOMT5GDM"),"Common",null,2192,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colX3PXDZRCVLOBDCLSAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNU6OMXZDVLOBDCLSAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col726AU7BDVLOBDCLSAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXF3AP6RCVLOBDCLSAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW53AP6RCVLOBDCLSAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUESOPNO7S5DEXMRED7NWYI7IFU"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNJJ6EUBFNNC4RGFO3RDQW4M6UQ"),false)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtH764BABKAFB7BBJUGRNUN3B6SA"),true,null,true,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSQWLHWR5VXOBDCLUAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7TGF7PWBJ5D3XLVFA2EBYHVO5Q")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(290855,null,null,null),null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("sprFWHOGC2LVXOBDCLUAALOMT5GDM"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::PersoComm::RecvMessage:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colNU6OMXZDVLOBDCLSAALOMT5GDM"),"{0,date,dd/MM/yyyy HH:mm:ss}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colXF3AP6RCVLOBDCLSAALOMT5GDM")," - {0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(28677,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::PersoComm::RecvMessage:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::PersoComm::RecvMessage:channelId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2HVBIVBDVLOBDCLSAALOMT5GDM"),"channelId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::RecvMessage:address-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col726AU7BDVLOBDCLSAALOMT5GDM"),"address",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls44TNWNZ5VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::RecvMessage:sendTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNQ6OMXZDVLOBDCLSAALOMT5GDM"),"sendTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE4YWIZZ5VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::RecvMessage:recvTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNU6OMXZDVLOBDCLSAALOMT5GDM"),"recvTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6OP6CYB5VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::RecvMessage:importance-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW53AP6RCVLOBDCLSAALOMT5GDM"),"importance",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP7IQATR5VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTUGILCRUVXOBDCLUAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::RecvMessage:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colX3PXDZRCVLOBDCLSAALOMT5GDM"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCMOUKVZ5VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::RecvMessage:body-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXB3AP6RCVLOBDCLSAALOMT5GDM"),"body",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5ATNWNZ5VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::RecvMessage:subject-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXF3AP6RCVLOBDCLSAALOMT5GDM"),"subject",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU4BN7JR5VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::RecvMessage:destEntityGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUESOPNO7S5DEXMRED7NWYI7IFU"),"destEntityGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::RecvMessage:destMsgId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW2PIZESMNZHVXOWVZDGB24HSBQ"),"destMsgId",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::RecvMessage:destPid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNJJ6EUBFNNC4RGFO3RDQW4M6UQ"),"destPid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::RecvMessage:destTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKJZSQCXP2RE3DKTTC5Y6YIVZWY"),"destTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKZZW33URLVBI5M4KTC4LVTBGVU"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::RecvMessage:unit-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFQAETETA6ZGZTNYAADE5MFDKQY"),"unit",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQAUFQOK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("ref2K7HYMRQVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::RecvMessage:channelUnit-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRFTIAZIK7JG57D6E6PGV6AA3QQ"),"channelUnit",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGZVXYHF7NFAIHLZDE77ERH6YSE"),org.radixware.kernel.common.enums.EValType.USER_CLASS,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::RecvMessage:isUssd-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMHW5BQRKWNEQHIGODNT4FJOEV4"),"isUssd",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLIUU5H5T3ZDNJFCGPSY6EBAA2U"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::RecvMessage:ussdServiceOp-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDNDZIZBEJACZGTFJ6UZYLXJV4"),"ussdServiceOp",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQTSEKXICJGVZPQSVIPGDAYP6A"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::RecvMessage:channelKind-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5GBK5RKSFND3RI7MWIHKWOEE7A"),"channelKind",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls46TPEMLPCFEX7AEVBDTXOMTMZA"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::PersoComm::RecvMessage:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprD5ADU7IYBVDZ5CAZONUP64P7MI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLXOABYZEK5EB3J35XOJXBMJOT4"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSHKXSWRQD5FATEYVAHGKTETUJE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZP3IOFMMHJDV5EAXFLTMMSGOOY"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBAVPB45UIRHZPEEWZGJDNFFPK4"),"createFromXml",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xMess",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQV5N5Y7O6JCIZJB3AHFLMVVUII"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::PersoComm::RecvMessage - Desktop Executable*/

/*Radix::PersoComm::RecvMessage-Entity Class*/

package org.radixware.ads.PersoComm.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage")
public interface RecvMessage {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.PersoComm.explorer.RecvMessage.RecvMessage_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.PersoComm.explorer.RecvMessage.RecvMessage_DefaultModel )  super.getEntity(i);}
	}






































































































	/*Radix::PersoComm::RecvMessage:address:address-Presentation Property*/


	public class Address extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Address(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:address:address",line=1162)
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:address:address",line=1168)
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Address getAddress();
	/*Radix::PersoComm::RecvMessage:unit:unit-Presentation Property*/


	public class Unit extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Unit(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:unit:unit",line=1211)
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:unit:unit",line=1217)
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Unit getUnit();
	/*Radix::PersoComm::RecvMessage:ussdServiceOp:ussdServiceOp-Presentation Property*/


	public class UssdServiceOp extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public UssdServiceOp(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:ussdServiceOp:ussdServiceOp",line=1250)
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:ussdServiceOp:ussdServiceOp",line=1256)
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public UssdServiceOp getUssdServiceOp();
	/*Radix::PersoComm::RecvMessage:isUssd:isUssd-Presentation Property*/


	public class IsUssd extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsUssd(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:isUssd:isUssd",line=1289)
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:isUssd:isUssd",line=1295)
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsUssd getIsUssd();
	/*Radix::PersoComm::RecvMessage:destPid:destPid-Presentation Property*/


	public class DestPid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DestPid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:destPid:destPid",line=1328)
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:destPid:destPid",line=1334)
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DestPid getDestPid();
	/*Radix::PersoComm::RecvMessage:sendTime:sendTime-Presentation Property*/


	public class SendTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public SendTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:sendTime:sendTime",line=1367)
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:sendTime:sendTime",line=1373)
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public SendTime getSendTime();
	/*Radix::PersoComm::RecvMessage:recvTime:recvTime-Presentation Property*/


	public class RecvTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public RecvTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:recvTime:recvTime",line=1406)
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:recvTime:recvTime",line=1412)
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public RecvTime getRecvTime();
	/*Radix::PersoComm::RecvMessage:destEntityGuid:destEntityGuid-Presentation Property*/


	public class DestEntityGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DestEntityGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:destEntityGuid:destEntityGuid",line=1445)
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:destEntityGuid:destEntityGuid",line=1451)
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DestEntityGuid getDestEntityGuid();
	/*Radix::PersoComm::RecvMessage:destMsgId:destMsgId-Presentation Property*/


	public class DestMsgId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DestMsgId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:destMsgId:destMsgId",line=1484)
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:destMsgId:destMsgId",line=1490)
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DestMsgId getDestMsgId();
	/*Radix::PersoComm::RecvMessage:importance:importance-Presentation Property*/


	public class Importance extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Importance(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EPersoCommImportance dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPersoCommImportance ? (org.radixware.kernel.common.enums.EPersoCommImportance)x : org.radixware.kernel.common.enums.EPersoCommImportance.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EPersoCommImportance> getValClass(){
			return org.radixware.kernel.common.enums.EPersoCommImportance.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EPersoCommImportance dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPersoCommImportance ? (org.radixware.kernel.common.enums.EPersoCommImportance)x : org.radixware.kernel.common.enums.EPersoCommImportance.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:importance:importance",line=1542)
		public  org.radixware.kernel.common.enums.EPersoCommImportance getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:importance:importance",line=1548)
		public   void setValue(org.radixware.kernel.common.enums.EPersoCommImportance val) {
			Value = val;
		}
	}
	public Importance getImportance();
	/*Radix::PersoComm::RecvMessage:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:id:id",line=1581)
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:id:id",line=1587)
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::PersoComm::RecvMessage:body:body-Presentation Property*/


	public class Body extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Body(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:body:body",line=1620)
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:body:body",line=1626)
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Body getBody();
	/*Radix::PersoComm::RecvMessage:subject:subject-Presentation Property*/


	public class Subject extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Subject(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:subject:subject",line=1659)
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:subject:subject",line=1665)
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Subject getSubject();
	/*Radix::PersoComm::RecvMessage:channelKind:channelKind-Presentation Property*/


	public class ChannelKind extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ChannelKind(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.PersoComm.common.ChannelKind dummy = x == null ? null : (x instanceof org.radixware.ads.PersoComm.common.ChannelKind ? (org.radixware.ads.PersoComm.common.ChannelKind)x : org.radixware.ads.PersoComm.common.ChannelKind.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.PersoComm.common.ChannelKind> getValClass(){
			return org.radixware.ads.PersoComm.common.ChannelKind.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.PersoComm.common.ChannelKind dummy = x == null ? null : (x instanceof org.radixware.ads.PersoComm.common.ChannelKind ? (org.radixware.ads.PersoComm.common.ChannelKind)x : org.radixware.ads.PersoComm.common.ChannelKind.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:channelKind:channelKind",line=1717)
		public  org.radixware.ads.PersoComm.common.ChannelKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:channelKind:channelKind",line=1723)
		public   void setValue(org.radixware.ads.PersoComm.common.ChannelKind val) {
			Value = val;
		}
	}
	public ChannelKind getChannelKind();
	/*Radix::PersoComm::RecvMessage:destTitle:destTitle-Presentation Property*/


	public class DestTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DestTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:destTitle:destTitle",line=1756)
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:destTitle:destTitle",line=1762)
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DestTitle getDestTitle();
	public static class ViewDest extends org.radixware.kernel.common.client.models.items.Command{
		protected ViewDest(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}



}

/* Radix::PersoComm::RecvMessage - Desktop Meta*/

/*Radix::PersoComm::RecvMessage-Entity Class*/

package org.radixware.ads.PersoComm.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class RecvMessage_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::PersoComm::RecvMessage:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
			"Radix::PersoComm::RecvMessage",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgVCK6KMX7SFXU6XSYOUIU53QR6WQRB2VU"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprFWHOGC2LVXOBDCLUAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA53KJRJ5VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB6R3DUJ5VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA53KJRJ5VXOBDCLUAALOMT5GDM"),28677,

			/*Radix::PersoComm::RecvMessage:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::PersoComm::RecvMessage:address:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col726AU7BDVLOBDCLSAALOMT5GDM"),
						"address",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls44TNWNZ5VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						28,
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

						/*Radix::PersoComm::RecvMessage:address:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:sendTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNQ6OMXZDVLOBDCLSAALOMT5GDM"),
						"sendTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE4YWIZZ5VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						28,
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

						/*Radix::PersoComm::RecvMessage:sendTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:recvTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNU6OMXZDVLOBDCLSAALOMT5GDM"),
						"recvTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6OP6CYB5VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						28,
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

						/*Radix::PersoComm::RecvMessage:recvTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:importance:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW53AP6RCVLOBDCLSAALOMT5GDM"),
						"importance",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP7IQATR5VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						28,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTUGILCRUVXOBDCLUAALOMT5GDM"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::RecvMessage:importance:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTUGILCRUVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colX3PXDZRCVLOBDCLSAALOMT5GDM"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCMOUKVZ5VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						28,
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

						/*Radix::PersoComm::RecvMessage:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:body:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXB3AP6RCVLOBDCLSAALOMT5GDM"),
						"body",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5ATNWNZ5VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						28,
						null,
						null,
						null,
						true,
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

						/*Radix::PersoComm::RecvMessage:body:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:subject:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXF3AP6RCVLOBDCLSAALOMT5GDM"),
						"subject",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU4BN7JR5VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						28,
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

						/*Radix::PersoComm::RecvMessage:subject:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:destEntityGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUESOPNO7S5DEXMRED7NWYI7IFU"),
						"destEntityGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::RecvMessage:destEntityGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:destMsgId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW2PIZESMNZHVXOWVZDGB24HSBQ"),
						"destMsgId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::RecvMessage:destMsgId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:destPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNJJ6EUBFNNC4RGFO3RDQW4M6UQ"),
						"destPid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::RecvMessage:destPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:destTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKJZSQCXP2RE3DKTTC5Y6YIVZWY"),
						"destTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKZZW33URLVBI5M4KTC4LVTBGVU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						20,
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

						/*Radix::PersoComm::RecvMessage:destTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:unit:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFQAETETA6ZGZTNYAADE5MFDKQY"),
						"unit",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						21,
						null,
						null,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						null,
						null,
						null,
						262143,
						262143,false),

					/*Radix::PersoComm::RecvMessage:isUssd:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMHW5BQRKWNEQHIGODNT4FJOEV4"),
						"isUssd",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLIUU5H5T3ZDNJFCGPSY6EBAA2U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.BOOL,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::RecvMessage:isUssd:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:ussdServiceOp:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDNDZIZBEJACZGTFJ6UZYLXJV4"),
						"ussdServiceOp",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQTSEKXICJGVZPQSVIPGDAYP6A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::RecvMessage:ussdServiceOp:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:channelKind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5GBK5RKSFND3RI7MWIHKWOEE7A"),
						"channelKind",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls46TPEMLPCFEX7AEVBDTXOMTMZA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),
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

						/*Radix::PersoComm::RecvMessage:channelKind:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::PersoComm::RecvMessage:ViewDest-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdPLVNXYJEGFEJNKRRBA2PNCJPZI"),
						"ViewDest",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTH72SPC4L5FMVBNBMZFQW4HZ5Q"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgEBTCSRR6XJGUJHPO2J4QIENJSI"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.LOCAL,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTBHNMZQEJFHUJEKE7DIBKEY6BU")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
			},
			null,
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::PersoComm::RecvMessage:Time-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtH764BABKAFB7BBJUGRNUN3B6SA"),
						"Time",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYSB4ZI6EMBAKZCBWU5JOAODWDI"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNU6OMXZDVLOBDCLSAALOMT5GDM"),
								true)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref2K7HYMRQVXOBDCLUAALOMT5GDM"),"RecvMessage=>Unit (channelId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLXLOGLESUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col2HVBIVBDVLOBDCLSAALOMT5GDM")},new String[]{"channelId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSQWLHWR5VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7TGF7PWBJ5D3XLVFA2EBYHVO5Q"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprFWHOGC2LVXOBDCLUAALOMT5GDM")},
			false,false,false);
}

/* Radix::PersoComm::RecvMessage - Web Executable*/

/*Radix::PersoComm::RecvMessage-Entity Class*/

package org.radixware.ads.PersoComm.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage")
public interface RecvMessage {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.PersoComm.web.RecvMessage.RecvMessage_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.PersoComm.web.RecvMessage.RecvMessage_DefaultModel )  super.getEntity(i);}
	}






































































































	/*Radix::PersoComm::RecvMessage:address:address-Presentation Property*/


	public class Address extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Address(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:address:address",line=2478)
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:address:address",line=2484)
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Address getAddress();
	/*Radix::PersoComm::RecvMessage:unit:unit-Presentation Property*/


	public class Unit extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Unit(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:unit:unit",line=2527)
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:unit:unit",line=2533)
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Unit getUnit();
	/*Radix::PersoComm::RecvMessage:ussdServiceOp:ussdServiceOp-Presentation Property*/


	public class UssdServiceOp extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public UssdServiceOp(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:ussdServiceOp:ussdServiceOp",line=2566)
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:ussdServiceOp:ussdServiceOp",line=2572)
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public UssdServiceOp getUssdServiceOp();
	/*Radix::PersoComm::RecvMessage:isUssd:isUssd-Presentation Property*/


	public class IsUssd extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsUssd(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:isUssd:isUssd",line=2605)
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:isUssd:isUssd",line=2611)
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsUssd getIsUssd();
	/*Radix::PersoComm::RecvMessage:destPid:destPid-Presentation Property*/


	public class DestPid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DestPid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:destPid:destPid",line=2644)
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:destPid:destPid",line=2650)
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DestPid getDestPid();
	/*Radix::PersoComm::RecvMessage:sendTime:sendTime-Presentation Property*/


	public class SendTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public SendTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:sendTime:sendTime",line=2683)
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:sendTime:sendTime",line=2689)
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public SendTime getSendTime();
	/*Radix::PersoComm::RecvMessage:recvTime:recvTime-Presentation Property*/


	public class RecvTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public RecvTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:recvTime:recvTime",line=2722)
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:recvTime:recvTime",line=2728)
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public RecvTime getRecvTime();
	/*Radix::PersoComm::RecvMessage:destEntityGuid:destEntityGuid-Presentation Property*/


	public class DestEntityGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DestEntityGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:destEntityGuid:destEntityGuid",line=2761)
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:destEntityGuid:destEntityGuid",line=2767)
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DestEntityGuid getDestEntityGuid();
	/*Radix::PersoComm::RecvMessage:destMsgId:destMsgId-Presentation Property*/


	public class DestMsgId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DestMsgId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:destMsgId:destMsgId",line=2800)
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:destMsgId:destMsgId",line=2806)
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DestMsgId getDestMsgId();
	/*Radix::PersoComm::RecvMessage:importance:importance-Presentation Property*/


	public class Importance extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Importance(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EPersoCommImportance dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPersoCommImportance ? (org.radixware.kernel.common.enums.EPersoCommImportance)x : org.radixware.kernel.common.enums.EPersoCommImportance.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EPersoCommImportance> getValClass(){
			return org.radixware.kernel.common.enums.EPersoCommImportance.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EPersoCommImportance dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPersoCommImportance ? (org.radixware.kernel.common.enums.EPersoCommImportance)x : org.radixware.kernel.common.enums.EPersoCommImportance.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:importance:importance",line=2858)
		public  org.radixware.kernel.common.enums.EPersoCommImportance getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:importance:importance",line=2864)
		public   void setValue(org.radixware.kernel.common.enums.EPersoCommImportance val) {
			Value = val;
		}
	}
	public Importance getImportance();
	/*Radix::PersoComm::RecvMessage:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:id:id",line=2897)
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:id:id",line=2903)
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::PersoComm::RecvMessage:body:body-Presentation Property*/


	public class Body extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Body(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:body:body",line=2936)
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:body:body",line=2942)
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Body getBody();
	/*Radix::PersoComm::RecvMessage:subject:subject-Presentation Property*/


	public class Subject extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Subject(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:subject:subject",line=2975)
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:subject:subject",line=2981)
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Subject getSubject();
	/*Radix::PersoComm::RecvMessage:channelKind:channelKind-Presentation Property*/


	public class ChannelKind extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ChannelKind(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.PersoComm.common.ChannelKind dummy = x == null ? null : (x instanceof org.radixware.ads.PersoComm.common.ChannelKind ? (org.radixware.ads.PersoComm.common.ChannelKind)x : org.radixware.ads.PersoComm.common.ChannelKind.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.PersoComm.common.ChannelKind> getValClass(){
			return org.radixware.ads.PersoComm.common.ChannelKind.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.PersoComm.common.ChannelKind dummy = x == null ? null : (x instanceof org.radixware.ads.PersoComm.common.ChannelKind ? (org.radixware.ads.PersoComm.common.ChannelKind)x : org.radixware.ads.PersoComm.common.ChannelKind.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:channelKind:channelKind",line=3033)
		public  org.radixware.ads.PersoComm.common.ChannelKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:channelKind:channelKind",line=3039)
		public   void setValue(org.radixware.ads.PersoComm.common.ChannelKind val) {
			Value = val;
		}
	}
	public ChannelKind getChannelKind();
	/*Radix::PersoComm::RecvMessage:destTitle:destTitle-Presentation Property*/


	public class DestTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DestTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:destTitle:destTitle",line=3072)
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:destTitle:destTitle",line=3078)
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DestTitle getDestTitle();
	public static class ViewDest extends org.radixware.kernel.common.client.models.items.Command{
		protected ViewDest(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}



}

/* Radix::PersoComm::RecvMessage - Web Meta*/

/*Radix::PersoComm::RecvMessage-Entity Class*/

package org.radixware.ads.PersoComm.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class RecvMessage_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::PersoComm::RecvMessage:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
			"Radix::PersoComm::RecvMessage",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgVCK6KMX7SFXU6XSYOUIU53QR6WQRB2VU"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprFWHOGC2LVXOBDCLUAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA53KJRJ5VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB6R3DUJ5VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA53KJRJ5VXOBDCLUAALOMT5GDM"),28677,

			/*Radix::PersoComm::RecvMessage:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::PersoComm::RecvMessage:address:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col726AU7BDVLOBDCLSAALOMT5GDM"),
						"address",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls44TNWNZ5VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						28,
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

						/*Radix::PersoComm::RecvMessage:address:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:sendTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNQ6OMXZDVLOBDCLSAALOMT5GDM"),
						"sendTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE4YWIZZ5VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						28,
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

						/*Radix::PersoComm::RecvMessage:sendTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:recvTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNU6OMXZDVLOBDCLSAALOMT5GDM"),
						"recvTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6OP6CYB5VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						28,
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

						/*Radix::PersoComm::RecvMessage:recvTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:importance:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW53AP6RCVLOBDCLSAALOMT5GDM"),
						"importance",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP7IQATR5VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						28,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTUGILCRUVXOBDCLUAALOMT5GDM"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::RecvMessage:importance:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTUGILCRUVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colX3PXDZRCVLOBDCLSAALOMT5GDM"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCMOUKVZ5VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						28,
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

						/*Radix::PersoComm::RecvMessage:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:body:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXB3AP6RCVLOBDCLSAALOMT5GDM"),
						"body",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5ATNWNZ5VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						28,
						null,
						null,
						null,
						true,
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

						/*Radix::PersoComm::RecvMessage:body:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:subject:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXF3AP6RCVLOBDCLSAALOMT5GDM"),
						"subject",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU4BN7JR5VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						28,
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

						/*Radix::PersoComm::RecvMessage:subject:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:destEntityGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUESOPNO7S5DEXMRED7NWYI7IFU"),
						"destEntityGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::RecvMessage:destEntityGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:destMsgId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW2PIZESMNZHVXOWVZDGB24HSBQ"),
						"destMsgId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::RecvMessage:destMsgId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:destPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNJJ6EUBFNNC4RGFO3RDQW4M6UQ"),
						"destPid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::RecvMessage:destPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:destTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKJZSQCXP2RE3DKTTC5Y6YIVZWY"),
						"destTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKZZW33URLVBI5M4KTC4LVTBGVU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						20,
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

						/*Radix::PersoComm::RecvMessage:destTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:unit:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFQAETETA6ZGZTNYAADE5MFDKQY"),
						"unit",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						21,
						null,
						null,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						null,
						null,
						null,
						262143,
						262143,false),

					/*Radix::PersoComm::RecvMessage:isUssd:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMHW5BQRKWNEQHIGODNT4FJOEV4"),
						"isUssd",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLIUU5H5T3ZDNJFCGPSY6EBAA2U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.BOOL,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::RecvMessage:isUssd:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:ussdServiceOp:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDNDZIZBEJACZGTFJ6UZYLXJV4"),
						"ussdServiceOp",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQTSEKXICJGVZPQSVIPGDAYP6A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::RecvMessage:ussdServiceOp:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::RecvMessage:channelKind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5GBK5RKSFND3RI7MWIHKWOEE7A"),
						"channelKind",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls46TPEMLPCFEX7AEVBDTXOMTMZA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),
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

						/*Radix::PersoComm::RecvMessage:channelKind:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::PersoComm::RecvMessage:ViewDest-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdPLVNXYJEGFEJNKRRBA2PNCJPZI"),
						"ViewDest",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTH72SPC4L5FMVBNBMZFQW4HZ5Q"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgEBTCSRR6XJGUJHPO2J4QIENJSI"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.LOCAL,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTBHNMZQEJFHUJEKE7DIBKEY6BU")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
			},
			null,
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::PersoComm::RecvMessage:Time-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtH764BABKAFB7BBJUGRNUN3B6SA"),
						"Time",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYSB4ZI6EMBAKZCBWU5JOAODWDI"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNU6OMXZDVLOBDCLSAALOMT5GDM"),
								true)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref2K7HYMRQVXOBDCLUAALOMT5GDM"),"RecvMessage=>Unit (channelId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLXLOGLESUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col2HVBIVBDVLOBDCLSAALOMT5GDM")},new String[]{"channelId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSQWLHWR5VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7TGF7PWBJ5D3XLVFA2EBYHVO5Q"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprFWHOGC2LVXOBDCLUAALOMT5GDM")},
			false,false,false);
}

/* Radix::PersoComm::RecvMessage:Common - Desktop Meta*/

/*Radix::PersoComm::RecvMessage:Common-Editor Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class Common_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSQWLHWR5VXOBDCLUAALOMT5GDM"),
	"Common",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLXLOGLESUTOBDANVABIFNQAABA"),
	null,
	null,

	/*Radix::PersoComm::RecvMessage:Common:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::PersoComm::RecvMessage:Common:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgF4EIT7B5VXOBDCLUAALOMT5GDM"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRIC32MTIGFF3VP4LGEAS5WHLW4"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colX3PXDZRCVLOBDCLSAALOMT5GDM"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNU6OMXZDVLOBDCLSAALOMT5GDM"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNQ6OMXZDVLOBDCLSAALOMT5GDM"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW53AP6RCVLOBDCLSAALOMT5GDM"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col726AU7BDVLOBDCLSAALOMT5GDM"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXF3AP6RCVLOBDCLSAALOMT5GDM"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKJZSQCXP2RE3DKTTC5Y6YIVZWY"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXB3AP6RCVLOBDCLSAALOMT5GDM"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMHW5BQRKWNEQHIGODNT4FJOEV4"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDNDZIZBEJACZGTFJ6UZYLXJV4"),0,9,1,false,false)
			},null),

			/*Radix::PersoComm::RecvMessage:Common:Attachements-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgX47C3NXMVXOBDCLVAALOMT5GDM"),"Attachements",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYA7C3NXMVXOBDCLVAALOMT5GDM"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiQVWLTJHMVXOBDCLVAALOMT5GDM"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgF4EIT7B5VXOBDCLUAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgX47C3NXMVXOBDCLVAALOMT5GDM"))}
	,

	/*Radix::PersoComm::RecvMessage:Common:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::PersoComm::RecvMessage:Common:Attachement-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiQVWLTJHMVXOBDCLVAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5O3H4DJEVLOBDCLSAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6BHCGLBRVXOBDCLUAALOMT5GDM"),
					32,
					null,
					16560,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::PersoComm::RecvMessage:Common - Web Meta*/

/*Radix::PersoComm::RecvMessage:Common-Editor Presentation*/

package org.radixware.ads.PersoComm.web;
public final class Common_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSQWLHWR5VXOBDCLUAALOMT5GDM"),
	"Common",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLXLOGLESUTOBDANVABIFNQAABA"),
	null,
	null,

	/*Radix::PersoComm::RecvMessage:Common:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::PersoComm::RecvMessage:Common:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgF4EIT7B5VXOBDCLUAALOMT5GDM"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRIC32MTIGFF3VP4LGEAS5WHLW4"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colX3PXDZRCVLOBDCLSAALOMT5GDM"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNU6OMXZDVLOBDCLSAALOMT5GDM"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNQ6OMXZDVLOBDCLSAALOMT5GDM"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW53AP6RCVLOBDCLSAALOMT5GDM"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col726AU7BDVLOBDCLSAALOMT5GDM"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXF3AP6RCVLOBDCLSAALOMT5GDM"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKJZSQCXP2RE3DKTTC5Y6YIVZWY"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXB3AP6RCVLOBDCLSAALOMT5GDM"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMHW5BQRKWNEQHIGODNT4FJOEV4"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDNDZIZBEJACZGTFJ6UZYLXJV4"),0,9,1,false,false)
			},null),

			/*Radix::PersoComm::RecvMessage:Common:Attachements-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgX47C3NXMVXOBDCLVAALOMT5GDM"),"Attachements",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYA7C3NXMVXOBDCLVAALOMT5GDM"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiQVWLTJHMVXOBDCLVAALOMT5GDM"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgF4EIT7B5VXOBDCLUAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgX47C3NXMVXOBDCLVAALOMT5GDM"))}
	,

	/*Radix::PersoComm::RecvMessage:Common:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::PersoComm::RecvMessage:Common:Attachement-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiQVWLTJHMVXOBDCLVAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5O3H4DJEVLOBDCLSAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6BHCGLBRVXOBDCLUAALOMT5GDM"),
					32,
					null,
					16560,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::PersoComm::RecvMessage:Common:Model - Desktop Executable*/

/*Radix::PersoComm::RecvMessage:Common:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model")
public class Common:Model  extends org.radixware.ads.PersoComm.explorer.RecvMessage.RecvMessage_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Common:Model_mi.rdxMeta; }



	public Common:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::PersoComm::RecvMessage:Common:Model:Nested classes-Nested Classes*/

	/*Radix::PersoComm::RecvMessage:Common:Model:Properties-Properties*/

	/*Radix::PersoComm::RecvMessage:Common:Model:address-Presentation Property*/




	public class Address extends org.radixware.ads.PersoComm.explorer.RecvMessage.col726AU7BDVLOBDCLSAALOMT5GDM{
		public Address(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:address",line=3842)
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:address",line=3848)
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public Address getAddress(){return (Address)getProperty(col726AU7BDVLOBDCLSAALOMT5GDM);}

	/*Radix::PersoComm::RecvMessage:Common:Model:sendTime-Presentation Property*/




	public class SendTime extends org.radixware.ads.PersoComm.explorer.RecvMessage.colNQ6OMXZDVLOBDCLSAALOMT5GDM{
		public SendTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:sendTime",line=3884)
		public published  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:sendTime",line=3890)
		public published   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public SendTime getSendTime(){return (SendTime)getProperty(colNQ6OMXZDVLOBDCLSAALOMT5GDM);}

	/*Radix::PersoComm::RecvMessage:Common:Model:recvTime-Presentation Property*/




	public class RecvTime extends org.radixware.ads.PersoComm.explorer.RecvMessage.colNU6OMXZDVLOBDCLSAALOMT5GDM{
		public RecvTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:recvTime",line=3926)
		public published  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:recvTime",line=3932)
		public published   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public RecvTime getRecvTime(){return (RecvTime)getProperty(colNU6OMXZDVLOBDCLSAALOMT5GDM);}

	/*Radix::PersoComm::RecvMessage:Common:Model:importance-Presentation Property*/




	public class Importance extends org.radixware.ads.PersoComm.explorer.RecvMessage.colW53AP6RCVLOBDCLSAALOMT5GDM{
		public Importance(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EPersoCommImportance dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPersoCommImportance ? (org.radixware.kernel.common.enums.EPersoCommImportance)x : org.radixware.kernel.common.enums.EPersoCommImportance.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EPersoCommImportance> getValClass(){
			return org.radixware.kernel.common.enums.EPersoCommImportance.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EPersoCommImportance dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPersoCommImportance ? (org.radixware.kernel.common.enums.EPersoCommImportance)x : org.radixware.kernel.common.enums.EPersoCommImportance.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:importance",line=3987)
		public published  org.radixware.kernel.common.enums.EPersoCommImportance getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:importance",line=3993)
		public published   void setValue(org.radixware.kernel.common.enums.EPersoCommImportance val) {
			Value = val;
		}
	}
	public Importance getImportance(){return (Importance)getProperty(colW53AP6RCVLOBDCLSAALOMT5GDM);}

	/*Radix::PersoComm::RecvMessage:Common:Model:id-Presentation Property*/




	public class Id extends org.radixware.ads.PersoComm.explorer.RecvMessage.colX3PXDZRCVLOBDCLSAALOMT5GDM{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:id",line=4029)
		public published  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:id",line=4035)
		public published   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId(){return (Id)getProperty(colX3PXDZRCVLOBDCLSAALOMT5GDM);}

	/*Radix::PersoComm::RecvMessage:Common:Model:body-Presentation Property*/




	public class Body extends org.radixware.ads.PersoComm.explorer.RecvMessage.colXB3AP6RCVLOBDCLSAALOMT5GDM{
		public Body(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:body",line=4071)
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:body",line=4077)
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public Body getBody(){return (Body)getProperty(colXB3AP6RCVLOBDCLSAALOMT5GDM);}

	/*Radix::PersoComm::RecvMessage:Common:Model:subject-Presentation Property*/




	public class Subject extends org.radixware.ads.PersoComm.explorer.RecvMessage.colXF3AP6RCVLOBDCLSAALOMT5GDM{
		public Subject(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:subject",line=4113)
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:subject",line=4119)
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public Subject getSubject(){return (Subject)getProperty(colXF3AP6RCVLOBDCLSAALOMT5GDM);}

	/*Radix::PersoComm::RecvMessage:Common:Model:destEntityGuid-Presentation Property*/




	public class DestEntityGuid extends org.radixware.ads.PersoComm.explorer.RecvMessage.colUESOPNO7S5DEXMRED7NWYI7IFU{
		public DestEntityGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:destEntityGuid",line=4155)
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:destEntityGuid",line=4161)
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public DestEntityGuid getDestEntityGuid(){return (DestEntityGuid)getProperty(colUESOPNO7S5DEXMRED7NWYI7IFU);}

	/*Radix::PersoComm::RecvMessage:Common:Model:destMsgId-Presentation Property*/




	public class DestMsgId extends org.radixware.ads.PersoComm.explorer.RecvMessage.colW2PIZESMNZHVXOWVZDGB24HSBQ{
		public DestMsgId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:destMsgId",line=4197)
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:destMsgId",line=4203)
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public DestMsgId getDestMsgId(){return (DestMsgId)getProperty(colW2PIZESMNZHVXOWVZDGB24HSBQ);}

	/*Radix::PersoComm::RecvMessage:Common:Model:destPid-Presentation Property*/




	public class DestPid extends org.radixware.ads.PersoComm.explorer.RecvMessage.colNJJ6EUBFNNC4RGFO3RDQW4M6UQ{
		public DestPid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:destPid",line=4239)
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:destPid",line=4245)
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public DestPid getDestPid(){return (DestPid)getProperty(colNJJ6EUBFNNC4RGFO3RDQW4M6UQ);}

	/*Radix::PersoComm::RecvMessage:Common:Model:destTitle-Presentation Property*/




	public class DestTitle extends org.radixware.ads.PersoComm.explorer.RecvMessage.prdKJZSQCXP2RE3DKTTC5Y6YIVZWY{
		public DestTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:destTitle",line=4281)
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:destTitle",line=4287)
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public DestTitle getDestTitle(){return (DestTitle)getProperty(prdKJZSQCXP2RE3DKTTC5Y6YIVZWY);}




























	/*Radix::PersoComm::RecvMessage:Common:Model:Methods-Methods*/

	/*Radix::PersoComm::RecvMessage:Common:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:afterRead",line=4326)
	protected published  void afterRead () {
		super.afterRead();
		if(!isInSelectorRowContext()){
		    final boolean isSms = channelKind.Value == ChannelKind:Sms;
		    isUssd.setVisible(isSms);
		    ussdServiceOp.setVisible(isSms);
		    getCommand(idof[RecvMessage:ViewDest]).setVisible(Explorer.Utils::DialogUtils.canRunEditor(destEntityGuid.Value, destPid.Value));
		}
	}

	/*Radix::PersoComm::RecvMessage:Common:Model:ViewDest-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:ViewDest",line=4339)
	public published  void ViewDest (org.radixware.ads.PersoComm.explorer.RecvMessage.ViewDest command, org.radixware.kernel.common.types.Id propertyId) {
		Explorer.Utils::DialogUtils.runEditor(Environment,destEntityGuid.Value, destPid.Value);
	}
	public final class ViewDest extends org.radixware.ads.PersoComm.explorer.RecvMessage.ViewDest{
		protected ViewDest(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ViewDest( this, propertyId );
		}

	}













}

/* Radix::PersoComm::RecvMessage:Common:Model - Desktop Meta*/

/*Radix::PersoComm::RecvMessage:Common:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Common:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemSQWLHWR5VXOBDCLUAALOMT5GDM"),
						"Common:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::PersoComm::RecvMessage:Common:Model:Properties-Properties*/
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

/* Radix::PersoComm::RecvMessage:Common:Model - Web Executable*/

/*Radix::PersoComm::RecvMessage:Common:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model")
public class Common:Model  extends org.radixware.ads.PersoComm.web.RecvMessage.RecvMessage_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Common:Model_mi.rdxMeta; }



	public Common:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::PersoComm::RecvMessage:Common:Model:Nested classes-Nested Classes*/

	/*Radix::PersoComm::RecvMessage:Common:Model:Properties-Properties*/

	/*Radix::PersoComm::RecvMessage:Common:Model:destEntityGuid-Presentation Property*/




	public class DestEntityGuid extends org.radixware.ads.PersoComm.web.RecvMessage.colUESOPNO7S5DEXMRED7NWYI7IFU{
		public DestEntityGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:destEntityGuid",line=4444)
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:destEntityGuid",line=4450)
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public DestEntityGuid getDestEntityGuid(){return (DestEntityGuid)getProperty(colUESOPNO7S5DEXMRED7NWYI7IFU);}

	/*Radix::PersoComm::RecvMessage:Common:Model:destPid-Presentation Property*/




	public class DestPid extends org.radixware.ads.PersoComm.web.RecvMessage.colNJJ6EUBFNNC4RGFO3RDQW4M6UQ{
		public DestPid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:destPid",line=4486)
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:destPid",line=4492)
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public DestPid getDestPid(){return (DestPid)getProperty(colNJJ6EUBFNNC4RGFO3RDQW4M6UQ);}










	/*Radix::PersoComm::RecvMessage:Common:Model:Methods-Methods*/

	/*Radix::PersoComm::RecvMessage:Common:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:afterRead",line=4513)
	protected published  void afterRead () {
		super.afterRead();
		if(!isInSelectorRowContext()){
		    final boolean isSms = channelKind.Value == ChannelKind:Sms;
		    isUssd.setVisible(isSms);
		    ussdServiceOp.setVisible(isSms);
		    getCommand(idof[RecvMessage:ViewDest]).setVisible(Explorer.Utils::DialogUtils.canRunEditor(destEntityGuid.Value, destPid.Value));
		}
	}

	/*Radix::PersoComm::RecvMessage:Common:Model:ViewDest-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::RecvMessage:Common:Model:ViewDest",line=4526)
	public published  void ViewDest (org.radixware.ads.PersoComm.web.RecvMessage.ViewDest command, org.radixware.kernel.common.types.Id propertyId) {
		Explorer.Utils::DialogUtils.runEditor(Environment,destEntityGuid.Value, destPid.Value);
	}
	public final class ViewDest extends org.radixware.ads.PersoComm.web.RecvMessage.ViewDest{
		protected ViewDest(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ViewDest( this, propertyId );
		}

	}













}

/* Radix::PersoComm::RecvMessage:Common:Model - Web Meta*/

/*Radix::PersoComm::RecvMessage:Common:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Common:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemSQWLHWR5VXOBDCLUAALOMT5GDM"),
						"Common:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::PersoComm::RecvMessage:Common:Model:Properties-Properties*/
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

/* Radix::PersoComm::RecvMessage:Create - Desktop Meta*/

/*Radix::PersoComm::RecvMessage:Create-Editor Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7TGF7PWBJ5D3XLVFA2EBYHVO5Q"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLXLOGLESUTOBDANVABIFNQAABA"),
			null,
			null,

			/*Radix::PersoComm::RecvMessage:Create:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::PersoComm::RecvMessage:Create:Main-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg6F6DF2DB7ZCBDAHV4UIDJ2I7CA"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6SNGMWGNEVCKBC7UAKGXNUZQBU"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colX3PXDZRCVLOBDCLSAALOMT5GDM"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNU6OMXZDVLOBDCLSAALOMT5GDM"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col726AU7BDVLOBDCLSAALOMT5GDM"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXF3AP6RCVLOBDCLSAALOMT5GDM"),0,3,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg6F6DF2DB7ZCBDAHV4UIDJ2I7CA"))}
			,

			/*Radix::PersoComm::RecvMessage:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			2192,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.explorer.RecvMessage.RecvMessage_DefaultModel(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::PersoComm::RecvMessage:Create - Web Meta*/

/*Radix::PersoComm::RecvMessage:Create-Editor Presentation*/

package org.radixware.ads.PersoComm.web;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7TGF7PWBJ5D3XLVFA2EBYHVO5Q"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLXLOGLESUTOBDANVABIFNQAABA"),
			null,
			null,

			/*Radix::PersoComm::RecvMessage:Create:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::PersoComm::RecvMessage:Create:Main-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg6F6DF2DB7ZCBDAHV4UIDJ2I7CA"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6SNGMWGNEVCKBC7UAKGXNUZQBU"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colX3PXDZRCVLOBDCLSAALOMT5GDM"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNU6OMXZDVLOBDCLSAALOMT5GDM"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col726AU7BDVLOBDCLSAALOMT5GDM"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXF3AP6RCVLOBDCLSAALOMT5GDM"),0,3,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg6F6DF2DB7ZCBDAHV4UIDJ2I7CA"))}
			,

			/*Radix::PersoComm::RecvMessage:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			2192,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.web.RecvMessage.RecvMessage_DefaultModel(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::PersoComm::RecvMessage:Common - Desktop Meta*/

/*Radix::PersoComm::RecvMessage:Common-Selector Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class Common_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Common_mi();
	private Common_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprFWHOGC2LVXOBDCLUAALOMT5GDM"),
		"Common",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLXLOGLESUTOBDANVABIFNQAABA"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtH764BABKAFB7BBJUGRNUN3B6SA"),
		null,
		false,
		true,
		null,
		290855,
		null,
		2192,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7TGF7PWBJ5D3XLVFA2EBYHVO5Q")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSQWLHWR5VXOBDCLUAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colX3PXDZRCVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNU6OMXZDVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col726AU7BDVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXF3AP6RCVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW53AP6RCVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUESOPNO7S5DEXMRED7NWYI7IFU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNJJ6EUBFNNC4RGFO3RDQW4M6UQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.explorer.RecvMessage.DefaultGroupModel(userSession,this);
	}
}
/* Radix::PersoComm::RecvMessage:Common - Web Meta*/

/*Radix::PersoComm::RecvMessage:Common-Selector Presentation*/

package org.radixware.ads.PersoComm.web;
public final class Common_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Common_mi();
	private Common_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprFWHOGC2LVXOBDCLUAALOMT5GDM"),
		"Common",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLXLOGLESUTOBDANVABIFNQAABA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLXLOGLESUTOBDANVABIFNQAABA"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtH764BABKAFB7BBJUGRNUN3B6SA"),
		null,
		false,
		true,
		null,
		290855,
		null,
		2192,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7TGF7PWBJ5D3XLVFA2EBYHVO5Q")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSQWLHWR5VXOBDCLUAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colX3PXDZRCVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNU6OMXZDVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col726AU7BDVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXF3AP6RCVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW53AP6RCVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUESOPNO7S5DEXMRED7NWYI7IFU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNJJ6EUBFNNC4RGFO3RDQW4M6UQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.web.RecvMessage.DefaultGroupModel(userSession,this);
	}
}
/* Radix::PersoComm::RecvMessage - Localizing Bundle */
package org.radixware.ads.PersoComm.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class RecvMessage - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Адрес");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Address");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls44TNWNZ5VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вид канала");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Channel type");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls46TPEMLPCFEX7AEVBDTXOMTMZA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Содержит модуль системы, который является каналом передачи сообщения.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Contains the system unit serving as the message delivery channel.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4K72IWULFZHCNIAUC6SWMCZXUE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текст сообщения");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message text");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5ATNWNZ5VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время получения");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Received");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6OP6CYB5VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Главное");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6SNGMWGNEVCKBC7UAKGXNUZQBU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Полученное сообщение");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Received Message");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA53KJRJ5VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Полученные сообщения");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Received Messages");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB6R3DUJ5VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCMOUKVZ5VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Код USSD-операции");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"USSD operation code");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQTSEKXICJGVZPQSVIPGDAYP6A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время отправки");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Sent");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE4YWIZZ5VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс, реализующий журнал полученных сообщений.Обеспечивает механизм для работы с такими сообщениями.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class implements the log of received messages. Provides the mechanism to work with these messages.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsG6SZAFFQ3RFMJNBYFNZBJJ6CHM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Получатель сообщения");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message destination");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKZZW33URLVBI5M4KTC4LVTBGVU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"USSD");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"USSD");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLIUU5H5T3ZDNJFCGPSY6EBAA2U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Важность");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Importance");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP7IQATR5VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRIC32MTIGFF3VP4LGEAS5WHLW4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Просмотреть объект назначения");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"View Destination Entity");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTH72SPC4L5FMVBNBMZFQW4HZ5Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тема");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Subject");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU4BN7JR5VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Содержит название сущности, которая является получателем сообщения.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Contains the name of the entity that is a message receiver.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXABJ7FEUIJFF7IOR7VPFMN22GQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вложения");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attachments");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYA7C3NXMVXOBDCLVAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По времени");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By time");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYSB4ZI6EMBAKZCBWU5JOAODWDI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(RecvMessage - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecLXLOGLESUTOBDANVABIFNQAABA"),"RecvMessage - Localizing Bundle",$$$items$$$);
}
