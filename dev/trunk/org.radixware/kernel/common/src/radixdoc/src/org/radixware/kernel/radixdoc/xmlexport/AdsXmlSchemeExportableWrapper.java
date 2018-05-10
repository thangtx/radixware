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
package org.radixware.kernel.radixdoc.xmlexport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.dialogs.chooseobject.SelectableObjectWrapper;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author dlastochkin
 */
public class AdsXmlSchemeExportableWrapper implements IExportableXmlSchema {

    private final AdsXmlSchemeDef schema;
    private final Set<Id> enumIds = new HashSet<>();

    public AdsXmlSchemeExportableWrapper(AdsXmlSchemeDef schema) {
        this.schema = schema;
    }

    @Override
    public Collection<IExportableXmlSchema> getLinkedSchemas() {
        List<IExportableXmlSchema> result = new ArrayList<>();

        for (AdsXmlSchemeDef linkedSchema : schema.getLinkedSchemas().keySet()) {
            result.add(new AdsXmlSchemeExportableWrapper(linkedSchema));
        }

        XmlObject obj = schema.getXmlDocument();
        if (obj == null) {
            obj = schema.getXmlContent();
        }
        if (obj != null) {
            List<XmlUtils.Namespace2Location> imports = XmlUtils.getImportedNamespaces2Loc(obj, false);
            for (XmlUtils.Namespace2Location imp : imports) {
                if (imp.isSpecialLocation()) {
                    try {
                        URI uri = new URI(imp.location);
                        File file = new File(uri.getPath());

                        String schemaName = file.getName();
                        XmlObject importObj = findKernelSchema(schemaName, imp.namespace);
                        if (importObj != null) {
                            result.add(new KernelXmlSchemaExportableWrapper(importObj, schemaName) {

                                @Override
                                public XmlObject findKernelSchema(String name, String namespace) {
                                    return AdsXmlSchemeExportableWrapper.this.findKernelSchema(name, namespace);
                                }
                            });
                        }
                    } catch (URISyntaxException ex) {
                        Logger.getLogger(AdsXmlSchemeExportableWrapper.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        return result;
    }

    @Override
    public InputStream getDefinitionInputStream() {
        try {
            return new FileInputStream(schema.getFile());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AdsXmlSchemeExportableWrapper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public InputStream getInputStream() {
        XmlObject obj = schema.getXmlDocument();
        if (obj == null) {
            obj = schema.getXmlContent();
        }

        if (obj != null) {
            return obj.newInputStream();
        } else {
            return null;
        }
    }

    @Override
    public Map<Id, String> getLocalizedStrings(EIsoLanguage lang) {
        Map<Id, String> result = new HashMap<>();
        EIsoLanguage actualLang = schema.getLayer().getLanguages().contains(lang) ? lang : EIsoLanguage.ENGLISH;

        for (AdsMultilingualStringDef mlString : schema.findLocalizingBundle().getStrings().getLocal()) {
            result.put(mlString.getId(), mlString.getValue(actualLang));
        }

        return result;
    }

    @Override
    public void processEnumerations(Node node) {
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
                        if (n.getNodeValue().equals("http://schemas.radixware.org/types.xsd")) {
                            isMustAdd = true;
                            break;
                        }
                    }
                }

                String tUri = "";
                if (isMustAdd) {
                    AdsDefinition definition = null;
                    int len2 = node.getChildNodes().getLength();
                    for (int i = 0; i < len2; i++) {
                        Node node3 = node.getChildNodes().item(i);
                        if (node3.getNodeType() == Node.ELEMENT_NODE
                                && node3.getLocalName().equals("class")
                                && node3.getChildNodes().getLength() == 1
                                && node3.getChildNodes().item(0).getNodeType() == Node.TEXT_NODE) {
                            tUri = node3.getNamespaceURI();
                            //first look at classid attribute
                            Node classIdAttr = node3.getAttributes().getNamedItem("classId");
                            if (classIdAttr != null) {
                                String idCandidate = classIdAttr.getNodeValue();
                                Id id = Id.Factory.loadFrom(idCandidate);
                                DefinitionSearcher<AdsDefinition> searcher
                                        = AdsSearcher.Factory.newAdsDefinitionSearcher(schema);
                                definition = searcher.findById(id).get();
                            }

                            if (definition == null) {
                                String val = node3.getChildNodes().item(0).getNodeValue();

                                if (val != null) {
                                    String valParts[] = val.split("\\.");
                                    String idVal = valParts[valParts.length - 1];

                                    if (idVal.length() == Id.DEFAULT_ID_AS_STR_LENGTH) {
                                        Id id = Id.Factory.loadFrom(idVal);
                                        DefinitionSearcher<AdsDefinition> searcher = AdsSearcher.Factory.newAdsDefinitionSearcher(schema);
                                        definition = searcher.findById(id).get();
                                    } else {
                                        //try to look for published enumeration
                                        IPlatformClassPublisher publisher = ((AdsSegment) schema.getLayer().getAds()).getBuildPath().getPlatformPublishers().findPublisherByName(val);
                                        if (publisher instanceof AdsDefinition) {
                                            definition = (AdsDefinition) publisher;
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }

                    if (definition != null && definition instanceof AdsEnumDef) {
                        AdsEnumDef enumDef = (AdsEnumDef) definition;
                        enumIds.add(definition.getId());

                        //String xsPreff = node.getPrefix().isEmpty() ? "" : node.getPrefix() + ":";
                        Node doc = node.getOwnerDocument().createElementNS(
                                node.getNamespaceURI(), "documentation");
                        Node text = node.getOwnerDocument().createTextNode("Enum \"" + enumDef.getName() + "\"");

                        node.appendChild(doc);
                        doc.appendChild(text);

                        Node enumItems = node.getOwnerDocument().createElementNS(tUri, "enumItems");
                        node.appendChild(enumItems);

                        List<AdsEnumItemDef> itemList = enumDef.getItems().get(ExtendableDefinitions.EScope.ALL);
                        for (AdsEnumItemDef item : itemList) {
                            Element enumItem = node.getOwnerDocument().createElementNS(tUri, "enumItem");
                            enumItems.appendChild(enumItem);

                            enumItem.setAttribute("Val", item.getValue().toString());
                            enumItem.setAttribute("Name", item.getName());
                            List<EIsoLanguage> lngLst
                                    = schema.getModule().getSegment().getLayer().getLanguages();
                            for (EIsoLanguage l : lngLst) {   //Element lng = node.getOwnerDocument().createElementNS(tUri, "Title");
                                if (item.getTitle(l) != null && !item.getTitle(l).trim().isEmpty()) {
                                    Element lng = node.getOwnerDocument().createElement("Title");
                                    enumItem.appendChild(lng);
                                    lng.setAttribute("language", l.getValue().toUpperCase());

                                    Node text2 = node.getOwnerDocument().createTextNode(item.getTitle(l));
                                    lng.appendChild(text2);
                                }
                            }
                        }
                    }
                }
                isUseChilds = false;
            }
        }
        if (isUseChilds) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                processEnumerations(nodeList.item(i));
            }
        }
    }

    @Override
    public String getTitle() {
        return SelectableObjectWrapper.getRadixObjectWrap(schema).getTitle();
    }

    @Override
    public Object getUserObject() {
        return schema;
    }

    @Override
    public String getLocation() {
        return SelectableObjectWrapper.getRadixObjectWrap(schema).getLocation();
    }

    @Override
    public Icon getLocationIcon() {
        return SelectableObjectWrapper.getRadixObjectWrap(schema).getLocationIcon();
    }

    @Override
    public Icon getIcon() {
        return SelectableObjectWrapper.getRadixObjectWrap(schema).getIcon();
    }

    @Override
    public Id getId() {
        return schema.getId();
    }

    @Override
    public Collection<Id> getEnumIds() {
        return new HashSet<>(enumIds);
    }

    @Override
    public XmlObject findKernelSchema(String name, String namespace) {
        XmlObject obj = ((AdsSegment) schema.getLayer().getAds()).getBuildPath().getPlatformXml().findXmlSchema(namespace, name, ERuntimeEnvironmentType.COMMON);
        return obj;
    }
}
