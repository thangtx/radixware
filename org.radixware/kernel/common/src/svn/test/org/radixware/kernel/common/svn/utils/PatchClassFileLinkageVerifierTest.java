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

import java.sql.DriverManager;
import java.sql.SQLException;
import org.radixware.kernel.common.dialogs.AuthenticationCancelledException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.enums.ESvnAuthType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;

public class PatchClassFileLinkageVerifierTest {

    public PatchClassFileLinkageVerifierTest() {
    }
    static String CERT_FILE;

    @BeforeClass
    public static void setUpClass() throws Exception {
        if (System.getProperty("user.name").equals("akrylov")) {
            CERT_FILE = "/home/akrylov/Desktop/cert.pem";
        } else if (System.getProperty("user.name").equals("abelyaev")) {
            CERT_FILE = "C:/Distrib/s/abelyaev.pem";
        }
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
     * Test of createKernelClassLoader method, of class
     * PatchClassFileLinkageVerifier.
     */
    /**
     * Test of verify method, of class PatchClassFileLinkageVerifier.
     */
    @Test

    public void testVerify() throws RadixSvnException, AuthenticationCancelledException {
        System.out.println("verify");
        SVNRepositoryAdapter repository = SVNRepositoryAdapter.Factory.newInstance("svn+cplus://svn.compassplus.ru/radix", "", "svn", SVN.getForAuthType(ESvnAuthType.SSH_KEY_FILE), CERT_FILE);
        final BranchHolderParams branchParams = new BranchHolderParams(repository, "releases/1.1.92", "/home/akrylov/Desktop/org.radixware-240-1.1.92.19.zip", false, Utils.NO_BASE_DEVELOPMENT_LAYER_SPECIFIED);
        PatchClassFileLinkageVerifier instance = new PatchClassFileLinkageVerifier (branchParams, true){

            @Override
            public void error(Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void error(String message) {
                System.err.println(message);
            }

            @Override
            public void message(String message) {
                System.out.println(message);
            }
        };
        boolean result = instance.verify();
        assertEquals(true, result);
        // TODO review the generated test code and remove the default call to fail.        
    }

    @Test
    public void testVerifyUf() throws RadixSvnException, AuthenticationCancelledException, SQLException {
        SVNRepositoryAdapter repository = SVNRepositoryAdapter.Factory.newInstance("svn+cplus://svn.compassplus.ru/twrbs/", "", "svn", SVN.getForAuthType(ESvnAuthType.SSH_KEY_FILE), CERT_FILE);
        final BranchHolderParams branchParams = new BranchHolderParams(repository, "trunk/dev/trunk", "/home/akrylov/Desktop/org.radixware-240-1.1.92.19.zip", true, Utils.NO_BASE_DEVELOPMENT_LAYER_SPECIFIED);
        PatchClassFileLinkageVerifier instance = new PatchClassFileLinkageVerifier(branchParams, true) {

            @Override
            public void error(Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void error(String message) {
                System.err.println(message);
            }

            @Override
            public void message(String message) {
                System.out.println(message);
            }
        };
        java.sql.Connection con = DriverManager.getConnection("jdbc:oracle:thin:@dev1:1521/x", "RBS", "RBS");
        try {
            boolean result = instance.verifyUserFunctions(con);
            assertEquals(true, result);
        } finally {
            con.close();
        }

    }
}
