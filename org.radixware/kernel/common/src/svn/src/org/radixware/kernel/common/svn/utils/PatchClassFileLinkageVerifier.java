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
package org.radixware.kernel.common.svn.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.lang.ClassLinkageAnalyzer;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.utils.FileUtils;

public abstract class PatchClassFileLinkageVerifier extends ClassLinkageAnalyzer implements NoizyVerifier {

    private final String branchPath;
    private final List<String> layersPathList;
    private final String zipFilePath;
    private final ZipFile zipFile;
    private SVNRepositoryAdapter repository;
    private final boolean skipDevelopmentLayers;
    private final String predefinedBaseDevLayerURI;

    @Override
    protected KernelClassLoader createKernelClassLoader(LayerInfo layer, ERuntimeEnvironmentType env) {
        return new MyKernelClassLoader((org.radixware.kernel.common.svn.utils.Utils.LayerInfo) layer, env);
    }

    @Override
    protected AdsClassLoader createAdsClassLoader(ClassLinkageAnalyzer.LayerInfo layer, ERuntimeEnvironmentType env) {
        return new MyAdsClassLoader(layer, env);
    }

    @Override
    protected void acceptProblem(ClassLinkageAnalyzer.ModuleInfo module, String message) {
        String report = "Error at " + module.getDisplayName() + ": " + message;
        if (isWarningOnErrorMessage(message)) {
            message(report);
        } else {
            wasErrors = true;
            error(report);
        }
    }

    @Override
    protected void acceptMessage(String message) {
        message(message);
    }

    public PatchClassFileLinkageVerifier(SVNRepositoryAdapter repository, String branchPath, String zipFilePath, boolean skipDevelopmentLayers, String predefinedBaseDevLayerURI) {
        this(repository, branchPath, null, zipFilePath, skipDevelopmentLayers, predefinedBaseDevLayerURI, true);
    }

    public PatchClassFileLinkageVerifier(SVNRepositoryAdapter repository, String branchPath, ZipFile zipFile, boolean skipDevelopmentLayers, String predefinedBaseDevLayerURI) {
        this(repository, branchPath, zipFile, null, skipDevelopmentLayers, predefinedBaseDevLayerURI, false);
    }

    public PatchClassFileLinkageVerifier(SVNRepositoryAdapter repository, String branchPath, ZipFile zipFile, String zipFilePath, boolean skipDevelopmentLayers, String predefinedBaseDevLayerURI, boolean verbose) {
        super(verbose);
        this.repository = repository;
        this.branchPath = branchPath;
        this.layersPathList = null;
        this.zipFilePath = zipFilePath;
        this.skipDevelopmentLayers = skipDevelopmentLayers;
        this.predefinedBaseDevLayerURI = predefinedBaseDevLayerURI;
        this.zipFile = zipFile;
    }

    public PatchClassFileLinkageVerifier(SVNRepositoryAdapter repository, final List<String> layersPathList, ZipFile zipFile, String zipFilePath, boolean skipDevelopmentLayers, String predefinedBaseDevLayerURI, boolean verbose) {
        super(verbose);
        this.repository = repository;
        this.branchPath = null;
        this.layersPathList = layersPathList;
        this.zipFilePath = zipFilePath;
        this.skipDevelopmentLayers = skipDevelopmentLayers;
        this.predefinedBaseDevLayerURI = predefinedBaseDevLayerURI;
        this.zipFile = zipFile;
    }
    boolean wasErrors = false;

    @Override
    public boolean verify() {
        BranchHolder h = new BranchHolder(this, verbose);
        try {
            if (branchPath != null) {
                if (!h.initialize(repository, branchPath, zipFile, zipFilePath, predefinedBaseDevLayerURI, skipDevelopmentLayers)) {
                    return false;
                }
            } else {
                if (!h.initializeEx(repository, layersPathList, zipFile, zipFilePath, predefinedBaseDevLayerURI, skipDevelopmentLayers)) {
                    return false;
                }
            }
            return verifyExternalHolder(h);
        } finally {
            h.finish();
        }
    }

    public boolean verifyExternalHolder(BranchHolder holder) {
        wasErrors = false;
        for (org.radixware.kernel.common.svn.utils.Utils.LayerInfo info : holder.layers.values()) {
            for (org.radixware.kernel.common.svn.utils.Utils.ModuleInfo module : info.modules.values()) {
                if (wasCancelled()) {
                    return false;
                }
                process(module);
            }
        }
        return !wasErrors;
    }

    private class MyKernelClassLoader extends KernelClassLoader {

        public MyKernelClassLoader(org.radixware.kernel.common.svn.utils.Utils.LayerInfo layer, ERuntimeEnvironmentType env) {
            super(layer, env);
        }

        @Override
        protected byte[] findClassBytesBySlashSeparatedName(String name) throws ClassNotFoundException {
            org.radixware.kernel.common.svn.utils.Utils.LayerInfo layerInfo = (org.radixware.kernel.common.svn.utils.Utils.LayerInfo) layer;

            byte[] result = layerInfo.getLib(env).findClassBytesBySlashSeparatedName(name);
            if (result != null) {
                return result;
            } else {
                result = findClassBytesBySlashSeparatedName(layerInfo, name, env);
                if (result != null) {
                    return result;
                }
            }

            if (env != ERuntimeEnvironmentType.COMMON) {
                layerInfo = (org.radixware.kernel.common.svn.utils.Utils.LayerInfo) layer;
                result = layerInfo.getLib(ERuntimeEnvironmentType.COMMON).findClassBytesBySlashSeparatedName(name);
                if (result != null) {
                    return result;
                } else {
                    result = findClassBytesBySlashSeparatedName(layerInfo, name, ERuntimeEnvironmentType.COMMON);
                    if (result != null) {
                        return result;
                    }
                }

                return null;
            } else {
                return null;
            }
        }

        private byte[] findClassBytesBySlashSeparatedName(org.radixware.kernel.common.svn.utils.Utils.LayerInfo layerInfo, String name, ERuntimeEnvironmentType env) {
            List<LayerInfo> prevs = layerInfo.findPrevLayer();
            for (final LayerInfo prev : prevs) {
                final org.radixware.kernel.common.svn.utils.Utils.LayerInfo info = (org.radixware.kernel.common.svn.utils.Utils.LayerInfo) prev;
                try {
                    byte[] result = info.getLib(env).findClassBytesBySlashSeparatedName(name);
                    if (result != null) {
                        return result;
                    }
                } catch (ClassNotFoundException e) {
                }
            }
            for (final LayerInfo prev : prevs) {
                final org.radixware.kernel.common.svn.utils.Utils.LayerInfo info = (org.radixware.kernel.common.svn.utils.Utils.LayerInfo) prev;
                byte[] result = findClassBytesBySlashSeparatedName(info, name, env);
                if (result != null) {
                    return result;
                }
            }
            return null;
        }

        @Override
        protected void acceptProblem(String message) {
            String report = "Error at " + layer.getDisplayName() + ": " + message;
            if (isWarningOnErrorMessage(message)) {
                message(report);
            } else {
                error(report);
                wasErrors = true;
            }
        }

        @Override
        protected void acceptMessage(String message) {
            message(message);
        }

        @Override
        protected void close() {
            org.radixware.kernel.common.svn.utils.Utils.LayerInfo info = (org.radixware.kernel.common.svn.utils.Utils.LayerInfo) layer;
            for (ERuntimeEnvironmentType e : ERuntimeEnvironmentType.values()) {
                info.getLib(e).close();
            }
        }

        @Override
        protected void summary() {
        }
    }

    private class MyAdsClassLoader extends AdsClassLoader {

        public MyAdsClassLoader(ClassLinkageAnalyzer.LayerInfo layer, ERuntimeEnvironmentType env) {
            super(layer, env);
        }

        @Override
        protected void acceptProblem(String message) {
            String report = "Error at " + layer.getDisplayName() + ": " + message;
            if (isWarningOnErrorMessage(message)) {
                message(report);
            } else {
                error(report);
                wasErrors = true;
            }
        }

        @Override
        protected void acceptMessage(String message) {
            message(message);
        }
    }
    List<org.radixware.kernel.common.svn.utils.Utils.ZipModule> zipModules = new LinkedList<org.radixware.kernel.common.svn.utils.Utils.ZipModule>();

    private class ClassInfo {

        String name;
        byte[] bytes;
        Class clazz;

        public ClassInfo(String name, byte[] bytes) {
            this.name = name.replace("/", ".");
            this.bytes = bytes;
        }
    }

    protected boolean isWarningWhenClassMissing(String className) {
        return false;
    }

    protected boolean isWarningOnErrorMessage(String message) {
        int idx1 = message.indexOf("'");
        if (idx1 > 0) {
            int idx2 = message.indexOf("'", idx1 + 1);
            if (idx2 > 0) {
                String className = message.substring(idx1 + 1, idx2);
                if (isWarningWhenClassMissing(className)) {
                    return true;
                }
            }
        }
        return false;
    }

    private class UserFuncClassLoader extends ClassLoader {

        final List<Utils.LayerInfo> topLayers;
        final List<ClassInfo> classFiles;
        private final Set<String> resolvedClasses = new HashSet<String>();
        private final BranchHolder branchHolder;

        public UserFuncClassLoader(BranchHolder branchHolder, List<ClassInfo> names, List<Utils.LayerInfo> topLayers) {
            this.topLayers = topLayers;
            this.classFiles = names;
            this.branchHolder = branchHolder;
        }

        public boolean verify() {
            try {
                for (ClassInfo info : classFiles) {
                    info.clazz = defineClass(info.name, info.bytes, 0, info.bytes.length);
                }
                for (ClassInfo info : classFiles) {
                    if (!resolve(info.clazz)) {
                        return false;
                    }
                }
                return true;
            } catch (RuntimeException e) {
                return false;
            }
        }

        private boolean resolve(Class c) {
            synchronized (resolvedClasses) {
                if (resolvedClasses.contains(c.getName())) {
                    return true;
                }
                resolvedClasses.add(c.getName());
                try {
                    this.resolveClass(c);
                    c.getDeclaredClasses();
                    c.getDeclaredAnnotations();
                    c.getDeclaredConstructors();
                    c.getDeclaredFields();
                    c.getDeclaredMethods();
                    c.getDeclaringClass();
                    return true;
                } catch (LinkageError e) {
                    if (branchHolder.isClassMayBeInDevelopmentLayer(e.getMessage())) {
                        error("Warning: can not find class " + e.getMessage() + ". Seems like class from development layer");
                        return true;
                    } else {

                        String message = e.getMessage();
                        if (isWarningOnErrorMessage(message)) {
                            message("Class linkage error: " + e.getMessage());
                            return true;
                        } else {
                            error("Class linkage error: " + e.getMessage());
                            return false;
                        }
                    }
                }
            }
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            for (ClassInfo info : classFiles) {
                if (info.name.equals(name)) {
                    return info.clazz;
                }
            }
            for (LayerInfo info : topLayers) {
                AdsClassLoader loader = getAdsLoader(info, ERuntimeEnvironmentType.SERVER);
                try {
                    Class<?> c = loader.loadClass(name);
                    if (c != null) {
                        return c;
                    }
                } catch (ClassNotFoundException e) {
                }
            }
            throw new ClassNotFoundException(name);
        }
    }

    boolean verifyUserFunctions(final Connection c) {
        BranchHolder holder = new BranchHolder(this, verbose);
        try {
            if (!holder.initialize(repository, branchPath, zipFile, zipFilePath, predefinedBaseDevLayerURI, skipDevelopmentLayers)) {
                return false;
            }
            return verifyUserFunctions(c, holder);
        } finally {
            holder.finish();
        }
    }

    boolean verifyUserFunctions(final Connection c, BranchHolder holder) {
        try {
            final List<Utils.LayerInfo> topLayers = Utils.topLayers(holder.layers.values());
            PreparedStatement stmt = c.prepareStatement("select javaruntime,classguid,updefid,upownerentityid,upownerpid from rdx_userfunc where javaruntime is not null");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Blob blob = rs.getBlob(1);
                String updefid = rs.getString(3);
                String upownerid = rs.getString(4);
                String upownerpid = rs.getString(5);
                String userFuncName = upownerid + ":" + upownerpid + ":" + updefid;
                InputStream in = blob.getBinaryStream();
                File tmpFile = null;
                OutputStream out = null;
                List<ClassInfo> allClasses = new LinkedList<ClassInfo>();
                try {
                    tmpFile = File.createTempFile("rdx1", "rdx1");
                    out = new FileOutputStream(tmpFile);
                    FileUtils.copyStream(in, out);
                    ZipFile zip = null;
                    try {
                        zip = new ZipFile(tmpFile);
                        Enumeration<? extends ZipEntry> entries = zip.entries();
                        while (entries.hasMoreElements()) {
                            ZipEntry e = entries.nextElement();
                            if (!e.isDirectory() && e.getName().endsWith(".class")) {
                                allClasses.add(new ClassInfo(e.getName().substring(0, e.getName().length() - 6), FileUtils.getZipEntryByteContent(e, zip)));
                            }
                        }
                    } catch (ZipException e) {
                        error(new RadixError("Unable to read binary data for user function " + userFuncName, e));
                        return false;
                    } finally {
                        if (zip != null) {
                            zip.close();
                        }
                    }
                } catch (IOException ex) {
                    error(new RadixError("Unable to read binary data for user function " + userFuncName, ex));
                } finally {
                    if (tmpFile != null) {
                        FileUtils.deleteFile(tmpFile);
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException ex) {
                        }
                    }
                }
                if (allClasses.isEmpty()) {
                    error("No classfiles found for user function " + userFuncName);
                    return false;
                }
                UserFuncClassLoader cl = new UserFuncClassLoader(holder, allClasses, topLayers);
                if (!cl.verify()) {
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            error(e);
            return false;
        }
    }
}
