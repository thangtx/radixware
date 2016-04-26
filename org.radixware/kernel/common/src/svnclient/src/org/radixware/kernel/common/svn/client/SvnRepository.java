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

import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import javax.net.ssl.X509TrustManager;
import org.radixware.kernel.common.svn.RadixSvnException;

/**
 *
 * @author akrylov
 */
public abstract class SvnRepository {

    public static final class Factory {

        private Factory() {
        }

        public static SvnRepository newInstance(URI location, SvnCredentials[] credentials) throws RadixSvnException {
            return newInstance(location, credentials, null);
        }

        public static SvnRepository newInstance(URI location, SvnCredentials[] credentials, X509TrustManager trustManager) throws RadixSvnException {
            SvnRepository repo = newInstanceImpl(location, "", credentials, trustManager);
            String root = repo.getRootUrl();
            if (root.equals(location.toString())) {
                return repo;
            } else {
                try {
                    String path = location.toString().substring(root.length());
                    if (path.startsWith("/")) {
                        path = path.substring(1);
                    }
                    return newInstanceImpl(new URI(root), path, credentials, trustManager);
                } catch (URISyntaxException ex) {
                    throw new RadixSvnException(ex);
                }
            }
        }

        private static SvnRepository newInstanceImpl(URI location, String path, SvnCredentials[] credentials, X509TrustManager trustManager) {
            if ("http".equals(location.getScheme()) || "https".equals(location.getScheme())) {
                return new SvnHttpRepository(location, path, credentials, trustManager);
            } else {
                return new SvnRARepository(location, path, credentials);
            }
        }
    }

    private final SvnCredentials[] credentials;
    private final X509TrustManager trustManager;
    private SvnCredentials activeCredentials;
    private SvnConnection connection;
    private final URI location;
    private final String myPath;
    private String rootUrl;
    private String uuid;

    public SvnRepository(URI location, String path, SvnCredentials[] credentials, X509TrustManager trustManager) {
        this.credentials = credentials;
        this.trustManager = trustManager;
        this.location = location;
        this.myPath = path;
    }

    public X509TrustManager getTrustManager() {
        return trustManager;
    }

    public SvnCredentials getCredentials() {
        return activeCredentials;
    }

    public SvnConnection getConnection() {
        return connection;
    }

    public void close() {
        disconnectImpl();
    }

    public URI getLocation() {
        return location;
    }

    public String getPath() throws RadixSvnException {
        return myPath;
    }

    public void connect() throws RadixSvnException {

        synchronized (this) {
//            if (connection != null) {
//                disconnect();
//            }

            if (connection != null) {
                if (!connection.isAlive()) {
                    disconnectImpl();
                } else {
                    return;
                }
            }
            int credentialsIndex = 0;

            if (activeCredentials == null) {
                while (activeCredentials == null) {
                    if (credentialsIndex >= credentials.length) {
                        throw new RadixSvnException(RadixSvnException.Type.REJECT_AUTH);
                    }
                    activeCredentials = credentials[credentialsIndex];
                    connection = createConnection();
                    try {
                        connection.open(this);
                        break;
                    } catch (RadixSvnException ex) {
                        credentialsIndex++;

                        activeCredentials = null;
                        if (credentialsIndex >= credentials.length) {
                            throw ex;
                        }
                        continue;
                    }
                }
            } else {

                connection = createConnection();
                try {
                    connection.open(this);
                } catch (RadixSvnException ex) {
                    if (ex.isAuthenticationCancelled()) {
                        throw ex;
                    }
                    throw new RadixSvnException("Authentication required", ex);
                }
            }
        }
    }

    protected void disconnectImpl() {
        synchronized (this) {
            if (connection != null) {
                try {
                    connection.close();
                } finally {
                    connection = null;
                }
            }
        }
    }

    protected void disconnect() {
        disconnect(false);
    }

    protected void disconnect(boolean force) {
        synchronized (this) {
            if (force) {
                disconnectImpl();
            }
//            if (disconnectorTask == null) {
//                disconnector.schedule(disconnectorTask = new TimerTask() {
//
//                    @Override
//                    public void run() {
//                        disconnectImpl();
//                    }
//                }, 3000);
//            }
        }
    }

    protected abstract SvnConnection createConnection();

    public String getExternalUserName() {
        return null;
    }

    protected String getPathRelativeToMine(String path) throws RadixSvnException {

        final String mPath = getPath();
        if (path.equals(".")) {
            return mPath;
        }
        if (path.startsWith("/")) {
            return path;
        } else {
            return SvnPath.append(mPath, path);
        }
    }

    protected abstract SvnConnector createConnector();

    protected void authenticate() throws RadixSvnException {
        if (connection != null) {
            connection.authenticate(this);
        }
    }

    protected void setInfo(String uuid, String root) {
        rootUrl = root;
        this.uuid = uuid;
    }

    protected void setUUID(String uuid) {
        this.uuid = uuid;
    }

    protected void setRootUrl(String url) {
        rootUrl = url;
    }

    public String getRootUrl() throws RadixSvnException {
        if (rootUrl == null) {
            connect();
            disconnect();
        }
        return rootUrl;
    }

    public String getPathInRepository(String path) {
        if (path.startsWith(rootUrl)) {
            return path.substring(rootUrl.length());
        } else {
            return path;
        }
    }

    public abstract SvnEntry getDir(String path, long revision, boolean wantProps, boolean wantContents, boolean wantInheritedProps, SvnEntryHandler handler) throws RadixSvnException;

    public abstract long getLatestRevision() throws RadixSvnException;

    public abstract SvnEntry info(String path, long revision) throws RadixSvnException;

    public abstract long getFile(String path, long revision, Map<String, String> props, OutputStream contents) throws RadixSvnException;

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public abstract SvnEntry.Kind checkPath(String path, long revision) throws RadixSvnException;

    public abstract SvnEditor getEditor(String logMessage) throws RadixSvnException;

    public abstract void replay(long lowRevision, long highRevision, boolean sendDeltas, SvnEditor editor) throws RadixSvnException;

    public abstract void setRevisionPropertyValue(long revision, String propertyName, SvnProperties.Value value) throws RadixSvnException;

    public abstract SvnProperties getRevisionProperties(long revision, SvnProperties properties) throws RadixSvnException;

    public abstract void log(String path, long startRevision, long endRevision, boolean changedPaths, final ISvnLogHandler logHandler) throws RadixSvnException;

    public abstract void log(String path, long startRevision, long endRevision, boolean changedPaths, boolean strictNode, final ISvnLogHandler logHandler) throws RadixSvnException;

    public abstract void log(String pathes[], long startRevision, long endRevision, boolean changedPaths, final ISvnLogHandler logHandler) throws RadixSvnException;

    public abstract void log(String pathes[], long startRevision, long endRevision, boolean changedPaths, boolean strictNode, final ISvnLogHandler logHandler) throws RadixSvnException;
}
