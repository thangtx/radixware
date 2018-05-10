
/* Radix::PersoComm::Unit.Channel.AbstractBase - Server Executable*/

/*Radix::PersoComm::Unit.Channel.AbstractBase-Application Class*/

package org.radixware.ads.PersoComm.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase")
public abstract published class Unit.Channel.AbstractBase  extends org.radixware.ads.System.server.Unit  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Unit.Channel.AbstractBase_mi.rdxMeta;}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Nested classes-Nested Classes*/

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Properties-Properties*/

	/*Radix::PersoComm::Unit.Channel.AbstractBase:kind-Detail Column Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:kind")
	public published  org.radixware.ads.PersoComm.common.ChannelKind getKind() {
		return kind;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:kind")
	public published   void setKind(org.radixware.ads.PersoComm.common.ChannelKind val) {
		kind = val;
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:addressTemplate-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:addressTemplate")
	public published  Str getAddressTemplate() {
		return addressTemplate;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:addressTemplate")
	public published   void setAddressTemplate(Str val) {
		addressTemplate = val;
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:subjectTemplate-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:subjectTemplate")
	public published  Str getSubjectTemplate() {
		return subjectTemplate;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:subjectTemplate")
	public published   void setSubjectTemplate(Str val) {
		subjectTemplate = val;
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:sendPeriod-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:sendPeriod")
	public published  Int getSendPeriod() {
		return sendPeriod;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:sendPeriod")
	public published   void setSendPeriod(Int val) {
		sendPeriod = val;
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:recvPeriod-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:recvPeriod")
	public published  Int getRecvPeriod() {
		return recvPeriod;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:recvPeriod")
	public published   void setRecvPeriod(Int val) {
		recvPeriod = val;
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:sendAddress-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:sendAddress")
	public published  Str getSendAddress() {
		return sendAddress;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:sendAddress")
	public published   void setSendAddress(Str val) {
		sendAddress = val;
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:recvAddress-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:recvAddress")
	public published  Str getRecvAddress() {
		return recvAddress;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:recvAddress")
	public published   void setRecvAddress(Str val) {
		recvAddress = val;
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:messAddressRegexp-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:messAddressRegexp")
	public published  Str getMessAddressRegexp() {
		return messAddressRegexp;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:messAddressRegexp")
	public published   void setMessAddressRegexp(Str val) {
		messAddressRegexp = val;
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:routingPriority-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:routingPriority")
	public published  Int getRoutingPriority() {
		return routingPriority;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:routingPriority")
	public published   void setRoutingPriority(Int val) {
		routingPriority = val;
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:encoding-Detail Column Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:encoding")
	public published  org.radixware.ads.Common.common.Encoding getEncoding() {
		return encoding;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:encoding")
	public published   void setEncoding(org.radixware.ads.Common.common.Encoding val) {
		encoding = val;
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:handlersSeqs-Dynamic Property*/



	protected org.radixware.kernel.common.types.ArrInt handlersSeqs=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:handlersSeqs")
	private final  org.radixware.kernel.common.types.ArrInt getHandlersSeqs() {

		if (internal[handlersSeqs] == null) {
		    internal[handlersSeqs] = new ArrInt();
		    PersoComm.Db::ChannelHandlersByUnitCursor cur = PersoComm.Db::ChannelHandlersByUnitCursor.open(this.id);
		    try {
		        while (cur.next()) {
		            internal[handlersSeqs].add(cur.seq);
		        }
		    } finally {
		        cur.close();
		    }
		}

		return internal[handlersSeqs];
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:minImportance-Detail Column Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:minImportance")
	public published  org.radixware.kernel.common.enums.EPersoCommImportance getMinImportance() {
		return minImportance;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:minImportance")
	public published   void setMinImportance(org.radixware.kernel.common.enums.EPersoCommImportance val) {
		minImportance = val;
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:maxImportance-Detail Column Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:maxImportance")
	public published  org.radixware.kernel.common.enums.EPersoCommImportance getMaxImportance() {
		return maxImportance;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:maxImportance")
	public published   void setMaxImportance(org.radixware.kernel.common.enums.EPersoCommImportance val) {
		maxImportance = val;
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:sendTimeout-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:sendTimeout")
	public published  Int getSendTimeout() {
		return sendTimeout;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:sendTimeout")
	public published   void setSendTimeout(Int val) {
		sendTimeout = val;
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:calcPriorityFunc-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:calcPriorityFunc")
	public published  org.radixware.ads.PersoComm.server.UserFunc.CalcChannelPriority getCalcPriorityFunc() {
		return calcPriorityFunc;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:calcPriorityFunc")
	public published   void setCalcPriorityFunc(org.radixware.ads.PersoComm.server.UserFunc.CalcChannelPriority val) {
		calcPriorityFunc = val;
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:routingKeyRegexp-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:routingKeyRegexp")
	public published  Str getRoutingKeyRegexp() {
		return routingKeyRegexp;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:routingKeyRegexp")
	public published   void setRoutingKeyRegexp(Str val) {
		routingKeyRegexp = val;
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPolicy-Detail Column Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPolicy")
	public  org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy getDeliveryTrackingPolicy() {
		return deliveryTrackingPolicy;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPolicy")
	public   void setDeliveryTrackingPolicy(org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy val) {
		deliveryTrackingPolicy = val;
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPeriod-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPeriod")
	public  Int getDeliveryTrackingPeriod() {

		return internal[deliveryTrackingPeriod] == null ? null : internal[deliveryTrackingPeriod].longValue() / 3600;

	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPeriod")
	public   void setDeliveryTrackingPeriod(Int val) {

		internal[deliveryTrackingPeriod] = val == null ? null : val.longValue() * 3600;

	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingRetryPeriod-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingRetryPeriod")
	public  Int getDeliveryTrackingRetryPeriod() {

		return internal[deliveryTrackingRetryPeriod] == null ? null : internal[deliveryTrackingRetryPeriod].longValue() / 60;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingRetryPeriod")
	public   void setDeliveryTrackingRetryPeriod(Int val) {

		internal[deliveryTrackingRetryPeriod] = val == null ? null : val.longValue() * 60;
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:forwardDelaySec-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:forwardDelaySec")
	public  Int getForwardDelaySec() {
		return forwardDelaySec;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:forwardDelaySec")
	public   void setForwardDelaySec(Int val) {
		forwardDelaySec = val;
	}

















































































































































	/*Radix::PersoComm::Unit.Channel.AbstractBase:Methods-Methods*/

	/*Radix::PersoComm::Unit.Channel.AbstractBase:getUsedAddresses-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:getUsedAddresses")
	public published  java.util.Collection<org.radixware.ads.System.server.AddressInfo> getUsedAddresses () {
		return null;
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:getHandlers-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:getHandlers")
	public published  org.radixware.kernel.server.types.ArrEntity<org.radixware.ads.mdlU6XJ2RXD4ZAJPBS2QB6WDAD3JQ.server.aecYDGQACJVVLOBDCLSAALOMT5GDM> getHandlers () {
		ArrParentRef<ChannelHandler> handlers = new ArrParentRef<ChannelHandler>(getArte());

		for (Int seq : handlersSeqs) {
		    handlers.add(ChannelHandler.loadByPK(this.id, seq, true));
		}

		return handlers;
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:calcPriority-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:calcPriority")
	public published  int calcPriority (org.radixware.ads.PersoComm.server.OutMessage msg) {
		if (calcPriorityFunc == null) {
		    return routingPriority.intValue();
		}
		return calcPriorityFunc.calcPriority(msg);
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:onReceive-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:onReceive")
	public published  Int onReceive (org.radixware.schemas.personalcommunications.MessageType xMess) {
		if (xMess == null) {
		    return null;
		}
		Arte::Trace.enterContext(Arte::EventContextType:SystemUnit, id);
		try {
		    Arte::Trace.put(Arte::EventSeverity:Debug, "Received persocomm message from '" + xMess.getAddressFrom() + "'", Arte::EventSource:AppPersoComm);
		    Arte::Trace.putSensitive(Arte::EventSeverity:Debug, "Message content: " + xMess, Arte::EventSource:AppPersoComm);

		    for (ChannelHandler h : getHandlers()) {

		        final Str subj = xMess.getSubject() != null ? xMess.getSubject() : "";
		        if (h.subjectRegexp != null && !subj.matches(h.subjectRegexp)) {
		            continue;
		        }

		        final Str body = xMess.getBody() != null ? xMess.getBody() : "";
		        if (h.bodyRegexp != null && !body.matches(h.bodyRegexp)) {
		            continue;
		        }

		        RecvMessage recvMess = h.process(xMess);
		        if (recvMess != null) {
		            return recvMess.id;
		        } else {
		            return null;
		        }
		    }
		    Arte::Trace.put(Arte::EventSeverity:Debug, "No handler found for message from '" + xMess.getAddressFrom() + "'", Arte::EventSource:AppPersoComm);
		    return null;
		} finally {
		    Arte::Trace.leaveContext(Arte::EventContextType:SystemUnit, id);
		}
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::PersoComm::Unit.Channel.AbstractBase - Server Meta*/

/*Radix::PersoComm::Unit.Channel.AbstractBase-Application Class*/

package org.radixware.ads.PersoComm.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.Channel.AbstractBase_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),"Unit.Channel.AbstractBase",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGZVXYHF7NFAIHLZDE77ERH6YSE"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::PersoComm::Unit.Channel.AbstractBase:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
							/*Owner Class Name*/
							"Unit.Channel.AbstractBase",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGZVXYHF7NFAIHLZDE77ERH6YSE"),
							/*Property presentations*/

							/*Radix::PersoComm::Unit.Channel.AbstractBase:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::PersoComm::Unit.Channel.AbstractBase:kind:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62V3FXUDVTOBDCLTAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.AbstractBase:addressTemplate:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colL3M53M2LBNGE3HZGY7AKTWLDMI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.AbstractBase:subjectTemplate:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM5FHLVP23ZBQZJXA3VI647TST4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.AbstractBase:sendPeriod:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXBZDYDMEVTOBDCLTAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.AbstractBase:recvPeriod:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXFZDYDMEVTOBDCLTAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.AbstractBase:sendAddress:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXJZDYDMEVTOBDCLTAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.AbstractBase:recvAddress:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXNZDYDMEVTOBDCLTAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.AbstractBase:messAddressRegexp:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXRZDYDMEVTOBDCLTAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.AbstractBase:routingPriority:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXVZDYDMEVTOBDCLTAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::PersoComm::Unit.Channel.AbstractBase:encoding:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXZZDYDMEVTOBDCLTAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.AbstractBase:handlersSeqs:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdF3SGW4U5M5EJNASVPFBOFXZK7E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.AbstractBase:minImportance:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7HPHZW2DJRHVJEYJSANRF7CASM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.AbstractBase:maxImportance:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4TIXFUZ7YNDYPDT5MVYA5XWERA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.AbstractBase:sendTimeout:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYYXVCC53YBHT3PZYC2UEGXPGGE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.AbstractBase:calcPriorityFunc:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZYMZD3ODGNDZZDU2Q746Q65XXE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.AbstractBase:routingKeyRegexp:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCFB4V6V7VC47GPA5WGBSCKPWA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPolicy:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEXIOHPTFYRHDBOLCXVAFJD5EJ4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPeriod:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPERH4NC4CVF4DG44EEBXFWHKXM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingRetryPeriod:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVBZ4AKNLFVCFJDFAUJXZPZS5ZY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::Unit.Channel.AbstractBase:forwardDelaySec:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC7GIBM6CVJDIXB2MCZKYXAAEJI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
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
									/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprGVZBTVH6VXOBDCLVAALOMT5GDM"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM"),
									3216,
									null,

									/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:ChannelHandler-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiAIGOSGOM3PORDOFEABIFNQAABA"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYDGQACJVVLOBDCLSAALOMT5GDM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZ2IX76OO3PORDOFEABIFNQAABA"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refJPRLYGJVVLOBDCLSAALOMT5GDM"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::PersoComm::Unit.Channel.AbstractBase:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("spr42ALGCTMUZB6BICZW7XRIE5DOA"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6AFPZS6TRHNRDB5MAALOMT5GDM"),16506,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:DbFuncCall FuncId=\"dfn3OXAHT4FYLORDBBJABIFNQAABA\"/></xsc:Item><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colF2TCBU6YX7NRDB6TAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>, </xsc:Sql></xsc:Item><xsc:Item><xsc:Id Path=\"aclVCLH3TEDVTOBDCLTAALOMT5GDM\"/></xsc:Item><xsc:Item><xsc:Sql>)>0</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprS6PW3X2EKJFUNPGCXYDBACXY7A")},null,null)
							},
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							null,
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::PersoComm::Unit.Channel.AbstractBase:Default-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccKOGFFVPJAHOBDA26AAMPGXSZKU"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
									}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),

						/*Radix::PersoComm::Unit.Channel.AbstractBase:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::PersoComm::Unit.Channel.AbstractBase:kind-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62V3FXUDVTOBDCLTAALOMT5GDM"),"kind",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZVCKAEK2VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colB2VQZVZFVLOBDCLSAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.AbstractBase:addressTemplate-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colL3M53M2LBNGE3HZGY7AKTWLDMI"),"addressTemplate",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5NSOFBS7GRBXFGN37IEKRDUUD4"),org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colDSHWAVJSVLOBDCLSAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.AbstractBase:subjectTemplate-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM5FHLVP23ZBQZJXA3VI647TST4"),"subjectTemplate",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMOFCSELSZNGCTJAAD7T4TNF65Y"),org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col6CT2K2RSVLOBDCLSAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.AbstractBase:sendPeriod-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXBZDYDMEVTOBDCLTAALOMT5GDM"),"sendPeriod",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYKZ5DIK2VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colNSHJLYZFVLOBDCLSAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.AbstractBase:recvPeriod-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXFZDYDMEVTOBDCLTAALOMT5GDM"),"recvPeriod",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBWFQQT22VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col6ARRB4BFVLOBDCLSAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.AbstractBase:sendAddress-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXJZDYDMEVTOBDCLTAALOMT5GDM"),"sendAddress",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKOCJ3B22VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colWASXULBLVLOBDCLSAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.AbstractBase:recvAddress-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXNZDYDMEVTOBDCLTAALOMT5GDM"),"recvAddress",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD6UAIN22VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colNOHTUMZLVLOBDCLSAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.AbstractBase:messAddressRegexp-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXRZDYDMEVTOBDCLTAALOMT5GDM"),"messAddressRegexp",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPSVBYHK2VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSI5Q6JLVLOBDCLSAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom(".*")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.AbstractBase:routingPriority-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXVZDYDMEVTOBDCLTAALOMT5GDM"),"routingPriority",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEFIE6ZC2VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colVV73PCZLVLOBDCLSAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.AbstractBase:encoding-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXZZDYDMEVTOBDCLTAALOMT5GDM"),"encoding",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH32KAMUEVTOBDCLTAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsVXR524YMLLOBDB6NAAN7YHKUNI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colRPM5HGZLVLOBDCLSAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("(java.nio.charset.Charset.defaultCharset().name())")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.AbstractBase:handlersSeqs-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdF3SGW4U5M5EJNASVPFBOFXZK7E"),"handlersSeqs",null,org.radixware.kernel.common.enums.EValType.ARR_INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.AbstractBase:minImportance-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7HPHZW2DJRHVJEYJSANRF7CASM"),"minImportance",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEFYXFATGNRDN7LORT5PAMUGK6U"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTUGILCRUVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colDS7R3IHBBFBJVGXEA5XTB36HHI"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.AbstractBase:maxImportance-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4TIXFUZ7YNDYPDT5MVYA5XWERA"),"maxImportance",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCPW73T3XB5BY3HEW3E3KHABMRQ"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTUGILCRUVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col2TCIGGC4EFA6BGRDSLQGCOLQQ4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("2")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.AbstractBase:sendTimeout-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYYXVCC53YBHT3PZYC2UEGXPGGE"),"sendTimeout",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6JMQL5RAUBELDB4YA65PCDVWOU"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colV4JLFAINZBDR3MMLVE36PDT4FA"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.AbstractBase:calcPriorityFunc-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZYMZD3ODGNDZZDU2Q746Q65XXE"),"calcPriorityFunc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWBWNAKAFAZCOBJ25T7CP3OF7GI"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLJ4LHVR3HRCVBOB3GK5MY2O53U"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.AbstractBase:routingKeyRegexp-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCFB4V6V7VC47GPA5WGBSCKPWA"),"routingKeyRegexp",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7ETXLGO36FHIBJ237B3F5F25IQ"),org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colESMLS2WYC5G6FGDNC5MAUIXPDI"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPolicy-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEXIOHPTFYRHDBOLCXVAFJD5EJ4"),"deliveryTrackingPolicy",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsG3IGDWS7FBCXHNB3ZED4OW5KI4"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsQYGCBO7AWFG7RBTB6B355UVJNY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colQYYL4LVVA5CFVMK63MXXWHGJV4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("None")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPeriod-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPERH4NC4CVF4DG44EEBXFWHKXM"),"deliveryTrackingPeriod",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDJ6NV2YI2BHQLE4LTI3VAGS3JU"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHUCZH4SC5A6VKLQWEXJLA7AJM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingRetryPeriod-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVBZ4AKNLFVCFJDFAUJXZPZS5ZY"),"deliveryTrackingRetryPeriod",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAE33OZQQX5CY5LXM4U3PY3P3L4"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colOSQWZLFLI5G3RCM5C7JFSQX6M4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::Unit.Channel.AbstractBase:forwardDelaySec-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC7GIBM6CVJDIXB2MCZKYXAAEJI"),"forwardDelaySec",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLO3SEVIHJZDWHFVC32FYS4C67Y"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col4TEUNP3GBJBMJFVSUIHACL4PZE"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::PersoComm::Unit.Channel.AbstractBase:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQIF7CPRFE5GUTIY26PT5SYUD6E"),"getUsedAddresses",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTHZOL5JYP5CNVDJVPKGCVTGP5A"),"getHandlers",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.ARR_REF),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEMFM5YNBGFGDLMZKF5X635K7QY"),"calcPriority",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("msg",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr55KBSJ4TEJFZJKNVNGMAKCFGHE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEMLY6CZEXBCT3EW5LRCIVNEAUM"),"onReceive",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xMess",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMWQ4JKT2CRDO5NMYKP7QOXI5RQ"))
								},org.radixware.kernel.common.enums.EValType.INT)
						},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("refLJSTGVRFVLOBDCLSAALOMT5GDM")},
						null,null,null,false);
}

/* Radix::PersoComm::Unit.Channel.AbstractBase - Desktop Executable*/

/*Radix::PersoComm::Unit.Channel.AbstractBase-Application Class*/

package org.radixware.ads.PersoComm.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase")
public interface Unit.Channel.AbstractBase   extends org.radixware.ads.System.explorer.Unit  {



























































































































































































































































































	/*Radix::PersoComm::Unit.Channel.AbstractBase:maxImportance:maxImportance-Presentation Property*/


	public class MaxImportance extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxImportance(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:maxImportance:maxImportance")
		public  org.radixware.kernel.common.enums.EPersoCommImportance getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:maxImportance:maxImportance")
		public   void setValue(org.radixware.kernel.common.enums.EPersoCommImportance val) {
			Value = val;
		}
	}
	public MaxImportance getMaxImportance();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:kind:kind-Presentation Property*/


	public class Kind extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Kind(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:kind:kind")
		public  org.radixware.ads.PersoComm.common.ChannelKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:kind:kind")
		public   void setValue(org.radixware.ads.PersoComm.common.ChannelKind val) {
			Value = val;
		}
	}
	public Kind getKind();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:minImportance:minImportance-Presentation Property*/


	public class MinImportance extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MinImportance(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:minImportance:minImportance")
		public  org.radixware.kernel.common.enums.EPersoCommImportance getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:minImportance:minImportance")
		public   void setValue(org.radixware.kernel.common.enums.EPersoCommImportance val) {
			Value = val;
		}
	}
	public MinImportance getMinImportance();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:forwardDelaySec:forwardDelaySec-Presentation Property*/


	public class ForwardDelaySec extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ForwardDelaySec(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:forwardDelaySec:forwardDelaySec")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:forwardDelaySec:forwardDelaySec")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ForwardDelaySec getForwardDelaySec();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:routingKeyRegexp:routingKeyRegexp-Presentation Property*/


	public class RoutingKeyRegexp extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RoutingKeyRegexp(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:routingKeyRegexp:routingKeyRegexp")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:routingKeyRegexp:routingKeyRegexp")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoutingKeyRegexp getRoutingKeyRegexp();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPolicy:deliveryTrackingPolicy-Presentation Property*/


	public class DeliveryTrackingPolicy extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DeliveryTrackingPolicy(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy ? (org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy)x : org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy> getValClass(){
			return org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy ? (org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy)x : org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPolicy:deliveryTrackingPolicy")
		public  org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPolicy:deliveryTrackingPolicy")
		public   void setValue(org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy val) {
			Value = val;
		}
	}
	public DeliveryTrackingPolicy getDeliveryTrackingPolicy();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:addressTemplate:addressTemplate-Presentation Property*/


	public class AddressTemplate extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AddressTemplate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:addressTemplate:addressTemplate")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:addressTemplate:addressTemplate")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AddressTemplate getAddressTemplate();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:subjectTemplate:subjectTemplate-Presentation Property*/


	public class SubjectTemplate extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SubjectTemplate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:subjectTemplate:subjectTemplate")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:subjectTemplate:subjectTemplate")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SubjectTemplate getSubjectTemplate();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPeriod:deliveryTrackingPeriod-Presentation Property*/


	public class DeliveryTrackingPeriod extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public DeliveryTrackingPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPeriod:deliveryTrackingPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPeriod:deliveryTrackingPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DeliveryTrackingPeriod getDeliveryTrackingPeriod();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingRetryPeriod:deliveryTrackingRetryPeriod-Presentation Property*/


	public class DeliveryTrackingRetryPeriod extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public DeliveryTrackingRetryPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingRetryPeriod:deliveryTrackingRetryPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingRetryPeriod:deliveryTrackingRetryPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DeliveryTrackingRetryPeriod getDeliveryTrackingRetryPeriod();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:sendPeriod:sendPeriod-Presentation Property*/


	public class SendPeriod extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SendPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:sendPeriod:sendPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:sendPeriod:sendPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SendPeriod getSendPeriod();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:recvPeriod:recvPeriod-Presentation Property*/


	public class RecvPeriod extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public RecvPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:recvPeriod:recvPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:recvPeriod:recvPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public RecvPeriod getRecvPeriod();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:sendAddress:sendAddress-Presentation Property*/


	public class SendAddress extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SendAddress(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:sendAddress:sendAddress")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:sendAddress:sendAddress")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SendAddress getSendAddress();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:recvAddress:recvAddress-Presentation Property*/


	public class RecvAddress extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RecvAddress(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:recvAddress:recvAddress")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:recvAddress:recvAddress")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RecvAddress getRecvAddress();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:messAddressRegexp:messAddressRegexp-Presentation Property*/


	public class MessAddressRegexp extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public MessAddressRegexp(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:messAddressRegexp:messAddressRegexp")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:messAddressRegexp:messAddressRegexp")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MessAddressRegexp getMessAddressRegexp();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:routingPriority:routingPriority-Presentation Property*/


	public class RoutingPriority extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public RoutingPriority(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:routingPriority:routingPriority")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:routingPriority:routingPriority")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public RoutingPriority getRoutingPriority();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:encoding:encoding-Presentation Property*/


	public class Encoding extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Encoding(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Common.common.Encoding dummy = x == null ? null : (x instanceof org.radixware.ads.Common.common.Encoding ? (org.radixware.ads.Common.common.Encoding)x : org.radixware.ads.Common.common.Encoding.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Common.common.Encoding> getValClass(){
			return org.radixware.ads.Common.common.Encoding.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Common.common.Encoding dummy = x == null ? null : (x instanceof org.radixware.ads.Common.common.Encoding ? (org.radixware.ads.Common.common.Encoding)x : org.radixware.ads.Common.common.Encoding.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:encoding:encoding")
		public  org.radixware.ads.Common.common.Encoding getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:encoding:encoding")
		public   void setValue(org.radixware.ads.Common.common.Encoding val) {
			Value = val;
		}
	}
	public Encoding getEncoding();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:sendTimeout:sendTimeout-Presentation Property*/


	public class SendTimeout extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SendTimeout(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:sendTimeout:sendTimeout")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:sendTimeout:sendTimeout")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SendTimeout getSendTimeout();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:handlersSeqs:handlersSeqs-Presentation Property*/


	public class HandlersSeqs extends org.radixware.kernel.common.client.models.items.properties.PropertyArrInt{
		public HandlersSeqs(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.ArrInt dummy = ((org.radixware.kernel.common.types.ArrInt)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:handlersSeqs:handlersSeqs")
		public  org.radixware.kernel.common.types.ArrInt getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:handlersSeqs:handlersSeqs")
		public   void setValue(org.radixware.kernel.common.types.ArrInt val) {
			Value = val;
		}
	}
	public HandlersSeqs getHandlersSeqs();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:calcPriorityFunc:calcPriorityFunc-Presentation Property*/


	public class CalcPriorityFunc extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public CalcPriorityFunc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:calcPriorityFunc:calcPriorityFunc")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:calcPriorityFunc:calcPriorityFunc")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public CalcPriorityFunc getCalcPriorityFunc();


}

/* Radix::PersoComm::Unit.Channel.AbstractBase - Desktop Meta*/

/*Radix::PersoComm::Unit.Channel.AbstractBase-Application Class*/

package org.radixware.ads.PersoComm.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.Channel.AbstractBase_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::PersoComm::Unit.Channel.AbstractBase:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
			"Radix::PersoComm::Unit.Channel.AbstractBase",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGZVXYHF7NFAIHLZDE77ERH6YSE"),null,null,0,

			/*Radix::PersoComm::Unit.Channel.AbstractBase:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::PersoComm::Unit.Channel.AbstractBase:kind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62V3FXUDVTOBDCLTAALOMT5GDM"),
						"kind",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZVCKAEK2VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.AbstractBase:kind:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:addressTemplate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colL3M53M2LBNGE3HZGY7AKTWLDMI"),
						"addressTemplate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5NSOFBS7GRBXFGN37IEKRDUUD4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::PersoComm::Unit.Channel.AbstractBase:addressTemplate:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:subjectTemplate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM5FHLVP23ZBQZJXA3VI647TST4"),
						"subjectTemplate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMOFCSELSZNGCTJAAD7T4TNF65Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::PersoComm::Unit.Channel.AbstractBase:subjectTemplate:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:sendPeriod:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXBZDYDMEVTOBDCLTAALOMT5GDM"),
						"sendPeriod",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYKZ5DIK2VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.AbstractBase:sendPeriod:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:recvPeriod:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXFZDYDMEVTOBDCLTAALOMT5GDM"),
						"recvPeriod",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBWFQQT22VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.AbstractBase:recvPeriod:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:sendAddress:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXJZDYDMEVTOBDCLTAALOMT5GDM"),
						"sendAddress",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKOCJ3B22VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::PersoComm::Unit.Channel.AbstractBase:sendAddress:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,500,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:recvAddress:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXNZDYDMEVTOBDCLTAALOMT5GDM"),
						"recvAddress",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD6UAIN22VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::PersoComm::Unit.Channel.AbstractBase:recvAddress:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,500,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:messAddressRegexp:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXRZDYDMEVTOBDCLTAALOMT5GDM"),
						"messAddressRegexp",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPSVBYHK2VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom(".*"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.AbstractBase:messAddressRegexp:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,500,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:routingPriority:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXVZDYDMEVTOBDCLTAALOMT5GDM"),
						"routingPriority",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEFIE6ZC2VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						28,
						null,
						null,
						null,
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

						/*Radix::PersoComm::Unit.Channel.AbstractBase:routingPriority:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-99L,99L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:encoding:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXZZDYDMEVTOBDCLTAALOMT5GDM"),
						"encoding",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH32KAMUEVTOBDCLTAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsVXR524YMLLOBDB6NAAN7YHKUNI"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("(java.nio.charset.Charset.defaultCharset().name())"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.AbstractBase:encoding:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsVXR524YMLLOBDB6NAAN7YHKUNI"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:handlersSeqs:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdF3SGW4U5M5EJNASVPFBOFXZK7E"),
						"handlersSeqs",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.ARR_INT,
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

						/*Radix::PersoComm::Unit.Channel.AbstractBase:handlersSeqs:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:minImportance:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7HPHZW2DJRHVJEYJSANRF7CASM"),
						"minImportance",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEFYXFATGNRDN7LORT5PAMUGK6U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTUGILCRUVXOBDCLUAALOMT5GDM"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.AbstractBase:minImportance:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTUGILCRUVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:maxImportance:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4TIXFUZ7YNDYPDT5MVYA5XWERA"),
						"maxImportance",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCPW73T3XB5BY3HEW3E3KHABMRQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTUGILCRUVXOBDCLUAALOMT5GDM"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("2"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.AbstractBase:maxImportance:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTUGILCRUVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:sendTimeout:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYYXVCC53YBHT3PZYC2UEGXPGGE"),
						"sendTimeout",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6JMQL5RAUBELDB4YA65PCDVWOU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::PersoComm::Unit.Channel.AbstractBase:sendTimeout:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,120L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:calcPriorityFunc:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZYMZD3ODGNDZZDU2Q746Q65XXE"),
						"calcPriorityFunc",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLJ4LHVR3HRCVBOB3GK5MY2O53U"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						null,
						0L,
						0L,false,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:routingKeyRegexp:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCFB4V6V7VC47GPA5WGBSCKPWA"),
						"routingKeyRegexp",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7ETXLGO36FHIBJ237B3F5F25IQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::PersoComm::Unit.Channel.AbstractBase:routingKeyRegexp:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,500,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFWETB7TG6JCQ5GJBYSVVG7HEH4"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPolicy:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEXIOHPTFYRHDBOLCXVAFJD5EJ4"),
						"deliveryTrackingPolicy",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsG3IGDWS7FBCXHNB3ZED4OW5KI4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsQYGCBO7AWFG7RBTB6B355UVJNY"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("None"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPolicy:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsQYGCBO7AWFG7RBTB6B355UVJNY"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.INCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aciTI3DUPQPNFBBPFGS46RDGA3RI4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aciKEGSGVPWZ5DHVIYD2KWJQNYLVY")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPeriod:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPERH4NC4CVF4DG44EEBXFWHKXM"),
						"deliveryTrackingPeriod",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDJ6NV2YI2BHQLE4LTI3VAGS3JU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPeriod:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,168L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingRetryPeriod:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVBZ4AKNLFVCFJDFAUJXZPZS5ZY"),
						"deliveryTrackingRetryPeriod",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAE33OZQQX5CY5LXM4U3PY3P3L4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingRetryPeriod:PropertyPresentation:Edit Options:-Edit Mask Time Interval*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskTimeInterval(60000L,"hh:mm",1L,720L),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:forwardDelaySec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC7GIBM6CVJDIXB2MCZKYXAAEJI"),
						"forwardDelaySec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLO3SEVIHJZDWHFVC32FYS4C67Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.AbstractBase:forwardDelaySec:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
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
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprGVZBTVH6VXOBDCLVAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr42ALGCTMUZB6BICZW7XRIE5DOA")},
			true,true,false);
}

/* Radix::PersoComm::Unit.Channel.AbstractBase - Web Executable*/

/*Radix::PersoComm::Unit.Channel.AbstractBase-Application Class*/

package org.radixware.ads.PersoComm.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase")
public interface Unit.Channel.AbstractBase   extends org.radixware.ads.System.web.Unit  {















































































































































































































































































	/*Radix::PersoComm::Unit.Channel.AbstractBase:maxImportance:maxImportance-Presentation Property*/


	public class MaxImportance extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxImportance(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:maxImportance:maxImportance")
		public  org.radixware.kernel.common.enums.EPersoCommImportance getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:maxImportance:maxImportance")
		public   void setValue(org.radixware.kernel.common.enums.EPersoCommImportance val) {
			Value = val;
		}
	}
	public MaxImportance getMaxImportance();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:kind:kind-Presentation Property*/


	public class Kind extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Kind(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:kind:kind")
		public  org.radixware.ads.PersoComm.common.ChannelKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:kind:kind")
		public   void setValue(org.radixware.ads.PersoComm.common.ChannelKind val) {
			Value = val;
		}
	}
	public Kind getKind();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:minImportance:minImportance-Presentation Property*/


	public class MinImportance extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MinImportance(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:minImportance:minImportance")
		public  org.radixware.kernel.common.enums.EPersoCommImportance getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:minImportance:minImportance")
		public   void setValue(org.radixware.kernel.common.enums.EPersoCommImportance val) {
			Value = val;
		}
	}
	public MinImportance getMinImportance();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:forwardDelaySec:forwardDelaySec-Presentation Property*/


	public class ForwardDelaySec extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ForwardDelaySec(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:forwardDelaySec:forwardDelaySec")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:forwardDelaySec:forwardDelaySec")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ForwardDelaySec getForwardDelaySec();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:routingKeyRegexp:routingKeyRegexp-Presentation Property*/


	public class RoutingKeyRegexp extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RoutingKeyRegexp(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:routingKeyRegexp:routingKeyRegexp")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:routingKeyRegexp:routingKeyRegexp")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoutingKeyRegexp getRoutingKeyRegexp();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPolicy:deliveryTrackingPolicy-Presentation Property*/


	public class DeliveryTrackingPolicy extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DeliveryTrackingPolicy(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy ? (org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy)x : org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy> getValClass(){
			return org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy ? (org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy)x : org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPolicy:deliveryTrackingPolicy")
		public  org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPolicy:deliveryTrackingPolicy")
		public   void setValue(org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy val) {
			Value = val;
		}
	}
	public DeliveryTrackingPolicy getDeliveryTrackingPolicy();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:addressTemplate:addressTemplate-Presentation Property*/


	public class AddressTemplate extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AddressTemplate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:addressTemplate:addressTemplate")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:addressTemplate:addressTemplate")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AddressTemplate getAddressTemplate();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:subjectTemplate:subjectTemplate-Presentation Property*/


	public class SubjectTemplate extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SubjectTemplate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:subjectTemplate:subjectTemplate")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:subjectTemplate:subjectTemplate")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SubjectTemplate getSubjectTemplate();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPeriod:deliveryTrackingPeriod-Presentation Property*/


	public class DeliveryTrackingPeriod extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public DeliveryTrackingPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPeriod:deliveryTrackingPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPeriod:deliveryTrackingPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DeliveryTrackingPeriod getDeliveryTrackingPeriod();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingRetryPeriod:deliveryTrackingRetryPeriod-Presentation Property*/


	public class DeliveryTrackingRetryPeriod extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public DeliveryTrackingRetryPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingRetryPeriod:deliveryTrackingRetryPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingRetryPeriod:deliveryTrackingRetryPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DeliveryTrackingRetryPeriod getDeliveryTrackingRetryPeriod();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:sendPeriod:sendPeriod-Presentation Property*/


	public class SendPeriod extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SendPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:sendPeriod:sendPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:sendPeriod:sendPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SendPeriod getSendPeriod();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:recvPeriod:recvPeriod-Presentation Property*/


	public class RecvPeriod extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public RecvPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:recvPeriod:recvPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:recvPeriod:recvPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public RecvPeriod getRecvPeriod();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:sendAddress:sendAddress-Presentation Property*/


	public class SendAddress extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SendAddress(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:sendAddress:sendAddress")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:sendAddress:sendAddress")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SendAddress getSendAddress();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:recvAddress:recvAddress-Presentation Property*/


	public class RecvAddress extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RecvAddress(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:recvAddress:recvAddress")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:recvAddress:recvAddress")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RecvAddress getRecvAddress();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:messAddressRegexp:messAddressRegexp-Presentation Property*/


	public class MessAddressRegexp extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public MessAddressRegexp(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:messAddressRegexp:messAddressRegexp")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:messAddressRegexp:messAddressRegexp")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MessAddressRegexp getMessAddressRegexp();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:routingPriority:routingPriority-Presentation Property*/


	public class RoutingPriority extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public RoutingPriority(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:routingPriority:routingPriority")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:routingPriority:routingPriority")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public RoutingPriority getRoutingPriority();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:encoding:encoding-Presentation Property*/


	public class Encoding extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Encoding(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Common.common.Encoding dummy = x == null ? null : (x instanceof org.radixware.ads.Common.common.Encoding ? (org.radixware.ads.Common.common.Encoding)x : org.radixware.ads.Common.common.Encoding.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Common.common.Encoding> getValClass(){
			return org.radixware.ads.Common.common.Encoding.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Common.common.Encoding dummy = x == null ? null : (x instanceof org.radixware.ads.Common.common.Encoding ? (org.radixware.ads.Common.common.Encoding)x : org.radixware.ads.Common.common.Encoding.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:encoding:encoding")
		public  org.radixware.ads.Common.common.Encoding getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:encoding:encoding")
		public   void setValue(org.radixware.ads.Common.common.Encoding val) {
			Value = val;
		}
	}
	public Encoding getEncoding();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:sendTimeout:sendTimeout-Presentation Property*/


	public class SendTimeout extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SendTimeout(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:sendTimeout:sendTimeout")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:sendTimeout:sendTimeout")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SendTimeout getSendTimeout();
	/*Radix::PersoComm::Unit.Channel.AbstractBase:handlersSeqs:handlersSeqs-Presentation Property*/


	public class HandlersSeqs extends org.radixware.kernel.common.client.models.items.properties.PropertyArrInt{
		public HandlersSeqs(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.ArrInt dummy = ((org.radixware.kernel.common.types.ArrInt)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:handlersSeqs:handlersSeqs")
		public  org.radixware.kernel.common.types.ArrInt getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:handlersSeqs:handlersSeqs")
		public   void setValue(org.radixware.kernel.common.types.ArrInt val) {
			Value = val;
		}
	}
	public HandlersSeqs getHandlersSeqs();


}

/* Radix::PersoComm::Unit.Channel.AbstractBase - Web Meta*/

/*Radix::PersoComm::Unit.Channel.AbstractBase-Application Class*/

package org.radixware.ads.PersoComm.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.Channel.AbstractBase_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::PersoComm::Unit.Channel.AbstractBase:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
			"Radix::PersoComm::Unit.Channel.AbstractBase",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGZVXYHF7NFAIHLZDE77ERH6YSE"),null,null,0,

			/*Radix::PersoComm::Unit.Channel.AbstractBase:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::PersoComm::Unit.Channel.AbstractBase:kind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62V3FXUDVTOBDCLTAALOMT5GDM"),
						"kind",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZVCKAEK2VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.AbstractBase:kind:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:addressTemplate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colL3M53M2LBNGE3HZGY7AKTWLDMI"),
						"addressTemplate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5NSOFBS7GRBXFGN37IEKRDUUD4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::PersoComm::Unit.Channel.AbstractBase:addressTemplate:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:subjectTemplate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM5FHLVP23ZBQZJXA3VI647TST4"),
						"subjectTemplate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMOFCSELSZNGCTJAAD7T4TNF65Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::PersoComm::Unit.Channel.AbstractBase:subjectTemplate:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:sendPeriod:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXBZDYDMEVTOBDCLTAALOMT5GDM"),
						"sendPeriod",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYKZ5DIK2VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.AbstractBase:sendPeriod:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:recvPeriod:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXFZDYDMEVTOBDCLTAALOMT5GDM"),
						"recvPeriod",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBWFQQT22VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.AbstractBase:recvPeriod:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:sendAddress:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXJZDYDMEVTOBDCLTAALOMT5GDM"),
						"sendAddress",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKOCJ3B22VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::PersoComm::Unit.Channel.AbstractBase:sendAddress:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,500,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:recvAddress:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXNZDYDMEVTOBDCLTAALOMT5GDM"),
						"recvAddress",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD6UAIN22VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::PersoComm::Unit.Channel.AbstractBase:recvAddress:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,500,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:messAddressRegexp:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXRZDYDMEVTOBDCLTAALOMT5GDM"),
						"messAddressRegexp",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPSVBYHK2VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom(".*"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.AbstractBase:messAddressRegexp:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,500,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:routingPriority:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXVZDYDMEVTOBDCLTAALOMT5GDM"),
						"routingPriority",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEFIE6ZC2VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						28,
						null,
						null,
						null,
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

						/*Radix::PersoComm::Unit.Channel.AbstractBase:routingPriority:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-99L,99L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:encoding:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXZZDYDMEVTOBDCLTAALOMT5GDM"),
						"encoding",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH32KAMUEVTOBDCLTAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsVXR524YMLLOBDB6NAAN7YHKUNI"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("(java.nio.charset.Charset.defaultCharset().name())"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.AbstractBase:encoding:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsVXR524YMLLOBDB6NAAN7YHKUNI"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:handlersSeqs:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdF3SGW4U5M5EJNASVPFBOFXZK7E"),
						"handlersSeqs",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.ARR_INT,
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

						/*Radix::PersoComm::Unit.Channel.AbstractBase:handlersSeqs:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:minImportance:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7HPHZW2DJRHVJEYJSANRF7CASM"),
						"minImportance",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEFYXFATGNRDN7LORT5PAMUGK6U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTUGILCRUVXOBDCLUAALOMT5GDM"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.AbstractBase:minImportance:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTUGILCRUVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:maxImportance:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4TIXFUZ7YNDYPDT5MVYA5XWERA"),
						"maxImportance",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCPW73T3XB5BY3HEW3E3KHABMRQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTUGILCRUVXOBDCLUAALOMT5GDM"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("2"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.AbstractBase:maxImportance:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTUGILCRUVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:sendTimeout:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYYXVCC53YBHT3PZYC2UEGXPGGE"),
						"sendTimeout",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6JMQL5RAUBELDB4YA65PCDVWOU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::PersoComm::Unit.Channel.AbstractBase:sendTimeout:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,120L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:calcPriorityFunc:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZYMZD3ODGNDZZDU2Q746Q65XXE"),
						"calcPriorityFunc",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclLJ4LHVR3HRCVBOB3GK5MY2O53U"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						null,
						null,
						null,
						0L,
						0L,false,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:routingKeyRegexp:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCFB4V6V7VC47GPA5WGBSCKPWA"),
						"routingKeyRegexp",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7ETXLGO36FHIBJ237B3F5F25IQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::PersoComm::Unit.Channel.AbstractBase:routingKeyRegexp:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,500,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFWETB7TG6JCQ5GJBYSVVG7HEH4"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPolicy:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEXIOHPTFYRHDBOLCXVAFJD5EJ4"),
						"deliveryTrackingPolicy",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsG3IGDWS7FBCXHNB3ZED4OW5KI4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsQYGCBO7AWFG7RBTB6B355UVJNY"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("None"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPolicy:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsQYGCBO7AWFG7RBTB6B355UVJNY"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.INCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aciTI3DUPQPNFBBPFGS46RDGA3RI4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aciKEGSGVPWZ5DHVIYD2KWJQNYLVY")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPeriod:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPERH4NC4CVF4DG44EEBXFWHKXM"),
						"deliveryTrackingPeriod",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDJ6NV2YI2BHQLE4LTI3VAGS3JU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingPeriod:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,168L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingRetryPeriod:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVBZ4AKNLFVCFJDFAUJXZPZS5ZY"),
						"deliveryTrackingRetryPeriod",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAE33OZQQX5CY5LXM4U3PY3P3L4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::PersoComm::Unit.Channel.AbstractBase:deliveryTrackingRetryPeriod:PropertyPresentation:Edit Options:-Edit Mask Time Interval*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskTimeInterval(60000L,"hh:mm",1L,720L),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::Unit.Channel.AbstractBase:forwardDelaySec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC7GIBM6CVJDIXB2MCZKYXAAEJI"),
						"forwardDelaySec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLO3SEVIHJZDWHFVC32FYS4C67Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::Unit.Channel.AbstractBase:forwardDelaySec:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
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
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("spr42ALGCTMUZB6BICZW7XRIE5DOA")},
			true,true,false);
}

/* Radix::PersoComm::Unit.Channel.AbstractBase:Edit - Desktop Meta*/

/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit-Editor Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprGVZBTVH6VXOBDCLVAALOMT5GDM"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	null,
	null,

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Incoming Message Handlers-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg2YV3PH6P3PORDOFEABIFNQAABA"),"Incoming Message Handlers",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZIO3XJOP3PORDOFEABIFNQAABA"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiAIGOSGOM3PORDOFEABIFNQAABA")),

			/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Channel-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgRTEJ2K77VXOBDCLVAALOMT5GDM"),"Channel",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRXEJ2K77VXOBDCLVAALOMT5GDM"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62V3FXUDVTOBDCLTAALOMT5GDM"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXVZDYDMEVTOBDCLTAALOMT5GDM"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXBZDYDMEVTOBDCLTAALOMT5GDM"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXFZDYDMEVTOBDCLTAALOMT5GDM"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXNZDYDMEVTOBDCLTAALOMT5GDM"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXJZDYDMEVTOBDCLTAALOMT5GDM"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXZZDYDMEVTOBDCLTAALOMT5GDM"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXRZDYDMEVTOBDCLTAALOMT5GDM"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7HPHZW2DJRHVJEYJSANRF7CASM"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4TIXFUZ7YNDYPDT5MVYA5XWERA"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYYXVCC53YBHT3PZYC2UEGXPGGE"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZYMZD3ODGNDZZDU2Q746Q65XXE"),0,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCFB4V6V7VC47GPA5WGBSCKPWA"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEXIOHPTFYRHDBOLCXVAFJD5EJ4"),0,13,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPERH4NC4CVF4DG44EEBXFWHKXM"),0,14,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVBZ4AKNLFVCFJDFAUJXZPZS5ZY"),0,15,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC7GIBM6CVJDIXB2MCZKYXAAEJI"),0,16,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOCDVZAOTBXOBDNTPAAMPGXSZKU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgRTEJ2K77VXOBDCLVAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg2YV3PH6P3PORDOFEABIFNQAABA")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUJB2RCH6KZHD5IMTQTE76DQSZA")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgJ4BHY7HBLBBBXIIQZEPCKTW2MU"))}
	,

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:ChannelHandler-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiAIGOSGOM3PORDOFEABIFNQAABA"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecYDGQACJVVLOBDCLSAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZ2IX76OO3PORDOFEABIFNQAABA"),
					32,
					null,
					16560,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	3216,0,0);
}
/* Radix::PersoComm::Unit.Channel.AbstractBase:Edit - Web Meta*/

/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit-Editor Presentation*/

package org.radixware.ads.PersoComm.web;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprGVZBTVH6VXOBDCLVAALOMT5GDM"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	null,
	null,

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Editor Pages-Editor Presentation Pages*/
	null,
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
	}
	,

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	3216,0,0);
}
/* Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model - Desktop Executable*/

/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model")
public class Edit:Model  extends org.radixware.ads.PersoComm.explorer.Unit.Channel.AbstractBase.Unit.Channel.AbstractBase_DefaultModel.eprXCFCDDGTRHNRDB5MAALOMT5GDM_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:Properties-Properties*/

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:minImportance-Presentation Property*/




	public class MinImportance extends org.radixware.ads.PersoComm.explorer.Unit.Channel.AbstractBase.Unit.Channel.AbstractBase_DefaultModel.eprXCFCDDGTRHNRDB5MAALOMT5GDM_ModelAdapter.col7HPHZW2DJRHVJEYJSANRF7CASM{
		public MinImportance(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:minImportance")
		public  org.radixware.kernel.common.enums.EPersoCommImportance getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:minImportance")
		public   void setValue(org.radixware.kernel.common.enums.EPersoCommImportance val) {

			internal[minImportance] = val;
			if (val.Value.compareTo(maxImportance.Value.Value) > 0) {
			    maxImportance.Value = val;
			}
		}
	}
	public MinImportance getMinImportance(){return (MinImportance)getProperty(col7HPHZW2DJRHVJEYJSANRF7CASM);}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:maxImportance-Presentation Property*/




	public class MaxImportance extends org.radixware.ads.PersoComm.explorer.Unit.Channel.AbstractBase.Unit.Channel.AbstractBase_DefaultModel.eprXCFCDDGTRHNRDB5MAALOMT5GDM_ModelAdapter.col4TIXFUZ7YNDYPDT5MVYA5XWERA{
		public MaxImportance(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:maxImportance")
		public  org.radixware.kernel.common.enums.EPersoCommImportance getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:maxImportance")
		public   void setValue(org.radixware.kernel.common.enums.EPersoCommImportance val) {

			internal[maxImportance] = val;
			if (val.Value.compareTo(minImportance.Value.Value) < 0) {
			    minImportance.Value = val;
			}
		}
	}
	public MaxImportance getMaxImportance(){return (MaxImportance)getProperty(col4TIXFUZ7YNDYPDT5MVYA5XWERA);}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:deliveryTrackingPolicy-Presentation Property*/




	public class DeliveryTrackingPolicy extends org.radixware.ads.PersoComm.explorer.Unit.Channel.AbstractBase.Unit.Channel.AbstractBase_DefaultModel.eprXCFCDDGTRHNRDB5MAALOMT5GDM_ModelAdapter.colEXIOHPTFYRHDBOLCXVAFJD5EJ4{
		public DeliveryTrackingPolicy(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy ? (org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy)x : org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy> getValClass(){
			return org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy ? (org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy)x : org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:deliveryTrackingPolicy")
		public  org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:deliveryTrackingPolicy")
		public   void setValue(org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy val) {

			internal[deliveryTrackingPolicy] = val;
			setDeliveryTrackingFieldsPresentationProperties();

		}
	}
	public DeliveryTrackingPolicy getDeliveryTrackingPolicy(){return (DeliveryTrackingPolicy)getProperty(colEXIOHPTFYRHDBOLCXVAFJD5EJ4);}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:deliveryTrackingPeriod-Presentation Property*/




	public class DeliveryTrackingPeriod extends org.radixware.ads.PersoComm.explorer.Unit.Channel.AbstractBase.Unit.Channel.AbstractBase_DefaultModel.eprXCFCDDGTRHNRDB5MAALOMT5GDM_ModelAdapter.colPERH4NC4CVF4DG44EEBXFWHKXM{
		public DeliveryTrackingPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:deliveryTrackingPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:deliveryTrackingPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DeliveryTrackingPeriod getDeliveryTrackingPeriod(){return (DeliveryTrackingPeriod)getProperty(colPERH4NC4CVF4DG44EEBXFWHKXM);}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:deliveryTrackingRetryPeriod-Presentation Property*/




	public class DeliveryTrackingRetryPeriod extends org.radixware.ads.PersoComm.explorer.Unit.Channel.AbstractBase.Unit.Channel.AbstractBase_DefaultModel.eprXCFCDDGTRHNRDB5MAALOMT5GDM_ModelAdapter.colVBZ4AKNLFVCFJDFAUJXZPZS5ZY{
		public DeliveryTrackingRetryPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:deliveryTrackingRetryPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:deliveryTrackingRetryPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DeliveryTrackingRetryPeriod getDeliveryTrackingRetryPeriod(){return (DeliveryTrackingRetryPeriod)getProperty(colVBZ4AKNLFVCFJDFAUJXZPZS5ZY);}
















	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:Methods-Methods*/

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:setVisibility-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:setVisibility")
	protected published  void setVisibility () {
		if (!isNew()) {
		    routingPriority.setVisible(calcPriorityFunc.Value == null);
		}

		final boolean sendTimeoutSupported = this.type.Value == System::UnitType:DPC_SMTP || this.type.Value == System::UnitType:DPC_SMSviaSMTP;
		sendTimeout.setVisible(sendTimeoutSupported);

	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:afterRead")
	protected  void afterRead () {
		super.afterRead();
		if (!isInSelectorRowContext()) {
		    setVisibility();
		    setDeliveryTrackingFieldsPresentationProperties();
		}
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:afterChangePropertyObject-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:afterChangePropertyObject")
	public published  void afterChangePropertyObject (org.radixware.kernel.common.client.models.items.properties.PropertyObject property) {
		if (!isInSelectorRowContext()) {
		    setVisibility();
		}
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:setDeliveryTrackingFieldsPresentationProperties-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:setDeliveryTrackingFieldsPresentationProperties")
	protected published  void setDeliveryTrackingFieldsPresentationProperties () {
		final boolean periodEnabled = deliveryTrackingPolicy.Value != DeliveryTrackingPolicy:NONE;
		final boolean retryPeriodEnabled = deliveryTrackingPolicy.Value == DeliveryTrackingPolicy:QUERY;

		deliveryTrackingPeriod.setEnabled(periodEnabled);
		deliveryTrackingPeriod.setMandatory(periodEnabled);
		deliveryTrackingRetryPeriod.setEnabled(retryPeriodEnabled);
		deliveryTrackingRetryPeriod.setMandatory(retryPeriodEnabled);
	}


}

/* Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model - Desktop Meta*/

/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemGVZBTVH6VXOBDCLVAALOMT5GDM"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemXCFCDDGTRHNRDB5MAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:Properties-Properties*/
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

/* Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model - Web Executable*/

/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model")
public class Edit:Model  extends org.radixware.ads.PersoComm.web.Unit.Channel.AbstractBase.Unit.Channel.AbstractBase_DefaultModel.eprXCFCDDGTRHNRDB5MAALOMT5GDM_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:Properties-Properties*/

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:minImportance-Presentation Property*/




	public class MinImportance extends org.radixware.ads.PersoComm.web.Unit.Channel.AbstractBase.Unit.Channel.AbstractBase_DefaultModel.eprXCFCDDGTRHNRDB5MAALOMT5GDM_ModelAdapter.col7HPHZW2DJRHVJEYJSANRF7CASM{
		public MinImportance(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:minImportance")
		public  org.radixware.kernel.common.enums.EPersoCommImportance getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:minImportance")
		public   void setValue(org.radixware.kernel.common.enums.EPersoCommImportance val) {

			internal[minImportance] = val;
			if (val.Value.compareTo(maxImportance.Value.Value) > 0) {
			    maxImportance.Value = val;
			}
		}
	}
	public MinImportance getMinImportance(){return (MinImportance)getProperty(col7HPHZW2DJRHVJEYJSANRF7CASM);}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:maxImportance-Presentation Property*/




	public class MaxImportance extends org.radixware.ads.PersoComm.web.Unit.Channel.AbstractBase.Unit.Channel.AbstractBase_DefaultModel.eprXCFCDDGTRHNRDB5MAALOMT5GDM_ModelAdapter.col4TIXFUZ7YNDYPDT5MVYA5XWERA{
		public MaxImportance(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:maxImportance")
		public  org.radixware.kernel.common.enums.EPersoCommImportance getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:maxImportance")
		public   void setValue(org.radixware.kernel.common.enums.EPersoCommImportance val) {

			internal[maxImportance] = val;
			if (val.Value.compareTo(minImportance.Value.Value) < 0) {
			    minImportance.Value = val;
			}
		}
	}
	public MaxImportance getMaxImportance(){return (MaxImportance)getProperty(col4TIXFUZ7YNDYPDT5MVYA5XWERA);}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:deliveryTrackingPolicy-Presentation Property*/




	public class DeliveryTrackingPolicy extends org.radixware.ads.PersoComm.web.Unit.Channel.AbstractBase.Unit.Channel.AbstractBase_DefaultModel.eprXCFCDDGTRHNRDB5MAALOMT5GDM_ModelAdapter.colEXIOHPTFYRHDBOLCXVAFJD5EJ4{
		public DeliveryTrackingPolicy(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy ? (org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy)x : org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy> getValClass(){
			return org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy ? (org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy)x : org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:deliveryTrackingPolicy")
		public  org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:deliveryTrackingPolicy")
		public   void setValue(org.radixware.kernel.common.enums.EPersoCommDeliveryTrackingPolicy val) {

			internal[deliveryTrackingPolicy] = val;
			setDeliveryTrackingFieldsPresentationProperties();

		}
	}
	public DeliveryTrackingPolicy getDeliveryTrackingPolicy(){return (DeliveryTrackingPolicy)getProperty(colEXIOHPTFYRHDBOLCXVAFJD5EJ4);}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:deliveryTrackingPeriod-Presentation Property*/




	public class DeliveryTrackingPeriod extends org.radixware.ads.PersoComm.web.Unit.Channel.AbstractBase.Unit.Channel.AbstractBase_DefaultModel.eprXCFCDDGTRHNRDB5MAALOMT5GDM_ModelAdapter.colPERH4NC4CVF4DG44EEBXFWHKXM{
		public DeliveryTrackingPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:deliveryTrackingPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:deliveryTrackingPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DeliveryTrackingPeriod getDeliveryTrackingPeriod(){return (DeliveryTrackingPeriod)getProperty(colPERH4NC4CVF4DG44EEBXFWHKXM);}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:deliveryTrackingRetryPeriod-Presentation Property*/




	public class DeliveryTrackingRetryPeriod extends org.radixware.ads.PersoComm.web.Unit.Channel.AbstractBase.Unit.Channel.AbstractBase_DefaultModel.eprXCFCDDGTRHNRDB5MAALOMT5GDM_ModelAdapter.colVBZ4AKNLFVCFJDFAUJXZPZS5ZY{
		public DeliveryTrackingRetryPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:deliveryTrackingRetryPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:deliveryTrackingRetryPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DeliveryTrackingRetryPeriod getDeliveryTrackingRetryPeriod(){return (DeliveryTrackingRetryPeriod)getProperty(colVBZ4AKNLFVCFJDFAUJXZPZS5ZY);}
















	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:Methods-Methods*/

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:setVisibility-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:setVisibility")
	protected published  void setVisibility () {
		//todo: hide routingPriority when calcPriorityFunction is defined
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:afterRead")
	protected  void afterRead () {
		super.afterRead();
		if (!isInSelectorRowContext()) {
		    setVisibility();
		    setDeliveryTrackingFieldsPresentationProperties();
		}
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:afterChangePropertyObject-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:afterChangePropertyObject")
	public published  void afterChangePropertyObject (org.radixware.kernel.common.client.models.items.properties.PropertyObject property) {
		if (!isInSelectorRowContext()) {
		    setVisibility();
		}
	}

	/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:setDeliveryTrackingFieldsPresentationProperties-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:setDeliveryTrackingFieldsPresentationProperties")
	protected published  void setDeliveryTrackingFieldsPresentationProperties () {
		//todo: hide routingPriority when calcPriorityFunction is defined
	}


}

/* Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model - Web Meta*/

/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemGVZBTVH6VXOBDCLVAALOMT5GDM"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemXCFCDDGTRHNRDB5MAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::PersoComm::Unit.Channel.AbstractBase:Edit:Model:Properties-Properties*/
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

/* Radix::PersoComm::Unit.Channel.AbstractBase:General - Desktop Meta*/

/*Radix::PersoComm::Unit.Channel.AbstractBase:General-Selector Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr42ALGCTMUZB6BICZW7XRIE5DOA"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6AFPZS6TRHNRDB5MAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ4EGWUQZ5RBCJFL5GWHBF66H6U"),
		null,
		null,
		null,
		false,
		null,
		null,
		false,
		false,
		null,
		0,null,
		16506,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprS6PW3X2EKJFUNPGCXYDBACXY7A")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},
		false,true,false,0,0);
		columns = null;;
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.explorer.Unit.Channel.AbstractBase.DefaultGroupModel(userSession,this);
	}
}
/* Radix::PersoComm::Unit.Channel.AbstractBase:General - Web Meta*/

/*Radix::PersoComm::Unit.Channel.AbstractBase:General-Selector Presentation*/

package org.radixware.ads.PersoComm.web;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr42ALGCTMUZB6BICZW7XRIE5DOA"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6AFPZS6TRHNRDB5MAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ4EGWUQZ5RBCJFL5GWHBF66H6U"),
		null,
		null,
		null,
		false,
		null,
		null,
		false,
		false,
		null,
		0,null,
		16506,
		null,
		null,
		false,true,false,0,0);
		columns = null;;
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.web.Unit.Channel.AbstractBase.DefaultGroupModel(userSession,this);
	}
}
/* Radix::PersoComm::Unit.Channel.AbstractBase - Localizing Bundle */
package org.radixware.ads.PersoComm.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.Channel.AbstractBase - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Шаблон адреса");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Address template");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5NSOFBS7GRBXFGN37IEKRDUUD4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тайм-аут отправки (с)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Send timeout (s)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6JMQL5RAUBELDB4YA65PCDVWOU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Маска ключа маршрутизации");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Routing key mask");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7ETXLGO36FHIBJ237B3F5F25IQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Период попыток отслеживания доставки (минут)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Delivery tracking retry period (minutes)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAE33OZQQX5CY5LXM4U3PY3P3L4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Период приема (с)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Receiving period (s)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBWFQQT22VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Максимальная важность");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maximum importance");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCPW73T3XB5BY3HEW3E3KHABMRQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Адрес отправителя");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Source address");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD6UAIN22VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Период отслеживания доставки (часов)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Delivery tracking period (hours)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDJ6NV2YI2BHQLE4LTI3VAGS3JU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приоритет маршрутизации");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Routing priority");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEFIE6ZC2VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Минимальная важность");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Minimum importance");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEFYXFATGNRDN7LORT5PAMUGK6U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Получает обработчики для текущего канала.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the handlers for the current channel.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFQ3X5QLS3JGRZOKJKTA674EHNA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<ключ не задан>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<key is not defined>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFWETB7TG6JCQ5GJBYSVVG7HEH4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отслеживание доставки");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Delivery tracking");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsG3IGDWS7FBCXHNB3ZED4OW5KI4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGZVXYHF7NFAIHLZDE77ERH6YSE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Кодировка");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Encoding");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH32KAMUEVTOBDCLTAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Период (в секундах) между попытками отследить доставку для активных вариантов отслеживания\nFYI: хранится в секундах, отображается в минутах");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Period (seconds) between delivery tracking retries for active tracking variants\nFYI: stored in seconds, displayed in minutes\n");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHX2CPXVFCVFX7BN3WS3XBUE7PQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Адрес шлюза");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gateway address");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKOCJ3B22VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задержка перенаправления (секунд)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Forward delay (sec)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLO3SEVIHJZDWHFVC32FYS4C67Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Шаблон темы");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Subject template");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMOFCSELSZNGCTJAAD7T4TNF65Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Маска адреса");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Address mask");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPSVBYHK2VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Каналы");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Channels");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ4EGWUQZ5RBCJFL5GWHBF66H6U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Период отслеживания доставки (в секундах)\nFYI: хранится в секундах, отображается в минутах");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Delivery tracking period (seconds)\nFYI: stored in seconds, displayed in minutes");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQDMHQOWEMREQZP2KQC4AQBBCGQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Все обработчики канала.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"All handlers of the channel.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRK7ZA5NWYBGEXPUYANUCWXQDSY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Канал");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Channel");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRXEJ2K77VXOBDCLVAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Частота передачи (с)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Sending period (s)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYKZ5DIK2VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчики входящих сообщений");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Inbound Message Handlers");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZIO3XJOP3PORDOFEABIFNQAABA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс, который реализует абстрактную основу для  модулей системы, которые являются каналами передачи сообщений.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class implements the abstract base for the system units used as message delivery channels.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZIWRUAICSNGUBIUETHK7ZS5ASE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вид канала");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Channel type");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZVCKAEK2VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Unit.Channel.AbstractBase - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclVCLH3TEDVTOBDCLTAALOMT5GDM"),"Unit.Channel.AbstractBase - Localizing Bundle",$$$items$$$);
}
