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

import java.util.LinkedList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.radixware.kernel.common.dialogs.AuthenticationCancelledException;
import org.radixware.kernel.common.enums.ESvnAuthType;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import static org.radixware.kernel.common.svn.utils.ReleaseVerifierTest.CERT_FILE;

public class LayersCheckerTest {

    public LayersCheckerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        if (System.getProperty("user.name").equals("akrylov")) {
            CERT_FILE = "/home/akrylov/.ssh/id_rsa";
        } else if (System.getProperty("user.name").equals("abelyaev")) {
            CERT_FILE = "C:/Distrib/s/abelyaev.pem";
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getRepository method, of class LayersChecker.
     * @throws org.radixware.kernel.common.svn.RadixSvnException
     */
    @Test
    public void test() throws RadixSvnException, AuthenticationCancelledException {
        LayersCheckerImpl impl = new LayersCheckerImpl();
        assertTrue(impl.verify());

    }

    /**
     * Test of getRevision method, of class LayersChecker.
     */
    public class LayersCheckerImpl extends LayersChecker {

        private SVNRepositoryAdapter repository;

        public LayersCheckerImpl() throws RadixSvnException, AuthenticationCancelledException {
//svn+cplus://svn.compassplus.ru/twrbs/trunk/releases/1.1.17.10
            repository = SVNRepositoryAdapter.Factory.newInstance("svn+cplus://svn.compassplus.ru/twrbs/trunk/", "releases/1.1.17.10", "svn", SVN.getForAuthType(ESvnAuthType.SSH_KEY_FILE), CERT_FILE);
        }

        @Override
        public SVNRepositoryAdapter getRepository() {
            return repository;
        }

        @Override
        public long getRevision() {
            return -1;
        }

        @Override
        public List<String> getLayersSvnPathList() {
            List<String> result = new LinkedList<>();
            result.add("org.radixware");
            result.add("com.tranzaxis");
            return result;
        }

        @Override
        public String[] getTopLayersURI() {
            return new String[]{"com.tranzaxis"};
        }

        @Override
        public boolean wasCanceled() {
            return false;
        }

        @Override
        public void error(Exception e) {
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
}
