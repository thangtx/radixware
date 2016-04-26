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
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.build.directory.DirectoryFileSigner;
import org.radixware.kernel.common.dialogs.AuthenticationCancelledException;
import org.radixware.kernel.common.enums.ESvnAuthType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.client.SvnEntry;
import org.radixware.kernel.common.svn.client.SvnPath;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.schemas.product.Directory;
import org.radixware.schemas.product.DirectoryDocument;
import org.radixware.schemas.product.LayerDocument;
import org.radixware.schemas.product.ReleaseDocument;

public abstract class ReleaseVerifier implements NoizyVerifier {

    private static boolean isDbDependentScriptMode = false;

    public static void enableDbDependentScriptsMode() {
        isDbDependentScriptMode = true;
    }

    public static boolean isDbDependentScriptMode() {
        return isDbDependentScriptMode;
    }

    class ReleaseFile {

        File file = null;
        String svnPath;
        SVNRepositoryAdapter repository;
        final long revision;

        public ReleaseFile(String svnPath, SVNRepositoryAdapter repository, long revision) {
            this.svnPath = svnPath;
            this.repository = repository;

            this.revision = revision;
        }

        public void read() throws RadixSvnException, IOException {
            if (file != null) {
                return;
            }
            file = File.createTempFile("rdx", "rdx");
            SVN.getFile(repository, svnPath, revision, file);
        }

        void close() {
            if (file != null) {
                file.delete();
                file = null;
            }
        }

        public String getPath() {
            return releaseUrl() + "/" + svnPath;
        }
    }
    private final String repositoryRoot;
    private final String releasePath;
    private final String userName;
    private final ESvnAuthType authType;
    private final String sshKeyFile;

    protected ReleaseVerifier(String repositoryRoot, String releasePath, String userName, ESvnAuthType authType, String sshKeyFile) {
        this.repositoryRoot = repositoryRoot;
        this.releasePath = releasePath;
        this.userName = userName;
        this.authType = authType;
        this.sshKeyFile = sshKeyFile;
    }

    private boolean initializeRepository() {
        try {
            repository = SVNRepositoryAdapter.Factory.newInstance(repositoryRoot, releasePath, userName, SVN.getForAuthType(authType), sshKeyFile);
            revision = repository.getLatestRevision();
            return true;

        } catch (RadixSvnException ex) {
            repository = null;
            error(ex);
            return false;
        }
    }

    ReleaseFile getFile(String path) {
        return new ReleaseFile(path, repository, revision);
    }
    private static final String INVALID_FRELEASE_FILE_FORMAT = "    Invalid release description file format";
    private SVNRepositoryAdapter repository = null;
    private long revision;

    @Override
    public boolean verify() {
        //969843
        return verify(null);
    }

    private String releaseUrl() {
        return SvnPath.append(repositoryRoot, releasePath);
    }

    public boolean verifyLayers(final List<String> layers) {
        if (!initializeRepository()) {
            return false;
        }

        message("Verifying release at " + releaseUrl() + "...");

        try {
            final List<String> layerNames = new LinkedList<>();
            try {
                repository.getDir("", revision, new SVNRepositoryAdapter.EntryHandler() {

                    @Override
                    public void accept(SvnEntry entry) throws RadixSvnException {
                        if (entry.getKind() == SvnEntry.Kind.DIRECTORY) {
                            final String name = entry.getName();

                            if (layers.contains(name)) {
                                layerNames.add(entry.getName());
                            }
                        }
                    }
                });
            } catch (RadixSvnException ex) {
                error(new RadixError("Unable to read layer list", ex));
                return false;
            }

            return processVerify(repository, layerNames);
        } finally {

            repository.close();
        }
    }

    public boolean verify(List<String> modulePathes) {

        if (!initializeRepository()) {
            return false;
        }

        message("Verifying release at " + releaseUrl() + "...");

        try {

            if (modulePathes != null) {
                boolean result = true;
                for (String modulePath : modulePathes) {
                    final String moduleDirIndex = SvnPath.append(modulePath, FileUtils.DIRECTORY_XML_FILE_NAME);
                    if (!processDirectoryIndexFile(moduleDirIndex, repository, revision)) {
                        result = false;
                    }
                }
                return result;
            } else {
                final List<String> layerNames = new LinkedList<String>();
                try {

                    repository.getDir("", revision, new SVNRepositoryAdapter.EntryHandler() {

                        @Override
                        public void accept(SvnEntry svnde) throws RadixSvnException {
                            if (svnde.getKind() == SvnEntry.Kind.DIRECTORY) {
                                layerNames.add(svnde.getName());
                            }
                        }
                    });
                } catch (RadixSvnException ex) {
                    error(new RadixError("Unable to read layer list", ex));
                    return false;
                }
                return processVerify(repository, layerNames);
            }
        } finally {

            repository.close();
        }
    }

    private boolean processVerify(SVNRepositoryAdapter repository, final List<String> layerNames) {
        final ReleaseFile releasexml = new ReleaseFile("release.xml", repository, revision);
        try {
            try {
                releasexml.read();
            } catch (RadixSvnException | IOException ex) {
                return unabletoReadFileError(releaseUrl());
            }
            ReleaseDocument xDoc;
            try {
                xDoc = ReleaseDocument.Factory.parse(releasexml.file);
            } catch (IOException | XmlException ex) {
                error(INVALID_FRELEASE_FILE_FORMAT);
                return false;
            }
            if (xDoc.getRelease() != null && xDoc.getRelease() != null) {
                boolean result = true;
                if (xDoc.getRelease().getReleaseNumber() == null || !releasePath.endsWith(xDoc.getRelease().getReleaseNumber())) {
                    error("    Release URL does not match to release number: must be " + String.valueOf(xDoc.getRelease().getReleaseNumber()));
                    result = false;
                }
                for (String layerUri : layerNames) {
                    final String layerDirIndex = SvnPath.append(layerUri, FileUtils.DIRECTORY_XML_FILE_NAME);
                    final ReleaseFile layerDesc = new ReleaseFile(SvnPath.append(layerUri, Layer.LAYER_XML_FILE_NAME), repository, revision);
                    try {
                        layerDesc.read();
                    } catch (RadixSvnException | IOException ex) {
                        error(new RadixError("Unable to read layer.xml file from layer " + layerUri, ex));
                        result = false;
                        continue;
                    }

                    LayerDocument xLayerDoc;
                    try {
                        xLayerDoc = LayerDocument.Factory.parse(layerDesc.file);
                    } catch (XmlException ex) {
                        error(new RadixError("Invalid format of layer.xml file in layer " + layerUri, ex));
                        result = false;
                        continue;
                    } catch (IOException ex) {
                        error(new RadixError("Unable to read layer.xml file in layer " + layerUri, ex));
                        result = false;
                        continue;
                    }
                    final org.radixware.schemas.product.Layer xLayer = xLayerDoc.getLayer();

                    if (!processDirectoryIndexFile(layerDirIndex, repository, revision)) {
                        result = false;
                    }

                    if (xLayer.getReleaseNumber() != null && Objects.equals(xLayer.getReleaseNumber(), xDoc.getRelease().getReleaseNumber())) {
                        if (!xLayer.isSetIsLocalizing() || !xLayer.getIsLocalizing()) {
                            if (!processScripts(xDoc.getRelease().getReleaseNumber(), xDoc.getRelease().getPrevReleaseNumber(), layerUri)) {
                                result = false;
                            }
                        }
                    } else {
                        //layer is redistributable
                        //skip checking
                    }
                }

                return result;
            } else {
                error(INVALID_FRELEASE_FILE_FORMAT);
                return false;
            }
        } finally {
            releasexml.close();
        }
    }

    private boolean processScripts(String releaseNumber, String prevReleaseNumber, String layerName) {
        message("Verifying scripts of layer " + layerName + " for release " + releaseNumber);
        String scriptsUrl = SvnPath.append(SvnPath.removeTail(SvnPath.removeTail(releaseUrl())), "scripts");

        SVNRepositoryAdapter scriptsRepository;
        try {
            scriptsRepository = SVNRepositoryAdapter.Factory.newInstance(scriptsUrl, null, userName, SVN.getForAuthType(authType), sshKeyFile);
        } catch (RadixSvnException ex) {
            error("    Unable to create scripts repository");
            return false;
        }
        try {

            List<String> scriptsDirsToCheck = new ArrayList<String>(4);

            scriptsDirsToCheck.add(SvnPath.append(layerName, "0-" + releaseNumber));
            scriptsDirsToCheck.add(SvnPath.append(layerName, "x-" + releaseNumber));
            scriptsDirsToCheck.add(SvnPath.append(layerName, releaseNumber + "-x"));
            if (prevReleaseNumber != null) {
                scriptsDirsToCheck.add(SvnPath.append(layerName, prevReleaseNumber + "-" + releaseNumber));
            }

            long revision;
            try {
                revision = scriptsRepository.getLatestRevision();
            } catch (RadixSvnException ex) {
                return false;
            }
            boolean result = true;
            for (String scriptDir : scriptsDirsToCheck) {
                try {
                    SvnEntry.Kind kind = scriptsRepository.checkPath(scriptDir, revision);
                    if (kind != SvnEntry.Kind.DIRECTORY) {
                        error("    Missing script directory: " + scriptDir);
                        result = false;
                    } else {
                        if (isDbDependentScriptMode()) {
                            String dbDir = SvnPath.append(scriptDir, "oracle");
                            kind = scriptsRepository.checkPath(dbDir, revision);
                            if (kind != SvnEntry.Kind.DIRECTORY) {
                                error("    Missing script directory: " + scriptDir);
                                result = false;
                            }
                        }
                    }
                } catch (RadixSvnException ex) {
                    result = false;
                }
            }

            return result;
        } finally {
            scriptsRepository.close();
        }
    }

    private boolean processDirectoryIndexFile(String path, SVNRepositoryAdapter repository, long revision) {
        message("Checking directory index file " + path + "...");
        if (path.endsWith("org.radixware/kernel/starter/directory-dist.xml")) {
            return true;
        }
        final ReleaseFile dirIndex = new ReleaseFile(path, repository, revision);
        try {
            try {
                dirIndex.read();
            } catch (RadixSvnException ex) {
                return unabletoReadFileError(path);
            } catch (IOException ex) {
                return unabletoReadFileError(path);
            }

            DirectoryDocument xDoc;
            try {
                xDoc = DirectoryDocument.Factory.parse(dirIndex.file);
            } catch (IOException ex) {
                error("    Unable to parse directory index file");
                return false;
            } catch (XmlException ex) {
                error("    Unable to parse directory index file");
                return false;
            } finally {
                dirIndex.close();
            }
            if (xDoc.getDirectory() != null) {
                boolean result = true;
                String thisDirPath = SvnPath.removeTail(path);
                if (xDoc.getDirectory().getIncludes() != null) {
                    for (Directory.Includes.Include inc : xDoc.getDirectory().getIncludes().getIncludeList()) {
                        String includedFilePath = SvnPath.append(thisDirPath, inc.getFileName());
                        if (!processDirectoryIndexFile(includedFilePath, repository, revision)) {
                            result = false;
                        }
                    }
                }
                if (xDoc.getDirectory().getFileGroups() != null) {
                    for (Directory.FileGroups.FileGroup xGroup : xDoc.getDirectory().getFileGroups().getFileGroupList()) {
                        for (Directory.FileGroups.FileGroup.File xFile : xGroup.getFileList()) {

                            String filePath = SvnPath.append(thisDirPath, xFile.getName());
                            ReleaseFile file = new ReleaseFile(filePath, repository, revision);
                            byte[] storedDigest = xFile.getDigest();
                            try {
                                try {
                                    file.read();
                                    boolean isJar = xFile.getName().endsWith(".jar");
                                    byte[] fileDigest = isJar
                                            ? DirectoryFileSigner.readJarDigest(file.file)
                                            : DirectoryFileSigner.calcFileDigest(file.file, isJar, xFile.getName().endsWith(".xml"));
                                    if (!Arrays.equals(storedDigest, fileDigest)) {
                                        error("   Invalid signature of file " + filePath + ": expected " + Hex.encode(storedDigest) + " but was " + Hex.encode(fileDigest));
                                        result = false;
                                    }
                                } catch (RadixSvnException ex) {
                                    result = unabletoReadFileError(filePath);
                                    continue;
                                } catch (IOException ex) {
                                    result = unabletoReadFileError(filePath);
                                    continue;
                                } catch (NoSuchAlgorithmException ex) {
                                    //ignore
                                    continue;
                                }
                            } finally {
                                file.close();
                            }
                        }
                    }
                }
                return result;
            } else {
                error("   Invalid directory index file: " + path);
                return false;
            }
        } finally {
            dirIndex.close();
        }
    }

    private boolean unabletoReadFileError(String path) {
        error("   Unable to read file " + path);
        return false;
    }
}
