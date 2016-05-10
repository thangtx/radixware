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

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.net.ssl.X509TrustManager;
import org.radixware.kernel.common.svn.client.ISvnFSClient;
import org.radixware.kernel.common.svn.client.ISvnLogHandler;
import org.radixware.kernel.common.svn.client.ISvnPasswordProvider;
import org.radixware.kernel.common.svn.client.SvnAuthType;
import org.radixware.kernel.common.svn.client.SvnCommitSummary;
import org.radixware.kernel.common.svn.client.SvnCredentials;
import org.radixware.kernel.common.svn.client.SvnEntry;
import org.radixware.kernel.common.svn.client.SvnProperties;

/**
 *
 * @author akrylov
 */
public interface SVNRepositoryAdapter {

    public interface IEngine {

        public SVNRepositoryAdapter build(Builder builder, URI uri) throws RadixSvnException;

        public SVNRepositoryAdapter build(Builder builder, String uri, String path) throws RadixSvnException;

        public SVNRepositoryAdapter newInstance(ISvnFSClient client, File localFile, String user, SvnAuthType auth, String sshKeyFile) throws RadixSvnException;

        public SVNRepositoryAdapter newRootInstance(String anyInsideUrl, String user, SvnAuthType auth, String sshKeyFile) throws RadixSvnException;

        public SVNRepositoryAdapter newInstance(String url, String path, String user, SvnAuthType auth, String sshKeyFilePath) throws RadixSvnException;

        public SVNRepositoryAdapter newInstance(String url, String path, String user, SvnAuthType auth, String sshKeyFilePath, ISvnPasswordProvider passwordGetter) throws RadixSvnException;

        public SVNRepositoryAdapter newInstance(SVNRepositoryExtendedOptions options, String url, String path, String user, SvnAuthType auth, String sshKeyFilePath, ISvnPasswordProvider passwordGetter) throws RadixSvnException;

        public SVNRepositoryAdapter newInstance(String url, SVNRepositoryAdapter credentialsProvider) throws RadixSvnException;
    }

    public class Builder {

        private X509TrustManager trustManager;
        private List<SvnCredentials> credentials = new LinkedList<>();

        public Builder() {
            credentials.add(SvnCredentials.Factory.newEmptyInstance());
        }

        public X509TrustManager getTrustManager() {
            return trustManager;
        }

        public List<SvnCredentials> getCredentials() {
            return credentials;
        }

        public Builder addUserName(String userName, char[] password) {
            credentials.add(new SvnCredentials(SvnAuthType.SVN_PASSWORD, userName, password, null, null, null, null));
            return this;
        }

        public Builder addUserName(String userName) {
            credentials.add(new SvnCredentials(SvnAuthType.NONE, userName, null, null, null, null, null));
            return this;
        }

        public Builder addSSH(String userName, char[] password) {
            credentials.add(new SvnCredentials(SvnAuthType.SSH_PASSWORD, userName, password, null, null, null, null));
            return this;
        }

        public Builder addSSH(String userName, String keyFile, char[] keyFilePassPhrase) {
            credentials.add(new SvnCredentials(SvnAuthType.SSH_KEY_FILE, userName, null, keyFile, null, keyFilePassPhrase, null));
            return this;
        }

        public Builder addSSL(String userName, String clientCertFile) {
            credentials.add(new SvnCredentials(SvnAuthType.SSL, userName, null, null, clientCertFile, null, null));
            return this;
        }

        public Builder addSSL(String userName, String clientCertFile, char[] clientCertPassword) {
            credentials.add(new SvnCredentials(SvnAuthType.SSL, userName, null, null, clientCertFile, clientCertPassword, null));
            return this;
        }

        public void setTrustManager(X509TrustManager trustManager) {
            this.trustManager = trustManager;
        }

        public SVNRepositoryAdapter build(URI uri) throws RadixSvnException {
            return EngineLookup.getEngine().build(this, uri);
        }

        public SVNRepositoryAdapter build(String uri, String path) throws RadixSvnException {
            return EngineLookup.getEngine().build(this, uri, path);
        }
    }

    public interface Editor {

        void cancel() throws RadixSvnException;

        SvnCommitSummary commit() throws RadixSvnException;

        void openDir(String name) throws RadixSvnException;

        void openDir(String name, SvnConnectionType connectionType) throws RadixSvnException;

        int openDirs(String path) throws RadixSvnException;

        void appendDir(String name) throws RadixSvnException;

        void copyDir(String from, String to, long fromRevision) throws RadixSvnException;

        int appendDirs(String path) throws RadixSvnException;

        void closeDir() throws RadixSvnException;

        void closeDirs(int cnt) throws RadixSvnException;

        void modifyFile(String name, byte[] data) throws RadixSvnException;

        void modifyFile(String name, InputStream data) throws RadixSvnException;

        void appendFile(String name, byte[] data) throws RadixSvnException;

        void appendFile(String name, InputStream data) throws RadixSvnException;

        void copyFile(String from, String to, long fromRevision) throws RadixSvnException;

        void deleteEntry(String name, long revision) throws RadixSvnException;
    }

    interface EntryHandler {

        void accept(SvnEntry entry) throws RadixSvnException;
    }

    public static final class Factory {

        private Factory() {
        }

        public static boolean isUseRadixClient() {
            return EngineLookup.isUseRadixClient();
        }

        public static SVNRepositoryAdapter newInstance(ISvnFSClient client, File localFile, String user, SvnAuthType auth, String sshKeyFile) throws RadixSvnException {
            return EngineLookup.getEngine().newInstance(client, localFile, user, auth, sshKeyFile);
        }

        public static SVNRepositoryAdapter newRootInstance(String anyInsideUrl, String user, SvnAuthType auth, String sshKeyFile) throws RadixSvnException {
            return EngineLookup.getEngine().newRootInstance(anyInsideUrl, user, auth, sshKeyFile);
        }

        public static SVNRepositoryAdapter newInstance(String url, String path, String user, SvnAuthType auth, String sshKeyFilePath) throws RadixSvnException {
            return EngineLookup.getEngine().newInstance(url, path, user, auth, sshKeyFilePath);
        }

        public static SVNRepositoryAdapter newInstance(String url, SVNRepositoryAdapter credentialsProvider) throws RadixSvnException {
            return EngineLookup.getEngine().newInstance(url, credentialsProvider);
        }

        public static SVNRepositoryAdapter newInstance(String url, String path, String user, SvnAuthType auth, String sshKeyFilePath, ISvnPasswordProvider passwordGetter) throws RadixSvnException {
            return EngineLookup.getEngine().newInstance(url, path, user, auth, sshKeyFilePath, passwordGetter);
        }

        public static SVNRepositoryAdapter newInstance(SVNRepositoryExtendedOptions options, String url, String path, String user, SvnAuthType auth, String sshKeyFilePath, ISvnPasswordProvider passwordGetter) throws RadixSvnException {
            return EngineLookup.getEngine().newInstance(options, url, path, user, auth, sshKeyFilePath, passwordGetter);
        }
    }

    Editor createEditor(String commitMessage) throws RadixSvnException;

    String getLocation();

    String getRepositoryRoot() throws RadixSvnException;

    String getLocalPath() throws RadixSvnException;

    boolean isDir(String path, long revision) throws RadixSvnException;

    boolean isFile(String path, long revision) throws RadixSvnException;

    boolean isNone(String path, long revision) throws RadixSvnException;

    long getLatestRevision() throws RadixSvnException;

    byte[] getFileBytes(String path, long revision);

    
    SvnEntry.Kind checkPath(String path, long revision) throws RadixSvnException;

    SvnEntry info(String path, long revision) throws RadixSvnException;

    long getFile(String path, long revision, OutputStream contents) throws RadixSvnException;

    long getFile(String path, long revision, Map<String, String> props, OutputStream contents) throws RadixSvnException;

    long getFile(String path, long revision, Map<String, String> props) throws RadixSvnException;

    void getDir(String path, long revision, EntryHandler handler) throws RadixSvnException;

    SvnEntry getDir(String path, long revision) throws RadixSvnException;

    void close();

    void log(String path, long startRevision, long endRevision, final ISvnLogHandler logHandler) throws RadixSvnException;

    void log(String path, long startRevision, long endRevision, boolean strictNodes, final ISvnLogHandler logHandler) throws RadixSvnException;

    void log(String pathes[], long startRevision, long endRevision, final ISvnLogHandler logHandler) throws RadixSvnException;

    SvnProperties getRevisionProperties(long revision) throws RadixSvnException;

    void setRevisionProperty(long revision, String propName, SvnProperties.Value propValue) throws RadixSvnException;

    void replay(long lowRevision, long revision, boolean sendDeltas, Editor editor) throws RadixSvnException;

}
