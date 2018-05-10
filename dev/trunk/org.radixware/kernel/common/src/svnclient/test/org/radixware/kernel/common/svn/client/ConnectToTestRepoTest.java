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
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Ignore;
import org.junit.Test;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;

/**
 *
 * @author akrylov
 */
public class ConnectToTestRepoTest {

    @Test
    @Ignore
    public void test() throws RadixSvnException {
        SVNRepositoryAdapter.Builder builder = new SVNRepositoryAdapter.Builder();
        SVNRepositoryAdapter adapter = builder.addUserName("admin", "admin".toCharArray()).build(URI.create("http://10.77.206.25/svn/optt"));
        long revision = adapter.getLatestRevision();
        String rootUrl = adapter.getRepositoryRoot();
        adapter.getDir("", revision, new SVNRepositoryAdapter.EntryHandler() {

            @Override
            public void accept(SvnEntry entry) throws RadixSvnException {
                System.out.println(entry.getName());
            }
        });

        byte[] layerXml = adapter.getFileBytes("prod/com.optt/layer.xml", -1);

        System.out.println(new String(layerXml));
        System.out.println(revision);
    }

    @Test
    public void testPublicTLS() throws RadixSvnException {
        SVNRepositoryAdapter.Builder builder = new SVNRepositoryAdapter.Builder();
        SVNRepositoryAdapter adapter = builder.addUserName("admin", "admin".toCharArray()).build(URI.create("https://10.77.206.44:18080/svn/repos1/prod"));
        long revision = adapter.getLatestRevision();
        String rootUrl = adapter.getRepositoryRoot();
//        adapter.getDir("", revision, new SVNRepositoryAdapter.EntryHandler() {
//
//            @Override
//            public void accept(SvnEntry entry) throws RadixSvnException {
//                System.out.println(entry.getName());
//            }
//        });
//
//        byte[] layerXml = adapter.getFileBytes("prod/com.optt/layer.xml", -1);
//
//        System.out.println(new String(layerXml));
        System.out.println(revision);
    }

    @Test
    public void testPublicSVN() throws RadixSvnException, URISyntaxException {
        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("svn+ssh://svn2.compassplus.ru:/radix/releases/2.1.6.10.7"), new SvnCredentials[]{SvnCredentials.Factory.newSshKeyFileInstance("svn", new File("/home/akrylov/.ssh/id_rsa"))});
        long revision = instance.getLatestRevision();
        SvnEntry root = instance.getDir("", revision, false, false, false, null);
        
        System.out.println(root.getPath());
    }
}
