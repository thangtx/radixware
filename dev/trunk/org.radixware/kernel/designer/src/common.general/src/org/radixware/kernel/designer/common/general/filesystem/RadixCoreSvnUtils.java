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
package org.radixware.kernel.designer.common.general.filesystem;

import java.io.File;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.RadixObject;

import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.client.ISvnFSClient;
import org.radixware.kernel.common.svn.client.SvnAuthType;

public class RadixCoreSvnUtils {

    /**
     * @return true if versioning status of specified file or folder (recursive)
     * is up to date or it is not under SVN, false otherwise.
     */
    public static SVNRepositoryAdapter getRepository(RadixObject module) throws RadixSvnException {
        final String userName = SVN.SVNPreferences.getUserName();
        final SvnAuthType authType = SVN.SVNPreferences.getAuthType();
        final String sshKeyFile = SVN.SVNPreferences.getSSHKeyFilePath();
        final File branchDir = module.getBranch().getDirectory();
        ISvnFSClient client = getFsClient(module);

        try {
            return SVNRepositoryAdapter.Factory.newInstance(client, branchDir, userName, authType, sshKeyFile);
        } catch (RadixSvnException ex) {
            if (ex.isAuthenticationCancelled()) {
                Logger.getLogger(RadixCoreSvnUtils.class.getName()).info("SVN authentication cancelled by user");
            } else {
                throw ex;
            }
            return null;
        }
    }

    public static ISvnFSClient getFsClient(RadixObject module) {
        final File branchDir = module.getBranch().getDirectory();

        try {
            final Class c = Thread.currentThread().getContextClassLoader().loadClass("org.radixware.kernel.designer.subversion.util.SvnBridge");
            if (c != null) {
                final Method method = c.getMethod("getClientAdapter", File.class);
                if (method != null) {
                    return (ISvnFSClient) method.invoke(null, branchDir);
                }
            }
        } catch (Throwable e) {

        }
        return null;
    }

    public static boolean hasLocalModifications(ISvnFSClient client, File file, long revision) throws ISvnFSClient.SvnFsClientException {
        class CancelProcessException extends RuntimeException {
        }

        try {
            client.getStatus(file, false, true, false, false, new ISvnFSClient.ISvnStatusCallback() {

                @Override
                public void doStatus(String string, ISvnFSClient.ISvnStatus status) {
                    ISvnFSClient.SvnStatusKind contentsStatus = status.getTextStatus();

                    if (contentsStatus.isModified()
                            || contentsStatus.isConfilicted()
                            || contentsStatus.isDeleted()
                            || contentsStatus.isAdded()
                            || contentsStatus.isMissing()
                            || contentsStatus.isIncomplete()
                            || contentsStatus.isObstructed()
                            || contentsStatus.isReplaced()) {
                        throw new CancelProcessException();
                    }
                }
            }
            );
        } catch (CancelProcessException e) {
            return true;
        }

        return false;
    }

    public static boolean isUpToDate(ISvnFSClient client, File file, long revision) throws ISvnFSClient.SvnFsClientException {
        class CancelProcessException extends RuntimeException {
        }

        try {
            client.getStatus(file, false, true, true, false, new ISvnFSClient.ISvnStatusCallback() {

                @Override
                public void doStatus(String string, ISvnFSClient.ISvnStatus status) {
                    ISvnFSClient.SvnStatusKind contentsStatus = status.getTextStatus();

                    if (!status.getRepositoryPropStatus().isNone() || !status.getRepositoryTextStatus().isNone()) {
                        throw new CancelProcessException();
                    }
                }
            }
            );
        } catch (CancelProcessException e) {
            return false;
        }

        return true;
    }

    public static void refreshStatus(File... files) {
        try {
            final Class c = Thread.currentThread().getContextClassLoader().loadClass("org.radixware.kernel.designer.subversion.util.RadixSvnUtils");
            if (c != null) {
                final Method method = c.getMethod("refreshStatus", File[].class);
                if (method != null) {
                    method.invoke(null, files);
                }
            }
        } catch (Throwable e) {
            //ignore if not consistent
        }

    }
//    public interface SvnAction {
//        boolean execute() throws SVNException;
//    }
//    
//    public static boolean execute(SvnAction action) {
//        for (int i = 1; true; i++) {
//            try {
//                return action.execute();
//            } catch (SVNException ex) {
//                
//                final SVNErrorMessage errorMessage = ex.getErrorMessage();
//                if (errorMessage == null
//                        || (errorMessage.getErrorCode() != SVNErrorCode.RA_SVN_MALFORMED_DATA
//                        && errorMessage.getErrorCode() != SVNErrorCode.RA_SVN_IO_ERROR)) {
//                    return false;
//                }
//                try {
//                    Thread.sleep(SVN.getSvnTimeoutForMalformedDataException(i, ex));
//                } catch (SVNException | InterruptedException e) {
//                    return false;
//                }
//            }
//        }
//    }
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
