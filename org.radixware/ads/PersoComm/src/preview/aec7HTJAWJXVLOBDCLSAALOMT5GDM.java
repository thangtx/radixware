
/* Radix::PersoComm::EventSubscription - Server Executable*/

/*Radix::PersoComm::EventSubscription-Entity Class*/

package org.radixware.ads.PersoComm.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription")
public final published class EventSubscription  extends org.radixware.ads.Types.server.Entity  implements org.radixware.ads.CfgManagement.server.ICfgReferencedObject,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return EventSubscription_mi.rdxMeta;}

	/*Radix::PersoComm::EventSubscription:Nested classes-Nested Classes*/

	/*Radix::PersoComm::EventSubscription:CfgObjectLookupAdvizor-Server Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:CfgObjectLookupAdvizor")
	private static class CfgObjectLookupAdvizor  implements org.radixware.ads.CfgManagement.server.ICfgObjectLookupAdvizor,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


		/**Metainformation accessor method*/
		@SuppressWarnings("unused")
		public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return EventSubscription_mi.rdxMeta_adcFNRYX7JAMBDAHAHOHOC5R4HMOQ;}

		/*Radix::PersoComm::EventSubscription:CfgObjectLookupAdvizor:Nested classes-Nested Classes*/

		/*Radix::PersoComm::EventSubscription:CfgObjectLookupAdvizor:Properties-Properties*/





























		/*Radix::PersoComm::EventSubscription:CfgObjectLookupAdvizor:Methods-Methods*/

		/*Radix::PersoComm::EventSubscription:CfgObjectLookupAdvizor:getCfgObjectsByExtGuid-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:CfgObjectLookupAdvizor:getCfgObjectsByExtGuid")
		public published  java.util.List<org.radixware.ads.Types.server.Entity> getCfgObjectsByExtGuid (Str extGuid, boolean considerContext, org.radixware.ads.Types.server.Entity context) {
			java.util.List<Types::Entity> objects = new java.util.ArrayList<>();
			if (extGuid != null) {
			    PersoComm.Db::GetEventSubscriptionByExtGuidCursor c = PersoComm.Db::GetEventSubscriptionByExtGuidCursor.open(extGuid);
			    try {
			        while (c.next()) {
			            objects.add(c.subscription);
			        }
			    } finally {
			        c.close();
			    }
			}
			return objects;
		}


	}

	/*Radix::PersoComm::EventSubscription:Properties-Properties*/

	/*Radix::PersoComm::EventSubscription:lowImportanceMaxSev-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:lowImportanceMaxSev")
	public published  org.radixware.kernel.common.enums.EEventSeverity getLowImportanceMaxSev() {
		return lowImportanceMaxSev;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:lowImportanceMaxSev")
	public published   void setLowImportanceMaxSev(org.radixware.kernel.common.enums.EEventSeverity val) {
		lowImportanceMaxSev = val;
	}

	/*Radix::PersoComm::EventSubscription:channelKind-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:channelKind")
	public published  org.radixware.ads.PersoComm.common.ChannelKind getChannelKind() {
		return channelKind;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:channelKind")
	public published   void setChannelKind(org.radixware.ads.PersoComm.common.ChannelKind val) {
		channelKind = val;
	}

	/*Radix::PersoComm::EventSubscription:minEventSeverity-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:minEventSeverity")
	public published  org.radixware.kernel.common.enums.EEventSeverity getMinEventSeverity() {
		return minEventSeverity;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:minEventSeverity")
	public published   void setMinEventSeverity(org.radixware.kernel.common.enums.EEventSeverity val) {
		minEventSeverity = val;
	}

	/*Radix::PersoComm::EventSubscription:normalImportanceMaxSev-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:normalImportanceMaxSev")
	public published  org.radixware.kernel.common.enums.EEventSeverity getNormalImportanceMaxSev() {
		return normalImportanceMaxSev;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:normalImportanceMaxSev")
	public published   void setNormalImportanceMaxSev(org.radixware.kernel.common.enums.EEventSeverity val) {
		normalImportanceMaxSev = val;
	}

	/*Radix::PersoComm::EventSubscription:isActive-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:isActive")
	public published  Bool getIsActive() {
		return isActive;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:isActive")
	public published   void setIsActive(Bool val) {
		isActive = val;
	}

	/*Radix::PersoComm::EventSubscription:userGroup-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:userGroup")
	public published  org.radixware.ads.Acs.server.UserGroup getUserGroup() {
		return userGroup;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:userGroup")
	public published   void setUserGroup(org.radixware.ads.Acs.server.UserGroup val) {
		userGroup = val;
	}

	/*Radix::PersoComm::EventSubscription:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:id")
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:id")
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::PersoComm::EventSubscription:bodyTemplate-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:bodyTemplate")
	public published  Str getBodyTemplate() {
		return bodyTemplate;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:bodyTemplate")
	public published   void setBodyTemplate(Str val) {
		bodyTemplate = val;
	}

	/*Radix::PersoComm::EventSubscription:subjectTemplate-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:subjectTemplate")
	public published  Str getSubjectTemplate() {
		return subjectTemplate;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:subjectTemplate")
	public published   void setSubjectTemplate(Str val) {
		subjectTemplate = val;
	}

	/*Radix::PersoComm::EventSubscription:userGroupName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:userGroupName")
	public published  Str getUserGroupName() {
		return userGroupName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:userGroupName")
	public published   void setUserGroupName(Str val) {
		userGroupName = val;
	}

	/*Radix::PersoComm::EventSubscription:title-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:title")
	public published  Str getTitle() {
		return title;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:title")
	public published   void setTitle(Str val) {
		title = val;
	}

	/*Radix::PersoComm::EventSubscription:eventSource-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:eventSource")
	public published  org.radixware.ads.Arte.common.EventSource getEventSource() {
		return eventSource;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:eventSource")
	public published   void setEventSource(org.radixware.ads.Arte.common.EventSource val) {
		eventSource = val;
	}

	/*Radix::PersoComm::EventSubscription:toStoreInHist-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:toStoreInHist")
	public published  Bool getToStoreInHist() {
		return toStoreInHist;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:toStoreInHist")
	public published   void setToStoreInHist(Bool val) {
		toStoreInHist = val;
	}

	/*Radix::PersoComm::EventSubscription:language-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:language")
	public published  org.radixware.kernel.common.enums.EIsoLanguage getLanguage() {
		return language;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:language")
	public published   void setLanguage(org.radixware.kernel.common.enums.EIsoLanguage val) {
		language = val;
	}

	/*Radix::PersoComm::EventSubscription:beforeSendFunc-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:beforeSendFunc")
	public published  org.radixware.ads.PersoComm.server.UserFunc.BeforeSend getBeforeSendFunc() {
		return beforeSendFunc;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:beforeSendFunc")
	public published   void setBeforeSendFunc(org.radixware.ads.PersoComm.server.UserFunc.BeforeSend val) {
		beforeSendFunc = val;
	}

	/*Radix::PersoComm::EventSubscription:limitMessBodyTemplate-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitMessBodyTemplate")
	public published  Str getLimitMessBodyTemplate() {
		return limitMessBodyTemplate;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitMessBodyTemplate")
	public published   void setLimitMessBodyTemplate(Str val) {
		limitMessBodyTemplate = val;
	}

	/*Radix::PersoComm::EventSubscription:limitMessSubjectTemplate-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitMessSubjectTemplate")
	public published  Str getLimitMessSubjectTemplate() {
		return limitMessSubjectTemplate;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitMessSubjectTemplate")
	public published   void setLimitMessSubjectTemplate(Str val) {
		limitMessSubjectTemplate = val;
	}

	/*Radix::PersoComm::EventSubscription:limitPeriodKind-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitPeriodKind")
	public published  org.radixware.ads.PersoComm.common.LimitPeriodKind getLimitPeriodKind() {
		return limitPeriodKind;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitPeriodKind")
	public published   void setLimitPeriodKind(org.radixware.ads.PersoComm.common.LimitPeriodKind val) {
		limitPeriodKind = val;
	}

	/*Radix::PersoComm::EventSubscription:limitCnt-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitCnt")
	public published  Int getLimitCnt() {
		return limitCnt;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitCnt")
	public published   void setLimitCnt(Int val) {
		limitCnt = val;
	}

	/*Radix::PersoComm::EventSubscription:extGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:extGuid")
	public published  Str getExtGuid() {
		return extGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:extGuid")
	public published   void setExtGuid(Str val) {
		extGuid = val;
	}

	/*Radix::PersoComm::EventSubscription:routingKey-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:routingKey")
	public published  Str getRoutingKey() {
		return routingKey;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:routingKey")
	public published   void setRoutingKey(Str val) {
		routingKey = val;
	}

	/*Radix::PersoComm::EventSubscription:eventContextParams-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:eventContextParams")
	public  org.radixware.kernel.common.types.ArrStr getEventContextParams() {
		return eventContextParams;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:eventContextParams")
	public   void setEventContextParams(org.radixware.kernel.common.types.ArrStr val) {
		eventContextParams = val;
	}

	/*Radix::PersoComm::EventSubscription:eventContextTypeIdPairs-Dynamic Property*/



	protected org.radixware.kernel.common.types.ArrStr eventContextTypeIdPairs=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:eventContextTypeIdPairs")
	public  org.radixware.kernel.common.types.ArrStr getEventContextTypeIdPairs() {

		if (internal[eventContextTypeIdPairs] == null && eventContextParams != null && !eventContextParams.isEmpty()) {
		    final ArrStr list = new ArrStr();
		    for (int i = 1, j = 2; j < eventContextParams.size(); i += 3, j += 3) {
		        list.add(eventContextParams.get(i) + "=" + Utils::Nvl.get(eventContextParams.get(j), ""));
		    }
		    internal[eventContextTypeIdPairs] = list;
		}
		return internal[eventContextTypeIdPairs];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:eventContextTypeIdPairs")
	public   void setEventContextTypeIdPairs(org.radixware.kernel.common.types.ArrStr val) {
		eventContextTypeIdPairs = val;
	}







































































































































































	/*Radix::PersoComm::EventSubscription:Methods-Methods*/

	/*Radix::PersoComm::EventSubscription:afterInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:afterInit")
	protected published  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		if(extGuid == null)
		  extGuid = Arte::Arte.generateGuid();
		else
		  if(src != null && ((EventSubscription)src).extGuid == extGuid)
		    extGuid = Arte::Arte.generateGuid();
	}

	/*Radix::PersoComm::EventSubscription:exportToXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:exportToXml")
	private final  org.radixware.ads.PersoComm.common.ImpExpXsd.EventSubscriptionDocument exportToXml () {
		ImpExpXsd:EventSubscriptionDocument xDoc = ImpExpXsd:EventSubscriptionDocument.Factory.newInstance();
		ImpExpXsd:EventSubscription xSub = xDoc.addNewEventSubscription();
		xSub.Title = title;
		xSub.ExtGuid = extGuid;
		xSub.IsActive = isActive;
		xSub.UserGroupName = userGroupName;
		xSub.ChannelKind = channelKind;
		xSub.EventSource = eventSource;
		xSub.MinEventSeverity = minEventSeverity;
		xSub.LowImportanceMaxSev = lowImportanceMaxSev;
		xSub.NormalImportanceMaxSev = normalImportanceMaxSev;
		xSub.SubjectTemplate = subjectTemplate;
		xSub.BodyTemplate = bodyTemplate;
		xSub.Language = language;
		xSub.ToStoreInHist = toStoreInHist;
		xSub.LimitPeriodKind = limitPeriodKind;
		xSub.LimitCnt = limitCnt;
		xSub.LimitMessSubjectTemplate = limitMessSubjectTemplate;
		xSub.LimitMessBodyTemplate = limitMessBodyTemplate;
		xSub.RoutingKey = routingKey;

		if (beforeSendFunc != null) {
		    xSub.addNewBeforeSendFunc().set(beforeSendFunc.export());
		}

		PersoComm.Db::SubscriptionCodesBySubscriptionIdCursor cursor = PersoComm.Db::SubscriptionCodesBySubscriptionIdCursor.open(id);
		try {
		    while (cursor.next()) {
		        xSub.addNewEventSubscriptionCode().Code = cursor.code;
		    }
		} finally {
		    cursor.close();
		}

		return xDoc;
	}

	/*Radix::PersoComm::EventSubscription:exportThis-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:exportThis")
	  void exportThis (org.radixware.ads.CfgManagement.server.CfgExportData data) {
		data.itemClassId = idof[CfgItem.EventSubscriptionSingle];
		data.object = this;
		data.data = exportToXml();
		data.fileContent = data.data;
	}

	/*Radix::PersoComm::EventSubscription:create-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:create")
	 static  org.radixware.ads.PersoComm.server.EventSubscription create (Str extGuid) {
		if (extGuid == null) {
		    throw new java.lang.NullPointerException("Unable to create EventSubcription, extGuid is null.");
		}
		EventSubscription es = new EventSubscription();
		es.init();
		es.extGuid = extGuid;
		return es;
	}

	/*Radix::PersoComm::EventSubscription:updateFromCfgItem-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:updateFromCfgItem")
	  void updateFromCfgItem (org.radixware.ads.PersoComm.server.CfgItem.EventSubscriptionSingle cfg, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		importThis(cfg.myData.EventSubscription, helper);
	}

	/*Radix::PersoComm::EventSubscription:importThis-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:importThis")
	  boolean importThis (org.radixware.ads.PersoComm.common.ImpExpXsd.EventSubscription xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		if (xml == null) {
		    return false;
		}

		if (xml.UserGroupName != null && Acs::UserGroup.loadByPK(xml.UserGroupName, true) == null) {
		    throw new AppError(Str.format("Error importing the event subscription: group of recipients not found by name %s.", xml.UserGroupName));
		}

		title = xml.Title;
		isActive = xml.IsActive;
		userGroupName = xml.UserGroupName;
		channelKind = xml.ChannelKind;
		eventSource = xml.EventSource;
		minEventSeverity = xml.MinEventSeverity;
		lowImportanceMaxSev = xml.LowImportanceMaxSev;
		normalImportanceMaxSev = xml.NormalImportanceMaxSev;
		subjectTemplate = xml.SubjectTemplate;
		bodyTemplate = xml.BodyTemplate;
		language = xml.Language;
		toStoreInHist = xml.ToStoreInHist;
		limitPeriodKind = xml.LimitPeriodKind;
		limitCnt = xml.LimitCnt;
		limitMessSubjectTemplate = xml.LimitMessSubjectTemplate;
		limitMessBodyTemplate = xml.LimitMessBodyTemplate;
		routingKey = xml.RoutingKey;

		helper.createOrUpdateAndReport(this);

		UserFunc::UserFunc.import(this, idof[EventSubscription:beforeSendFunc], xml.isSetBeforeSendFunc(), xml.BeforeSendFunc, helper);

		PersoComm.Db::DeleteSubscriptionCodes.execute(id);
		for (ImpExpXsd:EventSubscriptionCode xCode : xml.getEventSubscriptionCodeList()) {
		    EventSubscriptionCode.create(id, xCode.Code).create();
		}

		if (isModified()) {
		    update();
		}

		return true;
	}

	/*Radix::PersoComm::EventSubscription:importOne-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:importOne")
	 static  org.radixware.ads.PersoComm.server.EventSubscription importOne (org.radixware.ads.PersoComm.common.ImpExpXsd.EventSubscription xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		if (xml == null) {
		    return null;
		}

		final String extGuid = xml.ExtGuid;
		EventSubscription obj;
		PersoComm.Db::GetEventSubscriptionByExtGuidCursor cursor = PersoComm.Db::GetEventSubscriptionByExtGuidCursor.open(extGuid);
		try {
		    if (!cursor.next()) {
		        obj = create(extGuid);
		        if (!obj.importThis(xml, helper)) {
		            obj.discard();
		            obj = null;
		        }
		    } else {
		        obj = cursor.subscription;
		        switch (helper.getActionIfObjExists(obj)) {
		            case UPDATE:
		                obj.importThis(xml, helper);
		                break;
		            case NEW:
		                obj = create(Arte::Arte.generateGuid());
		                if (!obj.importThis(xml, helper)) {
		                    obj.discard();
		                    obj = null;
		                }
		                break;
		            case CANCELL:
		                helper.reportObjectCancelled(obj);
		                break;
		        }
		    }
		} finally {
		    cursor.close();
		}
		return obj;
	}

	/*Radix::PersoComm::EventSubscription:exportAll-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:exportAll")
	 static  void exportAll (org.radixware.ads.CfgManagement.server.CfgExportData data, java.util.Iterator<org.radixware.ads.PersoComm.server.EventSubscription> iter, org.radixware.ads.CfgManagement.server.CfgItem cfgItem) {
		ImpExpXsd:EventSubscriptionGroupDocument groupDoc = ImpExpXsd:EventSubscriptionGroupDocument.Factory.newInstance();
		if (iter == null) {
		    PersoComm.Db::GetEventSubscriptionByExtGuidCursor cursor = PersoComm.Db::GetEventSubscriptionByExtGuidCursor.open(null);
		    iter = new EntityCursorIterator(cursor, idof[PersoComm.Db::GetEventSubscriptionByExtGuidCursor:subscription]);
		}

		try {
		    while (iter.hasNext()) {
		        EventSubscription evSub = iter.next();
		        ImpExpXsd:EventSubscriptionDocument singleDoc = evSub.exportToXml();
		        data.children.add(new CfgExportData(evSub, null, idof[CfgItem.EventSubscriptionSingle], singleDoc));
		        groupDoc.ensureEventSubscriptionGroup().addNewItem().set(singleDoc.EventSubscription);
		    }
		} finally {
		    EntityCursorIterator.closeIterator(iter);
		}

		data.itemClassId = idof[CfgItem.EventSubscriptionGroup];
		data.object = null;
		data.data = null;
		data.fileContent = groupDoc;
	}

	/*Radix::PersoComm::EventSubscription:importAll-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:importAll")
	 static  void importAll (org.radixware.ads.PersoComm.common.ImpExpXsd.EventSubscriptionGroupDocument xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		if (xml == null || xml.EventSubscriptionGroup == null) {
		    return;
		}
		for (ImpExpXsd:EventSubscription xSub : xml.EventSubscriptionGroup.ItemList) {
		    EventSubscription.importOne(xSub, helper);
		    if (helper.wasCancelled()) {
		        break;
		    }
		}
	}

	/*Radix::PersoComm::EventSubscription:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:onCommand_Import")
	public  org.radixware.schemas.types.StrDocument onCommand_Import (org.radixware.ads.PersoComm.common.ImpExpXsd.EventSubscriptionDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		if (input != null) {
		    CfgImportHelper.Interactive helper = new CfgImportHelper.Interactive(true, false);
		    importThis(input.EventSubscription, helper);
		    return helper.getResultsAsHtmlStr();
		}
		return null;
	}

	/*Radix::PersoComm::EventSubscription:getCfgReferenceExtGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:getCfgReferenceExtGuid")
	public published  Str getCfgReferenceExtGuid () {
		return extGuid;
	}

	/*Radix::PersoComm::EventSubscription:getCfgReferencePropId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:getCfgReferencePropId")
	public published  org.radixware.kernel.common.types.Id getCfgReferencePropId () {
		return idof[EventSubscription:extGuid];
	}

	/*Radix::PersoComm::EventSubscription:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:loadByPK")
	public static published  org.radixware.ads.PersoComm.server.EventSubscription loadByPK (Int id, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(id==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWHRNERXVLOBDCLSAALOMT5GDM"),id);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl7HTJAWJXVLOBDCLSAALOMT5GDM"),pkValsMap);
		try{
		return (
		org.radixware.ads.PersoComm.server.EventSubscription) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::PersoComm::EventSubscription:loadByPidStr-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:loadByPidStr")
	public static published  org.radixware.ads.PersoComm.server.EventSubscription loadByPidStr (Str pidAsStr, boolean checkExistance) {
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl7HTJAWJXVLOBDCLSAALOMT5GDM"),pidAsStr);
		try{
		return (
		org.radixware.ads.PersoComm.server.EventSubscription) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::PersoComm::EventSubscription:loadByExtGuid-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:loadByExtGuid")
	public static published  org.radixware.ads.PersoComm.server.EventSubscription loadByExtGuid (Str guid) {
		if (guid == null)
		    return null;

		PersoComm.Db::GetEventSubscriptionByExtGuidCursor cur = PersoComm.Db::GetEventSubscriptionByExtGuidCursor.open(guid);
		try {
		    if (cur.next())
		        return cur.subscription;
		    return null;
		} finally {
		    cur.close();
		}
	}

	/*Radix::PersoComm::EventSubscription:afterCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:afterCreate")
	protected published  void afterCreate (org.radixware.kernel.server.types.Entity src) {
		super.afterCreate(src);

		if (src != null && src instanceof EventSubscription) {
		    EventSubscription srcSubscribtion = (EventSubscription) src;
		    PersoComm.Db::EventSubscriptionCodesBySubscription cursor = PersoComm.Db::EventSubscriptionCodesBySubscription.open(srcSubscribtion.id);    
		    
		    try {
		        while (cursor.next()) {
		            EventSubscriptionCode subscriptionCodeCopy = EventSubscriptionCode.create(id, cursor.code);
		            subscriptionCodeCopy.create();
		        }
		    } finally {
		        cursor.close();
		    }
		}
	}



	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmdV3POXULHXRFHZC2VFVQZV3FNRM){
			org.radixware.schemas.types.StrDocument result = onCommand_Import((org.radixware.ads.PersoComm.common.ImpExpXsd.EventSubscriptionDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.ads.PersoComm.common.ImpExpXsd.EventSubscriptionDocument.class),newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else 
			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::PersoComm::EventSubscription - Server Meta*/

/*Radix::PersoComm::EventSubscription-Entity Class*/

package org.radixware.ads.PersoComm.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EventSubscription_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),"EventSubscription",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKI2DGBS6VXOBDCLUAALOMT5GDM"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::PersoComm::EventSubscription:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
							/*Owner Class Name*/
							"EventSubscription",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKI2DGBS6VXOBDCLUAALOMT5GDM"),
							/*Property presentations*/

							/*Radix::PersoComm::EventSubscription:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::PersoComm::EventSubscription:lowImportanceMaxSev:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3RTQXCB5VLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::EventSubscription:channelKind:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5OTM5EZZVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::EventSubscription:minEventSeverity:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6MRC5ZR3VLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::EventSubscription:normalImportanceMaxSev:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAQICTNR5VLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::EventSubscription:isActive:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHOISLJZVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::EventSubscription:userGroup:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHBY6K7K4VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(262143,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::PersoComm::EventSubscription:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWHRNERXVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::EventSubscription:bodyTemplate:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIS5OGPB4VLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::EventSubscription:subjectTemplate:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJOXDSNJ4VLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::EventSubscription:title:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVGT4AJJZVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::EventSubscription:eventSource:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZQGUBQRZVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::EventSubscription:toStoreInHist:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVNCG43RIBREHHI5FFCUATH4PTM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::EventSubscription:language:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRJPBERR4VLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::EventSubscription:beforeSendFunc:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZMB564HFKNFC3E5O5XYS3KTLMA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::EventSubscription:limitMessBodyTemplate:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2FJBO5EEGJCW3MOVEG3KLYQCGA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::EventSubscription:limitMessSubjectTemplate:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVH3DNHSDUBD3JPNV535DVDBYBU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::EventSubscription:limitPeriodKind:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4DUQDXN7NRBVVPPE5WTHOZVOSA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::EventSubscription:limitCnt:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQQJTGSWPQRGS5LTJG42YEJUHZA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::EventSubscription:routingKey:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAHRGCGAEXRBSFHGEVGOAXJZ4BE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::EventSubscription:eventContextParams:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZCEGA3EGRVG4VFDJ6L3KHCHFWE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::PersoComm::EventSubscription:Export-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd3JO7YREZXRBYBMRVIO5D5LINYA"),"Export",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::PersoComm::EventSubscription:Import-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdV3POXULHXRFHZC2VFVQZV3FNRM"),"Import",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),false,true)
							},
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::PersoComm::EventSubscription:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4VJZE26HV3OBDCLWAALOMT5GDM"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::PersoComm::EventSubscription:General:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::PersoComm::EventSubscription:General:EventSubscriptionCode-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiHEGLI73CVXOBDCLUAALOMT5GDM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecY2BRNTJ5VLOBDCLSAALOMT5GDM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprM3OC37DCVXOBDCLUAALOMT5GDM"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refEH2GXZJ5VLOBDCLSAALOMT5GDM"),
													null,
													null),

												/*Radix::PersoComm::EventSubscription:General:EventLimitAcc-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiDKCAH7XSHRCMJI3735IQJ3FAFU"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOVPOORJSBFAGZERW5IXTRFTWOY"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprGUSFZX5UI5DQ5ANIHF4B3E4SHE"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refVGBPMMU3TZBYZMFDQJDJ4IR3ZQ"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::PersoComm::EventSubscription:Common-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWZH3CR26VXOBDCLUAALOMT5GDM"),"Common",null,2192,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWHRNERXVLOBDCLSAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVGT4AJJZVLOBDCLSAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5OTM5EZZVLOBDCLSAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHOISLJZVLOBDCLSAALOMT5GDM"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(null,true,null,true,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4VJZE26HV3OBDCLWAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4VJZE26HV3OBDCLWAALOMT5GDM")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(32,null,null,null),null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWZH3CR26VXOBDCLUAALOMT5GDM"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::PersoComm::EventSubscription:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWHRNERXVLOBDCLSAALOMT5GDM"),"{0}) ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colVGT4AJJZVLOBDCLSAALOMT5GDM"),"",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl7HTJAWJXVLOBDCLSAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::PersoComm::EventSubscription:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::PersoComm::EventSubscription:lowImportanceMaxSev-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3RTQXCB5VLOBDCLSAALOMT5GDM"),"lowImportanceMaxSev",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHTN44CC4VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::EventSubscription:channelKind-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5OTM5EZZVLOBDCLSAALOMT5GDM"),"channelKind",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZQP4Q5K3VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::EventSubscription:minEventSeverity-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6MRC5ZR3VLOBDCLSAALOMT5GDM"),"minEventSeverity",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQRG5KRC4VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("3")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::EventSubscription:normalImportanceMaxSev-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAQICTNR5VLOBDCLSAALOMT5GDM"),"normalImportanceMaxSev",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHQ57MLS4VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("2")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::EventSubscription:isActive-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHOISLJZVLOBDCLSAALOMT5GDM"),"isActive",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDV7BPNS3VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::EventSubscription:userGroup-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHBY6K7K4VXOBDCLUAALOMT5GDM"),"userGroup",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHFY6K7K4VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refAXWFGZZZVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::EventSubscription:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWHRNERXVLOBDCLSAALOMT5GDM"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDR7BPNS3VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::EventSubscription:bodyTemplate-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIS5OGPB4VLOBDCLSAALOMT5GDM"),"bodyTemplate",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHCMKAUS3VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::EventSubscription:subjectTemplate-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJOXDSNJ4VLOBDCLSAALOMT5GDM"),"subjectTemplate",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLJQBWUS4VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::EventSubscription:userGroupName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLHS5GRJZVLOBDCLSAALOMT5GDM"),"userGroupName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMHBOKWC4VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::EventSubscription:title-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVGT4AJJZVLOBDCLSAALOMT5GDM"),"title",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMDBOKWC4VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::EventSubscription:eventSource-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZQGUBQRZVLOBDCLSAALOMT5GDM"),"eventSource",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTK5CPIC3VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::EventSubscription:toStoreInHist-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVNCG43RIBREHHI5FFCUATH4PTM"),"toStoreInHist",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7WILLH5AXFCAFFZ2RIHAZZC74I"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::EventSubscription:language-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRJPBERR4VLOBDCLSAALOMT5GDM"),"language",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3WVW4M7LAJC75MDEZTOEQIS4I4"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acs2G44GBNBE5D7RO2QHQSVRZNLG4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::EventSubscription:beforeSendFunc-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZMB564HFKNFC3E5O5XYS3KTLMA"),"beforeSendFunc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHOUU5IBUURC3XF2FQ2KABFHFAE"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl7HTJAWJXVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acl2Y7336EO3VA4VNS4W2FSMR5RSI"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::EventSubscription:limitMessBodyTemplate-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2FJBO5EEGJCW3MOVEG3KLYQCGA"),"limitMessBodyTemplate",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHZIRNY3SFBGG3O4EWJ32DTTTZ4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::EventSubscription:limitMessSubjectTemplate-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVH3DNHSDUBD3JPNV535DVDBYBU"),"limitMessSubjectTemplate",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls33K3AFYHLNEFXHCTVJOGEME5YQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::EventSubscription:limitPeriodKind-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4DUQDXN7NRBVVPPE5WTHOZVOSA"),"limitPeriodKind",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKRJYCBYO7NDJHL4HJUFJSH67QA"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsS2G5HB3SZVBMTND5FBKRD7LXGU"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::EventSubscription:limitCnt-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQQJTGSWPQRGS5LTJG42YEJUHZA"),"limitCnt",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD47KRDSIN5FAZL73D3WLT7EIFU"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::EventSubscription:extGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKR5TN765CFF3DBX34OOON7AKBU"),"extGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::EventSubscription:routingKey-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAHRGCGAEXRBSFHGEVGOAXJZ4BE"),"routingKey",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2PYZRERJQ5CA5HWQZH7CZL5YSQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::EventSubscription:eventContextParams-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZCEGA3EGRVG4VFDJ6L3KHCHFWE"),"eventContextParams",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX5TWG52IPREY5L3V2WLCSUCERY"),org.radixware.kernel.common.enums.EValType.ARR_STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl7HTJAWJXVLOBDCLSAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::EventSubscription:eventContextTypeIdPairs-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQAOI2UM2JJFBVOBK6I3NKFZDBE"),"eventContextTypeIdPairs",null,org.radixware.kernel.common.enums.EValType.ARR_STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::PersoComm::EventSubscription:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6FUW7WOZMJHD5IVTJTQQC7MCJU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKVYSOTS5LRDYLEWSMHIP7CWYP4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDR6LDX46TVFKRICNOQUQXBX5DA"),"exportToXml",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPZCIN6THMVBXTI7VPIBCX6GCLA"),"exportThis",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr62AEPRLQWNFBLADC34ZJWYJZCI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGVA7HCKAB5FHBA2V7A32QHJVO4"),"create",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSZDVQO26J5HDLPQ5CI4CFGY3JE"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTTTT6P4VVRAQ7FQ2Y6OY6NMJBI"),"updateFromCfgItem",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("cfg",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3TRG5I74XNGNFHL4PF53ZCGO3U")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNVHYHQ7G6RBXVHUOWF5W537ARA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3JTXE4SNVRFAPHAGKZ3VSPK6KE"),"importThis",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprY3A73HW25ZBHLBBJDE4ZRPOJUU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5QHB3DEJWBBD3KYJOEBH2GEEQI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAPLGO2CEFFBCHIE5VIN2JQONOU"),"importOne",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6IJ24A3DZRDXFIY3HT4IKLYAYI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOLTY3T2BDJEAXNVFAG7FSTS2MI"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthT6GNMCJUMZBLXNQISFZIQAFZCE"),"exportAll",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprO62O7IXKPBEIBPHQWR2UNQ2YUM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("iter",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5FA4ZZUJH5HTBP2RUBYW2TLTQQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("cfgItem",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIAYL7EFKIRATRE5EBAH7ATZ5L4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGU75ET4DWBEPZBHV4SJACJFTQE"),"importAll",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBGU3KJKAFZFP3LGSUMMMBDLFVI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVDNZ25OSCJHPRB53EQJDJQHVAU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdV3POXULHXRFHZC2VFVQZV3FNRM"),"onCommand_Import",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDNXEXXESIZAXDDYOKNN6HG4KFA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEZOJ5J2SWBFK3MZDE2HGRNOSTE"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQXRW7PZ4QBBEJAJL57JFPES7EQ"),"getCfgReferenceExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTIFZEEQC2NEUNPFIRAGRH2B7EY"),"getCfgReferencePropId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVEZZG5DAH5AVRMBHBTKEGNGFTU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXLACIQALSBFBNK3C6TYPJLHRKI"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRBH2RUJTIZGGND4FXNTZ3ZUZQA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr67YKZVM4MBCA3LUS2YF35BFJPE"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHIWEMS33JFEF3DBC6F5P4MDF3I"),"loadByExtGuid",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("guid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRN3YCRDE4JGEZN5UDHSLVWO2O4"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6KS7LCP4W5AW5OCCJHH5KGO4YQ"),"afterCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNMGJQALV2JGILOBDDXQQBZ7ZR4"))
								},null)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta_adcFNRYX7JAMBDAHAHOHOC5R4HMOQ = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcFNRYX7JAMBDAHAHOHOC5R4HMOQ"),"CfgObjectLookupAdvizor",null,

						org.radixware.kernel.common.enums.EClassType.ENTITY,
						null,
						null,
						null,

						/*Radix::PersoComm::EventSubscription:CfgObjectLookupAdvizor:Properties-Properties*/
						null,

						/*Radix::PersoComm::EventSubscription:CfgObjectLookupAdvizor:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJPAOFWUPW5GIJKM3O7SE3KNB7Q"),"getCfgObjectsByExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRFWMR3ZYFZEWXCC4QDOUZSFGSQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("considerContext",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCVN4CLJDGZDTBDZ6IIOPH7DFAU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("context",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprU44FALDN3RGETBXLGXEF36HEAU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						null,
						null,null,null,false);
}

/* Radix::PersoComm::EventSubscription - Desktop Executable*/

/*Radix::PersoComm::EventSubscription-Entity Class*/

package org.radixware.ads.PersoComm.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription")
public interface EventSubscription {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}





		public org.radixware.ads.PersoComm.explorer.EventSubscription.EventSubscription_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.PersoComm.explorer.EventSubscription.EventSubscription_DefaultModel )  super.getEntity(i);}
	}


































































































































	/*Radix::PersoComm::EventSubscription:limitMessBodyTemplate:limitMessBodyTemplate-Presentation Property*/


	public class LimitMessBodyTemplate extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LimitMessBodyTemplate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitMessBodyTemplate:limitMessBodyTemplate")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitMessBodyTemplate:limitMessBodyTemplate")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LimitMessBodyTemplate getLimitMessBodyTemplate();
	/*Radix::PersoComm::EventSubscription:lowImportanceMaxSev:lowImportanceMaxSev-Presentation Property*/


	public class LowImportanceMaxSev extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public LowImportanceMaxSev(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EEventSeverity> getValClass(){
			return org.radixware.kernel.common.enums.EEventSeverity.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:lowImportanceMaxSev:lowImportanceMaxSev")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:lowImportanceMaxSev:lowImportanceMaxSev")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public LowImportanceMaxSev getLowImportanceMaxSev();
	/*Radix::PersoComm::EventSubscription:limitPeriodKind:limitPeriodKind-Presentation Property*/


	public class LimitPeriodKind extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LimitPeriodKind(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.PersoComm.common.LimitPeriodKind dummy = x == null ? null : (x instanceof org.radixware.ads.PersoComm.common.LimitPeriodKind ? (org.radixware.ads.PersoComm.common.LimitPeriodKind)x : org.radixware.ads.PersoComm.common.LimitPeriodKind.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.PersoComm.common.LimitPeriodKind> getValClass(){
			return org.radixware.ads.PersoComm.common.LimitPeriodKind.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.PersoComm.common.LimitPeriodKind dummy = x == null ? null : (x instanceof org.radixware.ads.PersoComm.common.LimitPeriodKind ? (org.radixware.ads.PersoComm.common.LimitPeriodKind)x : org.radixware.ads.PersoComm.common.LimitPeriodKind.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitPeriodKind:limitPeriodKind")
		public  org.radixware.ads.PersoComm.common.LimitPeriodKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitPeriodKind:limitPeriodKind")
		public   void setValue(org.radixware.ads.PersoComm.common.LimitPeriodKind val) {
			Value = val;
		}
	}
	public LimitPeriodKind getLimitPeriodKind();
	/*Radix::PersoComm::EventSubscription:channelKind:channelKind-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:channelKind:channelKind")
		public  org.radixware.ads.PersoComm.common.ChannelKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:channelKind:channelKind")
		public   void setValue(org.radixware.ads.PersoComm.common.ChannelKind val) {
			Value = val;
		}
	}
	public ChannelKind getChannelKind();
	/*Radix::PersoComm::EventSubscription:minEventSeverity:minEventSeverity-Presentation Property*/


	public class MinEventSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MinEventSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EEventSeverity> getValClass(){
			return org.radixware.kernel.common.enums.EEventSeverity.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:minEventSeverity:minEventSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:minEventSeverity:minEventSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MinEventSeverity getMinEventSeverity();
	/*Radix::PersoComm::EventSubscription:routingKey:routingKey-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:routingKey:routingKey")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:routingKey:routingKey")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoutingKey getRoutingKey();
	/*Radix::PersoComm::EventSubscription:normalImportanceMaxSev:normalImportanceMaxSev-Presentation Property*/


	public class NormalImportanceMaxSev extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public NormalImportanceMaxSev(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EEventSeverity> getValClass(){
			return org.radixware.kernel.common.enums.EEventSeverity.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:normalImportanceMaxSev:normalImportanceMaxSev")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:normalImportanceMaxSev:normalImportanceMaxSev")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public NormalImportanceMaxSev getNormalImportanceMaxSev();
	/*Radix::PersoComm::EventSubscription:isActive:isActive-Presentation Property*/


	public class IsActive extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsActive(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:isActive:isActive")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:isActive:isActive")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsActive getIsActive();
	/*Radix::PersoComm::EventSubscription:userGroup:userGroup-Presentation Property*/


	public class UserGroup extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public UserGroup(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.UserGroup.UserGroup_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.explorer.UserGroup.UserGroup_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.UserGroup.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.explorer.UserGroup.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:userGroup:userGroup")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:userGroup:userGroup")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public UserGroup getUserGroup();
	/*Radix::PersoComm::EventSubscription:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::PersoComm::EventSubscription:bodyTemplate:bodyTemplate-Presentation Property*/


	public class BodyTemplate extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public BodyTemplate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:bodyTemplate:bodyTemplate")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:bodyTemplate:bodyTemplate")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public BodyTemplate getBodyTemplate();
	/*Radix::PersoComm::EventSubscription:subjectTemplate:subjectTemplate-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:subjectTemplate:subjectTemplate")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:subjectTemplate:subjectTemplate")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SubjectTemplate getSubjectTemplate();
	/*Radix::PersoComm::EventSubscription:limitCnt:limitCnt-Presentation Property*/


	public class LimitCnt extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public LimitCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitCnt:limitCnt")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitCnt:limitCnt")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public LimitCnt getLimitCnt();
	/*Radix::PersoComm::EventSubscription:language:language-Presentation Property*/


	public class Language extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Language(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EIsoLanguage dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EIsoLanguage ? (org.radixware.kernel.common.enums.EIsoLanguage)x : org.radixware.kernel.common.enums.EIsoLanguage.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EIsoLanguage> getValClass(){
			return org.radixware.kernel.common.enums.EIsoLanguage.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EIsoLanguage dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EIsoLanguage ? (org.radixware.kernel.common.enums.EIsoLanguage)x : org.radixware.kernel.common.enums.EIsoLanguage.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:language:language")
		public  org.radixware.kernel.common.enums.EIsoLanguage getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:language:language")
		public   void setValue(org.radixware.kernel.common.enums.EIsoLanguage val) {
			Value = val;
		}
	}
	public Language getLanguage();
	/*Radix::PersoComm::EventSubscription:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::PersoComm::EventSubscription:limitMessSubjectTemplate:limitMessSubjectTemplate-Presentation Property*/


	public class LimitMessSubjectTemplate extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LimitMessSubjectTemplate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitMessSubjectTemplate:limitMessSubjectTemplate")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitMessSubjectTemplate:limitMessSubjectTemplate")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LimitMessSubjectTemplate getLimitMessSubjectTemplate();
	/*Radix::PersoComm::EventSubscription:toStoreInHist:toStoreInHist-Presentation Property*/


	public class ToStoreInHist extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public ToStoreInHist(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:toStoreInHist:toStoreInHist")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:toStoreInHist:toStoreInHist")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public ToStoreInHist getToStoreInHist();
	/*Radix::PersoComm::EventSubscription:eventSource:eventSource-Presentation Property*/


	public class EventSource extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public EventSource(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Arte.common.EventSource dummy = x == null ? null : (x instanceof org.radixware.ads.Arte.common.EventSource ? (org.radixware.ads.Arte.common.EventSource)x : org.radixware.ads.Arte.common.EventSource.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Arte.common.EventSource> getValClass(){
			return org.radixware.ads.Arte.common.EventSource.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Arte.common.EventSource dummy = x == null ? null : (x instanceof org.radixware.ads.Arte.common.EventSource ? (org.radixware.ads.Arte.common.EventSource)x : org.radixware.ads.Arte.common.EventSource.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:eventSource:eventSource")
		public  org.radixware.ads.Arte.common.EventSource getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:eventSource:eventSource")
		public   void setValue(org.radixware.ads.Arte.common.EventSource val) {
			Value = val;
		}
	}
	public EventSource getEventSource();
	/*Radix::PersoComm::EventSubscription:eventContextParams:eventContextParams-Presentation Property*/


	public class EventContextParams extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public EventContextParams(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.ArrStr dummy = ((org.radixware.kernel.common.types.ArrStr)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:eventContextParams:eventContextParams")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:eventContextParams:eventContextParams")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public EventContextParams getEventContextParams();
	/*Radix::PersoComm::EventSubscription:beforeSendFunc:beforeSendFunc-Presentation Property*/


	public class BeforeSendFunc extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public BeforeSendFunc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:beforeSendFunc:beforeSendFunc")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:beforeSendFunc:beforeSendFunc")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public BeforeSendFunc getBeforeSendFunc();
	public static class Export extends org.radixware.kernel.common.client.models.items.Command{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class Import extends org.radixware.kernel.common.client.models.items.Command{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.types.StrDocument send(org.radixware.ads.PersoComm.common.ImpExpXsd.EventSubscriptionDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.types.StrDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.types.StrDocument.class);
		}

	}



}

/* Radix::PersoComm::EventSubscription - Desktop Meta*/

/*Radix::PersoComm::EventSubscription-Entity Class*/

package org.radixware.ads.PersoComm.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EventSubscription_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::PersoComm::EventSubscription:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
			"Radix::PersoComm::EventSubscription",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("img3HB6PW72A7PPVUD65FYM2NHX5Z6YVQ4S"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWZH3CR26VXOBDCLUAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKI2DGBS6VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls432C2D26VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKI2DGBS6VXOBDCLUAALOMT5GDM"),0,

			/*Radix::PersoComm::EventSubscription:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::PersoComm::EventSubscription:lowImportanceMaxSev:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3RTQXCB5VLOBDCLSAALOMT5GDM"),
						"lowImportanceMaxSev",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHTN44CC4VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
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

						/*Radix::PersoComm::EventSubscription:lowImportanceMaxSev:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:channelKind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5OTM5EZZVLOBDCLSAALOMT5GDM"),
						"channelKind",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZQP4Q5K3VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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

						/*Radix::PersoComm::EventSubscription:channelKind:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.INCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aciLQ3IFOMEAZCUJNQW2WSFKVSL3E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aciL2BEJTYZVPORDJHCAANE2UAFXA")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:minEventSeverity:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6MRC5ZR3VLOBDCLSAALOMT5GDM"),
						"minEventSeverity",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQRG5KRC4VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("3"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::EventSubscription:minEventSeverity:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:normalImportanceMaxSev:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAQICTNR5VLOBDCLSAALOMT5GDM"),
						"normalImportanceMaxSev",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHQ57MLS4VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
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

						/*Radix::PersoComm::EventSubscription:normalImportanceMaxSev:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:isActive:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHOISLJZVLOBDCLSAALOMT5GDM"),
						"isActive",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDV7BPNS3VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
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

						/*Radix::PersoComm::EventSubscription:isActive:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:userGroup:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHBY6K7K4VXOBDCLUAALOMT5GDM"),
						"userGroup",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHFY6K7K4VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						null,
						null,
						null,
						262143,
						262143,false),

					/*Radix::PersoComm::EventSubscription:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWHRNERXVLOBDCLSAALOMT5GDM"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDR7BPNS3VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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

						/*Radix::PersoComm::EventSubscription:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:bodyTemplate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIS5OGPB4VLOBDCLSAALOMT5GDM"),
						"bodyTemplate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHCMKAUS3VXOBDCLUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVWJEDWXSBJHOTKNRIHONW3S3KU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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

						/*Radix::PersoComm::EventSubscription:bodyTemplate:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:subjectTemplate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJOXDSNJ4VLOBDCLSAALOMT5GDM"),
						"subjectTemplate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLJQBWUS4VXOBDCLUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOO5ZPULLI5BTPB7WMNDBVCCGMU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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

						/*Radix::PersoComm::EventSubscription:subjectTemplate:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVGT4AJJZVLOBDCLSAALOMT5GDM"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMDBOKWC4VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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

						/*Radix::PersoComm::EventSubscription:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:eventSource:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZQGUBQRZVLOBDCLSAALOMT5GDM"),
						"eventSource",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTK5CPIC3VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeNFVGJDG7QVAQXGMXNLZTX6CQRY"),
						true,

						/*Radix::PersoComm::EventSubscription:eventSource:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTTCH7Y3EPFE65DH6Z2B5C75ZAU"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:toStoreInHist:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVNCG43RIBREHHI5FFCUATH4PTM"),
						"toStoreInHist",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7WILLH5AXFCAFFZ2RIHAZZC74I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::EventSubscription:toStoreInHist:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:language:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRJPBERR4VLOBDCLSAALOMT5GDM"),
						"language",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3WVW4M7LAJC75MDEZTOEQIS4I4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs2G44GBNBE5D7RO2QHQSVRZNLG4"),
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

						/*Radix::PersoComm::EventSubscription:language:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs2G44GBNBE5D7RO2QHQSVRZNLG4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2ZGSS4HCQ5G7VO76SE6VTDJPYM"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:beforeSendFunc:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZMB564HFKNFC3E5O5XYS3KTLMA"),
						"beforeSendFunc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHOUU5IBUURC3XF2FQ2KABFHFAE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl2Y7336EO3VA4VNS4W2FSMR5RSI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						null,
						0L,
						0L,false,false),

					/*Radix::PersoComm::EventSubscription:limitMessBodyTemplate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2FJBO5EEGJCW3MOVEG3KLYQCGA"),
						"limitMessBodyTemplate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHZIRNY3SFBGG3O4EWJ32DTTTZ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRU3CAYHFPVDDNLVLEJZDC2L7CM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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

						/*Radix::PersoComm::EventSubscription:limitMessBodyTemplate:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:limitMessSubjectTemplate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVH3DNHSDUBD3JPNV535DVDBYBU"),
						"limitMessSubjectTemplate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls33K3AFYHLNEFXHCTVJOGEME5YQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBIO2MTODEJCGDIW63HUWOBODME"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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

						/*Radix::PersoComm::EventSubscription:limitMessSubjectTemplate:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:limitPeriodKind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4DUQDXN7NRBVVPPE5WTHOZVOSA"),
						"limitPeriodKind",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKRJYCBYO7NDJHL4HJUFJSH67QA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsS2G5HB3SZVBMTND5FBKRD7LXGU"),
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

						/*Radix::PersoComm::EventSubscription:limitPeriodKind:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsS2G5HB3SZVBMTND5FBKRD7LXGU"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:limitCnt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQQJTGSWPQRGS5LTJG42YEJUHZA"),
						"limitCnt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD47KRDSIN5FAZL73D3WLT7EIFU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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

						/*Radix::PersoComm::EventSubscription:limitCnt:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:routingKey:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAHRGCGAEXRBSFHGEVGOAXJZ4BE"),
						"routingKey",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2PYZRERJQ5CA5HWQZH7CZL5YSQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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

						/*Radix::PersoComm::EventSubscription:routingKey:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:eventContextParams:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZCEGA3EGRVG4VFDJ6L3KHCHFWE"),
						"eventContextParams",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX5TWG52IPREY5L3V2WLCSUCERY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
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
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeHJDLKPZLHRC2NIKQY3EDV32DYQ"),
						true,

						/*Radix::PersoComm::EventSubscription:eventContextParams:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3O2W2YIF7JB7NH6HJTF5F6WZMU"),
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::PersoComm::EventSubscription:Export-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd3JO7YREZXRBYBMRVIO5D5LINYA"),
						"Export",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXS7QMXI3NJHKDLDE46H6PI6ZJ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgVORNT5ZFORBEHLSYMJNUWJK6II"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("afh267OZOHNQNF2BAFAT4ATME24HQ"),
						org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::PersoComm::EventSubscription:Import-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdV3POXULHXRFHZC2VFVQZV3FNRM"),
						"Import",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEEQKYP33D5H55KQCIJRGKJQUPU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgMUCCZVMA4JC4NMTVVEGYXEXU5Q"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT)
			},
			null,
			null,
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refAXWFGZZZVLOBDCLSAALOMT5GDM"),"EventSubscription=>UserGroup (userGroupName=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl7HTJAWJXVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colLHS5GRJZVLOBDCLSAALOMT5GDM")},new String[]{"userGroupName"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4")},new String[]{"name"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4VJZE26HV3OBDCLWAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWZH3CR26VXOBDCLUAALOMT5GDM")},
			false,true,false);
}

/* Radix::PersoComm::EventSubscription - Web Executable*/

/*Radix::PersoComm::EventSubscription-Entity Class*/

package org.radixware.ads.PersoComm.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription")
public interface EventSubscription {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}





		public org.radixware.ads.PersoComm.web.EventSubscription.EventSubscription_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.PersoComm.web.EventSubscription.EventSubscription_DefaultModel )  super.getEntity(i);}
	}





























































































































	/*Radix::PersoComm::EventSubscription:limitMessBodyTemplate:limitMessBodyTemplate-Presentation Property*/


	public class LimitMessBodyTemplate extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LimitMessBodyTemplate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitMessBodyTemplate:limitMessBodyTemplate")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitMessBodyTemplate:limitMessBodyTemplate")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LimitMessBodyTemplate getLimitMessBodyTemplate();
	/*Radix::PersoComm::EventSubscription:lowImportanceMaxSev:lowImportanceMaxSev-Presentation Property*/


	public class LowImportanceMaxSev extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public LowImportanceMaxSev(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EEventSeverity> getValClass(){
			return org.radixware.kernel.common.enums.EEventSeverity.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:lowImportanceMaxSev:lowImportanceMaxSev")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:lowImportanceMaxSev:lowImportanceMaxSev")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public LowImportanceMaxSev getLowImportanceMaxSev();
	/*Radix::PersoComm::EventSubscription:limitPeriodKind:limitPeriodKind-Presentation Property*/


	public class LimitPeriodKind extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LimitPeriodKind(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.PersoComm.common.LimitPeriodKind dummy = x == null ? null : (x instanceof org.radixware.ads.PersoComm.common.LimitPeriodKind ? (org.radixware.ads.PersoComm.common.LimitPeriodKind)x : org.radixware.ads.PersoComm.common.LimitPeriodKind.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.PersoComm.common.LimitPeriodKind> getValClass(){
			return org.radixware.ads.PersoComm.common.LimitPeriodKind.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.PersoComm.common.LimitPeriodKind dummy = x == null ? null : (x instanceof org.radixware.ads.PersoComm.common.LimitPeriodKind ? (org.radixware.ads.PersoComm.common.LimitPeriodKind)x : org.radixware.ads.PersoComm.common.LimitPeriodKind.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitPeriodKind:limitPeriodKind")
		public  org.radixware.ads.PersoComm.common.LimitPeriodKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitPeriodKind:limitPeriodKind")
		public   void setValue(org.radixware.ads.PersoComm.common.LimitPeriodKind val) {
			Value = val;
		}
	}
	public LimitPeriodKind getLimitPeriodKind();
	/*Radix::PersoComm::EventSubscription:channelKind:channelKind-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:channelKind:channelKind")
		public  org.radixware.ads.PersoComm.common.ChannelKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:channelKind:channelKind")
		public   void setValue(org.radixware.ads.PersoComm.common.ChannelKind val) {
			Value = val;
		}
	}
	public ChannelKind getChannelKind();
	/*Radix::PersoComm::EventSubscription:minEventSeverity:minEventSeverity-Presentation Property*/


	public class MinEventSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MinEventSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EEventSeverity> getValClass(){
			return org.radixware.kernel.common.enums.EEventSeverity.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:minEventSeverity:minEventSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:minEventSeverity:minEventSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MinEventSeverity getMinEventSeverity();
	/*Radix::PersoComm::EventSubscription:routingKey:routingKey-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:routingKey:routingKey")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:routingKey:routingKey")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoutingKey getRoutingKey();
	/*Radix::PersoComm::EventSubscription:normalImportanceMaxSev:normalImportanceMaxSev-Presentation Property*/


	public class NormalImportanceMaxSev extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public NormalImportanceMaxSev(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EEventSeverity> getValClass(){
			return org.radixware.kernel.common.enums.EEventSeverity.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:normalImportanceMaxSev:normalImportanceMaxSev")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:normalImportanceMaxSev:normalImportanceMaxSev")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public NormalImportanceMaxSev getNormalImportanceMaxSev();
	/*Radix::PersoComm::EventSubscription:isActive:isActive-Presentation Property*/


	public class IsActive extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsActive(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:isActive:isActive")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:isActive:isActive")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsActive getIsActive();
	/*Radix::PersoComm::EventSubscription:userGroup:userGroup-Presentation Property*/


	public class UserGroup extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public UserGroup(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.web.UserGroup.UserGroup_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.web.UserGroup.UserGroup_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.web.UserGroup.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.web.UserGroup.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:userGroup:userGroup")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:userGroup:userGroup")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public UserGroup getUserGroup();
	/*Radix::PersoComm::EventSubscription:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::PersoComm::EventSubscription:bodyTemplate:bodyTemplate-Presentation Property*/


	public class BodyTemplate extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public BodyTemplate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:bodyTemplate:bodyTemplate")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:bodyTemplate:bodyTemplate")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public BodyTemplate getBodyTemplate();
	/*Radix::PersoComm::EventSubscription:subjectTemplate:subjectTemplate-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:subjectTemplate:subjectTemplate")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:subjectTemplate:subjectTemplate")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SubjectTemplate getSubjectTemplate();
	/*Radix::PersoComm::EventSubscription:limitCnt:limitCnt-Presentation Property*/


	public class LimitCnt extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public LimitCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitCnt:limitCnt")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitCnt:limitCnt")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public LimitCnt getLimitCnt();
	/*Radix::PersoComm::EventSubscription:language:language-Presentation Property*/


	public class Language extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Language(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EIsoLanguage dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EIsoLanguage ? (org.radixware.kernel.common.enums.EIsoLanguage)x : org.radixware.kernel.common.enums.EIsoLanguage.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EIsoLanguage> getValClass(){
			return org.radixware.kernel.common.enums.EIsoLanguage.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EIsoLanguage dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EIsoLanguage ? (org.radixware.kernel.common.enums.EIsoLanguage)x : org.radixware.kernel.common.enums.EIsoLanguage.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:language:language")
		public  org.radixware.kernel.common.enums.EIsoLanguage getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:language:language")
		public   void setValue(org.radixware.kernel.common.enums.EIsoLanguage val) {
			Value = val;
		}
	}
	public Language getLanguage();
	/*Radix::PersoComm::EventSubscription:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::PersoComm::EventSubscription:limitMessSubjectTemplate:limitMessSubjectTemplate-Presentation Property*/


	public class LimitMessSubjectTemplate extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LimitMessSubjectTemplate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitMessSubjectTemplate:limitMessSubjectTemplate")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:limitMessSubjectTemplate:limitMessSubjectTemplate")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LimitMessSubjectTemplate getLimitMessSubjectTemplate();
	/*Radix::PersoComm::EventSubscription:toStoreInHist:toStoreInHist-Presentation Property*/


	public class ToStoreInHist extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public ToStoreInHist(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:toStoreInHist:toStoreInHist")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:toStoreInHist:toStoreInHist")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public ToStoreInHist getToStoreInHist();
	/*Radix::PersoComm::EventSubscription:eventSource:eventSource-Presentation Property*/


	public class EventSource extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public EventSource(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Arte.common.EventSource dummy = x == null ? null : (x instanceof org.radixware.ads.Arte.common.EventSource ? (org.radixware.ads.Arte.common.EventSource)x : org.radixware.ads.Arte.common.EventSource.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Arte.common.EventSource> getValClass(){
			return org.radixware.ads.Arte.common.EventSource.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Arte.common.EventSource dummy = x == null ? null : (x instanceof org.radixware.ads.Arte.common.EventSource ? (org.radixware.ads.Arte.common.EventSource)x : org.radixware.ads.Arte.common.EventSource.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:eventSource:eventSource")
		public  org.radixware.ads.Arte.common.EventSource getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:eventSource:eventSource")
		public   void setValue(org.radixware.ads.Arte.common.EventSource val) {
			Value = val;
		}
	}
	public EventSource getEventSource();
	/*Radix::PersoComm::EventSubscription:eventContextParams:eventContextParams-Presentation Property*/


	public class EventContextParams extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public EventContextParams(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.ArrStr dummy = ((org.radixware.kernel.common.types.ArrStr)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:eventContextParams:eventContextParams")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:eventContextParams:eventContextParams")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public EventContextParams getEventContextParams();
	public static class Export extends org.radixware.kernel.common.client.models.items.Command{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class Import extends org.radixware.kernel.common.client.models.items.Command{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.types.StrDocument send(org.radixware.ads.PersoComm.common.ImpExpXsd.EventSubscriptionDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.types.StrDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.types.StrDocument.class);
		}

	}



}

/* Radix::PersoComm::EventSubscription - Web Meta*/

/*Radix::PersoComm::EventSubscription-Entity Class*/

package org.radixware.ads.PersoComm.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EventSubscription_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::PersoComm::EventSubscription:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
			"Radix::PersoComm::EventSubscription",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("img3HB6PW72A7PPVUD65FYM2NHX5Z6YVQ4S"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWZH3CR26VXOBDCLUAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKI2DGBS6VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls432C2D26VXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKI2DGBS6VXOBDCLUAALOMT5GDM"),0,

			/*Radix::PersoComm::EventSubscription:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::PersoComm::EventSubscription:lowImportanceMaxSev:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3RTQXCB5VLOBDCLSAALOMT5GDM"),
						"lowImportanceMaxSev",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHTN44CC4VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
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

						/*Radix::PersoComm::EventSubscription:lowImportanceMaxSev:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:channelKind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5OTM5EZZVLOBDCLSAALOMT5GDM"),
						"channelKind",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZQP4Q5K3VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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

						/*Radix::PersoComm::EventSubscription:channelKind:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.INCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aciLQ3IFOMEAZCUJNQW2WSFKVSL3E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aciL2BEJTYZVPORDJHCAANE2UAFXA")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:minEventSeverity:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6MRC5ZR3VLOBDCLSAALOMT5GDM"),
						"minEventSeverity",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQRG5KRC4VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("3"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::EventSubscription:minEventSeverity:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:normalImportanceMaxSev:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAQICTNR5VLOBDCLSAALOMT5GDM"),
						"normalImportanceMaxSev",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHQ57MLS4VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
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

						/*Radix::PersoComm::EventSubscription:normalImportanceMaxSev:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:isActive:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHOISLJZVLOBDCLSAALOMT5GDM"),
						"isActive",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDV7BPNS3VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
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

						/*Radix::PersoComm::EventSubscription:isActive:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:userGroup:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHBY6K7K4VXOBDCLUAALOMT5GDM"),
						"userGroup",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHFY6K7K4VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						null,
						null,
						null,
						262143,
						262143,false),

					/*Radix::PersoComm::EventSubscription:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWHRNERXVLOBDCLSAALOMT5GDM"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDR7BPNS3VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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

						/*Radix::PersoComm::EventSubscription:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:bodyTemplate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIS5OGPB4VLOBDCLSAALOMT5GDM"),
						"bodyTemplate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHCMKAUS3VXOBDCLUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVWJEDWXSBJHOTKNRIHONW3S3KU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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

						/*Radix::PersoComm::EventSubscription:bodyTemplate:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:subjectTemplate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJOXDSNJ4VLOBDCLSAALOMT5GDM"),
						"subjectTemplate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLJQBWUS4VXOBDCLUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOO5ZPULLI5BTPB7WMNDBVCCGMU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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

						/*Radix::PersoComm::EventSubscription:subjectTemplate:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVGT4AJJZVLOBDCLSAALOMT5GDM"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMDBOKWC4VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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

						/*Radix::PersoComm::EventSubscription:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:eventSource:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZQGUBQRZVLOBDCLSAALOMT5GDM"),
						"eventSource",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTK5CPIC3VXOBDCLUAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeZR4KNFFFEJGF3ARHZC76NI3WXA"),
						true,

						/*Radix::PersoComm::EventSubscription:eventSource:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTTCH7Y3EPFE65DH6Z2B5C75ZAU"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:toStoreInHist:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVNCG43RIBREHHI5FFCUATH4PTM"),
						"toStoreInHist",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7WILLH5AXFCAFFZ2RIHAZZC74I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::EventSubscription:toStoreInHist:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:language:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRJPBERR4VLOBDCLSAALOMT5GDM"),
						"language",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3WVW4M7LAJC75MDEZTOEQIS4I4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs2G44GBNBE5D7RO2QHQSVRZNLG4"),
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

						/*Radix::PersoComm::EventSubscription:language:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs2G44GBNBE5D7RO2QHQSVRZNLG4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2ZGSS4HCQ5G7VO76SE6VTDJPYM"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:beforeSendFunc:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZMB564HFKNFC3E5O5XYS3KTLMA"),
						"beforeSendFunc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHOUU5IBUURC3XF2FQ2KABFHFAE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl2Y7336EO3VA4VNS4W2FSMR5RSI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						null,
						null,
						null,
						0L,
						0L,false,false),

					/*Radix::PersoComm::EventSubscription:limitMessBodyTemplate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2FJBO5EEGJCW3MOVEG3KLYQCGA"),
						"limitMessBodyTemplate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHZIRNY3SFBGG3O4EWJ32DTTTZ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRU3CAYHFPVDDNLVLEJZDC2L7CM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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

						/*Radix::PersoComm::EventSubscription:limitMessBodyTemplate:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:limitMessSubjectTemplate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVH3DNHSDUBD3JPNV535DVDBYBU"),
						"limitMessSubjectTemplate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls33K3AFYHLNEFXHCTVJOGEME5YQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBIO2MTODEJCGDIW63HUWOBODME"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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

						/*Radix::PersoComm::EventSubscription:limitMessSubjectTemplate:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:limitPeriodKind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4DUQDXN7NRBVVPPE5WTHOZVOSA"),
						"limitPeriodKind",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKRJYCBYO7NDJHL4HJUFJSH67QA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsS2G5HB3SZVBMTND5FBKRD7LXGU"),
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

						/*Radix::PersoComm::EventSubscription:limitPeriodKind:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsS2G5HB3SZVBMTND5FBKRD7LXGU"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:limitCnt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQQJTGSWPQRGS5LTJG42YEJUHZA"),
						"limitCnt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD47KRDSIN5FAZL73D3WLT7EIFU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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

						/*Radix::PersoComm::EventSubscription:limitCnt:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:routingKey:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAHRGCGAEXRBSFHGEVGOAXJZ4BE"),
						"routingKey",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2PYZRERJQ5CA5HWQZH7CZL5YSQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
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

						/*Radix::PersoComm::EventSubscription:routingKey:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::EventSubscription:eventContextParams:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZCEGA3EGRVG4VFDJ6L3KHCHFWE"),
						"eventContextParams",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX5TWG52IPREY5L3V2WLCSUCERY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
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
						true,
						null,
						true,

						/*Radix::PersoComm::EventSubscription:eventContextParams:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3O2W2YIF7JB7NH6HJTF5F6WZMU"),
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			null,
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refAXWFGZZZVLOBDCLSAALOMT5GDM"),"EventSubscription=>UserGroup (userGroupName=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl7HTJAWJXVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colLHS5GRJZVLOBDCLSAALOMT5GDM")},new String[]{"userGroupName"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4")},new String[]{"name"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4VJZE26HV3OBDCLWAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWZH3CR26VXOBDCLUAALOMT5GDM")},
			false,true,false);
}

/* Radix::PersoComm::EventSubscription:General - Desktop Meta*/

/*Radix::PersoComm::EventSubscription:General-Editor Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4VJZE26HV3OBDCLWAALOMT5GDM"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl7HTJAWJXVLOBDCLSAALOMT5GDM"),
	null,
	null,

	/*Radix::PersoComm::EventSubscription:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::PersoComm::EventSubscription:General:CommonFilter-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMGISRHWCWLOBDCLZAALOMT5GDM"),"CommonFilter",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMKISRHWCWLOBDCLZAALOMT5GDM"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZQGUBQRZVLOBDCLSAALOMT5GDM"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6MRC5ZR3VLOBDCLSAALOMT5GDM"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZMB564HFKNFC3E5O5XYS3KTLMA"),0,2,1,false,false)
			},null),

			/*Radix::PersoComm::EventSubscription:General:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgQ4V765GHV3OBDCLWAALOMT5GDM"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPYPA54OHV3OBDCLWAALOMT5GDM"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWHRNERXVLOBDCLSAALOMT5GDM"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVGT4AJJZVLOBDCLSAALOMT5GDM"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5OTM5EZZVLOBDCLSAALOMT5GDM"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHBY6K7K4VXOBDCLUAALOMT5GDM"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHOISLJZVLOBDCLSAALOMT5GDM"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIS5OGPB4VLOBDCLSAALOMT5GDM"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3RTQXCB5VLOBDCLSAALOMT5GDM"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAQICTNR5VLOBDCLSAALOMT5GDM"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVNCG43RIBREHHI5FFCUATH4PTM"),0,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRJPBERR4VLOBDCLSAALOMT5GDM"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJOXDSNJ4VLOBDCLSAALOMT5GDM"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAHRGCGAEXRBSFHGEVGOAXJZ4BE"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZCEGA3EGRVG4VFDJ6L3KHCHFWE"),0,11,1,false,false)
			},null),

			/*Radix::PersoComm::EventSubscription:General:Event Codes-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGBWNWLDDVXOBDCLUAALOMT5GDM"),"Event Codes",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGFWNWLDDVXOBDCLUAALOMT5GDM"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiHEGLI73CVXOBDCLUAALOMT5GDM")),

			/*Radix::PersoComm::EventSubscription:General:Limits-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVOBE3JCFNFG3JJKD7BNNM5UIWU"),"Limits",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSGTJIWQBOFA6LA7RFIJOPXEJ3M"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4DUQDXN7NRBVVPPE5WTHOZVOSA"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQQJTGSWPQRGS5LTJG42YEJUHZA"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVH3DNHSDUBD3JPNV535DVDBYBU"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2FJBO5EEGJCW3MOVEG3KLYQCGA"),0,3,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgQ4V765GHV3OBDCLWAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMGISRHWCWLOBDCLZAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGBWNWLDDVXOBDCLUAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVOBE3JCFNFG3JJKD7BNNM5UIWU"))}
	,

	/*Radix::PersoComm::EventSubscription:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::PersoComm::EventSubscription:General:EventSubscriptionCode-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiHEGLI73CVXOBDCLUAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPGQRVXELWPOBDCL2AALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecY2BRNTJ5VLOBDCLSAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprM3OC37DCVXOBDCLUAALOMT5GDM"),
					0,
					null,
					16432,false),

				/*Radix::PersoComm::EventSubscription:General:EventLimitAcc-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiDKCAH7XSHRCMJI3735IQJ3FAFU"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOVPOORJSBFAGZERW5IXTRFTWOY"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprGUSFZX5UI5DQ5ANIHF4B3E4SHE"),
					0,
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
/* Radix::PersoComm::EventSubscription:General - Web Meta*/

/*Radix::PersoComm::EventSubscription:General-Editor Presentation*/

package org.radixware.ads.PersoComm.web;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4VJZE26HV3OBDCLWAALOMT5GDM"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl7HTJAWJXVLOBDCLSAALOMT5GDM"),
	null,
	null,

	/*Radix::PersoComm::EventSubscription:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::PersoComm::EventSubscription:General:CommonFilter-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMGISRHWCWLOBDCLZAALOMT5GDM"),"CommonFilter",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMKISRHWCWLOBDCLZAALOMT5GDM"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZQGUBQRZVLOBDCLSAALOMT5GDM"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6MRC5ZR3VLOBDCLSAALOMT5GDM"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZMB564HFKNFC3E5O5XYS3KTLMA"),0,2,1,false,false)
			},null),

			/*Radix::PersoComm::EventSubscription:General:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgQ4V765GHV3OBDCLWAALOMT5GDM"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPYPA54OHV3OBDCLWAALOMT5GDM"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWHRNERXVLOBDCLSAALOMT5GDM"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVGT4AJJZVLOBDCLSAALOMT5GDM"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5OTM5EZZVLOBDCLSAALOMT5GDM"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHBY6K7K4VXOBDCLUAALOMT5GDM"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHOISLJZVLOBDCLSAALOMT5GDM"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIS5OGPB4VLOBDCLSAALOMT5GDM"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3RTQXCB5VLOBDCLSAALOMT5GDM"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAQICTNR5VLOBDCLSAALOMT5GDM"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVNCG43RIBREHHI5FFCUATH4PTM"),0,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRJPBERR4VLOBDCLSAALOMT5GDM"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJOXDSNJ4VLOBDCLSAALOMT5GDM"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAHRGCGAEXRBSFHGEVGOAXJZ4BE"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruZCEGA3EGRVG4VFDJ6L3KHCHFWE"),0,11,1,false,false)
			},null),

			/*Radix::PersoComm::EventSubscription:General:Event Codes-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGBWNWLDDVXOBDCLUAALOMT5GDM"),"Event Codes",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGFWNWLDDVXOBDCLUAALOMT5GDM"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiHEGLI73CVXOBDCLUAALOMT5GDM")),

			/*Radix::PersoComm::EventSubscription:General:Limits-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVOBE3JCFNFG3JJKD7BNNM5UIWU"),"Limits",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSGTJIWQBOFA6LA7RFIJOPXEJ3M"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4DUQDXN7NRBVVPPE5WTHOZVOSA"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQQJTGSWPQRGS5LTJG42YEJUHZA"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVH3DNHSDUBD3JPNV535DVDBYBU"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2FJBO5EEGJCW3MOVEG3KLYQCGA"),0,3,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgQ4V765GHV3OBDCLWAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMGISRHWCWLOBDCLZAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGBWNWLDDVXOBDCLUAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVOBE3JCFNFG3JJKD7BNNM5UIWU"))}
	,

	/*Radix::PersoComm::EventSubscription:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::PersoComm::EventSubscription:General:EventSubscriptionCode-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiHEGLI73CVXOBDCLUAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPGQRVXELWPOBDCL2AALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecY2BRNTJ5VLOBDCLSAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprM3OC37DCVXOBDCLUAALOMT5GDM"),
					0,
					null,
					16432,false),

				/*Radix::PersoComm::EventSubscription:General:EventLimitAcc-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiDKCAH7XSHRCMJI3735IQJ3FAFU"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecOVPOORJSBFAGZERW5IXTRFTWOY"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprGUSFZX5UI5DQ5ANIHF4B3E4SHE"),
					0,
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
/* Radix::PersoComm::EventSubscription:General:Model - Desktop Executable*/

/*Radix::PersoComm::EventSubscription:General:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:General:Model")
public class General:Model  extends org.radixware.ads.PersoComm.explorer.EventSubscription.EventSubscription_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::PersoComm::EventSubscription:General:Model:Nested classes-Nested Classes*/

	/*Radix::PersoComm::EventSubscription:General:Model:Properties-Properties*/

	/*Radix::PersoComm::EventSubscription:General:Model:limitCnt-Presentation Property*/




	public class LimitCnt extends org.radixware.ads.PersoComm.explorer.EventSubscription.colQQJTGSWPQRGS5LTJG42YEJUHZA{
		public LimitCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:General:Model:limitCnt")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:General:Model:limitCnt")
		public   void setValue(Int val) {

			internal[limitCnt] = val;
			setLimitMandatority();
		}
	}
	public LimitCnt getLimitCnt(){return (LimitCnt)getProperty(colQQJTGSWPQRGS5LTJG42YEJUHZA);}

	/*Radix::PersoComm::EventSubscription:General:Model:limitPeriodKind-Presentation Property*/




	public class LimitPeriodKind extends org.radixware.ads.PersoComm.explorer.EventSubscription.col4DUQDXN7NRBVVPPE5WTHOZVOSA{
		public LimitPeriodKind(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.PersoComm.common.LimitPeriodKind dummy = x == null ? null : (x instanceof org.radixware.ads.PersoComm.common.LimitPeriodKind ? (org.radixware.ads.PersoComm.common.LimitPeriodKind)x : org.radixware.ads.PersoComm.common.LimitPeriodKind.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.PersoComm.common.LimitPeriodKind> getValClass(){
			return org.radixware.ads.PersoComm.common.LimitPeriodKind.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.PersoComm.common.LimitPeriodKind dummy = x == null ? null : (x instanceof org.radixware.ads.PersoComm.common.LimitPeriodKind ? (org.radixware.ads.PersoComm.common.LimitPeriodKind)x : org.radixware.ads.PersoComm.common.LimitPeriodKind.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:General:Model:limitPeriodKind")
		public  org.radixware.ads.PersoComm.common.LimitPeriodKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:General:Model:limitPeriodKind")
		public   void setValue(org.radixware.ads.PersoComm.common.LimitPeriodKind val) {

			internal[limitPeriodKind] = val;
			setLimitMandatority();
		}
	}
	public LimitPeriodKind getLimitPeriodKind(){return (LimitPeriodKind)getProperty(col4DUQDXN7NRBVVPPE5WTHOZVOSA);}










	/*Radix::PersoComm::EventSubscription:General:Model:Methods-Methods*/

	/*Radix::PersoComm::EventSubscription:General:Model:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:General:Model:onCommand_Import")
	public  void onCommand_Import (org.radixware.ads.PersoComm.explorer.EventSubscription.Import command) {
		java.io.File file = CfgManagement::DesktopUtils.openFileForImport(findNearestView(), command.getTitle());
		if (file == null)
		    return;

		try {
		    ImpExpXsd:EventSubscriptionDocument xIn = ImpExpXsd:EventSubscriptionDocument.Factory.parse(file);
		    Common.Dlg::ClientUtils.viewImportResult(command.send(xIn));
		    getView().reread();
		} catch (Exceptions::XmlException | Exceptions::ServiceClientException | Exceptions::IOException e) {
		    showException(e);
		} catch (Exceptions::InterruptedException e) {
		}
	}

	/*Radix::PersoComm::EventSubscription:General:Model:setLimitMandatority-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:General:Model:setLimitMandatority")
	public  void setLimitMandatority () {
		limitPeriodKind.setMandatory(limitPeriodKind.Value == null && limitCnt.Value != null);
		limitCnt.setMandatory(limitCnt.Value == null && limitPeriodKind.Value != null);
	}

	/*Radix::PersoComm::EventSubscription:General:Model:getDisplayString-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:General:Model:getDisplayString")
	public published  Str getDisplayString (org.radixware.kernel.common.types.Id propertyId, java.lang.Object propertyValue, Str defaultDisplayString, boolean isInherited) {
		if (propertyId == idof[EventSubscription:eventContextParams]) {
		    final ArrStr arr = (ArrStr)propertyValue;
		    if (arr != null && !arr.isEmpty()) {
		        final java.lang.StringBuilder sb = new java.lang.StringBuilder();
		        for (int i = 1; i < arr.size(); i += 3) {
		            if (i != 1) {
		                sb.append(" | ");
		            }
		            sb.append(arr.get(i)).append("-").append(Utils::Nvl.get(arr.get(i + 1), "<null>"));
		        }
		        return sb.toString();
		    }
		}

		return super.getDisplayString(propertyId, propertyValue, defaultDisplayString, isInherited);
	}
	public final class Import extends org.radixware.ads.PersoComm.explorer.EventSubscription.Import{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Import( this );
		}

	}













}

/* Radix::PersoComm::EventSubscription:General:Model - Desktop Meta*/

/*Radix::PersoComm::EventSubscription:General:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aem4VJZE26HV3OBDCLWAALOMT5GDM"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::PersoComm::EventSubscription:General:Model:Properties-Properties*/
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

/* Radix::PersoComm::EventSubscription:General:Model - Web Executable*/

/*Radix::PersoComm::EventSubscription:General:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:General:Model")
public class General:Model  extends org.radixware.ads.PersoComm.web.EventSubscription.EventSubscription_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::PersoComm::EventSubscription:General:Model:Nested classes-Nested Classes*/

	/*Radix::PersoComm::EventSubscription:General:Model:Properties-Properties*/

	/*Radix::PersoComm::EventSubscription:General:Model:limitCnt-Presentation Property*/




	public class LimitCnt extends org.radixware.ads.PersoComm.web.EventSubscription.colQQJTGSWPQRGS5LTJG42YEJUHZA{
		public LimitCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:General:Model:limitCnt")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:General:Model:limitCnt")
		public   void setValue(Int val) {

			internal[limitCnt] = val;
			setLimitMandatority();
		}
	}
	public LimitCnt getLimitCnt(){return (LimitCnt)getProperty(colQQJTGSWPQRGS5LTJG42YEJUHZA);}

	/*Radix::PersoComm::EventSubscription:General:Model:limitPeriodKind-Presentation Property*/




	public class LimitPeriodKind extends org.radixware.ads.PersoComm.web.EventSubscription.col4DUQDXN7NRBVVPPE5WTHOZVOSA{
		public LimitPeriodKind(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.PersoComm.common.LimitPeriodKind dummy = x == null ? null : (x instanceof org.radixware.ads.PersoComm.common.LimitPeriodKind ? (org.radixware.ads.PersoComm.common.LimitPeriodKind)x : org.radixware.ads.PersoComm.common.LimitPeriodKind.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.PersoComm.common.LimitPeriodKind> getValClass(){
			return org.radixware.ads.PersoComm.common.LimitPeriodKind.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.PersoComm.common.LimitPeriodKind dummy = x == null ? null : (x instanceof org.radixware.ads.PersoComm.common.LimitPeriodKind ? (org.radixware.ads.PersoComm.common.LimitPeriodKind)x : org.radixware.ads.PersoComm.common.LimitPeriodKind.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:General:Model:limitPeriodKind")
		public  org.radixware.ads.PersoComm.common.LimitPeriodKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:General:Model:limitPeriodKind")
		public   void setValue(org.radixware.ads.PersoComm.common.LimitPeriodKind val) {

			internal[limitPeriodKind] = val;
			setLimitMandatority();
		}
	}
	public LimitPeriodKind getLimitPeriodKind(){return (LimitPeriodKind)getProperty(col4DUQDXN7NRBVVPPE5WTHOZVOSA);}










	/*Radix::PersoComm::EventSubscription:General:Model:Methods-Methods*/

	/*Radix::PersoComm::EventSubscription:General:Model:setLimitMandatority-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:General:Model:setLimitMandatority")
	public  void setLimitMandatority () {
		limitPeriodKind.setMandatory(limitPeriodKind.Value == null && limitCnt.Value != null);
		limitCnt.setMandatory(limitCnt.Value == null && limitPeriodKind.Value != null);
	}

	/*Radix::PersoComm::EventSubscription:General:Model:getDisplayString-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:General:Model:getDisplayString")
	public published  Str getDisplayString (org.radixware.kernel.common.types.Id propertyId, java.lang.Object propertyValue, Str defaultDisplayString, boolean isInherited) {
		if (propertyId == idof[EventSubscription:eventContextParams]) {
		    final ArrStr arr = (ArrStr)propertyValue;
		    if (arr != null && !arr.isEmpty()) {
		        final java.lang.StringBuilder sb = new java.lang.StringBuilder();
		        for (int i = 1; i < arr.size(); i += 3) {
		            if (i != 1) {
		                sb.append(" | ");
		            }
		            sb.append(arr.get(i)).append("-").append(Utils::Nvl.get(arr.get(i + 1), "<null>"));
		        }
		        return sb.toString();
		    }
		}

		return super.getDisplayString(propertyId, propertyValue, defaultDisplayString, isInherited);
	}


}

/* Radix::PersoComm::EventSubscription:General:Model - Web Meta*/

/*Radix::PersoComm::EventSubscription:General:Model-Entity Model Class*/

package org.radixware.ads.PersoComm.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aem4VJZE26HV3OBDCLWAALOMT5GDM"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::PersoComm::EventSubscription:General:Model:Properties-Properties*/
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

/* Radix::PersoComm::EventSubscription:Common - Desktop Meta*/

/*Radix::PersoComm::EventSubscription:Common-Selector Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class Common_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Common_mi();
	private Common_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWZH3CR26VXOBDCLUAALOMT5GDM"),
		"Common",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl7HTJAWJXVLOBDCLSAALOMT5GDM"),
		null,
		null,
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
		2192,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4VJZE26HV3OBDCLWAALOMT5GDM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4VJZE26HV3OBDCLWAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWHRNERXVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVGT4AJJZVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5OTM5EZZVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHOISLJZVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
}
/* Radix::PersoComm::EventSubscription:Common - Web Meta*/

/*Radix::PersoComm::EventSubscription:Common-Selector Presentation*/

package org.radixware.ads.PersoComm.web;
public final class Common_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Common_mi();
	private Common_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWZH3CR26VXOBDCLUAALOMT5GDM"),
		"Common",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl7HTJAWJXVLOBDCLSAALOMT5GDM"),
		null,
		null,
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
		2192,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4VJZE26HV3OBDCLWAALOMT5GDM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4VJZE26HV3OBDCLWAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHWHRNERXVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVGT4AJJZVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5OTM5EZZVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHOISLJZVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
}
/* Radix::PersoComm::EventSubscription:Common:Model - Desktop Executable*/

/*Radix::PersoComm::EventSubscription:Common:Model-Group Model Class*/

package org.radixware.ads.PersoComm.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:Common:Model")
public class Common:Model  extends org.radixware.ads.PersoComm.explorer.EventSubscription.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Common:Model_mi.rdxMeta; }



	public Common:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::PersoComm::EventSubscription:Common:Model:Nested classes-Nested Classes*/

	/*Radix::PersoComm::EventSubscription:Common:Model:Properties-Properties*/

	/*Radix::PersoComm::EventSubscription:Common:Model:Methods-Methods*/

	/*Radix::PersoComm::EventSubscription:Common:Model:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:Common:Model:onCommand_Import")
	public  void onCommand_Import (org.radixware.ads.PersoComm.explorer.EventSubscriptionGroup.Import command) {
		java.io.File file = CfgManagement::DesktopUtils.openFileForImport(findNearestView(), command.getTitle());
		if (file == null)
		    return;

		ImpExpXsd:EventSubscriptionGroupDocument xIn;

		try {
		    try {
		        xIn = ImpExpXsd:EventSubscriptionGroupDocument.Factory.parse(file);
		    } catch (Exceptions::XmlException ex) {
		        ImpExpXsd:EventSubscriptionDocument xSingleEvSub = ImpExpXsd:EventSubscriptionDocument.Factory.parse(file);
		        xIn = ImpExpXsd:EventSubscriptionGroupDocument.Factory.newInstance();
		        xIn.addNewEventSubscriptionGroup().addNewItem().set(xSingleEvSub.EventSubscription);
		    }
		    
		    Common.Dlg::ClientUtils.viewImportResult(command.send(xIn));
		    getView().reread();
		} catch (Exceptions::XmlException | Exceptions::ServiceClientException | Exceptions::IOException e) {
		    showException(e);
		} catch (Exceptions::InterruptedException e) {
		}
	}
	public final class Import extends org.radixware.ads.PersoComm.explorer.EventSubscriptionGroup.Import{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Import( this );
		}

	}













}

/* Radix::PersoComm::EventSubscription:Common:Model - Desktop Meta*/

/*Radix::PersoComm::EventSubscription:Common:Model-Group Model Class*/

package org.radixware.ads.PersoComm.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Common:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmWZH3CR26VXOBDCLUAALOMT5GDM"),
						"Common:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::PersoComm::EventSubscription:Common:Model:Properties-Properties*/
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

/* Radix::PersoComm::EventSubscription:Common:Model - Web Executable*/

/*Radix::PersoComm::EventSubscription:Common:Model-Group Model Class*/

package org.radixware.ads.PersoComm.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventSubscription:Common:Model")
public class Common:Model  extends org.radixware.ads.PersoComm.web.EventSubscription.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Common:Model_mi.rdxMeta; }



	public Common:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::PersoComm::EventSubscription:Common:Model:Nested classes-Nested Classes*/

	/*Radix::PersoComm::EventSubscription:Common:Model:Properties-Properties*/

	/*Radix::PersoComm::EventSubscription:Common:Model:Methods-Methods*/


}

/* Radix::PersoComm::EventSubscription:Common:Model - Web Meta*/

/*Radix::PersoComm::EventSubscription:Common:Model-Group Model Class*/

package org.radixware.ads.PersoComm.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Common:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmWZH3CR26VXOBDCLUAALOMT5GDM"),
						"Common:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::PersoComm::EventSubscription:Common:Model:Properties-Properties*/
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

/* Radix::PersoComm::EventSubscription - Localizing Bundle */
package org.radixware.ads.PersoComm.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EventSubscription - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ключ маршрутизации");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Routing key");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2PYZRERJQ5CA5HWQZH7CZL5YSQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<системный>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<default>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2ZGSS4HCQ5G7VO76SE6VTDJPYM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Шаблон темы лимит-сообщений");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Limit message subject template");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls33K3AFYHLNEFXHCTVJOGEME5YQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любой>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3O2W2YIF7JB7NH6HJTF5F6WZMU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Язык");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Language");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3WVW4M7LAJC75MDEZTOEQIS4I4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Подписка на события");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Events Subscription");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls432C2D26VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сохранять отправленные сообщения");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Store sent messages");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7WILLH5AXFCAFFZ2RIHAZZC74I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс, реализующий подписку на уведомления по системным событиям.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class implements subscription to system event notifications.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAS5EJFVF2VCL3LFKJOZH7DNG4I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"{0} – ид. сообщения {1} – текст сообщения {2} – важность сообщения {3} – источник сообщения {4} – контекст сообщения {5} - время сообщения {6} – следующее время сброса лимита");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"{0} – event ID {1} – event text {2} – event severity {3} – event source {4} – event context  {5} - event time  {6} – limit purge time");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBIO2MTODEJCGDIW63HUWOBODME"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Лимит сообщений");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Limit count");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD47KRDSIN5FAZL73D3WLT7EIFU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDR7BPNS3VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Активна");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Active");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDV7BPNS3VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импорт");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEEQKYP33D5H55KQCIJRGKJQUPU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Фильтр по кодам событий");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Filter by Event Code");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGFWNWLDDVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Шаблон тела сообщений");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message body template");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHCMKAUS3VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа получателей");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Recipient group");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHFY6K7K4VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Функция перед отправкой сообщения");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Function before message sending");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHOUU5IBUURC3XF2FQ2KABFHFAE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Максимальная серьезность событий с обычной важностью");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maximum severity for normal importance messages");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHQ57MLS4VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Максимальная серьезность событий с низкой важностью");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maximum severity for low importance messages");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHTN44CC4VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Шаблон тела лимит-сообщений");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Limit message body template");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHZIRNY3SFBGG3O4EWJ32DTTTZ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Подписка на событие");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event Subscription");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKI2DGBS6VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Период лимита");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Limit period");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKRJYCBYO7NDJHL4HJUFJSH67QA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Свойство содержащее пользовательскую функцию, которая будет выполняться перед отправкой сообщения.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Property containing the user-defined function to be executed before sending the message.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL2SSLUIBCFCL5CTTHUPCUEHWLY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Шаблон темы сообщений");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message subject template");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLJQBWUS4VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMDBOKWC4VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя группы получателей");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Recipient group name");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMHBOKWC4VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Базовый фильтр событий");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Base Event Filter");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMKISRHWCWLOBDCLZAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"{0} – ид. сообщения {1} – текст сообщения {2} – важность сообщения {3} – источник сообщения {4} – контекст сообщения {5} - время сообщения");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"{0} – event ID {1} – event text {2} – event severity {3} – event source {4} – event context  {5} - event time");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOO5ZPULLI5BTPB7WMNDBVCCGMU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Фильтр кодов событий");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event Codes Filter");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPGQRVXELWPOBDCL2AALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"a[0 (mod 3)] = tableId\na[1 (mod 3)] = contextType\na[2 (mod 3)] = contextId\n");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPOCVCGS5N5BDPCK765FIFYETQU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPYPA54OHV3OBDCLWAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Минимальная серьезность событий");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Minimum event severity");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQRG5KRC4VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"{0} – ид. сообщения {1} – текст сообщения {2} – важность сообщения {3} – источник сообщения {4} – контекст сообщения {5} - время сообщения {6} – следующее время сброса лимита");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"{0} – event ID {1} – event text {2} – event severity {3} – event source {4} – event context  {5} - event time {6} – limit purge time");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRU3CAYHFPVDDNLVLEJZDC2L7CM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Лимиты");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Limits");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSGTJIWQBOFA6LA7RFIJOPXEJ3M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка импорта подписки на события: группа получателей не найдена по имени %s.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error importing the event subscription: group of recipients not found by name %s.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT5I6P2T6B5GINNRTCEYUJKEY5Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Источник событий");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event source");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTK5CPIC3VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любой>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTTCH7Y3EPFE65DH6Z2B5C75ZAU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"{0} – ид. сообщения {1} – текст сообщения {2} – важность сообщения {3} – источник сообщения {4} – контекст сообщения {5} - время сообщения");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"{0} – event ID {1} – event text {2} – event severity {3} – event source {4} – event context  {5} - event time");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVWJEDWXSBJHOTKNRIHONW3S3KU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры контекста события");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event context parameters");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX5TWG52IPREY5L3V2WLCSUCERY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспорт");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXS7QMXI3NJHKDLDE46H6PI6ZJ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вид канала доставки");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Delivery channel type");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZQP4Q5K3VXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\PersoComm"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(EventSubscription - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaec7HTJAWJXVLOBDCLSAALOMT5GDM"),"EventSubscription - Localizing Bundle",$$$items$$$);
}
