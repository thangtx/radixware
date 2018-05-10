
/* Radix::SystemMonitor::InstanceStateHistoryExporter - Server Executable*/

/*Radix::SystemMonitor::InstanceStateHistoryExporter-Server Dynamic Class*/

package org.radixware.ads.SystemMonitor.server;

import java.util.Scanner;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;


@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistoryExporter")
 class InstanceStateHistoryExporter  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private static String instThreadsStateHistTemplate;

	static {
	//    try {
	        try (Scanner sc = new Scanner(InstanceStateHistoryExporter.class.getResourceAsStream("instance_threads_state_history_template.html")).useDelimiter("\\A")) {
	            instThreadsStateHistTemplate = sc.hasNext() ? sc.next() : "";
	        }
	//    } catch (ClassNotFoundException ex) {
	//        //.().getTrace().put(, "Something wrong: " + ex.getLocalizedMessage(), );
	//        .("Something wrong: " + ex.getLocalizedMessage(), );
	//        instThreadsStateHistTemplate = instThreadsStateHistTemplate = "Error during processing request.";
	//    }
	}
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return InstanceStateHistoryExporter_mi.rdxMeta;}

	/*Radix::SystemMonitor::InstanceStateHistoryExporter:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::InstanceStateHistoryExporter:Properties-Properties*/





























	/*Radix::SystemMonitor::InstanceStateHistoryExporter:Methods-Methods*/

	/*Radix::SystemMonitor::InstanceStateHistoryExporter:export-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistoryExporter:export")
	 static  void export (Str fileType, Str fileName, Int maxCount, Int instanceId, java.util.Iterator<? extends org.radixware.ads.Types.server.Entity> iterator, org.radixware.ads.SystemMonitor.server.InstanceStateHistoryGroup group) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ResourceUsageException,org.radixware.kernel.common.exceptions.ResourceUsageTimeout,java.sql.SQLException,java.io.UnsupportedEncodingException,javax.xml.stream.XMLStreamException {
		final boolean isPlain = fileType.equals("text/plain");
		final boolean isHtml = fileType.equals("text/html");
		final boolean isXml = fileType.equals("text/xml");

		Client.Resources::FileOutResource fileOut = null;
		XMLStreamWriter xmlWriter = null;
		java.io.PrintWriter printWriter = null;
		Client.Resources::ProgressDialogProcessResource process = null;
		try {
		    Client.Resources::ProgressDialogResource dlg = new ProgressDialogResource(Arte::Arte.getInstance(), "Exporting events", true);
		    process = dlg.startNewProcess("Exporting events", true, maxCount != null);
		    fileOut = new FileOutResource(Arte::Arte.getInstance(),
		            fileName,
		            Client.Resources::FileOpenMode:TruncateOrCreate,
		            Client.Resources::FileOpenShareMode:Write);
		            
		    printWriter = isPlain || isHtml ? new java.io.PrintWriter(new java.io.OutputStreamWriter(fileOut, java.nio.charset.StandardCharsets.UTF_8)) : null;
		    xmlWriter = isXml ? XMLOutputFactory.newInstance().createXMLStreamWriter(fileOut, "UTF-8") : null;
		    if (isHtml) {
		        String instName = "<instance id unavailable>";
		        if (instanceId != null) {
		            System::Instance inst = System::Instance.loadByPK(instanceId, false);
		            instName = "Instance #" + inst.id + " - " + inst.title;
		        }
		        
		        printWriter.write(instThreadsStateHistTemplate.replace("${INSTANCE_NAME}", "" + instName));
		        java.lang.StringBuilder sb = new java.lang.StringBuilder();
		        sb.append("<table><tr>");
		        String[] colNames = new String[] {"Time", "Forced", "Thread Kind", "Id", "Name", "Stack", "Ancestor Id", "Unit Id", "ARTE Seq", "ARTE Serial", "DB Sid", "DB Serial", "Trace Contexts", "CPU Diff (ms)", "DB Diff (ms)", "Ext Diff (ms)", "Queue Diff (ms)", "Uptime (s)", "Rq Start Time", "Lock Name", "Lock Owner Name"};
		        for (String colName: colNames) {
		            sb.append("<th>").append(colName).append("</th>");
		        }
		        sb.append("</tr>");
		        printWriter.write(sb.toString());
		    }
		    
		    if (isXml) {
		        //xmlWriter.setDefaultNamespace(nameSpace);
		        xmlWriter.writeStartDocument();
		        xmlWriter.writeCharacters("\n");
		        xmlWriter.writeStartElement("InstanceThreadsStateHistory");
		        //xmlWriter.writeDefaultNamespace(nameSpace);
		        xmlWriter.writeAttribute("TimeZone", java.util.TimeZone.getDefault().getID());
		    }
		    
		    final int iMaxCount = maxCount != null
		            ? maxCount.intValue()
		            : Integer.MAX_VALUE;
		    InstanceStateHistory ish;
		    int i = 0;
		    for (; i < iMaxCount && (ish = group.getNext(iterator)) != null; ++i) {
		        if (i % 100 == 0) {
		            if (printWriter != null) {
		                printWriter.flush();
		            } else if (xmlWriter != null) {
		                xmlWriter.flush();
		            }
		            final Float percent = maxCount != null ? (i * 100.f) / iMaxCount : null;
		            if(!InstanceStateHistoryExporter.setTitleAndPercent(process, "Exported events count: " + i, percent)) {
		                return;
		            }
		        }
		        if (isPlain) {
		            ish.exportToPlain(printWriter);
		        } else if (isHtml) {
		            ish.exportToHtml(printWriter);
		        } else if (isXml) {
		            ish.exportToXml(xmlWriter);
		        }
		        
		        ish.discard();
		    }
		    
		    if (isHtml) {
		        printWriter.write("</table> </body> </html>");
		    }
		    
		    if (isXml) {
		        xmlWriter.writeCharacters("\n");
		        xmlWriter.writeEndElement();
		        xmlWriter.flush();
		    }
		   
		    try {
		        Client.Resources::MessageDialogResource.information(Arte::Arte.getInstance(), "Exporting events", String.format("Events exported: %d", i));
		    } catch(Exception e) {
		    }
		} finally {
		    if(process!=null)
		        try {
		            process.close();
		        } catch(Exception e) {
		            process = null;
		        }
		    
		    if(fileOut != null) {
		        if (printWriter != null) {
		            printWriter.flush();
		        }
		        
		        try {
		            if (isXml) {
		                xmlWriter.writeCharacters("\n");
		                xmlWriter.writeEndDocument();
		                xmlWriter.flush();
		            }
		        } catch(Exceptions::XmlStreamException e) {
		            Arte::Trace.warning(e.getMessage(), Arte::EventSource:App);
		        }
		        
		        try {
		            fileOut.close();
		        } catch(Exceptions::IOException e) {
		            Arte::Trace.warning(e.getMessage(), Arte::EventSource:App);
		        }
		    }
		}
	}

	/*Radix::SystemMonitor::InstanceStateHistoryExporter:setTitleAndPercent-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistoryExporter:setTitleAndPercent")
	private static  boolean setTitleAndPercent (org.radixware.kernel.server.arte.resources.ProgressDialogResource.Process process, Str title, java.lang.Float percent) {
		if(process == null)
		    return true;

		try {
		    if(process.checkIsCancelled()) {
		        Client.Resources::MessageDialogResource.information(Arte::Arte.getInstance(), "Exporting instance threads state history", "Cancelled by user");
		        return false;
		    }
		    process.set(title, percent, true);
		} catch(Exceptions::InterruptedException e) {
		    throw new AppError(e.getMessage(), e);
		} catch(Exceptions::ResourceUsageException e) {
		    Arte::Trace.warning(e.getMessage(), Arte::EventSource:App);
		} catch(Exceptions::ResourceUsageTimeout e) {
		    Arte::Trace.warning(e.getMessage(), Arte::EventSource:App);
		}

		return true;
	}


}

/* Radix::SystemMonitor::InstanceStateHistoryExporter - Server Meta*/

/*Radix::SystemMonitor::InstanceStateHistoryExporter-Server Dynamic Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class InstanceStateHistoryExporter_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcEGJTB4CV55EJ7FAO4J23DDCDHY"),"InstanceStateHistoryExporter",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::SystemMonitor::InstanceStateHistoryExporter:Properties-Properties*/
						null,

						/*Radix::SystemMonitor::InstanceStateHistoryExporter:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth62OQXO2XTVBB5NDTOIGFQYQEVE"),"export",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("fileType",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFQYKKDNXWNCKFHYDAMAV3HARLY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("fileName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6PKJVSPDJBEC5FJU2LQAUT5X4U")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("maxCount",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6ETN2LT3GBBVFDVNATCRDDSQMM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("instanceId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2NGE44MXXZEU5JH3ZQXXSUPPLQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("iterator",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGV7FP5YNQJDK7BQRRRFDH7X6CE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("group",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4WQAFBTZCVDSNPXFEKATGW6UWQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWVX7PTZ4JNFVFCYKXTITIF3FWM"),"setTitleAndPercent",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("process",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKLIVPEIHFRBK3MQEVL2TNNXODY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRRIZQWDLZRATNB3GCHKXQI7TGU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("percent",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWSAVEBMPRNAVLDMFWK6PFIOUIU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::InstanceStateHistoryExporter - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class InstanceStateHistoryExporter - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Exporting events");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспорт событий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2OPS2SIJGFHBBFWKN6HPJJ6ZPA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Exporting events");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспорт событий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBEJPP3DD2NGBHOVHIVOQV56UPU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Exported events count: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Число экспортированных событий: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK53DUVAILFFDXJ7JWM4ZO5H5KE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Exporting instance threads state history");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспорт истории состояния потоков инстанции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKKOYIRSCHJAZDHHUVUJ7GL3T2U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Exporting events");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспорт событий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOPTD6TX75VFIDD3M3GOO4N7GKE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Cancelled by user");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отменено пользователем");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSDCGZ66WIRDHLEPU7WKG2PP7O4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Events exported: %d");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Событий экспортировано: %d");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT4E3IBHAUBHBBJWU4DNP5LBCLM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(InstanceStateHistoryExporter - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcEGJTB4CV55EJ7FAO4J23DDCDHY"),"InstanceStateHistoryExporter - Localizing Bundle",$$$items$$$);
}
