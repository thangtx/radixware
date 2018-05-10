
/* Radix::System::EventLog - Server Executable*/

/*Radix::System::EventLog-Entity Class*/

package org.radixware.ads.System.server;

import org.apache.xmlbeans.GDate;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog")
public final published class EventLog  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return EventLog_mi.rdxMeta;}

	/*Radix::System::EventLog:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:Properties-Properties*/

	/*Radix::System::EventLog:receiptor-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:receiptor")
	public published  Str getReceiptor() {
		return receiptor;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:receiptor")
	public published   void setReceiptor(Str val) {
		receiptor = val;
	}

	/*Radix::System::EventLog:code-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:code")
	public published  Str getCode() {
		return code;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:code")
	public published   void setCode(Str val) {
		code = val;
	}

	/*Radix::System::EventLog:component-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:component")
	public published  org.radixware.ads.Arte.common.EventSource getComponent() {

		try {
		   return internal[component];
		} catch (Exceptions::NoConstItemWithSuchValueError e){
		    return null;
		}
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:component")
	public published   void setComponent(org.radixware.ads.Arte.common.EventSource val) {
		component = val;
	}

	/*Radix::System::EventLog:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:id")
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:id")
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::System::EventLog:wordsClob-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:wordsClob")
	public published  java.sql.Clob getWordsClob() {
		return wordsClob;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:wordsClob")
	public published   void setWordsClob(java.sql.Clob val) {
		wordsClob = val;
	}

	/*Radix::System::EventLog:raiseTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:raiseTime")
	public published  java.sql.Timestamp getRaiseTime() {
		return raiseTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:raiseTime")
	public published   void setRaiseTime(java.sql.Timestamp val) {
		raiseTime = val;
	}

	/*Radix::System::EventLog:severity-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:severity")
	public published  org.radixware.kernel.common.enums.EEventSeverity getSeverity() {
		return severity;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:severity")
	public published   void setSeverity(org.radixware.kernel.common.enums.EEventSeverity val) {
		severity = val;
	}

	/*Radix::System::EventLog:commentary-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:commentary")
	public published  Str getCommentary() {
		return commentary;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:commentary")
	public published   void setCommentary(Str val) {
		commentary = val;
	}

	/*Radix::System::EventLog:message-Dynamic Property*/



	protected Str message=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:message")
	public published  Str getMessage() {

		if(internal[message]==null)
		    internal[message] = EventLog.calcMessage(code, wordsClob);

		return internal[message];
	}

	/*Radix::System::EventLog:typeTitle-Dynamic Property*/



	protected Str typeTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:typeTitle")
	public published  Str getTypeTitle() {

		if(internal[typeTitle]==null)
		    internal[typeTitle] = EventLog.calcTypeTitle(code);
		return internal[typeTitle];
	}

	/*Radix::System::EventLog:user-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:user")
	public published  org.radixware.ads.Acs.server.User getUser() {
		return user;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:user")
	public published   void setUser(org.radixware.ads.Acs.server.User val) {
		user = val;
	}

	/*Radix::System::EventLog:station-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:station")
	public published  org.radixware.ads.Acs.server.Station getStation() {
		return station;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:station")
	public published   void setStation(org.radixware.ads.Acs.server.Station val) {
		station = val;
	}

	/*Radix::System::EventLog:stationName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:stationName")
	public published  Str getStationName() {
		return stationName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:stationName")
	public published   void setStationName(Str val) {
		stationName = val;
	}

	/*Radix::System::EventLog:userName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:userName")
	public published  Str getUserName() {
		return userName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:userName")
	public published   void setUserName(Str val) {
		userName = val;
	}

	/*Radix::System::EventLog:words-Dynamic Property*/



	protected org.radixware.kernel.common.types.ArrStr words=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:words")
	public published  org.radixware.kernel.common.types.ArrStr getWords() {

		if (wordsClob == null)
		    return null;
		try {
		    return ArrStr.fromValAsStr(wordsClob.getSubString(1, (int) wordsClob.length()));
		} catch (Exceptions::SQLException e) {
		    throw new DatabaseError(e);
		}
	}

	/*Radix::System::EventLog:messagePreview-Dynamic Property*/



	protected Str messagePreview=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:messagePreview")
	public published  Str getMessagePreview() {

		if (internal[messagePreview] != null) {
		    return internal[messagePreview];
		}
		final String messText = message;

		final int previewLength = 300;

		if (messText == null) {
		    return null;
		} else if (messText.length() <= previewLength) {
		     internal[messagePreview] = messText;   
		} else {
		    internal[messagePreview] = messText.substring(0, previewLength) + String.format("<%s characters omitted...>", messText.length() - previewLength);
		}
		return internal[messagePreview];
	}

	/*Radix::System::EventLog:isSensitive-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:isSensitive")
	public published  Bool getIsSensitive() {
		return isSensitive;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:isSensitive")
	public published   void setIsSensitive(Bool val) {
		isSensitive = val;
	}

	/*Radix::System::EventLog:originalSeverity-Dynamic Property*/



	protected org.radixware.kernel.common.enums.EEventSeverity originalSeverity=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:originalSeverity")
	public published  org.radixware.kernel.common.enums.EEventSeverity getOriginalSeverity() {

		if (internal[originalSeverity] == null) {
		    if (code != null) {
		        final Meta::EventCode2EventParams params = Meta::EventCode2EventParams.loadByPK(code, Arte::Arte.getVersion(), true);
		        internal[originalSeverity] = params == null ? severity : params.eventSeverity;
		    } else {
		        internal[originalSeverity] = severity;
		    }
		}
		return internal[originalSeverity];
	}





















































































































	/*Radix::System::EventLog:Methods-Methods*/

	/*Radix::System::EventLog:calcTypeTitle-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:calcTypeTitle")
	public static published  Str calcTypeTitle (Str code) {
		try{
		    return Meta::Utils.getEventTitleByCode(code);
		} catch(Throwable e) {
		    return "Get code title exception: " + e.getClass() +" - " + e.getMessage();
		}
	}

	/*Radix::System::EventLog:calcMessage-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:calcMessage")
	public static published  Str calcMessage (Str code, java.sql.Clob words) {
		return calcMessage(code, words, Arte::Arte.getClientLanguage());
	}

	/*Radix::System::EventLog:export-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:export")
	  void export (javax.xml.stream.XMLStreamWriter writer, Str nameSpace) throws java.sql.SQLException,java.io.UnsupportedEncodingException,javax.xml.stream.XMLStreamException {
		writer.writeCharacters("\n\t");
		writer.writeStartElement(nameSpace, "Event");
		writer.writeAttribute("Severity", severity.Value.toString());
		GDate gDate = new GDate(new java.util.Date(raiseTime.getTime()));
		writer.writeAttribute("RaiseTime", gDate.toString());
		if (code != null)
		    writer.writeAttribute("Code", code);
		if (component != null)
		    writer.writeAttribute("Component", component.Value);

		if (message != null) {
		    writer.writeCharacters("\n\t\t");
		    writer.writeStartElement(nameSpace, "Message");
		    writer.writeCharacters(org.radixware.kernel.common.build.xbeans.XmlEscapeStr.getSafeXmlString(message));
		    writer.writeEndElement();
		}
		if (wordsClob != null) {
		    writer.writeCharacters("\n\t\t");
		    writer.writeStartElement(nameSpace, "Words");
		    final long wordsLen = wordsClob.length();
		    final Str words;
		    if (wordsLen > Integer.MAX_VALUE) 
		        words = wordsClob.getSubString(1, Integer.MAX_VALUE);
		    else
		        words = wordsClob.getSubString(1, (int) wordsLen);
		    writer.writeCharacters(Utils::Base64.encode(words.getBytes("UTF-8")));
		    writer.writeEndElement();
		}

		EventLogContextCursor cur = EventLogContextCursor.open(raiseTime, id);
		try {
		    while (cur.next()) {
		        writer.writeCharacters("\n\t\t");
		        writer.writeStartElement(nameSpace, "EventContext");
		        writer.writeAttribute("Type", cur.type.Value);
		        writer.writeAttribute("ID", cur.id);
		        writer.writeEndElement();
		    }
		} finally {
		    cur.close();
		}

		writer.writeCharacters("\n\t");
		writer.writeEndElement();

		if (!this.isInDatabase(false))
		    this.discard();
	}

	/*Radix::System::EventLog:loadEventContext-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:loadEventContext")
	private static  org.radixware.ads.System.server.EventContext loadEventContext (org.radixware.kernel.server.arte.resources.ProgressDialogResource.Process process, javax.xml.stream.XMLStreamReader in, org.radixware.ads.System.server.EventLog event) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ResourceUsageException,org.radixware.kernel.common.exceptions.ResourceUsageTimeout {
		Arte::EventContextType type = null;
		Str id=null;
		for(int i = 0; i < in.getAttributeCount(); i++) {
		    final Str locName = in.getAttributeLocalName(i);
		    final Str value = in.getAttributeValue(i);
		    if (locName == "Type") {
		        try {
		            type = Arte::EventContextType.getForValue(value);
		        } catch (Exceptions::Error e) {
		            return null; //unknown context - skip
		        }
		    } else if(locName == "ID") {
		        id = value;
		    } else
		        EventLogGroup.processTraceError(process, "Unknown attribute: " + locName);
		}

		EventContext newContext = new EventContext();
		newContext.init();
		newContext.event = event;
		newContext.raiseTime = event.raiseTime;
		newContext.type = type;
		newContext.id = id;
		//newContext.();
		return newContext;
	}

	/*Radix::System::EventLog:import-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:import")
	 static  org.radixware.ads.System.server.EventLog import (org.radixware.kernel.server.arte.resources.ProgressDialogResource.Process process, javax.xml.stream.XMLStreamReader in, Str fileName, java.util.List<org.radixware.ads.System.server.EventContext> eventContextList, java.util.TimeZone timeZone) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ResourceUsageException,org.radixware.kernel.common.exceptions.ResourceUsageTimeout,java.io.UnsupportedEncodingException,javax.xml.stream.XMLStreamException {
		try {
		    EventLog newEvent = new EventLog();
		    newEvent.init();
		    for (int i = 0; i < in.getAttributeCount(); i++) {
		        final Str locName = in.getAttributeLocalName(i);
		        final Str value = in.getAttributeValue(i);
		        if (locName == "Severity") {
		            newEvent.severity = Arte::EventSeverity.getForValue(Int.valueOf(value));
		        } else if (locName == "RaiseTime") {
		            java.util.Date date = new GDate(value).Date;
		            if (timeZone != null) {
		                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		                df.setTimeZone(timeZone);
		                final String dateStr = df.format(date);
		                df.setTimeZone(java.util.TimeZone.Default);
		                try {
		                    date = df.parse(dateStr);
		                } catch (Exceptions::ParseException ex) {
		                    throw new RuntimeException(ex);
		                }
		            }
		            newEvent.raiseTime = new DateTime(date.Time);
		        } else if (locName == "Code") {
		            newEvent.code = value;
		        } else if (locName == "Component") {
		            try {
		                newEvent.component = Arte::EventSource.getForValue(value);
		            } catch (Exceptions::NoConstItemWithSuchValueError e) {
		                newEvent.component = Arte::EventSource:App;
		            }
		        } else
		            EventLogGroup.processTraceError(process, "Unknown attribute: " + locName);
		    }
		    //System.out.println("New event PID: " + newEvent.().toString());
		    //newEvent.();

		    //create import context
		    EventContext impContext = new EventContext();
		    impContext.init();
		    impContext.type = Arte::EventContextType:Import;
		    impContext.id = fileName;
		    impContext.raiseTime = newEvent.raiseTime;
		    impContext.event = newEvent;
		    //System.out.println("Import context PID: " + impContext.().toString());
		    //impContext.();
		    eventContextList.add(impContext);

		    int counter = 1;
		    while (counter > 0 && in.hasNext()) {
		        int xmlEvent = in.next();
		        if (xmlEvent == javax.xml.stream.XMLStreamConstants.START_ELEMENT) {
		            //System.out.println("START_ELEMENT");
		            counter++;
		            final Str name = in.getLocalName();
		            //System.out.println(name);
		            if (name == "Words") {
		                final Str value = new Str(Utils::Base64.decode(in.ElementText), "UTF-8");
		                counter--;
		                newEvent.wordsClob = Arte::Arte.createTemporaryClob();
		                newEvent.wordsClob.setString(1, value);
		                //newEvent.();
		            } else if (name == "EventContext") {
		                final EventContext eventContext = loadEventContext(process, in, newEvent);
		                if (eventContext != null)
		                    eventContextList.add(eventContext);
		            } else if (name == "Message") {
		                ;
		            } else
		                EventLogGroup.processTraceError(process, "Unknown element: " + name);
		        } else if (xmlEvent == javax.xml.stream.XMLStreamConstants.END_ELEMENT) {
		            counter--;
		            //System.out.println("END_ELEMENT");
		        }
		    }
		    return newEvent;
		} catch (Exceptions::SQLException e) {
		    throw new AppError(e.getMessage());
		}

	}

	/*Radix::System::EventLog:calcMessage-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:calcMessage")
	public static published  Str calcMessage (Str code, java.sql.Clob words, org.radixware.kernel.common.enums.EIsoLanguage language) {
		try {
		    final long wordsLen = words == null ? 0 : words.length();
		    if(wordsLen > Integer.MAX_VALUE)
		        return "Words field value is too long: " + String.valueOf(wordsLen);
		    else
		        return Meta::TraceItem.getMess(Arte::Arte.getInstance().MlsProcessor, code, language, words == null ? null : ArrStr.fromValAsStr(words.getSubString(1, (int)wordsLen)));
		} catch(Throwable e) {
		    return "Parse message exception: " + e.getClass() +" - " + e.getMessage() + "\nOriginal message: "+words;  
		}

	}

	/*Radix::System::EventLog:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:loadByPK")
	public static published  org.radixware.ads.System.server.EventLog loadByPK (java.sql.Timestamp raiseTime, Int id, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(7);
			if(raiseTime==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWBCRBDFDL2VDBNSNAAYOJ6SINF"),raiseTime);
			if(id==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUBCRBDFDL2VDBNSNAAYOJ6SINF"),id);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),pkValsMap);
		try{
		return (
		org.radixware.ads.System.server.EventLog) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::System::EventLog:getContexts-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:getContexts")
	public published  java.util.List<org.radixware.ads.System.server.EventContext> getContexts () {
		java.util.List<EventContext> res = new java.util.ArrayList<>();
		EventLogContextCursor c = EventLogContextCursor.open(raiseTime, id);
		try {
		    while (c.next())
		        res.add(c.context);
		} finally {
		    c.close();
		}
		return res;

	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::System::EventLog - Server Meta*/

/*Radix::System::EventLog-Entity Class*/

package org.radixware.ads.System.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EventLog_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),"EventLog",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsACX7FP5GUHOBDCLBAALOMT5GDM"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::System::EventLog:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
							/*Owner Class Name*/
							"EventLog",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsACX7FP5GUHOBDCLBAALOMT5GDM"),
							/*Property presentations*/

							/*Radix::System::EventLog:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::System::EventLog:component:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPHNPCMDWKPNRDAQSABIFNQAAAE"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::System::EventLog:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUBCRBDFDL2VDBNSNAAYOJ6SINF"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::System::EventLog:raiseTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWBCRBDFDL2VDBNSNAAYOJ6SINF"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::System::EventLog:severity:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXBCRBDFDL2VDBNSNAAYOJ6SINF"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::System::EventLog:commentary:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYBCRBDFDL2VDBNSNAAYOJ6SINF"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::System::EventLog:message:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFWK7DRDHOXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::EventLog:typeTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVSA77O3TXXNRDB6SAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::EventLog:user:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLTPIFVD36VCQ5HSGQAAOJEY4PM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(262143,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::System::EventLog:station:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNTU5A722VVASNC5GVHVSWDFLKU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(262143,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::System::EventLog:messagePreview:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEA2GUFJSQ5COTEOM2REI4CW344"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::EventLog:isSensitive:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLNECKEY3IJGX7D6OZOFLCAWOOI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::EventLog:originalSeverity:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFL5O6ILBDZED5LJJILT5XP5LEE"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							null,
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::System::EventLog:TimeDesc-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),"TimeDesc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3M5UBSD64DORDOFEABIFNQAABA"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWBCRBDFDL2VDBNSNAAYOJ6SINF"),org.radixware.kernel.common.enums.EOrder.DESC),

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUBCRBDFDL2VDBNSNAAYOJ6SINF"),org.radixware.kernel.common.enums.EOrder.DESC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>FIRST_ROWS(51) leading(ctxt) INDEX_DESC(</xsc:Sql></xsc:Item><xsc:Item><xsc:ThisTableSqlName/></xsc:Item><xsc:Item><xsc:Sql> </xsc:Sql></xsc:Item><xsc:Item><xsc:DbName Path=\"tblACQVZOMVVHWDBROXAAIT4AGD7E idxACQVZOMVVHWDBROXAAIT4AGD7E\"/></xsc:Item><xsc:Item><xsc:Sql>) use_nl(ctxt </xsc:Sql></xsc:Item><xsc:Item><xsc:ThisTableSqlName/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>","org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::System::EventLog:TimeAsc-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),"TimeAsc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6FQGPWH6UBH3TG4MUOPHDCVGHE"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWBCRBDFDL2VDBNSNAAYOJ6SINF"),org.radixware.kernel.common.enums.EOrder.ASC),

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUBCRBDFDL2VDBNSNAAYOJ6SINF"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>FIRST_ROWS(51) leading(ctxt) INDEX(</xsc:Sql></xsc:Item><xsc:Item><xsc:ThisTableSqlName/></xsc:Item><xsc:Item><xsc:Sql> </xsc:Sql></xsc:Item><xsc:Item><xsc:DbName Path=\"tblACQVZOMVVHWDBROXAAIT4AGD7E idxACQVZOMVVHWDBROXAAIT4AGD7E\"/></xsc:Item><xsc:Item><xsc:Sql>) use_nl(ctxt </xsc:Sql></xsc:Item><xsc:Item><xsc:ThisTableSqlName/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>","org.radixware")
							},
							/*Filters*/
							new org.radixware.kernel.server.meta.presentations.RadFilterDef[]{

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::System::EventLog:Today-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltUV5CRRHED3OBDCHDAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),"Today",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6FVFSS24OXOBDFKUAAMPGXUWTQ"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=trunc(Sysdate) \nAND ( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmSF36PORPYBF7ZH2MFL5OQT3LPY\"/></xsc:Item><xsc:Item><xsc:Sql> IS NULL OR </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql>>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmSF36PORPYBF7ZH2MFL5OQT3LPY\"/></xsc:Item><xsc:Item><xsc:Sql>)\nAND ( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmPUKHAASBJJFXDP7WOVZJGKJRVM\"/></xsc:Item><xsc:Item><xsc:Sql> IS NULL OR </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colPHNPCMDWKPNRDAQSABIFNQAAAE\" Owner=\"THIS\" SuppressedWarnings=\"100000\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmPUKHAASBJJFXDP7WOVZJGKJRVM\"/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),false,new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting[]{

											new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),"org.radixware"),

											new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E"),"org.radixware")
									},true,"org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::System::EventLog:TimeRange-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("flt3LGDG7LCCVEP5GK5MQN7UUVELA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),"TimeRange",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMZWO3WOGY5CG5PWS4TRQLWDS7Y"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=nvl(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmD7TRMTA7IFEH3M5LD7SUEACI7Y\"/></xsc:Item><xsc:Item><xsc:Sql>, to_date(\'1\', \'J\')))\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>&lt;=decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmP7RHN4373JGJTEIALKA3SCAU5A\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate + 1), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmP7RHN4373JGJTEIALKA3SCAU5A\"/></xsc:Item><xsc:Item><xsc:Sql>)\nAND ( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmBQUVW3USB5C45DVW5IXJCAZU5Q\"/></xsc:Item><xsc:Item><xsc:Sql> IS NULL OR </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql>>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmBQUVW3USB5C45DVW5IXJCAZU5Q\"/></xsc:Item><xsc:Item><xsc:Sql>)\nAND ( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmGXLCPBC4JFHIBE4MRVPYZOARKM\"/></xsc:Item><xsc:Item><xsc:Sql> IS NULL OR </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colPHNPCMDWKPNRDAQSABIFNQAAAE\" Owner=\"THIS\" SuppressedWarnings=\"100000\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmGXLCPBC4JFHIBE4MRVPYZOARKM\"/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),false,new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting[]{

											new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E"),"org.radixware"),

											new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),"org.radixware")
									},true,"org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::System::EventLog:User-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltQUTDPT7CTFGFVJRBQEFMWRTUFI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),"User",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLPCH3N4RVRBLPNFAXSJUS2FWSQ"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmMSYXKT4C4ZHSHPYWYXF5IUERA4\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmMSYXKT4C4ZHSHPYWYXF5IUERA4\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>&lt;= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmFCSBJ7FNLFFTPH2GXQAFKZI4QA\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate + 1), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmFCSBJ7FNLFFTPH2GXQAFKZI4QA\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colLTPIFVD36VCQ5HSGQAAOJEY4PM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:EntityRefParameter ParamId=\"prmOJRL3DEB6FFVXLHX6JJHF2RQW4\" ReferencedTableId=\"tblSY4KIOLTGLNRDHRZABQAQH3XQ4\" PidTranslationMode=\"PrimaryKeyProps\"/></xsc:Item><xsc:Item><xsc:Sql>\nAND ( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmG3NAYPEU3FBELFTF2VTLCGGAG4\"/></xsc:Item><xsc:Item><xsc:Sql> IS NULL OR </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql>>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmG3NAYPEU3FBELFTF2VTLCGGAG4\"/></xsc:Item><xsc:Item><xsc:Sql>)\n</xsc:Sql></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),false,new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting[]{

											new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E"),"org.radixware"),

											new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),"org.radixware")
									},false,"org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::System::EventLog:Station-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltM3WETXHKOZHYBIO63RKBNXMZVY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),"Station",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBR3XPTC47NG5XIA4KE3TK2SBUQ"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmSVDGDRZILFA2JKFGXLVRXYLQ3Q\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmSVDGDRZILFA2JKFGXLVRXYLQ3Q\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> &lt;= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmREAJESHPOVESTNOIMUXQCVNZQQ\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate + 1), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmREAJESHPOVESTNOIMUXQCVNZQQ\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colNTU5A722VVASNC5GVHVSWDFLKU\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> in (</xsc:Sql></xsc:Item><xsc:Item><xsc:EntityRefParameter ParamId=\"prm2UCNYUDK2VGYVOUZKJINQQY4U4\" ReferencedTableId=\"tblFDFDUXBHJ3NRDJIRACQMTAIZT4\" PidTranslationMode=\"PrimaryKeyProps\"/></xsc:Item><xsc:Item><xsc:Sql>)\nAND ( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmFOQJM4EYHZGABACHFRQTRYHP5M\"/></xsc:Item><xsc:Item><xsc:Sql> IS NULL OR </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql>>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmFOQJM4EYHZGABACHFRQTRYHP5M\"/></xsc:Item><xsc:Item><xsc:Sql>)\n</xsc:Sql></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),false,new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting[]{

											new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E"),"org.radixware"),

											new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),"org.radixware")
									},false,"org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::System::EventLog:Severity-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltBYGYNUVM4JBGZO3KC6O7C5I65Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),"Severity",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOKNP5QK2SRHJ3K4C4L2PO3JS5Y"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmN3KFLDVQ3FEKNFRJQAXANO3QJE\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmN3KFLDVQ3FEKNFRJQAXANO3QJE\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> &lt;= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmA6ZXQA6P65ER5KEZL7S5HAF2MU\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate + 1), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmA6ZXQA6P65ER5KEZL7S5HAF2MU\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand (</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmEZYWX2UJTBFEHJXFY4U24PVUMI\"/></xsc:Item><xsc:Item><xsc:Sql> or </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmEZYWX2UJTBFEHJXFY4U24PVUMI\"/></xsc:Item><xsc:Item><xsc:Sql> is null)\nand (</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>&lt;=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm4LJSDT4XNVAPJL66DQZJTUO2P4\"/></xsc:Item><xsc:Item><xsc:Sql> or </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm4LJSDT4XNVAPJL66DQZJTUO2P4\"/></xsc:Item><xsc:Item><xsc:Sql> is null)</xsc:Sql></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),true,new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting[]{

											new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),"org.radixware"),

											new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E"),"org.radixware")
									},true,"org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::System::EventLog:Imported-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltS5F72ARLKFBGJLCKEX27ZDUJR4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),"Imported",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNXWOLHNIBRARBFF4T3TDE3HZFE"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>exists (\n    select 1 from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblJABJB4OAP3NRDAQSABIFNQAAAE\"/></xsc:Item><xsc:Item><xsc:Sql> where \n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblJABJB4OAP3NRDAQSABIFNQAAAE\" PropId=\"colXHA62NOBP3NRDAQSABIFNQAAAE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colUBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> and\n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblJABJB4OAP3NRDAQSABIFNQAAAE\" PropId=\"colOSK5SVWBP3NRDAQSABIFNQAAAE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> and\n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJABJB4OAP3NRDAQSABIFNQAAAE\" PropId=\"colT67W77OAP3NRDAQSABIFNQAAAE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:ConstValue EnumId=\"acsPVLIUELSKTNRDAQSABIFNQAAAE\" ItemId=\"aciGBCHKTBTCJEZ5G6VKA5IWHC4VM\"/></xsc:Item><xsc:Item><xsc:Sql> and\n    upper(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJABJB4OAP3NRDAQSABIFNQAAAE\" PropId=\"colHIPN4HGBP3NRDAQSABIFNQAAAE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>) like upper(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmONPRLRODY5BFPM62Q72R53JXLY\"/></xsc:Item><xsc:Item><xsc:Sql>)\n)\nand (</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmRUGE7VE44ND25EYXVNNA6LDPZA\"/></xsc:Item><xsc:Item><xsc:Sql> or </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmRUGE7VE44ND25EYXVNNA6LDPZA\"/></xsc:Item><xsc:Item><xsc:Sql> is null)</xsc:Sql></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),false,new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting[]{

											new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),"org.radixware"),

											new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E"),"org.radixware")
									},true,"org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::System::EventLog:EventCodes-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltLFNJNPTVBJDWJDA57KRSGBRE2E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),"EventCodes",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUKQQ4VHYWFB2NGCNUXJYM6WG5M"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm2P723KWXQJA7DJ5E4OY7CNXRK4\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm2P723KWXQJA7DJ5E4OY7CNXRK4\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> &lt;= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmINQENZUDPJFU3H2I5H6JDKAFWQ\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate + 1), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmINQENZUDPJFU3H2I5H6JDKAFWQ\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand (</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmXO3CGUSUW5BZLBZ6NRCTNV577E\"/></xsc:Item><xsc:Item><xsc:Sql> is NULL or INSTR( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmXO3CGUSUW5BZLBZ6NRCTNV577E\"/></xsc:Item><xsc:Item><xsc:Sql>, </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colKZ33LLHXVHWDBROXAAIT4AGD7E\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> )>0)</xsc:Sql></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),true,null,true,"org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::System::EventLog:Period-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltTYT3ALDZTNAYDDFDPTL7AFYGUU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),"Period",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBOEVCBAVXVBKXKBYE7W2KPQ6OU"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:IfParam ParamId=\"prmDVPXQQQ72VHPTELUBU5GXJALB4\" Operation=\"Equal\"><xsc:Value>1</xsc:Value></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=trunc(Sysdate)\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmDVPXQQQ72VHPTELUBU5GXJALB4\" Operation=\"Equal\"><xsc:Value>2</xsc:Value></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=trunc(Sysdate-1)\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmDVPXQQQ72VHPTELUBU5GXJALB4\" Operation=\"Equal\"><xsc:Value>3</xsc:Value></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=trunc(Sysdate-7)\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmDVPXQQQ72VHPTELUBU5GXJALB4\" Operation=\"Equal\"><xsc:Value>4</xsc:Value></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=trunc(add_months(Sysdate,-1))\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmDVPXQQQ72VHPTELUBU5GXJALB4\" Operation=\"Equal\"><xsc:Value>5</xsc:Value></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    (</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=nvl(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmNKTTHR6EAJB6FAJGHIDNIW3PHI\"/></xsc:Item><xsc:Item><xsc:Sql>, to_date(\'1\', \'J\')))\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmXOAKGU7PA5CSFDQACAVI2U3RCM\" Operation=\"Null\"/></xsc:Item><xsc:Item><xsc:Sql>\nAND </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> &lt; trunc(sysdate + 1)\n</xsc:Sql></xsc:Item><xsc:Item><xsc:ElseIf/></xsc:Item><xsc:Item><xsc:Sql>\nAND </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>&lt;=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmXOAKGU7PA5CSFDQACAVI2U3RCM\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmAIGN2LJXGVAHRPELFC4H6UAMKY\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    AND </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql>>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmAIGN2LJXGVAHRPELFC4H6UAMKY\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmTR6BP77HZRADZJKBAN6JVLZYB4\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    AND </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql>&lt;=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmTR6BP77HZRADZJKBAN6JVLZYB4\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prm6AHGQBB5HFBEVNUUP6AZ435PRU\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    AND </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colPHNPCMDWKPNRDAQSABIFNQAAAE\" Owner=\"THIS\" SuppressedWarnings=\"100000\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm6AHGQBB5HFBEVNUUP6AZ435PRU\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmQSMXFK73ZZHKNLPYAIYGS56DXQ\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    AND </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colLTPIFVD36VCQ5HSGQAAOJEY4PM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:EntityRefParameter ParamId=\"prmQSMXFK73ZZHKNLPYAIYGS56DXQ\" ReferencedTableId=\"tblSY4KIOLTGLNRDHRZABQAQH3XQ4\" PidTranslationMode=\"PrimaryKeyProps\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prm4A2P5FE5AZGFTMSTY7E5CPD7I4\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    AND </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colNTU5A722VVASNC5GVHVSWDFLKU\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:EntityRefParameter ParamId=\"prm4A2P5FE5AZGFTMSTY7E5CPD7I4\" ReferencedTableId=\"tblFDFDUXBHJ3NRDJIRACQMTAIZT4\" PidTranslationMode=\"PrimaryKeyProps\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prm6FBEUOOMAFDVLN3J3TI737U5Q4\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    AND INSTR( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm6FBEUOOMAFDVLN3J3TI737U5Q4\"/></xsc:Item><xsc:Item><xsc:Sql>, </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colKZ33LLHXVHWDBROXAAIT4AGD7E\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> )>0\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmVND4W5T4GJCDVDMMEM7ZFG7SZU\" Operation=\"NotEqual\"><xsc:Value/></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    AND UPPER( </xsc:Sql></xsc:Item><xsc:Item><xsc:DbFuncCall FuncId=\"dfnMJOIKNAEUREC5CYMMNQLMBJAFM\"/></xsc:Item><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colKZ33LLHXVHWDBROXAAIT4AGD7E\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> , </xsc:Sql></xsc:Item><xsc:Item><xsc:DbFuncCall FuncId=\"dfnLMUSM5GXCTOBDCKFAAMPGXSZKU\"/></xsc:Item><xsc:Item><xsc:Sql>(), </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colVBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>) ) LIKE \'%\' || UPPER(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmVND4W5T4GJCDVDMMEM7ZFG7SZU\"/></xsc:Item><xsc:Item><xsc:Sql>) || \'%\'\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmI5PWPY5VHNAPXLI3J2TIDTRKOI\" Operation=\"NotEqual\"><xsc:Value/></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    AND UPPER( </xsc:Sql></xsc:Item><xsc:Item><xsc:DbFuncCall FuncId=\"dfnJ2PTRZ7GVZBMHN5USDST6QGLWI\"/></xsc:Item><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colKZ33LLHXVHWDBROXAAIT4AGD7E\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>,  </xsc:Sql></xsc:Item><xsc:Item><xsc:DbFuncCall FuncId=\"dfnLMUSM5GXCTOBDCKFAAMPGXSZKU\"/></xsc:Item><xsc:Item><xsc:Sql>(), </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colVBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>) ) NOT LIKE \'%\' || UPPER(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmI5PWPY5VHNAPXLI3J2TIDTRKOI\"/></xsc:Item><xsc:Item><xsc:Sql>) || \'%\'\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),false,new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting[]{

											new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E"),"org.radixware"),

											new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),"org.radixware")
									},false,"org.radixware")
							},
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::System::EventLog:WithoutContextInfo-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEEUTDHGEXTNRDB6RAALOMT5GDM"),
									"WithoutContextInfo",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::System::EventLog:WithoutContextInfo:Children-Explorer Items*/
										null
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(28679,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::System::EventLog:WithContextInfo-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM"),
									"WithContextInfo",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEEUTDHGEXTNRDB6RAALOMT5GDM"),
									10416,
									null,

									/*Radix::System::EventLog:WithContextInfo:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::System::EventLog:WithContextInfo:EventContext-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiGA2GHQDFV7NRDOC3ADQEZUQ3BM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJABJB4OAP3NRDAQSABIFNQAAAE"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2UIYK3COR3NRDB5QAALOMT5GDM"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("ref54TN3B6BP3NRDAQSABIFNQAAAE"),
													null,
													null)
										}
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::System::EventLog:Contextless-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprF6ONI4OEXTNRDB6RAALOMT5GDM"),"Contextless",null,2192,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWBCRBDFDL2VDBNSNAAYOJ6SINF"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUBCRBDFDL2VDBNSNAAYOJ6SINF"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXBCRBDFDL2VDBNSNAAYOJ6SINF"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEA2GUFJSQ5COTEOM2REI4CW344"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLTPIFVD36VCQ5HSGQAAOJEY4PM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNTU5A722VVASNC5GVHVSWDFLKU"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPHNPCMDWKPNRDAQSABIFNQAAAE"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),false,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E")},false,org.radixware.kernel.common.types.Id.Factory.loadFrom("fltTYT3ALDZTNAYDDFDPTL7AFYGUU"),false,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("fltBYGYNUVM4JBGZO3KC6O7C5I65Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltM3WETXHKOZHYBIO63RKBNXMZVY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("flt3LGDG7LCCVEP5GK5MQN7UUVELA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltUV5CRRHED3OBDCHDAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltQUTDPT7CTFGFVJRBQEFMWRTUFI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltLFNJNPTVBJDWJDA57KRSGBRE2E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltTYT3ALDZTNAYDDFDPTL7AFYGUU")},false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},null,org.radixware.kernel.server.types.Restrictions.Factory.newInstance(29623,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQGNH5TT7TXOBDCK6AALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdFKWW4F3LYNDFJGPVIVJ5NX5A74"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdNJQT6UHXLJBIHB5CIIQDQ74UGI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdVK2KXYIVHZFKNEVICOVHZQHIBE")},null,null),null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::System::EventLog:WithContextInfo_DeleteAllEnabled-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprHH4BKAI7UVEO3OS4I5AHCHKN3A"),"WithContextInfo_DeleteAllEnabled",org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVWY625ANRTNRDB5PAALOMT5GDM"),24794,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblJABJB4OAP3NRDAQSABIFNQAAAE\" PropId=\"colOSK5SVWBP3NRDAQSABIFNQAAAE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> </xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},null,org.radixware.kernel.server.types.Restrictions.Factory.newInstance(291733,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQGNH5TT7TXOBDCK6AALOMT5GDM")},null,null),null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::System::EventLog:WithContextInfo-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVWY625ANRTNRDB5PAALOMT5GDM"),"WithContextInfo",org.radixware.kernel.common.types.Id.Factory.loadFrom("sprF6ONI4OEXTNRDB6RAALOMT5GDM"),24826,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},null,null,null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::System::EventLog:Imported-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprW7YW3HO6WLOBDCLZAALOMT5GDM"),"Imported",org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVWY625ANRTNRDB5PAALOMT5GDM"),18496,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colUBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>=ctxt.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblJABJB4OAP3NRDAQSABIFNQAAAE\" PropId=\"colXHA62NOBP3NRDAQSABIFNQAAAE\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>=ctxt.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblJABJB4OAP3NRDAQSABIFNQAAAE\" PropId=\"colOSK5SVWBP3NRDAQSABIFNQAAAE\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql>\nand ctxt.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblJABJB4OAP3NRDAQSABIFNQAAAE\" PropId=\"colT67W77OAP3NRDAQSABIFNQAAAE\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:ConstValue EnumId=\"acsPVLIUELSKTNRDAQSABIFNQAAAE\" ItemId=\"aciGBCHKTBTCJEZ5G6VKA5IWHC4VM\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:TableSqlName TableId=\"tblJABJB4OAP3NRDAQSABIFNQAAAE\"/></xsc:Item><xsc:Item><xsc:Sql> ctxt</xsc:Sql></xsc:Item></xsc:Sqml>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),false,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E")},false,null,false,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("fltBYGYNUVM4JBGZO3KC6O7C5I65Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("flt3LGDG7LCCVEP5GK5MQN7UUVELA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltM3WETXHKOZHYBIO63RKBNXMZVY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltS5F72ARLKFBGJLCKEX27ZDUJR4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltUV5CRRHED3OBDCHDAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltQUTDPT7CTFGFVJRBQEFMWRTUFI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltTYT3ALDZTNAYDDFDPTL7AFYGUU")},false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},null,org.radixware.kernel.server.types.Restrictions.Factory.newInstance(291733,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdMO6BHZVDUHOBDCLBAALOMT5GDM")},null,null),null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::System::EventLog:ForContextLog-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),"ForContextLog",org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVWY625ANRTNRDB5PAALOMT5GDM"),16594,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = ctxt__RaiseTime and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colUBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = ctxt__EventId</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>/*will be rewritten by EventLogGroup.getQueryBuilderDelegate(), see body of EventLogGroup*/</xsc:Sql></xsc:Item></xsc:Sqml>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),false,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA")},false,org.radixware.kernel.common.types.Id.Factory.loadFrom("fltTYT3ALDZTNAYDDFDPTL7AFYGUU"),false,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("fltBYGYNUVM4JBGZO3KC6O7C5I65Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("flt3LGDG7LCCVEP5GK5MQN7UUVELA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltUV5CRRHED3OBDCHDAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltTYT3ALDZTNAYDDFDPTL7AFYGUU")},false,false,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>FIRST_ROWS(51) leading(ctxt) INDEX(</xsc:Sql></xsc:Item><xsc:Item><xsc:ThisTableSqlName/></xsc:Item><xsc:Item><xsc:Sql> </xsc:Sql></xsc:Item><xsc:Item><xsc:DbName Path=\"tblACQVZOMVVHWDBROXAAIT4AGD7E idxACQVZOMVVHWDBROXAAIT4AGD7E\"/></xsc:Item><xsc:Item><xsc:Sql>) use_nl(ctxt </xsc:Sql></xsc:Item><xsc:Item><xsc:ThisTableSqlName/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},null,org.radixware.kernel.server.types.Restrictions.Factory.newInstance(278583,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQGNH5TT7TXOBDCK6AALOMT5GDM")},null,null),null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::System::EventLog:ForContextLog_DeleteAllEnabled-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprKQ5WOSDJZ5AQFOGPY4PK3O2XTA"),"ForContextLog_DeleteAllEnabled",org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),16601,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},null,org.radixware.kernel.server.types.Restrictions.Factory.newInstance(278549,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQGNH5TT7TXOBDCK6AALOMT5GDM")},null,null),null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::System::EventLog:ForContextLog_WithoutAllContextsTab-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprY2GNJQQ7IRBLDKX4NEFOMP4J5A"),"ForContextLog_WithoutAllContextsTab",org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),16635,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEEUTDHGEXTNRDB6RAALOMT5GDM")},null,null,null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::System::EventLog:ForContextLog_SysUnits-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprDEZXGYRBYVBADE5OCIBI73JN2I"),"ForContextLog_SysUnits",org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),16635,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},null,null,null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::System::EventLog:WithContextInfo_Dashboard-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprE4HR7KORYZDQBAP4O5XMDBLIPE"),"WithContextInfo_Dashboard",org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVWY625ANRTNRDB5PAALOMT5GDM"),16635,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEEUTDHGEXTNRDB6RAALOMT5GDM")},null,null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::System::EventLog:ContextlessNoAutoUpdate-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7X6MMAXKTZHUHB46WUW333PMMA"),"ContextlessNoAutoUpdate",org.radixware.kernel.common.types.Id.Factory.loadFrom("sprF6ONI4OEXTNRDB6RAALOMT5GDM"),16633,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},null,null,null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVWY625ANRTNRDB5PAALOMT5GDM"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::System::EventLog:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colUBCRBDFDL2VDBNSNAAYOJ6SINF"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(28677,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::System::EventLog:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::System::EventLog:receiptor-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKUM7R4BBZLWDRRGGAAIT4AGD7E"),"receiptor",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5RVFSS24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::EventLog:code-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKZ33LLHXVHWDBROXAAIT4AGD7E"),"code",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::EventLog:component-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPHNPCMDWKPNRDAQSABIFNQAAAE"),"component",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5JVFSS24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::EventLog:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUBCRBDFDL2VDBNSNAAYOJ6SINF"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5FVFSS24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::EventLog:wordsClob-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVBCRBDFDL2VDBNSNAAYOJ6SINF"),"wordsClob",null,org.radixware.kernel.common.enums.EValType.CLOB,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::EventLog:raiseTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWBCRBDFDL2VDBNSNAAYOJ6SINF"),"raiseTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls45VFSS24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::EventLog:severity-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXBCRBDFDL2VDBNSNAAYOJ6SINF"),"severity",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4ZVFSS24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::EventLog:commentary-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYBCRBDFDL2VDBNSNAAYOJ6SINF"),"commentary",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4VVFSS24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::EventLog:message-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFWK7DRDHOXOBDFKUAAMPGXUWTQ"),"message",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHEN6MQ3MOXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::System::EventLog:typeTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVSA77O3TXXNRDB6SAALOMT5GDM"),"typeTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4RVFSS24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::System::EventLog:user-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLTPIFVD36VCQ5HSGQAAOJEY4PM"),"user",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTWJFJ647F5AJTOVO6WJO5Z4FPE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refL4N6UFTR3RDR7KPNKMMHZ2K6J4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::EventLog:station-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNTU5A722VVASNC5GVHVSWDFLKU"),"station",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPFS72MZ6KVDX5KCXBRBR24YS34"),org.radixware.kernel.common.types.Id.Factory.loadFrom("ref5TCE2JPJ4FGJDE7YJT3ES2RWHQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFDFDUXBHJ3NRDJIRACQMTAIZT4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::EventLog:stationName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJZ43ECZ62BEJ3JUXL7EVQTYQ74"),"stationName",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::EventLog:userName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colANWVUMA3FJEFHHZSTOLUH2UQ7A"),"userName",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::EventLog:words-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7OQLFMEPI5GZHACCAX56FIFCCA"),"words",null,org.radixware.kernel.common.enums.EValType.ARR_STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::System::EventLog:messagePreview-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEA2GUFJSQ5COTEOM2REI4CW344"),"messagePreview",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEIXKAYBC3BF6XC36DMJ5V4VETU"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::System::EventLog:isSensitive-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLNECKEY3IJGX7D6OZOFLCAWOOI"),"isSensitive",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3Z5AM6T3I5G37DJZPWGBKA4RO4"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::EventLog:originalSeverity-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFL5O6ILBDZED5LJJILT5XP5LEE"),"originalSeverity",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRUPQP3M7H5BPPPDBFZ6HPJGAZI"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE)
						},

						/*Radix::System::EventLog:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4PWVVN5RV7OBDCLXAALOMT5GDM"),"calcTypeTitle",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("code",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHI4RJ5E4EZDJXFG2EJKG2VTFUM"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRPBGL2SVLRGN3EZK7IX35VBRVM"),"calcMessage",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("code",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQW36E76GXFCBBK6BO45PUDH3NQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("words",org.radixware.kernel.common.enums.EValType.CLOB,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAFYXLEBWNVCN5LTQ3NKKWJAO7Y"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGPP7O3K3K5BFDGR2F7NGSOILNQ"),"export",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("writer",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLRZMHIYOPVCEDCIC6ZYHVPML74")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("nameSpace",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJKMNYSSELBGLXOL457FA7TOVGA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5JDBM3D7SBBYTCO7IJBY4DD2KQ"),"loadEventContext",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("process",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprU3XYHLAVBVEDJJMSRGIBXNT6HY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("in",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5IR4WKEKIRAI7GCTMRHKT42Y2I")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("event",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprB5LUN6HGDRB5JDESOYIGZKENXY"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthX7NCUBE6JRD67BJQ2JBELIXFZE"),"import",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("process",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXMTF7EYMPJDVPFC73W3NSTNJWY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("in",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQZ44VD637FCOXE6I56WIP63HHY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("fileName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDXIV27NIIJEG5EA3V24NNIKXJM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventContextList",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSQB3YZRQ2ZELBFMMQONZKJJPEA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timeZone",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZ2YZOIRWDNGCJHZH5NNEMUJNGQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUA4GEUXFXRHQ7JXI7EMOYCJPUE"),"calcMessage",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("code",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRY7JXTDAYFCY3J6T5XGOQ4F4KM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("words",org.radixware.kernel.common.enums.EValType.CLOB,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYWNXB7NGLFBEPPDTF5VQMHQEAY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("language",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprS2KZBSUEFNHFXOM6PM6STSVJBA"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("raiseTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5QCRRJLZWBADLBBISXYLHONGSU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2OODIRZZJNFJVASPEVS66IA2CU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr35UEQE4GKFGPHID5LU7TYEFSGM"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVQ4ACVK6UBHDDM6MBKSKVN4S5U"),"getContexts",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::System::EventLog - Desktop Executable*/

/*Radix::System::EventLog-Entity Class*/

package org.radixware.ads.System.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog")
public interface EventLog {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

		/*Radix::System::EventLogGroup:contextId:contextId-Presentation Property*/




		public class ContextId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
			public ContextId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogGroup:contextId:contextId")
			public  Str getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogGroup:contextId:contextId")
			public   void setValue(Str val) {
				Value = val;
			}
		}
		public ContextId getContextId(){return (ContextId)getProperty(pgpC5NUYGXTMFDDNKAXRZLD5WCWSE);}

		/*Radix::System::EventLogGroup:contextType:contextType-Presentation Property*/




		public class ContextType extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
			public ContextType(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogGroup:contextType:contextType")
			public  Str getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogGroup:contextType:contextType")
			public   void setValue(Str val) {
				Value = val;
			}
		}
		public ContextType getContextType(){return (ContextType)getProperty(pgpEHMEUTD3OVBXLKTYWCV4ZPCHSE);}









		public org.radixware.ads.System.explorer.EventLog.EventLog_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.System.explorer.EventLog.EventLog_DefaultModel )  super.getEntity(i);}
	}


















































































	/*Radix::System::EventLog:isSensitive:isSensitive-Presentation Property*/


	public class IsSensitive extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsSensitive(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:isSensitive:isSensitive")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:isSensitive:isSensitive")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsSensitive getIsSensitive();
	/*Radix::System::EventLog:user:user-Presentation Property*/


	public class User extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public User(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.User.User_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.explorer.User.User_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.User.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.explorer.User.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:user:user")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:user:user")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public User getUser();
	/*Radix::System::EventLog:station:station-Presentation Property*/


	public class Station extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Station(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.Station.Station_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.explorer.Station.Station_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.Station.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.explorer.Station.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:station:station")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:station:station")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Station getStation();
	/*Radix::System::EventLog:component:component-Presentation Property*/


	public class Component extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Component(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:component:component")
		public  org.radixware.ads.Arte.common.EventSource getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:component:component")
		public   void setValue(org.radixware.ads.Arte.common.EventSource val) {
			Value = val;
		}
	}
	public Component getComponent();
	/*Radix::System::EventLog:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::System::EventLog:raiseTime:raiseTime-Presentation Property*/


	public class RaiseTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public RaiseTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:raiseTime:raiseTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:raiseTime:raiseTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public RaiseTime getRaiseTime();
	/*Radix::System::EventLog:severity:severity-Presentation Property*/


	public class Severity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Severity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:severity:severity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:severity:severity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public Severity getSeverity();
	/*Radix::System::EventLog:commentary:commentary-Presentation Property*/


	public class Commentary extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Commentary(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:commentary:commentary")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:commentary:commentary")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Commentary getCommentary();
	/*Radix::System::EventLog:messagePreview:messagePreview-Presentation Property*/


	public class MessagePreview extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public MessagePreview(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:messagePreview:messagePreview")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:messagePreview:messagePreview")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MessagePreview getMessagePreview();
	/*Radix::System::EventLog:originalSeverity:originalSeverity-Presentation Property*/


	public class OriginalSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public OriginalSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:originalSeverity:originalSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:originalSeverity:originalSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public OriginalSeverity getOriginalSeverity();
	/*Radix::System::EventLog:message:message-Presentation Property*/


	public class Message extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Message(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:message:message")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:message:message")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Message getMessage();
	/*Radix::System::EventLog:typeTitle:typeTitle-Presentation Property*/


	public class TypeTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public TypeTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:typeTitle:typeTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:typeTitle:typeTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public TypeTitle getTypeTitle();


}

/* Radix::System::EventLog - Desktop Meta*/

/*Radix::System::EventLog-Entity Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EventLog_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::System::EventLog:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
			"Radix::System::EventLog",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgCTRW7MLKI7MYB6IOWIL3OXJANAPB2PZD"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVWY625ANRTNRDB5PAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsACX7FP5GUHOBDCLBAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4NVFSS24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsACX7FP5GUHOBDCLBAALOMT5GDM"),28677,

			/*Radix::System::EventLog:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::System::EventLog:component:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPHNPCMDWKPNRDAQSABIFNQAAAE"),
						"component",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5JVFSS24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),
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

						/*Radix::System::EventLog:component:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventLog:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUBCRBDFDL2VDBNSNAAYOJ6SINF"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5FVFSS24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::EventLog:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventLog:raiseTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWBCRBDFDL2VDBNSNAAYOJ6SINF"),
						"raiseTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls45VFSS24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::EventLog:raiseTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime("dd.MM.yyyy hh:mm:ss.zzz",null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventLog:severity:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXBCRBDFDL2VDBNSNAAYOJ6SINF"),
						"severity",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4ZVFSS24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
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

						/*Radix::System::EventLog:severity:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventLog:commentary:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYBCRBDFDL2VDBNSNAAYOJ6SINF"),
						"commentary",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4VVFSS24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::EventLog:commentary:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventLog:message:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFWK7DRDHOXOBDFKUAAMPGXUWTQ"),
						"message",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHEN6MQ3MOXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::System::EventLog:message:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventLog:typeTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVSA77O3TXXNRDB6SAALOMT5GDM"),
						"typeTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4RVFSS24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::EventLog:typeTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventLog:user:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLTPIFVD36VCQ5HSGQAAOJEY4PM"),
						"user",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCHCC4PFU5FHE7CCE7VVTL7G57I"),
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						null,
						null,
						null,
						262143,
						262143,false),

					/*Radix::System::EventLog:station:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNTU5A722VVASNC5GVHVSWDFLKU"),
						"station",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFDFDUXBHJ3NRDJIRACQMTAIZT4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFDFDUXBHJ3NRDJIRACQMTAIZT4"),
						null,
						null,
						null,
						262143,
						262143,false),

					/*Radix::System::EventLog:messagePreview:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEA2GUFJSQ5COTEOM2REI4CW344"),
						"messagePreview",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEIXKAYBC3BF6XC36DMJ5V4VETU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
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
						true,
						null,
						false,

						/*Radix::System::EventLog:messagePreview:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventLog:isSensitive:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLNECKEY3IJGX7D6OZOFLCAWOOI"),
						"isSensitive",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3Z5AM6T3I5G37DJZPWGBKA4RO4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5SQFCWEMTBFBXEQ5SZVJQRQIEA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::EventLog:isSensitive:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventLog:originalSeverity:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFL5O6ILBDZED5LJJILT5XP5LEE"),
						"originalSeverity",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRUPQP3M7H5BPPPDBFZ6HPJGAZI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
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

						/*Radix::System::EventLog:originalSeverity:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			new org.radixware.kernel.common.client.meta.filters.RadFilterDef[]{

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::EventLog:Today-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltUV5CRRHED3OBDCHDAALOMT5GDM"),
						"Today",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6FVFSS24OXOBDFKUAAMPGXUWTQ"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=trunc(Sysdate) \nAND ( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmSF36PORPYBF7ZH2MFL5OQT3LPY\"/></xsc:Item><xsc:Item><xsc:Sql> IS NULL OR </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql>>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmSF36PORPYBF7ZH2MFL5OQT3LPY\"/></xsc:Item><xsc:Item><xsc:Sql>)\nAND ( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmPUKHAASBJJFXDP7WOVZJGKJRVM\"/></xsc:Item><xsc:Item><xsc:Sql> IS NULL OR </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colPHNPCMDWKPNRDAQSABIFNQAAAE\" Owner=\"THIS\" SuppressedWarnings=\"100000\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmPUKHAASBJJFXDP7WOVZJGKJRVM\"/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E")},
						true,
						false,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmSF36PORPYBF7ZH2MFL5OQT3LPY"),
								"minSeverity",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN26A25KERVHDTI3PW736DBBTKA"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBSVWTN5RSVE27G6ZEFVAIUUPLM"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmPUKHAASBJJFXDP7WOVZJGKJRVM"),
								"source",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsINADGYD5DZDXDA56UJSGRVPXBU"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),
								null,
								false,
								false,
								false,
								null,
								false,

								/*Radix::System::EventLog:Today:source:Edit Options:-Edit Mask Enum*/
								new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_VALUE,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEGP2NUKPMZFYVGE5LMYY6ZBJIM"),
								false,
								null,
								null)
						},

						/*Radix::System::EventLog:Today:Editor Pages-Filter Pages*/
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

								/*Radix::System::EventLog:Today:Main-Editor Page*/

								 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgDSV6TMMBXFBJVLR3KVO3NV44KA"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmSF36PORPYBF7ZH2MFL5OQT3LPY"),0,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmPUKHAASBJJFXDP7WOVZJGKJRVM"),1,0,1,false,false)
								},null)
						},
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
							new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgDSV6TMMBXFBJVLR3KVO3NV44KA"))}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::EventLog:TimeRange-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("flt3LGDG7LCCVEP5GK5MQN7UUVELA"),
						"TimeRange",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMZWO3WOGY5CG5PWS4TRQLWDS7Y"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=nvl(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmD7TRMTA7IFEH3M5LD7SUEACI7Y\"/></xsc:Item><xsc:Item><xsc:Sql>, to_date(\'1\', \'J\')))\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>&lt;=decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmP7RHN4373JGJTEIALKA3SCAU5A\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate + 1), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmP7RHN4373JGJTEIALKA3SCAU5A\"/></xsc:Item><xsc:Item><xsc:Sql>)\nAND ( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmBQUVW3USB5C45DVW5IXJCAZU5Q\"/></xsc:Item><xsc:Item><xsc:Sql> IS NULL OR </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql>>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmBQUVW3USB5C45DVW5IXJCAZU5Q\"/></xsc:Item><xsc:Item><xsc:Sql>)\nAND ( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmGXLCPBC4JFHIBE4MRVPYZOARKM\"/></xsc:Item><xsc:Item><xsc:Sql> IS NULL OR </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colPHNPCMDWKPNRDAQSABIFNQAAAE\" Owner=\"THIS\" SuppressedWarnings=\"100000\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmGXLCPBC4JFHIBE4MRVPYZOARKM\"/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA")},
						true,
						false,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmP7RHN4373JGJTEIALKA3SCAU5A"),
								"toTime",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS2JAUBC535A3LC2VYCSLEG3CUY"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mls746RSR5RGNCGFPR4QPS4SVALPM"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmBQUVW3USB5C45DVW5IXJCAZU5Q"),
								"minSeverity",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHL25FQZVOJFMBLCWQ3TVVZE2DE"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO22MN6SUUBHRLNZIHQ7DFVFCV4"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmGXLCPBC4JFHIBE4MRVPYZOARKM"),
								"source",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLEUEUWJICBHHPCJHWCSS65ABKY"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),
								null,
								false,
								false,
								false,
								null,
								false,

								/*Radix::System::EventLog:TimeRange:source:Edit Options:-Edit Mask Enum*/
								new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_VALUE,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEGP2NUKPMZFYVGE5LMYY6ZBJIM"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmD7TRMTA7IFEH3M5LD7SUEACI7Y"),
								"fromTime",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPW6B77D23JAYRCVCHMFW6CFL24"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXSUFLJZNJBGXNC7KZ7XCSFTABQ"),
								false,
								null,
								null)
						},

						/*Radix::System::EventLog:TimeRange:Editor Pages-Filter Pages*/
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

								/*Radix::System::EventLog:TimeRange:Main-Editor Page*/

								 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHXTNCTTHSFCOJNC25XRFG7FUUI"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmP7RHN4373JGJTEIALKA3SCAU5A"),1,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmBQUVW3USB5C45DVW5IXJCAZU5Q"),2,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmGXLCPBC4JFHIBE4MRVPYZOARKM"),3,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmD7TRMTA7IFEH3M5LD7SUEACI7Y"),0,0,1,false,false)
								},null)
						},
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
							new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHXTNCTTHSFCOJNC25XRFG7FUUI"))}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::EventLog:User-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltQUTDPT7CTFGFVJRBQEFMWRTUFI"),
						"User",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLPCH3N4RVRBLPNFAXSJUS2FWSQ"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmMSYXKT4C4ZHSHPYWYXF5IUERA4\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmMSYXKT4C4ZHSHPYWYXF5IUERA4\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>&lt;= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmFCSBJ7FNLFFTPH2GXQAFKZI4QA\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate + 1), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmFCSBJ7FNLFFTPH2GXQAFKZI4QA\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colLTPIFVD36VCQ5HSGQAAOJEY4PM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:EntityRefParameter ParamId=\"prmOJRL3DEB6FFVXLHX6JJHF2RQW4\" ReferencedTableId=\"tblSY4KIOLTGLNRDHRZABQAQH3XQ4\" PidTranslationMode=\"PrimaryKeyProps\"/></xsc:Item><xsc:Item><xsc:Sql>\nAND ( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmG3NAYPEU3FBELFTF2VTLCGGAG4\"/></xsc:Item><xsc:Item><xsc:Sql> IS NULL OR </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql>>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmG3NAYPEU3FBELFTF2VTLCGGAG4\"/></xsc:Item><xsc:Item><xsc:Sql>)\n</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA")},
						false,
						false,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmOJRL3DEB6FFVXLHX6JJHF2RQW4"),
								"pUser",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNWO3EX76XRASXAGH2LXY2XDJ3I"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
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
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZONO647CNRGC5O5GHBHD6F7C7A")),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmG3NAYPEU3FBELFTF2VTLCGGAG4"),
								"minSeverity",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKKFH32WXAVAHHHZR3CJL7GWCUU"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZYO4N235ZZBXPAWMRGPDDDBSGA"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmMSYXKT4C4ZHSHPYWYXF5IUERA4"),
								"fromDate",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX3UKUR4JQJCKPFQ5Q3GXSHPB5Y"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGXNMWXVV45HLXOCKYE4ZXW4EDU"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmFCSBJ7FNLFFTPH2GXQAFKZI4QA"),
								"toDate",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN4LG75A4JZE7ZJF3XBB6DXH2RQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsACCVK3XLOFADBJ4ZO7MUFL7V7A"),
								false,
								null,
								null)
						},

						/*Radix::System::EventLog:User:Editor Pages-Filter Pages*/
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

								/*Radix::System::EventLog:User:Main-Editor Page*/

								 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5QRXYYRF25BR3EP6ORGPK3TYY4"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmG3NAYPEU3FBELFTF2VTLCGGAG4"),1,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmOJRL3DEB6FFVXLHX6JJHF2RQW4"),0,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmMSYXKT4C4ZHSHPYWYXF5IUERA4"),2,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmFCSBJ7FNLFFTPH2GXQAFKZI4QA"),3,0,1,false,false)
								},null)
						},
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
							new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5QRXYYRF25BR3EP6ORGPK3TYY4"))}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::EventLog:Station-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltM3WETXHKOZHYBIO63RKBNXMZVY"),
						"Station",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBR3XPTC47NG5XIA4KE3TK2SBUQ"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmSVDGDRZILFA2JKFGXLVRXYLQ3Q\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmSVDGDRZILFA2JKFGXLVRXYLQ3Q\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> &lt;= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmREAJESHPOVESTNOIMUXQCVNZQQ\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate + 1), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmREAJESHPOVESTNOIMUXQCVNZQQ\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colNTU5A722VVASNC5GVHVSWDFLKU\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> in (</xsc:Sql></xsc:Item><xsc:Item><xsc:EntityRefParameter ParamId=\"prm2UCNYUDK2VGYVOUZKJINQQY4U4\" ReferencedTableId=\"tblFDFDUXBHJ3NRDJIRACQMTAIZT4\" PidTranslationMode=\"PrimaryKeyProps\"/></xsc:Item><xsc:Item><xsc:Sql>)\nAND ( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmFOQJM4EYHZGABACHFRQTRYHP5M\"/></xsc:Item><xsc:Item><xsc:Sql> IS NULL OR </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql>>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmFOQJM4EYHZGABACHFRQTRYHP5M\"/></xsc:Item><xsc:Item><xsc:Sql>)\n</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA")},
						false,
						false,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm2UCNYUDK2VGYVOUZKJINQQY4U4"),
								"pStation",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4FDJ7ECZA5F3FDV6DCR6DUSCQM"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
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
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFDFDUXBHJ3NRDJIRACQMTAIZT4"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("spr3NY6WBNBT7NRDB56AALOMT5GDM")),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmFOQJM4EYHZGABACHFRQTRYHP5M"),
								"minSeverity",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTPFSQXY3ARHHPJ7ZP63W3NOHYU"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZYHZPE7AZNH4FJYACQKESSTGOA"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmSVDGDRZILFA2JKFGXLVRXYLQ3Q"),
								"fromDate",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAAWKAIWIT5H7DHTLZZVKYGROFY"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCFZ2WULHNNFVLAFZMMMMHC6QYY"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmREAJESHPOVESTNOIMUXQCVNZQQ"),
								"toDate",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZTA2SKREZJGLPKWI4ZYSNTLPMM"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWZH7TOE7FNHOPKKVPJZMXFRVOA"),
								false,
								null,
								null)
						},

						/*Radix::System::EventLog:Station:Editor Pages-Filter Pages*/
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

								/*Radix::System::EventLog:Station:Main-Editor Page*/

								 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgZZFUHTMSPBHMRFSH4OZKY5CAGQ"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm2UCNYUDK2VGYVOUZKJINQQY4U4"),0,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmFOQJM4EYHZGABACHFRQTRYHP5M"),1,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmSVDGDRZILFA2JKFGXLVRXYLQ3Q"),2,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmREAJESHPOVESTNOIMUXQCVNZQQ"),3,0,1,false,false)
								},null)
						},
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
							new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgZZFUHTMSPBHMRFSH4OZKY5CAGQ"))}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::EventLog:Severity-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltBYGYNUVM4JBGZO3KC6O7C5I65Y"),
						"Severity",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOKNP5QK2SRHJ3K4C4L2PO3JS5Y"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmN3KFLDVQ3FEKNFRJQAXANO3QJE\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmN3KFLDVQ3FEKNFRJQAXANO3QJE\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> &lt;= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmA6ZXQA6P65ER5KEZL7S5HAF2MU\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate + 1), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmA6ZXQA6P65ER5KEZL7S5HAF2MU\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand (</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmEZYWX2UJTBFEHJXFY4U24PVUMI\"/></xsc:Item><xsc:Item><xsc:Sql> or </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmEZYWX2UJTBFEHJXFY4U24PVUMI\"/></xsc:Item><xsc:Item><xsc:Sql> is null)\nand (</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>&lt;=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm4LJSDT4XNVAPJL66DQZJTUO2P4\"/></xsc:Item><xsc:Item><xsc:Sql> or </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm4LJSDT4XNVAPJL66DQZJTUO2P4\"/></xsc:Item><xsc:Item><xsc:Sql> is null)</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E")},
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmEZYWX2UJTBFEHJXFY4U24PVUMI"),
								"minSeverity",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mls34JM3AK2FRBNRFILS4YZW4Y7OA"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQZAKPB2ZWZF77ABOO5EQ52ZEOY"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmN3KFLDVQ3FEKNFRJQAXANO3QJE"),
								"fromDate",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKEPHPK6KTRCWFN2Y5A2X3QBY6M"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLYQO5D6XXZBVPDWPNCNPW2PEMQ"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmA6ZXQA6P65ER5KEZL7S5HAF2MU"),
								"toDate",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCYMVUJJQGJC5TICEGRTTHPP5IA"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAQVR57CHU5C5TLVCA5NUOHPN5I"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm4LJSDT4XNVAPJL66DQZJTUO2P4"),
								"maxSeverity",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBCYDW4725NCC7JJAE5Z2DQ7F6U"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBHTXCAVAXBGLTFKEO6FT6IFAZ4"),
								false,
								null,
								null)
						},

						/*Radix::System::EventLog:Severity:Editor Pages-Filter Pages*/
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

								/*Radix::System::EventLog:Severity:Main-Editor Page*/

								 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg4LNBTXV56NG3HMZQTHUOG6AJQU"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmEZYWX2UJTBFEHJXFY4U24PVUMI"),2,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmN3KFLDVQ3FEKNFRJQAXANO3QJE"),0,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmA6ZXQA6P65ER5KEZL7S5HAF2MU"),1,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm4LJSDT4XNVAPJL66DQZJTUO2P4"),3,0,1,false,false)
								},null)
						},
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
							new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg4LNBTXV56NG3HMZQTHUOG6AJQU"))}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::EventLog:Imported-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltS5F72ARLKFBGJLCKEX27ZDUJR4"),
						"Imported",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNXWOLHNIBRARBFF4T3TDE3HZFE"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>exists (\n    select 1 from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblJABJB4OAP3NRDAQSABIFNQAAAE\"/></xsc:Item><xsc:Item><xsc:Sql> where \n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblJABJB4OAP3NRDAQSABIFNQAAAE\" PropId=\"colXHA62NOBP3NRDAQSABIFNQAAAE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colUBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> and\n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblJABJB4OAP3NRDAQSABIFNQAAAE\" PropId=\"colOSK5SVWBP3NRDAQSABIFNQAAAE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> and\n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJABJB4OAP3NRDAQSABIFNQAAAE\" PropId=\"colT67W77OAP3NRDAQSABIFNQAAAE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:ConstValue EnumId=\"acsPVLIUELSKTNRDAQSABIFNQAAAE\" ItemId=\"aciGBCHKTBTCJEZ5G6VKA5IWHC4VM\"/></xsc:Item><xsc:Item><xsc:Sql> and\n    upper(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJABJB4OAP3NRDAQSABIFNQAAAE\" PropId=\"colHIPN4HGBP3NRDAQSABIFNQAAAE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>) like upper(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmONPRLRODY5BFPM62Q72R53JXLY\"/></xsc:Item><xsc:Item><xsc:Sql>)\n)\nand (</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmRUGE7VE44ND25EYXVNNA6LDPZA\"/></xsc:Item><xsc:Item><xsc:Sql> or </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmRUGE7VE44ND25EYXVNNA6LDPZA\"/></xsc:Item><xsc:Item><xsc:Sql> is null)</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E")},
						true,
						false,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmONPRLRODY5BFPM62Q72R53JXLY"),
								"pFile",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUIJ2UTOOZJF2DAFIJX55A7ZR2E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
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
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmRUGE7VE44ND25EYXVNNA6LDPZA"),
								"minSeverity",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP3667UYCKBAKHNSBPGATFZ6DLU"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUV52DU3SRNDEBIJTM3BA2627CA"),
								false,
								null,
								null)
						},

						/*Radix::System::EventLog:Imported:Editor Pages-Filter Pages*/
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

								/*Radix::System::EventLog:Imported:Main-Editor Page*/

								 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgN62QXPLQZNHTRFXSVZRHYS24LE"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmONPRLRODY5BFPM62Q72R53JXLY"),0,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmRUGE7VE44ND25EYXVNNA6LDPZA"),1,0,1,false,false)
								},null)
						},
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
							new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgN62QXPLQZNHTRFXSVZRHYS24LE"))}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::EventLog:EventCodes-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltLFNJNPTVBJDWJDA57KRSGBRE2E"),
						"EventCodes",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUKQQ4VHYWFB2NGCNUXJYM6WG5M"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm2P723KWXQJA7DJ5E4OY7CNXRK4\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm2P723KWXQJA7DJ5E4OY7CNXRK4\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> &lt;= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmINQENZUDPJFU3H2I5H6JDKAFWQ\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate + 1), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmINQENZUDPJFU3H2I5H6JDKAFWQ\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand (</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmXO3CGUSUW5BZLBZ6NRCTNV577E\"/></xsc:Item><xsc:Item><xsc:Sql> is NULL or INSTR( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmXO3CGUSUW5BZLBZ6NRCTNV577E\"/></xsc:Item><xsc:Item><xsc:Sql>, </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colKZ33LLHXVHWDBROXAAIT4AGD7E\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> )>0)</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						null,
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmXO3CGUSUW5BZLBZ6NRCTNV577E"),
								"eventCodes",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTHHNLUH6HZAGVIABUSFMVQ5WOQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								false,
								false,
								true,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeXGPX2QQOIVANBK6FXI2GY4U4D4"),
								true,
								null,
								null,
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm2P723KWXQJA7DJ5E4OY7CNXRK4"),
								"fromDate",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC4QADU6X4NBBZFY2MBFJPMFSBM"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7GDC3KEO2ZCLZM7BCJCZNDBEAY"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmINQENZUDPJFU3H2I5H6JDKAFWQ"),
								"toDate",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHF3NOFXHRZDHPD2I2B5PJO76OQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBGC5CJNZM5G2XITJKXEWJPQPTQ"),
								false,
								null,
								null)
						},

						/*Radix::System::EventLog:EventCodes:Editor Pages-Filter Pages*/
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

								/*Radix::System::EventLog:EventCodes:General-Editor Page*/

								 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUBGO7YQB4RFCTD2J2HKPEEVNQ4"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm2P723KWXQJA7DJ5E4OY7CNXRK4"),0,1,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmINQENZUDPJFU3H2I5H6JDKAFWQ"),1,1,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmXO3CGUSUW5BZLBZ6NRCTNV577E"),0,0,2,false,false)
								},null)
						},
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
							new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUBGO7YQB4RFCTD2J2HKPEEVNQ4"))}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::EventLog:Period-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltTYT3ALDZTNAYDDFDPTL7AFYGUU"),
						"Period",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBOEVCBAVXVBKXKBYE7W2KPQ6OU"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:IfParam ParamId=\"prmDVPXQQQ72VHPTELUBU5GXJALB4\" Operation=\"Equal\"><xsc:Value>1</xsc:Value></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=trunc(Sysdate)\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmDVPXQQQ72VHPTELUBU5GXJALB4\" Operation=\"Equal\"><xsc:Value>2</xsc:Value></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=trunc(Sysdate-1)\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmDVPXQQQ72VHPTELUBU5GXJALB4\" Operation=\"Equal\"><xsc:Value>3</xsc:Value></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=trunc(Sysdate-7)\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmDVPXQQQ72VHPTELUBU5GXJALB4\" Operation=\"Equal\"><xsc:Value>4</xsc:Value></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=trunc(add_months(Sysdate,-1))\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmDVPXQQQ72VHPTELUBU5GXJALB4\" Operation=\"Equal\"><xsc:Value>5</xsc:Value></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    (</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=nvl(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmNKTTHR6EAJB6FAJGHIDNIW3PHI\"/></xsc:Item><xsc:Item><xsc:Sql>, to_date(\'1\', \'J\')))\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmXOAKGU7PA5CSFDQACAVI2U3RCM\" Operation=\"Null\"/></xsc:Item><xsc:Item><xsc:Sql>\nAND </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> &lt; trunc(sysdate + 1)\n</xsc:Sql></xsc:Item><xsc:Item><xsc:ElseIf/></xsc:Item><xsc:Item><xsc:Sql>\nAND </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>&lt;=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmXOAKGU7PA5CSFDQACAVI2U3RCM\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmAIGN2LJXGVAHRPELFC4H6UAMKY\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    AND </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql>>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmAIGN2LJXGVAHRPELFC4H6UAMKY\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmTR6BP77HZRADZJKBAN6JVLZYB4\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    AND </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql>&lt;=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmTR6BP77HZRADZJKBAN6JVLZYB4\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prm6AHGQBB5HFBEVNUUP6AZ435PRU\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    AND </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colPHNPCMDWKPNRDAQSABIFNQAAAE\" Owner=\"THIS\" SuppressedWarnings=\"100000\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm6AHGQBB5HFBEVNUUP6AZ435PRU\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmQSMXFK73ZZHKNLPYAIYGS56DXQ\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    AND </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colLTPIFVD36VCQ5HSGQAAOJEY4PM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:EntityRefParameter ParamId=\"prmQSMXFK73ZZHKNLPYAIYGS56DXQ\" ReferencedTableId=\"tblSY4KIOLTGLNRDHRZABQAQH3XQ4\" PidTranslationMode=\"PrimaryKeyProps\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prm4A2P5FE5AZGFTMSTY7E5CPD7I4\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    AND </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colNTU5A722VVASNC5GVHVSWDFLKU\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:EntityRefParameter ParamId=\"prm4A2P5FE5AZGFTMSTY7E5CPD7I4\" ReferencedTableId=\"tblFDFDUXBHJ3NRDJIRACQMTAIZT4\" PidTranslationMode=\"PrimaryKeyProps\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prm6FBEUOOMAFDVLN3J3TI737U5Q4\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    AND INSTR( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm6FBEUOOMAFDVLN3J3TI737U5Q4\"/></xsc:Item><xsc:Item><xsc:Sql>, </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colKZ33LLHXVHWDBROXAAIT4AGD7E\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> )>0\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmVND4W5T4GJCDVDMMEM7ZFG7SZU\" Operation=\"NotEqual\"><xsc:Value/></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    AND UPPER( </xsc:Sql></xsc:Item><xsc:Item><xsc:DbFuncCall FuncId=\"dfnMJOIKNAEUREC5CYMMNQLMBJAFM\"/></xsc:Item><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colKZ33LLHXVHWDBROXAAIT4AGD7E\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> , </xsc:Sql></xsc:Item><xsc:Item><xsc:DbFuncCall FuncId=\"dfnLMUSM5GXCTOBDCKFAAMPGXSZKU\"/></xsc:Item><xsc:Item><xsc:Sql>(), </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colVBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>) ) LIKE \'%\' || UPPER(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmVND4W5T4GJCDVDMMEM7ZFG7SZU\"/></xsc:Item><xsc:Item><xsc:Sql>) || \'%\'\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmI5PWPY5VHNAPXLI3J2TIDTRKOI\" Operation=\"NotEqual\"><xsc:Value/></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    AND UPPER( </xsc:Sql></xsc:Item><xsc:Item><xsc:DbFuncCall FuncId=\"dfnJ2PTRZ7GVZBMHN5USDST6QGLWI\"/></xsc:Item><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colKZ33LLHXVHWDBROXAAIT4AGD7E\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>,  </xsc:Sql></xsc:Item><xsc:Item><xsc:DbFuncCall FuncId=\"dfnLMUSM5GXCTOBDCKFAAMPGXSZKU\"/></xsc:Item><xsc:Item><xsc:Sql>(), </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colVBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>) ) NOT LIKE \'%\' || UPPER(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmI5PWPY5VHNAPXLI3J2TIDTRKOI\"/></xsc:Item><xsc:Item><xsc:Sql>) || \'%\'\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item></xsc:Sqml>",
						null,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA")},
						false,
						false,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm6AHGQBB5HFBEVNUUP6AZ435PRU"),
								"source",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSO4TKWSEGREVZJRMOSBWA2AOGE"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),
								null,
								false,
								false,
								false,
								null,
								false,

								/*Radix::System::EventLog:Period:source:Edit Options:-Edit Mask Enum*/
								new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_VALUE,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDCWXD43GEBD5LDQOHP7GA5W6WI"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmAIGN2LJXGVAHRPELFC4H6UAMKY"),
								"minSeverity",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKB3KWU57LBBQ7G577WSZGHUOV4"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWQE2U26P4FBV7DKELXK2SD52MU"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmDVPXQQQ72VHPTELUBU5GXJALB4"),
								"period",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT6OLEABUYZEAZF3WSVRKVK5I7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								null,
								org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
								true,
								false,
								false,
								null,
								false,

								/*Radix::System::EventLog:Period:period:Edit Options:-Edit Mask List*/
								new org.radixware.kernel.common.client.meta.mask.EditMaskList(new org.radixware.kernel.common.client.meta.mask.EditMaskList.Item[]{

										new org.radixware.kernel.common.client.meta.mask.EditMaskList.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecACQVZOMVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPJP3PHRTGNEDBBR6QRS2U4RKRM"),org.radixware.kernel.common.defs.value.ValAsStr.fromStr("1",org.radixware.kernel.common.enums.EValType.INT)),

										new org.radixware.kernel.common.client.meta.mask.EditMaskList.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecACQVZOMVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls66CEJDVR6RDN7OD6HQ4TVVF3JM"),org.radixware.kernel.common.defs.value.ValAsStr.fromStr("2",org.radixware.kernel.common.enums.EValType.INT)),

										new org.radixware.kernel.common.client.meta.mask.EditMaskList.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecACQVZOMVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS65VEIXKPRA43HEXE6JL2GC5YE"),org.radixware.kernel.common.defs.value.ValAsStr.fromStr("3",org.radixware.kernel.common.enums.EValType.INT)),

										new org.radixware.kernel.common.client.meta.mask.EditMaskList.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecACQVZOMVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLKLGWZDJEFBRNE2SHBJV26FFII"),org.radixware.kernel.common.defs.value.ValAsStr.fromStr("4",org.radixware.kernel.common.enums.EValType.INT)),

										new org.radixware.kernel.common.client.meta.mask.EditMaskList.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecACQVZOMVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVXH3CLQNQ5CNXENVVSJ3MC4OQU"),org.radixware.kernel.common.defs.value.ValAsStr.fromStr("5",org.radixware.kernel.common.enums.EValType.INT))
								}),
								null,
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmTR6BP77HZRADZJKBAN6JVLZYB4"),
								"maxSeverity",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZP6424GWQNA6JIVGIDNLP6R45U"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBGJFJXPVMNHJTNWYWYRTJKW5T4"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmXOAKGU7PA5CSFDQACAVI2U3RCM"),
								"toTime",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHCBHLHP3O5FQFBDYQU6TLXGO7Y"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYVJBU4HKIFCJTIZOBSTDII3P6I"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmNKTTHR6EAJB6FAJGHIDNIW3PHI"),
								"fromTime",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQWFCYFBDPJBAVHU2XQPGECKHJQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOG5FEPSR5ZG57I3UMBK3SKIO2Q"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmQSMXFK73ZZHKNLPYAIYGS56DXQ"),
								"user",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCIACSRHOFJG4JOQRKR4LNECNPU"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.PARENT_REF,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH6FAIQSDZZBFVFVPFU5KJGY2PE"),
								false,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZONO647CNRGC5O5GHBHD6F7C7A")),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm4A2P5FE5AZGFTMSTY7E5CPD7I4"),
								"station",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUR4DB7VH5ZBYRJDDPOODD4WJCI"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.PARENT_REF,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWGZVU3F4ZJFK5G42SSYWCYGILI"),
								false,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFDFDUXBHJ3NRDJIRACQMTAIZT4"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("spr3NY6WBNBT7NRDB56AALOMT5GDM")),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm6FBEUOOMAFDVLN3J3TI737U5Q4"),
								"eventCodes",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGBPBXS7FXZAHBENS3IQRJZWBNQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								false,
								false,
								true,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeJPPYKYT4CVBFXJTNYI5YCA4IQQ"),
								true,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVJRANVOK25BXVBXSXFEZSDCAQE"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmVND4W5T4GJCDVDMMEM7ZFG7SZU"),
								"includedText",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZW7SV6PBORBPRKVL6GYRYMDZ44"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								false,
								false,
								false,
								null,
								false,

								/*Radix::System::EventLog:Period:includedText:Edit Options:-Edit Mask Str*/
								new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1999,true),
								null,
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmI5PWPY5VHNAPXLI3J2TIDTRKOI"),
								"excludedText",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZIU2N36B3ZDTJPKD5CS4STD7HY"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								false,
								false,
								false,
								null,
								false,

								/*Radix::System::EventLog:Period:excludedText:Edit Options:-Edit Mask Str*/
								new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1999,true),
								null,
								false,
								null,
								null)
						},

						/*Radix::System::EventLog:Period:Editor Pages-Filter Pages*/
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

								/*Radix::System::EventLog:Period:Main-Editor Page*/

								 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg6TB3ZIGLOBEYDKBHNST77UURYI"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmDVPXQQQ72VHPTELUBU5GXJALB4"),0,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmNKTTHR6EAJB6FAJGHIDNIW3PHI"),1,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmXOAKGU7PA5CSFDQACAVI2U3RCM"),2,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm6FBEUOOMAFDVLN3J3TI737U5Q4"),0,1,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmTR6BP77HZRADZJKBAN6JVLZYB4"),2,1,1,true,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmAIGN2LJXGVAHRPELFC4H6UAMKY"),1,1,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm6AHGQBB5HFBEVNUUP6AZ435PRU"),0,2,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm4A2P5FE5AZGFTMSTY7E5CPD7I4"),2,2,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmQSMXFK73ZZHKNLPYAIYGS56DXQ"),1,2,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmVND4W5T4GJCDVDMMEM7ZFG7SZU"),0,3,3,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmI5PWPY5VHNAPXLI3J2TIDTRKOI"),0,4,3,false,false)
								},null)
						},
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
							new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg6TB3ZIGLOBEYDKBHNST77UURYI"))}
						,
						null)
			},
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::System::EventLog:TimeDesc-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
						"TimeDesc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3M5UBSD64DORDOFEABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWBCRBDFDL2VDBNSNAAYOJ6SINF"),
								true),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUBCRBDFDL2VDBNSNAAYOJ6SINF"),
								true)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::System::EventLog:TimeAsc-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E"),
						"TimeAsc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6FQGPWH6UBH3TG4MUOPHDCVGHE"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWBCRBDFDL2VDBNSNAAYOJ6SINF"),
								false),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUBCRBDFDL2VDBNSNAAYOJ6SINF"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref5TCE2JPJ4FGJDE7YJT3ES2RWHQ"),"EventLog=>Station (stationName=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFDFDUXBHJ3NRDJIRACQMTAIZT4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colJZ43ECZ62BEJ3JUXL7EVQTYQ74")},new String[]{"stationName"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colFTFDUXBHJ3NRDJIRACQMTAIZT4")},new String[]{"name"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refJIYJJX6MP3NRDAQSABIFNQAAAE"),"EventLog=>User (receiptor=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colKUM7R4BBZLWDRRGGAAIT4AGD7E")},new String[]{"receiptor"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4")},new String[]{"name"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refL4N6UFTR3RDR7KPNKMMHZ2K6J4"),"EventLog=>User (userName=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colANWVUMA3FJEFHHZSTOLUH2UQ7A")},new String[]{"userName"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4")},new String[]{"name"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEEUTDHGEXTNRDB6RAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprF6ONI4OEXTNRDB6RAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprHH4BKAI7UVEO3OS4I5AHCHKN3A"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVWY625ANRTNRDB5PAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprW7YW3HO6WLOBDCLZAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprKQ5WOSDJZ5AQFOGPY4PK3O2XTA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprY2GNJQQ7IRBLDKX4NEFOMP4J5A"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprDEZXGYRBYVBADE5OCIBI73JN2I"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprE4HR7KORYZDQBAP4O5XMDBLIPE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7X6MMAXKTZHUHB46WUW333PMMA")},
			false,false,false);
}

/* Radix::System::EventLog - Web Executable*/

/*Radix::System::EventLog-Entity Class*/

package org.radixware.ads.System.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog")
public interface EventLog {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

		/*Radix::System::EventLogGroup:contextId:contextId-Presentation Property*/




		public class ContextId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
			public ContextId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogGroup:contextId:contextId")
			public  Str getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogGroup:contextId:contextId")
			public   void setValue(Str val) {
				Value = val;
			}
		}
		public ContextId getContextId(){return (ContextId)getProperty(pgpC5NUYGXTMFDDNKAXRZLD5WCWSE);}

		/*Radix::System::EventLogGroup:contextType:contextType-Presentation Property*/




		public class ContextType extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
			public ContextType(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogGroup:contextType:contextType")
			public  Str getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogGroup:contextType:contextType")
			public   void setValue(Str val) {
				Value = val;
			}
		}
		public ContextType getContextType(){return (ContextType)getProperty(pgpEHMEUTD3OVBXLKTYWCV4ZPCHSE);}









		public org.radixware.ads.System.web.EventLog.EventLog_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.System.web.EventLog.EventLog_DefaultModel )  super.getEntity(i);}
	}


















































































	/*Radix::System::EventLog:isSensitive:isSensitive-Presentation Property*/


	public class IsSensitive extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsSensitive(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:isSensitive:isSensitive")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:isSensitive:isSensitive")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsSensitive getIsSensitive();
	/*Radix::System::EventLog:user:user-Presentation Property*/


	public class User extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public User(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.web.User.User_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.web.User.User_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.web.User.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.web.User.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:user:user")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:user:user")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public User getUser();
	/*Radix::System::EventLog:station:station-Presentation Property*/


	public class Station extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Station(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.web.Station.Station_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.web.Station.Station_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.web.Station.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.web.Station.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:station:station")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:station:station")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Station getStation();
	/*Radix::System::EventLog:component:component-Presentation Property*/


	public class Component extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Component(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:component:component")
		public  org.radixware.ads.Arte.common.EventSource getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:component:component")
		public   void setValue(org.radixware.ads.Arte.common.EventSource val) {
			Value = val;
		}
	}
	public Component getComponent();
	/*Radix::System::EventLog:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::System::EventLog:raiseTime:raiseTime-Presentation Property*/


	public class RaiseTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public RaiseTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:raiseTime:raiseTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:raiseTime:raiseTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public RaiseTime getRaiseTime();
	/*Radix::System::EventLog:severity:severity-Presentation Property*/


	public class Severity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Severity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:severity:severity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:severity:severity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public Severity getSeverity();
	/*Radix::System::EventLog:commentary:commentary-Presentation Property*/


	public class Commentary extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Commentary(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:commentary:commentary")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:commentary:commentary")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Commentary getCommentary();
	/*Radix::System::EventLog:messagePreview:messagePreview-Presentation Property*/


	public class MessagePreview extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public MessagePreview(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:messagePreview:messagePreview")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:messagePreview:messagePreview")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MessagePreview getMessagePreview();
	/*Radix::System::EventLog:originalSeverity:originalSeverity-Presentation Property*/


	public class OriginalSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public OriginalSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:originalSeverity:originalSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:originalSeverity:originalSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public OriginalSeverity getOriginalSeverity();
	/*Radix::System::EventLog:message:message-Presentation Property*/


	public class Message extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Message(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:message:message")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:message:message")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Message getMessage();
	/*Radix::System::EventLog:typeTitle:typeTitle-Presentation Property*/


	public class TypeTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public TypeTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:typeTitle:typeTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:typeTitle:typeTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public TypeTitle getTypeTitle();


}

/* Radix::System::EventLog - Web Meta*/

/*Radix::System::EventLog-Entity Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EventLog_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::System::EventLog:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
			"Radix::System::EventLog",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgCTRW7MLKI7MYB6IOWIL3OXJANAPB2PZD"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVWY625ANRTNRDB5PAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsACX7FP5GUHOBDCLBAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4NVFSS24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsACX7FP5GUHOBDCLBAALOMT5GDM"),28677,

			/*Radix::System::EventLog:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::System::EventLog:component:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPHNPCMDWKPNRDAQSABIFNQAAAE"),
						"component",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5JVFSS24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),
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

						/*Radix::System::EventLog:component:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventLog:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUBCRBDFDL2VDBNSNAAYOJ6SINF"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5FVFSS24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::EventLog:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventLog:raiseTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWBCRBDFDL2VDBNSNAAYOJ6SINF"),
						"raiseTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls45VFSS24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::EventLog:raiseTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime("dd.MM.yyyy hh:mm:ss.zzz",null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventLog:severity:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXBCRBDFDL2VDBNSNAAYOJ6SINF"),
						"severity",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4ZVFSS24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
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

						/*Radix::System::EventLog:severity:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventLog:commentary:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYBCRBDFDL2VDBNSNAAYOJ6SINF"),
						"commentary",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4VVFSS24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::EventLog:commentary:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventLog:message:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFWK7DRDHOXOBDFKUAAMPGXUWTQ"),
						"message",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHEN6MQ3MOXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
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

						/*Radix::System::EventLog:message:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventLog:typeTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVSA77O3TXXNRDB6SAALOMT5GDM"),
						"typeTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4RVFSS24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::EventLog:typeTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventLog:user:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLTPIFVD36VCQ5HSGQAAOJEY4PM"),
						"user",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCHCC4PFU5FHE7CCE7VVTL7G57I"),
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						null,
						null,
						null,
						262143,
						262143,false),

					/*Radix::System::EventLog:station:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNTU5A722VVASNC5GVHVSWDFLKU"),
						"station",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFDFDUXBHJ3NRDJIRACQMTAIZT4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFDFDUXBHJ3NRDJIRACQMTAIZT4"),
						null,
						null,
						null,
						262143,
						262143,false),

					/*Radix::System::EventLog:messagePreview:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEA2GUFJSQ5COTEOM2REI4CW344"),
						"messagePreview",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEIXKAYBC3BF6XC36DMJ5V4VETU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
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
						true,
						null,
						false,

						/*Radix::System::EventLog:messagePreview:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventLog:isSensitive:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLNECKEY3IJGX7D6OZOFLCAWOOI"),
						"isSensitive",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3Z5AM6T3I5G37DJZPWGBKA4RO4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5SQFCWEMTBFBXEQ5SZVJQRQIEA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::EventLog:isSensitive:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventLog:originalSeverity:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFL5O6ILBDZED5LJJILT5XP5LEE"),
						"originalSeverity",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRUPQP3M7H5BPPPDBFZ6HPJGAZI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
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

						/*Radix::System::EventLog:originalSeverity:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			new org.radixware.kernel.common.client.meta.filters.RadFilterDef[]{

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::EventLog:Today-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltUV5CRRHED3OBDCHDAALOMT5GDM"),
						"Today",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6FVFSS24OXOBDFKUAAMPGXUWTQ"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=trunc(Sysdate) \nAND ( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmSF36PORPYBF7ZH2MFL5OQT3LPY\"/></xsc:Item><xsc:Item><xsc:Sql> IS NULL OR </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql>>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmSF36PORPYBF7ZH2MFL5OQT3LPY\"/></xsc:Item><xsc:Item><xsc:Sql>)\nAND ( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmPUKHAASBJJFXDP7WOVZJGKJRVM\"/></xsc:Item><xsc:Item><xsc:Sql> IS NULL OR </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colPHNPCMDWKPNRDAQSABIFNQAAAE\" Owner=\"THIS\" SuppressedWarnings=\"100000\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmPUKHAASBJJFXDP7WOVZJGKJRVM\"/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E")},
						true,
						false,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmSF36PORPYBF7ZH2MFL5OQT3LPY"),
								"minSeverity",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN26A25KERVHDTI3PW736DBBTKA"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBSVWTN5RSVE27G6ZEFVAIUUPLM"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmPUKHAASBJJFXDP7WOVZJGKJRVM"),
								"source",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsINADGYD5DZDXDA56UJSGRVPXBU"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),
								null,
								false,
								false,
								false,
								null,
								false,

								/*Radix::System::EventLog:Today:source:Edit Options:-Edit Mask Enum*/
								new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_VALUE,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEGP2NUKPMZFYVGE5LMYY6ZBJIM"),
								false,
								null,
								null)
						},

						/*Radix::System::EventLog:Today:Editor Pages-Filter Pages*/
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

								/*Radix::System::EventLog:Today:Main-Editor Page*/

								 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgDSV6TMMBXFBJVLR3KVO3NV44KA"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmSF36PORPYBF7ZH2MFL5OQT3LPY"),0,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmPUKHAASBJJFXDP7WOVZJGKJRVM"),1,0,1,false,false)
								},null)
						},
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
							new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgDSV6TMMBXFBJVLR3KVO3NV44KA"))}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::EventLog:TimeRange-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("flt3LGDG7LCCVEP5GK5MQN7UUVELA"),
						"TimeRange",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMZWO3WOGY5CG5PWS4TRQLWDS7Y"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=nvl(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmD7TRMTA7IFEH3M5LD7SUEACI7Y\"/></xsc:Item><xsc:Item><xsc:Sql>, to_date(\'1\', \'J\')))\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>&lt;=decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmP7RHN4373JGJTEIALKA3SCAU5A\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate + 1), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmP7RHN4373JGJTEIALKA3SCAU5A\"/></xsc:Item><xsc:Item><xsc:Sql>)\nAND ( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmBQUVW3USB5C45DVW5IXJCAZU5Q\"/></xsc:Item><xsc:Item><xsc:Sql> IS NULL OR </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql>>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmBQUVW3USB5C45DVW5IXJCAZU5Q\"/></xsc:Item><xsc:Item><xsc:Sql>)\nAND ( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmGXLCPBC4JFHIBE4MRVPYZOARKM\"/></xsc:Item><xsc:Item><xsc:Sql> IS NULL OR </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colPHNPCMDWKPNRDAQSABIFNQAAAE\" Owner=\"THIS\" SuppressedWarnings=\"100000\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmGXLCPBC4JFHIBE4MRVPYZOARKM\"/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA")},
						true,
						false,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmP7RHN4373JGJTEIALKA3SCAU5A"),
								"toTime",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS2JAUBC535A3LC2VYCSLEG3CUY"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mls746RSR5RGNCGFPR4QPS4SVALPM"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmBQUVW3USB5C45DVW5IXJCAZU5Q"),
								"minSeverity",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHL25FQZVOJFMBLCWQ3TVVZE2DE"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO22MN6SUUBHRLNZIHQ7DFVFCV4"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmGXLCPBC4JFHIBE4MRVPYZOARKM"),
								"source",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLEUEUWJICBHHPCJHWCSS65ABKY"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),
								null,
								false,
								false,
								false,
								null,
								false,

								/*Radix::System::EventLog:TimeRange:source:Edit Options:-Edit Mask Enum*/
								new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_VALUE,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEGP2NUKPMZFYVGE5LMYY6ZBJIM"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmD7TRMTA7IFEH3M5LD7SUEACI7Y"),
								"fromTime",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPW6B77D23JAYRCVCHMFW6CFL24"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXSUFLJZNJBGXNC7KZ7XCSFTABQ"),
								false,
								null,
								null)
						},

						/*Radix::System::EventLog:TimeRange:Editor Pages-Filter Pages*/
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

								/*Radix::System::EventLog:TimeRange:Main-Editor Page*/

								 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHXTNCTTHSFCOJNC25XRFG7FUUI"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmP7RHN4373JGJTEIALKA3SCAU5A"),1,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmBQUVW3USB5C45DVW5IXJCAZU5Q"),2,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmGXLCPBC4JFHIBE4MRVPYZOARKM"),3,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmD7TRMTA7IFEH3M5LD7SUEACI7Y"),0,0,1,false,false)
								},null)
						},
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
							new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHXTNCTTHSFCOJNC25XRFG7FUUI"))}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::EventLog:User-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltQUTDPT7CTFGFVJRBQEFMWRTUFI"),
						"User",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLPCH3N4RVRBLPNFAXSJUS2FWSQ"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmMSYXKT4C4ZHSHPYWYXF5IUERA4\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmMSYXKT4C4ZHSHPYWYXF5IUERA4\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>&lt;= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmFCSBJ7FNLFFTPH2GXQAFKZI4QA\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate + 1), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmFCSBJ7FNLFFTPH2GXQAFKZI4QA\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colLTPIFVD36VCQ5HSGQAAOJEY4PM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:EntityRefParameter ParamId=\"prmOJRL3DEB6FFVXLHX6JJHF2RQW4\" ReferencedTableId=\"tblSY4KIOLTGLNRDHRZABQAQH3XQ4\" PidTranslationMode=\"PrimaryKeyProps\"/></xsc:Item><xsc:Item><xsc:Sql>\nAND ( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmG3NAYPEU3FBELFTF2VTLCGGAG4\"/></xsc:Item><xsc:Item><xsc:Sql> IS NULL OR </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql>>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmG3NAYPEU3FBELFTF2VTLCGGAG4\"/></xsc:Item><xsc:Item><xsc:Sql>)\n</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA")},
						false,
						false,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmOJRL3DEB6FFVXLHX6JJHF2RQW4"),
								"pUser",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNWO3EX76XRASXAGH2LXY2XDJ3I"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
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
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZONO647CNRGC5O5GHBHD6F7C7A")),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmG3NAYPEU3FBELFTF2VTLCGGAG4"),
								"minSeverity",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKKFH32WXAVAHHHZR3CJL7GWCUU"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZYO4N235ZZBXPAWMRGPDDDBSGA"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmMSYXKT4C4ZHSHPYWYXF5IUERA4"),
								"fromDate",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX3UKUR4JQJCKPFQ5Q3GXSHPB5Y"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGXNMWXVV45HLXOCKYE4ZXW4EDU"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmFCSBJ7FNLFFTPH2GXQAFKZI4QA"),
								"toDate",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN4LG75A4JZE7ZJF3XBB6DXH2RQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsACCVK3XLOFADBJ4ZO7MUFL7V7A"),
								false,
								null,
								null)
						},

						/*Radix::System::EventLog:User:Editor Pages-Filter Pages*/
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

								/*Radix::System::EventLog:User:Main-Editor Page*/

								 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5QRXYYRF25BR3EP6ORGPK3TYY4"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmG3NAYPEU3FBELFTF2VTLCGGAG4"),1,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmOJRL3DEB6FFVXLHX6JJHF2RQW4"),0,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmMSYXKT4C4ZHSHPYWYXF5IUERA4"),2,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmFCSBJ7FNLFFTPH2GXQAFKZI4QA"),3,0,1,false,false)
								},null)
						},
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
							new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5QRXYYRF25BR3EP6ORGPK3TYY4"))}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::EventLog:Station-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltM3WETXHKOZHYBIO63RKBNXMZVY"),
						"Station",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBR3XPTC47NG5XIA4KE3TK2SBUQ"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmSVDGDRZILFA2JKFGXLVRXYLQ3Q\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmSVDGDRZILFA2JKFGXLVRXYLQ3Q\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> &lt;= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmREAJESHPOVESTNOIMUXQCVNZQQ\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate + 1), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmREAJESHPOVESTNOIMUXQCVNZQQ\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colNTU5A722VVASNC5GVHVSWDFLKU\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> in (</xsc:Sql></xsc:Item><xsc:Item><xsc:EntityRefParameter ParamId=\"prm2UCNYUDK2VGYVOUZKJINQQY4U4\" ReferencedTableId=\"tblFDFDUXBHJ3NRDJIRACQMTAIZT4\" PidTranslationMode=\"PrimaryKeyProps\"/></xsc:Item><xsc:Item><xsc:Sql>)\nAND ( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmFOQJM4EYHZGABACHFRQTRYHP5M\"/></xsc:Item><xsc:Item><xsc:Sql> IS NULL OR </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql>>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmFOQJM4EYHZGABACHFRQTRYHP5M\"/></xsc:Item><xsc:Item><xsc:Sql>)\n</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA")},
						false,
						false,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm2UCNYUDK2VGYVOUZKJINQQY4U4"),
								"pStation",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4FDJ7ECZA5F3FDV6DCR6DUSCQM"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
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
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFDFDUXBHJ3NRDJIRACQMTAIZT4"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("spr3NY6WBNBT7NRDB56AALOMT5GDM")),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmFOQJM4EYHZGABACHFRQTRYHP5M"),
								"minSeverity",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTPFSQXY3ARHHPJ7ZP63W3NOHYU"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZYHZPE7AZNH4FJYACQKESSTGOA"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmSVDGDRZILFA2JKFGXLVRXYLQ3Q"),
								"fromDate",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAAWKAIWIT5H7DHTLZZVKYGROFY"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCFZ2WULHNNFVLAFZMMMMHC6QYY"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmREAJESHPOVESTNOIMUXQCVNZQQ"),
								"toDate",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZTA2SKREZJGLPKWI4ZYSNTLPMM"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWZH7TOE7FNHOPKKVPJZMXFRVOA"),
								false,
								null,
								null)
						},

						/*Radix::System::EventLog:Station:Editor Pages-Filter Pages*/
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

								/*Radix::System::EventLog:Station:Main-Editor Page*/

								 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgZZFUHTMSPBHMRFSH4OZKY5CAGQ"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm2UCNYUDK2VGYVOUZKJINQQY4U4"),0,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmFOQJM4EYHZGABACHFRQTRYHP5M"),1,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmSVDGDRZILFA2JKFGXLVRXYLQ3Q"),2,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmREAJESHPOVESTNOIMUXQCVNZQQ"),3,0,1,false,false)
								},null)
						},
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
							new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgZZFUHTMSPBHMRFSH4OZKY5CAGQ"))}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::EventLog:Severity-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltBYGYNUVM4JBGZO3KC6O7C5I65Y"),
						"Severity",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOKNP5QK2SRHJ3K4C4L2PO3JS5Y"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmN3KFLDVQ3FEKNFRJQAXANO3QJE\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmN3KFLDVQ3FEKNFRJQAXANO3QJE\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> &lt;= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmA6ZXQA6P65ER5KEZL7S5HAF2MU\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate + 1), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmA6ZXQA6P65ER5KEZL7S5HAF2MU\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand (</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmEZYWX2UJTBFEHJXFY4U24PVUMI\"/></xsc:Item><xsc:Item><xsc:Sql> or </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmEZYWX2UJTBFEHJXFY4U24PVUMI\"/></xsc:Item><xsc:Item><xsc:Sql> is null)\nand (</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>&lt;=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm4LJSDT4XNVAPJL66DQZJTUO2P4\"/></xsc:Item><xsc:Item><xsc:Sql> or </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm4LJSDT4XNVAPJL66DQZJTUO2P4\"/></xsc:Item><xsc:Item><xsc:Sql> is null)</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E")},
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmEZYWX2UJTBFEHJXFY4U24PVUMI"),
								"minSeverity",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mls34JM3AK2FRBNRFILS4YZW4Y7OA"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQZAKPB2ZWZF77ABOO5EQ52ZEOY"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmN3KFLDVQ3FEKNFRJQAXANO3QJE"),
								"fromDate",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKEPHPK6KTRCWFN2Y5A2X3QBY6M"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLYQO5D6XXZBVPDWPNCNPW2PEMQ"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmA6ZXQA6P65ER5KEZL7S5HAF2MU"),
								"toDate",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCYMVUJJQGJC5TICEGRTTHPP5IA"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAQVR57CHU5C5TLVCA5NUOHPN5I"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm4LJSDT4XNVAPJL66DQZJTUO2P4"),
								"maxSeverity",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBCYDW4725NCC7JJAE5Z2DQ7F6U"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBHTXCAVAXBGLTFKEO6FT6IFAZ4"),
								false,
								null,
								null)
						},

						/*Radix::System::EventLog:Severity:Editor Pages-Filter Pages*/
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

								/*Radix::System::EventLog:Severity:Main-Editor Page*/

								 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg4LNBTXV56NG3HMZQTHUOG6AJQU"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmEZYWX2UJTBFEHJXFY4U24PVUMI"),2,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmN3KFLDVQ3FEKNFRJQAXANO3QJE"),0,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmA6ZXQA6P65ER5KEZL7S5HAF2MU"),1,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm4LJSDT4XNVAPJL66DQZJTUO2P4"),3,0,1,false,false)
								},null)
						},
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
							new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg4LNBTXV56NG3HMZQTHUOG6AJQU"))}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::EventLog:Imported-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltS5F72ARLKFBGJLCKEX27ZDUJR4"),
						"Imported",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNXWOLHNIBRARBFF4T3TDE3HZFE"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>exists (\n    select 1 from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblJABJB4OAP3NRDAQSABIFNQAAAE\"/></xsc:Item><xsc:Item><xsc:Sql> where \n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblJABJB4OAP3NRDAQSABIFNQAAAE\" PropId=\"colXHA62NOBP3NRDAQSABIFNQAAAE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colUBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> and\n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblJABJB4OAP3NRDAQSABIFNQAAAE\" PropId=\"colOSK5SVWBP3NRDAQSABIFNQAAAE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> and\n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJABJB4OAP3NRDAQSABIFNQAAAE\" PropId=\"colT67W77OAP3NRDAQSABIFNQAAAE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:ConstValue EnumId=\"acsPVLIUELSKTNRDAQSABIFNQAAAE\" ItemId=\"aciGBCHKTBTCJEZ5G6VKA5IWHC4VM\"/></xsc:Item><xsc:Item><xsc:Sql> and\n    upper(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJABJB4OAP3NRDAQSABIFNQAAAE\" PropId=\"colHIPN4HGBP3NRDAQSABIFNQAAAE\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>) like upper(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmONPRLRODY5BFPM62Q72R53JXLY\"/></xsc:Item><xsc:Item><xsc:Sql>)\n)\nand (</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmRUGE7VE44ND25EYXVNNA6LDPZA\"/></xsc:Item><xsc:Item><xsc:Sql> or </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmRUGE7VE44ND25EYXVNNA6LDPZA\"/></xsc:Item><xsc:Item><xsc:Sql> is null)</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E")},
						true,
						false,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmONPRLRODY5BFPM62Q72R53JXLY"),
								"pFile",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUIJ2UTOOZJF2DAFIJX55A7ZR2E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
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
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmRUGE7VE44ND25EYXVNNA6LDPZA"),
								"minSeverity",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP3667UYCKBAKHNSBPGATFZ6DLU"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUV52DU3SRNDEBIJTM3BA2627CA"),
								false,
								null,
								null)
						},

						/*Radix::System::EventLog:Imported:Editor Pages-Filter Pages*/
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

								/*Radix::System::EventLog:Imported:Main-Editor Page*/

								 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgN62QXPLQZNHTRFXSVZRHYS24LE"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmONPRLRODY5BFPM62Q72R53JXLY"),0,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmRUGE7VE44ND25EYXVNNA6LDPZA"),1,0,1,false,false)
								},null)
						},
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
							new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgN62QXPLQZNHTRFXSVZRHYS24LE"))}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::EventLog:EventCodes-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltLFNJNPTVBJDWJDA57KRSGBRE2E"),
						"EventCodes",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUKQQ4VHYWFB2NGCNUXJYM6WG5M"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm2P723KWXQJA7DJ5E4OY7CNXRK4\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm2P723KWXQJA7DJ5E4OY7CNXRK4\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> &lt;= decode(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmINQENZUDPJFU3H2I5H6JDKAFWQ\"/></xsc:Item><xsc:Item><xsc:Sql>, null, trunc(sysdate + 1), </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmINQENZUDPJFU3H2I5H6JDKAFWQ\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand (</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmXO3CGUSUW5BZLBZ6NRCTNV577E\"/></xsc:Item><xsc:Item><xsc:Sql> is NULL or INSTR( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmXO3CGUSUW5BZLBZ6NRCTNV577E\"/></xsc:Item><xsc:Item><xsc:Sql>, </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colKZ33LLHXVHWDBROXAAIT4AGD7E\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> )>0)</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						null,
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmXO3CGUSUW5BZLBZ6NRCTNV577E"),
								"eventCodes",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTHHNLUH6HZAGVIABUSFMVQ5WOQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								false,
								false,
								true,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeLUNCTCK525FYRBPRWUBKO7JWCY"),
								true,
								null,
								null,
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm2P723KWXQJA7DJ5E4OY7CNXRK4"),
								"fromDate",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC4QADU6X4NBBZFY2MBFJPMFSBM"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7GDC3KEO2ZCLZM7BCJCZNDBEAY"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmINQENZUDPJFU3H2I5H6JDKAFWQ"),
								"toDate",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHF3NOFXHRZDHPD2I2B5PJO76OQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBGC5CJNZM5G2XITJKXEWJPQPTQ"),
								false,
								null,
								null)
						},

						/*Radix::System::EventLog:EventCodes:Editor Pages-Filter Pages*/
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

								/*Radix::System::EventLog:EventCodes:General-Editor Page*/

								 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUBGO7YQB4RFCTD2J2HKPEEVNQ4"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm2P723KWXQJA7DJ5E4OY7CNXRK4"),0,1,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmINQENZUDPJFU3H2I5H6JDKAFWQ"),1,1,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmXO3CGUSUW5BZLBZ6NRCTNV577E"),0,0,2,false,false)
								},null)
						},
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
							new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUBGO7YQB4RFCTD2J2HKPEEVNQ4"))}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::EventLog:Period-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltTYT3ALDZTNAYDDFDPTL7AFYGUU"),
						"Period",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBOEVCBAVXVBKXKBYE7W2KPQ6OU"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:IfParam ParamId=\"prmDVPXQQQ72VHPTELUBU5GXJALB4\" Operation=\"Equal\"><xsc:Value>1</xsc:Value></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=trunc(Sysdate)\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmDVPXQQQ72VHPTELUBU5GXJALB4\" Operation=\"Equal\"><xsc:Value>2</xsc:Value></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=trunc(Sysdate-1)\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmDVPXQQQ72VHPTELUBU5GXJALB4\" Operation=\"Equal\"><xsc:Value>3</xsc:Value></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=trunc(Sysdate-7)\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmDVPXQQQ72VHPTELUBU5GXJALB4\" Operation=\"Equal\"><xsc:Value>4</xsc:Value></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=trunc(add_months(Sysdate,-1))\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmDVPXQQQ72VHPTELUBU5GXJALB4\" Operation=\"Equal\"><xsc:Value>5</xsc:Value></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    (</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>>=nvl(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmNKTTHR6EAJB6FAJGHIDNIW3PHI\"/></xsc:Item><xsc:Item><xsc:Sql>, to_date(\'1\', \'J\')))\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmXOAKGU7PA5CSFDQACAVI2U3RCM\" Operation=\"Null\"/></xsc:Item><xsc:Item><xsc:Sql>\nAND </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> &lt; trunc(sysdate + 1)\n</xsc:Sql></xsc:Item><xsc:Item><xsc:ElseIf/></xsc:Item><xsc:Item><xsc:Sql>\nAND </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colWBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>&lt;=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmXOAKGU7PA5CSFDQACAVI2U3RCM\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmAIGN2LJXGVAHRPELFC4H6UAMKY\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    AND </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql>>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmAIGN2LJXGVAHRPELFC4H6UAMKY\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmTR6BP77HZRADZJKBAN6JVLZYB4\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    AND </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colXBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql>&lt;=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmTR6BP77HZRADZJKBAN6JVLZYB4\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prm6AHGQBB5HFBEVNUUP6AZ435PRU\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    AND </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colPHNPCMDWKPNRDAQSABIFNQAAAE\" Owner=\"THIS\" SuppressedWarnings=\"100000\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm6AHGQBB5HFBEVNUUP6AZ435PRU\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmQSMXFK73ZZHKNLPYAIYGS56DXQ\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    AND </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colLTPIFVD36VCQ5HSGQAAOJEY4PM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:EntityRefParameter ParamId=\"prmQSMXFK73ZZHKNLPYAIYGS56DXQ\" ReferencedTableId=\"tblSY4KIOLTGLNRDHRZABQAQH3XQ4\" PidTranslationMode=\"PrimaryKeyProps\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prm4A2P5FE5AZGFTMSTY7E5CPD7I4\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    AND </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colNTU5A722VVASNC5GVHVSWDFLKU\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:EntityRefParameter ParamId=\"prm4A2P5FE5AZGFTMSTY7E5CPD7I4\" ReferencedTableId=\"tblFDFDUXBHJ3NRDJIRACQMTAIZT4\" PidTranslationMode=\"PrimaryKeyProps\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prm6FBEUOOMAFDVLN3J3TI737U5Q4\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    AND INSTR( </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm6FBEUOOMAFDVLN3J3TI737U5Q4\"/></xsc:Item><xsc:Item><xsc:Sql>, </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colKZ33LLHXVHWDBROXAAIT4AGD7E\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> )>0\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmVND4W5T4GJCDVDMMEM7ZFG7SZU\" Operation=\"NotEqual\"><xsc:Value/></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    AND UPPER( </xsc:Sql></xsc:Item><xsc:Item><xsc:DbFuncCall FuncId=\"dfnMJOIKNAEUREC5CYMMNQLMBJAFM\"/></xsc:Item><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colKZ33LLHXVHWDBROXAAIT4AGD7E\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> , </xsc:Sql></xsc:Item><xsc:Item><xsc:DbFuncCall FuncId=\"dfnLMUSM5GXCTOBDCKFAAMPGXSZKU\"/></xsc:Item><xsc:Item><xsc:Sql>(), </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colVBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>) ) LIKE \'%\' || UPPER(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmVND4W5T4GJCDVDMMEM7ZFG7SZU\"/></xsc:Item><xsc:Item><xsc:Sql>) || \'%\'\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmI5PWPY5VHNAPXLI3J2TIDTRKOI\" Operation=\"NotEqual\"><xsc:Value/></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    AND UPPER( </xsc:Sql></xsc:Item><xsc:Item><xsc:DbFuncCall FuncId=\"dfnJ2PTRZ7GVZBMHN5USDST6QGLWI\"/></xsc:Item><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colKZ33LLHXVHWDBROXAAIT4AGD7E\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>,  </xsc:Sql></xsc:Item><xsc:Item><xsc:DbFuncCall FuncId=\"dfnLMUSM5GXCTOBDCKFAAMPGXSZKU\"/></xsc:Item><xsc:Item><xsc:Sql>(), </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecACQVZOMVVHWDBROXAAIT4AGD7E\" PropId=\"colVBCRBDFDL2VDBNSNAAYOJ6SINF\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>) ) NOT LIKE \'%\' || UPPER(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmI5PWPY5VHNAPXLI3J2TIDTRKOI\"/></xsc:Item><xsc:Item><xsc:Sql>) || \'%\'\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item></xsc:Sqml>",
						null,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA")},
						false,
						false,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm6AHGQBB5HFBEVNUUP6AZ435PRU"),
								"source",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSO4TKWSEGREVZJRMOSBWA2AOGE"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),
								null,
								false,
								false,
								false,
								null,
								false,

								/*Radix::System::EventLog:Period:source:Edit Options:-Edit Mask Enum*/
								new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_VALUE,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDCWXD43GEBD5LDQOHP7GA5W6WI"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmAIGN2LJXGVAHRPELFC4H6UAMKY"),
								"minSeverity",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKB3KWU57LBBQ7G577WSZGHUOV4"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWQE2U26P4FBV7DKELXK2SD52MU"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmDVPXQQQ72VHPTELUBU5GXJALB4"),
								"period",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT6OLEABUYZEAZF3WSVRKVK5I7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								null,
								org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
								true,
								false,
								false,
								null,
								false,

								/*Radix::System::EventLog:Period:period:Edit Options:-Edit Mask List*/
								new org.radixware.kernel.common.client.meta.mask.EditMaskList(new org.radixware.kernel.common.client.meta.mask.EditMaskList.Item[]{

										new org.radixware.kernel.common.client.meta.mask.EditMaskList.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecACQVZOMVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPJP3PHRTGNEDBBR6QRS2U4RKRM"),org.radixware.kernel.common.defs.value.ValAsStr.fromStr("1",org.radixware.kernel.common.enums.EValType.INT)),

										new org.radixware.kernel.common.client.meta.mask.EditMaskList.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecACQVZOMVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls66CEJDVR6RDN7OD6HQ4TVVF3JM"),org.radixware.kernel.common.defs.value.ValAsStr.fromStr("2",org.radixware.kernel.common.enums.EValType.INT)),

										new org.radixware.kernel.common.client.meta.mask.EditMaskList.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecACQVZOMVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS65VEIXKPRA43HEXE6JL2GC5YE"),org.radixware.kernel.common.defs.value.ValAsStr.fromStr("3",org.radixware.kernel.common.enums.EValType.INT)),

										new org.radixware.kernel.common.client.meta.mask.EditMaskList.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecACQVZOMVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLKLGWZDJEFBRNE2SHBJV26FFII"),org.radixware.kernel.common.defs.value.ValAsStr.fromStr("4",org.radixware.kernel.common.enums.EValType.INT)),

										new org.radixware.kernel.common.client.meta.mask.EditMaskList.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecACQVZOMVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVXH3CLQNQ5CNXENVVSJ3MC4OQU"),org.radixware.kernel.common.defs.value.ValAsStr.fromStr("5",org.radixware.kernel.common.enums.EValType.INT))
								}),
								null,
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmTR6BP77HZRADZJKBAN6JVLZYB4"),
								"maxSeverity",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZP6424GWQNA6JIVGIDNLP6R45U"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBGJFJXPVMNHJTNWYWYRTJKW5T4"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmXOAKGU7PA5CSFDQACAVI2U3RCM"),
								"toTime",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHCBHLHP3O5FQFBDYQU6TLXGO7Y"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYVJBU4HKIFCJTIZOBSTDII3P6I"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmNKTTHR6EAJB6FAJGHIDNIW3PHI"),
								"fromTime",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQWFCYFBDPJBAVHU2XQPGECKHJQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOG5FEPSR5ZG57I3UMBK3SKIO2Q"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmQSMXFK73ZZHKNLPYAIYGS56DXQ"),
								"user",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCIACSRHOFJG4JOQRKR4LNECNPU"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.PARENT_REF,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH6FAIQSDZZBFVFVPFU5KJGY2PE"),
								false,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZONO647CNRGC5O5GHBHD6F7C7A")),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm4A2P5FE5AZGFTMSTY7E5CPD7I4"),
								"station",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUR4DB7VH5ZBYRJDDPOODD4WJCI"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.PARENT_REF,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWGZVU3F4ZJFK5G42SSYWCYGILI"),
								false,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFDFDUXBHJ3NRDJIRACQMTAIZT4"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("spr3NY6WBNBT7NRDB56AALOMT5GDM")),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm6FBEUOOMAFDVLN3J3TI737U5Q4"),
								"eventCodes",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGBPBXS7FXZAHBENS3IQRJZWBNQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								false,
								false,
								true,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeJR45J5TXOJBNJJEDT2CFPLSC3I"),
								true,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVJRANVOK25BXVBXSXFEZSDCAQE"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmVND4W5T4GJCDVDMMEM7ZFG7SZU"),
								"includedText",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZW7SV6PBORBPRKVL6GYRYMDZ44"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								false,
								false,
								false,
								null,
								false,

								/*Radix::System::EventLog:Period:includedText:Edit Options:-Edit Mask Str*/
								new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1999,true),
								null,
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmI5PWPY5VHNAPXLI3J2TIDTRKOI"),
								"excludedText",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZIU2N36B3ZDTJPKD5CS4STD7HY"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								false,
								false,
								false,
								null,
								false,

								/*Radix::System::EventLog:Period:excludedText:Edit Options:-Edit Mask Str*/
								new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1999,true),
								null,
								false,
								null,
								null)
						},

						/*Radix::System::EventLog:Period:Editor Pages-Filter Pages*/
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

								/*Radix::System::EventLog:Period:Main-Editor Page*/

								 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg6TB3ZIGLOBEYDKBHNST77UURYI"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmDVPXQQQ72VHPTELUBU5GXJALB4"),0,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmNKTTHR6EAJB6FAJGHIDNIW3PHI"),1,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmXOAKGU7PA5CSFDQACAVI2U3RCM"),2,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm6FBEUOOMAFDVLN3J3TI737U5Q4"),0,1,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmTR6BP77HZRADZJKBAN6JVLZYB4"),2,1,1,true,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmAIGN2LJXGVAHRPELFC4H6UAMKY"),1,1,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm6AHGQBB5HFBEVNUUP6AZ435PRU"),0,2,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm4A2P5FE5AZGFTMSTY7E5CPD7I4"),2,2,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmQSMXFK73ZZHKNLPYAIYGS56DXQ"),1,2,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmVND4W5T4GJCDVDMMEM7ZFG7SZU"),0,3,3,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmI5PWPY5VHNAPXLI3J2TIDTRKOI"),0,4,3,false,false)
								},null)
						},
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
							new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg6TB3ZIGLOBEYDKBHNST77UURYI"))}
						,
						null)
			},
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::System::EventLog:TimeDesc-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
						"TimeDesc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3M5UBSD64DORDOFEABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWBCRBDFDL2VDBNSNAAYOJ6SINF"),
								true),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUBCRBDFDL2VDBNSNAAYOJ6SINF"),
								true)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::System::EventLog:TimeAsc-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E"),
						"TimeAsc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6FQGPWH6UBH3TG4MUOPHDCVGHE"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWBCRBDFDL2VDBNSNAAYOJ6SINF"),
								false),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUBCRBDFDL2VDBNSNAAYOJ6SINF"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref5TCE2JPJ4FGJDE7YJT3ES2RWHQ"),"EventLog=>Station (stationName=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFDFDUXBHJ3NRDJIRACQMTAIZT4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colJZ43ECZ62BEJ3JUXL7EVQTYQ74")},new String[]{"stationName"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colFTFDUXBHJ3NRDJIRACQMTAIZT4")},new String[]{"name"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refJIYJJX6MP3NRDAQSABIFNQAAAE"),"EventLog=>User (receiptor=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colKUM7R4BBZLWDRRGGAAIT4AGD7E")},new String[]{"receiptor"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4")},new String[]{"name"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refL4N6UFTR3RDR7KPNKMMHZ2K6J4"),"EventLog=>User (userName=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colANWVUMA3FJEFHHZSTOLUH2UQ7A")},new String[]{"userName"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4")},new String[]{"name"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEEUTDHGEXTNRDB6RAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprF6ONI4OEXTNRDB6RAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprHH4BKAI7UVEO3OS4I5AHCHKN3A"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVWY625ANRTNRDB5PAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprW7YW3HO6WLOBDCLZAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprKQ5WOSDJZ5AQFOGPY4PK3O2XTA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprY2GNJQQ7IRBLDKX4NEFOMP4J5A"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprDEZXGYRBYVBADE5OCIBI73JN2I"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprE4HR7KORYZDQBAP4O5XMDBLIPE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7X6MMAXKTZHUHB46WUW333PMMA")},
			false,false,false);
}

/* Radix::System::EventLog:WithoutContextInfo - Desktop Meta*/

/*Radix::System::EventLog:WithoutContextInfo-Editor Presentation*/

package org.radixware.ads.System.explorer;
public final class WithoutContextInfo_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEEUTDHGEXTNRDB6RAALOMT5GDM"),
	"WithoutContextInfo",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
	null,
	null,

	/*Radix::System::EventLog:WithoutContextInfo:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::System::EventLog:WithoutContextInfo:Common-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgD3IJOWOTBXOBDNTPAAMPGXSZKU"),"Common",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5ZVFSS24OXOBDFKUAAMPGXUWTQ"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUBCRBDFDL2VDBNSNAAYOJ6SINF"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWBCRBDFDL2VDBNSNAAYOJ6SINF"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXBCRBDFDL2VDBNSNAAYOJ6SINF"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPHNPCMDWKPNRDAQSABIFNQAAAE"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYBCRBDFDL2VDBNSNAAYOJ6SINF"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVSA77O3TXXNRDB6SAALOMT5GDM"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLTPIFVD36VCQ5HSGQAAOJEY4PM"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNTU5A722VVASNC5GVHVSWDFLKU"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEA2GUFJSQ5COTEOM2REI4CW344"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLNECKEY3IJGX7D6OZOFLCAWOOI"),0,4,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgD3IJOWOTBXOBDNTPAAMPGXSZKU"))}
	,

	/*Radix::System::EventLog:WithoutContextInfo:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	28679,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::System::EventLog:WithoutContextInfo - Web Meta*/

/*Radix::System::EventLog:WithoutContextInfo-Editor Presentation*/

package org.radixware.ads.System.web;
public final class WithoutContextInfo_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEEUTDHGEXTNRDB6RAALOMT5GDM"),
	"WithoutContextInfo",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
	null,
	null,

	/*Radix::System::EventLog:WithoutContextInfo:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::System::EventLog:WithoutContextInfo:Common-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgD3IJOWOTBXOBDNTPAAMPGXSZKU"),"Common",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5ZVFSS24OXOBDFKUAAMPGXUWTQ"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUBCRBDFDL2VDBNSNAAYOJ6SINF"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWBCRBDFDL2VDBNSNAAYOJ6SINF"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXBCRBDFDL2VDBNSNAAYOJ6SINF"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPHNPCMDWKPNRDAQSABIFNQAAAE"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYBCRBDFDL2VDBNSNAAYOJ6SINF"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVSA77O3TXXNRDB6SAALOMT5GDM"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLTPIFVD36VCQ5HSGQAAOJEY4PM"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNTU5A722VVASNC5GVHVSWDFLKU"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEA2GUFJSQ5COTEOM2REI4CW344"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLNECKEY3IJGX7D6OZOFLCAWOOI"),0,4,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgD3IJOWOTBXOBDNTPAAMPGXSZKU"))}
	,

	/*Radix::System::EventLog:WithoutContextInfo:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	28679,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::System::EventLog:WithoutContextInfo:Model - Desktop Executable*/

/*Radix::System::EventLog:WithoutContextInfo:Model-Entity Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithoutContextInfo:Model")
public class WithoutContextInfo:Model  extends org.radixware.ads.System.explorer.EventLog.EventLog_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return WithoutContextInfo:Model_mi.rdxMeta; }



	public WithoutContextInfo:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::System::EventLog:WithoutContextInfo:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:WithoutContextInfo:Model:Properties-Properties*/

	/*Radix::System::EventLog:WithoutContextInfo:Model:messagePreview-Presentation Property*/




	public class MessagePreview extends org.radixware.ads.System.explorer.EventLog.prdEA2GUFJSQ5COTEOM2REI4CW344{
		public MessagePreview(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}

		/*Radix::System::EventLog:WithoutContextInfo:Model:messagePreview:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::EventLog:WithoutContextInfo:Model:messagePreview:Nested classes-Nested Classes*/

		/*Radix::System::EventLog:WithoutContextInfo:Model:messagePreview:Properties-Properties*/

		/*Radix::System::EventLog:WithoutContextInfo:Model:messagePreview:Methods-Methods*/

		/*Radix::System::EventLog:WithoutContextInfo:Model:messagePreview:createPropertyEditor-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithoutContextInfo:Model:messagePreview:createPropertyEditor")
		public published  org.radixware.kernel.common.client.views.IPropEditor createPropertyEditor () {
			final Client.Types::IMemoController memoController = new Client.Types::IMemoController() {
			    public String getFinalText(final String finalText) {
			        return messagePreview.Value;
			    }

			    public String prepareTextForMemo(final String originalText) {
			        return message.Value;
			    }
			};
			return new PropStrEditor(this) {
			    
			    public void refresh(Explorer.Models::ModelItem it) {
			        super.refresh(it);
			        if (getValEditor() instanceof Explorer.ValEditors::ValStrEditor) {
			            ((Explorer.ValEditors::ValStrEditor) getValEditor()).setMemoController(memoController);
			        }
			    }
			    
			};
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithoutContextInfo:Model:messagePreview")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithoutContextInfo:Model:messagePreview")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public MessagePreview getMessagePreview(){return (MessagePreview)getProperty(prdEA2GUFJSQ5COTEOM2REI4CW344);}








	/*Radix::System::EventLog:WithoutContextInfo:Model:Methods-Methods*/


}

/* Radix::System::EventLog:WithoutContextInfo:Model - Desktop Meta*/

/*Radix::System::EventLog:WithoutContextInfo:Model-Entity Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class WithoutContextInfo:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemEEUTDHGEXTNRDB6RAALOMT5GDM"),
						"WithoutContextInfo:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:WithoutContextInfo:Model:Properties-Properties*/
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

/* Radix::System::EventLog:WithoutContextInfo:Model - Web Executable*/

/*Radix::System::EventLog:WithoutContextInfo:Model-Entity Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithoutContextInfo:Model")
public class WithoutContextInfo:Model  extends org.radixware.ads.System.web.EventLog.EventLog_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return WithoutContextInfo:Model_mi.rdxMeta; }



	public WithoutContextInfo:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::System::EventLog:WithoutContextInfo:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:WithoutContextInfo:Model:Properties-Properties*/

	/*Radix::System::EventLog:WithoutContextInfo:Model:messagePreview-Presentation Property*/




	public class MessagePreview extends org.radixware.ads.System.web.EventLog.prdEA2GUFJSQ5COTEOM2REI4CW344{
		public MessagePreview(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}

		/*Radix::System::EventLog:WithoutContextInfo:Model:messagePreview:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::EventLog:WithoutContextInfo:Model:messagePreview:Nested classes-Nested Classes*/

		/*Radix::System::EventLog:WithoutContextInfo:Model:messagePreview:Properties-Properties*/

		/*Radix::System::EventLog:WithoutContextInfo:Model:messagePreview:Methods-Methods*/

		/*Radix::System::EventLog:WithoutContextInfo:Model:messagePreview:createPropertyEditor-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithoutContextInfo:Model:messagePreview:createPropertyEditor")
		public published  org.radixware.kernel.common.client.views.IPropEditor createPropertyEditor () {
			final Client.Types::IMemoController memoController = new Client.Types::IMemoController() {
			    public String getFinalText(final String finalText) {
			        return finalText;
			    }

			    public String prepareTextForMemo(final String originalText) {
			        return message.Value;
			    }
			};
			return new PropStrEditor(this) {
			    
			    public void refresh(Explorer.Models::ModelItem it) {
			        super.refresh(it);
			        if (getValEditor() instanceof Web.Widgets::ValStrEditorController) {
			            ((Web.Widgets::ValStrEditorController) getValEditor()).setMemoController(memoController);
			        }
			    }
			    
			};
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithoutContextInfo:Model:messagePreview")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithoutContextInfo:Model:messagePreview")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public MessagePreview getMessagePreview(){return (MessagePreview)getProperty(prdEA2GUFJSQ5COTEOM2REI4CW344);}








	/*Radix::System::EventLog:WithoutContextInfo:Model:Methods-Methods*/


}

/* Radix::System::EventLog:WithoutContextInfo:Model - Web Meta*/

/*Radix::System::EventLog:WithoutContextInfo:Model-Entity Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class WithoutContextInfo:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemEEUTDHGEXTNRDB6RAALOMT5GDM"),
						"WithoutContextInfo:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:WithoutContextInfo:Model:Properties-Properties*/
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

/* Radix::System::EventLog:WithContextInfo - Desktop Meta*/

/*Radix::System::EventLog:WithContextInfo-Editor Presentation*/

package org.radixware.ads.System.explorer;
public final class WithContextInfo_mi{
	private static final class WithContextInfo_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		WithContextInfo_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM"),
			"WithContextInfo",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEEUTDHGEXTNRDB6RAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
			null,
			null,

			/*Radix::System::EventLog:WithContextInfo:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::System::EventLog:WithContextInfo:Contexts-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgEDIJOWOTBXOBDNTPAAMPGXSZKU"),"Contexts",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls55VFSS24OXOBDFKUAAMPGXUWTQ"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiGA2GHQDFV7NRDOC3ADQEZUQ3BM"))
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgD3IJOWOTBXOBDNTPAAMPGXSZKU")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgEDIJOWOTBXOBDNTPAAMPGXSZKU"))}
			,

			/*Radix::System::EventLog:WithContextInfo:Children-Explorer Items*/
				new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

						/*Radix::System::EventLog:WithContextInfo:EventContext-Child Ref Explorer Item*/

						new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiGA2GHQDFV7NRDOC3ADQEZUQ3BM"),
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),null,
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJABJB4OAP3NRDAQSABIFNQAAAE"),
							org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2UIYK3COR3NRDB5QAALOMT5GDM"),
							32,
							null,
							16560,true)
				}
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			10416,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.System.explorer.WithoutContextInfo:Model(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new WithContextInfo_DEF(); 
;
}
/* Radix::System::EventLog:WithContextInfo - Web Meta*/

/*Radix::System::EventLog:WithContextInfo-Editor Presentation*/

package org.radixware.ads.System.web;
public final class WithContextInfo_mi{
	private static final class WithContextInfo_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		WithContextInfo_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM"),
			"WithContextInfo",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEEUTDHGEXTNRDB6RAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
			null,
			null,

			/*Radix::System::EventLog:WithContextInfo:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::System::EventLog:WithContextInfo:Contexts-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgEDIJOWOTBXOBDNTPAAMPGXSZKU"),"Contexts",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls55VFSS24OXOBDFKUAAMPGXUWTQ"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiGA2GHQDFV7NRDOC3ADQEZUQ3BM"))
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgD3IJOWOTBXOBDNTPAAMPGXSZKU")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgEDIJOWOTBXOBDNTPAAMPGXSZKU"))}
			,

			/*Radix::System::EventLog:WithContextInfo:Children-Explorer Items*/
				new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

						/*Radix::System::EventLog:WithContextInfo:EventContext-Child Ref Explorer Item*/

						new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiGA2GHQDFV7NRDOC3ADQEZUQ3BM"),
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),null,
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJABJB4OAP3NRDAQSABIFNQAAAE"),
							org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2UIYK3COR3NRDB5QAALOMT5GDM"),
							32,
							null,
							16560,true)
				}
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			10416,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.System.web.WithoutContextInfo:Model(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new WithContextInfo_DEF(); 
;
}
/* Radix::System::EventLog:Contextless - Desktop Meta*/

/*Radix::System::EventLog:Contextless-Selector Presentation*/

package org.radixware.ads.System.explorer;
public final class Contextless_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Contextless_mi();
	private Contextless_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprF6ONI4OEXTNRDB6RAALOMT5GDM"),
		"Contextless",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E")},
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("fltBYGYNUVM4JBGZO3KC6O7C5I65Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltM3WETXHKOZHYBIO63RKBNXMZVY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("flt3LGDG7LCCVEP5GK5MQN7UUVELA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltUV5CRRHED3OBDCHDAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltQUTDPT7CTFGFVJRBQEFMWRTUFI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltLFNJNPTVBJDWJDA57KRSGBRE2E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltTYT3ALDZTNAYDDFDPTL7AFYGUU")},
		false,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("fltTYT3ALDZTNAYDDFDPTL7AFYGUU"),
		29623,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQGNH5TT7TXOBDCK6AALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdFKWW4F3LYNDFJGPVIVJ5NX5A74"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdNJQT6UHXLJBIHB5CIIQDQ74UGI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdVK2KXYIVHZFKNEVICOVHZQHIBE")},
		2192,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},
		false,false,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWBCRBDFDL2VDBNSNAAYOJ6SINF"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUBCRBDFDL2VDBNSNAAYOJ6SINF"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXBCRBDFDL2VDBNSNAAYOJ6SINF"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEA2GUFJSQ5COTEOM2REI4CW344"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLTPIFVD36VCQ5HSGQAAOJEY4PM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNTU5A722VVASNC5GVHVSWDFLKU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPHNPCMDWKPNRDAQSABIFNQAAAE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
}
/* Radix::System::EventLog:Contextless - Web Meta*/

/*Radix::System::EventLog:Contextless-Selector Presentation*/

package org.radixware.ads.System.web;
public final class Contextless_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Contextless_mi();
	private Contextless_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprF6ONI4OEXTNRDB6RAALOMT5GDM"),
		"Contextless",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E")},
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("fltBYGYNUVM4JBGZO3KC6O7C5I65Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltM3WETXHKOZHYBIO63RKBNXMZVY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("flt3LGDG7LCCVEP5GK5MQN7UUVELA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltUV5CRRHED3OBDCHDAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltQUTDPT7CTFGFVJRBQEFMWRTUFI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltLFNJNPTVBJDWJDA57KRSGBRE2E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltTYT3ALDZTNAYDDFDPTL7AFYGUU")},
		false,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("fltTYT3ALDZTNAYDDFDPTL7AFYGUU"),
		29623,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQGNH5TT7TXOBDCK6AALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdFKWW4F3LYNDFJGPVIVJ5NX5A74"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdNJQT6UHXLJBIHB5CIIQDQ74UGI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdVK2KXYIVHZFKNEVICOVHZQHIBE")},
		2192,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},
		false,false,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWBCRBDFDL2VDBNSNAAYOJ6SINF"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUBCRBDFDL2VDBNSNAAYOJ6SINF"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXBCRBDFDL2VDBNSNAAYOJ6SINF"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEA2GUFJSQ5COTEOM2REI4CW344"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLTPIFVD36VCQ5HSGQAAOJEY4PM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNTU5A722VVASNC5GVHVSWDFLKU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPHNPCMDWKPNRDAQSABIFNQAAAE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
}
/* Radix::System::EventLog:Contextless:Model - Desktop Executable*/

/*Radix::System::EventLog:Contextless:Model-Group Model Class*/

package org.radixware.ads.System.explorer;

import org.radixware.kernel.explorer.editors.eventlog.CfgUpdatePeriodDialog;
import javax.sound.midi.*;
import java.util.List;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model")
public class Contextless:Model  extends org.radixware.ads.System.explorer.EventLog.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Contextless:Model_mi.rdxMeta; }

	protected com.trolltech.qt.gui.QToolButton btnUpdate = null;
	private int timerCounter = 0;

	private final static int MAX_ROW_COUNT = 1000;
	private final static int MAX_TIMER_TRY_COUNT = 3;

	class EventLogReaderController extends Explorer.Models::AboveReaderController {
	    public EventLogReaderController() {
	        super(getGroupView().getGroupModel(), MAX_ROW_COUNT);
	    }
	    
	    @Override    
	    public List<Explorer.Models::EntityModel> merge(final List<Explorer.Models::EntityModel> currentModels, final List<Explorer.Models::EntityModel> newModels, final Explorer.Models::EntityModel currentModel) {        
	        if (!isSound) {
	            return super.merge(currentModels, newModels, currentModel);
	        }
	        
	        try {
	            Arte::EventSeverity maxSeverity = Arte::EventSeverity:Debug;
	            for (Explorer.Models::EntityModel model : newModels) {
	                final Arte::EventSeverity severity = (Arte::EventSeverity)model.getProperty(idof[EventLog:severity]).getValueObject();
	                if (severity.Value.compareTo(maxSeverity.Value) > 0)
	                    maxSeverity = severity;
	            }
	            switch (maxSeverity) {
	                case Arte::EventSeverity:Event:
	                    playSound(eventDuration);
	                    break;
	                case Arte::EventSeverity:Warning:
	                    playSound(warningDuration);
	                    break;
	                case Arte::EventSeverity:Error:
	                    playSound(errorDuration);
	                    break;
	                case Arte::EventSeverity:Alarm:
	                    playSound(alarmDuration);
	                    break;
	                default: ;    
	            }
	        } catch (Exceptions::Exception e) {
	            Explorer.Utils::Trace.error(getEnvironment(), e.getMessage());
	        }
	        return super.merge(currentModels, newModels, currentModel);
	    }    
	}

	public Contextless:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::System::EventLog:Contextless:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:Contextless:Model:Properties-Properties*/

	/*Radix::System::EventLog:Contextless:Model:isUpdate-Dynamic Property*/



	protected boolean isUpdate=false;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:isUpdate")
	  boolean getIsUpdate() {
		return isUpdate;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:isUpdate")
	   void setIsUpdate(boolean val) {
		isUpdate = val;
	}

	/*Radix::System::EventLog:Contextless:Model:period-Dynamic Property*/



	protected int period=0;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:period")
	  int getPeriod() {
		return period;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:period")
	   void setPeriod(int val) {
		period = val;
	}

	/*Radix::System::EventLog:Contextless:Model:timer-Dynamic Property*/



	protected com.trolltech.qt.core.QTimer timer=new com.trolltech.qt.core.QTimer();











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:timer")
	 final  com.trolltech.qt.core.QTimer getTimer() {
		return timer;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:timer")
	 final   void setTimer(com.trolltech.qt.core.QTimer val) {
		timer = val;
	}

	/*Radix::System::EventLog:Contextless:Model:isSound-Dynamic Property*/



	protected boolean isSound=false;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:isSound")
	  boolean getIsSound() {
		return isSound;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:isSound")
	   void setIsSound(boolean val) {
		isSound = val;
	}

	/*Radix::System::EventLog:Contextless:Model:eventDuration-Dynamic Property*/



	protected int eventDuration=0;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:eventDuration")
	  int getEventDuration() {
		return eventDuration;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:eventDuration")
	   void setEventDuration(int val) {
		eventDuration = val;
	}

	/*Radix::System::EventLog:Contextless:Model:warningDuration-Dynamic Property*/



	protected int warningDuration=0;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:warningDuration")
	  int getWarningDuration() {
		return warningDuration;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:warningDuration")
	   void setWarningDuration(int val) {
		warningDuration = val;
	}

	/*Radix::System::EventLog:Contextless:Model:errorDuration-Dynamic Property*/



	protected int errorDuration=0;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:errorDuration")
	  int getErrorDuration() {
		return errorDuration;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:errorDuration")
	   void setErrorDuration(int val) {
		errorDuration = val;
	}

	/*Radix::System::EventLog:Contextless:Model:alarmDuration-Dynamic Property*/



	protected int alarmDuration=0;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:alarmDuration")
	  int getAlarmDuration() {
		return alarmDuration;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:alarmDuration")
	   void setAlarmDuration(int val) {
		alarmDuration = val;
	}

	/*Radix::System::EventLog:Contextless:Model:isPaused-Dynamic Property*/



	protected boolean isPaused=false;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:isPaused")
	  boolean getIsPaused() {
		return isPaused;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:isPaused")
	   void setIsPaused(boolean val) {
		isPaused = val;
	}






	/*Radix::System::EventLog:Contextless:Model:Methods-Methods*/

	/*Radix::System::EventLog:Contextless:Model:clean-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:clean")
	public published  void clean () {
		super.clean();

		if (isAutoUpdateEnabled()) {
		    if (timer.isActive())
		        timer.stop();

		    timer.timeout.disconnect();

		    final Explorer.Models::GroupModelAsyncReader reader = getAsyncReader();
		    if (reader.inProgress())
		        reader.stop();
		}
	}

	/*Radix::System::EventLog:Contextless:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		if (isAutoUpdateEnabled()) {
		    Str settingsKey = getConfigStoreGroupName() + "/eventlog_update_enabled";
		    isUpdate = getEnvironment().getConfigStore().readBoolean(settingsKey, false);

		    settingsKey = getConfigStoreGroupName() + "/eventlog_mute_enabled";
		    isSound = getEnvironment().getConfigStore().readBoolean(settingsKey, false);

		    settingsKey = getConfigStoreGroupName() + "/eventlog_update_period";
		    period = getEnvironment().getConfigStore().readInteger(settingsKey, 30);

		    settingsKey = getConfigStoreGroupName() + "/eventlog_event_sound_duration";
		    eventDuration = getEnvironment().getConfigStore().readInteger(settingsKey, 0);

		    settingsKey = getConfigStoreGroupName() + "/eventlog_warning_sound_duration";
		    warningDuration = getEnvironment().getConfigStore().readInteger(settingsKey, 1);

		    settingsKey = getConfigStoreGroupName() + "/eventlog_error_sound_duration";
		    errorDuration = getEnvironment().getConfigStore().readInteger(settingsKey, 3);

		    settingsKey = getConfigStoreGroupName() + "/eventlog_alarm_sound_duration";
		    alarmDuration = getEnvironment().getConfigStore().readInteger(settingsKey, 5);

		    isPaused = false;
		    timerCounter = 0;
		    ((Explorer.Views::SelectorView) getGroupView()).opened.connect(this, slot[EventLog:Contextless:Contextless:Model:init]);
		}

	}

	/*Radix::System::EventLog:Contextless:Model:onCommand_ClearSensitiveData-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:onCommand_ClearSensitiveData")
	private final  void onCommand_ClearSensitiveData (org.radixware.ads.System.explorer.EventLogGroup.ClearSensitiveData command) {
		try {
		    command.send();
		    reread();
		} catch(Exceptions::InterruptedException e) {
		    Environment.processException(e);
		} catch(Exceptions::ServiceClientException e) {
		    Environment.processException(e);
		}
	}

	/*Radix::System::EventLog:Contextless:Model:onCommand_ExportCodes-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:onCommand_ExportCodes")
	private final  void onCommand_ExportCodes (org.radixware.ads.System.explorer.EventLogGroup.ExportCodes command) {
		final String fileName = com.trolltech.qt.gui.QFileDialog.getSaveFileName(
		    Explorer.Env::Application.getMainWindow(),
		    "Select File to Save Codes",
		    null,
		    new com.trolltech.qt.gui.QFileDialog.Filter("CSV files (*.csv)"));

		if (fileName == null || fileName.isEmpty())
		    return; //user canceled

		try{
		    Common::CommandsXsd:FileNameDocument in = Common::CommandsXsd:FileNameDocument.Factory.newInstance();
		    in.addNewFileName();
		    in.FileName.Path = fileName;
		    command.send(in);
		} catch(Exceptions::InterruptedException e) {
		    Environment.processException(e);
		} catch(Exceptions::ServiceClientException e) {
		    Environment.processException(e);
		}
	}

	/*Radix::System::EventLog:Contextless:Model:timer-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:timer")
	protected  void timer () {
		if (isPaused || getView() == null)
		    return;

		/*
		if (GroupView.() != null) {
		     ml = new (GroupView);
		    if (!ml.isEmpty()) {
		        return;
		    }
		}

		if (() == null || !().isVisible())
		    return;

		 h = null;
		try {
		    h = ().().();
		    h.(null, false);
		    reread();
		} catch( e) {
		    ().().(e);
		} finally {
		    if (h != null)
		        h.();
		}
		*/

		final Explorer.Models::GroupModelAsyncReader reader = getAsyncReader();
		if (reader.inProgress()) {
		    if (++timerCounter >= MAX_TIMER_TRY_COUNT) {
		        reader.stop();
		    } else
		        return;
		}

		timerCounter = 0;
		reader.start(new EventLogReaderController());
	}

	/*Radix::System::EventLog:Contextless:Model:isFirstEntity-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:isFirstEntity")
	@Deprecated
	protected  boolean isFirstEntity () {
		final Explorer.Models::GroupModel groupModel = getGroupView().getGroupModel();
		final Explorer.Models::EntityModel currentEntity = getGroupView().getCurrentEntity();
		try {
		    if (!groupModel.isEmpty() && currentEntity != null) {
		        final Explorer.Types::Pid currentPid = currentEntity.getPid();
		        final boolean manyRows = groupModel.getEntitiesCount() > 0 || groupModel.hasMoreRows();
		        final Explorer.Types::Pid firstPid = manyRows ? groupModel.getEntity(0).getPid() : null;
		        return currentPid.equals(firstPid);
		    }
		} catch(Exception e) {
		    Explorer.Utils::Trace.error(getEnvironment(), e.getMessage());
		}
		return false;
	}

	/*Radix::System::EventLog:Contextless:Model:refresh-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:refresh")
	protected  void refresh () {
		if (!isUpdate)
		    return;

		final Explorer.Models::GroupModel groupModel = getGroupView().getGroupModel();
		final Explorer.Models::EntityModel currentEntity = getGroupView().getCurrentEntity();
		isPaused = !groupModel.isEmpty() && currentEntity != null;

		btnUpdate.setChecked(!isPaused);
		btnUpdate.setIcon(Explorer.Icons::ExplorerIcon.getQIcon(getEnvironment().getDefManager().getImage(isPaused ? idof[Workflow::update_paused] : idof[Workflow::update_disabled])));
	}

	/*Radix::System::EventLog:Contextless:Model:configClicked-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:configClicked")
	protected published  void configClicked () {
		if (timer.isActive())
		    timer.stop();

		final CfgUpdatePeriodDialog dialog = new CfgUpdatePeriodDialog(getEnvironment(), Explorer.Env::Application.getMainWindow(), period, eventDuration, warningDuration, errorDuration, alarmDuration);
		if (dialog.exec() == 1) {
		    period = dialog.getPeriod();
		    Str settingsKey = getConfigStoreGroupName() + "/eventlog_update_period";
		    getEnvironment().getConfigStore().writeInteger(settingsKey, period);

		    eventDuration = dialog.getEventDuration();
		    settingsKey = getConfigStoreGroupName() + "/eventlog_event_sound_duration";
		    getEnvironment().getConfigStore().writeInteger(settingsKey, eventDuration);

		    warningDuration = dialog.getWarningDuration();
		    settingsKey = getConfigStoreGroupName() + "/eventlog_warning_sound_duration";
		    getEnvironment().getConfigStore().writeInteger(settingsKey, warningDuration);

		    errorDuration = dialog.getErrorDuration();
		    settingsKey = getConfigStoreGroupName() + "/eventlog_error_sound_duration";
		    getEnvironment().getConfigStore().writeInteger(settingsKey, errorDuration);

		    alarmDuration = dialog.getAlarmDuration();
		    settingsKey = getConfigStoreGroupName() + "/eventlog_alarm_sound_duration";
		    getEnvironment().getConfigStore().writeInteger(settingsKey, alarmDuration);
		}

		if (isUpdate)
		    timer.start(period * 1000);
	}

	/*Radix::System::EventLog:Contextless:Model:updateClicked-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:updateClicked")
	protected published  void updateClicked () {
		final com.trolltech.qt.gui.QToolButton btnUpdate = (com.trolltech.qt.gui.QToolButton)Explorer.Qt.Types::QObject.signalSender();

		isPaused = false;
		isUpdate = btnUpdate.isChecked();
		Str settingsKey = getConfigStoreGroupName() + "/eventlog_update_enabled";
		getEnvironment().getConfigStore().writeBoolean(settingsKey, isUpdate);

		btnUpdate.setToolTip(isUpdate ? "Disable Auto Update" : "Enable Auto Update");
		btnUpdate.setIcon(Explorer.Icons::ExplorerIcon.getQIcon(getEnvironment().getDefManager().getImage(isUpdate ? idof[Workflow::update_disabled] : idof[Workflow::update_active])));
		if (isUpdate) {
		    resetCurrentEntity();
		    timer.start(period * 1000);
		} else {
		    timer.stop();
		    try {
		        reread();
		    } catch (Exceptions::ServiceClientException e) {
		        getEnvironment().getTracer().error(e);
		    }
		}
	}

	/*Radix::System::EventLog:Contextless:Model:soundClicked-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:soundClicked")
	protected published  void soundClicked () {
		final com.trolltech.qt.gui.QToolButton btnSound = (com.trolltech.qt.gui.QToolButton)Explorer.Qt.Types::QObject.signalSender();

		if (btnSound.isChecked()) {
		    try (Synthesizer synthesizer = MidiSystem.getSynthesizer()) {        
		        synthesizer.open();
		    } catch (javax.sound.midi.MidiUnavailableException e) {
		        getEnvironment().messageError("Sound Device Problem", "Sound device problem found. Notifications can't be enabled.");
		        btnSound.setChecked(false);
		        return;
		    }
		}

		isSound = btnSound.isChecked();
		Str settingsKey = getConfigStoreGroupName() + "/eventlog_mute_enabled";
		getEnvironment().getConfigStore().writeBoolean(settingsKey, isSound);

		btnSound.setToolTip(isSound ? "Disable Sound Notification" : "Enable Sound Notification");
		btnSound.setIcon(Explorer.Icons::ExplorerIcon.getQIcon(getEnvironment().getDefManager().getImage(isSound ? idof[mute-on] : idof[mute-off])));
	}

	/*Radix::System::EventLog:Contextless:Model:playSound-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:playSound")
	protected  void playSound (int duration) {
		if (duration <= 0)
		    return;

		try {
		    final Synthesizer synthesizer = MidiSystem.getSynthesizer();
		    synthesizer.open();
		    
		    final MidiChannel channel = synthesizer.getChannels()[synthesizer.getChannels().length > 9 ? 9 : 0];
		    
		    final int sec = duration;
		    new Thread(new Runnable() {
		        @Override
		        public void run() {
		            for (int i=0; i<sec; i++) {
		                channel.noteOn(60, 500);
		                try {
		                    Java.Lang::Thread.sleep(1000);
		                } catch (Exceptions::Throwable e) {
		                    break;
		                } finally {
		                    channel.noteOff(60);
		                }
		            }
		            synthesizer.close();
		        }        
		    }).start();
		} catch (javax.sound.midi.MidiUnavailableException e) {
		    Explorer.Utils::Trace.error(getEnvironment(), e.getMessage());
		}
	}

	/*Radix::System::EventLog:Contextless:Model:resetCurrentEntity-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:resetCurrentEntity")
	protected  void resetCurrentEntity () {
		timerCounter = 0;
		getGroupView().setCurrentEntity(null);
		((Explorer.Widgets::SelectorGrid)getGroupView().getSelectorWidget()).setCurrentIndex(null);
		refresh();
	}

	/*Radix::System::EventLog:Contextless:Model:init-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:init")
	protected  void init () {
		if (isAutoUpdateEnabled()) {
		    setupCommandToolBar();

		    getGroupView().addCurrentEntityHandler(
		            new Client.Views::Selector.CurrentEntityHandler() {
		                @Override
		                public void onSetCurrentEntity(Explorer.Models::EntityModel entity) {
		                    refresh();
		                }

		                @Override
		                public void onLeaveCurrentEntity() {
		                }
		            }
		    );

		    timer.timeout.connect(this, idof[EventLog:Contextless:Contextless:Model:timer] + "()");

		    if (isUpdate) {
		        resetCurrentEntity();
		        timer.start(period * 1000);
		    }
		}
	}

	/*Radix::System::EventLog:Contextless:Model:setupCommandToolBar-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:setupCommandToolBar")
	protected  void setupCommandToolBar () {
		final Explorer.Views::SelectorView view = (Explorer.Views::SelectorView)getGroupView();
		final org.radixware.kernel.explorer.widgets.commands.CommandToolBar toolBar = view.getCommandBar();
		toolBar.addSeparator();

		final com.trolltech.qt.gui.QToolButton btnConfig = new com.trolltech.qt.gui.QToolButton(toolBar);
		btnConfig.setToolButtonStyle(com.trolltech.qt.core.Qt.ToolButtonStyle.ToolButtonIconOnly);
		btnConfig.setIcon(Explorer.Icons::ExplorerIcon.getQIcon(getEnvironment().getDefManager().getImage(idof[Explorer.Icons::dinamicClass])));
		btnConfig.setToolTip("Display Settings");
		btnConfig.clicked.connect(this, idof[EventLog:Contextless:Contextless:Model:configClicked] + "()");
		toolBar.addWidget(btnConfig);

		final com.trolltech.qt.gui.QToolButton btnSound = new com.trolltech.qt.gui.QToolButton(toolBar);
		btnSound.setToolButtonStyle(com.trolltech.qt.core.Qt.ToolButtonStyle.ToolButtonIconOnly);
		btnSound.setIcon(Explorer.Icons::ExplorerIcon.getQIcon(getEnvironment().getDefManager().getImage(isSound ? idof[mute-on] : idof[mute-off])));
		btnSound.setToolTip(isSound ? "Disable Sound Notification" : "Enable Sound Notification");
		btnSound.setCheckable(true);
		btnSound.setChecked(isSound);
		btnSound.clicked.connect(this, idof[EventLog:Contextless:Contextless:Model:soundClicked] + "()");
		toolBar.addWidget(btnSound);

		btnUpdate = new com.trolltech.qt.gui.QToolButton(toolBar);
		btnUpdate.setToolButtonStyle(com.trolltech.qt.core.Qt.ToolButtonStyle.ToolButtonIconOnly);
		btnUpdate.setIcon(Explorer.Icons::ExplorerIcon.getQIcon(getEnvironment().getDefManager().getImage(isUpdate ? idof[Workflow::update_disabled] : idof[Workflow::update_active])));
		btnUpdate.setToolTip(isUpdate ? "Disable Auto Update" : "Enable Auto Update");
		btnUpdate.setCheckable(true);
		btnUpdate.setChecked(isUpdate);
		btnUpdate.clicked.connect(this, idof[EventLog:Contextless:Contextless:Model:updateClicked] + "()");
		toolBar.addWidget(btnUpdate);
	}

	/*Radix::System::EventLog:Contextless:Model:isAutoUpdateEnabled-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:isAutoUpdateEnabled")
	protected published  boolean isAutoUpdateEnabled () {
		return true;
	}

	/*Radix::System::EventLog:Contextless:Model:readCommonSettings-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:readCommonSettings")
	public published  void readCommonSettings () throws org.radixware.kernel.common.exceptions.ServiceClientException,java.lang.InterruptedException {
		final Explorer.Models::FilterModel periodFilter = getFilters().findById(idof[EventLog:Period]);
		if (periodFilter==null || getFilters().getDefaultFilter()!=periodFilter || getView()!=null){
		    super.readCommonSettings();
		}//else apply filter in init method and read settings
	}

	/*Radix::System::EventLog:Contextless:Model:setupDefaultFilter-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:setupDefaultFilter")
	protected  void setupDefaultFilter () {
		final Explorer.Models::FilterModel defaultFilter;
		if (getContext() instanceof Explorer.Context::TableSelectContext){
		    final Explorer.Context::TableSelectContext context = (Explorer.Context::TableSelectContext)getContext();
		    defaultFilter = context.getFilters().isEmpty() ? getFilters().findById(idof[EventLog:Period]) : null;
		}else{
		    defaultFilter = getFilters().findById(idof[EventLog:Period]);
		}
		if (defaultFilter!=null && getFilters().getDefaultFilter()==defaultFilter && getCurrentFilter()==null){
		    try{
		        setFilter(defaultFilter);
		    }catch(Exceptions::InterruptedException | Exceptions::ServiceClientException exception){
		        throw new CantOpenSelectorError(this,exception);
		    }catch(Explorer.Exceptions::PropertyIsMandatoryException | Explorer.Exceptions::InvalidPropertyValueException exception){
		        getEnvironment().getTracer().error(exception);
		    }
		}else if (!settingsWasRead()){
		    try{
		        readCommonSettings();
		    }catch(Exceptions::InterruptedException | Exceptions::ServiceClientException exception){
		        throw new CantOpenSelectorError(this,exception);
		    }    
		}
	}
	public final class ExportCodes extends org.radixware.ads.System.explorer.EventLogGroup.ExportCodes{
		protected ExportCodes(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ExportCodes( this );
		}

	}

	public final class ClearSensitiveData extends org.radixware.ads.System.explorer.EventLogGroup.ClearSensitiveData{
		protected ClearSensitiveData(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ClearSensitiveData( this );
		}

	}















}

/* Radix::System::EventLog:Contextless:Model - Desktop Meta*/

/*Radix::System::EventLog:Contextless:Model-Group Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Contextless:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmF6ONI4OEXTNRDB6RAALOMT5GDM"),
						"Contextless:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:Contextless:Model:Properties-Properties*/
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

/* Radix::System::EventLog:Contextless:Model - Web Executable*/

/*Radix::System::EventLog:Contextless:Model-Group Model Class*/

package org.radixware.ads.System.web;


@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model")
public class Contextless:Model  extends org.radixware.ads.System.web.EventLog.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Contextless:Model_mi.rdxMeta; }


	public Contextless:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::System::EventLog:Contextless:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:Contextless:Model:Properties-Properties*/

	/*Radix::System::EventLog:Contextless:Model:isUpdate-Dynamic Property*/



	protected boolean isUpdate=false;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:isUpdate")
	  boolean getIsUpdate() {
		return isUpdate;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:isUpdate")
	   void setIsUpdate(boolean val) {
		isUpdate = val;
	}

	/*Radix::System::EventLog:Contextless:Model:period-Dynamic Property*/



	protected int period=0;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:period")
	  int getPeriod() {
		return period;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:period")
	   void setPeriod(int val) {
		period = val;
	}

	/*Radix::System::EventLog:Contextless:Model:isSound-Dynamic Property*/



	protected boolean isSound=false;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:isSound")
	  boolean getIsSound() {
		return isSound;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:isSound")
	   void setIsSound(boolean val) {
		isSound = val;
	}

	/*Radix::System::EventLog:Contextless:Model:eventDuration-Dynamic Property*/



	protected int eventDuration=0;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:eventDuration")
	  int getEventDuration() {
		return eventDuration;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:eventDuration")
	   void setEventDuration(int val) {
		eventDuration = val;
	}

	/*Radix::System::EventLog:Contextless:Model:warningDuration-Dynamic Property*/



	protected int warningDuration=0;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:warningDuration")
	  int getWarningDuration() {
		return warningDuration;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:warningDuration")
	   void setWarningDuration(int val) {
		warningDuration = val;
	}

	/*Radix::System::EventLog:Contextless:Model:errorDuration-Dynamic Property*/



	protected int errorDuration=0;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:errorDuration")
	  int getErrorDuration() {
		return errorDuration;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:errorDuration")
	   void setErrorDuration(int val) {
		errorDuration = val;
	}

	/*Radix::System::EventLog:Contextless:Model:alarmDuration-Dynamic Property*/



	protected int alarmDuration=0;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:alarmDuration")
	  int getAlarmDuration() {
		return alarmDuration;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:alarmDuration")
	   void setAlarmDuration(int val) {
		alarmDuration = val;
	}

	/*Radix::System::EventLog:Contextless:Model:isPaused-Dynamic Property*/



	protected boolean isPaused=false;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:isPaused")
	  boolean getIsPaused() {
		return isPaused;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:isPaused")
	   void setIsPaused(boolean val) {
		isPaused = val;
	}






	/*Radix::System::EventLog:Contextless:Model:Methods-Methods*/

	/*Radix::System::EventLog:Contextless:Model:clean-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:clean")
	public published  void clean () {
		super.clean();
	}

	/*Radix::System::EventLog:Contextless:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:beforeOpenView")
	protected published  void beforeOpenView () {

	}

	/*Radix::System::EventLog:Contextless:Model:onCommand_ClearSensitiveData-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:onCommand_ClearSensitiveData")
	private final  void onCommand_ClearSensitiveData (org.radixware.ads.System.web.EventLogGroup.ClearSensitiveData command) {
		try {
		    command.send();
		    reread();
		} catch(Exceptions::InterruptedException e) {
		    Environment.processException(e);
		} catch(Exceptions::ServiceClientException e) {
		    Environment.processException(e);
		}
	}

	/*Radix::System::EventLog:Contextless:Model:timer-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:timer")
	protected  void timer () {

	}

	/*Radix::System::EventLog:Contextless:Model:isFirstEntity-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:isFirstEntity")
	@Deprecated
	protected  boolean isFirstEntity () {
		return false;
	}

	/*Radix::System::EventLog:Contextless:Model:refresh-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:refresh")
	protected  void refresh () {

	}

	/*Radix::System::EventLog:Contextless:Model:updateClicked-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:updateClicked")
	protected published  void updateClicked () {

	}

	/*Radix::System::EventLog:Contextless:Model:soundClicked-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:soundClicked")
	protected published  void soundClicked () {

	}

	/*Radix::System::EventLog:Contextless:Model:playSound-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:playSound")
	protected  void playSound (int duration) {

	}

	/*Radix::System::EventLog:Contextless:Model:resetCurrentEntity-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:resetCurrentEntity")
	protected  void resetCurrentEntity () {

	}

	/*Radix::System::EventLog:Contextless:Model:isAutoUpdateEnabled-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:isAutoUpdateEnabled")
	protected published  boolean isAutoUpdateEnabled () {
		return true;
	}

	/*Radix::System::EventLog:Contextless:Model:readCommonSettings-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:readCommonSettings")
	public published  void readCommonSettings () throws org.radixware.kernel.common.exceptions.ServiceClientException,java.lang.InterruptedException {
		final Explorer.Models::FilterModel periodFilter = getFilters().findById(idof[EventLog:Period]);
		if (periodFilter==null || getFilters().getDefaultFilter()!=periodFilter || getView()!=null){
		    super.readCommonSettings();
		}//else apply filter in init method and read settings
	}

	/*Radix::System::EventLog:Contextless:Model:setupDefaultFilter-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Contextless:Model:setupDefaultFilter")
	protected  void setupDefaultFilter () {
		final Explorer.Models::FilterModel defaultFilter;
		if (getContext() instanceof Explorer.Context::TableSelectContext){
		    final Explorer.Context::TableSelectContext context = (Explorer.Context::TableSelectContext)getContext();
		    defaultFilter = context.getFilters().isEmpty() ? getFilters().findById(idof[EventLog:Period]) : null;
		}else{
		    defaultFilter = getFilters().findById(idof[EventLog:Period]);
		}
		if (defaultFilter!=null && getFilters().getDefaultFilter()==defaultFilter && getCurrentFilter()==null){
		    try{
		        setFilter(defaultFilter);
		    }catch(Exceptions::InterruptedException | Exceptions::ServiceClientException exception){
		        throw new CantOpenSelectorError(this,exception);
		    }catch(Explorer.Exceptions::PropertyIsMandatoryException | Explorer.Exceptions::InvalidPropertyValueException exception){
		        getEnvironment().getTracer().error(exception);
		    }
		}else if (!settingsWasRead()){
		    try{
		        readCommonSettings();
		    }catch(Exceptions::InterruptedException | Exceptions::ServiceClientException exception){
		        throw new CantOpenSelectorError(this,exception);
		    }    
		}
	}
	public final class ClearSensitiveData extends org.radixware.ads.System.web.EventLogGroup.ClearSensitiveData{
		protected ClearSensitiveData(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ClearSensitiveData( this );
		}

	}













}

/* Radix::System::EventLog:Contextless:Model - Web Meta*/

/*Radix::System::EventLog:Contextless:Model-Group Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Contextless:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmF6ONI4OEXTNRDB6RAALOMT5GDM"),
						"Contextless:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:Contextless:Model:Properties-Properties*/
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

/* Radix::System::EventLog:WithContextInfo_DeleteAllEnabled - Desktop Meta*/

/*Radix::System::EventLog:WithContextInfo_DeleteAllEnabled-Selector Presentation*/

package org.radixware.ads.System.explorer;
public final class WithContextInfo_DeleteAllEnabled_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new WithContextInfo_DeleteAllEnabled_mi();
	private WithContextInfo_DeleteAllEnabled_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprHH4BKAI7UVEO3OS4I5AHCHKN3A"),
		"WithContextInfo_DeleteAllEnabled",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVWY625ANRTNRDB5PAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
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
		291733,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQGNH5TT7TXOBDCK6AALOMT5GDM")},
		24794,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},
		false,false,false,0,0);
		columns = null;;
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.System.explorer.Contextless:Model(userSession,this);
	}
}
/* Radix::System::EventLog:WithContextInfo_DeleteAllEnabled - Web Meta*/

/*Radix::System::EventLog:WithContextInfo_DeleteAllEnabled-Selector Presentation*/

package org.radixware.ads.System.web;
public final class WithContextInfo_DeleteAllEnabled_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new WithContextInfo_DeleteAllEnabled_mi();
	private WithContextInfo_DeleteAllEnabled_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprHH4BKAI7UVEO3OS4I5AHCHKN3A"),
		"WithContextInfo_DeleteAllEnabled",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVWY625ANRTNRDB5PAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
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
		291733,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQGNH5TT7TXOBDCK6AALOMT5GDM")},
		24794,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},
		false,false,false,0,0);
		columns = null;;
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.System.web.Contextless:Model(userSession,this);
	}
}
/* Radix::System::EventLog:WithContextInfo - Desktop Meta*/

/*Radix::System::EventLog:WithContextInfo-Selector Presentation*/

package org.radixware.ads.System.explorer;
public final class WithContextInfo_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new WithContextInfo_mi();
	private WithContextInfo_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVWY625ANRTNRDB5PAALOMT5GDM"),
		"WithContextInfo",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprF6ONI4OEXTNRDB6RAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
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
		24826,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},
		false,false,false,0,0);
		columns = null;;
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.System.explorer.Contextless:Model(userSession,this);
	}
}
/* Radix::System::EventLog:WithContextInfo - Web Meta*/

/*Radix::System::EventLog:WithContextInfo-Selector Presentation*/

package org.radixware.ads.System.web;
public final class WithContextInfo_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new WithContextInfo_mi();
	private WithContextInfo_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVWY625ANRTNRDB5PAALOMT5GDM"),
		"WithContextInfo",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprF6ONI4OEXTNRDB6RAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
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
		24826,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},
		false,false,false,0,0);
		columns = null;;
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.System.web.Contextless:Model(userSession,this);
	}
}
/* Radix::System::EventLog:Imported - Desktop Meta*/

/*Radix::System::EventLog:Imported-Selector Presentation*/

package org.radixware.ads.System.explorer;
public final class Imported_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Imported_mi();
	private Imported_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprW7YW3HO6WLOBDCLZAALOMT5GDM"),
		"Imported",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVWY625ANRTNRDB5PAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM4VUG6TZWPOBDCL2AALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("imgMJKPH63LXBEBJO5HB7UWMBCVBU"),
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E")},
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("fltBYGYNUVM4JBGZO3KC6O7C5I65Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("flt3LGDG7LCCVEP5GK5MQN7UUVELA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltM3WETXHKOZHYBIO63RKBNXMZVY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltS5F72ARLKFBGJLCKEX27ZDUJR4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltUV5CRRHED3OBDCHDAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltQUTDPT7CTFGFVJRBQEFMWRTUFI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltTYT3ALDZTNAYDDFDPTL7AFYGUU")},
		false,
		true,
		null,
		291733,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdMO6BHZVDUHOBDCLBAALOMT5GDM")},
		18496,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},
		false,false,false,0,0);
		columns = null;;
	}
}
/* Radix::System::EventLog:Imported - Web Meta*/

/*Radix::System::EventLog:Imported-Selector Presentation*/

package org.radixware.ads.System.web;
public final class Imported_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Imported_mi();
	private Imported_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprW7YW3HO6WLOBDCLZAALOMT5GDM"),
		"Imported",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVWY625ANRTNRDB5PAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM4VUG6TZWPOBDCL2AALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("imgMJKPH63LXBEBJO5HB7UWMBCVBU"),
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E")},
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("fltBYGYNUVM4JBGZO3KC6O7C5I65Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("flt3LGDG7LCCVEP5GK5MQN7UUVELA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltM3WETXHKOZHYBIO63RKBNXMZVY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltS5F72ARLKFBGJLCKEX27ZDUJR4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltUV5CRRHED3OBDCHDAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltQUTDPT7CTFGFVJRBQEFMWRTUFI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltTYT3ALDZTNAYDDFDPTL7AFYGUU")},
		false,
		true,
		null,
		291733,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdMO6BHZVDUHOBDCLBAALOMT5GDM")},
		18496,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},
		false,false,false,0,0);
		columns = null;;
	}
}
/* Radix::System::EventLog:Imported:Model - Desktop Executable*/

/*Radix::System::EventLog:Imported:Model-Group Model Class*/

package org.radixware.ads.System.explorer;

import com.trolltech.qt.gui.QFileDialog;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Imported:Model")
public class Imported:Model  extends org.radixware.ads.System.explorer.Contextless:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Imported:Model_mi.rdxMeta; }



	public Imported:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::System::EventLog:Imported:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:Imported:Model:Properties-Properties*/

	/*Radix::System::EventLog:Imported:Model:Methods-Methods*/

	/*Radix::System::EventLog:Imported:Model:onCommand_ImportEvents-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Imported:Model:onCommand_ImportEvents")
	private final  void onCommand_ImportEvents (org.radixware.ads.System.explorer.EventLogGroup.ImportEvents command) {
		try{
		    QFileDialog.Filter filter = new QFileDialog.Filter("XML File (*.xml);;All files (*)");
		    Str fileName = QFileDialog.getOpenFileName(Explorer.Env::Application.getMainWindow(),
		            "Import Event Log",
		            "",
		            filter);
		    if(fileName==null || fileName.isEmpty())
		        return;
		    SysCommandsXsd:EventsImportRqDocument input = SysCommandsXsd:EventsImportRqDocument.Factory.newInstance();
		    input.addNewEventsImportRq().File = fileName;
		    command.send(input);
		    reread();
		}catch(Exceptions::ServiceClientException e){
		    showException(e);
		}catch(Exceptions::InterruptedException e){
		    showException(e);
		}

	}

	/*Radix::System::EventLog:Imported:Model:init-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Imported:Model:init")
	protected  void init () {
		// do nothing
	}

	/*Radix::System::EventLog:Imported:Model:setupDefaultFilter-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Imported:Model:setupDefaultFilter")
	protected  void setupDefaultFilter () {
		//do nothing
	}
	public final class ImportEvents extends org.radixware.ads.System.explorer.EventLogGroup.ImportEvents{
		protected ImportEvents(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ImportEvents( this );
		}

	}













}

/* Radix::System::EventLog:Imported:Model - Desktop Meta*/

/*Radix::System::EventLog:Imported:Model-Group Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Imported:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmW7YW3HO6WLOBDCLZAALOMT5GDM"),
						"Imported:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agmF6ONI4OEXTNRDB6RAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:Imported:Model:Properties-Properties*/
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

/* Radix::System::EventLog:Imported:Model - Web Executable*/

/*Radix::System::EventLog:Imported:Model-Group Model Class*/

package org.radixware.ads.System.web;


@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Imported:Model")
public class Imported:Model  extends org.radixware.ads.System.web.Contextless:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Imported:Model_mi.rdxMeta; }



	public Imported:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::System::EventLog:Imported:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:Imported:Model:Properties-Properties*/

	/*Radix::System::EventLog:Imported:Model:Methods-Methods*/

	/*Radix::System::EventLog:Imported:Model:setupDefaultFilter-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Imported:Model:setupDefaultFilter")
	protected  void setupDefaultFilter () {
		//do nothing
	}


}

/* Radix::System::EventLog:Imported:Model - Web Meta*/

/*Radix::System::EventLog:Imported:Model-Group Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Imported:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmW7YW3HO6WLOBDCLZAALOMT5GDM"),
						"Imported:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agmF6ONI4OEXTNRDB6RAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:Imported:Model:Properties-Properties*/
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

/* Radix::System::EventLog:ForContextLog - Desktop Meta*/

/*Radix::System::EventLog:ForContextLog-Selector Presentation*/

package org.radixware.ads.System.explorer;
public final class ForContextLog_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new ForContextLog_mi();
	private ForContextLog_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),
		"ForContextLog",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVWY625ANRTNRDB5PAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA")},
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("fltBYGYNUVM4JBGZO3KC6O7C5I65Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("flt3LGDG7LCCVEP5GK5MQN7UUVELA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltUV5CRRHED3OBDCHDAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltTYT3ALDZTNAYDDFDPTL7AFYGUU")},
		false,
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("fltTYT3ALDZTNAYDDFDPTL7AFYGUU"),
		278583,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQGNH5TT7TXOBDCK6AALOMT5GDM")},
		16594,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},
		false,false,false,0,0);
		columns = null;;
	}
}
/* Radix::System::EventLog:ForContextLog - Web Meta*/

/*Radix::System::EventLog:ForContextLog-Selector Presentation*/

package org.radixware.ads.System.web;
public final class ForContextLog_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new ForContextLog_mi();
	private ForContextLog_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),
		"ForContextLog",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVWY625ANRTNRDB5PAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtBBATGL3LV5BXVHF5FMZHX7PA7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA")},
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srt3I5UBSD64DORDOFEABIFNQAABA"),
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("fltBYGYNUVM4JBGZO3KC6O7C5I65Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("flt3LGDG7LCCVEP5GK5MQN7UUVELA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltUV5CRRHED3OBDCHDAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("fltTYT3ALDZTNAYDDFDPTL7AFYGUU")},
		false,
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("fltTYT3ALDZTNAYDDFDPTL7AFYGUU"),
		278583,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQGNH5TT7TXOBDCK6AALOMT5GDM")},
		16594,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},
		false,false,false,0,0);
		columns = null;;
	}
}
/* Radix::System::EventLog:ForContextLog:Model - Desktop Executable*/

/*Radix::System::EventLog:ForContextLog:Model-Group Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:ForContextLog:Model")
public class ForContextLog:Model  extends org.radixware.ads.System.explorer.Contextless:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return ForContextLog:Model_mi.rdxMeta; }



	public ForContextLog:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::System::EventLog:ForContextLog:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:ForContextLog:Model:Properties-Properties*/

	/*Radix::System::EventLog:ForContextLog:Model:Methods-Methods*/

	/*Radix::System::EventLog:ForContextLog:Model:beforeReadFirstPage-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:ForContextLog:Model:beforeReadFirstPage")
	protected published  void beforeReadFirstPage () {
		super.beforeReadFirstPage();
		IEventContextProvider provider = (IEventContextProvider) this.findOwnerByClass(IEventContextProvider.class);

		if (provider != null) {
		    contextType.setValueObject(provider.getEventContextType());
		    contextId.setValueObject(provider.getEventContextId());
		}

	}


}

/* Radix::System::EventLog:ForContextLog:Model - Desktop Meta*/

/*Radix::System::EventLog:ForContextLog:Model-Group Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ForContextLog:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmVJGCRERZOBDS7GULG6RLDVLG3A"),
						"ForContextLog:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agmF6ONI4OEXTNRDB6RAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:ForContextLog:Model:Properties-Properties*/
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

/* Radix::System::EventLog:ForContextLog:Model - Web Executable*/

/*Radix::System::EventLog:ForContextLog:Model-Group Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:ForContextLog:Model")
public class ForContextLog:Model  extends org.radixware.ads.System.web.Contextless:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return ForContextLog:Model_mi.rdxMeta; }



	public ForContextLog:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::System::EventLog:ForContextLog:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:ForContextLog:Model:Properties-Properties*/

	/*Radix::System::EventLog:ForContextLog:Model:Methods-Methods*/

	/*Radix::System::EventLog:ForContextLog:Model:beforeReadFirstPage-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:ForContextLog:Model:beforeReadFirstPage")
	protected published  void beforeReadFirstPage () {
		super.beforeReadFirstPage();
		IEventContextProvider provider = (IEventContextProvider) this.findOwnerByClass(IEventContextProvider.class);

		if (provider != null) {
		    contextType.setValueObject(provider.getEventContextType());
		    contextId.setValueObject(provider.getEventContextId());
		}

	}


}

/* Radix::System::EventLog:ForContextLog:Model - Web Meta*/

/*Radix::System::EventLog:ForContextLog:Model-Group Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ForContextLog:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmVJGCRERZOBDS7GULG6RLDVLG3A"),
						"ForContextLog:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agmF6ONI4OEXTNRDB6RAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:ForContextLog:Model:Properties-Properties*/
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

/* Radix::System::EventLog:ForContextLog_DeleteAllEnabled - Desktop Meta*/

/*Radix::System::EventLog:ForContextLog_DeleteAllEnabled-Selector Presentation*/

package org.radixware.ads.System.explorer;
public final class ForContextLog_DeleteAllEnabled_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new ForContextLog_DeleteAllEnabled_mi();
	private ForContextLog_DeleteAllEnabled_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprKQ5WOSDJZ5AQFOGPY4PK3O2XTA"),
		"ForContextLog_DeleteAllEnabled",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
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
		278549,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQGNH5TT7TXOBDCK6AALOMT5GDM")},
		16601,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},
		false,false,false,0,0);
		columns = null;;
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.System.explorer.ForContextLog:Model(userSession,this);
	}
}
/* Radix::System::EventLog:ForContextLog_DeleteAllEnabled - Web Meta*/

/*Radix::System::EventLog:ForContextLog_DeleteAllEnabled-Selector Presentation*/

package org.radixware.ads.System.web;
public final class ForContextLog_DeleteAllEnabled_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new ForContextLog_DeleteAllEnabled_mi();
	private ForContextLog_DeleteAllEnabled_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprKQ5WOSDJZ5AQFOGPY4PK3O2XTA"),
		"ForContextLog_DeleteAllEnabled",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
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
		278549,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQGNH5TT7TXOBDCK6AALOMT5GDM")},
		16601,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},
		false,false,false,0,0);
		columns = null;;
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.System.web.ForContextLog:Model(userSession,this);
	}
}
/* Radix::System::EventLog:ForContextLog_WithoutAllContextsTab - Desktop Meta*/

/*Radix::System::EventLog:ForContextLog_WithoutAllContextsTab-Selector Presentation*/

package org.radixware.ads.System.explorer;
public final class ForContextLog_WithoutAllContextsTab_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new ForContextLog_WithoutAllContextsTab_mi();
	private ForContextLog_WithoutAllContextsTab_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprY2GNJQQ7IRBLDKX4NEFOMP4J5A"),
		"ForContextLog_WithoutAllContextsTab",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
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
		16635,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEEUTDHGEXTNRDB6RAALOMT5GDM")},
		false,false,false,0,0);
		columns = null;;
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.System.explorer.ForContextLog:Model(userSession,this);
	}
}
/* Radix::System::EventLog:ForContextLog_WithoutAllContextsTab - Web Meta*/

/*Radix::System::EventLog:ForContextLog_WithoutAllContextsTab-Selector Presentation*/

package org.radixware.ads.System.web;
public final class ForContextLog_WithoutAllContextsTab_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new ForContextLog_WithoutAllContextsTab_mi();
	private ForContextLog_WithoutAllContextsTab_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprY2GNJQQ7IRBLDKX4NEFOMP4J5A"),
		"ForContextLog_WithoutAllContextsTab",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
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
		16635,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEEUTDHGEXTNRDB6RAALOMT5GDM")},
		false,false,false,0,0);
		columns = null;;
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.System.web.ForContextLog:Model(userSession,this);
	}
}
/* Radix::System::EventLog:ForContextLog_SysUnits - Desktop Meta*/

/*Radix::System::EventLog:ForContextLog_SysUnits-Selector Presentation*/

package org.radixware.ads.System.explorer;
public final class ForContextLog_SysUnits_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new ForContextLog_SysUnits_mi();
	private ForContextLog_SysUnits_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprDEZXGYRBYVBADE5OCIBI73JN2I"),
		"ForContextLog_SysUnits",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
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
		16635,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},
		false,false,false,0,0);
		columns = null;;
	}
}
/* Radix::System::EventLog:ForContextLog_SysUnits - Web Meta*/

/*Radix::System::EventLog:ForContextLog_SysUnits-Selector Presentation*/

package org.radixware.ads.System.web;
public final class ForContextLog_SysUnits_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new ForContextLog_SysUnits_mi();
	private ForContextLog_SysUnits_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprDEZXGYRBYVBADE5OCIBI73JN2I"),
		"ForContextLog_SysUnits",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
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
		16635,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},
		false,false,false,0,0);
		columns = null;;
	}
}
/* Radix::System::EventLog:ForContextLog_SysUnits:Model - Desktop Executable*/

/*Radix::System::EventLog:ForContextLog_SysUnits:Model-Group Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:ForContextLog_SysUnits:Model")
public class ForContextLog_SysUnits:Model  extends org.radixware.ads.System.explorer.ForContextLog:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return ForContextLog_SysUnits:Model_mi.rdxMeta; }



	public ForContextLog_SysUnits:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::System::EventLog:ForContextLog_SysUnits:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:ForContextLog_SysUnits:Model:Properties-Properties*/

	/*Radix::System::EventLog:ForContextLog_SysUnits:Model:Methods-Methods*/

	/*Radix::System::EventLog:ForContextLog_SysUnits:Model:setupCommandToolBar-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:ForContextLog_SysUnits:Model:setupCommandToolBar")
	protected  void setupCommandToolBar () {
		//do nothing
	}


}

/* Radix::System::EventLog:ForContextLog_SysUnits:Model - Desktop Meta*/

/*Radix::System::EventLog:ForContextLog_SysUnits:Model-Group Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ForContextLog_SysUnits:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmDEZXGYRBYVBADE5OCIBI73JN2I"),
						"ForContextLog_SysUnits:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agmVJGCRERZOBDS7GULG6RLDVLG3A"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:ForContextLog_SysUnits:Model:Properties-Properties*/
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

/* Radix::System::EventLog:ForContextLog_SysUnits:Model - Web Executable*/

/*Radix::System::EventLog:ForContextLog_SysUnits:Model-Group Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:ForContextLog_SysUnits:Model")
public class ForContextLog_SysUnits:Model  extends org.radixware.ads.System.web.ForContextLog:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return ForContextLog_SysUnits:Model_mi.rdxMeta; }



	public ForContextLog_SysUnits:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::System::EventLog:ForContextLog_SysUnits:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:ForContextLog_SysUnits:Model:Properties-Properties*/

	/*Radix::System::EventLog:ForContextLog_SysUnits:Model:Methods-Methods*/


}

/* Radix::System::EventLog:ForContextLog_SysUnits:Model - Web Meta*/

/*Radix::System::EventLog:ForContextLog_SysUnits:Model-Group Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ForContextLog_SysUnits:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmDEZXGYRBYVBADE5OCIBI73JN2I"),
						"ForContextLog_SysUnits:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agmVJGCRERZOBDS7GULG6RLDVLG3A"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:ForContextLog_SysUnits:Model:Properties-Properties*/
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

/* Radix::System::EventLog:WithContextInfo_Dashboard - Desktop Meta*/

/*Radix::System::EventLog:WithContextInfo_Dashboard-Selector Presentation*/

package org.radixware.ads.System.explorer;
public final class WithContextInfo_Dashboard_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new WithContextInfo_Dashboard_mi();
	private WithContextInfo_Dashboard_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprE4HR7KORYZDQBAP4O5XMDBLIPE"),
		"WithContextInfo_Dashboard",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVWY625ANRTNRDB5PAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
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
		16635,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEEUTDHGEXTNRDB6RAALOMT5GDM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},
		false,false,false,0,0);
		columns = null;;
	}
}
/* Radix::System::EventLog:WithContextInfo_Dashboard - Web Meta*/

/*Radix::System::EventLog:WithContextInfo_Dashboard-Selector Presentation*/

package org.radixware.ads.System.web;
public final class WithContextInfo_Dashboard_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new WithContextInfo_Dashboard_mi();
	private WithContextInfo_Dashboard_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprE4HR7KORYZDQBAP4O5XMDBLIPE"),
		"WithContextInfo_Dashboard",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVWY625ANRTNRDB5PAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
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
		16635,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEEUTDHGEXTNRDB6RAALOMT5GDM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},
		false,false,false,0,0);
		columns = null;;
	}
}
/* Radix::System::EventLog:WithContextInfo_Dashboard:Model - Desktop Executable*/

/*Radix::System::EventLog:WithContextInfo_Dashboard:Model-Group Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model")
public class WithContextInfo_Dashboard:Model  extends org.radixware.ads.System.explorer.Contextless:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return WithContextInfo_Dashboard:Model_mi.rdxMeta; }



	public WithContextInfo_Dashboard:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:Properties-Properties*/

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:lastUserActivityMills-Dynamic Property*/



	protected long lastUserActivityMills=-1;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:lastUserActivityMills")
	  long getLastUserActivityMills() {
		return lastUserActivityMills;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:lastUserActivityMills")
	   void setLastUserActivityMills(long val) {
		lastUserActivityMills = val;
	}

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:updateResumeTime-Dynamic Property*/



	protected int updateResumeTime=30;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:updateResumeTime")
	  int getUpdateResumeTime() {
		return updateResumeTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:updateResumeTime")
	   void setUpdateResumeTime(int val) {
		updateResumeTime = val;
	}

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:minUpdateResumeTime-Dynamic Property*/



	protected int minUpdateResumeTime=15;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:minUpdateResumeTime")
	  int getMinUpdateResumeTime() {
		return minUpdateResumeTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:minUpdateResumeTime")
	   void setMinUpdateResumeTime(int val) {
		minUpdateResumeTime = val;
	}






	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:Methods-Methods*/

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:timer-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:timer")
	protected  void timer () {
		if(isUpdate && !isPaused) {
		    return;
		}

		if((System.currentTimeMillis() - updateResumeTime * 1000) >= lastUserActivityMills) {
		    btnUpdate.setChecked(true);
		    btnUpdate.clicked.emit(true);
		}
	}

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:isUpdateEnabled-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:isUpdateEnabled")
	public  boolean isUpdateEnabled () {
		return isUpdate && !isPaused;
	}

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:setupCommandToolBar-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:setupCommandToolBar")
	protected  void setupCommandToolBar () {
		final Explorer.Views::SelectorView view = (Explorer.Views::SelectorView)getGroupView();
		final com.trolltech.qt.gui.QToolBar toolBar = view.getToolBar();
		toolBar.addSeparator();

		btnUpdate = new com.trolltech.qt.gui.QToolButton(toolBar);
		btnUpdate.setToolButtonStyle(com.trolltech.qt.core.Qt.ToolButtonStyle.ToolButtonIconOnly);
		btnUpdate.setIcon(Explorer.Icons::ExplorerIcon.getQIcon(getEnvironment().getDefManager().getImage(isUpdate ? idof[Workflow::update_active] : idof[Workflow::update_disabled])));
		btnUpdate.setToolTip("Auto Update");
		btnUpdate.setCheckable(true);
		btnUpdate.setChecked(isUpdate);
		btnUpdate.clicked.connect(this, idof[EventLog:Contextless:Contextless:Model:updateClicked] + "()");
		toolBar.addWidget(btnUpdate);
	}

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		isUpdate = true;
		isPaused = false;
		period = 1; //1 sec.
	}

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:setLastUserActivityTime-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:setLastUserActivityTime")
	public  void setLastUserActivityTime () {
		lastUserActivityMills = System.currentTimeMillis();
	}

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:updateClicked-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:updateClicked")
	protected published  void updateClicked () {
		super.updateClicked();
		if (!timer.isActive()) {
		    timer.start(period * 1000);
		}
	}

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:setUpdateResumeTime-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:setUpdateResumeTime")
	public  void setUpdateResumeTime (int timeSec) {
		if(timeSec >= minUpdateResumeTime) {
		    updateResumeTime = timeSec;
		} else {
		    getEnvironment().getTracer().warning("Can't set update resume time of EventLog widget less than " + minUpdateResumeTime);
		}
	}

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:getUpdateResumeTime-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:getUpdateResumeTime")
	public  int getUpdateResumeTime () {
		return updateResumeTime;
	}

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:setResumeUpdateBtnEnabled-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:setResumeUpdateBtnEnabled")
	public  void setResumeUpdateBtnEnabled (boolean isEnabled) {
		btnUpdate.setEnabled(isEnabled);
	}


}

/* Radix::System::EventLog:WithContextInfo_Dashboard:Model - Desktop Meta*/

/*Radix::System::EventLog:WithContextInfo_Dashboard:Model-Group Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class WithContextInfo_Dashboard:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmE4HR7KORYZDQBAP4O5XMDBLIPE"),
						"WithContextInfo_Dashboard:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agmF6ONI4OEXTNRDB6RAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:Properties-Properties*/
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

/* Radix::System::EventLog:WithContextInfo_Dashboard:Model - Web Executable*/

/*Radix::System::EventLog:WithContextInfo_Dashboard:Model-Group Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model")
public class WithContextInfo_Dashboard:Model  extends org.radixware.ads.System.web.Contextless:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return WithContextInfo_Dashboard:Model_mi.rdxMeta; }



	public WithContextInfo_Dashboard:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:Properties-Properties*/

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:lastUserActivityMills-Dynamic Property*/



	protected long lastUserActivityMills=-1;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:lastUserActivityMills")
	  long getLastUserActivityMills() {
		return lastUserActivityMills;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:lastUserActivityMills")
	   void setLastUserActivityMills(long val) {
		lastUserActivityMills = val;
	}

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:updateResumeTime-Dynamic Property*/



	protected int updateResumeTime=30;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:updateResumeTime")
	  int getUpdateResumeTime() {
		return updateResumeTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:updateResumeTime")
	   void setUpdateResumeTime(int val) {
		updateResumeTime = val;
	}

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:minUpdateResumeTime-Dynamic Property*/



	protected int minUpdateResumeTime=15;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:minUpdateResumeTime")
	  int getMinUpdateResumeTime() {
		return minUpdateResumeTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:minUpdateResumeTime")
	   void setMinUpdateResumeTime(int val) {
		minUpdateResumeTime = val;
	}






	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:Methods-Methods*/

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:timer-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:timer")
	protected  void timer () {

	}

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:isUpdateEnabled-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:isUpdateEnabled")
	public  boolean isUpdateEnabled () {
		return isUpdate && !isPaused;
	}

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		isUpdate = true;
		isPaused = false;
		period = 1; //1 sec.
	}

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:setLastUserActivityTime-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:setLastUserActivityTime")
	public  void setLastUserActivityTime () {
		lastUserActivityMills = System.currentTimeMillis();
	}

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:updateClicked-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:updateClicked")
	protected published  void updateClicked () {

	}

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:setUpdateResumeTime-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:setUpdateResumeTime")
	public  void setUpdateResumeTime (int timeSec) {
		if(timeSec >= minUpdateResumeTime) {
		    updateResumeTime = timeSec;
		} else {
		    getEnvironment().getTracer().warning("Can't set update resume time of EventLog widget less than " + minUpdateResumeTime);
		}
	}

	/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:getUpdateResumeTime-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:WithContextInfo_Dashboard:Model:getUpdateResumeTime")
	public  int getUpdateResumeTime () {
		return updateResumeTime;
	}


}

/* Radix::System::EventLog:WithContextInfo_Dashboard:Model - Web Meta*/

/*Radix::System::EventLog:WithContextInfo_Dashboard:Model-Group Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class WithContextInfo_Dashboard:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmE4HR7KORYZDQBAP4O5XMDBLIPE"),
						"WithContextInfo_Dashboard:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agmF6ONI4OEXTNRDB6RAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:WithContextInfo_Dashboard:Model:Properties-Properties*/
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

/* Radix::System::EventLog:ContextlessNoAutoUpdate - Desktop Meta*/

/*Radix::System::EventLog:ContextlessNoAutoUpdate-Selector Presentation*/

package org.radixware.ads.System.explorer;
public final class ContextlessNoAutoUpdate_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new ContextlessNoAutoUpdate_mi();
	private ContextlessNoAutoUpdate_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7X6MMAXKTZHUHB46WUW333PMMA"),
		"ContextlessNoAutoUpdate",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprF6ONI4OEXTNRDB6RAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
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
		16633,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},
		false,true,false,0,0);
		columns = null;;
	}
}
/* Radix::System::EventLog:ContextlessNoAutoUpdate - Web Meta*/

/*Radix::System::EventLog:ContextlessNoAutoUpdate-Selector Presentation*/

package org.radixware.ads.System.web;
public final class ContextlessNoAutoUpdate_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new ContextlessNoAutoUpdate_mi();
	private ContextlessNoAutoUpdate_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr7X6MMAXKTZHUHB46WUW333PMMA"),
		"ContextlessNoAutoUpdate",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprF6ONI4OEXTNRDB6RAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
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
		16633,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWKBANRYNRTNRDB5PAALOMT5GDM")},
		false,true,false,0,0);
		columns = null;;
	}
}
/* Radix::System::EventLog:ContextlessNoAutoUpdate:Model - Desktop Executable*/

/*Radix::System::EventLog:ContextlessNoAutoUpdate:Model-Group Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:ContextlessNoAutoUpdate:Model")
public class ContextlessNoAutoUpdate:Model  extends org.radixware.ads.System.explorer.Contextless:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return ContextlessNoAutoUpdate:Model_mi.rdxMeta; }



	public ContextlessNoAutoUpdate:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::System::EventLog:ContextlessNoAutoUpdate:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:ContextlessNoAutoUpdate:Model:Properties-Properties*/

	/*Radix::System::EventLog:ContextlessNoAutoUpdate:Model:Methods-Methods*/

	/*Radix::System::EventLog:ContextlessNoAutoUpdate:Model:isAutoUpdateEnabled-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:ContextlessNoAutoUpdate:Model:isAutoUpdateEnabled")
	protected published  boolean isAutoUpdateEnabled () {
		return false;
	}


}

/* Radix::System::EventLog:ContextlessNoAutoUpdate:Model - Desktop Meta*/

/*Radix::System::EventLog:ContextlessNoAutoUpdate:Model-Group Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ContextlessNoAutoUpdate:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agm7X6MMAXKTZHUHB46WUW333PMMA"),
						"ContextlessNoAutoUpdate:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agmF6ONI4OEXTNRDB6RAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:ContextlessNoAutoUpdate:Model:Properties-Properties*/
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

/* Radix::System::EventLog:ContextlessNoAutoUpdate:Model - Web Executable*/

/*Radix::System::EventLog:ContextlessNoAutoUpdate:Model-Group Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:ContextlessNoAutoUpdate:Model")
public class ContextlessNoAutoUpdate:Model  extends org.radixware.ads.System.web.Contextless:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return ContextlessNoAutoUpdate:Model_mi.rdxMeta; }



	public ContextlessNoAutoUpdate:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::System::EventLog:ContextlessNoAutoUpdate:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:ContextlessNoAutoUpdate:Model:Properties-Properties*/

	/*Radix::System::EventLog:ContextlessNoAutoUpdate:Model:Methods-Methods*/

	/*Radix::System::EventLog:ContextlessNoAutoUpdate:Model:isAutoUpdateEnabled-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:ContextlessNoAutoUpdate:Model:isAutoUpdateEnabled")
	protected published  boolean isAutoUpdateEnabled () {
		return false;
	}


}

/* Radix::System::EventLog:ContextlessNoAutoUpdate:Model - Web Meta*/

/*Radix::System::EventLog:ContextlessNoAutoUpdate:Model-Group Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ContextlessNoAutoUpdate:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agm7X6MMAXKTZHUHB46WUW333PMMA"),
						"ContextlessNoAutoUpdate:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agmF6ONI4OEXTNRDB6RAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:ContextlessNoAutoUpdate:Model:Properties-Properties*/
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

/* Radix::System::EventLog:Today:Model - Desktop Executable*/

/*Radix::System::EventLog:Today:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Today:Model")
public class Today:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Today:Model_mi.rdxMeta; }



	public Today:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::EventLog:Today:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:Today:Model:Properties-Properties*/










	/*Radix::System::EventLog:Today:Model:Methods-Methods*/

	/*Radix::System::EventLog:Today:Model:isVisible-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Today:Model:isVisible")
	public published  boolean isVisible () {
		return false;
	}

	/*Radix::System::EventLog:Today:minSeverity:minSeverity-Presentation Property*/




	public class MinSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MinSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Today:minSeverity:minSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Today:minSeverity:minSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MinSeverity getMinSeverity(){return (MinSeverity)getProperty(prmSF36PORPYBF7ZH2MFL5OQT3LPY);}

	/*Radix::System::EventLog:Today:source:source-Presentation Property*/




	public class Source extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Source(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Today:source:source")
		public  org.radixware.ads.Arte.common.EventSource getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Today:source:source")
		public   void setValue(org.radixware.ads.Arte.common.EventSource val) {
			Value = val;
		}
	}
	public Source getSource(){return (Source)getProperty(prmPUKHAASBJJFXDP7WOVZJGKJRVM);}


}

/* Radix::System::EventLog:Today:Model - Desktop Meta*/

/*Radix::System::EventLog:Today:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Today:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcUV5CRRHED3OBDCHDAALOMT5GDM"),
						"Today:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:Today:Model:Properties-Properties*/
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

/* Radix::System::EventLog:Today:Model - Web Executable*/

/*Radix::System::EventLog:Today:Model-Filter Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Today:Model")
public class Today:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Today:Model_mi.rdxMeta; }



	public Today:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::EventLog:Today:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:Today:Model:Properties-Properties*/










	/*Radix::System::EventLog:Today:Model:Methods-Methods*/

	/*Radix::System::EventLog:Today:Model:isVisible-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Today:Model:isVisible")
	public published  boolean isVisible () {
		return false;
	}

	/*Radix::System::EventLog:Today:minSeverity:minSeverity-Presentation Property*/




	public class MinSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MinSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Today:minSeverity:minSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Today:minSeverity:minSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MinSeverity getMinSeverity(){return (MinSeverity)getProperty(prmSF36PORPYBF7ZH2MFL5OQT3LPY);}

	/*Radix::System::EventLog:Today:source:source-Presentation Property*/




	public class Source extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Source(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Today:source:source")
		public  org.radixware.ads.Arte.common.EventSource getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Today:source:source")
		public   void setValue(org.radixware.ads.Arte.common.EventSource val) {
			Value = val;
		}
	}
	public Source getSource(){return (Source)getProperty(prmPUKHAASBJJFXDP7WOVZJGKJRVM);}


}

/* Radix::System::EventLog:Today:Model - Web Meta*/

/*Radix::System::EventLog:Today:Model-Filter Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Today:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcUV5CRRHED3OBDCHDAALOMT5GDM"),
						"Today:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:Today:Model:Properties-Properties*/
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

/* Radix::System::EventLog:TimeRange:Model - Desktop Executable*/

/*Radix::System::EventLog:TimeRange:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:TimeRange:Model")
public class TimeRange:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return TimeRange:Model_mi.rdxMeta; }



	public TimeRange:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::EventLog:TimeRange:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:TimeRange:Model:Properties-Properties*/














	/*Radix::System::EventLog:TimeRange:Model:Methods-Methods*/

	/*Radix::System::EventLog:TimeRange:Model:isVisible-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:TimeRange:Model:isVisible")
	public published  boolean isVisible () {
		return false;
	}

	/*Radix::System::EventLog:TimeRange:toTime:toTime-Presentation Property*/




	public class ToTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public ToTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:TimeRange:toTime:toTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:TimeRange:toTime:toTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public ToTime getToTime(){return (ToTime)getProperty(prmP7RHN4373JGJTEIALKA3SCAU5A);}

	/*Radix::System::EventLog:TimeRange:minSeverity:minSeverity-Presentation Property*/




	public class MinSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MinSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:TimeRange:minSeverity:minSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:TimeRange:minSeverity:minSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MinSeverity getMinSeverity(){return (MinSeverity)getProperty(prmBQUVW3USB5C45DVW5IXJCAZU5Q);}

	/*Radix::System::EventLog:TimeRange:source:source-Presentation Property*/




	public class Source extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Source(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:TimeRange:source:source")
		public  org.radixware.ads.Arte.common.EventSource getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:TimeRange:source:source")
		public   void setValue(org.radixware.ads.Arte.common.EventSource val) {
			Value = val;
		}
	}
	public Source getSource(){return (Source)getProperty(prmGXLCPBC4JFHIBE4MRVPYZOARKM);}

	/*Radix::System::EventLog:TimeRange:fromTime:fromTime-Presentation Property*/




	public class FromTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public FromTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:TimeRange:fromTime:fromTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:TimeRange:fromTime:fromTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public FromTime getFromTime(){return (FromTime)getProperty(prmD7TRMTA7IFEH3M5LD7SUEACI7Y);}


}

/* Radix::System::EventLog:TimeRange:Model - Desktop Meta*/

/*Radix::System::EventLog:TimeRange:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TimeRange:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmc3LGDG7LCCVEP5GK5MQN7UUVELA"),
						"TimeRange:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:TimeRange:Model:Properties-Properties*/
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

/* Radix::System::EventLog:TimeRange:Model - Web Executable*/

/*Radix::System::EventLog:TimeRange:Model-Filter Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:TimeRange:Model")
public class TimeRange:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return TimeRange:Model_mi.rdxMeta; }



	public TimeRange:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::EventLog:TimeRange:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:TimeRange:Model:Properties-Properties*/














	/*Radix::System::EventLog:TimeRange:Model:Methods-Methods*/

	/*Radix::System::EventLog:TimeRange:Model:isVisible-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:TimeRange:Model:isVisible")
	public published  boolean isVisible () {
		return false;
	}

	/*Radix::System::EventLog:TimeRange:toTime:toTime-Presentation Property*/




	public class ToTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public ToTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:TimeRange:toTime:toTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:TimeRange:toTime:toTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public ToTime getToTime(){return (ToTime)getProperty(prmP7RHN4373JGJTEIALKA3SCAU5A);}

	/*Radix::System::EventLog:TimeRange:minSeverity:minSeverity-Presentation Property*/




	public class MinSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MinSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:TimeRange:minSeverity:minSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:TimeRange:minSeverity:minSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MinSeverity getMinSeverity(){return (MinSeverity)getProperty(prmBQUVW3USB5C45DVW5IXJCAZU5Q);}

	/*Radix::System::EventLog:TimeRange:source:source-Presentation Property*/




	public class Source extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Source(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:TimeRange:source:source")
		public  org.radixware.ads.Arte.common.EventSource getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:TimeRange:source:source")
		public   void setValue(org.radixware.ads.Arte.common.EventSource val) {
			Value = val;
		}
	}
	public Source getSource(){return (Source)getProperty(prmGXLCPBC4JFHIBE4MRVPYZOARKM);}

	/*Radix::System::EventLog:TimeRange:fromTime:fromTime-Presentation Property*/




	public class FromTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public FromTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:TimeRange:fromTime:fromTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:TimeRange:fromTime:fromTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public FromTime getFromTime(){return (FromTime)getProperty(prmD7TRMTA7IFEH3M5LD7SUEACI7Y);}


}

/* Radix::System::EventLog:TimeRange:Model - Web Meta*/

/*Radix::System::EventLog:TimeRange:Model-Filter Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TimeRange:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmc3LGDG7LCCVEP5GK5MQN7UUVELA"),
						"TimeRange:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:TimeRange:Model:Properties-Properties*/
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

/* Radix::System::EventLog:User:Model - Desktop Executable*/

/*Radix::System::EventLog:User:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:User:Model")
public class User:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return User:Model_mi.rdxMeta; }



	public User:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::EventLog:User:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:User:Model:Properties-Properties*/














	/*Radix::System::EventLog:User:Model:Methods-Methods*/

	/*Radix::System::EventLog:User:Model:isVisible-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:User:Model:isVisible")
	public published  boolean isVisible () {
		return false;
	}

	/*Radix::System::EventLog:User:pUser:pUser-Presentation Property*/




	public class PUser extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public PUser(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.filters.RadFilterParamDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.User.User_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.explorer.User.User_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.User.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.explorer.User.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:User:pUser:pUser")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:User:pUser:pUser")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public PUser getPUser(){return (PUser)getProperty(prmOJRL3DEB6FFVXLHX6JJHF2RQW4);}

	/*Radix::System::EventLog:User:minSeverity:minSeverity-Presentation Property*/




	public class MinSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MinSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:User:minSeverity:minSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:User:minSeverity:minSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MinSeverity getMinSeverity(){return (MinSeverity)getProperty(prmG3NAYPEU3FBELFTF2VTLCGGAG4);}

	/*Radix::System::EventLog:User:fromDate:fromDate-Presentation Property*/




	public class FromDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public FromDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:User:fromDate:fromDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:User:fromDate:fromDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public FromDate getFromDate(){return (FromDate)getProperty(prmMSYXKT4C4ZHSHPYWYXF5IUERA4);}

	/*Radix::System::EventLog:User:toDate:toDate-Presentation Property*/




	public class ToDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public ToDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:User:toDate:toDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:User:toDate:toDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public ToDate getToDate(){return (ToDate)getProperty(prmFCSBJ7FNLFFTPH2GXQAFKZI4QA);}


}

/* Radix::System::EventLog:User:Model - Desktop Meta*/

/*Radix::System::EventLog:User:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class User:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcQUTDPT7CTFGFVJRBQEFMWRTUFI"),
						"User:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:User:Model:Properties-Properties*/
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

/* Radix::System::EventLog:User:Model - Web Executable*/

/*Radix::System::EventLog:User:Model-Filter Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:User:Model")
public class User:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return User:Model_mi.rdxMeta; }



	public User:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::EventLog:User:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:User:Model:Properties-Properties*/














	/*Radix::System::EventLog:User:Model:Methods-Methods*/

	/*Radix::System::EventLog:User:Model:isVisible-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:User:Model:isVisible")
	public published  boolean isVisible () {
		return false;
	}

	/*Radix::System::EventLog:User:pUser:pUser-Presentation Property*/




	public class PUser extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public PUser(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.filters.RadFilterParamDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.web.User.User_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.web.User.User_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.web.User.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.web.User.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:User:pUser:pUser")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:User:pUser:pUser")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public PUser getPUser(){return (PUser)getProperty(prmOJRL3DEB6FFVXLHX6JJHF2RQW4);}

	/*Radix::System::EventLog:User:minSeverity:minSeverity-Presentation Property*/




	public class MinSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MinSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:User:minSeverity:minSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:User:minSeverity:minSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MinSeverity getMinSeverity(){return (MinSeverity)getProperty(prmG3NAYPEU3FBELFTF2VTLCGGAG4);}

	/*Radix::System::EventLog:User:fromDate:fromDate-Presentation Property*/




	public class FromDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public FromDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:User:fromDate:fromDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:User:fromDate:fromDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public FromDate getFromDate(){return (FromDate)getProperty(prmMSYXKT4C4ZHSHPYWYXF5IUERA4);}

	/*Radix::System::EventLog:User:toDate:toDate-Presentation Property*/




	public class ToDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public ToDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:User:toDate:toDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:User:toDate:toDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public ToDate getToDate(){return (ToDate)getProperty(prmFCSBJ7FNLFFTPH2GXQAFKZI4QA);}


}

/* Radix::System::EventLog:User:Model - Web Meta*/

/*Radix::System::EventLog:User:Model-Filter Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class User:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcQUTDPT7CTFGFVJRBQEFMWRTUFI"),
						"User:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:User:Model:Properties-Properties*/
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

/* Radix::System::EventLog:Station:Model - Desktop Executable*/

/*Radix::System::EventLog:Station:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Station:Model")
public class Station:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Station:Model_mi.rdxMeta; }



	public Station:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::EventLog:Station:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:Station:Model:Properties-Properties*/














	/*Radix::System::EventLog:Station:Model:Methods-Methods*/

	/*Radix::System::EventLog:Station:Model:isVisible-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Station:Model:isVisible")
	public published  boolean isVisible () {
		return false;
	}

	/*Radix::System::EventLog:Station:pStation:pStation-Presentation Property*/




	public class PStation extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public PStation(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.filters.RadFilterParamDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.Station.Station_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.explorer.Station.Station_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.Station.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.explorer.Station.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Station:pStation:pStation")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Station:pStation:pStation")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public PStation getPStation(){return (PStation)getProperty(prm2UCNYUDK2VGYVOUZKJINQQY4U4);}

	/*Radix::System::EventLog:Station:minSeverity:minSeverity-Presentation Property*/




	public class MinSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MinSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Station:minSeverity:minSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Station:minSeverity:minSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MinSeverity getMinSeverity(){return (MinSeverity)getProperty(prmFOQJM4EYHZGABACHFRQTRYHP5M);}

	/*Radix::System::EventLog:Station:fromDate:fromDate-Presentation Property*/




	public class FromDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public FromDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Station:fromDate:fromDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Station:fromDate:fromDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public FromDate getFromDate(){return (FromDate)getProperty(prmSVDGDRZILFA2JKFGXLVRXYLQ3Q);}

	/*Radix::System::EventLog:Station:toDate:toDate-Presentation Property*/




	public class ToDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public ToDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Station:toDate:toDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Station:toDate:toDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public ToDate getToDate(){return (ToDate)getProperty(prmREAJESHPOVESTNOIMUXQCVNZQQ);}


}

/* Radix::System::EventLog:Station:Model - Desktop Meta*/

/*Radix::System::EventLog:Station:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Station:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcM3WETXHKOZHYBIO63RKBNXMZVY"),
						"Station:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:Station:Model:Properties-Properties*/
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

/* Radix::System::EventLog:Station:Model - Web Executable*/

/*Radix::System::EventLog:Station:Model-Filter Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Station:Model")
public class Station:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Station:Model_mi.rdxMeta; }



	public Station:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::EventLog:Station:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:Station:Model:Properties-Properties*/














	/*Radix::System::EventLog:Station:Model:Methods-Methods*/

	/*Radix::System::EventLog:Station:Model:isVisible-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Station:Model:isVisible")
	public published  boolean isVisible () {
		return false;
	}

	/*Radix::System::EventLog:Station:pStation:pStation-Presentation Property*/




	public class PStation extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public PStation(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.filters.RadFilterParamDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.web.Station.Station_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.web.Station.Station_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.web.Station.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.web.Station.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Station:pStation:pStation")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Station:pStation:pStation")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public PStation getPStation(){return (PStation)getProperty(prm2UCNYUDK2VGYVOUZKJINQQY4U4);}

	/*Radix::System::EventLog:Station:minSeverity:minSeverity-Presentation Property*/




	public class MinSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MinSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Station:minSeverity:minSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Station:minSeverity:minSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MinSeverity getMinSeverity(){return (MinSeverity)getProperty(prmFOQJM4EYHZGABACHFRQTRYHP5M);}

	/*Radix::System::EventLog:Station:fromDate:fromDate-Presentation Property*/




	public class FromDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public FromDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Station:fromDate:fromDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Station:fromDate:fromDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public FromDate getFromDate(){return (FromDate)getProperty(prmSVDGDRZILFA2JKFGXLVRXYLQ3Q);}

	/*Radix::System::EventLog:Station:toDate:toDate-Presentation Property*/




	public class ToDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public ToDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Station:toDate:toDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Station:toDate:toDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public ToDate getToDate(){return (ToDate)getProperty(prmREAJESHPOVESTNOIMUXQCVNZQQ);}


}

/* Radix::System::EventLog:Station:Model - Web Meta*/

/*Radix::System::EventLog:Station:Model-Filter Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Station:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcM3WETXHKOZHYBIO63RKBNXMZVY"),
						"Station:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:Station:Model:Properties-Properties*/
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

/* Radix::System::EventLog:Severity:Model - Desktop Executable*/

/*Radix::System::EventLog:Severity:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Severity:Model")
public class Severity:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Severity:Model_mi.rdxMeta; }



	public Severity:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::EventLog:Severity:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:Severity:Model:Properties-Properties*/














	/*Radix::System::EventLog:Severity:Model:Methods-Methods*/

	/*Radix::System::EventLog:Severity:Model:isVisible-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Severity:Model:isVisible")
	public published  boolean isVisible () {
		return false;
	}

	/*Radix::System::EventLog:Severity:minSeverity:minSeverity-Presentation Property*/




	public class MinSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MinSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Severity:minSeverity:minSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Severity:minSeverity:minSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MinSeverity getMinSeverity(){return (MinSeverity)getProperty(prmEZYWX2UJTBFEHJXFY4U24PVUMI);}

	/*Radix::System::EventLog:Severity:fromDate:fromDate-Presentation Property*/




	public class FromDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public FromDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Severity:fromDate:fromDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Severity:fromDate:fromDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public FromDate getFromDate(){return (FromDate)getProperty(prmN3KFLDVQ3FEKNFRJQAXANO3QJE);}

	/*Radix::System::EventLog:Severity:toDate:toDate-Presentation Property*/




	public class ToDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public ToDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Severity:toDate:toDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Severity:toDate:toDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public ToDate getToDate(){return (ToDate)getProperty(prmA6ZXQA6P65ER5KEZL7S5HAF2MU);}

	/*Radix::System::EventLog:Severity:maxSeverity:maxSeverity-Presentation Property*/




	public class MaxSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Severity:maxSeverity:maxSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Severity:maxSeverity:maxSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MaxSeverity getMaxSeverity(){return (MaxSeverity)getProperty(prm4LJSDT4XNVAPJL66DQZJTUO2P4);}


}

/* Radix::System::EventLog:Severity:Model - Desktop Meta*/

/*Radix::System::EventLog:Severity:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Severity:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcBYGYNUVM4JBGZO3KC6O7C5I65Y"),
						"Severity:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:Severity:Model:Properties-Properties*/
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

/* Radix::System::EventLog:Severity:Model - Web Executable*/

/*Radix::System::EventLog:Severity:Model-Filter Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Severity:Model")
public class Severity:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Severity:Model_mi.rdxMeta; }



	public Severity:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::EventLog:Severity:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:Severity:Model:Properties-Properties*/














	/*Radix::System::EventLog:Severity:Model:Methods-Methods*/

	/*Radix::System::EventLog:Severity:Model:isVisible-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Severity:Model:isVisible")
	public published  boolean isVisible () {
		return false;
	}

	/*Radix::System::EventLog:Severity:minSeverity:minSeverity-Presentation Property*/




	public class MinSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MinSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Severity:minSeverity:minSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Severity:minSeverity:minSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MinSeverity getMinSeverity(){return (MinSeverity)getProperty(prmEZYWX2UJTBFEHJXFY4U24PVUMI);}

	/*Radix::System::EventLog:Severity:fromDate:fromDate-Presentation Property*/




	public class FromDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public FromDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Severity:fromDate:fromDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Severity:fromDate:fromDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public FromDate getFromDate(){return (FromDate)getProperty(prmN3KFLDVQ3FEKNFRJQAXANO3QJE);}

	/*Radix::System::EventLog:Severity:toDate:toDate-Presentation Property*/




	public class ToDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public ToDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Severity:toDate:toDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Severity:toDate:toDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public ToDate getToDate(){return (ToDate)getProperty(prmA6ZXQA6P65ER5KEZL7S5HAF2MU);}

	/*Radix::System::EventLog:Severity:maxSeverity:maxSeverity-Presentation Property*/




	public class MaxSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Severity:maxSeverity:maxSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Severity:maxSeverity:maxSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MaxSeverity getMaxSeverity(){return (MaxSeverity)getProperty(prm4LJSDT4XNVAPJL66DQZJTUO2P4);}


}

/* Radix::System::EventLog:Severity:Model - Web Meta*/

/*Radix::System::EventLog:Severity:Model-Filter Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Severity:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcBYGYNUVM4JBGZO3KC6O7C5I65Y"),
						"Severity:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:Severity:Model:Properties-Properties*/
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

/* Radix::System::EventLog:Imported:Model - Desktop Executable*/

/*Radix::System::EventLog:Imported:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Imported:Model")
public class Imported:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Imported:Model_mi.rdxMeta; }



	public Imported:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::EventLog:Imported:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:Imported:Model:Properties-Properties*/










	/*Radix::System::EventLog:Imported:Model:Methods-Methods*/

	/*Radix::System::EventLog:Imported:pFile:pFile-Presentation Property*/




	public class PFile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PFile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Imported:pFile:pFile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Imported:pFile:pFile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PFile getPFile(){return (PFile)getProperty(prmONPRLRODY5BFPM62Q72R53JXLY);}

	/*Radix::System::EventLog:Imported:minSeverity:minSeverity-Presentation Property*/




	public class MinSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MinSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Imported:minSeverity:minSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Imported:minSeverity:minSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MinSeverity getMinSeverity(){return (MinSeverity)getProperty(prmRUGE7VE44ND25EYXVNNA6LDPZA);}


}

/* Radix::System::EventLog:Imported:Model - Desktop Meta*/

/*Radix::System::EventLog:Imported:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Imported:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcS5F72ARLKFBGJLCKEX27ZDUJR4"),
						"Imported:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:Imported:Model:Properties-Properties*/
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

/* Radix::System::EventLog:Imported:Model - Web Executable*/

/*Radix::System::EventLog:Imported:Model-Filter Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Imported:Model")
public class Imported:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Imported:Model_mi.rdxMeta; }



	public Imported:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::EventLog:Imported:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:Imported:Model:Properties-Properties*/










	/*Radix::System::EventLog:Imported:Model:Methods-Methods*/

	/*Radix::System::EventLog:Imported:pFile:pFile-Presentation Property*/




	public class PFile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PFile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Imported:pFile:pFile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Imported:pFile:pFile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PFile getPFile(){return (PFile)getProperty(prmONPRLRODY5BFPM62Q72R53JXLY);}

	/*Radix::System::EventLog:Imported:minSeverity:minSeverity-Presentation Property*/




	public class MinSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MinSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Imported:minSeverity:minSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Imported:minSeverity:minSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MinSeverity getMinSeverity(){return (MinSeverity)getProperty(prmRUGE7VE44ND25EYXVNNA6LDPZA);}


}

/* Radix::System::EventLog:Imported:Model - Web Meta*/

/*Radix::System::EventLog:Imported:Model-Filter Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Imported:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcS5F72ARLKFBGJLCKEX27ZDUJR4"),
						"Imported:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:Imported:Model:Properties-Properties*/
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

/* Radix::System::EventLog:EventCodes:Model - Desktop Executable*/

/*Radix::System::EventLog:EventCodes:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:Model")
public class EventCodes:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return EventCodes:Model_mi.rdxMeta; }



	public EventCodes:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::EventLog:EventCodes:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:EventCodes:Model:Properties-Properties*/

	/*Radix::System::EventLog:EventCodes:Model:dummy-Presentation Property*/




	public class Dummy extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public Dummy(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:Model:dummy")
		public published  org.radixware.kernel.common.types.ArrStr getValue() {

			if (eventCodes.Value == null) {
			    return  null;
			} else {
			    return  (ArrStr) Types::ValAsStr.fromStr(eventCodes.Value, Meta::ValType:ArrStr);
			}
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:Model:dummy")
		public published   void setValue(org.radixware.kernel.common.types.ArrStr val) {

			if (val == null) {
			    eventCodes.Value = null;
			} else
			    eventCodes.Value = Types::ValAsStr.toStr(val, Meta::ValType:ArrStr);
		}
	}
	public Dummy getDummy(){return (Dummy)getProperty(prdUZRYIWXRUBCBVOR6XGILTQ7K5Y);}

	/*Radix::System::EventLog:EventCodes:Model:eventCodes-Presentation Property*/




	public class EventCodes extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public EventCodes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}

		/*Radix::System::EventLog:EventCodes:Model:eventCodes:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::EventLog:EventCodes:Model:eventCodes:Nested classes-Nested Classes*/

		/*Radix::System::EventLog:EventCodes:Model:eventCodes:Properties-Properties*/

		/*Radix::System::EventLog:EventCodes:Model:eventCodes:Methods-Methods*/

		/*Radix::System::EventLog:EventCodes:Model:eventCodes:execPropEditorDialog-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:Model:eventCodes:execPropEditorDialog")
		public published  org.radixware.kernel.common.client.views.IDialog.DialogResult execPropEditorDialog () {


			if (dummy.execPropEditorDialog() == Client.Views::DialogResult.ACCEPTED) {
			 
			    return Client.Views::DialogResult.ACCEPTED;
			} else {
			    return Client.Views::DialogResult.REJECTED;
			}
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:Model:eventCodes")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:Model:eventCodes")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public EventCodes getEventCodes(){return (EventCodes)getProperty(prmXO3CGUSUW5BZLBZ6NRCTNV577E);}














	/*Radix::System::EventLog:EventCodes:Model:Methods-Methods*/

	/*Radix::System::EventLog:EventCodes:Model:getDisplayString-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:Model:getDisplayString")
	public published  Str getDisplayString (org.radixware.kernel.common.types.Id propertyId, java.lang.Object propertyValue, Str defaultDisplayString, boolean isInherited) {
		if (propertyId == idof[EventLog:EventCodes:eventCodes]) {
		    final ArrStr arr = dummy.Value;
		    
		    if (arr != null) {
		        java.lang.StringBuilder result = new StringBuilder();
		        boolean first = true;
		        for (Str code : arr) {
		            String text = "";
		            try {
		                text = getEnvironment().getDefManager().getEventTitleByCode(code);
		            } catch (Throwable ex) {
		                text = code;
		            }
		            if (!first) {
		                result.append(", ");
		            } else {
		                first = false;
		            }
		            result.append(text);
		        }
		        return result.toString();
		    }
		}

		return super.getDisplayString(propertyId, propertyValue, defaultDisplayString, isInherited);
	}

	/*Radix::System::EventLog:EventCodes:Model:isVisible-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:Model:isVisible")
	public published  boolean isVisible () {
		return false;
	}

	/*Radix::System::EventLog:EventCodes:fromDate:fromDate-Presentation Property*/




	public class FromDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public FromDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:fromDate:fromDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:fromDate:fromDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public FromDate getFromDate(){return (FromDate)getProperty(prm2P723KWXQJA7DJ5E4OY7CNXRK4);}

	/*Radix::System::EventLog:EventCodes:toDate:toDate-Presentation Property*/




	public class ToDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public ToDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:toDate:toDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:toDate:toDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public ToDate getToDate(){return (ToDate)getProperty(prmINQENZUDPJFU3H2I5H6JDKAFWQ);}


}

/* Radix::System::EventLog:EventCodes:Model - Desktop Meta*/

/*Radix::System::EventLog:EventCodes:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EventCodes:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcLFNJNPTVBJDWJDA57KRSGBRE2E"),
						"EventCodes:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:EventCodes:Model:Properties-Properties*/
						new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

								/*Radix::System::EventLog:EventCodes:Model:dummy:dummy:PropertyPresentation-Property Presentation*/
								new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUZRYIWXRUBCBVOR6XGILTQ7K5Y"),
									"dummy",
									null,
									null,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
									org.radixware.kernel.common.enums.EValType.ARR_STR,
									org.radixware.kernel.common.enums.EPropNature.VIRTUAL,
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
									org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeXGPX2QQOIVANBK6FXI2GY4U4D4"),
									false,

									/*Radix::System::EventLog:EventCodes:Model:dummy:dummy:PropertyPresentation:Edit Options:-Edit Mask Str*/
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
						null,
						false,
						false,
						false
						);
}

/* Radix::System::EventLog:EventCodes:Model - Web Executable*/

/*Radix::System::EventLog:EventCodes:Model-Filter Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:Model")
public class EventCodes:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return EventCodes:Model_mi.rdxMeta; }



	public EventCodes:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::EventLog:EventCodes:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:EventCodes:Model:Properties-Properties*/

	/*Radix::System::EventLog:EventCodes:Model:dummy-Presentation Property*/




	public class Dummy extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public Dummy(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:Model:dummy")
		public published  org.radixware.kernel.common.types.ArrStr getValue() {

			if (eventCodes.Value == null) {
			    return  null;
			} else {
			    return  (ArrStr) Types::ValAsStr.fromStr(eventCodes.Value, Meta::ValType:ArrStr);
			}
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:Model:dummy")
		public published   void setValue(org.radixware.kernel.common.types.ArrStr val) {

			if (val == null) {
			    eventCodes.Value = null;
			} else
			    eventCodes.Value = Types::ValAsStr.toStr(val, Meta::ValType:ArrStr);
		}
	}
	public Dummy getDummy(){return (Dummy)getProperty(prdUZRYIWXRUBCBVOR6XGILTQ7K5Y);}

	/*Radix::System::EventLog:EventCodes:Model:eventCodes-Presentation Property*/




	public class EventCodes extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public EventCodes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}

		/*Radix::System::EventLog:EventCodes:Model:eventCodes:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::EventLog:EventCodes:Model:eventCodes:Nested classes-Nested Classes*/

		/*Radix::System::EventLog:EventCodes:Model:eventCodes:Properties-Properties*/

		/*Radix::System::EventLog:EventCodes:Model:eventCodes:Methods-Methods*/

		/*Radix::System::EventLog:EventCodes:Model:eventCodes:execPropEditorDialog-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:Model:eventCodes:execPropEditorDialog")
		public published  org.radixware.kernel.common.client.views.IDialog.DialogResult execPropEditorDialog () {


			if (dummy.execPropEditorDialog() == Client.Views::DialogResult.ACCEPTED) {
			 
			    return Client.Views::DialogResult.ACCEPTED;
			} else {
			    return Client.Views::DialogResult.REJECTED;
			}
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:Model:eventCodes")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:Model:eventCodes")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public EventCodes getEventCodes(){return (EventCodes)getProperty(prmXO3CGUSUW5BZLBZ6NRCTNV577E);}














	/*Radix::System::EventLog:EventCodes:Model:Methods-Methods*/

	/*Radix::System::EventLog:EventCodes:Model:getDisplayString-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:Model:getDisplayString")
	public published  Str getDisplayString (org.radixware.kernel.common.types.Id propertyId, java.lang.Object propertyValue, Str defaultDisplayString, boolean isInherited) {
		if (propertyId == idof[EventLog:EventCodes:eventCodes]) {
		    final ArrStr arr = dummy.Value;
		    
		    if (arr != null) {
		        java.lang.StringBuilder result = new StringBuilder();
		        boolean first = true;
		        for (Str code : arr) {
		            String text = "";
		            try {
		                text = getEnvironment().getDefManager().getEventTitleByCode(code);
		            } catch (Throwable ex) {
		                text = code;
		            }
		            if (!first) {
		                result.append(", ");
		            } else {
		                first = false;
		            }
		            result.append(text);
		        }
		        return result.toString();
		    }
		}

		return super.getDisplayString(propertyId, propertyValue, defaultDisplayString, isInherited);
	}

	/*Radix::System::EventLog:EventCodes:Model:isVisible-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:Model:isVisible")
	public published  boolean isVisible () {
		return false;
	}

	/*Radix::System::EventLog:EventCodes:fromDate:fromDate-Presentation Property*/




	public class FromDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public FromDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:fromDate:fromDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:fromDate:fromDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public FromDate getFromDate(){return (FromDate)getProperty(prm2P723KWXQJA7DJ5E4OY7CNXRK4);}

	/*Radix::System::EventLog:EventCodes:toDate:toDate-Presentation Property*/




	public class ToDate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public ToDate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:toDate:toDate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:EventCodes:toDate:toDate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public ToDate getToDate(){return (ToDate)getProperty(prmINQENZUDPJFU3H2I5H6JDKAFWQ);}


}

/* Radix::System::EventLog:EventCodes:Model - Web Meta*/

/*Radix::System::EventLog:EventCodes:Model-Filter Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EventCodes:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcLFNJNPTVBJDWJDA57KRSGBRE2E"),
						"EventCodes:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:EventCodes:Model:Properties-Properties*/
						new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

								/*Radix::System::EventLog:EventCodes:Model:dummy:dummy:PropertyPresentation-Property Presentation*/
								new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUZRYIWXRUBCBVOR6XGILTQ7K5Y"),
									"dummy",
									null,
									null,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
									org.radixware.kernel.common.enums.EValType.ARR_STR,
									org.radixware.kernel.common.enums.EPropNature.VIRTUAL,
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
									org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeLUNCTCK525FYRBPRWUBKO7JWCY"),
									false,

									/*Radix::System::EventLog:EventCodes:Model:dummy:dummy:PropertyPresentation:Edit Options:-Edit Mask Str*/
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
						null,
						false,
						false,
						false
						);
}

/* Radix::System::EventLog:Period:Model - Desktop Executable*/

/*Radix::System::EventLog:Period:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model")
public class Period:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Period:Model_mi.rdxMeta; }



	public Period:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::EventLog:Period:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:Period:Model:Properties-Properties*/

	/*Radix::System::EventLog:Period:Model:period-Presentation Property*/




	public class Period extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Period(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:period")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:period")
		public   void setValue(Int val) {

			internal[period] = val;
			updateTimeRange();
		}
	}
	public Period getPeriod(){return (Period)getProperty(prmDVPXQQQ72VHPTELUBU5GXJALB4);}

	/*Radix::System::EventLog:Period:Model:fromTime-Presentation Property*/




	public class FromTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public FromTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:fromTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:fromTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public FromTime getFromTime(){return (FromTime)getProperty(prmNKTTHR6EAJB6FAJGHIDNIW3PHI);}

	/*Radix::System::EventLog:Period:Model:toTime-Presentation Property*/




	public class ToTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public ToTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:toTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:toTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public ToTime getToTime(){return (ToTime)getProperty(prmXOAKGU7PA5CSFDQACAVI2U3RCM);}

	/*Radix::System::EventLog:Period:Model:lastFromTime-Dynamic Property*/



	protected java.sql.Timestamp lastFromTime=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:lastFromTime")
	private final  java.sql.Timestamp getLastFromTime() {
		return lastFromTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:lastFromTime")
	private final   void setLastFromTime(java.sql.Timestamp val) {
		lastFromTime = val;
	}

	/*Radix::System::EventLog:Period:Model:lastToTime-Dynamic Property*/



	protected java.sql.Timestamp lastToTime=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:lastToTime")
	private final  java.sql.Timestamp getLastToTime() {
		return lastToTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:lastToTime")
	private final   void setLastToTime(java.sql.Timestamp val) {
		lastToTime = val;
	}

	/*Radix::System::EventLog:Period:Model:TIME_RANGE_PERIOD-Dynamic Property*/



	protected static Int TIME_RANGE_PERIOD=(Int)org.radixware.kernel.common.defs.value.ValAsStr.fromStr("5",org.radixware.kernel.common.enums.EValType.INT);











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:TIME_RANGE_PERIOD")
	private static final  Int getTIME_RANGE_PERIOD() {
		return TIME_RANGE_PERIOD;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:TIME_RANGE_PERIOD")
	private static final   void setTIME_RANGE_PERIOD(Int val) {
		TIME_RANGE_PERIOD = val;
	}

	/*Radix::System::EventLog:Period:Model:eventCodes-Presentation Property*/




	public class EventCodes extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public EventCodes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}

		/*Radix::System::EventLog:Period:Model:eventCodes:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::EventLog:Period:Model:eventCodes:Nested classes-Nested Classes*/

		/*Radix::System::EventLog:Period:Model:eventCodes:Properties-Properties*/

		/*Radix::System::EventLog:Period:Model:eventCodes:Methods-Methods*/

		/*Radix::System::EventLog:Period:Model:eventCodes:execPropEditorDialog-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:eventCodes:execPropEditorDialog")
		public published  org.radixware.kernel.common.client.views.IDialog.DialogResult execPropEditorDialog () {
			return arrEventCodes.execPropEditorDialog();
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:eventCodes")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:eventCodes")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public EventCodes getEventCodes(){return (EventCodes)getProperty(prm6FBEUOOMAFDVLN3J3TI737U5Q4);}

	/*Radix::System::EventLog:Period:Model:arrEventCodes-Presentation Property*/




	public class ArrEventCodes extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public ArrEventCodes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:arrEventCodes")
		public published  org.radixware.kernel.common.types.ArrStr getValue() {

			if (eventCodes.getValue()==null){
			    return null;
			}else{
			    return  (ArrStr) Types::ValAsStr.fromStr(eventCodes.getValue(), Meta::ValType:ArrStr);
			}
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:arrEventCodes")
		public published   void setValue(org.radixware.kernel.common.types.ArrStr val) {

			if (val==null){
			    eventCodes.setValue(null);
			}else{
			    eventCodes.setValue(Types::ValAsStr.toStr(val, Meta::ValType:ArrStr));
			}
		}
	}
	public ArrEventCodes getArrEventCodes(){return (ArrEventCodes)getProperty(prdYMBKBFKPNBBZXAVJ2AF5TS3OEA);}






























	/*Radix::System::EventLog:Period:Model:Methods-Methods*/

	/*Radix::System::EventLog:Period:Model:afterApply-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:afterApply")
	public published  void afterApply () {
		super.afterApply();
		if (TIME_RANGE_PERIOD.equals(period.getValue())){
		    lastFromTime = fromTime.getValue();
		    lastToTime = toTime.getValue();
		}
	}

	/*Radix::System::EventLog:Period:Model:updateTimeRange-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:updateTimeRange")
	private final  void updateTimeRange () {
		if (TIME_RANGE_PERIOD.equals(period.getValue())){
		    if (lastFromTime!=null){
		        fromTime.setValue(lastFromTime);
		    }
		    if (lastToTime!=null){
		        toTime.setValue(lastToTime);
		    }
		    fromTime.getEditMask().setNoValueStr("<unlimited>");
		    toTime.getEditMask().setNoValueStr("<end of day>");
		    fromTime.setReadonly(false);
		    toTime.setReadonly(false);
		}else{
		    final DateTime today = Utils::Timing.truncTimeToDay(getEnvironment().getCurrentServerTime());
		    final DateTime toTime = new DateTime(Utils::Timing.addDay(today,1).getTime() - 1000l);    
		    final int period = period.getValue().intValue();
		    final DateTime fromTime;   
		    switch(period){
		        case 1:{
		            fromTime = today;
		            break;
		        }case 2:{
		            fromTime = Utils::Timing.addDay(today,-1);
		            break;
		        }case 3:{
		            fromTime = Utils::Timing.addDay(today,-7);
		            break;
		        }case 4:{
		            fromTime = Utils::Timing.addMonth(today,-1);
		            break;
		        }default:{
		            fromTime = null;
		        }
		    }
		    final Str fromTimeStr = "<"+fromTime.getEditMask().toStr(getEnvironment(), fromTime)+">";
		    final Str toTimeStr = "<"+toTime.getEditMask().toStr(getEnvironment(), toTime)+">";
		    fromTime.setReadonly(true);
		    toTime.setReadonly(true);    
		    fromTime.setValue(null);
		    toTime.setValue(null);
		    fromTime.getEditMask().setNoValueStr(fromTimeStr);
		    toTime.getEditMask().setNoValueStr(toTimeStr);
		}
	}

	/*Radix::System::EventLog:Period:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:beforeOpenView")
	protected  void beforeOpenView () {
		super.beforeOpenView();
		updateTimeRange();
	}

	/*Radix::System::EventLog:Period:Model:getDisplayString-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:getDisplayString")
	public published  Str getDisplayString (org.radixware.kernel.common.types.Id propertyId, java.lang.Object propertyValue, Str defaultDisplayString, boolean isInherited) {
		if (propertyId==idof[EventLog:Period:eventCodes]){
		    final ArrStr arr = arrEventCodes.getValue();
		    if (arr != null) {
		        java.lang.StringBuilder result = new StringBuilder();
		        boolean first = true;
		        for (Str code : arr) {
		            String text = "";
		            try {
		                text = getEnvironment().getDefManager().getEventTitleByCode(code);
		            } catch (Throwable ex) {
		                text = code;
		            }
		            if (!first) {
		                result.append(", ");
		            } else {
		                first = false;
		            }
		            result.append(text);
		        }
		        return result.toString();
		    }    
		}
		return super.getDisplayString(propertyId, propertyValue, defaultDisplayString, isInherited);
	}

	/*Radix::System::EventLog:Period:source:source-Presentation Property*/




	public class Source extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Source(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:source:source")
		public  org.radixware.ads.Arte.common.EventSource getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:source:source")
		public   void setValue(org.radixware.ads.Arte.common.EventSource val) {
			Value = val;
		}
	}
	public Source getSource(){return (Source)getProperty(prm6AHGQBB5HFBEVNUUP6AZ435PRU);}

	/*Radix::System::EventLog:Period:minSeverity:minSeverity-Presentation Property*/




	public class MinSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MinSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:minSeverity:minSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:minSeverity:minSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MinSeverity getMinSeverity(){return (MinSeverity)getProperty(prmAIGN2LJXGVAHRPELFC4H6UAMKY);}

	/*Radix::System::EventLog:Period:maxSeverity:maxSeverity-Presentation Property*/




	public class MaxSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:maxSeverity:maxSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:maxSeverity:maxSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MaxSeverity getMaxSeverity(){return (MaxSeverity)getProperty(prmTR6BP77HZRADZJKBAN6JVLZYB4);}

	/*Radix::System::EventLog:Period:user:user-Presentation Property*/




	public class User extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public User(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.filters.RadFilterParamDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.User.User_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.explorer.User.User_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.User.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.explorer.User.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:user:user")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:user:user")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public User getUser(){return (User)getProperty(prmQSMXFK73ZZHKNLPYAIYGS56DXQ);}

	/*Radix::System::EventLog:Period:station:station-Presentation Property*/




	public class Station extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Station(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.filters.RadFilterParamDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.Station.Station_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.explorer.Station.Station_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.Station.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.explorer.Station.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:station:station")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:station:station")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Station getStation(){return (Station)getProperty(prm4A2P5FE5AZGFTMSTY7E5CPD7I4);}

	/*Radix::System::EventLog:Period:includedText:includedText-Presentation Property*/




	public class IncludedText extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public IncludedText(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:includedText:includedText")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:includedText:includedText")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public IncludedText getIncludedText(){return (IncludedText)getProperty(prmVND4W5T4GJCDVDMMEM7ZFG7SZU);}

	/*Radix::System::EventLog:Period:excludedText:excludedText-Presentation Property*/




	public class ExcludedText extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ExcludedText(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:excludedText:excludedText")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:excludedText:excludedText")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExcludedText getExcludedText(){return (ExcludedText)getProperty(prmI5PWPY5VHNAPXLI3J2TIDTRKOI);}


}

/* Radix::System::EventLog:Period:Model - Desktop Meta*/

/*Radix::System::EventLog:Period:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Period:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcTYT3ALDZTNAYDDFDPTL7AFYGUU"),
						"Period:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:Period:Model:Properties-Properties*/
						new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

								/*Radix::System::EventLog:Period:Model:arrEventCodes:arrEventCodes:PropertyPresentation-Property Presentation*/
								new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYMBKBFKPNBBZXAVJ2AF5TS3OEA"),
									"arrEventCodes",
									null,
									null,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
									org.radixware.kernel.common.enums.EValType.ARR_STR,
									org.radixware.kernel.common.enums.EPropNature.VIRTUAL,
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
									org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeXGPX2QQOIVANBK6FXI2GY4U4D4"),
									false,

									/*Radix::System::EventLog:Period:Model:arrEventCodes:arrEventCodes:PropertyPresentation:Edit Options:-Edit Mask Str*/
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
						null,
						false,
						false,
						false
						);
}

/* Radix::System::EventLog:Period:Model - Web Executable*/

/*Radix::System::EventLog:Period:Model-Filter Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model")
public class Period:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Period:Model_mi.rdxMeta; }



	public Period:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::EventLog:Period:Model:Nested classes-Nested Classes*/

	/*Radix::System::EventLog:Period:Model:Properties-Properties*/

	/*Radix::System::EventLog:Period:Model:period-Presentation Property*/




	public class Period extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Period(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:period")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:period")
		public   void setValue(Int val) {

			internal[period] = val;
			updateTimeRange();
		}
	}
	public Period getPeriod(){return (Period)getProperty(prmDVPXQQQ72VHPTELUBU5GXJALB4);}

	/*Radix::System::EventLog:Period:Model:fromTime-Presentation Property*/




	public class FromTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public FromTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:fromTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:fromTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public FromTime getFromTime(){return (FromTime)getProperty(prmNKTTHR6EAJB6FAJGHIDNIW3PHI);}

	/*Radix::System::EventLog:Period:Model:toTime-Presentation Property*/




	public class ToTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public ToTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:toTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:toTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public ToTime getToTime(){return (ToTime)getProperty(prmXOAKGU7PA5CSFDQACAVI2U3RCM);}

	/*Radix::System::EventLog:Period:Model:lastFromTime-Dynamic Property*/



	protected java.sql.Timestamp lastFromTime=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:lastFromTime")
	private final  java.sql.Timestamp getLastFromTime() {
		return lastFromTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:lastFromTime")
	private final   void setLastFromTime(java.sql.Timestamp val) {
		lastFromTime = val;
	}

	/*Radix::System::EventLog:Period:Model:lastToTime-Dynamic Property*/



	protected java.sql.Timestamp lastToTime=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:lastToTime")
	private final  java.sql.Timestamp getLastToTime() {
		return lastToTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:lastToTime")
	private final   void setLastToTime(java.sql.Timestamp val) {
		lastToTime = val;
	}

	/*Radix::System::EventLog:Period:Model:TIME_RANGE_PERIOD-Dynamic Property*/



	protected static Int TIME_RANGE_PERIOD=(Int)org.radixware.kernel.common.defs.value.ValAsStr.fromStr("5",org.radixware.kernel.common.enums.EValType.INT);











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:TIME_RANGE_PERIOD")
	private static final  Int getTIME_RANGE_PERIOD() {
		return TIME_RANGE_PERIOD;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:TIME_RANGE_PERIOD")
	private static final   void setTIME_RANGE_PERIOD(Int val) {
		TIME_RANGE_PERIOD = val;
	}

	/*Radix::System::EventLog:Period:Model:eventCodes-Presentation Property*/




	public class EventCodes extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public EventCodes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}

		/*Radix::System::EventLog:Period:Model:eventCodes:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::EventLog:Period:Model:eventCodes:Nested classes-Nested Classes*/

		/*Radix::System::EventLog:Period:Model:eventCodes:Properties-Properties*/

		/*Radix::System::EventLog:Period:Model:eventCodes:Methods-Methods*/

		/*Radix::System::EventLog:Period:Model:eventCodes:execPropEditorDialog-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:eventCodes:execPropEditorDialog")
		public published  org.radixware.kernel.common.client.views.IDialog.DialogResult execPropEditorDialog () {
			return arrEventCodes.execPropEditorDialog();
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:eventCodes")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:eventCodes")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public EventCodes getEventCodes(){return (EventCodes)getProperty(prm6FBEUOOMAFDVLN3J3TI737U5Q4);}

	/*Radix::System::EventLog:Period:Model:arrEventCodes-Presentation Property*/




	public class ArrEventCodes extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public ArrEventCodes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:arrEventCodes")
		public published  org.radixware.kernel.common.types.ArrStr getValue() {

			if (eventCodes.getValue()==null){
			    return null;
			}else{
			    return  (ArrStr) Types::ValAsStr.fromStr(eventCodes.getValue(), Meta::ValType:ArrStr);
			}
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:arrEventCodes")
		public published   void setValue(org.radixware.kernel.common.types.ArrStr val) {

			if (val==null){
			    eventCodes.setValue(null);
			}else{
			    eventCodes.setValue(Types::ValAsStr.toStr(val, Meta::ValType:ArrStr));
			}
		}
	}
	public ArrEventCodes getArrEventCodes(){return (ArrEventCodes)getProperty(prdYMBKBFKPNBBZXAVJ2AF5TS3OEA);}






























	/*Radix::System::EventLog:Period:Model:Methods-Methods*/

	/*Radix::System::EventLog:Period:Model:afterApply-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:afterApply")
	public published  void afterApply () {
		super.afterApply();
		if (TIME_RANGE_PERIOD.equals(period.getValue())){
		    lastFromTime = fromTime.getValue();
		    lastToTime = toTime.getValue();
		}
	}

	/*Radix::System::EventLog:Period:Model:updateTimeRange-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:updateTimeRange")
	private final  void updateTimeRange () {
		if (TIME_RANGE_PERIOD.equals(period.getValue())){
		    if (lastFromTime!=null){
		        fromTime.setValue(lastFromTime);
		    }
		    if (lastToTime!=null){
		        toTime.setValue(lastToTime);
		    }
		    fromTime.getEditMask().setNoValueStr("<unlimited>");
		    toTime.getEditMask().setNoValueStr("<end of day>");
		    fromTime.setReadonly(false);
		    toTime.setReadonly(false);
		}else{
		    final DateTime today = Utils::Timing.truncTimeToDay(getEnvironment().getCurrentServerTime());
		    final DateTime toTime = new DateTime(Utils::Timing.addDay(today,1).getTime() - 1000l);    
		    final int period = period.getValue().intValue();
		    final DateTime fromTime;   
		    switch(period){
		        case 1:{
		            fromTime = today;
		            break;
		        }case 2:{
		            fromTime = Utils::Timing.addDay(today,-1);
		            break;
		        }case 3:{
		            fromTime = Utils::Timing.addDay(today,-7);
		            break;
		        }case 4:{
		            fromTime = Utils::Timing.addMonth(today,-1);
		            break;
		        }default:{
		            fromTime = null;
		        }
		    }
		    final Str fromTimeStr = "<"+fromTime.getEditMask().toStr(getEnvironment(), fromTime)+">";
		    final Str toTimeStr = "<"+toTime.getEditMask().toStr(getEnvironment(), toTime)+">";
		    fromTime.setReadonly(true);
		    toTime.setReadonly(true);    
		    fromTime.setValue(null);
		    toTime.setValue(null);
		    fromTime.getEditMask().setNoValueStr(fromTimeStr);
		    toTime.getEditMask().setNoValueStr(toTimeStr);
		}
	}

	/*Radix::System::EventLog:Period:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:beforeOpenView")
	protected  void beforeOpenView () {
		super.beforeOpenView();
		updateTimeRange();
	}

	/*Radix::System::EventLog:Period:Model:getDisplayString-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:Model:getDisplayString")
	public published  Str getDisplayString (org.radixware.kernel.common.types.Id propertyId, java.lang.Object propertyValue, Str defaultDisplayString, boolean isInherited) {
		if (propertyId==idof[EventLog:Period:eventCodes]){
		    final ArrStr arr = arrEventCodes.getValue();
		    if (arr != null) {
		        java.lang.StringBuilder result = new StringBuilder();
		        boolean first = true;
		        for (Str code : arr) {
		            String text = "";
		            try {
		                text = getEnvironment().getDefManager().getEventTitleByCode(code);
		            } catch (Throwable ex) {
		                text = code;
		            }
		            if (!first) {
		                result.append(", ");
		            } else {
		                first = false;
		            }
		            result.append(text);
		        }
		        return result.toString();
		    }    
		}
		return super.getDisplayString(propertyId, propertyValue, defaultDisplayString, isInherited);
	}

	/*Radix::System::EventLog:Period:source:source-Presentation Property*/




	public class Source extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Source(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:source:source")
		public  org.radixware.ads.Arte.common.EventSource getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:source:source")
		public   void setValue(org.radixware.ads.Arte.common.EventSource val) {
			Value = val;
		}
	}
	public Source getSource(){return (Source)getProperty(prm6AHGQBB5HFBEVNUUP6AZ435PRU);}

	/*Radix::System::EventLog:Period:minSeverity:minSeverity-Presentation Property*/




	public class MinSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MinSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:minSeverity:minSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:minSeverity:minSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MinSeverity getMinSeverity(){return (MinSeverity)getProperty(prmAIGN2LJXGVAHRPELFC4H6UAMKY);}

	/*Radix::System::EventLog:Period:maxSeverity:maxSeverity-Presentation Property*/




	public class MaxSeverity extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxSeverity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:maxSeverity:maxSeverity")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:maxSeverity:maxSeverity")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MaxSeverity getMaxSeverity(){return (MaxSeverity)getProperty(prmTR6BP77HZRADZJKBAN6JVLZYB4);}

	/*Radix::System::EventLog:Period:user:user-Presentation Property*/




	public class User extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public User(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.filters.RadFilterParamDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.web.User.User_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.web.User.User_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.web.User.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.web.User.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:user:user")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:user:user")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public User getUser(){return (User)getProperty(prmQSMXFK73ZZHKNLPYAIYGS56DXQ);}

	/*Radix::System::EventLog:Period:station:station-Presentation Property*/




	public class Station extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Station(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.filters.RadFilterParamDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.web.Station.Station_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.web.Station.Station_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.web.Station.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.web.Station.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:station:station")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:station:station")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Station getStation(){return (Station)getProperty(prm4A2P5FE5AZGFTMSTY7E5CPD7I4);}

	/*Radix::System::EventLog:Period:includedText:includedText-Presentation Property*/




	public class IncludedText extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public IncludedText(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:includedText:includedText")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:includedText:includedText")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public IncludedText getIncludedText(){return (IncludedText)getProperty(prmVND4W5T4GJCDVDMMEM7ZFG7SZU);}

	/*Radix::System::EventLog:Period:excludedText:excludedText-Presentation Property*/




	public class ExcludedText extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ExcludedText(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:excludedText:excludedText")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLog:Period:excludedText:excludedText")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExcludedText getExcludedText(){return (ExcludedText)getProperty(prmI5PWPY5VHNAPXLI3J2TIDTRKOI);}


}

/* Radix::System::EventLog:Period:Model - Web Meta*/

/*Radix::System::EventLog:Period:Model-Filter Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Period:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcTYT3ALDZTNAYDDFDPTL7AFYGUU"),
						"Period:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::EventLog:Period:Model:Properties-Properties*/
						new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

								/*Radix::System::EventLog:Period:Model:arrEventCodes:arrEventCodes:PropertyPresentation-Property Presentation*/
								new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYMBKBFKPNBBZXAVJ2AF5TS3OEA"),
									"arrEventCodes",
									null,
									null,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
									org.radixware.kernel.common.enums.EValType.ARR_STR,
									org.radixware.kernel.common.enums.EPropNature.VIRTUAL,
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
									org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeLUNCTCK525FYRBPRWUBKO7JWCY"),
									false,

									/*Radix::System::EventLog:Period:Model:arrEventCodes:arrEventCodes:PropertyPresentation:Edit Options:-Edit Mask Str*/
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
						null,
						false,
						false,
						false
						);
}

/* Radix::System::EventLog - Localizing Bundle */
package org.radixware.ads.System.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EventLog - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
		loadStrings2();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Enable Sound Notification");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Включить звуковое оповещение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2A2H7OWSAFC5DJI6D3EUABAL5E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unknown attribute: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Неизвестный атрибут: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2BNIHHSGURCCVAJGHY7KPXLN5A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Disable Auto Update");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отключить автообновление");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2ZSIMG5GABHJRAF5LTF53RTVIA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Minimum severity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Минимальная серьезность");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls34JM3AK2FRBNRFILS4YZW4Y7OA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Disable Sound Notification");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отключить звуковое оповещение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3F333RGQ7JDX3AS7ZJH2AKYXII"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По времени");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3M5UBSD64DORDOFEABIFNQAABA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Sensitive data");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Чувствительные данные");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3Z5AM6T3I5G37DJZPWGBKA4RO4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время записи");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls45VFSS24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Auto Update");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Автообновление");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4BEMFLG4G5BPNIEXSGSDUBPYK4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Station");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Станция");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4FDJ7ECZA5F3FDV6DCR6DUSCQM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event Log");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Журнал событий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4NVFSS24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Type");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4RVFSS24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Comment");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Комментарий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4VVFSS24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Severity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Серьезность");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4ZVFSS24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Contexts");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Контексты");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls55VFSS24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Select File to Save Codes");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выберите файл для сохранения справочника");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5B3OWQWMTJBZXDBH6L6QHXIX74"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5FVFSS24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Source");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Источник");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5JVFSS24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Receiptor");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Квитировщик");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5RVFSS24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Если установлено, данное сообщение было залоггировано с пометкой \"чувствительные данные\".\nВ обычном режиме работы такие сообщения не логгируются");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5SQFCWEMTBFBXEQ5SZVJQRQIEA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5ZVFSS24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last two days (today and yesterday)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Последние два дня (сегодня и вчера)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls66CEJDVR6RDN7OD6HQ4TVVF3JM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By time (earliest first)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По времени (сначала более ранние)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6FQGPWH6UBH3TG4MUOPHDCVGHE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Today");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"За сегодня");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6FVFSS24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"CSV files (*.csv)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Файлы CSV (*.csv)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6Y65FAO5GFHVNB7VBSRCW5YO4Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<end of day>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<конец дня>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls746RSR5RGNCGFPR4QPS4SVALPM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<beginning of the day>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<начало дня>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7GDC3KEO2ZCLZM7BCJCZNDBEAY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<%s characters omitted...>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<%s символов опущено...>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7ZIBRIFBIVF53JV6VWIBXIHQGQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"From");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"От");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAAWKAIWIT5H7DHTLZZVKYGROFY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<end of day>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<конец дня>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsACCVK3XLOFADBJ4ZO7MUFL7V7A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Событие");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsACX7FP5GUHOBDCLBAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Enable Sound Notification");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Включить звуковое оповещение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsADFRNG6SRFE5JAC3T5XHXV53MU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<end of day>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<конец дня>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAQVR57CHU5C5TLVCA5NUOHPN5I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maximum severity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Максимальная серьезность");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBCYDW4725NCC7JJAE5Z2DQ7F6U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<end of day>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<конец дня>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBGC5CJNZM5G2XITJKXEWJPQPTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любая>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBGJFJXPVMNHJTNWYWYRTJKW5T4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любая>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBHTXCAVAXBGLTFKEO6FT6IFAZ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"For period and ...");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"За период и ...");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBOEVCBAVXVBKXKBYE7W2KPQ6OU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By station event");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"События станции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBR3XPTC47NG5XIA4KE3TK2SBUQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любая>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBSVWTN5RSVE27G6ZEFVAIUUPLM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"From");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"От");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC4QADU6X4NBBZFY2MBFJPMFSBM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<beginning of the day>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<начало дня>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCFZ2WULHNNFVLAFZMMMMHC6QYY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<system>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<система>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCHCC4PFU5FHE7CCE7VVTL7G57I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователь");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCIACSRHOFJG4JOQRKR4LNECNPU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"To");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"До");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCYMVUJJQGJC5TICEGRTTHPP5IA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любой>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDCWXD43GEBD5LDQOHP7GA5W6WI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любой>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEGP2NUKPMZFYVGE5LMYY6ZBJIM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сообщение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEIXKAYBC3BF6XC36DMJ5V4VETU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unknown element: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Неизвестный элемент: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFPVRNWJIVBDBHPORLBWMQUUM4A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event codes");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Коды событий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGBPBXS7FXZAHBENS3IQRJZWBNQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Disable Auto Update");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отключить автообновление");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGLGSLX6HPFBZ3CRU5EPDW4LLNA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<beginning of the day>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<начало дня>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGXNMWXVV45HLXOCKYE4ZXW4EDU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любой>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH6FAIQSDZZBFVFVPFU5KJGY2PE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import Event Log");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импортировать журнал событий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHAAW2WV5SRDMFLDQQ6EWLXWSYA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"To");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"До");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHCBHLHP3O5FQFBDYQU6TLXGO7Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сообщение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHEN6MQ3MOXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"To");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"До");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHF3NOFXHRZDHPD2I2B5PJO76OQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Minimum severity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Минимальная серьезность");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHL25FQZVOJFMBLCWQ3TVVZE2DE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Source");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Источник");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsINADGYD5DZDXDA56UJSGRVPXBU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Minimum severity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Минимальная серьезность");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKB3KWU57LBBQ7G577WSZGHUOV4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"From");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"От");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKEPHPK6KTRCWFN2Y5A2X3QBY6M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Minimum severity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Минимальная серьезность");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKKFH32WXAVAHHHZR3CJL7GWCUU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Source");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Источник");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLEUEUWJICBHHPCJHWCSS65ABKY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last month");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Последний месяц");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLKLGWZDJEFBRNE2SHBJV26FFII"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By user event");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"События пользователя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLPCH3N4RVRBLPNFAXSJUS2FWSQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<beginning of the day>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<начало дня>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLYQO5D6XXZBVPDWPNCNPW2PEMQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Imported Events");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импортированные события");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM4VUG6TZWPOBDCL2AALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Display Settings");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Настройки отображения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMWSDNMXV6NAV3PR7ZSUZP2JXEA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By time range");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Диапазон времени");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMZWO3WOGY5CG5PWS4TRQLWDS7Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Minimum severity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Минимальная серьезность");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN26A25KERVHDTI3PW736DBBTKA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"To");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"До");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN4LG75A4JZE7ZJF3XBB6DXH2RQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователь");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNWO3EX76XRASXAGH2LXY2XDJ3I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Imported from file");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Загруженные из файла");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNXWOLHNIBRARBFF4T3TDE3HZFE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любая>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO22MN6SUUBHRLNZIHQ7DFVFCV4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<unlimited>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не ограничено>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOG5FEPSR5ZG57I3UMBK3SKIO2Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By severity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По серьезности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOKNP5QK2SRHJ3K4C4L2PO3JS5Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Minimum severity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Минимальная серьезность");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP3667UYCKBAKHNSBPGATFZ6DLU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Sound Device Problem");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проблема со звуковым устройством");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPEW34GDCINFVDHTDCJHGZKQBFQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Today");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сегодня");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPJP3PHRTGNEDBBR6QRS2U4RKRM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"From");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"С");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPW6B77D23JAYRCVCHMFW6CFL24"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Sound device problem found. Notifications can\'t be enabled.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обнаружены проблемы со звуковым устройством. Оповещения не могут быть включены.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ6CCCRUHY5HLBO7BHOXUB6G3RI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Disable Sound Notification");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отключить звуковое оповещение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQV7GGDHFVFHKBJIBMO3MPPUYUM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"From");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"От");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQWFCYFBDPJBAVHU2XQPGECKHJQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любая>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQZAKPB2ZWZF77ABOO5EQ52ZEOY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Original severity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Исходный уровень серьезности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRUPQP3M7H5BPPPDBFZ6HPJGAZI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"To");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"До");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS2JAUBC535A3LC2VYCSLEG3CUY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last week");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Последняя неделя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS65VEIXKPRA43HEXE6JL2GC5YE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Source");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Источник");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSO4TKWSEGREVZJRMOSBWA2AOGE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Period");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Период");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT6OLEABUYZEAZF3WSVRKVK5I7E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event codes");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Коды событий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTHHNLUH6HZAGVIABUSFMVQ5WOQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Minimum severity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Минимальная серьезность");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTPFSQXY3ARHHPJ7ZP63W3NOHYU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"File name as");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя файла как");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUIJ2UTOOZJF2DAFIJX55A7ZR2E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By event code");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По кодам событий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUKQQ4VHYWFB2NGCNUXJYM6WG5M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<end of day>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<конец дня>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUPTOTRWVP5BZ7FOVD4UN6LTX4M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Station");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Станция");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUR4DB7VH5ZBYRJDDPOODD4WJCI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<unlimited>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не ограничено>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUV4WWUC7YZHSXNOJNPS3J7TIWM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любая>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUV52DU3SRNDEBIJTM3BA2627CA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любые>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVJRANVOK25BXVBXSXFEZSDCAQE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Time range");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Диапазон времени");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVXH3CLQNQ5CNXENVVSJ3MC4OQU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любая>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWGZVU3F4ZJFK5G42SSYWCYGILI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Enable Auto Update");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Включить автообновление");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWOFIH3QQ3JGCHATCM7NULFEJIY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	@SuppressWarnings("unused")
	private static void loadStrings2(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любая>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWQE2U26P4FBV7DKELXK2SD52MU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<end of day>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<конец дня>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWZH7TOE7FNHOPKKVPJZMXFRVOA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"From");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"От");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX3UKUR4JQJCKPFQ5Q3GXSHPB5Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<unlimited>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не ограничено>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXSUFLJZNJBGXNC7KZ7XCSFTABQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"XML File (*.xml);;All files (*)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"XML файл (*.xml);;Все файлы (*)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY7Y4YGIOQFGY5NU5QLTM7VXMGM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Enable Auto Update");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Включить автообновление");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYIBE3Z7MARDAVOGNOSH7LJXOMM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<end of day>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<конец дня>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYVJBU4HKIFCJTIZOBSTDII3P6I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Can\'t set update resume time of EventLog widget less than ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Нельзя установить период возобновления процесса обновления EventLog менее ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZCOI6FTQ6JBOXKFXZ7M4G5ZQ5Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unknown attribute: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Неизвестный атрибут: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZH3R633UXVHRVBBNB2KM4YV6AI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZHDKW4MQ2BG6XBW5IDXSX4N22I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Text excluded");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Исключаемый текст");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZIU2N36B3ZDTJPKD5CS4STD7HY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maximum severity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Максимальная серьезность");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZP6424GWQNA6JIVGIDNLP6R45U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"To");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"До");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZTA2SKREZJGLPKWI4ZYSNTLPMM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Text included");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Включаемый текст");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZW7SV6PBORBPRKVL6GYRYMDZ44"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любая>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZYHZPE7AZNH4FJYACQKESSTGOA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любая>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZYO4N235ZZBXPAWMRGPDDDBSGA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(EventLog - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecACQVZOMVVHWDBROXAAIT4AGD7E"),"EventLog - Localizing Bundle",$$$items$$$);
}
