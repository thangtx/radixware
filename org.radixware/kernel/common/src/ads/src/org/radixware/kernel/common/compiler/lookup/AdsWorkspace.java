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
package org.radixware.kernel.common.compiler.lookup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;
import org.eclipse.jdt.internal.compiler.lookup.AdsCompilationUnit;
import org.eclipse.jdt.internal.compiler.lookup.AdsCompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.AdsCompilationUnitScope;
import org.eclipse.jdt.internal.compiler.lookup.AdsLookupEnvironment;
import org.eclipse.jdt.internal.compiler.lookup.AdsSourceTypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.AdsTypeRequestor;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.compiler.core.lookup.AdsNameEnvironment;
import org.radixware.kernel.common.compiler.core.problems.AdsProblemReporter;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.IJavaSourceProvider;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.xml.AbstractXmlDefinition;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Utils;

public class AdsWorkspace {

    private abstract static class DeclarationResolver {

        private final ERuntimeEnvironmentType env;

        public DeclarationResolver(ERuntimeEnvironmentType env) {
            this.env = env;
        }

        public abstract String getDeclarationFileName();

        public abstract AdsCompilationUnitDeclaration resolveDeclaration();

        public abstract void cleanup();

        public abstract String getExtPath();

        public ERuntimeEnvironmentType getEnvironmentType() {
            return env;
        }
    }

    private class DirectDefinitionDeclarationResolver extends DeclarationResolver {

        private final Definition definition;
        private final boolean isMeta;

        public DirectDefinitionDeclarationResolver(Definition definition, boolean isMeta) {
            super(AdsWorkspace.this.env);
            this.definition = definition;
            this.isMeta = isMeta;
        }

        @Override
        public AdsCompilationUnitDeclaration resolveDeclaration() {
            AdsCompilationUnit unit = new AdsCompilationUnit(definition, JavaSourceSupport.UsagePurpose.getPurpose(env, isMeta ? JavaSourceSupport.CodeType.META : JavaSourceSupport.CodeType.EXCUTABLE));
            return unit.declaration = new AdsCompilationUnitDeclaration(definition, AdsWorkspace.this, JavaSourceSupport.UsagePurpose.getPurpose(env, isMeta ? JavaSourceSupport.CodeType.META : JavaSourceSupport.CodeType.EXCUTABLE), reporter, new AdsCompilationResult(unit), getCompilerOptions(), isMeta, null);
        }

        @Override
        public void cleanup() {
        }

        @Override
        public String getExtPath() {
            return null;
        }

        @Override
        public String getDeclarationFileName() {
            return null;
        }
    }

    private class NewStyleDefinitionDeclarationResolver extends DeclarationResolver {

        private final Definition definition;
        private final String id;

        public NewStyleDefinitionDeclarationResolver(Definition definition, String id) {
            super(AdsWorkspace.this.env);
            this.definition = definition;
            this.id = id;
        }

        @Override
        public AdsCompilationUnitDeclaration resolveDeclaration() {
            IJavaSourceProvider provider = (IJavaSourceProvider) definition;
            AdsCompilationUnit unit = new AdsCompilationUnit(definition, JavaSourceSupport.UsagePurpose.getPurpose(env, JavaSourceSupport.CodeType.EXCUTABLE), id);
            AdsCompilationUnitDeclaration decl = unit.declaration = new AdsCompilationUnitDeclaration(definition, AdsWorkspace.this, JavaSourceSupport.UsagePurpose.getPurpose(env, JavaSourceSupport.CodeType.EXCUTABLE), reporter, new AdsCompilationResult(unit), getCompilerOptions(), false, id, null, null);
            SourceParser parser = new SourceParser(reporter, true, decl.radixObjectLookup);
            parser.parse(decl, provider.getJavaSource(id));
            for (int i = 0; i < decl.types.length; i++) {
                decl.types[i] = new AdsSourceTypeDeclaration((AdsCompilationUnitScope) decl.scope, definition, env, decl.types[i], decl.radixObjectLookup);
            }
            return decl;
        }

        @Override
        public void cleanup() {
        }

        @Override
        public String getExtPath() {
            return id;
        }

        @Override
        public String getDeclarationFileName() {
            return null;
        }
    }

    private class OldStyleDefinitionDeclarationResolver extends DeclarationResolver {

        private final JavaSourceSupport.UsagePurpose up;
        private final Definition definition;

        public OldStyleDefinitionDeclarationResolver(Definition definition, JavaSourceSupport.UsagePurpose up) {
            super(up.getEnvironment());
            this.up = up;
            this.definition = definition;
        }

        @Override
        public AdsCompilationUnitDeclaration resolveDeclaration() {
            AdsCompilationUnit unit = new AdsCompilationUnit(definition, up);
            ERuntimeEnvironmentType e = up.getEnvironment();
            final CodePrinter printer = CodePrinter.Factory.newJavaPrinter();
            AdsCompilationUnitDeclaration decl = unit.declaration = new AdsCompilationUnitDeclaration(definition, AdsWorkspace.this, up, reporter, new AdsCompilationResult(unit), getCompilerOptions(), up.isMeta(), null, getDeclarationFileName(), printer);
            printer.putProperty(RadixObjectLocator.PRINTER_PROPERTY_NAME, decl.radixObjectLookup);
            ((IJavaSource) definition).getJavaSourceSupport().getCodeWriter(up).writeCode(printer);
            decl.radixObjectLookup.commit();
            final char[] contents = printer.getContents();
            printer.clear();
            SourceParser parser = new SourceParser(reporter, true, decl.radixObjectLookup);
            parser.parse(decl, contents);
            if (decl.types != null) {
                for (int t = 0; t < decl.types.length; t++) {
                    decl.types[t] = new AdsSourceTypeDeclaration((AdsCompilationUnitScope) decl.scope, definition, e, decl.types[t], decl.radixObjectLookup);
                }
            }
            parser.scanner.source = null;
            return decl;
        }

        @Override
        public String getDeclarationFileName() {
            final IJavaSource provider = (IJavaSource) definition;
            char[][] mainClassName = provider.getJavaSourceSupport().getMainClassName(up);
            StringBuilder fileName = new StringBuilder();
            boolean first = true;
            for (char[] name : mainClassName) {
                if (first) {
                    first = false;
                } else {
                    fileName.append('.');
                }
                fileName.append(name, 0, name.length);
            }
            return fileName.toString();
        }

        @Override
        public void cleanup() {
        }

        @Override
        public String getExtPath() {
            return null;
        }
    }

    private class XmlDeclarationResolver extends DeclarationResolver {

        private final File file;
        private final String originalName;
        private final AbstractXmlDefinition definition;
        private char[] source;

        public XmlDeclarationResolver(AbstractXmlDefinition definition, ERuntimeEnvironmentType xmlEnv, String fileName, File file, char[] source) {
            super(xmlEnv);
            this.originalName = fileName;
            this.file = file;
            this.definition = definition;
            this.source = source;
        }

        @Override
        public AdsCompilationUnitDeclaration resolveDeclaration() {
            final String name = originalName;
            try {
                char[] src;
                if (source != null) {
                    src = source;
                } else {
                    try {
                        src = FileUtils.readTextFile(file, FileUtils.XML_ENCODING).toCharArray();
                    } catch (IOException ex) {
                        Logger.getLogger(AdsWorkspace.class.getName()).log(Level.SEVERE, null, ex);
                        return null;
                    }
                }
                AdsCompilationUnit unit = new AdsCompilationUnit(definition, JavaSourceSupport.UsagePurpose.getPurpose(getEnvironmentType(), JavaSourceSupport.CodeType.EXCUTABLE), name);
                AdsCompilationUnitDeclaration result = unit.declaration = new AdsCompilationUnitDeclaration(definition, AdsWorkspace.this, JavaSourceSupport.UsagePurpose.getPurpose(getEnvironmentType(), JavaSourceSupport.CodeType.EXCUTABLE), reporter, new AdsCompilationResult(unit), getCompilerOptions(), false, name, name, null);
                SourceParser parser = new SourceParser(reporter, true, unit.declaration.radixObjectLookup);
                parser.parse(unit.declaration, src);
                src = null;
                source = null;
                parser.scanner.source = null;
                if (unit.declaration.types != null) {
                    for (int t = 0; t < unit.declaration.types.length; t++) {
                        unit.declaration.types[t] = new AdsSourceTypeDeclaration((AdsCompilationUnitScope) unit.declaration.scope, definition, getEnvironmentType(), unit.declaration.types[t], unit.declaration.radixObjectLookup);
                    }
                }
                return result;
            } finally {
                if (file != null) {
                    FileUtils.deleteFile(file);
                }
            }
        }

        @Override
        public void cleanup() {
            if (file != null) {
                FileUtils.deleteFile(file);
            }
            source = null;
        }

        @Override
        public String getExtPath() {
            return originalName;
        }

        @Override
        public String getDeclarationFileName() {
            return originalName;
        }
    }

    public static class DeclarationData {

        private AdsCompilationUnitDeclaration declaration;
        private DeclarationResolver resolver;
        private boolean rejected = false;

        private DeclarationData(DeclarationResolver resolver) {
            this.resolver = resolver;
        }

        public String getExtPath() {
            synchronized (this) {
                if (declaration != null) {
                    return declaration.extPath;
                } else {
                    return resolver.getExtPath();
                }
            }
        }

        public String getDeclarationFileName() {
            synchronized (this) {
                if (declaration != null) {
                    return declaration.getUnitFileName();
                } else {
                    return resolver.getDeclarationFileName();
                }
            }
        }

        public AdsCompilationUnitDeclaration getDeclaration() {
            synchronized (this) {
                if (rejected) {
                    return null;
                }
                if (declaration == null) {
                    declaration = resolver.resolveDeclaration();
                    this.resolver.cleanup();
                    this.resolver = null;
                }

                return declaration;
            }
        }

        public boolean declarationProcessed() {
            if (declaration != null) {
                return declaration.processed;
            } else {
                return false;
            }
        }

        public void reject() {
            if (resolver != null) {
                rejected = true;
                resolver.cleanup();
            }
        }

        public ERuntimeEnvironmentType getEnvironmentType() {
            synchronized (this) {
                if (declaration != null) {
                    return declaration.getEnvironmentType();
                } else {
                    return resolver.getEnvironmentType();
                }
            }
        }
    }
    private ERuntimeEnvironmentType env;
    private Map<Definition, DeclarationData[]> def2unitDecl = new HashMap<>();
    private AdsProblemReporter reporter;
    private final AdsLookupEnvironment lookupEnvironment;
    private final Layer context;
    public boolean useBinariesLookup = false;

    public AdsWorkspace(AdsNameEnvironment env, AdsProblemReporter reporter) {
        this(env, reporter, false, false);
    }

    private File xmlSrcTmlDir = null;

    private File getXmlSrcTmpDir() throws IOException {
        synchronized (this) {
            if (xmlSrcTmlDir == null) {
                xmlSrcTmlDir = File.createTempFile("xml-src", "xml-src");
                xmlSrcTmlDir.delete();
                xmlSrcTmlDir.mkdirs();
            }
            return xmlSrcTmlDir;
        }
    }

    public AdsWorkspace(AdsNameEnvironment env, AdsProblemReporter reporter, boolean cacheBinaryTypes, boolean useCachedBinaries) {
        this.env = env.getEnvironment();
        this.reporter = reporter;
        this.context = env.getLayer();
        this.reporter.workspace = this;
        this.lookupEnvironment = new AdsLookupEnvironment(env, getCompilerOptions(), new AdsTypeRequestor(cacheBinaryTypes, useCachedBinaries), reporter);
        ((AdsNameEnvironment) this.lookupEnvironment.nameEnvironment).ensureClassPath(this);
    }

    public AdsWorkspace(Layer layer, ERuntimeEnvironmentType env, AdsProblemReporter reporter) {
        this(layer, env, reporter, false, false);
    }

    public AdsWorkspace(Layer layer, ERuntimeEnvironmentType env, AdsProblemReporter reporter, boolean cacheBinaryTypes, boolean useCachedBinaries) {
        this.env = env;
        this.reporter = reporter;
        this.context = layer;
        this.reporter.workspace = this;
        this.lookupEnvironment = new AdsLookupEnvironment(layer, env, getCompilerOptions(), reporter, cacheBinaryTypes, useCachedBinaries);
        ((AdsNameEnvironment) this.lookupEnvironment.nameEnvironment).ensureClassPath(this);
    }

    public AdsWorkspace(Layer layer, ERuntimeEnvironmentType env, AdsTypeRequestor customRequestor) {
        this.env = env;
        this.reporter = new AdsProblemReporter();
        this.reporter.workspace = this;
        this.context = layer;
        this.lookupEnvironment = new AdsLookupEnvironment(layer, env, getCompilerOptions(), customRequestor, this.reporter);
        ((AdsNameEnvironment) this.lookupEnvironment.nameEnvironment).ensureClassPath(this);
    }

    public AdsLookupEnvironment getLookupEnvironment() {
        return lookupEnvironment;
    }

    public AdsProblemReporter getReporter() {
        return reporter;
    }

    public ERuntimeEnvironmentType getEnvType() {
        return env;
    }

    public Layer getContextLayer() {
        return context;
    }

    public AdsCompilationUnitDeclaration findCompilationUnitDeclaration(Definition clazz) {
        return findCompilationUnitDeclaration(clazz, false, null);
    }

    private Definition getReal(Definition clazz) {
        if (clazz.getLayer() != context) {
            List<Module> faces = this.context.getAds().getModules().findById(ERepositorySegmentType.ADS, clazz.getModule().getId(), true, new ArrayList<Module>(0));
            if (!faces.isEmpty()) {
                Module m = faces.get(0);
                if (m != clazz.getModule()) {
                    AdsDefinition def = null;
                    while (m != null) {
                        def = ((AdsModule) m).getDefinitions().findById(clazz.getId());
                        if (def != null) {
                            clazz = def;
                            break;
                        }
                        m = m.findOverwritten();
                    }
                }
            }
        }
        return (Definition) clazz;
    }

    public AdsCompilationUnitDeclaration findCompilationUnitDeclaration(Definition definition, boolean meta) {
        return findCompilationUnitDeclaration(definition, meta, null);
    }

    public void markProcessedDeclarations() {
        for (Definition def : new ArrayList<>(def2unitDecl.keySet())) {
            DeclarationData[] decls = def2unitDecl.get(def);
            if (decls != null) {
                int nullCount = 0;
                for (int i = 0; i < decls.length; i++) {
                    if (decls[i] == null) {
                        nullCount++;
                    } else if (decls[i].declarationProcessed()) {
                        decls[i] = null;
                        nullCount++;
                    }
                }
                if (nullCount == decls.length) {
                    def2unitDecl.put(def, NO_DECLS);
                }
            }
        }
    }

    public AdsCompilationUnitDeclaration findCompilationUnitDeclaration(Definition definition, boolean meta, String sourceId) {
        DeclarationData[] decls = findCompilationUnitDeclarations(definition);

        if (decls == null) {
            return null;
        }
        if (decls == NO_DECLS) {
            return null;
        }
        int index = -1;

        if (definition instanceof IXmlDefinition) {

            String matchName = sourceId == null ? null : sourceId.split("\\.")[0];
            if (matchName != null) {
                for (int i = 0; i < decls.length; i++) {
                    DeclarationData decl = decls[i];
                    if (Utils.equals(decl.getExtPath(), matchName)) {
                        index = i;
                        break;
                    }
                }
            }
        } else if (definition instanceof IJavaSourceProvider) {
            for (int i = 0; i < decls.length; i++) {
                DeclarationData decl = decls[i];
                if (Utils.equals(decl.getExtPath(), sourceId)) {
                    index = i;
                    break;
                }
            }
        } else if (definition instanceof IJavaSource) {
            if (sourceId != null) {
                for (int i = 0; i < decls.length; i++) {
                    DeclarationData decl = decls[i];
                    if (Utils.equals(decl.getExtPath(), sourceId)) {
                        index = i;
                        break;
                    }
                }
            }
        } else {
            index = meta ? 1 : 0;
        }

        return index < 0 ? null : decls[index].getDeclaration();
    }
    private static final DeclarationData[] NO_DECLS = new DeclarationData[0];
    private static final AdsCompilationUnit[] NO_UNITS = new AdsCompilationUnit[0];

    public static boolean hasCompilableData(Definition definition, ERuntimeEnvironmentType env) {
        if (definition instanceof AbstractXmlDefinition) {
            AbstractXmlDefinition xmlDef = (AbstractXmlDefinition) definition;
            ERuntimeEnvironmentType xmlEnv = xmlDef.getUsageEnvironment();
            return env == xmlEnv;
        } else if (definition instanceof IJavaSourceProvider) {
            return true;
        } else if (definition instanceof IJavaSource) {
            IJavaSource provider = (IJavaSource) definition;
            JavaSourceSupport support = provider.getJavaSourceSupport();
            if (support != null) {
                final Set<JavaSourceSupport.CodeType> cts = support.getSeparateFileTypes(env);

                if (cts != null && !cts.isEmpty()) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    public void cleanup() {
        if (xmlSrcTmlDir != null) {
            FileUtils.deleteDirectory(xmlSrcTmlDir);
        }
    }

    public DeclarationData[] findCompilationUnitDeclarations(Definition definition) {
        definition = getReal(definition);

        DeclarationData[] decls = def2unitDecl.get(definition);
        if (decls != null) {
            int nullCount = 0;
            for (int i = 0; i < decls.length; i++) {
                if (decls[i] == null) {
                    nullCount++;
                } else if (decls[i].declarationProcessed()) {
                    decls[i] = null;
                    nullCount++;
                }
            }
            if (nullCount == decls.length) {
                def2unitDecl.put(definition, NO_DECLS);
                return NO_DECLS;
            }
            return decls;
        }
        if (definition instanceof AbstractXmlDefinition) {
            AbstractXmlDefinition xmlDef = (AbstractXmlDefinition) definition;
            ERuntimeEnvironmentType xmlEnv = xmlDef.getUsageEnvironment();
            ERuntimeEnvironmentType e = env;
            while (e != null && xmlEnv != e) {
                e = getNextEnv(e);
            }
            if (e == null) {
                def2unitDecl.put(definition, NO_DECLS);
                return NO_DECLS;
            }
            boolean generateFiles = false;
            if (generateFiles) {
                final File file;
                try {
                    file = File.createTempFile("xsd-tmp", "xsd-tmp");
                } catch (Exception ex) {
                    return NO_DECLS;
                }
                file.delete();
                file.mkdirs();

                final File[] sourceFiles = xmlDef.generateSourceFiles(file);
                if (sourceFiles == null) {
                    def2unitDecl.put(definition, NO_DECLS);
                    return NO_DECLS;
                }
                decls = new DeclarationData[sourceFiles.length];

                try {
                    for (int i = 0; i < decls.length; i++) {
                        try {
                            File storageFile = new File(getXmlSrcTmpDir(), UUID.randomUUID().toString());
                            FileUtils.copyFile(sourceFiles[i], storageFile);
                            decls[i] = new DeclarationData(new XmlDeclarationResolver(xmlDef, xmlEnv, sourceFiles[i].getName(), storageFile, null));
                        } catch (IOException ex) {
                            Logger.getLogger(AdsWorkspace.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                        }
                    }
                } finally {
                    FileUtils.deleteDirectory(file);
                }
            } else {
                final Map<String, char[]> sources = xmlDef.generateSources(this.reporter.getRadixProblemHandler());
                if (sources == null) {
                    def2unitDecl.put(definition, NO_DECLS);
                    return NO_DECLS;
                }
                decls = new DeclarationData[sources.size()];
                List<String> names = new ArrayList<>(sources.keySet());
                Collections.sort(names);

                for (int i = 0; i < decls.length; i++) {
                    String name = names.get(i);
                    char[] src = sources.remove(name);
                    decls[i] = new DeclarationData(new XmlDeclarationResolver(xmlDef, xmlEnv, name, null, src));
                    src = null;
                }
                sources.clear();
            }
            def2unitDecl.put(definition, decls);
        } else if (definition instanceof IJavaSourceProvider) {
            final IJavaSourceProvider provider = (IJavaSourceProvider) definition;
            String[] ids = provider.getSourceIds();
            decls = new DeclarationData[ids.length];

            for (int i = 0; i < ids.length; i++) {
                decls[i] = new DeclarationData(new NewStyleDefinitionDeclarationResolver(definition, ids[i]));
            }
            def2unitDecl.put(definition, decls);
        } else if (definition instanceof IJavaSource) {
            IJavaSource provider = (IJavaSource) definition;
            JavaSourceSupport support = provider.getJavaSourceSupport();
            if (support != null) {
                final Set<JavaSourceSupport.UsagePurpose> purposes = new HashSet<>();
                ERuntimeEnvironmentType e = env;
                while (e != null) {
                    final Set<JavaSourceSupport.CodeType> cts = support.getSeparateFileTypes(e);

                    if (cts != null && !cts.isEmpty()) {
                        for (JavaSourceSupport.CodeType ct : cts) {
                            purposes.add(JavaSourceSupport.UsagePurpose.getPurpose(e, ct));
                        }
                    }
                    e = getNextEnv(e);
                }
                if (purposes.isEmpty()) {
                    def2unitDecl.put(definition, NO_DECLS);
                    return NO_DECLS;
                }
                List<JavaSourceSupport.UsagePurpose> purposeList = new ArrayList<>(purposes);
                decls = new DeclarationData[purposeList.size()];

                int count = 0;
                for (int i = 0; i < purposeList.size(); i++) {
                    final CodePrinter printer = CodePrinter.Factory.newJavaPrinter();
                    final JavaSourceSupport.UsagePurpose up = purposeList.get(i);
                    if (!Utils.equals(support.getSourceFileExtension(up), "java")) {
                        continue;
                    }
                    count++;
                    decls[i] = new DeclarationData(new OldStyleDefinitionDeclarationResolver(definition, up));

                }
                if (count != decls.length) {
                    DeclarationData[] dummy = new DeclarationData[count];
                    count = 0;
                    for (int i = 0; i < decls.length; i++) {
                        if (decls[i] != null) {
                            dummy[count++] = decls[i];
                        }
                    }
                    decls = dummy;
                }
                def2unitDecl.put(definition, decls);
            }
        } else {
            decls = new DeclarationData[2];
            decls[0] = new DeclarationData(new DirectDefinitionDeclarationResolver(definition, false));
            decls[1] = new DeclarationData(new DirectDefinitionDeclarationResolver(definition, true));
            def2unitDecl.put(definition, decls);
        }
        return decls;
    }

    private ERuntimeEnvironmentType getNextEnv(ERuntimeEnvironmentType env) {
        if (env == null) {
            return null;
        }
        switch (env) {
            case SERVER:
            case COMMON_CLIENT:
                return ERuntimeEnvironmentType.COMMON;
            case EXPLORER:
            case WEB:
                return ERuntimeEnvironmentType.COMMON_CLIENT;
            default:
                return null;
        }
    }

    public static CompilerOptions getCompilerOptions() {
        final Map<String, String> options = new HashMap<>();
        options.put(CompilerOptions.OPTION_Source, "1.7");
        options.put(CompilerOptions.OPTION_Compliance, "1.7");
        options.put(CompilerOptions.OPTION_ReportUnusedWarningToken, "ignore");

        CompilerOptions opts = new CompilerOptions(options);
        opts.targetJDK = ClassFileConstants.JDK1_7;
        opts.inlineJsrBytecode = true;
        opts.produceDebugAttributes |= ClassFileConstants.ATTR_VARS;
        //opts.produceReferenceInfo = true;
        return opts;
    }

    public static class AdsCompilationResult extends CompilationResult {

        public AdsCompilationResult(AdsCompilationUnit compilationUnit) {
            super(compilationUnit, 0, 0, -1);
        }

        @Override
        public void record(CategorizedProblem newProblem, ReferenceContext referenceContext) {
            if (problemCount != 0 && problems == null) {
                problems = new CategorizedProblem[5];
            }
            super.record(newProblem, referenceContext); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void record(CategorizedProblem newProblem, ReferenceContext referenceContext, boolean mandatoryError) {
            if (problemCount != 0 && problems == null) {
                problems = new CategorizedProblem[5];
            }
            super.record(newProblem, referenceContext, mandatoryError); //To change body of generated methods, choose Tools | Templates.
//            if (newProblem.isError()) {
//                System.err.print("error:   ");
//            } else if (newProblem.isWarning()) {
//                System.err.print("warning: ");
//            } else {
//                System.err.print("info:    ");
//            }
//            System.err.println(newProblem.getMessage());
        }
    }
}
