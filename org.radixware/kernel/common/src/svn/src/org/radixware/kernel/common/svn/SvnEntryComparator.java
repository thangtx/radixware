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
package org.radixware.kernel.common.svn;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.radixware.kernel.common.svn.client.ISvnLogHandler;
import org.radixware.kernel.common.svn.client.SvnEntry;
import org.radixware.kernel.common.svn.client.SvnLogEntry;

public class SvnEntryComparator {

    public static final int MAY_COPY_NEW = -1;
    public static final int NOT_MAY_COPY = -2;

    public static boolean equals(final SVNRepositoryAdapter repository, final String path1, final String path2) throws RadixSvnException, UnsupportedEncodingException {

        long l = repository.getLatestRevision();
//        SVNDirEntry dirEntry1 = repository.info(path1, l);        
//        SVNDirEntry dirEntry2 = repository.info(path2, l);
//        
//        if (dirEntry1.getSize()!=dirEntry2.getSize()){
//            return false;
//        }
//        
//        final Sha1OutputStream sha1 = new Sha1OutputStream();
//        sha1.reset();        
//        SVN.getFile(repository, path1, l, sha1);
//        final byte[] digest1 = sha1.digest();
//                
//        sha1.reset();        
//        SVN.getFile(repository, path2, l, sha1);
//        final byte[] digest2 = sha1.digest();
//        
//        return Arrays.equals(digest1,digest2);    
        return Arrays.equals(getFileHash(repository, path1, l), getFileHash(repository, path2, l));
    }

    private static class ISVNLogEntryHandlerEx implements ISvnLogHandler {

        List<Long> arr;

        ISVNLogEntryHandlerEx(List<Long> arr) {
            this.arr = arr;
        }

        @Override
        public void accept(SvnLogEntry revision) throws RadixSvnException {
            arr.add(revision.getRevision());
        }

    }

    public static byte[] getFileHash(final SVNRepositoryAdapter repository, final String path, final long revision) throws RadixSvnException, UnsupportedEncodingException {
        final String checksum = SVN.getChecksum(repository, path, revision);
        return checksum==null ? null : checksum.getBytes("UTF-8");
        //final Sha1OutputStream sha1 = new Sha1OutputStream();
        //SVN.getFile(repository, path, revision, sha1);
        //return sha1.digest();
    }

    public static List<byte[]> getFileHashes(final SVNRepositoryAdapter repository, final String path, final List<Long> revisions) throws RadixSvnException, UnsupportedEncodingException {
        List<byte[]> ret = new ArrayList(revisions.size());
//        final Sha1OutputStream sha1 = new Sha1OutputStream();
        for (Long r : revisions) {
//            sha1.reset();        
//            SVN.getFile(repository, path, r.longValue(), sha1);
//            ret.add(sha1.digest());
            ret.add(SVN.getChecksum(repository, path, r).getBytes("UTF-8"));
        }
        return ret;
    }

    public static int equalsToThePreviousVersions(final SVNRepositoryAdapter repository, final String basePath, final String path) throws RadixSvnException, UnsupportedEncodingException {

        int cnt = 0;
        long baseLastRevision = repository.getLatestRevision();
//        SVNDirEntry dirEntry1 = repository.info(basePath, baseLastRevision);
//        
//        final Sha1OutputStream sha1 = new Sha1OutputStream();
//        
//        sha1.reset();        
//        SVN.getFile(repository, basePath, baseLastRevision, sha1);
//        final byte[] digest1 = sha1.digest();
        final byte[] digest1 = SVN.getChecksum(repository, basePath, baseLastRevision).getBytes("UTF-8");

        long lastRevision = repository.getLatestRevision();

        List<Long> arr = new ArrayList();
        repository.log(path, lastRevision, 1, new ISVNLogEntryHandlerEx(arr));

        for (int i = 0; i < arr.size(); i++) {

            final byte[] digest2 = SVN.getChecksum(repository, path, arr.get(i)).getBytes();

            if (Arrays.equals(digest1, digest2)) {
                return cnt;
            }
            cnt++;
        }

        return NOT_MAY_COPY;
    }

    public static class EntrySizeAndEntrySha1Digest {

        public long entrySize;
        public byte[] entryDigest;

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof EntrySizeAndEntrySha1Digest) {
                EntrySizeAndEntrySha1Digest entry = (EntrySizeAndEntrySha1Digest) obj;
                return Arrays.equals(entryDigest, entry.entryDigest) && entrySize == entry.entrySize;
            }
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 67 * hash + (int) (this.entrySize ^ (this.entrySize >>> 32));
            hash = 67 * hash + Arrays.hashCode(this.entryDigest);
            return hash;
        }
    }

    public static EntrySizeAndEntrySha1Digest getEntrySizeAndEntrySha1DigestByString(final String s) throws IOException {
        EntrySizeAndEntrySha1Digest entry = new EntrySizeAndEntrySha1Digest();

        final Sha1OutputStream sha1 = new Sha1OutputStream();
        sha1.reset();
        sha1.write(s.getBytes("UTF-8"));

        entry.entrySize = s.getBytes().length;
        entry.entryDigest = sha1.digest();

        return entry;
    }

    public static EntrySizeAndEntrySha1Digest getEntrySizeAndEntryTopSha1Digest(final SVNRepositoryAdapter repository, final String path) throws IOException, RadixSvnException {
        EntrySizeAndEntrySha1Digest entry = new EntrySizeAndEntrySha1Digest();
        long lastRevision = repository.getLatestRevision();
        SvnEntry dirEntry2 = repository.info(path, lastRevision);
        entry.entrySize = dirEntry2.getSize();
        final Sha1OutputStream sha1 = new Sha1OutputStream();
        sha1.reset();
        SVN.getFile(repository, path, lastRevision, sha1);
        entry.entryDigest = sha1.digest();
        return entry;
    }

    public static interface StringConverter {

        public String convert(String str) throws Exception;
    }

    public static int equalsToThePreviousVersions(final SVNRepositoryAdapter repository, final EntrySizeAndEntrySha1Digest beseEntry, final String path, StringConverter converter) throws Exception {

//        long baseLastRevision = repository.getLatestRevision();
//        SVNDirEntry dirEntry1 = repository.info(basePath, baseLastRevision);
        final Sha1OutputStream sha1 = new Sha1OutputStream();
//        
//        sha1.reset();        
//        SVN.getFile(repository, basePath, baseLastRevision, sha1);
//        final byte[] digest1 = sha1.digest();
//        final byte[] digest1 = SVN.getChecksum(repository, basePath, baseLastRevision).getBytes();

        long lastRevision = repository.getLatestRevision();

        List<Long> arr = new ArrayList();
        repository.log(path, lastRevision, 1, new ISVNLogEntryHandlerEx(arr));
        int cnt = 0;

        for (int i = 0; i < arr.size(); i++) {
            if (converter == null) {
                SvnEntry dirEntry2 = repository.info(path, arr.get(i));
                if (beseEntry.entrySize != dirEntry2.getSize()) {

                    continue;
                }
            }

            String s = SVN.getFileAsStr(repository, path, arr.get(i));
            if (s == null) {

                continue;
            }
            if (converter != null) {
                s = converter.convert(s);

                if (beseEntry.entrySize != s.getBytes("UTF-8").length) {

                    continue;
                }
            }

            sha1.reset();
            sha1.write(s.getBytes());

            final byte[] digest2 = sha1.digest();

            if (Arrays.equals(beseEntry.entryDigest, digest2)) {
                return cnt;
            }
            cnt++;
        }
        return NOT_MAY_COPY;
    }

    private static class Sha1OutputStream extends OutputStream {

        private final MessageDigest sha1;

        public Sha1OutputStream() {
            try {
                this.sha1 = MessageDigest.getInstance("SHA1");
            } catch (NoSuchAlgorithmException ex) {
                throw new IllegalStateException(ex);
            }
        }

        @Override
        public void write(int b) throws IOException {
            sha1.update((byte) b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            sha1.update(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            sha1.update(b, off, len);
        }

        public byte[] digest() {
            return sha1.digest();
        }

        public void reset() {
            sha1.reset();
        }
    }
}
