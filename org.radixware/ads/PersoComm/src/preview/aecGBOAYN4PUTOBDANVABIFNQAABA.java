
/* Radix::PersoComm::OutMessage - Server Executable*/

/*Radix::PersoComm::OutMessage-Entity Class*/

package org.radixware.ads.PersoComm.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage")
public final published class OutMessage  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return OutMessage_mi.rdxMeta;}

	/*Radix::PersoComm::OutMessage:Nested classes-Nested Classes*/

	/*Radix::PersoComm::OutMessage:Properties-Properties*/

	/*Radix::PersoComm::OutMessage:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:id")
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:id")
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::PersoComm::OutMessage:channelKind-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:channelKind")
	public published  org.radixware.ads.PersoComm.common.ChannelKind getChannelKind() {
		return channelKind;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:channelKind")
	public published   void setChannelKind(org.radixware.ads.PersoComm.common.ChannelKind val) {
		channelKind = val;
	}

	/*Radix::PersoComm::OutMessage:subject-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:subject")
	public published  Str getSubject() {
		return subject;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:subject")
	public published   void setSubject(Str val) {
		subject = val;
	}

	/*Radix::PersoComm::OutMessage:sourceMsgId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sourceMsgId")
	public published  Str getSourceMsgId() {
		return sourceMsgId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sourceMsgId")
	public published   void setSourceMsgId(Str val) {
		sourceMsgId = val;
	}

	/*Radix::PersoComm::OutMessage:sendCallbackMethodName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sendCallbackMethodName")
	public published  Str getSendCallbackMethodName() {
		return sendCallbackMethodName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sendCallbackMethodName")
	public published   void setSendCallbackMethodName(Str val) {
		sendCallbackMethodName = val;
	}

	/*Radix::PersoComm::OutMessage:sourceEntityGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sourceEntityGuid")
	public published  Str getSourceEntityGuid() {
		return sourceEntityGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sourceEntityGuid")
	public published   void setSourceEntityGuid(Str val) {
		sourceEntityGuid = val;
	}

	/*Radix::PersoComm::OutMessage:expireTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:expireTime")
	public published  java.sql.Timestamp getExpireTime() {
		return expireTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:expireTime")
	public published   void setExpireTime(java.sql.Timestamp val) {
		expireTime = val;
	}

	/*Radix::PersoComm::OutMessage:body-Column-Based Property*/




















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:body")
	public published  Str getBody() {
		return body;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:body")
	public published   void setBody(Str val) {
		body = val;
	}

	/*Radix::PersoComm::OutMessage:importance-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:importance")
	public published  org.radixware.kernel.common.enums.EPersoCommImportance getImportance() {
		return importance;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:importance")
	public published   void setImportance(org.radixware.kernel.common.enums.EPersoCommImportance val) {
		importance = val;
	}

	/*Radix::PersoComm::OutMessage:histMode-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:histMode")
	public published  org.radixware.kernel.common.enums.EPersoCommHistMode getHistMode() {
		return histMode;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:histMode")
	public published   void setHistMode(org.radixware.kernel.common.enums.EPersoCommHistMode val) {
		histMode = val;
	}

	/*Radix::PersoComm::OutMessage:destPid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:destPid")
	public published  Str getDestPid() {
		return destPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:destPid")
	public published   void setDestPid(Str val) {
		destPid = val;
	}

	/*Radix::PersoComm::OutMessage:sendCallbackClassName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sendCallbackClassName")
	public published  Str getSendCallbackClassName() {
		return sendCallbackClassName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sendCallbackClassName")
	public published   void setSendCallbackClassName(Str val) {
		sendCallbackClassName = val;
	}

	/*Radix::PersoComm::OutMessage:dueTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:dueTime")
	public published  java.sql.Timestamp getDueTime() {
		return dueTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:dueTime")
	public published   void setDueTime(java.sql.Timestamp val) {
		dueTime = val;
	}

	/*Radix::PersoComm::OutMessage:createTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:createTime")
	public published  java.sql.Timestamp getCreateTime() {
		return createTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:createTime")
	public published   void setCreateTime(java.sql.Timestamp val) {
		createTime = val;
	}

	/*Radix::PersoComm::OutMessage:sourcePid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sourcePid")
	public published  Str getSourcePid() {
		return sourcePid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sourcePid")
	public published   void setSourcePid(Str val) {
		sourcePid = val;
	}

	/*Radix::PersoComm::OutMessage:destEntityGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:destEntityGuid")
	public published  Str getDestEntityGuid() {
		return destEntityGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:destEntityGuid")
	public published   void setDestEntityGuid(Str val) {
		destEntityGuid = val;
	}

	/*Radix::PersoComm::OutMessage:address-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:address")
	public published  Str getAddress() {
		return address;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:address")
	public published   void setAddress(Str val) {
		address = val;
	}

	/*Radix::PersoComm::OutMessage:channelId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:channelId")
	public published  Int getChannelId() {
		return channelId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:channelId")
	public published   void setChannelId(Int val) {
		channelId = val;
	}

	/*Radix::PersoComm::OutMessage:sourceTitle-Dynamic Property*/



	protected Str sourceTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sourceTitle")
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

	/*Radix::PersoComm::OutMessage:channelUnit-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:channelUnit")
	public published  org.radixware.ads.System.server.Unit getChannelUnit() {
		return channelUnit;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:channelUnit")
	public published   void setChannelUnit(org.radixware.ads.System.server.Unit val) {
		channelUnit = val;
	}

	/*Radix::PersoComm::OutMessage:encoding-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:encoding")
	public published  org.radixware.kernel.common.enums.ESmppEncoding getEncoding() {
		return encoding;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:encoding")
	public published   void setEncoding(org.radixware.kernel.common.enums.ESmppEncoding val) {
		encoding = val;
	}

	/*Radix::PersoComm::OutMessage:addressFrom-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:addressFrom")
	public published  Str getAddressFrom() {
		return addressFrom;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:addressFrom")
	public published   void setAddressFrom(Str val) {
		addressFrom = val;
	}

	/*Radix::PersoComm::OutMessage:failedIsUnrecoverable-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedIsUnrecoverable")
	public published  Bool getFailedIsUnrecoverable() {
		return failedIsUnrecoverable;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedIsUnrecoverable")
	public published   void setFailedIsUnrecoverable(Bool val) {
		failedIsUnrecoverable = val;
	}

	/*Radix::PersoComm::OutMessage:failedLastSendDate-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedLastSendDate")
	public published  java.sql.Timestamp getFailedLastSendDate() {
		return failedLastSendDate;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedLastSendDate")
	public published   void setFailedLastSendDate(java.sql.Timestamp val) {
		failedLastSendDate = val;
	}

	/*Radix::PersoComm::OutMessage:failedMessage-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedMessage")
	public published  Str getFailedMessage() {
		return failedMessage;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedMessage")
	public published   void setFailedMessage(Str val) {
		failedMessage = val;
	}

	/*Radix::PersoComm::OutMessage:failedTryCount-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedTryCount")
	public published  Int getFailedTryCount() {
		return failedTryCount;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedTryCount")
	public published   void setFailedTryCount(Int val) {
		failedTryCount = val;
	}

	/*Radix::PersoComm::OutMessage:newAddress-Dynamic Property*/



	protected Str newAddress=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:newAddress")
	public published  Str getNewAddress() {

		OutMessageUpdateFields fields = updateFields;
		if (fields != null && fields.newAddress != null) {
		    return fields.newAddress;
		}
		return address;
	}

	/*Radix::PersoComm::OutMessage:newChannelId-Dynamic Property*/



	protected Int newChannelId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:newChannelId")
	public published  Int getNewChannelId() {

		OutMessageUpdateFields fields = updateFields;
		if (fields != null && fields.newChannel != null) {
		    return fields.newChannel;
		}
		return id;
	}

	/*Radix::PersoComm::OutMessage:updateFields-Dynamic Property*/



	protected org.radixware.ads.PersoComm.server.OutMessageUpdateFields updateFields=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:updateFields")
	public published  org.radixware.ads.PersoComm.server.OutMessageUpdateFields getUpdateFields() {

		if (OutMessageGroup.needReplaceStatic != null && OutMessageGroup.needReplaceStatic.booleanValue()) {
		    return OutMessageGroupChanges4Failed.calcUpdates(channelId, address, dueTime,
		    OutMessageGroup.oldChannelStatic, OutMessageGroup.oldDestinationAddressStatic,
		    OutMessageGroup.newChannelStatic, OutMessageGroup.newDestinationAddressStatic, OutMessageGroup.newDueTimeStatic);
		}
		return null;
	}

	/*Radix::PersoComm::OutMessage:newDueTime-Dynamic Property*/



	protected java.sql.Timestamp newDueTime=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:newDueTime")
	public published  java.sql.Timestamp getNewDueTime() {

		OutMessageUpdateFields fields = updateFields;
		if (fields != null && fields.newDueTime != null) {
		    return fields.newDueTime;
		}
		return dueTime;
	}

	/*Radix::PersoComm::OutMessage:maskedBody-Column-Based Property*/




















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:maskedBody")
	public published  Str getMaskedBody() {
		return maskedBody;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:maskedBody")
	public published   void setMaskedBody(Str val) {
		maskedBody = val;
	}

	/*Radix::PersoComm::OutMessage:displayedBody-Dynamic Property*/



	protected Str displayedBody=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:displayedBody")
	public published  Str getDisplayedBody() {

		if (maskedBody != null) {
		    return maskedBody;
		} else {
		    return body;
		}
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:displayedBody")
	public published   void setDisplayedBody(Str val) {
		displayedBody = val;
	}

	/*Radix::PersoComm::OutMessage:routingKey-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:routingKey")
	public published  Str getRoutingKey() {
		return routingKey;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:routingKey")
	public published   void setRoutingKey(Str val) {
		routingKey = val;
	}

	/*Radix::PersoComm::OutMessage:inProcess-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:inProcess")
	public published  Bool getInProcess() {
		return inProcess;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:inProcess")
	public published   void setInProcess(Bool val) {
		inProcess = val;
	}

	/*Radix::PersoComm::OutMessage:initialDueTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:initialDueTime")
	public published  java.sql.Timestamp getInitialDueTime() {
		return initialDueTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:initialDueTime")
	public published   void setInitialDueTime(java.sql.Timestamp val) {
		initialDueTime = val;
	}

	/*Radix::PersoComm::OutMessage:storeAttachInHist-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:storeAttachInHist")
	public published  Bool getStoreAttachInHist() {
		return storeAttachInHist;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:storeAttachInHist")
	public published   void setStoreAttachInHist(Bool val) {
		storeAttachInHist = val;
	}

	/*Radix::PersoComm::OutMessage:lastForwardTimeMillis-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:lastForwardTimeMillis")
	public  Int getLastForwardTimeMillis() {
		return lastForwardTimeMillis;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:lastForwardTimeMillis")
	public   void setLastForwardTimeMillis(Int val) {
		lastForwardTimeMillis = val;
	}

	/*Radix::PersoComm::OutMessage:lastForwardTime-Expression Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:lastForwardTime")
	public published  java.sql.Timestamp getLastForwardTime() {
		return lastForwardTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:lastForwardTime")
	public published   void setLastForwardTime(java.sql.Timestamp val) {
		lastForwardTime = val;
	}

	/*Radix::PersoComm::OutMessage:isUssd-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:isUssd")
	public published  Bool getIsUssd() {
		return isUssd;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:isUssd")
	public published   void setIsUssd(Bool val) {
		isUssd = val;
	}

	/*Radix::PersoComm::OutMessage:ussdServiceOp-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:ussdServiceOp")
	public published  Int getUssdServiceOp() {
		return ussdServiceOp;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:ussdServiceOp")
	public published   void setUssdServiceOp(Int val) {
		ussdServiceOp = val;
	}

	/*Radix::PersoComm::OutMessage:deliveryCallbackClassName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryCallbackClassName")
	public published  Str getDeliveryCallbackClassName() {
		return deliveryCallbackClassName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryCallbackClassName")
	public published   void setDeliveryCallbackClassName(Str val) {
		deliveryCallbackClassName = val;
	}

	/*Radix::PersoComm::OutMessage:deliveryCallbackMethodName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryCallbackMethodName")
	public published  Str getDeliveryCallbackMethodName() {
		return deliveryCallbackMethodName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryCallbackMethodName")
	public published   void setDeliveryCallbackMethodName(Str val) {
		deliveryCallbackMethodName = val;
	}

	/*Radix::PersoComm::OutMessage:deliveryTimeout-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryTimeout")
	public published  Int getDeliveryTimeout() {
		return deliveryTimeout;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryTimeout")
	public published   void setDeliveryTimeout(Int val) {
		deliveryTimeout = val;
	}

	/*Radix::PersoComm::OutMessage:sendCallbackData-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sendCallbackData")
	public published  Str getSendCallbackData() {
		return sendCallbackData;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sendCallbackData")
	public published   void setSendCallbackData(Str val) {
		sendCallbackData = val;
	}

	/*Radix::PersoComm::OutMessage:deliveryCallbackData-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryCallbackData")
	public published  Str getDeliveryCallbackData() {
		return deliveryCallbackData;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryCallbackData")
	public published   void setDeliveryCallbackData(Str val) {
		deliveryCallbackData = val;
	}

	/*Radix::PersoComm::OutMessage:stageNo-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:stageNo")
	public published  Int getStageNo() {
		return stageNo;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:stageNo")
	public published   void setStageNo(Int val) {
		stageNo = val;
	}

	/*Radix::PersoComm::OutMessage:prevStageMessageId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:prevStageMessageId")
	public published  Int getPrevStageMessageId() {
		return prevStageMessageId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:prevStageMessageId")
	public published   void setPrevStageMessageId(Int val) {
		prevStageMessageId = val;
	}

	/*Radix::PersoComm::OutMessage:prevStageNotSentMessage-Dynamic Property*/



	protected org.radixware.ads.PersoComm.server.OutMessage prevStageNotSentMessage=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:prevStageNotSentMessage")
	public published  org.radixware.ads.PersoComm.server.OutMessage getPrevStageNotSentMessage() {

		if (prevStageMessageId == null) {
		    return null;
		}

		return OutMessage.loadByPK(prevStageMessageId, true);

	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:prevStageNotSentMessage")
	public published   void setPrevStageNotSentMessage(org.radixware.ads.PersoComm.server.OutMessage val) {
		prevStageNotSentMessage = val;
	}

	/*Radix::PersoComm::OutMessage:prevStageSentMessage-Dynamic Property*/



	protected org.radixware.ads.PersoComm.server.SentMessage prevStageSentMessage=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:prevStageSentMessage")
	public published  org.radixware.ads.PersoComm.server.SentMessage getPrevStageSentMessage() {

		if (prevStageMessageId == null) {
		    return null;
		}

		return SentMessage.loadByPK(prevStageMessageId, true);

	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:prevStageSentMessage")
	public published   void setPrevStageSentMessage(org.radixware.ads.PersoComm.server.SentMessage val) {
		prevStageSentMessage = val;
	}

	/*Radix::PersoComm::OutMessage:prevStageNotSentMessageId-Dynamic Property*/



	protected Int prevStageNotSentMessageId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:prevStageNotSentMessageId")
	public published  Int getPrevStageNotSentMessageId() {

		return prevStageNotSentMessage == null ? null : prevStageMessageId;

	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:prevStageNotSentMessageId")
	private published   void setPrevStageNotSentMessageId(Int val) {
		prevStageNotSentMessageId = val;
	}

	/*Radix::PersoComm::OutMessage:prevStageSentMessageId-Dynamic Property*/



	protected Int prevStageSentMessageId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:prevStageSentMessageId")
	public published  Int getPrevStageSentMessageId() {

		return prevStageSentMessage == null ? null : prevStageMessageId;

	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:prevStageSentMessageId")
	private published   void setPrevStageSentMessageId(Int val) {
		prevStageSentMessageId = val;
	}



























































































































































































































































































































	/*Radix::PersoComm::OutMessage:Methods-Methods*/

	/*Radix::PersoComm::OutMessage:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:beforeCreate")
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		if (channelId == null) {
		    if (channelKind == null) 
		        throw new AppError("Channel type not defined");
		    final Int minId = findSuitableChannel(this);
		    if (minId == null) 
		        throw new AppError(Str.format("No suitable channel found for message #%s, channel type %s, address '%s', routing key '%s'", id, channelKind.getName(), address, routingKey));
		    channelId = minId;
		}

		if (createTime == null)
		    createTime = Arte::Arte.getCurrentTime();

		initialDueTime = dueTime;

		subject = Utils::String.truncTrail(subject, 200);
		return true;
	}

	/*Radix::PersoComm::OutMessage:loadByPidStr-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:loadByPidStr")
	public static published  org.radixware.ads.PersoComm.server.OutMessage loadByPidStr (Str pidAsStr, boolean checkExistance) {
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblGBOAYN4PUTOBDANVABIFNQAABA"),pidAsStr);
		try{
		return (
		org.radixware.ads.PersoComm.server.OutMessage) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::PersoComm::OutMessage:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:loadByPK")
	public static published  org.radixware.ads.PersoComm.server.OutMessage loadByPK (Int id, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(id==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YNY224PUTOBDANVABIFNQAABA"),id);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblGBOAYN4PUTOBDANVABIFNQAABA"),pkValsMap);
		try{
		return (
		org.radixware.ads.PersoComm.server.OutMessage) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::PersoComm::OutMessage:getAddressForPushNotificationCopy-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:getAddressForPushNotificationCopy")
	public published  org.radixware.schemas.personalcommunications.PushNotificationAddressDocument getAddressForPushNotificationCopy () throws org.apache.xmlbeans.XmlException {
		if (address == null) {
		    return null;
		}

		PersoCommXsd:PushNotificationAddressDocument xDoc = PersoCommXsd:PushNotificationAddressDocument.Factory.parse(address);

		return xDoc;
	}

	/*Radix::PersoComm::OutMessage:setAddressForPushNotification-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:setAddressForPushNotification")
	public published  void setAddressForPushNotification (org.radixware.schemas.personalcommunications.PushNotificationAddressDocument xAddress) {
		address = xAddress == null ? null : xAddress.xmlText();
	}

	/*Radix::PersoComm::OutMessage:setMessageForPushNotification-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:setMessageForPushNotification")
	public published  void setMessageForPushNotification (org.radixware.schemas.personalcommunications.PushNotificationMessageDocument xMessage) {
		body = xMessage == null ? null : xMessage.xmlText();
	}

	/*Radix::PersoComm::OutMessage:getPushNotificationMessageCopy-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:getPushNotificationMessageCopy")
	public published  org.radixware.schemas.personalcommunications.PushNotificationMessageDocument getPushNotificationMessageCopy () throws org.apache.xmlbeans.XmlException {
		if (body == null) {
		    return null;
		}

		PersoCommXsd:PushNotificationMessageDocument xDoc = PersoCommXsd:PushNotificationMessageDocument.Factory.parse(body);

		return xDoc;

	}

	/*Radix::PersoComm::OutMessage:findSuitableChannelId-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:findSuitableChannelId")
	@Deprecated
	public static published  Int findSuitableChannelId (org.radixware.ads.PersoComm.common.ChannelKind kind, Str address, org.radixware.kernel.common.enums.EPersoCommImportance importance, Int excludeUnitId, Int excludePriority) {
		PersoComm.Db::ChannelUnitsCursor cur = PersoComm.Db::ChannelUnitsCursor.open(kind, address, importance, excludeUnitId, excludePriority, null);
		try {
		    if (!cur.next()) {
		        return null;
		    } else {
		        return cur.id;
		    }
		} finally {
		    cur.close();
		}
	}

	/*Radix::PersoComm::OutMessage:findSuitableChannel-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:findSuitableChannel")
	public static published  Int findSuitableChannel (org.radixware.ads.PersoComm.server.OutMessage msg) {
		return findSuitableChannel(msg, null, null);
	}

	/*Radix::PersoComm::OutMessage:findSuitableChannel-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:findSuitableChannel")
	public static published  Int findSuitableChannel (org.radixware.ads.PersoComm.server.OutMessage msg, Int prevUnitId, Int prevPriority) {
		if (msg == null) {
		    return null;
		}
		int maxPriority = -1;
		Int bestUnitId = null;
		PersoComm.Db::ChannelUnitsCursor cur = PersoComm.Db::ChannelUnitsCursor.open(msg.channelKind, msg.address, msg.importance, prevUnitId, prevPriority, msg.routingKey);
		try {
		    while (cur.next()) {
		        try {
		            final Unit.Channel.AbstractBase pcUnit = (Unit.Channel.AbstractBase) System::Unit.loadByPK(cur.id, true);
		            int unitPriority = pcUnit.calcPriority(msg);
		            if (unitPriority > maxPriority && (prevPriority == null || unitPriority < prevPriority.intValue())) {
		                bestUnitId = cur.id;
		                maxPriority = unitPriority;
		            }
		        } catch (Throwable t) {
		            Arte::Trace.warning("Exception while testing unit '" + cur.id + ") " + cur.title + "' to for out message: " + Utils::ExceptionTextFormatter.throwableToString(t), Arte::EventSource:App);
		        }
		    }
		} finally {
		    cur.close();
		}
		return bestUnitId;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::PersoComm::OutMessage - Server Meta*/

/*Radix::PersoComm::OutMessage-Entity Class*/

package org.radixware.ads.PersoComm.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class OutMessage_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),"OutMessage",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUBY3ORBPVXOBDCLUAALOMT5GDM"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::PersoComm::OutMessage:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
							/*Owner Class Name*/
							"OutMessage",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUBY3ORBPVXOBDCLUAALOMT5GDM"),
							/*Property presentations*/

							/*Radix::PersoComm::OutMessage:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::PersoComm::OutMessage:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YNY224PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::PersoComm::OutMessage:channelKind:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4RHX4A4QUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::PersoComm::OutMessage:subject:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5CKW454PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::PersoComm::OutMessage:sourceMsgId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6LCP3VS5MFBXFIXFRYSR3WJBGM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::PersoComm::OutMessage:sendCallbackMethodName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6R756U4QUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::PersoComm::OutMessage:sourceEntityGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6STBOLMQUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::PersoComm::OutMessage:expireTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBRWI77EPUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::PersoComm::OutMessage:body:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDUU5BZMPUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:importance:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDYU5BZMPUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::PersoComm::OutMessage:destPid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHBZLONSMMRDLFPVLUPA36J4GDQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::PersoComm::OutMessage:sendCallbackClassName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLRRSUR4QUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::PersoComm::OutMessage:dueTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2TKV4MPUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::PersoComm::OutMessage:createTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWTKV4MPUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::PersoComm::OutMessage:sourcePid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPDVAS7UQUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::PersoComm::OutMessage:destEntityGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPIHVQDD6VZBQVMO5CNQ7SP33AU"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::PersoComm::OutMessage:address:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRIH4IEQUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::PersoComm::OutMessage:channelId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQQY3SX4QUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::PersoComm::OutMessage:sourceTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGQYOL3K2GFBQPKXDOFAII3RJRI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::PersoComm::OutMessage:channelUnit:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR255GGIKEFEYVA4P4RR5GYUZNI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tbl5WO5N3BEVLOBDCLSAALOMT5GDM\" PropId=\"colUZY3V5REVLOBDCLSAALOMT5GDM\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colEVLUYADHR5VDBNSIAAUMFADAIA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tbl5WO5N3BEVLOBDCLSAALOMT5GDM\" PropId=\"colB2VQZVZFVLOBDCLSAALOMT5GDM\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecGBOAYN4PUTOBDANVABIFNQAABA\" PropId=\"col4RHX4A4QUTOBDANVABIFNQAABA\" Owner=\"CHILD\"/></xsc:Item><xsc:Item><xsc:Sql>\nand regexp_like (</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblGBOAYN4PUTOBDANVABIFNQAABA\" PropId=\"colPRIH4IEQUTOBDANVABIFNQAABA\" Owner=\"CHILD\"/></xsc:Item><xsc:Item><xsc:Sql>, </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tbl5WO5N3BEVLOBDCLSAALOMT5GDM\" PropId=\"colPSI5Q6JLVLOBDCLSAALOMT5GDM\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>) \nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblGBOAYN4PUTOBDANVABIFNQAABA\" PropId=\"colDYU5BZMPUTOBDANVABIFNQAABA\" Owner=\"CHILD\"/></xsc:Item><xsc:Item><xsc:Sql> >= </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tbl5WO5N3BEVLOBDCLSAALOMT5GDM\" PropId=\"colDS7R3IHBBFBJVGXEA5XTB36HHI\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblGBOAYN4PUTOBDANVABIFNQAABA\" PropId=\"colDYU5BZMPUTOBDANVABIFNQAABA\" Owner=\"CHILD\"/></xsc:Item><xsc:Item><xsc:Sql> &lt;= </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tbl5WO5N3BEVLOBDCLSAALOMT5GDM\" PropId=\"col2TCIGGC4EFA6BGRDSLQGCOLQQ4\" Owner=\"TABLE\"/></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:TableSqlName TableId=\"tbl5WO5N3BEVLOBDCLSAALOMT5GDM\"/></xsc:Item></xsc:Sqml>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::PersoComm::OutMessage:encoding:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWBAD4MPNUFFYZCHKSKE4LH6LHI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:addressFrom:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFLYC2OLNIBEQRMU4HSLQGRFMLA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:failedIsUnrecoverable:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colS2LOJTRFBJCDFHYYJEGO5QSN24"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:failedLastSendDate:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2NMC43PMHRGYBONJZFLS22YQ3E"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:failedMessage:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DAJREKW3NHERCUYZJEOXLZKXQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:failedTryCount:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHIAURDZIHRC7JA5STQBH6PVXUQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:newAddress:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSUZRJQGMPFDLTGETUOEJQTSR2Y"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:newChannelId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYAU6AYKJXJGKDPNDOQYWOOAQGI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:newDueTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd53CYEZWTRFFXXLAQWAET4S4BYA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:maskedBody:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSEJB3YF6WFEZHOX4HX24NF4H4A"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:displayedBody:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDKI6KZ4SSBDXVHP3WMWQOM3HUY"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:routingKey:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSWFU5C55JC6HKXPXQCZIC25LM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:inProcess:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZKRADR2XH5AWHOSJVUP3XFJRLI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:initialDueTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCSFRDRISERF5BCBFNGO7PIVLOI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:lastForwardTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4Z7PGG5ZF5AMHG5FHZUWDBDN5I"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:isUssd:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSTBPUQBGHRBLBNXEY6BL2ZSKOY"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:ussdServiceOp:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHJPGXALIU5CFFNE3AXBAPQD4XM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:deliveryCallbackClassName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK5DZH446IRBRVFDGJRBTGYEWEQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:deliveryCallbackMethodName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP2XZB6FQONDH5GA4ZLDWHO2FVI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:deliveryTimeout:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colG2MGIAWXPJDGZMPJHM4QG6P4QA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:sendCallbackData:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNQOCLTUC4BADNMZKWPLKNSRFSM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:deliveryCallbackData:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHRVJLPPFBNFQ5KLQE3K7P2ND5U"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:stageNo:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colF4WMZEHSXZDCLLMKZHU73A57PA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:prevStageMessageId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBWUMTS2TWBEIPPXRMTVTPH3ZQY"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:prevStageNotSentMessageId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJXWN5XA7RVE5LBTICQEOT4HJZ4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::OutMessage:prevStageSentMessageId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5SSHOXXF7JFXJKVVCPWDKLRZQA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::PersoComm::OutMessage:ViewSource-Property Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdY6IIW44J7ZADVKESCT6Y65EHPA"),"ViewSource",org.radixware.kernel.common.enums.ECommandScope.PROPERTY,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGQYOL3K2GFBQPKXDOFAII3RJRI")},org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.LOCAL,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::PersoComm::OutMessage:ViewPrevStageMessage-Property Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd4SMJ76W7VNHJPFHVAABZ2QCLTY"),"ViewPrevStageMessage",org.radixware.kernel.common.enums.ECommandScope.PROPERTY,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colBWUMTS2TWBEIPPXRMTVTPH3ZQY")},org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.LOCAL,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),true,true)
							},
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::PersoComm::OutMessage:CreateTime-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtVYQNNZJZA5EKPGANNF6HR2Q2AQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),"CreateTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEJMVIMVORJBJDLU6JNDHP7JLMQ"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWTKV4MPUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::PersoComm::OutMessage:DueTime-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtDA5NSIAZ55HEJJXX2OU7F6JAGQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),"DueTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4DC2PCMZRBATHLCF7FNM5WEP7Y"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2TKV4MPUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.EOrder.DESC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::PersoComm::OutMessage:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprKTXPYSJZVXOBDCLUAALOMT5GDM"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::PersoComm::OutMessage:General:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::PersoComm::OutMessage:General:Attachments-Entity Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadTableExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiXHVKI4HMVXOBDCLVAALOMT5GDM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5O3H4DJEVLOBDCLSAALOMT5GDM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6BHCGLBRVXOBDCLUAALOMT5GDM"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tbl5O3H4DJEVLOBDCLSAALOMT5GDM\" PropId=\"col5W3H4DJEVLOBDCLSAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblGBOAYN4PUTOBDANVABIFNQAABA\" PropId=\"col3YNY224PUTOBDANVABIFNQAABA\" Owner=\"PARENT\"/></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::PersoComm::OutMessage:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOD336QIYRRBY5IH274Z7U24EIM"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::PersoComm::OutMessage:Create:Children-Explorer Items*/
										null
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::PersoComm::OutMessage:Failed-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOGAUTNZ7AFBDTHQYJNYCVQTJGM"),
									"Failed",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2064,
									null,

									/*Radix::PersoComm::OutMessage:Failed:Children-Explorer Items*/
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
									/*Radix::PersoComm::OutMessage:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOARLWAJ2VXOBDCLUAALOMT5GDM"),"General",null,2192,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YNY224PUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWTKV4MPUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCSFRDRISERF5BCBFNGO7PIVLOI"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2TKV4MPUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRIH4IEQUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5CKW454PUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDYU5BZMPUTOBDANVABIFNQAABA"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtDA5NSIAZ55HEJJXX2OU7F6JAGQ"),true,null,true,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprKTXPYSJZVXOBDCLUAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOD336QIYRRBY5IH274Z7U24EIM")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(28673,null,null,null),null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::PersoComm::OutMessage:FailedRecoverable-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVOMDNSLYXRBG5H7TIL3FNC3RYU"),"FailedRecoverable",null,0,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YNY224PUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFLYC2OLNIBEQRMU4HSLQGRFMLA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRIH4IEQUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR255GGIKEFEYVA4P4RR5GYUZNI"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DAJREKW3NHERCUYZJEOXLZKXQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHIAURDZIHRC7JA5STQBH6PVXUQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colS2LOJTRFBJCDFHYYJEGO5QSN24"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2TKV4MPUTOBDANVABIFNQAABA"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecGBOAYN4PUTOBDANVABIFNQAABA\" PropId=\"col3DAJREKW3NHERCUYZJEOXLZKXQ\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is not null</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtDA5NSIAZ55HEJJXX2OU7F6JAGQ"),true,null,true,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprKTXPYSJZVXOBDCLUAALOMT5GDM")},null,org.radixware.kernel.server.types.Restrictions.Factory.newInstance(32,null,null,null),null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::PersoComm::OutMessage:FailedUpdatePreview-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprAUXDUZIHSRDWRB4VRONHDFKDSM"),"FailedUpdatePreview",org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVOMDNSLYXRBG5H7TIL3FNC3RYU"),16569,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YNY224PUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFLYC2OLNIBEQRMU4HSLQGRFMLA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRIH4IEQUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSUZRJQGMPFDLTGETUOEJQTSR2Y"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQQY3SX4QUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYAU6AYKJXJGKDPNDOQYWOOAQGI"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2TKV4MPUTOBDANVABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd53CYEZWTRFFXXLAQWAET4S4BYA"),true)
									},null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOGAUTNZ7AFBDTHQYJNYCVQTJGM")},null,null,null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOARLWAJ2VXOBDCLUAALOMT5GDM"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::PersoComm::OutMessage:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YNY224PUTOBDANVABIFNQAABA"),"{0}) ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWTKV4MPUTOBDANVABIFNQAABA"),"",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col5CKW454PUTOBDANVABIFNQAABA")," - {0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(28673,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblGBOAYN4PUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::PersoComm::OutMessage:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::PersoComm::OutMessage:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YNY224PUTOBDANVABIFNQAABA"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQHUJIPZUVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:channelKind-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4RHX4A4QUTOBDANVABIFNQAABA"),"channelKind",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZOC2FYZTVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:subject-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5CKW454PUTOBDANVABIFNQAABA"),"subject",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO57MIEBXVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:sourceMsgId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6LCP3VS5MFBXFIXFRYSR3WJBGM"),"sourceMsgId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDXHHBCWGMPORDHZ7ABIFNQAABA"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:sendCallbackMethodName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6R756U4QUTOBDANVABIFNQAABA"),"sendCallbackMethodName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW7RYIIWGMPORDHZ7ABIFNQAABA"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:sourceEntityGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6STBOLMQUTOBDANVABIFNQAABA"),"sourceEntityGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU6UKJ2K27RERJNKLBSHIKTFKQQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:expireTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBRWI77EPUTOBDANVABIFNQAABA"),"expireTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMHMIEMZUVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:body-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDUU5BZMPUTOBDANVABIFNQAABA"),"body",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUVBA45ZPVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:importance-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDYU5BZMPUTOBDANVABIFNQAABA"),"importance",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDGV5QRRUVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTUGILCRUVXOBDCLUAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:histMode-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGBH2JEACTVBTTOIGPJHGZIUROM"),"histMode",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCHJF42GGMPORDHZ7ABIFNQAABA"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsVQOKSQR4RVAMPBGDQRMO7W7JBQ"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:destPid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHBZLONSMMRDLFPVLUPA36J4GDQ"),"destPid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQUJR2WOGMPORDHZ7ABIFNQAABA"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:sendCallbackClassName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLRRSUR4QUTOBDANVABIFNQAABA"),"sendCallbackClassName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUXVZKGOGMPORDHZ7ABIFNQAABA"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:dueTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2TKV4MPUTOBDANVABIFNQAABA"),"dueTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYZNCJ5RTVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:createTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWTKV4MPUTOBDANVABIFNQAABA"),"createTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMQ2NF3RTVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:sourcePid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPDVAS7UQUTOBDANVABIFNQAABA"),"sourcePid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRKRWJ66IVNGQFFHJ6J4FZRDYSQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:destEntityGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPIHVQDD6VZBQVMO5CNQ7SP33AU"),"destEntityGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVEXQ2UOGMPORDHZ7ABIFNQAABA"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:address-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRIH4IEQUTOBDANVABIFNQAABA"),"address",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXGQVVBELVTOBDCLTAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:channelId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQQY3SX4QUTOBDANVABIFNQAABA"),"channelId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE5HOJXYNJVCVXPBCSWYOB7YZLU"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:sourceTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGQYOL3K2GFBQPKXDOFAII3RJRI"),"sourceTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCJYZBFWZVVELNOKBIVPSLWCU7E"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:channelUnit-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR255GGIKEFEYVA4P4RR5GYUZNI"),"channelUnit",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQAUFQOK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refNA2RGKBQVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:encoding-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWBAD4MPNUFFYZCHKSKE4LH6LHI"),"encoding",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2QPJU7HCM5EZRGTPEJZCSYHHAE"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsSMFAHKLBOBGHVDYOXCSGSNFDME"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:addressFrom-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFLYC2OLNIBEQRMU4HSLQGRFMLA"),"addressFrom",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHC3X4FMRCNDYXBDIVB5IXOEFKY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:failedIsUnrecoverable-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colS2LOJTRFBJCDFHYYJEGO5QSN24"),"failedIsUnrecoverable",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIKCBVENZYFAPZFCQ3JELPLNOWU"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:failedLastSendDate-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2NMC43PMHRGYBONJZFLS22YQ3E"),"failedLastSendDate",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDX4DLJCNM5HZJI5JXNVZ4YCUAI"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:failedMessage-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DAJREKW3NHERCUYZJEOXLZKXQ"),"failedMessage",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAITTCPQVMVCLTBMT4LNDQT572U"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:failedTryCount-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHIAURDZIHRC7JA5STQBH6PVXUQ"),"failedTryCount",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNLAKKOZ3MZBHBGNPY5ZU7HNZI4"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:newAddress-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSUZRJQGMPFDLTGETUOEJQTSR2Y"),"newAddress",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2MZHLCKU7FHZBMLAKWM35HJBFQ"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:newChannelId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYAU6AYKJXJGKDPNDOQYWOOAQGI"),"newChannelId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX6SLCYYSBFE6FGV4TQ33PMGUFI"),org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:updateFields-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQOFZ7RBV45ECZBJLPRBEBITD4Q"),"updateFields",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:newDueTime-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd53CYEZWTRFFXXLAQWAET4S4BYA"),"newDueTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsILEKIENMIRCLLONO4VTLR2ZFIM"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:maskedBody-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSEJB3YF6WFEZHOX4HX24NF4H4A"),"maskedBody",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLXLCAVSKLVBE3MUFPWBGTTID2Y"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:displayedBody-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDKI6KZ4SSBDXVHP3WMWQOM3HUY"),"displayedBody",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5ULOH2HAKBF67PVPAX77U2LGA4"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:routingKey-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSWFU5C55JC6HKXPXQCZIC25LM"),"routingKey",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM37ZJNBXPBALRMMSV4A7GRJMNY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:inProcess-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZKRADR2XH5AWHOSJVUP3XFJRLI"),"inProcess",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6OA2Q6NSBRCO3JSF2KY3IQR7VE"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:initialDueTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCSFRDRISERF5BCBFNGO7PIVLOI"),"initialDueTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQPAHK5XB6ZFE5KRWJRJ33D74LY"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:storeAttachInHist-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJPLR6ALQ7BAQLE63V7E5J4YU2E"),"storeAttachInHist",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB334URGBVNFB3AGNJJTGJRIZ4E"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:lastForwardTimeMillis-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPTZGQXCTHVACRMZEFHIQV3W4EU"),"lastForwardTimeMillis",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:lastForwardTime-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4Z7PGG5ZF5AMHG5FHZUWDBDN5I"),"lastForwardTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNPMSOSC7FNE3HGQ3GV4DSMM2CQ"),org.radixware.kernel.common.enums.EValType.DATE_TIME,"DATE",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:DbFuncCall FuncId=\"dfn5N3HJKQHZ5AAHKGED3UYMETMCM\"/></xsc:Item><xsc:Item><xsc:Sql>( </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblGBOAYN4PUTOBDANVABIFNQAABA\" PropId=\"colPTZGQXCTHVACRMZEFHIQV3W4EU\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> )</xsc:Sql></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:isUssd-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSTBPUQBGHRBLBNXEY6BL2ZSKOY"),"isUssd",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4QQZVC6KXNCZXGXRJWDL4RCIDQ"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:ussdServiceOp-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHJPGXALIU5CFFNE3AXBAPQD4XM"),"ussdServiceOp",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVHSET6D5XBHQNKXULNU65OQQV4"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:deliveryCallbackClassName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK5DZH446IRBRVFDGJRBTGYEWEQ"),"deliveryCallbackClassName",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:deliveryCallbackMethodName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP2XZB6FQONDH5GA4ZLDWHO2FVI"),"deliveryCallbackMethodName",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:deliveryTimeout-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colG2MGIAWXPJDGZMPJHM4QG6P4QA"),"deliveryTimeout",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:sendCallbackData-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNQOCLTUC4BADNMZKWPLKNSRFSM"),"sendCallbackData",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:deliveryCallbackData-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHRVJLPPFBNFQ5KLQE3K7P2ND5U"),"deliveryCallbackData",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:stageNo-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colF4WMZEHSXZDCLLMKZHU73A57PA"),"stageNo",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC4LJPRTPFJAK5JBISDJDAIIBIQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:prevStageMessageId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBWUMTS2TWBEIPPXRMTVTPH3ZQY"),"prevStageMessageId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUGSOWHJV7RFCDCLR5CFBLEPK5E"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:prevStageNotSentMessage-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdAIEBFCG2LNAZXDA3WBVYDHURHQ"),"prevStageNotSentMessage",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUBY3ORBPVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.USER_CLASS,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:prevStageSentMessage-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdW5DUPR4VFRAPVJ52ZGD4HQSVFE"),"prevStageSentMessage",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQG2BDVKTVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.USER_CLASS,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOA4BGTURUTOBDANVABIFNQAABA"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:prevStageNotSentMessageId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJXWN5XA7RVE5LBTICQEOT4HJZ4"),"prevStageNotSentMessageId",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::OutMessage:prevStageSentMessageId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5SSHOXXF7JFXJKVVCPWDKLRZQA"),"prevStageSentMessageId",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::PersoComm::OutMessage:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDNWTU3SVY5GJ5K246MRCTSZE34"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPidAsStr__________________")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3YNY224PUTOBDANVABIFNQAABA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3CARRGDQXJGQTKZSFDWIP2TZMM"),"getAddressForPushNotificationCopy",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWIXINVXOBJFEBKQWW7PRJHRTJQ"),"setAddressForPushNotification",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xAddress",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYUKABAX57JEJ5NOC5YEYRHS2U4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXCNCHHGKCVDX5FLZKDRPNFAHG4"),"setMessageForPushNotification",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xMessage",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6TAO24PHUBDTZB3WIW2JPOVZ7E"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth77DP3WHDOZCAFECJYPXE6AOEGA"),"getPushNotificationMessageCopy",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthACPBDA7EV3OBDCLWAALOMT5GDM"),"findSuitableChannelId",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("kind",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRFARKLEUJVEKDGIU7H5CZY5VAA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("address",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCL43XZIFWJBFVARBXBJ6WUIVQI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("importance",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW6IGTPDSCBDYLJLONW2PEM6INY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("excludeUnitId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4GEC3EOOSFHKXDVQYRNHSE6FCY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("excludePriority",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFBINRFJK7FFFPFND7PWOQQ35YM"))
								},org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3CGPAYREYNGR7I555VISNT6J7Y"),"findSuitableChannel",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("msg",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2AHGFA3HGNBOTMX5JJPGTMFEBE"))
								},org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVOAEMIIKPJA7ZLAX5DR7DSCNLM"),"findSuitableChannel",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("msg",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2AHGFA3HGNBOTMX5JJPGTMFEBE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevUnitId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRZBVYJTCURBDJABFZ2LRK6MFVY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevPriority",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr77C3RAZPGJEXZDFU7W3SBM7EDI"))
								},org.radixware.kernel.common.enums.EValType.INT)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::PersoComm::OutMessage - Desktop Executable*/

/*Radix::PersoComm::OutMessage-Entity Class*/

package org.radixware.ads.PersoComm.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage")
public interface OutMessage {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

		/*Radix::PersoComm::OutMessageGroup:newChannel:newChannel-Presentation Property*/




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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:newChannel:newChannel")
			public  Int getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:newChannel:newChannel")
			public   void setValue(Int val) {
				Value = val;
			}
		}
		public NewChannel getNewChannel(){return (NewChannel)getProperty(pgpVRTQB3PDBZF2DAEGZIYC2ME2ZE);}

		/*Radix::PersoComm::OutMessageGroup:newDestinationAddress:newDestinationAddress-Presentation Property*/




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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:newDestinationAddress:newDestinationAddress")
			public  Str getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:newDestinationAddress:newDestinationAddress")
			public   void setValue(Str val) {
				Value = val;
			}
		}
		public NewDestinationAddress getNewDestinationAddress(){return (NewDestinationAddress)getProperty(pgpN5WWFDEAGZERFIIJO26OOZOJYQ);}

		/*Radix::PersoComm::OutMessageGroup:newDueTime:newDueTime-Presentation Property*/




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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:newDueTime:newDueTime")
			public  java.sql.Timestamp getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:newDueTime:newDueTime")
			public   void setValue(java.sql.Timestamp val) {
				Value = val;
			}
		}
		public NewDueTime getNewDueTime(){return (NewDueTime)getProperty(pgpJVTMEBULMJCHZDGIWO7C5F6YKI);}

		/*Radix::PersoComm::OutMessageGroup:oldChannel:oldChannel-Presentation Property*/




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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:oldChannel:oldChannel")
			public  Str getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:oldChannel:oldChannel")
			public   void setValue(Str val) {
				Value = val;
			}
		}
		public OldChannel getOldChannel(){return (OldChannel)getProperty(pgpUMVUGGT64JDFVBKG2PTM46BKDM);}

		/*Radix::PersoComm::OutMessageGroup:oldDestinationAddress:oldDestinationAddress-Presentation Property*/




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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:oldDestinationAddress:oldDestinationAddress")
			public  Str getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:oldDestinationAddress:oldDestinationAddress")
			public   void setValue(Str val) {
				Value = val;
			}
		}
		public OldDestinationAddress getOldDestinationAddress(){return (OldDestinationAddress)getProperty(pgpDUIGOMIG75FWRK4SFT3T3K3LG4);}

		/*Radix::PersoComm::OutMessageGroup:needReplace:needReplace-Presentation Property*/




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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:needReplace:needReplace")
			public  Bool getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:needReplace:needReplace")
			public   void setValue(Bool val) {
				Value = val;
			}
		}
		public NeedReplace getNeedReplace(){return (NeedReplace)getProperty(pgpANTORLZVUBCMFKO7YDNYHP5ZZU);}

















		public org.radixware.ads.PersoComm.explorer.OutMessage.OutMessage_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.PersoComm.explorer.OutMessage.OutMessage_DefaultModel )  super.getEntity(i);}
	}































































































































































































































































	/*Radix::PersoComm::OutMessage:failedLastSendDate:failedLastSendDate-Presentation Property*/


	public class FailedLastSendDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public FailedLastSendDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedLastSendDate:failedLastSendDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedLastSendDate:failedLastSendDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public FailedLastSendDate getFailedLastSendDate();
	/*Radix::PersoComm::OutMessage:failedMessage:failedMessage-Presentation Property*/


	public class FailedMessage extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public FailedMessage(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedMessage:failedMessage")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedMessage:failedMessage")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public FailedMessage getFailedMessage();
	/*Radix::PersoComm::OutMessage:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::PersoComm::OutMessage:channelKind:channelKind-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:channelKind:channelKind")
		public  org.radixware.ads.PersoComm.common.ChannelKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:channelKind:channelKind")
		public   void setValue(org.radixware.ads.PersoComm.common.ChannelKind val) {
			Value = val;
		}
	}
	public ChannelKind getChannelKind();
	/*Radix::PersoComm::OutMessage:lastForwardTime:lastForwardTime-Presentation Property*/


	public class LastForwardTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastForwardTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:lastForwardTime:lastForwardTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:lastForwardTime:lastForwardTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastForwardTime getLastForwardTime();
	/*Radix::PersoComm::OutMessage:subject:subject-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:subject:subject")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:subject:subject")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Subject getSubject();
	/*Radix::PersoComm::OutMessage:sourceMsgId:sourceMsgId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sourceMsgId:sourceMsgId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sourceMsgId:sourceMsgId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SourceMsgId getSourceMsgId();
	/*Radix::PersoComm::OutMessage:sendCallbackMethodName:sendCallbackMethodName-Presentation Property*/


	public class SendCallbackMethodName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SendCallbackMethodName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sendCallbackMethodName:sendCallbackMethodName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sendCallbackMethodName:sendCallbackMethodName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SendCallbackMethodName getSendCallbackMethodName();
	/*Radix::PersoComm::OutMessage:sourceEntityGuid:sourceEntityGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sourceEntityGuid:sourceEntityGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sourceEntityGuid:sourceEntityGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SourceEntityGuid getSourceEntityGuid();
	/*Radix::PersoComm::OutMessage:expireTime:expireTime-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:expireTime:expireTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:expireTime:expireTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public ExpireTime getExpireTime();
	/*Radix::PersoComm::OutMessage:prevStageMessageId:prevStageMessageId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:prevStageMessageId:prevStageMessageId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:prevStageMessageId:prevStageMessageId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PrevStageMessageId getPrevStageMessageId();
	/*Radix::PersoComm::OutMessage:initialDueTime:initialDueTime-Presentation Property*/


	public class InitialDueTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public InitialDueTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:initialDueTime:initialDueTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:initialDueTime:initialDueTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public InitialDueTime getInitialDueTime();
	/*Radix::PersoComm::OutMessage:body:body-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:body:body")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:body:body")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Body getBody();
	/*Radix::PersoComm::OutMessage:importance:importance-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:importance:importance")
		public  org.radixware.kernel.common.enums.EPersoCommImportance getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:importance:importance")
		public   void setValue(org.radixware.kernel.common.enums.EPersoCommImportance val) {
			Value = val;
		}
	}
	public Importance getImportance();
	/*Radix::PersoComm::OutMessage:stageNo:stageNo-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:stageNo:stageNo")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:stageNo:stageNo")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public StageNo getStageNo();
	/*Radix::PersoComm::OutMessage:addressFrom:addressFrom-Presentation Property*/


	public class AddressFrom extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AddressFrom(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:addressFrom:addressFrom")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:addressFrom:addressFrom")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AddressFrom getAddressFrom();
	/*Radix::PersoComm::OutMessage:deliveryTimeout:deliveryTimeout-Presentation Property*/


	public class DeliveryTimeout extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public DeliveryTimeout(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryTimeout:deliveryTimeout")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryTimeout:deliveryTimeout")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DeliveryTimeout getDeliveryTimeout();
	/*Radix::PersoComm::OutMessage:routingKey:routingKey-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:routingKey:routingKey")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:routingKey:routingKey")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoutingKey getRoutingKey();
	/*Radix::PersoComm::OutMessage:destPid:destPid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:destPid:destPid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:destPid:destPid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DestPid getDestPid();
	/*Radix::PersoComm::OutMessage:failedTryCount:failedTryCount-Presentation Property*/


	public class FailedTryCount extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public FailedTryCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedTryCount:failedTryCount")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedTryCount:failedTryCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public FailedTryCount getFailedTryCount();
	/*Radix::PersoComm::OutMessage:ussdServiceOp:ussdServiceOp-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:ussdServiceOp:ussdServiceOp")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:ussdServiceOp:ussdServiceOp")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public UssdServiceOp getUssdServiceOp();
	/*Radix::PersoComm::OutMessage:deliveryCallbackData:deliveryCallbackData-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryCallbackData:deliveryCallbackData")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryCallbackData:deliveryCallbackData")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DeliveryCallbackData getDeliveryCallbackData();
	/*Radix::PersoComm::OutMessage:deliveryCallbackClassName:deliveryCallbackClassName-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryCallbackClassName:deliveryCallbackClassName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryCallbackClassName:deliveryCallbackClassName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DeliveryCallbackClassName getDeliveryCallbackClassName();
	/*Radix::PersoComm::OutMessage:sendCallbackClassName:sendCallbackClassName-Presentation Property*/


	public class SendCallbackClassName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SendCallbackClassName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sendCallbackClassName:sendCallbackClassName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sendCallbackClassName:sendCallbackClassName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SendCallbackClassName getSendCallbackClassName();
	/*Radix::PersoComm::OutMessage:dueTime:dueTime-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:dueTime:dueTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:dueTime:dueTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public DueTime getDueTime();
	/*Radix::PersoComm::OutMessage:createTime:createTime-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:createTime:createTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:createTime:createTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public CreateTime getCreateTime();
	/*Radix::PersoComm::OutMessage:sendCallbackData:sendCallbackData-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sendCallbackData:sendCallbackData")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sendCallbackData:sendCallbackData")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SendCallbackData getSendCallbackData();
	/*Radix::PersoComm::OutMessage:deliveryCallbackMethodName:deliveryCallbackMethodName-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryCallbackMethodName:deliveryCallbackMethodName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryCallbackMethodName:deliveryCallbackMethodName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DeliveryCallbackMethodName getDeliveryCallbackMethodName();
	/*Radix::PersoComm::OutMessage:sourcePid:sourcePid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sourcePid:sourcePid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sourcePid:sourcePid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SourcePid getSourcePid();
	/*Radix::PersoComm::OutMessage:destEntityGuid:destEntityGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:destEntityGuid:destEntityGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:destEntityGuid:destEntityGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DestEntityGuid getDestEntityGuid();
	/*Radix::PersoComm::OutMessage:address:address-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:address:address")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:address:address")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Address getAddress();
	/*Radix::PersoComm::OutMessage:channelId:channelId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:channelId:channelId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:channelId:channelId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ChannelId getChannelId();
	/*Radix::PersoComm::OutMessage:channelUnit:channelUnit-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:channelUnit:channelUnit")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:channelUnit:channelUnit")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ChannelUnit getChannelUnit();
	/*Radix::PersoComm::OutMessage:failedIsUnrecoverable:failedIsUnrecoverable-Presentation Property*/


	public class FailedIsUnrecoverable extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public FailedIsUnrecoverable(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedIsUnrecoverable:failedIsUnrecoverable")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedIsUnrecoverable:failedIsUnrecoverable")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public FailedIsUnrecoverable getFailedIsUnrecoverable();
	/*Radix::PersoComm::OutMessage:maskedBody:maskedBody-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:maskedBody:maskedBody")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:maskedBody:maskedBody")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MaskedBody getMaskedBody();
	/*Radix::PersoComm::OutMessage:isUssd:isUssd-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:isUssd:isUssd")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:isUssd:isUssd")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsUssd getIsUssd();
	/*Radix::PersoComm::OutMessage:encoding:encoding-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:encoding:encoding")
		public  org.radixware.kernel.common.enums.ESmppEncoding getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:encoding:encoding")
		public   void setValue(org.radixware.kernel.common.enums.ESmppEncoding val) {
			Value = val;
		}
	}
	public Encoding getEncoding();
	/*Radix::PersoComm::OutMessage:inProcess:inProcess-Presentation Property*/


	public class InProcess extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public InProcess(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:inProcess:inProcess")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:inProcess:inProcess")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public InProcess getInProcess();
	/*Radix::PersoComm::OutMessage:newDueTime:newDueTime-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:newDueTime:newDueTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:newDueTime:newDueTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public NewDueTime getNewDueTime();
	/*Radix::PersoComm::OutMessage:prevStageSentMessageId:prevStageSentMessageId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:prevStageSentMessageId:prevStageSentMessageId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:prevStageSentMessageId:prevStageSentMessageId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PrevStageSentMessageId getPrevStageSentMessageId();
	/*Radix::PersoComm::OutMessage:displayedBody:displayedBody-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:displayedBody:displayedBody")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:displayedBody:displayedBody")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DisplayedBody getDisplayedBody();
	/*Radix::PersoComm::OutMessage:sourceTitle:sourceTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sourceTitle:sourceTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sourceTitle:sourceTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SourceTitle getSourceTitle();
	/*Radix::PersoComm::OutMessage:prevStageNotSentMessageId:prevStageNotSentMessageId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:prevStageNotSentMessageId:prevStageNotSentMessageId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:prevStageNotSentMessageId:prevStageNotSentMessageId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PrevStageNotSentMessageId getPrevStageNotSentMessageId();
	/*Radix::PersoComm::OutMessage:newAddress:newAddress-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:newAddress:newAddress")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:newAddress:newAddress")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public NewAddress getNewAddress();
	/*Radix::PersoComm::OutMessage:newChannelId:newChannelId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:newChannelId:newChannelId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:newChannelId:newChannelId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public NewChannelId getNewChannelId();
	public static class ViewPrevStageMessage extends org.radixware.kernel.common.client.models.items.Command{
		protected ViewPrevStageMessage(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class ViewSource extends org.radixware.kernel.common.client.models.items.Command{
		protected ViewSource(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}



}

/* Radix::PersoComm::OutMessage - Desktop Meta*/

/*Radix::PersoComm::OutMessage-Entity Class*/

package org.radixware.ads.PersoComm.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class OutMessage_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::PersoComm::OutMessage:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
			"Radix::PersoComm::OutMessage",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgMBVLLK6EQHE5K3R443YPB7ITPTWDXK2J"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOARLWAJ2VXOBDCLUAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUBY3ORBPVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAYH26YZPVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUBY3ORBPVXOBDCLUAALOMT5GDM"),28673,

			/*Radix::PersoComm::OutMessage:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::PersoComm::OutMessage:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YNY224PUTOBDANVABIFNQAABA"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQHUJIPZUVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:channelKind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4RHX4A4QUTOBDANVABIFNQAABA"),
						"channelKind",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZOC2FYZTVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
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

						/*Radix::PersoComm::OutMessage:channelKind:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:subject:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5CKW454PUTOBDANVABIFNQAABA"),
						"subject",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO57MIEBXVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:subject:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:sourceMsgId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6LCP3VS5MFBXFIXFRYSR3WJBGM"),
						"sourceMsgId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDXHHBCWGMPORDHZ7ABIFNQAABA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:sourceMsgId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:sendCallbackMethodName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6R756U4QUTOBDANVABIFNQAABA"),
						"sendCallbackMethodName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW7RYIIWGMPORDHZ7ABIFNQAABA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:sendCallbackMethodName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:sourceEntityGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6STBOLMQUTOBDANVABIFNQAABA"),
						"sourceEntityGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU6UKJ2K27RERJNKLBSHIKTFKQQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::OutMessage:sourceEntityGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:expireTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBRWI77EPUTOBDANVABIFNQAABA"),
						"expireTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMHMIEMZUVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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

						/*Radix::PersoComm::OutMessage:expireTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:body:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDUU5BZMPUTOBDANVABIFNQAABA"),
						"body",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUVBA45ZPVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::OutMessage:body:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:importance:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDYU5BZMPUTOBDANVABIFNQAABA"),
						"importance",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDGV5QRRUVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::OutMessage:importance:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTUGILCRUVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:destPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHBZLONSMMRDLFPVLUPA36J4GDQ"),
						"destPid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQUJR2WOGMPORDHZ7ABIFNQAABA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:destPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:sendCallbackClassName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLRRSUR4QUTOBDANVABIFNQAABA"),
						"sendCallbackClassName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUXVZKGOGMPORDHZ7ABIFNQAABA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:sendCallbackClassName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:dueTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2TKV4MPUTOBDANVABIFNQAABA"),
						"dueTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYZNCJ5RTVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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

						/*Radix::PersoComm::OutMessage:dueTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCLKNWCNLVRDTXIIVGFFH5Q6IYE"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:createTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWTKV4MPUTOBDANVABIFNQAABA"),
						"createTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMQ2NF3RTVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::OutMessage:createTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:sourcePid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPDVAS7UQUTOBDANVABIFNQAABA"),
						"sourcePid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRKRWJ66IVNGQFFHJ6J4FZRDYSQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::OutMessage:sourcePid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:destEntityGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPIHVQDD6VZBQVMO5CNQ7SP33AU"),
						"destEntityGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVEXQ2UOGMPORDHZ7ABIFNQAABA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:destEntityGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:address:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRIH4IEQUTOBDANVABIFNQAABA"),
						"address",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXGQVVBELVTOBDCLTAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::OutMessage:address:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:channelId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQQY3SX4QUTOBDANVABIFNQAABA"),
						"channelId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE5HOJXYNJVCVXPBCSWYOB7YZLU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.INT,
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::OutMessage:channelId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:sourceTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGQYOL3K2GFBQPKXDOFAII3RJRI"),
						"sourceTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCJYZBFWZVVELNOKBIVPSLWCU7E"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:sourceTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:channelUnit:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR255GGIKEFEYVA4P4RR5GYUZNI"),
						"channelUnit",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

					/*Radix::PersoComm::OutMessage:encoding:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWBAD4MPNUFFYZCHKSKE4LH6LHI"),
						"encoding",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2QPJU7HCM5EZRGTPEJZCSYHHAE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:encoding:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsSMFAHKLBOBGHVDYOXCSGSNFDME"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:addressFrom:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFLYC2OLNIBEQRMU4HSLQGRFMLA"),
						"addressFrom",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHC3X4FMRCNDYXBDIVB5IXOEFKY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:addressFrom:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2BVFZGCZQZEVJG5FCKOPGC4F6Y"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:failedIsUnrecoverable:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colS2LOJTRFBJCDFHYYJEGO5QSN24"),
						"failedIsUnrecoverable",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIKCBVENZYFAPZFCQ3JELPLNOWU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::OutMessage:failedIsUnrecoverable:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:failedLastSendDate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2NMC43PMHRGYBONJZFLS22YQ3E"),
						"failedLastSendDate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDX4DLJCNM5HZJI5JXNVZ4YCUAI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:failedLastSendDate:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:failedMessage:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DAJREKW3NHERCUYZJEOXLZKXQ"),
						"failedMessage",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAITTCPQVMVCLTBMT4LNDQT572U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:failedMessage:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:failedTryCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHIAURDZIHRC7JA5STQBH6PVXUQ"),
						"failedTryCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNLAKKOZ3MZBHBGNPY5ZU7HNZI4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.INT,
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::OutMessage:failedTryCount:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:newAddress:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSUZRJQGMPFDLTGETUOEJQTSR2Y"),
						"newAddress",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2MZHLCKU7FHZBMLAKWM35HJBFQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:newAddress:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:newChannelId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYAU6AYKJXJGKDPNDOQYWOOAQGI"),
						"newChannelId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX6SLCYYSBFE6FGV4TQ33PMGUFI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:newChannelId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:newDueTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd53CYEZWTRFFXXLAQWAET4S4BYA"),
						"newDueTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsILEKIENMIRCLLONO4VTLR2ZFIM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:newDueTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:maskedBody:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSEJB3YF6WFEZHOX4HX24NF4H4A"),
						"maskedBody",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLXLCAVSKLVBE3MUFPWBGTTID2Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:maskedBody:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:displayedBody:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDKI6KZ4SSBDXVHP3WMWQOM3HUY"),
						"displayedBody",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5ULOH2HAKBF67PVPAX77U2LGA4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:displayedBody:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:routingKey:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSWFU5C55JC6HKXPXQCZIC25LM"),
						"routingKey",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM37ZJNBXPBALRMMSV4A7GRJMNY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:routingKey:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:inProcess:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZKRADR2XH5AWHOSJVUP3XFJRLI"),
						"inProcess",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6OA2Q6NSBRCO3JSF2KY3IQR7VE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::OutMessage:inProcess:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:initialDueTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCSFRDRISERF5BCBFNGO7PIVLOI"),
						"initialDueTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQPAHK5XB6ZFE5KRWJRJ33D74LY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:initialDueTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTM4ECO4U7VFXPCH65ZFXCHQAGM"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:lastForwardTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4Z7PGG5ZF5AMHG5FHZUWDBDN5I"),
						"lastForwardTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNPMSOSC7FNE3HGQ3GV4DSMM2CQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.EXPRESSION,
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

						/*Radix::PersoComm::OutMessage:lastForwardTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:isUssd:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSTBPUQBGHRBLBNXEY6BL2ZSKOY"),
						"isUssd",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4QQZVC6KXNCZXGXRJWDL4RCIDQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::OutMessage:isUssd:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:ussdServiceOp:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHJPGXALIU5CFFNE3AXBAPQD4XM"),
						"ussdServiceOp",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVHSET6D5XBHQNKXULNU65OQQV4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:ussdServiceOp:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:deliveryCallbackClassName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK5DZH446IRBRVFDGJRBTGYEWEQ"),
						"deliveryCallbackClassName",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:deliveryCallbackClassName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:deliveryCallbackMethodName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP2XZB6FQONDH5GA4ZLDWHO2FVI"),
						"deliveryCallbackMethodName",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:deliveryCallbackMethodName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:deliveryTimeout:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colG2MGIAWXPJDGZMPJHM4QG6P4QA"),
						"deliveryTimeout",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:deliveryTimeout:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:sendCallbackData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNQOCLTUC4BADNMZKWPLKNSRFSM"),
						"sendCallbackData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:sendCallbackData:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:deliveryCallbackData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHRVJLPPFBNFQ5KLQE3K7P2ND5U"),
						"deliveryCallbackData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:deliveryCallbackData:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:stageNo:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colF4WMZEHSXZDCLLMKZHU73A57PA"),
						"stageNo",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC4LJPRTPFJAK5JBISDJDAIIBIQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:stageNo:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:prevStageMessageId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBWUMTS2TWBEIPPXRMTVTPH3ZQY"),
						"prevStageMessageId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUGSOWHJV7RFCDCLR5CFBLEPK5E"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:prevStageMessageId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:prevStageNotSentMessageId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJXWN5XA7RVE5LBTICQEOT4HJZ4"),
						"prevStageNotSentMessageId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:prevStageNotSentMessageId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:prevStageSentMessageId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5SSHOXXF7JFXJKVVCPWDKLRZQA"),
						"prevStageSentMessageId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:prevStageSentMessageId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::PersoComm::OutMessage:ViewSource-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdY6IIW44J7ZADVKESCT6Y65EHPA"),
						"ViewSource",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGJVXEKRQUNCANBK3HNEMS3RY2M"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgEBTCSRR6XJGUJHPO2J4QIENJSI"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.LOCAL,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGQYOL3K2GFBQPKXDOFAII3RJRI")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::PersoComm::OutMessage:ViewPrevStageMessage-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd4SMJ76W7VNHJPFHVAABZ2QCLTY"),
						"ViewPrevStageMessage",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHA5PHI34A5CPRCMOHNQI35VUYE"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgEBTCSRR6XJGUJHPO2J4QIENJSI"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.LOCAL,
						true,
						false,
						true,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colBWUMTS2TWBEIPPXRMTVTPH3ZQY")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT)
			},
			null,
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::PersoComm::OutMessage:CreateTime-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtVYQNNZJZA5EKPGANNF6HR2Q2AQ"),
						"CreateTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEJMVIMVORJBJDLU6JNDHP7JLMQ"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWTKV4MPUTOBDANVABIFNQAABA"),
								false)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::PersoComm::OutMessage:DueTime-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtDA5NSIAZ55HEJJXX2OU7F6JAGQ"),
						"DueTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4DC2PCMZRBATHLCF7FNM5WEP7Y"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2TKV4MPUTOBDANVABIFNQAABA"),
								true)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refNA2RGKBQVXOBDCLUAALOMT5GDM"),"OutMessage=>Unit (channelId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblGBOAYN4PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colQQY3SX4QUTOBDANVABIFNQAABA")},new String[]{"channelId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprKTXPYSJZVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOD336QIYRRBY5IH274Z7U24EIM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOGAUTNZ7AFBDTHQYJNYCVQTJGM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOARLWAJ2VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVOMDNSLYXRBG5H7TIL3FNC3RYU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprAUXDUZIHSRDWRB4VRONHDFKDSM")},
			false,false,false);
}

/* Radix::PersoComm::OutMessage - Web Executable*/

/*Radix::PersoComm::OutMessage-Entity Class*/

package org.radixware.ads.PersoComm.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage")
public interface OutMessage {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

		/*Radix::PersoComm::OutMessageGroup:newChannel:newChannel-Presentation Property*/




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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:newChannel:newChannel")
			public  Int getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:newChannel:newChannel")
			public   void setValue(Int val) {
				Value = val;
			}
		}
		public NewChannel getNewChannel(){return (NewChannel)getProperty(pgpVRTQB3PDBZF2DAEGZIYC2ME2ZE);}

		/*Radix::PersoComm::OutMessageGroup:newDestinationAddress:newDestinationAddress-Presentation Property*/




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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:newDestinationAddress:newDestinationAddress")
			public  Str getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:newDestinationAddress:newDestinationAddress")
			public   void setValue(Str val) {
				Value = val;
			}
		}
		public NewDestinationAddress getNewDestinationAddress(){return (NewDestinationAddress)getProperty(pgpN5WWFDEAGZERFIIJO26OOZOJYQ);}

		/*Radix::PersoComm::OutMessageGroup:newDueTime:newDueTime-Presentation Property*/




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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:newDueTime:newDueTime")
			public  java.sql.Timestamp getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:newDueTime:newDueTime")
			public   void setValue(java.sql.Timestamp val) {
				Value = val;
			}
		}
		public NewDueTime getNewDueTime(){return (NewDueTime)getProperty(pgpJVTMEBULMJCHZDGIWO7C5F6YKI);}

		/*Radix::PersoComm::OutMessageGroup:oldChannel:oldChannel-Presentation Property*/




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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:oldChannel:oldChannel")
			public  Str getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:oldChannel:oldChannel")
			public   void setValue(Str val) {
				Value = val;
			}
		}
		public OldChannel getOldChannel(){return (OldChannel)getProperty(pgpUMVUGGT64JDFVBKG2PTM46BKDM);}

		/*Radix::PersoComm::OutMessageGroup:oldDestinationAddress:oldDestinationAddress-Presentation Property*/




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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:oldDestinationAddress:oldDestinationAddress")
			public  Str getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:oldDestinationAddress:oldDestinationAddress")
			public   void setValue(Str val) {
				Value = val;
			}
		}
		public OldDestinationAddress getOldDestinationAddress(){return (OldDestinationAddress)getProperty(pgpDUIGOMIG75FWRK4SFT3T3K3LG4);}

		/*Radix::PersoComm::OutMessageGroup:needReplace:needReplace-Presentation Property*/




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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:needReplace:needReplace")
			public  Bool getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessageGroup:needReplace:needReplace")
			public   void setValue(Bool val) {
				Value = val;
			}
		}
		public NeedReplace getNeedReplace(){return (NeedReplace)getProperty(pgpANTORLZVUBCMFKO7YDNYHP5ZZU);}

















		public org.radixware.ads.PersoComm.web.OutMessage.OutMessage_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.PersoComm.web.OutMessage.OutMessage_DefaultModel )  super.getEntity(i);}
	}































































































































































































































































	/*Radix::PersoComm::OutMessage:failedLastSendDate:failedLastSendDate-Presentation Property*/


	public class FailedLastSendDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public FailedLastSendDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedLastSendDate:failedLastSendDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedLastSendDate:failedLastSendDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public FailedLastSendDate getFailedLastSendDate();
	/*Radix::PersoComm::OutMessage:failedMessage:failedMessage-Presentation Property*/


	public class FailedMessage extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public FailedMessage(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedMessage:failedMessage")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedMessage:failedMessage")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public FailedMessage getFailedMessage();
	/*Radix::PersoComm::OutMessage:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::PersoComm::OutMessage:channelKind:channelKind-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:channelKind:channelKind")
		public  org.radixware.ads.PersoComm.common.ChannelKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:channelKind:channelKind")
		public   void setValue(org.radixware.ads.PersoComm.common.ChannelKind val) {
			Value = val;
		}
	}
	public ChannelKind getChannelKind();
	/*Radix::PersoComm::OutMessage:lastForwardTime:lastForwardTime-Presentation Property*/


	public class LastForwardTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastForwardTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:lastForwardTime:lastForwardTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:lastForwardTime:lastForwardTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastForwardTime getLastForwardTime();
	/*Radix::PersoComm::OutMessage:subject:subject-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:subject:subject")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:subject:subject")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Subject getSubject();
	/*Radix::PersoComm::OutMessage:sourceMsgId:sourceMsgId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sourceMsgId:sourceMsgId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sourceMsgId:sourceMsgId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SourceMsgId getSourceMsgId();
	/*Radix::PersoComm::OutMessage:sendCallbackMethodName:sendCallbackMethodName-Presentation Property*/


	public class SendCallbackMethodName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SendCallbackMethodName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sendCallbackMethodName:sendCallbackMethodName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sendCallbackMethodName:sendCallbackMethodName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SendCallbackMethodName getSendCallbackMethodName();
	/*Radix::PersoComm::OutMessage:sourceEntityGuid:sourceEntityGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sourceEntityGuid:sourceEntityGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sourceEntityGuid:sourceEntityGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SourceEntityGuid getSourceEntityGuid();
	/*Radix::PersoComm::OutMessage:expireTime:expireTime-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:expireTime:expireTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:expireTime:expireTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public ExpireTime getExpireTime();
	/*Radix::PersoComm::OutMessage:prevStageMessageId:prevStageMessageId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:prevStageMessageId:prevStageMessageId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:prevStageMessageId:prevStageMessageId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PrevStageMessageId getPrevStageMessageId();
	/*Radix::PersoComm::OutMessage:initialDueTime:initialDueTime-Presentation Property*/


	public class InitialDueTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public InitialDueTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:initialDueTime:initialDueTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:initialDueTime:initialDueTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public InitialDueTime getInitialDueTime();
	/*Radix::PersoComm::OutMessage:body:body-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:body:body")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:body:body")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Body getBody();
	/*Radix::PersoComm::OutMessage:importance:importance-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:importance:importance")
		public  org.radixware.kernel.common.enums.EPersoCommImportance getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:importance:importance")
		public   void setValue(org.radixware.kernel.common.enums.EPersoCommImportance val) {
			Value = val;
		}
	}
	public Importance getImportance();
	/*Radix::PersoComm::OutMessage:stageNo:stageNo-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:stageNo:stageNo")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:stageNo:stageNo")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public StageNo getStageNo();
	/*Radix::PersoComm::OutMessage:addressFrom:addressFrom-Presentation Property*/


	public class AddressFrom extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AddressFrom(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:addressFrom:addressFrom")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:addressFrom:addressFrom")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AddressFrom getAddressFrom();
	/*Radix::PersoComm::OutMessage:deliveryTimeout:deliveryTimeout-Presentation Property*/


	public class DeliveryTimeout extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public DeliveryTimeout(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryTimeout:deliveryTimeout")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryTimeout:deliveryTimeout")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DeliveryTimeout getDeliveryTimeout();
	/*Radix::PersoComm::OutMessage:routingKey:routingKey-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:routingKey:routingKey")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:routingKey:routingKey")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoutingKey getRoutingKey();
	/*Radix::PersoComm::OutMessage:destPid:destPid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:destPid:destPid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:destPid:destPid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DestPid getDestPid();
	/*Radix::PersoComm::OutMessage:failedTryCount:failedTryCount-Presentation Property*/


	public class FailedTryCount extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public FailedTryCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedTryCount:failedTryCount")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedTryCount:failedTryCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public FailedTryCount getFailedTryCount();
	/*Radix::PersoComm::OutMessage:ussdServiceOp:ussdServiceOp-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:ussdServiceOp:ussdServiceOp")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:ussdServiceOp:ussdServiceOp")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public UssdServiceOp getUssdServiceOp();
	/*Radix::PersoComm::OutMessage:deliveryCallbackData:deliveryCallbackData-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryCallbackData:deliveryCallbackData")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryCallbackData:deliveryCallbackData")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DeliveryCallbackData getDeliveryCallbackData();
	/*Radix::PersoComm::OutMessage:deliveryCallbackClassName:deliveryCallbackClassName-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryCallbackClassName:deliveryCallbackClassName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryCallbackClassName:deliveryCallbackClassName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DeliveryCallbackClassName getDeliveryCallbackClassName();
	/*Radix::PersoComm::OutMessage:sendCallbackClassName:sendCallbackClassName-Presentation Property*/


	public class SendCallbackClassName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SendCallbackClassName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sendCallbackClassName:sendCallbackClassName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sendCallbackClassName:sendCallbackClassName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SendCallbackClassName getSendCallbackClassName();
	/*Radix::PersoComm::OutMessage:dueTime:dueTime-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:dueTime:dueTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:dueTime:dueTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public DueTime getDueTime();
	/*Radix::PersoComm::OutMessage:createTime:createTime-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:createTime:createTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:createTime:createTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public CreateTime getCreateTime();
	/*Radix::PersoComm::OutMessage:sendCallbackData:sendCallbackData-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sendCallbackData:sendCallbackData")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sendCallbackData:sendCallbackData")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SendCallbackData getSendCallbackData();
	/*Radix::PersoComm::OutMessage:deliveryCallbackMethodName:deliveryCallbackMethodName-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryCallbackMethodName:deliveryCallbackMethodName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:deliveryCallbackMethodName:deliveryCallbackMethodName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DeliveryCallbackMethodName getDeliveryCallbackMethodName();
	/*Radix::PersoComm::OutMessage:sourcePid:sourcePid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sourcePid:sourcePid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sourcePid:sourcePid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SourcePid getSourcePid();
	/*Radix::PersoComm::OutMessage:destEntityGuid:destEntityGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:destEntityGuid:destEntityGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:destEntityGuid:destEntityGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DestEntityGuid getDestEntityGuid();
	/*Radix::PersoComm::OutMessage:address:address-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:address:address")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:address:address")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Address getAddress();
	/*Radix::PersoComm::OutMessage:channelId:channelId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:channelId:channelId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:channelId:channelId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ChannelId getChannelId();
	/*Radix::PersoComm::OutMessage:channelUnit:channelUnit-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:channelUnit:channelUnit")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:channelUnit:channelUnit")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ChannelUnit getChannelUnit();
	/*Radix::PersoComm::OutMessage:failedIsUnrecoverable:failedIsUnrecoverable-Presentation Property*/


	public class FailedIsUnrecoverable extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public FailedIsUnrecoverable(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedIsUnrecoverable:failedIsUnrecoverable")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:failedIsUnrecoverable:failedIsUnrecoverable")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public FailedIsUnrecoverable getFailedIsUnrecoverable();
	/*Radix::PersoComm::OutMessage:maskedBody:maskedBody-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:maskedBody:maskedBody")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:maskedBody:maskedBody")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MaskedBody getMaskedBody();
	/*Radix::PersoComm::OutMessage:isUssd:isUssd-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:isUssd:isUssd")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:isUssd:isUssd")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsUssd getIsUssd();
	/*Radix::PersoComm::OutMessage:encoding:encoding-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:encoding:encoding")
		public  org.radixware.kernel.common.enums.ESmppEncoding getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:encoding:encoding")
		public   void setValue(org.radixware.kernel.common.enums.ESmppEncoding val) {
			Value = val;
		}
	}
	public Encoding getEncoding();
	/*Radix::PersoComm::OutMessage:inProcess:inProcess-Presentation Property*/


	public class InProcess extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public InProcess(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:inProcess:inProcess")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:inProcess:inProcess")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public InProcess getInProcess();
	/*Radix::PersoComm::OutMessage:newDueTime:newDueTime-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:newDueTime:newDueTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:newDueTime:newDueTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public NewDueTime getNewDueTime();
	/*Radix::PersoComm::OutMessage:prevStageSentMessageId:prevStageSentMessageId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:prevStageSentMessageId:prevStageSentMessageId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:prevStageSentMessageId:prevStageSentMessageId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PrevStageSentMessageId getPrevStageSentMessageId();
	/*Radix::PersoComm::OutMessage:displayedBody:displayedBody-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:displayedBody:displayedBody")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:displayedBody:displayedBody")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DisplayedBody getDisplayedBody();
	/*Radix::PersoComm::OutMessage:sourceTitle:sourceTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sourceTitle:sourceTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:sourceTitle:sourceTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SourceTitle getSourceTitle();
	/*Radix::PersoComm::OutMessage:prevStageNotSentMessageId:prevStageNotSentMessageId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:prevStageNotSentMessageId:prevStageNotSentMessageId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:prevStageNotSentMessageId:prevStageNotSentMessageId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PrevStageNotSentMessageId getPrevStageNotSentMessageId();
	/*Radix::PersoComm::OutMessage:newAddress:newAddress-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:newAddress:newAddress")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:newAddress:newAddress")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public NewAddress getNewAddress();
	/*Radix::PersoComm::OutMessage:newChannelId:newChannelId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:newChannelId:newChannelId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:newChannelId:newChannelId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public NewChannelId getNewChannelId();
	public static class ViewPrevStageMessage extends org.radixware.kernel.common.client.models.items.Command{
		protected ViewPrevStageMessage(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class ViewSource extends org.radixware.kernel.common.client.models.items.Command{
		protected ViewSource(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}



}

/* Radix::PersoComm::OutMessage - Web Meta*/

/*Radix::PersoComm::OutMessage-Entity Class*/

package org.radixware.ads.PersoComm.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class OutMessage_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::PersoComm::OutMessage:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
			"Radix::PersoComm::OutMessage",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgMBVLLK6EQHE5K3R443YPB7ITPTWDXK2J"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOARLWAJ2VXOBDCLUAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUBY3ORBPVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAYH26YZPVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUBY3ORBPVXOBDCLUAALOMT5GDM"),28673,

			/*Radix::PersoComm::OutMessage:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::PersoComm::OutMessage:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YNY224PUTOBDANVABIFNQAABA"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQHUJIPZUVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:channelKind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4RHX4A4QUTOBDANVABIFNQAABA"),
						"channelKind",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZOC2FYZTVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
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

						/*Radix::PersoComm::OutMessage:channelKind:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:subject:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5CKW454PUTOBDANVABIFNQAABA"),
						"subject",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO57MIEBXVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:subject:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:sourceMsgId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6LCP3VS5MFBXFIXFRYSR3WJBGM"),
						"sourceMsgId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDXHHBCWGMPORDHZ7ABIFNQAABA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:sourceMsgId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:sendCallbackMethodName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6R756U4QUTOBDANVABIFNQAABA"),
						"sendCallbackMethodName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW7RYIIWGMPORDHZ7ABIFNQAABA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:sendCallbackMethodName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:sourceEntityGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6STBOLMQUTOBDANVABIFNQAABA"),
						"sourceEntityGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU6UKJ2K27RERJNKLBSHIKTFKQQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::OutMessage:sourceEntityGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:expireTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBRWI77EPUTOBDANVABIFNQAABA"),
						"expireTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMHMIEMZUVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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

						/*Radix::PersoComm::OutMessage:expireTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:body:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDUU5BZMPUTOBDANVABIFNQAABA"),
						"body",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUVBA45ZPVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::OutMessage:body:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:importance:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDYU5BZMPUTOBDANVABIFNQAABA"),
						"importance",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDGV5QRRUVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::OutMessage:importance:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTUGILCRUVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:destPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHBZLONSMMRDLFPVLUPA36J4GDQ"),
						"destPid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQUJR2WOGMPORDHZ7ABIFNQAABA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:destPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:sendCallbackClassName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLRRSUR4QUTOBDANVABIFNQAABA"),
						"sendCallbackClassName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUXVZKGOGMPORDHZ7ABIFNQAABA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:sendCallbackClassName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:dueTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2TKV4MPUTOBDANVABIFNQAABA"),
						"dueTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYZNCJ5RTVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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

						/*Radix::PersoComm::OutMessage:dueTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCLKNWCNLVRDTXIIVGFFH5Q6IYE"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:createTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWTKV4MPUTOBDANVABIFNQAABA"),
						"createTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMQ2NF3RTVXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::OutMessage:createTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:sourcePid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPDVAS7UQUTOBDANVABIFNQAABA"),
						"sourcePid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRKRWJ66IVNGQFFHJ6J4FZRDYSQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::OutMessage:sourcePid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:destEntityGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPIHVQDD6VZBQVMO5CNQ7SP33AU"),
						"destEntityGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVEXQ2UOGMPORDHZ7ABIFNQAABA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:destEntityGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:address:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRIH4IEQUTOBDANVABIFNQAABA"),
						"address",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXGQVVBELVTOBDCLTAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::OutMessage:address:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:channelId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQQY3SX4QUTOBDANVABIFNQAABA"),
						"channelId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE5HOJXYNJVCVXPBCSWYOB7YZLU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.INT,
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::OutMessage:channelId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:sourceTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGQYOL3K2GFBQPKXDOFAII3RJRI"),
						"sourceTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCJYZBFWZVVELNOKBIVPSLWCU7E"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:sourceTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:channelUnit:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR255GGIKEFEYVA4P4RR5GYUZNI"),
						"channelUnit",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

					/*Radix::PersoComm::OutMessage:encoding:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWBAD4MPNUFFYZCHKSKE4LH6LHI"),
						"encoding",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2QPJU7HCM5EZRGTPEJZCSYHHAE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:encoding:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsSMFAHKLBOBGHVDYOXCSGSNFDME"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:addressFrom:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFLYC2OLNIBEQRMU4HSLQGRFMLA"),
						"addressFrom",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHC3X4FMRCNDYXBDIVB5IXOEFKY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:addressFrom:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2BVFZGCZQZEVJG5FCKOPGC4F6Y"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:failedIsUnrecoverable:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colS2LOJTRFBJCDFHYYJEGO5QSN24"),
						"failedIsUnrecoverable",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIKCBVENZYFAPZFCQ3JELPLNOWU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::OutMessage:failedIsUnrecoverable:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:failedLastSendDate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2NMC43PMHRGYBONJZFLS22YQ3E"),
						"failedLastSendDate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDX4DLJCNM5HZJI5JXNVZ4YCUAI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:failedLastSendDate:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:failedMessage:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DAJREKW3NHERCUYZJEOXLZKXQ"),
						"failedMessage",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAITTCPQVMVCLTBMT4LNDQT572U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:failedMessage:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:failedTryCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHIAURDZIHRC7JA5STQBH6PVXUQ"),
						"failedTryCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNLAKKOZ3MZBHBGNPY5ZU7HNZI4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.INT,
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::OutMessage:failedTryCount:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:newAddress:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSUZRJQGMPFDLTGETUOEJQTSR2Y"),
						"newAddress",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2MZHLCKU7FHZBMLAKWM35HJBFQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:newAddress:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:newChannelId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYAU6AYKJXJGKDPNDOQYWOOAQGI"),
						"newChannelId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX6SLCYYSBFE6FGV4TQ33PMGUFI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:newChannelId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:newDueTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd53CYEZWTRFFXXLAQWAET4S4BYA"),
						"newDueTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsILEKIENMIRCLLONO4VTLR2ZFIM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:newDueTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:maskedBody:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSEJB3YF6WFEZHOX4HX24NF4H4A"),
						"maskedBody",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLXLCAVSKLVBE3MUFPWBGTTID2Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:maskedBody:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:displayedBody:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDKI6KZ4SSBDXVHP3WMWQOM3HUY"),
						"displayedBody",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5ULOH2HAKBF67PVPAX77U2LGA4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:displayedBody:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:routingKey:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSWFU5C55JC6HKXPXQCZIC25LM"),
						"routingKey",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM37ZJNBXPBALRMMSV4A7GRJMNY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:routingKey:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:inProcess:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZKRADR2XH5AWHOSJVUP3XFJRLI"),
						"inProcess",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6OA2Q6NSBRCO3JSF2KY3IQR7VE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::OutMessage:inProcess:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:initialDueTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCSFRDRISERF5BCBFNGO7PIVLOI"),
						"initialDueTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQPAHK5XB6ZFE5KRWJRJ33D74LY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:initialDueTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTM4ECO4U7VFXPCH65ZFXCHQAGM"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:lastForwardTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4Z7PGG5ZF5AMHG5FHZUWDBDN5I"),
						"lastForwardTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNPMSOSC7FNE3HGQ3GV4DSMM2CQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.EXPRESSION,
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

						/*Radix::PersoComm::OutMessage:lastForwardTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:isUssd:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSTBPUQBGHRBLBNXEY6BL2ZSKOY"),
						"isUssd",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4QQZVC6KXNCZXGXRJWDL4RCIDQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::OutMessage:isUssd:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:ussdServiceOp:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHJPGXALIU5CFFNE3AXBAPQD4XM"),
						"ussdServiceOp",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVHSET6D5XBHQNKXULNU65OQQV4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:ussdServiceOp:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:deliveryCallbackClassName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK5DZH446IRBRVFDGJRBTGYEWEQ"),
						"deliveryCallbackClassName",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:deliveryCallbackClassName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:deliveryCallbackMethodName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP2XZB6FQONDH5GA4ZLDWHO2FVI"),
						"deliveryCallbackMethodName",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:deliveryCallbackMethodName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:deliveryTimeout:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colG2MGIAWXPJDGZMPJHM4QG6P4QA"),
						"deliveryTimeout",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:deliveryTimeout:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:sendCallbackData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNQOCLTUC4BADNMZKWPLKNSRFSM"),
						"sendCallbackData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:sendCallbackData:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:deliveryCallbackData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHRVJLPPFBNFQ5KLQE3K7P2ND5U"),
						"deliveryCallbackData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:deliveryCallbackData:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:stageNo:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colF4WMZEHSXZDCLLMKZHU73A57PA"),
						"stageNo",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC4LJPRTPFJAK5JBISDJDAIIBIQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:stageNo:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:prevStageMessageId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBWUMTS2TWBEIPPXRMTVTPH3ZQY"),
						"prevStageMessageId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUGSOWHJV7RFCDCLR5CFBLEPK5E"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:prevStageMessageId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:prevStageNotSentMessageId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJXWN5XA7RVE5LBTICQEOT4HJZ4"),
						"prevStageNotSentMessageId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:prevStageNotSentMessageId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::OutMessage:prevStageSentMessageId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5SSHOXXF7JFXJKVVCPWDKLRZQA"),
						"prevStageSentMessageId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
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

						/*Radix::PersoComm::OutMessage:prevStageSentMessageId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::PersoComm::OutMessage:ViewSource-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdY6IIW44J7ZADVKESCT6Y65EHPA"),
						"ViewSource",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGJVXEKRQUNCANBK3HNEMS3RY2M"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgEBTCSRR6XJGUJHPO2J4QIENJSI"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.LOCAL,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGQYOL3K2GFBQPKXDOFAII3RJRI")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::PersoComm::OutMessage:ViewPrevStageMessage-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd4SMJ76W7VNHJPFHVAABZ2QCLTY"),
						"ViewPrevStageMessage",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHA5PHI34A5CPRCMOHNQI35VUYE"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgEBTCSRR6XJGUJHPO2J4QIENJSI"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.LOCAL,
						true,
						false,
						true,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colBWUMTS2TWBEIPPXRMTVTPH3ZQY")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT)
			},
			null,
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::PersoComm::OutMessage:CreateTime-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtVYQNNZJZA5EKPGANNF6HR2Q2AQ"),
						"CreateTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEJMVIMVORJBJDLU6JNDHP7JLMQ"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWTKV4MPUTOBDANVABIFNQAABA"),
								false)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::PersoComm::OutMessage:DueTime-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtDA5NSIAZ55HEJJXX2OU7F6JAGQ"),
						"DueTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4DC2PCMZRBATHLCF7FNM5WEP7Y"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2TKV4MPUTOBDANVABIFNQAABA"),
								true)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refNA2RGKBQVXOBDCLUAALOMT5GDM"),"OutMessage=>Unit (channelId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblGBOAYN4PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colQQY3SX4QUTOBDANVABIFNQAABA")},new String[]{"channelId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprKTXPYSJZVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOD336QIYRRBY5IH274Z7U24EIM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOGAUTNZ7AFBDTHQYJNYCVQTJGM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOARLWAJ2VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVOMDNSLYXRBG5H7TIL3FNC3RYU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprAUXDUZIHSRDWRB4VRONHDFKDSM")},
			false,false,false);
}

/* Radix::PersoComm::OutMessage:General - Desktop Meta*/

/*Radix::PersoComm::OutMessage:General-Editor Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprKTXPYSJZVXOBDCLUAALOMT5GDM"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblGBOAYN4PUTOBDANVABIFNQAABA"),
	null,
	null,

	/*Radix::PersoComm::OutMessage:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::PersoComm::OutMessage:General:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg65LX33RZVXOBDCLUAALOMT5GDM"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVPCBFQWEMPORDHZ7ABIFNQAABA"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YNY224PUTOBDANVABIFNQAABA"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWTKV4MPUTOBDANVABIFNQAABA"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2TKV4MPUTOBDANVABIFNQAABA"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBRWI77EPUTOBDANVABIFNQAABA"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDYU5BZMPUTOBDANVABIFNQAABA"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRIH4IEQUTOBDANVABIFNQAABA"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5CKW454PUTOBDANVABIFNQAABA"),0,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4RHX4A4QUTOBDANVABIFNQAABA"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGQYOL3K2GFBQPKXDOFAII3RJRI"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR255GGIKEFEYVA4P4RR5GYUZNI"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWBAD4MPNUFFYZCHKSKE4LH6LHI"),0,14,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFLYC2OLNIBEQRMU4HSLQGRFMLA"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDKI6KZ4SSBDXVHP3WMWQOM3HUY"),0,13,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSWFU5C55JC6HKXPXQCZIC25LM"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("ppgM2SZIK5CMBG6XGM7LNXEXO32IU"),0,19,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCSFRDRISERF5BCBFNGO7PIVLOI"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSTBPUQBGHRBLBNXEY6BL2ZSKOY"),0,15,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHJPGXALIU5CFFNE3AXBAPQD4XM"),0,16,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBWUMTS2TWBEIPPXRMTVTPH3ZQY"),0,18,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colF4WMZEHSXZDCLLMKZHU73A57PA"),0,17,1,false,false)
			},new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PropertiesGroup[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PropertiesGroup(org.radixware.kernel.common.types.Id.Factory.loadFrom("ppgM2SZIK5CMBG6XGM7LNXEXO32IU"),"Sending",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2MTZWIAGDJF7ZEOORKOW5NUJSQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),false,true,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZKRADR2XH5AWHOSJVUP3XFJRLI"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHIAURDZIHRC7JA5STQBH6PVXUQ"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colS2LOJTRFBJCDFHYYJEGO5QSN24"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DAJREKW3NHERCUYZJEOXLZKXQ"),0,4,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2NMC43PMHRGYBONJZFLS22YQ3E"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4Z7PGG5ZF5AMHG5FHZUWDBDN5I"),0,5,1,false,false)
					})
			}),

			/*Radix::PersoComm::OutMessage:General:Attachements-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMC3TLCXMVXOBDCLVAALOMT5GDM"),"Attachements",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMG3TLCXMVXOBDCLVAALOMT5GDM"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiXHVKI4HMVXOBDCLVAALOMT5GDM")),

			/*Radix::PersoComm::OutMessage:General:System-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgP4BYRTGEMPORDHZ7ABIFNQAABA"),"System",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQABYRTGEMPORDHZ7ABIFNQAABA"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPIHVQDD6VZBQVMO5CNQ7SP33AU"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHBZLONSMMRDLFPVLUPA36J4GDQ"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6LCP3VS5MFBXFIXFRYSR3WJBGM"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6STBOLMQUTOBDANVABIFNQAABA"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPDVAS7UQUTOBDANVABIFNQAABA"),0,3,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg65LX33RZVXOBDCLUAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgP4BYRTGEMPORDHZ7ABIFNQAABA")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMC3TLCXMVXOBDCLVAALOMT5GDM"))}
	,

	/*Radix::PersoComm::OutMessage:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::PersoComm::OutMessage:General:Attachments-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiXHVKI4HMVXOBDCLVAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5O3H4DJEVLOBDCLSAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6BHCGLBRVXOBDCLUAALOMT5GDM"),
					32,
					null,
					16560,false)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::PersoComm::OutMessage:General - Web Meta*/

/*Radix::PersoComm::OutMessage:General-Editor Presentation*/

package org.radixware.ads.PersoComm.web;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprKTXPYSJZVXOBDCLUAALOMT5GDM"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblGBOAYN4PUTOBDANVABIFNQAABA"),
	null,
	null,

	/*Radix::PersoComm::OutMessage:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::PersoComm::OutMessage:General:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg65LX33RZVXOBDCLUAALOMT5GDM"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVPCBFQWEMPORDHZ7ABIFNQAABA"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YNY224PUTOBDANVABIFNQAABA"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWTKV4MPUTOBDANVABIFNQAABA"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2TKV4MPUTOBDANVABIFNQAABA"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBRWI77EPUTOBDANVABIFNQAABA"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDYU5BZMPUTOBDANVABIFNQAABA"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRIH4IEQUTOBDANVABIFNQAABA"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5CKW454PUTOBDANVABIFNQAABA"),0,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4RHX4A4QUTOBDANVABIFNQAABA"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGQYOL3K2GFBQPKXDOFAII3RJRI"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR255GGIKEFEYVA4P4RR5GYUZNI"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWBAD4MPNUFFYZCHKSKE4LH6LHI"),0,14,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFLYC2OLNIBEQRMU4HSLQGRFMLA"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDKI6KZ4SSBDXVHP3WMWQOM3HUY"),0,13,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSWFU5C55JC6HKXPXQCZIC25LM"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("ppgM2SZIK5CMBG6XGM7LNXEXO32IU"),0,19,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCSFRDRISERF5BCBFNGO7PIVLOI"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSTBPUQBGHRBLBNXEY6BL2ZSKOY"),0,15,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHJPGXALIU5CFFNE3AXBAPQD4XM"),0,16,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBWUMTS2TWBEIPPXRMTVTPH3ZQY"),0,18,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colF4WMZEHSXZDCLLMKZHU73A57PA"),0,17,1,false,false)
			},new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PropertiesGroup[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PropertiesGroup(org.radixware.kernel.common.types.Id.Factory.loadFrom("ppgM2SZIK5CMBG6XGM7LNXEXO32IU"),"Sending",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2MTZWIAGDJF7ZEOORKOW5NUJSQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),false,true,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZKRADR2XH5AWHOSJVUP3XFJRLI"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHIAURDZIHRC7JA5STQBH6PVXUQ"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colS2LOJTRFBJCDFHYYJEGO5QSN24"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DAJREKW3NHERCUYZJEOXLZKXQ"),0,4,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2NMC43PMHRGYBONJZFLS22YQ3E"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4Z7PGG5ZF5AMHG5FHZUWDBDN5I"),0,5,1,false,false)
					})
			}),

			/*Radix::PersoComm::OutMessage:General:Attachements-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMC3TLCXMVXOBDCLVAALOMT5GDM"),"Attachements",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMG3TLCXMVXOBDCLVAALOMT5GDM"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiXHVKI4HMVXOBDCLVAALOMT5GDM")),

			/*Radix::PersoComm::OutMessage:General:System-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgP4BYRTGEMPORDHZ7ABIFNQAABA"),"System",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQABYRTGEMPORDHZ7ABIFNQAABA"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPIHVQDD6VZBQVMO5CNQ7SP33AU"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHBZLONSMMRDLFPVLUPA36J4GDQ"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6LCP3VS5MFBXFIXFRYSR3WJBGM"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6STBOLMQUTOBDANVABIFNQAABA"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPDVAS7UQUTOBDANVABIFNQAABA"),0,3,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg65LX33RZVXOBDCLUAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgP4BYRTGEMPORDHZ7ABIFNQAABA")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMC3TLCXMVXOBDCLVAALOMT5GDM"))}
	,

	/*Radix::PersoComm::OutMessage:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::PersoComm::OutMessage:General:Attachments-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiXHVKI4HMVXOBDCLVAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5O3H4DJEVLOBDCLSAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6BHCGLBRVXOBDCLUAALOMT5GDM"),
					32,
					null,
					16560,false)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::PersoComm::OutMessage:General:Model - Desktop Executable*/

/*Radix::PersoComm::OutMessage:General:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model")
public class General:Model  extends org.radixware.ads.PersoComm.explorer.OutMessage.OutMessage_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::PersoComm::OutMessage:General:Model:Nested classes-Nested Classes*/

	/*Radix::PersoComm::OutMessage:General:Model:Properties-Properties*/

	/*Radix::PersoComm::OutMessage:General:Model:id-Presentation Property*/




	public class Id extends org.radixware.ads.PersoComm.explorer.OutMessage.col3YNY224PUTOBDANVABIFNQAABA{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:id")
		public published  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:id")
		public published   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId(){return (Id)getProperty(col3YNY224PUTOBDANVABIFNQAABA);}

	/*Radix::PersoComm::OutMessage:General:Model:channelKind-Presentation Property*/




	public class ChannelKind extends org.radixware.ads.PersoComm.explorer.OutMessage.col4RHX4A4QUTOBDANVABIFNQAABA{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:channelKind")
		public published  org.radixware.ads.PersoComm.common.ChannelKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:channelKind")
		public published   void setValue(org.radixware.ads.PersoComm.common.ChannelKind val) {
			Value = val;
		}
	}
	public ChannelKind getChannelKind(){return (ChannelKind)getProperty(col4RHX4A4QUTOBDANVABIFNQAABA);}

	/*Radix::PersoComm::OutMessage:General:Model:subject-Presentation Property*/




	public class Subject extends org.radixware.ads.PersoComm.explorer.OutMessage.col5CKW454PUTOBDANVABIFNQAABA{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:subject")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:subject")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public Subject getSubject(){return (Subject)getProperty(col5CKW454PUTOBDANVABIFNQAABA);}

	/*Radix::PersoComm::OutMessage:General:Model:sourceMsgId-Presentation Property*/




	public class SourceMsgId extends org.radixware.ads.PersoComm.explorer.OutMessage.col6LCP3VS5MFBXFIXFRYSR3WJBGM{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:sourceMsgId")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:sourceMsgId")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public SourceMsgId getSourceMsgId(){return (SourceMsgId)getProperty(col6LCP3VS5MFBXFIXFRYSR3WJBGM);}

	/*Radix::PersoComm::OutMessage:General:Model:callbackMethodName-Presentation Property*/




	public class CallbackMethodName extends org.radixware.ads.PersoComm.explorer.OutMessage.col6R756U4QUTOBDANVABIFNQAABA{
		public CallbackMethodName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:callbackMethodName")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:callbackMethodName")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public CallbackMethodName getCallbackMethodName(){return (CallbackMethodName)getProperty(col6R756U4QUTOBDANVABIFNQAABA);}

	/*Radix::PersoComm::OutMessage:General:Model:sourceEntityGuid-Presentation Property*/




	public class SourceEntityGuid extends org.radixware.ads.PersoComm.explorer.OutMessage.col6STBOLMQUTOBDANVABIFNQAABA{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:sourceEntityGuid")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:sourceEntityGuid")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public SourceEntityGuid getSourceEntityGuid(){return (SourceEntityGuid)getProperty(col6STBOLMQUTOBDANVABIFNQAABA);}

	/*Radix::PersoComm::OutMessage:General:Model:expireTime-Presentation Property*/




	public class ExpireTime extends org.radixware.ads.PersoComm.explorer.OutMessage.colBRWI77EPUTOBDANVABIFNQAABA{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:expireTime")
		public published  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:expireTime")
		public published   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public ExpireTime getExpireTime(){return (ExpireTime)getProperty(colBRWI77EPUTOBDANVABIFNQAABA);}

	/*Radix::PersoComm::OutMessage:General:Model:body-Presentation Property*/




	public class Body extends org.radixware.ads.PersoComm.explorer.OutMessage.colDUU5BZMPUTOBDANVABIFNQAABA{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:body")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:body")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public Body getBody(){return (Body)getProperty(colDUU5BZMPUTOBDANVABIFNQAABA);}

	/*Radix::PersoComm::OutMessage:General:Model:importance-Presentation Property*/




	public class Importance extends org.radixware.ads.PersoComm.explorer.OutMessage.colDYU5BZMPUTOBDANVABIFNQAABA{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:importance")
		public published  org.radixware.kernel.common.enums.EPersoCommImportance getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:importance")
		public published   void setValue(org.radixware.kernel.common.enums.EPersoCommImportance val) {
			Value = val;
		}
	}
	public Importance getImportance(){return (Importance)getProperty(colDYU5BZMPUTOBDANVABIFNQAABA);}

	/*Radix::PersoComm::OutMessage:General:Model:destPid-Presentation Property*/




	public class DestPid extends org.radixware.ads.PersoComm.explorer.OutMessage.colHBZLONSMMRDLFPVLUPA36J4GDQ{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:destPid")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:destPid")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public DestPid getDestPid(){return (DestPid)getProperty(colHBZLONSMMRDLFPVLUPA36J4GDQ);}

	/*Radix::PersoComm::OutMessage:General:Model:callbackClassName-Presentation Property*/




	public class CallbackClassName extends org.radixware.ads.PersoComm.explorer.OutMessage.colLRRSUR4QUTOBDANVABIFNQAABA{
		public CallbackClassName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:callbackClassName")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:callbackClassName")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public CallbackClassName getCallbackClassName(){return (CallbackClassName)getProperty(colLRRSUR4QUTOBDANVABIFNQAABA);}

	/*Radix::PersoComm::OutMessage:General:Model:dueTime-Presentation Property*/




	public class DueTime extends org.radixware.ads.PersoComm.explorer.OutMessage.colM2TKV4MPUTOBDANVABIFNQAABA{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:dueTime")
		public published  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:dueTime")
		public published   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public DueTime getDueTime(){return (DueTime)getProperty(colM2TKV4MPUTOBDANVABIFNQAABA);}

	/*Radix::PersoComm::OutMessage:General:Model:createTime-Presentation Property*/




	public class CreateTime extends org.radixware.ads.PersoComm.explorer.OutMessage.colMWTKV4MPUTOBDANVABIFNQAABA{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:createTime")
		public published  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:createTime")
		public published   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public CreateTime getCreateTime(){return (CreateTime)getProperty(colMWTKV4MPUTOBDANVABIFNQAABA);}

	/*Radix::PersoComm::OutMessage:General:Model:sourcePid-Presentation Property*/




	public class SourcePid extends org.radixware.ads.PersoComm.explorer.OutMessage.colPDVAS7UQUTOBDANVABIFNQAABA{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:sourcePid")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:sourcePid")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public SourcePid getSourcePid(){return (SourcePid)getProperty(colPDVAS7UQUTOBDANVABIFNQAABA);}

	/*Radix::PersoComm::OutMessage:General:Model:destEntityGuid-Presentation Property*/




	public class DestEntityGuid extends org.radixware.ads.PersoComm.explorer.OutMessage.colPIHVQDD6VZBQVMO5CNQ7SP33AU{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:destEntityGuid")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:destEntityGuid")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public DestEntityGuid getDestEntityGuid(){return (DestEntityGuid)getProperty(colPIHVQDD6VZBQVMO5CNQ7SP33AU);}

	/*Radix::PersoComm::OutMessage:General:Model:address-Presentation Property*/




	public class Address extends org.radixware.ads.PersoComm.explorer.OutMessage.colPRIH4IEQUTOBDANVABIFNQAABA{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:address")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:address")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public Address getAddress(){return (Address)getProperty(colPRIH4IEQUTOBDANVABIFNQAABA);}

	/*Radix::PersoComm::OutMessage:General:Model:sourceTitle-Presentation Property*/




	public class SourceTitle extends org.radixware.ads.PersoComm.explorer.OutMessage.prdGQYOL3K2GFBQPKXDOFAII3RJRI{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:sourceTitle")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:sourceTitle")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public SourceTitle getSourceTitle(){return (SourceTitle)getProperty(prdGQYOL3K2GFBQPKXDOFAII3RJRI);}

	/*Radix::PersoComm::OutMessage:General:Model:failedIsUnrecoverable-Presentation Property*/




	public class FailedIsUnrecoverable extends org.radixware.ads.PersoComm.explorer.OutMessage.colS2LOJTRFBJCDFHYYJEGO5QSN24{
		public FailedIsUnrecoverable(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:failedIsUnrecoverable")
		public published  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:failedIsUnrecoverable")
		public published   void setValue(Bool val) {
			Value = val;
		}
	}
	public FailedIsUnrecoverable getFailedIsUnrecoverable(){return (FailedIsUnrecoverable)getProperty(colS2LOJTRFBJCDFHYYJEGO5QSN24);}

	/*Radix::PersoComm::OutMessage:General:Model:prevStageMessageId-Presentation Property*/




	public class PrevStageMessageId extends org.radixware.ads.PersoComm.explorer.OutMessage.colBWUMTS2TWBEIPPXRMTVTPH3ZQY{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:prevStageMessageId")
		public published  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:prevStageMessageId")
		public published   void setValue(Int val) {
			Value = val;
		}
	}
	public PrevStageMessageId getPrevStageMessageId(){return (PrevStageMessageId)getProperty(colBWUMTS2TWBEIPPXRMTVTPH3ZQY);}












































	/*Radix::PersoComm::OutMessage:General:Model:Methods-Methods*/

	/*Radix::PersoComm::OutMessage:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		if(!isInSelectorRowContext()){
		    final boolean isSms = channelKind.Value == ChannelKind:Sms;
		    encoding.setVisible(isSms);
		    isUssd.setVisible(isSms);
		    ussdServiceOp.setVisible(isSms);
		    getCommand(idof[OutMessage:ViewSource]).setVisible(Explorer.Utils::DialogUtils.canRunEditor(sourceEntityGuid.Value, sourcePid.Value));
		    getCommand(idof[OutMessage:ViewPrevStageMessage]).setVisible(prevStageMessageId.Value != null);
		}
	}

	/*Radix::PersoComm::OutMessage:General:Model:ViewSource-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:ViewSource")
	public published  void ViewSource (org.radixware.ads.PersoComm.explorer.OutMessage.ViewSource command, org.radixware.kernel.common.types.Id propertyId) {
		Explorer.Utils::DialogUtils.runEditor(Environment,sourceEntityGuid.Value, sourcePid.Value);
	}

	/*Radix::PersoComm::OutMessage:General:Model:ViewPrevStageMessage-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:ViewPrevStageMessage")
	public published  void ViewPrevStageMessage (org.radixware.ads.PersoComm.explorer.OutMessage.ViewPrevStageMessage command, org.radixware.kernel.common.types.Id propertyId) {
		if (prevStageNotSentMessageId.Value != null) {
		    Explorer.Utils::DialogUtils.runEditor(Environment, idof[OutMessage].toString(), prevStageNotSentMessageId.Value.toString());
		} else if (prevStageSentMessageId.Value != null) {
		    Explorer.Utils::DialogUtils.runEditor(Environment, idof[SentMessage].toString(), prevStageSentMessageId.Value.toString());
		}
	}
	public final class ViewPrevStageMessage extends org.radixware.ads.PersoComm.explorer.OutMessage.ViewPrevStageMessage{
		protected ViewPrevStageMessage(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ViewPrevStageMessage( this, propertyId );
		}

	}

	public final class ViewSource extends org.radixware.ads.PersoComm.explorer.OutMessage.ViewSource{
		protected ViewSource(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ViewSource( this, propertyId );
		}

	}















}

/* Radix::PersoComm::OutMessage:General:Model - Desktop Meta*/

/*Radix::PersoComm::OutMessage:General:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemKTXPYSJZVXOBDCLUAALOMT5GDM"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::PersoComm::OutMessage:General:Model:Properties-Properties*/
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

/* Radix::PersoComm::OutMessage:General:Model - Web Executable*/

/*Radix::PersoComm::OutMessage:General:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model")
public class General:Model  extends org.radixware.ads.PersoComm.web.OutMessage.OutMessage_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::PersoComm::OutMessage:General:Model:Nested classes-Nested Classes*/

	/*Radix::PersoComm::OutMessage:General:Model:Properties-Properties*/

	/*Radix::PersoComm::OutMessage:General:Model:channelKind-Presentation Property*/




	public class ChannelKind extends org.radixware.ads.PersoComm.web.OutMessage.col4RHX4A4QUTOBDANVABIFNQAABA{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:channelKind")
		public published  org.radixware.ads.PersoComm.common.ChannelKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:channelKind")
		public published   void setValue(org.radixware.ads.PersoComm.common.ChannelKind val) {
			Value = val;
		}
	}
	public ChannelKind getChannelKind(){return (ChannelKind)getProperty(col4RHX4A4QUTOBDANVABIFNQAABA);}

	/*Radix::PersoComm::OutMessage:General:Model:sourceEntityGuid-Presentation Property*/




	public class SourceEntityGuid extends org.radixware.ads.PersoComm.web.OutMessage.col6STBOLMQUTOBDANVABIFNQAABA{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:sourceEntityGuid")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:sourceEntityGuid")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public SourceEntityGuid getSourceEntityGuid(){return (SourceEntityGuid)getProperty(col6STBOLMQUTOBDANVABIFNQAABA);}

	/*Radix::PersoComm::OutMessage:General:Model:sourcePid-Presentation Property*/




	public class SourcePid extends org.radixware.ads.PersoComm.web.OutMessage.colPDVAS7UQUTOBDANVABIFNQAABA{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:sourcePid")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:sourcePid")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public SourcePid getSourcePid(){return (SourcePid)getProperty(colPDVAS7UQUTOBDANVABIFNQAABA);}

	/*Radix::PersoComm::OutMessage:General:Model:failedIsUnrecoverable-Presentation Property*/




	public class FailedIsUnrecoverable extends org.radixware.ads.PersoComm.web.OutMessage.colS2LOJTRFBJCDFHYYJEGO5QSN24{
		public FailedIsUnrecoverable(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:failedIsUnrecoverable")
		public published  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:failedIsUnrecoverable")
		public published   void setValue(Bool val) {
			Value = val;
		}
	}
	public FailedIsUnrecoverable getFailedIsUnrecoverable(){return (FailedIsUnrecoverable)getProperty(colS2LOJTRFBJCDFHYYJEGO5QSN24);}

	/*Radix::PersoComm::OutMessage:General:Model:prevStageMessageId-Presentation Property*/




	public class PrevStageMessageId extends org.radixware.ads.PersoComm.web.OutMessage.colBWUMTS2TWBEIPPXRMTVTPH3ZQY{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:prevStageMessageId")
		public published  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:prevStageMessageId")
		public published   void setValue(Int val) {
			Value = val;
		}
	}
	public PrevStageMessageId getPrevStageMessageId(){return (PrevStageMessageId)getProperty(colBWUMTS2TWBEIPPXRMTVTPH3ZQY);}
















	/*Radix::PersoComm::OutMessage:General:Model:Methods-Methods*/

	/*Radix::PersoComm::OutMessage:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		if(!isInSelectorRowContext()){
		    final boolean isSms = channelKind.Value == ChannelKind:Sms;
		    encoding.setVisible(isSms);
		    isUssd.setVisible(isSms);
		    ussdServiceOp.setVisible(isSms);
		    getCommand(idof[OutMessage:ViewSource]).setVisible(Explorer.Utils::DialogUtils.canRunEditor(sourceEntityGuid.Value, sourcePid.Value));
		    getCommand(idof[OutMessage:ViewPrevStageMessage]).setVisible(prevStageMessageId.Value != null);
		}
	}

	/*Radix::PersoComm::OutMessage:General:Model:ViewSource-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:ViewSource")
	public published  void ViewSource (org.radixware.ads.PersoComm.web.OutMessage.ViewSource command, org.radixware.kernel.common.types.Id propertyId) {
		Explorer.Utils::DialogUtils.runEditor(Environment,sourceEntityGuid.Value, sourcePid.Value);
	}

	/*Radix::PersoComm::OutMessage:General:Model:ViewPrevStageMessage-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:ViewPrevStageMessage")
	public published  void ViewPrevStageMessage (org.radixware.ads.PersoComm.web.OutMessage.ViewPrevStageMessage command, org.radixware.kernel.common.types.Id propertyId) {
		if (prevStageNotSentMessageId.Value != null) {
		    Explorer.Utils::DialogUtils.runEditor(Environment, idof[OutMessage].toString(), prevStageNotSentMessageId.Value.toString());
		} else if (prevStageSentMessageId.Value != null) {
		    Explorer.Utils::DialogUtils.runEditor(Environment, idof[SentMessage].toString(), prevStageSentMessageId.Value.toString());
		}
	}
	public final class ViewPrevStageMessage extends org.radixware.ads.PersoComm.web.OutMessage.ViewPrevStageMessage{
		protected ViewPrevStageMessage(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ViewPrevStageMessage( this, propertyId );
		}

	}

	public final class ViewSource extends org.radixware.ads.PersoComm.web.OutMessage.ViewSource{
		protected ViewSource(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ViewSource( this, propertyId );
		}

	}















}

/* Radix::PersoComm::OutMessage:General:Model - Web Meta*/

/*Radix::PersoComm::OutMessage:General:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemKTXPYSJZVXOBDCLUAALOMT5GDM"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::PersoComm::OutMessage:General:Model:Properties-Properties*/
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

/* Radix::PersoComm::OutMessage:Create - Desktop Meta*/

/*Radix::PersoComm::OutMessage:Create-Editor Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOD336QIYRRBY5IH274Z7U24EIM"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblGBOAYN4PUTOBDANVABIFNQAABA"),
			null,
			null,

			/*Radix::PersoComm::OutMessage:Create:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::PersoComm::OutMessage:Create:Main-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgRLJUEH54YNEDPOP6IUJR2NRQQU"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEO27YJW7VVCHHIJEF7SWFDZLNY"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YNY224PUTOBDANVABIFNQAABA"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWTKV4MPUTOBDANVABIFNQAABA"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRIH4IEQUTOBDANVABIFNQAABA"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5CKW454PUTOBDANVABIFNQAABA"),0,3,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgRLJUEH54YNEDPOP6IUJR2NRQQU"))}
			,

			/*Radix::PersoComm::OutMessage:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			2192,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.explorer.OutMessage.OutMessage_DefaultModel(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::PersoComm::OutMessage:Create - Web Meta*/

/*Radix::PersoComm::OutMessage:Create-Editor Presentation*/

package org.radixware.ads.PersoComm.web;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOD336QIYRRBY5IH274Z7U24EIM"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblGBOAYN4PUTOBDANVABIFNQAABA"),
			null,
			null,

			/*Radix::PersoComm::OutMessage:Create:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::PersoComm::OutMessage:Create:Main-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgRLJUEH54YNEDPOP6IUJR2NRQQU"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEO27YJW7VVCHHIJEF7SWFDZLNY"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YNY224PUTOBDANVABIFNQAABA"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWTKV4MPUTOBDANVABIFNQAABA"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRIH4IEQUTOBDANVABIFNQAABA"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5CKW454PUTOBDANVABIFNQAABA"),0,3,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgRLJUEH54YNEDPOP6IUJR2NRQQU"))}
			,

			/*Radix::PersoComm::OutMessage:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			2192,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.web.OutMessage.OutMessage_DefaultModel(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::PersoComm::OutMessage:Failed - Desktop Meta*/

/*Radix::PersoComm::OutMessage:Failed-Editor Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class Failed_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOGAUTNZ7AFBDTHQYJNYCVQTJGM"),
	"Failed",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblGBOAYN4PUTOBDANVABIFNQAABA"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4J2ICU53LBA25B4LNAFHWQRH4Q"),
	null,

	/*Radix::PersoComm::OutMessage:Failed:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::PersoComm::OutMessage:Failed:FailedDescription-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg57PQD2UHRFH4RFIM26GXI3KZCA"),"FailedDescription",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM6MNKPVJZ5DY7JJAXFFIFFXM3I"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DAJREKW3NHERCUYZJEOXLZKXQ"),0,1,3,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHIAURDZIHRC7JA5STQBH6PVXUQ"),2,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colS2LOJTRFBJCDFHYYJEGO5QSN24"),2,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2NMC43PMHRGYBONJZFLS22YQ3E"),0,3,3,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFLYC2OLNIBEQRMU4HSLQGRFMLA"),1,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRIH4IEQUTOBDANVABIFNQAABA"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4RHX4A4QUTOBDANVABIFNQAABA"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR255GGIKEFEYVA4P4RR5GYUZNI"),1,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5CKW454PUTOBDANVABIFNQAABA"),0,7,3,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDKI6KZ4SSBDXVHP3WMWQOM3HUY"),0,8,3,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2TKV4MPUTOBDANVABIFNQAABA"),0,2,3,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YNY224PUTOBDANVABIFNQAABA"),0,0,3,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSWFU5C55JC6HKXPXQCZIC25LM"),0,6,3,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg57PQD2UHRFH4RFIM26GXI3KZCA"))}
	,

	/*Radix::PersoComm::OutMessage:Failed:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2064,0,0,null);
}
/* Radix::PersoComm::OutMessage:Failed - Web Meta*/

/*Radix::PersoComm::OutMessage:Failed-Editor Presentation*/

package org.radixware.ads.PersoComm.web;
public final class Failed_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOGAUTNZ7AFBDTHQYJNYCVQTJGM"),
	"Failed",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblGBOAYN4PUTOBDANVABIFNQAABA"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4J2ICU53LBA25B4LNAFHWQRH4Q"),
	null,

	/*Radix::PersoComm::OutMessage:Failed:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::PersoComm::OutMessage:Failed:FailedDescription-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg57PQD2UHRFH4RFIM26GXI3KZCA"),"FailedDescription",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM6MNKPVJZ5DY7JJAXFFIFFXM3I"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DAJREKW3NHERCUYZJEOXLZKXQ"),0,1,3,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHIAURDZIHRC7JA5STQBH6PVXUQ"),2,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colS2LOJTRFBJCDFHYYJEGO5QSN24"),2,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2NMC43PMHRGYBONJZFLS22YQ3E"),0,3,3,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFLYC2OLNIBEQRMU4HSLQGRFMLA"),1,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRIH4IEQUTOBDANVABIFNQAABA"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4RHX4A4QUTOBDANVABIFNQAABA"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR255GGIKEFEYVA4P4RR5GYUZNI"),1,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5CKW454PUTOBDANVABIFNQAABA"),0,7,3,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDKI6KZ4SSBDXVHP3WMWQOM3HUY"),0,8,3,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2TKV4MPUTOBDANVABIFNQAABA"),0,2,3,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YNY224PUTOBDANVABIFNQAABA"),0,0,3,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSWFU5C55JC6HKXPXQCZIC25LM"),0,6,3,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg57PQD2UHRFH4RFIM26GXI3KZCA"))}
	,

	/*Radix::PersoComm::OutMessage:Failed:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2064,0,0,null);
}
/* Radix::PersoComm::OutMessage:Failed:Model - Desktop Executable*/

/*Radix::PersoComm::OutMessage:Failed:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:Failed:Model")
public class Failed:Model  extends org.radixware.ads.PersoComm.explorer.OutMessage.OutMessage_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Failed:Model_mi.rdxMeta; }



	public Failed:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::PersoComm::OutMessage:Failed:Model:Nested classes-Nested Classes*/

	/*Radix::PersoComm::OutMessage:Failed:Model:Properties-Properties*/

	/*Radix::PersoComm::OutMessage:Failed:Model:Methods-Methods*/


}

/* Radix::PersoComm::OutMessage:Failed:Model - Desktop Meta*/

/*Radix::PersoComm::OutMessage:Failed:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Failed:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemOGAUTNZ7AFBDTHQYJNYCVQTJGM"),
						"Failed:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::PersoComm::OutMessage:Failed:Model:Properties-Properties*/
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

/* Radix::PersoComm::OutMessage:Failed:Model - Web Executable*/

/*Radix::PersoComm::OutMessage:Failed:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:Failed:Model")
public class Failed:Model  extends org.radixware.ads.PersoComm.web.OutMessage.OutMessage_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Failed:Model_mi.rdxMeta; }



	public Failed:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::PersoComm::OutMessage:Failed:Model:Nested classes-Nested Classes*/

	/*Radix::PersoComm::OutMessage:Failed:Model:Properties-Properties*/

	/*Radix::PersoComm::OutMessage:Failed:Model:Methods-Methods*/


}

/* Radix::PersoComm::OutMessage:Failed:Model - Web Meta*/

/*Radix::PersoComm::OutMessage:Failed:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Failed:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemOGAUTNZ7AFBDTHQYJNYCVQTJGM"),
						"Failed:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::PersoComm::OutMessage:Failed:Model:Properties-Properties*/
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

/* Radix::PersoComm::OutMessage:General - Desktop Meta*/

/*Radix::PersoComm::OutMessage:General-Selector Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOARLWAJ2VXOBDCLUAALOMT5GDM"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblGBOAYN4PUTOBDANVABIFNQAABA"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtDA5NSIAZ55HEJJXX2OU7F6JAGQ"),
		null,
		false,
		true,
		null,
		28673,
		null,
		2192,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOD336QIYRRBY5IH274Z7U24EIM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprKTXPYSJZVXOBDCLUAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YNY224PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWTKV4MPUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCSFRDRISERF5BCBFNGO7PIVLOI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2TKV4MPUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRIH4IEQUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5CKW454PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDYU5BZMPUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
}
/* Radix::PersoComm::OutMessage:General - Web Meta*/

/*Radix::PersoComm::OutMessage:General-Selector Presentation*/

package org.radixware.ads.PersoComm.web;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprOARLWAJ2VXOBDCLUAALOMT5GDM"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblGBOAYN4PUTOBDANVABIFNQAABA"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtDA5NSIAZ55HEJJXX2OU7F6JAGQ"),
		null,
		false,
		true,
		null,
		28673,
		null,
		2192,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOD336QIYRRBY5IH274Z7U24EIM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprKTXPYSJZVXOBDCLUAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YNY224PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWTKV4MPUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCSFRDRISERF5BCBFNGO7PIVLOI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2TKV4MPUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRIH4IEQUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5CKW454PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDYU5BZMPUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
}
/* Radix::PersoComm::OutMessage:General:Model - Desktop Executable*/

/*Radix::PersoComm::OutMessage:General:Model-Group Model Class*/

package org.radixware.ads.PersoComm.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model")
public class General:Model  extends org.radixware.ads.PersoComm.explorer.OutMessage.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::PersoComm::OutMessage:General:Model:Nested classes-Nested Classes*/

	/*Radix::PersoComm::OutMessage:General:Model:Properties-Properties*/

	/*Radix::PersoComm::OutMessage:General:Model:Methods-Methods*/

	/*Radix::PersoComm::OutMessage:General:Model:isCommandAccessible-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:isCommandAccessible")
	protected published  boolean isCommandAccessible (org.radixware.kernel.common.client.meta.RadCommandDef commandDef) {
		if (commandDef != null && commandDef.Id.equals(idof[OutMessageGroup:BulkMessageChanges])) {
		    return false;
		}
		return super.isCommandAccessible(commandDef);
	}


}

/* Radix::PersoComm::OutMessage:General:Model - Desktop Meta*/

/*Radix::PersoComm::OutMessage:General:Model-Group Model Class*/

package org.radixware.ads.PersoComm.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmOARLWAJ2VXOBDCLUAALOMT5GDM"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::PersoComm::OutMessage:General:Model:Properties-Properties*/
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

/* Radix::PersoComm::OutMessage:General:Model - Web Executable*/

/*Radix::PersoComm::OutMessage:General:Model-Group Model Class*/

package org.radixware.ads.PersoComm.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model")
public class General:Model  extends org.radixware.ads.PersoComm.web.OutMessage.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::PersoComm::OutMessage:General:Model:Nested classes-Nested Classes*/

	/*Radix::PersoComm::OutMessage:General:Model:Properties-Properties*/

	/*Radix::PersoComm::OutMessage:General:Model:Methods-Methods*/

	/*Radix::PersoComm::OutMessage:General:Model:isCommandAccessible-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:General:Model:isCommandAccessible")
	protected published  boolean isCommandAccessible (org.radixware.kernel.common.client.meta.RadCommandDef commandDef) {
		if (commandDef != null && commandDef.Id.equals(idof[OutMessageGroup:BulkMessageChanges])) {
		    return false;
		}
		return super.isCommandAccessible(commandDef);
	}


}

/* Radix::PersoComm::OutMessage:General:Model - Web Meta*/

/*Radix::PersoComm::OutMessage:General:Model-Group Model Class*/

package org.radixware.ads.PersoComm.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmOARLWAJ2VXOBDCLUAALOMT5GDM"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::PersoComm::OutMessage:General:Model:Properties-Properties*/
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

/* Radix::PersoComm::OutMessage:FailedRecoverable - Desktop Meta*/

/*Radix::PersoComm::OutMessage:FailedRecoverable-Selector Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class FailedRecoverable_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new FailedRecoverable_mi();
	private FailedRecoverable_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVOMDNSLYXRBG5H7TIL3FNC3RYU"),
		"FailedRecoverable",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblGBOAYN4PUTOBDANVABIFNQAABA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL5MNHBE5UFGXFHOHRQ4DX5BRLQ"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("imgI4EKIGAQ2ZBRZA6ENTLLFQCT2Q"),
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtDA5NSIAZ55HEJJXX2OU7F6JAGQ"),
		null,
		false,
		true,
		null,
		32,
		null,
		0,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprKTXPYSJZVXOBDCLUAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YNY224PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFLYC2OLNIBEQRMU4HSLQGRFMLA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCO6SP7A6NVGKNANOH2V4DKQR7M")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRIH4IEQUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR255GGIKEFEYVA4P4RR5GYUZNI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DAJREKW3NHERCUYZJEOXLZKXQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVGTW55PE45G3FN27XVDRCUMVME")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHIAURDZIHRC7JA5STQBH6PVXUQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBXROJZGYGNAIRHICUM3OT5TUYE")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colS2LOJTRFBJCDFHYYJEGO5QSN24"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ3LRKU52QFEFJKFPTCDFQJLRYM")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2TKV4MPUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::PersoComm::OutMessage:FailedRecoverable - Web Meta*/

/*Radix::PersoComm::OutMessage:FailedRecoverable-Selector Presentation*/

package org.radixware.ads.PersoComm.web;
public final class FailedRecoverable_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new FailedRecoverable_mi();
	private FailedRecoverable_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVOMDNSLYXRBG5H7TIL3FNC3RYU"),
		"FailedRecoverable",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblGBOAYN4PUTOBDANVABIFNQAABA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL5MNHBE5UFGXFHOHRQ4DX5BRLQ"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("imgI4EKIGAQ2ZBRZA6ENTLLFQCT2Q"),
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtDA5NSIAZ55HEJJXX2OU7F6JAGQ"),
		null,
		false,
		true,
		null,
		32,
		null,
		0,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprKTXPYSJZVXOBDCLUAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YNY224PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFLYC2OLNIBEQRMU4HSLQGRFMLA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCO6SP7A6NVGKNANOH2V4DKQR7M")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRIH4IEQUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR255GGIKEFEYVA4P4RR5GYUZNI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3DAJREKW3NHERCUYZJEOXLZKXQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVGTW55PE45G3FN27XVDRCUMVME")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHIAURDZIHRC7JA5STQBH6PVXUQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBXROJZGYGNAIRHICUM3OT5TUYE")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colS2LOJTRFBJCDFHYYJEGO5QSN24"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ3LRKU52QFEFJKFPTCDFQJLRYM")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2TKV4MPUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::PersoComm::OutMessage:FailedRecoverable:Model - Desktop Executable*/

/*Radix::PersoComm::OutMessage:FailedRecoverable:Model-Group Model Class*/

package org.radixware.ads.PersoComm.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:FailedRecoverable:Model")
public class FailedRecoverable:Model  extends org.radixware.ads.PersoComm.explorer.OutMessage.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return FailedRecoverable:Model_mi.rdxMeta; }



	public FailedRecoverable:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::PersoComm::OutMessage:FailedRecoverable:Model:Nested classes-Nested Classes*/

	/*Radix::PersoComm::OutMessage:FailedRecoverable:Model:Properties-Properties*/

	/*Radix::PersoComm::OutMessage:FailedRecoverable:Model:Methods-Methods*/


}

/* Radix::PersoComm::OutMessage:FailedRecoverable:Model - Desktop Meta*/

/*Radix::PersoComm::OutMessage:FailedRecoverable:Model-Group Model Class*/

package org.radixware.ads.PersoComm.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class FailedRecoverable:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmVOMDNSLYXRBG5H7TIL3FNC3RYU"),
						"FailedRecoverable:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::PersoComm::OutMessage:FailedRecoverable:Model:Properties-Properties*/
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

/* Radix::PersoComm::OutMessage:FailedRecoverable:Model - Web Executable*/

/*Radix::PersoComm::OutMessage:FailedRecoverable:Model-Group Model Class*/

package org.radixware.ads.PersoComm.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::OutMessage:FailedRecoverable:Model")
public class FailedRecoverable:Model  extends org.radixware.ads.PersoComm.web.OutMessage.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return FailedRecoverable:Model_mi.rdxMeta; }



	public FailedRecoverable:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::PersoComm::OutMessage:FailedRecoverable:Model:Nested classes-Nested Classes*/

	/*Radix::PersoComm::OutMessage:FailedRecoverable:Model:Properties-Properties*/

	/*Radix::PersoComm::OutMessage:FailedRecoverable:Model:Methods-Methods*/


}

/* Radix::PersoComm::OutMessage:FailedRecoverable:Model - Web Meta*/

/*Radix::PersoComm::OutMessage:FailedRecoverable:Model-Group Model Class*/

package org.radixware.ads.PersoComm.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class FailedRecoverable:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmVOMDNSLYXRBG5H7TIL3FNC3RYU"),
						"FailedRecoverable:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::PersoComm::OutMessage:FailedRecoverable:Model:Properties-Properties*/
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

/* Radix::PersoComm::OutMessage:FailedUpdatePreview - Desktop Meta*/

/*Radix::PersoComm::OutMessage:FailedUpdatePreview-Selector Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class FailedUpdatePreview_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new FailedUpdatePreview_mi();
	private FailedUpdatePreview_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprAUXDUZIHSRDWRB4VRONHDFKDSM"),
		"FailedUpdatePreview",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVOMDNSLYXRBG5H7TIL3FNC3RYU"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblGBOAYN4PUTOBDANVABIFNQAABA"),
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
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOGAUTNZ7AFBDTHQYJNYCVQTJGM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YNY224PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFLYC2OLNIBEQRMU4HSLQGRFMLA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEDVNT6W6TZAOBCIND4EJOSK2V4")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRIH4IEQUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSUZRJQGMPFDLTGETUOEJQTSR2Y"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQQY3SX4QUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYAU6AYKJXJGKDPNDOQYWOOAQGI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2TKV4MPUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd53CYEZWTRFFXXLAQWAET4S4BYA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.explorer.FailedRecoverable:Model(userSession,this);
	}
}
/* Radix::PersoComm::OutMessage:FailedUpdatePreview - Web Meta*/

/*Radix::PersoComm::OutMessage:FailedUpdatePreview-Selector Presentation*/

package org.radixware.ads.PersoComm.web;
public final class FailedUpdatePreview_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new FailedUpdatePreview_mi();
	private FailedUpdatePreview_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprAUXDUZIHSRDWRB4VRONHDFKDSM"),
		"FailedUpdatePreview",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVOMDNSLYXRBG5H7TIL3FNC3RYU"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGBOAYN4PUTOBDANVABIFNQAABA"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblGBOAYN4PUTOBDANVABIFNQAABA"),
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
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOGAUTNZ7AFBDTHQYJNYCVQTJGM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YNY224PUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFLYC2OLNIBEQRMU4HSLQGRFMLA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEDVNT6W6TZAOBCIND4EJOSK2V4")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRIH4IEQUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSUZRJQGMPFDLTGETUOEJQTSR2Y"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQQY3SX4QUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYAU6AYKJXJGKDPNDOQYWOOAQGI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2TKV4MPUTOBDANVABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd53CYEZWTRFFXXLAQWAET4S4BYA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.web.FailedRecoverable:Model(userSession,this);
	}
}
/* Radix::PersoComm::OutMessage - Localizing Bundle */
package org.radixware.ads.PersoComm.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class OutMessage - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<take from unit settings>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<брать из настроек модуля>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2BVFZGCZQZEVJG5FCKOPGC4F6Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Sending");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отправка");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2MTZWIAGDJF7ZEOORKOW5NUJSQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"New address");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Новый адрес");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2MZHLCKU7FHZBMLAKWM35HJBFQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Encoding");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Кодировка");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2QPJU7HCM5EZRGTPEJZCSYHHAE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class implements the outgoing messages queue. Provides the mechanism to work with these messages.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс, который реализует очередь исходящих сообщений. Обеспечивает механизм для работы с такими сообщениями.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2VGLT4RKCBFZHFAGPTRRAMMQXE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"New address for replacement");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Новый адрес на замену");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3437O5OJAVG6BG3QHDSKW5VZRA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By due time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По времени отправки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4DC2PCMZRBATHLCF7FNM5WEP7Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Undelivered message");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Недоставленное сообщение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4J2ICU53LBA25B4LNAFHWQRH4Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"USSD");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"USSD");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4QQZVC6KXNCZXGXRJWDL4RCIDQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message text");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текст сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5ULOH2HAKBF67PVPAX77U2LGA4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Overrides Radix::Explorer.Models::EntityModel:afterRead. Hides the encoding if the message is not SMS. Hides the button of the message source editor show command if the editor cannot be opened.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перегружает Radix::Explorer.Models::EntityModel:afterRead. Скрывает кодировку, если сообщение не является SMS. Так же скрывает кнопку команды для показа редактора источника сообщения если редактор не может быть открыт.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5WZBIBKMB5GXRNQW4IBCX7BIVU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Failed messages");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Неуспешно переданные сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6HAW4DBDANELFO4AFLJMNSIUGI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"In processing");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"В процессе обработки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6OA2Q6NSBRCO3JSF2KY3IQR7VE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error message");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сообщение о неуспешной передаче");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAITTCPQVMVCLTBMT4LNDQT572U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Outgoing Queue");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Очередь на отправку");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAYH26YZPVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Store attachments in history");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Хранить вложения в истории");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB334URGBVNFB3AGNJJTGJRIZ4E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Try count");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попыток передачи");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBXROJZGYGNAIRHICUM3OT5TUYE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Send stage number");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Номер этапа отправки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC4LJPRTPFJAK5JBISDJDAIIBIQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"History mode");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Режим сохранения в истории");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCHJF42GGMPORDHZ7ABIFNQAABA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message source");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Источник сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCJYZBFWZVVELNOKBIVPSLWCU7E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<never>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<никогда>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCLKNWCNLVRDTXIIVGFFH5Q6IYE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"This message can be recovered");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сообщение не может быть передано путем повторения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCMVGPRJ5LFD7NLYOXDWEQAB2XM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"From");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"От кого");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCO6SP7A6NVGKNANOH2V4DKQR7M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Opens the message source editor.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Команда для показа редактора источника сообщения.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDADLPB72TVFY3DB7TIZPA76EYA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Importance");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Важность");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDGV5QRRUVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Date of last failure");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Последняя дата неуспешной передачи");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDKBFAT7UYZEOTGWZDGAGQRMNYQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last sending failed");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дата последней неуспешной передачи");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDX4DLJCNM5HZJI5JXNVZ4YCUAI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Source message ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. сообщения источника");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDXHHBCWGMPORDHZ7ABIFNQAABA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Channel ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. канала передачи");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE5HOJXYNJVCVXPBCSWYOB7YZLU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"From");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"От кого");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEDVNT6W6TZAOBCIND4EJOSK2V4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By creation time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По времени создания");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEJMVIMVORJBJDLU6JNDHP7JLMQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Главное");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEO27YJW7VVCHHIJEF7SWFDZLNY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"No suitable channel found for message #%s, channel type %s, address \'%s\', routing key \'%s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не найден подходящий канал для сообщения #%s, типа канала %s, адрес \'%s\', ключ маршрутизации \'%s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFLIHCQALMFDQTK7NJCTOW6OJG4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Overrides Radix::Types::Entity:beforeCreate. Sets the creation time and channel for the message.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перегружает Radix::Types::Entity:beforeCreate. Устанавливает время создания и модуль канала для сообщения.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGCQ77RV7YZHLFBGGHOALD23YNU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"View Source Entity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Просмотреть источник сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGJVXEKRQUNCANBK3HNEMS3RY2M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"View previous stage message");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Просмотреть сообщение предыдущего этапа");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHA5PHI34A5CPRCMOHNQI35VUYE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"From");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"От");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHC3X4FMRCNDYXBDIVB5IXOEFKY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unrecoverable error");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка невосстановимая");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIKCBVENZYFAPZFCQ3JELPLNOWU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"New sending due time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Новое плановое время отправки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsILEKIENMIRCLLONO4VTLR2ZFIM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Failure description");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Описание проблемы при передаче");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIWIEFNU3BBCPBDWHEY6LGYWAM4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Undelivered Messages of Outgoing Queue");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Недоставленные сообщения очереди на отправку");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL5MNHBE5UFGXFHOHRQ4DX5BRLQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message text template");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Маска текста сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLXLCAVSKLVBE3MUFPWBGTTID2Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Routing key");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ключ маршрутизации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM37ZJNBXPBALRMMSV4A7GRJMNY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Undelivered Messages");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Недоставленные сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM6MNKPVJZ5DY7JJAXFFIFFXM3I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attachments");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вложения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMG3TLCXMVXOBDCLVAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Expiration time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время истечения срока отправки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMHMIEMZUVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Created");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время создания");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMQ2NF3RTVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Number of sending attempts");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Число попыток передачи");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNLAKKOZ3MZBHBGNPY5ZU7HNZI4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"@Deprecated Use variant with pointer to OutMessage instead\nSearches for the channel units that suit the function parameters. ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"@Deprecated Используйте вариант с указателем на OutMessage\nИщет модули каналов, которые подходят по параметрам функции.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNNOIBS5TJBE55GQWXG7DDNEGWA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last forwarded to another unit");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время последнего перенаправления на другой модуль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNPMSOSC7FNE3HGQ3GV4DSMM2CQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Subject");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тема");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO57MIEBXVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"New channel ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Новый идентификатор канала");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOOEI6HNTPVCFFNXJIUTQELSBUU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name of the message source. Obtained by Radix::PersoComm::OutMessage:sourceEntityGuid and Radix::PersoComm::OutMessage:sourcePid.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название источника сообщения. Получается по Radix::PersoComm::OutMessage:sourceEntityGuid и Radix::PersoComm::OutMessage:sourcePid.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPDG6V5YITJEKRA7IVW2EK3JOUQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"System Attributes");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Системные атрибуты");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQABYRTGEMPORDHZ7ABIFNQAABA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQHUJIPZUVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Initial sending due time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Первоначальное плановое время отправки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQPAHK5XB6ZFE5KRWJRJ33D74LY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Destination PID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"PID получателя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQUJR2WOGMPORDHZ7ABIFNQAABA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Source PID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"PID источника");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRKRWJ66IVNGQFFHJ6J4FZRDYSQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<never>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<никогда>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTM4ECO4U7VFXPCH65ZFXCHQAGM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message is being processed");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сообщение в процессе обработки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTYY7RHDJU5HFHG5TWPERVVQB34"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Number of failed retries");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Число неуспешных попыток передачи");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU6OX4D6Z6JF2HMPAYNEPENWA4U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Source entity ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. сущности источника");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU6UKJ2K27RERJNKLBSHIKTFKQQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Waiting Outgoing Message");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ожидающее отправки сообщение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUBY3ORBPVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Previous stage message id");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. сообщения предыдущего этапа");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUGSOWHJV7RFCDCLR5CFBLEPK5E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message text");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текст сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUVBA45ZPVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Callback class name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Callback class name");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUXVZKGOGMPORDHZ7ABIFNQAABA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Destination entity ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. сущности получателя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVEXQ2UOGMPORDHZ7ABIFNQAABA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Problem");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проблема");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVGTW55PE45G3FN27XVDRCUMVME"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"USSD operation code");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Код USSD-операции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVHSET6D5XBHQNKXULNU65OQQV4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVPCBFQWEMPORDHZ7ABIFNQAABA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Callback method name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Callback method name");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW7RYIIWGMPORDHZ7ABIFNQAABA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"New sending channel ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. нового канала доставки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX6SLCYYSBFE6FGV4TQ33PMGUFI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Address");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Адрес");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXGQVVBELVTOBDCLTAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Channel type not defined");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип канала не задан");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYJX4Y3PR6BALVGXJWSOLRWRVKI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Sending due time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Плановое время отправки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYZNCJ5RTVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unrecoverable");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Невосстановимая");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ3LRKU52QFEFJKFPTCDFQJLRYM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Channel type");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вид канала");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZOC2FYZTVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Waiting Outgoing Message");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Недоставленное сообщение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZUUDIYT6IVEUPLH3BA4I4HBKHI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(OutMessage - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecGBOAYN4PUTOBDANVABIFNQAABA"),"OutMessage - Localizing Bundle",$$$items$$$);
}
