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
package org.radixware.kernel.common.svn.utils.xml;

import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.radixware.kernel.common.dialogs.AuthenticationCancelledException;
import org.radixware.kernel.common.enums.ESvnAuthType;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;

public class XsdExporterTest {

    public XsdExporterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSomeMethod() throws RadixSvnException, AuthenticationCancelledException, IOException {
        SVNRepositoryAdapter svn = SVNRepositoryAdapter.Factory.newInstance("svn+cplus://svn2.compassplus.ru/twrbs/trunk/", "releases/1.1.28.10.26", "svn", SVN.getForAuthType(ESvnAuthType.SSH_KEY_FILE), "/home/akrylov/.ssh/id_rsa");
        new XsdExporter(svn, -1, new File("/home/akrylov/xsdexport"), null).process();
    }

    public static void main(String[] args) throws RadixSvnException, AuthenticationCancelledException, IOException {
        SVNRepositoryAdapter svn = SVNRepositoryAdapter.Factory.newInstance("svn+cplus://svn2.compassplus.ru/twrbs/trunk/", "releases/1.1.28.10.26", "svn", SVN.getForAuthType(ESvnAuthType.SSH_KEY_FILE), "/home/akrylov/.ssh/id_rsa");
        new XsdExporter(svn, -1, new File("/home/akrylov/xsdexport"), null).process();
    }
}
