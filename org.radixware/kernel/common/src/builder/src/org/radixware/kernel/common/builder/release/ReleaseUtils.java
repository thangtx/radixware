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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;

import org.radixware.kernel.common.dialogs.AuthenticationCancelledException;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.enums.ESvnAuthType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.client.ISvnFSClient;
import org.radixware.kernel.common.svn.client.ISvnFSClient.ISvnStatusCallback;
import org.radixware.kernel.common.svn.client.SvnEntry;
import org.radixware.kernel.common.svn.client.SvnPath;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.product.DistributiveGiveEnum;
import org.radixware.schemas.product.DistributiveGiveEnum.Enum;
import org.radixware.schemas.product.LayersType;
import org.radixware.schemas.product.ReleaseDocument;
import org.radixware.schemas.product.Upgrade;

public class ReleaseUtils {

    static class ReleaseComponentManager {

        private class Layer {

            final DistributiveGiveEnum.Enum give;
            final String uri;
            List<Segment> segments = null;

            private class Segment {

                final DistributiveGiveEnum.Enum give;
                final String name;
                List<Layer.Segment.Module> modules = null;

                private class Module {

                    final Id id;
                    final DistributiveGiveEnum.Enum give;

                    public Module(Id id, Enum give) {
                        this.id = id;
                        this.give = give;
                    }
                }

                private void addModule(Id id, Enum give) {
                    if (modules == null) {
                        modules = new LinkedList<>();
                    }
                    final Module m = new Module(id, give);
                    modules.add(m);
                }

                private Segment(String name, DistributiveGiveEnum.Enum give) {
                    this.name = name;
                    this.give = give;
                }
            }

            private Segment addSegment(String name, DistributiveGiveEnum.Enum give) {
                final Segment s = new Segment(name, give);
                if (segments == null) {
                    segments = new LinkedList<>();
                }
                segments.add(s);
                return s;
            }

            Layer(String uri, DistributiveGiveEnum.Enum give) {
                this.uri = uri;
                this.give = give;
            }
        }
        List<Layer> layers = null;

        public void acceptDistributiveDescription(Upgrade xDef) {
            final LayersType xLayers = xDef.getLayers();
            if (xLayers != null) {
                for (LayersType.Layer xLayer : xLayers.getLayerList()) {
                    final Layer l = new Layer(xLayer.getUri(), xLayer.getGive());
                    for (LayersType.Layer.Segment xSegment : xLayer.getSegmentList()) {
                        final Layer.Segment s = l.addSegment(xSegment.getName(), xSegment.getGive());
                        for (LayersType.Layer.Segment.Module xModule : xSegment.getModuleList()) {
                            final Id moduleId = Id.Factory.loadFrom(xModule.getId());
                            final DistributiveGiveEnum.Enum give = xModule.getGive();
                            s.addModule(moduleId, give);
                        }
                    }
                    if (layers == null) {
                        layers = new LinkedList<>();
                    }
                    layers.add(l);
                }
            }
        }

        public boolean sourcesRequired(String layerURI, Id moduleId) {
            if (layers == null) {
                return true;
            } else {
                for (Layer l : layers) {
                    if (Utils.equals(l.uri, layerURI)) {
                        if (l.segments != null) {
                            for (Layer.Segment s : l.segments) {
                                if (Utils.equals(s.name, ERepositorySegmentType.ADS.getValue())) {
                                    if (s.modules != null) {
                                        for (Layer.Segment.Module m : s.modules) {
                                            if (m.id == moduleId) {
                                                if (m.give != null) {
                                                    return m.give == DistributiveGiveEnum.BIN_AND_SRC;
                                                } else {
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    if (s.give != null) {
                                        return s.give == DistributiveGiveEnum.BIN_AND_SRC;
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                        if (l.give != null) {
                            return l.give == DistributiveGiveEnum.BIN_AND_SRC;
                        } else {
                            return true;
                        }
                    }
                }
                return true;
            }
        }
    }

    public static class StatusInfo {

        private List<File> modifiedFiles;
        private List<File> conflictedFiles;
        private List<File> outOfDateFiles;
        private List<File> deletedFiles;
        private List<File> addedFiles;
        private List<File> missingFiles;
        private List<File> obstructedFiles;
        private List<File> replacedFiles;

        private void modified(File path) {
            if (modifiedFiles == null) {
                modifiedFiles = new ArrayList<>();
            }
            modifiedFiles.add(path);
        }

        private void conflicted(File path) {
            if (conflictedFiles == null) {
                conflictedFiles = new ArrayList<>();
            }
            conflictedFiles.add(path);
        }

        private void outOfDate(File path) {
            if (outOfDateFiles == null) {
                outOfDateFiles = new ArrayList<>();
            }
            outOfDateFiles.add(path);
        }

        private void deleted(File path) {
            if (deletedFiles == null) {
                deletedFiles = new ArrayList<>();
            }
            deletedFiles.add(path);
        }

        private void added(File path) {
            if (addedFiles == null) {
                addedFiles = new ArrayList<>();
            }
            addedFiles.add(path);
        }

        private void missing(File path) {
            if (missingFiles == null) {
                missingFiles = new ArrayList<>();
            }
            missingFiles.add(path);
        }

        private void obstructed(File path) {
            if (obstructedFiles == null) {
                obstructedFiles = new ArrayList<>();
            }
            obstructedFiles.add(path);
        }

        private void replaced(File path) {
            if (replacedFiles == null) {
                replacedFiles = new ArrayList<>();
            }
            replacedFiles.add(path);
        }

        public boolean isConflicted() {
            return conflictedFiles != null;
        }

        public boolean isOutOfDate() {
            return outOfDateFiles != null;
        }

        public boolean isAdded() {
            return addedFiles != null;
        }

        public boolean isDeleted() {
            return deletedFiles != null;
        }

        public boolean isMissing() {
            return missingFiles != null;
        }

        public boolean isModified() {
            return modifiedFiles != null;
        }

        public boolean isObstructed() {
            return obstructedFiles != null;
        }

        public boolean isReplaced() {
            return replacedFiles != null;
        }

        public List<File> getOutOfDate() {
            if (outOfDateFiles == null) {
                return Collections.emptyList();
            } else {
                return Collections.unmodifiableList(outOfDateFiles);
            }
        }
    }

    static class SystemDirectory {

        final String path;
        final SystemDirectory parent;
        boolean create = false;
        boolean isFinal = false;
        final List<SystemDirectory> children = new LinkedList<>();

        public SystemDirectory(String path) {
            this.path = path;
            this.parent = null;
        }

        public SystemDirectory(SystemDirectory parent, String name) {
            this.path = SvnPath.append(parent.path, name);
            parent.children.add(this);
            this.parent = parent;
        }

        public SystemDirectory(SystemDirectory parent, String name, boolean isFinal) {
            this(parent, name);
            this.isFinal = isFinal;
        }

        boolean prepare(ReleaseFlow flow, long revision) throws Exception {
            if (!this.create) {
                this.create = !pathExistsAsDirectory(flow, path, true, revision);
            }
            if (isFinal && !this.create) {
                return flow.getSettings().getLogger().fatal("Directory " + path + " is already exists");
            }
            for (SystemDirectory dir : children) {
                if (!dir.prepare(flow, revision)) {
                    return false;
                }
            }
            return true;
        }

        boolean apply(SVNRepositoryAdapter.Editor editor) throws RadixSvnException {
            try {
                if (this.create) {
                    editor.appendDir(path);
                } else {
                    editor.openDir(path);
                }
                for (SystemDirectory dir : children) {
                    if (!dir.apply(editor)) {
                        return false;
                    }
                }
            } finally {
                editor.closeDir();
            }
            return true;
        }
    }

    static class ReleaseInfo {

        final SystemDirectory releaseDir;
        final SystemDirectory releaseSrcDir;
        final SystemDirectory scriptsDir;
        String prevReleaseUrl = null;
        long prevReleaseRevision = -1;

        public ReleaseInfo(SystemDirectory releaseDir, SystemDirectory releaseSrcDir, SystemDirectory scriptsDir) {
            this.releaseDir = releaseDir;
            this.releaseSrcDir = releaseSrcDir;
            this.scriptsDir = scriptsDir;
        }
    }
    private final String userName;
    private final ESvnAuthType authType;
    private final String sshKeyFile;

    public ReleaseUtils(String userName, ESvnAuthType authType, String sshKeyFile) {
        this.userName = userName;
        this.authType = authType;
        this.sshKeyFile = sshKeyFile;
    }

    public final String[] getReleaseLocation(ISvnFSClient client, Branch branch, String releaseVersion) throws RadixSvnException {
        String[] result = new String[1];
        SVNRepositoryAdapter repository = getReleasesDirLocation(client, branch, result);
        try {
            try {
                return new String[]{repository.getRepositoryRoot(), SvnPath.append(result[0], releaseVersion)};
            } catch (RadixSvnException ex) {
                return null;
            }
        } finally {
            if (repository != null) {
                repository.close();
            }
        }
    }

    private SVNRepositoryAdapter getReleasesDirLocation(ISvnFSClient client, Branch branch, String[] result) throws RadixSvnException {
        File branchDir = branch.getDirectory();
        if (branchDir == null) {
            throw new RadixSvnException("Unable to ubtain svn repository for virtual branch");
        }
        SVNRepositoryAdapter repository = null;
        try {
            repository = SVNRepositoryAdapter.Factory.newInstance(client, branch.getDirectory(), userName, SVN.getForAuthType(authType), sshKeyFile);
        } catch (RadixSvnException ex) {
            if (ex.isAuthenticationCancelled()) {
                throw ex;
            }
            throw new RadixSvnException("Unable to ubtain svn repository for branch " + branch.getDirectory().getAbsolutePath(), ex);
        } catch (RadixError ex) {
            throw new RadixSvnException("Unable to ubtain svn repository for branch " + branch.getDirectory().getAbsolutePath(), ex);
        }

        String branchPath = null;
        try {
            final ISvnFSClient.ISvnStatus status = client.getSingleStatus(branch.getDirectory());
            String branchUrl = status.getUrlString();
            branchPath = SvnPath.getRelativePath(repository.getRepositoryRoot(), branchUrl);
        } catch (RadixSvnException ex) {
            try {
                throw new RadixSvnException("Unable to ubtain svn url of branch " + branch.getDirectory().getAbsolutePath(), ex);
            } finally {
                repository.close();
            }
        }

        String basisUrl = SvnPath.removeTail(SvnPath.removeTail(branchPath));
        result[0] = SvnPath.append(basisUrl, "releases");
        return repository;
    }

    public final List<String> listReleaseVersions(ISvnFSClient client, Branch branch) throws RadixSvnException {
        String[] releasesDir = new String[1];
        SVNRepositoryAdapter repository = getReleasesDirLocation(client, branch, releasesDir);
        try {
            if (releasesDir[0] == null) {
                return Collections.emptyList();
            } else {
                try {
                    final List<String> list = new LinkedList<String>();

                    String path = releasesDir[0];
                    long revision = repository.getLatestRevision();
                    repository.getDir(path, revision, new SVNRepositoryAdapter.EntryHandler() {

                        @Override
                        public void accept(SvnEntry entry) throws RadixSvnException {
                            if (entry.getKind() == SvnEntry.Kind.DIRECTORY && ReleaseUtils.isValidReleaseName(entry.getName())) {
                                list.add(entry.getName());
                            }
                        }
                    });
                    Collections.sort(list, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            int[] arr1 = ReleaseUtils.parseReleaseVersion(o1);
                            int[] arr2 = ReleaseUtils.parseReleaseVersion(o2);
                            int len = Math.min(arr1.length, arr2.length);

                            for (int i = 0; i < len; i++) {
                                if (arr1[i] > arr2[i]) {
                                    return 1;
                                } else if (arr1[i] < arr2[i]) {
                                    return -1;
                                }
                            }

                            if (arr1.length == arr1.length) {
                                return 0;
                            } else {
                                return arr1.length < arr2.length ? 1 : -1;
                            }
                        }
                    });
                    return list;
                } catch (RadixSvnException ex) {
                    return Collections.emptyList();
                }
            }
        } finally {
            if (repository != null) {
                repository.close();
            }
        }
    }

    public static boolean isValidReleaseName(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '.') {
                if (i == 0) {
                    return false;
                }
                if (i >= s.length() - 1) {
                    return false;
                } else {
                    if (!Character.isDigit(s.charAt(i + 1))) {
                        return false;
                    }
                }
            } else if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static int[] parseReleaseVersion(String version) {
        if (!isValidReleaseName(version)) {
            return null;
        } else {
            String[] versions = version.split("\\.");
            int result[] = new int[versions.length];
            for (int i = 0; i < versions.length; i++) {
                result[i] = Integer.parseInt(versions[i]);
            }
            return result;
        }
    }

    public static String mergeReleaseVersion(int[] version) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < version.length; i++) {
            if (i > 0) {
                builder.append('.');
            }
            builder.append(version[i]);
        }
        return builder.toString();

    }

    static boolean pathExistsAsDirectory(ReleaseFlow flow, String url, boolean dir, long revision) throws Exception {
        final boolean isDir = flow.getRepository().isDir(url, revision);
        if (isDir) {
            return true;
        } else {
            final boolean isFile = flow.getRepository().isFile(url, revision);
            if (!isFile) {
                return false;
            } else {
                throw new Exception("Unable to create directory" + url + ". Node already exists as a file");
            }
        }
    }

//    public static List<String> listAvailableClients(SVNRepository repository, long revision) throws SVNException {
//        final List<String> clients = new LinkedList<String>();
//
//        repository.getDir("clients", revision, null, new ISVNDirEntryHandler() {
//
//            @Override
//            public void handleDirEntry(SVNDirEntry dirEntry) throws SVNException {
//                if (dirEntry.getKind() == SVNNodeKind.DIR) {
//                    clients.add(dirEntry.getName());
//                }
//            }
//        });
//        return clients;
//    }
//    public static List<String> listAvailableClients(SVNRepositoryAdapter repository, Branch branch, long revision) throws RadixSvnException {
//        final List<String> clients = new LinkedList<>();
//        String branchPath = getBranchPath(branch, repository);
//        repository.getDir(SvnPath.append(SvnPath.removeTail(SvnPath.removeTail(branchPath)), "clients"), revision, new SVNRepositoryAdapter.EntryHandler() {
//
//            @Override
//            public void accept(SvnEntry entry) throws RadixSvnException {
//                if (entry.getKind() == SvnEntry.Kind.DIRECTORY) {
//                    clients.add(entry.getName());
//                }
//            }
//        });
//        return clients;
//    }
//
//    public static List<String> listAvailableClients(ReleaseSettings settings) throws RadixSvnException, AuthenticationCancelledException {
//        SVNRepositoryAdapter repository = null;
//        try {
//            repository = SVNRepositoryAdapter.Factory.newInstance(settings.getBranch().getDirectory(), settings.getUserName(), settings.getAuthType(), settings.getSSHKeyFile());//RadixSVNRepository.newInstance(settings.getBranch(), settings.getUserName(), settings.getAuthType(), settings.getSSHKeyFile());
//
//            return listAvailableClients(repository, settings.getBranch(), repository.getLatestRevision());
//        } finally {
//            if (repository != null) {
//                repository.close();
//            }
//        }
//    }
    static StatusInfo getStatusInfo(ISvnFSClient clientManager, long revision, File file) throws ISvnFSClient.SvnFsClientException {
        final StatusInfo info = new StatusInfo();

        clientManager.getStatus(file, true, true, true, true, new ISvnStatusCallback() {

            @Override
            public void doStatus(String string, ISvnFSClient.ISvnStatus status) {
                ISvnFSClient.SvnStatusKind contentsStatus = status.getTextStatus();
                if (contentsStatus.isModified()) {
                    info.modified(status.getFile());
                } else if (contentsStatus.isConfilicted()) {
                    info.conflicted(status.getFile());
                } else if (contentsStatus.isDeleted()) {
                    info.deleted(status.getFile());
                } else if (contentsStatus.isAdded()) {
                    info.added(status.getFile());
                } else if (contentsStatus.isMissing() || contentsStatus.isIncomplete()) {
                    info.missing(status.getFile());
                } else if (contentsStatus.isObstructed()) {
                    info.obstructed(status.getFile());
                } else if (contentsStatus.isReplaced()) {
                    info.replaced(status.getFile());
                }
                if (!status.getRepositoryPropStatus().isNone()) {
                    String userDir = System.getProperty("user.dir");
                    if (!userDir.equals(status.getFile().getAbsolutePath())) {
                        info.outOfDate(status.getFile());
                    }
                }
            }
        });

        return info;
    }

    public static boolean isUrlExists(SVNRepositoryAdapter repository, String relativeUrl, long revision) throws RadixSvnException {
        SvnEntry.Kind kind = repository.checkPath(relativeUrl, revision);
        return kind == SvnEntry.Kind.FILE || kind == SvnEntry.Kind.DIRECTORY;
    }

    private static class DistribNumber {

        private final String src;
        private boolean isValid;
        private int version;
        private int number;
        private String releaseNumberStr;

        private DistribNumber(String number) {
            this.src = number;
            int index = number.indexOf("-");
            isValid = true;
            if (index <= 0) {
                isValid = false;
            } else {
                try {
                    this.number = Integer.parseInt(number.substring(0, index));
                    int endindex = number.indexOf('v');
                    if (endindex > 0) {
                        releaseNumberStr = number.substring(index + 1, endindex);
                        try {
                            version = Integer.parseInt(number.substring(endindex + 1));
                        } catch (NumberFormatException e) {
                            isValid = false;
                        }
                    } else {
                        version = -1;
                        releaseNumberStr = number.substring(index + 1);
                    }
                } catch (NumberFormatException e) {
                    isValid = false;
                }
            }
        }
    }

    public static Upgrade readDistributiveSettings(SVNRepositoryAdapter repository, String baseDir, String clientURI, final String baseReleaseVersion) throws RadixSvnException {
        String distribDirPath = SvnPath.append(baseDir, SvnPath.append(SvnPath.append("clients", clientURI), "distributives"));
        final List<String> candidates = new LinkedList<String>();
        final long revision = repository.getLatestRevision();
        SvnEntry.Kind kind = repository.checkPath(distribDirPath, revision);
        if (kind != SvnEntry.Kind.DIRECTORY) {
            return null;
        }
        repository.getDir(distribDirPath, revision, new SVNRepositoryAdapter.EntryHandler() {

            @Override
            public void accept(SvnEntry svnde) throws RadixSvnException {
                if (svnde.getKind() == SvnEntry.Kind.DIRECTORY && svnde.getName().contains(baseReleaseVersion)) {
                    candidates.add(svnde.getName());
                }
            }
        });

        if (candidates.isEmpty()) {
            return null;
        }
        for (Iterator<String> iter = candidates.iterator(); iter.hasNext();) {
            String value = iter.next();
            DistribNumber n = new DistribNumber(value);
            if (!Utils.equals(n.releaseNumberStr, baseReleaseVersion)) {
                iter.remove();
            }
        }

        if (candidates.isEmpty()) {
            return null;
        }

        Collections.sort(candidates, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                DistribNumber n1 = new DistribNumber(o1);
                DistribNumber n2 = new DistribNumber(o2);
                if (n1.number == n2.number) {
                    if (n1.version == n2.version) {
                        return 0;
                    } else if (n1.version > n2.version) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else {
                    if (n1.number > n2.number) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
        });

        String distDir = candidates.get(candidates.size() - 1);

        String distXmlPath = SvnPath.append(SvnPath.append(distribDirPath, distDir), "upgrade.xml");

        byte[] bytes = SVN.getFile(repository, distXmlPath, revision);
        if (bytes != null && bytes.length > 0) {
            try {//DistributiveDocument.Factory
                return Upgrade.Factory.parse(new ByteArrayInputStream(bytes));//.getDistributive();
            } catch (XmlException ex) {
                return null;
            } catch (IOException ex) {
                return null;
            }
        } else {
            return null;
        }
    }

//    public static String getBranchUrl(File branchDir, SVNRepositoryAdapter repository) throws RadixSvnException {
//        SVNClientManager manager = SVNClientManager.newInstance(new DefaultSVNOptions(), repository.getAuthenticationManager());
//        SVNStatus status = manager.getStatusClient().doStatus(branchDir, false);
//        return status.getURL().toString();
//    }
//
//    public static String getReleasePath(Branch branch, SVNRepositoryAdapter repository, String releaseVersion) throws RadixSvnException {
//        String branchPath = getBranchPath(branch, repository);
//        return SvnPath.append(SvnPath.append(SvnPath.removeTail(SvnPath.removeTail(branchPath)), "releases"), releaseVersion);
//    }
//
//    public static String getBranchUrl(Branch branch, SVNRepositoryAdapter repository) throws RadixSvnException {
//        return getBranchUrl(branch.getDirectory(), repository);
//    }
//
//    public static String getBranchPath(File branchDir, SVNRepositoryAdapter repository) throws RadixSvnException {
//        SVNURL rootUrl = repository.getRepositoryRoot();
//        String branchUrl = getBranchUrl(branchDir, repository);
//        return SvnPath.getRelativePath(rootUrl.toDecodedString(), branchUrl);
//    }
//    public static String getBranchPath(Branch branch, SVNRepositoryAdapter repository) throws RadixSvnException {
//        return getBranchPath(branch.getDirectory(), repository);
//    }
//
//    public static String getBasisPath(Branch branch, SVNRepositoryAdapter repository) throws RadixSvnException {
//        String branchPath = getBranchPath(branch, repository);
//        return SvnPath.removeTail(SvnPath.removeTail(branchPath));
//    }
    public static long getReleaseSrcRevision(SVNRepositoryAdapter repository, String releaseURI, long currentRevision) throws RadixSvnException {
        long fromRevision = -1;
        byte[] bytes = SVN.getFile(repository, SvnPath.append(releaseURI, "release.xml"), currentRevision);
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            ReleaseDocument xDoc;
            try {
                xDoc = ReleaseDocument.Factory.parse(in);
                if (xDoc.getRelease().getRepositoryRevision() != null) {
                    fromRevision = xDoc.getRelease().getRepositoryRevision().longValue();
                }
            } catch (XmlException | IOException ex) {
                Logger.getLogger(ReleaseUtils.class.getName()).log(Level.FINE, ex.getMessage(), ex);
            }

        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(ReleaseUtils.class.getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
        return fromRevision;
    }

//    public static long getReleaseCreateRevision(SVNRepositoryAdapter repository, String releaseURI, long currentRevision) throws RadixSvnException {
//        return getDirCreateRevision(repository, releaseURI, currentRevision);
//    }
//
//    public static long getDirCreateRevision(SVNRepositoryAdapter repository, String directoryPath, long currentRevision) throws RadixSvnException {
//        final long[] fromRevision = new long[]{-1};
//
//        repository.log(new String[]{directoryPath}, 0, currentRevision, false, true, new ISVNLogEntryHandler() {
//            @Override
//            public void handleLogEntry(SVNLogEntry svnle) throws SVNException {
//                long revision = svnle.getRevision();
//                if (fromRevision[0] < 0 || fromRevision[0] > revision) {
//                    fromRevision[0] = revision;
//                }
//                System.out.println(String.valueOf(revision));
//            }
//        });
//        return fromRevision[0];
//    }
}
