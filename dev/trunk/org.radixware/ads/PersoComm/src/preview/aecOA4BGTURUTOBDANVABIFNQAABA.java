
/* Radix::PersoComm::SentMessage - Server Executable*/

/*Radix::PersoComm::SentMessage-Entity Class*/

package org.radixware.ads.PersoComm.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage")
public final published class SentMessage  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return SentMessage_mi.rdxMeta;}

	/*Radix::PersoComm::SentMessage:Nested classes-Nested Classes*/

	/*Radix::PersoComm::SentMessage:Properties-Properties*/

	/*Radix::PersoComm::SentMessage:destEntityGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:destEntityGuid")
	public published  Str getDestEntityGuid() {
		return destEntityGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:destEntityGuid")
	public published   void setDestEntityGuid(Str val) {
		destEntityGuid = val;
	}

	/*Radix::PersoComm::SentMessage:sendError-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sendError")
	public published  Str getSendError() {
		return sendError;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sendError")
	public published   void setSendError(Str val) {
		sendError = val;
	}

	/*Radix::PersoComm::SentMessage:sentTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sentTime")
	public published  java.sql.Timestamp getSentTime() {
		return sentTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sentTime")
	public published   void setSentTime(java.sql.Timestamp val) {
		sentTime = val;
	}

	/*Radix::PersoComm::SentMessage:destPid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:destPid")
	public published  Str getDestPid() {
		return destPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:destPid")
	public published   void setDestPid(Str val) {
		destPid = val;
	}

	/*Radix::PersoComm::SentMessage:dueTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:dueTime")
	public published  java.sql.Timestamp getDueTime() {
		return dueTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:dueTime")
	public published   void setDueTime(java.sql.Timestamp val) {
		dueTime = val;
	}

	/*Radix::PersoComm::SentMessage:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:id")
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:id")
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::PersoComm::SentMessage:subject-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:subject")
	public published  Str getSubject() {
		return subject;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:subject")
	public published   void setSubject(Str val) {
		subject = val;
	}

	/*Radix::PersoComm::SentMessage:body-Column-Based Property*/




















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:body")
	public published  Str getBody() {
		return body;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:body")
	public published   void setBody(Str val) {
		body = val;
	}

	/*Radix::PersoComm::SentMessage:importance-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:importance")
	public published  org.radixware.kernel.common.enums.EPersoCommImportance getImportance() {
		return importance;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:importance")
	public published   void setImportance(org.radixware.kernel.common.enums.EPersoCommImportance val) {
		importance = val;
	}

	/*Radix::PersoComm::SentMessage:createTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:createTime")
	public published  java.sql.Timestamp getCreateTime() {
		return createTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:createTime")
	public published   void setCreateTime(java.sql.Timestamp val) {
		createTime = val;
	}

	/*Radix::PersoComm::SentMessage:sendCallbackMethodName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sendCallbackMethodName")
	public published  Str getSendCallbackMethodName() {
		return sendCallbackMethodName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sendCallbackMethodName")
	public published   void setSendCallbackMethodName(Str val) {
		sendCallbackMethodName = val;
	}

	/*Radix::PersoComm::SentMessage:expireTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:expireTime")
	public published  java.sql.Timestamp getExpireTime() {
		return expireTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:expireTime")
	public published   void setExpireTime(java.sql.Timestamp val) {
		expireTime = val;
	}

	/*Radix::PersoComm::SentMessage:channelKind-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:channelKind")
	public published  org.radixware.ads.PersoComm.common.ChannelKind getChannelKind() {
		return channelKind;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:channelKind")
	public published   void setChannelKind(org.radixware.ads.PersoComm.common.ChannelKind val) {
		channelKind = val;
	}

	/*Radix::PersoComm::SentMessage:address-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:address")
	public published  Str getAddress() {
		return address;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:address")
	public published   void setAddress(Str val) {
		address = val;
	}

	/*Radix::PersoComm::SentMessage:sourceEntityGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sourceEntityGuid")
	public published  Str getSourceEntityGuid() {
		return sourceEntityGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sourceEntityGuid")
	public published   void setSourceEntityGuid(Str val) {
		sourceEntityGuid = val;
	}

	/*Radix::PersoComm::SentMessage:sourcePid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sourcePid")
	public published  Str getSourcePid() {
		return sourcePid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sourcePid")
	public published   void setSourcePid(Str val) {
		sourcePid = val;
	}

	/*Radix::PersoComm::SentMessage:sourceMsgId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sourceMsgId")
	public published  Str getSourceMsgId() {
		return sourceMsgId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sourceMsgId")
	public published   void setSourceMsgId(Str val) {
		sourceMsgId = val;
	}

	/*Radix::PersoComm::SentMessage:sendCallbackClassName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sendCallbackClassName")
	public published  Str getSendCallbackClassName() {
		return sendCallbackClassName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sendCallbackClassName")
	public published   void setSendCallbackClassName(Str val) {
		sendCallbackClassName = val;
	}

	/*Radix::PersoComm::SentMessage:channelId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:channelId")
	public published  Int getChannelId() {
		return channelId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:channelId")
	public published   void setChannelId(Int val) {
		channelId = val;
	}

	/*Radix::PersoComm::SentMessage:histMode-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:histMode")
	public published  org.radixware.kernel.common.enums.EPersoCommHistMode getHistMode() {
		return histMode;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:histMode")
	public published   void setHistMode(org.radixware.kernel.common.enums.EPersoCommHistMode val) {
		histMode = val;
	}

	/*Radix::PersoComm::SentMessage:channelUnit-Dynamic Property*/



	protected org.radixware.ads.PersoComm.server.Unit.Channel.AbstractBase channelUnit=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:channelUnit")
	public published  org.radixware.ads.PersoComm.server.Unit.Channel.AbstractBase getChannelUnit() {

		if(internal[channelUnit] == null && channelId != null)
		    internal[channelUnit] = (Unit.Channel.AbstractBase)Unit.Channel.AbstractBase.loadByPK(channelId, true);
		return internal[channelUnit];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:channelUnit")
	public published   void setChannelUnit(org.radixware.ads.PersoComm.server.Unit.Channel.AbstractBase val) {
		channelUnit = val;
	}

	/*Radix::PersoComm::SentMessage:sourceTitle-Dynamic Property*/



	protected Str sourceTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sourceTitle")
	public published  Str getSourceTitle() {

		if(internal[sourceTitle] == null){
		    try{
		        internal[sourceTitle] = Utils.getSourceTitle(sourceEntityGuid, sourcePid);
		    }catch(Exceptions::Error e){
		        internal[sourceTitle] = e.getMessage();
		    }
		}

		return internal[sourceTitle];
	}

	/*Radix::PersoComm::SentMessage:encoding-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:encoding")
	public published  org.radixware.kernel.common.enums.ESmppEncoding getEncoding() {
		return encoding;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:encoding")
	public published   void setEncoding(org.radixware.kernel.common.enums.ESmppEncoding val) {
		encoding = val;
	}

	/*Radix::PersoComm::SentMessage:smppBytesSent-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppBytesSent")
	public published  Int getSmppBytesSent() {
		return smppBytesSent;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppBytesSent")
	public published   void setSmppBytesSent(Int val) {
		smppBytesSent = val;
	}

	/*Radix::PersoComm::SentMessage:smppCharsSent-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppCharsSent")
	public published  Int getSmppCharsSent() {
		return smppCharsSent;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppCharsSent")
	public published   void setSmppCharsSent(Int val) {
		smppCharsSent = val;
	}

	/*Radix::PersoComm::SentMessage:smppPartsSent-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppPartsSent")
	public published  Int getSmppPartsSent() {
		return smppPartsSent;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppPartsSent")
	public published   void setSmppPartsSent(Int val) {
		smppPartsSent = val;
	}

	/*Radix::PersoComm::SentMessage:maskedBody-Column-Based Property*/




















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:maskedBody")
	public published  Str getMaskedBody() {
		return maskedBody;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:maskedBody")
	public published   void setMaskedBody(Str val) {
		maskedBody = val;
	}

	/*Radix::PersoComm::SentMessage:displayedBody-Dynamic Property*/



	protected Str displayedBody=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:displayedBody")
	public published  Str getDisplayedBody() {

		if (maskedBody != null) {
		    return maskedBody;
		} else {
		    return body;
		}
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:displayedBody")
	public published   void setDisplayedBody(Str val) {
		displayedBody = val;
	}

	/*Radix::PersoComm::SentMessage:routingKey-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:routingKey")
	public published  Str getRoutingKey() {
		return routingKey;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:routingKey")
	public published   void setRoutingKey(Str val) {
		routingKey = val;
	}

	/*Radix::PersoComm::SentMessage:storeAttachInHist-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:storeAttachInHist")
	public published  Bool getStoreAttachInHist() {
		return storeAttachInHist;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:storeAttachInHist")
	private published   void setStoreAttachInHist(Bool val) {
		storeAttachInHist = val;
	}

	/*Radix::PersoComm::SentMessage:deliveryStatus-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryStatus")
	public published  org.radixware.kernel.common.enums.EPersoCommDeliveryStatus getDeliveryStatus() {

		return internal[deliveryStatus] == null ? DeliveryStatus:UNKNOWN : internal[deliveryStatus];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryStatus")
	public published   void setDeliveryStatus(org.radixware.kernel.common.enums.EPersoCommDeliveryStatus val) {
		deliveryStatus = val;
	}

	/*Radix::PersoComm::SentMessage:lastDeliveryStatusChangeDate-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:lastDeliveryStatusChangeDate")
	public published  java.sql.Timestamp getLastDeliveryStatusChangeDate() {
		return lastDeliveryStatusChangeDate;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:lastDeliveryStatusChangeDate")
	public published   void setLastDeliveryStatusChangeDate(java.sql.Timestamp val) {
		lastDeliveryStatusChangeDate = val;
	}

	/*Radix::PersoComm::SentMessage:smppMessageId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppMessageId")
	public published  Str getSmppMessageId() {
		return smppMessageId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppMessageId")
	public published   void setSmppMessageId(Str val) {
		smppMessageId = val;
	}

	/*Radix::PersoComm::SentMessage:isUssd-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:isUssd")
	public published  Bool getIsUssd() {
		return isUssd;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:isUssd")
	public published   void setIsUssd(Bool val) {
		isUssd = val;
	}

	/*Radix::PersoComm::SentMessage:ussdServiceOp-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:ussdServiceOp")
	public published  Int getUssdServiceOp() {
		return ussdServiceOp;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:ussdServiceOp")
	public published   void setUssdServiceOp(Int val) {
		ussdServiceOp = val;
	}

	/*Radix::PersoComm::SentMessage:sendCallbackRequired-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sendCallbackRequired")
	public  Int getSendCallbackRequired() {
		return sendCallbackRequired;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sendCallbackRequired")
	public   void setSendCallbackRequired(Int val) {
		sendCallbackRequired = val;
	}

	/*Radix::PersoComm::SentMessage:deliveryCallbackClassName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackClassName")
	public published  Str getDeliveryCallbackClassName() {
		return deliveryCallbackClassName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackClassName")
	public published   void setDeliveryCallbackClassName(Str val) {
		deliveryCallbackClassName = val;
	}

	/*Radix::PersoComm::SentMessage:deliveryCallbackMethodName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackMethodName")
	public published  Str getDeliveryCallbackMethodName() {
		return deliveryCallbackMethodName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackMethodName")
	public published   void setDeliveryCallbackMethodName(Str val) {
		deliveryCallbackMethodName = val;
	}

	/*Radix::PersoComm::SentMessage:deliveryCallbackRequired-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackRequired")
	public published  Int getDeliveryCallbackRequired() {
		return deliveryCallbackRequired;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackRequired")
	public published   void setDeliveryCallbackRequired(Int val) {
		deliveryCallbackRequired = val;
	}

	/*Radix::PersoComm::SentMessage:deliveryExpTimeMillis-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryExpTimeMillis")
	public published  Int getDeliveryExpTimeMillis() {
		return deliveryExpTimeMillis;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryExpTimeMillis")
	public published   void setDeliveryExpTimeMillis(Int val) {
		deliveryExpTimeMillis = val;
	}

	/*Radix::PersoComm::SentMessage:deliveryCallbackData-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackData")
	public published  Str getDeliveryCallbackData() {
		return deliveryCallbackData;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackData")
	public published   void setDeliveryCallbackData(Str val) {
		deliveryCallbackData = val;
	}

	/*Radix::PersoComm::SentMessage:sendCallbackData-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sendCallbackData")
	public published  Str getSendCallbackData() {
		return sendCallbackData;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sendCallbackData")
	public published   void setSendCallbackData(Str val) {
		sendCallbackData = val;
	}

	/*Radix::PersoComm::SentMessage:stageNo-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:stageNo")
	public published  Int getStageNo() {
		return stageNo;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:stageNo")
	public published   void setStageNo(Int val) {
		stageNo = val;
	}

	/*Radix::PersoComm::SentMessage:prevStageMessageId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:prevStageMessageId")
	public published  Int getPrevStageMessageId() {
		return prevStageMessageId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:prevStageMessageId")
	public published   void setPrevStageMessageId(Int val) {
		prevStageMessageId = val;
	}

	/*Radix::PersoComm::SentMessage:prevStageNotSentMessage-Dynamic Property*/



	protected org.radixware.ads.PersoComm.server.OutMessage prevStageNotSentMessage=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:prevStageNotSentMessage")
	public published  org.radixware.ads.PersoComm.server.OutMessage getPrevStageNotSentMessage() {

		if (prevStageMessageId == null) {
		    return null;
		}

		return OutMessage.loadByPK(prevStageMessageId, true);

	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:prevStageNotSentMessage")
	public published   void setPrevStageNotSentMessage(org.radixware.ads.PersoComm.server.OutMessage val) {
		prevStageNotSentMessage = val;
	}

	/*Radix::PersoComm::SentMessage:prevStageSentMessage-Dynamic Property*/



	protected org.radixware.ads.PersoComm.server.SentMessage prevStageSentMessage=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:prevStageSentMessage")
	public published  org.radixware.ads.PersoComm.server.SentMessage getPrevStageSentMessage() {

		if (prevStageMessageId == null) {
		    return null;
		}

		return SentMessage.loadByPK(prevStageMessageId, true);

	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:prevStageSentMessage")
	public published   void setPrevStageSentMessage(org.radixware.ads.PersoComm.server.SentMessage val) {
		prevStageSentMessage = val;
	}

	/*Radix::PersoComm::SentMessage:prevStageNotSentMessageId-Dynamic Property*/



	protected Int prevStageNotSentMessageId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:prevStageNotSentMessageId")
	public published  Int getPrevStageNotSentMessageId() {

		return prevStageNotSentMessage == null ? null : prevStageMessageId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:prevStageNotSentMessageId")
	private published   void setPrevStageNotSentMessageId(Int val) {
		prevStageNotSentMessageId = val;
	}

	/*Radix::PersoComm::SentMessage:prevStageSentMessageId-Dynamic Property*/



	protected Int prevStageSentMessageId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:prevStageSentMessageId")
	public published  Int getPrevStageSentMessageId() {

		return prevStageSentMessage == null ? null : prevStageMessageId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:prevStageSentMessageId")
	private published   void setPrevStageSentMessageId(Int val) {
		prevStageSentMessageId = val;
	}

	/*Radix::PersoComm::SentMessage:newAddress-Dynamic Property*/



	protected Str newAddress=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:newAddress")
	public published  Str getNewAddress() {

		OutMessageUpdateFields fields = updateFields;
		if (fields != null && fields.newAddress != null) {
		    return fields.newAddress;
		}
		return address;
	}

	/*Radix::PersoComm::SentMessage:newChannelId-Dynamic Property*/



	protected Int newChannelId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:newChannelId")
	public published  Int getNewChannelId() {

		OutMessageUpdateFields fields = updateFields;
		if (fields != null && fields.newChannel != null) {
		    return fields.newChannel;
		}
		return id;
	}

	/*Radix::PersoComm::SentMessage:newDueTime-Dynamic Property*/



	protected java.sql.Timestamp newDueTime=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:newDueTime")
	public published  java.sql.Timestamp getNewDueTime() {

		OutMessageUpdateFields fields = updateFields;
		if (fields != null && fields.newDueTime != null) {
		    return fields.newDueTime;
		}
		return dueTime;
	}

	/*Radix::PersoComm::SentMessage:updateFields-Dynamic Property*/



	protected org.radixware.ads.PersoComm.server.OutMessageUpdateFields updateFields=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:updateFields")
	public published  org.radixware.ads.PersoComm.server.OutMessageUpdateFields getUpdateFields() {

		if (OutMessageGroup.needReplaceStatic != null && OutMessageGroup.needReplaceStatic.booleanValue()) {
		    return OutMessageGroupChanges4Failed.calcUpdates(channelId, address, dueTime,
		    OutMessageGroup.oldChannelStatic, OutMessageGroup.oldDestinationAddressStatic,
		    OutMessageGroup.newChannelStatic, OutMessageGroup.newDestinationAddressStatic, OutMessageGroup.newDueTimeStatic);
		}
		return null;
	}

































































































































































































































































































































	/*Radix::PersoComm::SentMessage:Methods-Methods*/

	/*Radix::PersoComm::SentMessage:afterSendCallback-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:afterSendCallback")
	public published  void afterSendCallback (Str sendError) {
		Arte::Trace.debug("sendCallback(messageId = " + id + ", sendError = " + sendError + ")", Arte::EventSource:PersoCommUnit);
		if (sendCallbackRequired == null || sendCallbackRequired != 1 || Utils::String.isBlank(sendCallbackClassName) || Utils::String.isBlank(sendCallbackMethodName)) {
		    return;
		}
		rereadAndLock(10, false);
		if (sendCallbackRequired == null || sendCallbackRequired != 1 || Utils::String.isBlank(sendCallbackClassName) || Utils::String.isBlank(sendCallbackMethodName)) {
		    return;
		}

		Str sp = Arte::Arte.setSavepoint();
		boolean ok = false;

		try {
		    final Class<?>[] types = new Class<?>[] {SentMessage.class, Str.class};
		    final Object[] args = new Object[] {this, sendError};
		    ok = xxxCallback(sendCallbackClassName, sendCallbackMethodName, types, args);
		} catch (Exceptions::Exception e) {
		    if (e instanceof RuntimeException)
		        throw (RuntimeException)e;
		    throw new AppError(e.getMessage(), e);
		} finally {
		    try {
		        if (!ok) {
		            Arte::Trace.error("Initiating rollback due to send callback error", Arte::EventSource:PersoCommUnit);
		            Arte::Arte.rollbackToSavepoint(sp);
		        }
		        
		        final boolean deliveryCallbackRequired = Utils::String.isNotBlank(deliveryCallbackClassName)
		                && Utils::String.isNotBlank(deliveryCallbackMethodName) && Utils::String.isBlank(sendError);
		        sendCallbackRequired = deliveryCallbackRequired || histMode != HistMode:DontStore || Utils::String.isBlank(sendError) ? 2 : 3;
		        deliveryCallbackRequired = deliveryCallbackRequired ? (deliveryStatus == DeliveryStatus:TRACKING ? 1 : 2) : null;
		        update();
		        
		        if (Utils::String.isBlank(sendError) && (histMode == HistMode:DontStore || storeAttachInHist == false)) {
		            PersoComm.Db::DelAttachmentsStmt.execute(id);
		        }
		        
		        Arte::Arte.commit();
		    } catch (Exceptions::DatabaseError ex) {
		        Arte::Trace.error("Failure on send callback completion: " + Utils::ExceptionTextFormatter.exceptionStackToString(ex), Arte::EventSource:PersoCommUnit);
		    }
		}

	}

	/*Radix::PersoComm::SentMessage:afterDeliveryCallback-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:afterDeliveryCallback")
	public published  void afterDeliveryCallback (Str statusStr) {
		Arte::Trace.debug("deliveryCallback(messageId = " + id + ", statusStr = " + statusStr + ")", Arte::EventSource:PersoCommUnit);
		DeliveryStatus status = DeliveryStatus.getForValue(statusStr);

		if (deliveryCallbackRequired == null || deliveryCallbackRequired != 2 || Utils::String.isBlank(deliveryCallbackClassName) || Utils::String.isBlank(deliveryCallbackMethodName)) {
		    return;
		}
		rereadAndLock(10, false);
		if (deliveryCallbackRequired == null || deliveryCallbackRequired != 2 || Utils::String.isBlank(deliveryCallbackClassName) || Utils::String.isBlank(deliveryCallbackMethodName)) {
		    return;
		}

		Str sp = Arte::Arte.setSavepoint();
		boolean ok = false;

		try {
		    Java.Lang::Thread.sleep(20000);
		} catch (Exceptions::Throwable t) {
		    
		}

		try {
		    final Class<?>[] types = new Class<?>[]{SentMessage.class, DeliveryStatus.class};
		    final Object[] args = new Object[]{this, status};
		    ok = xxxCallback(deliveryCallbackClassName, deliveryCallbackMethodName, types, args);
		} catch (Exceptions::Exception e) {
		    if (e instanceof RuntimeException)
		        throw (RuntimeException)e;
		    throw new AppError(e.getMessage(), e);
		} finally {
		    try {
		        if (!ok) {
		            Arte::Trace.error("Initiating rollback due to delivery callback error", Arte::EventSource:PersoCommUnit);
		            Arte::Arte.rollbackToSavepoint(sp);
		        }
		        deliveryCallbackRequired = histMode != HistMode:DontStore ? 3 : 4;
		        update();
		        Arte::Arte.commit();
		    } catch (Exceptions::DatabaseError ex) {
		        Arte::Trace.error("Failure on delivery callback completion: " + Utils::ExceptionTextFormatter.exceptionStackToString(ex), Arte::EventSource:PersoCommUnit);
		    }
		}

	}

	/*Radix::PersoComm::SentMessage:xxxCallback-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:xxxCallback")
	private final  boolean xxxCallback (Str className, Str methodName, java.lang.Class<? extends java.lang.Object>[] types, java.lang.Object[] args) throws java.lang.Exception {
		try {
		    Types::Id classId = Types::Id.Factory.loadFrom(className);
		    Arte::Arte.invokeByClassId(
		            classId,
		            methodName,
		            types,
		            args);
		} catch(Exceptions::DefinitionNotFoundError e){
		    Arte::Arte.invokeByClassName(
		        className,
		        methodName,
		        types,
		        args);
		}
		return true;
	}

	/*Radix::PersoComm::SentMessage:testDeliveryCallback-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:testDeliveryCallback")
	public static published  void testDeliveryCallback (long messageId, org.radixware.kernel.common.enums.EPersoCommDeliveryStatus deliveryStatus, int unitId) throws java.lang.Exception {
		PersoCommDeliveryWsdl:DeliveryDocument doc = PersoCommDeliveryWsdl:DeliveryDocument.Factory.newInstance();
		PersoCommDeliveryXsd:DeliveryMess mess = doc.addNewDelivery();
		PersoCommDeliveryXsd:DeliveryRq rq = mess.addNewDeliveryRq();
		PersoCommDeliveryXsd:DeliveryItem item1 = rq.addNewDeliveryItem();
		item1.MessageId = messageId;
		item1.DeliveryStatus = deliveryStatus;

		Arte::Arte.invokeInternalService(doc, PersoCommDeliveryWsdl:DeliveryDocument.class, "http://schemas.radixware.org/persocomm-delivery.wsdl#" + unitId, 10, 10, 10, Arte::AadcMember:ANY);

	}

	/*Radix::PersoComm::SentMessage:testDeliveryCallbackNoEx-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:testDeliveryCallbackNoEx")
	public static published  void testDeliveryCallbackNoEx (long messageId, org.radixware.kernel.common.enums.EPersoCommDeliveryStatus deliveryStatus, int unitId) {
		try {
		    testDeliveryCallback(messageId, deliveryStatus, unitId);
		} catch (Exceptions::Throwable t) {
		    Arte::Trace.error("Ignoring exception in delivery callback: " + Utils::ExceptionTextFormatter.exceptionStackToString(t), Arte::EventSource:PersoCommUnit);
		}

	}

	/*Radix::PersoComm::SentMessage:loadByPidStr-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:loadByPidStr")
	public static published  org.radixware.ads.PersoComm.server.SentMessage loadByPidStr (Str pidAsStr, boolean checkExistance) {
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblOA4BGTURUTOBDANVABIFNQAABA"),pidAsStr);
		try{
		return (
		org.radixware.ads.PersoComm.server.SentMessage) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::PersoComm::SentMessage:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:loadByPK")
	public static published  org.radixware.ads.PersoComm.server.SentMessage loadByPK (Int id, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(id==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOI4BGTURUTOBDANVABIFNQAABA"),id);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblOA4BGTURUTOBDANVABIFNQAABA"),pkValsMap);
		try{
		return (
		org.radixware.ads.PersoComm.server.SentMessage) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::PersoComm::SentMessage - Server Meta*/

/*Radix::PersoComm::SentMessage-Entity Class*/

package org.radixware.ads.PersoComm.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class SentMessage_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),"SentMessage",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQG2BDVKTVXOBDCLUAALOMT5GDM"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::PersoComm::SentMessage:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
							/*Owner Class Name*/
							"SentMessage",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQG2BDVKTVXOBDCLUAALOMT5GDM"),
							/*Property presentations*/

							/*Radix::PersoComm::SentMessage:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::PersoComm::SentMessage:destEntityGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6HSMYFOSV5EWPD575PUINSYHEM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::PersoComm::SentMessage:sendError:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7WEBG24RUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:sentTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIE2KOWURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:destPid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJP4VCQ3UPBA4PIHAITVYDWTEV4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::PersoComm::SentMessage:dueTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO44BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOI4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:subject:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOM4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:body:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOQ4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:importance:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOU4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::PersoComm::SentMessage:createTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOY4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:expireTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPA4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:channelKind:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPE4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:address:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:sourceEntityGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPM4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:sourcePid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPQ4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:sourceMsgId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:channelId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQA4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:channelUnit:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6AME2JOY6ZAE5NLCLB5A6HFYEE"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(262143,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprGVZBTVH6VXOBDCLVAALOMT5GDM")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::PersoComm::SentMessage:sourceTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTBHNMZQEJFHUJEKE7DIBKEY6BU"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::PersoComm::SentMessage:encoding:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWRXU6KICKFCKBE5YNLT55VTE54"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:smppBytesSent:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBDMFQL73SVFQJIEN2O5QNJLRR4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:smppCharsSent:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT2UDNHX6OBH5NC5HWV3HOJY2BQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:smppPartsSent:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOIBT34FIG5G2DH6GQ3DBY4VXNQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:maskedBody:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSMQR44I5N5AGHIJMQFF6TUO4RU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:displayedBody:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNLFYGIFUGZANBP23DMNK7U2XRI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:routingKey:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAKIXW6SGNJBTFMGDGNF2DG3FJM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:deliveryStatus:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI3F7THEYXRF35CL4TRW2KAI7QU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:lastDeliveryStatusChangeDate:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAYKPP3DRG5DSJM6JICMM3UWIT4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:smppMessageId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR4UDIIE2IFC33IEFMZOEATMYM4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:isUssd:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOH42BANGWVAY3EXEMXZZNO7IRM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:ussdServiceOp:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLK7JQVGCFRAV3NJTQFYX7VMBYY"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:sendCallbackRequired:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP36RBYIF2FF75HEYQO3KV23FNA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:deliveryCallbackClassName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHBKOTQRWA5H73OKJK7RBV7TF4Y"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:deliveryCallbackMethodName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVVKDXIQPMJG7BPPIEW4H3LH7FE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:deliveryCallbackRequired:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVTBL3E6TJVGFXK3IZYZAAVN5DU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:deliveryExpTimeMillis:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGEUMW2NUSFF3RKAB64DOQLD75M"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:deliveryCallbackData:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXYWM73QCI5CMZIENZ7QXRZOWME"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:sendCallbackData:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6Z74VVJ3NFHBXKE5IS2NIYT3OM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:stageNo:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHXFPFBMDIVFBZJX6347JRYIOYA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:prevStageMessageId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT6MBYYMFXVFT7FTATBQSBIMSTI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:prevStageNotSentMessageId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUBVGYBJAPRDXRLHAGRDIYBZUD4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:prevStageSentMessageId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd55GQ3KHV5JC4VMHVQB4ETZQAQ4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:newAddress:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLFJIDL4YHBF3TN3KPU6DZSJ4WM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:newChannelId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJAV7SM2CSJGYJNNRHAKGULO72M"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::SentMessage:newDueTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVNXI6AEFQRHGFAAXNLAEE5EJXM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::PersoComm::SentMessage:ViewSource-Property Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdKOKLUBT4QBG2VNTBDPHFESHHAE"),"ViewSource",org.radixware.kernel.common.enums.ECommandScope.PROPERTY,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTBHNMZQEJFHUJEKE7DIBKEY6BU")},org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.LOCAL,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::PersoComm::SentMessage:ViewPrevStageMessage-Property Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd2BSOOFGDXRCNDD3T7J23FVJEQY"),"ViewPrevStageMessage",org.radixware.kernel.common.enums.ECommandScope.PROPERTY,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colT6MBYYMFXVFT7FTATBQSBIMSTI")},org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.LOCAL,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),true,true)
							},
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::PersoComm::SentMessage:Sent-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srt2TAMIGP57VAD3B5EDXWVX3L4LI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),"Sent",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUWT55S5AIVCDZJTPQYAH7YB66I"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIE2KOWURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EOrder.DESC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							new org.radixware.kernel.server.meta.presentations.RadFilterDef[]{

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::PersoComm::SentMessage:Address-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltDZGN4HVB2FDHZAGCMMFLBLUFJE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),"Address",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2FZN3F5NFJADJKNVF3QD6ZMHKA"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblOA4BGTURUTOBDANVABIFNQAABA\" PropId=\"colQA4BGTURUTOBDANVABIFNQAABA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:EntityRefParameter ParamId=\"prmWCGBWZWPJRAAVOJSIF3HLJGDWI\" ReferencedTableId=\"tbl5HP4XTP3EGWDBRCRAAIT4AGD7E\" PidTranslationMode=\"AsStr\"/></xsc:Item><xsc:Item><xsc:Sql>\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblOA4BGTURUTOBDANVABIFNQAABA\" PropId=\"colPI4BGTURUTOBDANVABIFNQAABA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> like </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmM67CPG3XJZF3RDDBJ2VOTPXZ7Y\"/></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srt2TAMIGP57VAD3B5EDXWVX3L4LI"),true,null,true,"org.radixware")
							},
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::PersoComm::SentMessage:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXVZ6UDCUVXOBDCLUAALOMT5GDM"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::PersoComm::SentMessage:General:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::PersoComm::SentMessage:General:Attachments-Entity Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadTableExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiHARKHSXMVXOBDCLVAALOMT5GDM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5O3H4DJEVLOBDCLSAALOMT5GDM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6BHCGLBRVXOBDCLUAALOMT5GDM"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tbl5O3H4DJEVLOBDCLSAALOMT5GDM\" PropId=\"col5W3H4DJEVLOBDCLSAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblOA4BGTURUTOBDANVABIFNQAABA\" PropId=\"colOI4BGTURUTOBDANVABIFNQAABA\" Owner=\"PARENT\"/></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::PersoComm::SentMessage:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprW77ZPPNK4ZBGROWGY7BIPQOWHE"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::PersoComm::SentMessage:Create:Children-Explorer Items*/
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
									/*Radix::PersoComm::SentMessage:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6ME4ANKUVXOBDCLUAALOMT5GDM"),"General",null,2192,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOI4BGTURUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6AME2JOY6ZAE5NLCLB5A6HFYEE"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI4BGTURUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOY4BGTURUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO44BGTURUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIE2KOWURUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOM4BGTURUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7WEBG24RUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOU4BGTURUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPM4BGTURUTOBDANVABIFNQAABA"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPQ4BGTURUTOBDANVABIFNQAABA"),false)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srt2TAMIGP57VAD3B5EDXWVX3L4LI"),true,null,true,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>FIRST_ROWS(51)</xsc:Sql></xsc:Item></xsc:Sqml>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXVZ6UDCUVXOBDCLUAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprW77ZPPNK4ZBGROWGY7BIPQOWHE")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(290855,null,null,null),null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::PersoComm::SentMessage:Failed-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprPCNPIO4LEJCCRPJQFVPMPDTWWM"),"Failed",null,0,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOI4BGTURUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI4BGTURUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6AME2JOY6ZAE5NLCLB5A6HFYEE"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7WEBG24RUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPM4BGTURUTOBDANVABIFNQAABA"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPQ4BGTURUTOBDANVABIFNQAABA"),false)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecOA4BGTURUTOBDANVABIFNQAABA\" PropId=\"col7WEBG24RUTOBDANVABIFNQAABA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is not null</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(null,true,null,true,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXVZ6UDCUVXOBDCLUAALOMT5GDM")},null,org.radixware.kernel.server.types.Restrictions.Factory.newInstance(32,null,null,null),null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::PersoComm::SentMessage:FailedUpdatePreview-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZ6YHZV7VUNFDNMIN56S6D274BY"),"FailedUpdatePreview",org.radixware.kernel.common.types.Id.Factory.loadFrom("sprPCNPIO4LEJCCRPJQFVPMPDTWWM"),16569,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOI4BGTURUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI4BGTURUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLFJIDL4YHBF3TN3KPU6DZSJ4WM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQA4BGTURUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJAV7SM2CSJGYJNNRHAKGULO72M"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO44BGTURUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVNXI6AEFQRHGFAAXNLAEE5EJXM"),true)
									},null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXVZ6UDCUVXOBDCLUAALOMT5GDM")},null,null,null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6ME4ANKUVXOBDCLUAALOMT5GDM"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::PersoComm::SentMessage:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colOY4BGTURUTOBDANVABIFNQAABA"),"{0,date,dd/MM/yyyy HH:mm:ss}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colOM4BGTURUTOBDANVABIFNQAABA")," - {0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(28677,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::PersoComm::SentMessage:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::PersoComm::SentMessage:destEntityGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6HSMYFOSV5EWPD575PUINSYHEM"),"destEntityGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJMALNOQNDRDPPLQUHTON7FS7GY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:sendError-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7WEBG24RUTOBDANVABIFNQAABA"),"sendError",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEN3N7FCTVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:sentTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIE2KOWURUTOBDANVABIFNQAABA"),"sentTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH52JHJKTVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:destPid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJP4VCQ3UPBA4PIHAITVYDWTEV4"),"destPid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZM6ZF5XSLFH2JPPEBHLWXTWVZQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:dueTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO44BGTURUTOBDANVABIFNQAABA"),"dueTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5HPRI6CTVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOI4BGTURUTOBDANVABIFNQAABA"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKTDKJCSTVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:subject-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOM4BGTURUTOBDANVABIFNQAABA"),"subject",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTY2HVTCTVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:body-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOQ4BGTURUTOBDANVABIFNQAABA"),"body",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSL7OITCTVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:importance-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOU4BGTURUTOBDANVABIFNQAABA"),"importance",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKXDKJCSTVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTUGILCRUVXOBDCLUAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:createTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOY4BGTURUTOBDANVABIFNQAABA"),"createTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUGCNU2STVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:sendCallbackMethodName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP44BGTURUTOBDANVABIFNQAABA"),"sendCallbackMethodName",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:expireTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPA4BGTURUTOBDANVABIFNQAABA"),"expireTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRLWTTA2TVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:channelKind-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPE4BGTURUTOBDANVABIFNQAABA"),"channelKind",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls75CXGWCTVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:address-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI4BGTURUTOBDANVABIFNQAABA"),"address",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFB2ZQEKQVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:sourceEntityGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPM4BGTURUTOBDANVABIFNQAABA"),"sourceEntityGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFWBKJSPCFNF6TAHCOWVR5ZNEOU"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:sourcePid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPQ4BGTURUTOBDANVABIFNQAABA"),"sourcePid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFZUUB3MYEVEFJI4BQQU7QEH4OI"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:sourceMsgId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU4BGTURUTOBDANVABIFNQAABA"),"sourceMsgId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVKJDCACBFJDD5BMJ5DBVFYIQGI"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:sendCallbackClassName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPY4BGTURUTOBDANVABIFNQAABA"),"sendCallbackClassName",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:channelId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQA4BGTURUTOBDANVABIFNQAABA"),"channelId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRGL45TZYUVCGDGYDSHEYJQB724"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:histMode-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZMZAPRLLXBFCLH7VUIGVA7HOK4"),"histMode",null,org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsVQOKSQR4RVAMPBGDQRMO7W7JBQ"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:channelUnit-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6AME2JOY6ZAE5NLCLB5A6HFYEE"),"channelUnit",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWBZRDZKYOZENVDV6PDPCPQLCDA"),org.radixware.kernel.common.enums.EValType.PARENT_REF,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:sourceTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTBHNMZQEJFHUJEKE7DIBKEY6BU"),"sourceTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSIXLQQYFJRFQHK5TZKGZ5RCUOQ"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:encoding-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWRXU6KICKFCKBE5YNLT55VTE54"),"encoding",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS2YX3WJISBE6VKMQKIOJVHLUOU"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsSMFAHKLBOBGHVDYOXCSGSNFDME"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:smppBytesSent-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBDMFQL73SVFQJIEN2O5QNJLRR4"),"smppBytesSent",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK27XN3N6MRC2NIKVIWDMUWTG34"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:smppCharsSent-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT2UDNHX6OBH5NC5HWV3HOJY2BQ"),"smppCharsSent",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5GQO5DOB4RG5XA7SEJM27LPEUQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:smppPartsSent-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOIBT34FIG5G2DH6GQ3DBY4VXNQ"),"smppPartsSent",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7BSDZCMRKBD2HPOMART25NE3FM"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:maskedBody-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSMQR44I5N5AGHIJMQFF6TUO4RU"),"maskedBody",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4FKQ7WEPPRFFNEHP42Q7UEC5GU"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:displayedBody-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNLFYGIFUGZANBP23DMNK7U2XRI"),"displayedBody",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ7GKJ45N3NHKVB5TGFLSW5GCC4"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:routingKey-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAKIXW6SGNJBTFMGDGNF2DG3FJM"),"routingKey",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFVT62RWB3VE6LLFLRLZ33VYIOM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:storeAttachInHist-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQXYMWXELEVCX7NF4GACIHMPVL4"),"storeAttachInHist",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTVMS6CLEG5E43KZCH4SV5D6JPY"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:deliveryStatus-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI3F7THEYXRF35CL4TRW2KAI7QU"),"deliveryStatus",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPGTVBLMW6JFQLISGJRLJ6NTLCE"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsBNGKCKPU5BEIVNZ73TQDMBXZ5Q"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:lastDeliveryStatusChangeDate-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAYKPP3DRG5DSJM6JICMM3UWIT4"),"lastDeliveryStatusChangeDate",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls36Q33ZP3NZDXFGTXEEIJI7LKKQ"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:smppMessageId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR4UDIIE2IFC33IEFMZOEATMYM4"),"smppMessageId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMC3AKZHBKNFMFNQFWZOV44SSWE"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:isUssd-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOH42BANGWVAY3EXEMXZZNO7IRM"),"isUssd",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7S6LUNU5IJHIZCV3DO3UTRIWAI"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:ussdServiceOp-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLK7JQVGCFRAV3NJTQFYX7VMBYY"),"ussdServiceOp",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYDDFWZ3Z5VFB7FDON5T5TXNN6A"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:sendCallbackRequired-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP36RBYIF2FF75HEYQO3KV23FNA"),"sendCallbackRequired",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:deliveryCallbackClassName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHBKOTQRWA5H73OKJK7RBV7TF4Y"),"deliveryCallbackClassName",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:deliveryCallbackMethodName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVVKDXIQPMJG7BPPIEW4H3LH7FE"),"deliveryCallbackMethodName",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:deliveryCallbackRequired-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVTBL3E6TJVGFXK3IZYZAAVN5DU"),"deliveryCallbackRequired",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:deliveryExpTimeMillis-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGEUMW2NUSFF3RKAB64DOQLD75M"),"deliveryExpTimeMillis",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:deliveryCallbackData-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXYWM73QCI5CMZIENZ7QXRZOWME"),"deliveryCallbackData",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:sendCallbackData-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6Z74VVJ3NFHBXKE5IS2NIYT3OM"),"sendCallbackData",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:stageNo-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHXFPFBMDIVFBZJX6347JRYIOYA"),"stageNo",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOPRUHXJXDNAZVNYOSHUYB3SIGA"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:prevStageMessageId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT6MBYYMFXVFT7FTATBQSBIMSTI"),"prevStageMessageId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXQCOX3OSXRAFBBSALOPTLVQU3M"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:prevStageNotSentMessage-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUE345NVIX5GBZL3AHKRUYBQLXU"),"prevStageNotSentMessage",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUBY3ORBPVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.USER_CLASS,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:prevStageSentMessage-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNKX4PVK5JJDA3ARZBZIT5W3FTE"),"prevStageSentMessage",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQG2BDVKTVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.USER_CLASS,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:prevStageNotSentMessageId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUBVGYBJAPRDXRLHAGRDIYBZUD4"),"prevStageNotSentMessageId",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:prevStageSentMessageId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd55GQ3KHV5JC4VMHVQB4ETZQAQ4"),"prevStageSentMessageId",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:newAddress-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLFJIDL4YHBF3TN3KPU6DZSJ4WM"),"newAddress",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZGMXBII6VNFQNIIVXDYMTQQFXM"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:newChannelId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJAV7SM2CSJGYJNNRHAKGULO72M"),"newChannelId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTAKKNILNXBAJZBM5VKXIV676KU"),org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:newDueTime-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVNXI6AEFQRHGFAAXNLAEE5EJXM"),"newDueTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRMSJ3POROFAGZIEDFSCJUOJFQY"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::SentMessage:updateFields-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFJPND4F36VBTFKGYPI7YTSCR5A"),"updateFields",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE)
						},

						/*Radix::PersoComm::SentMessage:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYMJXBQNCLRBIVONT32C7ZAEZAY"),"afterSendCallback",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sendError",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPOUURIOAGVGGNDLXCE2MJSZZ5Y"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEOLVVH65ENDSPBBYY3XAOBB2OA"),"afterDeliveryCallback",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("statusStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPOUURIOAGVGGNDLXCE2MJSZZ5Y"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7TFGVPAZOZE3JC26L574RDP4HA"),"xxxCallback",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("className",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2LNLJVQ4KFA6LAFR3EVIMM2FRA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("methodName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGKKQ3NMAQJHTRLKKYUJKZOAVW4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("types",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6BWLHSTCYREWPLDAKGAUE6MYZ4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("args",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJ6PXBGATBBBUTKZ25FVY6OREI4"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPO5GOPRDV5E7FILBNZEBPCUZC4"),"testDeliveryCallback",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("messageId",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRD7ZYC3AXFGBHH53WYHVUK6XTY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("deliveryStatus",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGZZ66SKCOZFGFE4L46LZKWJ6JE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("unitId",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZDOTBUECEVD55NIGLMHD2BJBIY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUWFDMCMNUZH3XDGDQLW2MW25WQ"),"testDeliveryCallbackNoEx",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("messageId",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRD7ZYC3AXFGBHH53WYHVUK6XTY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("deliveryStatus",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGZZ66SKCOZFGFE4L46LZKWJ6JE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("unitId",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZDOTBUECEVD55NIGLMHD2BJBIY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPidAsStr__________________")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOI4BGTURUTOBDANVABIFNQAABA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::PersoComm::SentMessage - Desktop Executable*/

/*Radix::PersoComm::SentMessage-Entity Class*/

package org.radixware.ads.PersoComm.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage")
public interface SentMessage {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

		/*Radix::PersoComm::SentMessageGroup:needReplace:needReplace-Presentation Property*/




		public class NeedReplace extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
			public NeedReplace(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:needReplace:needReplace")
			public  Bool getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:needReplace:needReplace")
			public   void setValue(Bool val) {
				Value = val;
			}
		}
		public NeedReplace getNeedReplace(){return (NeedReplace)getProperty(pgpZPVAQNVZ6ZDMNGIE2TONU4DGN4);}

		/*Radix::PersoComm::SentMessageGroup:newChannel:newChannel-Presentation Property*/




		public class NewChannel extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
			public NewChannel(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newChannel:newChannel")
			public  Int getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newChannel:newChannel")
			public   void setValue(Int val) {
				Value = val;
			}
		}
		public NewChannel getNewChannel(){return (NewChannel)getProperty(pgpBCGHBBXGQ5EHVBZX3WEM7SN2J4);}

		/*Radix::PersoComm::SentMessageGroup:newDestinationAddress:newDestinationAddress-Presentation Property*/




		public class NewDestinationAddress extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
			public NewDestinationAddress(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newDestinationAddress:newDestinationAddress")
			public  Str getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newDestinationAddress:newDestinationAddress")
			public   void setValue(Str val) {
				Value = val;
			}
		}
		public NewDestinationAddress getNewDestinationAddress(){return (NewDestinationAddress)getProperty(pgpCGVHXUT7GBG6DJFMHIYKYJESIA);}

		/*Radix::PersoComm::SentMessageGroup:newDueTime:newDueTime-Presentation Property*/




		public class NewDueTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
			public NewDueTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newDueTime:newDueTime")
			public  java.sql.Timestamp getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newDueTime:newDueTime")
			public   void setValue(java.sql.Timestamp val) {
				Value = val;
			}
		}
		public NewDueTime getNewDueTime(){return (NewDueTime)getProperty(pgpZZXMOBMPTBBZNI4ADXXRWDLDFU);}

		/*Radix::PersoComm::SentMessageGroup:oldChannel:oldChannel-Presentation Property*/




		public class OldChannel extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
			public OldChannel(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:oldChannel:oldChannel")
			public  Str getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:oldChannel:oldChannel")
			public   void setValue(Str val) {
				Value = val;
			}
		}
		public OldChannel getOldChannel(){return (OldChannel)getProperty(pgpIA2K5ICLERFDDHDMKXOOCSDRQU);}

		/*Radix::PersoComm::SentMessageGroup:oldDestinationAddress:oldDestinationAddress-Presentation Property*/




		public class OldDestinationAddress extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
			public OldDestinationAddress(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:oldDestinationAddress:oldDestinationAddress")
			public  Str getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:oldDestinationAddress:oldDestinationAddress")
			public   void setValue(Str val) {
				Value = val;
			}
		}
		public OldDestinationAddress getOldDestinationAddress(){return (OldDestinationAddress)getProperty(pgpADZWSYA755CZHBIHTIOO5GVYRI);}

















		public org.radixware.ads.PersoComm.explorer.SentMessage.SentMessage_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.PersoComm.explorer.SentMessage.SentMessage_DefaultModel )  super.getEntity(i);}
	}































































































































































































































































	/*Radix::PersoComm::SentMessage:destEntityGuid:destEntityGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:destEntityGuid:destEntityGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:destEntityGuid:destEntityGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DestEntityGuid getDestEntityGuid();
	/*Radix::PersoComm::SentMessage:sendCallbackData:sendCallbackData-Presentation Property*/


	public class SendCallbackData extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SendCallbackData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sendCallbackData:sendCallbackData")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sendCallbackData:sendCallbackData")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SendCallbackData getSendCallbackData();
	/*Radix::PersoComm::SentMessage:sendError:sendError-Presentation Property*/


	public class SendError extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SendError(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sendError:sendError")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sendError:sendError")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SendError getSendError();
	/*Radix::PersoComm::SentMessage:routingKey:routingKey-Presentation Property*/


	public class RoutingKey extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RoutingKey(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:routingKey:routingKey")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:routingKey:routingKey")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoutingKey getRoutingKey();
	/*Radix::PersoComm::SentMessage:lastDeliveryStatusChangeDate:lastDeliveryStatusChangeDate-Presentation Property*/


	public class LastDeliveryStatusChangeDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastDeliveryStatusChangeDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:lastDeliveryStatusChangeDate:lastDeliveryStatusChangeDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:lastDeliveryStatusChangeDate:lastDeliveryStatusChangeDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastDeliveryStatusChangeDate getLastDeliveryStatusChangeDate();
	/*Radix::PersoComm::SentMessage:smppBytesSent:smppBytesSent-Presentation Property*/


	public class SmppBytesSent extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SmppBytesSent(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppBytesSent:smppBytesSent")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppBytesSent:smppBytesSent")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SmppBytesSent getSmppBytesSent();
	/*Radix::PersoComm::SentMessage:deliveryExpTimeMillis:deliveryExpTimeMillis-Presentation Property*/


	public class DeliveryExpTimeMillis extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public DeliveryExpTimeMillis(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryExpTimeMillis:deliveryExpTimeMillis")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryExpTimeMillis:deliveryExpTimeMillis")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DeliveryExpTimeMillis getDeliveryExpTimeMillis();
	/*Radix::PersoComm::SentMessage:deliveryCallbackClassName:deliveryCallbackClassName-Presentation Property*/


	public class DeliveryCallbackClassName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DeliveryCallbackClassName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackClassName:deliveryCallbackClassName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackClassName:deliveryCallbackClassName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DeliveryCallbackClassName getDeliveryCallbackClassName();
	/*Radix::PersoComm::SentMessage:stageNo:stageNo-Presentation Property*/


	public class StageNo extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public StageNo(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:stageNo:stageNo")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:stageNo:stageNo")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public StageNo getStageNo();
	/*Radix::PersoComm::SentMessage:deliveryStatus:deliveryStatus-Presentation Property*/


	public class DeliveryStatus extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DeliveryStatus(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EPersoCommDeliveryStatus dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPersoCommDeliveryStatus ? (org.radixware.kernel.common.enums.EPersoCommDeliveryStatus)x : org.radixware.kernel.common.enums.EPersoCommDeliveryStatus.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EPersoCommDeliveryStatus> getValClass(){
			return org.radixware.kernel.common.enums.EPersoCommDeliveryStatus.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EPersoCommDeliveryStatus dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPersoCommDeliveryStatus ? (org.radixware.kernel.common.enums.EPersoCommDeliveryStatus)x : org.radixware.kernel.common.enums.EPersoCommDeliveryStatus.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryStatus:deliveryStatus")
		public  org.radixware.kernel.common.enums.EPersoCommDeliveryStatus getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryStatus:deliveryStatus")
		public   void setValue(org.radixware.kernel.common.enums.EPersoCommDeliveryStatus val) {
			Value = val;
		}
	}
	public DeliveryStatus getDeliveryStatus();
	/*Radix::PersoComm::SentMessage:sentTime:sentTime-Presentation Property*/


	public class SentTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public SentTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sentTime:sentTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sentTime:sentTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public SentTime getSentTime();
	/*Radix::PersoComm::SentMessage:destPid:destPid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:destPid:destPid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:destPid:destPid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DestPid getDestPid();
	/*Radix::PersoComm::SentMessage:ussdServiceOp:ussdServiceOp-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:ussdServiceOp:ussdServiceOp")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:ussdServiceOp:ussdServiceOp")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public UssdServiceOp getUssdServiceOp();
	/*Radix::PersoComm::SentMessage:dueTime:dueTime-Presentation Property*/


	public class DueTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public DueTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:dueTime:dueTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:dueTime:dueTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public DueTime getDueTime();
	/*Radix::PersoComm::SentMessage:isUssd:isUssd-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:isUssd:isUssd")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:isUssd:isUssd")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsUssd getIsUssd();
	/*Radix::PersoComm::SentMessage:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::PersoComm::SentMessage:smppPartsSent:smppPartsSent-Presentation Property*/


	public class SmppPartsSent extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SmppPartsSent(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppPartsSent:smppPartsSent")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppPartsSent:smppPartsSent")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SmppPartsSent getSmppPartsSent();
	/*Radix::PersoComm::SentMessage:subject:subject-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:subject:subject")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:subject:subject")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Subject getSubject();
	/*Radix::PersoComm::SentMessage:body:body-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:body:body")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:body:body")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Body getBody();
	/*Radix::PersoComm::SentMessage:importance:importance-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:importance:importance")
		public  org.radixware.kernel.common.enums.EPersoCommImportance getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:importance:importance")
		public   void setValue(org.radixware.kernel.common.enums.EPersoCommImportance val) {
			Value = val;
		}
	}
	public Importance getImportance();
	/*Radix::PersoComm::SentMessage:createTime:createTime-Presentation Property*/


	public class CreateTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public CreateTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:createTime:createTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:createTime:createTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public CreateTime getCreateTime();
	/*Radix::PersoComm::SentMessage:sendCallbackRequired:sendCallbackRequired-Presentation Property*/


	public class SendCallbackRequired extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SendCallbackRequired(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sendCallbackRequired:sendCallbackRequired")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sendCallbackRequired:sendCallbackRequired")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SendCallbackRequired getSendCallbackRequired();
	/*Radix::PersoComm::SentMessage:expireTime:expireTime-Presentation Property*/


	public class ExpireTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public ExpireTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:expireTime:expireTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:expireTime:expireTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public ExpireTime getExpireTime();
	/*Radix::PersoComm::SentMessage:channelKind:channelKind-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:channelKind:channelKind")
		public  org.radixware.ads.PersoComm.common.ChannelKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:channelKind:channelKind")
		public   void setValue(org.radixware.ads.PersoComm.common.ChannelKind val) {
			Value = val;
		}
	}
	public ChannelKind getChannelKind();
	/*Radix::PersoComm::SentMessage:address:address-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:address:address")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:address:address")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Address getAddress();
	/*Radix::PersoComm::SentMessage:sourceEntityGuid:sourceEntityGuid-Presentation Property*/


	public class SourceEntityGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SourceEntityGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sourceEntityGuid:sourceEntityGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sourceEntityGuid:sourceEntityGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SourceEntityGuid getSourceEntityGuid();
	/*Radix::PersoComm::SentMessage:sourcePid:sourcePid-Presentation Property*/


	public class SourcePid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SourcePid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sourcePid:sourcePid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sourcePid:sourcePid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SourcePid getSourcePid();
	/*Radix::PersoComm::SentMessage:sourceMsgId:sourceMsgId-Presentation Property*/


	public class SourceMsgId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SourceMsgId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sourceMsgId:sourceMsgId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sourceMsgId:sourceMsgId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SourceMsgId getSourceMsgId();
	/*Radix::PersoComm::SentMessage:channelId:channelId-Presentation Property*/


	public class ChannelId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ChannelId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:channelId:channelId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:channelId:channelId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ChannelId getChannelId();
	/*Radix::PersoComm::SentMessage:smppMessageId:smppMessageId-Presentation Property*/


	public class SmppMessageId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SmppMessageId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppMessageId:smppMessageId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppMessageId:smppMessageId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SmppMessageId getSmppMessageId();
	/*Radix::PersoComm::SentMessage:maskedBody:maskedBody-Presentation Property*/


	public class MaskedBody extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public MaskedBody(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:maskedBody:maskedBody")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:maskedBody:maskedBody")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MaskedBody getMaskedBody();
	/*Radix::PersoComm::SentMessage:smppCharsSent:smppCharsSent-Presentation Property*/


	public class SmppCharsSent extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SmppCharsSent(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppCharsSent:smppCharsSent")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppCharsSent:smppCharsSent")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SmppCharsSent getSmppCharsSent();
	/*Radix::PersoComm::SentMessage:prevStageMessageId:prevStageMessageId-Presentation Property*/


	public class PrevStageMessageId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PrevStageMessageId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:prevStageMessageId:prevStageMessageId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:prevStageMessageId:prevStageMessageId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PrevStageMessageId getPrevStageMessageId();
	/*Radix::PersoComm::SentMessage:deliveryCallbackRequired:deliveryCallbackRequired-Presentation Property*/


	public class DeliveryCallbackRequired extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public DeliveryCallbackRequired(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackRequired:deliveryCallbackRequired")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackRequired:deliveryCallbackRequired")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DeliveryCallbackRequired getDeliveryCallbackRequired();
	/*Radix::PersoComm::SentMessage:deliveryCallbackMethodName:deliveryCallbackMethodName-Presentation Property*/


	public class DeliveryCallbackMethodName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DeliveryCallbackMethodName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackMethodName:deliveryCallbackMethodName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackMethodName:deliveryCallbackMethodName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DeliveryCallbackMethodName getDeliveryCallbackMethodName();
	/*Radix::PersoComm::SentMessage:encoding:encoding-Presentation Property*/


	public class Encoding extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Encoding(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.ESmppEncoding dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.ESmppEncoding ? (org.radixware.kernel.common.enums.ESmppEncoding)x : org.radixware.kernel.common.enums.ESmppEncoding.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.ESmppEncoding> getValClass(){
			return org.radixware.kernel.common.enums.ESmppEncoding.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.ESmppEncoding dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.ESmppEncoding ? (org.radixware.kernel.common.enums.ESmppEncoding)x : org.radixware.kernel.common.enums.ESmppEncoding.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:encoding:encoding")
		public  org.radixware.kernel.common.enums.ESmppEncoding getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:encoding:encoding")
		public   void setValue(org.radixware.kernel.common.enums.ESmppEncoding val) {
			Value = val;
		}
	}
	public Encoding getEncoding();
	/*Radix::PersoComm::SentMessage:deliveryCallbackData:deliveryCallbackData-Presentation Property*/


	public class DeliveryCallbackData extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DeliveryCallbackData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackData:deliveryCallbackData")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackData:deliveryCallbackData")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DeliveryCallbackData getDeliveryCallbackData();
	/*Radix::PersoComm::SentMessage:prevStageSentMessageId:prevStageSentMessageId-Presentation Property*/


	public class PrevStageSentMessageId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PrevStageSentMessageId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:prevStageSentMessageId:prevStageSentMessageId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:prevStageSentMessageId:prevStageSentMessageId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PrevStageSentMessageId getPrevStageSentMessageId();
	/*Radix::PersoComm::SentMessage:channelUnit:channelUnit-Presentation Property*/


	public class ChannelUnit extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public ChannelUnit(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:channelUnit:channelUnit")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:channelUnit:channelUnit")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ChannelUnit getChannelUnit();
	/*Radix::PersoComm::SentMessage:newChannelId:newChannelId-Presentation Property*/


	public class NewChannelId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public NewChannelId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:newChannelId:newChannelId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:newChannelId:newChannelId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public NewChannelId getNewChannelId();
	/*Radix::PersoComm::SentMessage:newAddress:newAddress-Presentation Property*/


	public class NewAddress extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public NewAddress(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:newAddress:newAddress")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:newAddress:newAddress")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public NewAddress getNewAddress();
	/*Radix::PersoComm::SentMessage:displayedBody:displayedBody-Presentation Property*/


	public class DisplayedBody extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DisplayedBody(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:displayedBody:displayedBody")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:displayedBody:displayedBody")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DisplayedBody getDisplayedBody();
	/*Radix::PersoComm::SentMessage:sourceTitle:sourceTitle-Presentation Property*/


	public class SourceTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SourceTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sourceTitle:sourceTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sourceTitle:sourceTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SourceTitle getSourceTitle();
	/*Radix::PersoComm::SentMessage:prevStageNotSentMessageId:prevStageNotSentMessageId-Presentation Property*/


	public class PrevStageNotSentMessageId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PrevStageNotSentMessageId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:prevStageNotSentMessageId:prevStageNotSentMessageId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:prevStageNotSentMessageId:prevStageNotSentMessageId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PrevStageNotSentMessageId getPrevStageNotSentMessageId();
	/*Radix::PersoComm::SentMessage:newDueTime:newDueTime-Presentation Property*/


	public class NewDueTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public NewDueTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:newDueTime:newDueTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:newDueTime:newDueTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public NewDueTime getNewDueTime();
	public static class ViewPrevStageMessage extends org.radixware.kernel.common.client.models.items.Command{
		protected ViewPrevStageMessage(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class ViewSource extends org.radixware.kernel.common.client.models.items.Command{
		protected ViewSource(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}



}

/* Radix::PersoComm::SentMessage - Desktop Meta*/

/*Radix::PersoComm::SentMessage-Entity Class*/

package org.radixware.ads.PersoComm.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class SentMessage_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::PersoComm::SentMessage:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
			"Radix::PersoComm::SentMessage",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("img2BKM3KZ6XUI3SH3FA3HYB5JEVRFFPADL"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6ME4ANKUVXOBDCLUAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQG2BDVKTVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7O7GVZCTVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQG2BDVKTVXOBDCLUAALOMT5GDM"),28677,

			/*Radix::PersoComm::SentMessage:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::PersoComm::SentMessage:destEntityGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6HSMYFOSV5EWPD575PUINSYHEM"),
						"destEntityGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJMALNOQNDRDPPLQUHTON7FS7GY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
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

						/*Radix::PersoComm::SentMessage:destEntityGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:sendError:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7WEBG24RUTOBDANVABIFNQAABA"),
						"sendError",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEN3N7FCTVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:sendError:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:sentTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIE2KOWURUTOBDANVABIFNQAABA"),
						"sentTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH52JHJKTVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:sentTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:destPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJP4VCQ3UPBA4PIHAITVYDWTEV4"),
						"destPid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZM6ZF5XSLFH2JPPEBHLWXTWVZQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
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

						/*Radix::PersoComm::SentMessage:destPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:dueTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO44BGTURUTOBDANVABIFNQAABA"),
						"dueTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5HPRI6CTVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:dueTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOI4BGTURUTOBDANVABIFNQAABA"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKTDKJCSTVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:subject:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOM4BGTURUTOBDANVABIFNQAABA"),
						"subject",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTY2HVTCTVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:subject:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:body:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOQ4BGTURUTOBDANVABIFNQAABA"),
						"body",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSL7OITCTVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
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

						/*Radix::PersoComm::SentMessage:body:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:importance:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOU4BGTURUTOBDANVABIFNQAABA"),
						"importance",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKXDKJCSTVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
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

						/*Radix::PersoComm::SentMessage:importance:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTUGILCRUVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:createTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOY4BGTURUTOBDANVABIFNQAABA"),
						"createTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUGCNU2STVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:createTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:expireTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPA4BGTURUTOBDANVABIFNQAABA"),
						"expireTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRLWTTA2TVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:expireTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:channelKind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPE4BGTURUTOBDANVABIFNQAABA"),
						"channelKind",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls75CXGWCTVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),
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

						/*Radix::PersoComm::SentMessage:channelKind:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:address:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI4BGTURUTOBDANVABIFNQAABA"),
						"address",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFB2ZQEKQVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:address:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:sourceEntityGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPM4BGTURUTOBDANVABIFNQAABA"),
						"sourceEntityGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFWBKJSPCFNF6TAHCOWVR5ZNEOU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:sourceEntityGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:sourcePid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPQ4BGTURUTOBDANVABIFNQAABA"),
						"sourcePid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFZUUB3MYEVEFJI4BQQU7QEH4OI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:sourcePid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:sourceMsgId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU4BGTURUTOBDANVABIFNQAABA"),
						"sourceMsgId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVKJDCACBFJDD5BMJ5DBVFYIQGI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:sourceMsgId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:channelId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQA4BGTURUTOBDANVABIFNQAABA"),
						"channelId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRGL45TZYUVCGDGYDSHEYJQB724"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:channelId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:channelUnit:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6AME2JOY6ZAE5NLCLB5A6HFYEE"),
						"channelUnit",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWBZRDZKYOZENVDV6PDPCPQLCDA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						20,
						null,
						null,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprGVZBTVH6VXOBDCLVAALOMT5GDM")},
						null,
						null,
						262143,
						262143,false),

					/*Radix::PersoComm::SentMessage:sourceTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTBHNMZQEJFHUJEKE7DIBKEY6BU"),
						"sourceTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSIXLQQYFJRFQHK5TZKGZ5RCUOQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:sourceTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:encoding:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWRXU6KICKFCKBE5YNLT55VTE54"),
						"encoding",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS2YX3WJISBE6VKMQKIOJVHLUOU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsSMFAHKLBOBGHVDYOXCSGSNFDME"),
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

						/*Radix::PersoComm::SentMessage:encoding:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsSMFAHKLBOBGHVDYOXCSGSNFDME"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:smppBytesSent:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBDMFQL73SVFQJIEN2O5QNJLRR4"),
						"smppBytesSent",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK27XN3N6MRC2NIKVIWDMUWTG34"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:smppBytesSent:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:smppCharsSent:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT2UDNHX6OBH5NC5HWV3HOJY2BQ"),
						"smppCharsSent",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5GQO5DOB4RG5XA7SEJM27LPEUQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:smppCharsSent:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:smppPartsSent:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOIBT34FIG5G2DH6GQ3DBY4VXNQ"),
						"smppPartsSent",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7BSDZCMRKBD2HPOMART25NE3FM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:smppPartsSent:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:maskedBody:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSMQR44I5N5AGHIJMQFF6TUO4RU"),
						"maskedBody",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4FKQ7WEPPRFFNEHP42Q7UEC5GU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
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

						/*Radix::PersoComm::SentMessage:maskedBody:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:displayedBody:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNLFYGIFUGZANBP23DMNK7U2XRI"),
						"displayedBody",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ7GKJ45N3NHKVB5TGFLSW5GCC4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:displayedBody:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:routingKey:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAKIXW6SGNJBTFMGDGNF2DG3FJM"),
						"routingKey",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFVT62RWB3VE6LLFLRLZ33VYIOM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:routingKey:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:deliveryStatus:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI3F7THEYXRF35CL4TRW2KAI7QU"),
						"deliveryStatus",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPGTVBLMW6JFQLISGJRLJ6NTLCE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsBNGKCKPU5BEIVNZ73TQDMBXZ5Q"),
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

						/*Radix::PersoComm::SentMessage:deliveryStatus:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsBNGKCKPU5BEIVNZ73TQDMBXZ5Q"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:lastDeliveryStatusChangeDate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAYKPP3DRG5DSJM6JICMM3UWIT4"),
						"lastDeliveryStatusChangeDate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls36Q33ZP3NZDXFGTXEEIJI7LKKQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:lastDeliveryStatusChangeDate:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:smppMessageId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR4UDIIE2IFC33IEFMZOEATMYM4"),
						"smppMessageId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMC3AKZHBKNFMFNQFWZOV44SSWE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:smppMessageId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,64,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:isUssd:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOH42BANGWVAY3EXEMXZZNO7IRM"),
						"isUssd",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7S6LUNU5IJHIZCV3DO3UTRIWAI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:isUssd:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:ussdServiceOp:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLK7JQVGCFRAV3NJTQFYX7VMBYY"),
						"ussdServiceOp",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYDDFWZ3Z5VFB7FDON5T5TXNN6A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:ussdServiceOp:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:sendCallbackRequired:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP36RBYIF2FF75HEYQO3KV23FNA"),
						"sendCallbackRequired",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:sendCallbackRequired:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-9L,9L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:deliveryCallbackClassName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHBKOTQRWA5H73OKJK7RBV7TF4Y"),
						"deliveryCallbackClassName",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:deliveryCallbackClassName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:deliveryCallbackMethodName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVVKDXIQPMJG7BPPIEW4H3LH7FE"),
						"deliveryCallbackMethodName",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:deliveryCallbackMethodName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:deliveryCallbackRequired:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVTBL3E6TJVGFXK3IZYZAAVN5DU"),
						"deliveryCallbackRequired",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:deliveryCallbackRequired:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-9L,9L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:deliveryExpTimeMillis:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGEUMW2NUSFF3RKAB64DOQLD75M"),
						"deliveryExpTimeMillis",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:deliveryExpTimeMillis:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:deliveryCallbackData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXYWM73QCI5CMZIENZ7QXRZOWME"),
						"deliveryCallbackData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:deliveryCallbackData:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:sendCallbackData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6Z74VVJ3NFHBXKE5IS2NIYT3OM"),
						"sendCallbackData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:sendCallbackData:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:stageNo:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHXFPFBMDIVFBZJX6347JRYIOYA"),
						"stageNo",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOPRUHXJXDNAZVNYOSHUYB3SIGA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:stageNo:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:prevStageMessageId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT6MBYYMFXVFT7FTATBQSBIMSTI"),
						"prevStageMessageId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXQCOX3OSXRAFBBSALOPTLVQU3M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:prevStageMessageId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:prevStageNotSentMessageId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUBVGYBJAPRDXRLHAGRDIYBZUD4"),
						"prevStageNotSentMessageId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:prevStageNotSentMessageId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:prevStageSentMessageId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd55GQ3KHV5JC4VMHVQB4ETZQAQ4"),
						"prevStageSentMessageId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:prevStageSentMessageId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:newAddress:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLFJIDL4YHBF3TN3KPU6DZSJ4WM"),
						"newAddress",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZGMXBII6VNFQNIIVXDYMTQQFXM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:newAddress:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:newChannelId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJAV7SM2CSJGYJNNRHAKGULO72M"),
						"newChannelId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTAKKNILNXBAJZBM5VKXIV676KU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:newChannelId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:newDueTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVNXI6AEFQRHGFAAXNLAEE5EJXM"),
						"newDueTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRMSJ3POROFAGZIEDFSCJUOJFQY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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

						/*Radix::PersoComm::SentMessage:newDueTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::PersoComm::SentMessage:ViewSource-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdKOKLUBT4QBG2VNTBDPHFESHHAE"),
						"ViewSource",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHU5MPZAJGJEPRIE2JM4WDPWS3Q"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgEBTCSRR6XJGUJHPO2J4QIENJSI"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.LOCAL,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTBHNMZQEJFHUJEKE7DIBKEY6BU")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::PersoComm::SentMessage:ViewPrevStageMessage-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd2BSOOFGDXRCNDD3T7J23FVJEQY"),
						"ViewPrevStageMessage",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6CPITACTTZCU5OW4JREOL6AAJU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgEBTCSRR6XJGUJHPO2J4QIENJSI"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.LOCAL,
						true,
						false,
						true,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colT6MBYYMFXVFT7FTATBQSBIMSTI")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT)
			},
			new org.radixware.kernel.common.client.meta.filters.RadFilterDef[]{

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::PersoComm::SentMessage:Address-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltDZGN4HVB2FDHZAGCMMFLBLUFJE"),
						"Address",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2FZN3F5NFJADJKNVF3QD6ZMHKA"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblOA4BGTURUTOBDANVABIFNQAABA\" PropId=\"colQA4BGTURUTOBDANVABIFNQAABA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:EntityRefParameter ParamId=\"prmWCGBWZWPJRAAVOJSIF3HLJGDWI\" ReferencedTableId=\"tbl5HP4XTP3EGWDBRCRAAIT4AGD7E\" PidTranslationMode=\"AsStr\"/></xsc:Item><xsc:Item><xsc:Sql>\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblOA4BGTURUTOBDANVABIFNQAABA\" PropId=\"colPI4BGTURUTOBDANVABIFNQAABA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> like </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmM67CPG3XJZF3RDDBJ2VOTPXZ7Y\"/></xsc:Item></xsc:Sqml>",
						null,
						null,
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt2TAMIGP57VAD3B5EDXWVX3L4LI"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmWCGBWZWPJRAAVOJSIF3HLJGDWI"),
								"channel",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAXI4C42VR5AGXNJFTA6ZT3BAUQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblOA4BGTURUTOBDANVABIFNQAABA"),
								null,
								org.radixware.kernel.common.enums.EValType.PARENT_REF,
								null,
								null,
								true,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("spr42ALGCTMUZB6BICZW7XRIE5DOA")),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmM67CPG3XJZF3RDDBJ2VOTPXZ7Y"),
								"address",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQGBDL2FHRVBCFC2FMP6JRKEBP4"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblOA4BGTURUTOBDANVABIFNQAABA"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								true,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::PersoComm::SentMessage:Address:Editor Pages-Filter Pages*/
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

								/*Radix::PersoComm::SentMessage:Address:Main-Editor Page*/

								 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg3A3GPC5HLNAKFD42VJXSBNPSDA"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmM67CPG3XJZF3RDDBJ2VOTPXZ7Y"),1,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmWCGBWZWPJRAAVOJSIF3HLJGDWI"),0,0,1,false,false)
								},null)
						},
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
							new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg3A3GPC5HLNAKFD42VJXSBNPSDA"))}
						,
						null)
			},
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::PersoComm::SentMessage:Sent-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt2TAMIGP57VAD3B5EDXWVX3L4LI"),
						"Sent",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUWT55S5AIVCDZJTPQYAH7YB66I"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIE2KOWURUTOBDANVABIFNQAABA"),
								true)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refKKLRUFJQVXOBDCLUAALOMT5GDM"),"SentMessage=>Unit (channelId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblOA4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colQA4BGTURUTOBDANVABIFNQAABA")},new String[]{"channelId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXVZ6UDCUVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprW77ZPPNK4ZBGROWGY7BIPQOWHE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6ME4ANKUVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprPCNPIO4LEJCCRPJQFVPMPDTWWM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZ6YHZV7VUNFDNMIN56S6D274BY")},
			false,false,false);
}

/* Radix::PersoComm::SentMessage - Web Executable*/

/*Radix::PersoComm::SentMessage-Entity Class*/

package org.radixware.ads.PersoComm.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage")
public interface SentMessage {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

		/*Radix::PersoComm::SentMessageGroup:needReplace:needReplace-Presentation Property*/




		public class NeedReplace extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
			public NeedReplace(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:needReplace:needReplace")
			public  Bool getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:needReplace:needReplace")
			public   void setValue(Bool val) {
				Value = val;
			}
		}
		public NeedReplace getNeedReplace(){return (NeedReplace)getProperty(pgpZPVAQNVZ6ZDMNGIE2TONU4DGN4);}

		/*Radix::PersoComm::SentMessageGroup:newChannel:newChannel-Presentation Property*/




		public class NewChannel extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
			public NewChannel(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newChannel:newChannel")
			public  Int getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newChannel:newChannel")
			public   void setValue(Int val) {
				Value = val;
			}
		}
		public NewChannel getNewChannel(){return (NewChannel)getProperty(pgpBCGHBBXGQ5EHVBZX3WEM7SN2J4);}

		/*Radix::PersoComm::SentMessageGroup:newDestinationAddress:newDestinationAddress-Presentation Property*/




		public class NewDestinationAddress extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
			public NewDestinationAddress(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newDestinationAddress:newDestinationAddress")
			public  Str getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newDestinationAddress:newDestinationAddress")
			public   void setValue(Str val) {
				Value = val;
			}
		}
		public NewDestinationAddress getNewDestinationAddress(){return (NewDestinationAddress)getProperty(pgpCGVHXUT7GBG6DJFMHIYKYJESIA);}

		/*Radix::PersoComm::SentMessageGroup:newDueTime:newDueTime-Presentation Property*/




		public class NewDueTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
			public NewDueTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newDueTime:newDueTime")
			public  java.sql.Timestamp getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:newDueTime:newDueTime")
			public   void setValue(java.sql.Timestamp val) {
				Value = val;
			}
		}
		public NewDueTime getNewDueTime(){return (NewDueTime)getProperty(pgpZZXMOBMPTBBZNI4ADXXRWDLDFU);}

		/*Radix::PersoComm::SentMessageGroup:oldChannel:oldChannel-Presentation Property*/




		public class OldChannel extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
			public OldChannel(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:oldChannel:oldChannel")
			public  Str getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:oldChannel:oldChannel")
			public   void setValue(Str val) {
				Value = val;
			}
		}
		public OldChannel getOldChannel(){return (OldChannel)getProperty(pgpIA2K5ICLERFDDHDMKXOOCSDRQU);}

		/*Radix::PersoComm::SentMessageGroup:oldDestinationAddress:oldDestinationAddress-Presentation Property*/




		public class OldDestinationAddress extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
			public OldDestinationAddress(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:oldDestinationAddress:oldDestinationAddress")
			public  Str getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessageGroup:oldDestinationAddress:oldDestinationAddress")
			public   void setValue(Str val) {
				Value = val;
			}
		}
		public OldDestinationAddress getOldDestinationAddress(){return (OldDestinationAddress)getProperty(pgpADZWSYA755CZHBIHTIOO5GVYRI);}

















		public org.radixware.ads.PersoComm.web.SentMessage.SentMessage_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.PersoComm.web.SentMessage.SentMessage_DefaultModel )  super.getEntity(i);}
	}































































































































































































































































	/*Radix::PersoComm::SentMessage:destEntityGuid:destEntityGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:destEntityGuid:destEntityGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:destEntityGuid:destEntityGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DestEntityGuid getDestEntityGuid();
	/*Radix::PersoComm::SentMessage:sendCallbackData:sendCallbackData-Presentation Property*/


	public class SendCallbackData extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SendCallbackData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sendCallbackData:sendCallbackData")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sendCallbackData:sendCallbackData")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SendCallbackData getSendCallbackData();
	/*Radix::PersoComm::SentMessage:sendError:sendError-Presentation Property*/


	public class SendError extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SendError(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sendError:sendError")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sendError:sendError")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SendError getSendError();
	/*Radix::PersoComm::SentMessage:routingKey:routingKey-Presentation Property*/


	public class RoutingKey extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RoutingKey(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:routingKey:routingKey")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:routingKey:routingKey")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoutingKey getRoutingKey();
	/*Radix::PersoComm::SentMessage:lastDeliveryStatusChangeDate:lastDeliveryStatusChangeDate-Presentation Property*/


	public class LastDeliveryStatusChangeDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastDeliveryStatusChangeDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:lastDeliveryStatusChangeDate:lastDeliveryStatusChangeDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:lastDeliveryStatusChangeDate:lastDeliveryStatusChangeDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastDeliveryStatusChangeDate getLastDeliveryStatusChangeDate();
	/*Radix::PersoComm::SentMessage:smppBytesSent:smppBytesSent-Presentation Property*/


	public class SmppBytesSent extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SmppBytesSent(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppBytesSent:smppBytesSent")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppBytesSent:smppBytesSent")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SmppBytesSent getSmppBytesSent();
	/*Radix::PersoComm::SentMessage:deliveryExpTimeMillis:deliveryExpTimeMillis-Presentation Property*/


	public class DeliveryExpTimeMillis extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public DeliveryExpTimeMillis(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryExpTimeMillis:deliveryExpTimeMillis")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryExpTimeMillis:deliveryExpTimeMillis")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DeliveryExpTimeMillis getDeliveryExpTimeMillis();
	/*Radix::PersoComm::SentMessage:deliveryCallbackClassName:deliveryCallbackClassName-Presentation Property*/


	public class DeliveryCallbackClassName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DeliveryCallbackClassName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackClassName:deliveryCallbackClassName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackClassName:deliveryCallbackClassName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DeliveryCallbackClassName getDeliveryCallbackClassName();
	/*Radix::PersoComm::SentMessage:stageNo:stageNo-Presentation Property*/


	public class StageNo extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public StageNo(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:stageNo:stageNo")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:stageNo:stageNo")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public StageNo getStageNo();
	/*Radix::PersoComm::SentMessage:deliveryStatus:deliveryStatus-Presentation Property*/


	public class DeliveryStatus extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DeliveryStatus(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EPersoCommDeliveryStatus dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPersoCommDeliveryStatus ? (org.radixware.kernel.common.enums.EPersoCommDeliveryStatus)x : org.radixware.kernel.common.enums.EPersoCommDeliveryStatus.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EPersoCommDeliveryStatus> getValClass(){
			return org.radixware.kernel.common.enums.EPersoCommDeliveryStatus.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EPersoCommDeliveryStatus dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPersoCommDeliveryStatus ? (org.radixware.kernel.common.enums.EPersoCommDeliveryStatus)x : org.radixware.kernel.common.enums.EPersoCommDeliveryStatus.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryStatus:deliveryStatus")
		public  org.radixware.kernel.common.enums.EPersoCommDeliveryStatus getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryStatus:deliveryStatus")
		public   void setValue(org.radixware.kernel.common.enums.EPersoCommDeliveryStatus val) {
			Value = val;
		}
	}
	public DeliveryStatus getDeliveryStatus();
	/*Radix::PersoComm::SentMessage:sentTime:sentTime-Presentation Property*/


	public class SentTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public SentTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sentTime:sentTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sentTime:sentTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public SentTime getSentTime();
	/*Radix::PersoComm::SentMessage:destPid:destPid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:destPid:destPid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:destPid:destPid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DestPid getDestPid();
	/*Radix::PersoComm::SentMessage:ussdServiceOp:ussdServiceOp-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:ussdServiceOp:ussdServiceOp")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:ussdServiceOp:ussdServiceOp")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public UssdServiceOp getUssdServiceOp();
	/*Radix::PersoComm::SentMessage:dueTime:dueTime-Presentation Property*/


	public class DueTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public DueTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:dueTime:dueTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:dueTime:dueTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public DueTime getDueTime();
	/*Radix::PersoComm::SentMessage:isUssd:isUssd-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:isUssd:isUssd")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:isUssd:isUssd")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsUssd getIsUssd();
	/*Radix::PersoComm::SentMessage:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::PersoComm::SentMessage:smppPartsSent:smppPartsSent-Presentation Property*/


	public class SmppPartsSent extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SmppPartsSent(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppPartsSent:smppPartsSent")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppPartsSent:smppPartsSent")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SmppPartsSent getSmppPartsSent();
	/*Radix::PersoComm::SentMessage:subject:subject-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:subject:subject")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:subject:subject")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Subject getSubject();
	/*Radix::PersoComm::SentMessage:body:body-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:body:body")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:body:body")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Body getBody();
	/*Radix::PersoComm::SentMessage:importance:importance-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:importance:importance")
		public  org.radixware.kernel.common.enums.EPersoCommImportance getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:importance:importance")
		public   void setValue(org.radixware.kernel.common.enums.EPersoCommImportance val) {
			Value = val;
		}
	}
	public Importance getImportance();
	/*Radix::PersoComm::SentMessage:createTime:createTime-Presentation Property*/


	public class CreateTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public CreateTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:createTime:createTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:createTime:createTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public CreateTime getCreateTime();
	/*Radix::PersoComm::SentMessage:sendCallbackRequired:sendCallbackRequired-Presentation Property*/


	public class SendCallbackRequired extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SendCallbackRequired(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sendCallbackRequired:sendCallbackRequired")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sendCallbackRequired:sendCallbackRequired")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SendCallbackRequired getSendCallbackRequired();
	/*Radix::PersoComm::SentMessage:expireTime:expireTime-Presentation Property*/


	public class ExpireTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public ExpireTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:expireTime:expireTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:expireTime:expireTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public ExpireTime getExpireTime();
	/*Radix::PersoComm::SentMessage:channelKind:channelKind-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:channelKind:channelKind")
		public  org.radixware.ads.PersoComm.common.ChannelKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:channelKind:channelKind")
		public   void setValue(org.radixware.ads.PersoComm.common.ChannelKind val) {
			Value = val;
		}
	}
	public ChannelKind getChannelKind();
	/*Radix::PersoComm::SentMessage:address:address-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:address:address")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:address:address")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Address getAddress();
	/*Radix::PersoComm::SentMessage:sourceEntityGuid:sourceEntityGuid-Presentation Property*/


	public class SourceEntityGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SourceEntityGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sourceEntityGuid:sourceEntityGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sourceEntityGuid:sourceEntityGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SourceEntityGuid getSourceEntityGuid();
	/*Radix::PersoComm::SentMessage:sourcePid:sourcePid-Presentation Property*/


	public class SourcePid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SourcePid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sourcePid:sourcePid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sourcePid:sourcePid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SourcePid getSourcePid();
	/*Radix::PersoComm::SentMessage:sourceMsgId:sourceMsgId-Presentation Property*/


	public class SourceMsgId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SourceMsgId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sourceMsgId:sourceMsgId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sourceMsgId:sourceMsgId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SourceMsgId getSourceMsgId();
	/*Radix::PersoComm::SentMessage:channelId:channelId-Presentation Property*/


	public class ChannelId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ChannelId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:channelId:channelId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:channelId:channelId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ChannelId getChannelId();
	/*Radix::PersoComm::SentMessage:smppMessageId:smppMessageId-Presentation Property*/


	public class SmppMessageId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SmppMessageId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppMessageId:smppMessageId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppMessageId:smppMessageId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SmppMessageId getSmppMessageId();
	/*Radix::PersoComm::SentMessage:maskedBody:maskedBody-Presentation Property*/


	public class MaskedBody extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public MaskedBody(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:maskedBody:maskedBody")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:maskedBody:maskedBody")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MaskedBody getMaskedBody();
	/*Radix::PersoComm::SentMessage:smppCharsSent:smppCharsSent-Presentation Property*/


	public class SmppCharsSent extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SmppCharsSent(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppCharsSent:smppCharsSent")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:smppCharsSent:smppCharsSent")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SmppCharsSent getSmppCharsSent();
	/*Radix::PersoComm::SentMessage:prevStageMessageId:prevStageMessageId-Presentation Property*/


	public class PrevStageMessageId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PrevStageMessageId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:prevStageMessageId:prevStageMessageId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:prevStageMessageId:prevStageMessageId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PrevStageMessageId getPrevStageMessageId();
	/*Radix::PersoComm::SentMessage:deliveryCallbackRequired:deliveryCallbackRequired-Presentation Property*/


	public class DeliveryCallbackRequired extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public DeliveryCallbackRequired(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackRequired:deliveryCallbackRequired")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackRequired:deliveryCallbackRequired")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DeliveryCallbackRequired getDeliveryCallbackRequired();
	/*Radix::PersoComm::SentMessage:deliveryCallbackMethodName:deliveryCallbackMethodName-Presentation Property*/


	public class DeliveryCallbackMethodName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DeliveryCallbackMethodName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackMethodName:deliveryCallbackMethodName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackMethodName:deliveryCallbackMethodName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DeliveryCallbackMethodName getDeliveryCallbackMethodName();
	/*Radix::PersoComm::SentMessage:encoding:encoding-Presentation Property*/


	public class Encoding extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Encoding(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.ESmppEncoding dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.ESmppEncoding ? (org.radixware.kernel.common.enums.ESmppEncoding)x : org.radixware.kernel.common.enums.ESmppEncoding.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.ESmppEncoding> getValClass(){
			return org.radixware.kernel.common.enums.ESmppEncoding.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.ESmppEncoding dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.ESmppEncoding ? (org.radixware.kernel.common.enums.ESmppEncoding)x : org.radixware.kernel.common.enums.ESmppEncoding.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:encoding:encoding")
		public  org.radixware.kernel.common.enums.ESmppEncoding getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:encoding:encoding")
		public   void setValue(org.radixware.kernel.common.enums.ESmppEncoding val) {
			Value = val;
		}
	}
	public Encoding getEncoding();
	/*Radix::PersoComm::SentMessage:deliveryCallbackData:deliveryCallbackData-Presentation Property*/


	public class DeliveryCallbackData extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DeliveryCallbackData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackData:deliveryCallbackData")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:deliveryCallbackData:deliveryCallbackData")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DeliveryCallbackData getDeliveryCallbackData();
	/*Radix::PersoComm::SentMessage:prevStageSentMessageId:prevStageSentMessageId-Presentation Property*/


	public class PrevStageSentMessageId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PrevStageSentMessageId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:prevStageSentMessageId:prevStageSentMessageId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:prevStageSentMessageId:prevStageSentMessageId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PrevStageSentMessageId getPrevStageSentMessageId();
	/*Radix::PersoComm::SentMessage:channelUnit:channelUnit-Presentation Property*/


	public class ChannelUnit extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public ChannelUnit(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:channelUnit:channelUnit")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:channelUnit:channelUnit")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ChannelUnit getChannelUnit();
	/*Radix::PersoComm::SentMessage:newChannelId:newChannelId-Presentation Property*/


	public class NewChannelId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public NewChannelId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:newChannelId:newChannelId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:newChannelId:newChannelId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public NewChannelId getNewChannelId();
	/*Radix::PersoComm::SentMessage:newAddress:newAddress-Presentation Property*/


	public class NewAddress extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public NewAddress(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:newAddress:newAddress")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:newAddress:newAddress")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public NewAddress getNewAddress();
	/*Radix::PersoComm::SentMessage:displayedBody:displayedBody-Presentation Property*/


	public class DisplayedBody extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DisplayedBody(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:displayedBody:displayedBody")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:displayedBody:displayedBody")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DisplayedBody getDisplayedBody();
	/*Radix::PersoComm::SentMessage:sourceTitle:sourceTitle-Presentation Property*/


	public class SourceTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SourceTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sourceTitle:sourceTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:sourceTitle:sourceTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SourceTitle getSourceTitle();
	/*Radix::PersoComm::SentMessage:prevStageNotSentMessageId:prevStageNotSentMessageId-Presentation Property*/


	public class PrevStageNotSentMessageId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PrevStageNotSentMessageId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:prevStageNotSentMessageId:prevStageNotSentMessageId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:prevStageNotSentMessageId:prevStageNotSentMessageId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PrevStageNotSentMessageId getPrevStageNotSentMessageId();
	/*Radix::PersoComm::SentMessage:newDueTime:newDueTime-Presentation Property*/


	public class NewDueTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public NewDueTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:newDueTime:newDueTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:newDueTime:newDueTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public NewDueTime getNewDueTime();
	public static class ViewPrevStageMessage extends org.radixware.kernel.common.client.models.items.Command{
		protected ViewPrevStageMessage(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class ViewSource extends org.radixware.kernel.common.client.models.items.Command{
		protected ViewSource(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}



}

/* Radix::PersoComm::SentMessage - Web Meta*/

/*Radix::PersoComm::SentMessage-Entity Class*/

package org.radixware.ads.PersoComm.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class SentMessage_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::PersoComm::SentMessage:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
			"Radix::PersoComm::SentMessage",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("img2BKM3KZ6XUI3SH3FA3HYB5JEVRFFPADL"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6ME4ANKUVXOBDCLUAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQG2BDVKTVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7O7GVZCTVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQG2BDVKTVXOBDCLUAALOMT5GDM"),28677,

			/*Radix::PersoComm::SentMessage:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::PersoComm::SentMessage:destEntityGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6HSMYFOSV5EWPD575PUINSYHEM"),
						"destEntityGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJMALNOQNDRDPPLQUHTON7FS7GY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
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

						/*Radix::PersoComm::SentMessage:destEntityGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:sendError:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7WEBG24RUTOBDANVABIFNQAABA"),
						"sendError",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEN3N7FCTVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:sendError:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:sentTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIE2KOWURUTOBDANVABIFNQAABA"),
						"sentTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH52JHJKTVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:sentTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:destPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJP4VCQ3UPBA4PIHAITVYDWTEV4"),
						"destPid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZM6ZF5XSLFH2JPPEBHLWXTWVZQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
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

						/*Radix::PersoComm::SentMessage:destPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:dueTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO44BGTURUTOBDANVABIFNQAABA"),
						"dueTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5HPRI6CTVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:dueTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOI4BGTURUTOBDANVABIFNQAABA"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKTDKJCSTVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:subject:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOM4BGTURUTOBDANVABIFNQAABA"),
						"subject",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTY2HVTCTVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:subject:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:body:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOQ4BGTURUTOBDANVABIFNQAABA"),
						"body",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSL7OITCTVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
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

						/*Radix::PersoComm::SentMessage:body:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:importance:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOU4BGTURUTOBDANVABIFNQAABA"),
						"importance",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKXDKJCSTVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
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

						/*Radix::PersoComm::SentMessage:importance:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTUGILCRUVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:createTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOY4BGTURUTOBDANVABIFNQAABA"),
						"createTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUGCNU2STVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:createTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:expireTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPA4BGTURUTOBDANVABIFNQAABA"),
						"expireTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRLWTTA2TVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:expireTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:channelKind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPE4BGTURUTOBDANVABIFNQAABA"),
						"channelKind",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls75CXGWCTVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),
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

						/*Radix::PersoComm::SentMessage:channelKind:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:address:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI4BGTURUTOBDANVABIFNQAABA"),
						"address",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFB2ZQEKQVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:address:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:sourceEntityGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPM4BGTURUTOBDANVABIFNQAABA"),
						"sourceEntityGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFWBKJSPCFNF6TAHCOWVR5ZNEOU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:sourceEntityGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:sourcePid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPQ4BGTURUTOBDANVABIFNQAABA"),
						"sourcePid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFZUUB3MYEVEFJI4BQQU7QEH4OI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:sourcePid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:sourceMsgId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU4BGTURUTOBDANVABIFNQAABA"),
						"sourceMsgId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVKJDCACBFJDD5BMJ5DBVFYIQGI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:sourceMsgId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:channelId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQA4BGTURUTOBDANVABIFNQAABA"),
						"channelId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRGL45TZYUVCGDGYDSHEYJQB724"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:channelId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:channelUnit:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6AME2JOY6ZAE5NLCLB5A6HFYEE"),
						"channelUnit",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWBZRDZKYOZENVDV6PDPCPQLCDA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						20,
						null,
						null,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprGVZBTVH6VXOBDCLVAALOMT5GDM")},
						null,
						null,
						262143,
						262143,false),

					/*Radix::PersoComm::SentMessage:sourceTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTBHNMZQEJFHUJEKE7DIBKEY6BU"),
						"sourceTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSIXLQQYFJRFQHK5TZKGZ5RCUOQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:sourceTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:encoding:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWRXU6KICKFCKBE5YNLT55VTE54"),
						"encoding",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS2YX3WJISBE6VKMQKIOJVHLUOU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsSMFAHKLBOBGHVDYOXCSGSNFDME"),
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

						/*Radix::PersoComm::SentMessage:encoding:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsSMFAHKLBOBGHVDYOXCSGSNFDME"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:smppBytesSent:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBDMFQL73SVFQJIEN2O5QNJLRR4"),
						"smppBytesSent",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK27XN3N6MRC2NIKVIWDMUWTG34"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:smppBytesSent:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:smppCharsSent:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT2UDNHX6OBH5NC5HWV3HOJY2BQ"),
						"smppCharsSent",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5GQO5DOB4RG5XA7SEJM27LPEUQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:smppCharsSent:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:smppPartsSent:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOIBT34FIG5G2DH6GQ3DBY4VXNQ"),
						"smppPartsSent",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7BSDZCMRKBD2HPOMART25NE3FM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:smppPartsSent:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:maskedBody:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSMQR44I5N5AGHIJMQFF6TUO4RU"),
						"maskedBody",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4FKQ7WEPPRFFNEHP42Q7UEC5GU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
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

						/*Radix::PersoComm::SentMessage:maskedBody:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:displayedBody:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNLFYGIFUGZANBP23DMNK7U2XRI"),
						"displayedBody",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ7GKJ45N3NHKVB5TGFLSW5GCC4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:displayedBody:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:routingKey:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAKIXW6SGNJBTFMGDGNF2DG3FJM"),
						"routingKey",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFVT62RWB3VE6LLFLRLZ33VYIOM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:routingKey:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:deliveryStatus:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI3F7THEYXRF35CL4TRW2KAI7QU"),
						"deliveryStatus",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPGTVBLMW6JFQLISGJRLJ6NTLCE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsBNGKCKPU5BEIVNZ73TQDMBXZ5Q"),
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

						/*Radix::PersoComm::SentMessage:deliveryStatus:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsBNGKCKPU5BEIVNZ73TQDMBXZ5Q"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:lastDeliveryStatusChangeDate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAYKPP3DRG5DSJM6JICMM3UWIT4"),
						"lastDeliveryStatusChangeDate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls36Q33ZP3NZDXFGTXEEIJI7LKKQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:lastDeliveryStatusChangeDate:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:smppMessageId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR4UDIIE2IFC33IEFMZOEATMYM4"),
						"smppMessageId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMC3AKZHBKNFMFNQFWZOV44SSWE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:smppMessageId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,64,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:isUssd:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOH42BANGWVAY3EXEMXZZNO7IRM"),
						"isUssd",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7S6LUNU5IJHIZCV3DO3UTRIWAI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:isUssd:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:ussdServiceOp:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLK7JQVGCFRAV3NJTQFYX7VMBYY"),
						"ussdServiceOp",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYDDFWZ3Z5VFB7FDON5T5TXNN6A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:ussdServiceOp:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:sendCallbackRequired:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP36RBYIF2FF75HEYQO3KV23FNA"),
						"sendCallbackRequired",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:sendCallbackRequired:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-9L,9L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:deliveryCallbackClassName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHBKOTQRWA5H73OKJK7RBV7TF4Y"),
						"deliveryCallbackClassName",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:deliveryCallbackClassName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:deliveryCallbackMethodName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVVKDXIQPMJG7BPPIEW4H3LH7FE"),
						"deliveryCallbackMethodName",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:deliveryCallbackMethodName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:deliveryCallbackRequired:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVTBL3E6TJVGFXK3IZYZAAVN5DU"),
						"deliveryCallbackRequired",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:deliveryCallbackRequired:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-9L,9L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:deliveryExpTimeMillis:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGEUMW2NUSFF3RKAB64DOQLD75M"),
						"deliveryExpTimeMillis",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:deliveryExpTimeMillis:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:deliveryCallbackData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXYWM73QCI5CMZIENZ7QXRZOWME"),
						"deliveryCallbackData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:deliveryCallbackData:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:sendCallbackData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6Z74VVJ3NFHBXKE5IS2NIYT3OM"),
						"sendCallbackData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:sendCallbackData:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:stageNo:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHXFPFBMDIVFBZJX6347JRYIOYA"),
						"stageNo",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOPRUHXJXDNAZVNYOSHUYB3SIGA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:stageNo:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:prevStageMessageId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT6MBYYMFXVFT7FTATBQSBIMSTI"),
						"prevStageMessageId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXQCOX3OSXRAFBBSALOPTLVQU3M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::SentMessage:prevStageMessageId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:prevStageNotSentMessageId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUBVGYBJAPRDXRLHAGRDIYBZUD4"),
						"prevStageNotSentMessageId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:prevStageNotSentMessageId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:prevStageSentMessageId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd55GQ3KHV5JC4VMHVQB4ETZQAQ4"),
						"prevStageSentMessageId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:prevStageSentMessageId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:newAddress:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLFJIDL4YHBF3TN3KPU6DZSJ4WM"),
						"newAddress",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZGMXBII6VNFQNIIVXDYMTQQFXM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:newAddress:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:newChannelId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJAV7SM2CSJGYJNNRHAKGULO72M"),
						"newChannelId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTAKKNILNXBAJZBM5VKXIV676KU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::SentMessage:newChannelId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::SentMessage:newDueTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVNXI6AEFQRHGFAAXNLAEE5EJXM"),
						"newDueTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRMSJ3POROFAGZIEDFSCJUOJFQY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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

						/*Radix::PersoComm::SentMessage:newDueTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::PersoComm::SentMessage:ViewSource-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdKOKLUBT4QBG2VNTBDPHFESHHAE"),
						"ViewSource",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHU5MPZAJGJEPRIE2JM4WDPWS3Q"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgEBTCSRR6XJGUJHPO2J4QIENJSI"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.LOCAL,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTBHNMZQEJFHUJEKE7DIBKEY6BU")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::PersoComm::SentMessage:ViewPrevStageMessage-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd2BSOOFGDXRCNDD3T7J23FVJEQY"),
						"ViewPrevStageMessage",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6CPITACTTZCU5OW4JREOL6AAJU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgEBTCSRR6XJGUJHPO2J4QIENJSI"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.LOCAL,
						true,
						false,
						true,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colT6MBYYMFXVFT7FTATBQSBIMSTI")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT)
			},
			new org.radixware.kernel.common.client.meta.filters.RadFilterDef[]{

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::PersoComm::SentMessage:Address-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltDZGN4HVB2FDHZAGCMMFLBLUFJE"),
						"Address",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2FZN3F5NFJADJKNVF3QD6ZMHKA"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblOA4BGTURUTOBDANVABIFNQAABA\" PropId=\"colQA4BGTURUTOBDANVABIFNQAABA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:EntityRefParameter ParamId=\"prmWCGBWZWPJRAAVOJSIF3HLJGDWI\" ReferencedTableId=\"tbl5HP4XTP3EGWDBRCRAAIT4AGD7E\" PidTranslationMode=\"AsStr\"/></xsc:Item><xsc:Item><xsc:Sql>\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblOA4BGTURUTOBDANVABIFNQAABA\" PropId=\"colPI4BGTURUTOBDANVABIFNQAABA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> like </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmM67CPG3XJZF3RDDBJ2VOTPXZ7Y\"/></xsc:Item></xsc:Sqml>",
						null,
						null,
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt2TAMIGP57VAD3B5EDXWVX3L4LI"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmWCGBWZWPJRAAVOJSIF3HLJGDWI"),
								"channel",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAXI4C42VR5AGXNJFTA6ZT3BAUQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblOA4BGTURUTOBDANVABIFNQAABA"),
								null,
								org.radixware.kernel.common.enums.EValType.PARENT_REF,
								null,
								null,
								true,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVCLH3TEDVTOBDCLTAALOMT5GDM"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("spr42ALGCTMUZB6BICZW7XRIE5DOA")),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmM67CPG3XJZF3RDDBJ2VOTPXZ7Y"),
								"address",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQGBDL2FHRVBCFC2FMP6JRKEBP4"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblOA4BGTURUTOBDANVABIFNQAABA"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								true,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::PersoComm::SentMessage:Address:Editor Pages-Filter Pages*/
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

								/*Radix::PersoComm::SentMessage:Address:Main-Editor Page*/

								 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg3A3GPC5HLNAKFD42VJXSBNPSDA"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmM67CPG3XJZF3RDDBJ2VOTPXZ7Y"),1,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmWCGBWZWPJRAAVOJSIF3HLJGDWI"),0,0,1,false,false)
								},null)
						},
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
							new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg3A3GPC5HLNAKFD42VJXSBNPSDA"))}
						,
						null)
			},
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::PersoComm::SentMessage:Sent-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt2TAMIGP57VAD3B5EDXWVX3L4LI"),
						"Sent",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUWT55S5AIVCDZJTPQYAH7YB66I"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIE2KOWURUTOBDANVABIFNQAABA"),
								true)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refKKLRUFJQVXOBDCLUAALOMT5GDM"),"SentMessage=>Unit (channelId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblOA4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colQA4BGTURUTOBDANVABIFNQAABA")},new String[]{"channelId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXVZ6UDCUVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprW77ZPPNK4ZBGROWGY7BIPQOWHE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6ME4ANKUVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprPCNPIO4LEJCCRPJQFVPMPDTWWM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZ6YHZV7VUNFDNMIN56S6D274BY")},
			false,false,false);
}

/* Radix::PersoComm::SentMessage:General - Desktop Meta*/

/*Radix::PersoComm::SentMessage:General-Editor Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXVZ6UDCUVXOBDCLUAALOMT5GDM"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblOA4BGTURUTOBDANVABIFNQAABA"),
	null,
	null,

	/*Radix::PersoComm::SentMessage:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::PersoComm::SentMessage:General:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgFAWCWE2UVXOBDCLUAALOMT5GDM"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFTFIUCIUEVEUNBJSBOXRFNZ3YE"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOI4BGTURUTOBDANVABIFNQAABA"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOY4BGTURUTOBDANVABIFNQAABA"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIE2KOWURUTOBDANVABIFNQAABA"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7WEBG24RUTOBDANVABIFNQAABA"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOU4BGTURUTOBDANVABIFNQAABA"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOM4BGTURUTOBDANVABIFNQAABA"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI4BGTURUTOBDANVABIFNQAABA"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPE4BGTURUTOBDANVABIFNQAABA"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6AME2JOY6ZAE5NLCLB5A6HFYEE"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTBHNMZQEJFHUJEKE7DIBKEY6BU"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO44BGTURUTOBDANVABIFNQAABA"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWRXU6KICKFCKBE5YNLT55VTE54"),0,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBDMFQL73SVFQJIEN2O5QNJLRR4"),0,15,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT2UDNHX6OBH5NC5HWV3HOJY2BQ"),0,16,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOIBT34FIG5G2DH6GQ3DBY4VXNQ"),0,17,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNLFYGIFUGZANBP23DMNK7U2XRI"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI3F7THEYXRF35CL4TRW2KAI7QU"),0,18,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAYKPP3DRG5DSJM6JICMM3UWIT4"),0,19,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOH42BANGWVAY3EXEMXZZNO7IRM"),0,13,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLK7JQVGCFRAV3NJTQFYX7VMBYY"),0,14,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHXFPFBMDIVFBZJX6347JRYIOYA"),0,20,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT6MBYYMFXVFT7FTATBQSBIMSTI"),0,21,1,false,false)
			},null),

			/*Radix::PersoComm::SentMessage:General:Attachements-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgFKLN5YXMVXOBDCLVAALOMT5GDM"),"Attachements",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFOLN5YXMVXOBDCLVAALOMT5GDM"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiHARKHSXMVXOBDCLVAALOMT5GDM")),

			/*Radix::PersoComm::SentMessage:General:System-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHSK2VCSWOVFJJDXZZQSLCAO5XY"),"System",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXDY3HLVG7RGWLLGGHQG2I4TJRY"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6HSMYFOSV5EWPD575PUINSYHEM"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJP4VCQ3UPBA4PIHAITVYDWTEV4"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU4BGTURUTOBDANVABIFNQAABA"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPM4BGTURUTOBDANVABIFNQAABA"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPQ4BGTURUTOBDANVABIFNQAABA"),0,3,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgFAWCWE2UVXOBDCLUAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHSK2VCSWOVFJJDXZZQSLCAO5XY")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgFKLN5YXMVXOBDCLVAALOMT5GDM"))}
	,

	/*Radix::PersoComm::SentMessage:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::PersoComm::SentMessage:General:Attachments-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiHARKHSXMVXOBDCLVAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),null,
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
/* Radix::PersoComm::SentMessage:General - Web Meta*/

/*Radix::PersoComm::SentMessage:General-Editor Presentation*/

package org.radixware.ads.PersoComm.web;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXVZ6UDCUVXOBDCLUAALOMT5GDM"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblOA4BGTURUTOBDANVABIFNQAABA"),
	null,
	null,

	/*Radix::PersoComm::SentMessage:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::PersoComm::SentMessage:General:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgFAWCWE2UVXOBDCLUAALOMT5GDM"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFTFIUCIUEVEUNBJSBOXRFNZ3YE"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOI4BGTURUTOBDANVABIFNQAABA"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOY4BGTURUTOBDANVABIFNQAABA"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIE2KOWURUTOBDANVABIFNQAABA"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7WEBG24RUTOBDANVABIFNQAABA"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOU4BGTURUTOBDANVABIFNQAABA"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOM4BGTURUTOBDANVABIFNQAABA"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI4BGTURUTOBDANVABIFNQAABA"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPE4BGTURUTOBDANVABIFNQAABA"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6AME2JOY6ZAE5NLCLB5A6HFYEE"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTBHNMZQEJFHUJEKE7DIBKEY6BU"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO44BGTURUTOBDANVABIFNQAABA"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWRXU6KICKFCKBE5YNLT55VTE54"),0,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBDMFQL73SVFQJIEN2O5QNJLRR4"),0,15,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT2UDNHX6OBH5NC5HWV3HOJY2BQ"),0,16,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOIBT34FIG5G2DH6GQ3DBY4VXNQ"),0,17,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNLFYGIFUGZANBP23DMNK7U2XRI"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI3F7THEYXRF35CL4TRW2KAI7QU"),0,18,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAYKPP3DRG5DSJM6JICMM3UWIT4"),0,19,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOH42BANGWVAY3EXEMXZZNO7IRM"),0,13,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLK7JQVGCFRAV3NJTQFYX7VMBYY"),0,14,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHXFPFBMDIVFBZJX6347JRYIOYA"),0,20,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT6MBYYMFXVFT7FTATBQSBIMSTI"),0,21,1,false,false)
			},null),

			/*Radix::PersoComm::SentMessage:General:Attachements-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgFKLN5YXMVXOBDCLVAALOMT5GDM"),"Attachements",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFOLN5YXMVXOBDCLVAALOMT5GDM"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiHARKHSXMVXOBDCLVAALOMT5GDM")),

			/*Radix::PersoComm::SentMessage:General:System-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHSK2VCSWOVFJJDXZZQSLCAO5XY"),"System",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXDY3HLVG7RGWLLGGHQG2I4TJRY"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6HSMYFOSV5EWPD575PUINSYHEM"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJP4VCQ3UPBA4PIHAITVYDWTEV4"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU4BGTURUTOBDANVABIFNQAABA"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPM4BGTURUTOBDANVABIFNQAABA"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPQ4BGTURUTOBDANVABIFNQAABA"),0,3,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgFAWCWE2UVXOBDCLUAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHSK2VCSWOVFJJDXZZQSLCAO5XY")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgFKLN5YXMVXOBDCLVAALOMT5GDM"))}
	,

	/*Radix::PersoComm::SentMessage:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::PersoComm::SentMessage:General:Attachments-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiHARKHSXMVXOBDCLVAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),null,
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
/* Radix::PersoComm::SentMessage:General:Model - Desktop Executable*/

/*Radix::PersoComm::SentMessage:General:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:General:Model")
public class General:Model  extends org.radixware.ads.PersoComm.explorer.SentMessage.SentMessage_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::PersoComm::SentMessage:General:Model:Nested classes-Nested Classes*/

	/*Radix::PersoComm::SentMessage:General:Model:Properties-Properties*/

	/*Radix::PersoComm::SentMessage:General:Model:Methods-Methods*/

	/*Radix::PersoComm::SentMessage:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:General:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		if(!isInSelectorRowContext()){
		    final boolean isSms = channelKind.Value == ChannelKind:Sms;
		    encoding.setVisible(isSms);
		    isUssd.setVisible(isSms);
		    ussdServiceOp.setVisible(isSms);
		    smppBytesSent.setVisible(isSms);
		    smppCharsSent.setVisible(isSms);
		    smppPartsSent.setVisible(isSms);
		    getCommand(idof[SentMessage:ViewSource]).setVisible(Explorer.Utils::DialogUtils.canRunEditor(sourceEntityGuid.Value, sourcePid.Value));
		}

	}

	/*Radix::PersoComm::SentMessage:General:Model:ViewSource-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:General:Model:ViewSource")
	public published  void ViewSource (org.radixware.ads.PersoComm.explorer.SentMessage.ViewSource command, org.radixware.kernel.common.types.Id propertyId) {
		Explorer.Utils::DialogUtils.runEditor(Environment,sourceEntityGuid.Value, sourcePid.Value);
	}

	/*Radix::PersoComm::SentMessage:General:Model:ViewPrevStageMessage-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:General:Model:ViewPrevStageMessage")
	public published  void ViewPrevStageMessage (org.radixware.ads.PersoComm.explorer.SentMessage.ViewPrevStageMessage command, org.radixware.kernel.common.types.Id propertyId) {
		if (prevStageNotSentMessageId.Value != null) {
		    Explorer.Utils::DialogUtils.runEditor(Environment, idof[OutMessage].toString(), prevStageNotSentMessageId.Value.toString());
		} else if (prevStageSentMessageId.Value != null) {
		    Explorer.Utils::DialogUtils.runEditor(Environment, idof[SentMessage].toString(), prevStageSentMessageId.Value.toString());
		}
	}
	public final class ViewPrevStageMessage extends org.radixware.ads.PersoComm.explorer.SentMessage.ViewPrevStageMessage{
		protected ViewPrevStageMessage(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ViewPrevStageMessage( this, propertyId );
		}

	}

	public final class ViewSource extends org.radixware.ads.PersoComm.explorer.SentMessage.ViewSource{
		protected ViewSource(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ViewSource( this, propertyId );
		}

	}















}

/* Radix::PersoComm::SentMessage:General:Model - Desktop Meta*/

/*Radix::PersoComm::SentMessage:General:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemXVZ6UDCUVXOBDCLUAALOMT5GDM"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::PersoComm::SentMessage:General:Model:Properties-Properties*/
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

/* Radix::PersoComm::SentMessage:General:Model - Web Executable*/

/*Radix::PersoComm::SentMessage:General:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:General:Model")
public class General:Model  extends org.radixware.ads.PersoComm.web.SentMessage.SentMessage_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::PersoComm::SentMessage:General:Model:Nested classes-Nested Classes*/

	/*Radix::PersoComm::SentMessage:General:Model:Properties-Properties*/

	/*Radix::PersoComm::SentMessage:General:Model:Methods-Methods*/

	/*Radix::PersoComm::SentMessage:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:General:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		if(!isInSelectorRowContext()){
		    final boolean isSms = channelKind.Value == ChannelKind:Sms;
		    encoding.setVisible(isSms);
		    isUssd.setVisible(isSms);
		    ussdServiceOp.setVisible(isSms);
		    smppBytesSent.setVisible(isSms);
		    smppCharsSent.setVisible(isSms);
		    smppPartsSent.setVisible(isSms);
		    getCommand(idof[SentMessage:ViewSource]).setVisible(Explorer.Utils::DialogUtils.canRunEditor(sourceEntityGuid.Value, sourcePid.Value));
		}

	}

	/*Radix::PersoComm::SentMessage:General:Model:ViewSource-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:General:Model:ViewSource")
	public published  void ViewSource (org.radixware.ads.PersoComm.web.SentMessage.ViewSource command, org.radixware.kernel.common.types.Id propertyId) {
		Explorer.Utils::DialogUtils.runEditor(Environment,sourceEntityGuid.Value, sourcePid.Value);
	}

	/*Radix::PersoComm::SentMessage:General:Model:ViewPrevStageMessage-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:General:Model:ViewPrevStageMessage")
	public published  void ViewPrevStageMessage (org.radixware.ads.PersoComm.web.SentMessage.ViewPrevStageMessage command, org.radixware.kernel.common.types.Id propertyId) {
		if (prevStageNotSentMessageId.Value != null) {
		    Explorer.Utils::DialogUtils.runEditor(Environment, idof[OutMessage].toString(), prevStageNotSentMessageId.Value.toString());
		} else if (prevStageSentMessageId.Value != null) {
		    Explorer.Utils::DialogUtils.runEditor(Environment, idof[SentMessage].toString(), prevStageSentMessageId.Value.toString());
		}
	}
	public final class ViewPrevStageMessage extends org.radixware.ads.PersoComm.web.SentMessage.ViewPrevStageMessage{
		protected ViewPrevStageMessage(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ViewPrevStageMessage( this, propertyId );
		}

	}

	public final class ViewSource extends org.radixware.ads.PersoComm.web.SentMessage.ViewSource{
		protected ViewSource(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ViewSource( this, propertyId );
		}

	}















}

/* Radix::PersoComm::SentMessage:General:Model - Web Meta*/

/*Radix::PersoComm::SentMessage:General:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemXVZ6UDCUVXOBDCLUAALOMT5GDM"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::PersoComm::SentMessage:General:Model:Properties-Properties*/
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

/* Radix::PersoComm::SentMessage:Create - Desktop Meta*/

/*Radix::PersoComm::SentMessage:Create-Editor Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprW77ZPPNK4ZBGROWGY7BIPQOWHE"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblOA4BGTURUTOBDANVABIFNQAABA"),
			null,
			null,

			/*Radix::PersoComm::SentMessage:Create:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::PersoComm::SentMessage:Create:Main-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgNAAVEJIIEFA7NGFYSBLGYNA3VI"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6OLVYUEJNFG2ZPQLIY3M7JMMUM"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOI4BGTURUTOBDANVABIFNQAABA"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIE2KOWURUTOBDANVABIFNQAABA"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI4BGTURUTOBDANVABIFNQAABA"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOM4BGTURUTOBDANVABIFNQAABA"),0,3,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgNAAVEJIIEFA7NGFYSBLGYNA3VI"))}
			,

			/*Radix::PersoComm::SentMessage:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			2192,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.explorer.SentMessage.SentMessage_DefaultModel(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::PersoComm::SentMessage:Create - Web Meta*/

/*Radix::PersoComm::SentMessage:Create-Editor Presentation*/

package org.radixware.ads.PersoComm.web;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprW77ZPPNK4ZBGROWGY7BIPQOWHE"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblOA4BGTURUTOBDANVABIFNQAABA"),
			null,
			null,

			/*Radix::PersoComm::SentMessage:Create:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::PersoComm::SentMessage:Create:Main-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgNAAVEJIIEFA7NGFYSBLGYNA3VI"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6OLVYUEJNFG2ZPQLIY3M7JMMUM"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOI4BGTURUTOBDANVABIFNQAABA"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIE2KOWURUTOBDANVABIFNQAABA"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI4BGTURUTOBDANVABIFNQAABA"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOM4BGTURUTOBDANVABIFNQAABA"),0,3,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgNAAVEJIIEFA7NGFYSBLGYNA3VI"))}
			,

			/*Radix::PersoComm::SentMessage:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			2192,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.web.SentMessage.SentMessage_DefaultModel(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::PersoComm::SentMessage:General - Desktop Meta*/

/*Radix::PersoComm::SentMessage:General-Selector Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6ME4ANKUVXOBDCLUAALOMT5GDM"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblOA4BGTURUTOBDANVABIFNQAABA"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srt2TAMIGP57VAD3B5EDXWVX3L4LI"),
		null,
		false,
		true,
		null,
		290855,
		null,
		2192,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprW77ZPPNK4ZBGROWGY7BIPQOWHE")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXVZ6UDCUVXOBDCLUAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOI4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6AME2JOY6ZAE5NLCLB5A6HFYEE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOY4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO44BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIE2KOWURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOM4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7WEBG24RUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.STRETCH,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOU4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPM4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPQ4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.explorer.SentMessage.DefaultGroupModel(userSession,this);
	}
}
/* Radix::PersoComm::SentMessage:General - Web Meta*/

/*Radix::PersoComm::SentMessage:General-Selector Presentation*/

package org.radixware.ads.PersoComm.web;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6ME4ANKUVXOBDCLUAALOMT5GDM"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblOA4BGTURUTOBDANVABIFNQAABA"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srt2TAMIGP57VAD3B5EDXWVX3L4LI"),
		null,
		false,
		true,
		null,
		290855,
		null,
		2192,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprW77ZPPNK4ZBGROWGY7BIPQOWHE")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXVZ6UDCUVXOBDCLUAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOI4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6AME2JOY6ZAE5NLCLB5A6HFYEE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOY4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO44BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIE2KOWURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOM4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7WEBG24RUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.STRETCH,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOU4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPM4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPQ4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.web.SentMessage.DefaultGroupModel(userSession,this);
	}
}
/* Radix::PersoComm::SentMessage:Failed - Desktop Meta*/

/*Radix::PersoComm::SentMessage:Failed-Selector Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class Failed_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Failed_mi();
	private Failed_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprPCNPIO4LEJCCRPJQFVPMPDTWWM"),
		"Failed",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblOA4BGTURUTOBDANVABIFNQAABA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("mls67CVMRCKHJA2XJSR547IJV3DF4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("imgI4EKIGAQ2ZBRZA6ENTLLFQCT2Q"),
		null,
		null,
		true,
		null,
		null,
		false,
		true,
		null,
		32,
		null,
		0,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXVZ6UDCUVXOBDCLUAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOI4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6AME2JOY6ZAE5NLCLB5A6HFYEE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7WEBG24RUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAWFN5SQOEJDDJKJ4BY7OVMO6LU")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPM4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPQ4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::PersoComm::SentMessage:Failed - Web Meta*/

/*Radix::PersoComm::SentMessage:Failed-Selector Presentation*/

package org.radixware.ads.PersoComm.web;
public final class Failed_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Failed_mi();
	private Failed_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprPCNPIO4LEJCCRPJQFVPMPDTWWM"),
		"Failed",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblOA4BGTURUTOBDANVABIFNQAABA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("mls67CVMRCKHJA2XJSR547IJV3DF4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("imgI4EKIGAQ2ZBRZA6ENTLLFQCT2Q"),
		null,
		null,
		true,
		null,
		null,
		false,
		true,
		null,
		32,
		null,
		0,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXVZ6UDCUVXOBDCLUAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOI4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6AME2JOY6ZAE5NLCLB5A6HFYEE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7WEBG24RUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAWFN5SQOEJDDJKJ4BY7OVMO6LU")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPM4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPQ4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::PersoComm::SentMessage:Failed:Model - Desktop Executable*/

/*Radix::PersoComm::SentMessage:Failed:Model-Group Model Class*/

package org.radixware.ads.PersoComm.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:Failed:Model")
public class Failed:Model  extends org.radixware.ads.PersoComm.explorer.SentMessage.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Failed:Model_mi.rdxMeta; }



	public Failed:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::PersoComm::SentMessage:Failed:Model:Nested classes-Nested Classes*/

	/*Radix::PersoComm::SentMessage:Failed:Model:Properties-Properties*/

	/*Radix::PersoComm::SentMessage:Failed:Model:Methods-Methods*/


}

/* Radix::PersoComm::SentMessage:Failed:Model - Desktop Meta*/

/*Radix::PersoComm::SentMessage:Failed:Model-Group Model Class*/

package org.radixware.ads.PersoComm.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Failed:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmPCNPIO4LEJCCRPJQFVPMPDTWWM"),
						"Failed:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::PersoComm::SentMessage:Failed:Model:Properties-Properties*/
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

/* Radix::PersoComm::SentMessage:Failed:Model - Web Executable*/

/*Radix::PersoComm::SentMessage:Failed:Model-Group Model Class*/

package org.radixware.ads.PersoComm.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:Failed:Model")
public class Failed:Model  extends org.radixware.ads.PersoComm.web.SentMessage.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Failed:Model_mi.rdxMeta; }



	public Failed:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::PersoComm::SentMessage:Failed:Model:Nested classes-Nested Classes*/

	/*Radix::PersoComm::SentMessage:Failed:Model:Properties-Properties*/

	/*Radix::PersoComm::SentMessage:Failed:Model:Methods-Methods*/


}

/* Radix::PersoComm::SentMessage:Failed:Model - Web Meta*/

/*Radix::PersoComm::SentMessage:Failed:Model-Group Model Class*/

package org.radixware.ads.PersoComm.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Failed:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmPCNPIO4LEJCCRPJQFVPMPDTWWM"),
						"Failed:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::PersoComm::SentMessage:Failed:Model:Properties-Properties*/
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

/* Radix::PersoComm::SentMessage:FailedUpdatePreview - Desktop Meta*/

/*Radix::PersoComm::SentMessage:FailedUpdatePreview-Selector Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class FailedUpdatePreview_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new FailedUpdatePreview_mi();
	private FailedUpdatePreview_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZ6YHZV7VUNFDNMIN56S6D274BY"),
		"FailedUpdatePreview",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprPCNPIO4LEJCCRPJQFVPMPDTWWM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblOA4BGTURUTOBDANVABIFNQAABA"),
		null,
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
		16569,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXVZ6UDCUVXOBDCLUAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOI4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLFJIDL4YHBF3TN3KPU6DZSJ4WM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQA4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJAV7SM2CSJGYJNNRHAKGULO72M"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO44BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVNXI6AEFQRHGFAAXNLAEE5EJXM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.explorer.Failed:Model(userSession,this);
	}
}
/* Radix::PersoComm::SentMessage:FailedUpdatePreview - Web Meta*/

/*Radix::PersoComm::SentMessage:FailedUpdatePreview-Selector Presentation*/

package org.radixware.ads.PersoComm.web;
public final class FailedUpdatePreview_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new FailedUpdatePreview_mi();
	private FailedUpdatePreview_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZ6YHZV7VUNFDNMIN56S6D274BY"),
		"FailedUpdatePreview",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprPCNPIO4LEJCCRPJQFVPMPDTWWM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblOA4BGTURUTOBDANVABIFNQAABA"),
		null,
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
		16569,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXVZ6UDCUVXOBDCLUAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOI4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPI4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLFJIDL4YHBF3TN3KPU6DZSJ4WM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQA4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJAV7SM2CSJGYJNNRHAKGULO72M"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO44BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVNXI6AEFQRHGFAAXNLAEE5EJXM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.web.Failed:Model(userSession,this);
	}
}
/* Radix::PersoComm::SentMessage:Address:Model - Desktop Executable*/

/*Radix::PersoComm::SentMessage:Address:Model-Filter Model Class*/

package org.radixware.ads.PersoComm.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:Address:Model")
public class Address:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Address:Model_mi.rdxMeta; }



	public Address:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::PersoComm::SentMessage:Address:Model:Nested classes-Nested Classes*/

	/*Radix::PersoComm::SentMessage:Address:Model:Properties-Properties*/










	/*Radix::PersoComm::SentMessage:Address:Model:Methods-Methods*/

	/*Radix::PersoComm::SentMessage:Address:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:Address:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		if (address.Value == null)
		    address.Value="%";
		    
	}

	/*Radix::PersoComm::SentMessage:Address:channel:channel-Presentation Property*/




	public class Channel extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Channel(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.filters.RadFilterParamDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:Address:channel:channel")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:Address:channel:channel")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Channel getChannel(){return (Channel)getProperty(prmWCGBWZWPJRAAVOJSIF3HLJGDWI);}

	/*Radix::PersoComm::SentMessage:Address:address:address-Presentation Property*/




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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:Address:address:address")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:Address:address:address")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Address getAddress(){return (Address)getProperty(prmM67CPG3XJZF3RDDBJ2VOTPXZ7Y);}


}

/* Radix::PersoComm::SentMessage:Address:Model - Desktop Meta*/

/*Radix::PersoComm::SentMessage:Address:Model-Filter Model Class*/

package org.radixware.ads.PersoComm.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Address:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcDZGN4HVB2FDHZAGCMMFLBLUFJE"),
						"Address:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::PersoComm::SentMessage:Address:Model:Properties-Properties*/
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

/* Radix::PersoComm::SentMessage:Address:Model - Web Executable*/

/*Radix::PersoComm::SentMessage:Address:Model-Filter Model Class*/

package org.radixware.ads.PersoComm.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:Address:Model")
public class Address:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Address:Model_mi.rdxMeta; }



	public Address:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::PersoComm::SentMessage:Address:Model:Nested classes-Nested Classes*/

	/*Radix::PersoComm::SentMessage:Address:Model:Properties-Properties*/










	/*Radix::PersoComm::SentMessage:Address:Model:Methods-Methods*/

	/*Radix::PersoComm::SentMessage:Address:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:Address:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		if (address.Value == null)
		    address.Value="%";
		    
	}

	/*Radix::PersoComm::SentMessage:Address:channel:channel-Presentation Property*/




	public class Channel extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Channel(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.filters.RadFilterParamDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:Address:channel:channel")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:Address:channel:channel")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Channel getChannel(){return (Channel)getProperty(prmWCGBWZWPJRAAVOJSIF3HLJGDWI);}

	/*Radix::PersoComm::SentMessage:Address:address:address-Presentation Property*/




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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:Address:address:address")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::SentMessage:Address:address:address")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Address getAddress(){return (Address)getProperty(prmM67CPG3XJZF3RDDBJ2VOTPXZ7Y);}


}

/* Radix::PersoComm::SentMessage:Address:Model - Web Meta*/

/*Radix::PersoComm::SentMessage:Address:Model-Filter Model Class*/

package org.radixware.ads.PersoComm.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Address:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcDZGN4HVB2FDHZAGCMMFLBLUFJE"),
						"Address:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::PersoComm::SentMessage:Address:Model:Properties-Properties*/
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

/* Radix::PersoComm::SentMessage - Localizing Bundle */
package org.radixware.ads.PersoComm.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class SentMessage - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By address");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По адресу");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2FZN3F5NFJADJKNVF3QD6ZMHKA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Contains the system unit serving as the message delivery channel.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Содержит модуль системы, который является каналом передачи сообщения.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2S566HB4ZFDTXBK52VCMLDOTTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Delivery status last refreshed");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время последнего обновления статуса доставки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls36Q33ZP3NZDXFGTXEEIJI7LKKQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Date when the delivery status was last changed (refreshed)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время последнего изменения (обновления) статуса доставки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4EVMKL6DKVAHFPQZOK6RHOI7TI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message text template");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Маска текста сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4FKQ7WEPPRFFNEHP42Q7UEC5GU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name of the message source. Obtained by Radix::PersoComm::SentMessage:sourceEntityGuid and Radix::PersoComm::SentMessage:sourcePid.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название источника сообщения. Получается по  Radix::PersoComm::SentMessage:sourceEntityGuid и Radix::PersoComm::SentMessage:sourcePid.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5EVZJ26DHRDTHMX3WSS4RV2LLQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Сhars sent");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Передано символов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5GQO5DOB4RG5XA7SEJM27LPEUQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Sending due time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Плановое время отправки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5HPRI6CTVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Undelivered Messages");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Недоставленные сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls67CVMRCKHJA2XJSR547IJV3DF4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"View previous stage message");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Просмотреть сообщение предыдущего этапа");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6CPITACTTZCU5OW4JREOL6AAJU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6LSEYRYT2JCYDFUYCW64XHRSHY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Opens the message source editor.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Команда для показа редактора источника сообщения.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6NQGZE4XHJEBVBJ6EH4YAKZJ4Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Главное");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6OLVYUEJNFG2ZPQLIY3M7JMMUM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Channel type");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вид канала");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls75CXGWCTVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Parts sent");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Передано частей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7BSDZCMRKBD2HPOMART25NE3FM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Sent Messages");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отправленные сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7O7GVZCTVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"USSD");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"USSD");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7S6LUNU5IJHIZCV3DO3UTRIWAI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Overrides Radix::Explorer.Models::EntityModel:afterRead. Hides the encoding if the message is not SMS. Hides the button of the message source editor show command if the editor cannot be opened.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перегружает Radix::Explorer.Models::EntityModel:afterRead. Скрывает кодировку, если сообщение не является SMS. Так же скрывает кнопку команды для показа редактора источника сообщения если редактор не может быть открыт.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAPZGESF2I5DH7BVVTFLVNI4K2E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Problem");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проблема");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAWFN5SQOEJDDJKJ4BY7OVMO6LU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Channel");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Канал");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAXI4C42VR5AGXNJFTA6ZT3BAUQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDWGZWJC6M5H2VESKJUDA7MACI4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Sending error text");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текст ошибки отправки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEN3N7FCTVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Address");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Адрес");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFB2ZQEKQVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attachments");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вложения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFOLN5YXMVXOBDCLVAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFTFIUCIUEVEUNBJSBOXRFNZ3YE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Routing key");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ключ маршрутизации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFVT62RWB3VE6LLFLRLZ33VYIOM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Source entity ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. сущности источника");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFWBKJSPCFNF6TAHCOWVR5ZNEOU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Source PID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"PID источника");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFZUUB3MYEVEFJI4BQQU7QEH4OI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Sent");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время отправки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH52JHJKTVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Sends the messages. Calls the Callback method for the message.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод для отправки сообщений. Вызывает Callback метод для сообщения.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHMNXSEWDNRA5LLHRQK6KGE7XVI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"View Source Entity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Просмотреть объект сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHU5MPZAJGJEPRIE2JM4WDPWS3Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class that implements log of sent messages. Provides mechanism to work with this kind of messages.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIJFOX3XWR5HRHGPU3KHYNPSAC4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIOXA3XF7ZNGB5HIUCF6L3XHYK4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message text");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текст сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ7GKJ45N3NHKVB5TGFLSW5GCC4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Destination entity ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. сущности получателя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJMALNOQNDRDPPLQUHTON7FS7GY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Bytes sent");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Передано байт");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK27XN3N6MRC2NIKVIWDMUWTG34"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKM57SHYYVNA5XITZKBH2I4ZPQE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKTDKJCSTVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Importance");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Важность");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKXDKJCSTVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLHEENOFGIZBB7NCOIDROB3FQWU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"SMPP message id (assigned by SMS Center)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. SMPP-сообщения (присвоенное SMS-центром)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMC3AKZHBKNFMFNQFWZOV44SSWE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Send stage number");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Номер этапа отправки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOPRUHXJXDNAZVNYOSHUYB3SIGA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Delivery status");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Статус доставки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPGTVBLMW6JFQLISGJRLJ6NTLCE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Sent Message");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отправленное сообщение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQG2BDVKTVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Address like");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Адрес как");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQGBDL2FHRVBCFC2FMP6JRKEBP4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"New channel ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Новый идентификатор канала");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQSRSFWLWL5ATTG3FVQM56NUDZ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Channel ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. канала передачи");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRGL45TZYUVCGDGYDSHEYJQB724"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Expiration time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время истечения срока отправки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRLWTTA2TVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"New sending due time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Новое плановое время отправки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRMSJ3POROFAGZIEDFSCJUOJFQY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"New address for replacement");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Новый адрес на замену");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRT3BQLSIWJCT3KTWMXKJTDY2RM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Encoding");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Кодировка");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS2YX3WJISBE6VKMQKIOJVHLUOU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message source");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Источник сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSIXLQQYFJRFQHK5TZKGZ5RCUOQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message text");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текст сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSL7OITCTVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"New sending channel ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. нового канала доставки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTAKKNILNXBAJZBM5VKXIV676KU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Store attachments in history");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Хранить вложения в истории");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTVMS6CLEG5E43KZCH4SV5D6JPY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Subject");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тема");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTY2HVTCTVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Created");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время создания");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUGCNU2STVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By send time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По времени отправки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUWT55S5AIVCDZJTPQYAH7YB66I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Source message ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. сообщения источника");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVKJDCACBFJDD5BMJ5DBVFYIQGI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Channel");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Канал");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWBZRDZKYOZENVDV6PDPCPQLCDA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"System Attributes");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Системные атрибуты");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXDY3HLVG7RGWLLGGHQG2I4TJRY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Previous stage message id");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. сообщения предыдущего этапа");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXQCOX3OSXRAFBBSALOPTLVQU3M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"USSD operation code");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Код USSD-операции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYDDFWZ3Z5VFB7FDON5T5TXNN6A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"New address");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Новый адрес");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZGMXBII6VNFQNIIVXDYMTQQFXM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Destination PID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"PID получателя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZM6ZF5XSLFH2JPPEBHLWXTWVZQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Sends the messages. Calls the Callback method for the message.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод для отправки сообщений. Вызывает Callback метод для сообщения.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZR2OMJHRCZBARPRMTJUDH4NNPU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(SentMessage - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecOA4BGTURUTOBDANVABIFNQAABA"),"SentMessage - Localizing Bundle",$$$items$$$);
}
