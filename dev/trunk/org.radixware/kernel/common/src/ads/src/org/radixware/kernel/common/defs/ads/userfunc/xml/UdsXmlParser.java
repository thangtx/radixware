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
package org.radixware.kernel.common.defs.ads.userfunc.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.radixware.kernel.common.defs.ads.userfunc.UdsUtils;
import static org.radixware.kernel.common.defs.ads.userfunc.UdsUtils.*;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.XPathUtils;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument;
import org.radixware.schemas.adsdef.AdsUserReportExchangeDocument;
import org.radixware.schemas.adsdef.LocalizingBundleDefinition;
import org.radixware.schemas.udsdef.UdsDefinitionDocument;
import org.radixware.schemas.udsdef.UdsDefinitionListDocument;
import org.radixware.schemas.udsdef.UserFunctionDefinition;
import org.radixware.schemas.xscml.JmlFuncProfile;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author npopov
 */
public class UdsXmlParser implements IUdsXmlParser {

    private static final Map<String, String> ufTypeName2Namespace = new HashMap<>(2);
    private static final Logger logger = Logger.getLogger(UdsXmlParser.class.getName());

    static {
        ufTypeName2Namespace.put("Func", "http://schemas.radixware.org/common.xsd");
        ufTypeName2Namespace.put("UserFunc", "http://schemas.radixware.org/common.xsd");
    }

    private UdsXmlParser() {
        //create in Factory
    }

    public static final class Factory {

        public static IUdsXmlParser newInstance() {
            return new UdsXmlParser();
        }
    }

    @Override
    public List<ParseInfo> parse(File xml) throws XmlException, IOException {
        if (xml == null) {
            throw new NullPointerException("Input file is null");
        }
        try (FileInputStream fis = new FileInputStream(xml)) {
            return parse(FileUtils.readTextStream(fis, StandardCharsets.UTF_8.name()));
        }
    }

    @Override
    public List<ParseInfo> parse(InputStream xml) throws XmlException, IOException {
        if (xml == null) {
            throw new NullPointerException("Input stream is null");
        }
        return parse(FileUtils.readTextStream(xml, StandardCharsets.UTF_8.name()));
    }

    @Override
    public List<ParseInfo> parse(String xmlAsStr) throws XmlException {
        if (xmlAsStr == null) {
            throw new NullPointerException("Input string is null");
        }
        final List<ParseInfo> result = new ArrayList<>();
        UserFuncImportInfo.Builder builder = new UserFuncImportInfo.Builder();
        try {
            AdsUserFuncDefinitionDocument doc = AdsUserFuncDefinitionDocument.Factory.parse(xmlAsStr);
            AdsUserFuncDefinitionDocument.AdsUserFuncDefinition xUf = doc.getAdsUserFuncDefinition();
            Node node = xUf.getDomNode();
            if (node instanceof Element) {
                saveXPath((Element) node, builder);
            }
            builder.id = xUf.getId();
            builder.name = xUf.getName();
            builder.description = xUf.getDescription();
            builder.ownerClassId = xUf.getOwnerClassId();
            builder.propId = xUf.getPropId();
            builder.classId = xUf.getClassId();
            builder.methodId = xUf.getMethodId();
            builder.source = xUf.getSource();
            builder.strings = xUf.getStrings();
            if (xUf.getUserFuncProfile() != null) {
                builder.profInfo = new UserFuncImportInfo.ProfileInfo(xUf.getUserFuncProfile());
            }
            builder.suppressedWarnings = xUf.getSuppressedWarnings();
            result.add(builder.createUserFuncInfo());
            return result;
        } catch (XmlException ex) {
            //ignore
        }
        try {
            UdsDefinitionDocument doc = UdsDefinitionDocument.Factory.parse(xmlAsStr);
            if (doc.getUdsDefinition() != null && doc.getUdsDefinition().getUserFunc() != null) {
                UserFunctionDefinition xUf = doc.getUdsDefinition().getUserFunc();
                Node node = xUf.getDomNode();
                if (node instanceof Element) {
                    saveXPath((Element) node, builder);
                }
                builder.id = xUf.getId();
                builder.name = xUf.getName();
                builder.description = xUf.getDescription();
                builder.ownerClassId = xUf.getOwnerClassId();
                builder.propId = xUf.getPropId();
                builder.classId = xUf.getClassId();
                builder.methodId = xUf.getMethodId();
                builder.source = xUf.getSource();
                builder.strings = xUf.getStrings();
                if (xUf.getUserFuncProfile() != null) {
                    builder.profInfo = new UserFuncImportInfo.ProfileInfo(xUf.getUserFuncProfile());
                }
                builder.suppressedWarnings = xUf.getSuppressedWarnings();
                result.add(builder.createUserFuncInfo());
            }
            return result;
        } catch (XmlException ex) {
            //ignore
        }
        try {
            UdsDefinitionListDocument doc = UdsDefinitionListDocument.Factory.parse(xmlAsStr);
            if (doc.getUdsDefinitionList() != null && doc.getUdsDefinitionList().getUdsDefinitionList() != null) {
                for (UdsDefinitionDocument.UdsDefinition def : doc.getUdsDefinitionList().getUdsDefinitionList()) {
                    if (def.getUserFunc() != null) {
                        builder = new UserFuncImportInfo.Builder();
                        UserFunctionDefinition xUf = def.getUserFunc();
                        Node node = xUf.getDomNode();
                        if (node instanceof Element) {
                            saveXPath((Element) node, builder);
                        }
                        builder.id = xUf.getId();
                        builder.name = xUf.getName();
                        builder.description = xUf.getDescription();
                        builder.ownerClassId = xUf.getOwnerClassId();
                        builder.propId = xUf.getPropId();
                        builder.classId = xUf.getClassId();
                        builder.methodId = xUf.getMethodId();
                        builder.source = xUf.getSource();
                        builder.strings = xUf.getStrings();
                        if (xUf.getUserFuncProfile() != null) {
                            builder.profInfo = new UserFuncImportInfo.ProfileInfo(xUf.getUserFuncProfile());
                        }
                        builder.suppressedWarnings = xUf.getSuppressedWarnings();
                        result.add(builder.createUserFuncInfo());
                    }
                }
            }
            return result;
        } catch (XmlException ex) {
            //ignore
        }
        try {
            final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            Element docNode = docFactory
                    .newDocumentBuilder()
                    .parse(new ByteArrayInputStream(xmlAsStr.getBytes(StandardCharsets.UTF_8)))
                    .getDocumentElement();
            collectUfElements(docNode, result);
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            throw new XmlException("Error on parse xml", ex);
        }
        return result;
    }

    private void collectUfElements(Element root, final List<ParseInfo> result)  throws XmlException{
        final Deque<ElementInfo> elemsToVisit = new LinkedList<>();
        elemsToVisit.addFirst(new ElementInfo(root, null));
        while (!elemsToVisit.isEmpty()) {
            final ElementInfo elementInfo = elemsToVisit.removeFirst();
            final Element e = elementInfo.getElement();
            UserFuncLibInfo libInfo = null;
            if (isUserLibElement(e)){
                ParseInfo.Builder builder = new ParseInfo.Builder();
                saveXPath(e, builder);
                final NamedNodeMap attributes = e.getAttributes();
                if (attributes != null) {
                    Node attributeNode = attributes.getNamedItem("Name");
                    if (attributeNode != null) {
                        builder.name = attributeNode.getNodeValue();
                    }
                }
                libInfo = builder.createLib();
                result.add(libInfo);
                //logger.log(Level.INFO, "Found user func lib: \'{0}\'", e.getAttribute("Name"));
            } 
            
            if (isUserFuncElement(e)) {
//                counter++;
                UserFuncImportInfo funcInfo = parseUserFunc(elementInfo);
                if (funcInfo != null){
                    result.add(funcInfo);
                }
                continue;
//                logger.log(Level.INFO, "Import: Found user func: " + counter + ") " + e.getNodeName());
            } else {
                if (isUserReport(e)) {
                    try {
                        AdsUserReportExchangeDocument type = AdsUserReportExchangeDocument.Factory.parse(e);
                        ParseInfo.Builder builder = new ParseInfo.Builder();
                        saveXPath(e, builder);
                        UserReportInfo info = builder.createUserReportInfo();
                        info.setType(type.getAdsUserReportExchange());
                        result.add(info);
                        continue;
                    } catch (XmlException ex) {
                    }
                }
            }
            
            final List<Element> childs = XmlUtils.getChildElements(e);
            if (childs.size() > 0) {
                for (int index = childs.size() - 1; index >= 0; index--) {
                    elemsToVisit.addFirst(new ElementInfo(childs.get(index), libInfo == null ? elementInfo.getLib() : libInfo));
                }
            }
            
            
        }
    }
    
    private boolean isUserReport(Element e) {
        if (EXCHANGE_USER_REPORT.equals(e.getLocalName())) {
            return true;
        }
        if (XmlUtils.findChildByLocalName(e, USER_REPORT) != null) {
            return true;
        }
        return false;
    }

    private boolean isUserFuncElement(Element e) {
        if (ufTypeName2Namespace.containsKey(e.getNodeName())) {
            final Element child = XmlUtils.findFirstElement(e);
            final String namespace = ufTypeName2Namespace.get(e.getNodeName());
            return child != null && namespace.equals(child.getNamespaceURI());
        } else {
            if (XmlUtils.findChildByLocalName(e, JAVA_SRC) != null){
                return e.hasAttribute("OwnerEntityId") && 
                        (XmlUtils.findChildByLocalName(e, METHOD_ID) != null || XmlUtils.findChildByLocalName(e, METHOD_PROFILE) != null);
            }
        }
        return false;
    }
    
    private boolean isUserLibElement(Element e) {
        QName qname = new QName(e.getNamespaceURI(), e.getLocalName());
        if (UdsUtils.USERFUNCLIB_QNAME.equals(qname) || UdsUtils.LIBRARY_QNAME.equals(qname)){
            return true;
        }
        return XmlUtils.findChildByLocalName(e, FUNCTIONS) != null && e.hasAttribute("Name");
    }
    
    private UserFuncImportInfo parseUserFunc(ElementInfo elementInfo) throws XmlException {
        final Element xUf = elementInfo.getElement();
        ParseInfo.Builder builder = new ParseInfo.Builder();
        saveXPath(xUf, builder);
        final XmlOptions parseOpts = new XmlOptions().setLoadReplaceDocumentElement(null);
        final NamedNodeMap attributes = xUf.getAttributes();
        if (attributes != null) {
            Node attributeNode = attributes.getNamedItem("ClassGUID");
            if (attributeNode != null) {
                builder.classId = Id.Factory.loadFrom(attributeNode.getNodeValue());
            }
            attributeNode = attributes.getNamedItem("OwnerPid");
            if (attributeNode != null) {
                builder.ownerPid = attributeNode.getNodeValue();
            }
            attributeNode = attributes.getNamedItem("OwnerClassId");
            if (attributeNode != null) {
                builder.ownerClassId = Id.Factory.loadFrom(attributeNode.getNodeValue());
            }        
            attributeNode = attributes.getNamedItem("OwnerEntityId");
            if (attributeNode != null) {
                builder.ownerEntityId = Id.Factory.loadFrom(attributeNode.getNodeValue());
            }
            attributeNode = attributes.getNamedItem("OwnerPropId");
            if (attributeNode != null) {
                builder.propId = Id.Factory.loadFrom(attributeNode.getNodeValue());
            }
            attributeNode = attributes.getNamedItem("Profile");
            if (attributeNode != null) {
                builder.profile = attributeNode.getNodeValue();
                builder.name = builder.profile;
            }
        }
        for (Element e : XmlUtils.getChildElements(xUf)) {
            switch (e.getLocalName()) {
                case JAVA_SRC:
                    builder.source = org.radixware.schemas.xscml.JmlType.Factory.parse(e, parseOpts);
                    break;
                case METHOD_PROFILE:
                    JmlFuncProfile xJmlProfile = JmlFuncProfile.Factory.parse(e, parseOpts);
                    builder.profInfo = new UserFuncImportInfo.ProfileInfo(xJmlProfile);
                    break;
                case STRINGS:
                    builder.strings = LocalizingBundleDefinition.Factory.parse(e, parseOpts);
                    break;
                case "Description":
                    builder.description = e.getTextContent();
                    break;
                case METHOD_ID:
                    builder.methodId = Id.Factory.loadFrom(e.getTextContent());
                    break;
                default: ;
            }
        }
        UserFuncImportInfo userFuncImportInfo = builder.createUserFuncInfo();
        if (elementInfo.getLib() == null) {
            return userFuncImportInfo;
        }
        elementInfo.getLib().addUserFunc(userFuncImportInfo);
        return null;

    }

    private void saveXPath(Element e, UserFuncImportInfo.Builder builder) {
        List<String> list = new ArrayList<>();
        list.add("id");
        list.add("ownerpid");
        builder.xPath = XPathUtils.getXPath(e, list);        
    }
}
