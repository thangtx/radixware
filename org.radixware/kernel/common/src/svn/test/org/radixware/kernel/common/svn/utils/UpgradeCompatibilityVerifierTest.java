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

import java.util.zip.ZipException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import org.radixware.kernel.common.utils.FileUtils;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.radixware.kernel.common.dialogs.AuthenticationCancelledException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.radixware.kernel.common.enums.ESvnAuthType;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;

public class UpgradeCompatibilityVerifierTest {
    
    private final String keyFile = "C:\\Users\\npopov\\Documents\\npopov\\migrate\\my_files\\sert_2017\\key.pem";
    //private final String upgradeFilePath = "C:\\rwmanager\\radixProject\\distrib\\com.tranzaxis\\com.tranzaxis-1720-2.1.13.10.4-install.zip";
    private final String radixUrl = "svn+cplus://svn2.compassplus.ru/radix";
    private final String txUrl = "svn+cplus://svn2.compassplus.ru/twrbs/";

    private File extractZipFile() {
        File tmp = null;
        try {
            tmp = File.createTempFile("rdx", "rdx");
            InputStream in = getClass().getResourceAsStream("upgrade2.zip");
            OutputStream out = new FileOutputStream(tmp);
            try {
                FileUtils.copyStream(in, out);
            } finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }
            return tmp;
        } catch (IOException ex) {
            fail(ex.getMessage());
            if (tmp != null) {
                FileUtils.deleteFile(tmp);
            }
        }
        return null;
    }

    public UpgradeCompatibilityVerifierTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of verify method, of class Usages2APIVerifier.
     */
    @Test
    @Ignore
    public void testVerify() throws RadixSvnException, AuthenticationCancelledException, ZipException, IOException {
        //SVNRepository rep = SVN.createRepository(SVNURL.parseURIDecoded("svn+cplus://svn.compassplus.ru/twrbs/tests/testradix"), "", "svn", ESvnAuthType.SSH_KEY_FILE, "/home/akrylov/Desktop/cert.pem");
        SVNRepositoryAdapter rep = SVNRepositoryAdapter.Factory.newInstance("svn+cplus://svn.compassplus.ru/twrbs/trunk/", "", "svn", SVN.getForAuthType(ESvnAuthType.SSH_KEY_FILE), "/home/akrylov/.ssh/id_rsa");

        UpgradeCompatibilityVerifier instance = new UpgradeCompatibilityVerifierImpl(rep, "releases/1.1.5.20.18");
        boolean result = instance.verify();
        assertEquals(true, result);
    }

    public class UpgradeCompatibilityVerifierImpl extends UpgradeCompatibilityVerifier {

        public UpgradeCompatibilityVerifierImpl(SVNRepositoryAdapter rep, String bp) throws ZipException, IOException {
            super(new BranchHolderParams(rep, bp, null, false));
        }

        @Override
        public void error(Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            //fail(e.getMessage());
        }

        @Override
        public void error(String message) {
            System.err.println(message);
            //fail(message);
        }

        @Override
        public void message(String message) {
            System.out.println(message);
        }
    }
    
    public class UserFuncVerifierImpl extends UserFuncVerifier {

        public UserFuncVerifierImpl(SVNRepositoryAdapter rep, Connection con, String bp, String extJarsPath) {
            super(new BranchHolderParams(rep, bp, null, false), con, ERuntimeEnvironmentType.SERVER, extJarsPath);
        }

        @Override
        public void error(Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            //fail(e.getMessage());
        }

        @Override
        public void error(String message) {
            System.err.println(message);
            //fail(message);
        }

        @Override
        public void message(String message) {
            System.out.println(message);
        }
    }

    @Test
    @Ignore
    public void testVerifyUf() throws RadixSvnException, AuthenticationCancelledException, SQLException, ZipException, IOException {
        SVNRepositoryAdapter rep = SVNRepositoryAdapter.Factory.newInstance(radixUrl, "", "svn", SVN.getForAuthType(ESvnAuthType.SSH_KEY_FILE), keyFile);
        java.sql.Connection con = DriverManager.getConnection("jdbc:oracle:thin:@10.77.201.31:1521/x", "RADIX", "RADIX");
        UserFuncVerifier instance = new UserFuncVerifierImpl(rep, con, "releases/2.1.13.10.3", null);
        
        try {
            boolean result = instance.verify();
            assertEquals(true, result);
        } finally {
            con.close();
        }
    }

    @Test
    public void testVerifyUf2() throws RadixSvnException, AuthenticationCancelledException, SQLException, IOException {
        SVNRepositoryAdapter repository = SVNRepositoryAdapter.Factory.newInstance(txUrl, "", "svn", SVN.getForAuthType(ESvnAuthType.SSH_KEY_FILE), keyFile);
        java.sql.Connection con = DriverManager.getConnection("jdbc:oracle:thin:@10.7.1.55:1521/TERMINALPAB", "RBS", "RBS");
        UserFuncVerifier instance = new UserFuncVerifierImpl(repository, con, "trunk/releases/1.1.40.10.1", null);        //"C:\\Users\\npopov\\Downloads\\testJars"
        try {
            boolean result = instance.verify();
            assertEquals(true, result);
        } finally {
            con.close();
        }
    }
}
