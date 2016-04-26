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
package org.radixware.kernel.common.svn.client;

import java.io.File;
import org.radixware.kernel.common.svn.RadixSvnException;

/**
 *
 * @author akrylov
 */
public interface ISvnFSClient {

    public class SvnFsClientException extends RadixSvnException {

        public SvnFsClientException() {
        }

        public SvnFsClientException(String message) {
            super(message);
        }

        public SvnFsClientException(String message, Throwable cause) {
            super(message, cause);
        }

        public SvnFsClientException(Throwable cause) {
            super(cause);
        }

        public SvnFsClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }

    public interface ISvnStatusCallback {

        public void doStatus(String string, ISvnStatus isvns);
    }

    public interface SvnRevision {

        public interface Number extends SvnRevision {

            long getNumber();
        }

    }

    public interface SvnStatusKind {

        boolean isModified();

        boolean isConfilicted();

        boolean isDeleted();

        boolean isAdded();

        boolean isMissing();

        boolean isIncomplete();

        boolean isObstructed();

        boolean isReplaced();

        boolean isNone();

        public boolean isNormal();
    }

    public interface SvnNodeKind {

        boolean isFile();
    }

    public interface ISvnDirEntry {

        public SvnNodeKind getNodeKind();
    }

    public interface ISvnInfo {

        String getUrlString();
    }

    public interface ISvnStatus {

//    public SVNUrl getUrl();
//
        public String getUrlString();
//
//    public SVNRevision.Number getLastChangedRevision();
//
//    public Date getLastChangedDate();
//
//    public String getLastCommitAuthor();
//

        public SvnStatusKind getTextStatus();
//
//    public SVNStatusKind getRepositoryTextStatus();
//
//    public SVNStatusKind getPropStatus();
//

        public SvnStatusKind getRepositoryPropStatus();

        public SvnStatusKind getRepositoryTextStatus();
//

        public SvnRevision.Number getRevision();
//
//    public String getPath();
//
//    public String getMovedFromAbspath();
//
//    public String getMovedToAbspath();
//

        public File getFile();
//
//    public SVNNodeKind getNodeKind();
//
//    public boolean isCopied();
//
//    public boolean isWcLocked();
//
//    public boolean isSwitched();
//
//    public File getConflictNew();
//
//    public File getConflictOld();
//
//    public File getConflictWorking();
//
//    public String getLockOwner();
//
//    public Date getLockCreationDate();
//
//    public String getLockComment();
//
//    public boolean hasTreeConflict();
//
//    public SVNConflictDescriptor getConflictDescriptor();
//
//    public boolean isFileExternal();
    }

//    public boolean isThreadsafe();
//
//    public void addNotifyListener(ISVNNotifyListener il);
//
//    public void removeNotifyListener(ISVNNotifyListener il);
//
//    public SVNNotificationHandler getNotificationHandler();
//
//    public void setUsername(String string);
//
//    public void setPassword(String string);
//
//    public void addPasswordCallback(ISVNPromptUserPassword isvnp);
//
//    public void addConflictResolutionCallback(ISVNConflictResolver isvncr);
//
//    public void setProgressListener(ISVNProgressListener il);
//
//    public void addFile(File file) throws SVNClientException;
//
//    public void addDirectory(File file, boolean bln) throws SVNClientException;
//
//    public void addDirectory(File file, boolean bln, boolean bln1) throws SVNClientException;
//
    public void checkout(String svnurl, File file, SvnRevision svnr, boolean recursive) throws SvnFsClientException;
//
//    public void checkout(SVNUrl svnurl, File file, SVNRevision svnr, int i, boolean bln, boolean bln1) throws SVNClientException;
//

    public long commit(File[] files, String string, boolean bln) throws SvnFsClientException;

    public long commit(File[] files, String string, boolean bln, boolean bln1) throws SvnFsClientException;
//
//    public long[] commitAcrossWC(File[] files, String string, boolean bln, boolean bln1, boolean bln2) throws SVNClientException;
//
//    public String getPostCommitError();
//
//    public ISVNDirEntry[] getList(SVNUrl svnurl, SVNRevision svnr, boolean bln) throws SVNClientException;
//
//    public ISVNDirEntry[] getList(SVNUrl svnurl, SVNRevision svnr, SVNRevision svnr1, boolean bln) throws SVNClientException;
//
//    public ISVNDirEntryWithLock[] getListWithLocks(SVNUrl svnurl, SVNRevision svnr, SVNRevision svnr1, boolean bln) throws SVNClientException;
//
//    public ISVNDirEntry[] getList(File file, SVNRevision svnr, boolean bln) throws SVNClientException;
//
//    public ISVNDirEntry[] getList(File file, SVNRevision svnr, SVNRevision svnr1, boolean bln) throws SVNClientException;
//

    public ISvnDirEntry getDirEntry(String svnurl, SvnRevision svnr) throws SvnFsClientException;
//
//    public ISVNDirEntry getDirEntry(File file, SVNRevision svnr) throws SVNClientException;
//

    public ISvnStatus getSingleStatus(File file) throws SvnFsClientException;
////
//    public ISVNStatus[] getStatus(File[] files) throws SVNClientException;
//
//    public ISVNStatus[] getStatus(File file, boolean bln, boolean bln1) throws SVNClientException;
//
//    public ISVNStatus[] getStatus(File file, boolean bln, boolean bln1, boolean bln2) throws SVNClientException;
//
//    public ISVNStatus[] getStatus(File file, boolean bln, boolean bln1, boolean bln2, boolean bln3) throws SVNClientException;
//
//    public ISVNStatus[] getStatus(File file, boolean bln, boolean bln1, boolean bln2, boolean bln3, ISVNStatusCallback isvnsc) throws SVNClientException;

    public ISvnStatus[] getStatus(File path, boolean descend, boolean getAll, boolean contactServer, boolean ignoreExternals, ISvnStatusCallback isvnsc) throws SvnFsClientException;
//
//    public void copy(File file, File file1) throws SVNClientException;
//
//    public void copy(File file, SVNUrl svnurl, String string) throws SVNClientException;
//
//    public void copy(File[] files, SVNUrl svnurl, String string, boolean bln, boolean bln1) throws SVNClientException;
//
//    public void copy(SVNUrl svnurl, File file, SVNRevision svnr) throws SVNClientException;
//
//    public void copy(SVNUrl svnurl, File file, SVNRevision svnr, boolean bln, boolean bln1) throws SVNClientException;
//
//    public void copy(SVNUrl svnurl, File file, SVNRevision svnr, SVNRevision svnr1, boolean bln, boolean bln1) throws SVNClientException;
//
//    public void copy(SVNUrl svnurl, SVNUrl svnurl1, String string, SVNRevision svnr) throws SVNClientException;
//
//    public void copy(SVNUrl svnurl, SVNUrl svnurl1, String string, SVNRevision svnr, boolean bln) throws SVNClientException;
//
//    public void copy(SVNUrl[] svnurls, SVNUrl svnurl, String string, SVNRevision svnr, boolean bln, boolean bln1) throws SVNClientException;
//
//    public void remove(SVNUrl[] svnurls, String string) throws SVNClientException;
//

    public void remove(File[] files, boolean bln) throws SvnFsClientException;
//

    public void export(String svnurl, File file, SvnRevision svnr, boolean recursive) throws SvnFsClientException;
//
//    public void doExport(File file, File file1, boolean bln) throws SVNClientException;
//
//    public void doImport(File file, SVNUrl svnurl, String string, boolean bln) throws SVNClientException;
//
//    public void mkdir(SVNUrl svnurl, String string) throws SVNClientException;
//
//    public void mkdir(SVNUrl svnurl, boolean bln, String string) throws SVNClientException;
//
//    public void mkdir(File file) throws SVNClientException;
//
//    public void move(File file, File file1, boolean bln) throws SVNClientException;
//
//    public void move(SVNUrl svnurl, SVNUrl svnurl1, String string, SVNRevision svnr) throws SVNClientException;
//

    public long update(File file, SvnRevision svnr, boolean recursive) throws SvnFsClientException;

    public long[] update(File[] files, SvnRevision svnr, boolean recursive, boolean bln1) throws SvnFsClientException;
//
//    public long update(File file, SVNRevision svnr, int i, boolean bln, boolean bln1, boolean bln2) throws SVNClientException;
//
//    public long[] update(File[] files, SVNRevision svnr, boolean bln, boolean bln1) throws SVNClientException;
//
//    public long[] update(File[] files, SVNRevision svnr, int i, boolean bln, boolean bln1, boolean bln2) throws SVNClientException;
//

    public void revert(File file, boolean recursive) throws SvnFsClientException;
//
//    public ISVNLogMessage[] getLogMessages(SVNUrl svnurl, SVNRevision svnr, SVNRevision svnr1) throws SVNClientException;
//
//    public ISVNLogMessage[] getLogMessages(SVNUrl svnurl, SVNRevision svnr, SVNRevision svnr1, boolean bln) throws SVNClientException;
//
//    public ISVNLogMessage[] getLogMessages(SVNUrl svnurl, String[] strings, SVNRevision svnr, SVNRevision svnr1, boolean bln, boolean bln1) throws SVNClientException;
//
//    public ISVNLogMessage[] getLogMessages(File file, SVNRevision svnr, SVNRevision svnr1) throws SVNClientException;
//
//    public ISVNLogMessage[] getLogMessages(File file, SVNRevision svnr, SVNRevision svnr1, boolean bln) throws SVNClientException;
//
//    public ISVNLogMessage[] getLogMessages(File file, SVNRevision svnr, SVNRevision svnr1, boolean bln, boolean bln1) throws SVNClientException;
//
//    public ISVNLogMessage[] getLogMessages(File file, SVNRevision svnr, SVNRevision svnr1, boolean bln, boolean bln1, long l) throws SVNClientException;
//
//    public ISVNLogMessage[] getLogMessages(File file, SVNRevision svnr, SVNRevision svnr1, SVNRevision svnr2, boolean bln, boolean bln1, long l, boolean bln2) throws SVNClientException;
//
//    public ISVNLogMessage[] getLogMessages(SVNUrl svnurl, SVNRevision svnr, SVNRevision svnr1, SVNRevision svnr2, boolean bln, boolean bln1, long l) throws SVNClientException;
//
//    public ISVNLogMessage[] getLogMessages(SVNUrl svnurl, SVNRevision svnr, SVNRevision svnr1, SVNRevision svnr2, boolean bln, boolean bln1, long l, boolean bln2) throws SVNClientException;
//
//    public void getLogMessages(File file, SVNRevision svnr, SVNRevision svnr1, SVNRevision svnr2, boolean bln, boolean bln1, long l, boolean bln2, String[] strings, ISVNLogMessageCallback i) throws SVNClientException;
//
//    public void getLogMessages(SVNUrl svnurl, SVNRevision svnr, SVNRevision svnr1, SVNRevision svnr2, boolean bln, boolean bln1, long l, boolean bln2, String[] strings, ISVNLogMessageCallback i) throws SVNClientException;
//
//    public InputStream getContent(SVNUrl svnurl, SVNRevision svnr, SVNRevision svnr1) throws SVNClientException;
//
//    public InputStream getContent(SVNUrl svnurl, SVNRevision svnr) throws SVNClientException;
//
//    public InputStream getContent(File file, SVNRevision svnr) throws SVNClientException;
//
//    public void propertySet(File file, String string, String string1, boolean bln) throws SVNClientException;
//
//    public void propertySet(SVNUrl svnurl, SVNRevision.Number number, String string, String string1, String string2) throws SVNClientException;
//
//    public void propertySet(File file, String string, File file1, boolean bln) throws SVNClientException, IOException;
//
//    public ISVNProperty propertyGet(File file, String string) throws SVNClientException;
//
//    public ISVNProperty propertyGet(SVNUrl svnurl, String string) throws SVNClientException;
//
//    public ISVNProperty propertyGet(SVNUrl svnurl, SVNRevision svnr, SVNRevision svnr1, String string) throws SVNClientException;
//
//    public void propertyDel(File file, String string, boolean bln) throws SVNClientException;
//
//    public void setRevProperty(SVNUrl svnurl, SVNRevision.Number number, String string, String string1, boolean bln) throws SVNClientException;
//
//    public String getRevProperty(SVNUrl svnurl, SVNRevision.Number number, String string) throws SVNClientException;
//
//    public List getIgnoredPatterns(File file) throws SVNClientException;
//
//    public void addToIgnoredPatterns(File file, String string) throws SVNClientException;
//
//    public void setIgnoredPatterns(File file, List list) throws SVNClientException;
//
//    public void diff(File file, SVNRevision svnr, File file1, SVNRevision svnr1, File file2, boolean bln) throws SVNClientException;
//
//    public void diff(File file, SVNRevision svnr, File file1, SVNRevision svnr1, File file2, boolean bln, boolean bln1, boolean bln2, boolean bln3) throws SVNClientException;
//
//    public void diff(File file, File file1, boolean bln) throws SVNClientException;
//
//    public void diff(File[] files, File file, boolean bln) throws SVNClientException;
//
//    public void createPatch(File[] files, File file, File file1, boolean bln) throws SVNClientException;
//
//    public void diff(SVNUrl svnurl, SVNRevision svnr, SVNUrl svnurl1, SVNRevision svnr1, File file, boolean bln) throws SVNClientException;
//
//    public void diff(SVNUrl svnurl, SVNRevision svnr, SVNUrl svnurl1, SVNRevision svnr1, File file, boolean bln, boolean bln1, boolean bln2, boolean bln3) throws SVNClientException;
//
//    public void diff(SVNUrl svnurl, SVNRevision svnr, SVNRevision svnr1, SVNRevision svnr2, File file, int i, boolean bln, boolean bln1, boolean bln2) throws SVNClientException;
//
//    public void diff(SVNUrl svnurl, SVNRevision svnr, SVNRevision svnr1, SVNRevision svnr2, File file, boolean bln) throws SVNClientException;
//
//    public void diff(SVNUrl svnurl, SVNRevision svnr, SVNRevision svnr1, File file, boolean bln) throws SVNClientException;
//
//    public void diff(File file, SVNUrl svnurl, SVNRevision svnr, File file1, boolean bln) throws SVNClientException;
//
//    public SVNKeywords getKeywords(File file) throws SVNClientException;
//
//    public void setKeywords(File file, SVNKeywords svnk, boolean bln) throws SVNClientException;
//
//    public SVNKeywords addKeywords(File file, SVNKeywords svnk) throws SVNClientException;
//
//    public SVNKeywords removeKeywords(File file, SVNKeywords svnk) throws SVNClientException;
//
//    public ISVNAnnotations annotate(SVNUrl svnurl, SVNRevision svnr, SVNRevision svnr1) throws SVNClientException;
//
//    public ISVNAnnotations annotate(File file, SVNRevision svnr, SVNRevision svnr1) throws SVNClientException;
//
//    public ISVNAnnotations annotate(SVNUrl svnurl, SVNRevision svnr, SVNRevision svnr1, SVNRevision svnr2, boolean bln, boolean bln1) throws SVNClientException;
//
//    public ISVNAnnotations annotate(File file, SVNRevision svnr, SVNRevision svnr1, boolean bln, boolean bln1) throws SVNClientException;
//
//    public ISVNAnnotations annotate(File file, SVNRevision svnr, SVNRevision svnr1, SVNRevision svnr2, boolean bln, boolean bln1) throws SVNClientException;
//
//    public ISVNProperty[] getPropertiesIncludingInherited(File file) throws SVNClientException;
//
//    public ISVNProperty[] getPropertiesIncludingInherited(File file, boolean bln, boolean bln1, List<String> list) throws SVNClientException;
//
//    public ISVNProperty[] getPropertiesIncludingInherited(SVNUrl svnurl) throws SVNClientException;
//
//    public ISVNProperty[] getPropertiesIncludingInherited(SVNUrl svnurl, boolean bln, boolean bln1, List<String> list) throws SVNClientException;
//
//    public ISVNProperty[] getProperties(File file) throws SVNClientException;
//
//    public ISVNProperty[] getProperties(File file, boolean bln) throws SVNClientException;
//
//    public ISVNProperty[] getProperties(SVNUrl svnurl, SVNRevision svnr, SVNRevision svnr1, boolean bln) throws SVNClientException;
//
//    public ISVNProperty[] getProperties(SVNUrl svnurl, SVNRevision svnr, SVNRevision svnr1) throws SVNClientException;
//
//    public ISVNProperty[] getProperties(SVNUrl svnurl) throws SVNClientException;
//
//    public ISVNProperty[] getRevProperties(SVNUrl svnurl, SVNRevision.Number number) throws SVNClientException;
//
//    public void resolved(File file) throws SVNClientException;
//
//    public void resolve(File file, int i) throws SVNClientException;
//
//    public void createRepository(File file, String string) throws SVNClientException;
//
//    public void cancelOperation() throws SVNClientException;
//
//    public ISVNInfo getInfoFromWorkingCopy(File file) throws SVNClientException;
//

    public ISvnInfo getInfo(File file) throws SvnFsClientException;
//
//    public ISVNInfo[] getInfo(File file, boolean bln) throws SVNClientException;
//
//    public ISVNInfo getInfo(SVNUrl svnurl) throws SVNClientException;
//
//    public ISVNInfo getInfo(SVNUrl svnurl, SVNRevision svnr, SVNRevision svnr1) throws SVNClientException;
//
//    public void switchToUrl(File file, SVNUrl svnurl, SVNRevision svnr, boolean bln) throws SVNClientException;
//
//    public void switchToUrl(File file, SVNUrl svnurl, SVNRevision svnr, int i, boolean bln, boolean bln1, boolean bln2) throws SVNClientException;
//
//    public void switchToUrl(File file, SVNUrl svnurl, SVNRevision svnr, SVNRevision svnr1, int i, boolean bln, boolean bln1, boolean bln2) throws SVNClientException;
//
//    public void switchToUrl(File file, SVNUrl svnurl, SVNRevision svnr, SVNRevision svnr1, int i, boolean bln, boolean bln1, boolean bln2, boolean bln3) throws SVNClientException;
//
//    public void setConfigDirectory(File file) throws SVNClientException;
//
//    public void cleanup(File file) throws SVNClientException;
//
//    public void upgrade(File file) throws SVNClientException;
//

    public void merge(File file1, long revision1, File file2, long revision2, File localPath, boolean force, boolean recurse) throws SvnFsClientException;

    public void merge(String path1, long revision1, String path2, long revision2, File localPath, boolean force, boolean recurse) throws SvnFsClientException;
//    public void merge(SVNUrl svnurl, SVNRevision svnr, SVNUrl svnurl1, SVNRevision svnr1, File file, boolean bln, boolean bln1) throws SVNClientException;
//
//    public void merge(SVNUrl svnurl, SVNRevision svnr, SVNUrl svnurl1, SVNRevision svnr1, File file, boolean bln, boolean bln1, boolean bln2) throws SVNClientException;
//
//    public void merge(SVNUrl svnurl, SVNRevision svnr, SVNUrl svnurl1, SVNRevision svnr1, File file, boolean bln, boolean bln1, boolean bln2, boolean bln3) throws SVNClientException;
//
//    public void merge(SVNUrl svnurl, SVNRevision svnr, SVNUrl svnurl1, SVNRevision svnr1, File file, boolean bln, int i, boolean bln1, boolean bln2, boolean bln3) throws SVNClientException;
//
//    public void mergeReintegrate(SVNUrl svnurl, SVNRevision svnr, File file, boolean bln, boolean bln1) throws SVNClientException;
//
//    public void lock(SVNUrl[] svnurls, String string, boolean bln) throws SVNClientException;
//
//    public void unlock(SVNUrl[] svnurls, boolean bln) throws SVNClientException;
//
//    public void lock(File[] files, String string, boolean bln) throws SVNClientException;
//
//    public void unlock(File[] files, boolean bln) throws SVNClientException;
//
//    public boolean statusReturnsRemoteInfo();
//
//    public boolean canCommitAcrossWC();
//
//    public String getAdminDirectoryName();
//
//    public boolean isAdminDirectory(String string);
//
//    public void relocate(String string, String string1, String string2, boolean bln) throws SVNClientException;
//
//    public void merge(SVNUrl svnurl, SVNRevision svnr, SVNRevisionRange[] svnrrs, File file, boolean bln, int i, boolean bln1, boolean bln2, boolean bln3) throws SVNClientException;
//
//    public ISVNMergeInfo getMergeInfo(File file, SVNRevision svnr) throws SVNClientException;
//
//    public ISVNMergeInfo getMergeInfo(SVNUrl svnurl, SVNRevision svnr) throws SVNClientException;
//
//    public ISVNLogMessage[] getMergeinfoLog(int i, File file, SVNRevision svnr, SVNUrl svnurl, SVNRevision svnr1, boolean bln) throws SVNClientException;
//
//    public ISVNLogMessage[] getMergeinfoLog(int i, SVNUrl svnurl, SVNRevision svnr, SVNUrl svnurl1, SVNRevision svnr1, boolean bln) throws SVNClientException;
//
//    public SVNDiffSummary[] diffSummarize(SVNUrl svnurl, SVNRevision svnr, SVNUrl svnurl1, SVNRevision svnr1, int i, boolean bln) throws SVNClientException;
//
//    public SVNDiffSummary[] diffSummarize(SVNUrl svnurl, SVNRevision svnr, SVNRevision svnr1, SVNRevision svnr2, int i, boolean bln) throws SVNClientException;
//
//    public SVNDiffSummary[] diffSummarize(File file, SVNUrl svnurl, SVNRevision svnr, boolean bln) throws SVNClientException;
//
//    public String[] suggestMergeSources(File file) throws SVNClientException;
//
//    public String[] suggestMergeSources(SVNUrl svnurl, SVNRevision svnr) throws SVNClientException;
//
//    public void dispose();

    public SvnRevision getHeadRevision();

    public SvnRevision.Number getRevision(long number);

    void cancelOperation() throws SvnFsClientException;
}
