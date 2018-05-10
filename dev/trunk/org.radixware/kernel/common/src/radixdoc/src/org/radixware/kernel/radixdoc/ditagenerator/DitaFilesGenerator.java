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
package org.radixware.kernel.radixdoc.ditagenerator;

import org.radixware.kernel.radixdoc.generator.GeneratorUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.conventions.RadixdocConventions;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.radixdoc.xmlexport.DefinitionDocInfo;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.RadixdocGenerationException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import static org.radixware.kernel.radixdoc.ditagenerator.DitaGeneratorUtils.documentToString;
import org.radixware.kernel.radixdoc.enums.EDitaDocumentType;
import org.radixware.kernel.radixdoc.enums.ELocalizationMapKeys;
import org.radixware.kernel.radixdoc.html.TmpFileInputStreamWrapper;
import org.radixware.kernel.radixdoc.generator.RadixdocGenerationContext;
import org.radixware.schemas.radixdoc.AbstractDocItem;
import org.radixware.schemas.radixdoc.AdsDefDocItem;
import org.radixware.schemas.radixdoc.AdsXmlSchemeDefDocItem;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class DitaFilesGenerator {
    
    private final List<Id> definitionsList;
    private final RadixdocGenerationContext context;

    private EIsoLanguage lang = EIsoLanguage.ENGLISH;
    private boolean needsGenerateRulesTopic = false;
    private boolean needsGenerateChangeLogTopic = false;

    private static final String DOCUMENT_TITLE = "Branch";
    private static final String RULES_AND_CONVENTIONS_TITLE = "rules_and_conventions";
    private static final String CHANGELOG_TITLE = "changelog";

    public DitaFilesGenerator(List<Id> definitionsList, RadixdocGenerationContext context) {
        this.definitionsList = definitionsList;
        this.context = context;
    }

    public InputStream generate(final EIsoLanguage lang, final EDitaGenerationMode generationMode, String[] generatedFileName, final String layerVersionFull) throws RadixdocGenerationException {
        this.lang = lang;
        try {
            final Map<EDefType, List<TopicInfo>> typeClusters = new HashMap<>();

            final Map<EDefType, Integer> typeDefIndexes = new HashMap<>();

            final Set<XmlObject> topicObjects = new HashSet<>();

            final Set<String> existingElements = new HashSet<>();

            final Map<String, String> xmlSchemasNamespace2PrefixMap = new HashMap<>();

            final Map<Id, Map<String, String>> module2TopicDitamap = new HashMap<>();

            final Map<String, Map<String, String>> layer2ModuleDitamap = new HashMap<>();

            final Set<Id> generatedTopicsDefIds = new HashSet<>();

            final DefinitionDitaGeneratorFactory definitionGeneratorFactory = new DefinitionDitaGeneratorFactory(existingElements, xmlSchemasNamespace2PrefixMap, generationMode);
            definitionGeneratorFactory.setChangeLogContainer(new ChangeLogDitaContainer(context));

            File tmp = File.createTempFile("ditafiles", ".zip");
            tmp.deleteOnExit();

            FileOutputStream fos = new FileOutputStream(tmp);
            try (ZipOutputStream resultZos = new ZipOutputStream(fos)) {
                
                // Generate topicInfo by defId
                for (Id defId : definitionsList) {
                    if (generatedTopicsDefIds.contains(defId)) {
                        continue;
                    } else {
                        generatedTopicsDefIds.add(defId);
                    }

                    DefinitionDocInfo docInfo = context.getDefinitionDocInfo(defId);
                    if (docInfo == null) {
                        continue;
                    }

                    AbstractDocItem item = docInfo.getDocItem();
                    if (item instanceof AdsDefDocItem) {
                        AdsDefDocItem defItem = (AdsDefDocItem) item;
                        EDefType defType = docInfo.getDocItem().getDefinitionType();

                        int definitionIndex = typeDefIndexes.isEmpty() || !typeDefIndexes.containsKey(defType) ? 1 : typeDefIndexes.get(defType);

                        XmlObject topicObject = definitionGeneratorFactory.getDefinitionDitaGenerator(docInfo, context).generate(lang, definitionIndex);

                        Map<String, String> topicName2DefName;
                        if (module2TopicDitamap.containsKey(docInfo.getModuleId())) {
                            topicName2DefName = module2TopicDitamap.get(docInfo.getModuleId());
                        } else {
                            topicName2DefName = new HashMap<>();
                            module2TopicDitamap.put(docInfo.getModuleId(), topicName2DefName);
                        }

                        topicObjects.add(topicObject);

                        String topicName = defItem.getId().toString() + ".dita";
                        topicName2DefName.put(topicName, defItem.getName());

                        // typeTopics
                        List<TopicInfo> typeTopics;
                        if (typeClusters.containsKey(defType)) {
                            typeTopics = typeClusters.get(defType);
                        } else {
                            typeTopics = new ArrayList<TopicInfo>();
                            typeClusters.put(defType, typeTopics);
                        }
                        
                        // typeTopic
                        TopicInfo typeTopic;
                        if (!docInfo.getDocItem().getIsPublished()) {
                            typeTopic = new TopicInfo(null, null, null, "Definition '" + docInfo.getQualifiedName() + "' is not published");
                        } else {
                            AdsDefDocItem docItem = (AdsDefDocItem) docInfo.getDocItem();

                            if (docItem instanceof AdsXmlSchemeDefDocItem) {
                                AdsXmlSchemeDefDocItem xmlItem = (AdsXmlSchemeDefDocItem) docItem;
                                if (!xmlItem.getNeedsDocumentation()) {
                                    // docItem не нудного формата. Пропускаем его.
                                    if (definitionsList.get(0) == defId) {
                                        if (context.getDocLogger() != null) {
                                            context.getDocLogger().put(EEventSeverity.WARNING, "Schema '" + docInfo.getQualifiedName() + "' was skipped because it shouldn't be documented");
                                        }

                                        resultZos.close();
                                        if (tmp.exists()) {
                                            FileUtils.deleteFile(tmp);
                                        }
                                        return null;
                                    } else {
                                        typeTopic = new TopicInfo(null, null, null,
                                                "Schema '" + docInfo.getQualifiedName() + "' was skipped because it shouldn't be documented");
                                    }
                                } else {
                                    typeTopic = new TopicInfo(docInfo, topicName, topicObject);
                                }
                            } else {
                                typeTopic = new TopicInfo(docInfo, topicName, topicObject);
                            }
                        }
                        typeTopics.add(typeTopic);
                        if (typeTopic.getDocItem() != null) {
                            typeDefIndexes.put(defType, definitionIndex + 1);
                        }
                    }

                    if (generationMode == EDitaGenerationMode.GENERAL_TOPIC) {
                        Map<String, String> moduleDitamap2ModuleName;
                        String layerUri = docInfo.getLayerUri();
                        if (layer2ModuleDitamap.containsKey(layerUri)) {
                            moduleDitamap2ModuleName = layer2ModuleDitamap.get(layerUri);
                        } else {
                            moduleDitamap2ModuleName = new HashMap<>();
                            layer2ModuleDitamap.put(layerUri, moduleDitamap2ModuleName);
                        }

                        Document mapDocument = generateGeneralDitaMapDocument(docInfo.getModuleName(), module2TopicDitamap.get(docInfo.getModuleId()));
                        if (mapDocument == null) {
                            continue;
                        }

                        String mapString = DitaGeneratorUtils.documentToString(mapDocument, EDitaDocumentType.MAP);
                        ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream();
                        tmpBaos.write(mapString.getBytes(StandardCharsets.UTF_8));

                        String mapName = docInfo.getLayerUri() + "-" + docInfo.getModuleId().toString() + ".ditamap";
                        moduleDitamap2ModuleName.put(mapName, docInfo.getModuleName());

                        resultZos.putNextEntry(new ZipEntry(mapName));
                        resultZos.write(tmpBaos.toByteArray());
                    }
                }

                for (XmlObject topicObject : topicObjects) {
                    XmlObject[] parts = topicObject.selectPath("$this//xref");
                    for (XmlObject part : parts) {
                        if (part.getDomNode() instanceof Element) {
                            Element xrefElement = (Element) part.getDomNode();
                            String href = xrefElement.getAttribute("href");
                            if (href != null && !href.isEmpty()) {
                                if (!existingElements.contains(href)) {
                                    String xrefValue = "Unknown";

                                    if (xrefElement.hasChildNodes()) {
                                        xrefValue = xrefElement.getFirstChild().getNodeValue();
                                    }

                                    Node xrefParent = xrefElement.getParentNode();

                                    if (xrefParent.getChildNodes().getLength() > 1) {
                                        Node prevNode = null;
                                        for (int i = 0; i < xrefParent.getChildNodes().getLength(); i++) {
                                            Node child = xrefParent.getChildNodes().item(i);
                                            if (child == xrefElement) {
                                                if (i != xrefParent.getChildNodes().getLength() - 1) {
                                                    prevNode = xrefParent.getChildNodes().item(i + 1);
                                                    break;
                                                }
                                            }
                                        }
                                        xrefParent.removeChild(xrefElement);
                                        if (prevNode != null) {
                                            xrefParent.insertBefore(xrefParent.getOwnerDocument().createTextNode(xrefValue), prevNode);
                                        } else {
                                            xrefParent.appendChild(xrefParent.getOwnerDocument().createTextNode(xrefValue));
                                        }
                                    } else {
                                        xrefParent.removeChild(xrefElement);
                                        Element paragraph = xrefParent.getOwnerDocument().createElement("lines");
                                        paragraph.appendChild(xrefParent.getOwnerDocument().createTextNode(xrefValue));
                                        xrefParent.appendChild(paragraph);
                                    }
                                }
                            }
                        }
                    }
                }

                // Generate files
                for (List<TopicInfo> topics : typeClusters.values()) {
                    for (TopicInfo topic : topics) {
                        if (topic.getTopicObject() != null) {
                            XmlObject topicObject = topic.getTopicObject();

                            ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream();
                            topicObject.save(tmpBaos);

                            resultZos.putNextEntry(new ZipEntry(topic.getTopicName()));
                            resultZos.write(tmpBaos.toByteArray());
                        }
                    }
                }

                if (!xmlSchemasNamespace2PrefixMap.isEmpty()) {
                    needsGenerateRulesTopic = true;
                    XmlObject rulesAndConventionsObject = definitionGeneratorFactory.getDefinitionDitaGenerator(null, context).generate(lang, 1);
                    ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream();
                    rulesAndConventionsObject.save(tmpBaos);

                    resultZos.putNextEntry(new ZipEntry(RULES_AND_CONVENTIONS_TITLE + ".dita"));
                    resultZos.write(tmpBaos.toByteArray());
                }

                if (definitionGeneratorFactory.getChangeLogContainer() != null && !definitionGeneratorFactory.getChangeLogContainer().isEmpty()) {
                    needsGenerateChangeLogTopic = true;
                    XmlObject changeLogObject = definitionGeneratorFactory.getChangeLogContainer().getChangeLogDitaDocument(lang);
                    ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream();
                    changeLogObject.save(tmpBaos);

                    resultZos.putNextEntry(new ZipEntry(CHANGELOG_TITLE + ".dita"));
                    resultZos.write(tmpBaos.toByteArray());
                }

                //
                String outputFileName = generatedFileName[0] == null ? "" : generatedFileName[0];
                switch (generationMode) {
                    case GENERAL_TOPIC:
                        generatedFileName[0] = appendGeneralTopicMap(resultZos, layer2ModuleDitamap, outputFileName);
                        break;
                    case TOPICS_CLUSTER:
                        generatedFileName[0] = appendClusterTopicMap(resultZos, typeClusters, lang, outputFileName, layerVersionFull);
                        break;
                }

                resultZos.flush();
                return new TmpFileInputStreamWrapper(tmp);
            } catch (ParserConfigurationException | IOException | DOMException | XmlException t) {
                Logger.getLogger(DitaFilesGenerator.class.getName()).log(Level.SEVERE, null, t);
                throw new RadixdocGenerationException("Radixdoc generation failed!", t);
            }
        } catch (IOException ex) {
            Logger.getLogger(DitaFilesGenerator.class.getName()).log(Level.SEVERE, null, ex);
            throw new RadixdocGenerationException("Radixdoc generation failed!", ex);
        }
    }

    private Document generateGeneralDitaMapDocument(String mapTitle, Map<String, String> topics) throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();

        Document document = docBuilder.newDocument();

        Element map = document.createElement("map");
        document.appendChild(map);

        Element title = document.createElement("title");
        title.appendChild(document.createTextNode(mapTitle == null ? "" : mapTitle));
        map.appendChild(title);

        Element topichead;
        if (!DOCUMENT_TITLE.equals(mapTitle)) {
            topichead = document.createElement("topichead");
            map.appendChild(topichead);
            topichead.setAttribute("navtitle", mapTitle == null ? "" : mapTitle);
        } else {
            topichead = map;
        }

        for (Entry<String, String> entry : topics.entrySet()) {
            String elementTag;

            if (entry.getKey().endsWith(".dita")) {
                elementTag = "topicref";
            } else {
                elementTag = "mapref";
            }

            Element ref = document.createElement(elementTag);
            topichead.appendChild(ref);

            ref.setAttribute("href", entry.getKey());
            ref.setAttribute("navtitle", entry.getValue());
        }

        return document;
    }

    private Document generateClusterDitaMapDocument(List<TopicInfo> defCluster, EDefType type) throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();

        Document document = docBuilder.newDocument();

        Element map = document.createElement("map");
        document.appendChild(map);

        String topLevelPrefix = type == EDefType.XML_SCHEME ? ETopLevelPrefixes.XSD_TOP_LEVEL_PREFIX : ETopLevelPrefixes.ENUM_TOP_LEVEl_PREFIX;
        String mapName = type == EDefType.XML_SCHEME ? (topLevelPrefix + " " + GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.XSD_TITLE, lang)) : (topLevelPrefix + " " + GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.ENUM_TITLE, lang));

        Element title = document.createElement("title");
        title.appendChild(document.createTextNode(mapName));
        map.appendChild(title);

        Element topichead;
        topichead = document.createElement("topichead");
        map.appendChild(topichead);
        topichead.setAttribute("navtitle", mapName);

        int index = 1;
        for (TopicInfo item : defCluster) {
            if (item == null) {
                continue;
            }

            if (item.getDocItem() == null) {
                if (context.getDocLogger() != null) {
                    context.getDocLogger().put(EEventSeverity.WARNING, item.getLogMessage());
                }
                continue;
            }

            Element ref = document.createElement("topicref");
            topichead.appendChild(ref);

            ref.setAttribute("href", item.getTopicName());
            ref.setAttribute("locktitle", "yes");

            Element topicmeta = document.createElement("topicmeta");
            ref.appendChild(topicmeta);

            Element navtitle = document.createElement("navtitle");
            topicmeta.appendChild(navtitle);

            String navtitleText;
            String titlePrefix = topLevelPrefix + (index++) + ". ";
            if (type == EDefType.XML_SCHEME) {
                String ns = ((AdsXmlSchemeDefDocItem) item.getDocItem()).getTargetNamespace();
                navtitleText = titlePrefix + item.getDocItemQualifiedName() + " (" + ns + ")";
            } else {
                navtitleText = titlePrefix + item.getDocItemQualifiedName();
            }

            Text navtitleTextNode = document.createTextNode(navtitleText);
            navtitle.appendChild(navtitleTextNode);
        }

        return document;
    }

    private String appendGeneralTopicMap(ZipOutputStream resultZos, Map<String, Map<String, String>> layer2ModuleDitamap, String outputFileName) throws XmlException, IOException, ParserConfigurationException {
        final Map<String, String> generatedLayerMaps = new HashMap<>();

        for (Entry<String, Map<String, String>> entry : layer2ModuleDitamap.entrySet()) {
            Document layerMapDocument = generateGeneralDitaMapDocument(entry.getKey(), entry.getValue());
            if (layerMapDocument == null) {
                continue;
            }

            String mapString = documentToString(layerMapDocument, EDitaDocumentType.MAP);
            ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream();
            tmpBaos.write(mapString.getBytes(StandardCharsets.UTF_8));

            String mapName = entry.getKey() + ".ditamap";
            generatedLayerMaps.put(mapName, entry.getKey());

            resultZos.putNextEntry(new ZipEntry(mapName));
            resultZos.write(tmpBaos.toByteArray());
        }

        Document branchMapDocument = generateGeneralDitaMapDocument(DOCUMENT_TITLE, generatedLayerMaps);

        String mapString = documentToString(branchMapDocument, EDitaDocumentType.MAP);
        ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream();
        tmpBaos.write(mapString.getBytes(StandardCharsets.UTF_8));

        String mapName = outputFileName.isEmpty() ? DOCUMENT_TITLE.toLowerCase() + ".ditamap" : outputFileName;

        resultZos.putNextEntry(new ZipEntry(mapName));
        resultZos.write(tmpBaos.toByteArray());

        return mapName;
    }

    private String appendClusterTopicMap(ZipOutputStream resultZos, Map<EDefType, List<TopicInfo>> typeClusters, EIsoLanguage lang, String outputFileName, String layerVersionFull) throws XmlException, IOException, ParserConfigurationException {
        if (typeClusters == null || typeClusters.isEmpty()) {
            if (context.getDocLogger() != null) {
                context.getDocLogger().put(EEventSeverity.WARNING, "It is impossible to generate documentation for selected definitions. Check that all definitions are published.");
            }
            return "";
        }

        Id firstDefId = definitionsList.get(0);

        if ("".equals(GeneratorUtils.getDefaultMapName(context, firstDefId, null))) {
            return "";
        }

        List<String> typeTopics = new ArrayList<>();
        List<TopicInfo> enumsCluster = null;
        for (Entry<EDefType, List<TopicInfo>> entry : typeClusters.entrySet()) {
            List<TopicInfo> cluster = entry.getValue();

            if (entry.getKey() == EDefType.ENUMERATION) {
                enumsCluster = new ArrayList<>(cluster);
                continue;
            }

            typeTopics.add(writeClusterToStream(resultZos, cluster, entry.getKey()));
        }

        if (enumsCluster != null) {
            typeTopics.add(writeClusterToStream(resultZos, enumsCluster, EDefType.ENUMERATION));
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();

        Document document = docBuilder.newDocument();

        Element map = document.createElement("map");
        map.setAttribute("xml:lang", lang.getValue());
        document.appendChild(map);

        String layerVersion = layerVersionFull;

        Element titleElement = document.createElement("title");
        String title = outputFileName.isEmpty() ? GeneratorUtils.getMapName(lang, context, firstDefId, layerVersion) : outputFileName;
        titleElement.appendChild(document.createTextNode(title));
        map.appendChild(titleElement);

        if (needsGenerateRulesTopic) {
            Element rulesRef = document.createElement("topicref");
            map.appendChild(rulesRef);
            rulesRef.setAttribute("href", RULES_AND_CONVENTIONS_TITLE + ".dita");
            rulesRef.setAttribute("navtitle", RULES_AND_CONVENTIONS_TITLE);
        }

        if (needsGenerateChangeLogTopic) {
            Element changeLogRef = document.createElement("topicref");
            map.appendChild(changeLogRef);
            changeLogRef.setAttribute("href", CHANGELOG_TITLE + ".dita");
            changeLogRef.setAttribute("navtitle", CHANGELOG_TITLE);
        }

        for (String topic : typeTopics) {
            String elementTag = "mapref";

            Element ref = document.createElement(elementTag);
            map.appendChild(ref);

            String navTitle;
            String topicPrefix = topic.split("_")[0];
            switch (topicPrefix) {
                case "enum":
                    navTitle = GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.ENUM_TITLE, lang);
                    break;
                case "xml":
                    navTitle = GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.XSD_TITLE, lang);
                    break;
                default:
                    navTitle = "Unknown Definitions";
            }

            ref.setAttribute("href", topic);
            ref.setAttribute("navtitle", navTitle);
        }

        String mapString = documentToString(document, EDitaDocumentType.MAP);
        ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream();
        tmpBaos.write(mapString.getBytes(StandardCharsets.UTF_8));

        String layerVersionString = !GeneratorUtils.isDefinitionHasTitle(context, firstDefId) ? "" : (" v" + RadixdocConventions.RADIXDOC_VERSION_DELIMITER + GeneratorUtils.getBaseVersion(layerVersion));
        String mapName = (outputFileName.isEmpty() ? (title + layerVersionString) : outputFileName) + ".ditamap";
        resultZos.putNextEntry(new ZipEntry(mapName));
        resultZos.write(tmpBaos.toByteArray());

        return mapName;
    }

    private String writeClusterToStream(ZipOutputStream resultZos, List<TopicInfo> cluster, EDefType type) throws IOException, ParserConfigurationException {
        if (cluster.get(0).getDocItem() == null) {
            if (context.getDocLogger() != null) {
                context.getDocLogger().put(EEventSeverity.WARNING, cluster.get(0).getLogMessage());
            }

            return "";
        }

        Document clusterMapDocument = generateClusterDitaMapDocument(cluster, type);

        String mapString = documentToString(clusterMapDocument, EDitaDocumentType.MAP);
        ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream();
        tmpBaos.write(mapString.getBytes(StandardCharsets.UTF_8));

        String typePrefix;
        switch (type) {
            case ENUMERATION:
                typePrefix = "enum_";
                break;
            case XML_SCHEME:
                typePrefix = "xml_";
                break;
            default:
                typePrefix = "unknown";
        }
        String mapName = typePrefix + definitionsList.get(0).toString() + ".ditamap";

        resultZos.putNextEntry(new ZipEntry(mapName));
        resultZos.write(tmpBaos.toByteArray());

        return mapName;
    }

    private static class TopicInfo {

        final DefinitionDocInfo item;
        final String topicName;
        final XmlObject topicObject;
        final String logMessage;

        public TopicInfo(DefinitionDocInfo item, String topicName, XmlObject topicObject) {
            this.item = item;
            this.topicName = topicName;
            this.topicObject = topicObject;
            this.logMessage = "";
        }

        public TopicInfo(DefinitionDocInfo item, String topicName, XmlObject topicObject, String logMessage) {
            this.item = item;
            this.topicName = topicName;
            this.topicObject = topicObject;
            this.logMessage = logMessage;
        }

        public AdsDefDocItem getDocItem() {
            if (item != null && item.getDocItem() instanceof AdsDefDocItem) {
                return (AdsDefDocItem) item.getDocItem();
            } else {
                return null;
            }
        }

        public String getDocItemName() {
            if (item != null && item.getDocItem() != null) {
                return item.getDocItem().getName() == null ? "" : item.getDocItem().getName();
            }
            return "";
        }

        public String getDocItemQualifiedName() {
            if (item != null) {
                return item.getQualifiedName(true) == null ? "" : item.getQualifiedName(true);
            }
            return "";
        }

        public String getTopicName() {
            return topicName;
        }

        public XmlObject getTopicObject() {
            return topicObject;
        }

        public String getLogMessage() {
            return logMessage;
        }
    }

}
