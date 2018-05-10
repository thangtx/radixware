
/* Radix::System::EventLogExporter - Server Executable*/

/*Radix::System::EventLogExporter-Server Dynamic Class*/

package org.radixware.ads.System.server;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogExporter")
 class EventLogExporter  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return EventLogExporter_mi.rdxMeta;}

	/*Radix::System::EventLogExporter:Nested classes-Nested Classes*/

	/*Radix::System::EventLogExporter:Properties-Properties*/





























	/*Radix::System::EventLogExporter:Methods-Methods*/

	/*Radix::System::EventLogExporter:export-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogExporter:export")
	 static  void export (Str fileName, Int maxCount, java.util.Iterator<? extends org.radixware.ads.Types.server.Entity> iterator, org.radixware.ads.System.server.IEventLogExportInterface exported) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ResourceUsageException,org.radixware.kernel.common.exceptions.ResourceUsageTimeout,java.sql.SQLException,java.io.UnsupportedEncodingException,javax.xml.stream.XMLStreamException {
		final Str nameSpace = "http://schemas.radixware.org/systemcommands.xsd";
		Client.Resources::FileOutResource fileOut = null;
		XMLStreamWriter writer = null;
		Client.Resources::ProgressDialogProcessResource process = null;
		try {
		    Client.Resources::ProgressDialogResource dlg = new ProgressDialogResource(Arte::Arte.getInstance(), "Exporting events", true);
		    process = dlg.startNewProcess("Exporting events", true, maxCount != null);
		    fileOut = new FileOutResource(Arte::Arte.getInstance(),
		            fileName,
		            Client.Resources::FileOpenMode:TruncateOrCreate,
		            Client.Resources::FileOpenShareMode:Write);
		    writer = XMLOutputFactory.newInstance().createXMLStreamWriter(fileOut, "UTF-8");
		    writer.setDefaultNamespace(nameSpace);
		    
		    writer.writeStartDocument();
		    writer.writeCharacters("\n");
		    writer.writeStartElement(nameSpace, "EventList");
		    writer.writeDefaultNamespace(nameSpace);
		    
		    writer.writeAttribute("TimeZone", java.util.TimeZone.getDefault().getID());
		    writer.writeAttribute("LayerVersions", Arte::Arte.getAllLayerVersionsAsString());
		    
		    DateTime currentTime = new DateTime(java.lang.System.currentTimeMillis());
		    writer.writeAttribute("ExportTime", Types::ValAsStr.toStr(currentTime, Meta::ValType:DateTime));
		    
		    int currentInstanceId = Arte::Arte.getInstance().getInstance().getId();
		    Instance currentInstance = Instance.loadByPK(currentInstanceId, true);
		    writer.writeAttribute("ExportedFromHost", currentInstance.hostIpAddressesStr);
		    
		    int i = 0;
		    final int iMaxCount = maxCount != null
		            ? maxCount.intValue()
		            : Integer.MAX_VALUE;
		    EventLog event;
		    while(i<iMaxCount && null != (event = exported.getNextEvent(iterator))) {        
		        i++;
		        if(i%100 == 0) {
		            writer.flush();
		            final Float percent = maxCount != null ? ((float)i*100)/iMaxCount : null;
		            if(!EventLogExporter.setTitleAndPercent(process, "Exported events count: " + i, percent))
		                return;
		        }
		        
		        if(event != null) {
		            event.export(writer, nameSpace);
		            event.discard();
		        }
		    }
		    writer.writeCharacters("\n");
		    writer.writeEndElement();
		    writer.flush();
		    
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
		        try {
		            if(writer != null) {
		                writer.writeEndDocument();
		                writer.flush();
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

	/*Radix::System::EventLogExporter:setTitleAndPercent-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::EventLogExporter:setTitleAndPercent")
	private static  boolean setTitleAndPercent (org.radixware.kernel.server.arte.resources.ProgressDialogResource.Process process, Str title, java.lang.Float percent) {
		if(process == null)
		    return true;

		try {
		    if(process.checkIsCancelled()) {
		        Client.Resources::MessageDialogResource.information(Arte::Arte.getInstance(), "Exporting events", "Cancelled by user");
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

/* Radix::System::EventLogExporter - Server Meta*/

/*Radix::System::EventLogExporter-Server Dynamic Class*/

package org.radixware.ads.System.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EventLogExporter_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcA3INHB3PFJA2JLS7Z33GFYG22A"),"EventLogExporter",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::System::EventLogExporter:Properties-Properties*/
						null,

						/*Radix::System::EventLogExporter:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAVPXNJUXERGZJBVDUBUNUQPADM"),"export",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("fileName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6PKJVSPDJBEC5FJU2LQAUT5X4U")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("maxCount",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6ETN2LT3GBBVFDVNATCRDDSQMM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("iterator",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGV7FP5YNQJDK7BQRRRFDH7X6CE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("exported",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4WQAFBTZCVDSNPXFEKATGW6UWQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYBCJN7FYDFH4NFES3H4JZLI2UQ"),"setTitleAndPercent",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("process",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKLIVPEIHFRBK3MQEVL2TNNXODY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRRIZQWDLZRATNB3GCHKXQI7TGU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("percent",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWSAVEBMPRNAVLDMFWK6PFIOUIU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

/* Radix::System::EventLogExporter - Localizing Bundle */
package org.radixware.ads.System.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EventLogExporter - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Cancelled by user");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отменено пользователем");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDUIBBI7E25BYHJ2BYFLPFMXNKQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Exported events count: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Число экспортированных событий: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFXLSVCH7MZGLRD3VCHLWEWVDSU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Exporting events");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспорт событий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGO6MZAA6ING5VCGP3XZ7K5XDHA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Exporting events");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспорт событий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKCYP5BSOMZAFPJPJSFO4KD3OWM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Exporting events");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспорт событий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMAJ65KZ6DBF5ZGU5BJLN5US2WY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Exporting events");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспорт событий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNALGNQR4JJFVDCLROENT5OYOLA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Events exported: %d");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Событий экспортировано: %d");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQU3DKXEJLNBH7PMZBSA7NDE3S4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(EventLogExporter - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcA3INHB3PFJA2JLS7Z33GFYG22A"),"EventLogExporter - Localizing Bundle",$$$items$$$);
}
