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
package org.radixware.kernel.common.defs.ads.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaAttributeGroup;
import org.apache.xmlbeans.SchemaGlobalAttribute;
import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaIdentityConstraint;
import org.apache.xmlbeans.SchemaModelGroup;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlSaxHandler;
import org.apache.xmlbeans.impl.config.BindingConfigImpl;
import org.apache.xmlbeans.impl.schema.BuiltinSchemaTypeSystem;
import org.apache.xmlbeans.impl.schema.SchemaTypeSystemImpl;
import org.apache.xmlbeans.impl.schema.StscChecker;
import org.apache.xmlbeans.impl.schema.AdsStscUtils;
import org.apache.xmlbeans.impl.schema.StscImporter;
import org.apache.xmlbeans.impl.schema.StscJavaizer;
import org.apache.xmlbeans.impl.schema.StscResolver;
import org.apache.xmlbeans.impl.schema.StscState;
import org.apache.xmlbeans.impl.schema.StscTranslator;
import org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument;
import org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig;
import org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import java.util.logging.Logger;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.ProblemAnnotationFactory.TextPositionAnnotation;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.XmlType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.XmlObjectProcessor;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class AdsXbeansTypeLoader implements SchemaTypeLoader {

    private static final String XSNS = "http://www.w3.org/2001/XMLSchema";

    private ConfigDocument generateConfig() {
        ConfigDocument xDoc = ConfigDocument.Factory.newInstance();
        ConfigDocument.Config xConf = xDoc.addNewConfig();
        Extensionconfig xExt = xConf.addNewExtension();

        xExt.setFor("*");
        Extensionconfig.Interface iface = xExt.addNewInterface();
        iface.setName("org.radixware.kernel.common.build.xbeans.IXBeansChangeEmitter");
        iface.setStaticHandler("org.radixware.kernel.common.build.xbeans.XBeansChangeEmitterHandler");

        xExt = xConf.addNewExtension();
        xExt.setFor("*");
        Extensionconfig.PrePostSet prepost = xExt.addNewPrePostSet();
        prepost.setStaticHandler("org.radixware.kernel.common.build.xbeans.XBeansChangeEmitterHandler");

        final AbstractXmlDefinition xdef = context;

        AdsType xmlType = xdef.getType(EValType.XML, null);

        final String configPrefix = String.valueOf(((XmlType) xmlType).getJavaSourceSupport().getQualifiedPackageName(null, false));

        if (xdef instanceof AdsXmlSchemeDef) {
            AdsXmlSchemeDef scheme = (AdsXmlSchemeDef) xdef;
            if (scheme.isTransparent()) {
                return null;
            }
        }
        final Nsconfig xNs = xConf.addNewNamespace();
        xNs.setUri(xdef.getTargetNamespace());
        xNs.setPackage(configPrefix);

        if (xdef instanceof AdsMsdlSchemeDef) {
            xExt = xConf.addNewExtension();
            xExt.setFor(configPrefix + ".MessageInstanceDocument");
            Extensionconfig.Interface xInt = xExt.addNewInterface();
            xInt.setName("org.radixware.kernel.common.msdl.XmlObjectMessagingInterface");
            xInt.setStaticHandler("org.radixware.kernel.common.msdl.XmlObjectMessagingHandler");
        }
        return xDoc;
    }
    private AbstractXmlDefinition context;

    public AdsXbeansTypeLoader(AbstractXmlDefinition context) {
        this.context = context;

    }

    private void prepareSchemeToCodeGeneration(XmlObject object) {
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
                            for (int i = 0; i < appInfos.length; i++) {
                                if ("http://schemas.radixware.org/types.xsd".equals(appInfos[i].getSource())) {
                                    XmlObject cc = XmlObjectProcessor.getXmlObjectFirstChild(appInfos[i]);
                                    if (cc.getDomNode().getLocalName().equals("class")) {
                                        String constSetIdStr = cc.getDomNode().getFirstChild() != null ? cc.getDomNode().getFirstChild().getNodeValue() : null;
                                        if (constSetIdStr != null) {
                                            Id constSetId = Id.Factory.loadFrom(constSetIdStr);
                                            AdsDefinition enumDef = AdsSearcher.Factory.newAdsDefinitionSearcher(context).findById(constSetId).get();
                                            if (enumDef instanceof AdsEnumDef) {
                                                AdsEnumDef e = (AdsEnumDef) enumDef;
                                                cc.getDomNode().getFirstChild().setNodeValue(new String(e.getType(e.getItemType(), null).getJavaSourceSupport().getQualifiedTypeName(JavaSourceSupport.UsagePurpose.getPurpose(ERuntimeEnvironmentType.COMMON, JavaSourceSupport.CodeType.EXCUTABLE), false)));
                                                if (cc.getDomNode() instanceof Element) {
                                                    Node node = cc.getDomNode().getOwnerDocument().createAttribute("classId");
                                                    ((Element) cc.getDomNode()).getAttributes().setNamedItem(node);
                                                    node.setNodeValue(constSetIdStr);
                                                    //((Element) cc.getDomNode()).setAttribute("classId", constSetIdStr);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if (child != null && child != object && child.getDomNode().getNodeType() == Node.ELEMENT_NODE) {
                            prepareSchemeToCodeGeneration(child);
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
    private IProblemHandler contextProblemHandler;

    private static final Lock resolveLock = new ReentrantLock();

    public SchemaTypeSystem resolve(IProblemHandler handler) {
        while (true) {
            if (resolveLock.tryLock()) {
                try {
                    this.contextProblemHandler = handler;
                    final List<XmlError> errors = new LinkedList<>();
                    final StscState state = AdsStscUtils.create();
                    try {
                        state.setErrorListener(errors);
                        state.setImportingTypeLoader(this);
                        SchemaDocument.Schema[] schemas = context.lookForSchema(context.getXmlDocument());
                        if (schemas == null) {
                            return null;
                        }
                        StscImporter.SchemaToProcess[] xsds = new StscImporter.SchemaToProcess[schemas.length];

                        for (int i = 0; i < schemas.length; i++) {
                            prepareSchemeToCodeGeneration(schemas[i]);
                            xsds[i] = new StscImporter.SchemaToProcess(schemas[i], null);
                        }
                        StscTranslator.addAllDefinitions(xsds);

                        state.setBindingConfig(BindingConfigImpl.forConfigDocuments(new ConfigDocument.Config[]{generateConfig().getConfig()}, null, null));

                        StscResolver.resolveAll();
                        StscChecker.checkAll();
                        StscJavaizer.javaizeAllTypes(true);

                        SchemaTypeSystemImpl system = new SchemaTypeSystemImpl(context.getId().toString());
                        system.loadFromStscState(StscState.get());

                        if (handler != null && !errors.isEmpty()) {
                            for (XmlError e : errors) {
                                handler.accept(RadixProblem.Factory.newError(context, e.getMessage(), new TextPositionAnnotation(e.getLine(), e.getColumn())));
                            }
                            return null;
                        }

                        return system;
                    } finally {
                        StscState.end();
                        contextProblemHandler = null;
                    }
                } finally {
                    resolveLock.unlock();
                }
            } else {
                try {
                    Logger.getLogger(AdsXbeansTypeLoader.class.getName()).log(Level.FINE, "XSD TS resolver is locked by concurrent execution thread. Sleep 100 ms before try again");
                    Thread.currentThread().sleep(100);
                } catch (InterruptedException ex) {
                    return null;
                }
            }
        }
    }
    private static final SchemaTypeSystem[] NO_STS = new SchemaTypeSystem[0];

    private SchemaTypeSystem[] get(String namespace) {
        if (XSNS.equals(namespace)) {
            return new SchemaTypeSystem[]{BuiltinSchemaTypeSystem.get()};
        } else {
            IXmlDefinition def = AdsSearcher.Factory.newXmlDefinitionSearcher(context).findByNs(namespace).get();
            if (def instanceof AbstractXmlDefinition) {
                return ((AbstractXmlDefinition) def).buildXBeansTs(contextProblemHandler);
            } else {
                return NO_STS;
            }
        }
    }

    @Override
    public SchemaType findType(QName qname) {
        //check qname namespace to avoid cycles
        String targetNamespace = context.getTargetNamespace();
        String qnameNamespace = qname.getNamespaceURI();
        if(Utils.equals(qnameNamespace, targetNamespace)){
            //looking for type in schema currently being constructed.
            //do not call nested findType()
            return null;
        }
        SchemaTypeSystem[] sts = get(qname.getNamespaceURI());
        if (sts != null) {
            for (SchemaTypeSystem ts : sts) {
                SchemaType type = ts.findType(qname);
                if (type != null) {
                    return type;
                }
            }
        }
        return null;
    }

    @Override
    public SchemaType findDocumentType(QName qname) {
        SchemaTypeSystem[] sts = get(qname.getNamespaceURI());
        if (sts != null) {
            for (SchemaTypeSystem ts : sts) {
                SchemaType type = ts.findDocumentType(qname);
                if (type != null) {
                    return type;
                }
            }
        }
        return null;
    }

    @Override
    public SchemaType findAttributeType(QName qname) {
        SchemaTypeSystem[] sts = get(qname.getNamespaceURI());
        if (sts != null) {
            for (SchemaTypeSystem ts : sts) {
                SchemaType type = ts.findAttributeType(qname);
                if (type != null) {
                    return type;
                }
            }
        }
        return null;
    }

    @Override
    public SchemaGlobalElement findElement(QName qname) {
        SchemaTypeSystem[] sts = get(qname.getNamespaceURI());
        if (sts != null) {
            for (SchemaTypeSystem ts : sts) {
                SchemaGlobalElement type = ts.findElement(qname);
                if (type != null) {
                    return type;
                }
            }
        }
        return null;
    }

    @Override
    public SchemaGlobalAttribute findAttribute(QName qname) {
        SchemaTypeSystem[] sts = get(qname.getNamespaceURI());
        if (sts != null) {
            for (SchemaTypeSystem ts : sts) {
                SchemaGlobalAttribute type = ts.findAttribute(qname);
                if (type != null) {
                    return type;
                }
            }
        }
        return null;
    }

    @Override
    public SchemaModelGroup findModelGroup(QName qname) {
        SchemaTypeSystem[] sts = get(qname.getNamespaceURI());
        if (sts != null) {
            for (SchemaTypeSystem ts : sts) {
                SchemaModelGroup type = ts.findModelGroup(qname);
                if (type != null) {
                    return type;
                }
            }
        }
        return null;
    }

    @Override
    public SchemaAttributeGroup findAttributeGroup(QName qname) {
        SchemaTypeSystem[] sts = get(qname.getNamespaceURI());
        if (sts != null) {
            for (SchemaTypeSystem ts : sts) {
                SchemaAttributeGroup type = ts.findAttributeGroup(qname);
                if (type != null) {
                    return type;
                }
            }
        }
        return null;
    }

    @Override
    public boolean isNamespaceDefined(String string) {
        SchemaTypeSystem[] res = get(string);
        return res.length > 0;
    }

    @Override
    public SchemaType.Ref findTypeRef(QName qname) {
        SchemaTypeSystem[] sts = get(qname.getNamespaceURI());
        if (sts != null) {
            for (SchemaTypeSystem ts : sts) {
                SchemaType.Ref type = ts.findTypeRef(qname);
                if (type != null) {
                    return type;
                }
            }
        }
        return null;
    }

    @Override
    public SchemaType.Ref findDocumentTypeRef(QName qname) {
        SchemaTypeSystem[] sts = get(qname.getNamespaceURI());
        if (sts != null) {
            for (SchemaTypeSystem ts : sts) {
                SchemaType.Ref type = ts.findDocumentTypeRef(qname);
                if (type != null) {
                    return type;
                }
            }
        }
        return null;
    }

    @Override
    public SchemaType.Ref findAttributeTypeRef(QName qname) {
        SchemaTypeSystem[] sts = get(qname.getNamespaceURI());
        if (sts != null) {
            for (SchemaTypeSystem ts : sts) {
                SchemaType.Ref type = ts.findAttributeTypeRef(qname);
                if (type != null) {
                    return type;
                }
            }
        }
        return null;
    }

    @Override
    public SchemaGlobalElement.Ref findElementRef(QName qname) {
        SchemaTypeSystem[] sts = get(qname.getNamespaceURI());
        if (sts != null) {
            for (SchemaTypeSystem ts : sts) {
                SchemaGlobalElement.Ref type = ts.findElementRef(qname);
                if (type != null) {
                    return type;
                }
            }
        }
        return null;
    }

    @Override
    public SchemaGlobalAttribute.Ref findAttributeRef(QName qname) {
        SchemaTypeSystem[] sts = get(qname.getNamespaceURI());
        if (sts != null) {
            for (SchemaTypeSystem ts : sts) {
                SchemaGlobalAttribute.Ref type = ts.findAttributeRef(qname);
                if (type != null) {
                    return type;
                }
            }
        }
        return null;
    }

    @Override
    public SchemaModelGroup.Ref findModelGroupRef(QName qname) {
        SchemaTypeSystem[] sts = get(qname.getNamespaceURI());
        if (sts != null) {
            for (SchemaTypeSystem ts : sts) {
                SchemaModelGroup.Ref type = ts.findModelGroupRef(qname);
                if (type != null) {
                    return type;
                }
            }
        }
        return null;
    }

    @Override
    public SchemaAttributeGroup.Ref findAttributeGroupRef(QName qname) {
        SchemaTypeSystem[] sts = get(qname.getNamespaceURI());
        if (sts != null) {
            for (SchemaTypeSystem ts : sts) {
                SchemaAttributeGroup.Ref type = ts.findAttributeGroupRef(qname);
                if (type != null) {
                    return type;
                }
            }
        }
        return null;
    }

    @Override
    public SchemaIdentityConstraint.Ref findIdentityConstraintRef(QName qname) {
        SchemaTypeSystem[] sts = get(qname.getNamespaceURI());
        if (sts != null) {
            for (SchemaTypeSystem ts : sts) {
                SchemaIdentityConstraint.Ref type = ts.findIdentityConstraintRef(qname);
                if (type != null) {
                    return type;
                }
            }
        }
        return null;
    }

    @Override
    public SchemaType typeForSignature(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SchemaType typeForClassname(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public InputStream getSourceAsStream(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String compilePath(String string, XmlOptions xo) throws XmlException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String compileQuery(String string, XmlOptions xo) throws XmlException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public XmlObject newInstance(SchemaType st, XmlOptions xo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public XmlObject parse(String string, SchemaType st, XmlOptions xo) throws XmlException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public XmlObject parse(File file, SchemaType st, XmlOptions xo) throws XmlException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public XmlObject parse(URL url, SchemaType st, XmlOptions xo) throws XmlException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public XmlObject parse(InputStream in, SchemaType st, XmlOptions xo) throws XmlException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public XmlObject parse(XMLStreamReader reader, SchemaType st, XmlOptions xo) throws XmlException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public XmlObject parse(Reader reader, SchemaType st, XmlOptions xo) throws XmlException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public XmlObject parse(Node node, SchemaType st, XmlOptions xo) throws XmlException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public XmlObject parse(XMLInputStream stream, SchemaType st, XmlOptions xo) throws XmlException, XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public XmlSaxHandler newXmlSaxHandler(SchemaType st, XmlOptions xo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DOMImplementation newDomImplementation(XmlOptions xo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public XMLInputStream newValidatingXMLInputStream(XMLInputStream stream, SchemaType st, XmlOptions xo) throws XmlException, XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
