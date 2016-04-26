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
package org.radixware.kernel.server.utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.apache.xmlbeans.impl.xb.xsdschema.Annotated;
import org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.apache.xmlbeans.impl.xb.xsdschema.AttributeGroupRef;
import org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument.ComplexContent;
import org.apache.xmlbeans.impl.xb.xsdschema.ComplexType;
import org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation;
import org.apache.xmlbeans.impl.xb.xsdschema.Element;
import org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup;
import org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType;
import org.apache.xmlbeans.impl.xb.xsdschema.GroupRef;
import org.apache.xmlbeans.impl.xb.xsdschema.LocalElement;
import org.apache.xmlbeans.impl.xb.xsdschema.NamedAttributeGroup;
import org.apache.xmlbeans.impl.xb.xsdschema.NamedGroup;
import org.apache.xmlbeans.impl.xb.xsdschema.RestrictionType;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelElement;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelSimpleType;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.arte.Arte;
import org.xmlsoap.schemas.wsdl.DefinitionsDocument;
import org.xmlsoap.schemas.wsdl.TMessage;
import org.xmlsoap.schemas.wsdl.TOperation;
import org.xmlsoap.schemas.wsdl.TPart;
import org.xmlsoap.schemas.wsdl.TPortType;

public class XPathUtils {

    public static class NodeInfo {

        public final XmlObject node;
        public final XmlObject context;

        public NodeInfo(XmlObject node, XmlObject context) {
            this.node = node;
            this.context = context;
        }
    }
    public static final String XML_SCHEMA_NS = "http://www.w3.org/2001/XMLSchema";
    private final Arte arte;

    public XPathUtils(Arte arte) {
        this.arte = arte;
    }
    private static final Map<Long, Map<String, XmlObject>> ns2XmlByVersion = new HashMap();

    public final XmlObject findXmlDefinition(String ns) {
        Map<String, XmlObject> ns2Xml;
        synchronized (ns2XmlByVersion) {
            ns2Xml = ns2XmlByVersion.get(arte.getVersion());
        }
        if (ns2Xml == null) {
            ns2Xml = new HashMap<>();
            for (Id id : arte.getDefManager().getDefIdsByType(EDefType.XML_SCHEME)) {
                final String fileName = arte.getDefManager().getReleaseCache().getRelease().getRepository().getAdsIndices().getXmlFileNameByMsdlSchemaDefId(id);
                final InputStream is = arte.getDefManager().getClassLoader().getResourceAsStream(fileName);
                if (is != null) {
                    try {
                        final SchemaDocument xsd = SchemaDocument.Factory.parse(is);
                        if (xsd != null) {
                            ns2Xml.put(xsd.getSchema().getTargetNamespace(), xsd);
                        }
                        continue;
                    } catch (Exception ex) {
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                    try {
                        is.reset();
                    } catch (IOException e) {
                        continue;
                    }
                    try {
                        final DefinitionsDocument wsdl = DefinitionsDocument.Factory.parse(is);
                        if (wsdl != null) {
                            ns2Xml.put(wsdl.getDefinitions().getTargetNamespace(), wsdl);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                }
            }
        }
        synchronized (ns2XmlByVersion) {
            ns2XmlByVersion.put(arte.getVersion(), ns2Xml);
            final Iterator<Long> it = ns2XmlByVersion.keySet().iterator();
            while (it.hasNext()) {
                if (it.next() != arte.getActualVersion()) {
                    it.remove();
                }
            }
        }
        return ns2Xml.get(ns);
    }

    public final List<NodeInfo> getAvailableChildren(XmlObject schemaNode, String targetNamespace, XmlObject context) {
        if (schemaNode instanceof TOperation) {
            ArrayList<NodeInfo> result = new ArrayList<NodeInfo>();
            DefinitionsDocument doc = (DefinitionsDocument) context;
            TOperation operation = (TOperation) schemaNode;
            if (operation.getInput() != null) {
                for (TMessage mess : doc.getDefinitions().getMessageList()) {
                    if (mess.getName() != null && mess.getName().equals(operation.getInput().getMessage().getLocalPart())) {
                        for (TPart part : mess.getPartList()) {
                            if (part.getName() != null) {
                                String typeNS = part.getType().getNamespaceURI();
                                XmlObject typeScheme = findXmlDefinition(typeNS);
                                if (typeScheme != null) {
                                    result.add(new NodeInfo(part, typeScheme));
                                }
                            }
                        }
                    }
                }
            }
            if (operation.getOutput() != null) {
                for (TMessage mess : doc.getDefinitions().getMessageList()) {
                    if (mess.getName() != null && mess.getName().equals(operation.getOutput().getMessage().getLocalPart())) {
                        for (TPart part : mess.getPartList()) {
                            if (part.getName() != null) {
                                String typeNS = part.getType().getNamespaceURI();
                                XmlObject typeScheme = findXmlDefinition(typeNS);
                                if (typeScheme != null) {
                                    result.add(new NodeInfo(part, typeScheme));
                                }
                            }
                        }
                    }
                }
            }
            return result;
        } else if (schemaNode instanceof TPart) {
            String type = ((TPart) schemaNode).getType().getLocalPart();
            if (type != null) {
                NodeInfo complexType = findTopLevelComplexType(context, type);
                if (complexType != null) {
                    return getComplexTypeChildren((TopLevelComplexType) complexType.node, complexType.context);
                }
            }
            return Collections.emptyList();
        } else if (schemaNode instanceof Element) {
            return getElementChildren((Element) schemaNode, targetNamespace, context);
        } else {
            return Collections.emptyList();
        }
    }

    public final Integer getMaxOccurs(XmlObject doc) {
        if (doc instanceof Element) {
            Element element = (Element) doc;
            if (element.getMaxOccurs() != null) {
                if (element.getMaxOccurs() instanceof BigInteger) {
                    return ((BigInteger) element.getMaxOccurs()).intValue();
                } else {
                    return -1;
                }
            }
        }
        return 0;
    }

    public final Integer getMinOccurs(XmlObject doc) {
        if (doc instanceof Element) {
            Element element = (Element) doc;
            if (element.getMinOccurs() != null) {
                return element.getMinOccurs().intValue();
            }
        }
        return -1;
    }

    public final List<NodeInfo> getElementChildren(Element element, String targetNamespace, XmlObject context) {
        if (element.getType() != null && !element.getType().getLocalPart().isEmpty()) {
            String type = element.getType().getLocalPart();
            String typeNS = element.getType().getNamespaceURI();
            if (!typeNS.isEmpty()
                    && !typeNS.equals(targetNamespace)
                    && !typeNS.equals(XML_SCHEMA_NS)) {
                XmlObject typeScheme = findXmlDefinition(typeNS);
                if (typeScheme != null) {
                    NodeInfo complexType = findTopLevelComplexType(typeScheme, type);
                    if (complexType != null) {
                        return getComplexTypeChildren((TopLevelComplexType) complexType.node, complexType.context);
                    }
                }
            } else {
                NodeInfo complexType = findTopLevelComplexType(context, type);
                if (complexType != null) {
                    return getComplexTypeChildren((TopLevelComplexType) complexType.node, complexType.context);
                }
            }
        } else if (element.getComplexType() != null) {
            return getComplexTypeChildren(element.getComplexType(), context);
        } else if (element.getRef() != null) {
            QName ref = element.getRef();
            String refNS = ref.getNamespaceURI();
            if (refNS.equals(targetNamespace)) {
                NodeInfo reference = findTopLevelElement(context, ref.getLocalPart());
                if (reference != null) {
                    return getElementChildren((Element) reference.node, targetNamespace, reference.context);
                }
            } else {
                XmlObject refSchema = findXmlDefinition(refNS);
                if (refSchema != null) {
                    NodeInfo reference = findTopLevelElement(refSchema, ref.getLocalPart());
                    if (reference != null) {
                        return getElementChildren((Element) reference.node, refNS, reference.context);
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    private void addAttributes(Attribute[] attributes, List<NodeInfo> result, XmlObject context) {
        for (Attribute a : attributes) {
            if (a.getRef() != null) {
                NodeInfo top = findTopLevelAttribute(context, a.getRef());
                result.add(top);
            } else {
                result.add(new NodeInfo(a, context));
            }
        }
    }

    private void addAttributesGroups(AttributeGroupRef[] groups, List<NodeInfo> result, XmlObject context) {
        for (AttributeGroupRef ref : groups) {
            NodeInfo namedGroup = findAttributesGroupByRef(context, ref.getRef());
            if (namedGroup != null) {
                for (Attribute a : ((NamedAttributeGroup) namedGroup.node).getAttributeArray()) {
                    result.add(new NodeInfo(a, namedGroup.context));
                }
            }
        }
    }

    public final List<NodeInfo> getComplexTypeAttributes(ComplexType ct, XmlObject context) {
        LinkedList<NodeInfo> result = new LinkedList<NodeInfo>();
        if (ct.getComplexContent() != null) {
            ComplexContent complex = ct.getComplexContent();
            ExtensionType extenstion = complex.getExtension();
            RestrictionType restriction = complex.getRestriction();

            if (extenstion != null) {
                QName baseTypeName = extenstion.getBase();
                String targetNS = getSchemaNodeTargetNamespace(context);
                if (baseTypeName.getNamespaceURI().equals(targetNS)) {
                    NodeInfo baseType = findTopLevelComplexType(context, baseTypeName.getLocalPart());
                    if (baseType != null) {
                        result.addAll(getComplexTypeAttributes((ComplexType) baseType.node, baseType.context));
                    }
                } else {
                    XmlObject baseSchema = findXmlDefinition(baseTypeName.getNamespaceURI());
                    if (baseSchema != null) {
                        NodeInfo baseType = findTopLevelComplexType(baseSchema, baseTypeName.getLocalPart());
                        if (baseType != null) {
                            result.addAll(getComplexTypeAttributes((ComplexType) baseType.node, baseType.context));
                        }
                    }
                }

                Attribute[] attributes = extenstion.getAttributeArray();
                addAttributes(attributes, result, context);

                AttributeGroupRef[] attributesGroupRefs = extenstion.getAttributeGroupArray();
                addAttributesGroups(attributesGroupRefs, result, context);

            } else if (restriction != null) {
                addAttributes(restriction.getAttributeArray(), result, context);
                addAttributesGroups(restriction.getAttributeGroupArray(), result, context);
            }

        } else {
            Attribute[] attributes = ct.getAttributeArray();
            addAttributes(attributes, result, context);

            AttributeGroupRef[] attributesGroupRefs = ct.getAttributeGroupArray();
            addAttributesGroups(attributesGroupRefs, result, context);
        }

        return result;
    }

    public final List<NodeInfo> getComplexTypeElements(ComplexType ct, XmlObject context) {
        LinkedList<NodeInfo> result = new LinkedList<NodeInfo>();
        if (ct.getComplexContent() != null) {
            ComplexContent complex = ct.getComplexContent();
            ExtensionType extenstion = complex.getExtension();
            RestrictionType restriction = complex.getRestriction();

            if (extenstion != null) {
                QName baseTypeName = extenstion.getBase();
                String targetNS = getSchemaNodeTargetNamespace(context);
                if (baseTypeName.getNamespaceURI().equals(targetNS)) {
                    NodeInfo baseType = findTopLevelComplexType(context, baseTypeName.getLocalPart());
                    if (baseType != null) {
                        result.addAll(getComplexTypeElements((ComplexType) baseType.node, baseType.context));
                    }
                } else {
                    XmlObject baseSchema = findXmlDefinition(baseTypeName.getNamespaceURI());
                    if (baseSchema != null) {
                        NodeInfo baseType = findTopLevelComplexType(baseSchema, baseTypeName.getLocalPart());
                        if (baseType != null) {
                            result.addAll(getComplexTypeElements((ComplexType) baseType.node, baseType.context));
                        }
                    }
                }

                if (extenstion.getSequence() != null) {
                    result.addAll(getExplicitGroupChildren(context, extenstion.getSequence()));
                } else if (extenstion.getChoice() != null) {
                    result.addAll(getExplicitGroupChildren(context, extenstion.getChoice()));
                } else if (extenstion.getGroup() != null) {
                    NamedGroup group = (NamedGroup) findElementsGroupByRef(context, extenstion.getGroup().getRef());
                    if (group != null) {
                        result.addAll(getTopLevelGroupChildren(context, group));
                    }
                } else if (extenstion.getAll() != null) {
                    result.addAll(getExplicitGroupChildren(context, extenstion.getAll()));
                }

            } else if (restriction != null) {
                if (restriction.getSequence() != null) {
                    result.addAll(getExplicitGroupChildren(context, restriction.getSequence()));
                } else if (restriction.getChoice() != null) {
                    result.addAll(getExplicitGroupChildren(context, restriction.getChoice()));
                } else if (restriction.getGroup() != null) {
                    NamedGroup group = (NamedGroup) findElementsGroupByRef(context, restriction.getGroup().getRef());
                    if (group != null) {
                        result.addAll(getTopLevelGroupChildren(context, group));
                    }
                } else if (restriction.getAll() != null) {
                    result.addAll(getExplicitGroupChildren(context, restriction.getAll()));
                }
            }
        } else {
            if (ct.getSequence() != null) {
                result.addAll(getExplicitGroupChildren(context, ct.getSequence()));
            } else if (ct.getChoice() != null) {
                result.addAll(getExplicitGroupChildren(context, ct.getChoice()));
            } else if (ct.getGroup() != null) {
                NamedGroup group = (NamedGroup) findElementsGroupByRef(context, ct.getGroup().getRef());
                if (group != null) {
                    result.addAll(getTopLevelGroupChildren(context, group));
                }
            } else if (ct.getAll() != null) {
                result.addAll(getExplicitGroupChildren(context, ct.getAll()));
            }
        }

        return result;
    }

    public final List<NodeInfo> getComplexTypeChildren(ComplexType ct, XmlObject context) {
        LinkedList<NodeInfo> result = new LinkedList<NodeInfo>();
        result.addAll(getComplexTypeAttributes(ct, context));
        result.addAll(getComplexTypeElements(ct, context));
        return result;
    }

    public final List<NodeInfo> getExplicitGroupChildren(XmlObject context, ExplicitGroup group) {
        LinkedList<NodeInfo> result = new LinkedList<NodeInfo>();

        LocalElement[] elements = group.getElementArray();
        for (LocalElement le : elements) {
            result.add(new NodeInfo(le, context));
        }

        ExplicitGroup[] sequences = group.getSequenceArray();
        for (ExplicitGroup gr : sequences) {
            result.addAll(getExplicitGroupChildren(context, gr));
        }

        ExplicitGroup[] choices = group.getChoiceArray();
        for (ExplicitGroup gr : choices) {
            result.addAll(getExplicitGroupChildren(context, gr));
        }

        ExplicitGroup[] allGroups = group.getAllArray();
        for (ExplicitGroup gr : allGroups) {
            result.addAll(getExplicitGroupChildren(context, gr));
        }

        GroupRef[] groupRefs = group.getGroupArray();
        for (GroupRef ref : groupRefs) {
            NamedGroup namedGroup = (NamedGroup) findElementsGroupByRef(context, ref.getRef());
            result.addAll(getTopLevelGroupChildren(context, namedGroup));
        }

        return result;
    }

    public final List<NodeInfo> getTopLevelGroupChildren(XmlObject context, NamedGroup group) {
        LinkedList<NodeInfo> result = new LinkedList<NodeInfo>();
        if (group.getAllArray() != null && group.getAllArray().length > 0) {
            All all = group.getAllArray(0);
            result.addAll(getExplicitGroupChildren(context, all));
        } else if (group.getSequenceArray() != null && group.getSequenceArray().length > 0) {
            ExplicitGroup seq = group.getSequenceArray(0);
            result.addAll(getExplicitGroupChildren(context, seq));
        } else if (group.getChoiceArray() != null && group.getChoiceArray().length > 0) {
            ExplicitGroup choice = group.getChoiceArray(0);
            result.addAll(getExplicitGroupChildren(context, choice));
        }
        return result;
    }

    public final NodeInfo findElementsGroupByRef(XmlObject context, QName ref) {
        if (context != null) {
            assert (context instanceof SchemaDocument);

            SchemaDocument doc = (SchemaDocument) context;
            String ts = doc.getSchema().getTargetNamespace();

            if (ref.getNamespaceURI().isEmpty()
                    || ts.equals(ref.getNamespaceURI())) {
                NamedGroup[] groups = doc.getSchema().getGroupArray();
                for (NamedGroup ng : groups) {
                    if (ng.getName().equals(ref.getLocalPart())) {
                        return new NodeInfo(ng, context);
                    }
                }
            } else {
                XmlObject importedSchema = findXmlDefinition(ref.getNamespaceURI());
                if (importedSchema != null) {
                    return findElementsGroupByRef(importedSchema, ref);
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public final NodeInfo findAttributesGroupByRef(XmlObject context, QName qName) {
        if (context != null) {
            XmlObject doc = context;
            if (doc instanceof SchemaDocument) {
                SchemaDocument schemaDoc = (SchemaDocument) doc;
                String ts = schemaDoc.getSchema().getTargetNamespace();
                if (ts.equals(qName.getNamespaceURI())
                        || qName.getNamespaceURI().isEmpty()) {
                    NamedAttributeGroup[] groups = schemaDoc.getSchema().getAttributeGroupArray();
                    for (NamedAttributeGroup g : groups) {
                        if (g.getName().equals(qName.getLocalPart())) {
                            return new NodeInfo(g, context);
                        }
                    }
                } else {
                    XmlObject importedSchema = findXmlDefinition(qName.getNamespaceURI());
                    if (importedSchema != null) {
                        return findAttributesGroupByRef(importedSchema, qName);
                    } else {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    public final NodeInfo findTopLevelAttribute(XmlObject context, QName ref) {
        if (context != null) {
            assert (context instanceof SchemaDocument);

            SchemaDocument doc = (SchemaDocument) context;
            String ts = doc.getSchema().getTargetNamespace();

            if (ref.getNamespaceURI().isEmpty() || !ts.equals(ref.getNamespaceURI())) {
                Attribute[] attrs = doc.getSchema().getAttributeArray();
                for (Attribute a : attrs) {
                    if (a.getName().equals(ref.getLocalPart())) {
                        return new NodeInfo(a, context);
                    }
                }
            } else {
                XmlObject importedSchema = findXmlDefinition(ref.getNamespaceURI());
                if (importedSchema != null) {
                    return findAttributesGroupByRef(importedSchema, ref);
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public final NodeInfo findTopLevelComplexType(XmlObject schema, String name) {
        if (schema instanceof SchemaDocument) {
            TopLevelComplexType[] types = ((SchemaDocument) schema).getSchema().getComplexTypeArray();
            for (TopLevelComplexType ct : types) {
                if (ct.getName().equals(name)) {
                    return new NodeInfo(ct, schema);
                }
            }
        }
        return null;
    }

    public final XmlObject findTopLevelSimpleType(XmlObject schema, String name) {
        if (schema instanceof SchemaDocument) {
            TopLevelSimpleType[] types = ((SchemaDocument) schema).getSchema().getSimpleTypeArray();
            for (TopLevelSimpleType s : types) {
                if (s.getName().equals(name)) {
                    return s;
                }
            }
        }
        return null;
    }

    public final NodeInfo findTopLevelElement(XmlObject context, SchemaDocument schema, String name) {
        if (schema != null) {
            TopLevelElement[] elements = schema.getSchema().getElementArray();
            for (TopLevelElement tle : elements) {
                if (tle.getName().equals(name)) {
                    return new NodeInfo(tle, context);
                }
            }
        }
        return null;
    }

    public final NodeInfo findTopLevelElement(XmlObject schema, String name) {
        if (schema instanceof SchemaDocument) {
            return findTopLevelElement(schema, (SchemaDocument) schema, name);
        }
        return null;
    }

    public final List<NodeInfo> getTopLevelElements(XmlObject context, XmlObject adsScheme) {
        assert (adsScheme instanceof SchemaDocument || adsScheme instanceof DefinitionsDocument);

        if (adsScheme instanceof SchemaDocument) {
            TopLevelElement[] elements = ((SchemaDocument) adsScheme).getSchema().getElementArray();
            if (elements.length > 0) {
                ArrayList<NodeInfo> result = new ArrayList<NodeInfo>();
                for (int i = 0, size = elements.length - 1; i <= size; i++) {
                    result.add(new NodeInfo(elements[i], context));
                }
                return result;
            }
        }

        if (adsScheme instanceof DefinitionsDocument) {
            ArrayList<NodeInfo> result = new ArrayList<NodeInfo>();
            for (TPortType port : ((DefinitionsDocument) adsScheme).getDefinitions().getPortTypeList()) {
                for (TOperation operation : port.getOperationList()) {
                    if (operation.getName() != null) {
                        result.add(new NodeInfo(operation, context));
                    }
                }
            }
            return result;
        }

        return Collections.emptyList();
    }

    public final String getDisplayName(XmlObject item) {
        String name = "<Not Defined>";
        if (item != null) {
            if (item instanceof Element) {
                return ((Element) item).getName();
            }
            if (item instanceof TOperation) {
                return ((TOperation) item).getName();
            }
            if (item instanceof TPart) {
                return ((TPart) item).getName();
            }
            if (item instanceof Attribute) {
                return ((Attribute) item).getName();
            }
        }
        return name;
    }

    public final boolean isAttribute(XmlObject item) {
        return item instanceof Attribute;
    }

    public final boolean isSchemaDocument(XmlObject doc) {
        return doc instanceof SchemaDocument;
    }

    public final boolean isDefinitionsDocument(XmlObject doc) {
        return doc instanceof DefinitionsDocument;
    }

    public final String getSchemaNodeTargetNamespace(XmlObject doc) {
        if (doc instanceof SchemaDocument) {
            return ((SchemaDocument) doc).getSchema().getTargetNamespace();
        }
        if (doc instanceof DefinitionsDocument) {
            return ((DefinitionsDocument) doc).getDefinitions().getTargetNamespace();
        }
        return "<unknown>";
    }

    public final QName getQName(XmlObject doc, String targetNS) {
        if (doc instanceof Element) {
            Element element = (Element) doc;
            String name = element.getName();
            return new QName(targetNS, name);
        }
        if (doc instanceof TOperation) {
            TOperation operation = (TOperation) doc;
            String name = operation.getName();
            return new QName(targetNS, name);
        }
        if (doc instanceof TPart) {
            TPart part = (TPart) doc;
            String name = part.getName();
            return new QName(targetNS, name);
        }
        if (doc instanceof Attribute) {
            Attribute attr = (Attribute) doc;
            String name = attr.getName();
            if (attr.getType() != null && !attr.getType().getNamespaceURI().equals(XML_SCHEMA_NS)) {
                return new QName(attr.getType().getNamespaceURI(), name);
            }

            return new QName(targetNS, name);
        }
        return new QName("");
    }

    public final String getAnnotation(XmlObject doc) {
        if (doc instanceof SchemaDocument) {
            Schema schemaDoc = ((SchemaDocument) doc).getSchema();
            if (schemaDoc != null && schemaDoc.getAnnotationArray() != null) {
                Annotation[] anntotations = schemaDoc.getAnnotationArray();
                if (anntotations.length > 0) {
                    Annotation first = anntotations[0];
                    if (first.getDocumentationArray() != null && first.getDocumentationArray().length > 0) {
                        Documentation documentation = first.getDocumentationArray()[0];
                        XmlCursor cursor = documentation.newCursor();
                        return cursor.getTextValue();
                    }
                }
            }
        } else if (doc instanceof Annotated) {
            Annotated element = (Annotated) doc;
            if (element.getAnnotation() != null) {
                Annotation annotation = element.getAnnotation();
                if (annotation.getDocumentationArray() != null && annotation.getDocumentationArray().length > 0) {
                    Documentation documentation = annotation.getDocumentationArray()[0];
                    XmlCursor cursor = documentation.newCursor();
                    return cursor.getTextValue();
                }
            }
        }
        return null;
    }
}
