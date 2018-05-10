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
package org.radixware.kernel.common.defs.ads.build;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.AdsCompilationUnit;
import org.eclipse.jdt.internal.compiler.lookup.AdsCompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.AdsTypeRequestor;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.radixware.kernel.common.compiler.lookup.AdsWorkspace;
import org.eclipse.jdt.internal.compiler.util.SuffixConstants;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.compiler.CompilerConstants;
import org.radixware.kernel.common.compiler.CompilerUtils;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;
import org.radixware.kernel.common.components.ICancellable;
import org.radixware.kernel.common.components.IProgressHandle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportModelClassDef;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.MetaInfServicesCatalog.Service;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaFileSupport;
import org.radixware.kernel.common.defs.ads.src.JavaFileSupport.EKind;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.SharedData;
import org.radixware.kernel.common.defs.ads.src.xml.AdsXmlJavaSourceSupport;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomReportDialogDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomReportDialogDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtUIDef;
import org.radixware.kernel.common.defs.ads.xml.AbstractXmlDefinition;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.IValueSet;
import org.radixware.kernel.common.utils.JarFiles;

public class Make extends JavaAction {

    public static class MutabilityStatistics {

        long metaDefinitionsCount;
        long xmlDefinitionsCount;
        long enumMetaCount;
        long clcMetaCount;
        long otherMutable;
        long total;
        long otherImmutable;

        public void reset() {
            metaDefinitionsCount = 0;
            xmlDefinitionsCount = 0;
            otherMutable = 0;
            otherImmutable = 0;
            clcMetaCount = 0;
            total = 0;
            enumMetaCount = 0;
        }

    }

    public static MutabilityStatistics mcStat = new MutabilityStatistics();

    public static class SessionDataDir implements SharedData.Item {

        public SessionDataDir() {
        }
        private File rootDir = null;
        private boolean unavailable = false;

        /**
         * Returns root directory for make file cache Returns null if cache is
         * unavailable
         */
        public File getFile() {
            synchronized (this) {
                if (unavailable) {
                    return null;
                }
                if (rootDir == null) {
                    try {
                        rootDir = File.createTempFile("radix_platform_xml", "cache");
                        if (rootDir.exists()) {
                            rootDir.delete();
                        }
                        if (!rootDir.mkdirs()) {
                            unavailable = true;
                            return null;
                        }
                    } catch (IOException ex) {
                        unavailable = true;
                        return null;
                    }
                }
                return rootDir;
            }
        }

        @Override
        public void cleanup() {
            if (rootDir != null && rootDir.exists()) {
                FileUtils.deleteDirectory(rootDir);
            }
        }
    }

    public static class SessionFaults implements SharedData.Item {

        private HashMap<Id, IJavaSource> faults = null;

        public void fault(IJavaSource root) {
            synchronized (this) {
                if (faults == null) {
                    faults = new HashMap<>();
                }
                faults.put(((Definition) root).getId(), root);
            }
        }

        public boolean isFault(IJavaSource root) {
            synchronized (this) {
                return faults == null ? false : faults.containsKey(((AdsDefinition) root).getId());
            }
        }

        @Override
        public void cleanup() {
            faults = null;
        }

        private SessionFaults() {
        }
    }
    private final boolean storeBinaryInformation;
    private final SharedData sharedData;

    public Make(IFlowLogger flowLogger) {
        this(flowLogger, false);
    }

    public Make(IFlowLogger flowLogger, final boolean storeBinaryInformation) {
        super(flowLogger);
        this.storeBinaryInformation = storeBinaryInformation;
        this.sharedData = createSharedData();
    }

    public Make() {
        this(new NullFlowLogger(), false);
    }

    public Make(boolean storeBinaryInformation) {
        this(new NullFlowLogger(), storeBinaryInformation);
    }

    public static SharedData createSharedData() {
        SharedData sharedData = new SharedData();
        sharedData.registerItemByClass(new SessionDataDir());
        sharedData.registerItemByClass(new SessionFaults());
        return sharedData;
    }

    @Override
    public String getDisplayName() {
        return "Build";
    }
    //   private AdsCompiler compiler = null;
    private ArrayList<DefinitionInfo> addDists = null;

    private static final class DefinitionInfo {

        final Definition definition;
        final char[][] binaries;
        final char[] moduleDirPath;

        public DefinitionInfo(Definition definition, List<File> binaries) {
            this.definition = definition;
            this.moduleDirPath = definition.getModule().getDirectory().getAbsolutePath().toCharArray();
            int prefixLen = moduleDirPath.length;
            if (binaries != null) {
                this.binaries = new char[binaries.size()][];
                for (int i = 0, len = binaries.size(); i < len; i++) {
                    char[] filePath = binaries.get(i).getAbsolutePath().toCharArray();
                    if (CharOperation.prefixEquals(moduleDirPath, filePath)) {
                        this.binaries[i] = CharOperation.subarray(filePath, prefixLen, filePath.length);
                    }

                }
            } else {
                this.binaries = null;
            }

        }
    }
    private org.radixware.kernel.common.compiler.Compiler compiler;

    public static class Requestor implements ICompilerRequestor {

        public enum UnitProcessingStatus {

            SUCCESS,
            FAIL,
            NONE
        }
        public final IProblemHandler problemHandler;
        private final Make make;
        private final Set<Definition> savedXmlSts = new HashSet<>();
        private final Map<String, UnitProcessingStatus> processedDeclarations = new HashMap<>();
        private final ERuntimeEnvironmentType env;

        public Requestor(IProblemHandler problemHandler, Make make, ERuntimeEnvironmentType env) {
            this.problemHandler = problemHandler;
            this.make = make;
            this.env = env;
        }

        public UnitProcessingStatus getUnitProcessingStatus(AdsWorkspace.DeclarationData compilationUnit) {
            UnitProcessingStatus status = processedDeclarations.get(compilationUnit.getDeclarationFileName());
            return status == null ? UnitProcessingStatus.NONE : status;
        }

        private Object annotateProblem(AdsCompilationUnitDeclaration compilationUnit, int start, int end) {
            if (compilationUnit == null) {
                return null;
            }
            if (compilationUnit.radixObjectLookup != null) {
                final RadixObjectLocator.RadixObjectData[] descriptor = compilationUnit.radixObjectLookup.take(start, end);
                if (descriptor != null && descriptor.length > 0) {
                    int sdi, ldi;
                    final Scml.Item startItem;
                    final Scml.Item endItem;
                    if (descriptor[0].radixObject instanceof Scml.Item) {
                        startItem = (Scml.Item) descriptor[0].radixObject;
                        sdi = 0;
                    } else {
                        startItem = null;
                        sdi = -1;
                    }
                    if (descriptor.length > 1 && descriptor[descriptor.length - 1].radixObject instanceof Scml.Item) {
                        endItem = (Scml.Item) descriptor[descriptor.length - 1].radixObject;
                        ldi = descriptor.length - 1;
                    } else {
                        endItem = startItem;
                        ldi = sdi;
                    }

                    if (startItem != null && endItem != null) {

                        final int s = descriptor[sdi].convertSrcPosToObjectOffset(start);
                        final int e = descriptor[ldi].convertSrcPosToObjectOffset(end) + 1;
                        return new Scml.ScmlAreaInfo() {
                            @Override
                            public Scml getScml() {
                                return startItem.getOwnerScml();
                            }

                            @Override
                            public int getSourceStartOffset() {
                                return s;
                            }

                            @Override
                            public int getSourceEndOffset() {
                                return e;
                            }

                            @Override
                            public Scml.Item getStartJmlItem() {
                                return startItem;
                            }

                            @Override
                            public Scml.Item getEndJmlItem() {
                                return endItem;
                            }
                        };
                    } else {
                        return descriptor[0].radixObject;
                    }
                } else if (descriptor != null) {
                    return descriptor[0].radixObject;
                }
            }
            return compilationUnit.getContextDefinition();
        }

        public void acceptDefinition(Definition definition) {
            if (definition instanceof AbstractXmlDefinition && !savedXmlSts.contains(definition)) {
                AbstractXmlDefinition xmlDef = (AbstractXmlDefinition) definition;
                final JavaSourceSupport support = xmlDef.getJavaSourceSupport();
                final JavaFileSupport fileSupport = new JavaFileSupport((IJavaSource) definition, xmlDef.getUsageEnvironment());
                final File xmlTsRoot = fileSupport.getPackagesRoot(EKind.SOURCE);
                try {
                    if (xmlDef.isTransparent()) {
                        XmlObject xDoc = xmlDef.getXmlDocument();
                        if (xDoc != null) {
                            File srcFile = new File(xmlTsRoot, ((AdsXmlJavaSourceSupport) support).getContentResourceName());

                            srcFile.getParentFile().mkdirs();
                            xDoc.save(srcFile, new XmlOptions().setSavePrettyPrint().setSaveNamespacesFirst());
                            make.registerBinaries(ERuntimeEnvironmentType.COMMON, definition, new File[]{srcFile});
                        }
                    } else {
                        final File[] additionalFiles = xmlDef.saveXBeansTs(xmlTsRoot, problemHandler);
                        make.registerBinaries(ERuntimeEnvironmentType.COMMON, definition, additionalFiles);
                    }
                    savedXmlSts.add(definition);
                } catch (IOException ex) {
                    Logger.getLogger(Make.class.getName()).log(Level.SEVERE, null, ex);
                    if (problemHandler != null) {
                        problemHandler.accept(RadixProblem.Factory.newError(xmlDef, "Unable to save xml content to build output location"));
                    }
                }
            }

        }

        @Override
        public void acceptResult(CompilationResult result) {
            synchronized (this) {
                try {
                    AdsCompilationUnitDeclaration declaration = ((AdsCompilationUnit) result.compilationUnit).declaration;
                    if (processedDeclarations.containsKey(declaration.getUnitFileName())) {//unit already was accepted
                        return;
                    }

                    if (declaration.getWorkspace().getContextLayer()
                            != declaration.getContextDefinition().getLayer()) {
                        Logger.getLogger(Make.class.getName()).log(Level.FINE, "Skipping compilation unit from another layer:{0}. Current layer = {1}", new Object[]{declaration.getContextDefinition().getQualifiedName(), declaration.getWorkspace().getContextLayer().getURI()});
                        return;
                    }
                    UnitProcessingStatus status = result.hasErrors() ? UnitProcessingStatus.FAIL : UnitProcessingStatus.SUCCESS;
                    ClassFile[] classFiles = result.getClassFiles();
                    TypeDeclaration[] compiledTypes = declaration.types;

                    if (env == ERuntimeEnvironmentType.SERVER || env == ERuntimeEnvironmentType.COMMON) {
                        Definition def = declaration.getContextDefinition();

                        Set<String> set = make.workingSet.get(def.getModule());
                        if (set == null) {
                            set = new HashSet<>();
                            make.workingSet.put(def.getModule(), set);
                        }
                        boolean isMeta = declaration.getUsagePurpose().getCodeType() == JavaSourceSupport.CodeType.META;
                        boolean isException = def instanceof AdsClassDef && ((AdsClassDef) def).getClassDefType() == EClassType.EXCEPTION;
                        boolean isXml = def instanceof IXmlDefinition;
                        boolean isEnumMeta = isMeta && (def instanceof AdsEnumDef);
                        boolean isClcMeta = isMeta && (def instanceof AdsContextlessCommandDef);
                        if (isMeta || isException || isXml) {

                            for (int i = 0; i < compiledTypes.length; i++) {
                                TypeDeclaration type = compiledTypes[i];
                                String className = CompilerUtils.MutabilityCheckResult.getClassName(type.binding);

                                set.add(className);
                                make.mutabilityCheck.put(className, true);
                                if (isMeta) {
                                    if (isEnumMeta) {
                                        mcStat.enumMetaCount++;
                                    } else if (isClcMeta) {
                                        mcStat.clcMetaCount++;
                                    } else {
                                        mcStat.metaDefinitionsCount++;
                                    }
                                } else if (isXml) {
                                    mcStat.xmlDefinitionsCount++;
                                } else {
                                    mcStat.otherImmutable++;
                                }
                                mcStat.total++;

                            }
                        } else {
                            if (compiledTypes != null) {
                                for (int i = 0; i < compiledTypes.length; i++) {
                                    TypeDeclaration type = compiledTypes[i];
                                    if (type != null && type.binding != null) {
                                        String className = CompilerUtils.MutabilityCheckResult.getClassName(type.binding);
                                        set.add(className);
                                        if (CompilerUtils.isImmutable(type.binding, make.mutabilityCheck)) {
                                            mcStat.otherImmutable++;
                                        } else {
                                            mcStat.otherMutable++;
                                        }
                                        mcStat.total++;
                                    }
                                }
                            }
                        }
                    }

                    try {
                        if (classFiles != null) {
                            Definition definition = declaration.getContextDefinition();
                            if (definition instanceof IJavaSource) {
                                final File file = definition.getFile();
                                long time = System.currentTimeMillis();
                                if (file != null && file.lastModified() > time) {
                                    file.setLastModified(time);
                                }
                                try {
                                    JavaFileSupport support = new JavaFileSupport((IJavaSource) definition, declaration.getEnvironmentType());
                                    File[] writtenFiles = support.writePackageContent(new ClassFileWriter((IJavaSource) definition, classFiles), true, declaration.getUsagePurpose().getCodeType(), problemHandler);

                                    if (definition instanceof AbstractXmlDefinition && !savedXmlSts.contains(definition)) {
                                        AbstractXmlDefinition xmlDef = (AbstractXmlDefinition) definition;
                                        final File xmlTsRoot = support.getPackagesRoot(EKind.SOURCE);
                                        final File[] additionalFiles = xmlDef.saveXBeansTs(xmlTsRoot, problemHandler);
                                        if (make != null && additionalFiles.length > 0) {
                                            if (writtenFiles == null) {
                                                writtenFiles = additionalFiles;
                                            } else {
                                                File[] newArr = new File[writtenFiles.length + additionalFiles.length];
                                                System.arraycopy(writtenFiles, 0, newArr, 0, writtenFiles.length);
                                                System.arraycopy(additionalFiles, 0, newArr, writtenFiles.length, additionalFiles.length);
                                                writtenFiles = newArr;
                                            }
                                        }
                                        savedXmlSts.add(definition);
                                    }
                                    if (definition instanceof AdsClassDef) {
                                        AdsClassDef clazz = (AdsClassDef) definition;
                                        if (!clazz.getResources().isEmpty()) {
                                            File resourcesDir = support.getPackagesRoot(EKind.SOURCE);

                                            if (declaration.currentPackage != null) {
                                                for (int i = 0; i < declaration.currentPackage.tokens.length; i++) {
                                                    resourcesDir = new File(resourcesDir, String.valueOf(declaration.currentPackage.tokens[i]));
                                                }
                                            }
                                            if (resourcesDir.exists() || resourcesDir.mkdirs()) {
                                                List<File> additionalFiles = new LinkedList<>();
                                                for (AdsClassDef.Resources.Resource res : clazz.getResources()) {
                                                    if (res.getData() != null) {
                                                        File resourceFile = new File(resourcesDir, res.getName());
                                                        CompilerUtils.writeString(resourceFile, res.getData(), "UTF-8");
                                                        additionalFiles.add(resourceFile);
                                                    }
                                                }
                                                if (!additionalFiles.isEmpty()) {
                                                    if (writtenFiles != null) {
                                                        additionalFiles.addAll(Arrays.asList(writtenFiles));
                                                    }
                                                    writtenFiles = additionalFiles.toArray(new File[additionalFiles.size()]);
                                                }
                                            }
                                        }
                                    }
                                    if (definition instanceof AdsRwtUIDef) {
                                        AdsRwtUIDef rwtUIDef = (AdsRwtUIDef) definition;
                                        final JavaSourceSupport sup = rwtUIDef.getJavaSourceSupport();
                                        Set<JavaSourceSupport.CodeType> set = sup.getSeparateFileTypes(env);
                                        if (set.contains(JavaSourceSupport.CodeType.ADDON)) {
                                            final File[] additionalFiles = support.writePackageContent(sup.getSourceFileWriter(), true, JavaSourceSupport.CodeType.ADDON, problemHandler);
                                            if (make != null && additionalFiles.length > 0) {
                                                if (writtenFiles == null) {
                                                    writtenFiles = additionalFiles;
                                                } else {
                                                    File[] newArr = new File[writtenFiles.length + additionalFiles.length];
                                                    System.arraycopy(writtenFiles, 0, newArr, 0, writtenFiles.length);
                                                    System.arraycopy(additionalFiles, 0, newArr, writtenFiles.length, additionalFiles.length);
                                                    writtenFiles = newArr;
                                                }
                                            }
                                        }
                                    }
                                    if (make != null) {
                                        make.registerBinaries(declaration.getEnvironmentType(), definition, writtenFiles);
                                    }
                                } catch (IOException ex) {
                                    Logger.getLogger(Make.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                        if (result.problems != null) {
                            for (int p = 0; p < result.problemCount; p++) {
                                final CategorizedProblem problem = result.problems[p];
                                if (problemHandler != null) {
                                    RadixObject source = null;
                                    Scml.ScmlAreaInfo info = null;
                                    final Object annotation = annotateProblem(declaration, problem.getSourceStart(), problem.getSourceEnd());
                                    if (annotation instanceof Scml.ScmlAreaInfo) {
                                        info = (Scml.ScmlAreaInfo) annotation;
                                        source = info.getScml();
                                    } else if (annotation instanceof RadixObject) {
                                        source = (RadixObject) annotation;
                                    }
                                    if (source != null) {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append(declaration.getEnvironmentType().getName()).append(": ");
                                        while (sb.length() < 16) {
                                            sb.append(' ');
                                        }
                                        final String env = sb.toString();
                                        final RadixProblem radixProblem;
                                        if (problem.isError()) {
                                            radixProblem = RadixProblem.Factory.newError(source, env + problem.getMessage(), info);
                                        } else if (problem.isWarning()) {
                                            Definition def = source.getOwnerDefinition();
                                            AdsDefinition adsDef = null;
                                                while (def != null) {
                                                    if (def instanceof AdsDefinition) {
                                                        adsDef = (AdsDefinition) def;
                                                        break;
                                                    }
                                                    def = def.getOwnerDefinition();
                                                }
                                            if (adsDef == null || !adsDef.isCompilerWarningSuppressed(problem.getID())) {
                                                radixProblem = RadixProblem.Factory.newWarning(source, env + problem.getMessage(), info);
                                            } else {
                                                radixProblem = null;
                                            }
                                        } else {
                                            radixProblem = null;
                                        }
                                        if (radixProblem != null) {
                                            radixProblem.setNativeCode(problem.getID());
                                            problemHandler.accept(radixProblem);
                                        }
                                    }
                                }
                            }
                        }
                    } finally {
                        processedDeclarations.put(declaration.getUnitFileName(), status);
                    }

                } finally {
                    result.compiledTypes.clear();
                    result.problems = null;
                }
            }
        }
    }
    private Map<ERuntimeEnvironmentType, Map<Definition, File[]>> resultMap;

    private void registerBinaries(ERuntimeEnvironmentType env, Definition definition, File[] classFiles) {
        if (resultMap == null) {
            resultMap = new HashMap<>();
        }
        Map<Definition, File[]> envMap = resultMap.get(env);
        if (envMap == null) {
            envMap = new HashMap<>();
            resultMap.put(env, envMap);
        }
        if (storeBinaryInformation) {
            File[] alreadyStoredFiles = envMap.get(definition);
            if (alreadyStoredFiles != null) {
                File[] dummy = new File[alreadyStoredFiles.length + classFiles.length];
                System.arraycopy(alreadyStoredFiles, 0, dummy, 0, alreadyStoredFiles.length);
                System.arraycopy(classFiles, 0, dummy, alreadyStoredFiles.length, classFiles.length);
                envMap.put(definition, dummy);
            } else {
                envMap.put(definition, classFiles);
            }
        } else {
            envMap.put(definition, null);
        }
    }

    private Map<Definition, File[]> getDistributableData(ERuntimeEnvironmentType env) {
        if (resultMap == null) {
            return Collections.emptyMap();
        } else {
            Map<Definition, File[]> envMap = resultMap.get(env);
            if (envMap == null) {
                return Collections.emptyMap();
            }
            return Collections.unmodifiableMap(envMap);
        }
    }

    private final Map<String, Boolean> mutabilityCheck = new HashMap<>();
    private final Map<Module, Set<String>> workingSet = new HashMap<>();

    public void compile(Layer contextLayer, Collection<Definition> definitions, ERuntimeEnvironmentType env, final IProblemHandler problemHandler, final Map<Id, Id> idReplaceMap, boolean force, boolean userMode, IProgressHandle progressHandler, ICancellable cancellable) {
        //System.gc();
        //order by layer
        AdsTypeRequestor.resetBinariesCache();
        Map<Module, List<Definition>> modules = new HashMap<>();

        for (Definition definition : definitions) {
            if (definition instanceof Module) {
                Module module = (Module) definition;
                if (!modules.containsKey(module)) {
                    module.enableAutoUpload(false);
                    modules.put(module, null);
                }
                continue;
            }

            Module module = definition.getModule();
            List<Definition> list = modules.get(module);
            if (list == null) {

                list = new ArrayList<>();
                modules.put(module, list);
            }
            list.add(definition);
        }

        if (compiler == null || compiler.getProblemHandler() != problemHandler) {
            compiler = new org.radixware.kernel.common.compiler.Compiler(problemHandler);
        }

        if (progressHandler != null) {
            progressHandler.switchToDeterminate(modules.size());
            progressHandler.setDisplayName("Build " + contextLayer.getName() + "(" + env.getName() + "): ");
        }
        int progress = 0;
        final List<Module> allModules = new ArrayList<>(modules.keySet());
        Collections.sort(allModules, new Comparator< Module>() {
            @Override
            public int compare(Module o1, Module o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        final Requestor requestor = new Requestor(problemHandler, this, env);
        try {
            for (Module module : allModules) {

                if (cancellable != null && cancellable.wasCancelled()) {
                    break;
                }

                final List<Definition> moduleList = modules.get(module);

                Collections.sort(moduleList, new Comparator<Definition>() {
                    @Override
                    public int compare(Definition o1, Definition o2) {
                        return o1.getId().compareTo(o2.getId());
                    }
                });
                if (module instanceof AdsModule) {
                    if (((AdsModule) module).getJavaSourceSupport().isSeparateFilesRequired(env)) {
                        moduleList.add(module);
                    }
                }

                if (progressHandler != null) {
                    progressHandler.setDisplayName("Build module: " + module.getQualifiedName() + " (" + env.getName() + "): ");
                }
                if (!moduleList.isEmpty()) {
                    compiler.compile(contextLayer, moduleList, env, requestor, force, null, cancellable, false);
                }
                if (progressHandler != null) {
                    progressHandler.progress(progress++);
                }

            }
        } finally {
            for (Module module : modules.keySet()/*c.modules*/) {
                module.enableAutoUpload(true);
            }
            compiler = null;
        }
        //}
    }

    public void compile(Definition definition, ERuntimeEnvironmentType env, final IProblemHandler problemHandler, final Map<Id, Id> idReplaceMap, boolean force, boolean userMode) {
        //System.gc();
        Layer contextLayer = definition.getModule().getSegment().getLayer();
        Branch contextBranch = contextLayer.getBranch();

        if (contextBranch != null) {
            compiler = new org.radixware.kernel.common.compiler.Compiler(problemHandler);

            List<Definition> compileList = new LinkedList<>();
            compileList.add(definition);

            compiler.compile(definition.getLayer(), compileList, env, new Requestor(problemHandler, this, env), force, null, null, userMode);
        }
        compiler = null;
    }

    public void compile(AdsWorkspace sharedWorkspace, Definition definition, final Map<Id, Id> idReplaceMap, boolean force, boolean userMode) {
        //System.gc();
        Layer contextLayer = sharedWorkspace.getContextLayer();
        Branch contextBranch = contextLayer.getBranch();

        if (contextBranch != null) {
            compiler = new org.radixware.kernel.common.compiler.Compiler(sharedWorkspace.getReporter().getRadixProblemHandler());

            List<Definition> compileList = new LinkedList<>();
            compileList.add(definition);

            compiler.compile(sharedWorkspace, compileList, new Requestor(sharedWorkspace.getReporter().getRadixProblemHandler(), this, sharedWorkspace.getEnvType()), force, null, null, userMode);
        }
        compiler = null;
    }

    public static class Distribution {

        private final Module module;
        protected Map<Definition, DefinitionInfo> defs = null;
        protected final ERuntimeEnvironmentType env;

        public Distribution(Module module, ERuntimeEnvironmentType env) {
            this.module = module;
            this.env = env;
        }

        public Module getModule() {
            return module;
        }

        private void addDef(DefinitionInfo def) {
            if (defs == null) {
                defs = new HashMap<>();
            }
            defs.put(def.definition, def);
        }

        public boolean contains(final AdsDefinition def) {
            return defs != null && defs.containsKey(def);
        }

        public static List<CompoundDistribution> merge(List<List<Distribution>> lists) {
            if (lists == null) {
                return Collections.emptyList();
            }
            final Map<Definition, CompoundDistribution> distributions = new HashMap<>();

            for (List<Distribution> list : lists) {
                if (list == null) {
                    continue;
                }
                for (Distribution dist : list) {
                    if (dist == null) {
                        continue;
                    }
                    if (dist.defs != null) {
                        for (DefinitionInfo def : dist.defs.values()) {
                            Definition main = def.definition;
                            if (main instanceof AdsLocalizingBundleDef) {
                                main = ((AdsLocalizingBundleDef) main).findBundleOwner();
                            } else if (main instanceof AdsReportModelClassDef) {
                                main = ((AdsReportModelClassDef) main).getOwnerClass();
                            } else if (main instanceof AdsCustomReportDialogDef) {
                                main = ((AdsCustomReportDialogDef) main).getOwnerClass();
                            } else if (main instanceof AdsRwtCustomReportDialogDef) {
                                main = ((AdsRwtCustomReportDialogDef) main).getOwnerClass();
                            }
                            if (main != null) {
                                CompoundDistribution reg = distributions.get(main);
                                if (reg == null) {
                                    reg = new CompoundDistribution(main);
                                    distributions.put(main, reg);
                                }
                                reg.addDefInfo(dist.env, def);

                            }
                        }
                    }
                }
            }
            return new ArrayList<>(distributions.values());
        }
    }

    public static class CompoundDistribution extends SimpleJarDistribution {

        private Definition def;
        private Map<ERuntimeEnvironmentType, List<DefinitionInfo>> infos;

        public CompoundDistribution(Definition locator) {
            super(null, locator.getModule());
            this.def = locator;
        }

        public Definition getDefinition() {
            return def;
        }

        private void addDefInfo(ERuntimeEnvironmentType env, DefinitionInfo info) {
            if (infos == null) {
                infos = new HashMap<>();
            }
            List<DefinitionInfo> envs = infos.get(env);
            if (envs == null) {
                envs = new LinkedList<>();
                infos.put(env, envs);
            }
            envs.add(info);
            //infos.put(env, info);
        }

        public File mkJar(IProblemHandler problemHandler) {
            if (infos != null) {
                //final List<File> binaries = defs.get(def).binaries;

                List<File> srcDirs = new ArrayList<>(2);
                final List<File> totalBinaries = new ArrayList<>();
                final List<File> imageFiles = new ArrayList<>();
                for (Map.Entry<ERuntimeEnvironmentType, List<DefinitionInfo>> e : infos.entrySet()) {
                    for (DefinitionInfo info : e.getValue()) {

                        if (info.binaries != null && info.binaries.length > 0) {
                            File baseDir = new File(String.valueOf(info.moduleDirPath));
                            for (int i = 0; i < info.binaries.length; i++) {
                                totalBinaries.add(new File(baseDir, String.valueOf(info.binaries[i])));
                            }
                            File sourceDir = JavaFileSupport.getBaseDirOrJarFile(getModule(), e.getKey(), EKind.SOURCE);
                            srcDirs.add(sourceDir);
                            if (e.getKey() == ERuntimeEnvironmentType.COMMON) {
                                final List<Definition> dependes = new LinkedList<>();
                                def.visit(new IVisitor() {
                                    @Override
                                    public void accept(RadixObject radixObject) {
                                        radixObject.collectDependences(dependes);
                                    }
                                }, VisitorProviderFactory.createDefaultVisitorProvider());

                                for (Definition dep : dependes) {
                                    if (dep instanceof AdsImageDef) {
                                        AdsImageDef image = (AdsImageDef) dep;
                                        File file = image.getImageFile();
                                        if (file != null && file.exists()) {
                                            try {
                                                File targetFile = new File(sourceDir, file.getName());
                                                FileUtils.copyFile(file, targetFile);
                                                imageFiles.add(targetFile);
                                            } catch (IOException ex) {
                                                Logger.getLogger(Make.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        } else {//another attempt
                                            try {
                                                InputStream is = ((AdsImageDef) dep).getImageData();
                                                if (is != null) {
                                                    File targetFile = new File(sourceDir, image.getImageFileName());
                                                    FileOutputStream out = new FileOutputStream(targetFile);
                                                    FileUtils.copyStream(is, out);
                                                    imageFiles.add(targetFile);
                                                }
                                            } catch (IOException ex) {
                                                Logger.getLogger(Make.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (srcDirs.isEmpty()) {
                    return null;
                }

                File destJar = new File(JavaFileSupport.getBinDir(getModule()), def.getId().toString() + CompilerConstants.SUFFIX_STRING_JAR);
                try {
                    final int acceptedCount[] = new int[]{0};
                    JarFiles.mkJar(srcDirs.toArray(new File[srcDirs.size()]), destJar, new FileFilter() {
                        @Override
                        public boolean accept(File file) {
                            if (file.isDirectory() ? (!file.getName().equals(".svn")) : totalBinaries.contains(file) || imageFiles.contains(file)) {
                                acceptedCount[0]++;
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }, false);
                    if (acceptedCount[0] == 0) {
                        FileUtils.deleteFile(destJar);
                        return null;
                    }
                    return destJar;

                } catch (IOException ex) {
                    problemHandler.accept(RadixProblem.Factory.newError((RadixObject) getModule(), "Can not create jar file"));
                }
                return null;
            } else {
                return null;
            }
        }
    }

    public interface DistributionProcessor {

        public void processSingleEnvironment(ERuntimeEnvironmentType env, List<Make.Distribution> infos);

        public void summary();
    }

    public static class SimpleJarDistribution extends Distribution {

        public SimpleJarDistribution(ERuntimeEnvironmentType env, Module module) {
            super(module, env);
        }

        @Override
        public AdsModule getModule() {
            return (AdsModule) super.getModule();
        }
    }

    public static class JarDistribution extends Distribution {

        //private AdsModule module;
        private ERuntimeEnvironmentType ret;
        private final SharedData sharedData;
        private Make make;
        private static FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory() ? (!file.getName().equals(".svn")) : !(file.getName().endsWith(SuffixConstants.SUFFIX_STRING_java) || file.getName().endsWith(".java.smap"));
            }
        };

        JarDistribution(Make make, AdsModule module, SharedData sharedData, IFlowLogger logger, IProblemHandler ph, ERuntimeEnvironmentType sc, final Map<Id, Id> idReplaceMap) {
            super(module, sc);
            this.ret = sc;
            this.sharedData = sharedData;
            this.make = make;
            writeMetaInfServices(module, sc);
            generateMutabilityReport(module, sc);
        }
        private static final String[] META_INF_DIR = {"META-INF", "services"};

        private File getMetaInfServicesDir(AdsModule module, ERuntimeEnvironmentType env) {

            File file = JavaFileSupport.getBaseDirOrJarFile(module, env, EKind.SOURCE);

            for (final String part : META_INF_DIR) {
                file = new File(file, part);
            }

            return file;
        }

        private File getMetaInfDir(AdsModule module, ERuntimeEnvironmentType env) {

            File file = JavaFileSupport.getBaseDirOrJarFile(module, env, EKind.SOURCE);

            return new File(file, META_INF_DIR[0]);
        }

        private void writeMetaInfServices(AdsModule module, ERuntimeEnvironmentType env) {

            final List<Service> services = module.getServicesCatalog().getServices(env);

            if (services.isEmpty()) {
                return;
            }

            final File rootDir = getMetaInfServicesDir(module, env);

            if (!rootDir.exists()) {
                rootDir.mkdirs();
            }

            for (final Service service : services) {
                final IValueSet<String, String> content = service.getContent(env);
                final File serviceFile = new File(rootDir, content.get(Service.FILE_NAME_KEY));

                try {
                    serviceFile.createNewFile();
                    CompilerUtils.writeString(serviceFile, content.get(Service.SERVICE_IMPLEMENTATION_KEY), "UTF-8");

                } catch (IOException ex) {
                    Logger.getLogger(Make.class.getName()).log(Level.SEVERE, "Failed to write META-INF services", ex);
                }
            }
        }

        public File mkJar(IProblemHandler problemHandler) {
            //JavaFileSupport support = new JavaFileSupport(module, sc);
            File sourceDir = JavaFileSupport.getBaseDirOrJarFile(getModule(), ret, EKind.SOURCE);//support.getPackagesRoot(EKind.SOURCE);
            File destJar = JavaFileSupport.getBaseDirOrJarFile(getModule(), ret, EKind.BINARY);

            try {
                if (sourceDir != null && destJar != null) {

                    JarFiles.mkJar(sourceDir, destJar, filter, false);
                    return destJar;
                }
            } catch (IOException ex) {
                problemHandler.accept(RadixProblem.Factory.newError(getModule(), "Can not create jar file"));
            }
            return null;
        }

        private void generateMutabilityReport(AdsModule module, ERuntimeEnvironmentType env) {
            if (env != ERuntimeEnvironmentType.COMMON && env != ERuntimeEnvironmentType.SERVER) {
                return;
            }

            StringBuilder content = new StringBuilder();
            Set<String> set = make.workingSet.remove(module);
            if (set != null) {
                for (String binding : set) {
                    Boolean result = make.mutabilityCheck.get(binding);
                    if (result != null && result) {
                        content.append(binding).append("\n");
                    }
                }
            }
//            //print mutability stat
//            System.out.println("------------------------ begin mutability report --------------------------");
//            System.out.println("Meta classes (all immutable): " + mcStat.metaDefinitionsCount);
//            System.out.println("Enum meta classes (all immutable): " + mcStat.enumMetaCount);
//            System.out.println("Clc meta classes (all immutable): " + mcStat.clcMetaCount);
//            System.out.println("Xml classes: (all immutable)" + mcStat.xmlDefinitionsCount);
//            System.out.println("Other immutable: " + mcStat.otherImmutable);
//            System.out.println("All mutable: " + mcStat.otherMutable);
//            System.out.println("Total: " + mcStat.total);
//
//            System.out.println("------------------------ end mutability report --------------------------");
            if (content.length() > 0) {
                final File rootDir = getMetaInfDir(module, env);

                if (!rootDir.exists()) {
                    rootDir.mkdirs();
                }

                File indexFile = new File(rootDir, "immutable.index");
                try {
                    FileUtils.writeToFile(indexFile, content.toString(), "UTF-8");
//            Collections.sort(mutable, new Comparator<CompilerUtils.MutabilityCheckResult>() {
//
//                @Override
//                public int compare(CompilerUtils.MutabilityCheckResult o1, CompilerUtils.MutabilityCheckResult o2) {
//                    return o1.getClassName().compareTo(o2.getClassName());
//                }
//            });
//            Collections.sort(immutable, new Comparator<CompilerUtils.MutabilityCheckResult>() {
//
//                @Override
//                public int compare(CompilerUtils.MutabilityCheckResult o1, CompilerUtils.MutabilityCheckResult o2) {
//                    return o1.getClassName().compareTo(o2.getClassName());
//                }
//            });
//            System.out.println("Immutable classes. Total " + immutable.size());
//            for (CompilerUtils.MutabilityCheckResult result : immutable) {
//                System.out.println(result.toString());
//            }
                } catch (IOException ex) {
                    //ignore
                }
            }
//            System.out.println("Mmutable classes. Total " + mutable.size());
//            for (CompilerUtils.MutabilityCheckResult result : mutable) {
//                System.out.println(result.toString());
//            }
        }

        //returns jar file pathname for specified definition  if definition in distribution list of current module or null
        //Used by user function compiler
        public File mkJar(IProblemHandler problemHandler, AdsDefinition def) {
            if (defs != null && defs.containsKey(def)) {
                final List<File> binaries;
                DefinitionInfo info = defs.get(def);
                if (info.binaries != null) {
                    binaries = new LinkedList<>();
                    File baseDir = new File(String.valueOf(info.moduleDirPath));
                    for (int i = 0; i < info.binaries.length; i++) {
                        binaries.add(new File(baseDir, String.valueOf(info.binaries[i])));
                    }
                } else {
                    binaries = null;
                }

                File sourceDir = JavaFileSupport.getBaseDirOrJarFile(getModule(), ret, EKind.SOURCE);//support.getPackagesRoot(EKind.SOURCE);
                File destJar = JavaFileSupport.getBaseDirOrJarFile(getModule(), ret, EKind.BINARY);
                try {
                    if (binaries != null && sourceDir != null && destJar != null) {

                        JarFiles.mkJar(sourceDir, destJar, new FileFilter() {
                            @Override
                            public boolean accept(File pathname) {
                                return pathname.isDirectory() || binaries.contains(pathname);
                            }
                        }, false);
                        return destJar;
                    }
                } catch (IOException ex) {
                    problemHandler.accept(RadixProblem.Factory.newError((RadixObject) getModule(), "Can not create jar file"));
                }
                return null;
            } else {
                return null;
            }
        }

        @Override
        public AdsModule getModule() {
            return (AdsModule) super.getModule();
        }
    }

    public List<Distribution> computeDistribution(ERuntimeEnvironmentType env, IProblemHandler problemHandler, final Map<Id, Id> idReplaceMap) {

        final HashMap<Module, Distribution> used = new HashMap<>();
        if (addDists != null) {
            for (DefinitionInfo def : addDists) {
                Module module = def.definition instanceof Module ? (Module) def.definition : def.definition.getModule();
                if (module instanceof AdsModule) {
                    Distribution dist = used.get(module);
                    if (dist == null) {
                        dist = new JarDistribution(this, (AdsModule) module, sharedData, flowLogger, problemHandler, env, idReplaceMap);
                        used.put(module, dist);
                    }
                    dist.addDef(def);
                }
            }
            addDists = null;
        }

        for (final Map.Entry<Definition, File[]> e : getDistributableData(env).entrySet()) {
            final Definition def = e.getKey();
            final File[] files = e.getValue();
            Module module = def instanceof Module ? (Module) def : def.getModule();
            Distribution dist = used.get(module);
            if (module instanceof AdsModule) {
                if (dist == null) {
                    dist = new JarDistribution(this, (AdsModule) module, sharedData, flowLogger, problemHandler, env, idReplaceMap);
                    used.put(module, dist);
                }
                dist.addDef(new DefinitionInfo(def, files == null ? null : Arrays.asList(files)));
            } else {
                if (dist == null) {
                    dist = new Distribution(module, env);
                    used.put(module, dist);
                }
                dist.addDef(new DefinitionInfo(def, null));
            }
        }

        ArrayList<Distribution> result = new ArrayList<>(used.size());
        for (Distribution dist : used.values()) {
            if (dist != null) {
                result.add(dist);
            }
        }
        return result;
    }

    public void reset() {

        sharedData.cleanup();
    }
}
