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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.svn.client.ISvnFSClient;
import org.radixware.kernel.common.svn.client.ISvnPasswordProvider;
import org.radixware.kernel.common.svn.client.SvnAuthType;
import org.radixware.kernel.common.svn.client.SvnCredentials;
import org.radixware.kernel.common.svn.client.SvnPath;

/**
 *
 * @author akrylov
 */
class EngineLookup {

    private static boolean isUseRadixClient = false;
    private static SVNRepositoryAdapter.IEngine engine;

    static {
        engine = loadEngine();
    }

    static SVNRepositoryAdapter.IEngine getEngine() {
        return engine;
    }

    static boolean isUseRadixClient() {
        return isUseRadixClient;
    }

    private static SVNRepositoryAdapter.IEngine loadEngine() {

        String engineClassName = System.getProperty("org.radixware.svn.client.engine-class-name");
        //String engineClassName = ("org.radixware.kernel.common.svn.svnkitdept.SvnRepositoryAdapterBuilderSvnKitEngine");
        
        
        if (engineClassName != null) {
            try {
                Class engineClass = EngineLookup.class.getClassLoader().loadClass(engineClassName);
                if (SVNRepositoryAdapter.IEngine.class.isAssignableFrom(engineClass)) {
                    return (SVNRepositoryAdapter.IEngine) engineClass.newInstance();
                }
            } catch (Throwable ex) {
                //on any error reset to RadixClient
                Logger.getLogger(SVNRepositoryAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        isUseRadixClient = true;
        return new SVNRepositoryAdapter.IEngine() {

            @Override
            public SVNRepositoryAdapter build(SVNRepositoryAdapter.Builder builder, URI uri) throws RadixSvnException {
                return new RadixSVNRepositoryAdapter(uri, builder.getCredentials().toArray(new SvnCredentials[builder.getCredentials().size()]), builder.getTrustManager());
            }

            @Override
            public SVNRepositoryAdapter build(SVNRepositoryAdapter.Builder builder, String uri, String path) throws RadixSvnException {
                try {
                    return new RadixSVNRepositoryAdapter(new URI(SvnPath.append(uri, path)), builder.getCredentials().toArray(new SvnCredentials[builder.getCredentials().size()]), builder.getTrustManager());
                } catch (URISyntaxException ex) {
                    throw new RadixSvnException(ex);
                }
            }

            @Override
            public SVNRepositoryAdapter newInstance(ISvnFSClient client, File localFile, String user, SvnAuthType auth, String sshKeyFile) throws RadixSvnException {
                if (!localFile.isDirectory()) {
                    localFile = localFile.getParentFile();
                }
                ISvnFSClient.ISvnStatus status = client.getSingleStatus(localFile);
                String directoryUrl = status.getUrlString();
                SVNRepositoryAdapter adapter = RadixSVNRepositoryAdapter.createRootRepository(null, directoryUrl, user, auth, sshKeyFile, new LoginDialog.PasswordGetter());
                String repoRoot = adapter.getRepositoryRoot();
                String path = SvnPath.getRelativePath(repoRoot, directoryUrl);
                return newInstance(repoRoot, path, user, auth, sshKeyFile);
            }

            @Override
            public SVNRepositoryAdapter newRootInstance(String anyInsideUrl, String user, SvnAuthType auth, String sshKeyFile) throws RadixSvnException {
                return RadixSVNRepositoryAdapter.createRootRepository(null, anyInsideUrl, user, auth, sshKeyFile, new LoginDialog.PasswordGetter());
            }

            @Override
            public SVNRepositoryAdapter newInstance(String url, String path, String user, SvnAuthType auth, String sshKeyFilePath) throws RadixSvnException {
                return RadixSVNRepositoryAdapter.createRepository(url, path, user, auth, sshKeyFilePath, new LoginDialog.PasswordGetter());
            }

            @Override
            public SVNRepositoryAdapter newInstance(String url, String path, String user, SvnAuthType auth, String sshKeyFilePath, ISvnPasswordProvider passwordGetter) throws RadixSvnException {
                return RadixSVNRepositoryAdapter.createRepository(url, path, user, auth, sshKeyFilePath, passwordGetter);
            }

//            @Override
//            public SVNRepositoryAdapter newInstance(SVNRepositoryExtendedOptions options, String url, String path, String user, SvnAuthType auth, String sshKeyFilePath, ISvnPasswordProvider passwordGetter) throws RadixSvnException {
//                return RadixSVNRepositoryAdapter.createRepository(url, path, user, auth, sshKeyFilePath, passwordGetter);
//            }

            @Override
            public SVNRepositoryAdapter newInstance(String url, SVNRepositoryAdapter credentialsProvider) throws RadixSvnException {
                return RadixSVNRepositoryAdapter.createRepository(url, credentialsProvider);
            }

        };
    }
}
