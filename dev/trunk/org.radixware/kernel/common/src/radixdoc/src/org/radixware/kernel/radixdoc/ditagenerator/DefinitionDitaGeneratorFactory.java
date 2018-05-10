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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.namespace.QName;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.xmlbeans.XmlDocumentProperties;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.conventions.RadixdocConventions;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.XPathUtils;
import org.radixware.kernel.radixdoc.enums.ELocalizationMapKeys;
import org.radixware.kernel.radixdoc.xmlexport.DefinitionDocInfo;
import org.radixware.kernel.radixdoc.generator.RadixdocGenerationContext;
import org.radixware.schemas.radixdoc.XmlItemDocEntry;
import org.radixware.schemas.radixdoc.AdsEnumDefDocItem;
import org.radixware.schemas.radixdoc.AdsMapDefDocItem;
import org.radixware.schemas.radixdoc.AdsTopicDefDocItem;
import org.radixware.schemas.radixdoc.AdsXmlSchemeDefDocItem;
import org.radixware.schemas.radixdoc.EnumItemDocEntry;
import org.radixware.schemas.radixdoc.XmlItemDocEntries;
import org.radixware.schemas.radixdoc.XmlItemType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DefinitionDitaGeneratorFactory {

    private final static String RADIX_TYPES_SCHEMA_URI = "http://schemas.radixware.org/types.xsd";

    private final Set<String> existingElements;
    private final Map<String, String> xmlSchemafPrefix2NamespaceMap;
    private final EDitaGenerationMode generationMode;
    private ChangeLogDitaContainer changeLogContainer = null;

    public DefinitionDitaGeneratorFactory(final Set<String> existingElements, final Map<String, String> xmlSchemafPrefix2NamespaceMap, final EDitaGenerationMode generationMode) {
        this.existingElements = existingElements;
        this.xmlSchemafPrefix2NamespaceMap = xmlSchemafPrefix2NamespaceMap;
        this.generationMode = generationMode;
    }

    public ChangeLogDitaContainer getChangeLogContainer() {
        return changeLogContainer;
    }

    public void setChangeLogContainer(ChangeLogDitaContainer changeLogContainer) {
        this.changeLogContainer = changeLogContainer;
    }

    public IDefinitionDitaGenerator getDefinitionDitaGenerator(final DefinitionDocInfo docInfo, final RadixdocGenerationContext context) {
        if (docInfo == null) {
            return new RulesAndConventionsDitaGenerator(context, xmlSchemafPrefix2NamespaceMap, generationMode);
        }

        switch (docInfo.getDocItem().getDefinitionType()) {
            case XML_SCHEME:
                return new XmlDefinitionDitaGenerator(docInfo, context, generationMode, existingElements, xmlSchemafPrefix2NamespaceMap, changeLogContainer);
            case ENUMERATION:
                return new EnumDefinitionDitaGenerator(docInfo, context, generationMode, existingElements);
            default:
                return new BaseDefinitionDitaGenerator(docInfo, context, generationMode) {

                    @Override
                    public XmlObject generate(final EIsoLanguage lang, final Integer definitionIndex) {
                        return null;
                    }
                };
        }
    }

    private static abstract class BaseDefinitionDitaGenerator implements IDefinitionDitaGenerator {

        protected static final String[] TOPIC_DECLARATION_PARTS = {"topic", "-//OASIS//DTD DITA Topic//EN", "topic.dtd"};
        protected static final String[] CHAPTER_DECLARATION_PARTS = {"chapter", "urn:pubid:dita4publishers.org:doctypes:dita:chapter", "urn:pubid:dita4publishers.org:doctypes:dita:chapter"};

        protected static final String EMPTY_DESCRIPTION_PLACEHOLDER_ENV = "RDX_DOC_EMPTY_DESC_PLACEHOLDER";
        protected static final String EMPTY_DESCRIPTION_PLACEHOLDER = System.getenv(EMPTY_DESCRIPTION_PLACEHOLDER_ENV) == null ? "" : System.getenv(EMPTY_DESCRIPTION_PLACEHOLDER_ENV);

        protected final DefinitionDocInfo docInfo;
        protected final RadixdocGenerationContext context;
        protected final EDitaGenerationMode generationMode;

        public BaseDefinitionDitaGenerator(final DefinitionDocInfo docInfo, final RadixdocGenerationContext context, final EDitaGenerationMode generationMode) {
            this.docInfo = docInfo;
            this.context = context;
            this.generationMode = generationMode;
        }

    }

    private static class XmlDefinitionDitaGenerator extends BaseDefinitionDitaGenerator {

        private static final int MAX_TITLE_PREFIX_LENGTH = 3;
        private static final int LEVELS_LIMIT = 25;

        private final ChangeLogDitaContainer changeLogContainer;
        private final Set<String> existingElements;

        public final Map<String, String> xmlSchemafPrefix2NamespaceMap;

        public XmlDefinitionDitaGenerator(final DefinitionDocInfo docInfo, final RadixdocGenerationContext context, final EDitaGenerationMode generationMode, final Set<String> existingElements, final Map<String, String> xmlSchemafPrefix2NamespaceMap, final ChangeLogDitaContainer changeLogContainer) {
            super(docInfo, context, generationMode);
            this.existingElements = existingElements;
            this.xmlSchemafPrefix2NamespaceMap = xmlSchemafPrefix2NamespaceMap;
            this.changeLogContainer = changeLogContainer;
        }

        private Map<String, String> getPathPartsLocalization(EIsoLanguage lang) {
            Map<String, String> pathPartsLocalization = new HashMap<>();
            pathPartsLocalization.put("Element", GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.PATH_PART_ELEM, lang));
            pathPartsLocalization.put("Type", GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.PATH_PART_TYPE, lang));
            pathPartsLocalization.put("Attribute Group", GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.PATH_PART_ATTR_GROUP, lang));
            pathPartsLocalization.put("Attribute", GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.PATH_PART_ATTR, lang));

            return pathPartsLocalization;
        }

        private String transformXPathIntoElementId(String humanReadableXPath) {
            String[] parts = humanReadableXPath.split("/");
            StringBuilder result = new StringBuilder();

            for (int i = 0; i < parts.length; i++) {
                String clearPart = parts[i].trim().replace("'", "").replace(" ", "_").toLowerCase();
                result.append(clearPart);
                if (i < parts.length - 1) {
                    result.append("_");
                }
            }

            return result.toString();
        }

        private void addSchemaRow(final Document document, final Element tableBody, final AdsXmlSchemeDefDocItem xmlItem, final EIsoLanguage lang) {
            Element row = document.createElement("row");
            tableBody.appendChild(row);

            String firstColumnString = GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.SCHEMA, lang);
            row.appendChild(DitaGeneratorUtils.createTableEntry(document, firstColumnString + ":"));
            {
                Element refEntry = document.createElement("entry");
                row.appendChild(refEntry);

                refEntry.setAttribute("valign", "middle");

                String strId = xmlItem.getId().toString();

                String xrefText = "'" + xmlItem.getName() + "'(" + xmlItem.getTargetNamespace() + ")";
                String xrefHref = strId + ".dita#" + strId;
                Element xref = DitaGeneratorUtils.createXRef(document, xrefText, xrefHref);
                refEntry.appendChild(xref);
            }
        }

        private void addOwnerRow(final Document document, final Element tableBody, final AdsXmlSchemeDefDocItem xmlItem, final String xpath, final EIsoLanguage lang) {
            Element row = document.createElement("row");
            tableBody.appendChild(row);

            Map<String, String> pathPartsLocalization = getPathPartsLocalization(lang);

            String humanReadableXpath = XPathUtils.getHumanReadableXPath(xpath, null);
            String localizedHumanReadableXpath = XPathUtils.getHumanReadableXPath(xpath, pathPartsLocalization);
            String strId = xmlItem.getId().toString();

            row.appendChild(DitaGeneratorUtils.createTableEntry(document, GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.OWNER, lang) + ":"));

            String owner = humanReadableXpath.replace(XPathUtils.getLastHumanReadableXPathPart(xpath, null), "").trim();
            owner = owner.endsWith("/") ? owner.substring(0, owner.length() - 1).trim() : owner;

            String localizedOwner = localizedHumanReadableXpath.replace(XPathUtils.getLastHumanReadableXPathPart(xpath, pathPartsLocalization), "").trim();
            localizedOwner = localizedOwner.endsWith("/") ? localizedOwner.substring(0, localizedOwner.length() - 1).trim() : localizedOwner;
            {
                Element refEntry = document.createElement("entry");
                row.appendChild(refEntry);

                refEntry.setAttribute("valign", "middle");

                String ownerId = transformXPathIntoElementId(owner);
                String xrefText = GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.SCHEMA, lang) + " '"
                        + xmlItem.getName() + "'"
                        + (Utils.emptyOrNull(localizedOwner) ? "" : " / " + localizedOwner);
                String xrefHref = strId + ".dita#" + strId + (Utils.emptyOrNull(ownerId) ? "" : "_") + ownerId;
                Element xref = DitaGeneratorUtils.createXRef(document, xrefText, xrefHref);
                refEntry.appendChild(xref);
            }
        }

        private void addContainerTypeRow(final Document document, final Element tableBody, final String xpath, final EIsoLanguage lang) {
            String container = XPathUtils.getElementContainer(xpath);
            if (!"schema".equals(container)) {
                Element row = document.createElement("row");
                tableBody.appendChild(row);

                row.appendChild(DitaGeneratorUtils.createTableEntry(document, GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.CONTAINER_TYPE, lang) + ":"));
                row.appendChild(DitaGeneratorUtils.createTableEntry(document, container));
            }
        }

        private void addTypeRow(final Document document, final Element tableBody, final XmlItemType type, final EIsoLanguage lang) {
            Element row = document.createElement("row");
            tableBody.appendChild(row);

            row.appendChild(DitaGeneratorUtils.createTableEntry(document, GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.TYPE, lang) + ":"));

            if (type == null) {
                row.appendChild(DitaGeneratorUtils.createTableEntry(document, "unknown"));
                return;
            } else if (type.getId() == null) {
                String value = type.getValue() == null ? "unknown" : type.getValue();
                if ("complexType".equals(value)) {
                    value = GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.COMPLEX_TYPE, lang);
                }
                value = value.
                        replace(RadixdocConventions.RADIXDOC_2_BEGIN_XREF_MARK, "").
                        replace(RadixdocConventions.RADIXDOC_2_END_XREF_MARK, "").
                        replace(RadixdocConventions.RADIXDOC_2_NEW_LINE_MARK, "\n");

                Matcher matcher = Pattern.compile("\\{mls\\w*\\}").matcher(value);
                while (matcher.find(0)) {
                    String strMlsId = value.substring(matcher.start() + 1, matcher.end() - 1);
                    Id mlsId = Id.Factory.loadFrom(strMlsId);
                    String partLocalization = GeneratorUtils.getLocalizedStringValueByModulePath(
                            context,
                            RadixdocConventions.LOCALIZING_MODULE_PATH,
                            RadixdocConventions.LOCALIZING_BUNDLE_ID,
                            mlsId, lang
                    );
                    value = value.replaceFirst("\\{mls\\w*\\}", partLocalization);
                    matcher = Pattern.compile("\\{mls\\w*\\}").matcher(value);
                }

                row.appendChild(DitaGeneratorUtils.createTableEntry(document, value));
                return;
            }

            String typeId = type.getId().toString();
            String typeName = type.getValue();

            String typeNs = type.getNamespace();
            String typePrefix = "";
            if (type.getPrefix() != null) {
                typeNs = null;
                typePrefix = type.getPrefix();

                int prefixCounter = 1;
                if (xmlSchemafPrefix2NamespaceMap.containsKey(typePrefix)
                        && xmlSchemafPrefix2NamespaceMap.get(typePrefix) != null
                        && !xmlSchemafPrefix2NamespaceMap.get(typePrefix).equals(type.getNamespace())) {
                    while (xmlSchemafPrefix2NamespaceMap.containsKey(typePrefix)) {
                        typePrefix = type.getPrefix() + "_" + prefixCounter;
                        prefixCounter++;
                    }
                }

                xmlSchemafPrefix2NamespaceMap.put(typePrefix, type.getNamespace());
            }

            QName typeQName = new QName(typeNs, (typePrefix.isEmpty() ? "" : (typePrefix + ":")) + type.getValue());

            String typeValue = type.getValue();
            boolean typeIsEnum = false;
            if (type.getNamespace().startsWith("enum_")) {
                typeValue = GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.ENUMERATION, lang) + " '" + type.getValue() + "'";
                typeIsEnum = true;
            } else {
                Matcher matcher = Pattern.compile("\\{mls\\w*\\}").matcher(typeValue);
                while (matcher.find(0)) {
                    String strMlsId = typeValue.substring(matcher.start() + 1, matcher.end() - 1);
                    Id mlsId = Id.Factory.loadFrom(strMlsId);
                    String partLocalization = GeneratorUtils.getLocalizedStringValueByModulePath(
                            context,
                            RadixdocConventions.LOCALIZING_MODULE_PATH,
                            RadixdocConventions.LOCALIZING_BUNDLE_ID,
                            mlsId, lang
                    );
                    typeValue = typeValue.replaceFirst("\\{mls\\w*\\}", partLocalization);
                    matcher = Pattern.compile("\\{mls\\w*\\}").matcher(typeValue);
                }
            }

            typeValue = typeValue.replace(RadixdocConventions.RADIXDOC_2_NEW_LINE_MARK, "\n");
            Element typeEntry = document.createElement("entry");
            row.appendChild(typeEntry);

            typeEntry.setAttribute("valign", "middle");

            if (typeValue.contains(RadixdocConventions.RADIXDOC_2_BEGIN_XREF_MARK) && typeValue.contains(RadixdocConventions.RADIXDOC_2_END_XREF_MARK)) {
                int xrefBegin = typeValue.indexOf(RadixdocConventions.RADIXDOC_2_BEGIN_XREF_MARK);
                int xrefEnd = typeValue.indexOf(RadixdocConventions.RADIXDOC_2_END_XREF_MARK);

                String firstTextPart = typeValue.substring(0, xrefBegin);
                String actualTypeValue = typeValue.substring((xrefBegin + RadixdocConventions.RADIXDOC_2_BEGIN_XREF_MARK.length()), xrefEnd);
                QName refQName = new QName(typeNs, (typePrefix.isEmpty() ? "" : (typePrefix + ":")) + actualTypeValue);
                String refText = StringEscapeUtils.escapeXml(refQName.toString());
                String secondTextPart = typeValue.substring((xrefEnd + RadixdocConventions.RADIXDOC_2_END_XREF_MARK.length()), typeValue.length());
                String xrefHref = typeId + ".dita#" + typeId + (typeIsEnum ? "" : ("_type_" + actualTypeValue.toLowerCase()));

                typeEntry.appendChild(DitaGeneratorUtils.createLinesWithXref(document, firstTextPart, secondTextPart, refText, xrefHref));

                return;
            } else {
                typeValue = typeIsEnum ? typeValue : StringEscapeUtils.escapeXml(typeQName.toString());
            }

            String xrefHref = typeId + ".dita#" + typeId + (typeIsEnum ? "" : ("_type_" + typeName.toLowerCase()));
            Element xref = DitaGeneratorUtils.createXRef(document, typeValue, xrefHref);
            typeEntry.appendChild(xref);
        }

        private void addMandatoryRow(final Document document, final Element tableBody, final String mandatory, final EIsoLanguage lang) {
            if (mandatory != null && !mandatory.isEmpty()) {
                Element row = document.createElement("row");
                tableBody.appendChild(row);

                String localizedMandatory = new String(mandatory);
                Matcher matcher = Pattern.compile("\\{mls\\w*\\}").matcher(localizedMandatory);
                while (matcher.find(0)) {
                    String strMlsId = localizedMandatory.substring(matcher.start() + 1, matcher.end() - 1);
                    Id mlsId = Id.Factory.loadFrom(strMlsId);
                    String partLocalization = GeneratorUtils.getLocalizedStringValueByModulePath(
                            context,
                            RadixdocConventions.LOCALIZING_MODULE_PATH,
                            RadixdocConventions.LOCALIZING_BUNDLE_ID,
                            mlsId, lang
                    );
                    localizedMandatory = localizedMandatory.replaceFirst("\\{mls\\w*\\}", partLocalization);
                    matcher = Pattern.compile("\\{mls\\w*\\}").matcher(localizedMandatory);
                }

                String firstColumnString;
                if (localizedMandatory.contains("..")) {
                    firstColumnString = GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.OCCURRENCES, lang);
                } else {
                    firstColumnString = GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.MANDATORY, lang);
                }
                row.appendChild(DitaGeneratorUtils.createTableEntry(document, firstColumnString + ":"));
                row.appendChild(DitaGeneratorUtils.createTableEntry(document, localizedMandatory));
            }
        }

        private void addDescriptionRow(final Document document, final Element tableBody, final Id schemaId, final Id descriptionId, final EIsoLanguage lang) {
            Element row = document.createElement("row");
            tableBody.appendChild(row);

            String elementDescription = GeneratorUtils.getLocalizedStringValueById(context, schemaId, descriptionId, lang);
            elementDescription = elementDescription.isEmpty() ? EMPTY_DESCRIPTION_PLACEHOLDER : elementDescription;

            row.appendChild(DitaGeneratorUtils.createTableEntry(document, GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.DESCRIPTION, lang) + ":"));
            row.appendChild(DitaGeneratorUtils.createTableEntry(document, elementDescription == null ? "" : elementDescription));
        }

        private void addSinceVersionRow(final Document document, final Element tableBody, final String sinceVersion, final EIsoLanguage lang) {
            if (sinceVersion != null && !sinceVersion.isEmpty()) {
                Element row = document.createElement("row");
                tableBody.appendChild(row);

                row.appendChild(DitaGeneratorUtils.createTableEntry(document, GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.SINCE_VERSION, lang) + ":"));
                row.appendChild(DitaGeneratorUtils.createTableEntry(document, sinceVersion));
            }
        }

        private void appendXmlItemsDocEntries(final Document document, final Element container, final XmlItemDocEntries xmlItemDocEntries, final AdsXmlSchemeDefDocItem xmlItem, final EIsoLanguage lang, final String parentTitlePrefix) {
            List<XmlItemDocEntry> entries = xmlItemDocEntries.getXmlItemDocEntryList();
            for (XmlItemDocEntry entry : entries) {
                Element subsection = document.createElement("subsection");
                container.appendChild(subsection);

                String sectionId = xmlItem.getId().toString() + "_" + transformXPathIntoElementId(XPathUtils.getHumanReadableXPath(entry.getNodeXPath()));
                subsection.setAttribute("id", sectionId);
                existingElements.add(xmlItem.getId().toString() + ".dita#" + sectionId);

                String ownTitlePrefix = parentTitlePrefix + (entries.indexOf(entry) + 1) + ".";
                subsection.appendChild(DitaGeneratorUtils.createElement(document, "title", ownTitlePrefix + " " + XPathUtils.getLastHumanReadableXPathPart(entry.getNodeXPath(), getPathPartsLocalization(lang))));

                Element body = document.createElement("body");
                subsection.appendChild(body);

                int level = parentTitlePrefix.split("\\.").length;
                Element p = createXmlItemDocEntryParagraphElement(document, entry, xmlItem, lang, level);
                if (p != null) {
                    body.appendChild(p);
                }

                if (entry.getAttributes() != null) {
                    for (XmlItemDocEntry attribute : entry.getAttributes().getXmlItemDocEntryList()) {
                        body.appendChild(createXmlItemDocEntryParagraphElement(document, attribute, xmlItem, lang, level));
                    }
                }

                if (entry.getSubElements() != null) {
                    if (level < MAX_TITLE_PREFIX_LENGTH) {
                        appendXmlItemsDocEntries(document, subsection, entry.getSubElements(), xmlItem, lang, ownTitlePrefix);
                    } else if (level <= LEVELS_LIMIT) {
                        appendXmlItemsDocEntries(document, container, entry.getSubElements(), xmlItem, lang, ownTitlePrefix);
                    }
                }
            }
        }

        private Element createXmlItemDocEntryParagraphElement(Document document, XmlItemDocEntry entry, AdsXmlSchemeDefDocItem xmlItem, EIsoLanguage lang, int level) {
            Element p = document.createElement("p");

            Id schemaId = xmlItem.getId();
            String xpath = entry.getNodeXPath();

            String elementTitleParts[] = XPathUtils.getLastHumanReadableXPathPart(xpath, null).split(" '");
            String xsdType = elementTitleParts == null ? "" : elementTitleParts[0].trim().toLowerCase();

            if (xsdType.contains("schema")) {
                return null;
            }

            Element elementTable = document.createElement("table");
            p.appendChild(elementTable);

            elementTable.setAttribute("rowsep", "1");
            elementTable.setAttribute("colsep", "1");
            elementTable.setAttribute("frame", "all");

            if ("attribute".equals(xsdType)) {
                Element desc = document.createElement("desc");
                elementTable.appendChild(desc);

                desc.appendChild(DitaGeneratorUtils.createElement(document, "b", XPathUtils.getLastHumanReadableXPathPart(entry.getNodeXPath(), getPathPartsLocalization(lang))));
            }

            int elementTableCols = 2;
            String[] columnsWidthPercent = {"20%", "80%"};

            Element elemTGroup = document.createElement("tgroup");
            elementTable.appendChild(elemTGroup);
            elemTGroup.setAttribute("cols", String.valueOf(elementTableCols));

            for (int i = 0; i < elementTableCols; i++) {
                Element colspec = document.createElement("colspec");
                elemTGroup.appendChild(colspec);

                colspec.setAttribute("colname", "COLSPEC" + i);
                colspec.setAttribute("align", "left");
                colspec.setAttribute("colwidth", columnsWidthPercent[i]);
            }

            Element elemTBody = document.createElement("tbody");
            elemTGroup.appendChild(elemTBody);
            elemTBody.setAttribute("valign", "middle");

            if (!"attribute".equals(xsdType)) {
                addSchemaRow(document, elemTBody, xmlItem, lang);
            }

            addOwnerRow(document, elemTBody, xmlItem, xpath, lang);

            if (!"type".equals(xsdType) && !xsdType.contains("attribute")) {
                addContainerTypeRow(document, elemTBody, xpath, lang);
            }

            if (!"attribute group".equals(xsdType)) {
                XmlItemType type = entry.getXmlItemType();
                if ("type".equals(xsdType)) {
                    if (type != null && type.getNamespace() != null && (type.getNamespace().startsWith("enum_") || XPathUtils.isSimpleType(xpath))) {
                        addTypeRow(document, elemTBody, type, lang);
                    }
                } else {
                    addTypeRow(document, elemTBody, type, lang);
                }
            }

            if (XPathUtils.getHumanReadableXPath(xpath).split("/").length > 1) {
                addMandatoryRow(document, elemTBody, entry.getMandatory(), lang);
            }

            Id definitionId = schemaId;
            Id descriptionId = entry.getLocalizedStringId();
            if (entry.getXmlItemType() != null && entry.getXmlItemType().getNamespace() != null) {
                if (descriptionId == null && entry.getXmlItemType().getNamespace().startsWith("enum_")) {
                    definitionId = Id.Factory.loadFrom(entry.getXmlItemType().getNamespace().replace("enum_", ""));
                    if (context.getDefinitionDocInfo(definitionId) != null && context.getDefinitionDocInfo(definitionId).getDocItem() != null) {
                        descriptionId = context.getDefinitionDocInfo(definitionId).getDocItem().getDescriptionId();
                    }
                }
            }

            addDescriptionRow(document, elemTBody, definitionId, descriptionId, lang);
            addSinceVersionRow(document, elemTBody, entry.getSinceVersion(), lang);

            return p;
        }

        @Override
        public XmlObject generate(final EIsoLanguage lang, final Integer definitionIndex) {
            final AdsXmlSchemeDefDocItem xmlItem = (AdsXmlSchemeDefDocItem) docInfo.getDocItem();

            XmlObject resultObject = XmlObject.Factory.newInstance();

            XmlDocumentProperties props = resultObject.documentProperties();
            props.setDoctypeName(CHAPTER_DECLARATION_PARTS[0]);
            props.setDoctypePublicId(CHAPTER_DECLARATION_PARTS[1]);
            props.setDoctypeSystemId(CHAPTER_DECLARATION_PARTS[2]);

            Document document = resultObject.getDomNode().getOwnerDocument();

            Element chapter = document.createElement("chapter");
            resultObject.getDomNode().appendChild(chapter);
            chapter.setAttribute("id", xmlItem.getId().toString());
            existingElements.add(xmlItem.getId().toString() + ".dita#" + xmlItem.getId().toString());

            String titlePrefix = generationMode == EDitaGenerationMode.GENERAL_TOPIC ? "" : ETopLevelPrefixes.XSD_TOP_LEVEL_PREFIX + definitionIndex + ".";
            String schemaTitle = GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.SCHEMA_TITLE, lang);

            chapter.appendChild(DitaGeneratorUtils.createElement(document, "title",
                    titlePrefix + " " + schemaTitle + " '"
                    + docInfo.getQualifiedName(true)
                    + "' (" + xmlItem.getTargetNamespace() + ")"
            ));

            Element body = document.createElement("body");
            chapter.appendChild(body);

            //----------------------TARGET NAMESPACE SECTION--------------------
            {
                Element targetNsSection = document.createElement("section");
                body.appendChild(targetNsSection);

                targetNsSection.setAttribute("id", "target_namespace_" + lang.getValue());
                Element title = document.createElement("title");
                targetNsSection.appendChild(title);
                title.appendChild(DitaGeneratorUtils.createElement(document, "b", GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.TARGET_NAMESPACE, lang)));

                targetNsSection.appendChild(DitaGeneratorUtils.createElement(document, "p", xmlItem.getTargetNamespace()));
            }

            //-----------------------OVERVIEW SECTION--------------------
            String description = GeneratorUtils.getLocalizedStringValueById(context, xmlItem.getId(), xmlItem.getDescriptionId(), lang);
            description = description.isEmpty() ? EMPTY_DESCRIPTION_PLACEHOLDER : description;
            if (description != null) {
                Element overviewSection = document.createElement("section");
                body.appendChild(overviewSection);

                overviewSection.setAttribute("id", "overview_" + lang.getValue());

                Element title = document.createElement("title");
                overviewSection.appendChild(title);
                title.appendChild(DitaGeneratorUtils.createElement(document, "b", GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.OVERVIEW, lang)));

                overviewSection.appendChild(DitaGeneratorUtils.createElement(document, "p", description));
            }

            //-----------------------CHANGELOG SECTION--------------------
            this.changeLogContainer.addSchemaChangeLog(docInfo, xmlItem.getXmlChangeLog());

            //-----------------------ELEMENTS SECTION--------------------
            if (xmlItem.getXmlItemDocEntries() != null && !xmlItem.getXmlItemDocEntries().getXmlItemDocEntryList().isEmpty()) {
                String parentTiltlePrefix = generationMode == EDitaGenerationMode.GENERAL_TOPIC ? "" : ETopLevelPrefixes.XSD_TOP_LEVEL_PREFIX + definitionIndex + ".";
                appendXmlItemsDocEntries(document, chapter, xmlItem.getXmlItemDocEntries(), xmlItem, lang, parentTiltlePrefix);
            }

            return resultObject;
        }
    }

    private static class EnumDefinitionDitaGenerator extends BaseDefinitionDitaGenerator {

        private final Set<String> existingElements;

        public EnumDefinitionDitaGenerator(final DefinitionDocInfo docInfo, final RadixdocGenerationContext context, final EDitaGenerationMode generationMode, final Set<String> existingElements) {
            super(docInfo, context, generationMode);
            this.existingElements = existingElements;
        }

        @Override
        public XmlObject generate(final EIsoLanguage lang, final Integer definitionIndex) {
            AdsEnumDefDocItem docItem = (AdsEnumDefDocItem) docInfo.getDocItem();

            XmlObject resultObject = XmlObject.Factory.newInstance();

            XmlDocumentProperties props = resultObject.documentProperties();
            props.setDoctypeName(TOPIC_DECLARATION_PARTS[0]);
            props.setDoctypePublicId(TOPIC_DECLARATION_PARTS[1]);
            props.setDoctypeSystemId(TOPIC_DECLARATION_PARTS[2]);

            Document document = resultObject.getDomNode().getOwnerDocument();

            Element topic = document.createElement("topic");
            resultObject.getDomNode().appendChild(topic);

            String strId = docItem.getId().toString();
            topic.setAttribute("id", strId);
            existingElements.add(strId + ".dita#" + strId);

            String titlePrefix = generationMode == EDitaGenerationMode.GENERAL_TOPIC ? "" : ETopLevelPrefixes.ENUM_TOP_LEVEl_PREFIX + definitionIndex + ".";
            String enumTitle = GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.ENUMERATION, lang);
            topic.appendChild(DitaGeneratorUtils.createElement(document, "title", titlePrefix + " " + enumTitle + " '" + docInfo.getQualifiedName(true) + "'"));

            Element body = document.createElement("body");
            topic.appendChild(body);

            //-----------------------VALUE TYPE SECTION--------------------
            {
                Element valTypeSection = document.createElement("section");
                body.appendChild(valTypeSection);

                valTypeSection.setAttribute("id", "valType_" + lang.getValue());

                Element title = document.createElement("title");
                valTypeSection.appendChild(title);
                title.appendChild(DitaGeneratorUtils.createElement(document, "b", GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.VALUE_TYPE, lang)));

                valTypeSection.appendChild(DitaGeneratorUtils.createElement(document, "p", docItem.getItemsType()));
            }

            //-----------------------OVERVIEW SECTION--------------------
            Id descriptionId = docItem.getDescriptionId();
            String description = GeneratorUtils.getLocalizedStringValueById(context, docItem.getId(), descriptionId, lang);
            description = description.isEmpty() ? EMPTY_DESCRIPTION_PLACEHOLDER : description;
            if (description != null && !"".equals(description)) {
                Element overviewSection = document.createElement("section");
                body.appendChild(overviewSection);

                overviewSection.setAttribute("id", "overview_" + lang.getValue());

                Element title = document.createElement("title");
                overviewSection.appendChild(title);
                title.appendChild(DitaGeneratorUtils.createElement(document, "b", GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.OVERVIEW, lang)));

                overviewSection.appendChild(DitaGeneratorUtils.createElement(document, "p", description));
            }

            //-----------------------ELEMENTS SECTION--------------------
            if (docItem.getEnumItemsDocEntries() != null) {
                Element elementsSection = document.createElement("section");
                body.appendChild(elementsSection);

                elementsSection.setAttribute("id", "elements_" + lang.getValue());

                Element titleElem = document.createElement("title");
                elementsSection.appendChild(titleElem);
                titleElem.appendChild(DitaGeneratorUtils.createElement(document, "b", GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.ITEMS, lang)));

                Element paragraph = document.createElement("p");
                elementsSection.appendChild(paragraph);

                Element elementsTable = document.createElement("table");
                paragraph.appendChild(elementsTable);

                elementsTable.setAttribute("rowsep", "1");
                elementsTable.setAttribute("colsep", "1");
                elementsTable.setAttribute("frame", "all");

                int elementsTableCols = 2;
                Element tgroup = document.createElement("tgroup");
                elementsTable.appendChild(tgroup);

                tgroup.setAttribute("cols", String.valueOf(elementsTableCols));
                for (int i = 0; i < elementsTableCols; i++) {
                    Element colspec = document.createElement("colspec");
                    tgroup.appendChild(colspec);

                    colspec.setAttribute("colname", "COLSPEC" + i);
                    colspec.setAttribute("align", "left");
                }

                Element thead = document.createElement("thead");
                tgroup.appendChild(thead);

                Element theadRow = document.createElement("row");
                thead.appendChild(theadRow);

                theadRow.appendChild(DitaGeneratorUtils.createTableTitleEntry(document, GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.VALUE, lang)));
                theadRow.appendChild(DitaGeneratorUtils.createTableTitleEntry(document, GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.NAME, lang)));

                Element tbody = document.createElement("tbody");
                tgroup.appendChild(tbody);
                tbody.setAttribute("valign", "middle");

                for (EnumItemDocEntry entry : docItem.getEnumItemsDocEntries().getEnumItemDocEntryList()) {
                    Element row = document.createElement("row");
                    tbody.appendChild(row);

                    row.appendChild(DitaGeneratorUtils.createTableEntry(document, entry.getValue()));

                    String itemDesc = GeneratorUtils.getLocalizedStringValueById(context, docItem.getId(), entry.getDescriptionId(), lang);
                    itemDesc = itemDesc.isEmpty() ? GeneratorUtils.getLocalizedStringValueById(context, docItem.getId(), entry.getTitleId(), lang) : itemDesc;

                    itemDesc = itemDesc.isEmpty() ? EMPTY_DESCRIPTION_PLACEHOLDER : itemDesc;
                    if (!itemDesc.isEmpty()) {
                        itemDesc += "\n";
                    }

                    row.appendChild(DitaGeneratorUtils.createTableEntry(document, itemDesc + "(" + entry.getName() + ")"));
                }
            }

            return resultObject;
        }
    }

    private static class RulesAndConventionsDitaGenerator extends BaseDefinitionDitaGenerator {

        private final Map<String, String> xmlSchemafPrefix2NamespaceMap;

        public RulesAndConventionsDitaGenerator(final RadixdocGenerationContext context, final Map<String, String> xmlSchemafPrefix2NamespaceMap, final EDitaGenerationMode generationMode) {
            super(null, context, generationMode);
            this.xmlSchemafPrefix2NamespaceMap = xmlSchemafPrefix2NamespaceMap;
        }

        @Override
        public XmlObject generate(EIsoLanguage lang, final Integer definitionIndex) {
            XmlObject resultObject = XmlObject.Factory.newInstance();

            XmlDocumentProperties props = resultObject.documentProperties();
            props.setDoctypeName(TOPIC_DECLARATION_PARTS[0]);
            props.setDoctypePublicId(TOPIC_DECLARATION_PARTS[1]);
            props.setDoctypeSystemId(TOPIC_DECLARATION_PARTS[2]);

            Document document = resultObject.getDomNode().getOwnerDocument();

            Element topic = document.createElement("topic");
            resultObject.getDomNode().appendChild(topic);

            topic.setAttribute("id", "rules_and_conventions");
            topic.appendChild(DitaGeneratorUtils.createElement(document, "title", GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.RNC_TITLE, lang)));

            Element body = document.createElement("body");
            topic.appendChild(body);

            //-----------------------NAMESPACE PREFIXES SECTION--------------------
            Element elementsSection = document.createElement("section");
            body.appendChild(elementsSection);

            elementsSection.setAttribute("id", "elements_" + lang.getValue());

            Element title = document.createElement("title");
            elementsSection.appendChild(title);
            title.appendChild(DitaGeneratorUtils.createElement(document, "b", GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.NS_PREFIXES_TITLE, lang)));

            Element paragraph = document.createElement("p");
            elementsSection.appendChild(paragraph);

            Element elementsTable = document.createElement("table");
            paragraph.appendChild(elementsTable);

            elementsTable.setAttribute("rowsep", "1");
            elementsTable.setAttribute("colsep", "1");
            elementsTable.setAttribute("frame", "all");

            int elementsTableCols = 2;
            Element tgroup = document.createElement("tgroup");
            elementsTable.appendChild(tgroup);

            tgroup.setAttribute("cols", String.valueOf(elementsTableCols));
            for (int i = 0; i < elementsTableCols; i++) {
                Element colspec = document.createElement("colspec");
                tgroup.appendChild(colspec);

                colspec.setAttribute("colname", "COLSPEC" + i);
                colspec.setAttribute("align", "left");
            }

            Element thead = document.createElement("thead");
            tgroup.appendChild(thead);

            Element theadRow = document.createElement("row");
            thead.appendChild(theadRow);

            theadRow.appendChild(DitaGeneratorUtils.createTableTitleEntry(document, GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.NAMESPACE, lang)));
            theadRow.appendChild(DitaGeneratorUtils.createTableTitleEntry(document, GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.PREFIX, lang)));

            Element tbody = document.createElement("tbody");
            tgroup.appendChild(tbody);
            tbody.setAttribute("valign", "middle");

            for (Entry<String, String> entry : xmlSchemafPrefix2NamespaceMap.entrySet()) {
                Element row = document.createElement("row");
                tbody.appendChild(row);

                String prefix = RADIX_TYPES_SCHEMA_URI.equals(entry.getValue()) ? "<default namespace>" : entry.getKey();

                row.appendChild(DitaGeneratorUtils.createTableEntry(document, entry.getValue()));
                row.appendChild(DitaGeneratorUtils.createTableEntry(document, prefix));

            }
            return resultObject;
        }
    }
}
