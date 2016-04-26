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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import org.eclipse.jdt.internal.compiler.util.SuffixConstants;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.compiler.CompilerConstants;
import org.radixware.kernel.common.defs.Definition;

import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.IJavaModule;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsModule;
import org.radixware.kernel.common.repository.fs.IJarDataProvider;

/**
 * Support for java related files: *.java, *.class, *.jar
 *
 */
public class JavaFileSupport {

    public enum EKind {

        SOURCE,
        BINARY;
    }
    private ERuntimeEnvironmentType env;
    private IJavaSource sourceRoot;

    public JavaFileSupport(IJavaSource sourceRoot, ERuntimeEnvironmentType env) {
        this.env = env;
        this.sourceRoot = sourceRoot;
    }

    public static abstract class FileWriter {

        public abstract boolean write(final File packagesRoot, final JavaSourceSupport.UsagePurpose purpose, final boolean force, IProblemHandler problemHandler, final Collection<File> writtenFiles) throws IOException;

        public abstract boolean canWrite(ERuntimeEnvironmentType env);

        public abstract EKind getKind();

        public abstract IJavaSource getJavaSource();

        public boolean deletePackagesRootOnFail() {
            return true;
        }
    }
    private File checkedBaseDir = null;

    private File checkBaseDirectory(EKind kind) {
        synchronized (this) {
            if (checkedBaseDir == null) {

                File dir = getBaseDirOrJarFile(sourceRoot instanceof IJavaModule ? (IJavaModule) sourceRoot
                        : (IJavaModule) ((Definition) sourceRoot).getModule(), env, kind);

                if (!dir.exists()) {
                    if (dir.mkdirs()) {
                        checkedBaseDir = dir;
                    } else {
                        return null;
                    }
                } else {
                    checkedBaseDir = dir;
                }
            }
            return checkedBaseDir;
        }
    }

    public File getPackagesRoot(EKind kind) {
        return getBaseDirOrJarFile((IJavaModule) ((Definition) sourceRoot).getModule(), env, kind);
    }

    public static File getCompiledBinaryFile(IJavaModule module, ERuntimeEnvironmentType env) {
        return new File(getBinDir(module), env.getValue() + CompilerConstants.SUFFIX_STRING_JAR);
    }

    public static IJarDataProvider getCompiledBinary(AdsModule module, ERuntimeEnvironmentType env) {
        if (module == null) {
            return null;
        }
        IRepositoryAdsModule rep = module.getRepository();
        if (rep == null) {
            return null;
        }

        StringBuilder path = new StringBuilder();
        path.append("bin/");

        switch (env) {
            case COMMON:
                path.append(AdsModule.GEN_COMMON_DIR_NAME);
                break;
            case SERVER:
                path.append(AdsModule.GEN_SERVER_DIR_NAME);
                break;
            case COMMON_CLIENT:
                path.append(AdsModule.GEN_COMMON_CLIENT_JAR_NAME);
                break;
            case EXPLORER:
                path.append(AdsModule.GEN_EXPLORER_DIR_NAME);
                break;
            case WEB:
                path.append(AdsModule.GEN_WEB_DIR_NAME);
                break;
            default:
                throw new DefinitionError("Unsupported Java Environment: " + env == null ? "null" : env.name());
        }
        path.append(CompilerConstants.SUFFIX_STRING_JAR);
        try {
            return rep.getJarFile(path.toString());
        } catch (IOException ex) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public File[] writePackageContent(final FileWriter writer, final boolean force, final IProblemHandler problemHandler) throws IOException {
        return writePackageContent(writer, force, (Set) null, problemHandler);
    }

    /**
     * Writes given writer to some file inside of packages root directory
     * Returns packages root
     */
    public File[] writePackageContent(final FileWriter writer, final boolean force, Set<JavaSourceSupport.CodeType> skipCodeTypes, final IProblemHandler problemHandler) throws IOException {
//        assert writer.getJavaSource() == this.sourceRoot;
//
//        if (!writer.canWrite(env)) {
//            return null;
//        }
//        JavaSourceSupport support = sourceRoot.getJavaSourceSupport();
//        File srcRootDir = checkBaseDirectory(writer.getKind());
//        ArrayList<File> files = new ArrayList<File>();
//        for (JavaSourceSupport.CodeType codeType : support.getSeparateFileTypes(env)) {
//            if (!writer.write(srcRootDir, JavaSourceSupport.UsagePurpose.getPurpose(env, codeType), force, problemHandler, files)) {
//                return new File[0];
//            }
//        }
//        // }
//        return files.toArray(new File[files.size()]);
        return writePackageContent(writer, force, null, skipCodeTypes, problemHandler);
    }

    public File[] writePackageContent(final FileWriter writer, final boolean force, JavaSourceSupport.CodeType type, final IProblemHandler problemHandler) throws IOException {
        return writePackageContent(writer, force, type, null, problemHandler);
    }

    public File[] writePackageContent(final FileWriter writer, final boolean force, JavaSourceSupport.CodeType type, Set<JavaSourceSupport.CodeType> skipCodeTypes, final IProblemHandler problemHandler) throws IOException {
        assert writer.getJavaSource() == this.sourceRoot;

        if (!writer.canWrite(env)) {
            return new File[0];
        }
        JavaSourceSupport support = sourceRoot.getJavaSourceSupport();
        File srcRootDir = checkBaseDirectory(writer.getKind());
        ArrayList<File> files = new ArrayList<File>();
        Set<JavaSourceSupport.CodeType> codeTypes = support.getSeparateFileTypes(env);
        if (codeTypes != null) {
            for (JavaSourceSupport.CodeType codeType : codeTypes) {
                if (type != null && type != codeType) {
                    continue;
                }
                if (skipCodeTypes != null && skipCodeTypes.contains(codeType)) {
                    continue;
                }
                if (!writer.write(srcRootDir, JavaSourceSupport.UsagePurpose.getPurpose(env, codeType), force, problemHandler, files)) {
                    return new File[0];
                }
            }
        }
        // }
        return files.toArray(new File[files.size()]);
    }

    public static File getBinDir(IJavaModule module) {
        return getBaseDir(module, null, EKind.BINARY);
    }

    public static File getSourceDir(IJavaModule module, ERuntimeEnvironmentType sc) {
        return getBaseDir(module, sc, EKind.SOURCE);
    }

    private static File getBaseDir(IJavaModule module, ERuntimeEnvironmentType env, EKind kind) {
        File kindDir;
        switch (kind) {
            case BINARY:
                //kindDir = new File(module.getRepository().getBinariesDirContainer(), AdsModule.BINARIES_DIR_NAME);
                //break;
                return new File(module.getBinDirContainer(), AdsModule.BINARIES_DIR_NAME);
            case SOURCE:
                kindDir = new File(module.getSrcDirContainer(), AdsModule.BUILD_DIR_NAME);
                break;
            default:
                throw new DefinitionError("Unsupported Java Kind: " + kind == null ? "null" : kind.name());
        }
        File envDir;
        if (env != null) {
            switch (env) {
                case COMMON:
                    envDir = new File(kindDir, AdsModule.GEN_COMMON_DIR_NAME);
                    break;
                case SERVER:
                    envDir = new File(kindDir, AdsModule.GEN_SERVER_DIR_NAME);
                    break;
                case EXPLORER:
                    envDir = new File(kindDir, AdsModule.GEN_EXPLORER_DIR_NAME);
                    break;
                case COMMON_CLIENT:
                    envDir = new File(kindDir, AdsModule.GEN_COMMON_CLIENT_DIR_NAME);
                    break;
                case WEB:
                    envDir = new File(kindDir, AdsModule.GEN_WEB_DIR_NAME);
                    break;
                default:
                    throw new DefinitionError("Unsupported Java Environment: " + env == null ? "null" : env.name());
            }
        } else {
            envDir = kindDir;
        }
        return envDir;
    }

    /**
     * Returns base directory pathname for source root with given environment
     * and kind. Use this function to determine definition's package root for
     * any kinds of packages
     */
    public static File getBaseDirOrJarFile(IJavaModule root, ERuntimeEnvironmentType env, EKind kind) {
        File envDir = getBaseDir(root, env, kind);

        switch (kind) {
            case BINARY:
                //return new File(envDir, getDefinitionSourceRootName(root) + SuffixConstants.SUFFIX_STRING_jar);
                //return new File(envDir.getParent(), envDir.getName() + SuffixConstants.SUFFIX_STRING_jar);
                return getCompiledBinaryFile(root, env);
            case SOURCE:
                //return new File(envDir, getDefinitionSourceRootName(root));
                return envDir;
            default:
                throw new DefinitionError("Unsupported Java Kind: " + kind == null ? "null" : kind.name());
        }
    }
//    private static String getDefinitionSourceRootName(Definition def) {
//        if (def instanceof AdsDefinition) {
//            switch (((AdsDefinition) def).getDefinitionType()) {
//                case DOMAIN:
//                    return ((AdsDefinition) def).findTopLevelDef().getId().toString();
//
//
//            }
//        }
//        return def.getId().toString();
//    }
}
