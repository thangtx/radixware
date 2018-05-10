/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.svn.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.radixware.kernel.common.svn.RadixSvnException;

/**
 *
 * @author akrylov
 */
public class SvnRepositoryTest {

    public SvnRepositoryTest() {
    }

    @Test
    public void testSvnLog() throws RadixSvnException, URISyntaxException {

        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("svn://10.7.2.138/rep1"), new SvnCredentials[]{SvnCredentials.Factory.newEmptyInstance()});
        final long revision = instance.getLatestRevision();
        instance.log("/dev/trunk/org.radixware/ads/m222/src/rolAKERLVOTMNGPZJHMTPU6HGUHSY.xml", revision, 1, true, new ISvnLogHandler() {

            @Override
            public void accept(SvnLogEntry entry) throws RadixSvnException {
                System.out.println(entry.getRevision());
            }
        });
    }

    
    

    @Test
    public void testGetChecksum() throws RadixSvnException, URISyntaxException {
        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("svn+ssh://svn2.compassplus.ru/radix/dev/trunk/branch.xml"), new SvnCredentials[]{SvnCredentials.Factory.newSshKeyFileInstance("svn", new File("/home/akrylov/.ssh/id_rsa"))});
        long rev = instance.getLatestRevision();
        Map<String, String> props = new HashMap<>();

        final List<Long> revisions = new LinkedList<>();
        instance.log("/dev/trunk/org.radixware/ads/Acs/src/adcO3RN2IYLGXOBDGAOABIFNQAABA.xml", rev, 1, true, new ISvnLogHandler() {

            @Override
            public void accept(SvnLogEntry entry) throws RadixSvnException {
                revisions.add(entry.getRevision());
            }
        });
        for (long revision : revisions) {
            System.out.println(revision);
            SvnEntry.Kind kind = instance.checkPath("/dev/trunk/org.radixware/ads/Acs/src/adcO3RN2IYLGXOBDGAOABIFNQAABA.xml", revision);
            SvnEntry e = instance.info("/dev/trunk/org.radixware/ads/Acs/src/adcO3RN2IYLGXOBDGAOABIFNQAABA.xml", revision);
            instance.getFile("/dev/trunk/org.radixware/ads/Acs/src/adcO3RN2IYLGXOBDGAOABIFNQAABA.xml", revision, props, null);
        }
    }

    private void addFiles(SvnEditor editor, File dir, String basePath) throws RadixSvnException, IOException {
        File[] files = dir.listFiles();
        for (File file : files) {
            String filePath = SvnPath.append(basePath, file.getName());
            if (file.isDirectory() && !"bin".equals(file.getName()) && !"build".equals(file.getName())) {
                editor.openDir(filePath, -1);
                addFiles(editor, file, filePath);
                editor.closeDir();
            } else {
                if ("directory.xml".equals(file.getName())) {
                    FileInputStream in = new FileInputStream(file);
                    try {
                        editor.appendFile(filePath, -1, in);
                    } finally {
                        in.close();
                    }
                }
            }
        }
    }

    private void mkDirs(SvnEditor editor, File dir, String basePath) throws RadixSvnException, IOException {
        File[] files = dir.listFiles();
        for (File file : files) {
            String filePath = SvnPath.append(basePath, file.getName());
            if (file.isDirectory()) {
                editor.addDir(filePath, null, -1);
                mkDirs(editor, file, filePath);
                editor.closeDir();
            }
        }
    }

    @Test

    public void testGetDirSimple() throws IOException, URISyntaxException, Exception {
        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("svn+ssh://svn2.compassplus.ru/radix/dev/trunk"), new SvnCredentials[]{SvnCredentials.Factory.newSshKeyFileInstance("svn", new File("/home/akrylov/.ssh/id_rsa"))});

        long revision = instance.getLatestRevision();
        instance.getDir("org.radixware", revision, true, true, true, new SvnEntryHandler() {

            @Override
            public void accept(SvnEntry entry) throws RadixSvnException {
                System.out.println(entry);
            }
        });

    }

    public void testConnectWithBadCredentials() throws URISyntaxException, RadixSvnException {
        try {
            SvnRepository instance = SvnRepository.Factory.newInstance(new URI("svn://10.7.2.138/rep5"), new SvnCredentials[]{SvnCredentials.Factory.newSshKeyFileInstance("svn", new File(""))});
            final long revision = instance.getLatestRevision();

        } catch (RadixSvnException ex) {
            ex.printStackTrace();
        }
    }

    @Test

    public void testCopyFile() throws URISyntaxException, RadixSvnException {
        final int revisionForEditor = -1;
        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("svn://10.7.2.138/rep1"), new SvnCredentials[]{SvnCredentials.Factory.newEmptyInstance()});

        try {
            long revision = instance.getLatestRevision();
            SvnEditor editor = instance.getEditor("SVN Framework test");
//
//            editor.addDir("aaa", null, revisionForEditor);
//            editor.appendFile("aaa/bbb", revisionForEditor, "123");
//            editor.closeEdit();

            editor.addDir("321", null, revisionForEditor);
            editor.addFile("321/321", "aaa/bbb", revision);
            editor.closeEdit();

        } catch (RadixSvnException ex) {
            ex.printStackTrace();
        } finally {
            SvnEditor editor = instance.getEditor("SVN Framework test");
//
//            editor.addDir("aaa", null, revisionForEditor);
//            editor.appendFile("aaa/bbb", revisionForEditor, "123");
//            editor.closeEdit();

            editor.deleteEntry("321", -1);
            editor.closeEdit();
        }
    }

    @Test
    public void testCopyAndModify() throws URISyntaxException, RadixSvnException {
        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("svn://10.7.2.138/rep5"), new SvnCredentials[]{SvnCredentials.Factory.newEmptyInstance()});
        try {
            final long revision = instance.getLatestRevision();
            SvnEditor editor = instance.getEditor("SVN Framework test");
            final int revisionForEditor = -1;
            editor.openDir("clients", revisionForEditor);
            editor.openDir("clients/com.tranzaxis.wfspa", revisionForEditor);
            editor.openDir("clients/com.tranzaxis.wfspa/distributives", revisionForEditor);
            editor.addDir("clients/com.tranzaxis.wfspa/distributives/42-1.1.32.10.10", null, revisionForEditor);
            editor.addDir("clients/com.tranzaxis.wfspa/distributives/42-1.1.32.10.10/release", null, revisionForEditor);
            editor.addDir("clients/com.tranzaxis.wfspa/distributives/42-1.1.32.10.10/release/com.tranzaxis", null, revisionForEditor);
            editor.addDir("clients/com.tranzaxis.wfspa/distributives/42-1.1.32.10.10/release/com.tranzaxis/ads", null, revisionForEditor);
            editor.addDir("clients/com.tranzaxis.wfspa/distributives/42-1.1.32.10.10/release/com.tranzaxis/ads/Accounting", "releases/1.1.32.10.10/com.tranzaxis/ads/Accounting", revision);
            editor.updateFile("clients/com.tranzaxis.wfspa/distributives/42-1.1.32.10.10/release/com.tranzaxis/ads/Accounting/directory.xml", revisionForEditor, "test content");
            editor.closeEdit();
        } catch (Throwable ex) {
            throw ex;
        } finally {
            SvnEditor editor = instance.getEditor("SVN Framework test");
            editor.deleteEntry("clients/com.tranzaxis.wfspa/distributives/42-1.1.32.10.10", -1);
        }
    }

    @Test
    public void testCopyAndModifyInRepoWithPath() throws URISyntaxException, RadixSvnException {
        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("svn://10.7.2.138/rep5/clients/com.tranzaxis.wfspa/distributives"), new SvnCredentials[]{SvnCredentials.Factory.newEmptyInstance()});
        try {
            final long revision = instance.getLatestRevision();
            SvnEditor editor = instance.getEditor("SVN Framework test");
            final int revisionForEditor = -1;
            editor.addDir("42-1.1.32.10.10", null, revisionForEditor);
            editor.addDir("42-1.1.32.10.10/release", null, revisionForEditor);
            editor.addDir("42-1.1.32.10.10/release/com.tranzaxis", null, revisionForEditor);
            editor.addDir("42-1.1.32.10.10/release/com.tranzaxis/ads", null, revisionForEditor);
            editor.addDir("42-1.1.32.10.10/release/com.tranzaxis/ads/Accounting", "releases/1.1.32.10.10/com.tranzaxis/ads/Accounting", revision);
            editor.updateFile("42-1.1.32.10.10/release/com.tranzaxis/ads/Accounting/directory.xml", revisionForEditor, "test content");
            editor.closeEdit();
        } catch (Throwable ex) {
            throw ex;
        } finally {
            SvnEditor editor = instance.getEditor("SVN Framework test");
            editor.deleteEntry("42-1.1.32.10.10", -1);
        }
    }

    @Test
    @Ignore
    public void testGetDirRADUX11089() throws IOException, URISyntaxException, Exception {
        final SvnRepository instance = SvnRepository.Factory.newInstance(new URI("svn+ssh://svn2.compassplus.ru/twrbs/"), new SvnCredentials[]{SvnCredentials.Factory.newSshKeyFileInstance("svn", new File("/home/akrylov/.ssh/id_rsa"))});

        final long revision = instance.getLatestRevision();
        SvnEntry entry = instance.info("trunk/scripts/com.tranzaxis/1.1.24.10.13-1.1.24.10.12", revision);
        System.out.println(entry.getName());
        for (int i = 0; i < 100; i++) {
            System.out.println(i);
            instance.getDir("trunk/scripts/com.tranzaxis/1.1.24.10.13-1.1.24.10.12", revision, true, true, true, new SvnEntryHandler() {

                @Override
                public void accept(SvnEntry entry) throws RadixSvnException {
                    System.out.println(entry.getName());
                    String subPath = entry.getPath();
                    instance.getDir(subPath, revision, true, true, true, null);
                }
            });
        }

    }

    @Test
    @Ignore
    public void testGetProps() throws IOException, URISyntaxException, Exception {
        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("svn+ssh://svn2.compassplus.ru/radix/dev/trunk"), new SvnCredentials[]{SvnCredentials.Factory.newSshKeyFileInstance("svn", new File("/home/akrylov/.ssh/id_rsa"))});

        long revision = instance.getLatestRevision();
        final Map<String, String> props = new HashMap<>();
        instance.getFile("branch.xml", revision, props, new TestOutputStream());
        Assert.assertFalse(props.isEmpty());
        for (Map.Entry<String, String> p : props.entrySet()) {
            System.out.println(p.getKey() + " = " + p.getValue());
        }

    }

    @Test
    @Ignore
    public void testAppendTrunk2SubDurWithReplaceOfDirectoryXmlOfRadix() throws IOException, URISyntaxException, Exception {
        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("svn+ssh://svn2.compassplus.ru/radix/dev/trunk"), new SvnCredentials[]{SvnCredentials.Factory.newSshKeyFileInstance("svn", new File("/home/akrylov/.ssh/id_rsa"))});

        long revision = instance.getLatestRevision();
        SvnEditor editor = instance.getEditor("SVN Framework test");
        final int revisionForEditor = -1;
        editor.openRoot(revisionForEditor);
        editor.addDir("test_1", "org.radixware/ads", revision);
        //  mkDirs(editor, new File("/home/akrylov/ssd/radix/dev/trunk/org.radixware/ads"), "test_1");
        editor.openDir("test_1/Workflow", revisionForEditor);
        editor.deleteEntry("test_1/Workflow/module.xml", revisionForEditor);
        editor.appendFile("test_1/Workflow/module.xml", revisionForEditor, new FileInputStream(new File("/home/akrylov/ssd/radix/dev/trunk/org.radixware/ads/Workflow/module.xml")));
        editor.closeDir();//workflow
        editor.closeDir();//test_1
        SvnCommitSummary summary = editor.closeEdit();
        System.out.println(summary);

        editor = instance.getEditor("SVN Framework test");
        editor.openRoot(revisionForEditor);
        editor.deleteEntry("test_1", revisionForEditor);
        summary = editor.closeEdit();
        System.out.println(summary);
    }

    @Test
    @Ignore
    public void testAppendTrunk2SubDur() throws IOException, URISyntaxException, Exception {
        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("svn+ssh://svn2.compassplus.ru/radix/dev/trunk"), new SvnCredentials[]{SvnCredentials.Factory.newSshKeyFileInstance("svn", new File("/home/akrylov/.ssh/id_rsa"))});

        long revision = instance.getLatestRevision();
        SvnEditor editor = instance.getEditor("SVN Framework test");
        final int revisionForEditor = -1;
        editor.openRoot(revisionForEditor);
        editor.addDir("test_1", "org.radixware/ads", revision);
        //  mkDirs(editor, new File("/home/akrylov/ssd/radix/dev/trunk/org.radixware/ads"), "test_1");
        editor.closeDir();

        editor.openDir("test_1", revisionForEditor);
        addFiles(editor, new File("/home/akrylov/ssd/radix/dev/trunk/org.radixware/ads"), "test_1");
        editor.closeDir();
//        editor.closeDir();
        SvnCommitSummary summary = editor.closeEdit();
        System.out.println(summary);

        editor = instance.getEditor("SVN Framework test");
        editor.openRoot(revisionForEditor);
        editor.deleteEntry("test_1", revisionForEditor);
        summary = editor.closeEdit();
        System.out.println(summary);
    }

    @Test
    @Ignore
    public void testAppendFile2SubDur() throws IOException, URISyntaxException, Exception {
        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("svn+ssh://svn2.compassplus.ru/radix/dev/trunk"), new SvnCredentials[]{SvnCredentials.Factory.newSshKeyFileInstance("svn", new File("/home/akrylov/.ssh/id_rsa"))});
        SvnEditor editor = instance.getEditor("SVN Framework test");
        final int revisionForEditor = -1;
        editor.openRoot(revisionForEditor);
        editor.openDir("org.radixware", revisionForEditor);
        editor.openDir("org.radixware/kernel", revisionForEditor);
        editor.appendFile("org.radixware/kernel/111", revisionForEditor, "hello-from-radix");
        SvnCommitSummary summary = editor.closeEdit();
        System.out.println(summary);

        editor = instance.getEditor("SVN Framework test");
        editor.openRoot(revisionForEditor);
        editor.openDir("org.radixware", revisionForEditor);
        editor.openDir("org.radixware/kernel", revisionForEditor);
        editor.deleteEntry("org.radixware/kernel/111", revisionForEditor);
        summary = editor.closeEdit();
        System.out.println(summary);
    }

    @Test
    @Ignore
    public void testLongTimeEditing() throws IOException, URISyntaxException, Exception {
        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("svn+ssh://svn2.compassplus.ru/radix/dev/trunk"), new SvnCredentials[]{SvnCredentials.Factory.newSshKeyFileInstance("svn", new File("/home/akrylov/.ssh/id_rsa"))});
        int timeout = 0;
        for (int i = 0; i < 10; i++) {
            SvnEditor editor = instance.getEditor("SVN Framework test");
            final int revisionForEditor = -1;
            editor.openRoot(revisionForEditor);
            editor.openDir("org.radixware", revisionForEditor);
            if (timeout > 0) {
                Thread.sleep(timeout * 1000);
            }
            editor.appendFile("org.radixware/111", revisionForEditor, "hello-from-radix");
            SvnCommitSummary summary = editor.closeEdit();
            System.out.println(summary);

            editor = instance.getEditor("SVN Framework test");
            editor.openRoot(revisionForEditor);
            editor.openDir("org.radixware", revisionForEditor);
            editor.deleteEntry("org.radixware/111", revisionForEditor);
            summary = editor.closeEdit();
            System.out.println(summary);
            timeout += 10;
        }

    }

    /**
     * Test of getCredentials method, of class SvnRepository.
     */
    @Test
    @Ignore
    public void testGetDir() throws IOException, URISyntaxException, Exception {
        System.out.println("connect");

        SvnRepository instance = SvnRepository.Factory.newInstance(new URI("svn+ssh://svn2.compassplus.ru/radix/"), new SvnCredentials[]{SvnCredentials.Factory.newSshKeyFileInstance("svn", new File("/home/akrylov/.ssh/id_rsa"))});
        long revision = instance.getLatestRevision();
        instance.getDir("", revision, true, true, true, new SvnEntryHandler() {

            @Override
            public void accept(SvnEntry entry) throws RadixSvnException {
                System.out.println(entry.getUrl());
            }
        });
        SvnEntry.Kind kind = instance.checkPath("/scripts/", -1);
        kind = instance.checkPath("/scripts/org.radixware", -1);

        instance = SvnRepository.Factory.newInstance(new URI("svn+ssh://svn2.compassplus.ru/radix/dev/trunk"), new SvnCredentials[]{SvnCredentials.Factory.newSshKeyFileInstance("svn", new File("/home/akrylov/.ssh/id_rsa"))});
        revision = instance.getLatestRevision();
        //    instance.getRevisionProperties(revision);
        instance.getDir("", revision, true, true, true, new SvnEntryHandler() {

            @Override
            public void accept(SvnEntry entry) throws RadixSvnException {
                System.out.println(entry.getUrl());
            }
        });
//        instance.getDir("../../releases/src", revision, true, true, true, new SvnEntryHandler() {
//
//            @Override
//            public void accept(SvnEntry entry) throws RadixSvnException {
//                System.out.println(entry.getUrl());
//            }
//        });
        //"/dev/1.2.30.10/org.radixware/ads/Arte/locale/en/mlbpdcArte______________________.xml"

        System.out.println("----------------- read branch.xml --------------------");
        TestOutputStream out;
        instance.getFile("branch.xml", revision, null, out = new TestOutputStream());
        System.out.println(out.countOfBytes);
        System.out.println("----------------- read org.radixware/layer.xml --------------------");
        instance.getFile("org.radixware/layer.xml", revision, null, out = new TestOutputStream());
        System.out.println(out.countOfBytes);
        System.out.println("----------------- read org.radixware/kernel/server/lib/cxf-2.7.5.jar --------------------");
        instance.getFile("org.radixware/kernel/server/lib/cxf-2.7.5.jar", revision, null, out = new TestOutputStream());
        System.out.println(out.countOfBytes);
        System.out.println("----------------- DONE --------------------");

        System.out.println("----------------- EDITOR TEST --------------------");
        SvnEditor editor = instance.getEditor("SVN Framework test");
        final int revisionForEditor = -1;
        editor.openRoot(revisionForEditor);
        editor.openDir("org.radixware", revisionForEditor);
        editor.addDir("org.radixware/111111", null, revisionForEditor);
        SvnCommitSummary summary = editor.closeEdit();
        System.out.println(summary);

        editor = instance.getEditor("SVN Framework test");
        editor.openRoot(revisionForEditor);
        editor.openDir("org.radixware", revisionForEditor);
        editor.deleteEntry("org.radixware/111111", revisionForEditor);
        summary = editor.closeEdit();
        System.out.println(summary);

        editor = instance.getEditor("SVN Framework test");
        editor.openRoot(revisionForEditor);
        editor.openDir("org.radixware", revisionForEditor);
        editor.appendFile("org.radixware/111", revisionForEditor, "hello-from-radix");
        editor.closeDir();
        summary = editor.closeEdit();
        System.out.println(summary);

        instance.getFile("org.radixware/111", summary.revision, null, out = new TestOutputStream(true));
        try {
            if (!"hello-from-radix".equals(out.getString())) {
                throw new IllegalStateException("Test failed");
            }
        } finally {

        }

        editor = instance.getEditor("SVN Framework test");
        editor.openRoot(revisionForEditor);
        editor.openDir("org.radixware", revisionForEditor);
        editor.updateFile("org.radixware/111", revisionForEditor, "hello from radix");
        editor.closeDir();
        summary = editor.closeEdit();
        System.out.println(summary);

        instance.getFile("org.radixware/111", summary.revision, null, out = new TestOutputStream(true));
        try {
            if (!"hello from radix".equals(out.getString())) {
                throw new IllegalStateException("Test failed");
            }
        } finally {
            editor = instance.getEditor("SVN Framework test");
            editor.openRoot(revisionForEditor);
            editor.openDir("org.radixware", revisionForEditor);
            editor.deleteEntry("org.radixware/111", revisionForEditor);
            summary = editor.closeEdit();
            System.out.println(summary);
        }

        //test add file functionality
        editor = instance.getEditor("SVN Framework test");
        editor.openRoot(revisionForEditor);
        editor.openDir("org.radixware", revisionForEditor);
        editor.appendFile("org.radixware/test_file", revisionForEditor, "hello from radix");
        editor.closeDir();
        summary = editor.closeEdit();
        System.out.println(summary);

        instance.getFile("org.radixware/test_file", summary.revision, null, out = new TestOutputStream(true));
        try {
            if (!"hello from radix".equals(out.getString())) {
                throw new IllegalStateException("Test failed");
            }
        } finally {
            editor = instance.getEditor("SVN Framework test");
            editor.openRoot(revisionForEditor);
            editor.openDir("org.radixware", revisionForEditor);
            editor.deleteEntry("org.radixware/test_file", revisionForEditor);
            summary = editor.closeEdit();
            System.out.println(summary);
        }
        kind = instance.checkPath("org.radixware/kernel", summary.revision);
        System.out.println(kind);
        kind = instance.checkPath("branch.xml", summary.revision);
        System.out.println(kind);
        kind = instance.checkPath("aaa", summary.revision);
        System.out.println(kind);
        SvnEntry info = instance.info("branch.xml", summary.revision);
        System.out.println(info);

        instance.log("branch.xml", summary.revision - 2000, summary.revision, false, new ISvnLogHandler() {

            @Override
            public void accept(SvnLogEntry entry) throws RadixSvnException {
                System.out.println(entry);
            }
        });

    }

    @Test
    public void testRelativePath() throws IOException, URISyntaxException, Exception {
        try {

            SvnRepository instance = SvnRepository.Factory.newInstance(new URI("svn://10.7.2.138/rep1/dev/trunk"), new SvnCredentials[]{SvnCredentials.Factory.newEmptyInstance()});
            //   SvnRepository instance = SvnRepository.Factory.newInstance(new URI("svn://10.7.2.138/rep1"), new SvnCredentials[]{SvnCredentials.Factory.newEmptyInstance()});
            SvnEditor editor;
            instance.getPath();

            editor = instance.getEditor("SVN Framework test");
            editor.openDir("/bbb1", -1);
            editor.openDir("/bbb1/bb.b2", -1);
            editor.updateFile("/bbb1/bb.b2/rolAKERLVOTMNGPZJHMTPU6HGUHSY.xml", -1, "data");
            editor.closeDir();
            editor.closeDir();
            editor.closeEdit(); //good commit

            editor = instance.getEditor("SVN Framework test");
            editor.openDir("/dev", -1);
            editor.openDir("/dev/1.56", -1);
            editor.openDir("/dev/1.56/org.radixware", -1);
            editor.openDir("/dev/1.56/org.radixware/ads", -1);
            editor.openDir("/dev/1.56/org.radixware/ads/m222", -1);
            editor.openDir("/dev/1.56/org.radixware/ads/m222/src", -1);

            editor.updateFile("/dev/1.56/org.radixware/ads/m222/src/rolAKERLVOTMNGPZJHMTPU6HGUHSY.xml", -1, "data");
            editor.closeDir();
            editor.closeDir();
            editor.closeDir();
            editor.closeDir();
            editor.closeDir();
            editor.closeDir();
            editor.closeEdit(); //bad commit

        } catch (RadixSvnException ex) {
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException, URISyntaxException, Exception {
        new SvnRepositoryTest().testRelativePath();
    }

    static class TestOutputStream extends OutputStream {

        PrintWriter writer;
        StringWriter string;
        int countOfBytes = 0;

        public TestOutputStream() {
        }

        public TestOutputStream(boolean write) {
            if (write) {
                writer = new PrintWriter(string = new StringWriter());
            }
        }

        @Override
        public void write(int b) throws IOException {
            if (writer != null) {
                writer.write(b);
            }
            countOfBytes++;
        }

        public String getString() {
            return string.toString();
        }

    }
}
