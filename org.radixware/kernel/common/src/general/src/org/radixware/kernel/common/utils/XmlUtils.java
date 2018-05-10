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
package org.radixware.kernel.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument.Import;
import org.apache.xmlbeans.impl.xb.xsdschema.IncludeDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema;
import org.radixware.kernel.common.build.xbeans.XmlEscapeStr;

import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlsoap.schemas.wsdl.DefinitionsDocument;
import org.xmlsoap.schemas.wsdl.TDefinitions;
import org.xmlsoap.schemas.wsdl.TImport;
import org.xmlsoap.schemas.wsdl.TTypes;

public class XmlUtils {

    public static final String RDX_TYPES_XMLNS = "http://schemas.radixware.org/types.xsd";
    public static final String XMLNS = "xmlns";

    private static void addSchemaIncludeLocation(Schema s, List<String> list) {
        IncludeDocument.Include[] includes = s.getIncludeArray();
        if (includes != null) {
            for (IncludeDocument.Include include : includes) {
                list.add(include.getSchemaLocation());
            }
        }
    }
    
    private static void addSchemaImportNs(Schema s, List<String> list, boolean ignoreSpecialRefs) {
        Import[] imports = s.getImportArray();
        if (imports != null) {
            for (int i = 0; i < imports.length; i++) {
                if (ignoreSpecialRefs && imports[i].getSchemaLocation() != null && imports[i].getSchemaLocation().startsWith("radix:/")) {
                    continue;
                }
                list.add(imports[i].getNamespace());
            }
        }
    }

    private static void addSchemaImportNs2Loc(Schema s, List<Namespace2Location> list, boolean ignoreSpecialRefs) {
        final Import[] imports = s.getImportArray();
        if (imports != null) {
            for (int i = 0; i < imports.length; i++) {
                final Import imp = imports[i];
                if (ignoreSpecialRefs && imports[i].getSchemaLocation() != null && imports[i].getSchemaLocation().startsWith("radix:/")) {
                    continue;
                }
                list.add(new Namespace2Location(imports[i].getNamespace(), imports[i].getSchemaLocation(), new Namespace2Location.LocationSetter() {
                    @Override
                    void set(String newLocation) {
                        imp.setSchemaLocation(newLocation);
                    }
                }));
            }
        }
    }

    public static List<String> getImportedNamespaces(XmlObject object) {
        return getImportedNamespaces(object, false);
    }

    public static List<String> getImportedNamespaces(XmlObject object, boolean ignoreSpecialRefs) {
        if (object instanceof Schema) {
            ArrayList<String> list = new ArrayList<>();
            addSchemaImportNs((Schema) object, list, ignoreSpecialRefs);
            return list;
        } else if (object instanceof TDefinitions) {
            ArrayList<String> list = new ArrayList<>();
            for (TImport importDef : ((TDefinitions) object).getImportList()) {
                if (ignoreSpecialRefs && importDef.getLocation() != null && importDef.getLocation().startsWith("radix:/")) {
                    continue;
                }
                list.add(importDef.getNamespace());
            }
            for (TTypes type : ((TDefinitions) object).getTypesList()) {
                XmlObject obj = XmlObjectProcessor.getXmlObjectFirstChild(type);
                if (obj != null && obj instanceof Schema) {
                    addSchemaImportNs((Schema) obj, list, ignoreSpecialRefs);
                }
            }
            return list;
        } else if (object instanceof SchemaDocument) {
            return getImportedNamespaces(((SchemaDocument) object).getSchema(), ignoreSpecialRefs);
        } else if (object instanceof DefinitionsDocument) {
            return getImportedNamespaces(((DefinitionsDocument) object).getDefinitions(), ignoreSpecialRefs);
        } else {
            return Collections.emptyList();
        }
    }

    public static class Namespace2Location {

        private static abstract class LocationSetter {

            abstract void set(String newLocation);
        }
        public final String namespace;
        public final String location;
        private final LocationSetter lockSetter;

        private Namespace2Location(String namespace, String location, LocationSetter setter) {
            this.namespace = namespace;
            this.location = location;
            this.lockSetter = setter;
        }

        public boolean isSpecialLocation() {
            return location != null && location.startsWith("radix:/");
        }

        public void setActualLocation(String newLocation) {
            lockSetter.set(newLocation);
        }
    }

    public static XmlObject newCopy(XmlObject xObj) throws IOException {
        try (InputStream stream = xObj.newInputStream()) {
            return XmlObject.Factory.parse(stream);
        } catch (XmlException ex) {
            throw new IOException(ex);
        }
    }

    public static List<String> getIncludedLocations(XmlObject object) {
        if (object instanceof Schema) {
            ArrayList<String> list = new ArrayList<>();
            addSchemaIncludeLocation((Schema) object, list);
            return list;
        } else if (object instanceof TDefinitions) {
            ArrayList<String> list = new ArrayList<>();            
            for (TTypes type : ((TDefinitions) object).getTypesList()) {                
                XmlObject obj = XmlObjectProcessor.getXmlObjectFirstChild(type);
                if (obj != null && obj instanceof Schema) {
                    addSchemaIncludeLocation((Schema) obj, list);
                }
            }
            return list;
        } else if (object instanceof SchemaDocument) {
            return getIncludedLocations(((SchemaDocument) object).getSchema());
        } else if (object instanceof DefinitionsDocument) {
            return getIncludedLocations(((DefinitionsDocument) object).getDefinitions());
        } else {
            return Collections.emptyList();
        }
    }
    
    public static List<Namespace2Location> getImportedNamespaces2Loc(XmlObject object, boolean ignoreSpecialRefs) {
        if (object instanceof Schema) {
            ArrayList<Namespace2Location> list = new ArrayList<>();
            addSchemaImportNs2Loc((Schema) object, list, ignoreSpecialRefs);
            return list;
        } else if (object instanceof TDefinitions) {
            ArrayList<Namespace2Location> list = new ArrayList<>();
            for (final TImport importDef : ((TDefinitions) object).getImportList()) {
                if (ignoreSpecialRefs && importDef.getLocation() != null && importDef.getLocation().startsWith("radix:/")) {
                    continue;
                }
                list.add(new Namespace2Location(importDef.getNamespace(), importDef.getLocation(), new Namespace2Location.LocationSetter() {
                    @Override
                    void set(String newLocation) {
                        importDef.setLocation(newLocation);
                    }
                }));
            }
            for (TTypes type : ((TDefinitions) object).getTypesList()) {
                XmlObject obj = XmlObjectProcessor.getXmlObjectFirstChild(type);
                if (obj != null && obj instanceof Schema) {
                    addSchemaImportNs2Loc((Schema) obj, list, ignoreSpecialRefs);
                }
            }
            return list;
        } else if (object instanceof SchemaDocument) {
            return getImportedNamespaces2Loc(((SchemaDocument) object).getSchema(), ignoreSpecialRefs);
        } else if (object instanceof DefinitionsDocument) {
            return getImportedNamespaces2Loc(((DefinitionsDocument) object).getDefinitions(), ignoreSpecialRefs);
        } else {
            return Collections.emptyList();
        }
    }

    public static void collectUsedEnumIds(XmlObject object, List<Id> list) {
        if (object == null) {
            return;
        }
        XmlCursor cursor = null;
        try {
            cursor = object.newCursor();
            if (cursor.toFirstChild()) {
                do {
                    XmlObject child = cursor.getObject();
                    if (child instanceof org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation) {
                        org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation a = (org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation) child;
                        org.apache.xmlbeans.impl.xb.xsdschema.AppinfoDocument.Appinfo[] appInfos = a.getAppinfoArray();
                        if (appInfos != null) {
                            for (int i = 0; i
                                    < appInfos.length; i++) {
                                if ("http://schemas.radixware.org/types.xsd".equals(appInfos[i].getSource())) {
                                    XmlObject cc = XmlObjectProcessor.getXmlObjectFirstChild(appInfos[i]);
                                    if (cc != null && cc.getDomNode().getLocalName().equals("class")) {
                                        String constSetIdStr = cc.getDomNode().getFirstChild() != null ? cc.getDomNode().getFirstChild().getNodeValue() : null;

                                        if (constSetIdStr != null) {
                                            String[] components = constSetIdStr.split("\\.");
                                            if (components.length > 0) {
                                                constSetIdStr = components[components.length - 1];
                                                if (constSetIdStr.startsWith(EDefinitionIdPrefix.ADS_ENUMERATION.getValue())) {
                                                    list.add(Id.Factory.loadFrom(constSetIdStr));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if (child != null && child != object && child.getDomNode().getNodeType() == Node.ELEMENT_NODE) {
                            collectUsedEnumIds(child, list);
                        }

                    }
                } while (cursor.toNextSibling());
            }

        } finally {
            if (cursor != null) {
                cursor.dispose();
            }
        }

    }

    public static String getTargetNamespace(XmlObject object) {
        if (object instanceof Schema) {
            return ((Schema) object).getTargetNamespace();
        } else if (object instanceof TDefinitions) {
            return ((TDefinitions) object).getTargetNamespace();
        } else if (object instanceof SchemaDocument) {
            return getTargetNamespace(((SchemaDocument) object).getSchema());
        } else if (object instanceof DefinitionsDocument) {
            return getTargetNamespace(((DefinitionsDocument) object).getDefinitions());
        } else {
            return null;
        }
    }

    public static org.w3c.dom.Element findFirstElement(org.w3c.dom.Node node) {
        for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof org.w3c.dom.Element) {
                return (org.w3c.dom.Element) child;
            }
        }
        return null;
    }

    public static org.w3c.dom.Element findChildByLocalName(org.w3c.dom.Node node, String localName) {
        for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (localName.equals(child.getLocalName())) {
                return (org.w3c.dom.Element) child;
            }
        }
        return null;
    }

    public static String getString(org.w3c.dom.Node node, String attributeName) {
        final NamedNodeMap attributes = node.getAttributes();
        if (attributes == null) {
            return null;
        }
        final Node attributeNode = attributes.getNamedItem(attributeName);
        if (attributeNode == null) {
            return null;
        }
        final String value = attributeNode.getNodeValue();
        return value;
    }

    public static Integer getInteger(org.w3c.dom.Node node, String attributeName) {
        final String value = getString(node, attributeName);
        if (value != null) {
            final Integer result = Integer.parseInt(value);
            return result;
        } else {
            return null;
        }
    }

    public static Boolean getBoolean(org.w3c.dom.Node node, String attributeName) {
        final String value = getString(node, attributeName);
        return "1".equals(value) || "true".equals(value);
    }

    public static XmlOptions getPrettyXmlOptions() {
        final XmlOptions opt = new XmlOptions();
        opt.setSaveOuter();
        opt.setCharacterEncoding(FileUtils.XML_ENCODING);
        opt.setSaveNamespacesFirst();
        opt.setSaveAggressiveNamespaces();
        opt.setUseDefaultNamespace();
        opt.setSavePrettyPrint();
        return opt;
    }

    public static void saveXmlPretty(final XmlObject doc, final File file) throws IOException {
        final XmlOptions opt = getPrettyXmlOptions();
        saveWithoutCR(doc, opt, file);
    }

    public static void saveXmlPrettyNoLock(final XmlObject doc, final File file) throws IOException {
        final XmlOptions opt = getPrettyXmlOptions();
        saveWithoutCR(new FileUtils.DefaultFileOperations(), doc, opt, file);
    }

    public static void saveXmlPretty(final XmlObject doc, final OutputStream stream) throws IOException {
        final XmlOptions opt = getPrettyXmlOptions();
        saveWithoutCR(doc, opt, stream);
    }

    public static byte[] saveXmlPretty(final XmlObject doc) throws IOException {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            final XmlOptions opt = getPrettyXmlOptions();
            saveWithoutCR(doc, opt, stream);
            return stream.toByteArray();
        }
    }

    private static void cleanup(Node node) {
        NodeList nodes = node.getChildNodes();
        if (nodes != null) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node child = nodes.item(i);
                cleanup(child);
            }
        }
        NamedNodeMap attrs = node.getAttributes();

        if (attrs != null) {
            for (int i = 0; i < attrs.getLength();) {
                Node attr = attrs.item(i);
                if (attr.getNodeName().contains("xmlns")) {
                    attrs.removeNamedItem(attr.getNodeName());
                } else {
                    i++;
                }
            }
        }
    }

    public static XmlObject removeRepeatedNamespaceDeclarations(XmlObject obj) throws XmlException {
        Node node = obj.getDomNode().cloneNode(true);
        cleanup(node);
        return XmlObject.Factory.parse(node);
    }

    private static class CRIgnoreOutputStream extends OutputStream {

        private final OutputStream target;

        public CRIgnoreOutputStream(OutputStream target) {
            this.target = target;
        }

        @Override
        public void write(int b) throws IOException {
            if (b != 0xD) {
                target.write(b);
            }
        }

        @Override
        public void write(byte[] b) throws IOException {
            for (int i = 0; i < b.length; i++) {
                write(b[i]);
            }
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            for (int i = off; i < off + len; i++) {
                write(b[i]);
            }
        }

        @Override
        public void close() throws IOException {
            target.close();
        }
    }

    public static void saveWithoutCR(FileOperations fo, XmlObject xmlObject, XmlOptions options, File file) throws IOException {
        try (OutputStream stream = fo.getOutputStream(file)) {
            saveWithoutCR(xmlObject, options, stream);
        }
    }

    public static void saveWithoutCR(XmlObject xmlObject, XmlOptions options, File file) throws IOException {
        saveWithoutCR(FileOperations.getDefault(), xmlObject, options, file);
    }

    public static void saveWithoutCR(XmlObject xmlObject, XmlOptions options, OutputStream stream) throws IOException {
        try (CRIgnoreOutputStream wrapper = new CRIgnoreOutputStream(stream)) {
            xmlObject.save(wrapper, options);
        }
    }

    /**
     * Convert string to XML 1.0 supported string: replace illegal characters by
     * '?'. RADIX-4704, http://en.wikipedia.org/wiki/Valid_characters_in_XML
     */
    public static String stringToXmlString(final String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        final char[] chars = s.toCharArray();
        boolean converted = false;
        for (int i = chars.length - 1; i >= 0; i--) {
            char c = chars[i];
            // 'The Java programming language represents text in sequences of 16-bit code units, using the UTF-16 encoding.'
            // so, characters with codes more then 0xFFFF (UTF-32) are impossible.
            if (isBadXmlChar(c)) {
                chars[i] = '?';
                converted = true;
            }
        }
        return (converted ? String.valueOf(chars) : s);
    }

    /**
     * Convert string with characters that cannot be used in XML document into
     * valid XML string. If character cannot be used in XML it will be replaced
     * with backslash and character code in UTF-8 (for hexadecimal digits).
     * Backslash character in string escaped by preceding it with another
     * backslash. http://en.wikipedia.org/wiki/Valid_characters_in_XML
     *
     * @param s string that may contains illegal characters
     * @return encoded string with valid XML characters
     */
    public static String getSafeXmlString(final String s) {
        return XmlEscapeStr.getSafeXmlString(s);
    }

    /**
     * Convert string with escaped characters into source string. Backslash
     * character and followed character code in UTF-8 (for hexadecimal digits)
     * will be replaced with single character. Double backslash characters will
     * be replaced with single backslash.
     * http://en.wikipedia.org/wiki/Valid_characters_in_XML
     *
     * @param s string from XML document
     * @return decoded string with unescaped characters
     */
    public static String parseSafeXmlString(final String s) {
        return XmlEscapeStr.parseSafeXmlString(s);
    }

    /**
     * Check if character cannot be used in XML document.
     *
     * @param c character to check for
     * @return true if character cannot be used in XML
     */
    public static boolean isBadXmlChar(final char c) {
        return !(c == 0x09 || c == 0x0A || c == 0x0D || c >= 0x20 && c <= 0xD7FF || c >= 0xE000 && c <= 0xFFFD);
    }

    public static QName[] xPathToQNames(final String xPathStr) {
        if (xPathStr == null) {
            return null;
        }
        final String nsDeclPatternStr = "\\s*declare\\s+namespace\\s+\\w+=['\"][^'\"]*['\"]\\s*;";
        final String nsDefaultDeclPatternStr = "\\s*declare\\s+default\\s+element\\s+namespace\\s+['\"][^'\"]*['\"]\\s*;";
        final String nsPatternStr = nsDeclPatternStr + "|" + nsDefaultDeclPatternStr;
        final Pattern nsPattern = Pattern.compile(nsPatternStr);
        final Pattern nsDeclPattern = Pattern.compile(nsDeclPatternStr);
        final Matcher matcher = nsPattern.matcher(xPathStr);
        final Map<String, String> prefixMap = new HashMap<>();
        int endOfLastDecl = -1;
        final String defaultNsKey = "";
        while (matcher.find()) {
            String decl = matcher.group();
            final char quote = decl.indexOf('\'') > 0 ? '\'' : '"';
            if (nsDeclPattern.matcher(decl).matches()) {
                int eqPos = decl.indexOf("=");
                int endPrefixPos = eqPos - 1;
                while (Character.isWhitespace(decl.charAt(endPrefixPos))) {
                    endPrefixPos--;
                }
                endPrefixPos++;
                int startPrefixPos = endPrefixPos - 1;
                while (!Character.isWhitespace(decl.charAt(startPrefixPos))) {
                    startPrefixPos--;
                }
                startPrefixPos++;
                prefixMap.put(decl.substring(startPrefixPos, endPrefixPos), decl.substring(decl.indexOf(quote) + 1, decl.lastIndexOf(quote)));
            } else {
                prefixMap.put(defaultNsKey, decl.substring(decl.indexOf(quote) + 1, decl.lastIndexOf(quote)));
            }
            endOfLastDecl = matcher.end();
        }

        String xPathElements = xPathStr.substring(endOfLastDecl + 1).trim();
        if (xPathElements.startsWith("/")) {
            xPathElements = xPathElements.substring(1);
        }
        final String[] elements = xPathElements.split("/");

        final QName[] result = new QName[elements.length];
        final String defaultNs = prefixMap.get(defaultNsKey);
        for (int i = 0; i < elements.length; i++) {
            final String e = elements[i].trim();
            int colonIdx = e.indexOf(":");
            if (colonIdx >= 0) {
                final String prefix = e.substring(0, colonIdx);
                if (!prefixMap.containsKey(prefix)) {
                    throw new IllegalArgumentException("Definiton of the prefix '" + prefix + "' is not found in xpath string: '" + xPathStr + "'");
                }
                result[i] = new QName(prefixMap.get(prefix), e.substring(colonIdx + 1));
            } else {
                result[i] = new QName(defaultNs == null ? "" : defaultNs, e);
            }
        }
        return result;
    }
    
    public static List<org.w3c.dom.Element> getChildElements(Node element) {
        List<org.w3c.dom.Element> childElements = new ArrayList<>();
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                childElements.add((org.w3c.dom.Element) nodeList.item(i));
            }
        }
        return childElements;
    }
    
    public static Map<String, String> getNamespacePrefixes(XmlObject obj) {
        Map<String, String> result = new HashMap<>();
        org.w3c.dom.Element schemaElement = findChildByLocalName(obj.getDomNode(), "schema");
        if (schemaElement != null) {
            NamedNodeMap attributes = schemaElement.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attribute = attributes.item(i);
                if (attribute.getNodeName().contains(XMLNS)) {
                    result.put(attribute.getLocalName(), attribute.getNodeValue());
                }
            }
        }
        
        return result;        
    }
}
