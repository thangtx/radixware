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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.radixware.kernel.common.svn.RadixSvnException;

/**
 *
 * @author akrylov
 */
public class HttpRepositoryTest {

    @Test
    @Ignore
    public void testGetDirRev() throws RadixSvnException, URISyntaxException {

        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("http://localhost/svn/first/"), new SvnCredentials[]{SvnCredentials.Factory.newSVNPasswordInstance("SvnUser", new ISvnPasswordProvider() {

            @Override
            public char[] getPassword(boolean firstAttempt, SvnAuthType authType, String message, String targetTitle, String targetLocation, String userName) throws RadixSvnException {
                return "svn".toCharArray();
            }
        })});
        instance.getDir("", -1, true, false, false, new SvnEntryHandler() {

            @Override
            public void accept(SvnEntry entry) throws RadixSvnException {
                System.out.println(entry.getName() + ":   " + entry.getRevision());
            }
        });
        SvnEditor editor = instance.getEditor("Test");
        editor.addDir("aa", null, -1);
        editor.deleteEntry("aa", -1);
        editor.abortEdit();
    }

    @Test
    @Ignore
    public void testCheckPath() throws RadixSvnException, URISyntaxException {

        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("http://localhost/svn/first/"), new SvnCredentials[]{SvnCredentials.Factory.newSVNPasswordInstance("SvnUser", new ISvnPasswordProvider() {

            @Override
            public char[] getPassword(boolean firstAttempt, SvnAuthType authType, String message, String targetTitle, String targetLocation, String userName) throws RadixSvnException {
                return "svn".toCharArray();
            }
        })});

        SvnEntry.Kind kind = instance.checkPath("scripts/org.radixware/1.2.30-1.2.30.1/scripts.xml", 93);
        Assert.assertEquals(SvnEntry.Kind.NONE, kind);
    }

    @Test
    @Ignore
    public void testSvnProtocol() throws Exception {
        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("svn://10.7.2.138/rep1"), new SvnCredentials[]{SvnCredentials.Factory.newEmptyInstance()});
        long revision = instance.getLatestRevision();
        //    instance.getRevisionProperties(revision);
        instance.getDir("", revision, true, true, true, null);
    }

    @Test
    @Ignore
    public void testGetProps() throws IOException, URISyntaxException, Exception {
        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("https://svn.radixware.org/repos/radix/"), new SvnCredentials[]{SvnCredentials.Factory.newCertificateInstance("akrylov", new File("/home/akrylov/cert2015/akrylov.p12"), "IYiEw=ZXHl)3".toCharArray())});

        long revision = instance.getLatestRevision();
        final Map<String, String> props = new HashMap<>();
        instance.getFile("dev/trunk/branch.xml", revision, props, new SvnRepositoryTest.TestOutputStream());
        Assert.assertFalse(props.isEmpty());
        for (Map.Entry<String, String> p : props.entrySet()) {
            System.out.println(p.getKey() + " = " + p.getValue());
        }

    }

    @Test
    public void testMakeReleaseRadixRepo() throws Exception {
        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("https://svn.radixware.org/repos/radix/"), new SvnCredentials[]{SvnCredentials.Factory.newCertificateInstance("akrylov", new File("/home/akrylov/cert2015/akrylov.p12"), "IYiEw=ZXHl)3".toCharArray())});
        String root = instance.getRootUrl();

        SvnEditor editor = instance.getEditor("SVN Framework test");
        final int editorRevision = -1;
        try {
            editor.openDir("/releases", editorRevision);
            editor.addDir("/releases/____none____", null, editorRevision);
            editor.addDir("/releases/____none____/org.radixware", "dev/trunk/org.radixware", editorRevision);
            editor.closeDir();
            editor.closeDir();
            editor.closeDir();
            editor.openDir("/releases", editorRevision);
            editor.openDir("/releases/____none____", editorRevision);
            editor.openDir("/releases/____none____/org.radixware", editorRevision);
            editor.openDir("/releases/____none____/org.radixware/ads", editorRevision);
            editor.openDir("/releases/____none____/org.radixware/ads/Workflow", editorRevision);
            editor.appendFile("/releases/____none____/org.radixware/ads/Workflow/directory.xml", editorRevision, "test");
            editor.closeEdit();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            try {
                editor = instance.getEditor("SVN Framework test");
                editor.openDir("/releases", editorRevision);
                editor.deleteEntry("/releases/____none____", editorRevision);
                editor.closeEdit();
            } catch (Throwable ex) {
            }
        }
    }

    @Test
    @Ignore
    public void testSvnCopy() throws Exception {
        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("http://localhost/svn/first"), new SvnCredentials[]{SvnCredentials.Factory.newSVNPasswordInstance("SvnUser", new ISvnPasswordProvider() {

            @Override
            public char[] getPassword(boolean firstAttempt, SvnAuthType authType, String message, String targetTitle, String targetLocation, String userName) throws RadixSvnException {
                return "svn".toCharArray();
            }
        })});
        long revision = instance.getLatestRevision();
        SvnEditor editor = instance.getEditor("SVN Framework test");
        final int editorRevision = -1;
        SvnCommitSummary summary;
        try {
            editor.openDir("/destination", editorRevision);
            editor.addDir("/destination/copy-of-source", "sorce", editorRevision);
            editor.closeDir();
            editor.closeDir();
            editor.openDir("/destination", editorRevision);
            editor.openDir("/destination/copy-of-source", editorRevision);
            editor.appendFile("/destination/copy-of-source/test", editorRevision, "test");
            editor.closeDir();
            editor.closeDir();
            summary = editor.closeEdit();
            System.out.println(summary);
        } catch (Throwable ex) {
            ex.printStackTrace();
        } finally {
            try {
                editor = instance.getEditor("SVN Framework test");
                editor.openDir("/destination", editorRevision);
                editor.deleteEntry("/destination/copy-of-source", editorRevision);
                summary = editor.closeEdit();
                System.out.println(summary);
            } catch (Throwable ex) {
            }
        }
    }

    @Test
    @Ignore
    public void testHttpProtocolWithBasicAuth() throws Exception {
        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("http://localhost/svn/first"), new SvnCredentials[]{SvnCredentials.Factory.newSVNPasswordInstance("SvnUser", new ISvnPasswordProvider() {

            @Override
            public char[] getPassword(boolean firstAttempt, SvnAuthType authType, String message, String targetTitle, String targetLocation, String userName) throws RadixSvnException {
                return "svn".toCharArray();
            }
        })});
        long revision = instance.getLatestRevision();
        System.out.println(revision);

        SvnEditor editor = instance.getEditor("SVN Framework test");
        final int editorRevision = -1;
        editor.openRoot(editorRevision);
        SvnCommitSummary summary;
        try {
            editor.addDir("testDir1", null, editorRevision);
            editor.closeDir();
            editor.addDir("testDir2", null, editorRevision);
            editor.addDir("testDir3", null, editorRevision);
            summary = editor.closeEdit();
            System.out.println(summary);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        revision = instance.getLatestRevision();
        instance.getDir("testDir2", revision, true, true, true, new SvnEntryHandler() {

            @Override
            public void accept(SvnEntry entry) throws RadixSvnException {
                System.out.println(entry.getName());
            }
        });
        editor = instance.getEditor("SVN Framework test");
        editor.openRoot(editorRevision);
        try {
            editor.deleteEntry("testDir1", editorRevision);
        } catch (RadixSvnException ex) {
            ex.printStackTrace();
        }
        try {
            editor.deleteEntry("testDir2", editorRevision);
        } catch (RadixSvnException ex) {
            ex.printStackTrace();
        }
        summary = editor.closeEdit();
        System.out.println(summary);
        revision = instance.getLatestRevision();
        System.out.println(revision);
    }

    @Test
    @Ignore
    public void testHttpsProtocolWithScripts() throws Exception {
        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("https://svn.radixware.org/repos/radix/"), new SvnCredentials[]{SvnCredentials.Factory.newCertificateInstance("akrylov", new File("/home/akrylov/cert2015/akrylov.p12"), "IYiEw=ZXHl)3".toCharArray())});
        long revision = instance.getLatestRevision();
        SvnEntry.Kind kind = instance.checkPath("/scripts/org.radixware", -1);

    }

    @Test
    @Ignore
    public void testHttpsProtocolWithSslAuth() throws Exception {

        // System.setProperty("javax.net.debug", "ssl,handshake,record");
//            SVNRepository repo = SVN.createRepository(SVNURL.parseURIDecoded("https://svn.radixware.org/repos/radix"), "", "akrylov", ESvnAuthType.SSL, "/home/akrylov/cert2015/akrylov.p12");
//            long r = repo.getLatestRevision();
        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("https://svn.radixware.org/repos/radix/dev/trunk"), new SvnCredentials[]{SvnCredentials.Factory.newCertificateInstance("akrylov", new File("/home/akrylov/cert2015/akrylov.p12"), "IYiEw=ZXHl)3".toCharArray())});
        long revision = instance.getLatestRevision();
        instance.getDir("", -1, true, true, true, null);

        SvnProperties props = instance.getRevisionProperties(528, null);
//        System.out.println(props);

        instance.log("", 328, 528, false, new ISvnLogHandler() {

            @Override
            public void accept(SvnLogEntry entry) throws RadixSvnException {
                System.out.println(entry);
            }
        });

        instance.getDir("", revision, true, true, true, null);
        System.out.println("----------------- read branch.xml --------------------");
        SvnRepositoryTest.TestOutputStream out;
        instance.getFile("branch.xml", revision, null, out = new SvnRepositoryTest.TestOutputStream(true));
        System.out.println(out.countOfBytes);
        System.out.println("----------------- read org.radixware/layer.xml --------------------");
        instance.getFile("org.radixware/layer.xml", revision, null, out = new SvnRepositoryTest.TestOutputStream());
        System.out.println(out.countOfBytes);
        System.out.println("----------------- read org.radixware/kernel/server/lib/cxf-2.7.5.jar --------------------");
        instance.getFile("org.radixware/kernel/server/lib/cxf-2.7.5.jar", revision, null, out = new SvnRepositoryTest.TestOutputStream());
        System.out.println(out.countOfBytes);
        System.out.println("----------------- DONE --------------------");

        System.out.println("----------------- EDITOR TEST --------------------");
        SvnEditor editor = instance.getEditor("SVN Framework test");
        final int editorRevision = -1;
        //  editor.openRoot(editorRevision);
        editor.openDir("org.radixware", editorRevision);
        SvnCommitSummary summary;
        try {
            editor.addDir("org.radixware/111111", null, editorRevision);
            summary = editor.closeEdit();
            System.out.println(summary);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        editor = instance.getEditor("SVN Framework test");
        //  editor.openRoot(editorRevision);
        editor.openDir("org.radixware", editorRevision);
        editor.deleteEntry("org.radixware/111111", editorRevision);
        summary = editor.closeEdit();
        System.out.println(summary);

        editor = instance.getEditor("SVN Framework test");
        //  editor.openRoot(editorRevision);
        editor.openDir("org.radixware", editorRevision);
        editor.appendFile("org.radixware/111", editorRevision, "hello-from-radix");
        editor.closeDir();
        summary = editor.closeEdit();
        System.out.println(summary);

        instance.getFile("org.radixware/111", summary.revision, null, out = new SvnRepositoryTest.TestOutputStream(true));
        try {
            if (!"hello-from-radix".equals(out.getString())) {
                throw new IllegalStateException("Test failed");
            }
        } finally {

        }

        editor = instance.getEditor("SVN Framework test");
        //    editor.openRoot(editorRevision);
        editor.openDir("org.radixware", editorRevision);
        editor.updateFile("org.radixware/111", editorRevision, "hello from radix");
        editor.closeDir();
        summary = editor.closeEdit();
        System.out.println(summary);

        instance.getFile("org.radixware/111", summary.revision, null, out = new SvnRepositoryTest.TestOutputStream(true));
        try {
            if (!"hello from radix".equals(out.getString())) {
                throw new IllegalStateException("Test failed");
            }
        } finally {
            editor = instance.getEditor("SVN Framework test");
            //      editor.openRoot(editorRevision);
            editor.openDir("org.radixware", editorRevision);
            editor.deleteEntry("org.radixware/111", editorRevision);
            summary = editor.closeEdit();
            System.out.println(summary);
        }

        //test add file functionality
        editor = instance.getEditor("SVN Framework test");
        //       editor.openRoot(editorRevision);
        editor.openDir("org.radixware", editorRevision);
        editor.appendFile("org.radixware/test_file", editorRevision, "hello from radix");
        editor.closeDir();
        summary = editor.closeEdit();
        System.out.println(summary);

        instance.getFile("org.radixware/test_file", summary.revision, null, out = new SvnRepositoryTest.TestOutputStream(true));
        try {
            if (!"hello from radix".equals(out.getString())) {
                throw new IllegalStateException("Test failed");
            }
        } finally {
            editor = instance.getEditor("SVN Framework test");
            //       editor.openRoot(editorRevision);
            editor.openDir("org.radixware", editorRevision);
            editor.deleteEntry("org.radixware/test_file", editorRevision);
            summary = editor.closeEdit();
            System.out.println(summary);
        }
        instance.getDir("org.radixware/kernel/", summary.revision, false, false, false, new SvnEntryHandler() {

            @Override
            public void accept(SvnEntry entry) throws RadixSvnException {
                System.out.println(entry);
            }
        });
        SvnEntry.Kind kind = instance.checkPath("org.radixware/kernel", summary.revision);
        System.out.println(kind);
        kind = instance.checkPath("branch.xml", summary.revision);
        System.out.println(kind);
        kind = instance.checkPath("aaa", summary.revision);
        System.out.println(kind);
        SvnEntry info = instance.info("branch.xml", summary.revision);
        System.out.println(info);
        info = instance.info("org.radixware", summary.revision);
        System.out.println(info);

    }

    public static void main(String[] args) throws Exception {
//        SSLContext context = SSLContext.getDefault();
//        SSLSocketFactory sf = context.getSocketFactory();
//        String[] cipherSuites = sf.getSupportedCipherSuites();
        //     Security.addProvider(new BouncyCastleProvider());

        new HttpRepositoryTest().testHttpsProtocolWithSslAuth();
    }
}
