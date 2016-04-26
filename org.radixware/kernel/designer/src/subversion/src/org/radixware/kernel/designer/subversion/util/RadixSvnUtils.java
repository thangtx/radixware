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

package org.radixware.kernel.designer.subversion.util;

import java.io.File;
import java.io.IOException;
import org.netbeans.modules.subversion.FileInformation;
import org.netbeans.modules.subversion.FileStatusCache;
import org.netbeans.modules.subversion.Subversion;
import org.netbeans.modules.subversion.SubversionVCS;
import org.netbeans.modules.subversion.client.SvnClient;
import org.netbeans.modules.versioning.spi.VersioningSupport;
import org.netbeans.modules.versioning.spi.VersioningSystem;
import org.radixware.kernel.common.repository.Branch;
import org.tigris.subversion.svnclientadapter.ISVNStatus;
import org.tigris.subversion.svnclientadapter.SVNClientException;


public class RadixSvnUtils {

    public static final class Factory {

        public static final RadixSvnUtils newInstance(Branch branch) throws SVNClientException {
            final File dir = branch.getDirectory();
            final SvnClient client = findClient(dir);
            if (client == null) {
                return null;
            } else {
                return new RadixSvnUtils(client);
            }
        }
    }
    private final SvnClient client;

    private RadixSvnUtils(SvnClient client) {
        this.client = client;
    }

    private static SvnClient findClient(final File file) throws SVNClientException {
        final VersioningSystem versioningSystem = VersioningSupport.getOwner(file);
        if (versioningSystem == null) {
            return null; // non VCS project
        }
        if (!(versioningSystem instanceof SubversionVCS)) {
            return null; // not supported VCS
        }

        return Subversion.getInstance().getClient(file);
    }

    /**
     * @return true if versioning status of specified file or folder (recursive)
     * is up to date or it is not under SVN, false otherwise.
     */
    public boolean isUpToDate(final File file) throws IOException, SVNClientException {
        final FileStatusCache fileStatusCache = Subversion.getInstance().getStatusCache();

        final ISVNStatus[] repositoryStatuses = client.getStatus(file, true /*
                 * recursive
                 */, false /*
                 * extra information
                 */, true /*
                 * contact server
                 */);
        for (ISVNStatus repositoryStatus : repositoryStatuses) {
            final File subFile = repositoryStatus.getFile();
            FileStatusCache.RepositoryStatus statusInfo = new FileStatusCache.RepositoryStatus(repositoryStatus, null);
            final FileInformation info = fileStatusCache.refresh(subFile, statusInfo);
            final int status = info.getStatus();
            if ((status & FileInformation.STATUS_REMOTE_CHANGE) != 0 || (status & FileInformation.STATUS_VERSIONED_CONFLICT) != 0) {
                return false;
            }
        }
        return true;
    }

    public static void refreshStatus(File... files) {
        final FileStatusCache statusCache = Subversion.getInstance().getStatusCache();
        statusCache.refreshAsync(files);
    }
//    public void addFile(final File file) throws SVNClientException {
//        client.addFile(file);
//    }
//
//    public void revert(final File file) throws SVNClientException {
//        client.revert(file, false /*recursive*/);
//
//    }
//
//    public void commitDirectory(final File dir, final String message) throws SVNClientException {
//        client.commit(new File[]{dir}, message, true /*recursive*/);
//    }
//    public static void notifyChanged(File file) {
//        //Subversion.getInstance().versionedFilesChanged();
//        final FileStatusCache fileStatusCache = Subversion.getInstance().getStatusCache();
//        fileStatusCache.refresh(file, FileStatusCache.REPOSITORY_STATUS_UNKNOWN);
//    }
//
//    public void setIgnorePatterns(File dir, List<String> patterns) throws SVNClientException {
//        client.setIgnoredPatterns(dir, patterns);
//    }
//
//    public void addDirectory(final File dir) throws SVNClientException {
//        client.addDirectory(dir, true /*recursive*/);
//    }
//
//    /**
//     * @return last changed revision of specified file or -1 if impossible to determine
//     */
//    public long getRepositoryRevision(final File file) throws SVNClientException {
//        //final FileStatusCache fileStatusCache = Subversion.getInstance().getStatusCache();
//
//        final ISVNStatus[] repositoryStatuses = client.getStatus(new File[]{file});
//
//        for (ISVNStatus repositoryStatus : repositoryStatuses) {
//            if (Utils.equals(file, repositoryStatus.getFile())) {
//                return repositoryStatus.getLastChangedRevision().getNumber();
//            }
//        }
//        return -1;
//    }
//    public boolean isUnderVersionControl(File file) throws SVNClientException {
//        ISVNInfo info = getInfo(file);
//        if ((info.getNodeKind() != SVNNodeKind.DIR && info.getNodeKind() != SVNNodeKind.FILE) || info.getUrl() == null || info.getRevision() == null || info.getRevision().getNumber() == 0) {
//            return false;
//        } else {
//            return true;
//        }
//
//    }
//
//    public void commitFiles(final File[] files, final String message) throws SVNClientException {
//        if (files.length > 0) {
//            client.commit(files, message, false /*recursive*/);
//        }
//    }
//    public void copy(final SVNUrl src, final SVNUrl dest, String message, SVNRevision revision) throws SVNClientException {
//        client.copy(src, dest, message, revision);
//    }
//
//    public void copy(final File src, final SVNUrl dest, String message, boolean recurs) throws SVNClientException {
//        client.doImport(src, dest, message, recurs);
//    }
//    public ISVNInfo getInfo(SVNUrl url) throws SVNClientException {
//        return client.getInfo(url);
//    }
//
//    public ISVNDirEntry[] getList(SVNUrl url, boolean rec) throws SVNClientException {
//        return client.getList(url, SVNRevision.HEAD, rec);
//    }
//
//    public InputStream getContent(SVNUrl url) throws SVNClientException {
//        return client.getContent(url, SVNRevision.HEAD);
//    }
//
//    public ISVNInfo getInfo(File file) throws SVNClientException {
//        return client.getInfo(file);
//    }
//
//    public ISVNStatus[] getStatus(File file) throws SVNClientException {
//        return client.getStatus(new File[]{file});
//    }
//    public void mkdir(SVNUrl url, String message) throws SVNClientException {
//        client.mkdir(url, message);
//    }
//
//    public void mkdirs(SVNUrl url, String message) throws SVNClientException {
//        client.mkdir(url, true, message);
//    }
//
//    public void checkout(SVNUrl source, File dest) throws SVNClientException {
//        client.checkout(source, dest, SVNRevision.HEAD, true);
//    }
//
//    public void delete(File target) throws SVNClientException {
//        client.remove(new File[]{target}, false);
//    }
//
//    public void delete(SVNUrl target) throws SVNClientException {
//        client.remove(new SVNUrl[]{target}, "");
//    }
//    public static VersioningSystem findVersioningSystem(RadixObject radixObject) {
//        final Branch branch = radixObject.getBranch();
//        if (branch != null) {
//            final File branchFile = branch.getFile();
//            return VersioningSupport.getOwner(branchFile);
//        } else {
//            return null;
//        }
//    }
}
