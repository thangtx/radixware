
/* Radix::System::EventLogGroup - Server Executable*/

/*Radix::System::EventLogGroup-Entity Group Class*/

package org.radixware.ads.System.server;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import org.radixware.schemas.eas.Trace;
import org.radixware.schemas.eas.TraceLevelEnum;
import java.util.*;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogGroup")
public published class EventLogGroup  extends org.radixware.ads.Types.server.EntityGroup<org.radixware.ads.System.server.EventLog>  implements org.radixware.ads.System.server.IEventLogExportInterface,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private org.radixware.kernel.server.dbq.QueryBuilderDelegate qbDelegate = null;

	public org.radixware.kernel.server.dbq.QueryBuilderDelegate getQueryBuilderDelegate() {
	    if (qbDelegate == null) {
	//a hack that modifies "order by" section of select from EventLog
	        //in case it is joined with EventContext to 
	        //replace EventLog.id and EventLog.raiseTime with EventContext.eventId and EventContext.raiseTime
	        //so Oracle can build more optimal execution plan
	        qbDelegate = new org.radixware.kernel.server.dbq.QueryBuilderDelegate() {
	            private final String ctxtRaiseTimeAlias = "ctxt__RaiseTime";
	            private final String ctxtEventIdAlias = "ctxt__EventId";
	            private final String ctxtAlias = "ctxt";

	            @Override
	            public boolean appendOrdColDirective(final StringBuilder query,
	                    final org.radixware.kernel.server.dbq.SqlBuilder builder,
	                    final Types::Id propId,
	                    final boolean bForSelectParent,
	                    final org.radixware.kernel.common.enums.EOrder order,
	                    final org.radixware.kernel.server.dbq.GroupQuerySqlBuilder.ESortingColumnPurpose purpose) {
	                final StringBuilder toAdd = new StringBuilder();
	                try {
	                    if (forContext()) {
	                        if (idof[EventLog:raiseTime].equals(propId)) {
	                            toAdd.append(ctxtRaiseTimeAlias);
	                            if (purpose == org.radixware.kernel.server.dbq.GroupQuerySqlBuilder.ESortingColumnPurpose.ORDER_CLAUSE) {
	                                if (order == org.radixware.kernel.common.enums.EOrder.DESC) {
	                                    toAdd.append(" desc");
	                                }
	                            } else if (purpose == org.radixware.kernel.server.dbq.GroupQuerySqlBuilder.ESortingColumnPurpose.RELATIVE_CONDITION_COMPARISON) {
	                                toAdd.append(order == org.radixware.kernel.common.enums.EOrder.DESC ? " <= " : " >= ");
	                                toAdd.append("?");
	                                builder.addParameter(new org.radixware.kernel.server.dbq.SelectQuery.InputPrevObjectPropParam(propId));
	                            } else if (purpose == org.radixware.kernel.server.dbq.GroupQuerySqlBuilder.ESortingColumnPurpose.RELATIVE_CONDITION_EQUALITY) {
	                                toAdd.append(" = ?");
	                                builder.addParameter(new org.radixware.kernel.server.dbq.SelectQuery.InputPrevObjectPropParam(propId));
	                            }
	                        } else if (idof[EventLog:id].equals(propId)) {
	                            toAdd.append(ctxtEventIdAlias);
	                            if (purpose == org.radixware.kernel.server.dbq.GroupQuerySqlBuilder.ESortingColumnPurpose.ORDER_CLAUSE) {
	                                if (order == org.radixware.kernel.common.enums.EOrder.DESC) {
	                                    toAdd.append(" desc");
	                                }
	                            } else if (purpose == org.radixware.kernel.server.dbq.GroupQuerySqlBuilder.ESortingColumnPurpose.RELATIVE_CONDITION_COMPARISON) {
	                                toAdd.append(order == org.radixware.kernel.common.enums.EOrder.DESC ? " <= " : " >= ");
	                                toAdd.append("?");
	                                builder.addParameter(new org.radixware.kernel.server.dbq.SelectQuery.InputPrevObjectPropParam(propId));
	                            } else if (purpose == org.radixware.kernel.server.dbq.GroupQuerySqlBuilder.ESortingColumnPurpose.RELATIVE_CONDITION_EQUALITY) {
	                                toAdd.append(" = ?");
	                                builder.addParameter(new org.radixware.kernel.server.dbq.SelectQuery.InputPrevObjectPropParam(propId));
	                            }
	                        }
	                        if (toAdd.length() > 0) {
	                            query.append(toAdd);
	                            return true;
	                        }
	                    }
	                } catch (Exceptions::RuntimeException ex) {
	                    //ignore, return false;
	                }

	                return false;
	            }

	            @Override
	            public boolean appendConditionFrom(final StringBuilder query, final org.radixware.kernel.server.dbq.SqlBuilder builder, final org.radixware.kernel.server.meta.presentations.RadConditionDef condition) {
	                try {
	                    if (forContext()) {

	                        final boolean desc = getOrderBy() != null && !getOrderBy().isEmpty() && getOrderBy().get(0).Order != org.radixware.kernel.common.enums.EOrder.ASC;
	                        final String hint = " /*+ first_rows(51) index" + (desc ? "_desc" : "") + "(" + ctxtAlias + " " + dbName[Context] + ") */ ";
	                        query.append(", (select " + hint + ctxtAlias + "." + dbName[raiseTime] + " " + ctxtRaiseTimeAlias
	                                + ", " + ctxtAlias + "." + dbName[eventId] + " " + ctxtEventIdAlias + " from " + dbName[Radix::System::EventContext] + " " + ctxtAlias
	                                + " where " + ctxtAlias + "." + dbName[type] + "= ? and " + ctxtAlias + "." + dbName[id] + " = ?)");
	                        builder.addParameter(new org.radixware.kernel.server.dbq.DbQuery.InputGroupPropParam(idof[EventLogGroup:contextType]));
	                        builder.addParameter(new org.radixware.kernel.server.dbq.DbQuery.InputGroupPropParam(idof[EventLogGroup:contextId]));
	                        return true;
	                    }
	                } catch (Exceptions::RuntimeException ex) {
	                    //ignore, return false;
	                }
	                return false;
	            }

	            private boolean forContext() {
	                return getPresentation().getName().contains("ForContextLog");

	            }
	        };
	    }
	    return qbDelegate;
	}

	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return EventLogGroup_mi.rdxMeta;}

	public EventLogGroup(boolean isContextWrapper){super(isContextWrapper);}
	/*Radix::System::EventLogGroup:Nested classes-Nested Classes*/

	/*Radix::System::EventLogGroup:Properties-Properties*/

	/*Radix::System::EventLogGroup:contextId-Entity Group Parameter*/



	protected Str contextId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogGroup:contextId")
	public published  Str getContextId() {
		return contextId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogGroup:contextId")
	public published   void setContextId(Str val) {
		contextId = val;
	}

	/*Radix::System::EventLogGroup:contextType-Entity Group Parameter*/



	protected Str contextType=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogGroup:contextType")
	public published  Str getContextType() {
		return contextType;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogGroup:contextType")
	public published   void setContextType(Str val) {
		contextType = val;
	}









































	/*Radix::System::EventLogGroup:Methods-Methods*/

	/*Radix::System::EventLogGroup:onCommand_ImportEvents-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogGroup:onCommand_ImportEvents")
	private final  void onCommand_ImportEvents (org.radixware.schemas.systemcommands.EventsImportRqDocument input) {
		Int cachingSessionId = null;
		Client.Resources::FileInResource fileIn = null;
		XMLStreamReader in = null;
		Client.Resources::ProgressDialogProcessResource process = null;

		try {
		    Client.Resources::ProgressDialogResource dlg
		            = new ProgressDialogResource(getArte(), "Import Events", true);
		    process = dlg.startNewProcess("Import Events", true, false);

		    final Str fileName = input.EventsImportRq.File;
		    String ext = org.apache.commons.io.FilenameUtils.getExtension(fileName);
		    if (!"xml".equals(ext.toLowerCase())) {
		        throw new AppError("Imported file has wrong extension. Expected: \".xml\"'; Received: \"." + ext + "\"");
		    }
		    
		    fileIn = new FileInResource(//extends InputStream
		            getArte(), fileName, Client.Resources::FileOpenShareMode:Read);

		    final List<EventLog> cache = new ArrayList<EventLog>();
		    final Map<EventLog, List<EventContext>> eventContextCache = new HashMap<EventLog, List<EventContext>>();

		    java.io.Reader filteredIn = new java.io.FilterReader(new java.io.InputStreamReader(fileIn, "UTF-8")) {
		        public int read() throws java.io.IOException {
		            char next = (char) in.read();
		            if (next >= 0 && org.radixware.kernel.common.utils.XmlUtils.isBadXmlChar(next)) {
		                next = '?';
		            }
		            return next;
		        }

		        public int read(char cbuf[], int off, int len) throws java.io.IOException {
		            int size = in.read(cbuf, off, len);
		            for (int i = 0; i < size; i++) {
		                if (org.radixware.kernel.common.utils.XmlUtils.isBadXmlChar(cbuf[i])) {
		                    cbuf[i] = '?';
		                }
		            }
		            return size;
		        }
		    };

		    int cacheSize = 10;
		    in = XMLInputFactory.newInstance().createXMLStreamReader(filteredIn);
		    int i = 0;

		    boolean isFirst = true;
		    final String ELEMENTS_NS = "http://schemas.radixware.org/systemcommands.xsd";

		    TimeZone tz = null;
		    EventLog prevEvent = null;
		    while (in.hasNext()) {
		        int xmlEvent = in.next();
		        if (xmlEvent == javax.xml.stream.XMLStreamConstants.START_ELEMENT) {
		            String name = in.getLocalName();
		            if (isFirst && (!ELEMENTS_NS.equals(in.getNamespaceURI()) || !"EventList".equals(name))) {
		                throw new AppError("Imported file is not a list of events");
		            }
		            
		            if (name == "Event") {
		                if (cachingSessionId == null)
		                    cachingSessionId = Arte::Arte.enterCachingSession();
		                i++;
		                if (i % 100 == 0) {
		                    if (process != null)
		                        if (!processSetTitle(process, "Imported events count: " + i))
		                            return;
		                }

		                final List<EventContext> ecl = new ArrayList<EventContext>();
		                final EventLog event = EventLog.import(process, in, fileName, ecl, tz);

		                if (cacheSize-- <= 0) {
		                    if (event.raiseTime.compareTo(prevEvent.raiseTime) != 0) {
		                        // flush buffer
		                        flushBuffer(cache, eventContextCache);
		                        cacheSize = 10;
		                    }
		                }

		                cache.add(event);
		                eventContextCache.put(event, ecl);
		                prevEvent = event;
		            } else if (name == "EventList") {
		                for (int a = 0; a < in.getAttributeCount(); a++) {
		                    if ("TimeZone".equals(in.getAttributeLocalName(a))) {
		                        try {
		                            final String tzId = in.getAttributeValue(a);
		                            final TimeZone timeZone = TimeZone.getTimeZone(tzId);
		                            if (timeZone == null) {
		                                throw new IllegalArgumentException("Unknown timezone: '" + tzId + "'");
		                            }
		                            if (!TimeZone.getDefault().getID().equals(timeZone.getID())) {

		                                List<Client.Resources::DialogButtonType> buttons = new ArrayList<Client.Resources::DialogButtonType>();
		                                buttons.add(Client.Resources::DialogButtonType:Yes);
		                                buttons.add(Client.Resources::DialogButtonType:No);
		                                buttons.add(Client.Resources::DialogButtonType:Cancel);

		                                Client.Resources::DialogButtonType result = Client.Resources::MessageDialogResource.show(
		                                        Arte::Arte.getInstance(),
		                                        Client.Resources::DialogType:Confirmation,
		                                        "Convert time",
		                                        buttons,
		                                        String.format("Timezone of imported events (%s) differs...", timeZone.getID(), TimeZone.getDefault().getID()));
		                                if (result == Client.Resources::DialogButtonType:Cancel) {
		                                    return;
		                                }
		                                if (result == Client.Resources::DialogButtonType:Yes) {
		                                    tz = null;//by default original time is imported and will be shown in server timezone
		                                } else {
		                                    tz = timeZone;//we want to see events like they were seen in specified timezone
		                                }

		                            }
		                        } catch (Exceptions::RuntimeException ex) {
		                            Client.Resources::MessageDialogResource.warning(Arte::Arte.getInstance(), "Unable to parse the time zone information", String.format("Error parsing timezone info: %s. Events time will be considered as in current timezone.", ex.getMessage()));
		                        }
		                    }
		                }
		            } else {
		                processTraceError(process, "Unknown element: " + name);
		            }
		            isFirst = false;
		        }
		    }
		    flushBuffer(cache, eventContextCache);

		    try {
		        Client.Resources::MessageDialogResource.information(getArte(), "Import Events", String.format("Events imported: %d", i));
		    } catch (Exception e) {
		    }
		} catch (Exception e) {
		    throw new AppError("Import error: " + e.getMessage(), e);
		} finally {
		    if (cachingSessionId != null)
		        Arte::Arte.leaveCachingSession(cachingSessionId);

		    if (fileIn != null)
		        try {
		            fileIn.close();
		        } catch (Exception e) {
		        }

		    if (process != null)
		        try {
		            process.close();
		        } catch (Exception e) {
		            process = null;
		        }
		}
	}

	/*Radix::System::EventLogGroup:export-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogGroup:export")
	  void export (Str fileName, Int maxCount) {
		try {
		    EventLogExporter.export(fileName, maxCount, iterator(), this);
		} catch(Exceptions::XmlStreamException e) {
		    throw new AppError(e.getMessage());
		} catch(Exceptions::ResourceUsageException e) {
		    throw new AppError(e.getMessage());
		} catch(Exceptions::ResourceUsageTimeout e) {
		    throw new AppError(e.getMessage());
		} catch(Exceptions::InterruptedException e) {
		    throw new AppError(e.getMessage());
		} catch(Exceptions::SQLException e) {
		    throw new AppError(e.getMessage());
		} catch(Exceptions::UnsupportedEncodingException e) {
		    throw new AppError(e.getMessage());
		}
	}

	/*Radix::System::EventLogGroup:getNextEvent-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogGroup:getNextEvent")
	public published  org.radixware.ads.System.server.EventLog getNextEvent (java.util.Iterator<? extends org.radixware.kernel.server.types.Entity> iterator) {
		if (iterator.hasNext())
		    return (EventLog)iterator.next();
		return null;
	}

	/*Radix::System::EventLogGroup:processTraceError-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogGroup:processTraceError")
	 static  boolean processTraceError (org.radixware.kernel.server.arte.resources.ProgressDialogResource.Process process, Str mess) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ResourceUsageException,org.radixware.kernel.common.exceptions.ResourceUsageTimeout {
		if(process == null)
		    return true;

		if(process.checkIsCancelled()) {    
		    Client.Resources::MessageDialogResource.information(Arte::Arte.getInstance(), "Importing events", "Cancelled by user");    
		    return false;
		}

		Trace trace = Trace.Factory.newInstance();
		Trace.Item item = trace.addNewItem();
		item.setStringValue(mess);
		item.setLevel(TraceLevelEnum.ERROR);
		process.trace(trace);

		return true;
	}

	/*Radix::System::EventLogGroup:processSetTitle-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogGroup:processSetTitle")
	private static  boolean processSetTitle (org.radixware.kernel.server.arte.resources.ProgressDialogResource.Process process, Str title) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ResourceUsageException,org.radixware.kernel.common.exceptions.ResourceUsageTimeout {
		if(process == null)
		    return true;

		if(process.checkIsCancelled()) {    
		    Client.Resources::MessageDialogResource.information(Arte::Arte.getInstance(), "Importing events", "Cancelled by user");
		    return false;
		}

		process.set(title, null, true);


		return true;
	}

	/*Radix::System::EventLogGroup:onCommand_ClearSensitiveData-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogGroup:onCommand_ClearSensitiveData")
	private final  void onCommand_ClearSensitiveData () {
		ClearTracedSensitiveDataStmt q;
		do {
		    q = ClearTracedSensitiveDataStmt.execute(10000);
		    Arte::Arte.commit();
		} while (!Arte::Arte.needBreak() && q.updatedCount.longValue() > 0);

	}

	/*Radix::System::EventLogGroup:onCommand_ExportCodes-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogGroup:onCommand_ExportCodes")
	private final  void onCommand_ExportCodes (org.radixware.ads.Common.common.CommandsXsd.FileNameDocument input) {
		Str fileName = input.FileName.Path;
		Client.Resources::FileOutResource fileOut = null;
		java.io.PrintStream stream = null;
		try {
		    fileOut = new FileOutResource(Arte::Arte.getInstance(),
		            fileName,
		            Client.Resources::FileOpenMode:TruncateOrCreate,
		            Client.Resources::FileOpenShareMode:Write);
		    stream = new java.io.PrintStream(fileOut);

		    stream.println("code;severity;source;message");

		    final java.util.List<Str> codes = Arte::Arte.getDefManager().EventCodeList;
		    for (Str code : codes) {
		        stream.print(code);
		        Arte::EventSeverity sev = null;
		        try {
		            sev = Meta::Utils.getEventSeverityByCode(code);
		        } catch (Exceptions::NoConstItemWithSuchValueError e) {
		            Arte::Trace.error(e.getMessage(), Arte::EventSource:Arte);
		            sev = null;
		        }
		        stream.print(";");
		        if (sev != null) {
		            stream.print(sev.Value);
		        }
		        Arte::EventSource es = null;
		        try {
		            es = Meta::Utils.getEventSourceByCode(code);
		        } catch (Exceptions::NoConstItemWithSuchValueError e) {
		            es = null;
		        }
		        stream.print(";");
		        if (es != null)
		            stream.print(es.Value);
		        String title = Meta::Utils.getEventTitleByCode(code);
		        stream.print(";");
		        if (title != null) {
		            stream.print(title.replace('\n', ' '));
		        }
		        stream.println();
		    }

		} catch (Exception e) {
		    throw new AppError(e.getMessage(), e);
		} finally {
		    if (stream != null) {
		        stream.close();
		    }
		}

	}

	/*Radix::System::EventLogGroup:flushBuffer-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogGroup:flushBuffer")
	  void flushBuffer (java.util.List<org.radixware.ads.System.server.EventLog> cache, java.util.Map<org.radixware.ads.System.server.EventLog,java.util.List<org.radixware.ads.System.server.EventContext>> eventContextCache) {
		// swap ids of events with the same raise time
		final Map<DateTime, List<EventLog>> m = new HashMap<DateTime, List<EventLog>>();
		for (EventLog event : cache) {
		    List<EventLog> l = m.get(event.raiseTime);
		    if (l == null) {
		        l = new ArrayList<EventLog>();
		        m.put(event.raiseTime, l);
		    }
		    l.add(event);
		}

		for (List<EventLog> l : m.values()) {
		    final int sz = l.size();
		    if (sz > 1) {
		        for (int i=0; i<sz/2; i++) {
		            final EventLog e1 = l.get(i);
		            final EventLog e2 = l.get(sz-i-1);
		            final Int id = e1.id;
		            e1.id = e2.id;
		            e2.id = id;
		        }
		    }
		}
		    
		for (EventLog event : cache) {
		    event.create();
		    for (EventContext eventContext : eventContextCache.get(event)) {
		        eventContext.create();
		        eventContext.discard();
		    }
		    event.discard();
		}

		cache.clear();
		eventContextCache.clear();
	}





	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.apache.xmlbeans.XmlObject input, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmdFKWW4F3LYNDFJGPVIVJ5NX5A74){
			onCommand_ExportCodes((org.radixware.ads.Common.common.CommandsXsd.FileNameDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.ads.Common.common.CommandsXsd.FileNameDocument.class));return null;
		} else if(cmdId == cmdMO6BHZVDUHOBDCLBAALOMT5GDM){
			onCommand_ImportEvents((org.radixware.schemas.systemcommands.EventsImportRqDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.systemcommands.EventsImportRqDocument.class));return null;
		} else if(cmdId == cmdNJQT6UHXLJBIHB5CIIQDQ74UGI){
			onCommand_ClearSensitiveData();
			return null;
		} else 
			return super.execCommand(cmdId,input,output);
	}


}

/* Radix::System::EventLogGroup - Server Meta*/

/*Radix::System::EventLogGroup-Entity Group Class*/

package org.radixware.ads.System.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EventLogGroup_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("agcACQVZOMVVHWDBROXAAIT4AGD7E"),"EventLogGroup",null,

						org.radixware.kernel.common.enums.EClassType.ENTITY_GROUP,

						/*Radix::System::EventLogGroup:Presentations-Entity Group Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("agcACQVZOMVVHWDBROXAAIT4AGD7E"),
							/*Owner Class Name*/
							"EventLogGroup",
							/*Title Id*/
							null,
							/*Property presentations*/

							/*Radix::System::EventLogGroup:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::System::EventLogGroup:contextId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpC5NUYGXTMFDDNKAXRZLD5WCWSE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::EventLogGroup:contextType:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpEHMEUTD3OVBXLKTYWCV4ZPCHSE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::EventLogGroup:ExportEvents-Group Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQGNH5TT7TXOBDCK6AALOMT5GDM"),"ExportEvents",org.radixware.kernel.common.enums.ECommandScope.GROUP,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("agcACQVZOMVVHWDBROXAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::EventLogGroup:ImportEvents-Group Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdMO6BHZVDUHOBDCLBAALOMT5GDM"),"ImportEvents",org.radixware.kernel.common.enums.ECommandScope.GROUP,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("agcACQVZOMVVHWDBROXAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::EventLogGroup:DeleteDebugMessages-Group Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdVK2KXYIVHZFKNEVICOVHZQHIBE"),"DeleteDebugMessages",org.radixware.kernel.common.enums.ECommandScope.GROUP,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("agcACQVZOMVVHWDBROXAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::EventLogGroup:ClearSensitiveData-Group Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdNJQT6UHXLJBIHB5CIIQDQ74UGI"),"ClearSensitiveData",org.radixware.kernel.common.enums.ECommandScope.GROUP,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("agcACQVZOMVVHWDBROXAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::EventLogGroup:ExportCodes-Group Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdFKWW4F3LYNDFJGPVIVJ5NX5A74"),"ExportCodes",org.radixware.kernel.common.enums.ECommandScope.GROUP,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("agcACQVZOMVVHWDBROXAAIT4AGD7E"),false,true)
							},
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							null,
							/*Selector presentations*/
							null,
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							null,
							/*Object title format*/
							null,
							/*Class catalogs*/
							null,
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntityGroup_______________"),

						/*Radix::System::EventLogGroup:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::System::EventLogGroup:contextId-Entity Group Parameter*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpC5NUYGXTMFDDNKAXRZLD5WCWSE"),"contextId",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::EventLogGroup:contextType-Entity Group Parameter*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpEHMEUTD3OVBXLKTYWCV4ZPCHSE"),"contextType",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::System::EventLogGroup:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdMO6BHZVDUHOBDCLBAALOMT5GDM"),"onCommand_ImportEvents",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7DIOAQTN45BIHIF5DDIEUBPJRY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDCNIPS4GTZAQJJBPFAVRGIMFOU"),"export",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("fileName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3HIWGAAYCZHTHFCJ5TSUDCOVDQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("maxCount",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6G6CH5PBXFD5ZODO5FHG4U5YQE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNXJINMB2URDRBHIRUCINEMUHU4"),"getNextEvent",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("iterator",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprG6TW6RQ2ORB4NJ6FVCOUND75GE"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLF6HJDWEUTOBDCLHAALOMT5GDM"),"processTraceError",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("process",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZHONXILOQRDRRGDC2HP7CZD3T4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCX6KZYHTIJDLXMZG6LYSPXTFEY"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLPYKDY77VBFLRIFSKJGBS7KG4U"),"processSetTitle",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("process",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4KNORVSYDBFQVEK3MRBGCBM7CM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprL7JHNR5LUNCKBMJHUQCXLXYPLU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdNJQT6UHXLJBIHB5CIIQDQ74UGI"),"onCommand_ClearSensitiveData",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdFKWW4F3LYNDFJGPVIVJ5NX5A74"),"onCommand_ExportCodes",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZO3YN6GLABBJJDDOZJUR5SYEA4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGAOKJADAKNHOLGL65LM7EXMYSQ"),"flushBuffer",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("cache",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprD3OJ6M5S3NCARJITR4CLEI2XZQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventContextCache",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSIPTDFJVAFFGPJOPBZWNECXDNE"))
								},null)
						},
						null,
						null,null,null,false);
}

/* Radix::System::EventLogGroup - Desktop Executable*/

/*Radix::System::EventLogGroup-Entity Group Class*/

package org.radixware.ads.System.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogGroup")
public class EventLogGroup {











	public static org.radixware.kernel.common.client.models.items.Command createCommand(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){
		@SuppressWarnings("unused")
		org.radixware.kernel.common.types.Id commandId = def.getId();
		if(cmdFKWW4F3LYNDFJGPVIVJ5NX5A74 == commandId) return new EventLogGroup.ExportCodes(model,def);
		else if(cmdMO6BHZVDUHOBDCLBAALOMT5GDM == commandId) return new EventLogGroup.ImportEvents(model,def);
		else if(cmdNJQT6UHXLJBIHB5CIIQDQ74UGI == commandId) return new EventLogGroup.ClearSensitiveData(model,def);
		else if(cmdQGNH5TT7TXOBDCK6AALOMT5GDM == commandId) return new EventLogGroup.ExportEvents(model,def);
		else if(cmdVK2KXYIVHZFKNEVICOVHZQHIBE == commandId) return new EventLogGroup.DeleteDebugMessages(model,def);
		else return null;
	}

	public static class ExportCodes extends org.radixware.kernel.common.client.models.items.Command{
		protected ExportCodes(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.ads.Common.common.CommandsXsd.FileNameDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			processResponse(response, null);
		}

	}

	public static class ImportEvents extends org.radixware.kernel.common.client.models.items.Command{
		protected ImportEvents(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.schemas.systemcommands.EventsImportRqDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			processResponse(response, null);
		}

	}

	public static class ClearSensitiveData extends org.radixware.kernel.common.client.models.items.Command{
		protected ClearSensitiveData(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class ExportEvents extends org.radixware.kernel.common.client.models.items.Command{
		protected ExportEvents(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class DeleteDebugMessages extends org.radixware.kernel.common.client.models.items.Command{
		protected DeleteDebugMessages(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}



}

/* Radix::System::EventLogGroup - Desktop Meta*/

/*Radix::System::EventLogGroup-Entity Group Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EventLogGroup_mi{
	public static final org.radixware.kernel.common.client.meta.RadGroupHandlerDef rdxMeta = 
	/*Radix::System::EventLogGroup:Presentations-Entity Group Presentations*/
	new org.radixware.kernel.common.client.meta.RadGroupHandlerDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agcACQVZOMVVHWDBROXAAIT4AGD7E"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::EventLogGroup:ExportEvents-Group Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQGNH5TT7TXOBDCK6AALOMT5GDM"),
						"ExportEvents",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agcACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEJYVBV37TXOBDCK6AALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgYGRKUM6D5NFB7HJSZFG3KGBC6M"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("afhAW6CLCCBPVD6FCAONJI57PZU4Q"),
						org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.GROUP,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::EventLogGroup:ImportEvents-Group Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdMO6BHZVDUHOBDCLBAALOMT5GDM"),
						"ImportEvents",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agcACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4MW5D35DUHOBDCLBAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgEVEBHW62BFDDBLQVX2YPL7ZERM"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.GROUP,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::EventLogGroup:DeleteDebugMessages-Group Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdVK2KXYIVHZFKNEVICOVHZQHIBE"),
						"DeleteDebugMessages",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agcACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAVHPJXXWS5A3JCJTZWOKCJAW6U"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img52WAVQ7TWJHZBJHJXNP56OTTQI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("afh5ETPSDEWGNDHHBE7SYR4SOVRIM"),
						org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.GROUP,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::EventLogGroup:ClearSensitiveData-Group Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdNJQT6UHXLJBIHB5CIIQDQ74UGI"),
						"ClearSensitiveData",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agcACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDPLTT7N3SZALJOAPKDFPMDNN2Q"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgBETHPABPOJEGJA4OH34ARRFYPI"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.GROUP,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::EventLogGroup:ExportCodes-Group Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdFKWW4F3LYNDFJGPVIVJ5NX5A74"),
						"ExportCodes",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agcACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCPHE3ZERVBHBRG67FOTATTDE2M"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgIKGEDGLFUREJ3N7PPJE7IMEH3Y"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.GROUP,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
			},

			/*Radix::System::EventLogGroup:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::System::EventLogGroup:contextId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpC5NUYGXTMFDDNKAXRZLD5WCWSE"),
						"contextId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agcACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.GROUP_PROPERTY,
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

						/*Radix::System::EventLogGroup:contextId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::EventLogGroup:contextType:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpEHMEUTD3OVBXLKTYWCV4ZPCHSE"),
						"contextType",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agcACQVZOMVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.GROUP_PROPERTY,
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

						/*Radix::System::EventLogGroup:contextType:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			});
	}

	/* Radix::System::EventLogGroup - Web Executable*/

	/*Radix::System::EventLogGroup-Entity Group Class*/

	package org.radixware.ads.System.web;

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogGroup")
	public class EventLogGroup {











		public static org.radixware.kernel.common.client.models.items.Command createCommand(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){
			@SuppressWarnings("unused")
			org.radixware.kernel.common.types.Id commandId = def.getId();
			if(cmdFKWW4F3LYNDFJGPVIVJ5NX5A74 == commandId) return new EventLogGroup.ExportCodes(model,def);
			else if(cmdMO6BHZVDUHOBDCLBAALOMT5GDM == commandId) return new EventLogGroup.ImportEvents(model,def);
			else if(cmdNJQT6UHXLJBIHB5CIIQDQ74UGI == commandId) return new EventLogGroup.ClearSensitiveData(model,def);
			else if(cmdQGNH5TT7TXOBDCK6AALOMT5GDM == commandId) return new EventLogGroup.ExportEvents(model,def);
			else if(cmdVK2KXYIVHZFKNEVICOVHZQHIBE == commandId) return new EventLogGroup.DeleteDebugMessages(model,def);
			else return null;
		}

		public static class ExportCodes extends org.radixware.kernel.common.client.models.items.Command{
			protected ExportCodes(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
			public void send(org.radixware.ads.Common.common.CommandsXsd.FileNameDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
				org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
				processResponse(response, null);
			}

		}

		public static class ImportEvents extends org.radixware.kernel.common.client.models.items.Command{
			protected ImportEvents(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
			public void send(org.radixware.schemas.systemcommands.EventsImportRqDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
				org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
				processResponse(response, null);
			}

		}

		public static class ClearSensitiveData extends org.radixware.kernel.common.client.models.items.Command{
			protected ClearSensitiveData(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
			public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
				org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
				processResponse(response, null);
			}

		}

		public static class ExportEvents extends org.radixware.kernel.common.client.models.items.Command{
			protected ExportEvents(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

		}

		public static class DeleteDebugMessages extends org.radixware.kernel.common.client.models.items.Command{
			protected DeleteDebugMessages(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

		}



	}

	/* Radix::System::EventLogGroup - Web Meta*/

	/*Radix::System::EventLogGroup-Entity Group Class*/

	package org.radixware.ads.System.web;
	@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
	public final class EventLogGroup_mi{
		public static final org.radixware.kernel.common.client.meta.RadGroupHandlerDef rdxMeta = 
		/*Radix::System::EventLogGroup:Presentations-Entity Group Presentations*/
		new org.radixware.kernel.common.client.meta.RadGroupHandlerDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agcACQVZOMVVHWDBROXAAIT4AGD7E"),
				org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
				new org.radixware.kernel.common.client.meta.RadCommandDef[]{
						new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
						/*Radix::System::EventLogGroup:DeleteDebugMessages-Group Command*/

							org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdVK2KXYIVHZFKNEVICOVHZQHIBE"),
							"DeleteDebugMessages",
							org.radixware.kernel.common.types.Id.Factory.loadFrom("agcACQVZOMVVHWDBROXAAIT4AGD7E"),
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAVHPJXXWS5A3JCJTZWOKCJAW6U"),
							org.radixware.kernel.common.types.Id.Factory.loadFrom("img52WAVQ7TWJHZBJHJXNP56OTTQI"),
							org.radixware.kernel.common.types.Id.Factory.loadFrom("afh5ETPSDEWGNDHHBE7SYR4SOVRIM"),
							org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
							true,
							false,
							false,
							org.radixware.kernel.common.enums.ECommandScope.GROUP,
							null,
							org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
						new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
						/*Radix::System::EventLogGroup:ClearSensitiveData-Group Command*/

							org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdNJQT6UHXLJBIHB5CIIQDQ74UGI"),
							"ClearSensitiveData",
							org.radixware.kernel.common.types.Id.Factory.loadFrom("agcACQVZOMVVHWDBROXAAIT4AGD7E"),
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDPLTT7N3SZALJOAPKDFPMDNN2Q"),
							org.radixware.kernel.common.types.Id.Factory.loadFrom("imgBETHPABPOJEGJA4OH34ARRFYPI"),
							null,
							org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
							true,
							true,
							false,
							org.radixware.kernel.common.enums.ECommandScope.GROUP,
							null,
							org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
				},

				/*Radix::System::EventLogGroup:Properties-Properties*/
				new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

						/*Radix::System::EventLogGroup:contextId:PropertyPresentation-Property Presentation*/
						new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpC5NUYGXTMFDDNKAXRZLD5WCWSE"),
							"contextId",
							null,
							null,
							org.radixware.kernel.common.types.Id.Factory.loadFrom("agcACQVZOMVVHWDBROXAAIT4AGD7E"),
							org.radixware.kernel.common.enums.EValType.STR,
							org.radixware.kernel.common.enums.EPropNature.GROUP_PROPERTY,
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

							/*Radix::System::EventLogGroup:contextId:PropertyPresentation:Edit Options:-Edit Mask Str*/
							new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
							null,
							null,
							null,
							true,-1,-1,1,
							false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

						/*Radix::System::EventLogGroup:contextType:PropertyPresentation-Property Presentation*/
						new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpEHMEUTD3OVBXLKTYWCV4ZPCHSE"),
							"contextType",
							null,
							null,
							org.radixware.kernel.common.types.Id.Factory.loadFrom("agcACQVZOMVVHWDBROXAAIT4AGD7E"),
							org.radixware.kernel.common.enums.EValType.STR,
							org.radixware.kernel.common.enums.EPropNature.GROUP_PROPERTY,
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

							/*Radix::System::EventLogGroup:contextType:PropertyPresentation:Edit Options:-Edit Mask Str*/
							new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
							null,
							null,
							null,
							true,-1,-1,1,
							false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
				});
		}

		/* Radix::System::EventLogGroup - Localizing Bundle */
		package org.radixware.ads.System.common;
		@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
		public final class EventLogGroup - Localizing Bundle_mi{
			@SuppressWarnings("unused")
			private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
			static{
				loadStrings1();
			}

			@SuppressWarnings("unused")
			private static void loadStrings1(){
				java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unknown timezone: \'");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Неизвестный часовой пояс: \'");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls236GJZOHUNGCFN526VEM57U74U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import Events (add)");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импортировать события (добавить)");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4MW5D35DUHOBDCLBAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Cancelled by user");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отменено пользователем");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4RMJ75OFY5BM3NX7VSJJFOBHQI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import Events");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импорт событий");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4UAEO6RRBZB7XJ7NQ2XDKXOZJE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Imported file is not a list of events");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импортируемый файл не является списком событий");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6ABEWTVBEVCG7GKXVQZLHK7DWU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import Events");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импорт событий");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7THZ3HXB7BBOHJJ7KOORILGR3A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Importing events");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импорт событий");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7YR6DKX5ZBAI3HA4I5RNNZ72JE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Delete All Debug Messages from Event Log");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Удалить все отладочные сообщения из журнала событий");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAVHPJXXWS5A3JCJTZWOKCJAW6U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Convert time");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конвертация времени");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBHQ537S6KNB7LMHVA4J4B23ZTA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export Event Codes");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспорт справочника кодов событий");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCPHE3ZERVBHBRG67FOTATTDE2M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Delete All Sensitive Data from Event Log");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Удалить все чувствительные данные из журнала событий");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDPLTT7N3SZALJOAPKDFPMDNN2Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Events imported: %d");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Событий импортировано: %d");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEAP345YUDRCNVMPWFUAITVROQQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unknown element: ");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Неизвестный элемент: ");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEGOSEOD7SVE6LHP2VFO6D5W234"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export Events");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспортировать события");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEJYVBV37TXOBDCK6AALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Timezone of imported events (%s) differs\nfrom current timezone (%s). Do you\nwant to convert time of the events to \ncurrent timezone?");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Часовой пояс импортируемых событий  (%s)\nотличается от текущего (%s). Вы хотите\nпроизвести конвертацию времени событий\nв текущий часовой пояс?");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEOWK7Y6LGZHJJBWH5A4TCPIZVI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to parse the time zone information");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось прочитать информацию о часовом поясе");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFX6NEU2FVRDZTDP5IVKS5BTK6U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Imported events count: ");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Число импортированных событий: ");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI2AIUR64GVCRDO4DZGUVOOFM5Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error parsing timezone info: %s. Events time will be considered as in current timezone.");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при чтении информации о часовом поясе: \'%s\'. Время событий будет интерпретироваться в текущем часовом поясе.");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI3UKLI6KLJFVZFMG2OELL5V4AA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Cancelled by user");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отменено пользователем");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV5IXM24BYZH2JMRIOX4YITTIPM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Imported file has wrong extension. Expected: \".xml\"\'; Received: \".");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импортируемый файл имеет неверное расширение. Ожидалось: \".xml\"; Получено: \".");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVATW3ZK2WFHUBD2JYHCEP2HXRE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Importing events");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импорт событий");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWBOJMGBI7VAYZB6BHHQPW7ELGE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import Events");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импорт событий");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXKTS5BMDXJE3PF3AOM7UPMD2Y4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import error: ");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка импорта: ");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYRRWXULRW5AEXKDWLWLS4OZEAA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
			}

			public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(EventLogGroup - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbagcACQVZOMVVHWDBROXAAIT4AGD7E"),"EventLogGroup - Localizing Bundle",$$$items$$$);
		}
