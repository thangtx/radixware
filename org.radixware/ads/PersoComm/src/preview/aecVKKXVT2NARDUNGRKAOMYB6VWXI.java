
/* Radix::PersoComm::NotificationSender - Server Executable*/

/*Radix::PersoComm::NotificationSender-Entity Class*/

package org.radixware.ads.PersoComm.server;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.MessageFormat;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender")
public final published class NotificationSender  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	static class Match {
	    final int start;
	    final int end;
	    final String group;
	    final String param;
	    String formatted;
	    Match(int start, int end, String group, String param) {
	        this.start = start;
	        this.end = end;
	        this.group = group;
	        this.param = param;
	    }
	}

	final Pattern pattern = Pattern.compile("\\$\\{([a-zA-Z]+)(,(number|date|time)(,.+?)?)?}");
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return NotificationSender_mi.rdxMeta;}

	/*Radix::PersoComm::NotificationSender:Nested classes-Nested Classes*/

	/*Radix::PersoComm::NotificationSender:Properties-Properties*/

	/*Radix::PersoComm::NotificationSender:argsPreparationFunc-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:argsPreparationFunc")
	public  org.radixware.ads.PersoComm.server.UserFunc.NotificationSender.ArgsPreparation getArgsPreparationFunc() {
		return argsPreparationFunc;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:argsPreparationFunc")
	public   void setArgsPreparationFunc(org.radixware.ads.PersoComm.server.UserFunc.NotificationSender.ArgsPreparation val) {
		argsPreparationFunc = val;
	}

	/*Radix::PersoComm::NotificationSender:bodyTemplate-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:bodyTemplate")
	public published  Str getBodyTemplate() {
		return bodyTemplate;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:bodyTemplate")
	public published   void setBodyTemplate(Str val) {
		bodyTemplate = val;
	}

	/*Radix::PersoComm::NotificationSender:channelKind-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:channelKind")
	public published  org.radixware.ads.PersoComm.common.ChannelKind getChannelKind() {
		return channelKind;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:channelKind")
	public published   void setChannelKind(org.radixware.ads.PersoComm.common.ChannelKind val) {
		channelKind = val;
	}

	/*Radix::PersoComm::NotificationSender:extGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:extGuid")
	public published  Str getExtGuid() {
		return extGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:extGuid")
	public published   void setExtGuid(Str val) {
		extGuid = val;
	}

	/*Radix::PersoComm::NotificationSender:histMode-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:histMode")
	public published  org.radixware.kernel.common.enums.EPersoCommHistMode getHistMode() {
		return histMode;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:histMode")
	public published   void setHistMode(org.radixware.kernel.common.enums.EPersoCommHistMode val) {
		histMode = val;
	}

	/*Radix::PersoComm::NotificationSender:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:id")
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:id")
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::PersoComm::NotificationSender:messCorrectionFunc-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:messCorrectionFunc")
	public  org.radixware.ads.PersoComm.server.UserFunc.NotificationSender.MessCorrection getMessCorrectionFunc() {
		return messCorrectionFunc;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:messCorrectionFunc")
	public   void setMessCorrectionFunc(org.radixware.ads.PersoComm.server.UserFunc.NotificationSender.MessCorrection val) {
		messCorrectionFunc = val;
	}

	/*Radix::PersoComm::NotificationSender:rid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:rid")
	public published  Str getRid() {
		return rid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:rid")
	public published   void setRid(Str val) {
		rid = val;
	}

	/*Radix::PersoComm::NotificationSender:routingKey-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:routingKey")
	public published  Str getRoutingKey() {
		return routingKey;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:routingKey")
	public published   void setRoutingKey(Str val) {
		routingKey = val;
	}

	/*Radix::PersoComm::NotificationSender:storeAttachInHist-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:storeAttachInHist")
	public published  Bool getStoreAttachInHist() {
		return storeAttachInHist;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:storeAttachInHist")
	public published   void setStoreAttachInHist(Bool val) {
		storeAttachInHist = val;
	}

	/*Radix::PersoComm::NotificationSender:subjectTemplate-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:subjectTemplate")
	public published  Str getSubjectTemplate() {
		return subjectTemplate;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:subjectTemplate")
	public published   void setSubjectTemplate(Str val) {
		subjectTemplate = val;
	}

	/*Radix::PersoComm::NotificationSender:title-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:title")
	public published  Str getTitle() {
		return title;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:title")
	public published   void setTitle(Str val) {
		title = val;
	}

	/*Radix::PersoComm::NotificationSender:userGroup-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:userGroup")
	public published  org.radixware.ads.Acs.server.UserGroup getUserGroup() {
		return userGroup;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:userGroup")
	public published   void setUserGroup(org.radixware.ads.Acs.server.UserGroup val) {
		userGroup = val;
	}

	/*Radix::PersoComm::NotificationSender:userGroupName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:userGroupName")
	public published  Str getUserGroupName() {
		return userGroupName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:userGroupName")
	public published   void setUserGroupName(Str val) {
		userGroupName = val;
	}

















































































































	/*Radix::PersoComm::NotificationSender:Methods-Methods*/

	/*Radix::PersoComm::NotificationSender:afterInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:afterInit")
	protected published  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		super.afterInit(src, phase);
		if (extGuid == null) {
		    extGuid = Arte::Arte.generateGuid();
		} else if (src != null && ((NotificationSender)src).extGuid == extGuid) {
		    extGuid = Arte::Arte.generateGuid();
		}

	}

	/*Radix::PersoComm::NotificationSender:debug-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:debug")
	private final  void debug (Str message) {
		Arte::Trace.debug(message, Arte::EventSource:NotificationSender);
	}

	/*Radix::PersoComm::NotificationSender:fillTemplate-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:fillTemplate")
	private final  Str fillTemplate (Str template, java.util.Map<Str,java.lang.Object> args) {
		if (Utils::String.isBlank(template)) {
		    return template;
		}

		final Matcher matcher = pattern.matcher(template);
		final List<Match> matches = new ArrayList<>();
		while (matcher.find()) {
		    final Match m = new Match(matcher.start(), matcher.end(), matcher.group(), matcher.group(1));
		    if (args.containsKey(m.param)) {
		        final String formatString = m.group.replace(m.param, "0").substring(1);
		        final MessageFormat formatter = new MessageFormat(formatString);
		        final Object[] objs = { args.get(m.param) };
		        final String formatted = formatter.format(objs);
		        m.formatted = formatted;
		    } else {
		        throw new IllegalStateException("Template parameter '" + m.param + "' of group[" + matches.size() + "] - '" + m.group + "' - not found in arguments map");
		    }
		    matches.add(m);
		}

		final StringBuilder sb = new StringBuilder();
		int prevEnd = 0;
		for (int i = 0; i < matches.size(); ++i) {
		    final Match m = matches.get(i);
		    sb.append(template.substring(prevEnd, m.start));
		    sb.append(m.formatted);
		    prevEnd = m.end;
		}
		sb.append(template.substring(prevEnd));

		return sb.toString();

	}

	/*Radix::PersoComm::NotificationSender:getUserAddress-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:getUserAddress")
	private final  Str getUserAddress (org.radixware.ads.Acs.server.User user) {
		final Str address = this.channelKind == ChannelKind:Email ? user.email : user.mobilePhone;
		if (Utils::String.isBlank(address)) {
		    if (this.messCorrectionFunc == null) {
		        throw new IllegalArgumentException("User address is blank and message correction function is not set: user name = " + user.name);
		    }
		}
		return address;

	}

	/*Radix::PersoComm::NotificationSender:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:loadByPK")
	public static published  org.radixware.ads.PersoComm.server.NotificationSender loadByPK (Int id, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(id==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC6KU45IE5ZDTJOPL2DDMLU3NX4"),id);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVKKXVT2NARDUNGRKAOMYB6VWXI"),pkValsMap);
		try{
		return (
		org.radixware.ads.PersoComm.server.NotificationSender) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::PersoComm::NotificationSender:loadByPidStr-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:loadByPidStr")
	public static published  org.radixware.ads.PersoComm.server.NotificationSender loadByPidStr (Str pidAsStr, boolean checkExistance) {
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVKKXVT2NARDUNGRKAOMYB6VWXI"),pidAsStr);
		try{
		return (
		org.radixware.ads.PersoComm.server.NotificationSender) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::PersoComm::NotificationSender:precheck-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:precheck")
	private final  void precheck (org.radixware.ads.PersoComm.server.NotificationSenderEvent event) {
		if (event == null) {
		    throw new IllegalArgumentException("Notification event is null");
		}

		if (event.entity == null) {
		    throw new IllegalArgumentException("Notification event entity is null");
		}

		int eventTargets = 0;
		eventTargets += event.userGroup != null ? 1 : 0;
		eventTargets += event.user != null ? 1 : 0;
		eventTargets += event.recipients != null ? 1 : 0;
		if (eventTargets > 1) {
		    throw new IllegalArgumentException("Multiple notification event targets are defined");
		}

		if (eventTargets == 0 && this.userGroup == null) {
		    throw new IllegalArgumentException("Neither notification event targets not notification sender user group are defined");
		}

		if (event.recipients != null) {
		    if (event.recipients.isEmpty()) {
		        throw new IllegalArgumentException("Notification event recipients list is empty");
		    }
		    for (int i = 0; i < event.recipients.size(); ++i) {
		        if (event.recipients.get(i).First == null) {
		            throw new IllegalArgumentException("Notification event recipient[" + i + "] channel kind is null");
		        } else if (Utils::String.isBlank(event.recipients.get(i).Second)) {
		            throw new IllegalArgumentException("Notification event recipient[" + i + "] address is blank");
		        }
		    }
		}

		if (this.channelKind == null && event.recipients == null) {
		    throw new IllegalArgumentException("Notification sender channel kind is null but required for notifications by user or group");
		}

		if (event.attachments != null) {
		    int seqSetCount = 0;
		    for (int i = 0; i < event.attachments.size(); ++i) {
		        final Attachment atch = event.attachments.get(i);
		        if (atch == null) {
		            throw new IllegalArgumentException("attachments[" + i + "] is null");
		        } else if (!atch.isNewObject()) {
		            throw new IllegalArgumentException("attachments[" + i + "] is not new object");
		        } else if (atch.messId != null) {
		            throw new IllegalArgumentException("attachments[" + i + "].messId is not null");
		        } else if (atch.seq != null) {
		            seqSetCount++;
		        }
		    }
		    if (seqSetCount != 0 && seqSetCount != event.attachments.size()) {
		        throw new IllegalArgumentException("Attachments have both null are not null seq fields at the same time");
		    }
		}

	}

	/*Radix::PersoComm::NotificationSender:prepareRecipients-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:prepareRecipients")
	private final  java.util.List<org.radixware.ads.PersoComm.server.NotificationSenderRecipient> prepareRecipients (org.radixware.ads.PersoComm.server.NotificationSenderEvent event) {
		final List<NotificationSenderRecipient> recipients = new ArrayList<>();
		if (event.recipients != null) {
		    for (Utils::Pair<ChannelKind, Str> rec: event.recipients) {
		        recipients.add(new NotificationSenderRecipient(rec.First, rec.Second, null));
		    }
		} else if (event.user != null) {
		    recipients.add(new NotificationSenderRecipient(this.channelKind, getUserAddress(event.user), event.user));
		} else {
		    final Acs::UserGroup userGroup = event.userGroup != null ? event.userGroup : this.userGroup;
		    try (PersoComm.Db::UsersOfUserGroupCursor cur = PersoComm.Db::UsersOfUserGroupCursor.open(userGroup.name)) {
		        while (cur.next()) {
		            recipients.add(new NotificationSenderRecipient(this.channelKind, getUserAddress(cur.user), cur.user));
		        }
		    }
		    if (recipients.isEmpty()) {
		        throw new IllegalArgumentException("Empty user group: " + this.userGroupName);
		    }
		}

		return recipients;
	}

	/*Radix::PersoComm::NotificationSender:send-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:send")
	public published  void send (org.radixware.ads.PersoComm.server.NotificationSenderEvent event) {
		precheck(event);

		final List<NotificationSenderRecipient> recipients = prepareRecipients(event);

		boolean reuseAttach = true;
		for (NotificationSenderRecipient recipient: recipients) {

		    OutMessage mess = new OutMessage();
		    mess.init();
		    mess.channelKind = recipient.channelKind;
		    mess.address = recipient.address;
		    mess.dueTime = Utils::Timing.getCurrentLocalDate();
		    
		    mess.routingKey = this.routingKey;
		    mess.histMode = this.histMode;
		    mess.storeAttachInHist = this.storeAttachInHist;

		    mess.sourceEntityGuid = event.entity.getClassDefinitionId().toString();
		    mess.sourcePid = event.entity.Pid.toString();

		    mess.sendCallbackClassName = event.sendCallbackClassName;
		    mess.sendCallbackMethodName = event.sendCallbackMethodName;
		    mess.sendCallbackData = event.sendCallbackData;

		    mess.deliveryCallbackClassName = event.deliveryCallbackClassName;
		    mess.deliveryCallbackMethodName = event.deliveryCallbackMethodName;
		    mess.deliveryCallbackData = event.deliveryCallbackData;
		    mess.deliveryTimeout = event.deliveryTimeoutSeconds;
		    
		    
		    final Map<Str, Object> args = argsPreparationFunc == null ? event.args
		            : java.util.Collections.unmodifiableMap(argsPreparationFunc.prepareArgs(event, mess, recipient.user));
		    final String subject = fillTemplate(this.subjectTemplate, args);
		    final String body = fillTemplate(this.bodyTemplate, args);
		    mess.subject = subject;
		    mess.body = body;
		    debug("messageId: " + mess.id);
		    debug("subject: " + subject);
		    debug("body: " + body);
		    
		    boolean toSend = this.messCorrectionFunc == null || this.messCorrectionFunc.correctMess(event, mess, recipient.user);
		    if (toSend) {
		        if (Utils::String.isBlank(mess.address)) {
		            //mess.();
		            throw new IllegalArgumentException("Address is not defined after message correction function invocation");
		        }
		        mess.create();
		        if (event.attachments != null) {
		            for (int i = 0; i < event.attachments.size(); ++i) {
		                final Attachment atch = event.attachments.get(i);
		                final Int seq = Utils::Nvl.get(atch.seq, i);
		                final Attachment newAtch = reuseAttach ? atch : (Attachment)Arte::Arte.newObject(idof[Attachment]);
		                if (!newAtch.isInited()) {
		                    newAtch.init(null, atch);
		                }
		                newAtch.messId = mess.id;
		                newAtch.seq = seq;
		                debug("attachment.seq = " + seq);
		                newAtch.create();
		            }
		            reuseAttach = false;
		        }
		    } else {
		        debug("toSend == false");
		        mess.discard();
		    }
		    
		}

	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::PersoComm::NotificationSender - Server Meta*/

/*Radix::PersoComm::NotificationSender-Entity Class*/

package org.radixware.ads.PersoComm.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class NotificationSender_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),"NotificationSender",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3IITGJB6NBG7HOPLPFQCCAPF2Q"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::PersoComm::NotificationSender:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
							/*Owner Class Name*/
							"NotificationSender",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3IITGJB6NBG7HOPLPFQCCAPF2Q"),
							/*Property presentations*/

							/*Radix::PersoComm::NotificationSender:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::PersoComm::NotificationSender:argsPreparationFunc:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru5NLSZQUQ5RGO5HJHCSBFUNKSHQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::NotificationSender:bodyTemplate:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLB4DFGLQ7RD6TK65SWSARQVKGA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::NotificationSender:channelKind:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPH5JTQND3VDZTJJ7Q46PRKF7JE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::NotificationSender:extGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDDQIPTWYDVGT3CSRPXZUPZNIZM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::NotificationSender:histMode:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEI7GIMO4KFAPRAU4HAVU6BQ3PI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::NotificationSender:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC6KU45IE5ZDTJOPL2DDMLU3NX4"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::NotificationSender:messCorrectionFunc:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruRZE4ECEYPNA2LARKXY4RZJTG4I"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::NotificationSender:rid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFOGZWULAQBCDDOMGLD2JGDR4TU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::NotificationSender:routingKey:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTGS3PTFC5RGSLAFMQFUSXEJUOI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::NotificationSender:storeAttachInHist:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHDP4Q3IHNRGLZFFT6IRB3M3G24"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::NotificationSender:subjectTemplate:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYRJIL7ZCM5AK5LDULILF6B2FDA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::NotificationSender:title:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colARKLG7SWKREHPGQYQWOU5XVGJI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::PersoComm::NotificationSender:userGroup:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ3I7LMAFMJFC5FIRM4VXAPBBTM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::PersoComm::NotificationSender:userGroupName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXECQRSZBN5F27GJKI242EGT7NA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
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
									/*Radix::PersoComm::NotificationSender:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprX6LAA6CFYVAA5EYOMFEAA75RH4"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::PersoComm::NotificationSender:General:Children-Explorer Items*/
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
									/*Radix::PersoComm::NotificationSender:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprDSRXSN5FIBHO3LW2WUQD3LKO7I"),"General",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC6KU45IE5ZDTJOPL2DDMLU3NX4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colARKLG7SWKREHPGQYQWOU5XVGJI"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPH5JTQND3VDZTJJ7Q46PRKF7JE"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTGS3PTFC5RGSLAFMQFUSXEJUOI"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(null,false,new org.radixware.kernel.common.types.Id[]{},false,null,false,new org.radixware.kernel.common.types.Id[]{},false,false,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprX6LAA6CFYVAA5EYOMFEAA75RH4")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprX6LAA6CFYVAA5EYOMFEAA75RH4")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(32,null,null,null),null)
							},
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::PersoComm::NotificationSender:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),null,null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVKKXVT2NARDUNGRKAOMYB6VWXI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::PersoComm::NotificationSender:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::PersoComm::NotificationSender:argsPreparationFunc-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru5NLSZQUQ5RGO5HJHCSBFUNKSHQ"),"argsPreparationFunc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVIMN6HV37VFA5LVYQQDNNRZDQE"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVKKXVT2NARDUNGRKAOMYB6VWXI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6IPBFCSAYJAM5LVYGALSEZ5XXY"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSender:bodyTemplate-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLB4DFGLQ7RD6TK65SWSARQVKGA"),"bodyTemplate",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZSDWR27WJZHHZAQE4GQRHCR3DQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSender:channelKind-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPH5JTQND3VDZTJJ7Q46PRKF7JE"),"channelKind",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNN65IIDYBNC6VOEZANAHZZP4HA"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSender:extGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDDQIPTWYDVGT3CSRPXZUPZNIZM"),"extGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSender:histMode-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEI7GIMO4KFAPRAU4HAVU6BQ3PI"),"histMode",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZTHURWANMFHK5OPGZZRMZYLXFQ"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsVQOKSQR4RVAMPBGDQRMO7W7JBQ"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSender:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC6KU45IE5ZDTJOPL2DDMLU3NX4"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7FPEZQNJ7ZHLFO4QFI6WGMOJGA"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSender:messCorrectionFunc-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruRZE4ECEYPNA2LARKXY4RZJTG4I"),"messCorrectionFunc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRPWNAIRKU5BDZF7745YKI67CYI"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVKKXVT2NARDUNGRKAOMYB6VWXI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclGQXVD4MS2JAE7BDB6ZUUXIYQXA"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSender:rid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFOGZWULAQBCDDOMGLD2JGDR4TU"),"rid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFPIVWM466ZCUXNM5SRNYSXXWXI"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSender:routingKey-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTGS3PTFC5RGSLAFMQFUSXEJUOI"),"routingKey",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls32LXJJIQHBCA7LKKUOJA7LRGP4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSender:storeAttachInHist-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHDP4Q3IHNRGLZFFT6IRB3M3G24"),"storeAttachInHist",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLV2KKNMOLBB6JCNRIQ7QOUOJXM"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSender:subjectTemplate-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYRJIL7ZCM5AK5LDULILF6B2FDA"),"subjectTemplate",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUZPSXQCDBRH3FF6WOSVIL4D2U4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSender:title-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colARKLG7SWKREHPGQYQWOU5XVGJI"),"title",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYEAWHZAIWZE2BIOJZXDHH6MCLY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSender:userGroup-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ3I7LMAFMJFC5FIRM4VXAPBBTM"),"userGroup",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRJ4TNSEACVF23GXHZZO5TZUNRU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refPBH6ZHX4X5GFFLXY3NOHK22EVI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::PersoComm::NotificationSender:userGroupName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXECQRSZBN5F27GJKI242EGT7NA"),"userGroupName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR2LGYWYYGJHSHMN4AKUZDOKVZM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::PersoComm::NotificationSender:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHJTNSTN4G5H2HOCW5OBHWDNWWU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZLY63VWBKZFBVA3T5A6USHE7QM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthALQ2ARHCOVEBPEFJRFLLG6LV4E"),"debug",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("message",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4YZFD3Y6PZCILOUQCE7FGBDHQE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSF7EJP77DVG3HJBZZN2BDKLSMA"),"fillTemplate",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("template",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXODVMUITWRDHBMNTFS7HRUVCT4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("args",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPQRF5J2OEJCBJIG5CT4VTYYIEU"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3ISX7K35FZHTTF5FSQZVZRCOJY"),"getUserAddress",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("user",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPRNJ55YALFDOHC5MJ626EUE76I"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprC6KU45IE5ZDTJOPL2DDMLU3NX4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPidAsStr__________________")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYS3E75O6QBHUBNXPME4B55G24U"),"precheck",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("event",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2U5VJW3TLJA5HMMESHX4GPUYOU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAVHEP4O5MNFC5KDBTG5XWXBX2M"),"prepareRecipients",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("event",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNFXO6U3MUJGFHIEWOC6OWUWY7I"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOXYQ4Q3PJVBU7JYKK5DUPTZS3A"),"send",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("event",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprILOUKGPJVNB73IPDFY45WFJKDE"))
								},null)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::PersoComm::NotificationSender - Desktop Executable*/

/*Radix::PersoComm::NotificationSender-Entity Class*/

package org.radixware.ads.PersoComm.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender")
public interface NotificationSender {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.PersoComm.explorer.NotificationSender.NotificationSender_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.PersoComm.explorer.NotificationSender.NotificationSender_DefaultModel )  super.getEntity(i);}
	}




























































































	/*Radix::PersoComm::NotificationSender:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::PersoComm::NotificationSender:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::PersoComm::NotificationSender:extGuid:extGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:extGuid:extGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:extGuid:extGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtGuid getExtGuid();
	/*Radix::PersoComm::NotificationSender:histMode:histMode-Presentation Property*/


	public class HistMode extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public HistMode(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EPersoCommHistMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPersoCommHistMode ? (org.radixware.kernel.common.enums.EPersoCommHistMode)x : org.radixware.kernel.common.enums.EPersoCommHistMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EPersoCommHistMode> getValClass(){
			return org.radixware.kernel.common.enums.EPersoCommHistMode.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EPersoCommHistMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPersoCommHistMode ? (org.radixware.kernel.common.enums.EPersoCommHistMode)x : org.radixware.kernel.common.enums.EPersoCommHistMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:histMode:histMode")
		public  org.radixware.kernel.common.enums.EPersoCommHistMode getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:histMode:histMode")
		public   void setValue(org.radixware.kernel.common.enums.EPersoCommHistMode val) {
			Value = val;
		}
	}
	public HistMode getHistMode();
	/*Radix::PersoComm::NotificationSender:rid:rid-Presentation Property*/


	public class Rid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Rid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:rid:rid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:rid:rid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Rid getRid();
	/*Radix::PersoComm::NotificationSender:storeAttachInHist:storeAttachInHist-Presentation Property*/


	public class StoreAttachInHist extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public StoreAttachInHist(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:storeAttachInHist:storeAttachInHist")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:storeAttachInHist:storeAttachInHist")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public StoreAttachInHist getStoreAttachInHist();
	/*Radix::PersoComm::NotificationSender:bodyTemplate:bodyTemplate-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:bodyTemplate:bodyTemplate")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:bodyTemplate:bodyTemplate")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public BodyTemplate getBodyTemplate();
	/*Radix::PersoComm::NotificationSender:channelKind:channelKind-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:channelKind:channelKind")
		public  org.radixware.ads.PersoComm.common.ChannelKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:channelKind:channelKind")
		public   void setValue(org.radixware.ads.PersoComm.common.ChannelKind val) {
			Value = val;
		}
	}
	public ChannelKind getChannelKind();
	/*Radix::PersoComm::NotificationSender:routingKey:routingKey-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:routingKey:routingKey")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:routingKey:routingKey")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoutingKey getRoutingKey();
	/*Radix::PersoComm::NotificationSender:userGroupName:userGroupName-Presentation Property*/


	public class UserGroupName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public UserGroupName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:userGroupName:userGroupName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:userGroupName:userGroupName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UserGroupName getUserGroupName();
	/*Radix::PersoComm::NotificationSender:subjectTemplate:subjectTemplate-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:subjectTemplate:subjectTemplate")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:subjectTemplate:subjectTemplate")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SubjectTemplate getSubjectTemplate();
	/*Radix::PersoComm::NotificationSender:userGroup:userGroup-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:userGroup:userGroup")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:userGroup:userGroup")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public UserGroup getUserGroup();
	/*Radix::PersoComm::NotificationSender:argsPreparationFunc:argsPreparationFunc-Presentation Property*/


	public class ArgsPreparationFunc extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public ArgsPreparationFunc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:argsPreparationFunc:argsPreparationFunc")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:argsPreparationFunc:argsPreparationFunc")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ArgsPreparationFunc getArgsPreparationFunc();
	/*Radix::PersoComm::NotificationSender:messCorrectionFunc:messCorrectionFunc-Presentation Property*/


	public class MessCorrectionFunc extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public MessCorrectionFunc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:messCorrectionFunc:messCorrectionFunc")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:messCorrectionFunc:messCorrectionFunc")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public MessCorrectionFunc getMessCorrectionFunc();


}

/* Radix::PersoComm::NotificationSender - Desktop Meta*/

/*Radix::PersoComm::NotificationSender-Entity Class*/

package org.radixware.ads.PersoComm.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class NotificationSender_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::PersoComm::NotificationSender:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
			"Radix::PersoComm::NotificationSender",
			null,
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3IITGJB6NBG7HOPLPFQCCAPF2Q"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZOQQAQQJBNGF5IOD6R27PSR5TI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3IITGJB6NBG7HOPLPFQCCAPF2Q"),0,

			/*Radix::PersoComm::NotificationSender:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::PersoComm::NotificationSender:argsPreparationFunc:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru5NLSZQUQ5RGO5HJHCSBFUNKSHQ"),
						"argsPreparationFunc",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6IPBFCSAYJAM5LVYGALSEZ5XXY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						null,
						0L,
						0L,false,false),

					/*Radix::PersoComm::NotificationSender:bodyTemplate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLB4DFGLQ7RD6TK65SWSARQVKGA"),
						"bodyTemplate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZSDWR27WJZHHZAQE4GQRHCR3DQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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

						/*Radix::PersoComm::NotificationSender:bodyTemplate:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::NotificationSender:channelKind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPH5JTQND3VDZTJJ7Q46PRKF7JE"),
						"channelKind",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNN65IIDYBNC6VOEZANAHZZP4HA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::NotificationSender:channelKind:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.INCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aciL2BEJTYZVPORDJHCAANE2UAFXA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aciLQ3IFOMEAZCUJNQW2WSFKVSL3E")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::NotificationSender:extGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDDQIPTWYDVGT3CSRPXZUPZNIZM"),
						"extGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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

						/*Radix::PersoComm::NotificationSender:extGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::NotificationSender:histMode:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEI7GIMO4KFAPRAU4HAVU6BQ3PI"),
						"histMode",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZTHURWANMFHK5OPGZZRMZYLXFQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsVQOKSQR4RVAMPBGDQRMO7W7JBQ"),
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

						/*Radix::PersoComm::NotificationSender:histMode:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsVQOKSQR4RVAMPBGDQRMO7W7JBQ"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::NotificationSender:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC6KU45IE5ZDTJOPL2DDMLU3NX4"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7FPEZQNJ7ZHLFO4QFI6WGMOJGA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::NotificationSender:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::NotificationSender:messCorrectionFunc:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruRZE4ECEYPNA2LARKXY4RZJTG4I"),
						"messCorrectionFunc",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclGQXVD4MS2JAE7BDB6ZUUXIYQXA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						null,
						0L,
						0L,false,false),

					/*Radix::PersoComm::NotificationSender:rid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFOGZWULAQBCDDOMGLD2JGDR4TU"),
						"rid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFPIVWM466ZCUXNM5SRNYSXXWXI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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

						/*Radix::PersoComm::NotificationSender:rid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::NotificationSender:routingKey:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTGS3PTFC5RGSLAFMQFUSXEJUOI"),
						"routingKey",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls32LXJJIQHBCA7LKKUOJA7LRGP4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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

						/*Radix::PersoComm::NotificationSender:routingKey:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::NotificationSender:storeAttachInHist:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHDP4Q3IHNRGLZFFT6IRB3M3G24"),
						"storeAttachInHist",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLV2KKNMOLBB6JCNRIQ7QOUOJXM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::NotificationSender:storeAttachInHist:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::NotificationSender:subjectTemplate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYRJIL7ZCM5AK5LDULILF6B2FDA"),
						"subjectTemplate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUZPSXQCDBRH3FF6WOSVIL4D2U4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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

						/*Radix::PersoComm::NotificationSender:subjectTemplate:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::NotificationSender:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colARKLG7SWKREHPGQYQWOU5XVGJI"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYEAWHZAIWZE2BIOJZXDHH6MCLY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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

						/*Radix::PersoComm::NotificationSender:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::NotificationSender:userGroup:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ3I7LMAFMJFC5FIRM4VXAPBBTM"),
						"userGroup",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRJ4TNSEACVF23GXHZZO5TZUNRU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::PersoComm::NotificationSender:userGroupName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXECQRSZBN5F27GJKI242EGT7NA"),
						"userGroupName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR2LGYWYYGJHSHMN4AKUZDOKVZM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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

						/*Radix::PersoComm::NotificationSender:userGroupName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			null,
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refPBH6ZHX4X5GFFLXY3NOHK22EVI"),"NotificationSender=>UserGroup (userGroupName=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVKKXVT2NARDUNGRKAOMYB6VWXI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colXECQRSZBN5F27GJKI242EGT7NA")},new String[]{"userGroupName"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4")},new String[]{"name"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprX6LAA6CFYVAA5EYOMFEAA75RH4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprDSRXSN5FIBHO3LW2WUQD3LKO7I")},
			false,true,false);
}

/* Radix::PersoComm::NotificationSender - Web Executable*/

/*Radix::PersoComm::NotificationSender-Entity Class*/

package org.radixware.ads.PersoComm.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender")
public interface NotificationSender {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.PersoComm.web.NotificationSender.NotificationSender_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.PersoComm.web.NotificationSender.NotificationSender_DefaultModel )  super.getEntity(i);}
	}


















































































	/*Radix::PersoComm::NotificationSender:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::PersoComm::NotificationSender:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::PersoComm::NotificationSender:extGuid:extGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:extGuid:extGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:extGuid:extGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtGuid getExtGuid();
	/*Radix::PersoComm::NotificationSender:histMode:histMode-Presentation Property*/


	public class HistMode extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public HistMode(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EPersoCommHistMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPersoCommHistMode ? (org.radixware.kernel.common.enums.EPersoCommHistMode)x : org.radixware.kernel.common.enums.EPersoCommHistMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EPersoCommHistMode> getValClass(){
			return org.radixware.kernel.common.enums.EPersoCommHistMode.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EPersoCommHistMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPersoCommHistMode ? (org.radixware.kernel.common.enums.EPersoCommHistMode)x : org.radixware.kernel.common.enums.EPersoCommHistMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:histMode:histMode")
		public  org.radixware.kernel.common.enums.EPersoCommHistMode getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:histMode:histMode")
		public   void setValue(org.radixware.kernel.common.enums.EPersoCommHistMode val) {
			Value = val;
		}
	}
	public HistMode getHistMode();
	/*Radix::PersoComm::NotificationSender:rid:rid-Presentation Property*/


	public class Rid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Rid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:rid:rid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:rid:rid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Rid getRid();
	/*Radix::PersoComm::NotificationSender:storeAttachInHist:storeAttachInHist-Presentation Property*/


	public class StoreAttachInHist extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public StoreAttachInHist(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:storeAttachInHist:storeAttachInHist")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:storeAttachInHist:storeAttachInHist")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public StoreAttachInHist getStoreAttachInHist();
	/*Radix::PersoComm::NotificationSender:bodyTemplate:bodyTemplate-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:bodyTemplate:bodyTemplate")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:bodyTemplate:bodyTemplate")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public BodyTemplate getBodyTemplate();
	/*Radix::PersoComm::NotificationSender:channelKind:channelKind-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:channelKind:channelKind")
		public  org.radixware.ads.PersoComm.common.ChannelKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:channelKind:channelKind")
		public   void setValue(org.radixware.ads.PersoComm.common.ChannelKind val) {
			Value = val;
		}
	}
	public ChannelKind getChannelKind();
	/*Radix::PersoComm::NotificationSender:routingKey:routingKey-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:routingKey:routingKey")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:routingKey:routingKey")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoutingKey getRoutingKey();
	/*Radix::PersoComm::NotificationSender:userGroupName:userGroupName-Presentation Property*/


	public class UserGroupName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public UserGroupName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:userGroupName:userGroupName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:userGroupName:userGroupName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UserGroupName getUserGroupName();
	/*Radix::PersoComm::NotificationSender:subjectTemplate:subjectTemplate-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:subjectTemplate:subjectTemplate")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:subjectTemplate:subjectTemplate")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SubjectTemplate getSubjectTemplate();
	/*Radix::PersoComm::NotificationSender:userGroup:userGroup-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:userGroup:userGroup")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::PersoComm::NotificationSender:userGroup:userGroup")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public UserGroup getUserGroup();


}

/* Radix::PersoComm::NotificationSender - Web Meta*/

/*Radix::PersoComm::NotificationSender-Entity Class*/

package org.radixware.ads.PersoComm.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class NotificationSender_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::PersoComm::NotificationSender:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
			"Radix::PersoComm::NotificationSender",
			null,
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3IITGJB6NBG7HOPLPFQCCAPF2Q"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZOQQAQQJBNGF5IOD6R27PSR5TI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3IITGJB6NBG7HOPLPFQCCAPF2Q"),0,

			/*Radix::PersoComm::NotificationSender:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::PersoComm::NotificationSender:argsPreparationFunc:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru5NLSZQUQ5RGO5HJHCSBFUNKSHQ"),
						"argsPreparationFunc",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6IPBFCSAYJAM5LVYGALSEZ5XXY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						null,
						null,
						null,
						0L,
						0L,false,false),

					/*Radix::PersoComm::NotificationSender:bodyTemplate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLB4DFGLQ7RD6TK65SWSARQVKGA"),
						"bodyTemplate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZSDWR27WJZHHZAQE4GQRHCR3DQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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

						/*Radix::PersoComm::NotificationSender:bodyTemplate:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::NotificationSender:channelKind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPH5JTQND3VDZTJJ7Q46PRKF7JE"),
						"channelKind",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNN65IIDYBNC6VOEZANAHZZP4HA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::NotificationSender:channelKind:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.INCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aciL2BEJTYZVPORDJHCAANE2UAFXA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aciLQ3IFOMEAZCUJNQW2WSFKVSL3E")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::NotificationSender:extGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDDQIPTWYDVGT3CSRPXZUPZNIZM"),
						"extGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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

						/*Radix::PersoComm::NotificationSender:extGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::NotificationSender:histMode:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEI7GIMO4KFAPRAU4HAVU6BQ3PI"),
						"histMode",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZTHURWANMFHK5OPGZZRMZYLXFQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsVQOKSQR4RVAMPBGDQRMO7W7JBQ"),
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

						/*Radix::PersoComm::NotificationSender:histMode:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsVQOKSQR4RVAMPBGDQRMO7W7JBQ"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::NotificationSender:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC6KU45IE5ZDTJOPL2DDMLU3NX4"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7FPEZQNJ7ZHLFO4QFI6WGMOJGA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::NotificationSender:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::NotificationSender:messCorrectionFunc:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruRZE4ECEYPNA2LARKXY4RZJTG4I"),
						"messCorrectionFunc",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclGQXVD4MS2JAE7BDB6ZUUXIYQXA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						null,
						null,
						null,
						0L,
						0L,false,false),

					/*Radix::PersoComm::NotificationSender:rid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFOGZWULAQBCDDOMGLD2JGDR4TU"),
						"rid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFPIVWM466ZCUXNM5SRNYSXXWXI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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

						/*Radix::PersoComm::NotificationSender:rid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::NotificationSender:routingKey:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTGS3PTFC5RGSLAFMQFUSXEJUOI"),
						"routingKey",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls32LXJJIQHBCA7LKKUOJA7LRGP4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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

						/*Radix::PersoComm::NotificationSender:routingKey:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::NotificationSender:storeAttachInHist:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHDP4Q3IHNRGLZFFT6IRB3M3G24"),
						"storeAttachInHist",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLV2KKNMOLBB6JCNRIQ7QOUOJXM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::PersoComm::NotificationSender:storeAttachInHist:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::NotificationSender:subjectTemplate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYRJIL7ZCM5AK5LDULILF6B2FDA"),
						"subjectTemplate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUZPSXQCDBRH3FF6WOSVIL4D2U4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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

						/*Radix::PersoComm::NotificationSender:subjectTemplate:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::NotificationSender:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colARKLG7SWKREHPGQYQWOU5XVGJI"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYEAWHZAIWZE2BIOJZXDHH6MCLY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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

						/*Radix::PersoComm::NotificationSender:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::PersoComm::NotificationSender:userGroup:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ3I7LMAFMJFC5FIRM4VXAPBBTM"),
						"userGroup",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRJ4TNSEACVF23GXHZZO5TZUNRU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::PersoComm::NotificationSender:userGroupName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXECQRSZBN5F27GJKI242EGT7NA"),
						"userGroupName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR2LGYWYYGJHSHMN4AKUZDOKVZM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
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

						/*Radix::PersoComm::NotificationSender:userGroupName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			null,
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refPBH6ZHX4X5GFFLXY3NOHK22EVI"),"NotificationSender=>UserGroup (userGroupName=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVKKXVT2NARDUNGRKAOMYB6VWXI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colXECQRSZBN5F27GJKI242EGT7NA")},new String[]{"userGroupName"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4")},new String[]{"name"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprX6LAA6CFYVAA5EYOMFEAA75RH4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprDSRXSN5FIBHO3LW2WUQD3LKO7I")},
			false,true,false);
}

/* Radix::PersoComm::NotificationSender:General - Desktop Meta*/

/*Radix::PersoComm::NotificationSender:General-Editor Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class General_mi{
	private static final class General_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		General_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprX6LAA6CFYVAA5EYOMFEAA75RH4"),
			"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVKKXVT2NARDUNGRKAOMYB6VWXI"),
			null,
			null,

			/*Radix::PersoComm::NotificationSender:General:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::PersoComm::NotificationSender:General:General-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgDKR7VAZZPJC3VBRSRN5UGK366U"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC6KU45IE5ZDTJOPL2DDMLU3NX4"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colARKLG7SWKREHPGQYQWOU5XVGJI"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFOGZWULAQBCDDOMGLD2JGDR4TU"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPH5JTQND3VDZTJJ7Q46PRKF7JE"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTGS3PTFC5RGSLAFMQFUSXEJUOI"),0,4,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEI7GIMO4KFAPRAU4HAVU6BQ3PI"),0,5,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHDP4Q3IHNRGLZFFT6IRB3M3G24"),0,6,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYRJIL7ZCM5AK5LDULILF6B2FDA"),0,7,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLB4DFGLQ7RD6TK65SWSARQVKGA"),0,8,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ3I7LMAFMJFC5FIRM4VXAPBBTM"),0,9,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru5NLSZQUQ5RGO5HJHCSBFUNKSHQ"),0,10,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruRZE4ECEYPNA2LARKXY4RZJTG4I"),0,11,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgDKR7VAZZPJC3VBRSRN5UGK366U"))}
			,

			/*Radix::PersoComm::NotificationSender:General:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			2192,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.explorer.NotificationSender.NotificationSender_DefaultModel(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new General_DEF(); 
;
}
/* Radix::PersoComm::NotificationSender:General - Web Meta*/

/*Radix::PersoComm::NotificationSender:General-Editor Presentation*/

package org.radixware.ads.PersoComm.web;
public final class General_mi{
	private static final class General_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		General_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprX6LAA6CFYVAA5EYOMFEAA75RH4"),
			"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVKKXVT2NARDUNGRKAOMYB6VWXI"),
			null,
			null,

			/*Radix::PersoComm::NotificationSender:General:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::PersoComm::NotificationSender:General:General-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgDKR7VAZZPJC3VBRSRN5UGK366U"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC6KU45IE5ZDTJOPL2DDMLU3NX4"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colARKLG7SWKREHPGQYQWOU5XVGJI"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFOGZWULAQBCDDOMGLD2JGDR4TU"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPH5JTQND3VDZTJJ7Q46PRKF7JE"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTGS3PTFC5RGSLAFMQFUSXEJUOI"),0,4,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEI7GIMO4KFAPRAU4HAVU6BQ3PI"),0,5,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHDP4Q3IHNRGLZFFT6IRB3M3G24"),0,6,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYRJIL7ZCM5AK5LDULILF6B2FDA"),0,7,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLB4DFGLQ7RD6TK65SWSARQVKGA"),0,8,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ3I7LMAFMJFC5FIRM4VXAPBBTM"),0,9,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru5NLSZQUQ5RGO5HJHCSBFUNKSHQ"),0,10,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruRZE4ECEYPNA2LARKXY4RZJTG4I"),0,11,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgDKR7VAZZPJC3VBRSRN5UGK366U"))}
			,

			/*Radix::PersoComm::NotificationSender:General:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			2192,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.web.NotificationSender.NotificationSender_DefaultModel(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new General_DEF(); 
;
}
/* Radix::PersoComm::NotificationSender:General - Desktop Meta*/

/*Radix::PersoComm::NotificationSender:General-Selector Presentation*/

package org.radixware.ads.PersoComm.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprDSRXSN5FIBHO3LW2WUQD3LKO7I"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVKKXVT2NARDUNGRKAOMYB6VWXI"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		false,
		null,
		32,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprX6LAA6CFYVAA5EYOMFEAA75RH4")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprX6LAA6CFYVAA5EYOMFEAA75RH4")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC6KU45IE5ZDTJOPL2DDMLU3NX4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colARKLG7SWKREHPGQYQWOU5XVGJI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPH5JTQND3VDZTJJ7Q46PRKF7JE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTGS3PTFC5RGSLAFMQFUSXEJUOI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.explorer.NotificationSender.DefaultGroupModel(userSession,this);
	}
}
/* Radix::PersoComm::NotificationSender:General - Web Meta*/

/*Radix::PersoComm::NotificationSender:General-Selector Presentation*/

package org.radixware.ads.PersoComm.web;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprDSRXSN5FIBHO3LW2WUQD3LKO7I"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVKKXVT2NARDUNGRKAOMYB6VWXI"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVKKXVT2NARDUNGRKAOMYB6VWXI"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		false,
		null,
		32,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprX6LAA6CFYVAA5EYOMFEAA75RH4")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprX6LAA6CFYVAA5EYOMFEAA75RH4")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC6KU45IE5ZDTJOPL2DDMLU3NX4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colARKLG7SWKREHPGQYQWOU5XVGJI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPH5JTQND3VDZTJJ7Q46PRKF7JE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTGS3PTFC5RGSLAFMQFUSXEJUOI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.PersoComm.web.NotificationSender.DefaultGroupModel(userSession,this);
	}
}
/* Radix::PersoComm::NotificationSender - Localizing Bundle */
package org.radixware.ads.PersoComm.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class NotificationSender - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Routing key");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ключ маршрутизации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls32LXJJIQHBCA7LKKUOJA7LRGP4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Notification Sender");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отправитель уведомлений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3IITGJB6NBG7HOPLPFQCCAPF2Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7FPEZQNJ7ZHLFO4QFI6WGMOJGA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Reference ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ссылочный ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFPIVWM466ZCUXNM5SRNYSXXWXI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Store attachments in history");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Хранить вложения в истории");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLV2KKNMOLBB6JCNRIQ7QOUOJXM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Channel type");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вид канала ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNN65IIDYBNC6VOEZANAHZZP4HA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Recipient group name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя группы получателей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR2LGYWYYGJHSHMN4AKUZDOKVZM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Recipient group");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа получателей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRJ4TNSEACVF23GXHZZO5TZUNRU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message subject template");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Шаблон темы сообщений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUZPSXQCDBRH3FF6WOSVIL4D2U4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Title");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYEAWHZAIWZE2BIOJZXDHH6MCLY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Notification Senders");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отправители уведомлений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZOQQAQQJBNGF5IOD6R27PSR5TI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message body template");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Шаблон тела сообщений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZSDWR27WJZHHZAQE4GQRHCR3DQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"History mode");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Режим сохранения в истории");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZTHURWANMFHK5OPGZZRMZYLXFQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(NotificationSender - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecVKKXVT2NARDUNGRKAOMYB6VWXI"),"NotificationSender - Localizing Bundle",$$$items$$$);
}
