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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.client.ISvnFSClient;
import org.radixware.kernel.common.svn.client.ISvnFSClient.SvnFsClientException;
import org.radixware.kernel.common.svn.client.ISvnLogHandler;
import org.radixware.kernel.common.svn.client.SvnAuthType;
import org.radixware.kernel.common.svn.client.SvnEntry;
import org.radixware.kernel.common.svn.client.SvnLogEntry;

public class SVN {

    public static final class SVNPreferences {

        public static final String AUTH_TYPE = "AuthenticationType";
        public static final String SSH_KEY_FILE = "SSHKeyFilePath";
        public static final String USER_NAME = "UserName";

        public static SvnAuthType getAuthType() {
            try {
                return SvnAuthType.getForValue(Preferences.userNodeForPackage(SVN.class).get(AUTH_TYPE, SvnAuthType.SSH_PASSWORD.getValue()));
            } catch (IllegalStateException e) {
                return SvnAuthType.SSH_PASSWORD;
            }
        }

        public static void setAuthType(SvnAuthType at) {
            Preferences.userNodeForPackage(SVN.class).put(AUTH_TYPE, at.getValue());
        }

        public static String getSSHKeyFilePath() {
            return Preferences.userNodeForPackage(SVN.class).get(SSH_KEY_FILE, "");
        }

        public static void setSSHKeyFilePath(String path) {
            Preferences.userNodeForPackage(SVN.class).put(SSH_KEY_FILE, path);
        }

        public static String getUserName() {
            return Preferences.userNodeForPackage(SVN.class).get(USER_NAME, "");
        }

        public static void setUserName(String userName) {
            Preferences.userNodeForPackage(SVN.class).put(USER_NAME, userName);
        }
    }
    protected static final HashMap<String, char[]> passwordsCache = new HashMap<>();

    public static void clearPasswordCache(String url, String user, SvnAuthType auth) {
        String key = url + ":" + user + ":" + auth.toString();
        passwordsCache.remove(key);
    }

    public static boolean passwordIsSet(String url, String user, SvnAuthType auth) {
        String key = url + ":" + user + ":" + auth.toString();
        return passwordsCache.containsKey(key);
    }
//

    public static boolean isFileExists(SVNRepositoryAdapter repository, String path, long revision) {
        try {
            SvnEntry info = repository.info(path, revision);
            if (info != null) {
                return SvnEntry.Kind.FILE == info.getKind();
            }
        } catch (RadixSvnException ex) {
        }
        return false;
    }

    public static boolean isFileExists(SVNRepositoryAdapter repository, String path) {
        try {
            SvnEntry info = repository.info(path, repository.getLatestRevision());
            if (info != null) {
                return SvnEntry.Kind.FILE == info.getKind();
            }
        } catch (RadixSvnException ex) {
        }
        return false;
    }

    public static boolean isFileExists(ISvnFSClient repository, String fileUrl) {
        try {
            ISvnFSClient.ISvnDirEntry info = repository.getDirEntry(fileUrl, repository.getHeadRevision());
            if (info != null) {
                return info.getNodeKind().isFile();
            }
        } catch (ISvnFSClient.SvnFsClientException ex) {
        }
        return false;
    }

    public static SvnEntry.Kind getKind(SVNRepositoryAdapter repository, String path, long revision) {
        try {
            SvnEntry info = repository.info(path, revision);
            if (info == null) {
                return null;
            }
            return info.getKind();
        } catch (RadixSvnException ex) {
        }
        return null;
    }

    public static boolean isExists(SVNRepositoryAdapter repository, String path) {
        return isExists(repository, path, null);
    }

    public static boolean isExists(SVNRepositoryAdapter repository, String path, Long rev) {
        for (int i = 1; true; i++) {
            try {
                if (rev == null) {
                    rev = repository.getLatestRevision();
                }
                SvnEntry.Kind kind = repository.checkPath(path, rev);
                if (kind != null) {
                    return kind == SvnEntry.Kind.DIRECTORY || SvnEntry.Kind.FILE == kind;
                } else {
                    return false;
                }
            } catch (RadixSvnException ex) {
                if (!ex.isIOError() || !ex.isMalformedDataError()) {
                    return false;
                }
                try {
                    Thread.sleep(getSvnTimeoutForMalformedDataException(i, null));
                } catch (Exception e) {
                    return false;
                }
            }
        }
    }

    public static boolean isNormalSvnStatus(ISvnFSClient repository, File file) {
        ISvnFSClient.SvnStatusKind type = getSvnStatus(repository, file);
        return type != null && type.isNormal();
    }

    public static boolean isСonflictedSvnStatus(ISvnFSClient repository, File file) {
        ISvnFSClient.SvnStatusKind type = getSvnStatus(repository, file);
        return type != null && type.isConfilicted();
    }

    private static ISvnFSClient.SvnStatusKind getSvnStatus(ISvnFSClient repository, File file) {
        try {
            ISvnFSClient.ISvnStatus st = repository.getSingleStatus(file);
            if (st != null) {
                return st.getTextStatus();
            }
        } catch (SvnFsClientException ex) {
        }
        return null;
    }

    public static ISvnFSClient.SvnStatusKind getSvnFileStatusType(ISvnFSClient repository, File file) {
        try {
            ISvnFSClient.ISvnStatus st = repository.getSingleStatus(file);
            if (st != null) {
                return st.getTextStatus();
            }
        } catch (SvnFsClientException ex) {
        }
        return null;
    }

    public static long getFileRevision(ISvnFSClient repository, File file) {
        try {
            ISvnFSClient.ISvnStatus st = repository.getSingleStatus(file);
            if (st != null) {
                return st.getRevision().getNumber();
            }
        } catch (SvnFsClientException ex) {
        }
        return -1;
    }

    public static String getFileUrl(ISvnFSClient repository, File file) throws ISvnFSClient.SvnFsClientException {
        try {
            ISvnFSClient.ISvnInfo info = repository.getInfo(file);
            return info.getUrlString();
        } catch (ISvnFSClient.SvnFsClientException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public static String getFileAsStr(SVNRepositoryAdapter svn, String file, long revision) throws RadixSvnException, UnsupportedEncodingException {
        ByteArrayOutputStream buf = null;
        try {
            buf = new ByteArrayOutputStream();
            getFile(svn, file, revision, buf);
            return buf.toString("UTF-8");
        } finally {
            if (buf != null) {
                try {
                    buf.close();
                } catch (IOException ex) {
                    Logger.getLogger(SVN.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static byte[] getFile(SVNRepositoryAdapter svn, String file, long revision) throws RadixSvnException {
        ByteArrayOutputStream buf = null;
        try {
            buf = new ByteArrayOutputStream();
            getFile(svn, file, revision, buf);
            return buf.toByteArray();
        } finally {
            if (buf != null) {
                try {
                    buf.close();
                } catch (IOException ex) {
                    Logger.getLogger(SVN.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public static long getDirLatestRevision(SVNRepositoryAdapter repository) throws RadixSvnException {
        return getDirLatestRevision(repository, "");
    }

    private static int getSvnTimeoutForMalformedDataException(int steep, RadixSvnException ex) throws RadixSvnException {
        int timeout;
        switch (steep) {
            case 1:
                timeout = 10;
                break;
            case 2:
                timeout = 500;
                break;
            case 3:
                timeout = 2000;
                break;
            case 4:
                timeout = 4000;
                break;
            default:
                if (ex != null) {
                    throw ex;
                }
                return 0;
        }
        return timeout;
    }

    public static long getDirLatestRevision(SVNRepositoryAdapter repository, String path) throws RadixSvnException {
        for (int i = 1; true; i++) {
            try {
                long latestRevision = repository.getLatestRevision();
                SvnEntry e = repository.getDir("", latestRevision);
                if (e == null) {
                    return latestRevision;
                } else {
                    return e.getRevision();
                }
            } catch (RadixSvnException ex) {
                if (!ex.isIOError() && !ex.isMalformedDataError()) {
                    throw ex;
                }
                try {
                    Thread.sleep(getSvnTimeoutForMalformedDataException(i, ex));
                } catch (InterruptedException e) {
                    throw ex;
                }
            }
        }
    }

    public static long getFile(SVNRepositoryAdapter repository, String path, long revision, File file) throws RadixSvnException, IOException {
        FileOutputStream stream = new FileOutputStream(file);
        try {
            long result = getFile(repository, path, revision, stream);
            stream.close();
            stream = null;
            return result;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    // NOTHING - safe original exception
                }
            }
        }
    }

    public static long getFileSize(SVNRepositoryAdapter repository, String path, long revision) throws RadixSvnException {
        for (int i = 1; true; i++) {
            try {
                return repository.info(path, revision).getSize();
            } catch (RadixSvnException ex) {
                if (!ex.isIOError() && !ex.isMalformedDataError()) {
                    throw ex;
                }
                try {
                    Thread.sleep(getSvnTimeoutForMalformedDataException(i, ex));
                } catch (InterruptedException e) {
                    throw ex;
                }
            }
        }
    }
    private static final String CHECKSUM_PROP_NAME = "svn:entry:checksum";

    public static String getChecksum(SVNRepositoryAdapter repository, String path, long revision) throws RadixSvnException {
        for (int i = 1; true; i++) {
            try {
                Map<String, String> properties = new HashMap<>();
                repository.getFile(path, revision, properties, null);
                if (!properties.containsKey(CHECKSUM_PROP_NAME)) {
                    return null;
                }
                return properties.get(CHECKSUM_PROP_NAME);

            } catch (RadixSvnException ex) {
                if (!ex.isIOError() && !ex.isMalformedDataError()) {
                    throw ex;
                }
                try {
                    Thread.sleep(getSvnTimeoutForMalformedDataException(i, ex));
                } catch (InterruptedException e) {
                    throw ex;
                }
            }
        }
    }

    // 	RADIX-4889
    public static long getFile(SVNRepositoryAdapter repository, String path, long revision, OutputStream outputStream) throws RadixSvnException {
        for (int i = 1; true; i++) {
            try {
                return repository.getFile(path, revision, outputStream);
            } catch (RadixSvnException ex) {
                if (!ex.isIOError() && !ex.isMalformedDataError()) {
                    throw ex;
                }
                try {
                    Thread.sleep(getSvnTimeoutForMalformedDataException(i, ex));
                } catch (InterruptedException e) {
                    throw ex;
                }
            }
        }
    }

    public static List<Long> getSvnLog(SVNRepositoryAdapter repository, String path) throws RadixSvnException {
        List<Long> revisions = new ArrayList();
        getSvnLog(repository, path, null, null, revisions, null, getDirLatestRevision(repository), 1, Integer.MAX_VALUE);
        return revisions;
    }

    public static void getSvnLog(SVNRepositoryAdapter repository, String path,
            List<String> comments,
            List<String> authors,
            List<Long> revisions,
            List<Date> dates,
            long endRevision) {
        try {
            getSvnLog(repository, path, comments, authors, revisions, dates, getDirLatestRevision(repository), endRevision, Integer.MAX_VALUE);
        } catch (Exception ex) {
        }
    }

    public static long getPriorRevision(SVNRepositoryAdapter repository, String path, long startRevision) throws RadixSvnException {
        List<Long> revisions = new ArrayList<Long>();
        getSvnLog(repository, path, null, null, revisions, null, startRevision, 1, 2);
        if (revisions.size() == 2) {
            return revisions.get(1);
        }
        return -1;
    }

    public static void getSvnLog(SVNRepositoryAdapter repository, String path,
            List<String> comments,
            List<String> authors,
            List<Long> revisions,
            List<Date> dates,
            long startRevision,
            long endRevision,
            int maxCount) throws RadixSvnException {
        //try 
        for (int i = 1; true; i++) {

            try {
                if (comments != null) {
                    comments.clear();
                }
                if (authors != null) {
                    authors.clear();
                }
                if (revisions != null) {
                    revisions.clear();
                }
                if (dates != null) {
                    dates.clear();
                }
                repository.log(path, startRevision != -1 ? startRevision : getDirLatestRevision(repository), endRevision, new SVNLogEntryHandle(comments, authors, revisions, dates, maxCount));
                return;
            } catch (CancellationException ex) {
                return;
            } catch (RadixSvnException ex) {
                if (ex.isFsNotFound()) {
                    return;
                }
                if (!ex.isIOError() && !ex.isMalformedDataError()) {
                    throw ex;
                }
                try {
                    Thread.sleep(getSvnTimeoutForMalformedDataException(i, ex));
                } catch (InterruptedException e) {
                    throw ex;
                }
            } 
        }
    }

    private static class SVNLogEntryHandle implements ISvnLogHandler {

        final List<String> comments;
        final List<String> authors;
        final List<Long> revisions;
        final List<Date> dates;
        final int maxCount;
        int count;

        SVNLogEntryHandle(List<String> comments, List<String> authors, List<Long> revisions, List<Date> dates, int maxCount) {
            this.comments = comments;
            this.authors = authors;
            this.revisions = revisions;
            this.dates = dates;
            this.maxCount = maxCount;
            count = 0;
        }

        @Override
        public void accept(SvnLogEntry svnle) throws RadixSvnException {
            if (comments != null) {
                comments.add(svnle.getMessage());
            }
            if (authors != null) {
                authors.add(svnle.getAuthor());
            }
            if (revisions != null) {
                revisions.add(svnle.getRevision());
            }
            if (dates != null) {
                dates.add(svnle.getDate());
            }
            if (++count >= maxCount) {
                throw new CancellationException();
            }
        }
    }

    protected final static int RADIX_OPTIONS_ITERATION_COUNT = 2;

    public static void revert(ISvnFSClient client, File file) throws SvnFsClientException {
        for (int i = 0; i < RADIX_OPTIONS_ITERATION_COUNT; i++) {
            try {
                client.revert(file, true);
                return;
            } catch (SvnFsClientException ex) {
                if (i == RADIX_OPTIONS_ITERATION_COUNT - 1) {
                    throw ex;
                }
            }
        }
    }

    public static void merge(ISvnFSClient rep, File fromPath, long rev1, long rev2, File desFile) throws SvnFsClientException {
        for (int i = 0; i < RADIX_OPTIONS_ITERATION_COUNT; i++) {
            try {
                rep.merge(fromPath, rev1, fromPath, rev2, desFile, false, false);
                return;
            } catch (SvnFsClientException ex) {
                if (i == RADIX_OPTIONS_ITERATION_COUNT - 1) {
                    throw ex;
                }
            }
        }
    }

    public static void commit(ISvnFSClient rep, File file, String mess) throws SvnFsClientException {
        for (int i = 0; i < RADIX_OPTIONS_ITERATION_COUNT; i++) {
            try {
                rep.commit(new File[]{file}, mess, true);
                return;
            } catch (SvnFsClientException ex) {
                if (i == RADIX_OPTIONS_ITERATION_COUNT - 1) {
                    throw ex;
                }
            }
        }
    }

    public static void commit(ISvnFSClient rep, File[] files, String mess) throws SvnFsClientException {
        for (int i = 0; i < RADIX_OPTIONS_ITERATION_COUNT; i++) {
            try {
                rep.commit(files, mess, true);
                return;
            } catch (SvnFsClientException ex) {
                if (i == RADIX_OPTIONS_ITERATION_COUNT - 1) {
                    throw ex;
                }
            }
        }
    }

    public static void update(ISvnFSClient client, File[] files) throws SvnFsClientException {
        update(null, null, client, files);

    }

    public static void update(ISvnFSClient client, File file) throws SvnFsClientException {
        update(null, null, client, new File[]{file});

    }

    public static void update(final PrintWriter out, final PrintWriter err, final ISvnFSClient client, final File[] files) throws SvnFsClientException {
        for (int i = 0; i < RADIX_OPTIONS_ITERATION_COUNT; i++) {
            try {
                client.update(files, client.getHeadRevision(), true, true);
                return;
            } catch (ISvnFSClient.SvnFsClientException ex) {
                if (i == RADIX_OPTIONS_ITERATION_COUNT - 1) {
                    throw ex;
                }
            }
        }
    }

    public static long delete(final ISvnFSClient rep, final File file, final String message) throws SvnFsClientException {
        for (int i = 0; i < RADIX_OPTIONS_ITERATION_COUNT; i++) {
            try {
                final File[] files = new File[]{file};
                rep.remove(files, true);
                return rep.commit(files, message, true);
            } catch (SvnFsClientException ex) {
                if (i == RADIX_OPTIONS_ITERATION_COUNT - 1) {
                    throw ex;
                }
            }
        }
        throw new SvnFsClientException("Internal error");
    }
}
