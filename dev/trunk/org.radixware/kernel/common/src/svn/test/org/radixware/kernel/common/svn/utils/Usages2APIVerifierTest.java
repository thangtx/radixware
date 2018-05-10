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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.radixware.kernel.common.enums.ESvnAuthType;
import static org.junit.Assert.*;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;

public class Usages2APIVerifierTest {

    public Usages2APIVerifierTest() {
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
    public void testVerify() throws RadixSvnException, AuthenticationCancelledException {
        SVNRepositoryAdapter rep = SVNRepositoryAdapter.Factory.newInstance("svn+cplus://svn.compassplus.ru/radix", "", "svn", SVN.getForAuthType(ESvnAuthType.SSH_KEY_FILE), "/home/akrylov/Desktop/cert.pem");
        Usages2APIVerifier instance = new Usages2APIVerifierImpl(rep, "releases/1.1.99");
        boolean result = instance.verify();
        assertEquals(true, result);
    }

    public class Usages2APIVerifierImpl extends Usages2APIVerifier {

        public Usages2APIVerifierImpl(SVNRepositoryAdapter rep, String bp) {
            super(new BranchHolderParams(rep, bp, "/home/akrylov/Desktop/org.radixware.games-ext-17-1.1.99.zip", false, Utils.NO_BASE_DEVELOPMENT_LAYER_SPECIFIED));
        }

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
    }

    @Test
    public void testVerifyUf() throws RadixSvnException, AuthenticationCancelledException, SQLException {
        SVNRepositoryAdapter rep = SVNRepositoryAdapter.Factory.newInstance("svn+cplus://svn.compassplus.ru/radix", "", "svn", SVN.getForAuthType(ESvnAuthType.SSH_KEY_FILE), "/home/akrylov/Desktop/cert.pem");
        Usages2APIVerifier instance = new Usages2APIVerifierImpl(rep, "releases/1.1.99");
        java.sql.Connection con = DriverManager.getConnection("jdbc:oracle:thin:@dev1:1521/x", "RADIX", "RADIX");
        try {
            boolean result = instance.verifyUserFunctions(con);
            assertEquals(true, result);
        } finally {
            con.close();
        }
    }
}
