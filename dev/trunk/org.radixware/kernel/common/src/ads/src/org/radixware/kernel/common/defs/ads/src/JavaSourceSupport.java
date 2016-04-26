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

package org.radixware.kernel.common.defs.ads.src;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.compiler.CompilerUtils;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.IFileBasedDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.IModelPublishableProperty.Provider;
import org.radixware.kernel.common.defs.ads.clazz.members.IModelPublishableProperty.Support;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.defs.ads.src.JavaFileSupport.EKind;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.LineMatcher;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.CharOperations;


public abstract class JavaSourceSupport {

    public static final char[] ANNOTATION_OVERRIDE = "@Override".toCharArray();
    public static final char[] ANNOTATION_DEPRECATED = "@Deprecated".toCharArray();
    public static final char[] DEFAULT_PACKAGE_NAME = new char[0];
    public static final char[][] DEFAULT_PACKAGE = new char[0][0];
    public static final char[] SINGLE_LINE_COMMENT_START = "//".toCharArray();
    public static final char[] MULTY_LINE_COMMENT_START = "/*".toCharArray();
    public static final char[] MULTY_LINE_COMMENT_END = "*/".toCharArray();
    /**
     * Java promitive types
     */
    public static final String PRIMITIVE_TYPE_NAME_BYTE = "byte";
    public static final String PRIMITIVE_TYPE_NAME_SHORT = "short";
    public static final String PRIMITIVE_TYPE_NAME_INT = "int";
    public static final String PRIMITIVE_TYPE_NAME_LONG = "long";
    public static final String PRIMITIVE_TYPE_NAME_FLOAT = "float";
    public static final String PRIMITIVE_TYPE_NAME_DOUBLE = "double";
    public static final String PRIMITIVE_TYPE_NAME_BOOLEAN = "boolean";
    public static final String PRIMITIVE_TYPE_NAME_CHAR = "char";
    /**
     * Radix system packages
     */
    public static final char[] RADIX_EXPLORER_TYPES_PACKAGE_NAME = "org.radixware.kernel.common.client.types".toCharArray();
    public static final char[] RADIX_SERVER_TYPES_PACKAGE_NAME = "org.radixware.kernel.server.types".toCharArray();
    public static final char[][] RADIX_SERVER_TYPES_PACKAGE_NAME_ARR = new char[][]{"org".toCharArray(),
        "radixware".toCharArray(),
        "kernel".toCharArray(),
        "server".toCharArray(),
        "types".toCharArray()};
    public static final char[][] RADIX_EXPLORER_TYPES_PACKAGE_NAME_ARR = new char[][]{"org".toCharArray(),
        "radixware".toCharArray(),
        "kernel".toCharArray(),
        "common".toCharArray(),
        "client".toCharArray(),
        "types".toCharArray()};
    public static final char[] RADIX_COMMON_TYPES_PACKAGE_NAME = "org.radixware.kernel.common.types".toCharArray();
    /**
     * Builtin radix server types
     */
    public static final char[] RADIX_TYPE_ARR_BLOB = "ArrBlob".toCharArray();
    public static final char[] RADIX_TYPE_ARR_CLOB = "ArrClob".toCharArray();
    public static final char[] RADIX_TYPE_ARR_ENTITY = "ArrEntity".toCharArray();
    public static final char[] RADIX_TYPE_ARR_CURSOR = "Cursor".toCharArray();
    public static final char[] RADIX_TYPE_ENTITY = "Entity".toCharArray();
    /**
     * Builtin java types
     */
    public static final char[] JAVA_TYPE_STRING = String.class.getName().toCharArray();
    public static final char[] JAVA_TYPE_LONG = Long.class.getName().toCharArray();
    public static final char[] JAVA_TYPE_BOOLEAN = Boolean.class.getName().toCharArray();
    public static final char[] JAVA_TYPE_CHARACTER = Character.class.getName().toCharArray();
    public static final char[] JAVA_TYPE_BIG_DECIMAL = BigDecimal.class.getName().toCharArray();
    /**
     * Builtin radix explorer types
     */
    public static final char[] RADIX_TYPE_REFERENCE = "Reference".toCharArray();

    public enum CodeType {

        EXCUTABLE("Executable"),
        INVOKE("Invocation"),
        META("Meta"),
        ADDON("Addon");
        private final String name;

        private CodeType(String name) {
            this.name = name;
        }

        public final String getName() {
            return name;
        }
    }

    public enum UsagePurpose {

        COMMON_EXECUTABLE(CodeType.EXCUTABLE, ERuntimeEnvironmentType.COMMON),
        SERVER_EXECUTABLE(CodeType.EXCUTABLE, ERuntimeEnvironmentType.SERVER),
        COMMON_CLIENT_EXECUTABLE(CodeType.EXCUTABLE, ERuntimeEnvironmentType.COMMON_CLIENT),
        EXPLORER_EXECUTABLE(CodeType.EXCUTABLE, ERuntimeEnvironmentType.EXPLORER),
        WEB_EXECUTABLE(CodeType.EXCUTABLE, ERuntimeEnvironmentType.WEB),
        COMMON_META(CodeType.META, ERuntimeEnvironmentType.COMMON),
        SERVER_META(CodeType.META, ERuntimeEnvironmentType.SERVER),
        COMMON_CLIENT_META(CodeType.META, ERuntimeEnvironmentType.COMMON_CLIENT),
        EXPLORER_META(CodeType.META, ERuntimeEnvironmentType.EXPLORER),
        WEB_META(CodeType.META, ERuntimeEnvironmentType.WEB),
        COMMON_ADDON(CodeType.ADDON, ERuntimeEnvironmentType.COMMON),
        SERVER_ADDON(CodeType.ADDON, ERuntimeEnvironmentType.SERVER),
        COMMON_CLIENT_ADDON(CodeType.ADDON, ERuntimeEnvironmentType.COMMON_CLIENT),
        EXPLORER_ADDON(CodeType.ADDON, ERuntimeEnvironmentType.EXPLORER),
        WEB_ADDON(CodeType.ADDON, ERuntimeEnvironmentType.WEB),
        COMMON_INVOCATION(CodeType.INVOKE, ERuntimeEnvironmentType.COMMON),
        SERVER_INVOCATION(CodeType.INVOKE, ERuntimeEnvironmentType.SERVER),
        COMMON_CLIENT_INVOCATION(CodeType.INVOKE, ERuntimeEnvironmentType.COMMON_CLIENT),
        EXPLORER_INVOCATION(CodeType.INVOKE, ERuntimeEnvironmentType.EXPLORER),
        WEB_INVOCATION(CodeType.INVOKE, ERuntimeEnvironmentType.WEB);
        private final ERuntimeEnvironmentType environment;
        private final CodeType codeType;

        private UsagePurpose(CodeType type, ERuntimeEnvironmentType environment) {
            this.environment = environment;
            this.codeType = type;
        }

        public ERuntimeEnvironmentType getEnvironment() {
            return environment;
        }

        public CodeType getCodeType() {
            return codeType;
        }

        public boolean isExecutable() {
            return codeType == CodeType.EXCUTABLE;
        }

        public boolean isMeta() {
            return codeType == CodeType.META;
        }

        public boolean isAddon() {
            return codeType == CodeType.ADDON;
        }

        public static UsagePurpose getPurpose(ERuntimeEnvironmentType env, CodeType codeType) {
            switch (env) {
                case COMMON:
                    switch (codeType) {
                        case ADDON:
                            return COMMON_ADDON;
                        case EXCUTABLE:
                            return COMMON_EXECUTABLE;
                        case INVOKE:
                            return COMMON_INVOCATION;
                        case META:
                            return COMMON_META;
                        default:
                            break;
                    }
                    break;
                case COMMON_CLIENT:
                    switch (codeType) {
                        case ADDON:
                            return COMMON_CLIENT_ADDON;
                        case EXCUTABLE:
                            return COMMON_CLIENT_EXECUTABLE;
                        case INVOKE:
                            return COMMON_CLIENT_INVOCATION;
                        case META:
                            return COMMON_CLIENT_META;
                        default:
                            break;
                    }
                    break;
                case EXPLORER:
                    switch (codeType) {
                        case ADDON:
                            return EXPLORER_ADDON;
                        case EXCUTABLE:
                            return EXPLORER_EXECUTABLE;
                        case INVOKE:
                            return EXPLORER_INVOCATION;
                        case META:
                            return EXPLORER_META;
                        default:
                            break;
                    }
                    break;
                case WEB:
                    switch (codeType) {
                        case ADDON:
                            return WEB_ADDON;
                        case EXCUTABLE:
                            return WEB_EXECUTABLE;
                        case INVOKE:
                            return WEB_INVOCATION;
                        case META:
                            return WEB_META;
                        default:
                            break;
                    }
                    break;
                case SERVER:
                    switch (codeType) {
                        case ADDON:
                            return SERVER_ADDON;
                        case EXCUTABLE:
                            return SERVER_EXECUTABLE;
                        case INVOKE:
                            return SERVER_INVOCATION;
                        case META:
                            return SERVER_META;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
            throw new DefinitionError("Can not switch code type");
        }

        public UsagePurpose forCodeType(CodeType codeType) {
            return getPurpose(environment, codeType);
        }
    }

    public static class SharedData {

        public interface Item {

            void cleanup();
        }
        private final Map<Class<? extends Item>, Item> historyItems = new HashMap<Class<? extends Item>, Item>();

        @SuppressWarnings("unchecked")
        public <T extends Item> T findItemByClass(Class<T> cl) {
            synchronized (this) {
                return (T) historyItems.get(cl);
            }
        }

        public void registerItemByClass(Item obj) {
            synchronized (this) {
                historyItems.put(obj.getClass(), obj);
            }
        }

        public void clear() {
            synchronized (this) {
                historyItems.clear();
            }
        }

        public SharedData() {
        }

        public void cleanup() {
            synchronized (this) {
                for (Item item : historyItems.values()) {
                    item.cleanup();
                }
                historyItems.clear();
            }
        }
    }
    private RadixObject currentObject;
    private Definition currentRoot;

    public Definition getCurrentRoot() {
        return currentRoot;
    }

    public IJavaSource getCurrentObject() {
        return (IJavaSource) currentObject;
    }

    protected JavaSourceSupport(IJavaSource source) {
        this.currentObject = source instanceof RadixObject ? (RadixObject) source : null;
        this.currentRoot = source instanceof Definition ? (Definition) source : null;
    }
    private SharedData sharedData = null;

    public SharedData getSharedData() {
        synchronized (this) {
            return sharedData;
        }
    }

    public void setSharedData(SharedData data) {
        synchronized (this) {
            this.sharedData = data;
        }
    }

    public Set<CodeType> getSeparateFileTypes(ERuntimeEnvironmentType sc) {
        return Collections.emptySet();
    }

    public Set<CodeType> getSeparateFileTypesForSearch(ERuntimeEnvironmentType sc) {
        return getSeparateFileTypes(sc);
    }

    public boolean isSeparateFilesRequired(ERuntimeEnvironmentType sc) {
        Set<CodeType> types = getSeparateFileTypes(sc);
        return types != null && !types.isEmpty();
    }

    public boolean isSeparateFilesRequiredForSearch(ERuntimeEnvironmentType sc) {
        Set<CodeType> types = getSeparateFileTypesForSearch(sc);
        return types != null && !types.isEmpty();
    }

    public EnumSet<ERuntimeEnvironmentType> getSupportedEnvironments() {
        return EnumSet.allOf(ERuntimeEnvironmentType.class);
    }

    public static abstract class CodeWriter {

        protected final JavaSourceSupport.UsagePurpose usagePurpose;
        private final JavaSourceSupport support;

        protected CodeWriter(JavaSourceSupport support, JavaSourceSupport.UsagePurpose usagePurpose) {
            this.usagePurpose = usagePurpose;
            this.support = support;
        }

        public final JavaSourceSupport getSupport() {
            return support;
        }

        /**
         * Writes java code to given printer according UsagePurpose
         */
        public abstract boolean writeCode(CodePrinter printer);

        /**
         * Writes code using given source's code writer an environment settings
         * from current writer. If source is instance of RadixObject then
         * current object of environment given to source will be set to source
         * otherwise to null. Equal to
         * source.getJavaSourceSupport(JavaSourceContext.Factory.newInstance(getSupport().getContext()[,(RadixObject)source]).getCodeWriter(usagePurpose).writeCode(printer);
         */
        public boolean writeCode(CodePrinter printer, IJavaSource source) {
            synchronized (this) {
                return getCodeWriter(source).writeCode(printer);
            }
        }

        /**
         * Writes code using given source's code writer an environment settings
         * from current writer with given currentObject. Use this method for
         * IJavaSource implementations that are not RadixObjects or for
         * RadixObjects without containers Equal to
         * source.getJavaSourceSupport(JavaSourceContext.Factory.newInstance(getSupport().getContext(),currentObject).getCodeWriter(usagePurpose).writeCode(printer);
         */
        public boolean writeCode(CodePrinter printer, IJavaSource source, RadixObject currentObject) {
            synchronized (this) {
                return getCodeWriter(source, currentObject).writeCode(printer);
            }
        }

        public boolean writeCode(CodePrinter printer, IJavaSource source, SharedData data) {
            synchronized (this) {
                return getCodeWriter(source, data).writeCode(printer);
            }
        }

        /**
         * Writes object invocation typical for current environment settings
         */
        public abstract void writeUsage(CodePrinter printer);

        /**
         * Behavior equals to {@linkplain #writeCode(org.radixware.kernel.common.scml.CodePrinter, org.radixware.kernel.common.defs.ads.src.IJavaSource)
         * }
         * excepts {@linkplain #writeUsage(org.radixware.kernel.common.scml.CodePrinter)
         * } is called instead of null null null null null null null null null
         * null null null null null null null null null null null null         {@linkplain #writeCode(org.radixware.kernel.common.scml.CodePrinter)
         * }
         */
        public void writeUsage(CodePrinter printer, IJavaSource source) {
            synchronized (this) {
                getCodeWriter(source).writeUsage(printer);
            }
        }

        /**
         * Behavior equals to {@linkplain #writeCode(org.radixware.kernel.common.scml.CodePrinter, org.radixware.kernel.common.defs.ads.src.IJavaSource, org.radixware.kernel.common.defs.RadixObject) )
         * }
         * excepts {@linkplain #writeUsage(org.radixware.kernel.common.scml.CodePrinter)
         * } is called instead of null null null null null null null null null
         * null null null null null null null null null null null null         {@linkplain #writeCode(org.radixware.kernel.common.scml.CodePrinter)
         * }
         */
        public void writeUsage(CodePrinter printer, IJavaSource source, RadixObject currentObject) {
            synchronized (this) {
                getCodeWriter(source, currentObject).writeUsage(printer);
            }
        }

        /**
         * Gets code writer of source with the same usage purpose
         */
        public final CodeWriter getCodeWriter(IJavaSource source) {
            final JavaSourceSupport ex_s = source.getJavaSourceSupport();
            ex_s.currentRoot = support.currentRoot;
            return ex_s.getCodeWriter(usagePurpose);
        }

        public final CodeWriter getCodeWriter(IJavaSource source, RadixObject currentObject) {
            final JavaSourceSupport ex_s = source.getJavaSourceSupport();
            ex_s.currentObject = currentObject;
            ex_s.currentRoot = support.currentRoot;
            return ex_s.getCodeWriter(usagePurpose);
        }

        public final CodeWriter getCodeWriter(IJavaSource source, SharedData data) {
            final JavaSourceSupport ex_s = source.getJavaSourceSupport();
            ex_s.setSharedData(data);
            ex_s.currentRoot = support.currentRoot;
            return ex_s.getCodeWriter(usagePurpose);
        }
    }

    /**
     * Returns instance of code writer for given definition target
     */
    public abstract CodeWriter getCodeWriter(JavaSourceSupport.UsagePurpose purpose);

    protected class SourceFileWriter extends JavaFileSupport.FileWriter {

        protected SourceFileWriter() {
        }

        protected File getPackageDir(File packagesRoot, UsagePurpose purpose) {
            return JavaSourceSupport.getPackageDir(packagesRoot, currentRoot, purpose);
        }

        @Override
        public boolean write(final File packagesDir, final UsagePurpose purpose, final boolean force, final IProblemHandler problemHandler, final Collection<File> writtenFiles) throws IOException {

            File packageDir = getPackageDir(packagesDir, purpose);//
            return writeDefinition(packageDir, purpose, force, writtenFiles);
        }

        protected String getDefFileName() {
//            if (currentRoot instanceof AdsCustomPageEditorDef) {
//                return currentRoot.getId().toString() + "_" + ((AdsCustomPageEditorDef) currentRoot).getOwnerEditorPage().getOwnerDef().getId().toString();
//            } else {
            if (currentRoot instanceof AdsClassDef) {
                return ((AdsClassDef) currentRoot).getRuntimeLocalClassName();
            } else if (currentRoot instanceof AdsRoleDef && ((AdsRoleDef) currentRoot).isAppRole()) {
                return ((AdsRoleDef) currentRoot).getRuntimeId().toString();
            } else if (currentRoot instanceof AdsLocalizingBundleDef) {
                return ((AdsLocalizingBundleDef) currentRoot).getRuntimeId().toString();
            } else {
                return currentRoot.getId().toString();
            }
            //}
        }

        protected String getDefFileSuffixString(UsagePurpose usagePurpose) {
            return "." + getSourceFileExtension(usagePurpose);
        }

        protected boolean writeDefinition(File packageDir, UsagePurpose purpose, final boolean force, Collection<File> writtenFiles) throws IOException {
            String defFileName = getDefFileName();
            if (purpose.isMeta()) {
                defFileName += "_mi";
            }
            defFileName += getDefFileSuffixString(purpose);
            File defFile = new File(packageDir, defFileName);

            if (defFile.exists() && !force) {
                if (currentRoot instanceof IFileBasedDef) {
                    if (defFile.lastModified() >= ((IFileBasedDef) currentRoot).getFileLastModifiedTime()) {
                        writtenFiles.add(defFile);
                        return true;
                    }
                }
            }

            if (!packageDir.exists()) {
                if (!packageDir.mkdirs()) {
                    return false;
                }
            }

            JavaSourceSupport.CodeWriter writer = getCodeWriter(purpose);
            CodePrinter printer = CodePrinter.Factory.newJavaPrinter();
            if (!writer.writeCode(printer)) {
                return false;
            }

            CompilerUtils.writeString(defFile, printer.toString(), "UTF-8");
            writtenFiles.add(defFile);
            LineMatcher lm = printer.getLineMatcher();
            if (!lm.isEmpty()) {
                File smapFile = new File(packageDir, defFileName + ".smap");
                CompilerUtils.writeString(smapFile, lm.getJSR045(defFileName), "UTF-8");
            }
            if (currentRoot instanceof AdsClassDef) {
                AdsClassDef clazz = (AdsClassDef) currentRoot;
                if (!clazz.getResources().isEmpty()) {
                    for (AdsClassDef.Resources.Resource res : clazz.getResources()) {
                        if (res.getData() != null) {
                            File resourceFile = new File(packageDir, res.getName());
                            CompilerUtils.writeString(resourceFile, res.getData(), "UTF-8");
                        }
                    }
                }
            }
            return true;
        }

        @Override
        public boolean canWrite(ERuntimeEnvironmentType env) {
            return getSupportedEnvironments().contains(env);
        }

        @Override
        public EKind getKind() {
            return EKind.SOURCE;
        }

        @Override
        public IJavaSource getJavaSource() {
            return (IJavaSource) currentRoot;
        }
    }

    public static String getClassFullQualifiedJavaName(AdsPath path, AdsModule module) {
        final Definition definition = path.resolve(module).get();

        if (definition instanceof AdsClassDef) {
            final AdsClassDef cls = (AdsClassDef) definition;
            final StringBuilder builder = new StringBuilder();
            final String packageName = getPackageName(cls, JavaSourceSupport.UsagePurpose.getPurpose(
                    cls.getUsageEnvironment(), CodeType.EXCUTABLE));

            if (AdsTransparence.isTransparent(cls)) {
                return cls.getTransparence().getPublishedName();
            }

            builder.append(packageName).append('.');
            boolean first = true;
            for (final Id id : path.asArray()) {
                if (first) {
                    first = false;
                } else {
                    builder.append('$');
                }
                builder.append(id.toString());
            }

            return builder.toString();
        }
        return null;
    }

    public static final String getPackageName(Definition def, UsagePurpose purpose) {
        final char[][] components = getPackageNameComponents(def, purpose);
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < components.length; i++) {
            if (i > 0) {
                builder.append('.');
            }

            builder.append(components[i]);
        }
        return builder.toString();
    }

    public static final File getPackageDir(File packagesRoot, Definition def, UsagePurpose purpose) {
        final char[][] packageNames = getPackageNameComponents(def, purpose);
        File res = packagesRoot;
        for (int i = 0; i < packageNames.length; i++) {
            res = new File(res, new String(packageNames[i]));
        }
        return res;
    }
    private static final char[] META_PACKAGE_NAME = new char[]{'m', 'e', 't', 'a'};

    public static char[] getModulePackageName(Module module) {
        if (module instanceof AdsModule) {
            final AdsModule companion = ((AdsModule) module).findCompanionModule();
            if (companion != null) {
                return getModulePackageName(companion);
            } else {
                return module.getId().toCharArray();
            }
        } else {
            return module.getId().toCharArray();
        }
    }

    public static final char[][] getPackageNameComponents(Definition def, UsagePurpose purpose) {
        return getPackageNameComponents(def, purpose, false);
    }

    public static final char[][] getPackageNameComponents(Definition def, UsagePurpose purpose, boolean withOwnLayer) {
        Module module;

        boolean idIsPackagePart = false;
        if (def instanceof Module) {
            module = (Module) def;
            if (!withOwnLayer) {
                Module ovr = module.findOverwritten();
                while (ovr != null) {
                    module = ovr;
                    ovr = ovr.findOverwritten();
                }
            }
        } else if (def instanceof AdsDefinition) {
            final AdsDefinition determinant = ((AdsDefinition) def).findJavaPackageNameProvider();
            module = determinant.getModule();
            if (module == null) {
                return DEFAULT_PACKAGE;
            }
            if (def instanceof IXmlDefinition) {
                idIsPackagePart = true;
            }
        } else if (def instanceof Definition) {
            module = def.getModule();
            if (module == null) {
                return DEFAULT_PACKAGE;
            }
        } else {
            return DEFAULT_PACKAGE;
        }
        final int additionalNameCount = idIsPackagePart ? 4 : 3;

        final Segment segment = module.getSegment();
        final Layer layer = segment.getLayer();
        final String layerUri = layer.getURI();
        final String[] layerUriParts = layerUri.split("\\.");

        char[][] res = new char[layerUriParts.length + additionalNameCount][];

        int i = 0;
        for (; i < layerUriParts.length; i++) {
            res[i] = layerUriParts[i].toCharArray();
            CharOperation.replace(res[i], '-', '_');
        }
        res[i++] = segment.getName().toLowerCase().toCharArray();


        res[i++] = getModulePackageName(module);

        switch (purpose.getEnvironment()) {
            case SERVER:
                res[i++] = AdsModule.GEN_SERVER_DIR_NAME_C;
                break;
            case EXPLORER:
                res[i++] = AdsModule.GEN_EXPLORER_DIR_NAME_C;
                break;
            case COMMON:
                res[i++] = AdsModule.GEN_COMMON_DIR_NAME_C;
                break;
            case COMMON_CLIENT:
                res[i++] = AdsModule.GEN_COMMON_CLIENT_DIR_NAME_C;
                break;
            case WEB:
                res[i++] = AdsModule.GEN_WEB_DIR_NAME_C;
                break;
            default:
                break;
        }
        if (idIsPackagePart) {
            res[i++] = def.getId().toCharArray();
        }

        return res;
    }

    public char[][] getMainClassName(UsagePurpose up) {
        final char[][] packages = getPackageNameComponents(getCurrentRoot(), up);
        final char[][] result = new char[packages.length + 1][];
        System.arraycopy(packages, 0, result, 0, packages.length);
        if (up.isMeta()) {
            result[packages.length] = CharOperations.merge(getSimpleJavaName(), META_CLASS_SUFFIX);
        } else {
            result[packages.length] = getSimpleJavaName();
        }
        return result;
    }

    private char[] getSimpleJavaName() {
        return getCurrentRoot().getId().toCharArray();
    }
    public static final char[] META_CLASS_SUFFIX = "_mi".toCharArray();

    public JavaFileSupport.FileWriter getSourceFileWriter() {
        return new SourceFileWriter();
    }

    private static AdsDefinition findNestedDefinition(AdsDefinition def, AdsDefinition root, String className) {

        if (className != null) {

            if (className.indexOf('$') != -1) {
                final String[] names = className.split("\\$");
                final Id[] path = new Id[names.length + 1];

                path[0] = def.getId();
                for (int i = 0; i < names.length; i++) {
                    path[i + 1] = Id.Factory.loadFrom(names[i]);
                }

                return (AdsDefinition) def.findComponentDefinition(path);
            }

            Id subdefid = Id.Factory.loadFrom(className);
            AdsDefinition sub = def.findComponentDefinition(subdefid).get();
            if (sub == null) {
                sub = root.findComponentDefinition(subdefid).get();
                if (sub == null && def instanceof AdsModelClassDef) {
                    Provider provider = ((AdsModelClassDef) def).findModelPropertyProvider();
                    if (provider != null) {
                        Support support = provider.getModelPublishablePropertySupport();
                        if (support != null) {
                            sub = (AdsDefinition) support.findById(subdefid, EScope.ALL);
                        }
                    }
                }
            }

            return sub;
        }

        return null;
    }

    public static AdsDefinition javaName2AdsDefinitionRef(String name, Branch context) {
        String names[] = name.split("\\.");
        final String moduleIdPrefix = EDefinitionIdPrefix.MODULE.getValue();
        for (int i = 0; i < names.length; i++) {
            if (names[i].startsWith(moduleIdPrefix) && i > 0 && names[i - 1].equals("ads")) {
                if (names.length < i + 3) {
                    return null;
                }
                ERuntimeEnvironmentType env;
                try {
                    env = ERuntimeEnvironmentType.getForValue(names[i + 1]);
                } catch (NoConstItemWithSuchValueError e) {
                    return null;
                }
                String className = names[i + 2];
                int dollarIdx = className.indexOf("$");
                String subclassName = null;
                if (dollarIdx > 0) {
                    subclassName = className.substring(dollarIdx + 1);
                    className = className.substring(0, dollarIdx);
                }
                Id classDefinitionId = Id.Factory.loadFrom(className);
                Id moduleId = Id.Factory.loadFrom(names[i]);
                //build layer name;
                StringBuilder layerURI = new StringBuilder();
                for (int l = 0; l < i - 1; l++) {
                    if (l > 0) {
                        layerURI.append('.');
                    }
                    layerURI.append(names[l]);
                }

                Layer layer = context.getLayers().findByURI(layerURI.toString());
                if (layer == null) {
                    return null;
                }

                AdsModule module = ((AdsSegment) layer.getAds()).getModules().findById(moduleId);
                if (module == null) {
                    return null;
                }

                AdsDefinition def = module.getDefinitions().findById(classDefinitionId);
                AdsDefinition defRoot = null;
                if (def == null) {
                    for (AdsDefinition root : module.getDefinitions()) {
                        def = root.findComponentDefinition(classDefinitionId).get();
                        if (def != null) {
                            defRoot = root;
                            break;
                        }
                    }
                } else {
                    defRoot = def;
                }

                if (def != null) {
                    final AdsDefinition sub = findNestedDefinition(def, defRoot, subclassName);

                    if (sub != null) {
                        def = sub;
                    }
                }

                return def;
            }
        }
        return null;
    }

    public static SrcPositionLocator.SrcLocation javaLocation2ScmlLocation(Branch context, String className, int javaLineNumber) {
        AdsDefinition def = javaName2AdsDefinitionRef(className, context);
        if (def == null) {
            return null;
        } else {
            IJavaSource root = findJavaSourceRoot(def);
            if (root == null) {
                return null;
            } else {
                JavaSourceSupport.CodeWriter writer = root.getJavaSourceSupport().getCodeWriter(UsagePurpose.getPurpose(def.getUsageEnvironment(), CodeType.EXCUTABLE));
                CodePrinter printer = CodePrinter.Factory.newJavaPrinter();
                writer.writeCode(printer);
                char[] contents = printer.getContents();
                SrcPositionLocator locator = SrcPositionLocator.Factory.newInstance(root, contents);
                int position = lineNumber2Position(contents, javaLineNumber);
                return locator.calc(position, position);
            }
        }
    }

    public static IJavaSource findJavaSourceRoot(AdsDefinition def) {
        ERuntimeEnvironmentType env = def.getUsageEnvironment();

        while (def != null) {
            if (def instanceof IJavaSource) {
                IJavaSource src = (IJavaSource) def;

                if (def instanceof AdsClassDef && ((AdsClassDef) def).isDual() && env == ERuntimeEnvironmentType.COMMON_CLIENT) {
                    if (src.getJavaSourceSupport().isSeparateFilesRequired(ERuntimeEnvironmentType.EXPLORER) || src.getJavaSourceSupport().isSeparateFilesRequired(ERuntimeEnvironmentType.WEB)) {
                        return src;
                    }
                }

                if (src.getJavaSourceSupport().isSeparateFilesRequired(env)) {
                    return src;
                }
            }
            def = def.getOwnerDef();
        }
        return null;
    }

    public static int lineNumber2Position(char[] arr, int lineNumber) {
        int currentLine = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == '\n') {
                currentLine++;
                if (currentLine == lineNumber) {
                    return i;
                }
            }
        }
        return 0;
    }

    public String getSourceFileExtension(UsagePurpose purpose) {
        return "java";
    }
}
