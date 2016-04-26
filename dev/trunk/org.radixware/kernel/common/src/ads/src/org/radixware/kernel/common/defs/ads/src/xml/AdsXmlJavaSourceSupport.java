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

package org.radixware.kernel.common.defs.ads.src.xml;

import java.io.File;
import java.io.IOException;
import java.util.*;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument.Import;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.build.Make;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.defs.ads.platform.BuildPath;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaFileSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.defs.ads.type.XmlType;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.CharOperations;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.XmlObjectProcessor;
import org.radixware.kernel.common.utils.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xmlsoap.schemas.wsdl.TImport;

/**
 * JavaSourceSupport for xmlbeans providing definitions: <ul>
 * <li>{@linkplain AdsXmlSchemeDef}</li> <li>{@linkplain AdsMsdlSchemeDef}</li>
 *
 */
public class AdsXmlJavaSourceSupport extends JavaSourceSupport {

    public static final class Factory {

        public static AdsXmlJavaSourceSupport newInstance(AdsXmlSchemeDef scheme) {
            return new AdsXmlJavaSourceSupport(scheme);
        }

        public static AdsXmlJavaSourceSupport newInstance(AdsMsdlSchemeDef scheme) {
            return new AdsXmlJavaSourceSupport(scheme);
        }
    }
    private BuildPath buildPath;

    protected AdsXmlJavaSourceSupport(IJavaSource root) {
        super(root);
        if (((AdsDefinition) root).isInBranch()) {
            buildPath = ((AdsSegment) ((AdsDefinition) root).getModule().getSegment()).getBuildPath();
        }
    }

    private IXmlDefinition getDefinition() {
        return (IXmlDefinition) getCurrentRoot();
    }

    @Override
    public CodeWriter getCodeWriter(UsagePurpose purpose) {
        return new CodeWriter(this, purpose) {
            @Override
            public boolean writeCode(CodePrinter printer) {
                return false;
            }

            @Override
            public void writeUsage(CodePrinter printer) {
            }
        };
    }

    @Override
    public EnumSet<ERuntimeEnvironmentType> getSupportedEnvironments() {
        return EnumSet.of(((IXmlDefinition) getCurrentRoot()).getTargetEnvironment());
    }

    @Override
    public JavaFileSupport.FileWriter getSourceFileWriter() {
        return new XmlBeansSourceFileWriter();
    }

    private class SchemeTree {

        private ArrayList<SchemeTree> children;
        private final String targetNamespace;
        private final IXmlDefinition def;
        private final XmlObject schemaDoc;
        private String fileName;

        public SchemeTree(String namespace, IXmlDefinition def, XmlObject schemaDoc) {
            this.targetNamespace = namespace;
            this.def = def;
            this.schemaDoc = schemaDoc;
            if (def == null /*
                     * || def.isTransparent()
                     */) {
                this.fileName = null;//namespace.replace("http", "file");//tns2fileName(targetNamespace);
            } else {
                this.fileName = ((AdsDefinition) def).getId().toString();
            }
        }

        private String tns2fileName(String tns) {
            return String.valueOf(tns.hashCode()) + ".xml";
        }

        private void addChild(SchemeTree child) {
            synchronized (this) {
                if (children == null) {
                    children = new ArrayList<>();
                }
                children.add(child);
            }
        }

        public void buildTree(AdsSearcher.Factory.XmlDefinitionSearcher searcher, Map<String, SchemeTree> processedNamespaces) {
            synchronized (this) {
                List<String> inss = null;


                List<String> notIgnoreForDef = null;
                if (def != null) {
                    notIgnoreForDef = XmlUtils.getImportedNamespaces(def.getXmlContent(), true);
                    inss = def.getImportedNamespaces();
                } else if (this.schemaDoc != null) {
                    inss = XmlUtils.getImportedNamespaces(this.schemaDoc);
                    notIgnoreForDef = XmlUtils.getImportedNamespaces(this.schemaDoc, true);
                }
                if (inss != null) {
                    for (String ns : inss) {
                        if (processedNamespaces.get(ns) != null) {
                            continue;
                        }
                        if (notIgnoreForDef != null && notIgnoreForDef.contains(ns)) {
                            IXmlDefinition childDef = searcher.findByNs(ns).get();
                            XmlObject platformSchema = null;
                            if (childDef == null) {
                                platformSchema = buildPath.getPlatformXml().findXmlSchema(ns, getDefinition().getTargetEnvironment());
                            }
                            SchemeTree childTree = new SchemeTree(ns, childDef, platformSchema);
                            addChild(childTree);
                            processedNamespaces.put(ns, childTree);
                            childTree.buildTree(searcher, processedNamespaces);
                        }
                    }
                }
//                if (children != null) {
//                    for (SchemeTree child : children) {
//                        child.buildTree(searcher, processedNamespaces);
//                    }
//                }
            }
        }
    }

    private UsagePurpose getUsagePurpose() {
        return UsagePurpose.getPurpose(((AdsDefinition) getCurrentRoot()).getUsageEnvironment(), CodeType.EXCUTABLE);
    }

    public String getContentResourceName() {
        char[][] names = JavaSourceSupport.getPackageNameComponents(getCurrentRoot(), getUsagePurpose());
        StringBuilder result = new StringBuilder();
        result.append(CharOperations.merge(names, '/'));
        result.append('/');
        result.append(getCurrentRoot().getId().toCharArray());
        result.append(".xml");
        return result.toString();
    }

    

    
    public class XmlBeansSourceFileWriter extends JavaSourceSupport.SourceFileWriter {

        private BuildPath buildPath;

        public XmlBeansSourceFileWriter() {
            buildPath = ((AdsSegment) getCurrentRoot().getModule().getSegment()).getBuildPath();
        }

        private boolean writeXml(File packagesRoot) throws IOException {
            IXmlDefinition def = getDefinition();
            if (def == null) {
                return false;
            }
            XmlObject doc = def.getXmlDocument();
            if (doc == null) {
                return false;
            }
            File xmlFile = new File(JavaSourceSupport.getPackageDir(packagesRoot, getCurrentRoot(), getUsagePurpose()), getCurrentRoot().getId().toString() + ".xml");
            File parentDir = xmlFile.getParentFile();
            if (!parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    return false;
                }
            }

            doc.save(xmlFile);
            return true;
        }

        public Map<Id, XBeansTypeSystem> computeTSS(final File packagesDir, final ERuntimeEnvironmentType env, final IProblemHandler problemHandler) throws IOException {
            Map<Id, XBeansTypeSystem> map = new HashMap<>();
//            if (!process(packagesDir, env, false, problemHandler, new ArrayList<File>(), map)) {
//                return Collections.emptyMap();
//            } else {
//                return map;
//            }
            return map;
        }

        @Override
        public boolean write(final File packagesDir, final UsagePurpose purpose, final boolean force, final IProblemHandler problemHandler, final Collection<File> writtenFiles) throws IOException {
            return false;//process(packagesDir, purpose.getEnvironment(), force, problemHandler, writtenFiles, null);
        }

//        private boolean process(final File packagesDir, final ERuntimeEnvironmentType env, final boolean force, final IProblemHandler problemHandler, final Collection<File> writtenFiles, final Map<Id, XBeansTypeSystem> tsCollection) throws IOException {
//            try {
//                Generator.AdsEntityReslover.reset();
//                final IXmlDefinition target = getDefinition();
//
//                TransparentSchemasTmpStorage storage = getSharedData().findItemByClass(TransparentSchemasTmpStorage.class);
//
//                if (storage == null) {
//                    storage = new TransparentSchemasTmpStorage(getSharedData());
//                    getSharedData().registerItemByClass(storage);
//                }
//
//                final TransparentSchemasTmpStorage platformSchemeStorage = storage;
//                final File packagesRoot = JavaFileSupport.getBaseDirOrJarFile(((AdsDefinition) target).getModule(), env, JavaFileSupport.EKind.SOURCE);
//                final File packageDir = JavaSourceSupport.getPackageDir(packagesRoot, getCurrentRoot(), getUsagePurpose());
//
//                if (!force && packageDir.exists() && packageDir.lastModified() >= ((AdsDefinition) getCurrentRoot()).getFileLastModifiedTime() && tsCollection == null) {
//                    CompilerUtils.collectSourceFiles(packageDir, writtenFiles);
//                    if (!writtenFiles.isEmpty()) {
//                        return true;
//                    }
//                }
//
//                if (tsCollection == null) {
//                    FileUtils.deleteDirectory(packageDir);
//                }
//
//                if (!writeXml(packagesDir)) {
//                    return false;
//                }
//
//                if (getCurrentRoot() instanceof AdsXmlSchemeDef && ((AdsXmlSchemeDef) getCurrentRoot()).isTransparent() && tsCollection == null) {
//                    return true;
//                }
//
//                final Map<String, SchemeTree> map = new HashMap<>();
//
//                final SchemeTree tree = new SchemeTree(target.getTargetNamespace(), target, null);
//
//                map.put(target.getTargetNamespace(), tree);
//
//                tree.buildTree(AdsSearcher.Factory.newXmlDefinitionSearcher((AdsDefinition) getCurrentRoot()), map);
//
//                final File tmpSrcDir = new File(packagesDir, ((AdsDefinition) target).getId().toString() + "_xsd_src");
//                try {
//                    final boolean srcDirCreated;
//                    if (!tmpSrcDir.exists()) {
//                        srcDirCreated = tmpSrcDir.mkdirs();
//                    } else {
//                        if (tmpSrcDir.isDirectory()) {
//                            FileUtils.deleteDirectory(tmpSrcDir);
//                        } else {
//                            FileUtils.deleteFile(tmpSrcDir);
//                        }
//                        srcDirCreated = tmpSrcDir.mkdirs();
//                    }
//
//                    if (!srcDirCreated) {
//                        return false;
//                    }
//
//
//                    final String configStr = getConfigFileData(map, env);
//                    if (configStr == null) {
//                        return false;
//                    }
//
//                    final File configFile = new File(tmpSrcDir, "xb.conf");
//                    final FileOutputStream out = new FileOutputStream(configFile);
//                    try {
//                        FileUtils.writeString(out, configStr, FileUtils.XML_ENCODING);
//                    } finally {
//                        try {
//                            out.close(); 
//                        } catch (IOException e) {
//                        }
//                    }
//
//                    final ArrayList<File> classPath = new ArrayList<>();
//
//                    final File baseDir = getCurrentRoot() != null && getCurrentRoot().getBranch() != null ? getCurrentRoot().getBranch().getDirectory() : null;
//                    if (!new SchemeTreeIterator(tree) {
//                        @Override
//                        boolean accept(final SchemeTree item) throws IOException {
//                            XmlObject schemaDocument;
//                            Id schemaId = null;
//
//                            boolean isTemporary = false;
//
//                            if (item.def == null || item.def.isTransparent()) {
//                                if (tsCollection != null && item.def != null) {
//                                    isTemporary = true;
//                                    schemaDocument = item.def.getXmlDocument();
//                                    schemaId = ((AdsDefinition) item.def).getId();
//                                } else {
//                                    final IJarDataProvider schemaJar = buildPath.getPlatformXml().findXmlSchemaJar(item.targetNamespace, env);
//                                    if (schemaJar != null) {
//                                        classPath.add(schemaJar.getFile());
//                                        return true;
//                                    } else {
//                                        if (problemHandler != null) {
//                                            problemHandler.accept(RadixProblem.Factory.newError((RadixObject) target, "Platform xml scheme with namespace " + item.targetNamespace + " not found"));
//                                        }
//                                        return false;
//                                    }
//                                }
//                            } else {
//                                if (item.def != target) {
//                                    if (!item.def.isReadOnly()) {
//                                        final JavaSourceSupport support = item.def.getJavaSourceSupport();
//                                        support.setSharedData(getSharedData());
//                                        final File[] addCp = new JavaFileSupport((IJavaSource) item.def, env).writePackageContent(support.getSourceFileWriter(), false, problemHandler);
//                                        if (addCp != null) {
//                                            classPath.add(JavaFileSupport.getBaseDirOrJarFile(((AdsDefinition) item.def).getModule(), env, JavaFileSupport.EKind.SOURCE));
//                                            return true;
//                                        } else {
//                                            if (problemHandler != null) {
//                                                problemHandler.accept(RadixProblem.Factory.newError((RadixObject) target, ((AdsDefinition) item.def).getQualifiedName() + " classes not found"));
//                                            }
//                                            return false;
//                                        }
//                                    } else {
//                                        IJarDataProvider jarData = JavaFileSupport.getCompiledBinary(((AdsDefinition) item.def).getModule(), env);
//                                        File file = null;
//                                        if (jarData != null) {
//                                            file = jarData.getFile();
//                                        }
//                                        //File file = JavaFileSupport.getBaseDirOrJarFile(((AdsDefinition) item.def).getModule(), env, JavaFileSupport.EKind.BINARY);
//                                        if (file != null && file.exists()) {
//                                            classPath.add(file);
//                                            return true;
//                                        } else {
//                                            if (problemHandler != null) {
//                                                problemHandler.accept(RadixProblem.Factory.newError((RadixObject) target, ((AdsDefinition) item.def).getQualifiedName() + " classes not found"));
//                                            }
//                                            return false;
//                                        }
//                                    }
//                                } else {
//                                    schemaDocument = item.def.getXmlDocument();
//                                    schemaId = ((Definition) item.def).getId();
//                                }
//                            }
//
//                            if (schemaDocument != null) {
//                                if (schemaDocument instanceof MessageElementDocument) {
//                                    try {
//                                        schemaDocument = XmlObjectMessagingXsdCreator.createXSD(((MessageElementDocument) schemaDocument).getMessageElement(), schemaId.toString(), new XmlObjectMessagingXsdCreator.EnumTypeResolver() {
//                                            @Override
//                                            public String getEnumTypeName(String enumId) {
//                                                Id id = Id.Factory.loadFrom(enumId);
//                                                AdsEnumDef enumeration = AdsSearcher.Factory.newAdsEnumSearcher(((Definition) item.def).getModule()).findById(id).get();
//                                                if (enumeration != null) {
//                                                    if (enumeration.getItemType() == EValType.CHAR) {
//                                                        return "Char";
//                                                    } else {
//                                                        return "Str";
//                                                    }
//                                                } else {
//                                                    return "Str";
//                                                }
//                                            }
//                                        });
//                                    } catch (XmlException ex) {
//                                        if (problemHandler != null) {
//                                            problemHandler.accept(RadixProblem.Factory.newError((RadixObject) target, ex.getMessage()));
//                                        }
//                                        return false;
//                                    }
//                                }
//                                schemaDocument = schemaDocument.copy();
//                                prepareSchemeToCodeGeneration((AdsDefinition) target, schemaDocument, map);
//
//                                final File sourceDir;
//                                final File xml;
//                                if (isTemporary) {
//                                    final File tmpDir = platformSchemeStorage.getTmpDir();
//                                    if (tmpDir == null) {
//                                        if (problemHandler != null) {
//                                            problemHandler.accept(RadixProblem.Factory.newError((RadixObject) target, "Can not process transparent xml scheme: file cache is not available"));
//                                        }
//                                        return false;
//                                    }
//                                    sourceDir = new File(tmpDir, item.fileName + "_folder");
//                                    if (sourceDir.exists()) {
//                                        classPath.add(sourceDir);
//                                        return true;
//                                    }
//                                    sourceDir.mkdir();
//                                    xml = new File(tmpDir, item.fileName);
//                                } else {
//                                    sourceDir = packagesDir;
//                                    xml = new File(tmpSrcDir, item.fileName);
//                                }
//
//                                schemaDocument.save(xml);
//
//
//                                File[] cp = new File[classPath.size()];
//                                classPath.toArray(cp);
//
//                                if (tsCollection == null) {
//                                    if (!new Generator(item.def, buildPath, problemHandler).generate(baseDir, sourceDir, xml, configFile, cp, schemaDocument instanceof DefinitionsDocument, writtenFiles)) {
//                                        return false;
//                                    }
//                                } else {
//                                    try {
//                                        if (!new Generator(item.def, buildPath, problemHandler).computeXsdTypes((AbstractXmlDefinition) item.def, schemaId, baseDir, sourceDir, xml, configFile, cp, schemaDocument instanceof DefinitionsDocument, tsCollection)) {
//                                            return false;
//                                        }
//                                    } finally {
//                                        //FileUtils.deleteDirectory(packageDir);
//                                    }
//                                }
//
//
//                                classPath.add(sourceDir);
//                            }
//                            return true;
//                        }
//                    }.process()) {
//                        //  cleanUp(packagesDir);
//                        return false;
//                    } else {
//                        return true;
//                    }
//                } finally {
//                    if (tmpSrcDir.exists()) {
//                        FileUtils.deleteDirectory(tmpSrcDir);
//                    }
//                }
//            } finally {
//                Generator.AdsEntityReslover.reset();
//            }
//        }
        private String getConfigFileData(Map<String, SchemeTree> defs, ERuntimeEnvironmentType env) {
            Definition targetXml = getCurrentRoot();
            if (!(targetXml instanceof IAdsTypeSource)) {
                return null;
            }

            AdsType xmlType = ((IAdsTypeSource) targetXml).getType(EValType.XML, null);

            final char[] configPrefix;

            if (xmlType instanceof XmlType) {
                configPrefix = ((XmlType) xmlType).getJavaSourceSupport().getQualifiedPackageName(null);
            } else {
                return null;
            }

            StringBuilder b = new StringBuilder();
            b.append("<xb:config xmlns:xb=\"http://xml.apache.org/xmlbeans/2004/02/xbean/config\">\n");

            for (Map.Entry<String, SchemeTree> e : defs.entrySet()) {
                IXmlDefinition xdef = e.getValue().def;
                if (xdef == null) {
                    continue;
                }

                if (xdef instanceof AdsXmlSchemeDef) {
                    AdsXmlSchemeDef scheme = (AdsXmlSchemeDef) xdef;
                    if (scheme.isTransparent()) {
                        continue;
                    }
                }

                b.append("<xb:namespace uri=\"");
                b.append(xdef.getTargetNamespace());
                b.append("\">\n");
                b.append("<xb:package>");
                b.append(configPrefix);
                b.append("</xb:package>\n");
                b.append("</xb:namespace>\n");
            }


            b.append("<xb:extension for=\"*\">\n");
            b.append("  <xb:interface  name=\"org.radixware.kernel.common.build.xbeans.IXBeansChangeEmitter\">\n");
            b.append("     <xb:staticHandler>org.radixware.kernel.common.build.xbeans.XBeansChangeEmitterHandler</xb:staticHandler>\n");
            b.append("  </xb:interface>\n");
            b.append("</xb:extension>\n");
            b.append("<xb:extension for=\"*\">\n");
            b.append("  <xb:prePostSet>\n");
            b.append("     <xb:staticHandler>org.radixware.kernel.common.build.xbeans.XBeansChangeEmitterHandler</xb:staticHandler>\n");
            b.append("  </xb:prePostSet>\n");
            b.append("</xb:extension>\n");



            if (getCurrentRoot() instanceof AdsMsdlSchemeDef) {
                b.append("<xb:extension for=\"");
                b.append(configPrefix);
                b.append(".MessageInstanceDocument\">\n");
                //b.append("*\">\n");
                b.append("  <xb:interface  name=\"org.radixware.kernel.common.msdl.XmlObjectMessagingInterface\">\n");
                b.append("     <xb:staticHandler>org.radixware.kernel.common.msdl.XmlObjectMessagingHandler</xb:staticHandler>\n");
                b.append("  </xb:interface>\n");
                b.append("</xb:extension>\n");
            }

            b.append("\n</xb:config>");
            return b.toString();
        }
    }

    private static String getSchemaLocation(String namespace, String srcLocation, Map<String, SchemeTree> imports) {
        if (srcLocation != null && srcLocation.startsWith("radix:/")) {//force use file from kernel of layer
            return srcLocation;
        } else {
            SchemeTree imp = imports.get(namespace);
            if (imp != null) {
                return imp.fileName;
            } else {
                return null;
            }
        }

    }

    private static void prepareSchemeToCodeGeneration(AdsDefinition root, XmlObject object, Map<String, SchemeTree> imports) {
        XmlCursor cursor = null;
        try {
            cursor = object.newCursor();
            if (cursor.toFirstChild()) {
                do {
                    XmlObject child = cursor.getObject();
                    if (child instanceof Import) {
                        Import imp = (Import) child;
                        imp.setSchemaLocation(getSchemaLocation(imp.getNamespace(), imp.getSchemaLocation(), imports));

                    } else if (child instanceof TImport) {
                        TImport imp = (TImport) child;
                        imp.setLocation(getSchemaLocation(imp.getNamespace(), imp.getLocation(), imports));

                    } else if (child instanceof org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation) {
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
                                            AdsDefinition enumDef = AdsSearcher.Factory.newAdsDefinitionSearcher(root).findById(constSetId).get();
                                            if (enumDef instanceof AdsEnumDef) {
                                                AdsEnumDef e = (AdsEnumDef) enumDef;
                                                cc.getDomNode().getFirstChild().setNodeValue(new String(e.getType(e.getItemType(), null).getJavaSourceSupport().getQualifiedTypeName(UsagePurpose.getPurpose(ERuntimeEnvironmentType.COMMON, CodeType.EXCUTABLE))));
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
                            prepareSchemeToCodeGeneration(root, child, imports);
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

    @Override
    public Set<CodeType> getSeparateFileTypes(ERuntimeEnvironmentType sc) {
        return sc == this.getDefinition().getTargetEnvironment() ? EnumSet.of(CodeType.EXCUTABLE) : null;
    }
    private static final char[] MSDL_ROOT_CLASS_NAME = "MessageInstanceDocument".toCharArray();

    @Override
    public char[][] getMainClassName(UsagePurpose env) {
        if (getDefinition() instanceof AdsMsdlSchemeDef) {
            char[][] packages = getPackageNameComponents(getCurrentRoot(), env);
            char[][] mainClassName = new char[packages.length + 1][];
            System.arraycopy(packages, 0, mainClassName, 0, packages.length);
            mainClassName[packages.length + 1] = MSDL_ROOT_CLASS_NAME;
            return mainClassName;
        } else {
            return null;
        }
    }

    public XBeansTypeSystem computeTypeSystem() {
        try {
            IXmlDefinition def = getDefinition();
            if (getSharedData() == null) {
                setSharedData(new SharedData());
            }
            Make.SessionDataDir sddir = getSharedData().findItemByClass(Make.SessionDataDir.class);
            try {
                if (sddir == null) {
                    sddir = new Make.SessionDataDir();
                    getSharedData().registerItemByClass(sddir);
                }
                JavaFileSupport fileSupport = new JavaFileSupport((IJavaSource) getCurrentRoot(), getDefinition().getTargetEnvironment());


                File dir = fileSupport.getPackagesRoot(JavaFileSupport.EKind.SOURCE);
                Map<Id, XBeansTypeSystem> map = new XmlBeansSourceFileWriter().computeTSS(dir, getDefinition().getTargetEnvironment(), new IProblemHandler() {
                    @Override
                    public void accept(RadixProblem problem) {
                        if (problem.getSeverity() == RadixProblem.ESeverity.ERROR) {
                            System.err.println(problem.getSource().getQualifiedName() + ": " + problem.getMessage());
                        }
                    }
                });
                return map.get(((AdsDefinition) def).getId());
            } finally {
                if (sddir != null) {
                    sddir.cleanup();
                }
            }
        } catch (IOException ex) {
            return null;
        }
    }
}
