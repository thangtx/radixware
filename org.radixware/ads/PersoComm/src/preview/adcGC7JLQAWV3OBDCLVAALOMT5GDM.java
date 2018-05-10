
/* Radix::PersoComm::EventNotifier - Server Executable*/

/*Radix::PersoComm::EventNotifier-Server Dynamic Class*/

package org.radixware.ads.PersoComm.server;

import java.util.HashMap;


@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventNotifier")
public class EventNotifier  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private static class FormatCodes {
	    final public static Str EVENT_ID = "{0}";
	    final public static Str EVENT_TITLE = "{1}";
	    final public static Str EVENT_SEVERITY = "{2}";
	    final public static Str EVENT_SOURCE = "{3}";
	    final public static Str EVENT_CONTEXT = "{4}";
	    final public static Str EVENT_TIME = "{5}";
	    final public static Str LIMIT_TIME = "{6}";
	}
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return EventNotifier_mi.rdxMeta;}

	/*Radix::PersoComm::EventNotifier:Nested classes-Nested Classes*/

	/*Radix::PersoComm::EventNotifier:Properties-Properties*/

	/*Radix::PersoComm::EventNotifier:usersBySubscription-Dynamic Property*/



	protected java.util.Map<Int,org.radixware.kernel.server.types.ArrEntity<org.radixware.ads.mdlRADIXDRC__________________.server.aecSY4KIOLTGLNRDHRZABQAQH3XQ4>> usersBySubscription=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventNotifier:usersBySubscription")
	private final  java.util.Map<Int,org.radixware.kernel.server.types.ArrEntity<org.radixware.ads.mdlRADIXDRC__________________.server.aecSY4KIOLTGLNRDHRZABQAQH3XQ4>> getUsersBySubscription() {

		if(internal[usersBySubscription] == null)
		    internal[usersBySubscription] = new HashMap<Int, ArrParentRef<Acs::User>>();

		return internal[usersBySubscription];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventNotifier:usersBySubscription")
	private final   void setUsersBySubscription(java.util.Map<Int,org.radixware.kernel.server.types.ArrEntity<org.radixware.ads.mdlRADIXDRC__________________.server.aecSY4KIOLTGLNRDHRZABQAQH3XQ4>> val) {
		usersBySubscription = val;
	}

	/*Radix::PersoComm::EventNotifier:subscriptions-Dynamic Property*/



	protected org.radixware.ads.PersoComm.server.EventNotifier.Subscriptions subscriptions=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventNotifier:subscriptions")
	private final  org.radixware.ads.PersoComm.server.EventNotifier.Subscriptions getSubscriptions() {
		return subscriptions;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventNotifier:subscriptions")
	private final   void setSubscriptions(org.radixware.ads.PersoComm.server.EventNotifier.Subscriptions val) {
		subscriptions = val;
	}









































	/*Radix::PersoComm::EventNotifier:Methods-Methods*/

	/*Radix::PersoComm::EventNotifier:createMessages-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventNotifier:createMessages")
	private final  int createMessages (org.radixware.ads.PersoComm.server.EventSubscription subscription, org.radixware.ads.System.server.EventLog event, Str subject, Str body, org.radixware.kernel.common.enums.EPersoCommImportance importance, java.sql.Timestamp dueTime, java.sql.Timestamp expTime, Str sourceEntityGuid) {
		int count = 0;
		for(Acs::User user : getUsers(subscription.id)) {
		    Str address = getAddress(subscription, user); // may be inited in user function
		    
		    OutMessage msg = new OutMessage();
		    msg.init();
		    msg.address = address;
		    msg.subject = subject;
		    msg.body = body;
		    msg.importance = importance;
		    msg.sourceEntityGuid = sourceEntityGuid;
		    msg.sourcePid = subscription.id.toString();
		    msg.createTime = Arte::Arte.getCurrentTime();
		    msg.dueTime = dueTime;
		    msg.expireTime = expTime;
		    msg.channelKind = subscription.channelKind;
		    msg.routingKey = subscription.routingKey;
		    msg.histMode = subscription.toStoreInHist == true ? HistMode:Audit : HistMode:DontStore;

		    if (subscription.beforeSendFunc!=null && !subscription.beforeSendFunc.beforeSend(event, msg, user))
		        address = null;

		    try {
		        if(Utils::String.isNotBlank(address) && checkLimit(subscription, event, user, sourceEntityGuid)){
		            msg.create();
		            count++;
		        }else{
		            msg.discard();
		        }
		    } catch(Exceptions::DatabaseError e){
		        Arte::Trace.error(String.format("Error creating a message for subscription '%d', address '%s'", subscription.id, msg.address)+"\n"+e.getMessage(), Arte::EventSource:AppSysEventNotify);
		        msg.discard();
		    }
		}

		return count;
	}

	/*Radix::PersoComm::EventNotifier:exec-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventNotifier:exec")
	public static  int exec (java.sql.Timestamp prevTime, java.sql.Timestamp curTime) {
		return exec(prevTime, curTime, idof[EventSubscription].toString(), false);
	}

	/*Radix::PersoComm::EventNotifier:getUsers-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventNotifier:getUsers")
	private final  org.radixware.kernel.server.types.ArrEntity<org.radixware.ads.mdlRADIXDRC__________________.server.aecSY4KIOLTGLNRDHRZABQAQH3XQ4> getUsers (Int subscriptionId) {
		if(!usersBySubscription.containsKey(subscriptionId)) {
		    Arte::Trace.debug("Building users by subscription ID: " + subscriptionId, Arte::EventSource:AppSysEventNotify);
		    PersoComm.Db::SubscribedUsersCursor cur = PersoComm.Db::SubscribedUsersCursor.open(subscriptionId);
		    ArrParentRef<Acs::User> users = new ArrParentRef<Acs::User>(Arte::Arte.getInstance());
		    try{
		        while(cur.next()){
		            Arte::Trace.debug("User added: " + cur.user.calcTitle(), Arte::EventSource:AppSysEventNotify);
		            users.add(cur.user);
		        }
		    }finally{
		        cur.close();
		    }
		    usersBySubscription.put(subscriptionId, users);
		}

		return usersBySubscription.get(subscriptionId);
	}

	/*Radix::PersoComm::EventNotifier:do-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventNotifier:do")
	private final  int do (java.sql.Timestamp prevTime, java.sql.Timestamp curTime, Str sourceEntityGuid, boolean testing) {
		Arte::Trace.debug("Generating messages for period: "
		        + Utils::Timing.dateTimeToLocalString(prevTime) + " - " + Utils::Timing.dateTimeToLocalString(curTime),
		        Arte::EventSource:AppSysEventNotify);

		if (subscriptions.isEmpty()) {
		    Arte::Trace.debug("No subscriptions", Arte::EventSource:AppSysEventNotify);
		    return 0;
		}

		int lastCommitedPortion = 0;
		int totalCount = 0;
		int eventsCount = 0;
		Int cachingSectionId = null;

		try {
		    PersoComm.Db::EventsCursor cursor = PersoComm.Db::EventsCursor.open(prevTime, curTime);
		    try {
		        while (cursor.next()) {
		            if (eventsCount == 0) {
		                cachingSectionId = Arte::Arte.enterCachingSession();
		            }
		            eventsCount++;
		            //one event - many subscribers and templates
		            totalCount += processEvent(cursor.event, sourceEntityGuid, testing);
		            if (totalCount - lastCommitedPortion > 100) {
		                lastCommitedPortion = totalCount;
		                try {
		                    Arte::Arte.leaveCachingSession(cachingSectionId);
		                    Arte::Arte.commit();
		                    try {
		                        cachingSectionId = Arte::Arte.enterCachingSession();
		                    } catch (Exception ex) {
		                        Arte::Trace.warning("Unable to enter to new caching section: " + Utils::ExceptionTextFormatter.exceptionStackToString(ex), Arte::EventSource:AppSysEventNotify);
		                    }
		                } catch (Exception ex) {
		                    Arte::Trace.warning("Unable to leave caching section to clear temporary resources: " + Utils::ExceptionTextFormatter.exceptionStackToString(ex), Arte::EventSource:AppSysEventNotify);
		                }
		            }
		        }
		    } finally {
		        cursor.close();
		    }
		} finally {
		    if (cachingSectionId != null) {
		        try {
		            Arte::Arte.leaveCachingSession(cachingSectionId);
		        } catch (Exception ex) {
		            Arte::Trace.warning("Unable to leave caching section to clear temporary resources: " + Utils::ExceptionTextFormatter.exceptionStackToString(ex), Arte::EventSource:AppSysEventNotify);
		        }
		    }
		}

		Arte::Trace.debug(String.format("Messages generation completed. Records processed: %s, messages generated: %s", eventsCount, totalCount), Arte::EventSource:AppSysEventNotify);
		return totalCount;
	}

	/*Radix::PersoComm::EventNotifier:formatString-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventNotifier:formatString")
	private static  Str formatString (Str format, Int eventId, Str eventTitle, org.radixware.kernel.common.enums.EEventSeverity eventSeverity, Str eventSourceTitle, Str contextString, java.sql.Timestamp time, java.sql.Timestamp limitTime) {
		if(format == null || format.isEmpty())
		    return null;

		Str res = format;
		res = res.replaceAll("\\" + FormatCodes.EVENT_ID, java.util.regex.Matcher.quoteReplacement(String.valueOf(eventId)));
		res = res.replaceAll("\\" + FormatCodes.EVENT_TITLE, java.util.regex.Matcher.quoteReplacement(String.valueOf(eventTitle)));
		res = res.replaceAll("\\" + FormatCodes.EVENT_SEVERITY, java.util.regex.Matcher.quoteReplacement(Meta::Utils.getEventSeverityTitle(eventSeverity)));
		res = res.replaceAll("\\" + FormatCodes.EVENT_SOURCE, java.util.regex.Matcher.quoteReplacement(eventSourceTitle));
		res = res.replaceAll("\\" + FormatCodes.EVENT_CONTEXT, java.util.regex.Matcher.quoteReplacement(String.valueOf(contextString)));
		res = res.replaceAll("\\" + FormatCodes.EVENT_TIME, java.util.regex.Matcher.quoteReplacement(String.valueOf(time)));
		res = res.replaceAll("\\" + FormatCodes.LIMIT_TIME, java.util.regex.Matcher.quoteReplacement(String.valueOf(limitTime)));
		return res;
	}

	/*Radix::PersoComm::EventNotifier:getNeedContext-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventNotifier:getNeedContext")
	private static  boolean getNeedContext (org.radixware.ads.PersoComm.server.EventSubscription subscription) {
		return (subscription.subjectTemplate != null && subscription.subjectTemplate.indexOf("{4}") != 0) ||
		        (subscription.bodyTemplate != null && subscription.bodyTemplate.indexOf("{4}") != 0) ||
		        subscription.eventContextTypeIdPairs != null;
	}

	/*Radix::PersoComm::EventNotifier:getSubject-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventNotifier:getSubject")
	private static  Str getSubject (Str template, Int eventId, Str eventTitle, org.radixware.kernel.common.enums.EEventSeverity eventSeverity, Str eventSourceTitle, Str contextString, java.sql.Timestamp time, java.sql.Timestamp limitTime) {
		return Utils::String.truncTrail(formatString(template, eventId, eventTitle, eventSeverity, eventSourceTitle, contextString, time, limitTime), 200);
	}

	/*Radix::PersoComm::EventNotifier:getBody-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventNotifier:getBody")
	private static  Str getBody (Str template, Int eventId, Str eventTitle, org.radixware.kernel.common.enums.EEventSeverity eventSeverity, Str eventSourceTitle, Str contextString, java.sql.Timestamp time, java.sql.Timestamp limitTime) {
		return Utils::String.truncTrail(formatString(template, eventId, eventTitle, eventSeverity, eventSourceTitle, contextString, time, limitTime), 4000);
	}

	/*Radix::PersoComm::EventNotifier:exec-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventNotifier:exec")
	public static  int exec (java.sql.Timestamp prevTime, java.sql.Timestamp curTime, Str sourceEntityGuid, boolean testing) {
		//первый запуск
		if(prevTime == null || curTime == null)
		    return 0;

		EventNotifier notifier = new EventNotifier();

		return notifier.do(new DateTime(prevTime.getTime()-5*1000),
		        new DateTime(curTime.getTime()-5*1000),
		        sourceEntityGuid,
		        testing);
	}

	/*Radix::PersoComm::EventNotifier:EventNotifier-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventNotifier:EventNotifier")
	protected  EventNotifier () {
		Arte::Trace.debug("Building subscribers list", Arte::EventSource:AppSysEventNotify);

		PersoComm.Db::ActiveSubscriptionCodesCursor cur = PersoComm.Db::ActiveSubscriptionCodesCursor.open();
		subscriptions = new EventNotifier.Subscriptions();
		try {
		    while(cur.next()){
		        Arte::Trace.debug("Event code: " + cur.code + ", subscription ID: " + cur.subscription.id, Arte::EventSource:AppSysEventNotify);
		        subscriptions.add(cur.code, cur.subscription);
		    }
		} finally {
		    cur.close();
		}
	}

	/*Radix::PersoComm::EventNotifier:processEvent-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventNotifier:processEvent")
	protected published  int processEvent (org.radixware.ads.System.server.EventLog event, Str sourceEntityGuid, boolean testing) {
		Arte::Trace.debug("Process event ID: " + event.id.toString(), Arte::EventSource:AppSysEventNotify);

		final DateTime dueTime;
		final DateTime expTime;
		if (testing) {
		    expTime = Utils::Timing.getCurrentTime();
		    dueTime = Utils::Timing.addDay(expTime, 1);
		} else {
		    expTime = null;
		    dueTime = Utils::Timing.getCurrentTime();
		}

		int count = 0;
		for (EventSubscription sub : subscriptions.get(event.code, event.component, event.severity)) {
		    try {
		        Str contextStr = null;
		        if (EventNotifier.getNeedContext(sub) == true) {
		            PersoComm.Db::GetEventContextStrStmt proc = PersoComm.Db::GetEventContextStrStmt.execute(event.raiseTime, event.id);
		            contextStr = proc.contextsStr;
		            
		            if (sub.eventContextTypeIdPairs != null) {
		                if (contextStr == null) {
		                    continue;
		                }
		                boolean contextsMatch = false;
		                for (int i = 0; !contextsMatch && i < sub.eventContextTypeIdPairs.size(); ++i) {
		                    contextsMatch = contextStr.contains(sub.eventContextTypeIdPairs.get(i));
		                }
		                if (!contextsMatch) {
		                    continue;
		                }
		            }
		        }

		        final Str title;
		        if (sub.language != null)
		            title = System::EventLog.calcMessage(event.code, event.wordsClob, sub.language);
		        else
		            title = event.message;
		        final Str eventSourceTitle = event.component == null ? "<null>" : (sub.language != null ? event.component.getTitle(sub.language) : event.component.Title);
		        final Str subject = EventNotifier.getSubject(sub.subjectTemplate, event.id, title, event.severity, eventSourceTitle, contextStr, event.raiseTime, null);
		        final Str body = EventNotifier.getBody(sub.bodyTemplate, event.id, title, event.severity, eventSourceTitle, contextStr, event.raiseTime, null);

		        final Importance importance = (event.severity.getValue().intValue() <= sub.lowImportanceMaxSev.getValue().intValue())
		                ? Importance:Low
		                : (event.severity.getValue().intValue() <= sub.normalImportanceMaxSev.getValue().intValue())
		                        ? Importance:Normal : Importance:High;
		        final int c = createMessages(sub, event, subject, body, importance, dueTime, expTime, sourceEntityGuid);
		        Arte::Trace.debug(String.format("%d message(s) generated for event # %s (code: '%s'), subscription ID: %d", c, String.valueOf(event.id), event.code, sub.id), Arte::EventSource:AppSysEventNotify);
		        count += c;
		    } catch (Throwable t) {
		        String subscrTitle = "#" + sub.id;
		        try {
		            subscrTitle = sub.calcTitle();
		        } catch (Throwable t1) {
		            //ignore
		        }
		        Arte::Trace.put(eventCode["Error generating messages for event subscription '%1': %2"], subscrTitle, Utils::ExceptionTextFormatter.throwableToString(t));
		    }
		}
		return count;
	}

	/*Radix::PersoComm::EventNotifier:checkLimit-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventNotifier:checkLimit")
	private final  boolean checkLimit (org.radixware.ads.PersoComm.server.EventSubscription subscription, org.radixware.ads.System.server.EventLog event, org.radixware.ads.Acs.server.User user, Str sourceEntityGuid) {
		if (subscription.limitPeriodKind == null || subscription.limitCnt == null || event.code == null)
		    return true;

		long dt = 0;
		switch (subscription.limitPeriodKind) {
		    case LimitPeriodKind:Minute:
		        dt = 60*1000;
		        break;
		    case LimitPeriodKind:Hour:
		        dt = 60*60*1000;
		        break;
		    case LimitPeriodKind:Day:
		        dt = 24*60*60*1000;
		        break;
		}

		final DateTime curTime = Arte::Arte.getCurrentTime();
		EventLimitAcc limit = EventLimitAcc.loadByPK(subscription.id, user.name, event.code, subscription.channelKind, true);
		if (limit == null) {
		    limit = new EventLimitAcc();
		    limit.init();
		    limit.subscriptionId = subscription.id;
		    limit.userName = user.name;
		    limit.eventCode = event.code;
		    limit.channelKind = subscription.channelKind;
		    limit.startTime = curTime;
		    limit.cnt = 0;
		    limit.create();
		}

		if (curTime.getTime() - limit.startTime.getTime() > dt) {
		    limit.startTime = curTime;    
		    limit.cnt = 0;
		}

		limit.cnt = limit.cnt.longValue() + 1;
		if (limit.cnt.longValue() >= subscription.limitCnt.longValue() + 1) {
		    if (limit.cnt.longValue() == subscription.limitCnt.longValue() + 1) {        
		        final Str title = Str.format("Message limit has been reached for the subscription '%d', user '%s'", subscription.id, user.name);
		        final Int eventId = Arte::Trace.putAndFlush(Arte::EventSeverity:Warning, title, Arte::EventSource:AppSysEventNotify, curTime.getTime());
		        final Str address = getAddress(subscription, user);
		        final Str subjectTemplate = subscription.limitMessSubjectTemplate;
		        final Str bodyTemplate = subscription.limitMessBodyTemplate;
		        if (Utils::String.isBlank(address)) {
		            return false;
		        }
		        
		        Str contextStr = null;
		        if (EventNotifier.getNeedContext(subscription) && eventId != null) {
		            PersoComm.Db::GetEventContextStrStmt proc = PersoComm.Db::GetEventContextStrStmt.execute(curTime, eventId);
		            contextStr = proc.contextsStr;
		        }
		    
		        final Str subject = EventNotifier.getSubject(subjectTemplate, eventId, title, Arte::EventSeverity:Warning, Arte::EventSource:AppSysEventNotify.getTitle(), contextStr, curTime, new DateTime(limit.startTime.getTime() + dt));
		        final Str body = EventNotifier.getBody(bodyTemplate, eventId, title, Arte::EventSeverity:Warning, Arte::EventSource:AppSysEventNotify.getTitle(), contextStr, curTime, new DateTime(limit.startTime.getTime() + dt));
		                
		        // notify        
		        OutMessage msg = new OutMessage();
		        msg.init();
		        msg.address = address;
		        msg.subject = subject;
		        msg.body = body;
		        msg.importance = Importance:Normal;
		        msg.sourceEntityGuid = sourceEntityGuid;
		        msg.sourcePid = Str.valueOf(subscription.id);
		        msg.createTime = curTime;
		        msg.dueTime = curTime;
		        msg.expireTime = null;
		        msg.channelKind = subscription.channelKind;
		        msg.routingKey = subscription.routingKey;
		        msg.histMode = subscription.toStoreInHist == true ? HistMode:Audit : HistMode:DontStore;
		    
		        try {
		            msg.create();
		        } catch(Exceptions::DatabaseError e) {
		            Arte::Trace.error(Str.format("Error creating a limit violation message for the subscription '%d', address '%s'", subscription.id, msg.address) + "\n" + e.getMessage(), Arte::EventSource:AppSysEventNotify);
		            msg.discard();
		        }        
		    }
		    return false;
		}

		return true;
	}

	/*Radix::PersoComm::EventNotifier:getAddress-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::EventNotifier:getAddress")
	private static  Str getAddress (org.radixware.ads.PersoComm.server.EventSubscription subscription, org.radixware.ads.Acs.server.User user) {
		final Str address;
		switch(subscription.channelKind) {
		    case ChannelKind:Email:
		        address = user.email;
		        break;
		    case ChannelKind:Sms:
		        address = user.mobilePhone;
		        break;
		    default:
		        throw new AppError("Unsupported channel kind " + subscription.channelKind.getTitle());
		}
		return address;
	}


}

/* Radix::PersoComm::EventNotifier - Server Meta*/

/*Radix::PersoComm::EventNotifier-Server Dynamic Class*/

package org.radixware.ads.PersoComm.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EventNotifier_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcGC7JLQAWV3OBDCLVAALOMT5GDM"),"EventNotifier",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::PersoComm::EventNotifier:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::PersoComm::EventNotifier:usersBySubscription-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4XQNRD36DRFCTJY6BLUFLNRPNQ"),"usersBySubscription",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::EventNotifier:subscriptions-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGDTAXDXBTNASZEM73YMN4D7XW4"),"subscriptions",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::PersoComm::EventNotifier:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthK7LFVLJTV3OBDCLVAALOMT5GDM"),"createMessages",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("subscription",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHHGPIIONTJDJNJIJX42IQIPA6A")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("event",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7Z5NNI2LBVCPNNAVL5BN57YQZE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("subject",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGBIPQJWAIVGZ7PF3YI5EBJXMS4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("body",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVGU5G5BI4VE6HNQBMRHOZ22RQU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("importance",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVYI5LKPN5FFLBCFV2XZ7I6RR6Y")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("dueTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSSECGCBVBFC3TOI7CRULD25I5I")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("expTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUAO52C23XNBGPLJHCP3EBZFZIM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sourceEntityGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4R5UUOVDQNFG3AE3666IY6BY7A"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKDZ3HSIWV3OBDCLVAALOMT5GDM"),"exec",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprS3WYITA24JFQ5N2J4HJ4VO7X6I")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZGFGBRYHV5DGDKP2WMOJYNYJBM"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLYRGCU3WT5BSFLT5M7VYSZUQPI"),"getUsers",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("subscriptionId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprE6C5J7C5YRGOBJUMIPG6RKGS5I"))
								},org.radixware.kernel.common.enums.EValType.ARR_REF),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7RWVDMAVXFGSPJASA5ODINKXOI"),"do",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprF27TFIZ7EFC2HN6HCZZSPXTMCI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr76UPJZOUABEYZJ2CRQ5MNSMFEM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sourceEntityGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2YJP5DD2QREM7ETSOASGJBHWJU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("testing",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQXJT5LSWEFDNLKXQ4ZOL5K4X4A"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthI2BMPKMVTZHRBGOWI4BML3CDHY"),"formatString",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("format",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr72EATTFFMZDNPC3EJSOT3IF7RQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6OTENIAXEBHYJMWB5SH22NZPKU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprM5WTQLF6YNBQNCC6GHP6JWQRFU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventSeverity",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5CSSIEJMXRA4LOAPFQ533XSONE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventSourceTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUS5IVSPEJVDTFGDWCYT63QCETE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("contextString",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr26BKIRBVRJDP7EIFCJ6MPFKT64")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("time",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5KSNKUMB4ZG2DG2HLQICWPIALQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("limitTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMFCYMS6IMNHTFM5F7NZB6A6J2U"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLOQD6KB4RRHVLHUJAF2OGGCXHE"),"getNeedContext",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("subscription",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCE5YZL7QG5HYDFKAAKPOTJ3RAM"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJHLWBGXEBNB4LPM27P32O32Y6I"),"getSubject",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("template",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBK5ETJRBENEZVETUGDG7YDIJLU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr67GF5VNPFFEELJCIXM27MQYRO4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQAJTRJFDUJCCVDXEXBAVVLVCPM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventSeverity",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprROM2A5HPARD2FHYNLNTZRWNCSQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventSourceTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAOHRCRL6BNDRTPQYT2EAWKPM2Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("contextString",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSQKB57VLNNAKPI454KDMPKKLGE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("time",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprF4OLZV5EZZDA5BAJLHUWCYPL4U")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("limitTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGTB6XDTYGJFKBA53EDQ6CHY4BM"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthG7NI6WTR35F3BH4LLZZ7N7IF5M"),"getBody",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("template",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNUNW5AISV5HJVBDWDUCI4ZXJYY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWKSY7ZWDOZDU7AAGBHZDA7QKI4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprU6RCMRLNRJDPLB4SJSSOR2V2WA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventSeverity",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPBGEW6SBTZCVRKSKAJ4PXA5BNM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventSourceTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOZIEXVWTTRF4ZMAHG7EVTAFUFM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("contextString",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWYMQJFJMS5ECJFNI7QYM6ABEOE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("time",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWMPKNAUIONAX5NZY6D2S45HBA4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("limitTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6K6GMKZMHBABZIKCAPREJEJ4TE"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDVU3PQN7NVFZLJJ5ISTMDKVOQI"),"exec",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJNMHPHZSPVHF3KFVVT7YB5W2DQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprR4SXYPU6SRB5VL3GGR2VR4CGSM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sourceEntityGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW6BCSZXZCVAUHB6GQTG3V2AZGE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("testing",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4UBZX36TDFCJFGU7BZ3XRQZYSU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGUDOLFFIURHH5G4OZ2CUBOMYKE"),"EventNotifier",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPMY3VGU57BFBNOM4UXJ2RBMHCM"),"processEvent",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("event",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYZCL7OGHPVFYVNHEGBFRBZTR6A")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sourceEntityGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOZYMNYJDLJAQTH6QPJIIPZC6JQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("testing",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7IWTOZ5XOFBG7G4HGZMDZTGQIY"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth53CAEXCT6BHXVEBV2LXR5NGZTE"),"checkLimit",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("subscription",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTJCBITJ2OZAYBIDVV3W4K4G6HU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("event",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRCR525BLH5DYNLHJRMHPDAINTQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("user",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVIHQ2D32BFCNDO5DBISNLFIWTI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sourceEntityGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLQSQSX67KVEQLASJBHZ4OVEXO4"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7UTQ36CNOFH7VAWVBY3L7TSGHE"),"getAddress",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("subscription",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprST6NLXDGOVFFLJOZ563RF3X3PY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("user",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprI53MPUSH3VBIFPR45DP7VONKJE"))
								},org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
}

/* Radix::PersoComm::EventNotifier - Localizing Bundle */
package org.radixware.ads.PersoComm.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EventNotifier - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Messages generation completed. Records processed: %s, messages generated: %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Генерация сообщений завершена. Записей обработано: %s, сообщений сгенерировано: %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls64Q25VPMYBDFZANAAFW4IRYFWE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class implements the mechanism of notifying customers of system events.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс, реализующий механизм уведомления клиентов о системных событиях.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA26T7ARXJVDS3MRZSJN7PSJHQE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Determines whether the user needs the context of the event.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Определяет, требуется ли пользователю контекст события.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBEW57LCJQJGSVG7ZQCVG6W4E24"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Starts the process of generating messages for a certain period and sending these messages to the customer.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запускает процесс генерации сообщений за период и отправки их клиенту.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCNRD62TGRZA5JJNGWKY36NEKFI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Formats the message string according to the format.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Форматирование строки сообщения в соответствии с форматом.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDI4CGEVU3BGVLGB66PBHWOHW3M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Checks whether the limit on the messages for a certain user is violated and, if the limit is violated, displays the message that the subscription limit is reached.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проверяет не истек ли лимит сообщений для конкретного пользователя и, если истек, выводит сообщение об исчерпании лимита по подписке.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsERK5QLXWTBBGZOJNFELNVXSSDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Generating messages for period: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Генерация сообщений для периода: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF7SKCZWLZZC5REOSCHKMDVA3PA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Handles the events and identifies the parameters for message generation.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обрабатывает события и выделяет параметры для генерации сообщения.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHABFZLTOCBHEDCD6LF3KH53T6A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the message body from the system event.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Получает тело сообщения из системного события.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI54IE75KOFDVDBOCI6KJQGASOM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Creates messages for subscribers to system events.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Создает сообщения для подписчиков на системные события.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsINSVH5KQSZDCDEDJ5UTBWZGEQM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Building subscribers list");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Построение списка подписчиков");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQGTZ5R3UQJAZXJQDH2RH5OMYUM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Notifies the users of system events over the specified period.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выполняет уведомление пользователей о системных событиях за указанный период времени.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQH6XSAEXLBB5FHKZV7SOXR4BTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the users subscribed to the event.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Получает пользователей, которые подписаны на событие.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQKAV556B3RFJ3DZFJV4COHWMDE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error creating a limit violation message for the subscription \'%d\', address \'%s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка создания сообщения исчерпывания лимита по подписке \'%d\' на адрес \'%s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsURPQ7OT7INF3PFCHOE7LPISCLA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"See Radix::ADS::PersoComm::EventNotifier:exec(DateTime prevTime, DateTime curTime)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"См. Radix::ADS::PersoComm::EventNotifier:exec(DateTime prevTime, DateTime curTime)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVZTLGVE5VFGT3DRCH2FNPEKQTI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message limit has been reached for the subscription \'%d\', user \'%s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Исчерпан лимит сообщений по подписке \'%d\' для пользователя \'%s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX4DOERYSOBBYVFF7AAOJHS6XDI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error creating a message for subscription \'%d\', address \'%s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка создания сообщения по подписке \'%d\' на адрес \'%s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX7NNL3IRWLOBDCLYAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error generating messages for event subscription \'%1\': %2");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при генерации сообщений для подписки на события \'%1\': %2");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ2DPM2ZPXNDCFJRFOVP3XMTIQU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App.SysEventNotify",null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the message subject from the system event.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Получает тему сообщения из системного события.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ5CZIK2NQ5GO5J6WGMIGHUD2OE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(EventNotifier - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcGC7JLQAWV3OBDCLVAALOMT5GDM"),"EventNotifier - Localizing Bundle",$$$items$$$);
}
