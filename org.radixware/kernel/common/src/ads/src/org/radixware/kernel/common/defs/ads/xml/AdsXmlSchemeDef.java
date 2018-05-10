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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.common.NameUtil;
import org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument.Import;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.impl.SchemaDocumentImpl;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.DefaultDependenceProvider;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.Dependences.Dependence;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.IDependenceProvider;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ObjectLink;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.radixdoc.XmlRadixdoc;
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
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ELocalizedStringKind;
import org.radixware.kernel.common.enums.EMultilingualStringKind;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.SERVER;
import org.radixware.kernel.common.enums.EXmlSchemaLinkMode;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.fs.IJarDataProvider;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Guid;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.XmlDefDocumentation;
import org.radixware.schemas.adsdef.XmlDefLinkedSchema;
import org.radixware.schemas.adsdef.XmlDefLinkedSchemas;
import org.radixware.schemas.commondef.ChangeLogItem;
import org.radixware.schemas.commondef.ChangeLog;
import org.radixware.schemas.radixdoc.Page;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AdsXmlSchemeDef extends AbstractXmlDefinition<XmlDefinition> implements IAdsTypeSource, IJavaSource, IPlatformClassPublisher, AdsDefinition.IDeprecatable, IRadixdocProvider {

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

    public static final class XmlItemDocEntry {

        private Id id;
        private String sinceVersion;

        public XmlItemDocEntry(Id id, String sinceVersion) {
            this.id = id;
            this.sinceVersion = sinceVersion;
        }

        public Id getId() {
            return id;
        }

        public String getSinceVersion() {
            return sinceVersion;
        }

        public void setId(Id id) {
            this.id = id;
        }

        public void setSinceVersion(String sinceVersion) {
            this.sinceVersion = sinceVersion;
        }
    }

    public static final class ChangeLogEntry {

        private Id id;
        private String version;
        private Calendar date;
        private String author;
        private String guid;

        public ChangeLogEntry() {
            this(null, null, null, null);
        }

        public ChangeLogEntry(Id id, String version, Calendar date, String author) {
            this.id = id;
            this.version = version;
            this.date = date;
            this.author = author;
            this.guid = Guid.generateGuid();
        }

        public ChangeLogEntry(ChangeLogEntry entry) {
            this();
            if (entry != null) {
                this.id = entry.getId();
                this.version = entry.getVersion();
                this.date = entry.getDate();
                this.author = entry.getAuthor();
                this.guid = entry.getGuid();
            }
        }

        public Id getId() {
            return id;
        }

        public void setId(Id id) {
            this.id = id;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public Calendar getDate() {
            return date;
        }

        public void setDate(Calendar date) {
            this.date = date;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }
    }

    private String publishedUri;
    private String locationHint;
    private SchemaDocument xsdSchema;
    private DefinitionsDocument wsdlDefinitions;
    private XmlObject publishedContent;
    private String storedTargetNamespace;
    private String namespacePrefix;
    private ERuntimeEnvironmentType environment = null;
    private boolean isDeprecated;
    private Map<String, XmlItemDocEntry> nodesDescriptionMap;
    private List<ChangeLogEntry> changeLog;
    private Map<Id, EXmlSchemaLinkMode> linkedSchemas;
    private boolean needsDoc = false;
    private boolean needsActualizeImportsOnSave = true;
    private Id documentationTitleId = null;
    private Id schemaZIPTitleId = null;

    private IChangeLogActualizationListener changeLogActualizationListener = new IChangeLogActualizationListener() {
        @Override
        public boolean onSubmitChanges(ChangeLogEntry entry) {
            return false;
        }
    };
    private Set<String> docElements;

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

        this.needsDoc = xDef.getNeedsDoc();

        XmlDefDocumentation xDoc = xDef.getDocumentation();
        if (xDoc != null) {
            namespacePrefix = xDoc.getNamespacePrefix();
            documentationTitleId = xDoc.getDocumentationTitleId();
            schemaZIPTitleId = xDoc.getSchemasZIPTitleId();

            if (xDoc.getXmlItemDocEntries() != null) {
                nodesDescriptionMap = new HashMap<>();
                for (org.radixware.schemas.adsdef.XmlItemDocEntry entry : xDoc.getXmlItemDocEntries().getXmlItemDocEntryList()) {
                    nodesDescriptionMap.put(entry.getNodeXPath().trim(), new XmlItemDocEntry(entry.getLocalizedStringId(), entry.getSinceVersion()));
                }
            }

            if (xDoc.getXmlChangeLog() != null) {
                changeLog = new ArrayList<>();
                for (ChangeLogItem entry : xDef.getDocumentation().getXmlChangeLog().getChangeLogItemList()) {
                    Calendar date = Calendar.getInstance();
                    date.setTime(entry.getDate());
                    changeLog.add(new ChangeLogEntry(entry.getDescriptionMlsId(), entry.getSinceVersion(), date, entry.getAuthor()));
                }
            }
        }

        boolean isLinkedSchemasEmpty = xDef.getLinkedSchemas() == null;
        if (!isLinkedSchemasEmpty) {
            needsActualizeImportsOnSave = xDef.getLinkedSchemas().getActualizeOnSave();
            linkedSchemas = new HashMap<>();
            for (org.radixware.schemas.adsdef.XmlDefLinkedSchema schema : xDef.getLinkedSchemas().getLinkedSchemaList()) {
                linkedSchemas.put(schema.getId(), schema.getLinkMode());
            }
        }

        this.docElements = findDocElements();
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

    public void setNeedsDoc(boolean needsDoc) {
        if (this.needsDoc != needsDoc) {
            this.needsDoc = needsDoc;
            setEditState(EEditState.MODIFIED);
        }
    }

    public Id getDocumentationTitleId() {
        return documentationTitleId;
    }

    public void setDocumentationTitleId(Id documentationTitleId) {
        this.documentationTitleId = documentationTitleId;
        this.setEditState(EEditState.MODIFIED);
    }

    public Id getSchemaZIPTitleId() {
        return schemaZIPTitleId;
    }

    public void setSchemaZIPTitleId(Id schemaZIPTitleId) {
        this.schemaZIPTitleId = schemaZIPTitleId;
        this.setEditState(EEditState.MODIFIED);
    }

    public boolean isNeedActualizeImportsOnSave() {
        return needsActualizeImportsOnSave;
    }

    public void setNeedsActualizeImportsOnSave(boolean actualizeImportsOnSave) {
        this.needsActualizeImportsOnSave = actualizeImportsOnSave;
        setEditState(EEditState.MODIFIED);
    }

    public List<String> getXsdSchemaImportedListSchemaLocations() {
        if (this.getXmlContent() == null || !(this.getXmlContent() instanceof SchemaDocumentImpl)) {
            return null;
        }
        List<String> result = new ArrayList<>();

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

    public XmlItemDocEntry getXmlItemDocEntry(String itemXPath) {
        return itemXPath == null || "".equals(itemXPath) || nodesDescriptionMap == null ? null : nodesDescriptionMap.get(itemXPath);
    }

    public void addNodeDescription(String nodeXPath, XmlItemDocEntry desription) {
        if (nodesDescriptionMap == null) {
            nodesDescriptionMap = new HashMap<>();
        }
        nodesDescriptionMap.put(nodeXPath, desription);
        setEditState(EEditState.MODIFIED);
    }

    public void removeNodeDescription(String nodeXPath) {
        nodesDescriptionMap.remove(nodeXPath);
        setEditState(EEditState.MODIFIED);
    }

    public List<String> getDocumentedNodes() {
        if (nodesDescriptionMap != null) {
            return new ArrayList(nodesDescriptionMap.keySet());
        } else {
            return new ArrayList();
        }
    }

    public Map<String, AdsMultilingualStringDef> getNodesDocumentationStrings() {
        Map<String, AdsMultilingualStringDef> result = new HashMap<>();
        if (nodesDescriptionMap != null) {
            for (String xpath : nodesDescriptionMap.keySet()) {
                AdsMultilingualStringDef docString = findLocalizedString(nodesDescriptionMap.get(xpath).getId());
                if (docString != null) {
                    result.put(xpath, docString);
                }
            }
        }

        return result;
    }

    public void addChangeLogEnry(ChangeLogEntry entry) {
        if (changeLog == null) {
            changeLog = new ArrayList<>();
        }

        ChangeLogUtils.addToChangeLog(changeLog, entry);
        setEditState(EEditState.MODIFIED);
    }

    public void removeChangeLogEntry(String guid) {
        if (changeLog == null) {
            return;
        }
        ChangeLogUtils.removeFromChangeLog(changeLog, guid);
        setEditState(EEditState.MODIFIED);
    }

    public ChangeLogEntry getChangeLogEntry(String guid) {
        if (changeLog == null) {
            return null;
        }
        return ChangeLogUtils.getFromChangeLog(changeLog, guid);
    }

    public void applyChangeLogChanges(Map<AdsXmlSchemeDef.ChangeLogEntry, RadixObject.EEditState> changes) {
        if (changeLog == null) {
            if (changes != null) {
                changeLog = new ArrayList<>();
            } else {
                return;
            }
        }

        ChangeLogUtils.applyChangesToChangeLog(changeLog, changes);
        if (!changes.isEmpty()) {
            setEditState(EEditState.MODIFIED);
        }
    }

    public List<ChangeLogEntry> getChangeLog() {
        if (changeLog != null) {
            ArrayList<ChangeLogEntry> copy = new ArrayList<>();
            for (ChangeLogEntry entry : changeLog) {
                copy.add(new ChangeLogEntry(entry));
            }
            return copy;
        } else {
            return new ArrayList<>();
        }
    }

    public void addImportedSchema(AdsXmlSchemeDef schema) {
        if (linkedSchemas == null) {
            linkedSchemas = new HashMap<>();
        }

        if (!linkedSchemas.containsKey(schema.getId())) {
            linkedSchemas.put(schema.getId(), EXmlSchemaLinkMode.IMPORT);
            setEditState(EEditState.MODIFIED);
        }
    }

    public void addLinkedSchema(AdsXmlSchemeDef schema) {
        if (linkedSchemas == null) {
            linkedSchemas = new HashMap<>();
        }

        linkedSchemas.put(schema.getId(), EXmlSchemaLinkMode.MANUAL);

        setEditState(EEditState.MODIFIED);
    }

    public void removeLinkedSchema(Id schemaId) {
        if (linkedSchemas == null || linkedSchemas.isEmpty()) {
            return;
        }

        linkedSchemas.remove(schemaId);
        setEditState(EEditState.MODIFIED);
    }

    public Map<AdsXmlSchemeDef, EXmlSchemaLinkMode> getLinkedSchemas() {
        Map<AdsXmlSchemeDef, EXmlSchemaLinkMode> result = new HashMap<>();

        if (linkedSchemas == null || linkedSchemas.isEmpty() || this.getBranch() == null) {
            return result;
        }

        for (final Entry<Id, EXmlSchemaLinkMode> entry : linkedSchemas.entrySet()) {
            final AtomicReference<AdsXmlSchemeDef> schemeRef = new AtomicReference<>(null);
            Layer.HierarchyWalker.walk(getLayer(), new Layer.HierarchyWalker.Acceptor<Object>() {
                @Override
                public void accept(HierarchyWalker.Controller<Object> controller, Layer layer) {
                    layer.visit(new IVisitor() {
                        @Override
                        public void accept(RadixObject radixObject) {
                            if (radixObject instanceof AdsXmlSchemeDef) {
                                schemeRef.set((AdsXmlSchemeDef) radixObject);
                            }

                        }
                    }, new AdsVisitorProvider.AdsTopLevelDefVisitorProvider() {
                        @Override
                        public boolean isTarget(RadixObject radixObject) {
                            return (radixObject instanceof AdsXmlSchemeDef) && ((AdsXmlSchemeDef) radixObject).getId() == entry.getKey();
                        }

                        @Override
                        public boolean isContainer(RadixObject radixObject) {
                            return radixObject instanceof Branch || radixObject instanceof Layer || radixObject instanceof AdsSegment || radixObject instanceof Module || radixObject instanceof ModuleDefinitions;
                        }

                        @Override
                        public boolean isClassContainer(Class c) {
                            if (ILocalizingBundleDef.class.isAssignableFrom(c)) {
                                return false;
                            }
                            return super.isClassContainer(c);
                        }


                    });
                }
            });

            AdsXmlSchemeDef def = schemeRef.get();

            if (def != null) {
                result.put(def, entry.getValue());
            }
        }

        return result;
    }

    public Set<Id> getLinkedSchemasIds() {
        return linkedSchemas == null ? null : linkedSchemas.keySet();
    }

    public boolean containsLinkedSchema(Id linkedSchemaId) {
        return linkedSchemas == null ? false : linkedSchemas.containsKey(linkedSchemaId) && linkedSchemas.get(linkedSchemaId) == EXmlSchemaLinkMode.MANUAL;
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
        List<IXmlDefinition> defs = new ArrayList<>();
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
    public String getJavaPackageName(boolean isHumanReadable) {
        if (isTransparent()) {
            return NameUtil.getPackageFromNamespace(this.getTargetNamespace());
        } else {
            return JavaSourceSupport.getPackageName(this, JavaSourceSupport.UsagePurpose.getPurpose(getTargetEnvironment(), JavaSourceSupport.CodeType.EXCUTABLE), isHumanReadable);
        }
    }

    @Override
    public boolean isDeprecated() {
        return isDeprecated;
    }

    @Override
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

        xDef.setNeedsDoc(needsDoc);

        actualizeNodesDescriptionMap();

        XmlDefDocumentation xDoc = xDef.addNewDocumentation();

        if (documentationTitleId != null) {
            xDoc.setDocumentationTitleId(documentationTitleId);
        }

        if (schemaZIPTitleId != null) {
            xDoc.setSchemasZIPTitleId(schemaZIPTitleId);
        }

        if (namespacePrefix != null) {
            xDoc.setNamespacePrefix(namespacePrefix);
        }

        if (nodesDescriptionMap != null && !nodesDescriptionMap.isEmpty()) {
            org.radixware.schemas.adsdef.XmlItemDocEntries xDocEntries = xDoc.addNewXmlItemDocEntries();

            List<String> sortedXpathList = new ArrayList<>(nodesDescriptionMap.keySet());
            Collections.sort(sortedXpathList);

            for (String key : sortedXpathList) {
                org.radixware.schemas.adsdef.XmlItemDocEntry xDocEntry = xDocEntries.addNewXmlItemDocEntry();
                xDocEntry.setNodeXPath(key);
                xDocEntry.setLocalizedStringId(nodesDescriptionMap.get(key).getId());
                xDocEntry.setSinceVersion(nodesDescriptionMap.get(key).getSinceVersion());
            }
        }

        actualizeChangeLog();

        if (changeLog != null && !changeLog.isEmpty()) {
            ChangeLog xChangeLog = xDoc.addNewXmlChangeLog();
            for (ChangeLogEntry entry : changeLog) {
                ChangeLogItem xDocEntry = xChangeLog.addNewChangeLogItem();
                xDocEntry.setDescriptionMlsId(entry.getId());
                xDocEntry.setSinceVersion(entry.getVersion());
                xDocEntry.setDate(new Timestamp(entry.getDate().getTimeInMillis()));
                xDocEntry.setAuthor(entry.getAuthor());
            }
        }

        String docPrettyText = xDoc.xmlText(new XmlOptions().setSavePrettyPrint());
        try {
            XmlDefDocumentation prettyDoc = XmlDefDocumentation.Factory.parse(docPrettyText);
            xDef.setDocumentation(prettyDoc);
        } catch (XmlException ex) {
            Logger.getLogger(AdsXmlSchemeDef.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (needsActualizeImportsOnSave && saveMode == ESaveMode.NORMAL) {
            actualizeLinkedSchemas();
        }

        XmlDefLinkedSchemas xLinkedSchemas = null;
        if (linkedSchemas != null && !linkedSchemas.isEmpty()) {
            xLinkedSchemas = xDef.addNewLinkedSchemas();
            xLinkedSchemas.setActualizeOnSave(needsActualizeImportsOnSave);
            List<Id> sortedIds = new ArrayList<>(linkedSchemas.keySet());
            Collections.sort(sortedIds, new Comparator<Id>() {

                @Override
                public int compare(Id o1, Id o2) {
                    return o1.toString().compareTo(o2.toString());
                }
            });

            for (Id id : sortedIds) {
                XmlDefLinkedSchema xSchema = xLinkedSchemas.addNewLinkedSchema();
                xSchema.setId(id);
                xSchema.setLinkMode(linkedSchemas.get(id));
            }
        }

        if (xLinkedSchemas != null) {
            String linkedSchemasPrettyText = xLinkedSchemas.xmlText(new XmlOptions().setSavePrettyPrint());
            try {
                XmlDefLinkedSchemas prettyLinkedSchemas = XmlDefLinkedSchemas.Factory.parse(linkedSchemasPrettyText);
                xDef.setLinkedSchemas(prettyLinkedSchemas);
            } catch (XmlException ex) {
                Logger.getLogger(AdsXmlSchemeDef.class.getName()).log(Level.SEVERE, null, ex);
            }
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

    private void actualizeNodesDescriptionMap() {
        if (nodesDescriptionMap != null && !nodesDescriptionMap.isEmpty()) {
            XmlObject innerXml = this.getXmlDocument();
            if (innerXml == null) {
                innerXml = this.getXmlContent();
                if (innerXml == null) {
                    return;
                }
            }

            List<String> documentedNodes = this.getDocumentedNodes();
            removeExistingNodesFromDocumentedNodesList(XmlUtils.getChildElements(innerXml.getDomNode()).get(0), documentedNodes);
            nodesDescriptionMap.keySet().removeAll(documentedNodes);
        }
    }

    public void actualizeLinkedSchemas() {
        List<IXmlDefinition> importedSchemas = this.getImportedDefinitions();

        for (IXmlDefinition importedSchema : importedSchemas) {
            if (importedSchema instanceof AdsXmlSchemeDef) {
                AdsXmlSchemeDef tmp = (AdsXmlSchemeDef) importedSchema;
                addImportedSchema(tmp);
            }
        }

        if (linkedSchemas == null || linkedSchemas.isEmpty()) {
            return;
        }

        if (linkedSchemas.keySet().contains(this.getId())) {
            linkedSchemas.remove(this.getId());
            setEditState(EEditState.MODIFIED);
        }

        Iterator<Id> iterator = linkedSchemas.keySet().iterator();
        while (iterator.hasNext()) {
            Id next = iterator.next();

            if (!isImportExist(next) && linkedSchemas.get(next) == EXmlSchemaLinkMode.IMPORT) {
                iterator.remove();
                setEditState(EEditState.MODIFIED);
            }
        }
    }

    public boolean isImportExist(final Id importedSchemaId) {
        final AtomicReference<AdsXmlSchemeDef> schemeRef = new AtomicReference<>(null);
        Layer l = getLayer();
        if (l == null) {
            return true;
        }
        Layer.HierarchyWalker.walk(l, new Layer.HierarchyWalker.Acceptor<RadixObject>() {
            @Override
            public void accept(HierarchyWalker.Controller<RadixObject> controller, Layer layer) {
                layer.visit(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                        if (radixObject instanceof AdsXmlSchemeDef) {
                            schemeRef.set((AdsXmlSchemeDef) radixObject);
                        }

                    }
                }, new AdsVisitorProvider.AdsTopLevelDefVisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject radixObject) {
                        return (radixObject instanceof AdsXmlSchemeDef) && ((AdsXmlSchemeDef) radixObject).getId() == importedSchemaId;
                    }

                    @Override
                    public boolean isContainer(RadixObject radixObject) {
                        return radixObject instanceof Branch || radixObject instanceof Layer || radixObject instanceof AdsSegment || radixObject instanceof Module || radixObject instanceof ModuleDefinitions;
                    }
                    
                    @Override
                    public boolean isClassContainer(Class c) {
                        if (ILocalizingBundleDef.class.isAssignableFrom(c)) {
                            return false;
                        }
                        return super.isClassContainer(c);
                    }
                });
            }
        });

        AdsXmlSchemeDef importedDef = schemeRef.get();

        return importedDef != null && this.getImportedDefinitions().contains(importedDef);
    }

    private void removeExistingNodesFromDocumentedNodesList(Element node, List<String> nodesList) {
        nodesList.remove(org.radixware.kernel.common.utils.XPathUtils.getXPath(node));
        for (Element child : XmlUtils.getChildElements(node)) {
            removeExistingNodesFromDocumentedNodesList(child, nodesList);
        }
    }

    @Override
    public ClipboardSupport<AdsXmlSchemeDef> getClipboardSupport() {
        return new AdsClipboardSupport<AdsXmlSchemeDef>(this) {
            @Override
            protected XmlObject copyToXml() {
                XmlDefinition xDef = XmlDefinition.Factory.newInstance();
                appendTo(xDef, ESaveMode.NORMAL);

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

    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        super.collectUsedMlStringIds(ids);
        if (documentationTitleId != null) {
            ids.add(new MultilingualStringInfo(this) {

                @Override
                public String getContextDescription() {
                    return " Documentation Title";
                }

                @Override
                public Id getId() {
                    return documentationTitleId;
                }

                @Override
                public EAccess getAccess() {
                    return AdsXmlSchemeDef.this.getAccessMode();
                }

                @Override
                public void updateId(Id newId) {
                    setDocumentationTitleId(newId);
                }

                @Override
                public boolean isPublished() {
                    return AdsXmlSchemeDef.this.isPublished();
                }

                @Override
                public EMultilingualStringKind getKind() {
                    return EMultilingualStringKind.DESCRIPTION;
                }
            });
        }

        if (schemaZIPTitleId != null) {
            ids.add(new MultilingualStringInfo(this) {

                @Override
                public String getContextDescription() {
                    return " Schema ZIP Title";
                }

                @Override
                public Id getId() {
                    return schemaZIPTitleId;
                }

                @Override
                public EAccess getAccess() {
                    return AdsXmlSchemeDef.this.getAccessMode();
                }

                @Override
                public void updateId(Id newId) {
                    setSchemaZIPTitleId(newId);
                }

                @Override
                public boolean isPublished() {
                    return AdsXmlSchemeDef.this.isPublished();
                }

                @Override
                public EMultilingualStringKind getKind() {
                    return EMultilingualStringKind.DESCRIPTION;
                }
            });
        }

        if (nodesDescriptionMap != null) {
            for (final Map.Entry<String, XmlItemDocEntry> docEntry : nodesDescriptionMap.entrySet()) {
                ids.add(new MultilingualStringInfo(this) {

                    @Override
                    public String getContextDescription() {
                        return " Item: " + org.radixware.kernel.common.utils.XPathUtils.getHumanReadableXPath(docEntry.getKey()) + "<br/> Owner";
                    }

                    @Override
                    public Id getId() {
                        return docEntry.getValue().getId();
                    }

                    @Override
                    public EAccess getAccess() {
                        return AdsXmlSchemeDef.this.getAccessMode();
                    }

                    @Override
                    public void updateId(Id newId) {
                        docEntry.getValue().setId(newId);
                    }

                    @Override
                    public boolean isPublished() {
                        return AdsXmlSchemeDef.this.isPublished();
                    }

                    @Override
                    public EMultilingualStringKind getKind() {
                        return EMultilingualStringKind.XSD_ITEM;
                    }
                });
            }
        }
        if (changeLog != null) {
            for (final ChangeLogEntry docEntry : changeLog) {
                ids.add(new MultilingualStringInfo(this) {

                    @Override
                    public String getContextDescription() {
                        return " Description";
                    }

                    @Override
                    public Id getId() {
                        return docEntry.getId();
                    }

                    @Override
                    public EAccess getAccess() {
                        return AdsXmlSchemeDef.this.getAccessMode();
                    }

                    @Override
                    public void updateId(Id newId) {
                        docEntry.setId(newId);
                    }

                    @Override
                    public boolean isPublished() {
                        return AdsXmlSchemeDef.this.isPublished();
                    }

                    @Override
                    public EMultilingualStringKind getKind() {
                        return EMultilingualStringKind.DESCRIPTION;
                    }
                });
            }
        }
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsXmlSchemeDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new XmlRadixdoc(getSource(), page, options);
            }

        };
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }

    @Override
    public boolean needsDocumentation() {
        return needsDoc;
    }

    public String getNamespacePrefix() {
        return namespacePrefix;
    }

    public void setNamespacePrefix(String namespacePrefix) {
        if (!String.valueOf(namespacePrefix).equals(String.valueOf(this.namespacePrefix))) {
            setEditState(EEditState.MODIFIED);
            this.namespacePrefix = namespacePrefix;
        }
    }

    private Set<String> findDocElements() {
        Set<String> result = new HashSet<>();
        if (isTransparent()) {
            return null;
        }

        XmlObject obj = getXmlDocument() != null ? getXmlDocument() : getXmlContent();
        if (obj == null) {
            return null;
        }

        List<Element> childElements = XmlUtils.getChildElements((Document) obj.getDomNode());
        if (!childElements.isEmpty()) {
            getChildrenDocElements(childElements.get(0), result);
        }

        return result;
    }

    private void getChildrenDocElements(Element parent, Set<String> docElementsSet) {
        List<Element> childElements = XmlUtils.getChildElements(parent);
        for (Element element : childElements) {
            if (org.radixware.kernel.common.utils.XPathUtils.isElementNeedsDoc(element)) {
                docElementsSet.add(org.radixware.kernel.common.utils.XPathUtils.getXPath(element));
            }
            getChildrenDocElements(element, docElementsSet);
        }
    }

    public IChangeLogActualizationListener getChangeLogActualizationListener() {
        return changeLogActualizationListener;
    }

    public void setChangeLogActualizationListener(IChangeLogActualizationListener changeLogActualizationListener) {
        this.changeLogActualizationListener = changeLogActualizationListener;
    }   
    
    private void actualizeChangeLog() {
        Set<String> currentDocElements = findDocElements();
        if (docElements == null || currentDocElements == null) {
            return;
        }

        Set<String> removedElements = ChangeLogUtils.getAddedSetElements(currentDocElements, docElements);
        Set<String> addedElements = ChangeLogUtils.getAddedSetElements(docElements, currentDocElements);

        if (removedElements.isEmpty() && addedElements.isEmpty()) {
            return;
        }
                
        String version = null;
        Iterator<String> iterator = removedElements.isEmpty() ? addedElements.iterator() : removedElements.iterator();
        while (iterator.hasNext()) {            
            XmlItemDocEntry docEntry = getXmlItemDocEntry(iterator.next());
            if (docEntry != null && !Utils.emptyOrNull(docEntry.sinceVersion)) {
                version = docEntry.sinceVersion;
                break;
            }
        }                

        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(System.currentTimeMillis());

        String author = System.getProperty("user.name");
        
        IMultilingualStringDef description = findLocalizingBundle().createString(ELocalizedStringKind.DESCRIPTION);
        for (EIsoLanguage lang : getLayer().getLanguages()) {
            description.setValue(lang, ChangeLogUtils.getEditedElementsDescription(addedElements, removedElements, lang));
        }

        findLocalizingBundle().getStrings().getLocal().add((AdsMultilingualStringDef) description);

        ChangeLogEntry entry = new ChangeLogEntry(description.getId(), version, date, author);
        
        if (changeLogActualizationListener.onSubmitChanges(entry)) {
            addChangeLogEnry(entry);
        } else {
            findLocalizingBundle().getStrings().getLocal().remove((AdsMultilingualStringDef) description);
        }

        docElements = currentDocElements;
    }
}
