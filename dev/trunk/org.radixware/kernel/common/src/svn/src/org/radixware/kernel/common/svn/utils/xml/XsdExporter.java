/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.svn.utils.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.IncludeDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.radixware.kernel.common.svn.radixdoc.xmlexport.ManagerXmlSchemaExportDataProvider;
import org.radixware.kernel.common.svn.radixdoc.xmlexport.ManagerXmlSchemaExportDataProvider.EnumData;
import org.radixware.kernel.common.svn.radixdoc.xmlexport.ManagerXmlSchemaExportDataProvider.Localization;
import org.radixware.kernel.common.svn.radixdoc.xmlexport.ManagerXmlSchemaExportDataProvider.Namespace2FileName;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.client.SvnEntry;
import org.radixware.kernel.common.svn.client.SvnPath;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.APIDocument;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.EnumDefinition;
import org.radixware.schemas.adsdef.EnumItemDefinition;
import org.radixware.schemas.adsdef.LocalizedString;
import org.radixware.schemas.adsdef.LocalizingBundleDefinition;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlsoap.schemas.wsdl.DefinitionsDocument;
import org.xmlsoap.schemas.wsdl.TImport;
import org.xmlsoap.schemas.wsdl.TTypes;

public class XsdExporter extends AbstractSvnObserver {
    
    private static class FileInfo {

        String name;
        boolean fromKernel;

        public FileInfo(String name, boolean fromKernel) {
            this.name = name;
            this.fromKernel = fromKernel;
        }
    }
    
    private static final String KERNEL_XSD_PATH = "schemaorg_apache_xmlbeans/src/";
    
    private Set<EIsoLanguage> langs;
    private final Map<Id, AdsDefinitionDocument> xsdDefs = new HashMap<>();
    private final Map<String, String> schemaNs2ModuleName = new HashMap<>();
    private final File outputDir;

    public XsdExporter(SVNRepositoryAdapter repository, long revision, File outputDir, Set<EIsoLanguage> languages) {
        super(repository, revision);
        this.outputDir = outputDir;
        this.langs = languages;
    }

    private List<String> listLayers() throws RadixSvnException {
        return listLayers("");
    }
    
    private void checkSchemaImports(File file, SchemaDocument.Schema xSchema, Map<String, List<FileInfo>> ns2FileName) {
        for (ImportDocument.Import xImport : xSchema.getImportArray()) {
            String ns = xImport.getNamespace();
            if (ns != null) {
                List<FileInfo> fileNames = ns2FileName.get(ns);
                if (fileNames != null) {
                    if (fileNames.size() == 1) {
                        xImport.setSchemaLocation(fileNames.get(0).name);
                    } else {
                        String location = xImport.getSchemaLocation();
                        if (location != null && location.startsWith("radix:/")) {
                            String originalSuffix = location.substring(7);
                            for (FileInfo info : fileNames) {
                                if (info.name.endsWith(originalSuffix)) {
                                    xImport.setSchemaLocation(info.name);
                                    break;
                                }
                            }
                        } else {
                            xImport.setSchemaLocation(fileNames.get(0).name);
                        }
                    }
                }
            }
        }
        String ns = xSchema.getTargetNamespace();
        if (ns != null) {
            for (IncludeDocument.Include xInclude : xSchema.getIncludeArray()) {
                String location = xInclude.getSchemaLocation();
                List<FileInfo> fileNames = ns2FileName.get(ns);

                if (fileNames != null) {
                    for (FileInfo info : fileNames) {
                        if (!Utils.equals(info.name, file.getName()) && info.name.endsWith(location)) {
                            xInclude.setSchemaLocation(info.name);
                        }
                    }
                }
            }
        }
    }

    private void processEnumerations(Node node, Map<String, EnumData> enums, Map<String, Localization> titles) {
        NodeList nodeList = node.getChildNodes();

        boolean isUseChilds = true;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            String locName = node.getLocalName();

            if (locName != null && locName.equals("appinfo")) {
                int len = node.getAttributes().getLength();
                boolean isMustAdd = false;
                for (int j = 0; j < len; j++) {
                    Node n = node.getAttributes().item(j);
                    if (n.getNodeName().equals("source")) {
                        if (n.getNodeName().equals("source")
                                && n.getNodeValue().equals("http://schemas.radixware.org/types.xsd")) {
                            isMustAdd = true;
                            break;
                        }
                    }
                }
                String tUri = "";
                if (isMustAdd) {
                    EnumData definition = null;
                    int len2 = node.getChildNodes().getLength();
                    for (int i = 0; i < len2; i++) {
                        Node node3 = node.getChildNodes().item(i);
                        if (node3.getNodeType() == Node.ELEMENT_NODE
                                && node3.getLocalName().equals("class")
                                && node3.getChildNodes().getLength() == 1
                                && node3.getChildNodes().item(0).getNodeType() == Node.TEXT_NODE) {
                            tUri = node3.getNamespaceURI();

                            Node classIdAttr = node3.getAttributes().getNamedItem("classId");
                            if (classIdAttr != null) {
                                String idCandidate = classIdAttr.getNodeValue();
                                definition = enums.get(idCandidate);
                            }
                            if (definition == null) {

                                String val = node3.getChildNodes().item(0).getNodeValue();
                                if (val != null) {
                                    definition = enums.get(val);
                                    if (definition == null) {
                                        int lastDot = val.lastIndexOf('.');
                                        if (lastDot > 0 && lastDot + 1 < val.length() - 1) {
                                            definition = enums.get(val.substring(lastDot + 1));
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                    if (definition != null) {

                        //String xsPreff = node.getPrefix().isEmpty() ? "" : node.getPrefix() + ":";
                        NodeList children = node.getChildNodes();
                        Node doc = null;
                        String docText = "Enum \"" + definition.getEnumName() + "\"";

                        if (children != null) {
                            loop:
                            for (int i = 0; i < children.getLength(); i++) {
                                Node child = children.item(i);
                                if ("documentation".equals(child.getLocalName())) {
                                    NodeList docChildren = child.getChildNodes();
                                    for (int d = 0; d < docChildren.getLength(); d++) {
                                        Node dchild = docChildren.item(d);
                                        if (dchild.getNodeType() == Node.TEXT_NODE && docText.equals(dchild.getNodeValue())) {
                                            doc = child;
                                            break loop;
                                        }
                                    }
                                }
                            }
                        }
                        if (doc == null) {
                            doc = node.getOwnerDocument().createElementNS(
                                    node.getNamespaceURI(), "documentation");
                            Node text = node.getOwnerDocument().createTextNode(docText);

                            node.appendChild(doc);
                            doc.appendChild(text);
                        }

                        node.appendChild(node.getOwnerDocument().createTextNode("\n\t"));
                        Node enumItems = node.getOwnerDocument().createElementNS(tUri, "enumItems");
                        node.appendChild(enumItems);
                        enumItems.appendChild(node.getOwnerDocument().createTextNode("\n\t"));

                        Localization locale = titles.get(definition.getId().toString());
                        List<EnumData.EnumItem> itemList = definition.getItems();
                        for (EnumData.EnumItem item : itemList) {
                            Element enumItem = node.getOwnerDocument().createElementNS(tUri, "enumItem");
                            enumItems.appendChild(enumItem);
                            enumItem.setAttribute("Name", item.getName());
                            enumItem.setAttribute("Val", item.getValue());
                            enumItems.appendChild(node.getOwnerDocument().createTextNode("\n\t"));

                            if (item.getTitleId() != null && locale != null) {
                                Map<EIsoLanguage, String> vals = locale.getTranslation(item.getTitleId());
                                if (vals != null) {
                                    List<EIsoLanguage> langList;
                                    if (langs != null) {//add all translations
                                        langList = new ArrayList<>(langs);
                                    } else {
                                        langList = new ArrayList<>(vals.keySet());
                                    }
                                    Collections.sort(langList, new Comparator<EIsoLanguage>() {
                                        @Override
                                        public int compare(EIsoLanguage o1, EIsoLanguage o2) {
                                            return o1.getValue().compareTo(o2.getValue());
                                        }
                                    });

                                    for (EIsoLanguage lang : langList) {
                                        String val = vals.get(lang);
                                        if (val != null) {
                                            enumItem.appendChild(node.getOwnerDocument().createTextNode("\n\t\t"));
                                            Element lng = node.getOwnerDocument().createElement("Title");
                                            enumItem.appendChild(lng);
                                            lng.setAttribute("language", lang.getValue().toUpperCase());
                                            Node text2 = node.getOwnerDocument().createTextNode(val);
                                            lng.appendChild(text2);
                                        }
                                    }

                                }
                            }
                            enumItem.appendChild(node.getOwnerDocument().createTextNode("\n\t"));
                        }
                    }
                }
                isUseChilds = false;
            }
        }
        if (isUseChilds) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                processEnumerations(nodeList.item(i), enums, titles);
            }
        }
    }

    public void process() throws RadixSvnException, IOException {
        Map<String, List<FileInfo>> ns2FileName = new HashMap<>();
        List<File> writtenFiles = new LinkedList<>();
        List<String> layers = listLayers();
        Map<String, EnumData> enums = new LinkedHashMap<>();
        Map<String, Localization> titles = new LinkedHashMap<>();
        for (String layer : layers) {
            List<String> modules = listKernelModules(layer);
            for (String module : modules) {
                consoleProcessModule(module, true, ns2FileName, writtenFiles, enums, titles);
            }
            modules = listAdsModules(layer);
            for (String module : modules) {
                consoleProcessModule(module, false, ns2FileName, writtenFiles, enums, titles);
            }
        }

        for (File file : writtenFiles) {
            try {
                SchemaDocument xDoc = SchemaDocument.Factory.parse(file);
                if (xDoc.getSchema() == null) {
                    continue;
                }

                checkSchemaImports(file, xDoc.getSchema(), ns2FileName);
                processEnumerations(xDoc.getSchema().getDomNode(), enums, titles);
                xDoc.save(file);
            } catch (XmlException e) {
                try {
                    DefinitionsDocument xDoc = DefinitionsDocument.Factory.parse(file);
                    if (xDoc.getDefinitions() == null) {
                        continue;
                    }
                    if (xDoc.getDefinitions().getTargetNamespace() == null) {
                        continue;
                    }
                    for (TImport xImport : xDoc.getDefinitions().getImportList()) {
                        String ns = xImport.getNamespace();
                        if (ns != null) {
                            List<FileInfo> fileNames = ns2FileName.get(ns);
                            if (fileNames != null) {
                                if (fileNames.size() == 1) {
                                    xImport.setLocation(fileNames.get(0).name);
                                } else {
                                    String location = xImport.getLocation();
                                    if (location != null && location.startsWith("radix:/")) {
                                        String originalSuffix = location.substring(7);
                                        for (FileInfo info : fileNames) {
                                            if (info.name.endsWith(originalSuffix)) {
                                                xImport.setLocation(info.name);
                                                break;
                                            }
                                        }
                                    } else {
                                        xImport.setLocation(fileNames.get(0).name);
                                    }
                                }
                            }
                        }
                    }
                    if (xDoc.getDefinitions().getTypesList() != null) {
                        for (TTypes types : xDoc.getDefinitions().getTypesList()) {
                            SchemaDocument xSchema = SchemaDocument.Factory.parse(types.xmlText());
                            checkSchemaImports(file, xSchema.getSchema(), ns2FileName);
                            types.set(xSchema);
                        }

                    }
                    processEnumerations(xDoc.getDefinitions().getDomNode(), enums, titles);
                    xDoc.save(file);
                } catch (XmlException ex) {
                }
            }
        }
    }

    private String getFilePath(String module, byte[] xmlBytes, Map<String, List<FileInfo>> ns2FileName, boolean fromKernel, String pathHint) throws XmlException, IOException {
        String[] parts = module.split("/");
        String prefix = parts[0] + "-" + parts[parts.length - 1] + "-";
        String targetNamespace = null;
        try {

            SchemaDocument xDoc = SchemaDocument.Factory.parse(new ByteArrayInputStream(xmlBytes));
            if (xDoc.getSchema() == null) {
                return null;
            }
            if (xDoc.getSchema().getTargetNamespace() == null) {
                return null;
            }
            targetNamespace = xDoc.getSchema().getTargetNamespace();
        } catch (XmlException e) {
            DefinitionsDocument xDoc = DefinitionsDocument.Factory.parse(new ByteArrayInputStream(xmlBytes));
            if (xDoc.getDefinitions() == null) {
                return null;
            }
            if (xDoc.getDefinitions().getTargetNamespace() == null) {
                return null;
            }
            targetNamespace = xDoc.getDefinitions().getTargetNamespace();
        }
        if (targetNamespace == null) {
            return null;
        }
        List<FileInfo> list = ns2FileName.get(targetNamespace);
        if (list == null) {
            list = new LinkedList<>();

        }

        if (!fromKernel) {
            for (FileInfo info : list) {
                if (info.fromKernel) {
                    return null;
                }
            }
        }

        parts = targetNamespace.split("/");

        String result = prefix + (pathHint == null ? parts[parts.length - 1] : pathHint);

        for (FileInfo info : list) {
            if (info.name.equals(result)) {
                return null;
            }
        }

        list.add(new FileInfo(result, fromKernel));
        ns2FileName.put(targetNamespace, list);
        return result;
    }
    
    private void consoleProcessModule(String module, boolean isKernelModule, Map<String, List<FileInfo>> ns2fileName, List<File> writtenFiles, Map<String, EnumData> enums, Map<String, Localization> titles) throws RadixSvnException, IOException {
        //look for enums
        String apiFileName = SvnPath.append(module, "api.xml");
        if (SVN.getKind(repository, apiFileName, revision) == SvnEntry.Kind.FILE) {
            try {
                String content = SVN.getFileAsStr(repository, apiFileName, revision);
                if (content != null) {
                    try {
                        APIDocument xDoc = APIDocument.Factory.parse(content);
                        APIDocument.API xApi = xDoc.getAPI();
                        if (xApi != null) {
                            for (AdsDefinitionElementType xDef : xApi.getDefinitionList()) {
                                if (xDef.isSetAdsEnumDefinition()) {
                                    EnumDefinition xEnumDef = xDef.getAdsEnumDefinition();
                                    EnumData data = enums.get(xEnumDef.getId());
                                    if (data == null) {
                                        data = new EnumData(xEnumDef.getId());
                                        enums.put(xEnumDef.getId().toString(), data);
                                        if (xEnumDef.getPublishing() != null && xEnumDef.getPublishing().getPlatformEnum() != null) {
                                            enums.put(xEnumDef.getPublishing().getPlatformEnum(), data);
                                        }
                                    }
                                    data.setEnumName(xEnumDef.getName());
                                    if (xEnumDef.getItems() != null) {
                                        for (EnumItemDefinition xItem : xEnumDef.getItems().getItemList()) {
                                            data.addItem(xItem.getId(), xItem.getName(), xItem.getValue(), xItem.getTitleId());
                                        }
                                    }
                                }
                            }
                        }
                    } catch (XmlException ex) {
                        //ignore
                    }
                }
            } catch (RadixSvnException e) {
                //ignore
            }
        }
        String binDir = SvnPath.append(module, "bin");

        if (SVN.getKind(repository, binDir, revision) == SvnEntry.Kind.DIRECTORY) {
            //localize enumerations

            List<String> jars = getTargetJarFiles(binDir, isKernelModule);
            for (String commonJar : jars) {
                if (SVN.getKind(repository, commonJar, revision) == SvnEntry.Kind.FILE) {
                    File tmp = File.createTempFile("xml", null);
                    try {
                        FileOutputStream out = new FileOutputStream(tmp);
                        try {
                            repository.getFile(commonJar, revision, null, out);
                        } finally {
                            out.close();
                        }
                        JarFile jar = new JarFile(tmp);
                        try {
                            Enumeration<JarEntry> entries = jar.entries();
                            while (entries.hasMoreElements()) {
                                JarEntry e = entries.nextElement();
                                if (!e.isDirectory()) {
                                    String name = e.getName();

                                    boolean isSchemaEntry = false;
                                    boolean isLocaleEntry = false;
                                    String pathHint = null;
                                    if (isKernelModule) {
                                        isSchemaEntry = name.startsWith(KERNEL_XSD_PATH) && (name.endsWith(".xsd") || name.endsWith(".wsdl"));
                                        if (isSchemaEntry) {
                                            String[] parts = name.split("/");
                                            pathHint = parts[parts.length - 1];
                                        }
                                    } else {
                                        if (name.endsWith(".xml")) {
                                            String[] parts = name.split("/");
                                            if (parts.length > 2) {
                                                if (parts[parts.length - 1].startsWith("xsd")) {
                                                    if (Utils.equals(parts[parts.length - 2] + ".xml", parts[parts.length - 1])) {
                                                        isSchemaEntry = true;
                                                    }
                                                } else if (parts[parts.length - 1].startsWith("mlbacs") && parts[parts.length - 1].endsWith(".xml")) {
                                                    isLocaleEntry = true;
                                                }
                                            }
                                        }
                                    }
                                    if (isSchemaEntry || isLocaleEntry) {
                                        byte[] xmlBytes = FileUtils.getZipEntryByteContent(e, jar);

                                        if (isLocaleEntry) {
                                            try {
                                                AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.parse(new ByteArrayInputStream(xmlBytes));
                                                if (xDoc.getAdsDefinition().isSetAdsLocalizingBundleDefinition()) {
                                                    LocalizingBundleDefinition xDef = xDoc.getAdsDefinition().getAdsLocalizingBundleDefinition();
                                                    String id = xDef.getId().toString().substring(3);
                                                    Localization loc = titles.get(id);
                                                    if (loc == null) {
                                                        loc = new Localization();
                                                        titles.put(id, loc);
                                                    }
                                                    for (LocalizedString str : xDef.getStringList()) {
                                                        for (LocalizedString.Value xVal : str.getValueList()) {
                                                            loc.addTranslation(str.getId(), xVal.getLanguage(), xVal.getStringValue());
                                                        }
                                                    }
                                                }
                                            } catch (XmlException ex) {
                                            }
                                            continue;
                                        }

                                        if (!outputDir.exists()) {
                                            outputDir.mkdirs();
                                        }
                                        try {
                                            String path = getFilePath(module, xmlBytes, ns2fileName, isKernelModule, pathHint);
                                            if (path == null) {
                                                continue;
                                            }
                                            File outFile = new File(outputDir, path);
                                            FileOutputStream outStream = new FileOutputStream(outFile);
                                            try {
                                                FileUtils.writeBytes(outStream, xmlBytes);
                                            } finally {
                                                outStream.close();
                                            }
                                            writtenFiles.add(outFile);

                                        } catch (XmlException | IOException ex) {
                                            continue;
                                        }
                                    }
                                }
                            }
                        } finally {
                            jar.close();
                        }

                    } finally {
                        FileUtils.deleteFile(tmp);
                    }
                }
            }
        }
    }
    
    public ManagerXmlSchemaExportDataProvider getDialogData(Set<Id> exportSchemasIds) throws RadixSvnException, IOException {
        Map<Namespace2FileName, byte[]> xmlSchemasBytes = new HashMap<>();
        Map<String, EnumData> enums = new LinkedHashMap<>();
        Map<String, Localization> titles = new LinkedHashMap<>();
        Map<String, Localization> schemasDoc = new LinkedHashMap<>();

        List<String> layers = listLayers();
        for (String layer : layers) {
            List<String> modules = listKernelModules(layer);
            for (String module : modules) {
                dialogProcessModule(module, true, xmlSchemasBytes, enums, titles, schemasDoc);
            }
            modules = listAdsModules(layer);
            for (String module : modules) {
                dialogProcessModule(module, false, xmlSchemasBytes, enums, titles, schemasDoc);
            }
        }
        
        if (exportSchemasIds == null) {
            exportSchemasIds = new HashSet<>(xsdDefs.keySet());
        } else {
            exportSchemasIds.addAll(xsdDefs.keySet());            
        }
        
        ManagerXmlSchemaExportDataProvider xmlSchemaWrapCreator = new ManagerXmlSchemaExportDataProvider(xsdDefs, xmlSchemasBytes, schemasDoc, schemaNs2ModuleName, langs, enums, titles);
        
        return xmlSchemaWrapCreator;
    }

    private void dialogProcessModule(String module, boolean isKernelModule, Map<Namespace2FileName, byte[]> xmlSchemas, Map<String, EnumData> enums, Map<String, Localization> titles, Map<String, Localization> schemasDoc) throws RadixSvnException, IOException {
        //look for enums
        String apiFileName = SvnPath.append(module, "api.xml");
        if (SVN.getKind(repository, apiFileName, revision) == SvnEntry.Kind.FILE) {
            try {
                String content = SVN.getFileAsStr(repository, apiFileName, revision);
                if (content != null) {
                    try {
                        APIDocument xDoc = APIDocument.Factory.parse(content);
                        APIDocument.API xApi = xDoc.getAPI();
                        if (xApi != null) {
                            for (AdsDefinitionElementType xDef : xApi.getDefinitionList()) {
                                if (xDef.isSetAdsEnumDefinition()) {
                                    EnumDefinition xEnumDef = xDef.getAdsEnumDefinition();
                                    EnumData data = enums.get(xEnumDef.getId().toString());
                                    if (data == null) {
                                        data = new EnumData(xEnumDef.getId());
                                        enums.put(xEnumDef.getId().toString(), data);
                                        if (xEnumDef.getPublishing() != null && xEnumDef.getPublishing().getPlatformEnum() != null) {
                                            enums.put(xEnumDef.getPublishing().getPlatformEnum(), data);
                                        }
                                    }
                                    data.setEnumName(xEnumDef.getName());
                                    if (xEnumDef.getItems() != null) {
                                        for (EnumItemDefinition xItem : xEnumDef.getItems().getItemList()) {
                                            data.addItem(xItem.getId(), xItem.getName(), xItem.getValue(), xItem.getTitleId());
                                        }
                                    }
                                }

                                if (xDef.isSetAdsXmlSchemeDefinition()) {
                                    AdsDefinitionDocument xDefDoc = AdsDefinitionDocument.Factory.newInstance();
                                    xDefDoc.addNewAdsDefinition().setAdsXmlSchemeDefinition(xDef.getAdsXmlSchemeDefinition());
                                    xsdDefs.put(xDef.getAdsXmlSchemeDefinition().getId(), xDefDoc);
                                    schemaNs2ModuleName.put(xDef.getAdsXmlSchemeDefinition().getTargetNamespace(), module);
                                }
                            }
                        }
                    } catch (XmlException ex) {
                        //ignore
                    }
                }
            } catch (RadixSvnException e) {
                //ignore
            }
        }
        String binDir = SvnPath.append(module, "bin");

        if (SVN.getKind(repository, binDir, revision) == SvnEntry.Kind.DIRECTORY) {
            //localize enumerations
            List<String> jars = getTargetJarFiles(binDir, isKernelModule);
            for (String commonJar : jars) {
                if (SVN.getKind(repository, commonJar, revision) == SvnEntry.Kind.FILE) {
                    File tmp = File.createTempFile("xml", null);
                    try {
                        FileOutputStream out = new FileOutputStream(tmp);
                        try {
                            repository.getFile(commonJar, revision, null, out);
                        } finally {
                            out.close();
                        }
                        JarFile jar = new JarFile(tmp);
                        try {
                            Enumeration<JarEntry> entries = jar.entries();
                            while (entries.hasMoreElements()) {
                                JarEntry e = entries.nextElement();
                                if (!e.isDirectory()) {
                                    String name = e.getName();

                                    boolean isSchemaEntry = false;
                                    boolean isEnumLocaleEntry = false;
                                    boolean isXsdLocaleEntry = false;
                                    String pathHint = null;
                                    if (isKernelModule) {
                                        isSchemaEntry = name.startsWith(KERNEL_XSD_PATH) && (name.endsWith(".xsd") || name.endsWith(".wsdl"));
                                        if (isSchemaEntry) {
                                            String[] parts = name.split("/");
                                            pathHint = parts[parts.length - 1];
                                        }
                                    } else {
                                        if (name.endsWith(".xml")) {
                                            String[] parts = name.split("/");
                                            if (parts.length > 2) {
                                                if (parts[parts.length - 1].startsWith("xsd")) {
                                                    if (Utils.equals(parts[parts.length - 2] + ".xml", parts[parts.length - 1])) {
                                                        isSchemaEntry = true;
                                                    }
                                                } else if (parts[parts.length - 1].startsWith("mlbacs") && parts[parts.length - 1].endsWith(".xml")) {
                                                    isEnumLocaleEntry = true;
                                                } else if (parts[parts.length - 1].startsWith("mlbxsd") && parts[parts.length - 1].endsWith(".xml")) {
                                                    isXsdLocaleEntry = true;
                                                }
                                            }
                                        }
                                    }
                                    if (isSchemaEntry || isEnumLocaleEntry || isXsdLocaleEntry) {
                                        byte[] xmlBytes = FileUtils.getZipEntryByteContent(e, jar);

                                        if (isEnumLocaleEntry || isXsdLocaleEntry) {
                                            try {
                                                AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.parse(new ByteArrayInputStream(xmlBytes));
                                                if (xDoc.getAdsDefinition().isSetAdsLocalizingBundleDefinition()) {
                                                    LocalizingBundleDefinition xDef = xDoc.getAdsDefinition().getAdsLocalizingBundleDefinition();
                                                    String id = xDef.getId().toString().substring(3);
                                                    Localization loc;
                                                    if (isEnumLocaleEntry) {
                                                        loc = titles.get(id);
                                                        if (loc == null) {
                                                            loc = new Localization();
                                                            titles.put(id, loc);
                                                        }
                                                    } else {
                                                        loc = schemasDoc.get(id);
                                                        if (loc == null) {
                                                            loc = new Localization();
                                                            schemasDoc.put(id, loc);
                                                        }
                                                    }
                                                    for (LocalizedString str : xDef.getStringList()) {
                                                        for (LocalizedString.Value xVal : str.getValueList()) {
                                                            loc.addTranslation(str.getId(), xVal.getLanguage(), xVal.getStringValue());
                                                        }
                                                    }
                                                }
                                            } catch (XmlException ex) {
                                            }
                                            continue;
                                        }

                                        String schemaNamespace = null;
                                        try {
                                            SchemaDocument xDoc = SchemaDocument.Factory.parse(new ByteArrayInputStream(xmlBytes));
                                            if (xDoc.getSchema() != null && xDoc.getSchema().getTargetNamespace() != null) {
                                                schemaNamespace = xDoc.getSchema().getTargetNamespace();
                                            } else {
                                                continue;
                                            }
                                        } catch (XmlException ex) {
                                            try {
                                                DefinitionsDocument xDoc = DefinitionsDocument.Factory.parse(new ByteArrayInputStream(xmlBytes));
                                                if (xDoc.getDefinitions() != null && xDoc.getDefinitions().getTargetNamespace() != null) {
                                                    schemaNamespace = xDoc.getDefinitions().getTargetNamespace();
                                                } else {
                                                    continue;
                                                }
                                            } catch (XmlException ex1) {
                                                Logger.getLogger(XsdExporter.class.getName()).log(Level.SEVERE, null, ex1);
                                            }
                                        }

                                        if (schemaNamespace != null && !schemaNamespace.isEmpty()) {
                                            xmlSchemas.put(new Namespace2FileName(schemaNamespace, pathHint), xmlBytes);
                                        }
                                    }
                                }
                            }
                        } finally {
                            jar.close();
                        }

                    } finally {
                        FileUtils.deleteFile(tmp);
                    }
                }
            }
        }
    }
    
    private List<String> getTargetJarFiles(final String moduleBinDir, boolean isKernelModule) throws RadixSvnException {
        if (isKernelModule) {
            final List<String> result = new LinkedList<>();
            repository.getDir(moduleBinDir, revision, new SVNRepositoryAdapter.EntryHandler() {

                @Override
                public void accept(SvnEntry svnde) throws RadixSvnException {
                    if (svnde.getKind() == SvnEntry.Kind.FILE && svnde.getName().endsWith(".jar")) {
                        result.add(SvnPath.append(moduleBinDir, svnde.getName()));
                    }
                }
            });
            return result;
        } else {
            final List<String> result = new LinkedList<>();
            repository.getDir(moduleBinDir, revision, new SVNRepositoryAdapter.EntryHandler() {

                @Override
                public void accept(SvnEntry svnde) throws RadixSvnException {
                    if (svnde.getKind() == SvnEntry.Kind.FILE && svnde.getName().endsWith(".jar") && (svnde.getName().startsWith("common") || svnde.getName().startsWith("locale"))) {
                        result.add(SvnPath.append(moduleBinDir, svnde.getName()));
                    }
                }
            });
            return result;
        }
    }   

    private List<String> listKernelModules(String layer) throws RadixSvnException {
        final List<String> candidates = new LinkedList<>();
        final String kernelPath = SvnPath.append(layer, "kernel");
        if (SVN.getKind(repository, kernelPath, revision) == SvnEntry.Kind.DIRECTORY) {
            repository.getDir(kernelPath, revision, new SVNRepositoryAdapter.EntryHandler() {

                @Override
                public void accept(SvnEntry entry) throws RadixSvnException {
                    if (entry.getKind() == SvnEntry.Kind.DIRECTORY) {
                        candidates.add(SvnPath.append(kernelPath, entry.getName()));
                    }
                }
            });
        }
        List<String> modules = new LinkedList<>();
        for (String entry : candidates) {
            String entryPath = SvnPath.append(entry, "module.xml");

            if (SVN.getKind(repository, entryPath, revision) == SvnEntry.Kind.FILE) {
                modules.add(entry);
            }
        }
        return modules;
    }
}
