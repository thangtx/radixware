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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.builder.DirectoryFile;
import org.radixware.kernel.common.builder.release.ReleaseUtils.ReleaseInfo;
import org.radixware.kernel.common.builder.release.ReleaseUtils.SystemDirectory;
import org.radixware.kernel.common.defs.ads.build.IFlowLogger;
import org.radixware.kernel.common.dialogs.AuthenticationCancelledException;
import org.radixware.kernel.common.enums.ERepositoryBranchType;
import org.radixware.kernel.common.enums.ESvnAuthType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.SvnConnectionType;
import org.radixware.kernel.common.svn.client.ISvnFSClient;
import org.radixware.kernel.common.svn.client.SvnPath;
import org.radixware.kernel.common.svn.utils.ReleaseVerifier;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.schemas.product.*;

public class ReleaseBuilder {

    enum EReleaseDirStatus {

        SOURCE,
        RELEASE,
        RELEASE_SOURCE;
    }
    private final ReleaseFlow flow;
    private final IFlowLogger logger;
    private final ReleaseSettings settings;

    public ReleaseBuilder(ReleaseSettings settings) {
        this.flow = new ReleaseFlow(settings);
        this.logger = settings.getLogger();
        this.settings = settings;
    }

//    private long getMaxLocalRevisionNumber(SVNClientManager clientManager, long rootRevision) throws SVNException {
//        ArrayList<String> changeList = new ArrayList<>();
//        final long[] revisionArr = new long[]{rootRevision};
//
//        clientManager.getStatusClient().doStatus(settings.getBranch().getDirectory(), SVNRevision.HEAD, SVNDepth.INFINITY, false, true, false, false, new ISVNStatusHandler() {
//            @Override
//            public void handleStatus(SVNStatus status) throws SVNException {
//                SVNRevision revision = status.getRevision();
//                if (revision != null && revision.getNumber() > revisionArr[0]) {
//                    revisionArr[0] = revision.getNumber();
//                }
//            }
//        }, changeList);
//        return revisionArr[0];
//    }
    private long getMaxLocalRevisionNumber(ISvnFSClient svnClientAdapter, long rootRevision) throws ISvnFSClient.SvnFsClientException {
        final long[] revisionArr = new long[]{rootRevision};

        svnClientAdapter.getStatus(settings.getBranch().getDirectory(), true, true, false, true, new ISvnFSClient.ISvnStatusCallback() {

            @Override
            public void doStatus(String string, ISvnFSClient.ISvnStatus status) {
                ISvnFSClient.SvnRevision.Number revision = status.getRevision();
                if (revision != null && revision.getNumber() > revisionArr[0]) {
                    revisionArr[0] = revision.getNumber();
                }
            }
        });
        return revisionArr[0];
    }

    public boolean process() {

        if (!ReleaseSettings.isValidReleaseName(settings.getNumber(), false)) {
            return logger.fatal("Invalid release number: " + settings.getNumber());
        }
        File branchDir = settings.getBranch().getDirectory();
      
        logger.stateMessage("Preparing to make release version " + settings.getNumber() + " of " + branchDir.getAbsolutePath() + "...");

        SvnConnectionType connectionType = SvnConnectionType.SVN;
        SVNRepositoryAdapter repository = null;
        ISvnFSClient svnClientAdapter = (ISvnFSClient) settings.getSvnClientAdapter();
        String branchUrl;
        long revision;
        try {
            ISvnFSClient.ISvnStatus status = svnClientAdapter.getSingleStatus(branchDir);
            revision = status.getRevision().getNumber();
            branchUrl = status.getUrlString();
        } catch (ISvnFSClient.SvnFsClientException ex) {
            return settings.getLogger().fatal(ex);
        }

        try {
            try {
                repository = SVNRepositoryAdapter.Factory.newRootInstance(branchUrl, settings.getUserName(), SVN.getForAuthType(settings.getAuthType()), settings.getSSHKeyFile());//RadixSVNRepository.newInstance(settings.getBranch(), settings.getUserName(), settings.getAuthType(), settings.getSSHKeyFile());
            } catch (RadixSvnException ex) {
                if (ex.isAuthenticationCancelled()) {
                    return logger.fatal("SVN authentication cancelled by user");
                }
                return logger.fatal(ex);
            }

            try {
                final SVNRepositoryAdapter.Editor editor = repository.createEditor("Check write access");
                editor.cancel();
            } catch (RadixSvnException ex) {
                String url = "";
                try {
                    url = repository.getLocation();
                } catch (Throwable e) {
                    url = "unknown repository location";
                }
                return logger.fatal("Write access to '" + branchDir.toString() + "' (" + url + ") not exist");
            }
            flow.setRepository(repository);

            String rootUrl = null;
            try {
                rootUrl = repository.getRepositoryRoot();
            } catch (RadixSvnException ex) {
                return logger.fatal(ex);
            }
            if (rootUrl != null) {
                if (rootUrl.length() > 4 && rootUrl.substring(0, 4).equals("http")) {
                    connectionType = SvnConnectionType.HTTP;
                }
            }

            String basisUrl = SvnPath.getRelativePath(rootUrl, SvnPath.removeTail(SvnPath.removeTail(branchUrl)));
            String branchPath = SvnPath.getRelativePath(rootUrl, branchUrl);

            final ReleaseUtils.ReleaseComponentManager componentManager = new ReleaseUtils.ReleaseComponentManager();
            flow.setComponentManager(componentManager);

            logger.message("Checking svn status of branch " + branchDir.getAbsolutePath() + "...");
            try {

                long topRevision = getMaxLocalRevisionNumber(svnClientAdapter, revision);

                ReleaseUtils.StatusInfo statusInfo = ReleaseUtils.getStatusInfo(svnClientAdapter, topRevision, branchDir);
                if (statusInfo.isAdded() || statusInfo.isConflicted()
                        || statusInfo.isModified() || statusInfo.isDeleted()
                        || statusInfo.isMissing() || statusInfo.isObstructed() || statusInfo.isReplaced()) {
                    String message = "<html>There are local modifications in branch " + branchDir.getAbsolutePath() + "<br><b>Warning:</b>this may cause missynchronization of sources and binaries or scripts loss in release";
                    if (settings.TEST_MODE) {
                        logger.error(message);
                    } else {
                        if (!logger.recoverableError(message)) {
                            return false;
                        }
                    }
                }
                List<File> outDated = statusInfo.getOutOfDate();

                // updating working copy
                if (topRevision != revision || !outDated.isEmpty()) {//different versions

                    logger.message("Updating repository to revision " + topRevision + "...");

                    svnClientAdapter.update(branchDir,
                            svnClientAdapter.getRevision(topRevision),
                            true);

                    //  manager.getUpdateClient().doUpdate(branchDir, SVNRevision.create(topRevision), SVNDepth.INFINITY, false, false);
                    if (!outDated.isEmpty()) {

                        StringBuilder builder = new StringBuilder();
                        builder.append("<html>Following files were updated during release preparation:<br>");
                        builder.append("<table><tbody>");

                        for (int i = 0; i < outDated.size(); i++) {
                            builder.append("<tr><td>");
                            if (i > 10) {
                                builder.append(String.valueOf(outDated.size() - i)).append(" more...");
                                break;
                            } else {
                                builder.append(outDated.get(i));
                            }
                            builder.append("</td></tr>");
                        }

                        builder.append("</tbody></table>");
                        builder.append("<br><b>Warning:</b> This may cause in release incnsistence!");
                        builder.append("</html>");
                        if (!logger.recoverableError(builder.toString())) {
                            if (!flow.getSettings().TEST_MODE) {
                                return false;
                            }
                        }
                        logger.message("File changes detected. Force ADS build process ");
                        //settings.setPerformCleanAndBuild(true);
                    }

                    revision = topRevision;
                }

                logger.message("Will use repository revision number " + String.valueOf(revision));

            } catch (ISvnFSClient.SvnFsClientException ex) {
                return logger.fatal(ex);
            }

            logger.stateMessage("Checking creation possibilities...");
            flow.sourceBranchUrl = branchPath;
            flow.setRevision(revision);

            final ReleaseInfo releaseInfo;
            final SystemDirectory sysRoot;

            sysRoot = new SystemDirectory(basisUrl);
            SystemDirectory releases = new SystemDirectory(sysRoot, "releases");
            SystemDirectory release = new SystemDirectory(releases, settings.getNumber(), true);
            SystemDirectory releasesSrc = new SystemDirectory(sysRoot, "releases.src");
            SystemDirectory releaseSrc = new SystemDirectory(releasesSrc, settings.getNumber(), true);
            SystemDirectory scripts = new SystemDirectory(sysRoot, "scripts");
            releaseInfo = new ReleaseInfo(release, releaseSrc, scripts);

            final ArrayList<LayerInfo> layerFiles = new ArrayList<>();
            final Map<Layer, LayerInfo> layerInfos = new HashMap<>();
            try {
                if (!sysRoot.prepare(flow, revision)) {
                    return false;
                }

                releaseInfo.prevReleaseUrl = settings.getPrevNumber() == null ? null : SvnPath.append(releaseInfo.releaseDir.parent.path, settings.getPrevNumber());
                if (repository.isDir(releaseInfo.prevReleaseUrl, revision)) {
                    releaseInfo.prevReleaseRevision = repository.getLatestRevision();
                } else {
                    releaseInfo.prevReleaseUrl = null;
                }

                for (Layer l : settings.getBranch().getLayers().getInOrder()) {
                    if (!l.isDistributable()) {
                        continue;
                    }
                    LayerInfo info = new LayerInfo(l, branchPath, flow, releaseInfo);
                    String newAPIVersion = null;

                    if (!info.prepare(revision, newAPIVersion)) {
                        return false;
                    }
                    layerFiles.add(0, info);
                    layerInfos.put(l, info);
                }
                if (layerInfos.isEmpty()) {
                    return logger.fatal("No distributable layers found");
                }
            } catch (Exception e) {
                return flow.getSettings().getLogger().fatal(new RadixError("Unable to check directory creation possibilities", e));
            }

            byte[] branchXmlContent = repository.getFileBytes(SvnPath.append(branchPath, "branch.xml"), revision);
            if (branchXmlContent == null) {
                return logger.fatal("Unable to read branch description from " + SvnPath.append(branchPath, "branch.xml") + " at revision " + revision);
            }

            try {
                logger.stateMessage("Applying release");
                final SVNRepositoryAdapter.Editor editor = flow.getEditor();
                // editor.openDir(basisUrl);
                sysRoot.apply(editor);
                //  editor.closeDir();

                if (!addBranchDescriptionFile(editor, branchXmlContent, branchPath, EReleaseDirStatus.SOURCE)) {
                    return false;
                }
                if (!addReleaseDescFile(editor, releaseInfo.releaseDir.path, branchUrl, layerFiles)) {
                    return false;
                }

                if (releaseInfo.releaseSrcDir != null) {
                    if (!addReleaseDescFile(editor, releaseInfo.releaseSrcDir.path, branchUrl, layerFiles)) {
                        return false;
                    }
                }
                if (!addBranchDescriptionFile(editor, branchXmlContent, releaseInfo.releaseDir.path, EReleaseDirStatus.RELEASE)) {
                    return false;
                }
                if (releaseInfo.releaseSrcDir != null) {
                    if (!addBranchDescriptionFile(editor, branchXmlContent, releaseInfo.releaseSrcDir.path, EReleaseDirStatus.RELEASE_SOURCE)) {
                        return false;
                    }
                }

                boolean isFirstNotreadOnly = true;
                for (LayerInfo info : layerFiles) {
                    if (info.fileList != null) {
                        if (!info.fileList.apply(editor, info.changesXmlContent, isFirstNotreadOnly, false, connectionType)) {
                            return false;
                        }
                        if (!info.layer.isReadOnly()) {
                            isFirstNotreadOnly = false;
                        }

                        String layerUrl = SvnPath.append(releaseInfo.releaseDir.path, info.layer.getURI());
                        if (!processLayer(editor, info, layerUrl, false, connectionType)) {
                            return false;
                        }
                        if (releaseInfo.releaseSrcDir != null) {
                            layerUrl = SvnPath.append(releaseInfo.releaseSrcDir.path, info.layer.getURI());
                            if (!processLayer(editor, info, layerUrl, true, connectionType)) {
                                return false;
                            }
                        }
                    }
                }
            } catch (RadixSvnException ex) {
                return logger.fatal(ex);
            }

            String keyWord = "Release ";
            if (!logger.confirmation(keyWord + settings.getNumber() + " is prepared successfully. Commit?")) {
                return logger.fatal(keyWord + "is cancelled by user");
            }

            boolean result = flow.commit(svnClientAdapter);
            if (result && settings.verifyRelease()) {
                final ReleaseVerifier vf = new ReleaseVerifier(rootUrl, releaseInfo.releaseDir.path, settings.getUserName(), settings.getAuthType(), settings.getSSHKeyFile()) {
                    @Override
                    public void error(Exception e) {
                        logger.error(e.getMessage());
                    }

                    @Override
                    public void error(String message) {
                        logger.error(message);
                    }

                    @Override
                    public void message(String message) {
                        logger.message(message);
                    }
                };

                // collects development layers
                final String baseDevLayerUri = settings.getBranch().getBaseDevelopmentLayerUri();
                if (baseDevLayerUri == null || baseDevLayerUri.isEmpty()) {
                    result = vf.verify();
                } else {

                    final List<Layer> layers = settings.getBranch().getLayers().getInOrder();
                    final Set<String> devLayers = new HashSet<>();

                    boolean find = false;
                    for (final Layer layer : layers) {
                        if (!find && Objects.equals(layer.getURI(), baseDevLayerUri)) {
                            find = true;
                            devLayers.add(layer.getURI());
                        } else if (find) {
                            final List<String> baseLayerURIs = layer.getBaseLayerURIs();

                            for (final String baseUri : baseLayerURIs) {
                                if (devLayers.contains(baseUri)) {
                                    devLayers.add(layer.getURI());
                                    break;
                                }
                            }
                        }
                    }

                    //patchModulePathes
                    if (!devLayers.isEmpty()) {
                        result = vf.verifyLayers(new ArrayList<String>(devLayers));
                    }
                }
            }

            return result;
        } finally {
            if (repository != null) {
                repository.close();
            }
        }
    }

    private boolean addReleaseDescFile(SVNRepositoryAdapter.Editor editor, String releaseUrl, String srcBranchUrl, List<LayerInfo> layerInfos) {
        byte[] fileBytes = flow.getReleaseDescriptionFileContent();
        if (fileBytes == null) {
            logger.message("Generating release description file...");
            ReleaseDocument xDoc = ReleaseDocument.Factory.newInstance();
            Release xDef = xDoc.addNewRelease();

            xDef.setReleaseNumber(settings.getNumber());
            if (settings.getPrevNumber() != null && !settings.getPrevNumber().isEmpty()) {
                xDef.setPrevReleaseNumber(settings.getPrevNumber());
            }

            if (!settings.getBranch().getLayers().isEmpty()) {
                Release.Branch xBranch = xDef.addNewBranch();
                for (Layer l : settings.getBranch().getLayers()) {
                    Release.Branch.Layer xLayer = xBranch.addNewLayer();
                    xLayer.setUri(l.getURI());
                    xLayer.setBaseLayerURIs(l.getBaseLayerURIs());
                }
            }

            xDef.setStatus(settings.getStatus());
            xDef.setSrcBranch(srcBranchUrl);

            xDef.setRepositoryRevision(flow.getRevision());

            try {
                fileBytes = XmlUtils.saveXmlPretty(xDef);
            } catch (IOException ex) {
                return logger.fatal(new RadixError("Unable to generate release description file", ex));
            }
            flow.setReleaseDescriptionFileContent(fileBytes);
        }
        try {
            int cnt = editor.openDirs(releaseUrl);
            String releaseFileUrl = SvnPath.append(releaseUrl, "release.xml");
            editor.appendFile(releaseFileUrl, fileBytes);
            editor.closeDirs(cnt);
            return true;
        } catch (RadixSvnException e) {
            return logger.fatal(e);
        }
    }

    private boolean addBranchDescriptionFile(SVNRepositoryAdapter.Editor editor, byte[] branchDescriptionFile, String releaseUrl, EReleaseDirStatus status) {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(branchDescriptionFile);
        try {
            final Branch.Saver saver = new Branch.Saver(BranchDocument.Factory.parse(inputStream).getBranch());
            switch (status) {
                case SOURCE: {
                    saver.setLastReleaseVersion(settings.getNumber());
                    saver.setTitle(settings.getBranch().getTitle());
                    saver.setFormatVersion(settings.getBranch().getFormatVersion());
                    final byte[] bytes = saver.save();
                    try {
                        editor.openDir(releaseUrl);
                        final String releaseFileUrl = SvnPath.append(releaseUrl, "branch.xml");
                        editor.modifyFile(releaseFileUrl, bytes);
                        editor.closeDir();
                        return true;
                    } catch (RadixSvnException e) {
                        return logger.fatal(e);
                    }
                }
                case RELEASE: {
                    return true;
                }
                case RELEASE_SOURCE: {
                    saver.setType(ERepositoryBranchType.OFFSHOOT);
                    saver.setLastReleaseVersion(null);
                    saver.setBaseReleaseVersion(settings.getNumber());
                    saver.setTitle(settings.getBranch().getTitle());
                    saver.setFormatVersion(settings.getBranch().getFormatVersion());
                    final byte[] bytes = saver.save();
                    try {
                        int cnt = editor.openDirs(releaseUrl);
                        final String releaseFileUrl = SvnPath.append(releaseUrl, "branch.xml");
                        editor.appendFile(releaseFileUrl, bytes);
                        editor.closeDirs(cnt);
                        return true;
                    } catch (RadixSvnException e) {
                        return logger.fatal(e);
                    }
                }
                default:
                    return logger.fatal("Unknown branch description context");
            }
        } catch (XmlException | IOException e) {
            return logger.fatal("Invalid branch description file format");
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
    }

    private boolean processLayer(final SVNRepositoryAdapter.Editor editor, final LayerInfo info, final String destLayerUrl, boolean forSrc, SvnConnectionType connectionType) {
        if (!info.layer.isReadOnly()) {
            byte[] layerXmlContent = forSrc ? info.layerXmlContentForSrc : info.layerXmlContent;

            if (layerXmlContent == null) {
                if (!flow.isPatchRelease()) {
                    return logger.fatal("Unable to convert description file of layer " + info.layer.getQualifiedName() + " to release compatible format");
                }
            } else {
                try {

                    final String layerXmlUrl = SvnPath.append(destLayerUrl, "layer.xml");
                    int cnt = 1;

                    if (connectionType.equals(SvnConnectionType.HTTP)) {
                        cnt = editor.appendDirs(destLayerUrl);
                    } else if (connectionType.equals(SvnConnectionType.SVN)) {
                        editor.openDir(destLayerUrl);
                    }
                    editor.modifyFile(layerXmlUrl, layerXmlContent);
                    logger.message("Layer description file saved to " + layerXmlUrl);

                    if (!destLayerUrl.contains("releases.src")) {
                        final String directoryLayerName = "directory-layer.xml";
                        final String directoryLayerUrl = SvnPath.append(destLayerUrl, directoryLayerName);
                        final DirectoryFile directoryLayerFile = DirectoryFile.Factory.loadFromDirectoryLayer(info.layer, layerXmlContent);
                        final byte[] directoryLayerBytes = directoryLayerFile.getBytes();
                        if (directoryLayerBytes != null) {
                            SvnFile file = info.fileList.getLayerRoot().findChild(directoryLayerName);
                            if (file == null || destLayerUrl.contains("releases.src")) {
                                if (file != null && !file.external) {
                                    editor.deleteEntry(directoryLayerUrl, file.sourceRevision);
                                }
                                editor.appendFile(directoryLayerUrl, directoryLayerBytes);
                                logger.message("directory-layer.xml was added: " + directoryLayerUrl);
                            } else {
                                editor.modifyFile(directoryLayerUrl, directoryLayerBytes);
//                                editor.deleteEntry(directoryLayerUrl, file.sourceRevision);
//                                editor.appendFile(directoryLayerUrl, );
                                logger.message("directory-layer.xml was updated: " + directoryLayerUrl);
                            }
                        } else {
                            logger.error("directory-layer.xml was not updated");
                        }
                    }
                    editor.closeDirs(cnt);

                    return true;
//                } catch (NoSuchAlgorithmException ex) {
//                    return logger.fatal(ex);
                } catch (RadixSvnException ex) {
                    return logger.fatal(ex);
                }
            }
        }
        return true;
    }
}
