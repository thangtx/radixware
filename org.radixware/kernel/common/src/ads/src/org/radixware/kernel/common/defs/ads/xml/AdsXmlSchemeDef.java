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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.common.NameUtil;
import org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument.Import;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.impl.SchemaDocumentImpl;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.DefaultDependenceProvider;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.DefinitionSearcher;

import org.radixware.kernel.common.defs.Dependences.Dependence;
import org.radixware.kernel.common.defs.IDependenceProvider;
import org.radixware.kernel.common.defs.ObjectLink;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.AdsDefinition;

import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;

import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.defs.ads.src.xml.AdsXmlJavaSourceSupport;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.defs.ads.type.XmlType;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.XmlDefinition;
import org.xmlsoap.schemas.wsdl.DefinitionsDocument;
import org.radixware.kernel.common.defs.ads.src.xml.XBeansTypeSystem;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.SERVER;
import org.radixware.kernel.common.repository.fs.IJarDataProvider;
import org.radixware.kernel.common.utils.FileUtils;

public class AdsXmlSchemeDef extends AbstractXmlDefinition<XmlDefinition> implements IAdsTypeSource, IJavaSource, IPlatformClassPublisher, AdsDefinition.IDeprecatable {

    @Override
    public AdsXmlJavaSourceSupport getJavaSourceSupport() {
        return AdsXmlJavaSourceSupport.Factory.newInstance(this);
    }

    public static final class Factory {

        public static AdsXmlSchemeDef loadFrom(XmlDefinition xDef) {
            return new AdsXmlSchemeDef(xDef);
        }

        public static AdsXmlSchemeDef newInstance() {
            return new AdsXmlSchemeDef("NewXmlScheme");
        }

        public static AdsXmlSchemeDef newInstance(String publishedUrl) {
            return new AdsXmlSchemeDef("NewXmlScheme", publishedUrl);
        }
    }

    public enum SchemeType {

        XSD,
        WSDL,
        UNKNOWN
    }
    private String publishedUri;
    private String locationHint;
    private SchemaDocument xsdSchema;
    private DefinitionsDocument wsdlDefinitions;
    private XmlObject publishedContent;
    private String storedTargetNamespace;
    private ERuntimeEnvironmentType environment = null;
    private boolean isDeprecated;

    private AdsXmlSchemeDef(String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.XML_SCHEME), name);
        this.environment = ERuntimeEnvironmentType.COMMON;
        this.xsdSchema = SchemaDocument.Factory.newInstance();
        this.xsdSchema.addNewSchema();        
    }

    private AdsXmlSchemeDef(String name, String publishedUrl) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.XML_SCHEME), name);
        this.publishedUri = publishedUrl;
        this.environment = ERuntimeEnvironmentType.COMMON;
    }

    @Override
    public boolean isTransparent() {
        return publishedUri != null;
    }

    public String getPublishedUri() {
        return publishedUri;
    }

    public String getLocationHint() {
        return locationHint;
    }

    public List<String> getXsdSchemaImportedListSchemaLocations() {
        if (this.getXmlContent() == null || !(this.getXmlContent() instanceof SchemaDocumentImpl)) {
            return null;
        }
        List<String> result = new ArrayList<String>();

        SchemaDocumentImpl schemaImpl = (SchemaDocumentImpl) this.getXmlContent();
        Import arrImport[] = schemaImpl.getSchema().getImportArray();

        for (int i = 0; i < arrImport.length; i++) {
            String fileName = arrImport[i].getSchemaLocation();
            if (fileName != null && !fileName.isEmpty()) {
                result.add(fileName);
            }
        }
        return result;
    }

    private AdsXmlSchemeDef(XmlDefinition xDef) {
        super(xDef);
        storedTargetNamespace = xDef.getTargetNamespace();
        environment = xDef.getEnvironment();
        if (environment == null) {
            environment = ERuntimeEnvironmentType.COMMON;
        }
        if (xDef.isSetIsDeprecated()) {
            isDeprecated = xDef.getIsDeprecated();
        }
        if (xDef.isSetPublishedUri()) {
            this.publishedUri = xDef.getPublishedUri().getStringValue();
            if (xDef.getPublishedUri().isSetLocationHint()) {
                this.locationHint = xDef.getPublishedUri().getLocationHint();
            }
        }
    }

    @Override
    protected XBeansTypeSystem checkFixedTypeSystem(XmlDefinition xDef) {
        if (xDef.isSetTypeList()) {
            return new XBeansTypeSystem(this, xDef.getTypeList());
        } else {
            if (xDef.isSetPublishedUri()) {
                this.publishedUri = xDef.getPublishedUri().getStringValue();
                if (xDef.getPublishedUri().isSetLocationHint()) {
                    this.locationHint = xDef.getPublishedUri().getLocationHint();
                }
            } else {
                try {
                    XmlObject[] children = xDef.selectChildren(QNameSet.forArray(new QName[]{new QName("http://schemas.xmlsoap.org/wsdl/", "definitions"), new QName("http://www.w3.org/2001/XMLSchema", "schema")}));
                    if (children != null && children.length > 0) {
                        XmlObject obj = children[0];
                        if ("schema".equals(obj.getDomNode().getLocalName())) {
                            SchemaDocument doc = SchemaDocument.Factory.parse(obj.getDomNode());
                            this.xsdSchema = doc;
                        } else if ("definitions".equals(obj.getDomNode().getLocalName())) {
                            DefinitionsDocument doc = DefinitionsDocument.Factory.parse(obj.getDomNode());
                            this.wsdlDefinitions = doc;
                        } else {
                            throw new IOException("Unsupported schema definition element:" + obj.getDomNode().getNodeName());
                        }
                    }
//                    if (xDef.isSetSchema()) {
//                        SchemaDocument doc = SchemaDocument.Factory.newInstance();
//                        doc.setSchema(xDef.getSchema());
//                        this.xsdSchema = doc;
//                    }
//                    } else if (xDef.isSetDefinitions()) {
//                        DefinitionsDocument doc = DefinitionsDocument.Factory.newInstance();
//                        doc.setDefinitions(xDef.getDefinitions());
//                        this.wsdlDefinitions = doc;
//                    }
                } catch (Throwable e) {
                    this.xsdSchema = null;
                    this.wsdlDefinitions = null;
                }
            }
            return null;
        }
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.XML_SCHEME;
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    @Override
    public AdsType getType(EValType typeId, String extStr) {
        return XmlType.Factory.newInstance(this, extStr);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        List<IXmlDefinition> imports = getImportedDefinitions();
        for (IXmlDefinition def : imports) {
            if (def instanceof Definition) {
                list.add((Definition) def);
            }
        }
        List<Id> enumIds = new ArrayList<Id>();
        final XmlObject xmlContent = getXmlContent();
        if (xmlContent != null) {
            XmlUtils.collectUsedEnumIds(xmlContent, enumIds);
        }
        DefinitionSearcher<AdsDefinition> searcher = AdsSearcher.Factory.newAdsDefinitionSearcher(this);
        for (Id id : enumIds) {
            AdsDefinition e = searcher.findById(id).get();
            if (e != null) {
                list.add(e);

            }
        }
    }

    public SchemeType getSchemeType() {
        XmlObject obj = getXmlDocument();
        if (obj instanceof SchemaDocument) {
            return SchemeType.XSD;
        } else if (obj instanceof DefinitionsDocument) {
            return SchemeType.WSDL;
        } else {
            return SchemeType.UNKNOWN;
        }
    }

    @Override
    public String getTargetNamespace() {
        if (publishedUri != null) {
            return publishedUri;
        } else {
            if (xsdSchema != null && xsdSchema.getSchema() != null) {
                return xsdSchema.getSchema().getTargetNamespace();
            } else if (wsdlDefinitions != null && wsdlDefinitions.getDefinitions() != null) {
                return wsdlDefinitions.getDefinitions().getTargetNamespace();
            }
            return storedTargetNamespace;
        }
    }

    @Override
    public XmlObject getXmlContent() {
        synchronized (this) {
            if (publishedUri != null) {
                if (publishedContent == null) {
                    publishedContent = loadSchemaFromPlatformClassPath();
                }
                return publishedContent;
            } else {
                if (xsdSchema == null && wsdlDefinitions == null && getSaveMode() == ESaveMode.API) {
                    try {
                        String jarFileName = "bin/common.jar";
                        switch (getUsageEnvironment()) {
                            case SERVER:
                                jarFileName = "bin/server.jar";
                                break;
                            case EXPLORER:
                                jarFileName = "bin/explorer.jar";
                                break;
                            case WEB:
                                jarFileName = "bin/web.jar";
                                break;
                            case COMMON_CLIENT:
                                jarFileName = "bin/common-client.jar";
                                break;
                        }

                        IJarDataProvider provider = getModule().getRepository().getJarFile(jarFileName);
                        if (provider != null) {
                            String path = getJavaSourceSupport().getContentResourceName();
                            if (provider.entryExists(path)) {
                                try (InputStream stream = provider.getEntryDataStream(path)) {
                                    XmlObject result = XmlObject.Factory.parse(stream);
                                    if (result instanceof SchemaDocument) {
                                        xsdSchema = (SchemaDocument) result;
                                    } else if (result instanceof DefinitionsDocument) {
                                        wsdlDefinitions = (DefinitionsDocument) result;
                                    }
                                } catch (XmlException ex) {
                                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                                }
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                }

                if (xsdSchema != null) {
                    return xsdSchema;
                } else if (wsdlDefinitions != null) {
                    return wsdlDefinitions;
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    public List<IXmlDefinition> getImportedDefinitions() {
        List<String> imports = XmlUtils.getImportedNamespaces(getXmlContent(), true);
        List<IXmlDefinition> defs = new ArrayList<IXmlDefinition>();
        AdsSearcher.Factory.XmlDefinitionSearcher searcher = AdsSearcher.Factory.newXmlDefinitionSearcher(this);
        for (String imp : imports) {
            IXmlDefinition def = searcher.findByNs(imp).get();
            if (def != null) {
                defs.add(def);
            }
        }
        return defs;
    }

    @Override
    public List<String> getImportedNamespaces() {
        return XmlUtils.getImportedNamespaces(getXmlContent());
    }

    private XmlObject loadSchemaFromPlatformClassPath() {

        XmlObject obj = ((AdsSegment) getModule().getSegment()).getBuildPath().getPlatformXml().findXmlSchema(publishedUri, locationHint, environment);
        if (obj != null) {
            if (obj instanceof SchemaDocument) {
                return (SchemaDocument) obj;
            } else if (obj instanceof DefinitionsDocument) {
                return (DefinitionsDocument) obj;
            }
        }
        return null;
    }

    @Override
    public String getSchemaFileType() {
        if (isTransparent()) {
            return null;
        }
        if (xsdSchema != null) {
            return "xsd";
        } else if (wsdlDefinitions != null) {
            return "wsdl";
        } else {
            return null;
        }
    }

    static public String getExceptionMessageForXsd(String text) {
        String mess = null;
        try {
            SchemaDocument xsDoc = SchemaDocument.Factory.parse(text);
            if (xsDoc != null) {
                return "";
            }
        } catch (Throwable ex) {
            //may be wsdl&
            mess = ex.getMessage();
            try {
                DefinitionsDocument wsdlDoc = DefinitionsDocument.Factory.parse(text);
                if (wsdlDoc != null) {
                    return "";
                }
            } catch (Throwable ex2) {
                mess += "\n" + ex2.getMessage();
            }

        }
        if (mess == null || mess.isEmpty()) {
            mess = "Invalid xml";
        }
        return mess;
    }

    public boolean setXmlText(String text) {
        if (isTransparent()) {
            return false;
        }
        try {
            SchemaDocument xsDoc = SchemaDocument.Factory.parse(text);
            if (xsDoc != null) {
                this.xsdSchema = xsDoc;
                this.wsdlDefinitions = null;
                setEditState(EEditState.MODIFIED);
                return true;
            }
        } catch (Throwable ex) {//for prevent crash caused by xmlbeans bugs
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        try {
            DefinitionsDocument xsDoc = DefinitionsDocument.Factory.parse(text);
            if (xsDoc != null) {
                this.wsdlDefinitions = xsDoc;
                this.xsdSchema = null;
                setEditState(EEditState.MODIFIED);
                return true;
            }
        } catch (Throwable ex) {//for prevent crash caused by xmlbeans bugs
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        return false;
    }

    public boolean setXml(InputStream in) {
        if (isTransparent()) {
            return false;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            FileUtils.copyStream(in, out);
        } catch (IOException ex) {
            return false;
        }
        final byte[] bytes = out.toByteArray();
        try {
            SchemaDocument xsDoc = SchemaDocument.Factory.parse(new ByteArrayInputStream(bytes));
            if (xsDoc != null) {
                this.xsdSchema = xsDoc;
                this.wsdlDefinitions = null;
                return true;
            }
        } catch (Throwable ex) {//for prevent crash caused by xmlbeans bugs
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        try {
            DefinitionsDocument xsDoc = DefinitionsDocument.Factory.parse(new ByteArrayInputStream(bytes));
            if (xsDoc != null) {
                this.wsdlDefinitions = xsDoc;
                this.xsdSchema = null;
                return true;
            }
        } catch (Throwable ex) {//for prevent crash caused by xmlbeans bugs
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        return false;
    }

    @Override
    public XmlObject getXmlDocument() {
        if (isTransparent()) {
            XmlObject content = getXmlContent();
            if (content instanceof SchemaDocument) {
                return (SchemaDocument) content;
            } else if (content instanceof DefinitionsDocument) {
                return (DefinitionsDocument) content;
            } else {
                return null;
            }
        }
        if (xsdSchema != null && xsdSchema.getSchema() != null) {
            return xsdSchema;
        } else if (wsdlDefinitions != null && wsdlDefinitions.getDefinitions() != null) {
            return wsdlDefinitions;
        } else {
            if (getSaveMode() == ESaveMode.API) {
                getXmlContent();
            }
            if (xsdSchema != null && xsdSchema.getSchema() != null) {
                return xsdSchema;
            } else if (wsdlDefinitions != null && wsdlDefinitions.getDefinitions() != null) {
                return wsdlDefinitions;
            } else {
                return null;
            }
        }
    }

    @Override
    public ERuntimeEnvironmentType getTargetEnvironment() {
        return environment != null ? environment : ERuntimeEnvironmentType.COMMON;
    }

    public boolean setTargetEnvironment(ERuntimeEnvironmentType env) {
        environment = env;
        setEditState(EEditState.MODIFIED);
        return true;
    }
    private RadixEventSource<PlatformClassPublisherChangesListener, PlatformClassPublishingChangedEvent> publishingChanges;

    @Override
    public IPlatformClassPublishingSupport getPlatformClassPublishingSupport() {
        return new IPlatformClassPublishingSupport() {
            @Override
            public RadixEventSource<PlatformClassPublisherChangesListener, PlatformClassPublishingChangedEvent> getPlatformClassPublishingChengesSupport() {
                synchronized (AdsXmlSchemeDef.this) {
                    if (publishingChanges == null) {
                        publishingChanges = new RadixEventSource<PlatformClassPublisherChangesListener, PlatformClassPublishingChangedEvent>();
                    }
                    return publishingChanges;
                }
            }

            @Override
            public boolean isPlatformClassPublisher() {
                return isTransparent();
            }

            @Override
            public String getPlatformClassName() {
                return publishedUri;
            }

            @Override
            public boolean isExtendablePublishing() {
                return false;
            }
        };
    }

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        return EnumSet.of(getTargetEnvironment());
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        return getTargetEnvironment();
    }

    @Override
    public String getJavaPackageName() {
        if (isTransparent()) {
            return NameUtil.getPackageFromNamespace(this.getTargetNamespace());
        } else {
            return JavaSourceSupport.getPackageName(this, JavaSourceSupport.UsagePurpose.getPurpose(getTargetEnvironment(), JavaSourceSupport.CodeType.EXCUTABLE));
        }
    }

    @Override
    public boolean isDeprecated() {
        return isDeprecated;
    }

    public void setDeprecated(boolean isDeprecated) {
        if (this.isDeprecated != isDeprecated) {
            this.isDeprecated = isDeprecated;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {
        appendTo(xDefRoot.addNewAdsXmlSchemeDefinition(), saveMode);
    }

    public void appendTo(XmlDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        xDef.setTargetNamespace(getTargetNamespace());
        if (saveMode == ESaveMode.API) {
            XBeansTypeSystem ts = getSchemaTypeSystem();
            if (ts != null) {
                ts.appendTo(xDef.addNewTypeList());
            }
        }
        if (isDeprecated) {
            xDef.setIsDeprecated(isDeprecated);
        }
        xDef.setEnvironment(environment);
        if (publishedUri != null) {
            XmlDefinition.PublishedUri uri = xDef.addNewPublishedUri();
            uri.setStringValue(publishedUri);
            if (locationHint != null) {
                uri.setLocationHint(locationHint);
            }

        } else {
            if (saveMode != ESaveMode.API) {
                XmlCursor schemaCursor = null;
                if (xsdSchema != null && xsdSchema.getSchema() != null) {
                    XmlObject clone = xsdSchema.copy();
                    schemaCursor = ((SchemaDocument) clone).getSchema().newCursor();
                } else if (wsdlDefinitions != null && wsdlDefinitions.getDefinitions() != null) {
                    XmlObject clone = wsdlDefinitions.copy();
                    schemaCursor = ((DefinitionsDocument) clone).getDefinitions().newCursor();
                }
                if (schemaCursor != null) {
                    XmlCursor cursor = xDef.newCursor();
                    cursor.toEndToken();
                    schemaCursor.moveXml(cursor);
                    schemaCursor.dispose();
                    cursor.dispose();
                }
            }
        }
    }

    @Override
    public ClipboardSupport<AdsXmlSchemeDef> getClipboardSupport() {
        return new AdsClipboardSupport<AdsXmlSchemeDef>(this) {
            @Override
            protected XmlObject copyToXml() {
                XmlDefinition xDef = XmlDefinition.Factory.newInstance();
                appendTo(
                        xDef, ESaveMode.NORMAL);

                return xDef;

            }

            @Override
            protected AdsXmlSchemeDef loadFrom(XmlObject xmlObject) {
                if (xmlObject instanceof XmlDefinition) {
                    return Factory.loadFrom((XmlDefinition) xmlObject);

                } else {
                    return super.loadFrom(xmlObject);

                }
            }
        };

    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        return collection instanceof ModuleDefinitions;

    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.XML_SCHEME;

    }

    public List<Id> getUsedEnumIds() {
        ArrayList<Id> ids = new ArrayList<Id>();
        XmlUtils.collectUsedEnumIds(getXmlContent(), ids);

        return ids;

    }
    // separate and static to fix deadlock
    private AtomicBoolean provideLocalDependencesOnly = new AtomicBoolean(false);

    private class XsdDependenceProvider extends DefaultDependenceProvider {

        private final AdsSearcher.Factory.XmlDefinitionSearcher searcher;
        private final Map<Id, Dependence> moduleId2Dependence = new HashMap<>();
        private boolean inited = false;

        public XsdDependenceProvider() {
            super(AdsXmlSchemeDef.this);
            searcher = AdsSearcher.Factory.newXmlDefinitionSearcher(AdsXmlSchemeDef.this);
        }

        @Override
        public void collect(final Map<Id, Dependence> moduleId2Dependence) {

            try {
                provideLocalDependencesOnly.set(true);
                super.collect(moduleId2Dependence);

                List<String> nss = AdsXmlSchemeDef.this.getImportedNamespaces();
                if (nss != null && !nss.isEmpty()) {
                    for (String ns : nss) {
                        IXmlDefinition def = searcher.findByNs(ns).get();
                        if (def instanceof AdsDefinition) {
                            ((AdsDefinition) def).getDependenceProvider().collect(moduleId2Dependence);
                        }
                    }
                }

            } finally {
                provideLocalDependencesOnly.set(false);
            }
        }

        @Override
        public Map<Id, Dependence> get() {
            if (!inited) {
                try {
                    collect(moduleId2Dependence);
                } finally {
                    inited = true;
                }
            }
            return moduleId2Dependence;
        }
    }

    private class DependenceProviderLink extends ObjectLink<XsdDependenceProvider> {

        XsdDependenceProvider provider;

        @Override
        protected XsdDependenceProvider search() {
            return new XsdDependenceProvider();
        }

        public XsdDependenceProvider get() {
            provider = find();
            if (provider == null) {
                provider = update();
            }
            return provider;
        }
    }
    private final DependenceProviderLink providerLink = new DependenceProviderLink();

    @Override
    public IDependenceProvider getDependenceProvider() {
        if (provideLocalDependencesOnly.get()) {
            return super.getDependenceProvider();
        } else {
            return providerLink.get();
        }
    }

    @Override
    protected void insertToolTipPrefix(StringBuilder sb) {
        super.insertToolTipPrefix(sb);
        String access = getDefaultAccess().getName().toUpperCase().charAt(0) + getDefaultAccess().getName().substring(1, getDefaultAccess().getName().length());
        sb.append("<b>").append(access).append(" ").append(getTargetEnvironment().getName()).append("</b>&nbsp;");
    }

    @Override
    public String getTypeTitle() {
        return "Xml Schema";
    }
}
