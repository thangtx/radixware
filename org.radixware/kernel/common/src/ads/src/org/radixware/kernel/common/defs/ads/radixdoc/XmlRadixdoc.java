/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.defs.ads.radixdoc;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.conventions.RadixdocConventions;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.xml.AbstractXmlDefinition;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.radixdoc.DefaultAttributes;
import org.radixware.kernel.common.utils.XPathUtils;
import org.radixware.kernel.common.radixdoc.DefaultStyle;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.RadixIconResource;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Pair;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.schemas.commondef.ChangeLog;
import org.radixware.schemas.commondef.ChangeLogItem;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XmlRadixdoc extends AdsDefinitionRadixdoc<AdsXmlSchemeDef> {

    private static final String SCHEMA_NS = "http://www.w3.org/2001/XMLSchema";
    private static final String TYPES_NS = "http://schemas.radixware.org/types.xsd";

    private static final Map<String, String> LOCALIZATION_MAP = new HashMap<String, String>();

    static {
        LOCALIZATION_MAP.put("BASE_TYPE", "mlsXT4ZPQMGOBDK3GRF4WH7LTFN2A");
        LOCALIZATION_MAP.put("RESTRICTIONS", "mlsQ4YBMOLU6BC6FI2ZVGXYJO5LCM");
        LOCALIZATION_MAP.put("MIN_LENGTH", "mlsY4OLX54IABFD7OUWUCNRBASYBI");
        LOCALIZATION_MAP.put("LENGTH", "mlsD27DTD46PJEW7EKDODBVHDNZVE");
        LOCALIZATION_MAP.put("MAX_LENGTH", "mlsF2L4IHD4KZHMPN3EOT7TBJSLMM");
        LOCALIZATION_MAP.put("TOTAL_DIGITS", "mlsKZDPVXDTUVGSPPS7F43U5D6Y6A");
        LOCALIZATION_MAP.put("FRACTION_DIGITS", "mlsHJ6EPNBWHFBG5DJFFWZWSQJIPU");
        LOCALIZATION_MAP.put("MIN_INCLUSIVE", "mlsLLBH5SJFM5AADH2VOSK5HVALQE");
        LOCALIZATION_MAP.put("MAX_INCLUSIVE", "mlsBOPMFT5TKNEALH4QRTWF2BXZVI");
        LOCALIZATION_MAP.put("PATTERN", "mlsPIAQX2BPOBDYFL757XQMNLGXM4");
        LOCALIZATION_MAP.put("LIST_OF", "mlsLXC2N5GMJNGPHGA3YYYEOPT3GI");
        LOCALIZATION_MAP.put("YES", "mls34AJTSFBFJCPDC23X623JV5X4Y");
        LOCALIZATION_MAP.put("NO", "mlsSGSO7QOIAZDI5M3Z2F4OFDUFZU");
        LOCALIZATION_MAP.put("UNBOUNDED", "mlsFUX6LGKOU5BZTMFEBU3GHRUFFM");
        LOCALIZATION_MAP.put("ENUM", "mlsOHQHDKNBBJBJRLFYKQFV657W64");
    }

    private static final String[] MANDATORY_VARIANTS = {
        "{" + LOCALIZATION_MAP.get("YES") + "}",
        "{" + LOCALIZATION_MAP.get("NO") + "}",
        "{0}..{1}"
    };

    private Map<String, String> ns2LocalPrefixMap;
    private final Map<String, Id> ns2DefIdMap = new HashMap<>();
    private final Map<String, String> ns2TypePrefixMap = new HashMap<>();

    public XmlRadixdoc(AdsXmlSchemeDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    protected void documentOverview(ContentContainer container) {
        writeOverview(newBlock("Overview", container));
        writeChangeLogInfo(newBlock("ChangeLog", container), source.getChangeLog());
        writeXsdItemsInfo(newBlock("Elements", container), source.getDocumentedNodes());
    }

    private Block newBlock(String title, ContentContainer container) {
        Block block = container.addNewBlock();
        block.setStyle(DefaultStyle.CHAPTER);
        getWriter().appendStyle(block, DefaultStyle.CHAPTER, DefaultStyle.OVERVIEW);
        getWriter().addStrTitle(block, title);
        return block;
    }

    private void writeOverview(ContentContainer container) {
        final Block innerContent = container.addNewBlock();
        innerContent.setStyle(DefaultStyle.NAMED);
        if (source.getDescriptionId() != null || source.getDescription() != null && !source.getDescription().isEmpty()) {
            getWriter().documentDescription(innerContent, source);
        }
    }

    private void writeChangeLogInfo(ContentContainer container, List<AdsXmlSchemeDef.ChangeLogEntry> changeLog) {
        if (source.getChangeLog().isEmpty()) {
            Block tmp = container.addNewBlock();
            tmp.setStyle(DefaultStyle.NAMED);
            tmp.addNewText().setStringValue("<None>");
        } else {
            final Table tableChangeLog = container.addNewTable();

            Table.Row rowHead = tableChangeLog.addNewRow();
            rowHead.setMeta("head");
            rowHead.addNewCell().addNewText().setStringValue("Version");
            rowHead.addNewCell().addNewText().setStringValue("Author");
            rowHead.addNewCell().addNewText().setStringValue("Date");
            rowHead.addNewCell().addNewText().setStringValue("Description");

            tableChangeLog.setStyle(DefaultStyle.MEMBERS);

            Collections.sort(changeLog, new Comparator<AdsXmlSchemeDef.ChangeLogEntry>() {
                @Override
                public int compare(AdsXmlSchemeDef.ChangeLogEntry o1, AdsXmlSchemeDef.ChangeLogEntry o2) {
                    return o2.getDate().compareTo(o1.getDate());
                }
            });

            for (AdsXmlSchemeDef.ChangeLogEntry entry : changeLog) {
                Table.Row row = tableChangeLog.addNewRow();
                row.setStyle(DefaultStyle.INHERITED_ITEM);
                row.addNewCell().addNewText().setStringValue(entry.getVersion());
                row.addNewCell().addNewText().setStringValue(entry.getAuthor());
                row.addNewCell().addNewText().setStringValue(new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(entry.getDate().getTime()));
                Table.Row.Cell cell = row.addNewCell();
                if (source.findExistingLocalizingBundle() != null && entry.getId() != null) {
                    getWriter().addMslId(cell, source.findExistingLocalizingBundle().getId(), entry.getId());
                } else {
                    getWriter().addText(cell, "no description");
                }
            }
        }
    }

    private String parseValue(String str) {
        if (str.contains("[@value='")) {
            return str.substring(str.indexOf("[@value='") + "[@value='".length(), str.indexOf("']"));
        } else {
            return "";
        }
    }

    private String parseType(String str) {
        if (str.contains("[@")) {
            return str.substring(0, str.indexOf("[@"));
        } else {
            return str;
        }
    }

    private RadixWareIcons getIconType(String iconType) {
        if (iconType.startsWith("import")) {
            return RadixWareIcons.XML.IMPORTED_SCHEMA;
        }
        switch (iconType) {
            case "simpleType":
                return RadixWareIcons.XML.SIMPLE_TYPE;
            case "attribute":
                return RadixWareIcons.XML.ATTRIBUTE;
            case "restriction":
                return RadixWareIcons.XML.RESTRICTION;
            case "sequence":
                return RadixWareIcons.XML.SEQUENCE;
            case "element":
                return RadixWareIcons.XML.ELEMENT;
            case "enumeration":
                return RadixWareIcons.XML.ENUM;
            case "complexType":
                return RadixWareIcons.XML.COMPLEX_TYPE;
            case "choice":
                return RadixWareIcons.XML.CHOICE;
            default:
                return RadixWareIcons.FILE.NEW_DOCUMENT;
        }
    }

    private void writeElementInfo(Table container, String xPath, AdsXmlSchemeDef.XmlItemDocEntry element) {
        String[] xPathBlocks = xPath.split("/ns\\d*:");
        Table.Row rowHead = container.addNewRow();
        rowHead.setMeta("head");
        Table.Row.Cell headCell = rowHead.addNewCell();
        getWriter().setAttribute(headCell, DefaultAttributes.ANCHOR, xpathToNodeId(xPath));
        headCell.setColspan(2);
        final RadixIconResource res = new RadixIconResource(getIconType(parseType(xPathBlocks[xPathBlocks.length - 1])));
        headCell.addNewResource().setSource(res.getKey());
        addResource(res);
        headCell.addNewText().setStringValue(XPathUtils.getHumanReadableXPath(xPath));
        container.setStyle(DefaultStyle.MEMBERS);

        Table.Row row = container.addNewRow();
        row.setStyle(DefaultStyle.INHERITED_ITEM);
        row.addNewCell().addNewText().setStringValue("Name: ");
        row.addNewCell().addNewText().setStringValue(XPathUtils.getXPathElementName(xPathBlocks[xPathBlocks.length - 1])
                + parseValue(xPathBlocks[xPathBlocks.length - 1]) + " ");

        row = container.addNewRow();
        row.setStyle(DefaultStyle.INHERITED_ITEM);
        row.addNewCell().addNewText().setStringValue("Type: ");
        row.addNewCell().addNewText().setStringValue(parseType(xPathBlocks[xPathBlocks.length - 1]));

        row = container.addNewRow();
        row.setStyle(DefaultStyle.INHERITED_ITEM);
        row.addNewCell().addNewText().setStringValue("Description: ");
        if (element.getId() != null) {
            getWriter().addMslId(row.addNewCell(), source.findExistingLocalizingBundle().getId(), element.getId());
        } else {
            row.addNewCell().addNewText().setStringValue("");
        }

        row = container.addNewRow();
        row.setStyle(DefaultStyle.INHERITED_ITEM);
        row.addNewCell().addNewText().setStringValue("Since: ");
        row.addNewCell().addNewText().setStringValue(element.getSinceVersion());

//        row = xPathTable.addNewRow();
//        row.setStyle(DefaultStyle.INHERITED_ITEM);
//        row.addNewCell().addNewText().setStringValue("xPath: ");
//        row.addNewCell().addNewText().setStringValue(xPath);
    }

    private void writeXsdItemsInfo(ContentContainer container, List<String> xPath) {
        if (!xPath.isEmpty()) {
//            Block tmp = container.addNewBlock();
//            tmp.setStyle(DefaultStyle.NAMED);
//            tmp.addNewText().setStringValue("There where no documented items");
//        } else {
            Collections.sort(xPath);
            for (String xPathItem : xPath) {
                if (xPathItem.split("/ns\\d*:").length > 2) {
                    writeElementInfo(container.addNewTable(), xPathItem, source.getXmlItemDocEntry(xPathItem));
                }
            }
        }
    }
    
    private String xpathToNodeId(String xpath) {
        StringBuilder result = new StringBuilder();
        String[] xpathParts = xpath.split("/ns\\d*:");        
        
        for (String part : xpathParts) {
            if (part.contains("[@name='")) {
                if (result.length() != 0) {
                    result.append("_");
                }
                
                
                result.append(XPathUtils.getXPathElementName(part));
            }
        }
        
        return result.toString();
    }

    @Override
    public AbstractDefDocItem buildDocItem() {
        XmlObject obj = source.getXmlDocument();
        if (obj == null) {
            obj = source.getXmlContent();
        }

        ns2LocalPrefixMap = XmlUtils.getNamespacePrefixes(obj);

        AdsXmlSchemeDefDocItem xmlDocItem = AdsXmlSchemeDefDocItem.Factory.newInstance();

        xmlDocItem.setId(source.getId());
        xmlDocItem.setName(source.getName());
        xmlDocItem.setTargetNamespace(source.getTargetNamespace());
        xmlDocItem.setDefinitionType(EDefType.XML_SCHEME);
        xmlDocItem.setDescriptionId(source.getDescriptionId());
        xmlDocItem.setIsDepricated(source.isDeprecated());
        xmlDocItem.setIsFinal(source.isFinal());
        xmlDocItem.setIsPublished(source.isPublished());
        xmlDocItem.setNeedsDocumentation(source.needsDocumentation());
        if (source.getDocumentationTitleId() != null) {
            xmlDocItem.setDocumentationTitleId(source.getDocumentationTitleId());
        }
        
        if (source.getSchemaZIPTitleId() != null) {
            xmlDocItem.setSchemaZIPTitleId(source.getSchemaZIPTitleId());
        }

        XmlItemDocEntries entries = xmlDocItem.addNewXmlItemDocEntries();

        for (XmlItemDocEntry itemEntry : getXmlItemDocEntries(obj)) {
            entries.getXmlItemDocEntryList().add(itemEntry);
        }

        if (!source.getChangeLog().isEmpty()) {
            ChangeLog changeLog = xmlDocItem.addNewXmlChangeLog();
            for (AdsXmlSchemeDef.ChangeLogEntry entry : source.getChangeLog()) {
                ChangeLogItem item = changeLog.addNewChangeLogItem();
                item.setDescriptionMlsId(entry.getId());
                item.setSinceVersion(entry.getVersion());
                item.setDate(new Timestamp(entry.getDate().getTimeInMillis()));
                item.setAuthor(entry.getAuthor());
            }
        }

        return xmlDocItem;
    }

    private List<XmlItemDocEntry> getXmlItemDocEntries(XmlObject obj) {
        List<XmlItemDocEntry> result = new ArrayList<>();

        if (obj == null) {
            return result;
        }

        Element root = XmlUtils.findFirstElement(obj.getDomNode());
        if (root != null) {
            addElementEntry(root, result, null);
        }

        return result;
    }

    private void addElementEntry(Element element, List<XmlItemDocEntry> list, XmlItemDocEntry parent) {
        String elementXpath = XPathUtils.getXPath(element);
        XmlItemDocEntry entry = parent;

        if (XPathUtils.isElementNeedsDoc(element) || source.getDocumentedNodes().contains(elementXpath)) {
            entry = XmlItemDocEntry.Factory.newInstance();
            entry.setNodeXPath(elementXpath);
            entry.setMandatory(getElementMandatory(element));

            if (!source.getDocumentedNodes().isEmpty() && source.getXmlItemDocEntry(elementXpath) != null) {
                entry.setLocalizedStringId(source.getXmlItemDocEntry(elementXpath).getId());
                entry.setSinceVersion(source.getXmlItemDocEntry(elementXpath).getSinceVersion());
            } else {
                entry.setLocalizedStringId(null);
                entry.setSinceVersion("");
            }
            entry.setXmlItemType(getElementType(element));
        }

        List<Element> children = XmlUtils.getChildElements(element);

        for (Element child : children) {
            addElementEntry(child, list, entry);
        }

        if (XPathUtils.isElementNeedsDoc(element) || source.getDocumentedNodes().contains(elementXpath)) {
            if (parent == null) {
                list.add(entry);
            } else {
                XmlItemDocEntries subEntries;
                if (element.getLocalName().equals("attribute")) {
                    subEntries = parent.getAttributes() == null ? parent.addNewAttributes() : parent.getAttributes();
                } else {
                    subEntries = parent.getSubElements() == null ? parent.addNewSubElements() : parent.getSubElements();
                }
                subEntries.getXmlItemDocEntryList().add(entry);
            }
        }
    }

    private XmlItemType getElementType(Element element) {
        XmlItemType type = XmlItemType.Factory.newInstance();

        Pair<String, String> ns2TypePair = getElementTypeNsValuePair(element);

        type.setNamespace(ns2TypePair.getFirst());
        type.setValue(ns2TypePair.getSecond());

        Id typeId = null;
        String typeNs = ns2TypePair.getFirst();

        AdsXmlSchemeDef def = null;
        if (!ns2DefIdMap.containsKey(typeNs)) {
            if (typeNs.equals(source.getTargetNamespace())) {
                typeId = source.getId();
            } else {
                AdsSearcher.Factory.XmlDefinitionSearcher searcher = AdsSearcher.Factory.newXmlDefinitionSearcher(source);
                IXmlDefinition typeHolderDef = searcher.findByNs(typeNs).get();
                if (typeHolderDef != null) {
                    if (typeHolderDef instanceof AbstractXmlDefinition) {
                        typeId = ((AbstractXmlDefinition) typeHolderDef).getId();
                        def = typeHolderDef instanceof AdsXmlSchemeDef ? ((AdsXmlSchemeDef) typeHolderDef) : null;
                    }
                }
            }
            ns2DefIdMap.put(typeNs, typeId);
        } else {
            typeId = ns2DefIdMap.get(typeNs);
        }
        type.setId(typeId);

        String typePrefix = null;
        if (!ns2TypePrefixMap.containsKey(typeNs)) {
            if (typeNs.equals(source.getTargetNamespace())) {
                typePrefix = source.getNamespacePrefix();
            } else {
                if (def != null) {
                    typePrefix = ((AdsXmlSchemeDef) def).getNamespacePrefix();
                } else {
                    AdsSearcher.Factory.XmlDefinitionSearcher searcher = AdsSearcher.Factory.newXmlDefinitionSearcher(source);
                    IXmlDefinition typeHolderDef = searcher.findByNs(typeNs).get();
                    if (typeHolderDef != null) {
                        if (typeHolderDef instanceof AdsXmlSchemeDef) {
                            typePrefix = ((AdsXmlSchemeDef) typeHolderDef).getNamespacePrefix();
                        }
                    }
                }
            }
            ns2TypePrefixMap.put(typeNs, typePrefix);
        } else {
            typePrefix = ns2TypePrefixMap.get(typeNs);
        }

        if (typePrefix != null) {
            type.setPrefix(typePrefix);
        }

        return type;
    }

    private Pair<String, String> getElementTypeNsValuePair(Element element) {
        if (element.getLocalName().equals("schema")) {
            return new Pair(SCHEMA_NS, element.getLocalName());
        }

        if (element.getLocalName().equals("complexType")) {
            return new Pair(SCHEMA_NS, element.getLocalName());
        }

        if (element.getLocalName().equals("simpleType")) {
            return getSimpleTypeElementType(element);
        }

        if (element.hasAttribute("type")) {
            String[] typeParts = element.getAttribute("type").split(":");
            if (typeParts.length < 1 || typeParts.length > 2) {
                return new Pair(SCHEMA_NS, element.getLocalName());
            }

            String baseNS;
            if (typeParts.length == 1 || typeParts[0].isEmpty()) {
                baseNS = source.getTargetNamespace();
            } else {
                baseNS = ns2LocalPrefixMap.get(typeParts[0]) == null ? typeParts[0] : ns2LocalPrefixMap.get(typeParts[0]);
            }

            String type = typeParts.length == 1 ? typeParts[0] : typeParts[1];

            return new Pair(baseNS, type);
        } else {
            Element child = XmlUtils.findChildByLocalName(element, "complexType");
            if (child != null) {
                return getElementTypeNsValuePair(child);
            } else {
                child = XmlUtils.findChildByLocalName(element, "simpleType");
                if (child != null) {
                    return getElementTypeNsValuePair(child);
                } else {
                    return new Pair(SCHEMA_NS, element.getLocalName());
                }
            }
        }
    }

    private Pair<String, String> getSimpleTypeElementType(Element element) {
        if (XmlUtils.getChildElements(element).isEmpty()) {
            return new Pair(SCHEMA_NS, element.getLocalName());
        }

        Element child = XmlUtils.findChildByLocalName(element, "annotation");
        if (child != null) {
            if (!XmlUtils.getChildElements(child).isEmpty()) {
                Element annotationChild = XmlUtils.findChildByLocalName(child, "appinfo");
                if (annotationChild != null) {
                    if (annotationChild.hasAttribute("source") && TYPES_NS.equals(annotationChild.getAttribute("source"))) {
                        List<Element> appinfoChildren = XmlUtils.getChildElements(annotationChild);
                        for (Element appinfoChild : appinfoChildren) {
                            if ("class".equals(appinfoChild.getLocalName())) {
                                Definition definition = null;

                                if (appinfoChild.hasAttribute("classId")) {
                                    String idCandidate = appinfoChild.getAttribute("classId");

                                    Id id = Id.Factory.loadFrom(idCandidate);
                                    DefinitionSearcher<AdsDefinition> searcher = AdsSearcher.Factory.newAdsDefinitionSearcher(source);
                                    definition = searcher.findById(id).get();
                                }

                                if (definition == null) {
                                    String val = appinfoChild.getChildNodes().item(0).getNodeValue();

                                    if (val != null) {
                                        String valParts[] = val.split("\\.");
                                        String idVal = valParts[valParts.length - 1];

                                        if (idVal.length() == Id.DEFAULT_ID_AS_STR_LENGTH) {
                                            Id id = Id.Factory.loadFrom(idVal);
                                            DefinitionSearcher<AdsDefinition> searcher = AdsSearcher.Factory.newAdsDefinitionSearcher(source);
                                            definition = searcher.findById(id).get();
                                        } else {
                                            IPlatformClassPublisher publisher = ((AdsSegment) source.getLayer().getAds()).getBuildPath().getPlatformPublishers().findPublisherByName(val);
                                            if (publisher instanceof AdsDefinition) {
                                                definition = (AdsDefinition) publisher;
                                            } else {
                                                return new Pair<>(val, val);
                                            }
                                        }
                                    }
                                }

                                if (definition != null && definition instanceof AdsEnumDef) {
                                    ns2DefIdMap.put("enum_" + definition.getId().toString(), definition.getId());

                                    return new Pair("enum_" + definition.getId().toString(), definition.getQualifiedName());
                                }
                            }
                        }
                    }
                }
            }
        }

        child = XmlUtils.findChildByLocalName(element, "restriction");
        if (child != null) {
            if (child.hasAttribute("base")) {
                String[] typeParts = child.getAttribute("base").split(":");

                if (typeParts.length < 1 || typeParts.length > 2) {
                    return new Pair(SCHEMA_NS, element.getLocalName());
                }

                String baseNS;
                if (typeParts.length == 1 || typeParts[0].isEmpty()) {
                    baseNS = source.getTargetNamespace();
                } else {
                    baseNS = ns2LocalPrefixMap.get(typeParts[0]) == null ? typeParts[0] : ns2LocalPrefixMap.get(typeParts[0]);
                }

                String type = typeParts.length == 1 ? typeParts[0] : typeParts[1];
                if (XmlUtils.findChildByLocalName(child, "enumeration") != null) {
                    type = "XML " + RadixdocConventions.RADIXDOC_2_BEGIN_XREF_MARK + type + RadixdocConventions.RADIXDOC_2_END_XREF_MARK + " {" + LOCALIZATION_MAP.get("ENUM") + "}";
                } else {
                    StringBuilder typeBuilder = new StringBuilder();
                    typeBuilder.append("{").append(LOCALIZATION_MAP.get("BASE_TYPE")).append("}: ").
                            append(RadixdocConventions.RADIXDOC_2_BEGIN_XREF_MARK).
                            append(type).
                            append(RadixdocConventions.RADIXDOC_2_END_XREF_MARK);

                    if (child.hasChildNodes()) {
                        typeBuilder.append(RadixdocConventions.RADIXDOC_2_NEW_LINE_MARK).
                                append("{").append(LOCALIZATION_MAP.get("RESTRICTIONS")).append("}: ");

                        Element restrictionElement;

                        restrictionElement = XmlUtils.findChildByLocalName(child, "minLength");
                        if (restrictionElement != null) {
                            typeBuilder.append(RadixdocConventions.RADIXDOC_2_NEW_LINE_MARK).
                                    append("{").append(LOCALIZATION_MAP.get("MIN_LENGTH")).append("}: ").
                                    append(restrictionElement.getAttribute("value"));
                        }

                        restrictionElement = XmlUtils.findChildByLocalName(child, "length");
                        if (restrictionElement != null) {
                            typeBuilder.append(RadixdocConventions.RADIXDOC_2_NEW_LINE_MARK).
                                    append("{").append(LOCALIZATION_MAP.get("LENGTH")).append("}: ").
                                    append(restrictionElement.getAttribute("value"));
                        }

                        restrictionElement = XmlUtils.findChildByLocalName(child, "maxLength");
                        if (restrictionElement != null) {
                            typeBuilder.append(RadixdocConventions.RADIXDOC_2_NEW_LINE_MARK).
                                    append("{").append(LOCALIZATION_MAP.get("MAX_LENGTH")).append("}: ").
                                    append(restrictionElement.getAttribute("value"));
                        }

                        restrictionElement = XmlUtils.findChildByLocalName(child, "totalDigits");
                        if (restrictionElement != null) {
                            typeBuilder.append(RadixdocConventions.RADIXDOC_2_NEW_LINE_MARK).
                                    append("{").append(LOCALIZATION_MAP.get("TOTAL_DIGITS")).append("}: ").
                                    append(restrictionElement.getAttribute("value"));
                        }

                        restrictionElement = XmlUtils.findChildByLocalName(child, "fractionDigits");
                        if (restrictionElement != null) {
                            typeBuilder.append(RadixdocConventions.RADIXDOC_2_NEW_LINE_MARK).
                                    append("{").append(LOCALIZATION_MAP.get("FRACTION_DIGITS")).append("}: ").
                                    append(restrictionElement.getAttribute("value"));
                        }

                        restrictionElement = XmlUtils.findChildByLocalName(child, "minInclusive");
                        if (restrictionElement != null) {
                            typeBuilder.append(RadixdocConventions.RADIXDOC_2_NEW_LINE_MARK).
                                    append("{").append(LOCALIZATION_MAP.get("MIN_INCLUSIVE")).append("}: ").
                                    append(restrictionElement.getAttribute("value"));
                        }

                        restrictionElement = XmlUtils.findChildByLocalName(child, "maxInclusive");
                        if (restrictionElement != null) {
                            typeBuilder.append(RadixdocConventions.RADIXDOC_2_NEW_LINE_MARK).
                                    append("{").append(LOCALIZATION_MAP.get("MAX_INCLUSIVE")).append("}: ").
                                    append(restrictionElement.getAttribute("value"));
                        }

                        restrictionElement = XmlUtils.findChildByLocalName(child, "pattern");
                        if (restrictionElement != null) {
                            typeBuilder.append(RadixdocConventions.RADIXDOC_2_NEW_LINE_MARK).
                                    append("{").append(LOCALIZATION_MAP.get("PATTERN")).append("}: ").
                                    append(restrictionElement.getAttribute("value"));
                        }
                    }
                    type = typeBuilder.toString();
                }

                if (baseNS != null && !baseNS.isEmpty() && type != null && !type.isEmpty()) {
                    return new Pair(baseNS, type);
                }
            }
        }

        child = XmlUtils.findChildByLocalName(element, "list");
        if (child != null) {
            if (child.hasAttribute("itemType")) {
                String[] typeParts = child.getAttribute("itemType").split(":");

                if (typeParts.length < 1 || typeParts.length > 2) {
                    return new Pair(SCHEMA_NS, element.getLocalName());
                }

                String baseNS;
                if (typeParts.length == 1 || typeParts[0].isEmpty()) {
                    baseNS = source.getTargetNamespace();
                } else {
                    baseNS = ns2LocalPrefixMap.get(typeParts[0]) == null ? typeParts[0] : ns2LocalPrefixMap.get(typeParts[0]);
                }

                String type = typeParts.length == 1 ? typeParts[0] : typeParts[1];
                return new Pair<>(baseNS, "{" + LOCALIZATION_MAP.get("LIST_OF") + "} " + RadixdocConventions.RADIXDOC_2_BEGIN_XREF_MARK + type + RadixdocConventions.RADIXDOC_2_END_XREF_MARK);
            }
        }

        return new Pair(SCHEMA_NS, element.getLocalName());
    }

    private String getElementMandatory(Element element) {
        String minOccurs = null;
        String maxOccurs = null;

        if (element.hasAttribute("maxOccurs")) {
            minOccurs = "1";
            maxOccurs = element.getAttribute("maxOccurs");
            if ("0".equals(maxOccurs)) {
                return MANDATORY_VARIANTS[1];
            }
        }

        if (element.getLocalName().equals("element")) {
            minOccurs = "1";
            maxOccurs = maxOccurs == null ? "1" : maxOccurs;
            if (element.hasAttribute("minOccurs")) {
                minOccurs = element.getAttribute("minOccurs");
            } else {
                Node parent = element.getParentNode();
                while (parent != null && !(parent instanceof Element)) {
                    parent = parent.getParentNode();
                }

                if (parent != null && "schema".equals(parent.getLocalName())) {
                    return MANDATORY_VARIANTS[1];
                }
            }
        }

        if (minOccurs != null && maxOccurs != null) {
            if ("unbounded".equals(maxOccurs)) {
                maxOccurs = "{" + LOCALIZATION_MAP.get("UNBOUNDED") + "}";
            }
            return MessageFormat.format(MANDATORY_VARIANTS[2], minOccurs, maxOccurs);
        }

        if (element.getLocalName().equals("attribute")) {
            if (element.hasAttribute("use")) {
                String use = element.getAttribute("use");
                switch (use) {
                    case "optional":
                    case "prohibited":
                        return MANDATORY_VARIANTS[1];
                    case "required":
                        return MANDATORY_VARIANTS[0];
                    default:
                        return MANDATORY_VARIANTS[1];
                }
            } else {
                return MANDATORY_VARIANTS[1];
            }
        }

        return null;
    }

}
