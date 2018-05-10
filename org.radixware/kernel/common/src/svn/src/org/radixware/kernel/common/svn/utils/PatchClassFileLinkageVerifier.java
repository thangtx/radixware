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
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.lang.ClassLinkageAnalyzer;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;

public abstract class PatchClassFileLinkageVerifier extends ClassLinkageAnalyzer implements NoizyVerifierEx, IContextVerifier {

    private final BranchHolderParams branchParams;
    private IVerifyContext context;
    boolean wasErrors = false;
    
    public PatchClassFileLinkageVerifier(SVNRepositoryAdapter curRepository, List<String> layersPathList, ZipFile upgradeFile, String upgradeFilePath, boolean skipDevelopmentLayers, String predefinedBaseDevLayerURI, boolean verbose) {
        this(new BranchHolderParams(curRepository, layersPathList, upgradeFile, upgradeFilePath, skipDevelopmentLayers, predefinedBaseDevLayerURI), verbose);
    }
    
    public PatchClassFileLinkageVerifier(SVNRepositoryAdapter curRepository, String curBranchPath, ZipFile upgradeFile, String upgradeFilePath, boolean skipDevelopmentLayers, String predefinedBaseDevLayerURI, boolean verbose) {
        this(new BranchHolderParams(curRepository, curBranchPath, null, upgradeFile, upgradeFilePath, skipDevelopmentLayers, predefinedBaseDevLayerURI), verbose);
    }

    public PatchClassFileLinkageVerifier(BranchHolderParams branchParams, boolean verbose) {
        super(verbose);
        this.branchParams = branchParams;
    }

    @Override
    protected KernelClassLoader createKernelClassLoader(LayerInfo layer, ERuntimeEnvironmentType env) {
        return new MyKernelClassLoader((org.radixware.kernel.common.svn.utils.Utils.LayerInfo) layer, env);
    }

    @Override
    protected AdsClassLoader createAdsClassLoader(ClassLinkageAnalyzer.LayerInfo layer, ERuntimeEnvironmentType env) {
        return new MyAdsClassLoader(layer, env);
    }
    
    protected ClassLoader createBranchLoader(BranchHolder holder, ERuntimeEnvironmentType env, String extJarsPath) {
        final BranchClassLoader branchLoader = new BranchClassLoader(holder, env);
        if (extJarsPath != null && !extJarsPath.isEmpty()) {
            final File dir = new File(extJarsPath);
            if (dir.isDirectory()) {
                final List<File> jars = new ArrayList<>();
                dir.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        if (file.isDirectory()) {
                            file.listFiles(this);
                        } else if (file.getName().endsWith(".jar")) {
                            jars.add(file);
                        }
                        return false;
                    }
                });
                if (!jars.isEmpty()) {
                    final List<URL> urls = new ArrayList<>(jars.size());
                    for (File jar : jars) {
                        try {
                            urls.add(new URL("file:///" + jar.getAbsolutePath()));
                        } catch (MalformedURLException ex) {
                            holder.verifier.error(ex);
                        }
                    }
                    if (!urls.isEmpty()) {
                        return new URLClassLoader(urls.toArray(new URL[urls.size()]), branchLoader);
                    }
                }
            }
        }
        return branchLoader;
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

    @Override
    public boolean verify() {
        BranchHolder h = new BranchHolder(this, verbose);
        try {
            if (!h.initialize(branchParams)) {
                return false;
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
            return ClassLoaderUtilExt.findClassBytesBySlashSeparatedName(name, (org.radixware.kernel.common.svn.utils.Utils.LayerInfo) layer, env);
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

    static class ClassInfo {

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
        int idx1 = message.indexOf('\'');
        if (idx1 > 0) {
            int idx2 = message.indexOf('\'', idx1 + 1);
            if (idx2 > 0) {
                String className = message.substring(idx1 + 1, idx2);
                if (isWarningWhenClassMissing(className)) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean verifyUserFunctions(final Connection c) {
        BranchHolder holder = new BranchHolder(this, verbose);
        try {
            if (!holder.initialize(branchParams)) {
                return false;
            }
            return verifyUserFunctions(c, holder, ERuntimeEnvironmentType.SERVER, null);
        } finally {
            holder.finish();
        }
    }

    public boolean verifyUserFunctions(final Connection c, BranchHolder holder, ERuntimeEnvironmentType env, String extJarsPath) {
        int totalCnt;
        try (PreparedStatement stmt = c.prepareStatement("select count(*) from rdx_userfunc")) {
            try (ResultSet cntRs = stmt.executeQuery()) {
                totalCnt = cntRs.next() ? cntRs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            error(e);
            return false;
        }

        try (PreparedStatement stmt = c.prepareStatement("select javaruntime,classguid,updefid,upownerentityid,upownerpid,id from rdx_userfunc order by id desc")) {
            try (ResultSet rs = stmt.executeQuery()) {
                final ClassLoader branchClassLoader = createBranchLoader(holder, env, extJarsPath);
                final Set<String> notFoundClasses = new HashSet<>();
                final List<ClassInfo> allClasses = new LinkedList<>();
                int index = 0;
                final long startCheckMillis = System.currentTimeMillis();
                long t0 = startCheckMillis;
                while (rs.next()) {
                    if (++index % 50 == 0) {
                        final long t1 = System.currentTimeMillis();
                        message(String.format("Processed functions: %d from %d. Took time: %d ms", index, totalCnt, t1 - t0));
                        t0 = t1;
                    }
                    Blob blob = rs.getBlob(1);
                    Id classGuid = Id.Factory.loadFrom(rs.getString(2));
                    String updefid = rs.getString(3);
                    String upownerid = rs.getString(4);
                    String upownerpid = rs.getString(5);
                    int ufId = rs.getInt(6);
                    String userFuncName = upownerid + ":" + upownerpid + ":" + updefid;
                    File tmpFile = null;
                    allClasses.clear();
                    
                    try {
                        setContext(new UserFuncContext(ufId, upownerid, upownerpid, updefid, classGuid));
                        if (blob == null) {
                            error("Function is not compiled");
                            continue;
                        }
                        
                        tmpFile = File.createTempFile("rdx1", "rdx1");
                        try (InputStream in = blob.getBinaryStream(); OutputStream out = new FileOutputStream(tmpFile)) {
                            FileUtils.copyStream(in, out);
                        }
                        try (ZipFile zip = new ZipFile(tmpFile)) {
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
                        }

                        if (allClasses.isEmpty()) {
                            error("No classfiles found for user function " + userFuncName);
                            return false;
                        }
                        UserFuncClassLoader cl = new UserFuncClassLoader(ufId, branchClassLoader, this, allClasses, notFoundClasses);
                        if (!cl.verify()) {
                            return false;
                        }
                    } catch (IOException ex) {
                        error(new RadixError("Unable to read binary data for user function " + userFuncName, ex));
                    } finally {
                        if (tmpFile != null) {
                            FileUtils.deleteFile(tmpFile);
                        }
                        leaveContext();
                    }
                }

                final Calendar totalTime = Calendar.getInstance();
                totalTime.setTimeInMillis(System.currentTimeMillis() - startCheckMillis);
                final String pattern = totalTime.getTimeInMillis() > 60 * 1000 ? "mm:ss" : "ss";
                final SimpleDateFormat timeFormat = new SimpleDateFormat(pattern);
                message(String.format("Processed %d functions (total time: %s)", index, timeFormat.format(totalTime.getTime())));
                return true;
            }
        } catch (SQLException e) {
            error(e);
            return false;
        }
    }
    
    @Override
    public void message(EEventSeverity sev, String message) {
        message(message);
    }

    @Override
    public void setContext(IVerifyContext ctx) {
        this.context = ctx;
    }

    @Override
    public void leaveContext() {
        this.context = null;
    }
    
    @Override
    public IVerifyContext getContext() {
        return context;
    }
}
