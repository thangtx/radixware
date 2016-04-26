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
package org.radixware.kernel.common.compiler.core.lookup.locations;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.eclipse.jdt.internal.compiler.lookup.AdsCompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.AdsMissingBinaryType;
import org.radixware.kernel.common.compiler.lookup.AdsWorkspace;
import org.eclipse.jdt.internal.compiler.util.SuffixConstants;
import org.radixware.kernel.common.compiler.CompilerConstants;
import org.radixware.kernel.common.compiler.core.lookup.AdsJavaPackage;
import org.radixware.kernel.common.compiler.core.lookup.AdsNameEnvironment;
import static org.radixware.kernel.common.compiler.core.lookup.locations.AdsModulePackageLocation.PACKAGE_NAME_PREFIX_XSD;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsPresentableClass;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.src.JavaFileSupport;
import org.radixware.kernel.common.defs.ads.src.xml.XBeansType;
import org.radixware.kernel.common.defs.ads.xml.AbstractXmlDefinition;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.enums.EDefType;
import static org.radixware.kernel.common.enums.EDefType.CLASS;
import static org.radixware.kernel.common.enums.EDefType.CONTEXTLESS_COMMAND;
import static org.radixware.kernel.common.enums.EDefType.CUSTOM_EDITOR;
import static org.radixware.kernel.common.enums.EDefType.ENUMERATION;
import static org.radixware.kernel.common.enums.EDefType.MSDL_SCHEME;
import static org.radixware.kernel.common.enums.EDefType.PARAGRAPH;
import static org.radixware.kernel.common.enums.EDefType.ROLE;
import static org.radixware.kernel.common.enums.EDefType.XML_SCHEME;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.WEB;
import org.radixware.kernel.common.repository.fs.IJarDataProvider;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.CharOperations;

public class AdsModuleDevPackageLocation extends AdsModulePackageLocation {

    private final AdsWorkspace workspace;
    private File packagesRoot;

    public AdsModuleDevPackageLocation(AdsModule module, AdsWorkspace workspace, ERuntimeEnvironmentType env) {
        super(module, env);
        this.workspace = workspace;
        buildNamesCache();
    }

    @Override
    public void createPackages(AdsJavaPackage root) {
        super.createPackages(root);
        final Set<Id> processedDefIds = new HashSet<>();
        int index = 0;
        for (AdsModule m = module; m != null; m = m.findOverwritten()) {
            for (AdsDefinition definition : m.getDefinitions()) {
                if (processedDefIds.contains(definition.getId())) {
                    continue;
                }
                processedDefIds.add(definition.getId());
                if (definition instanceof AbstractXmlDefinition && definition.getUsageEnvironment() == env) {
                    final char[][] xsd = new char[basePackageName[index].length + 1][];
                    System.arraycopy(basePackageName[index], 0, xsd, 0, basePackageName[index].length);
                    xsd[basePackageName[index].length] = definition.getId().toCharArray();
                    createPackages(root, xsd);
                    final char[][] xsdiml = new char[xsd.length + 1][];
                    System.arraycopy(xsd, 0, xsdiml, 0, xsd.length);
                    xsdiml[xsd.length] = "impl".toCharArray();
                    createPackages(root, xsdiml);
                }
            }
            index++;
        }
    }

    @Override
    public boolean containsPackageName(char[][] packageName) {
        if (packageName.length > basePackageName.length) {
            if (packageName.length == basePackageName.length + 1 || packageName.length == basePackageName.length + 2) {//possible xml definition package
                char[] candidate = packageName[basePackageName.length];
                if (packageName.length == basePackageName.length + 2) {
                    if (CharOperation.equals(candidate, "impl".toCharArray())) {
                        candidate = packageName[basePackageName.length - 1];
                    } else {
                        return false;
                    }
                }
                if (CharOperations.startsWith(candidate, PACKAGE_NAME_PREFIX_XSD, 0) || CharOperations.startsWith(candidate, PACKAGE_NAME_PREFIX_MSDL, 0)) {
                    final AdsDefinition def = module.getDefinitions().findById(Id.Factory.loadFrom(String.valueOf(candidate)));
                    if (def == null) {
                        return false;
                    }
                    if (def.getDefinitionType() == EDefType.XML_SCHEME || def.getDefinitionType() == EDefType.MSDL_SCHEME) {
                        return true;
                    }
                }
            }
            return false;
        }
        return prefixEquals(packageName);
    }
    //  private final HashtableOfObject notFoundNames = new HashtableOfObject();
    private Map<Id, Definition> namesCache = null;
    private Map<Id, Definition> xsdNamesCache = null;

    private void buildNamesCache() {
        if (namesCache == null) {
            namesCache = new HashMap<>();
            xsdNamesCache = new HashMap<>();
            switch (env) {
                case COMMON:
                    processModule(new ModuleAcceptor() {
                        @Override
                        public void accept(AdsModule module) {
                            for (AdsDefinition def : module.getDefinitions()) {
                                switch (def.getDefinitionType()) {
                                    case CLASS:
                                    case ENUMERATION:
                                    case XML_SCHEME:
                                    case MSDL_SCHEME:
                                    case LOCALIZING_BUNDLE:
                                        break;
                                    default:
                                        continue;
                                }
                                if (def.getUsageEnvironment() == ERuntimeEnvironmentType.COMMON) {
                                    if (def instanceof IXmlDefinition) {
                                        xsdNamesCache.put(def.getId(), def);
                                    } else {
                                        namesCache.put(def.getId(), def);
                                    }
                                }
                            }
                        }
                    });
                    break;
                case SERVER:
                    processModule(new ModuleAcceptor() {
                        @Override
                        public void accept(AdsModule module) {
                            for (AdsDefinition def : module.getDefinitions()) {
                                switch (def.getDefinitionType()) {
                                    case CLASS:
                                    case XML_SCHEME:
                                    case MSDL_SCHEME:
                                        if (def.getUsageEnvironment() == ERuntimeEnvironmentType.SERVER) {
                                            if (def instanceof IXmlDefinition) {
                                                xsdNamesCache.put(def.getId(), def);
                                            } else {
                                                namesCache.put(def.getId(), def);
                                            }
                                        }
                                        break;
                                    case PARAGRAPH:
                                    case CONTEXTLESS_COMMAND:
                                    case ROLE:
                                        break;
                                    default:
                                        continue;
                                }
                                namesCache.put(def.getId(), def);
                            }
                        }
                    });
                    break;
                case EXPLORER:
                case WEB:
                    processModule(new ModuleAcceptor() {
                        @Override
                        public void accept(AdsModule module) {
                            for (AdsDefinition def : module.getDefinitions()) {
                                checkDefinitionForClientEnv(def);
                                RadixObject subjectOfVisitor = null;
                                if (def instanceof IAdsPresentableClass) {
                                    subjectOfVisitor = ((IAdsPresentableClass) def).getPresentations();
                                } else {
                                    switch (def.getDefinitionType()) {
                                        case CUSTOM_DIALOG:
                                        case CUSTOM_PROP_EDITOR:
                                        case CUSTOM_WIDGET_DEF:
                                        case PARAGRAPH:
                                            subjectOfVisitor = def;
                                    }
                                }
                                if (subjectOfVisitor != null) {
                                    subjectOfVisitor.visit(new IVisitor() {
                                        @Override
                                        public void accept(RadixObject radixObject) {
                                            if (radixObject instanceof AdsDefinition) {
                                                checkDefinitionForClientEnv((AdsDefinition) radixObject);
                                            }
                                        }
                                    }, VisitorProviderFactory.createDefaultVisitorProvider());
                                }
                            }
                        }
                    });
                    break;
                case COMMON_CLIENT:
                    processModule(new ModuleAcceptor() {
                        @Override
                        public void accept(AdsModule module) {
                            for (AdsDefinition def : module.getDefinitions()) {
                                switch (def.getDefinitionType()) {
                                    case CLASS:
                                        AdsClassDef clazz = (AdsClassDef) def;
                                        if (clazz.getClientEnvironment() == env && !clazz.isDual()) {
                                            namesCache.put(def.getId(), def);
                                        }
                                        break;
                                    case CONTEXTLESS_COMMAND:
                                        AdsContextlessCommandDef clc = (AdsContextlessCommandDef) def;
                                        namesCache.put(def.getId(), def);
                                        break;
                                    case XML_SCHEME:
                                    case MSDL_SCHEME:
                                        if (def.getUsageEnvironment() == env) {
                                            xsdNamesCache.put(def.getId(), def);
                                        }
                                        break;
                                    case PARAGRAPH:

                                        def.visit(new IVisitor() {
                                            @Override
                                            public void accept(RadixObject radixObject) {
                                                if (radixObject instanceof AdsDefinition) {
                                                    checkDefinitionForClientEnv((AdsDefinition) radixObject);
                                                }
                                            }
                                        }, VisitorProviderFactory.createDefaultVisitorProvider());
                                        break;
                                }
                            }
                        }
                    });
                    break;
            }
        }
    }

    private void checkDefinitionForClientEnv(AdsDefinition def) {
        switch (def.getDefinitionType()) {
            case CLASS:
                AdsClassDef clazz = (AdsClassDef) def;
                if (clazz.getClientEnvironment() == env || clazz.isDual() && clazz.getClientEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
                    namesCache.put(def.getId(), def);
                }
                break;
            case XML_SCHEME:
            case MSDL_SCHEME:
                if (def.getUsageEnvironment() == env) {
                    xsdNamesCache.put(def.getId(), def);
                }
                break;
            case PARAGRAPH:
                break;
            case CUSTOM_DIALOG:
            case CUSTOM_EDITOR:
            case CUSTOM_FORM_EDITOR:
            case CUSTOM_PROP_EDITOR:
            case CUSTOM_FILTER_DIALOG:
            case CUSTOM_PARAG_EDITOR:
            case CUSTOM_PAGE_EDITOR:
            case CUSTOM_REPORT_EDITOR:
            case CUSTOM_SELECTOR:
            case CUSTOM_WIDGET_DEF:
            case EDITOR_PRESENTATION:
            case SELECTOR_PRESENTATION:
                if (def.getUsageEnvironment() == env) {
                    break;
                } else {
                    return;
                }
            default:
                return;

        }
        namesCache.put(def.getId(), def);
    }

    private interface ModuleAcceptor {

        void accept(AdsModule module);
    }

    private void processModule(ModuleAcceptor acceptor) {
        AdsModule module = this.module;
        final List<AdsModule> reverseList = new LinkedList<>();
        while (module != null) {
            reverseList.add(0, module);
            module = module.findOverwritten();
        }
        for (AdsModule m : reverseList) {
            acceptor.accept(m);
        }
    }

    private File getPackagesRoot() {
        if (packagesRoot == null) {
            packagesRoot = JavaFileSupport.getBaseDirOrJarFile(module, env, JavaFileSupport.EKind.SOURCE);
        }
        return packagesRoot;
    }

    private Definition findDefinitionByQualifiedName(char[][] name) {
        Definition result = null;
        for (int i = 0; i < name.length; i++) {
            String str = String.valueOf(name[i]);
            if (str.startsWith("xsd")) {
                final Id idCandidate = Id.Factory.loadFrom(String.valueOf(str));
                result = namesCache.get(idCandidate);
                if (result != null) {
                    return result;
                }
            }
            if (i == name.length - 1) {//last chance
                int index = str.indexOf("$");
                if (index > 0) {
                    str = str.substring(0, index);
                }
                final Id idCandidate = Id.Factory.loadFrom(String.valueOf(str));
                return namesCache.get(idCandidate);
            }
        }
        return null;
    }

    @Override
    public boolean invokeRequest(final AdsNameEnvironment.NameRequest request) {
        final IJarDataProvider dataProvider;
        try {
            char[][] externalName = null;
            if (request instanceof AdsNameEnvironment.PackageNameRequest) {
                externalName = ((AdsNameEnvironment.PackageNameRequest) request).getPackageName();
            }
            boolean needsPackages = request instanceof AdsNameEnvironment.PackageNamePackageRequest;
            dataProvider = module.getRepository().getJarFile("bin/" + env.getValue() + CompilerConstants.SUFFIX_STRING_JAR);
            if (dataProvider == null) {
                if (module.isReadOnly()) {
                    return false;
                } else {//try to look for classfiles
                }
            } else {
                Collection<IJarDataProvider.IJarEntry> entries = dataProvider.entries();
                if (entries == null) {
                    return false;
                }
                loop:
                for (IJarDataProvider.IJarEntry e : entries) {
                    final String entryName = e.getName();
                    boolean isClassEntry = !e.isDirectory() && entryName.endsWith(".class");
                    if (!isClassEntry && !needsPackages) {
                        continue;
                    }
                    String name = entryName.substring(0, entryName.length() - 6);
                    String[] namesAsStrs = name.split("/");

                    if (externalName != null && externalName.length + 1 != namesAsStrs.length) {

                        if (needsPackages) {
                            if (externalName.length < namesAsStrs.length - 1) {
                                for (int i = 0; i < externalName.length; i++) {
                                    String asStr = String.valueOf(externalName[i]);
                                    if (!asStr.equals(namesAsStrs[i])) {
                                        continue loop;
                                    }
                                }
                                char[][] packageName = new char[externalName.length + 1][];
                                System.arraycopy(externalName, 0, packageName, 0, externalName.length);
                                packageName[externalName.length] = namesAsStrs[externalName.length].toCharArray();
                                if (((AdsNameEnvironment.PackageNamePackageRequest) request).accept(packageName)) {
                                    return true;
                                }
                            }

                        }
                        continue;
                    }
                    final char[][] names = new char[namesAsStrs.length][];
                    for (int i = 0; i < names.length; i++) {
                        names[i] = namesAsStrs[i].toCharArray();
                    }

                    if (externalName != null) {
                        for (int i = 0; i < externalName.length; i++) {
                            if (!CharOperation.equals(externalName[i], names[i])) {
                                continue loop;
                            }
                        }
                    }
                    char[][] packageName = new char[names.length - 1][];
                    System.arraycopy(names, 0, packageName, 0, packageName.length);

                    if (isClassEntry) {
                        if (request.accept(packageName, names[packageName.length], new AdsNameEnvironment.ClassDataProvider() {
                            @Override
                            public NameEnvironmentAnswer getAnswer() {
                                try {
                                    return new NameEnvironmentAnswer(AdsClassFileReader.read(findDefinitionByQualifiedName(names), dataProvider.getEntryDataStream(entryName), entryName, true), null);
                                } catch (IOException | ClassFormatException ex) {
                                    Logger.getLogger(AdsModuleDevPackageLocation.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                return null;
                            }

                            @Override
                            public byte[] getClassFileBytes() {
                                try {
                                    return dataProvider.getEntryData(entryName);
                                } catch (IOException e) {
                                    return null;
                                }
                            }
                        })) {
                            return true;
                        }
                    } else {
                        if (needsPackages) {
                            if (((AdsNameEnvironment.PackageNamePackageRequest) request).accept(packageName)) {
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(AdsModuleDevPackageLocation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public NameEnvironmentAnswer findAnswer(char[][] packageName, char[][] typeName) {
        if (packageName.length < basePackageName.length) {
            return null;
        }
        if (!prefixEquals(packageName)) {
            return null;
        }
//        if (notFoundNames.containsKey(typeName[0])) {
//            return null;
//        }
        buildNamesCache();

        boolean isReadOnlyModule = module.isReadOnly();

        Definition def = null;
        for (int b = 0; b < basePackageName.length; b++) {
            if (packageName.length == basePackageName[b].length + 1) {
                final Id idCandidate = Id.Factory.loadFrom(String.valueOf(packageName[basePackageName[b].length]));
                def = xsdNamesCache.get(idCandidate);
            } else if (packageName.length == basePackageName[b].length + 2 && CharOperation.equals(packageName[packageName.length - 1], "impl".toCharArray())) {
                final Id idCandidate = Id.Factory.loadFrom(String.valueOf(packageName[packageName.length - 2]));
                def = xsdNamesCache.get(idCandidate);
            } else if (packageName.length == basePackageName[b].length) {
                final char[] matchName;
                if (CharOperation.endsWith(typeName[0], "_mi".toCharArray())) {
                    matchName = CharOperation.subarray(typeName[0], 0, typeName[0].length - 3);
                } else {
                    matchName = typeName[0];
                }
                if (matchName.length < 29) {
                    continue;
                }
                final Id idCandidate = Id.Factory.loadFrom(String.valueOf(matchName));
                def = namesCache.get(idCandidate);
            }
            if (def != null) {
                break;
            }
        }
        if (def != null && def.getModule() != null) {
            if (def.getModule() != this.module && def.getModule().isReadOnly()) {
                isReadOnlyModule = true;
            }
        }

        if (isReadOnlyModule) {//try look in jar file
//            final NameEnvironmentAnswer jarResult = findClassFileInJar(def, packageName, typeName);
//            if (jarResult != null) {
//                return jarResult;
//            }
            return findClassFileInJar(def, packageName, typeName);
        }

        if (def != null) {

            final NameEnvironmentAnswer classFileResult = findClassFile(def, packageName, typeName);

            if (classFileResult != null) {
                return classFileResult;
            }

            boolean lookForCompilationUnit = !workspace.useBinariesLookup;
//            AdsWorkspace.SessionData sessionData = workspace.getSessionData();
//            if (sessionData != null) {
//                char[][] checkName = CharOperation.arrayConcat(packageName, typeName);
//                if (sessionData.isWellKnownAsProblemUnit(checkName)) {
//                    lookForCompilationUnit = false;
////                }
////            }

            if (lookForCompilationUnit) {
                final AdsWorkspace.DeclarationData[] declarations = workspace.findCompilationUnitDeclarations(def);
                String lookForName = String.valueOf(CharOperation.concatWith(CharOperation.arrayConcat(packageName, typeName[0]), '.'));

                if (declarations != null) {
                    for (int i = 0; i < declarations.length; i++) {
                        final AdsWorkspace.DeclarationData data = declarations[i];
                        if (data == null || data.getEnvironmentType() != env) {
                            continue;
                        }
                        String unitName = data.getDeclarationFileName();
                        if (!unitName.equals(lookForName)) {
                            continue;
                        }

                        AdsCompilationUnitDeclaration decl = data.getDeclaration();

                        if (decl != null && decl.types != null) {
                            for (int t = 0; t < decl.types.length; t++) {
                                if (CharOperations.equals(decl.types[t].name, typeName[0])) {
                                    return new NameEnvironmentAnswer(decl.compilationResult.compilationUnit, null);
                                }
                            }
                        }
                    }
                }
            }

            boolean returnAnswer = true;
            if (def instanceof IXmlDefinition) {
                boolean lookForType = false;
                if (String.valueOf(packageName[packageName.length - 1]).equals(def.getId().toString())) {
                    if (!"impl".equals(String.valueOf(typeName[0]))) {
                        lookForType = true;
                    } else {
                        returnAnswer = false;
                    }
                } else if (String.valueOf(packageName[packageName.length - 2]).equals(def.getId().toString()) && String.valueOf(packageName[packageName.length - 1]).equals("impl")) {
                    lookForType = true;
                } else {
                    returnAnswer = false;
                }
                if (lookForType) {
                    for (char[] type : typeName) {
                        XBeansType x = ((AbstractXmlDefinition) def).getSchemaTypeSystem().findTypeOrInterface(String.valueOf(type));
                        if (x == null) {
                            returnAnswer = false;
                            break;
                        }
                    }
                }
            }

            NameEnvironmentAnswer answer = findClassFileInJar(def, packageName, typeName);
            if (answer != null) {
                return answer;
            }

            if (returnAnswer) {
                return new NameEnvironmentAnswer(new AdsMissingBinaryType(def, packageName, typeName), null);
            }
        }
//        notFoundNames.putUnsafely(typeName[0], null);
        return null;
    }

    private NameEnvironmentAnswer findClassFile(Definition def, char[][] packageName, char[][] typeName) {
        final File rootDir = getPackagesRoot();
        File dir = rootDir;
        for (int i = 0; i < packageName.length; i++) {
            dir = new File(dir, String.valueOf(packageName[i]));
        }
        StringBuilder fileName = new StringBuilder();
        for (int i = 0; i < typeName.length; i++) {
            if (i > 0) {
                fileName.append('$');
            }
            fileName.append(typeName[i], 0, typeName[i].length);
        }
        fileName.append(SuffixConstants.SUFFIX_STRING_class);
        final File classFile = new File(dir, fileName.toString());

        if (classFile.exists()) {
            try {
                if (def instanceof AdsDefinition) {
                    if (def.getFile() != null && def.getFile().lastModified() > classFile.lastModified()) {
                        return null;
                    }
                }
                Definition definition = def;
                if (typeName.length > 1) {//member type;
                    definition = null;
                }

                return new NameEnvironmentAnswer(AdsClassFileReader.read(definition, classFile, true), null);
            } catch (ClassFormatException ex) {
                Logger.getLogger(AdsModuleDevPackageLocation.class.getName()).log(Level.FINE, ex.getMessage(), ex);
            } catch (IOException ex) {
                Logger.getLogger(AdsModuleDevPackageLocation.class.getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
        return null;
    }

    private NameEnvironmentAnswer findClassFileInJar(Definition def, char[][] packageName, char[][] typeName) {
        try {
            AdsModule module = (AdsModule) (def != null && def.getModule() != null ? def.getModule() : this.module);

            if (module != null) {
                StringBuilder entryName = new StringBuilder();
                for (int i = 0; i < packageName.length; i++) {
                    entryName.append(packageName[i], 0, packageName[i].length);
                    entryName.append('/');
                }
                for (int i = 0; i < typeName.length; i++) {
                    if (i > 0) {
                        entryName.append('$');
                    }
                    entryName.append(typeName[i], 0, typeName[i].length);
                }
                entryName.append(SuffixConstants.SUFFIX_STRING_class);
                String path = entryName.toString();
                while (module != null) {
                    IJarDataProvider dataProvider = module.getRepository().getJarFile("bin/" + env.getValue() + CompilerConstants.SUFFIX_STRING_JAR);
                    if (dataProvider != null && dataProvider.entryExists(path)) {
                        try {
                            return new NameEnvironmentAnswer(AdsClassFileReader.read(def, dataProvider.getEntryDataStream(path), path, true), null);
                        } catch (ClassFormatException ex) {
                            Logger.getLogger(AdsModuleDevPackageLocation.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                        } catch (IOException ex) {
                            Logger.getLogger(AdsModuleDevPackageLocation.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                        }
                    }

                    module = module.findOverwritten();
                }
            }

        } catch (IOException ex) {
            return null;
        }

        final File rootDir = getPackagesRoot();
        File dir = rootDir;
        for (int i = 0;
                i < packageName.length;
                i++) {
            dir = new File(dir, String.valueOf(packageName[i]));
        }
        StringBuilder fileName = new StringBuilder();
        for (int i = 0;
                i < typeName.length;
                i++) {
            if (i > 0) {
                fileName.append('$');
            }
            fileName.append(typeName[i], 0, typeName[i].length);
        }

        fileName.append(SuffixConstants.SUFFIX_STRING_class);
        final File classFile = new File(dir, fileName.toString());

        if (classFile.exists()) {
            try {
                return new NameEnvironmentAnswer(AdsClassFileReader.read(def, classFile, true), null);
            } catch (ClassFormatException ex) {
                Logger.getLogger(AdsModuleDevPackageLocation.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            } catch (IOException ex) {
                Logger.getLogger(AdsModuleDevPackageLocation.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }

        return null;
    }

    @Override
    public NameEnvironmentAnswer findAnswer(Definition definition, String suffix) {
        if (definition.getModule() == module) {
            final AdsWorkspace.DeclarationData[] declarations = workspace.findCompilationUnitDeclarations(definition);
            if (declarations != null) {
                final char[] suffixChars = suffix.toCharArray();
                for (int i = 0; i < declarations.length; i++) {
                    final AdsCompilationUnitDeclaration decl = declarations[i].getDeclaration();
                    if (decl != null && decl.types != null) {
                        for (int t = 0; t < decl.types.length; t++) {
                            if (CharOperations.equals(decl.types[t].name, suffixChars)) {
                                return new NameEnvironmentAnswer(decl.compilationResult.compilationUnit, null);
                            }
                        }
                    }
                }
            }
            return null;
        } else {
            return null;
        }
    }
}
