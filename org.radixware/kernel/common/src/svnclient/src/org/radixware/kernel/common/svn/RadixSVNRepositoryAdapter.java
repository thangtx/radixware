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
package org.radixware.kernel.common.svn;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.net.ssl.X509TrustManager;
import org.radixware.kernel.common.svn.client.ISvnLogHandler;
import org.radixware.kernel.common.svn.client.ISvnPasswordProvider;
import org.radixware.kernel.common.svn.client.SvnAuthType;
import org.radixware.kernel.common.svn.client.SvnCommitSummary;
import org.radixware.kernel.common.svn.client.SvnCredentials;
import org.radixware.kernel.common.svn.client.SvnEditor;
import org.radixware.kernel.common.svn.client.SvnEntry;
import org.radixware.kernel.common.svn.client.SvnEntryHandler;
import org.radixware.kernel.common.svn.client.SvnPath;
import org.radixware.kernel.common.svn.client.SvnProperties;
import org.radixware.kernel.common.svn.client.SvnRepository;

/**
 *
 * @author akrylov
 */
class RadixSVNRepositoryAdapter implements SVNRepositoryAdapter {

    static SVNRepositoryAdapter createRootRepository(Object object, String anyInsideUrl, String user, SvnAuthType auth, String sshKeyFile, ISvnPasswordProvider passwordGetter) throws RadixSvnException {
        try {
            URI uri = new URI(anyInsideUrl);
            SvnCredentials credentials;
            switch (auth) {
                case SSH_KEY_FILE:
                    credentials = SvnCredentials.Factory.newSshKeyFileInstance(user, new File(sshKeyFile));
                    break;
                case SSL:
                    credentials = SvnCredentials.Factory.newCertificateInstance(user, new File(sshKeyFile), passwordGetter);
                    break;
                case SVN_PASSWORD:
                    credentials = SvnCredentials.Factory.newSVNPasswordInstance(user, passwordGetter);
                    break;
                case SSH_PASSWORD:
                    credentials = SvnCredentials.Factory.newSSHPasswordInstance(user, passwordGetter);
                    break;
                case NONE:
                    credentials = SvnCredentials.Factory.newEmptyInstance();
                    break;
                default:
                    throw new RadixSvnException("Unexpected authentication type");
            }

            SvnRepository repo = SvnRepository.Factory.newInstance(uri, new SvnCredentials[]{credentials}, null);
            String root = repo.getRootUrl();
            return new RadixSVNRepositoryAdapter(new URI(root), credentials);
        } catch (URISyntaxException ex) {
            throw new RadixSvnException("Incorrect repository url: " + anyInsideUrl, ex);
        }
    }

    static SVNRepositoryAdapter createRepository(String url, String path, String user, SvnAuthType auth, String sshKeyFile, ISvnPasswordProvider passwordGetter) throws RadixSvnException {
        try {
            URI uri = new URI(SvnPath.append(url, path));
            SvnCredentials credentials;
            switch (auth) {
                case SSH_KEY_FILE:
                    credentials = SvnCredentials.Factory.newSshKeyFileInstance(user, new File(sshKeyFile));
                    break;
                case SSL:
                    credentials = SvnCredentials.Factory.newCertificateInstance(user, new File(sshKeyFile), passwordGetter);
                    break;
                case SVN_PASSWORD:
                    credentials = SvnCredentials.Factory.newSVNPasswordInstance(user, passwordGetter);
                    break;
                case SSH_PASSWORD:
                    credentials = SvnCredentials.Factory.newSSHPasswordInstance(user, passwordGetter);
                    break;
                case NONE:
                    credentials = SvnCredentials.Factory.newEmptyInstance();
                    break;
                default:
                    throw new RadixSvnException("Unexpected authentication type");
            }

            return new RadixSVNRepositoryAdapter(uri, credentials);
        } catch (URISyntaxException ex) {
            throw new RadixSvnException("Incorrect repository url: " + SvnPath.append(url, path), ex);
        }
    }

    static SVNRepositoryAdapter createRepository(String url, SVNRepositoryAdapter credentialsProvider) throws RadixSvnException {
        try {
            return new RadixSVNRepositoryAdapter(new URI(url), ((RadixSVNRepositoryAdapter) credentialsProvider).initialCredentials);
        } catch (URISyntaxException ex) {
            throw new RadixSvnException("Incorrect repository url: " + url, ex);
        }
    }

    private static final class EditorImpl implements RadixSVNRepositoryAdapter.Editor {

        private final SvnEditor editor;

        public EditorImpl(SvnEditor editor) {
            this.editor = editor;
        }

        @Override
        public void cancel() throws RadixSvnException {
            editor.abortEdit();
        }

        @Override
        public SvnCommitSummary commit() throws RadixSvnException {
            return editor.closeEdit();
        }

        @Override
        public void openDir(String name) throws RadixSvnException {
            editor.openDir(name, -1);
        }

        @Override
        public void openDir(String name, SvnConnectionType connectionType) throws RadixSvnException {
            editor.openDir(name, -1);
        }

        @Override
        public int openDirs(String path) throws RadixSvnException {
            if (path == null || path.isEmpty()) {
                editor.openRoot(-1);
                return 1;
            } else {
                List<String> dirs = parseDir(path);
                for (String s : dirs) {
                    if (!s.isEmpty()) {
                        editor.openDir(s, -1);
                    }
                }
                return dirs.size();
            }
        }

        @Override
        public void appendDir(String name) throws RadixSvnException {
            editor.addDir(name, null, -1);
        }

        @Override
        public void copyDir(String from, String to, long fromRevision) throws RadixSvnException {
            editor.addDir(to, from, fromRevision);
        }

        @Override
        public int appendDirs(String path) throws RadixSvnException {
            List<String> dirs = parseDir(path);
            for (String s : dirs) {
                try {
                    editor.addDir(s, null, -1);
                } catch (RadixSvnException ex) {
//                  //Path already exists - do nothing
//                    editor.closeDir();
//                    editor.openDir(s, -1);
                }
            }
            return dirs.size();
        }

        private List<String> parseDir(String dir) {
            List<String> list = new ArrayList<>();
            int pos = 0;
            while (true) {
                if (pos >= dir.length()) {
                    break;
                }
                int currIndex = dir.indexOf("/", pos);
                if (currIndex == -1) {
                    if (pos < dir.length()) {
                        String s = dir;//.substring(pos);
                        if (!s.isEmpty()) {
                            list.add(s);

                        }
                    }
                    break;
                }
                String s = dir.substring(0, currIndex);
                pos = currIndex + 1;
                if (!s.isEmpty()) {
                    list.add(s);
                }
            }

            return list;
        }

        @Override
        public void closeDir() throws RadixSvnException {
            editor.closeDir();
        }

        @Override
        public void closeDirs(int cnt) throws RadixSvnException {
            for (int i = 0; i < cnt; i++) {
                editor.closeDir();
            }
        }

        @Override
        public void modifyFile(String name, byte[] data) throws RadixSvnException {
            editor.updateFile(name, -1, new ByteArrayInputStream(data));
        }

        @Override
        public void modifyFile(String name, InputStream data) throws RadixSvnException {
            editor.updateFile(name, -1, data);
        }

        @Override
        public void appendFile(String name, byte[] data) throws RadixSvnException {
            editor.appendFile(name, -1, new ByteArrayInputStream(data));
        }

        @Override
        public void appendFile(String name, InputStream data) throws RadixSvnException {
            editor.appendFile(name, -1, data);
        }

        @Override
        public void copyFile(String from, String to, long fromRevision) throws RadixSvnException {
            editor.addFile(to, from, fromRevision);
        }

        @Override
        public void deleteEntry(String name, long revision) throws RadixSvnException {
            editor.deleteEntry(name, revision);
        }

    }
    private SvnRepository radix;
    SvnCredentials[] initialCredentials;

    public RadixSVNRepositoryAdapter(URI localtion, SvnCredentials[] credentials, X509TrustManager trustManager) throws RadixSvnException {
        radix = SvnRepository.Factory.newInstance(localtion, credentials, trustManager);
        this.initialCredentials = credentials;
    }

    public RadixSVNRepositoryAdapter(URI localtion, SvnCredentials[] credentials) throws RadixSvnException {
        radix = SvnRepository.Factory.newInstance(localtion, credentials, null);
        this.initialCredentials = credentials;
    }

    public RadixSVNRepositoryAdapter(URI localtion, SvnCredentials credentials) throws RadixSvnException {
        this(localtion, new SvnCredentials[]{credentials});
    }

    @Override
    public Editor createEditor(String commitMessage) throws RadixSvnException {
        return new EditorImpl(radix.getEditor(commitMessage));
    }

    @Override
    public String getLocation() {
        return radix.getLocation().toString();
    }

    @Override
    public String getRepositoryRoot() throws RadixSvnException {
        return radix.getRootUrl();
    }

    @Override
    public String getLocalPath() throws RadixSvnException {
        return radix.getPath();
    }

    @Override
    public boolean isDir(String path, long revision) throws RadixSvnException {
        return checkPath(path, revision) == SvnEntry.Kind.DIRECTORY;
    }

    @Override
    public boolean isFile(String path, long revision) throws RadixSvnException {
        return checkPath(path, revision) == SvnEntry.Kind.FILE;
    }

    @Override
    public boolean isNone(String path, long revision) throws RadixSvnException {
        return checkPath(path, revision) == SvnEntry.Kind.NONE;
    }

    @Override
    public long getLatestRevision() throws RadixSvnException {
        return radix.getLatestRevision();
    }

    @Override
    public byte[] getFileBytes(String path, long revision) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            radix.getFile(path, revision, null, out);
            return out.toByteArray();
        } catch (RadixSvnException ex) {
            return null;
        }
    }
    

    @Override
    public SvnEntry.Kind checkPath(String path, long revision) throws RadixSvnException {
        return radix.checkPath(path, revision);
    }

    @Override
    public SvnEntry info(String path, long revision) throws RadixSvnException {
        return radix.info(path, revision);
    }

    @Override
    public long getFile(String path, long revision, OutputStream contents) throws RadixSvnException {
        return radix.getFile(path, revision, null, contents);
    }

    @Override
    public long getFile(String path, long revision, Map<String, String> props, OutputStream contents) throws RadixSvnException {
        return radix.getFile(path, revision, props, contents);
    }

    @Override
    public long getFile(String path, long revision, Map<String, String> props) throws RadixSvnException {
        return radix.getFile(path, revision, props, null);
    }

    @Override
    public void getDir(String path, long revision, final EntryHandler handler) throws RadixSvnException {
        radix.getDir(path, revision, false, true, false, new SvnEntryHandler() {

            @Override
            public void accept(SvnEntry entry) throws RadixSvnException {
                handler.accept(entry);
            }
        });
    }

    @Override
    public SvnEntry getDir(final String path, long revision) throws RadixSvnException {
        return radix.getDir(path, revision, false, true, false, null);
    }

    @Override
    public void close() {
        radix.close();
    }

    @Override
    public void log(String path, long startRevision, long endRevision, ISvnLogHandler logHandler) throws RadixSvnException {
        radix.log(path, startRevision, endRevision, false, logHandler);
    }

    @Override
    public void log(String path, long startRevision, long endRevision, boolean strictNodes, ISvnLogHandler logHandler) throws RadixSvnException {
        radix.log(path, startRevision, endRevision, false, strictNodes, logHandler);
    }

    @Override
    public void log(String[] pathes, long startRevision, long endRevision, ISvnLogHandler logHandler) throws RadixSvnException {
        for (String path : pathes) {
            log(path, startRevision, endRevision, logHandler);
        }
    }

    @Override
    public SvnProperties getRevisionProperties(long revision) throws RadixSvnException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRevisionProperty(long revision, String propName, SvnProperties.Value propValue) throws RadixSvnException {
        radix.setRevisionPropertyValue(revision, propName, propValue);
    }

    @Override
    public void replay(long lowRevision, long revision, boolean sendDeltas, Editor editor) throws RadixSvnException {
        radix.replay(lowRevision, revision, sendDeltas, ((RadixSVNRepositoryAdapter.EditorImpl) editor).editor);
    }

}
