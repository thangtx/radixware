/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.designer.subversion.util;

import java.io.File;
import java.net.MalformedURLException;
import org.netbeans.modules.subversion.Subversion;
import org.radixware.kernel.common.svn.client.ISvnFSClient;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNDirEntry;
import org.tigris.subversion.svnclientadapter.ISVNInfo;
import org.tigris.subversion.svnclientadapter.ISVNStatus;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNNodeKind;
import org.tigris.subversion.svnclientadapter.SVNRevision;
import org.tigris.subversion.svnclientadapter.SVNStatusKind;
import org.tigris.subversion.svnclientadapter.SVNUrl;

/**
 *
 * @author akrylov
 */
public class SvnBridge {

    private static class SvnInfo implements ISvnFSClient.ISvnInfo {

        private ISVNInfo info;

        public SvnInfo(ISVNInfo info) {
            this.info = info;
        }

        @Override
        public String getUrlString() {
            return info.getUrlString();
        }

    }

    private static class SvnNodeKind implements ISvnFSClient.SvnNodeKind {

        private SVNNodeKind kind;

        public SvnNodeKind(SVNNodeKind kind) {
            this.kind = kind;
        }

        @Override
        public boolean isFile() {
            return kind == SVNNodeKind.FILE;
        }

    }

    private static class SvnDirEntry implements ISvnFSClient.ISvnDirEntry {

        private ISVNDirEntry entry;

        public SvnDirEntry(ISVNDirEntry entry) {
            this.entry = entry;
        }

        @Override
        public ISvnFSClient.SvnNodeKind getNodeKind() {
            return new SvnNodeKind(entry.getNodeKind());
        }

    }

    private static class SvnStatusKind implements ISvnFSClient.SvnStatusKind {

        private final SVNStatusKind kind;

        public SvnStatusKind(SVNStatusKind kind) {
            this.kind = kind;
        }

        @Override
        public boolean isModified() {
            return kind == SVNStatusKind.MODIFIED;
        }

        @Override
        public boolean isNormal() {
            return kind == SVNStatusKind.NORMAL;
        }

        @Override
        public boolean isConfilicted() {
            return kind == SVNStatusKind.CONFLICTED;
        }

        @Override
        public boolean isDeleted() {
            return kind == SVNStatusKind.DELETED;
        }

        @Override
        public boolean isAdded() {
            return kind == SVNStatusKind.ADDED;
        }

        @Override
        public boolean isMissing() {
            return kind == SVNStatusKind.MISSING;
        }

        @Override
        public boolean isIncomplete() {
            return kind == SVNStatusKind.INCOMPLETE;
        }

        @Override
        public boolean isObstructed() {
            return kind == SVNStatusKind.OBSTRUCTED;
        }

        @Override
        public boolean isReplaced() {
            return kind == SVNStatusKind.REPLACED;
        }

        @Override
        public boolean isNone() {
            return kind == SVNStatusKind.NONE;
        }

    }

    private static class Revision extends AbstractRevision implements ISvnFSClient.SvnRevision.Number {

        public Revision(SVNRevision.Number number) {
            super(number);
        }

        @Override
        public long getNumber() {
            return ((SVNRevision.Number) revision).getNumber();
        }

    }

    private static class AbstractRevision implements ISvnFSClient.SvnRevision {

        protected final SVNRevision revision;

        public AbstractRevision(SVNRevision number) {
            this.revision = number;
        }

    }

    private static class Status implements ISvnFSClient.ISvnStatus {

        private ISVNStatus status;

        public Status(ISVNStatus status) {
            this.status = status;
        }

        @Override
        public ISvnFSClient.SvnStatusKind getTextStatus() {
            return new SvnStatusKind(status.getTextStatus());
        }

        @Override
        public File getFile() {
            return status.getFile();
        }

        @Override
        public ISvnFSClient.SvnStatusKind getRepositoryPropStatus() {
            return new SvnStatusKind(status.getRepositoryPropStatus());
        }

        @Override
        public ISvnFSClient.SvnStatusKind getRepositoryTextStatus() {
            return new SvnStatusKind(status.getRepositoryTextStatus());
        }

        @Override
        public String getUrlString() {
            return status.getUrlString();
        }

        @Override
        public ISvnFSClient.SvnRevision.Number getRevision() {
            return status.getRevision() == null ? null : new Revision(status.getRevision());
        }

    }

    private static class SVNFsClient implements ISvnFSClient {

        private ISVNClientAdapter adapter;

        public SVNFsClient(ISVNClientAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public ISvnFSClient.ISvnStatus getSingleStatus(File file) throws ISvnFSClient.SvnFsClientException {
            try {
                return new Status(adapter.getSingleStatus(file));
            } catch (SVNClientException ex) {
                throw new ISvnFSClient.SvnFsClientException(ex);
            }
        }

        @Override
        public ISvnFSClient.ISvnStatus[] getStatus(File path, boolean descend, boolean getAll, boolean contactServer, boolean ignoreExternals, final ISvnFSClient.ISvnStatusCallback isvnsc) throws ISvnFSClient.SvnFsClientException {
            try {
                ISVNStatus[] result = adapter.getStatus(path, descend, getAll, contactServer, ignoreExternals);
                if (result != null) {
                    ISvnFSClient.ISvnStatus[] statuses = new ISvnFSClient.ISvnStatus[result.length];
                    for (int i = 0; i < result.length; i++) {
                        statuses[i] = new Status(result[i]);
                        if (isvnsc != null) {
                            isvnsc.doStatus(statuses[i].getFile().getPath(), statuses[i]);
                        }
                    }
                    return statuses;
                }
                return null;
            } catch (SVNClientException ex) {
                throw new ISvnFSClient.SvnFsClientException(ex);
            }
        }

        @Override
        public long update(File file, ISvnFSClient.SvnRevision svnr, boolean bln) throws ISvnFSClient.SvnFsClientException {
            try {
                return adapter.update(file, ((AbstractRevision) svnr).revision, bln);
            } catch (SVNClientException ex) {
                throw new ISvnFSClient.SvnFsClientException(ex);
            }
        }

        @Override
        public long[] update(final File[] files, final ISvnFSClient.SvnRevision svnr, final boolean recursive, final boolean bln1) throws ISvnFSClient.SvnFsClientException {
            try {
                try {
                    return adapter.update(files, ((AbstractRevision) svnr).revision, recursive, bln1);
                } catch (UnsupportedOperationException ex) {
                    final long rez[] = new long[files.length];
                    int i = 0;
                    for (File file : files) {
                        rez[i] = adapter.update(file, ((AbstractRevision) svnr).revision, bln1);
                        i++;
                    }
                    return rez;
                }

            } catch (SVNClientException ex) {
                throw new ISvnFSClient.SvnFsClientException(ex);
            }
        }

        @Override
        public void checkout(String svnurl, File file, ISvnFSClient.SvnRevision svnr, boolean recursive) throws ISvnFSClient.SvnFsClientException {
            try {
                adapter.checkout(new SVNUrl(svnurl), file, ((AbstractRevision) svnr).revision, recursive);
            } catch (SVNClientException | MalformedURLException ex) {
                throw new ISvnFSClient.SvnFsClientException(ex);
            }
        }

        @Override
        public void export(String svnurl, File file, ISvnFSClient.SvnRevision svnr, boolean recursive) throws ISvnFSClient.SvnFsClientException {
            try {
                adapter.doExport(new SVNUrl(svnurl), file, ((AbstractRevision) svnr).revision, recursive);
            } catch (SVNClientException | MalformedURLException ex) {
                throw new ISvnFSClient.SvnFsClientException(ex);
            }
        }

        @Override
        public void revert(File file, boolean recursive) throws ISvnFSClient.SvnFsClientException {
            try {
                adapter.revert(file, recursive);
            } catch (SVNClientException ex) {
                throw new ISvnFSClient.SvnFsClientException(ex);
            }
        }

        @Override
        public ISvnFSClient.SvnRevision getHeadRevision() {
            return new AbstractRevision(SVNRevision.HEAD);
        }

        @Override
        public ISvnFSClient.SvnRevision.Number getRevision(long number) {
            return new Revision(new SVNRevision.Number(number));
        }

        @Override
        public ISvnFSClient.ISvnDirEntry getDirEntry(String svnurl, ISvnFSClient.SvnRevision svnr) throws ISvnFSClient.SvnFsClientException {
            try {
                return new SvnDirEntry(adapter.getDirEntry(new SVNUrl(svnurl), ((AbstractRevision) svnr).revision));
            } catch (MalformedURLException ex) {
                throw new ISvnFSClient.SvnFsClientException(ex);
            } catch (SVNClientException ex) {
                throw new ISvnFSClient.SvnFsClientException(ex);
            }
        }

        @Override
        public ISvnFSClient.ISvnInfo getInfo(File file) throws ISvnFSClient.SvnFsClientException {
            try {
                return new SvnInfo(adapter.getInfoFromWorkingCopy(file));
            } catch (SVNClientException ex) {
                throw new ISvnFSClient.SvnFsClientException(ex);
            }
        }

        @Override
        public long commit(File[] files, String string, boolean bln) throws ISvnFSClient.SvnFsClientException {
            try {
                return adapter.commit(files, string, bln);
            } catch (SVNClientException ex) {
                throw new ISvnFSClient.SvnFsClientException(ex);
            }
        }

        @Override
        public long commit(File[] files, String string, boolean bln, boolean bln1) throws ISvnFSClient.SvnFsClientException {
            try {
                return adapter.commit(files, string, bln, bln1);
            } catch (SVNClientException ex) {
                throw new ISvnFSClient.SvnFsClientException(ex);
            }
        }

        @Override
        public void remove(File[] files, boolean bln) throws ISvnFSClient.SvnFsClientException {
            try {
                adapter.remove(files, bln);
            } catch (SVNClientException ex) {
                throw new ISvnFSClient.SvnFsClientException(ex);
            }
        }

        @Override
        public void merge(File file1, long revision1, File file2, long revision2, File localPath, boolean force, boolean recurse) throws ISvnFSClient.SvnFsClientException {
            try {
                ISVNInfo info1 = adapter.getInfoFromWorkingCopy(file1);
                ISVNInfo info2 = adapter.getInfoFromWorkingCopy(file2);
                merge(info1.getUrlString(), revision1, info2.getUrlString(), revision2, localPath, force, recurse);
            } catch (SVNClientException ex) {
                throw new ISvnFSClient.SvnFsClientException(ex);
            }
        }

        @Override
        public void merge(String path1, long revision1, String path2, long revision2, File localPath, boolean force, boolean recurse) throws ISvnFSClient.SvnFsClientException {
            try {
                final SVNUrl url1 = new SVNUrl(path1);
                final SVNUrl url2 = new SVNUrl(path2);
                final SVNRevision r1 = revision1 == -1 ? SVNRevision.HEAD : new SVNRevision.Number(revision1);
                final SVNRevision r2 = revision2 == -1 ? SVNRevision.HEAD : new SVNRevision.Number(revision2);
                adapter.merge(url1, r1, url2, r2, localPath, force, recurse);
            } catch (SVNClientException | MalformedURLException ex) {
                throw new ISvnFSClient.SvnFsClientException(ex);
            }
        }

        @Override
        public void cancelOperation() throws ISvnFSClient.SvnFsClientException {
            try {
                adapter.cancelOperation();
            } catch (SVNClientException ex) {
                throw new ISvnFSClient.SvnFsClientException(ex);
            }
        }

    }

    public SvnBridge() {

    }

    public static ISvnFSClient getClientAdapter(File branchDir) {
        try {
            return new SVNFsClient(Subversion.getInstance().getClient(branchDir));
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }
}
