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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.SchemaComponent;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.schema.SchemaTypeSystemCompiler;
import org.apache.xmlbeans.impl.schema.SchemaTypeSystemImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.radixware.kernel.common.build.xbeans.XbeansSchemaCodePrinter;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;

import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.defs.ads.src.xml.XBeansTypeSystem;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.msdl.XmlObjectMessagingXsdCreator;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.DescribedAdsDefinition;
import org.radixware.schemas.msdl.MessageElementDocument;
import org.xmlsoap.schemas.wsdl.DefinitionsDocument;
import org.xmlsoap.schemas.wsdl.TTypes;

public abstract class AbstractXmlDefinition<T extends DescribedAdsDefinition> extends AdsDefinition implements IXmlDefinition {

    private XBeansTypeSystem typeSystem = null;
    private final boolean isFixedTypeSystem;
    private final Object typeListComputeLock = new Object();
    private final Object tsComputeLock = new Object();
    // private Collection<String> typeList = null;

    public AbstractXmlDefinition(Id id, String name) {
        super(id, name);
        isFixedTypeSystem = false;
    }

    public AbstractXmlDefinition(T xDef) {
        super(xDef);
        typeSystem = checkFixedTypeSystem(xDef);
        isFixedTypeSystem = typeSystem != null;
    }

    protected abstract XBeansTypeSystem checkFixedTypeSystem(T xDef);

    @Override
    protected void onModified() {
        synchronized (typeListComputeLock) {
//            if (typeList != null) {
//                typeList.clear();
//            }
            if ((typeSystem != null || xbeansTs != null) && !isFixedTypeSystem) {
                typeSystem = null;
                xbeansTs = null;
            }
        }
    }

    @Override
    public boolean isReadOnly() {
        if (isFixedTypeSystem) {
            return true;
        } else {
            return super.isReadOnly();
        }
    }
    private static final SchemaDocument.Schema[] NO_SCHEMA = new SchemaDocument.Schema[0];

    SchemaDocument.Schema[] lookForSchema(XmlObject obj) {
        SchemaDocument.Schema[] result = null;
        if (obj instanceof SchemaDocument) {
            result = new SchemaDocument.Schema[]{((SchemaDocument) obj).getSchema()};
        } else if (obj instanceof DefinitionsDocument) {
            DefinitionsDocument wsdldoc = (DefinitionsDocument) obj;
            final List<TTypes> types = wsdldoc.getDefinitions().getTypesList();
            final List<SchemaDocument.Schema> list = new LinkedList<>();
            for (TTypes type : types) {
                XmlObject[] schemas = type.selectPath("declare namespace xs=\"http://www.w3.org/2001/XMLSchema\" xs:schema");
                if (schemas.length == 0) {
                    continue;
                }
                for (int k = 0; k < schemas.length; k++) {
                    if (schemas[k] instanceof SchemaDocument.Schema) {
                        list.add((SchemaDocument.Schema) schemas[k]);
                    }

                }
            }
            if (list.isEmpty()) {
                return NO_SCHEMA;
            }
            result = list.toArray(new SchemaDocument.Schema[list.size()]);
        } else if (obj instanceof MessageElementDocument) {
            SchemaDocument xDoc;
            try {
                xDoc = XmlObjectMessagingXsdCreator.createXSD(((MessageElementDocument) obj).getMessageElement(), getId().toString(), new XmlObjectMessagingXsdCreator.DefinitionTypeResolver() {
                    @Override
                    public String getEnumTypeName(String enumId) {
                        Id id = Id.Factory.loadFrom(enumId);
                        AdsEnumDef enumeration = AdsSearcher.Factory.newAdsEnumSearcher(getModule()).findById(id).get();
                        if (enumeration != null) {
                            if (enumeration.getItemType() == EValType.CHAR) {
                                return "Char";
                            } else {
                                return "Str";
                            }
                        } else {
                            return "Str";
                        }
                    }

                    @Override
                    public String getMsdlSchemeNs(String schemeId) {
                        Id id = Id.Factory.loadFrom(schemeId);
                        Definition enumeration = AdsSearcher.Factory.newAdsDefinitionSearcher(getModule()).findById(id).get();
                        if (enumeration instanceof AdsMsdlSchemeDef) {
                            return ((AdsMsdlSchemeDef) enumeration).getTargetNamespace();
                        } else {
                            return null;
                        }
                    }

                });
            } catch (XmlException ex) {
                Logger.getLogger(AbstractXmlDefinition.class.getName()).log(Level.FINE, null, ex);
                return null;
            }
            if (xDoc != null && xDoc.getSchema() != null) {
                result = new SchemaDocument.Schema[]{xDoc.getSchema()};
            } else {
                return null;
            }
        } else {
            return null;
        }

        return result;
    }
    private SchemaTypeSystem[] xbeansTs = null;
    private static final SchemaTypeSystem[] NO_STS = new SchemaTypeSystem[0];

    public void setXBeansTs(SchemaTypeSystem[] sts) {
        synchronized (tsComputeLock) {
            this.xbeansTs = sts;
        }
    }

    public SchemaTypeSystem[] getXBeansTs() {
        synchronized (tsComputeLock) {
            return xbeansTs;
        }
    }

    @Override
    public XBeansTypeSystem getSchemaTypeSystem() {
        return getSchemaTypeSystem(null);
    }

    public XBeansTypeSystem getSchemaTypeSystem(IProblemHandler problemHandler) {
        synchronized (typeListComputeLock) {
            if (typeSystem == null) {
                buildXBeansTs(problemHandler);
                if (xbeansTs != null) {
                    typeSystem = new XBeansTypeSystem(this, xbeansTs);
                }
            }
            return typeSystem;
        }
    }

    public File[] saveXBeansTs(File folder) {
        return saveXBeansTs(folder, null);
    }

    public File[] saveXBeansTs(File folder, IProblemHandler ph) {
        return saveXBeansTs(folder, ph, false);
    }

    private static final Field localHandlesField;
    private static final Field handlesMapField;

    static {
        Field f1 = null;
        Field f2 = null;
        try {
            f1 = SchemaTypeSystemImpl.class.getDeclaredField("_localHandles");
            f1.setAccessible(true);
            Class clazz = Class.forName(SchemaTypeSystemImpl.class.getName() + "$HandlePool");
            f2 = clazz.getDeclaredField("_handlesToRefs");
            f2.setAccessible(true);
        } catch (Throwable ex) {
            Logger.getLogger(AbstractXmlDefinition.class.getName()).log(Level.SEVERE, null, ex);
        }
        localHandlesField = f1;
        handlesMapField = f2;
    }

    private static void resolveHandles(SchemaTypeSystem sts) {
        try {
            if (localHandlesField != null && handlesMapField != null) {
                Object value = localHandlesField.get(sts);
                if (value != null) {
                    Map map = (Map) handlesMapField.get(value);
                    if (map != null) {
                        final List list = new ArrayList(map.keySet());
                        for (Object obj : list) {
                            Object val = map.get(obj);
                            if (val instanceof SchemaComponent.Ref) {
                                ((SchemaComponent.Ref) val).getComponent();
                            }
                        }
                        if (map.size() != list.size()) {
                            System.err.println("catch");
                        }
                    }

                }
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(AbstractXmlDefinition.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public File[] saveXBeansTs(File folder, IProblemHandler problemHandler, boolean reset) {
        synchronized (tsComputeLock) {
            try {
                SchemaTypeSystem[] sts = buildXBeansTs(null);
                if (sts == null || sts.length == 0) {
                    return new File[0];
                } else {
                    final AdsXmlFilerImpl filer = new AdsXmlFilerImpl(folder);
                    resolveHandles(sts[0]);
                    try {
                        sts[0].save(filer);
                    } catch (ConcurrentModificationException ex) {
                        //Try again
                        try {
                            sts[0].save(filer);
                        } catch (ConcurrentModificationException ex2) {
                            if (problemHandler != null) {
                                problemHandler.accept(RadixProblem.Factory.newError(this, "Unable to save schema type system. Incremental resolve. See designer log file for details"));
                                Logger.getLogger(AbstractXmlDefinition.class.getName()).log(Level.SEVERE, null, ex);
                                return new File[0];
                            }
                        }
                    }
                    File[] stsFiles = filer.getFiles();

                    try {
                        File srcFile = new File(folder, getJavaSourceSupport().getContentResourceName());
                        getXmlDocument().save(srcFile, new XmlOptions().setSavePrettyPrint().setSaveNamespacesFirst());
                        File[] allFiles = new File[stsFiles.length + 1];
                        System.arraycopy(stsFiles, 0, allFiles, 0, stsFiles.length);
                        allFiles[stsFiles.length] = srcFile;
                        return allFiles;
                    } catch (IOException ex) {
                        //ignore
                    }

                    return stsFiles;
                }
            } finally {
                if (reset) {
                    xbeansTs = null;
                }
            }
        }
    }

    public SchemaTypeSystem[] buildXBeansTs(IProblemHandler problemHandler) {
        synchronized (tsComputeLock) {
            if (xbeansTs == null || xbeansTs == NO_STS) {
                if (this instanceof AdsXmlSchemeDef) {
                    AdsXmlSchemeDef xsd = (AdsXmlSchemeDef) this;
                    if (xsd.isTransparent()) {
                        return this.xbeansTs = ((AdsSegment) getModule().getSegment()).getBuildPath().getPlatformXml().getSts(this);
                    }
                }
                AdsXbeansTypeLoader loader = new AdsXbeansTypeLoader(this);
                SchemaTypeSystem ts = loader.resolve(problemHandler);
                if (ts == null) {
                    return this.xbeansTs = NO_STS;
                } else {
                    return this.xbeansTs = new SchemaTypeSystem[]{ts};
                }
            }
            return xbeansTs;
        }
    }

    public Map<String, char[]> generateSources(IProblemHandler problemHandler) {
        SchemaTypeSystem[] ts = buildXBeansTs(problemHandler);
        if (ts == null || ts.length == 0) {
            return null;
        }
        final AdsXmlFilerImpl filer = new AdsXmlFilerImpl(null);
        SchemaTypeSystemCompiler.generateTypes(ts[0], filer, new XmlOptions()
                .setSchemaCodePrinter(new XbeansSchemaCodePrinter()).setGenerateJavaVersion("1.5"));
        return filer.getSources();
    }

    public File[] generateSourceFiles(File baseDir) {
        SchemaTypeSystem[] ts = buildXBeansTs(null);
        if (ts == null || ts.length == 0) {
            return null;
        }
        final AdsXmlFilerImpl filer = new AdsXmlFilerImpl(baseDir);
        SchemaTypeSystemCompiler.generateTypes(ts[0], filer, new XmlOptions()
                .setSchemaCodePrinter(new XbeansSchemaCodePrinter()).setGenerateJavaVersion("1.5"));
        return filer.getFiles();
    }

    @Override
    public boolean delete() {
        synchronized (typeListComputeLock) {
            //typeList = null;
            if (!isFixedTypeSystem) {
                typeSystem = null;
            }
        }
        return super.delete();
    }

    @Override
    public Collection<String> getSchemaTypeList() {
        XBeansTypeSystem ts = getSchemaTypeSystem();
        if (ts == null) {
            return Collections.emptySet();
        } else {
            return ts.getTypeList();
        }
    }

    @Override
    public String getXmlText() {
        XmlObject obj = getXmlDocument();
        if (obj != null) {
            XmlOptions options = new XmlOptions();
            options.setSavePrettyPrint();
            return obj.xmlText(options);
        } else {
            return null;
        }
    }
}
