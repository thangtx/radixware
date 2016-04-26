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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.constants.FileNames;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.lang.ClassLinkageAnalyzer;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.client.SvnEntry;
import org.radixware.kernel.common.svn.client.SvnPath;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.JarFiles;
import org.radixware.schemas.product.BranchDocument;
import org.radixware.schemas.product.FileChanges.Add;
import org.radixware.schemas.product.FileChanges.Modify;
import org.radixware.schemas.product.FileChanges.Remove;
import org.radixware.schemas.product.LayerDocument;
import org.radixware.schemas.product.ModuleDocument;
import org.radixware.schemas.product.Upgrade;
import org.radixware.schemas.product.UpgradeDocument;

class Utils {

    static final String NO_BASE_DEVELOPMENT_LAYER_SPECIFIED = "no base layer";

    static class ModuleInfo implements ClassLinkageAnalyzer.ModuleInfo {

        protected final Id id;
        protected final String name;
        protected final Id companionId;
        protected final LayerInfo layer;
        private File commonJar;
        private File serverJar;
        private File explorerJar;
        private File commonClientJar;
        private File webJar;
        private SVNRepositoryAdapter repository;
        private long revision;
        private String branchPath;
        List<String> removedFiles;

        public ModuleInfo(LayerInfo layer, Id id, Id companionId, String name) {
            this.id = id;
            this.companionId = companionId;
            this.name = name;
            this.layer = layer;
        }

        @Override
        public Id getId() {
            return id;
        }

        private byte[] getNamedFileData(String fileName) {
            if (isFileRemovedFromModule(removedFiles, fileName)) {
                return REMOVED_DATA;
            }
            try {
                final String path = SvnPath.append(SvnPath.append(SvnPath.append(SvnPath.append(branchPath, layer.uri), "ads"), name), fileName);
                return SVN.getFile(repository, path, revision);
            } catch (RadixSvnException ex) {
                System.out.println(ex.getMessage());
                return null;
            }
        }

        @Override
        public byte[] findDefinitionsIndexData() {
            return getNamedFileData("definitions.xml");
        }
        
        @Override
        public byte[] findDirectoryIndexData(){
            return getNamedFileData(FileNames.DIRECTORY_XML);
        }

        byte[] getAPIXmlData() {
            return getNamedFileData("api.xml");
        }

        byte[] getUsagesXmlData() {
            return getNamedFileData("usages.xml");
        }

        @Override
        public File findBinaryFile(ERuntimeEnvironmentType env) {
            switch (env) {
                case COMMON:
                    if (commonJar == null) {
                        commonJar = checkOutBinaryFile(env);
                    }
                    return commonJar;
                case SERVER:
                    if (serverJar == null) {
                        serverJar = checkOutBinaryFile(env);
                    }
                    return serverJar;
                case EXPLORER:
                    if (explorerJar == null) {
                        explorerJar = checkOutBinaryFile(env);
                    }
                    return explorerJar;
                case COMMON_CLIENT:
                    if (commonClientJar == null) {
                        commonClientJar = checkOutBinaryFile(env);
                    }
                    return commonClientJar;
                case WEB:
                    if (webJar == null) {
                        webJar = checkOutBinaryFile(env);
                    }
                    return webJar;
            }
            return null;
        }

        @Override
        public LayerInfo getLayer() {
            return layer;
        }

        protected File checkOutBinaryFile(ERuntimeEnvironmentType env) {
            try {

                String filePathInModule = "bin/" + env.getValue() + ".jar";
                if (isFileRemovedFromModule(removedFiles, filePathInModule)) {
                    return null;
                }

                String path = SvnPath.append(SvnPath.append(SvnPath.append(SvnPath.append(branchPath, layer.uri), "ads"), name), filePathInModule);
                File out = null;

                try {
                    out = File.createTempFile("rdx2", "rdx2");
                    SVN.getFile(repository, path, revision, out);
                    return out;
                } catch (RadixSvnException ex) {
                    if (out != null) {
                        FileUtils.deleteFile(out);
                    }
                    return null;
                }
            } catch (IOException ex) {
                return null;
            }
        }

        @Override
        public String getDisplayName() {
            return layer.name + "::" + name;
        }

        @Override
        public void close() {
            if (commonJar != null) {
                FileUtils.deleteFile(commonJar);
            }
            if (explorerJar != null) {
                FileUtils.deleteFile(explorerJar);
            }
            if (serverJar != null) {
                FileUtils.deleteFile(serverJar);
            }
            if (commonClientJar != null) {
                FileUtils.deleteFile(commonClientJar);
            }
            if (webJar != null) {
                FileUtils.deleteFile(webJar);
            }
        }

        public String toString() {
            return getDisplayName() + " " + (companionId == null ? "" : " has companion");
        }
    }

    private static File extractZipFile(ZipFile zip, String entryName) {
        File tmp = null;
        try {
            ZipEntry e = zip.getEntry("files/" + entryName);
            if (e == null) {
                return null;
            }
            tmp = File.createTempFile("rdx3", "rdx3");
            InputStream in = zip.getInputStream(e);
            OutputStream out = new FileOutputStream(tmp);
            try {
                FileUtils.copyStream(in, out);
            } finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }
            return tmp;
        } catch (IOException ex) {
            if (tmp != null) {
                FileUtils.deleteFile(tmp);
            }
        }
        return null;
    }

    static class ZipFileInfo extends FileInfo {

        private ZipFile zipFile;
        private String zipFilePath;
        private String upgradeEntryPath;

        ZipFileInfo(ZipFile zipFile, String zipFilePath, String upgradeEntryPath) {
            super(null);
            this.zipFile = zipFile;
            this.zipFilePath = zipFilePath;
            this.upgradeEntryPath = upgradeEntryPath;
        }

        @Override
        protected void ensureInitialized() {
            super.ensureInitialized();
            ZipFile zip = zipFile;
            if (zip == null) {
                try {
                    zip = new ZipFile(zipFilePath);
                } catch (IOException ex) {
                }
            }
            file = extractZipFile(zip, upgradeEntryPath);
        }
    }

    static class FileInfo {

        File file;
        List<String> entries;

        public FileInfo(File file) {
            this.file = file;
        }

        protected void ensureInitialized() {
            entries = new LinkedList<String>();
        }

        public List<String> entries() {
            if (entries != null) {
                return entries;
            }
            ensureInitialized();

            if (file != null) {

                ZipFile zip = null;
                try {
                    zip = new ZipFile(file);
                    Enumeration<? extends ZipEntry> zipentries = zip.entries();

                    while (zipentries.hasMoreElements()) {
                        ZipEntry e = zipentries.nextElement();
                        entries.add(e.getName());
                    }
                } catch (ZipException ex) {
                } catch (IOException ex) {
                } finally {
                    if (zip != null) {
                        try {
                            zip.close();
                        } catch (IOException ex) {
                        }
                    }
                }
            }
            return entries;
        }
    }
    private static final FileInfo NO_FILE = new FileInfo(null);

    static class Lib {

        private Map<String, FileInfo> jars = null;
        private FileInfo[] jreJars;
        private final LayerInfo layer;
        private ERuntimeEnvironmentType env;
        private List<String> removedFiles;
        private ZipFile upgradeFile;
        private String upgradeFilePath;

        public Lib(LayerInfo layer, ERuntimeEnvironmentType env, List<String> removedFiles, ZipFile upgradeFile, String upgradeFilePath) {
            this.env = env;
            this.layer = layer;
            this.removedFiles = removedFiles;
            this.upgradeFile = upgradeFile;
            this.upgradeFilePath = upgradeFilePath;
        }

        void close() {
        }

        private void removeFiles(Collection<FileInfo> files) {
            for (FileInfo f : files) {
                if (f != NO_FILE && f != null && f.file != null) {
                    FileUtils.deleteFile(f.file);
                }
            }
        }

        void cleanup() {
            if (jars != null) {
                removeFiles(jars.values());
                jars = null;
            }
            jreJars = null;
        }

        private void list() {
            if (jars == null) {
                jars = new HashMap<String, FileInfo>();
                for (String bin : new String[]{"bin", "lib"}) {
                    loadFromModule(env.getValue(), bin);
                }
                if (env == ERuntimeEnvironmentType.COMMON && layer.getURI().equals("org.radixware")) {
                    loadFromModule("starter", "bin");
                }
            }
        }

        private String getDirPathInBranch(String fullPath) {
            String result;
            if (fullPath.startsWith(layer.branchPath)) {
                int len = layer.branchPath.length();
                if (!layer.branchPath.endsWith("/")) {
                    len++;
                }
                result = fullPath.substring(len);
            } else {
                result = fullPath;
            }
            if (!result.endsWith("/")) {
                return result + "/";
            } else {
                return result;
            }
        }

        private void loadFromModule(String moduleName, String binariesDirName) {

            String segmentPath = SvnPath.append(SvnPath.append(layer.branchPath, layer.getURI()), "kernel");

            final String modulePath = SvnPath.append(SvnPath.append(segmentPath, moduleName), binariesDirName);
            final String pathInBranch = getDirPathInBranch(modulePath);
            if (isFileRemovedFromBranch(removedFiles, pathInBranch)) {
                return;
            }
            try {
                List<String> subfolders = new ArrayList<String>(20);
                processBinaryFolder(modulePath, pathInBranch, subfolders);
//                layer.repository.getDir(modulePath, layer.revision, null, new ISVNDirEntryHandler() {
//
//                    @Override
//                    public void handleDirEntry(SVNDirEntry svnde) throws SVNException {
//                        if (svnde.getKind() == SVNNodeKind.FILE && svnde.getName().endsWith(".jar")) {
//                            String jarFilePathInBranch = pathInBranch + svnde.getName();
//                            if (!isFileRemovedFromBranch(removedFiles, jarFilePathInBranch)) {
//                                if (layer.upgradeFileKernelJars != null && layer.upgradeFileKernelJars.contains(jarFilePathInBranch)) {
//                                    jars.put(jarFilePathInBranch, new ZipFileInfo(upgradeFile, upgradeFilePath, jarFilePathInBranch));
//                                } else {
//                                    jars.put(SvnPath.append(modulePath, svnde.getName()), null);
//                                }
//                            }
//                        } else if (svnde.getKind() == SVNNodeKind.DIR) {
//                            initSubfolders.add(SvnPath.append(modulePath, svnde.getName()));
//                        }
//                    }
//                });

                final List<String> newSubfolders = new ArrayList<String>(10);
                while (!subfolders.isEmpty()) {
                    newSubfolders.clear();
                    for (String subfolder : subfolders) {
                        String subfolderPathInBranch = getDirPathInBranch(subfolder);
                        processBinaryFolder(subfolder, subfolderPathInBranch, newSubfolders);
                    }
                    subfolders.clear();
                    subfolders.addAll(newSubfolders);
                }
                if (layer.upgradeFileKernelJars != null) {
                    for (String kernelJarFromUpgrade : layer.upgradeFileKernelJars) {
                        //do not check for removing
                        //because only added and modified files in layer.upgradeFileKernelJars
                        if (kernelJarFromUpgrade.startsWith(pathInBranch)) {
                            FileInfo info = jars.get(kernelJarFromUpgrade);
                            if (info == null) {//does not exist in current repository
                                jars.put(kernelJarFromUpgrade, new ZipFileInfo(upgradeFile, upgradeFilePath, kernelJarFromUpgrade));
                            }
                        }
                    }
                }

            } catch (RadixSvnException ex) {
                if (layer.verifier != null) {
                    layer.verifier.error("Unable to locate kernel modules of layer " + layer.getDisplayName());
                }
            }
        }

        private void processBinaryFolder(final String path, final String pathInBranch, final List<String> newSubfolders) throws RadixSvnException {
            SvnEntry.Kind pathKind = layer.repository.checkPath(path, layer.revision);
            if (pathKind == SvnEntry.Kind.DIRECTORY) {
                layer.repository.getDir(path, layer.revision, new SVNRepositoryAdapter.EntryHandler() {

                    @Override
                    public void accept(SvnEntry svnde) throws RadixSvnException {
                        if (svnde.getKind() == SvnEntry.Kind.FILE && svnde.getName().endsWith(".jar")) {
                            String jarFilePathInBranch = pathInBranch + svnde.getName();
                            if (!isFileRemovedFromBranch(removedFiles, jarFilePathInBranch)) {
                                if (layer.upgradeFileKernelJars != null && layer.upgradeFileKernelJars.contains(jarFilePathInBranch)) {
                                    jars.put(jarFilePathInBranch, new ZipFileInfo(upgradeFile, upgradeFilePath, jarFilePathInBranch));
                                } else {
                                    jars.put(SvnPath.append(path, svnde.getName()), null);
                                }
                            }
                        } else if (svnde.getKind() == SvnEntry.Kind.DIRECTORY) {
                            newSubfolders.add(SvnPath.append(path, svnde.getName()));
                        }
                    }
                });
            }
        }

        protected byte[] findClassBytesBySlashSeparatedName(String name) throws ClassNotFoundException {
            list();
            List<String> pathes = new LinkedList<String>(jars.keySet());
            for (String path : pathes) {
                FileInfo file = getLibFile(path);
                if (file != null) {
                    byte[] result = findClassBytesBySlashSeparatedName(file, name);
                    if (result != null) {
                        return result;
                    }
                }
            }
            if (env == ERuntimeEnvironmentType.COMMON) {
                if (jreJars == null) {
                    File[] jreFiles = JarFiles.lookupJreJars();
                    jreJars = new FileInfo[jreFiles.length];
                    for (int i = 0; i < jreFiles.length; i++) {
                        jreJars[i] = new FileInfo(jreFiles[i]);
                    }
                }

                for (FileInfo file : jreJars) {
                    byte[] result = findClassBytesBySlashSeparatedName(file, name);
                    if (result != null) {
                        return result;
                    }
                }
            } else {
            }

            return null;
        }

        private FileInfo getLibFile(String path) {
            list();
            FileInfo uploaded = jars.get(path);
            if (uploaded == NO_FILE) {
                return null;
            }
            if (uploaded == null) {
                try {
                    File tmp = File.createTempFile("rdx4", "rdx4");
                    SVN.getFile(layer.repository, path, layer.revision, tmp);
                    uploaded = new FileInfo(tmp);
                } catch (RadixSvnException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                jars.put(path, uploaded);
            }
            return uploaded;
        }

        private byte[] findClassBytesBySlashSeparatedName(FileInfo file, String name) throws ClassNotFoundException {
            ZipFile zip = null;
            try {
                List<String> entries = file.entries();
                String entryName = name + ".class";
                if (entries.contains(entryName)) {
                    zip = new ZipFile(file.file);
                    ZipEntry e = zip.getEntry(entryName);
                    return FileUtils.getZipEntryByteContent(e, zip);
                }
            } catch (ZipException ex) {
                Logger.getLogger(PatchClassFileLinkageVerifier.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PatchClassFileLinkageVerifier.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (zip != null) {
                    try {
                        zip.close();
                    } catch (IOException ex) {
                    }
                }
            }
            return null;
        }
    }

    static class LayerInfo implements ClassLinkageAnalyzer.LayerInfo {

        public final String uri;
        public final List<String> prevLayerUris = new LinkedList<>();
        ;
        public final boolean hasKernel;
        public final boolean hasAds;
        public final boolean hasDds;
        public final String name;
        private List<ClassLinkageAnalyzer.LayerInfo> prevLayers = new LinkedList<>();
        private long revision;
        private NoizyVerifier verifier;
        private SVNRepositoryAdapter repository;
        private String branchPath;
        private final Lib explorerLib;
        private final Lib webLib;
        private final Lib clientLib;
        private final Lib serverLib;
        private final Lib commonLib;
        Map<Id, ModuleInfo> modules = null;
        List<String> upgradeFileKernelJars;

        public LayerInfo(String name, String uri, List<String> prevLayerUri, boolean hasKernel, boolean hasDds, boolean hasAds, List<String> removedFiles, ZipFile upgradeFile, String upgradeFilePath, List<String> upgradeKernelPathes) {
            this.name = name;
            this.uri = uri;
            if (prevLayerUri != null) {
                this.prevLayerUris.addAll(prevLayerUri);
            }
            this.hasKernel = hasKernel;
            this.hasAds = hasAds;
            this.hasDds = hasDds;
            explorerLib = new Lib(this, ERuntimeEnvironmentType.EXPLORER, removedFiles, upgradeFile, upgradeFilePath);
            serverLib = new Lib(this, ERuntimeEnvironmentType.SERVER, removedFiles, upgradeFile, upgradeFilePath);
            commonLib = new Lib(this, ERuntimeEnvironmentType.COMMON, removedFiles, upgradeFile, upgradeFilePath);
            clientLib = new Lib(this, ERuntimeEnvironmentType.COMMON_CLIENT, removedFiles, upgradeFile, upgradeFilePath);
            webLib = new Lib(this, ERuntimeEnvironmentType.WEB, removedFiles, upgradeFile, upgradeFilePath);
            this.upgradeFileKernelJars = upgradeKernelPathes.isEmpty() ? null : new LinkedList<String>(upgradeKernelPathes);
        }

        @Override
        public String getURI() {
            return uri;
        }

        Lib getLib(ERuntimeEnvironmentType e) {
            switch (e) {
                case COMMON:
                    return commonLib;
                case EXPLORER:
                    return explorerLib;
                case SERVER:
                    return serverLib;
                case COMMON_CLIENT:
                    return clientLib;
                case WEB:
                    return webLib;
            }
            return null;
        }

//        if (module instanceof AdsModule) {
//            AdsModule companion = ((AdsModule) module).findCompanionModule();
//            if (companion != null) {
//                return getModulePackageName(companion);
//            } else {
//                return module.getId().toCharArray();
//            }
//        } else {
//            return module.getId().toCharArray();
//        }
        @Override
        public ClassLinkageAnalyzer.ModuleInfo[] findModulesByPackageNameId(Id id) {
            List<ModuleInfo> infos = new LinkedList<>();

            for (ModuleInfo info : modules.values()) {

                if (info != null) {
                    if (info.id == id || info.companionId == id) {
                        infos.add(info);
                    }
                }
            }

            List<ModuleInfo> lookup = new LinkedList<>(infos);
            for (;;) {
                List<ModuleInfo> addons = new LinkedList<>();

                for (ModuleInfo found : lookup) {
                    for (ModuleInfo info : modules.values()) {
                        if (info != null) {
                            if (info.companionId == found.id) {//tranzitive companion
                                addons.add(info);
                            }
                        }
                    }
                }

                lookup.clear();
                for (ModuleInfo info : addons) {
                    if (!infos.contains(info)) {
                        infos.add(info);
                        lookup.add(info);
                    }
                }
                if (lookup.isEmpty()) {
                    break;
                }
            }

            return infos.toArray(new ModuleInfo[infos.size()]);
        }

        public ClassLinkageAnalyzer.ModuleInfo findModule(Id id) {
            return modules.get(id);
        }

        @Override
        public List<ClassLinkageAnalyzer.LayerInfo> findPrevLayer() {
            return prevLayers;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }

    static boolean listLayersEx(boolean verbose, final SVNRepositoryAdapter repository, final long revision, final List<String> layersPathList, String baseDevLayerURI, boolean skipDevelopmentLayers, NoizyVerifier context, List<LayerInfo> infos, List<String> removedFiles, ZipFile upgradeFile, String upgradeFilePath, List<String> allKernelJars, List<String> devLayers, List<String> addedAndModifiedLayerDescriptions, ZipFile zipFile, String zipFilePath) {
        try {
            String baseDevelopmentLayerURI = null;
            ZipFile useZipFile = null;

//            if (skipDevelopmentLayers) {
//                if (baseDevLayerURI == NO_BASE_DEVELOPMENT_LAYER_SPECIFIED) {
//                    ByteArrayInputStream in = null;
//                    try {
//                        byte[] bytes = SVN.getFile(repository, SvnPath.append(branchPath, Branch.BRANCH_XML_FILE_NAME), revision);
//                        in = new ByteArrayInputStream(bytes);
//                        BranchDocument xDoc = BranchDocument.Factory.parse(in);
//                        baseDevelopmentLayerURI = xDoc.getBranch().getBaseDevUri();
//                    } catch (Exception ex) {
//                        if (context != null) {
//                            context.error("Unable to obtain base  development layer URI of branch " + branchPath);
//                            return false;
//                        }
//                    } finally {
//                        if (in != null) {
//                            try {
//                                in.close();
//                            } catch (IOException ex) {
//                            }
//                        }
//                    }
//                } else {
//                    baseDevelopmentLayerURI = baseDevLayerURI;
//                }
//            }
//            final List<String> candidates = new LinkedList<String>();
//            repository.getDir(branchPath, revision, null, new ISVNDirEntryHandler() {
//
//                @Override
//                public void handleDirEntry(SVNDirEntry svnde) throws SVNException {
//                    if (svnde.getKind() == SVNNodeKind.DIR) {
//                        candidates.add(svnde.getName());
//                    }
//                }
//            });
            final List<String> layerSpecificKernelJars = new LinkedList();

            if (verbose) {
                context.message("Looking for layers...");
            }

            for (String fullLayerPath : layersPathList) {
                if (verbose) {
                    context.message("Processing " + fullLayerPath + "...");
                }

                String candidate = SvnPath.tail(fullLayerPath);
                String branchPath = SvnPath.removeTail(fullLayerPath);

                final boolean[] flags = new boolean[]{
                    false, false, false, false
                };
                String candidatePath = SvnPath.append(branchPath, candidate);
                repository.getDir(candidatePath, revision, new SVNRepositoryAdapter.EntryHandler() {

                    @Override
                    public void accept(SvnEntry svnde) throws RadixSvnException {
                        if (svnde.getKind() == SvnEntry.Kind.DIRECTORY) {
                            if (ERepositorySegmentType.KERNEL.getValue().equals(svnde.getName())) {
                                flags[0] = true;
                            } else if (ERepositorySegmentType.DDS.getValue().equals(svnde.getName())) {
                                flags[1] = true;
                            } else if (ERepositorySegmentType.ADS.getValue().equals(svnde.getName())) {
                                flags[2] = true;
                            }
                        } else {
                            if (Layer.LAYER_XML_FILE_NAME.equals(svnde.getName())) {
                                flags[3] = true;
                            }
                        }
                    }
                });
                if (flags[3]) {//is layer
                    String layerPath = candidate + "/";

                    //DEBUG STRING. COMMENT
//                    if (layerPath.contains("multicarta") || layerPath.contains("demo") ) {
//                        continue;
//                    }
                    if (isFileRemovedFromBranch(removedFiles, layerPath)) {
                        continue;
                    }

                    layerSpecificKernelJars.clear();

                    String layerSpecificKernelPrefix = candidate + "/kernel/";
                    for (String kernelJar : allKernelJars) {
                        if (kernelJar.startsWith(layerSpecificKernelPrefix)) {
                            layerSpecificKernelJars.add(kernelJar);
                        }
                    }

                    String layerXmlLocalPath = SvnPath.append(layerPath, Layer.LAYER_XML_FILE_NAME);
                    InputStream in;
                    String layerXmlDisplayName;
                    if (addedAndModifiedLayerDescriptions.contains(layerXmlLocalPath)) {//load layer.xml from zip file
                        if (useZipFile == null) {
                            useZipFile = getZipFile(context, zipFile, zipFilePath);
                            if (useZipFile == null) {
                                context.error("Unable to open upgrade file: " + String.valueOf(zipFilePath));
                                return false;
                            }
                        }
                        ZipEntry e = useZipFile.getEntry("files/" + layerXmlLocalPath);
                        try {
                            in = useZipFile.getInputStream(e);
                        } catch (IOException ex) {
                            context.error(ex);
                            return false;
                        }
                        layerXmlDisplayName = useZipFile.getName() + ":" + "files/" + layerXmlLocalPath;
                    } else {
                        String layerXmlPath = SvnPath.append(candidatePath, Layer.LAYER_XML_FILE_NAME);
                        byte[] bytes = SVN.getFile(repository, layerXmlPath, revision);
                        in = new ByteArrayInputStream(bytes);
                        layerXmlDisplayName = layerXmlPath;
                    }
                    try {
                        LayerDocument xDoc = LayerDocument.Factory.parse(in);
                        org.radixware.schemas.product.Layer xDef = xDoc.getLayer();
                        List<String> prevLayers = new LinkedList<>();
                        if (xDef.isSetPrevLayerUri()) {
                            prevLayers.add(xDef.getPrevLayerUri());
                        } else if (xDef.isSetBaseLayerURIs()) {
                            prevLayers.addAll(xDef.getBaseLayerURIs());
                        }

                        LayerInfo info = new LayerInfo(xDef.getName(), xDef.getUri(), prevLayers, flags[0], flags[1], flags[2], removedFiles, upgradeFile, upgradeFilePath, layerSpecificKernelJars);
                        info.repository = repository;
                        info.branchPath = branchPath;
                        info.revision = revision;
                        infos.add(info);
                        if (verbose && context != null) {
                            context.message("Layer found: " + info.name + " (" + info.uri + ")");
                        }
                    } catch (XmlException ex) {
                        if (context != null) {
                            context.error(new RadixError("Invalid format of layer description file " + layerXmlDisplayName, ex));
                        }
                    } catch (IOException ex) {
                        if (context != null) {
                            context.error(new RadixError("Inable to load layer description file " + layerXmlDisplayName, ex));
                        }
                    } finally {
                        try {
                            in.close();
                        } catch (IOException ex) {
                            //
                        }
                    }
                }
            }
            for (LayerInfo info : infos) {
                if (info.prevLayerUris != null && !info.prevLayerUris.isEmpty()) {

                    for (LayerInfo info2 : infos) {
                        if (info2 == info) {
                            continue;
                        } else if (info.prevLayerUris.contains(info2.uri)) {
                            info.prevLayers.add(info2);
                            break;
                        }
                    }
                }
            }
            if (skipDevelopmentLayers && baseDevelopmentLayerURI != null) {
                List<LayerInfo> toRemove = new LinkedList<LayerInfo>();
                for (LayerInfo info : infos) {
                    if (org.radixware.kernel.common.utils.Utils.equals(info.uri, baseDevelopmentLayerURI) || isHigherThanDev(info, baseDevelopmentLayerURI)) {
                        toRemove.add(info);
                    }
                }
                for (LayerInfo info : toRemove) {
                    if (verbose && context != null) {
                        context.message("Layer " + info.getDisplayName() + " is looks like development layer. Skipped");
                        devLayers.add(info.getURI());
                    }
                    infos.remove(info);
                }
            }
            return true;
        } catch (RadixSvnException e) {
            if (context != null) {
                context.error(new RadixError("Unable to obtain layer list", e));
            }
            return false;
        }
    }

    static boolean listLayers(boolean verbose, final SVNRepositoryAdapter repository, final long revision, final String branchPath, String baseDevLayerURI, boolean skipDevelopmentLayers, NoizyVerifier context, List<LayerInfo> infos, List<String> removedFiles, ZipFile upgradeFile, String upgradeFilePath, List<String> allKernelJars, List<String> devLayers, List<String> addedAndModifiedLayerDescriptions, ZipFile zipFile, String zipFilePath) {
        try {
            String baseDevelopmentLayerURI = null;
            ZipFile useZipFile = null;

            if (skipDevelopmentLayers) {
                if (baseDevLayerURI == NO_BASE_DEVELOPMENT_LAYER_SPECIFIED) {
                    ByteArrayInputStream in = null;
                    try {
                        byte[] bytes = SVN.getFile(repository, SvnPath.append(branchPath, Branch.BRANCH_XML_FILE_NAME), revision);
                        in = new ByteArrayInputStream(bytes);
                        BranchDocument xDoc = BranchDocument.Factory.parse(in);
                        baseDevelopmentLayerURI = xDoc.getBranch().getBaseDevUri();
                    } catch (Exception ex) {
                        if (context != null) {
                            context.error("Unable to obtain base  development layer URI of branch " + branchPath);
                            return false;
                        }
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException ex) {
                            }
                        }
                    }
                } else {
                    baseDevelopmentLayerURI = baseDevLayerURI;
                }
            }
            final List<String> candidates = new LinkedList<>();
            repository.getDir(branchPath, revision, new SVNRepositoryAdapter.EntryHandler() {

                @Override
                public void accept(SvnEntry entry) throws RadixSvnException {
                    if (entry.getKind() == SvnEntry.Kind.DIRECTORY) {
                        candidates.add(entry.getName());
                    }
                }
            });
            final List<String> layerSpecificKernelJars = new LinkedList<>();

            for (String candidate : candidates) {
                final boolean[] flags = new boolean[]{
                    false, false, false, false
                };
                String candidatePath = SvnPath.append(branchPath, candidate);
                repository.getDir(candidatePath, revision, new SVNRepositoryAdapter.EntryHandler() {
                    @Override
                    public void accept(SvnEntry svnde) throws RadixSvnException {
                        if (svnde.getKind() == SvnEntry.Kind.DIRECTORY) {
                            if (ERepositorySegmentType.KERNEL.getValue().equals(svnde.getName())) {
                                flags[0] = true;
                            } else if (ERepositorySegmentType.DDS.getValue().equals(svnde.getName())) {
                                flags[1] = true;
                            } else if (ERepositorySegmentType.ADS.getValue().equals(svnde.getName())) {
                                flags[2] = true;
                            }
                        } else {
                            if (Layer.LAYER_XML_FILE_NAME.equals(svnde.getName())) {
                                flags[3] = true;
                            }
                        }
                    }
                });
                if (flags[3]) {//is layer
                    String layerPath = candidate + "/";

                    //DEBUG STRING. COMMENT
//                    if (layerPath.contains("multicarta") || layerPath.contains("demo") ) {
//                        continue;
//                    }
                    if (isFileRemovedFromBranch(removedFiles, layerPath)) {
                        continue;
                    }

                    layerSpecificKernelJars.clear();

                    String layerSpecificKernelPrefix = candidate + "/kernel/";
                    for (String kernelJar : allKernelJars) {
                        if (kernelJar.startsWith(layerSpecificKernelPrefix)) {
                            layerSpecificKernelJars.add(kernelJar);
                        }
                    }

                    String layerXmlLocalPath = SvnPath.append(layerPath, Layer.LAYER_XML_FILE_NAME);
                    InputStream in;
                    String layerXmlDisplayName;
                    if (addedAndModifiedLayerDescriptions.contains(layerXmlLocalPath)) {//load layer.xml from zip file
                        if (useZipFile == null) {
                            useZipFile = getZipFile(context, zipFile, zipFilePath);
                            if (useZipFile == null) {
                                context.error("Unable to open upgrade file: " + String.valueOf(zipFilePath));
                                return false;
                            }
                        }
                        ZipEntry e = useZipFile.getEntry("files/" + layerXmlLocalPath);
                        try {
                            in = useZipFile.getInputStream(e);
                        } catch (IOException ex) {
                            context.error(ex);
                            return false;
                        }
                        layerXmlDisplayName = useZipFile.getName() + ":" + "files/" + layerXmlLocalPath;
                    } else {
                        String layerXmlPath = SvnPath.append(candidatePath, Layer.LAYER_XML_FILE_NAME);
                        byte[] bytes = SVN.getFile(repository, layerXmlPath, revision);
                        in = new ByteArrayInputStream(bytes);
                        layerXmlDisplayName = layerXmlPath;
                    }
                    try {
                        LayerDocument xDoc = LayerDocument.Factory.parse(in);
                        org.radixware.schemas.product.Layer xDef = xDoc.getLayer();
                        List<String> prevLayers = new LinkedList<>();
                        if (xDef.isSetPrevLayerUri()) {
                            prevLayers.add(xDef.getPrevLayerUri());
                        } else if (xDef.isSetBaseLayerURIs()) {
                            prevLayers.addAll(xDef.getBaseLayerURIs());
                        }

                        LayerInfo info = new LayerInfo(xDef.getName(), xDef.getUri(), prevLayers, flags[0], flags[1], flags[2], removedFiles, upgradeFile, upgradeFilePath, layerSpecificKernelJars);
                        info.repository = repository;
                        info.branchPath = branchPath;
                        info.revision = revision;
                        infos.add(info);
                        if (verbose && context != null) {
                            context.message("Layer found: " + info.name + " (" + info.uri + ")");
                        }
                    } catch (XmlException ex) {
                        if (context != null) {
                            context.error(new RadixError("Invalid format of layer description file " + layerXmlDisplayName, ex));
                        }
                    } catch (IOException ex) {
                        if (context != null) {
                            context.error(new RadixError("Inable to load layer description file " + layerXmlDisplayName, ex));
                        }
                    } finally {
                        try {
                            in.close();
                        } catch (IOException ex) {
                            //
                        }
                    }
                }
            }
            for (LayerInfo info : infos) {
                if (info.prevLayerUris != null && !info.prevLayerUris.isEmpty()) {

                    for (LayerInfo info2 : infos) {
                        if (info2 == info) {
                            continue;
                        } else if (info.prevLayerUris.contains(info2.uri)) {
                            info.prevLayers.add(info2);
                            break;
                        }
                    }
                }
            }
            if (skipDevelopmentLayers && baseDevelopmentLayerURI != null) {
                List<LayerInfo> toRemove = new LinkedList<LayerInfo>();
                for (LayerInfo info : infos) {
                    if (org.radixware.kernel.common.utils.Utils.equals(info.uri, baseDevelopmentLayerURI) || isHigherThanDev(info, baseDevelopmentLayerURI)) {
                        toRemove.add(info);
                    }
                }
                for (LayerInfo info : toRemove) {
                    if (verbose && context != null) {
                        context.message("Layer " + info.getDisplayName() + " is looks like development layer. Skipped");
                        devLayers.add(info.getURI());
                    }
                    infos.remove(info);
                }
            }
            return true;
        } catch (RadixSvnException e) {
            if (context != null) {
                context.error(new RadixError("Unable to obtain layer list from " + branchPath, e));
            }
            return false;
        }
    }

    private static boolean isHigherThanDev(ClassLinkageAnalyzer.LayerInfo info, String baseDevLayer) {
        List<ClassLinkageAnalyzer.LayerInfo> prevs = info.findPrevLayer();
        for (ClassLinkageAnalyzer.LayerInfo prev : prevs) {
            if (org.radixware.kernel.common.utils.Utils.equals(prev.getURI(), baseDevLayer)) {
                return true;
            }
        }
        for (ClassLinkageAnalyzer.LayerInfo prev : prevs) {
            if (isHigherThanDev(prev, baseDevLayer)) {
                return true;
            }
        }
        return false;
    }

    static String getModulePathInBranch(String layerUri, String moduleName) {
        return layerUri + "/ads/" + moduleName + "/";
    }

    static boolean uploadModules(boolean verbose, LayerInfo info, NoizyVerifier context, List<ZipModule> zipModules, final List<String> removedFiles) {
        info.modules = new HashMap<>();
        String adsPath = SvnPath.append(SvnPath.append(info.branchPath, info.uri), "ads");
        final List<String> candidates = new LinkedList<String>();
        try {
            if (info.repository.checkPath(adsPath, info.revision) == SvnEntry.Kind.DIRECTORY) {
                info.repository.getDir(adsPath, info.revision, new SVNRepositoryAdapter.EntryHandler() {
                    @Override
                    public void accept(SvnEntry svnde) throws RadixSvnException {
                        if (svnde.getKind() == SvnEntry.Kind.DIRECTORY) {
                            candidates.add(svnde.getName());
                        }
                    }
                });

                List<String> filesDeletedFromModule = new LinkedList<String>();

                for (String candidate : candidates) {

                    String path = SvnPath.append(SvnPath.append(adsPath, candidate), Module.MODULE_XML_FILE_NAME);
                    final byte[] bytes;
                    try {
                        bytes = SVN.getFile(info.repository, path, info.revision);
                    } catch (RadixSvnException ex) {
                        //Missing module description file. not a module. if this really was a module, unresolved link will appear on next stages
                        continue;
                    }
                    if (bytes != null) {
                        try {
                            ModuleDocument xDoc = ModuleDocument.Factory.parse(new ByteArrayInputStream(bytes));
                            org.radixware.schemas.product.Module xDef = xDoc.getModule();

                            Id id = Id.Factory.loadFrom(xDef.getId());
                            Id companionId = xDef.isSetCompanionId() ? xDef.getCompanionId() : null;
                            ModuleInfo module = findModuleInZipFile(info, id, companionId, xDef.getName(), zipModules);
                            if (module == null) {
                                module = new ModuleInfo(info, id, companionId, candidate);
                                if (verbose && context != null) {
                                    context.message("Module found: " + module.getDisplayName() + " (repository module)");
                                }
                            } else {

                                if (verbose && context != null) {
                                    if (!org.radixware.kernel.common.utils.Utils.equals(module.name, candidate)) {
                                        context.message("Module found: " + module.getDisplayName() + " (upgrade module (module rename detected, old name - " + candidate + "))");
                                    } else {
                                        context.message("Module found: " + module.getDisplayName() + " (upgrade module)");
                                    }

                                }
                            }
                            filesDeletedFromModule.clear();
                            if (!isModuleRemoved(info.uri, module.name, removedFiles, filesDeletedFromModule)) {
                                module.repository = info.repository;
                                module.branchPath = info.branchPath;
                                module.revision = info.revision;
                                if (!filesDeletedFromModule.isEmpty()) {
                                    module.removedFiles = new LinkedList<String>(filesDeletedFromModule);
                                }
                                info.modules.put(module.getId(), module);
                            }
                        } catch (XmlException ex) {
                            if (context != null) {
                                context.error(new RadixError("Invalid module description file format at " + candidate + "/module.xml", ex));
                            }
                            return false;
                        } catch (IOException ex) {
                            if (context != null) {
                                context.error(new RadixError("Unable to read module description file from " + candidate, ex));
                            }
                            return false;
                        }
                    }

                }
                List<ModuleInfo> newModules = listModulesFromZipFileExceptGivenSet(info, zipModules, info.modules.keySet());
                for (ModuleInfo module : newModules) {
                    if (verbose && context != null) {
                        context.message("Module found: " + module.getDisplayName() + " (upgrade module,newly added)");
                    }
                    filesDeletedFromModule.clear();
                    if (!isModuleRemoved(info.uri, module.name, removedFiles, filesDeletedFromModule)) {
                        module.repository = info.repository;
                        module.branchPath = info.branchPath;
                        module.revision = info.revision;
                        if (!filesDeletedFromModule.isEmpty()) {
                            module.removedFiles = new LinkedList<String>(filesDeletedFromModule);
                        }
                        info.modules.put(module.getId(), module);
                    }
                }
            }
        } catch (RadixSvnException ex) {
            if (context != null) {
                context.error(new RadixError("Unable to obtain modules list ", ex));
            }
            return false;
        }
        return true;
    }

    static class ZipModule {

        Id id;
        Id companionId;
        String name;
        String layerUri;
        ZipFile zipFile;
        String zipFilePath;
        boolean isPatch;
        boolean descriptionInZip = false;

        public ZipModule(Id id, Id companionId, String name, String layerUri, ZipFile zipFile, String zipFilePath, boolean isPatch) {
            this.id = id;
            this.companionId = companionId;
            this.name = name;
            this.layerUri = layerUri;
            this.zipFile = zipFile;
            this.zipFilePath = zipFilePath;
            this.isPatch = isPatch;
        }
    }

    private static boolean isModuleRemoved(String layerUri, String moduleName, List<String> allRemovedFiles, List<String> removedEntries) {
        final String modulePathInBranch = layerUri + "/ads/" + moduleName + "/";
        final int modulePathLength = modulePathInBranch.length();
        for (String removedFile : allRemovedFiles) {
            if (removedFile.startsWith(modulePathInBranch)) {
                if (removedFile.equals(modulePathInBranch)) {
                    return true;
                } else {
                    removedEntries.add(removedFile.substring(modulePathLength));
                }
            }
        }
        return false;
    }

    private static boolean isKernelJarPath(String path) {
        if (!path.endsWith(".jar")) {
            return false;
        }
        String[] names = path.split("/");
        if (names.length < 5) {
            return false;
        }
        if (!"kernel".equals(names[1])) {
            return false;
        }
        if (!"bin".equals(names[3]) && !"lib".equals(names[3])) {
            return false;
        }
        return true;
    }

    private static boolean isLayerDescriptionFile(String path) {
        if (path.endsWith("layer.xml")) {
            String[] names = path.split("/");
            if (names.length != 2) {
                return false;
            } else {
                return names[1].equals("layer.xml");
            }
        } else {
            return false;
        }
    }

    private static ZipFile getZipFile(NoizyVerifier context, ZipFile zipFile, String zipFilePath) {
        ZipFile tmpZipFile = zipFile;

        if (tmpZipFile == null) {
            if (zipFilePath == null) {//
                context.message("No upgrade file specified. Skipping injections");
                return null;
            }
            try {
                return new ZipFile(zipFilePath);
            } catch (IOException ex) {
                return null;
            }
        } else {
            return zipFile;
        }

    }

    static boolean uploadModulesFromZipFile(boolean verbose, NoizyVerifier context, ZipFile zipFile, String zipFilePath, List<ZipModule> zipModules, List<String> removedFiles, List<String> addedAndModifiedKernelJars, List<String> addedAndModifiedLayerDescriptions) {
        if (verbose && context != null) {
            context.message("Reading upgrade package...");
        }
        ZipFile tmpZipFile = zipFile;
        try {
            if (tmpZipFile == null) {
                if (zipFilePath == null) {//
                    if (context != null) {
                        context.message("No upgrade file specified. Skipping injections");
                    }
                    return true;
                }
                tmpZipFile = new ZipFile(zipFilePath);
            }

            ZipEntry e = tmpZipFile.getEntry("upgrade.xml");
            boolean isNormalUpgrade = true;
            if (e == null) {
                e = tmpZipFile.getEntry("patch.xml");
                if (e == null) {
                    if (context != null) {
                        context.error(tmpZipFile.getName() + " does not seems like valid upgrade or patch file");
                    }
                    return false;
                } else {
                    isNormalUpgrade = false;
                }
            } else {
                InputStream stream = tmpZipFile.getInputStream(e);
                try {
                    try {
                        UpgradeDocument xDoc = UpgradeDocument.Factory.parse(stream);
                        Upgrade xUpgrade = xDoc.getUpgrade();
                        for (Remove xRemove : xUpgrade.getFiles().getRemoveList()) {
                            removedFiles.add(xRemove.getName());
                        }
                        for (Add xAdd : xUpgrade.getFiles().getAddList()) {
                            String path = xAdd.getName();
                            if (isKernelJarPath(path)) {
                                addedAndModifiedKernelJars.add(path);
                            }
                            if (isLayerDescriptionFile(path)) {
                                addedAndModifiedLayerDescriptions.add(path);
                            }
                        }
                        for (Modify xModify : xUpgrade.getFiles().getModifyList()) {
                            String path = xModify.getName();
                            if (isKernelJarPath(path)) {
                                addedAndModifiedKernelJars.add(path);
                            }
                            if (isLayerDescriptionFile(path)) {
                                addedAndModifiedLayerDescriptions.add(path);
                            }
                        }
                    } catch (XmlException ex) {
                        if (context != null) {
                            context.error(new RadixError("Invalid format of upgrade.xml file in upgrade file " + tmpZipFile.getName(), ex));
                        }
                    }
                } finally {
                    stream.close();
                }
            }
            Enumeration<? extends ZipEntry> entries = tmpZipFile.entries();

            int adsNameIndex = isNormalUpgrade ? 2 : 1;
            int moduleNameIndex = adsNameIndex + 1;

            final Map<String, ZipModule> knownModules = new HashMap<String, ZipModule>();

            while (entries.hasMoreElements()) {
                e = entries.nextElement();
                if (e.getName().contains("/ads/") && !e.isDirectory()) {
                    String[] names = e.getName().split("/");
                    if (names.length > moduleNameIndex && names[adsNameIndex].equals("ads")) {
                        String layerUri = names[adsNameIndex - 1];
                        String moduleName = names[moduleNameIndex];
                        String moduleKey = layerUri + moduleName;

                        Id moduleId = null;
                        Id companionId = null;
                        boolean hasDescription = false;;
                        if (names.length == moduleNameIndex + 2 && names[moduleNameIndex + 1].equals(Module.MODULE_XML_FILE_NAME)) {
                            ModuleDocument xDoc;
                            try {
                                xDoc = ModuleDocument.Factory.parse(tmpZipFile.getInputStream(e));
                                org.radixware.schemas.product.Module xDef = xDoc.getModule();
                                moduleId = Id.Factory.loadFrom(xDef.getId());
                                companionId = xDef.isSetCompanionId() ? xDef.getCompanionId() : null;
                                hasDescription = true;
                            } catch (XmlException ex) {
                                if (context != null) {
                                    context.error(new RadixError("Invalid format of module.xml file in module  " + moduleName + " from layer " + layerUri + " from upgrade file " + tmpZipFile.getName()));
                                }
                                return false;
                            }
                        }
                        ZipModule ex = knownModules.get(moduleKey);
                        if (ex == null) {
                            ex = new ZipModule(moduleId, companionId, moduleName, layerUri, zipFile, zipFilePath, !isNormalUpgrade);
                            knownModules.put(moduleKey, ex);
                        } else if (moduleId != null) {
                            ex.id = moduleId;
                            if (ex.descriptionInZip || hasDescription) {
                                ex.companionId = companionId;
                            }
                        }
                        if (!ex.descriptionInZip) {
                            ex.descriptionInZip = hasDescription;
                        }

                    }
                }
            }
            zipModules.addAll(knownModules.values());

            return true;
        } catch (IOException ex) {
            if (context != null) {
                context.error(new RadixError("Unable to read modules from upgrade file", ex));
            }
            return false;
        } finally {
            if (zipFile == null) {
                if (tmpZipFile != null) {
                    try {
                        tmpZipFile.close();
                    } catch (IOException ex) {
                        //
                    }
                }
            }
        }
    }

    static boolean isFileRemovedFromModule(List<String> removedFiles, String pathInModule) {
        if (removedFiles != null) {
            boolean isDir = pathInModule.endsWith("/");
            String[] path = pathInModule.split("/");
            String matchName = "";

            for (int i = 0; i < path.length; i++) {
                matchName = matchName + path[i];
                if (isDir || i < path.length - 1) {
                    matchName += "/";
                }
                if (removedFiles.contains(matchName)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    static boolean isFileRemovedFromBranch(List<String> removedFiles, String pathInBranch) {
        if (removedFiles != null) {
            boolean isDir = pathInBranch.endsWith("/");
            String[] path = pathInBranch.split("/");
            String matchName = "";

            for (int i = 0; i < path.length; i++) {
                matchName = matchName + path[i];
                if (isDir || i < path.length - 1) {
                    matchName += "/";
                }
                if (removedFiles.contains(matchName)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }
    private static final byte[] REMOVED_DATA = new byte[0];

    private static class ZipModuleInfo extends org.radixware.kernel.common.svn.utils.Utils.ModuleInfo {

        private ZipFile zipFile;
        private String zipFilePath;
        private boolean isPatch;

        ZipModuleInfo(org.radixware.kernel.common.svn.utils.Utils.LayerInfo layer, ZipModule module, Id companionId) {
            this(layer, module, module.id, companionId);
        }

        ZipModuleInfo(org.radixware.kernel.common.svn.utils.Utils.LayerInfo layer, ZipModule module, Id id, Id companionId) {
            super(layer, id, module.descriptionInZip ? module.companionId : companionId, module.name);
            this.zipFile = module.zipFile;
            this.zipFilePath = module.zipFilePath;
            this.isPatch = module.isPatch;

        }

        private String getModuleEntryPath() {
            return isPatch ? layer.getURI() + "/ads/" + name + "/" : "files/" + layer.getURI() + "/ads/" + name + "/";
        }

        private byte[] findDataOfEntryInModuleRoot(String entryName) {
            if (isFileRemovedFromModule(removedFiles, entryName)) {
                return REMOVED_DATA;
            }
            String entryPath = getModuleEntryPath() + entryName;
            ZipFile zip = this.zipFile;
            try {
                if (zip == null) {
                    zip = new ZipFile(zipFilePath);
                }
                Enumeration<? extends ZipEntry> entries = zip.entries();

                while (entries.hasMoreElements()) {
                    ZipEntry e = entries.nextElement();
                    if (entryPath.equals(e.getName())) {
                        return FileUtils.getZipEntryByteContent(e, zip);
                    }
                }
            } catch (IOException ex) {
            } finally {
                if (zipFile == null) {
                    if (zip != null) {
                        try {
                            zip.close();
                        } catch (IOException ex) {
                            //
                        }
                    }
                }
            }
            return null;
        }

        @Override
        public byte[] findDefinitionsIndexData() {
            byte[] result = findDataOfEntryInModuleRoot("definitions.xml");
            if (result != null) {
                if (result == REMOVED_DATA) {
                    return null;
                }
                return result;
            }
            return super.findDefinitionsIndexData();
        }
        
        
        @Override
        public byte[] findDirectoryIndexData() {
            byte[] result = findDataOfEntryInModuleRoot(FileNames.DIRECTORY_XML);
            if (result != null) {
                if (result == REMOVED_DATA) {
                    return null;
                }
                return result;
            }
            return super.findDirectoryIndexData();
        }
         

        @Override
        byte[] getAPIXmlData() {
            byte[] result = findDataOfEntryInModuleRoot("api.xml");
            if (result != null) {
                if (result == REMOVED_DATA) {
                    return null;
                }
                return result;
            }
            return super.getAPIXmlData();
        }

        @Override
        byte[] getUsagesXmlData() {
            byte[] result = findDataOfEntryInModuleRoot("usages.xml");
            if (result != null) {
                if (result == REMOVED_DATA) {
                    return null;
                }
                return result;
            }
            return super.getUsagesXmlData();
        }

        @Override
        protected File checkOutBinaryFile(ERuntimeEnvironmentType env) {
            String entryName = "bin/" + env.getValue() + ".jar";

            if (isFileRemovedFromModule(removedFiles, entryName)) {
                return null;
            }
            String entryPath = getModuleEntryPath() + entryName;
            ZipFile zip = zipFile;
            try {
                if (zip == null) {
                    zip = new ZipFile(zipFilePath);
                }
                Enumeration<? extends ZipEntry> entries = zip.entries();

                while (entries.hasMoreElements()) {
                    ZipEntry e = entries.nextElement();
                    if (entryPath.equals(e.getName())) {
                        InputStream in = null;
                        FileOutputStream out = null;
                        try {
                            in = zip.getInputStream(e);
                            File tmp = null;
                            try {
                                tmp = File.createTempFile("rdx5", "rdx5");
                                out = new FileOutputStream(tmp);
                                FileUtils.copyStream(in, out);
                            } catch (Exception ex) {
                                if (tmp != null) {
                                    FileUtils.deleteFile(tmp);
                                }
                            }
                            return tmp;
                        } finally {
                            if (in != null) {
                                in.close();
                            }
                            if (out != null) {
                                out.close();
                            }
                        }
                    }
                }
            } catch (IOException ex) {
            } finally {
                if (zipFile == null) {
                    if (zip != null) {
                        try {
                            zip.close();
                        } catch (IOException ex) {
                            //
                        }
                    }
                }
            }
            return super.checkOutBinaryFile(env);
        }
    }

    private static List<ModuleInfo> listModulesFromZipFileExceptGivenSet(org.radixware.kernel.common.svn.utils.Utils.LayerInfo layer, List<ZipModule> zipModulesWithIds, Set<Id> foundModules) {
        List<ModuleInfo> result = new LinkedList<ModuleInfo>();
        for (ZipModule module : zipModulesWithIds) {
            if (org.radixware.kernel.common.utils.Utils.equals(module.layerUri, layer.getURI())) {
                if (module.id != null && !foundModules.contains(module.id)) {
                    result.add(new ZipModuleInfo(layer, module, module.companionId));
                }
            }
        }
        return result;
    }

    private static ZipModuleInfo findModuleInZipFile(org.radixware.kernel.common.svn.utils.Utils.LayerInfo layer, Id moduleId, Id companionId, String moduleName, List<ZipModule> zipModulesWithIds) {
        List<ZipModule> idlessModules = new LinkedList<ZipModule>();
        for (ZipModule module : zipModulesWithIds) {
            if (org.radixware.kernel.common.utils.Utils.equals(module.layerUri, layer.getURI())) {
                if (module.id != null) {
                    if (moduleId == module.id) {
                        return new ZipModuleInfo(layer, module, companionId);
                    }
                } else {
                    idlessModules.add(module);
                }
            }
        }
        for (ZipModule module : idlessModules) {
            if (org.radixware.kernel.common.utils.Utils.equals(module.name, moduleName)) {
                return new ZipModuleInfo(layer, module, moduleId, companionId);
            }
        }
        return null;
    }

    static List<LayerInfo> topLayers(Collection<LayerInfo> layers) {
        List<LayerInfo> topLayers = new LinkedList<>(layers);
        for (ClassLinkageAnalyzer.LayerInfo layer : layers) {
            List<ClassLinkageAnalyzer.LayerInfo> prevs = layer.findPrevLayer();
            if (prevs != null && !prevs.isEmpty()) {
                for (final ClassLinkageAnalyzer.LayerInfo prev : prevs) {
                    LayerInfo info = (LayerInfo) prev;
                    if (topLayers.contains(info)) {
                        topLayers.remove(info);
                    }
                }
            }
        }
        return topLayers;
    }
}
