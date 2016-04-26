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
package org.radixware.kernel.common.builder.release;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.build.directory.DirectoryFileSigner;
import org.radixware.kernel.common.builder.DirectoryFile;
import org.radixware.kernel.common.builder.DirectoryFile.AdsDirectoryReleaseData;
import org.radixware.kernel.common.builder.api.IProgressHandle;
import org.radixware.kernel.common.conventions.RadixdocConventions;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.SvnConnectionType;
import org.radixware.kernel.common.svn.client.SvnEntry;
import org.radixware.kernel.common.svn.client.SvnPath;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.schemas.product.*;

class SvnFileList {

    static final String ADS_DIRECTORY_XML = "ads/directory.xml";
    static final String ADS_SEGMENT = "ads";

    interface SVNFileVisitor {

        void visit(SvnFile file);

        void endVisit(SvnFile file);
    }

    static class ChangeList {

        public void modified(final String path) {
        }

        public void added(final String path) {
        }

        public void removed(final String path) {
        }
    }

    private static class DirectoryLoadInfo {

        final Directory xDef;
        final boolean canContinue;

        public DirectoryLoadInfo(Directory xDef, boolean canContinue) {
            this.xDef = xDef;
            this.canContinue = canContinue;
        }
    }

    private enum DirIndexProcessResult {

        OK,
        FAILURE,
        SKIP
    }

    private enum DirIndexHint {

        LAYER,
        SEGMENT,
        MODULE, NONE, MODULE_DIR;

        private DirIndexHint next() {
            switch (this) {
                case LAYER:
                    return SEGMENT;
                case SEGMENT:
                    return MODULE;
                case MODULE:
                    return MODULE_DIR;
                default:
                    return NONE;
            }
        }
    }

    private static class UnderConstructionChecker {

        Map<String, DirectoryFile.AdsDirectoryReleaseData> moduleData = new HashMap<>();
        boolean find = false;

        boolean isUnderConstruction(String moduleId) {
            final AdsDirectoryReleaseData data = moduleData.get(moduleId);
            return data != null && data.isUnderConstruction();
        }

        void addModule(Id moduleId, String name, boolean isUnderConstruction) {
            moduleData.put(moduleId.toString(), new AdsDirectoryReleaseData(name, moduleId, isUnderConstruction));

            if (isUnderConstruction) {
                find = true;
            }
        }

        DirectoryFile.AdsDirectoryReleaseData getModuleData(String moduleId) {
            return moduleData.get(moduleId);
        }

        boolean find() {
            return find;
        }
    }
    private final ReleaseFlow flow;
    private final SvnDir layerRoot;// = null;
    private final String layerSrcUrl;
    private final File srcDir;
    private final long srcRevision;
    private final String releaseUrl;
    private final String releaseSrcUrl;
    private final String releaseScriptsUrl;
    private final List<Id> patchModuleIds;
    private final Layer layer;
    private final ReleaseUtils.ReleaseInfo releaseInfo;
    private List<SvnDir> explicitFiles;
    private final UnderConstructionChecker constructionChecker;

    public SvnFileList(final ReleaseFlow flow, final String layerSrcUrl, final Layer layer, final long srcRevision, final String releaseUrl, final String releaseSrcUrl, final String releaseScriptsUrl, final ReleaseUtils.ReleaseInfo releaseInfo, List<Id> patchModuleIds) {
        this.flow = flow;
        this.layerRoot = new SvnDir(layerSrcUrl);
        this.layer = layer;
        this.layerSrcUrl = layerSrcUrl;
        this.srcDir = layer.getDirectory();
        this.srcRevision = srcRevision;
        this.releaseUrl = releaseUrl;
        this.releaseSrcUrl = releaseSrcUrl;
        this.releaseScriptsUrl = releaseScriptsUrl;
        this.releaseInfo = releaseInfo;
        this.patchModuleIds = patchModuleIds;
        this.constructionChecker = new UnderConstructionChecker();
    }

    /*
     *
     */
    private byte[] createAdsDirectoryXml() {
        final DirectoryFile directoryFile = DirectoryFile.Factory.createAdsDirectoryForRelease((AdsSegment) layer.getAds(), constructionChecker.moduleData.values());
        return directoryFile.getBytes();
    }

    private String getLayerUrlInPrevRelease() {
        return releaseInfo.prevReleaseUrl == null ? null : SvnPath.append(releaseInfo.prevReleaseUrl, srcDir.getName());
    }

    private String getLayerUrlInPrevReleaseSrc() {

        final String layerUrlInPrevRelease = getLayerUrlInPrevRelease();
        if (layerUrlInPrevRelease == null) {
            return null;
        }

        final int start = layerUrlInPrevRelease.indexOf("/");
        return "releases.src/" + layerUrlInPrevRelease.substring(start + 1);
    }

    /*
     *
     */
    private void registerPatchModule(SvnDir path, Id moduleId) {
        synchronized (this) {
            if (explicitFiles == null) {
                explicitFiles = new LinkedList<>();
            }
            explicitFiles.add(path);
        }
    }

    private boolean walkVersionedFiles(final SvnDir dir, File localDir, final String path, final DirIndexHint hint, final boolean inAds, List<String> distributableFiles) throws RadixSvnException {
        final List<SvnDir> dirs = new LinkedList<>();
        final List<SvnFile> modules = new LinkedList<>();

        if (hint == DirIndexHint.MODULE) {
            File dirIndex = new File(localDir, "directory.xml");
            if (dirIndex.exists()) {
                DirectoryLoadInfo loadInfo = loadDirectoryDesc(dirIndex, true);
                if (!loadInfo.canContinue) {
                    return false;
                }

                distributableFiles = new LinkedList<>();

                if (loadInfo.xDef.getFileGroups() != null && loadInfo.xDef.getFileGroups().getFileGroupList() != null) {
                    loop:
                    for (Directory.FileGroups.FileGroup fg : loadInfo.xDef.getFileGroups().getFileGroupList()) {
                        for (Directory.FileGroups.FileGroup.File f : fg.getFileList()) {
                            distributableFiles.add(f.getName());
                        }
                    }
                }

            } else {
                distributableFiles = null;
            }
        }
        final List<String> effectiveDirIndex = distributableFiles;
        final Map<SvnDir, SvnEntry> path2entry = new HashMap<>();
        flow.getRepository().getDir(path, srcRevision, new SVNRepositoryAdapter.EntryHandler() {
            @Override
            public void accept(SvnEntry svnde) throws RadixSvnException {
                if (svnde.getKind() != SvnEntry.Kind.DIRECTORY && svnde.getKind() != SvnEntry.Kind.FILE) {
                    //ignore entry
                    return;
                }

                boolean notDistributable = false;

                if (svnde.getKind() == SvnEntry.Kind.FILE && hint == DirIndexHint.MODULE_DIR && "lib".equals(dir.name) && effectiveDirIndex != null) {//only registered in index file third parties are allowed
                    String checkName = "lib/" + svnde.getName();
                    boolean found = false;
                    for (String f : effectiveDirIndex) {
                        if (checkName.equals(f)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        notDistributable = true;
                    }
                }

                final SvnFile file = dir.addFileLocal(svnde.getName(), svnde.getKind() == SvnEntry.Kind.DIRECTORY, false);
                if (notDistributable) {
                    file.forceDelete = true;
                }
                if (svnde.getKind() == SvnEntry.Kind.DIRECTORY) {
                    dirs.add((SvnDir) file);
                    path2entry.put((SvnDir) file, svnde);
                } else if (inAds && hint == DirIndexHint.MODULE && Module.MODULE_XML_FILE_NAME.equals(svnde.getName())) {
                    modules.add(file);
                }
            }
        });

        for (SvnFile module : modules) {
            final String moduleFileUrl = SvnPath.append(layerSrcUrl, module.getPath());
            org.radixware.schemas.product.Module moduleDesc = loadModuleDesc(moduleFileUrl);
            if (moduleDesc != null) {
                final Id moduleId = Id.Factory.loadFrom(moduleDesc.getId());
                final boolean isUnderConstruction = moduleDesc.isSetIsUnderConstruction() && moduleDesc.getIsUnderConstruction();
                constructionChecker.addModule(moduleId, moduleDesc.getName(), isUnderConstruction);
                module.parent.isUnderConstruction = isUnderConstruction;
                module.parent.moduleId = moduleId;
            }
        }

        boolean ads = inAds;
        if (hint == DirIndexHint.SEGMENT) {
            ads = ADS_SEGMENT.equals(dir.getPath());
        }
        
        for (SvnDir child : dirs) {
            final String newPath = SvnPath.append(path, child.getName());

            if (!walkVersionedFiles(child, new File(localDir, child.getName()), newPath, hint.next(), ads, effectiveDirIndex) /*SVN.isExists(flow.getRepository(), newPath, srcRevision)&& */) {
                return false;
            }

        }
        return true;
    }

    public boolean prepare() {
        try {
            if (layer.isReadOnly()) {
                flow.getSettings().getLogger().message("Layer " + layerSrcUrl + " is read only. Complete copy is scheduled");
            } else {
                String message = "Computing versioned files of layer " + layerSrcUrl + "...";
                flow.getSettings().getLogger().message(message);

                IProgressHandle handle = flow.getSettings().getBuildEnvironment().getBuildDisplayer().getProgressHandleFactory().createHandle(message);
                try {
                    handle.start();
                    if (!walkVersionedFiles(layerRoot, layer.getDirectory(), layerSrcUrl, DirIndexHint.LAYER, false, null)) {
                        return false;
                    }
                } finally {
                    handle.finish();
                }

                handle = flow.getSettings().getBuildEnvironment().getBuildDisplayer().getProgressHandleFactory().createHandle("Applying non-versioned content...");
                try {
                    handle.start();
                    if (releaseInfo.prevReleaseUrl != null) {
                        flow.getSettings().getLogger().message("Computing previous release state of layer " + layerSrcUrl);
                        if (!applyPrevReleaseBinaries(releaseInfo.prevReleaseUrl, releaseInfo.prevReleaseRevision)) {
                            return false;
                        }
                    }
                    flow.getSettings().getLogger().message("Computing changes in new release of layer " + layerSrcUrl);
                    if (!applyCurrentReleaseBinaries()) {
                        return false;
                    }
                    if (flow.getSettings().TEST_MODE) {
                        layerRoot.traverse(new SVNFileVisitor() {
                            @Override
                            public void visit(SvnFile file) {
                            }

                            @Override
                            public void endVisit(SvnFile file) {
                                if (file.external) {
                                    System.out.print("E ");
                                    if (file.sourceFile != null || file.sourceBytes != null) {
                                        if (file.sourceFile != null) {
                                            System.out.print("<local>    ");
                                        } else {
                                            System.out.print("<virtual>  ");
                                        }
                                    } else {
                                        if (file.doNotDeleteExternal) {
                                            System.out.print("<remote>   ");
                                        } else {
                                            System.out.print("<delete>   ");
                                        }
                                    }
                                } else {
                                    System.out.print("R          ");
                                }
                                System.out.println(file.getPath());
                            }
                        });
                    }
                    if (!layer.isLocalizing()) {
                        if (!computeScriptOperations()) {
                            return false;
                        }
                    }
                } finally {
                    handle.finish();
                }
            }
        } catch (RadixSvnException ex) {
            return flow.getSettings().getLogger().fatal(ex);
        }
        return true;
    }

    public SvnDir getLayerRoot() {
        return layerRoot;
    }

    private org.radixware.schemas.product.Module loadModuleDesc(String svnUrl) {
        byte[] bytes = flow.getRepository().getFileBytes(svnUrl, -1);
        try {
            if (bytes != null) {
                ModuleDocument xDoc = ModuleDocument.Factory.parse(new ByteArrayInputStream(bytes));
                if (xDoc.getModule() == null) {
                    throw new RadixError("No module entry found");
                } else {
                    return xDoc.getModule();
                }
            } else {
                throw new RadixError("");
            }
        } catch (XmlException | IOException | RadixError e) {
            flow.getSettings().getLogger().error("Unable to load module description file " + svnUrl);
            return null;
        }
    }

    private org.radixware.schemas.product.Module loadLocalModuleDesc(File file) {
        try {

            ModuleDocument xDoc = ModuleDocument.Factory.parse(new FileInputStream(file));
            if (xDoc.getModule() == null) {
                throw new RadixError("No module entry found");
            } else {
                return xDoc.getModule();
            }

        } catch (XmlException | IOException | RadixError e) {
            flow.getSettings().getLogger().error("Unable to load module description file " + file.getPath());
            return null;
        }
    }

    private DirectoryLoadInfo loadDirectoryDesc(String svnUrl) {
        byte[] bytes = flow.getRepository().getFileBytes(svnUrl, -1);
        try {
            if (bytes != null) {
                DirectoryDocument xDoc = DirectoryDocument.Factory.parse(new ByteArrayInputStream(bytes));
                if (xDoc.getDirectory() == null) {
                    throw new RadixError("No directory entry found");
                } else {
                    return new DirectoryLoadInfo(xDoc.getDirectory(), true);
                }
            } else {
                throw new RadixError("");
            }
        } catch (XmlException | IOException | RadixError ex) {
            flow.getSettings().getLogger().error("Unable to load directory index file " + svnUrl);
            return new DirectoryLoadInfo(null, true);
        }
    }

    /*
     * Prev release processing
     */
    public boolean applyPrevReleaseBinaries(final String prevReleaseUrl, final long latestRevision) {
        final String prevReleaseLayerUrl = SvnPath.append(prevReleaseUrl, srcDir.getName());
        final String layerIndexFileUrl = SvnPath.append(prevReleaseLayerUrl, FileUtils.DIRECTORY_XML_FILE_NAME);
        final DirectoryLoadInfo info = loadDirectoryDesc(layerIndexFileUrl);
        if (info.canContinue && info.xDef != null) {
            return processRemoteDirectoryIndex(info.xDef, "", latestRevision, prevReleaseLayerUrl, DirIndexHint.LAYER) != DirIndexProcessResult.FAILURE;
        } else {
            return true;
        }
    }

    private DirIndexProcessResult processRemoteDirectoryIndex(final Directory xDef, final String directoryIndexParentPath, final long latestRevision, final String prevReleaseUrl, DirIndexHint hint) {
        Directory.FileGroups.FileGroup.File xModuleDesc = null;
        List<Directory.FileGroups.FileGroup.File> xFiles = new LinkedList<>();
        if (xDef.getFileGroups() != null) {
            for (Directory.FileGroups.FileGroup xGroup : xDef.getFileGroups().getFileGroupList()) {
                for (Directory.FileGroups.FileGroup.File xFile : xGroup.getFileList()) {
                    xFiles.add(xFile);
                    if (Utils.equals(xFile.getName(), Module.MODULE_XML_FILE_NAME)) {
                        xModuleDesc = xFile;
                    }
                }
            }
        }
        if (hint == DirIndexHint.MODULE) {
            if (xModuleDesc == null) {
                if (flow.isPatchRelease()) {
                    return DirIndexProcessResult.FAILURE;
                }
            } else {
                final String moduleFilePath = SvnPath.append(directoryIndexParentPath, xModuleDesc.getName());
                final String moduleFileUrl = SvnPath.append(prevReleaseUrl, moduleFilePath);
                org.radixware.schemas.product.Module xModule = loadModuleDesc(moduleFileUrl);

                if (xModule == null) {
                    return DirIndexProcessResult.FAILURE;
                }

                if (flow.isPatchRelease()) {
                    if (!this.patchModuleIds.contains(Id.Factory.loadFrom(xModule.getId()))) {//skip module
                        return DirIndexProcessResult.SKIP;
                    }
                } else {
                    if (constructionChecker.isUnderConstruction(xModule.getId())) {
                        processRemoteUnderConstructionModule(xModule.getId(), directoryIndexParentPath, latestRevision);
                    }
                }
            }
        } else if (hint == DirIndexHint.SEGMENT && flow.isPatchRelease()) {
            if (!directoryIndexParentPath.endsWith(ERepositorySegmentType.ADS.getValue())) {//only ads should present in patch
                return DirIndexProcessResult.SKIP;
            }
        }
        for (Directory.FileGroups.FileGroup.File xFile : xFiles) {
            if (!processRemoteEntry(xFile, directoryIndexParentPath, latestRevision, hint)) {
                return DirIndexProcessResult.FAILURE;
            }
        }

        if (xDef.getIncludes() != null) {
            for (Directory.Includes.Include xInc : xDef.getIncludes().getIncludeList()) {
                if (!processRemoteInclude(xInc, directoryIndexParentPath, latestRevision, prevReleaseUrl, hint)) {
                    return DirIndexProcessResult.FAILURE;
                }
            }
        }

        if (!processRemoteRadixdoc(RadixdocConventions.RADIXDOC_ZIP_FILE, directoryIndexParentPath, prevReleaseUrl)) {
            return DirIndexProcessResult.FAILURE;
        }

        return DirIndexProcessResult.OK;
    }

    private boolean processRemoteUnderConstructionModule(String moduleId, final String modulePath, final long latestRevision) {
        SvnFile existingFile = layerRoot.findChild(modulePath);
        if (existingFile == null) {
            return false;
        }
        constructionChecker.getModuleData(moduleId).setPrevReleaseName(existingFile.getName());
        existingFile.doNotDeleteExternal = existingFile.external;
        existingFile.isUnderConstruction = true;
        existingFile.fromPrev = true;
        return true;
    }

    private boolean processRemoteInclude(final Directory.Includes.Include xDef, final String directoryIndexPathParent, final long latestRevision, final String prevReleaseUrl, DirIndexHint hint) {
        final String indexFilePath = SvnPath.append(directoryIndexPathParent, xDef.getFileName());
        final String indexFileUrl = SvnPath.append(prevReleaseUrl, indexFilePath);
        final DirectoryLoadInfo info = loadDirectoryDesc(indexFileUrl);

        if (info.xDef != null) {

            SvnFile existingFile = layerRoot.findChild(indexFilePath);
            if (existingFile == null) {
                existingFile = layerRoot.addFile(indexFilePath, false, flow.getSettings().getLogger(), true);
                if (existingFile == null) {
                    return false;
                }
                existingFile.external = true;
                existingFile.setExternalRevisionNumber(latestRevision);
            }
            DirIndexProcessResult result = processRemoteDirectoryIndex(info.xDef, existingFile.parent.getPath(), latestRevision, prevReleaseUrl, hint.next());
            if (result == DirIndexProcessResult.SKIP) {
                existingFile.parent.unregister();
            }
            return result != DirIndexProcessResult.FAILURE;
        } else {
            return info.canContinue;
        }
    }

    private boolean processRemoteEntry(final Directory.FileGroups.FileGroup.File xDef, final String directoryIndexParentPath, final long latestRevision, DirIndexHint hint) {

        final String entryPath = SvnPath.append(directoryIndexParentPath, xDef.getName());

        SvnFile existingFile = layerRoot.findChild(entryPath);
        if (existingFile != null) {
            //file now is under version control
            return true;
        } else {
            existingFile = layerRoot.addFile(entryPath, false, flow.getSettings().getLogger(), true);
            existingFile.remoteDigest = xDef.getDigest();
            existingFile.external = true;
            existingFile.setExternalRevisionNumber(latestRevision);
            return true;
        }
    }

    private DirectoryLoadInfo loadDirectoryDesc(final File file, final boolean continueOnError) {
        try {
            final DirectoryDocument xDoc = DirectoryDocument.Factory.parse(file);
            if (xDoc.getDirectory() == null) {
                throw new RadixError("No directory entry found");
            } else {
                return new DirectoryLoadInfo(xDoc.getDirectory(), true);
            }
        } catch (XmlException | IOException | RadixError ex) {
            boolean result = continueOnError;
            final String message = "Unable to parse directory index file: " + file.getAbsolutePath();
            if (continueOnError) {
                result = flow.getSettings().TEST_MODE || flow.getSettings().getLogger().recoverableError(message);
                if (flow.getSettings().TEST_MODE) {
                    flow.getSettings().getLogger().error(message);
                }
            } else {
                flow.getSettings().getLogger().fatal(new RadixError(message, ex));
            }
            return new DirectoryLoadInfo(null, result);
        }
    }

    /*
     * Local processing
     */
    public boolean applyCurrentReleaseBinaries() {
        File dirFile = new File(srcDir, FileUtils.DIRECTORY_XML_FILE_NAME);
        DirectoryLoadInfo info = loadDirectoryDesc(dirFile, false);
        if (info.canContinue && info.xDef != null) {
            return processLocalDirectoryIndex(layerRoot, info.xDef, "", dirFile.getParentFile(), DirIndexHint.LAYER) != DirIndexProcessResult.FAILURE;
        } else {
            return true;
        }
    }

    private DirIndexProcessResult processLocalDirectoryIndex(final SvnDir parent, final Directory xDef, final String directoryIndexParentPath, final File directoryFileParent, DirIndexHint hint) {
        Directory.FileGroups.FileGroup.File xModuleDesc = null;
        List<Directory.FileGroups.FileGroup.File> xFiles = new LinkedList<>();
        if (xDef.getFileGroups() != null) {
            for (Directory.FileGroups.FileGroup xGroup : xDef.getFileGroups().getFileGroupList()) {
                for (Directory.FileGroups.FileGroup.File xFile : xGroup.getFileList()) {
                    xFiles.add(xFile);
                    if (Utils.equals(xFile.getName(), Module.MODULE_XML_FILE_NAME)) {
                        xModuleDesc = xFile;
                    }
                }
            }
        }
        if (hint == DirIndexHint.MODULE && flow.isPatchRelease()) {
            if (xModuleDesc == null) {
                return DirIndexProcessResult.FAILURE;
            } else {
                org.radixware.schemas.product.Module xModule = loadLocalModuleDesc(new File(directoryFileParent, xModuleDesc.getName()));
                if (xModule == null) {
                    return DirIndexProcessResult.FAILURE;
                } else {
                    final Id moduleId = Id.Factory.loadFrom(xModule.getId());
                    if (!this.patchModuleIds.contains(moduleId)) {//skip module
                        return DirIndexProcessResult.SKIP;
                    } else {
                        registerPatchModule(parent, moduleId);
                    }
                }
            }
        } else if (hint == DirIndexHint.SEGMENT && flow.isPatchRelease()) {
            if (!Utils.equals(directoryFileParent.getName(), ERepositorySegmentType.ADS.getValue())) {//only ads should present in patch
                return DirIndexProcessResult.SKIP;
            }
        }
        for (Directory.FileGroups.FileGroup.File xFile : xFiles) {
            if (!processLocalEntry(xFile, directoryIndexParentPath, directoryFileParent)) {
                return DirIndexProcessResult.FAILURE;
            }
        }
        if (xDef.getIncludes() != null) {
            for (Directory.Includes.Include xInc : xDef.getIncludes().getIncludeList()) {
                if (!processLocalInclude(xInc, directoryIndexParentPath, directoryFileParent, hint)) {
                    return DirIndexProcessResult.FAILURE;
                }
            }
        }

        if (!processRadixdoc(RadixdocConventions.RADIXDOC_ZIP_FILE, directoryIndexParentPath, directoryFileParent)) {
            return DirIndexProcessResult.FAILURE;
        }

        return DirIndexProcessResult.OK;
    }

    private boolean processLocalInclude(final Directory.Includes.Include xDef, final String directoryIndexPath, final File directoryFileParent, DirIndexHint hint) {
        final String indexFilePath = SvnPath.append(directoryIndexPath, xDef.getFileName());
        File indexFile = new File(directoryFileParent, xDef.getFileName());

        DirectoryLoadInfo info = loadDirectoryDesc(indexFile, true);

        if (info.xDef != null) {

            SvnFile existingFile = layerRoot.findChild(indexFilePath);
            if (existingFile == null) {
                existingFile = layerRoot.addFile(indexFilePath, false, flow.getSettings().getLogger(), true);
                if (existingFile == null) {
                    return false;
                }
            }
            if (existingFile.external) {
                existingFile.sourceFile = indexFile;
                existingFile.setDoNotDeleteExternal(true);
            }
            if (existingFile.isUnderConstruction) {
                return true;
            }
            DirIndexProcessResult result = processLocalDirectoryIndex(existingFile.parent, info.xDef, existingFile.parent.getPath(), indexFile.getParentFile(), hint.next());
            if (result == DirIndexProcessResult.SKIP) {
                existingFile.parent.unregister();
            }
            return result != DirIndexProcessResult.FAILURE;
        } else {
            return info.canContinue;
        }
    }

    private boolean processLocalEntry(final Directory.FileGroups.FileGroup.File xDef, final String directoryIndexParentPath, final File directoryFileParent) {
        final File entryFile = new File(directoryFileParent, xDef.getName());

        if (!entryFile.exists()) {
            return flow.getSettings().getLogger().fatal("File " + entryFile.getAbsolutePath() + " referenced from " + new File(directoryFileParent, FileUtils.DIRECTORY_XML_FILE_NAME).getAbsolutePath() + " not found");
        }

        final String entryPath = SvnPath.append(directoryIndexParentPath, xDef.getName());
        SvnFile existingFile = layerRoot.findChild(entryPath);

        if (existingFile != null) {
            if (existingFile.external) {
                final byte[] digest = xDef.getDigest();
                if (digest == null) {//no digest
                    return flow.getSettings().getLogger().fatal("No digest for " + entryFile.getAbsolutePath() + " in " + new File(directoryFileParent, FileUtils.DIRECTORY_XML_FILE_NAME).getAbsolutePath());
                } else {
                    if (existingFile.remoteDigest == null || !Arrays.equals(digest, existingFile.remoteDigest)) { // FIXME: By KAV, temporary fix, definitions.xml not copied.
                        existingFile.localDigest = digest;
                        existingFile.sourceFile = entryFile;
                        existingFile.replaced = true;
                        existingFile.setDoNotDeleteExternal(true);
                    } else {
                        existingFile.setDoNotDeleteExternal(true);
                        // copy remote file
                    }
                    return true;
                }
            } else {
                //already under version control
                return true;
            }
        } else {
            existingFile = layerRoot.addFile(entryPath, false, flow.getSettings().getLogger(), true);
            existingFile.localDigest = xDef.getDigest();
            existingFile.sourceFile = entryFile;
            existingFile.setDoNotDeleteExternal(true);
            return true;
        }
    }

    private boolean processRadixdoc(final String radixdocFile, final String directoryIndexParentPath, final File directoryFileParent) {
        final File entryFile = new File(directoryFileParent, radixdocFile);

        if (!entryFile.exists()) {
            return true;
//            return flow.getSettings().getLogger().fatal("File " + entryFile.getAbsolutePath() + " referenced from " + new File(directoryFileParent, FileUtils.DIRECTORY_XML_FILE_NAME).getAbsolutePath() + " not found");
        }

        byte[] ownDigest = null;
        try {
            ownDigest = DirectoryFileSigner.calcFileDigest(entryFile, true);
        } catch (IOException | NoSuchAlgorithmException ex) {
            return true;
        }

        final String entryPath = SvnPath.append(directoryIndexParentPath, radixdocFile);
        SvnFile existingFile = layerRoot.findChild(entryPath);

        if (existingFile != null) {
            if (existingFile.external) {
                byte[] digest = existingFile.remoteDigest;
                if (digest == null || !Arrays.equals(digest, ownDigest)) {
                    existingFile.localDigest = ownDigest;
                    existingFile.sourceFile = entryFile;
                    existingFile.replaced = true;
                    existingFile.setDoNotDeleteExternal(true);
                } else {
                    existingFile.setDoNotDeleteExternal(true);
                }
                return true;
            } else {
                //already under version control
                return true;
            }
        } else {
            //try to lookup this file in repository
            existingFile = layerRoot.addFile(entryPath, false, flow.getSettings().getLogger(), true);
            existingFile.setDoNotDeleteExternal(true);
            existingFile.sourceFile = entryFile;
            return true;
        }
    }

    private boolean processRemoteRadixdoc(final String radixdocFile, final String directoryIndexParentPath, final String prevReleaseUrl) {
        final String entryPath = SvnPath.append(directoryIndexParentPath, radixdocFile);

        SvnFile existingFile = layerRoot.findChild(entryPath);
        if (existingFile != null) {
            //file now is under version control
            return true;
        } else {
            final String fullPath = SvnPath.append(prevReleaseUrl, entryPath);
            byte[] digest = null;
            long revision = -1;
            if (SVN.isFileExists(flow.getRepository(), fullPath)) {
                //try to compute file digest
                try {
                    File temp = null;
                    try {
                        temp = File.createTempFile("rdxdoc", "rdxdoc");
                        revision = SVN.getFile(flow.getRepository(), fullPath, srcRevision, temp);
                        if (revision > 0) {
                            digest = DirectoryFileSigner.calcFileDigest(temp, true);
//                            byte[] newDigest = DirectoryFileSigner.calcFileDigest(existingFile.sourceFile, true);
//                            if (Arrays.equals(oldDigest, newDigest)) {
//                                existingFile.sourceFile = null;
//                                existingFile.setDoNotDeleteExternal(true);
//                            }
                        }
                    } catch (IOException | NoSuchAlgorithmException ex) {
                        Logger.getLogger(SvnFileList.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                    } finally {
                        if (temp != null) {
                            FileUtils.deleteFile(temp);
                        }
                    }
                } catch (RadixSvnException e) {
                    Logger.getLogger(SvnFileList.class.getName()).log(Level.FINE, e.getMessage(), e);
                }
            }

            if (digest != null) {
                existingFile = layerRoot.addFile(entryPath, false, flow.getSettings().getLogger(), true);
                existingFile.remoteDigest = digest;
                existingFile.external = true;
                existingFile.setExternalRevisionNumber(revision);
            }

            return true;
        }
    }

    private boolean cleanUp(SvnDir dir) {
        if (explicitFiles.contains(dir)) {
            return false;
        }
        List<SvnFile> toDelete = new LinkedList<>();
        if (dir.children != null) {
            for (SvnFile file : dir.children) {
                if (file instanceof SvnDir) {
                    if (cleanUp((SvnDir) file)) {
                        toDelete.add(file);
                    }
                } else {
                    toDelete.add(file);
                }
            }
        }
        for (SvnFile file : toDelete) {
            file.unregister();
        }
        if (dir.children == null || dir.children.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    final List<String> getExplicitModulePathes() {
        final List<String> result = new LinkedList<>();
        if (explicitFiles != null) {
            for (SvnFile f : explicitFiles) {
                result.add(SvnPath.append(srcDir.getName(), f.getPath()));
            }
        }
        return result;
    }

    public boolean apply(final SVNRepositoryAdapter.Editor editor, byte[] changeLogData, boolean isFirstNotReadOnly, boolean isPatchRelease, SvnConnectionType connectionType) {

        String layerUrlInRelease = SvnPath.append(releaseUrl, srcDir.getName());
        ChangeList changeList = new ChangeList();
        String layerUrlInPrevRelease = getLayerUrlInPrevRelease();
        if (isPatchRelease) {
            try {
                if (explicitFiles == null || explicitFiles.isEmpty()) {
                    return true;
                }
                cleanUp(layerRoot);
                {
                    int cnt = editor.openDirs(SvnPath.removeTail(layerUrlInRelease));
                    editor.appendDir(layerUrlInRelease);
                    editor.appendDir(SvnPath.append(layerUrlInRelease, "ads"));
                    editor.closeDirs(cnt + 2);
                }

                ReleaseUtils.ReleaseComponentManager componentManager = flow.getComponentManager();
                for (SvnFile ef : explicitFiles) {
                    String modulePath = ef.getPath();
                    String moduleSrcUrl = SvnPath.append(layerSrcUrl, modulePath);
                    String moduleReleaseUrl = SvnPath.append(layerUrlInRelease, modulePath);
                    //editor.appendDir(moduleReleaseUrl);
                    int cnt = editor.openDirs(SvnPath.removeTail(moduleReleaseUrl));
                    editor.copyDir(moduleSrcUrl, moduleReleaseUrl, srcRevision);
                    editor.closeDirs(cnt + 1);

                }
                int cnt = editor.openDirs(SvnPath.removeTail(layerUrlInRelease));
                layerRoot.applyExternals(editor, layerUrlInPrevRelease, layerUrlInRelease, flow.getSettings().getLogger(), changeList, connectionType);
                editor.closeDirs(cnt);
                for (SvnDir ef : explicitFiles) {
                    String modulePath = ef.getPath();
                    String moduleReleaseUrl = SvnPath.append(layerUrlInRelease, modulePath);
                    //editor.appendDir(moduleReleaseUrl);
                    if (!componentManager.sourcesRequired(srcDir.getName(), ef.moduleId)) {
                        flow.getSettings().getLogger().message("Removing sources at " + moduleReleaseUrl);
                        if (ef.findChildLocal("src") != null) {
                            String srcDirPath = SvnPath.append(moduleReleaseUrl, "src");
                            editor.deleteEntry(srcDirPath, -1);
                        }
                        if (ef.findChildLocal("img") != null) {
                            String srcDirPath = SvnPath.append(moduleReleaseUrl, "img");
                            editor.deleteEntry(srcDirPath, -1);
                        }
                    }
                }
            } catch (RadixSvnException ex) {
                return flow.getSettings().getLogger().fatal(ex);
            }
        } else {
            //copy sources to release repository
            String layerUrlInReleaseSrc = SvnPath.append(releaseSrcUrl, srcDir.getName());
            try {

                int cnt0 = editor.openDirs(releaseUrl);
                editor.copyDir(layerSrcUrl, layerUrlInRelease, srcRevision);
                editor.closeDirs(cnt0 + 1);

                flow.getSettings().getLogger().message("Versioned content of layer " + layerSrcUrl + " is copied to " + layerUrlInRelease);

                cnt0 = editor.openDirs(releaseSrcUrl);
                editor.copyDir(layerSrcUrl, layerUrlInReleaseSrc, srcRevision);
                editor.closeDirs(cnt0 + 1);

                flow.getSettings().getLogger().message("Versioned content of layer " + layerSrcUrl + " is copied to " + layerUrlInReleaseSrc);
                if (layer.isReadOnly()) {
                    flow.getSettings().getLogger().message("Layer " + layerSrcUrl + " is read only. No external files required");
                } else {

                    // replace ads/directory.xml
                    if (constructionChecker.find()) {
                        SvnFile adsDirectory = layerRoot.findChild(ADS_DIRECTORY_XML);
                        if (adsDirectory == null) {
                            adsDirectory = layerRoot.addFile(ADS_DIRECTORY_XML, false, flow.getSettings().getLogger(), true);
                        }

                        if (adsDirectory == null) {
                            return flow.getSettings().getLogger().fatal("Could not create file 'ads/directory.xml'");
                        }
                        adsDirectory.sourceFile = null;
                        adsDirectory.sourceBytes = createAdsDirectoryXml();
                    }

                    flow.getSettings().getLogger().message("Applying externals of layer " + layerSrcUrl);

                    int cnt = editor.openDirs(releaseUrl);//layerUrlInRelease
                    layerRoot.applyExternals(editor, layerUrlInPrevRelease, layerUrlInRelease, flow.getSettings().getLogger(), changeList, connectionType);
                    editor.closeDirs(cnt);

                    // applying externals for source
                    if (constructionChecker.find()) {
                        cnt = editor.openDirs(releaseSrcUrl);
                        layerRoot.applySrc(editor, getLayerUrlInPrevReleaseSrc(), layerUrlInReleaseSrc, flow.getSettings().getLogger(), connectionType);
                        editor.closeDirs(cnt);
                    }

                    if (!layer.isLocalizing()) {
                        SvnDir scriptsDir = layerRoot.getScriptsDir();
                        if (!processScripts(scriptsDir, editor, "oracle", changeLogData, isFirstNotReadOnly, connectionType)) {
                            return false;
                        }
                    }
                }
            } catch (RadixSvnException ex) {
                return flow.getSettings().getLogger().fatal(ex);
            }
        }
        return true;
    }

    /*
     * Scripts processing
     */
    private ReleaseUtils.SystemDirectory createScriptsDir = null;
    private ReleaseUtils.SystemDirectory postScriptsDir = null;
    private ReleaseUtils.SystemDirectory preScriptsDir = null;
    private ReleaseUtils.SystemDirectory updateScriptsDir = null;
    private ReleaseUtils.SystemDirectory reverseScriptsDir = null;

    private boolean checkReverseScripts() {
        SvnDir scriptsDir = layerRoot.getScriptsDir();
        if (scriptsDir != null) {
            SvnFile scriptsXml = scriptsDir.findChild("scripts.xml");
            if (scriptsXml != null) {
                try {
                    byte[] bytes = flow.getRepository().getFileBytes(SvnPath.append(layerSrcUrl, scriptsXml.getPath()), srcRevision);
                    ScriptsDocument xDoc = ScriptsDocument.Factory.parse(new ByteArrayInputStream(bytes));
                    if (xDoc.getScripts() != null) {

                        for (int index = 0; index < xDoc.getScripts().sizeOfScriptArray(); index++) {
                            Scripts.Script xScript = xDoc.getScripts().getScriptArray(index);
                            if (xScript.isSetIsReverse() && xScript.getIsReverse()) {
                                return true;
                            }
                        }

                    } else {
                        throw new RadixError("");
                    }
                } catch (XmlException | IOException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        }
        return false;
    }

    boolean computeScriptOperations() {
        try {
            if (releaseInfo.scriptsDir != null) {
                flow.getSettings().getLogger().message("Preparing script of layer  " + layerSrcUrl);
                ReleaseUtils.SystemDirectory layerScripts = new ReleaseUtils.SystemDirectory(releaseInfo.scriptsDir, srcDir.getName());
                createScriptsDir = new ReleaseUtils.SystemDirectory(layerScripts, "0-" + flow.getSettings().getNumber(), true);
                postScriptsDir = new ReleaseUtils.SystemDirectory(layerScripts, "x-" + flow.getSettings().getNumber(), true);
                preScriptsDir = new ReleaseUtils.SystemDirectory(layerScripts, flow.getSettings().getNumber() + "-x", true);
                if (flow.getSettings().getPrevNumber() != null) {
                    updateScriptsDir = new ReleaseUtils.SystemDirectory(layerScripts, flow.getSettings().getPrevNumber() + "-" + flow.getSettings().getNumber(), true);
                    if (checkReverseScripts()) {
                        reverseScriptsDir = new ReleaseUtils.SystemDirectory(layerScripts, flow.getSettings().getNumber() + "-" + flow.getSettings().getPrevNumber(), true);
                    }
                }
                if (!layerScripts.prepare(flow, -1)) {
                    return false;
                } else {
                    SvnDir scriptsDir = layerRoot.getScriptsDir();
                    if (scriptsDir != null) {
                        SvnFile scriptsXml = scriptsDir.findChild("scripts.xml");
                        if (scriptsXml != null) {
                            byte[] scriptsXmlBytes = flow.getRepository().getFileBytes(SvnPath.append(layerSrcUrl, scriptsXml.getPath()), srcRevision);
                            scriptsXml.sourceBytes = scriptsXmlBytes;
                        }
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            return flow.getSettings().getLogger().fatal(ex);
        }
    }

    private boolean processScripts(final SvnDir scriptsDir, final SVNRepositoryAdapter.Editor editor, final String dbName, final byte[] changeLogData, boolean isFirst, SvnConnectionType connectionType) {
        try {
            if (releaseInfo.scriptsDir != null) {
                //  releaseInfo.scriptsDir.apply(editor);

                copyScripts(createScriptsDir, editor, "0-" + flow.getSettings().getNumber(), dbName, getInstallScripts(scriptsDir, dbName), null, connectionType);
                if (flow.getSettings().getPrevNumber() != null) {
                    List<SvnFile> upgradeScripts = getUpgradeScripts(scriptsDir, dbName, false);
                    if (upgradeScripts == null) {
                        return false;
                    }
                    copyScripts(updateScriptsDir, editor, flow.getSettings().getPrevNumber() + "-" + flow.getSettings().getNumber(), dbName, upgradeScripts, changeLogData, connectionType);
                    if (reverseScriptsDir != null) {
                        List<SvnFile> reverseScripts = getUpgradeScripts(scriptsDir, dbName, true);
                        if (reverseScripts == null) {
                            return false;
                        }
                        copyScripts(reverseScriptsDir, editor, flow.getSettings().getNumber() + "-" + flow.getSettings().getPrevNumber(), dbName, reverseScripts, changeLogData, connectionType);
                    }
                }
                copyScripts(preScriptsDir, editor, flow.getSettings().getNumber() + "-x", dbName, getPreScripts(scriptsDir, dbName), null, connectionType);
                copyScripts(postScriptsDir, editor, "x-" + flow.getSettings().getNumber(), dbName, getPostScripts(scriptsDir, dbName), null, connectionType);

                //remove all scripts at release dds
                //remove upgrade scripts at release src and src
                if (scriptsDir != null) {
                    String layerUrlInRelease = SvnPath.append(releaseInfo.releaseDir.path, srcDir.getName());
                    String ddsScriptsUrl = SvnPath.append(layerUrlInRelease, scriptsDir.getPath());
                    flow.getSettings().getLogger().message("Removing scripts at " + ddsScriptsUrl + "...");
                    editor.deleteEntry(ddsScriptsUrl, -1);
                    if (releaseInfo.releaseSrcDir != null) {
                        SvnFile upgradeDir = scriptsDir.findChild("upgrades");
                        SvnFile dbUpgradeDir = scriptsDir.findChild(dbName + "/upgrades");

                        final List<SvnFile> upgradeDirs = new ArrayList<>(2);
                        if (upgradeDir != null) {
                            upgradeDirs.add(upgradeDir);
                        }

                        if (dbUpgradeDir != null) {
                            upgradeDirs.add(dbUpgradeDir);
                        }

                        SvnFile scriptIndex = scriptsDir.findChild("scripts.xml");
                        if (!upgradeDirs.isEmpty() || scriptIndex != null) {
                            for (String layerUrl : new String[]{SvnPath.append(releaseInfo.releaseSrcDir.path, srcDir.getName()), layerSrcUrl}) {
                                if (!upgradeDirs.isEmpty()) {
                                    for (final SvnFile upDir : upgradeDirs) {
                                        String upgradeScriptsUrl = SvnPath.append(layerUrl, upDir.getPath());
                                        flow.getSettings().getLogger().message("Removing scripts at " + upgradeScriptsUrl + "...");
                                        editor.deleteEntry(upgradeScriptsUrl, -1);
                                    }
                                }
                                if (scriptIndex != null) {
                                    String scriptIndexUrl = SvnPath.append(layerUrl, scriptIndex.getPath());
                                    flow.getSettings().getLogger().message("Removing script index file at " + scriptIndexUrl + "...");
                                    editor.deleteEntry(scriptIndexUrl, -1);
                                }
                            }
                        }
                    }
                }
            }
        } catch (RadixSvnException ex) {
            return flow.getSettings().getLogger().fatal(ex);
        }

        return true;
    }

    private void copyScripts(ReleaseUtils.SystemDirectory scriptsDir,
            final SVNRepositoryAdapter.Editor editor, final String dirName,
            final String dbName, final List<SvnFile> files,
            byte[] changeLogBytes, SvnConnectionType connectionType) throws RadixSvnException {

        final String layerScriptsUrl = SvnPath.append(releaseScriptsUrl, srcDir.getName());
        final String installScriptsUrl = SvnPath.append(layerScriptsUrl, dirName);

        flow.getSettings().getLogger().message("Script dir created: " + installScriptsUrl);
        if (changeLogBytes != null) {
            String changeLogFilePath = SvnPath.append(installScriptsUrl, "changes.xml");
            editor.appendFile(changeLogFilePath, changeLogBytes);
        }

        int cnt = 0;

        final String installScriptsDbUrl = getScriptsDbUrl(installScriptsUrl, dbName);

        if (ReleaseSettings.isDbDependentScriptMode()) {
            if (connectionType.equals(SvnConnectionType.HTTP)) {
                cnt = editor.appendDirs(installScriptsDbUrl);
            } else {

                cnt = 1;

                editor.appendDir(installScriptsDbUrl);
                editor.closeDir();
                editor.openDir(installScriptsDbUrl);

            }
            flow.getSettings().getLogger().message("Script dir created: " + installScriptsDbUrl);
        }

        for (SvnFile file : files) {
            String fileDestUrl = SvnPath.append(installScriptsDbUrl, file.name);
            if (file.external && file.sourceBytes != null) {
                int count = editor.openDirs(installScriptsDbUrl);
                try {
                    editor.appendFile(fileDestUrl, file.sourceBytes);
                } finally {
                    editor.closeDirs(count);
                }
                flow.getSettings().getLogger().message("Script file created: " + fileDestUrl + "...");
            } else {
                String fileSrcUrl = SvnPath.append(layerSrcUrl, file.getPath());
                int count = editor.openDirs(installScriptsDbUrl);
                try {
                    editor.copyFile(fileSrcUrl, fileDestUrl, srcRevision);
                } finally {
                    editor.closeDirs(count);
                }
                flow.getSettings().getLogger().message("Script file copied: " + fileSrcUrl + " to " + fileDestUrl + "...");
            }
        }

        editor.closeDirs(cnt);
    }

    private String getScriptsDbUrl(String scriptsUrl, String dbName) {
        return !ReleaseSettings.isDbDependentScriptMode() ? scriptsUrl : SvnPath.append(scriptsUrl, dbName);
    }

    private List<SvnFile> getScripts(final SvnDir dir) {
        ArrayList<SvnFile> scripts = new ArrayList<>();

        if (dir.children != null) {
            for (SvnFile file : dir.children) {
                if (!(file instanceof SvnDir) && file.name.endsWith(".sql")) {
                    scripts.add(file);
                }
            }
        }
        return scripts;
    }

    private List<SvnFile> getScripts(final SvnDir scriptsDir, final String scriptName, final String dbName) {
        if (scriptsDir == null) {
            return Collections.emptyList();
        }

        // new script location
        SvnFile installDir = scriptsDir.findChild(scriptName);
        if (installDir instanceof SvnDir) {
            return getScripts((SvnDir) installDir);
        }

        // old script location
        installDir = scriptsDir.findChild(dbName + "/" + scriptName);
        if (installDir instanceof SvnDir) {
            return getScripts((SvnDir) installDir);
        }

        return Collections.emptyList();
    }

    private List<SvnFile> getInstallScripts(final SvnDir scriptsDir, final String dbName) {
        return getScripts(scriptsDir, "install", dbName);
    }

    private List<SvnFile> getUpgradeScripts(final SvnDir scriptsDir, final String dbName, final boolean reverse) {
        if (scriptsDir == null) {
            return Collections.emptyList();
        }

        SvnFile scriptsIndex = scriptsDir.findChildLocal("scripts.xml");
        if (scriptsIndex != null) {
            List<String> matchNames = null;
            SvnFile scriptsXml = null;
            if (scriptsIndex.sourceBytes != null) {
                try {
                    ScriptsDocument xDoc = ScriptsDocument.Factory.parse(new ByteArrayInputStream(scriptsIndex.sourceBytes));
                    List<Scripts.Script> matchedScripts = new LinkedList<>();
                    int totalScriptCount = 0;
                    if (xDoc.getScripts() != null) {

                        totalScriptCount = xDoc.getScripts().getScriptList().size();
                        for (int index = 0; index < xDoc.getScripts().sizeOfScriptArray();) {
                            Scripts.Script xScript = xDoc.getScripts().getScriptArray(index);
                            boolean scriptIsReverse = xScript.isSetIsReverse() && xScript.getIsReverse();
                            if (scriptIsReverse == reverse) {
                                matchedScripts.add(xScript);
                                index++;
                            } else {
                                xDoc.getScripts().removeScript(index);
                            }
                        }

                    } else {
                        throw new RadixError("");
                    }

                    if (totalScriptCount == matchedScripts.size() && !reverse) {
                        scriptsXml = scriptsIndex;
                    } else {
                        //not all scripts matched
                        if (totalScriptCount != matchedScripts.size()) {
                            matchNames = new LinkedList<>();
                            for (Scripts.Script xScript : matchedScripts) {
                                matchNames.add(xScript.getFileName());
                            }
                        }
                        if (reverse) {
                            for (int index = 0; index < xDoc.getScripts().sizeOfScriptArray(); index++) {
                                Scripts.Script xScript = xDoc.getScripts().getScriptArray(index);
                                xScript.unsetIsReverse();
                            }
                        }

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        XmlFormatter.save(xDoc, stream);
                        byte[] docBytes = stream.toByteArray();
                        SvnFile file = new SvnFile(scriptsIndex.name);
                        file.external = true;
                        file.sourceBytes = docBytes;
                        scriptsXml = file;
                    }

                } catch (Throwable ex) {
                    flow.getSettings().getLogger().fatal("Invalid script index file format:" + scriptsIndex.sourceFile.getAbsolutePath());
                    return null;
                }
            } else if (reverse) {
                return Collections.emptyList();
            }

            // new script location
            SvnFile installDir = scriptsDir.findChild("upgrades");
            if (!(installDir instanceof SvnDir)) {
                // old script location
                installDir = scriptsDir.findChild(dbName + "/upgrades");
            }
            if (installDir instanceof SvnDir) {
                List<SvnFile> upgradeScripts = getScripts((SvnDir) installDir);
                if (matchNames != null) {
                    for (SvnFile file : new ArrayList<>(upgradeScripts)) {
                        if (!matchNames.contains(file.name)) {
                            upgradeScripts.remove(file);
                        }
                    }
                }

                if (scriptsXml != null) {
                    upgradeScripts.add(scriptsXml);
                }
                return upgradeScripts;
            } else {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
    }

    private List<SvnFile> getPreScripts(final SvnDir scriptsDir, final String dbName) {
        return getScripts(scriptsDir, "pre", dbName);
    }

    private List<SvnFile> getPostScripts(final SvnDir scriptsDir, final String dbName) {
        return getScripts(scriptsDir, "post", dbName);
    }
}
