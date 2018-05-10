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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.radixdoc.IDocLogger;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.XPathUtils;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.schemas.adsdef.XmlItemDocEntries;
import org.radixware.schemas.adsdef.XmlItemDocEntry;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xmlsoap.schemas.wsdl.DefinitionsDocument;

public class XsdExportUtils {

    public static final String XML_NS = "http://www.w3.org/XML/1998/namespace";
    
    public static void embedNodeDocumentation(final XmlObject schemaObject, XmlItemDocEntries docEntries, Map<Id, String> localizedStrings, EIsoLanguage lang) {        
        for (XmlItemDocEntry entry : docEntries.getXmlItemDocEntryList()) {
            XmlObject[] docObjects = schemaObject.selectPath(entry.getNodeXPath());            
            if (docObjects == null || docObjects.length == 0) {
                continue;
            }            
            Node node = docObjects[0].getDomNode();

            boolean hasAnnotation = false;
            Element annotationElem = null;
            List<Element> nodeChildren = XmlUtils.getChildElements(node);
            for (Element childElem : nodeChildren) {
                if (childElem.getNamespaceURI().equals(XPathUtils.XML_SCHEMA_NS) && childElem.getLocalName().equals("annotation")) {
                    annotationElem = childElem;
                    hasAnnotation = true;
                    break;
                }
            }
            if (!hasAnnotation) {
                annotationElem = node.getOwnerDocument().createElementNS(XPathUtils.XML_SCHEMA_NS, "annotation");
            }

            String nodeText = localizedStrings.get(entry.getLocalizedStringId());
            if (nodeText == null || nodeText.isEmpty()) {
                continue;
            }
            
            Element documentationElem = node.getOwnerDocument().createElementNS(XPathUtils.XML_SCHEMA_NS, "documentation");
            Node documentationTextNode = node.getOwnerDocument().createTextNode(nodeText);
            
            documentationElem.setAttributeNS(XML_NS, "lang", lang.getValue());
            documentationElem.appendChild(documentationTextNode);
            annotationElem.appendChild(documentationElem);
            
            if (!hasAnnotation) {
                if (node.getFirstChild() != null) {
                    node.insertBefore(annotationElem, node.getFirstChild());
                } else {
                    node.appendChild(annotationElem);
                }
            }            
        }
    }

    public static String getSchemaNamespace(XmlObject schemaObject) {
        String namespace = null;
        if (schemaObject instanceof SchemaDocument) {
            SchemaDocument xsdSchema = (SchemaDocument) schemaObject;
            if (xsdSchema.getSchema().getTargetNamespace() != null) {
                namespace = xsdSchema.getSchema().getTargetNamespace();
            }
        } else {
            DefinitionsDocument wsldSchema = (DefinitionsDocument) schemaObject;
            if (wsldSchema.getDefinitions().getTargetNamespace() != null) {
                namespace = wsldSchema.getDefinitions().getTargetNamespace();
            }
        }
        return namespace;
    }

    public static XmlObject getSchemaObject(IExportableXmlSchema schema, IDocLogger logger) {
        XmlObject schemaObject = null;
        if (schema.getInputStream() == null) {
            return null;
        }
        try {
            schemaObject = SchemaDocument.Factory.parse(schema.getInputStream());
        } catch (XmlException ex) {
            if (schemaObject == null) {
                try {
                    schemaObject = DefinitionsDocument.Factory.parse(schema.getInputStream());
                } catch (XmlException | IOException ex1) {
                    Logger.getLogger(XmlSchemaExporter.class.getName()).log(Level.SEVERE, null, ex1);
                    if (logger != null) {
                        logger.put(EEventSeverity.ERROR, ExceptionTextFormatter.exceptionStackToString(ex));
                    }
                }
            } else {
                Logger.getLogger(XmlSchemaExporter.class.getName()).log(Level.SEVERE, "Can not parse schema", ex);
                if (logger != null) {
                    logger.put(EEventSeverity.ERROR, ExceptionTextFormatter.exceptionStackToString(ex));
                    logger.put(EEventSeverity.ERROR, "EXPORT FAILED!");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(XmlSchemaExporter.class.getName()).log(Level.SEVERE, null, ex);
            if (logger != null) {
                logger.put(EEventSeverity.ERROR, ExceptionTextFormatter.exceptionStackToString(ex));
                logger.put(EEventSeverity.ERROR, "EXPORT FAILED!");
            }
        }
        return schemaObject;
    }

    public static File getTempSchemasDir(IDocLogger logger) {
        File result = null;
        try {
            result = File.createTempFile("xsdExport", "");
            result.delete();
            result.mkdirs();
        } catch (IOException ex) {
            if (logger != null) {
                logger.put(EEventSeverity.ERROR, "Unable to create temporary directory for XSD schemas");
            }
            Logger.getLogger(XmlSchemaExporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static void copyXsdFromDirToZip(File tmpDir, ZipOutputStream zipStream, IDocLogger logger) {
        if (tmpDir == null || !tmpDir.exists() || !tmpDir.isDirectory()) {
            return;
        }

        for (File schemaFile : tmpDir.listFiles()) {
            try {
                if (schemaFile.isDirectory()) {
                    continue;
                }

                zipStream.putNextEntry(new ZipEntry(schemaFile.getName()) {
                    {
                        setMethod(ZipEntry.DEFLATED);
                    }
                });

                try (FileInputStream fis = new FileInputStream(schemaFile)) {
                    FileUtils.copyStream(fis, zipStream);
                }

                if (logger != null) {
                    logger.put(EEventSeverity.DEBUG, "Packed " + schemaFile.getName());
                }
            } catch (IOException ex) {
                Logger.getLogger(XsdExportUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static List<IExportableXmlSchema> getSchemaIncludes(final IExportableXmlSchema schema, IDocLogger logger) {
        List<IExportableXmlSchema> result = new ArrayList<>();

        XmlObject schemaObject = getSchemaObject(schema, logger);
        if (schemaObject == null) {
            return result;
        }

        for (String location : XmlUtils.getIncludedLocations(schemaObject)) {
            XmlObject include = schema.findKernelSchema(location, getSchemaNamespace(schemaObject));
            result.add(new KernelXmlSchemaExportableWrapper(include, location) {

                @Override
                public XmlObject findKernelSchema(String name, String namespace) {
                    return schema.findKernelSchema(name, namespace);
                }
            });
        }

        return result;
    }
}
