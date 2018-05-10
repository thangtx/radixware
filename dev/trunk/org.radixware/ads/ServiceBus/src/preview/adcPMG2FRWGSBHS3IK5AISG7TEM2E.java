
/* Radix::ServiceBus::SbDesktopUtil - Desktop Executable*/

/*Radix::ServiceBus::SbDesktopUtil-Desktop Dynamic Class*/

package org.radixware.ads.ServiceBus.explorer;

import java.util.*;
import org.radixware.kernel.common.msdl.XmlObjectMessagingXsdCreator;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbDesktopUtil")
public published class SbDesktopUtil  {



	/*Radix::ServiceBus::SbDesktopUtil:Nested classes-Nested Classes*/

	/*Radix::ServiceBus::SbDesktopUtil:Properties-Properties*/

	/*Radix::ServiceBus::SbDesktopUtil:PREDEFINED_SCHEMES_CACHE_ID-Dynamic Property*/



	protected static Str PREDEFINED_SCHEMES_CACHE_ID=java.util.UUID.randomUUID().toString();











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbDesktopUtil:PREDEFINED_SCHEMES_CACHE_ID")
	private static final  Str getPREDEFINED_SCHEMES_CACHE_ID() {
		return PREDEFINED_SCHEMES_CACHE_ID;
	}

	/*Radix::ServiceBus::SbDesktopUtil:Methods-Methods*/

	/*Radix::ServiceBus::SbDesktopUtil:getSchemes-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbDesktopUtil:getSchemes")
	public static  org.radixware.ads.ServiceBus.common.IXmlSchemes getSchemes (org.radixware.kernel.common.client.IClientEnvironment environment) {
		Map<Str, Xml> predefinedSchemes = 
		    (Map<Str, Xml>)org.radixware.kernel.common.client.env.EnvironmentVariables.get(PREDEFINED_SCHEMES_CACHE_ID,environment);
		final Client.Types::IProgressHandle progress = environment.getProgressHandleManager().newStandardProgressHandle();
		progress.startProgress("Loading Schemas",true);
		try{
		    final ServiceBus.Cmd::CommandsXsd:SchemeRsDocument xDoc = ServiceBus.Cmd::CommandUtil.getSchemeList(environment, predefinedSchemes==null);
		    if (progress.wasCanceled()){
		        return new XmlSchemes(Collections.<Str,Xml>emptyMap());
		    }
		    final Map<Str, Xml> allSchemes = new HashMap<>();
		    if (predefinedSchemes==null){
		        predefinedSchemes = new HashMap<Str, Xml>();
		        final ServiceBus.Cmd::CommandsXsd:SchemeRsDocument.SchemeRs.PredefinedSchemes predefinedSchemesXml = xDoc.getSchemeRs().getPredefinedSchemes();
		        if (predefinedSchemesXml!=null && predefinedSchemesXml.getItemList()!=null && !predefinedSchemesXml.getItemList().isEmpty()){
		            final int schemesCount = predefinedSchemesXml.getItemList().size();
		            progress.setMaximumValue(schemesCount);
		            for (int i=0; i<schemesCount; i++){
		                progress.setValue(i+1);
		                final ServiceBus.Cmd::CommandsXsd:SchemeRsDocument.SchemeRs.PredefinedSchemes.Item schema = predefinedSchemesXml.getItemList().get(i);
		                try{
		                    if (schema.SchemeType==System::DataSchemeType:MSDL.Value){                                
		                            final Xml xmlSchema;
		                            if (schema.getScheme()==null){
		                                final java.io.InputStream stream = environment.getDefManager().getRepository().getInputStreamForXmlScheme(schema.getId());
		                                final Meta::MsdlXsd:MessageElementDocument me = Meta::MsdlXsd:MessageElementDocument.Factory.parse(stream);
		                                final org.radixware.kernel.common.msdl.RootMsdlScheme root = new org.radixware.kernel.common.msdl.RootMsdlScheme(me.getMessageElement());
		                                final Xml xml = XmlObjectMessagingXsdCreator.createXSD(root.getMessage(), "");                    
		                                xmlSchema = xml;
		                            }else{
		                                xmlSchema = readXmlSchema(schema.getScheme().newInputStream());
		                            }
		                            if (xmlSchema!=null){
		                                predefinedSchemes.put(schema.getNamespace(),xmlSchema);
		                            }
		                    }else {                        
		                        final java.io.InputStream stream;
		                        if (schema.getScheme()==null){                            
		                            if (schema.IsTransparent.booleanValue()){
		                                stream = environment.getDefManager().getRepository().getInputStreamForXmlScheme(schema.getNamespace());
		                            }else{
		                                stream = environment.getDefManager().getRepository().getInputStreamForXmlScheme(schema.getId());
		                            }
		                        }else{
		                            stream = schema.getScheme().newInputStream();
		                        }
		                        final Xml xmlSchema = readXmlSchema(stream);
		                        if (xmlSchema==null){
		                            final String messageTemplate = "Xml document is null for '%1$s' (#%2$s)";
		                            environment.getTracer().warning(Str.format(messageTemplate,schema.getNamespace(),schema.getId().toString()));
		                            continue;                    
		                        }
		                        predefinedSchemes.put(schema.getNamespace(),xmlSchema);
		                    }
		                }catch(Exceptions::DefinitionError | Exceptions::IOException | Exceptions::XmlException exception){
		                    final Str messageTemplate;
		                    if (schema.SchemeType==System::DataSchemeType:MSDL.Value){
		                        messageTemplate = "Error reading the MSDL schema '%1$s' (#%2$s)";
		                    }else if (schema.SchemeType==System::DataSchemeType:XSD.Value){
		                        messageTemplate = "Error reading the XSD schema '%1$s' (#%2$s)";
		                    }else{
		                        messageTemplate = "Error reading the WSDL schema '%1$s' (#%2$s)";
		                    }
		                    environment.getTracer().error(Str.format(messageTemplate,schema.getNamespace(),schema.getId().toString()),exception);                    
		                }
		            }
		            org.radixware.kernel.common.client.env.EnvironmentVariables.put(PREDEFINED_SCHEMES_CACHE_ID,environment,predefinedSchemes);
		        }    
		    }
		    allSchemes.putAll(predefinedSchemes);
		    final ServiceBus.Cmd::CommandsXsd:SchemeRsDocument.SchemeRs.UserDefinedSchemes userdefinedSchemesXml = xDoc.getSchemeRs().getUserDefinedSchemes();
		    if (userdefinedSchemesXml!=null && userdefinedSchemesXml.getItemList()!=null && !userdefinedSchemesXml.getItemList().isEmpty()){
		        for (ServiceBus.Cmd::CommandsXsd:SchemeRsDocument.SchemeRs.UserDefinedSchemes.Item schema: userdefinedSchemesXml.getItemList()){
		                if (!allSchemes.containsKey(schema.getNamespace())){
		                    if (schema.getScheme() == null) {
		                        final String messageTemplate = "Schema element is null for '%1$s'";
		                        environment.getTracer().warning(Str.format(messageTemplate, schema.getNamespace()));
		                        continue;
		                    }
		                    final Xml xmlSchema = readXmlSchema(schema.getScheme().newInputStream());
		                    if (xmlSchema!=null){
		                        allSchemes.put(schema.getNamespace(),xmlSchema);
		                    }
		                }
		        }
		    }
		    return new XmlSchemes(allSchemes);
		}finally{
		    progress.finishProgress();
		}
	}

	/*Radix::ServiceBus::SbDesktopUtil:inputReferences-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbDesktopUtil:inputReferences")
	 static  boolean inputReferences (org.radixware.kernel.common.client.IClientEnvironment env, org.radixware.ads.ServiceBus.common.ImpExpXsd.PipelineDocument xDoc) {
		final Common.Dlg::InputPropsDlg dlg = new Common.Dlg::InputPropsDlg(env);
		final Common.Dlg::InputPropsDlg:Model model = (Common.Dlg::InputPropsDlg:Model) dlg.getModel();

		prepareInputProps(model, xDoc.Pipeline, null);
		if (model.isEmpty())
		    return true;

		return dlg.execDialog() == Client.Views::DialogResult.ACCEPTED;
	}

	/*Radix::ServiceBus::SbDesktopUtil:inputReferences-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbDesktopUtil:inputReferences")
	 static  boolean inputReferences (org.radixware.kernel.common.client.IClientEnvironment env, org.radixware.ads.ServiceBus.common.ImpExpXsd.PipelineGroupDocument xDoc) {
		final Common.Dlg::InputPropsDlg dlg = new Common.Dlg::InputPropsDlg(env);
		final Common.Dlg::InputPropsDlg:Model model = (Common.Dlg::InputPropsDlg:Model) dlg.getModel();

		for (ImpExpXsd:Pipeline x : xDoc.PipelineGroup.ItemList)
		    prepareInputProps(model, x, x.Title);

		if (model.isEmpty())
		    return true;

		return dlg.execDialog() == Client.Views::DialogResult.ACCEPTED;
	}

	/*Radix::ServiceBus::SbDesktopUtil:prepareInputProps-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbDesktopUtil:prepareInputProps")
	private static  void prepareInputProps (org.radixware.ads.Common.Dlg.explorer.InputPropsDlg:Model model, org.radixware.ads.ServiceBus.common.ImpExpXsd.Pipeline xml, Str owner) {
		ServiceBus.Cmd::CommandsXsd:GetPropsInfoByRidDocument xRq = ServiceBus.Cmd::CommandsXsd:GetPropsInfoByRidDocument.Factory.newInstance();
		xRq.addNewGetPropsInfoByRid();
		if (xml.isSetPipelineNodes())
		    for (ImpExpXsd:Node xNode : xml.PipelineNodes.NodeList) 
		        if (xNode.isSetReferenceProps()) {
		            for (Common::CommonXsd:EditableProperty xProp : xNode.ReferenceProps.ItemList) {
		                if (xProp.isSetRid() && xProp.Value == null) {
		                    xRq.GetPropsInfoByRid.addNewItem().set(xProp);
		                }
		            }
		        }
		if (xml.isSetOtherNodes())
		    for (ImpExpXsd:Node xNode : xml.OtherNodes.NodeList) 
		        if (xNode.isSetReferenceProps()) {
		            for (Common::CommonXsd:EditableProperty xProp : xNode.ReferenceProps.ItemList) {
		                if (xProp.isSetRid() && xProp.Value == null) {
		                    xRq.GetPropsInfoByRid.addNewItem().set(xProp);
		                }
		            }
		        }


		ServiceBus.Cmd::CommandsXsd:GetPropsInfoByRidDocument xRs = null;
		if (xRq.GetPropsInfoByRid.sizeOfItemArray() != 0) {
		    try {
		        xRs = (ServiceBus.Cmd::CommandsXsd:GetPropsInfoByRidDocument) model.getEnvironment().getEasSession().executeContextlessCommand(idof[ServiceBus.Cmd::GetNodeRefPropsInfoByRidCmd], xRq, ServiceBus.Cmd::CommandsXsd:GetPropsInfoByRidDocument.class);
		    } catch (Exceptions::InterruptedException | Exceptions::ServiceClientException ex) {
		        model.getEnvironment().getTracer().error("Error on read properties info by RID", ex);
		    }
		}

		if (xml.isSetPipelineNodes())
		    for (ImpExpXsd:Node xNode : xml.PipelineNodes.NodeList) {
		        if (xRs != null && xNode.isSetReferenceProps()) {
		            fillNodePropsPid(xNode.ReferenceProps, xRs.GetPropsInfoByRid);
		        }
		        model.addObj(owner, xNode.ClassId, xNode.Title, xNode.ReferenceProps);
		    }
		if (xml.isSetOtherNodes())
		    for (ImpExpXsd:Node xNode : xml.OtherNodes.NodeList) {
		        if (xRs != null && xNode.isSetReferenceProps()) {
		            fillNodePropsPid(xNode.ReferenceProps, xRs.GetPropsInfoByRid);
		        }
		        model.addObj(owner, xNode.ClassId, xNode.Title, xNode.ReferenceProps);
		    }

	}

	/*Radix::ServiceBus::SbDesktopUtil:readXmlSchema-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbDesktopUtil:readXmlSchema")
	private static  org.apache.xmlbeans.XmlObject readXmlSchema (java.io.InputStream stream) {
		java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
		try {
		    org.radixware.kernel.common.utils.FileUtils.copyStream(stream, out);
		} catch (Exceptions::IOException ex) {
		    java.util.logging.Logger.getLogger(org.radixware.kernel.common.utils.XPathUtils.class.getName()).log(java.util.logging.Level.FINE, ex.getMessage(), ex);
		    return null;
		}
		final byte[] bytes = out.toByteArray();
		try {
		    org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument xsDoc = org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Factory.parse(new java.io.ByteArrayInputStream(bytes));
		    if (xsDoc != null) {
		        return xsDoc;
		    }
		} catch (Throwable ex) {//for prevent crash caused by xmlbeans bugs
		    java.util.logging.Logger.getLogger(org.radixware.kernel.common.utils.XPathUtils.class.getName()).log(java.util.logging.Level.FINE, ex.getMessage(), ex);
		}
		try {
		    return org.xmlsoap.schemas.wsdl.DefinitionsDocument.Factory.parse(new java.io.ByteArrayInputStream(bytes));     
		} catch (Throwable ex) {//for prevent crash caused by xmlbeans bugs
		    java.util.logging.Logger.getLogger(org.radixware.kernel.common.utils.XPathUtils.class.getName()).log(java.util.logging.Level.FINE, ex.getMessage(), ex);
		    return null;
		}
	}

	/*Radix::ServiceBus::SbDesktopUtil:isUniDirectPipeline-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbDesktopUtil:isUniDirectPipeline")
	public static published  boolean isUniDirectPipeline (org.radixware.kernel.common.client.models.EntityModel model) {

		Pipeline pipeline = null;
		PipelineNode node;
		if(model instanceof PipelineNode){
		    node = (PipelineNode)model;
		}else
		    node = (PipelineNode)model.findOwnerByClass(PipelineNode.class);
		    
		if(node != null){
		    Explorer.Models::EntityModel entity = (Explorer.Models::EntityModel)node;
		    if (entity.getContext() instanceof SbCreatingContext) {
		        pipeline = ((SbCreatingContext) entity.getContext()).pipeline;     
		    } else if (entity.getContext() instanceof SbEditingContext) {
		        pipeline = ((SbEditingContext) entity.getContext()).pipeline;     
		    }    
		}


		return pipeline == null ? false : pipeline.isUniDirect.Value == Bool.TRUE;
	}

	/*Radix::ServiceBus::SbDesktopUtil:findOwnerPipeline-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbDesktopUtil:findOwnerPipeline")
	public static  org.radixware.ads.ServiceBus.explorer.Pipeline findOwnerPipeline (org.radixware.kernel.common.client.models.EntityModel model) {
		if(model.getContext() instanceof SbEditingContext){
		    SbEditingContext context = (SbEditingContext)model.getContext();
		    return context.pipeline;
		}
		return null;


		    
	}

	/*Radix::ServiceBus::SbDesktopUtil:showCommentDlg-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbDesktopUtil:showCommentDlg")
	public static  org.radixware.ads.Common.Dlg.explorer.CommentDlg showCommentDlg (org.radixware.kernel.common.client.IClientEnvironment env) {
		final Common.Dlg::CommentDlg dlg = new Common.Dlg::CommentDlg(env);
		dlg.getModel().caption = "Comment on Version Created";
		return dlg;
	}

	/*Radix::ServiceBus::SbDesktopUtil:fillNodePropsPid-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus::SbDesktopUtil:fillNodePropsPid")
	private static  void fillNodePropsPid (org.radixware.ads.Common.common.CommonXsd.EditableProperties xPropsToFill, org.radixware.ads.Common.common.CommonXsd.EditableProperties xPropsWithKnownPid) {
		for (Common::CommonXsd:EditableProperty xProp : xPropsToFill.ItemList) {
		    if (xProp.isSetRid()) {
		        for (Common::CommonXsd:EditableProperty xRidProp : xPropsWithKnownPid.ItemList) {
		            if (xProp.Id == xRidProp.Id && xProp.EntityId == xRidProp.EntityId && java.util.Objects.equals(xProp.Rid, xRidProp.Rid)) {
		                xProp.Value = xRidProp.Value;
		                break;
		            }
		        }
		    }
		}
	}


}

/* Radix::ServiceBus::SbDesktopUtil - Desktop Meta*/

/*Radix::ServiceBus::SbDesktopUtil-Desktop Dynamic Class*/

package org.radixware.ads.ServiceBus.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class SbDesktopUtil_mi{
}

/* Radix::ServiceBus::SbDesktopUtil - Localizing Bundle */
package org.radixware.ads.ServiceBus.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class SbDesktopUtil - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Loading Schemas");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Загрузка схем");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5NU7GZL5Z5CNBH6EVUNIOTWYTM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error reading the WSDL schema \'%1$s\' (#%2$s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при чтении WSDL-схемы \'%1$s\' (#%2$s)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIUQ2RTLBKFCFFLFLNLNFN3BT3E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error reading the MSDL schema \'%1$s\' (#%2$s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при чтении MSDL-схемы \'%1$s\' (#%2$s)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQD33HRRIS5B4XPSY3XH24U2TLQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error reading the XSD schema \'%1$s\' (#%2$s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при чтении XSD-схемы \'%1$s\' (#%2$s)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTD5BJANZUVHYZB7EZOQI4GQKHY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Comment on Version Created");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Комментарий для создаваемой версии");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYI626XIW2JHDLP2OT4KVTSPUUY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(SbDesktopUtil - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcPMG2FRWGSBHS3IK5AISG7TEM2E"),"SbDesktopUtil - Localizing Bundle",$$$items$$$);
}
